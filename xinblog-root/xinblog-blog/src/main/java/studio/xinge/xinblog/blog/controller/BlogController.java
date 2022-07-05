package studio.xinge.xinblog.blog.controller;

import java.util.*;
import java.util.concurrent.TimeUnit;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import studio.xinge.xinblog.blog.config.BlogThreadPool;
import studio.xinge.xinblog.blog.entity.BlogEntity;
import studio.xinge.xinblog.blog.service.BlogService;
import studio.xinge.xinblog.blog.service.IndexService;
import studio.xinge.xinblog.blog.util.MyHashOperations;
import studio.xinge.xinblog.common.utils.Constant;
import studio.xinge.xinblog.common.utils.PageUtils;
import studio.xinge.xinblog.common.utils.R;
import studio.xinge.xinblog.common.utils.ReturnCode;
import studio.xinge.xinblog.common.valid.groups.Add;
import studio.xinge.xinblog.common.valid.groups.Update;


/**
 * @author 欣哥工作室
 * @email haoxin_2014@163.com
 * @date 2021-07-19 14:43:52
 */
@RestController
@RequestMapping("blog")
@RefreshScope
@Slf4j
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private IndexService indexService;

    @Autowired
    private MyHashOperations myHashOperations;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private BlogThreadPool pool;

    @Value("${blog.cache.ttl.hours}")
    private int blogCacheTTLHours;

    @Value("${cache.update.threshold}")
    private int updateThreshold;

    /**
     * 分页查询
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = blogService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {

        BlogEntity blog = blogService.getById(id);

        return R.ok().put("blog", blog);

    }

    /**
     * 详情页浏览
     * 每次浏览，缓存中浏览数+1
     *
     * @param id
     * @return R
     * @Author xinge
     * @Description
     * @Date 2022/7/4
     */
    @RequestMapping("/view/{id}")
    public R view(@PathVariable("id") String id) throws InterruptedException {
        String key = Constant.BLOG_KEY + id;
        String lockName = Constant.BLOG_LOCK + id;
        Object entity = myHashOperations.get(key, id);
        if (null != entity) {
//            不存在博客的处理
            if (entity.getClass().equals(String.class)) {
                if (entity.toString().equals(Constant.BLOG_NOT_EXIST)) {
                    return R.error(ReturnCode.BLOG_NOT_EXIST);
                }
            }
            RReadWriteLock lock = redissonClient.getReadWriteLock(lockName);
            RLock writeLock = lock.writeLock();
            boolean tryLock = writeLock.tryLock(1200, 1000, TimeUnit.MILLISECONDS);
            try {
                if (tryLock) {
                    updateViewNum(key, id, (BlogEntity) entity);
                }
            } catch (Exception e) {
                log.error("update view_num error", e);
            } finally {
                if (tryLock) {
                    writeLock.unlock();
                }
                return R.ok().put("blog", entity);
            }
        }
//      缓存不存在，查DB，更新缓存
        RReadWriteLock lock = redissonClient.getReadWriteLock(lockName);
        RLock writeLock = lock.writeLock();
        boolean tryLock = writeLock.tryLock(1200, 1000, TimeUnit.MILLISECONDS);
        BlogEntity blog = null;
        try {
            if (tryLock) {
//                高并发下，再次判断缓存是否存在
                BlogEntity entityCheck = (BlogEntity) myHashOperations.get(key, id.toString());
                if (null != entityCheck) {
                    updateViewNum(key, id, entityCheck);
                    return R.ok().put("blog", entity);
                }
                blog = blogService.getById(id);
                if (null != blog) {
                    updateViewNum(key, id, blog);
                } else {
//                    第一次查询，对不存在的值做处理
                    myHashOperations.setHash(key, id, Constant.BLOG_NOT_EXIST, blogCacheTTLHours, TimeUnit.HOURS);
                    return R.error(ReturnCode.BLOG_NOT_EXIST);
                }
            }
        } catch (Exception e) {
            log.error("query DB error", e);
            e.printStackTrace();
        } finally {
            if (tryLock) {
                writeLock.unlock();
            }
        }

        return R.ok().put("blog", blog);
    }

    /**
     * 更新访问量
     * 1.cache实时更新
     * 2.DB，积累到阈值，异步线程池中去更新
     *
     * @param key
     * @param hashkey
     * @param entity
     * @Author xinge
     * @Description
     * @Date 2022/7/5
     */
    private void updateViewNum(String key, String hashkey, BlogEntity entity) {
        Integer viewNum = entity.getViewNum();
        viewNum++;
        entity.setViewNum(viewNum);
        myHashOperations.setHash(key, hashkey, entity, blogCacheTTLHours, TimeUnit.HOURS);
//        积累到阈值，提交异步任务更新
        Integer finalViewNum = viewNum;
        if (finalViewNum % updateThreshold == 0) {
            pool.getPool().submit(() -> {
                blogService.updateById(entity);
            });
        }
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@Validated(Add.class) @RequestBody BlogEntity blog) {

        Date date = new Date();
        blog.setCreateTime(date);
        blog.setUpdateTime(date);
        blogService.save(blog);

        myHashOperations.setHash(Constant.BLOG_KEY + blog.getId(), blog.getId().toString(), blog, blogCacheTTLHours, TimeUnit.HOURS);

        return R.ok();

    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@Validated(Update.class) @RequestBody BlogEntity blog) {
        Date date = new Date();
        blog.setUpdateTime(date);
        blogService.updateById(blog);

        return R.ok();
    }

    /**
     * @param ids
     * @return R
     * @Author xinge
     * @Description 伪删除，修改status为1
     * @Date 2021/9/20
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        Boolean result = false;
        LinkedList<BlogEntity> blogEntities = new LinkedList<>();
        for (Long id : ids) {
            BlogEntity entity = blogService.getById(id);
            if (null != entity) {
                entity.setStatus(Constant.DELETED_STATUS);
                entity.setUpdateTime(new Date());
                blogEntities.add(entity);
            }
            result = blogService.updateBatchById(blogEntities, 1000);
        }

        if (result) {
            return R.ok("删除成功");
        }

        return R.error(ReturnCode.DELETE_FAIL);
    }

    /**
     * @param ids
     * @return R
     * @Author xinge
     * @Description 数据库物理删除
     * @Date 2021/9/21
     */
    @RequestMapping("/deletefromdb")
    public R deleteFromDB(@RequestBody Long[] ids) {
        blogService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * @param blogs
     * @return R
     * @Author xinge
     * @Description 批量添加
     * @Date 2021/9/8
     */
    @RequestMapping("/batch")
    public R batchInsert(@RequestBody BlogEntity[] blogs) {

        boolean flag = blogService.saveBatch(Arrays.asList(blogs), 1000);

        if (flag) {
            return R.ok();
        }
        return R.error(ReturnCode.BATCHINSERT_ERROR);
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
    public R indexData(int from, int to) {
        List topList = (List<BlogEntity>) myHashOperations.get(Constant.BLOG_INDEX_CACHE + "topList", "topList");
        if (null == topList) {
//          缓存不存在时,手动触发
            indexService.getData();
            topList = (List<BlogEntity>) myHashOperations.get(Constant.BLOG_INDEX_CACHE + "topList", "topList");
        }
        List<BlogEntity> newestList = (List<BlogEntity>) myHashOperations.get(Constant.BLOG_INDEX_CACHE + "newestList", "newestList");
        List<BlogEntity> hotList = (List<BlogEntity>) myHashOperations.get(Constant.BLOG_INDEX_CACHE + "hotList", "hotList");

        HashMap<String, Object> indexData = new HashMap<>();
        indexData.put("topList", getSubList(topList, from, to));
        indexData.put("newestList", getSubList(newestList, from, to));
        indexData.put("hotList", getSubList(hotList, from, to));
        return R.ok().put("indexData", indexData);
    }

    private List<BlogEntity> getSubList(List list, int from, int to) {
        if (from > list.size() || to > list.size() || from > to) {
            from = 0;
            if (list.size() < 3) {
                to = list.size();
            } else {
                to = 3;
            }
        }
        return list.subList(from, to);
    }
}
