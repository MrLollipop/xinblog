package studio.xinge.xinblog.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import studio.xinge.xinblog.blog.entity.TTag;
import com.baomidou.mybatisplus.extension.service.IService;
import studio.xinge.xinblog.common.utils.PageUtils;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xinge
 * @since 2022-07-13
 */
public interface TTagService extends IService<TTag> {

    PageUtils listByPage(Map<String, Object> params);

    boolean existedTag(TTag tag);
}
