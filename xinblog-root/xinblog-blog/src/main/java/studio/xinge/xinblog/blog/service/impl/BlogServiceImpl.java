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

//import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import studio.xinge.xinblog.blog.dao.BlogDao;
import studio.xinge.xinblog.blog.entity.BlogEntity;
import studio.xinge.xinblog.blog.service.BlogService;
import studio.xinge.xinblog.common.utils.Constant;
import studio.xinge.xinblog.common.utils.PageUtils;
import studio.xinge.xinblog.common.utils.Query;
import studio.xinge.xinblog.common.utils.R;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogDao, BlogEntity> implements BlogService {

    @Value("${top.blog.limit}")
    private int topBlogLimit;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<BlogEntity> wrapper = new QueryWrapper<>();

        String title = Convert.toStr(params.get("title"));
        Integer status = Convert.toInt(params.get("status"));
        Object startDate = params.get("startDate");
        Object endDate = params.get("endDate");
        String top = Convert.toStr(params.get("top"));

        if (StrUtil.isNotBlank(title)) {
            wrapper.eq("title", title);
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

    /**
     * 检查置顶博客是否已达上线
     * 1.状态正常且为置顶的，数目不超过 topBlogLimit
     * 2.到达上限返回false，未到达返回true
     *
     * @return boolean
     * @Author xinge
     * @Description
     * @Date 2022/7/11
     */
    @Override
    public boolean checkTopLimit() {
        int count = this.count(new QueryWrapper<BlogEntity>().eq("status", Constant.BlogStatus.NORMAL.getValue()).eq("top", true));
        if (count >= topBlogLimit) {
            return false;
        }
        return true;
    }

}