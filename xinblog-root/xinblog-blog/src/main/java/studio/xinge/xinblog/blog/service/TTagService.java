package studio.xinge.xinblog.blog.service;

import studio.xinge.xinblog.blog.entity.TTag;
import com.baomidou.mybatisplus.extension.service.IService;
import studio.xinge.xinblog.blog.vo.TagVO;
import studio.xinge.xinblog.common.utils.PageUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
     * 将List TTag对象转换为List TagVO对象
     *
     * @param records
     * @param tagVOList
     * @Author xinge
     * @Description
     * @Date 2022/7/15
     */
    void changToVo(List<TTag> records, ArrayList<TagVO> tagVOList);

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
    boolean existedTag(TTag tag);

    /**
     * 将所有Tag保存到缓存中，方便用id索引查找
     *
     * @Author xinge
     * @Description
     * @Date 2022/7/15
     */
    void saveTagCache();

    /**
     * 用key值取出缓存中Tag标签名
     *
     * @param key
     * @return String
     * @Author xinge
     * @Description
     * @Date 2022/7/15
     */
    String getTagName(Long key);

    /**
     * 将Blog按Tag分组统计
     * 1.每个Tag下Blog数量
     * 2.每个Tag下Blog清单
     *
     * @Author xinge
     * @Description
     * @Date 2022/7/16
     */
    void blogGroupByTag();

    /**
     * 将Tagid字符串，转为数组
     * idStr字符串[5, 6]转为int数组
     *
     * @param idStr
     * @return int[]
     * @Author xinge
     * @Description
     * @Date 2022/7/16
     */
    int[] changeTagIdStrToArray(String idStr);

    /**
     * 根据Tag取出缓存中所有的BlogId
     *
     * @param key
     * @return HashSet<Long>
     * @Author xinge
     * @Description
     * @Date 2022/7/17
     */
    HashSet<Long> getBlogsByTag(long key);
}
