package studio.xinge.xinblog.blog.service;

import studio.xinge.xinblog.blog.entity.TBlogReply;
import com.baomidou.mybatisplus.extension.service.IService;
import studio.xinge.xinblog.blog.vo.CommentVO;
import studio.xinge.xinblog.blog.vo.TBlogReplyVO;

import java.util.LinkedList;
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

    /**
     * 一个临时计算回复记录id方法
     * 计算昵称、邮箱字符串哈希值
     * 哈希值可能为负值，故与Integer.MAX_VALUE做与运算
     *
     * @param vo
     * @return Long
     * @Author xinge
     * @Description
     * @Date 2022/7/24
     */
    Long getIdWithHashCode(TBlogReplyVO vo);

    /**
     * 按照层级排列回复
     * 一级回复，筛选出回复id为空的
     * 二级回复，筛选出回复id等于一级id
     *
     * @param replys
     * @return LinkedList<TBlogReplyVO>
     * @Author xinge
     * @Description
     * @Date 2022/7/24
     */
    LinkedList<TBlogReplyVO> sortReplyByLevel(List<TBlogReplyVO> replys);

    /**
     * 按照层级排列回复
     * 一级回复，筛选出回复id为空的
     * 二级回复，筛选出回复id等于一级id
     * todo 递归
     *
     * @param replys
     * @return LinkedList<CommentVO>
     * @Author xinge
     * @Description
     * @Date 2022/7/26
     */
    LinkedList<CommentVO> sortReply(List<TBlogReplyVO> replys);

    /**
     * 将VO类转为前端需要类型VO
     *
     * @param replyVO
     * @return CommentVO
     * @Author xinge
     * @Description
     * @Date 2022/7/26
     */
    CommentVO toCommentVO(TBlogReplyVO replyVO);
}
