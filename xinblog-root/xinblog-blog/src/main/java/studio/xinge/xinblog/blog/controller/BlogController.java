package studio.xinge.xinblog.blog.controller;

import java.util.*;
import java.util.concurrent.TimeUnit;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import studio.xinge.xinblog.blog.entity.BlogEntity;
import studio.xinge.xinblog.blog.service.BlogService;
import studio.xinge.xinblog.blog.util.MyHashOperations;
import studio.xinge.xinblog.common.utils.Constant;
import studio.xinge.xinblog.common.utils.PageUtils;
import studio.xinge.xinblog.common.utils.R;
import studio.xinge.xinblog.common.utils.ReturnCode;
import studio.xinge.xinblog.common.valid.groups.Add;
import studio.xinge.xinblog.common.valid.groups.Update;


/**
 * 内部接口，仅对管理台使用
 * DB操作实时响应，无需考虑并发及缓存
 *
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
    private MyHashOperations myHashOperations;

    @Value("${blog.cache.ttl.hours}")
    private int blogCacheTTLHours;

    @Value("${top.blog.limit}")
    private int topBlogLimit;

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
     * 仅管理台调用，实施查询，不做缓存
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {

        BlogEntity blog = blogService.getById(id);

        return R.ok().put("blog", blog);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@Validated(Add.class) @RequestBody BlogEntity blog) {

        Date date = new Date();
        blog.setCreateTime(date);
        blog.setUpdateTime(date);

        if (blog.getTop() && !blogService.checkTopLimit()) {
            return R.error(ReturnCode.TOP_BOLG_LIMIT);
        }

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

        /*
        如果原来不是置顶，现在修改为置顶，需要经过此判断
        如果原来已经置顶，不要判断
        */
        BlogEntity oldBlog = blogService.getById(blog.getId());
        Boolean top = oldBlog.getTop();
        if (!top && blog.getTop() && !blogService.checkTopLimit()) {
            return R.error(ReturnCode.TOP_BOLG_LIMIT);
        }

        blogService.updateById(blog);

//        删除缓存，被动触发更新
        myHashOperations.delete(Constant.BLOG_KEY + blog.getId(), blog.getId().toString());

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
                entity.setStatus(Constant.BlogStatus.DELETE.getValue());
                entity.setUpdateTime(new Date());
                blogEntities.add(entity);
                myHashOperations.delete(Constant.BLOG_KEY + entity.getId(), entity.getId().toString());
            }
        }
        result = blogService.updateBatchById(blogEntities, 1000);

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
        Arrays.stream(ids).forEach(id -> {
            myHashOperations.delete(Constant.BLOG_KEY + id, id.toString());
        });

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
}
