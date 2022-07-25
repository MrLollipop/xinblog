package studio.xinge.xinblog.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import studio.xinge.xinblog.blog.entity.TBlogReply;
import studio.xinge.xinblog.blog.mapper.TBlogReplyMapper;
import studio.xinge.xinblog.blog.service.TBlogReplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import studio.xinge.xinblog.blog.util.MyHashOperations;
import studio.xinge.xinblog.blog.vo.TBlogReplyVO;
import studio.xinge.xinblog.common.utils.Constant;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 博客回复表 服务实现类
 * </p>
 *
 * @author xinge
 * @since 2022-07-23
 */
@Service
public class TBlogReplyServiceImpl extends ServiceImpl<TBlogReplyMapper, TBlogReply> implements TBlogReplyService {

    @Autowired
    private MyHashOperations myHashOperations;

    /**
     * 保存入库
     * 加入缓存 blogId -> List<replyVO>
     *
     * @param tBlogReply
     * @Author xinge
     * @Description
     * @Date 2022/7/23
     */
    @Override
    public void saveAndUpdateCache(TBlogReply tBlogReply) {
//        入库
        this.save(tBlogReply);
//        取缓存
        Long id = tBlogReply.getBlogId();
        String blogId = StrUtil.toString(id);
        String key = Constant.REPLY + blogId;
        List<TBlogReplyVO> cache = (List) myHashOperations.get(key, blogId);
        if (null == cache) {
            cache = buildCache(id);
        }
        TBlogReplyVO blogReplyVO = BeanUtil.copyProperties(tBlogReply, TBlogReplyVO.class, "status", "top");
        cache.add(blogReplyVO);
        myHashOperations.put(key, blogId, cache);
    }

    /**
     * 回复缓存不存在，构造缓存
     *
     * @param id 博客id
     * @return List<TBlogReplyVO>
     * @Author xinge
     * @Description
     * @Date 2022/7/24
     */
    public List<TBlogReplyVO> buildCache(Long id) {
        List<TBlogReplyVO> cache = new LinkedList<>();
        List<TBlogReply> list = this.list(new QueryWrapper<TBlogReply>().eq("blog_id", id).eq("status", Constant.BlogStatus.NORMAL.getValue()));
        if (null != list && !list.isEmpty()) {
            list.stream().forEach(blogReply -> {
                TBlogReplyVO blogReplyVO = BeanUtil.copyProperties(blogReply, TBlogReplyVO.class, "status", "top");
                cache.add(blogReplyVO);
            });
        }
        myHashOperations.put(Constant.REPLY + StrUtil.toString(id), StrUtil.toString(id), cache);
        return cache;
    }

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
    @Override
    public Long getIdWithHashCode(TBlogReplyVO vo) {
        return (long) (vo.getReplyerNickName() + vo.getReplyerMail()).hashCode() & Integer.MAX_VALUE;
    }

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
    @Override
    public LinkedList<TBlogReplyVO> sortReplyByLevel(List<TBlogReplyVO> replys) {
        LinkedList<TBlogReplyVO> sorted = new LinkedList<>();

        //            一级回复，筛选出回复id为空的
        replys.stream().filter(item -> null == item.getReplyId()).forEach(item -> {
            sorted.add(item);
            //      二级回复，筛选出回复id等于一级id
            List<TBlogReplyVO> level2 = replys.stream().filter(item2 -> null != item2.getReplyId() && item2.getReplyId().equals(item.getId())).collect(Collectors.toList());
            sorted.addAll(level2);
        });
        return sorted;
    }
}
