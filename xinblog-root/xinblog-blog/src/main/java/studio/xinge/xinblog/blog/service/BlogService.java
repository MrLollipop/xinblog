package studio.xinge.xinblog.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import studio.xinge.xinblog.blog.entity.BlogEntity;
import studio.xinge.xinblog.blog.vo.BlogEntityVO;
import studio.xinge.xinblog.blog.vo.BlogListVO;
import studio.xinge.xinblog.blog.vo.PageVO;
import studio.xinge.xinblog.common.utils.PageUtils;
import studio.xinge.xinblog.common.utils.R;

import java.util.List;
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
     *
     * @param blog
     * @Author xinge
     * @Description
     * @Date 2022/7/15
     */
    BlogEntityVO changeEntityToVO(BlogEntity blog);

    /**
     * 将VO类转为Entity
     *
     * @param vo
     * @return BlogEntity
     * @Author xinge
     * @Description
     * @Date 2022/7/16
     */
    BlogEntity changeVOToEntity(BlogEntityVO vo);

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
    Boolean updateViewNumById(Long id, Integer viewNum);

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
    BlogEntityVO getBlogEntityVO(Long blogId);

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
    BlogListVO getSubList(List list, PageVO pageVO);

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
    void updateViewNum(String key, String hashKey, BlogEntityVO blogVO);

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
    R checkCacheExist(String key, String id);
}

