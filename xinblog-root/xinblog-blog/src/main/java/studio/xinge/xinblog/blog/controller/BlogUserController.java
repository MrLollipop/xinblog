package studio.xinge.xinblog.blog.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import studio.xinge.xinblog.blog.entity.BlogEntity;
import studio.xinge.xinblog.blog.entity.TBlogReply;
import studio.xinge.xinblog.blog.service.BlogService;
import studio.xinge.xinblog.blog.service.IndexService;
import studio.xinge.xinblog.blog.service.TBlogReplyService;
import studio.xinge.xinblog.blog.service.TTagService;
import studio.xinge.xinblog.blog.util.MyHashOperations;
import studio.xinge.xinblog.blog.vo.*;
import studio.xinge.xinblog.common.utils.Constant;
import studio.xinge.xinblog.common.utils.R;
import studio.xinge.xinblog.common.utils.ReturnCode;
import studio.xinge.xinblog.common.valid.groups.Add;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 针对于博客用户，对外开放接口
 * 需要注意并发问题
 * 对DB写操作异步
 *
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/7/8
 * @Description
 */
@RestController
@RequestMapping("blog/user")
@RefreshScope
@Slf4j
public class BlogUserController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private MyHashOperations myHashOperations;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private IndexService indexService;

    @Autowired
    private TTagService tagService;

    @Autowired
    private TBlogReplyService replyService;

    @Autowired
    private ExecutorService threadPool;

    @Value("${blog.cache.ttl.hours}")
    private int blogCacheTTLHours;

    @Value("${cache.update.threshold}")
    private int updateThreshold;

    /**
     * 详情页浏览：
     * 高并发访问，分布式锁控制从DB读数据
     * 1.对不存在数据做空值保护
     * 2.每次浏览，缓存中浏览数+1
     * Cache直接更新
     * 异步更新
     *
     * @param id
     * @return R
     * @Author xinge
     * @Description
     * @Date 2022/7/4
     */
    @RequestMapping("/view/{id}")
    public R view(@PathVariable("id") String id) throws InterruptedException {
        String key = Constant.BLOG + id;
        String lockName = Constant.BLOG_LOCK + id;
        R cache = checkCacheExist(key, id);
        if (null != cache) {
            return cache;
        }
//      缓存不存在，查DB，更新缓存；
//      分布式锁，没锁线程自旋等待其他线程释放；
        RLock lock = redissonClient.getLock(lockName);
        BlogEntity blog = null;
        try {
            lock.lock(200, TimeUnit.MILLISECONDS);
//                高并发下，再次判断缓存是否存在
            R cache2 = checkCacheExist(key, id);
            if (null != cache2) {
                return cache2;
            }
//                状态正常筛选
            blog = blogService.getOne(new QueryWrapper<BlogEntity>().eq("id", id).eq("status", Constant.BlogStatus.NORMAL.getValue()));
            if (null != blog) {
                updateViewNum(key, id, blogService.changeEntityToVO(blog));
            } else {
//                    第一次查询，对不存在的值做处理
                myHashOperations.setHash(key, id, Constant.BLOG_NOT_EXIST, blogCacheTTLHours, TimeUnit.HOURS);
                return R.error(ReturnCode.BLOG_NOT_EXIST);
            }
        } catch (Exception e) {
            log.error("query DB error", e);
        } finally {
            lock.unlock();
        }

        return R.ok().put("blog", blogService.changeEntityToVO(blog));
    }

    /**
     * 检查缓存中是否已有数据
     * 1.没有数据返回空
     * 2.有数据，判断类型
     * 字符型：返回错误提示。
     * Blog：更新访问量，返回正常。
     *
     * @param key
     * @param id
     * @return R
     * @Author xinge
     * @Description
     * @Date 2022/7/8
     */
    private R checkCacheExist(String key, String id) {
        Object entity = myHashOperations.get(key, id);
        if (null != entity) {
//            不存在博客的处理
            if (entity.getClass().equals(String.class) && entity.toString().equals(Constant.BLOG_NOT_EXIST)) {
                return R.error(ReturnCode.BLOG_NOT_EXIST);
            }
//          取出是vo
            BlogEntityVO blogVO = (BlogEntityVO) entity;
            updateViewNum(key, id, blogVO);
            return R.ok().put("blog", blogVO);
        }
        return null;
    }


    /**
     * 更新访问量
     * 1.cache实时更新，同时转vo
     * 2.DB，积累到阈值，异步线程池中去更新
     *
     * @param key
     * @param hashKey
     * @param blogVO
     * @Author xinge
     * @Description
     * @Date 2022/7/5
     */
    private void updateViewNum(String key, String hashKey, BlogEntityVO blogVO) {
        Integer viewNum = blogVO.getViewNum() == null ? 0 : blogVO.getViewNum();
        viewNum++;
        blogVO.setViewNum(viewNum);
        myHashOperations.setHash(key, hashKey, blogVO, blogCacheTTLHours, TimeUnit.HOURS);
//        积累到阈值，提交异步任务更新
        Integer finalViewNum = viewNum;
        if (finalViewNum % updateThreshold == 0) {
            threadPool.submit(() -> {
                blogService.updateViewNumById(blogVO.getId(), blogVO.getViewNum());
            });
        }
    }

    /**
     * 获取首页数据
     * 一次DB查询所有（state正常），整理3类数据
     * 1.置顶，top字段筛选
     * 2.最新，update_time逆序排序
     * 3.热门，view_num逆序排序
     *
     * @return R
     * @Author xinge
     * @Description
     * @Date 2022/7/4
     */
    @RequestMapping("/indexData")
    public R indexData(PageVO pageVO) {
        List topList = (List<BlogEntity>) myHashOperations.get(Constant.BLOG_INDEX_CACHE + "topList", "topList");
        if (null == topList) {
//          缓存不存在时,手动触发
            indexService.getData();
            topList = (List<BlogEntity>) myHashOperations.get(Constant.BLOG_INDEX_CACHE + "topList", "topList");
        }
        List<BlogEntity> newestList = (List<BlogEntity>) myHashOperations.get(Constant.BLOG_INDEX_CACHE + "newestList", "newestList");
        List<BlogEntity> hotList = (List<BlogEntity>) myHashOperations.get(Constant.BLOG_INDEX_CACHE + "hotList", "hotList");

        HashMap<String, Object> indexData = new HashMap<>();
//        置顶博客数目已有阈值保护，故全部返回
        indexData.put("topList", topList);
        indexData.put("newestList", getSubList(newestList, pageVO));
        indexData.put("hotList", getSubList(hotList, pageVO));
        return R.ok().put("indexData", indexData);
    }

    /**
     * 截取每页显示的list
     * 例如 pageVO.pageSize=3
     * 1.每页展示3个
     * 2.超出下标，返回最末3个
     *
     * @param list
     * @param pageVO
     * @return BlogListVO
     * @Author xinge
     * @Description
     * @Date 2022/7/18
     */
    private BlogListVO getSubList(List list, PageVO pageVO) {
        int from = pageVO.getFrom();
        int to = from + pageVO.getPageSize();
        int pageSize = pageVO.getPageSize();
        boolean end = false;
        if (from > list.size() - 1 || to > list.size()) {
            from = (list.size() - pageSize) < 0 ? 0 : list.size() - pageSize;
            to = list.size();
            end = true;
        }
        return new BlogListVO(list.subList(from, to), end);
    }

    /**
     * 最新列表
     *
     * @param pageVO
     * @return R
     * @Author xinge
     * @Description
     * @Date 2022/7/18
     */
    @RequestMapping("/newestList")
    public R newestList(PageVO pageVO) {
        List<BlogEntity> newestList = (List<BlogEntity>) myHashOperations.get(Constant.BLOG_INDEX_CACHE + "newestList", "newestList");

        return R.ok().put("newestList", getSubList(newestList, pageVO));
    }

    /**
     * 热门列表
     *
     * @param pageVO
     * @return R
     * @Author xinge
     * @Description
     * @Date 2022/7/18
     */
    @RequestMapping("/hotList")
    public R hotList(PageVO pageVO) {
        List<BlogEntity> hotList = (List<BlogEntity>) myHashOperations.get(Constant.BLOG_INDEX_CACHE + "hotList", "hotList");

        return R.ok().put("hotList", getSubList(hotList, pageVO));
    }

    /**
     * 获取指定标签下的博客
     * 1. 从缓存中提取指定标签的所有BlogID
     * 2. 从博客缓存中取指定博客
     *
     * @param key 标签id
     * @return R
     * @Author xinge
     * @Description
     * @Date 2022/7/16
     */
    @RequestMapping("/blogs/tag")
    public R getBlogsByTag(@RequestParam long key, PageVO pageVO) {
//        从tag->blogs缓存中取出对应blogs集合
        HashSet<Long> blogs = tagService.getBlogsByTag(key);
        if (null == blogs || blogs.isEmpty()) {
            return R.error(ReturnCode.RESULT_IS_EMPTY);
        }
        LinkedList<BlogEntityVO> blogList = new LinkedList<>();
        blogs.stream().forEach(blogId -> {
            BlogEntityVO vo = getBlogEntityVO(blogId);
            blogList.add(vo);
        });

        return R.ok().put("blogs", getSubList(blogList, pageVO));
    }

    /**
     * 从缓存中取BlogEntityVO
     * 不存在，查库，并放入缓存
     *
     * @param blogId
     * @return BlogEntityVO
     * @Author xinge
     * @Description
     * @Date 2022/7/22
     */
    private BlogEntityVO getBlogEntityVO(Long blogId) {
        //            先从博客缓存中查
        BlogEntityVO vo = (BlogEntityVO) myHashOperations.get(Constant.BLOG + blogId, String.valueOf(blogId));
        if (null == vo) {
//                不存在，将DB结果放入缓存
            BlogEntity blog = blogService.getOne(new QueryWrapper<BlogEntity>().eq("id", blogId).eq("status", Constant.BlogStatus.NORMAL.getValue()));
            if (null != blog) {
                vo = blogService.changeEntityToVO(blog);
                myHashOperations.setHash(Constant.BLOG + blogId, String.valueOf(blogId), vo, blogCacheTTLHours, TimeUnit.HOURS);
            }
        }
        return vo;
    }

    /**
     * 获取缓存中所有Tag
     * 遍历转vo
     *
     * @return R
     * @Author xinge
     * @Description
     * @Date 2022/7/16
     */
    @RequestMapping("tags")
    public R getTags() {
        HashMap<Long, String> tagsCache = (HashMap) myHashOperations.get(Constant.TAGS, Constant.TAGS);
        if (null == tagsCache) {
            tagService.saveTagCache();
            tagsCache = (HashMap) myHashOperations.get(Constant.TAGS, Constant.TAGS);
        }
        LinkedList<TagVO> vos = new LinkedList<>();
        tagsCache.entrySet().stream().forEach(entry -> {
            vos.add(new TagVO(entry.getKey(), entry.getValue()));
        });
        return R.ok().put("tags", vos);
    }

    /**
     * 找到相似博客（同样标签）
     * 根据tags找到每个tag的博客列表 (tag->blogs)(去掉自己)
     * 缓存bolgid->blogSimpleVO(id,title)
     *
     * @param keyStr
     * @param selfId
     * @return R
     * @Author xinge
     * @Description
     * @Date 2022/7/22
     */
    @GetMapping("/similar")
    public R getSimilarBlogs(String keyStr, long selfId) {
        long[] keys = StrUtil.splitToLong(keyStr, ',');
        Set<Long> similarIds = new HashSet<>();
        List<BlogSimpleVO> similar = new LinkedList<>();
        if (keys.length > 0) {
            Arrays.stream(keys).forEach(key -> {
                //        从tag->blogs缓存中取出对应blogs集合
                HashSet<Long> blogs = tagService.getBlogsByTag(key);
                similarIds.addAll(blogs);
            });
        }
        // 去掉自己
        similarIds.remove(selfId);

        similarIds.stream().forEach(blogId -> {
            BlogEntityVO vo = getBlogEntityVO(blogId);
            similar.add(new BlogSimpleVO(vo));
        });

        return R.ok().put("blogs", similar);
    }

    /**
     * 发布评论
     * 1. 异步线程写入DB
     * 2. 更新Cache
     *
     * @param vo
     * @return R
     * @Author xinge
     * @Description
     * @Date 2022/7/23
     */
    @PostMapping("reply")
    public R reply(@Validated(Add.class) TBlogReplyVO vo) {
        vo.setReplyerId(replyService.getIdWithHashCode(vo));
        vo.setCreateTime(LocalDateTime.now());
        TBlogReply tBlogReply = BeanUtil.copyProperties(vo, TBlogReply.class);
        tBlogReply.setStatus(Constant.BlogStatus.NORMAL.getValue());

        threadPool.submit(() -> {
            replyService.saveAndUpdateCache(tBlogReply);
        });

        return R.ok();
    }

    /**
     * 获得回复列表
     * 一级回复
     * 二级回复
     *
     * @param blogId
     * @return R
     * @Author xinge
     * @Description
     * @Date 2022/7/24
     */
    @GetMapping("reply/list")
    public R queryReply(Long blogId) {
        String key = Constant.REPLY + StrUtil.toString(blogId);
        List<TBlogReplyVO> replys = (List) myHashOperations.get(key, StrUtil.toString(blogId));
        if (null == replys) {
            replys = replyService.buildCache(blogId);
        }
        LinkedList<TBlogReplyVO> byLevels = replyService.sortReplyByLevel(replys);
        return R.ok().put("reply", byLevels);
    }

}
