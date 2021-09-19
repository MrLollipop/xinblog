package studio.xinge.xinblog.blog.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import studio.xinge.xinblog.blog.dao.BlogDao;
import studio.xinge.xinblog.blog.entity.BlogEntity;
import studio.xinge.xinblog.blog.service.BlogService;
import studio.xinge.xinblog.common.utils.PageUtils;
import studio.xinge.xinblog.common.utils.Query;


@Service("blogService")
public class BlogServiceImpl extends ServiceImpl<BlogDao, BlogEntity> implements BlogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<BlogEntity> wrapper = new QueryWrapper<>();

        String title = params.get("title").toString();
        String content = params.get("content").toString();
        Integer status = Convert.toInt(params.get("status"));
        Object startDate = params.get("startDate");
        Object endDate = params.get("endDate");
        String isTop = params.get("isTop").toString();

        if (StrUtil.isNotBlank(title)) {
            wrapper.eq("title", title);
        }
        if (StrUtil.isNotBlank(content)) {
            wrapper.like("content", content);
        }
        if (null != status) {
            wrapper.eq("status", status);
        }
        if (ObjectUtil.isAllNotEmpty(startDate, endDate)) {
            Date _startDate = Convert.toDate(startDate);
            Date _endDate = Convert.toDate(endDate);

            wrapper.ge("create_time", _startDate).le("create_time", _endDate).
                    or().ge("update_time", _startDate).le("update_time", _endDate);
        }
        if (StrUtil.isNotBlank(isTop)) {
            wrapper.eq("is_top", Convert.toBool(params.get("isTop")));
        }

        IPage<BlogEntity> page = this.page(
                new Query<BlogEntity>().getPage(params),
                wrapper
        );


        return new PageUtils(page);
    }

}