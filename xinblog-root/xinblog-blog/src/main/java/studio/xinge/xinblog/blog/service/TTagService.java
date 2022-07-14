package studio.xinge.xinblog.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import studio.xinge.xinblog.blog.entity.TTag;
import com.baomidou.mybatisplus.extension.service.IService;
import studio.xinge.xinblog.common.utils.PageUtils;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xinge
 * @since 2022-07-13
 */
public interface TTagService extends IService<TTag> {

    PageUtils listByPage(Map<String, Object> params);

    /**
     * 判断是否已存在tag
     * 1.其他label是否有相同的，且不是其本身
     *      有相同返回true
     * @param tag
     * @return boolean
     * @Author xinge
     * @Description
     * @Date 2022/7/14
     */
    boolean existedTag(TTag tag);
}
