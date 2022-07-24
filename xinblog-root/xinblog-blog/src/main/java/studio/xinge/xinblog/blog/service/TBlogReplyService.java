package studio.xinge.xinblog.blog.service;

import studio.xinge.xinblog.blog.entity.TBlogReply;
import com.baomidou.mybatisplus.extension.service.IService;
import studio.xinge.xinblog.blog.vo.TBlogReplyVO;

import java.util.List;

/**
 * <p>
 * 博客回复表 服务类
 * </p>
 *
 * @author xinge
 * @since 2022-07-23
 */
public interface TBlogReplyService extends IService<TBlogReply> {

    /**
     * 保存入库
     * 加入缓存 blogId -> List<replyVO>
     *
     * @param tBlogReply
     * @Author xinge
     * @Description
     * @Date 2022/7/23
     */
    void saveAndUpdateCache(TBlogReply tBlogReply);

    /**
     * 一次查询，获取当前博客所有回复
     * 放入缓存
     *
     * @param blogId
     * @return List<TBlogReplyVO>
     * @Author xinge
     * @Description
     * @Date 2022/7/24
     */
    List<TBlogReplyVO> buildCache(Long blogId);
}
