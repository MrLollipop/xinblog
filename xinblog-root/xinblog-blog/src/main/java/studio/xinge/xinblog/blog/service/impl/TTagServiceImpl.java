package studio.xinge.xinblog.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import studio.xinge.xinblog.blog.entity.TTag;
import studio.xinge.xinblog.blog.mapper.TTagMapper;
import studio.xinge.xinblog.blog.service.TTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import studio.xinge.xinblog.blog.util.MyHashOperations;
import studio.xinge.xinblog.blog.vo.TagVO;
import studio.xinge.xinblog.common.utils.Constant;
import studio.xinge.xinblog.common.utils.PageUtils;
import studio.xinge.xinblog.common.utils.Query;

import java.util.*;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private MyHashOperations myHashOperations;

    /**
     * 将所有Tag保存到缓存中，方便用id索引查找
     *
     * @Author xinge
     * @Description
     * @Date 2022/7/15
     */
    @Override
    public void saveTagCache() {
        List<TTag> tags = this.list();
        HashMap<Long, String> tagsCache = new HashMap<>();
        if (null != tags && !tags.isEmpty()) {
            tags.stream().forEach(t -> {
                tagsCache.put(t.getId(), t.getLabel());
            });
        }
        myHashOperations.setHash(Constant.TAGS, Constant.TAGS, tagsCache, 30, TimeUnit.MINUTES);
    }

    /**
     * 用key值取出缓存中Tag标签名
     *
     * @param key
     * @return String
     * @Author xinge
     * @Description
     * @Date 2022/7/15
     */
    @Override
    public String getTagName(Long key) {
        HashMap tagsCache = (HashMap) myHashOperations.get(Constant.TAGS, Constant.TAGS);
        if (null == tagsCache) {
            saveTagCache();
            tagsCache = (HashMap) myHashOperations.get(Constant.TAGS, Constant.TAGS);
        }

        return (String) tagsCache.get(key);
    }

    /**
     * 将Blog按Tag分组统计
     * 1.每个Tag下Blog数量
     * 2.每个Tag下Blog清单
     *
     * @Author xinge
     * @Description
     * @Date 2022/7/16
     */
    @Override
    public void blogGroupByTag() {
//        构造缓存数据 tagId - 博客idSet
        HashMap<Long, HashSet<Long>> keyBlogsCache = new HashMap<>();
        HashMap<Long, String> tagsCache = (HashMap) myHashOperations.get(Constant.TAGS, Constant.TAGS);
        if (null != tagsCache && !tagsCache.isEmpty()) {
            tagsCache.entrySet().stream().forEach(entry -> {
                keyBlogsCache.put(entry.getKey(), null);
            });
        }
/**        取出 博客id-标签id字符串 缓存
 *              标签id字符串转为标签id数组
 *              遍历字符串，放入 标签id->博客id
 **/
        HashMap<Long, String> blogTagCache = (HashMap) myHashOperations.get(Constant.BLOG_TAGS, Constant.BLOG_TAGS);
        blogTagCache.entrySet().stream().forEach(entry -> {
            int[] tagIds = this.changeTagIdStrToArray(entry.getValue());
            if (tagIds.length > 0) {
                for (long tagId : tagIds) {
                    HashSet<Long> blogIds = keyBlogsCache.get(tagId);
                    if (null == blogIds) {
                        blogIds = new HashSet<Long>();
                    }
                    blogIds.add(entry.getKey());
                    keyBlogsCache.put(tagId, blogIds);
                }
            }
        });

        myHashOperations.setHash(Constant.TAG_BLOGS, Constant.TAG_BLOGS, keyBlogsCache, 30, TimeUnit.MINUTES);
    }

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
    @Override
    public int[] changeTagIdStrToArray(String idStr) {
        if (StrUtil.isNotBlank(idStr)) {
            idStr = idStr.substring(1, idStr.length() - 1);
            int[] tagIdArray = StrUtil.splitToInt(idStr, ',');
            return tagIdArray;
        }
        return new int[0];
    }

    /**
     * 根据Tag取出缓存中所有的BlogId
     *
     * @param key
     * @return HashSet<Long>
     * @Author xinge
     * @Description
     * @Date 2022/7/17
     */
    @Override
    public HashSet<Long> getBlogsByTag(long key) {
        HashMap<Long, HashSet<Long>> tagBlogsCache = (HashMap) myHashOperations.get(Constant.TAG_BLOGS, Constant.TAG_BLOGS);
        if (null == tagBlogsCache) {
            blogGroupByTag();
            tagBlogsCache = (HashMap) myHashOperations.get(Constant.TAG_BLOGS, Constant.TAG_BLOGS);
        }
        HashSet<Long> blogs = tagBlogsCache.get(key);
        return blogs;
    }

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
     *
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
