package studio.xinge.xinblog.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import studio.xinge.xinblog.blog.entity.BlogEntity;
import studio.xinge.xinblog.blog.util.MyHashOperations;
import studio.xinge.xinblog.common.utils.Constant;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/7/5
 * @Description
 */
@Component
public class IndexService {

    @Autowired
    private BlogService blogService;

    @Autowired
    private MyHashOperations myHashOperations;

    /**
     * 获取首页数据
     * 一次DB查询所有（state正常），整理3类数据
     * 1.置顶，top字段筛选、逆序排序
     * 2.最新，非置顶、update_time逆序排序
     * 3.热门，非置顶、view_num逆序排序
     *
     * @Author xinge
     * @Description
     * @Date 2022/7/5
     */
    public void getData() {
//        返回正常状态博客全部数据
        List<BlogEntity> normalList = blogService.list(new QueryWrapper<BlogEntity>().eq("status", Constant.BlogStatus.NORMAL.getValue()));

//             * 1.置顶，top字段筛选、id逆序排序
        List<BlogEntity> topList = normalList.stream().filter((blog) -> blog.getTop()).sorted(Comparator.comparingLong(BlogEntity::getId).reversed()).collect(Collectors.toList());

//             * 2.最新，非置顶、update_time逆序排序
        List<BlogEntity> newestList = normalList.stream().filter((blog) -> !blog.getTop()).sorted(Comparator.comparing(BlogEntity::getUpdateTime).reversed()).collect(Collectors.toList());

//             * 3.热门，非置顶、view_num逆序排序
        List<BlogEntity> hotList = normalList.stream().filter((blog) -> !blog.getTop()).sorted(Comparator.comparingInt(BlogEntity::getViewNum).reversed()).collect(Collectors.toList());

        myHashOperations.setHash(Constant.BLOG_INDEX_CACHE + "topList", "topList", topList, 30, TimeUnit.MINUTES);
        myHashOperations.setHash(Constant.BLOG_INDEX_CACHE + "newestList", "newestList", newestList, 30, TimeUnit.MINUTES);
        myHashOperations.setHash(Constant.BLOG_INDEX_CACHE + "hotList", "hotList", hotList, 30, TimeUnit.MINUTES);
    }
}