package studio.xinge.xinblog.blog.controller;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import studio.xinge.xinblog.blog.entity.BlogEntity;
import studio.xinge.xinblog.blog.entity.TBlogReply;
import studio.xinge.xinblog.blog.service.TBlogReplyService;
import studio.xinge.xinblog.blog.util.MyHashOperations;
import studio.xinge.xinblog.blog.vo.TBlogReplyVO;
import studio.xinge.xinblog.common.utils.Constant;
import studio.xinge.xinblog.common.utils.PageUtils;
import studio.xinge.xinblog.common.utils.R;
import studio.xinge.xinblog.common.utils.ReturnCode;

import java.util.*;

/**
 * 博客管理台服务接口
 * 仅管理台调用，实效性要求，无并发要求
 *
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/7/29
 * @Description
 */
@RestController
@RequestMapping("blog/reply")
public class TReplyController {

    @Autowired
    private TBlogReplyService replyService;

    @Autowired
    private MyHashOperations myHashOperations;

    @GetMapping("list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = replyService.listByConditions(params);
        return R.ok().put("page", page);
    }

    @PostMapping("delete")
    public R delete(@RequestBody Long[] ids){
        Boolean result = false;
        LinkedList<TBlogReply> replies = new LinkedList<>();
        for (Long id : ids) {
            TBlogReply entity = replyService.getById(id);
            if (null != entity) {
                entity.setStatus(Constant.BlogStatus.DELETE.getValue());
                replies.add(entity);
                String hashKey = StrUtil.toString(entity.getBlogId());
                List<TBlogReplyVO> cache = (List) myHashOperations.get(Constant.REPLY + hashKey, hashKey);
                if (null != cache) {
                    TBlogReplyVO replyVO = new TBlogReplyVO(entity.getId());
                    cache.remove(replyVO);
                    myHashOperations.put(Constant.REPLY + hashKey, hashKey, cache);
                }
            }
        }
        result = replyService.updateBatchById(replies, 1000);

        if (result) {
            return R.ok("删除成功");
        }

        return R.error(ReturnCode.DELETE_FAIL);
    }
}
