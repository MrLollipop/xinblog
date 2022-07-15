package studio.xinge.xinblog.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import studio.xinge.xinblog.blog.entity.BlogEntity;
import studio.xinge.xinblog.blog.entity.TTag;
import studio.xinge.xinblog.blog.mapper.TTagMapper;
import studio.xinge.xinblog.blog.service.TTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import studio.xinge.xinblog.blog.vo.TagVO;
import studio.xinge.xinblog.common.utils.PageUtils;
import studio.xinge.xinblog.common.utils.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xinge
 * @since 2022-07-13
 */
@Service
public class TTagServiceImpl extends ServiceImpl<TTagMapper, TTag> implements TTagService {

    @Override
    public PageUtils listByPage(Map<String, Object> params) {
        Boolean useVO = Convert.toBool(params.get("useVO"), false);
        IPage<TTag> page = this.page(new Query<TTag>().getPage(params));
        if (useVO) {
            List<TTag> records = page.getRecords();
            ArrayList<TagVO> tagVOList = new ArrayList<>();
            changToVo(records, tagVOList);
            PageUtils pageUtils = new PageUtils(page);
            pageUtils.setList(tagVOList);
            return pageUtils;
        }
        return new PageUtils(page);
    }

    /**
     * 将List TTag对象转换为List TagVO对象
     * @param records
     * @param tagVOList
     * @Author xinge
     * @Description
     * @Date 2022/7/15
     */
    public void changToVo(List<TTag> records, ArrayList<TagVO> tagVOList) {
        if (null != records && !records.isEmpty()) {
            records.stream().forEach(tag -> {
                TagVO tagVO = new TagVO();
                BeanUtil.copyProperties(tag, tagVO);
                tagVO.setKey(tag.getId());
                tagVOList.add(tagVO);
            });
        }
    }

    /**
     * 判断是否已存在tag
     * 1.其他label是否有相同的，且不是其本身
     * 有相同返回true
     *
     * @param tag
     * @return boolean
     * @Author xinge
     * @Description
     * @Date 2022/7/14
     */
    public boolean existedTag(TTag tag) {
        String labelTrim = tag.getLabel().replaceAll("\\s*", "");
        Long tagId = tag.getId();
        List<TTag> tags = this.list();
        if (null != tags && !tags.isEmpty()) {
            for (TTag t : tags) {
                if (t.getLabel().replaceAll("\\s*", "").equalsIgnoreCase(labelTrim) && !t.getId().equals(tagId)) {
                    return true;
                }
            }
        }
        return false;
    }
}
