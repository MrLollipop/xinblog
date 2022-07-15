package studio.xinge.xinblog.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import studio.xinge.xinblog.blog.entity.BlogEntity;
import studio.xinge.xinblog.blog.vo.BlogEntityVO;
import studio.xinge.xinblog.common.utils.PageUtils;

import java.util.Map;

/**
 * @author 欣哥工作室
 * @email haoxin_2014@163.com
 * @date 2021-07-19 14:35:51
 */
public interface BlogService extends IService<BlogEntity> {

    PageUtils queryPage(Map<String, Object> params);

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
    boolean checkTopLimit();

    /**
     * 实体类BlogEntity转为VO
     * @param blog
     * @Author xinge
     * @Description
     * @Date 2022/7/15
     */
    BlogEntityVO changeEntityToVO(BlogEntity blog);
}

