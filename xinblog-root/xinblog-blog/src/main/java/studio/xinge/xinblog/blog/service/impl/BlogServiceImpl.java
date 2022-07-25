package studio.xinge.xinblog.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

//import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import studio.xinge.xinblog.blog.mapper.BlogDao;
import studio.xinge.xinblog.blog.entity.BlogEntity;
import studio.xinge.xinblog.blog.service.BlogService;
import studio.xinge.xinblog.blog.service.TTagService;
import studio.xinge.xinblog.blog.util.MyHashOperations;
import studio.xinge.xinblog.blog.vo.BlogEntityVO;
import studio.xinge.xinblog.blog.vo.BlogListVO;
import studio.xinge.xinblog.blog.vo.PageVO;
import studio.xinge.xinblog.blog.vo.TagVO;
import studio.xinge.xinblog.common.utils.*;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogDao, BlogEntity> implements BlogService {

    @Value("${top.blog.limit}")
    private int topBlogLimit;

    @Value("${blog.cache.ttl.hours}")
    private int blogCacheTTLHours;

    @Value("${cache.update.threshold}")
    private int updateThreshold;

    @Autowired
    private ExecutorService threadPool;

    @Autowired
    private TTagService tagService;

    @Autowired
    private MyHashOperations myHashOperations;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<BlogEntity> wrapper = new QueryWrapper<>();

        String title = Convert.toStr(params.get("title"));
        Integer status = Convert.toInt(params.get("status"));
        Object startDate = params.get("startDate");
        Object endDate = params.get("endDate");
        String top = Convert.toStr(params.get("top"));

        if (StrUtil.isNotBlank(title)) {
            wrapper.eq("title", title);
        }
        if (null != status) {
            wrapper.eq("status", status);
        }
        if (ObjectUtil.isAllNotEmpty(startDate, endDate)) {
            Date _startDate = DateUtil.parse(startDate.toString());
            Date _endDate = DateUtil.parse(endDate.toString().concat(" 23:59:59"));

            wrapper.and(_wrapper -> _wrapper.ge("create_time", _startDate).le("create_time", _endDate).
                    or().ge("update_time", _startDate).le("update_time", _endDate));
        }
        if (StrUtil.isNotBlank(top)) {
            wrapper.eq("top", Convert.toBool(top));
        }

        wrapper.orderByDesc("id");

        IPage<BlogEntity> page = this.page(
                new Query<BlogEntity>().getPage(params),
                wrapper
        );

        /**
         *  分页数据的entity转vo
         */
        List<BlogEntity> records = page.getRecords();
        ArrayList<BlogEntityVO> blogVOList = new ArrayList<>();
        if (null != records && !records.isEmpty()) {
            records.stream().forEach(item -> {
                blogVOList.add(changeEntityToVO(item));
            });
            PageUtils pageUtils = new PageUtils(page);
            pageUtils.setList(blogVOList);
            return pageUtils;
        }
        return new PageUtils(page);
    }

    /**
     * 检查置顶博客是否已达上线
     * 1.状态正常且为置顶的，数目不超过 topBlogLimit
     * 2.到达上限返回false，未到达返回true
     *
     * @return boolean
     * @Author xinge
     * @Description
     * @Date 2022/7/11
     */
    @Override
    public boolean checkTopLimit() {
        int count = this.count(new QueryWrapper<BlogEntity>().eq("status", Constant.BlogStatus.NORMAL.getValue()).eq("top", true));
        if (count >= topBlogLimit) {
            return false;
        }
        return true;
    }

    /**
     * 实体类BlogEntity转为VO
     *
     * @param blog
     * @Author xinge
     * @Description
     * @Date 2022/7/15
     */
    @Override
    public BlogEntityVO changeEntityToVO(BlogEntity blog) {
        BlogEntityVO vo = new BlogEntityVO();
        BeanUtil.copyProperties(blog, vo, "tags");
        int[] tagKeyArray = {};
        ArrayList<TagVO> tagVOList = new ArrayList<>();
//        字符串[5, 6]转为int数组
        int[] tagIds = tagService.changeTagIdStrToArray(blog.getTags());
        if (tagIds.length > 0) {
            for (int i : tagIds) {
                tagVOList.add(new TagVO(Long.valueOf(i), tagService.getTagName(Long.valueOf(i))));
            }
        }

        vo.setTags(tagIds);
        vo.setTagVOList(tagVOList);

        return vo;
    }

    /**
     * 将VO类转为Entity
     *
     * @param vo
     * @return BlogEntity
     * @Author xinge
     * @Description
     * @Date 2022/7/16
     */
    @Override
    public BlogEntity changeVOToEntity(BlogEntityVO vo) {
        BlogEntity entity = new BlogEntity();
        BeanUtil.copyProperties(vo, entity, "tags", "tagLabelList");
        entity.setTags(Arrays.toString(vo.getTags()));
        return entity;
    }

    /**
     * 根据id更新访问量
     *
     * @param id
     * @param viewNum
     * @return Boolean
     * @Author xinge
     * @Description
     * @Date 2022/7/16
     */
    @Override
    public Boolean updateViewNumById(Long id, Integer viewNum) {
        return this.getBaseMapper().updateViewNumById(id, viewNum);
    }

    /**
     * 从缓存中取BlogEntityVO
     * 不存在，查库，并放入缓存
     *
     * @param blogId
     * @return BlogEntityVO
     * @Author xinge
     * @Description
     * @Date 2022/7/24
     */
    @Override
    public BlogEntityVO getBlogEntityVO(Long blogId) {
        //            先从博客缓存中查
        BlogEntityVO vo = (BlogEntityVO) myHashOperations.get(Constant.BLOG + blogId, String.valueOf(blogId));
        if (null == vo) {
//                不存在，将DB结果放入缓存
            BlogEntity blog = this.getOne(new QueryWrapper<BlogEntity>().eq("id", blogId).eq("status", Constant.BlogStatus.NORMAL.getValue()));
            if (null != blog) {
                vo = this.changeEntityToVO(blog);
                myHashOperations.setHash(Constant.BLOG + blogId, String.valueOf(blogId), vo, blogCacheTTLHours, TimeUnit.HOURS);
            }
        }
        return vo;
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
     * @Date 2022/7/25
     */
    @Override
    public BlogListVO getSubList(List list, PageVO pageVO) {
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
     * 更新访问量
     * 1.cache实时更新，同时转vo
     * 2.DB，积累到阈值，异步线程池中去更新
     *
     * @param key
     * @param hashKey
     * @param blogVO
     * @Author xinge
     * @Description
     * @Date 2022/7/25
     */
    @Override
    public void updateViewNum(String key, String hashKey, BlogEntityVO blogVO) {
        Integer viewNum = blogVO.getViewNum() == null ? 0 : blogVO.getViewNum();
        viewNum++;
        blogVO.setViewNum(viewNum);
        myHashOperations.setHash(key, hashKey, blogVO, blogCacheTTLHours, TimeUnit.HOURS);
//        积累到阈值，提交异步任务更新
        Integer finalViewNum = viewNum;
        if (finalViewNum % updateThreshold == 0) {
            threadPool.submit(() -> {
                this.updateViewNumById(blogVO.getId(), blogVO.getViewNum());
            });
        }
    }

    /**
     * 检查缓存中是否已有数据
     * 1.没有数据返回空
     * 2.有数据，判断类型
     * 字符型：返回错误提示。
     * 3.Blog：更新访问量，返回正常。
     *
     * @param key
     * @param id
     * @return R
     * @Author xinge
     * @Description
     * @Date 2022/7/8
     */
    @Override
    public R checkCacheExist(String key, String id) {
        Object entity = myHashOperations.get(key, id);
        if (null != entity) {
//            不存在博客的处理
            if (entity.getClass().equals(String.class) && entity.toString().equals(Constant.BLOG_NOT_EXIST)) {
                return R.error(ReturnCode.BLOG_NOT_EXIST);
            }
//          取出是vo
            BlogEntityVO blogVO = (BlogEntityVO) entity;
            this.updateViewNum(key, id, blogVO);
            return R.ok().put("blog", blogVO);
        }
        return null;
    }

}