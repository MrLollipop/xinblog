package studio.xinge.xinblog.blog.mapper;

import org.apache.ibatis.annotations.Param;
import studio.xinge.xinblog.blog.entity.BlogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author 欣哥工作室
 * @email haoxin_2014@163.com
 * @date 2021-07-19 14:35:51
 */
@Mapper
public interface BlogDao extends BaseMapper<BlogEntity> {

    Boolean updateViewNumById(@Param("id") Long id, @Param("viewNum") int viewNum);
	
}
