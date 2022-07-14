package studio.xinge.xinblog.blog.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import studio.xinge.xinblog.blog.entity.TTag;
import studio.xinge.xinblog.blog.service.TTagService;
import studio.xinge.xinblog.blog.vo.TagVO;
import studio.xinge.xinblog.common.utils.PageUtils;
import studio.xinge.xinblog.common.utils.Query;
import studio.xinge.xinblog.common.utils.R;
import studio.xinge.xinblog.common.utils.ReturnCode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xinge
 * @since 2022-07-13
 */
@RestController
@RequestMapping("/blog/tag")
public class TTagController {

    @Autowired
    private TTagService tagService;

    /**
     * 分页查询
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = tagService.listByPage(params);

        return R.ok().put("page", page);
    }

    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") String id) {
        TTag tag = tagService.getById(id);
        return R.ok().put("tag", tag);
    }

    @RequestMapping("/create")
    public R create(@RequestBody TagVO tagVO) {
        TTag tag = new TTag();
        BeanUtil.copyProperties(tagVO, tag);

        if (tagService.existedTag(tag)) return R.error(ReturnCode.SAME_TAG_ERROR);
        boolean save = tagService.save(tag);
        if (!save) {
            return R.error();
        }
        return R.ok();
    }

    @RequestMapping("/update")
    public R update(@RequestBody TagVO tagVO) {
        TTag tag = new TTag();
        BeanUtil.copyProperties(tagVO, tag);
        tag.setId(tagVO.getKey());

        if (tagService.existedTag(tag)) return R.error(ReturnCode.SAME_TAG_ERROR);

        boolean update = tagService.updateById(tag);
        if (!update) {
            return R.error();
        }
        return R.ok();
    }

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        boolean delete = tagService.removeByIds(Arrays.asList(ids));
        if (!delete) {
            return R.error();
        }
        return R.ok();
    }

}
