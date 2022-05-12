package studio.xinge.xinblog.blog.controller;

import java.util.*;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import studio.xinge.xinblog.blog.entity.BlogEntity;
import studio.xinge.xinblog.blog.service.BlogService;
import studio.xinge.xinblog.common.utils.Constant;
import studio.xinge.xinblog.common.utils.PageUtils;
import studio.xinge.xinblog.common.utils.R;
import studio.xinge.xinblog.common.utils.ReturnCode;
import studio.xinge.xinblog.common.valid.groups.Add;
import studio.xinge.xinblog.common.valid.groups.Update;

import javax.validation.Valid;


/**
 * @author 欣哥工作室
 * @email haoxin_2014@163.com
 * @date 2021-07-19 14:43:52
 */
@RestController
@RequestMapping("blog")
@RefreshScope
public class BlogController {

    @Autowired
    private BlogService blogService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = blogService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
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
        blogService.save(blog);

        return R.ok();

    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@Validated(Update.class) @RequestBody BlogEntity blog) {
        Date date = new Date();
        blog.setUpdateTime(date);
        blogService.updateById(blog);

        return R.ok();
    }

    /**
     *
     * @Author xinge
     * @Description 伪删除，修改status为1
     * @Date 2021/9/20
    * @param ids
    * @return R
    */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        Boolean result = false;
        LinkedList<BlogEntity> blogEntities = new LinkedList<>();
        for (Long id : ids) {
            BlogEntity entity = blogService.getById(id);
            if (null != entity) {
                entity.setStatus(Constant.DELETED_STATUS);
                entity.setUpdateTime(new Date());
                blogEntities.add(entity);
            }
             result = blogService.updateBatchById(blogEntities, 1000);
        }

        if (result) {
            return R.ok("删除成功");
        }

        return R.error(ReturnCode.DELETE_FAIL);
    }

    /**
     *
     * @Author xinge
     * @Description 数据库物理删除
     * @Date 2021/9/21
    * @param ids
    * @return R
    */
    @RequestMapping("/deletefromdb")
    public R deleteFromDB(@RequestBody Long[] ids) {
        blogService.removeByIds(Arrays.asList(ids));

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
