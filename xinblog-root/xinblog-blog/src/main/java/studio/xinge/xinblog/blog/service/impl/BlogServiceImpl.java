package studio.xinge.xinblog.blog.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.dubbo.config.annotation.Service;
import studio.xinge.xinblog.blog.dao.BlogDao;
import studio.xinge.xinblog.blog.entity.BlogEntity;
import studio.xinge.xinblog.blog.service.BlogService;
import studio.xinge.xinblog.common.api.ApiBlogService;
import studio.xinge.xinblog.common.utils.PageUtils;
import studio.xinge.xinblog.common.utils.Query;
import studio.xinge.xinblog.common.utils.R;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogDao, BlogEntity> implements ApiBlogService, BlogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<BlogEntity> wrapper = new QueryWrapper<>();

        String title = Convert.toStr(params.get("title"));
        String content = Convert.toStr(params.get("content"));
        Integer status = Convert.toInt(params.get("status"));
        Object startDate = params.get("startDate");
        Object endDate = params.get("endDate");
        String top = Convert.toStr(params.get("top"));

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
            Date _startDate = DateUtil.parse(startDate.toString());
            Date _endDate = DateUtil.parse(endDate.toString().concat(" 23:59:59"));

            wrapper.and(_wrapper -> _wrapper.ge("create_time", _startDate).le("create_time", _endDate).
                    or().ge("update_time", _startDate).le("update_time", _endDate));
        }
        if (StrUtil.isNotBlank(top)) {
            wrapper.eq("top", Convert.toBool(top));
        }

        wrapper.orderByDesc("id");

        IPage<BlogEntity> page = this.page(
                new Query<BlogEntity>().getPage(params),
                wrapper
        );


        return new PageUtils(page);
    }

}