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
        String key = Constant.REPLY + StrUtil.toString(id);
        List<TBlogReplyVO> cache = (List) myHashOperations.get(key, StrUtil.toString(id));
        if (null == cache) {
            cache = buildCache(id);
        }
        TBlogReplyVO blogReplyVO = BeanUtil.copyProperties(tBlogReply, TBlogReplyVO.class, "status", "top");
        cache.add(blogReplyVO);
        myHashOperations.put(key, StrUtil.toString(id), cache);
    }

    /**
     * 缓存不存在，构造缓存
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
        return cache;
    }
}
