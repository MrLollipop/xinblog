package studio.xinge.xinblog.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import studio.xinge.xinblog.blog.entity.BlogEntity;
import studio.xinge.xinblog.common.utils.PageUtils;

import java.util.Map;

/**
 * 
 *
 * @author 欣哥工作室
 * @email haoxin_2014@163.com
 * @date 2021-07-19 14:35:51
 */
public interface BlogService extends IService<BlogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

