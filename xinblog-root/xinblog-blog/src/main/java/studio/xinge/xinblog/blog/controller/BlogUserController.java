package studio.xinge.xinblog.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studio.xinge.xinblog.blog.entity.BlogEntity;
import studio.xinge.xinblog.blog.service.BlogService;
import studio.xinge.xinblog.blog.service.IndexService;
import studio.xinge.xinblog.blog.util.MyHashOperations;
import studio.xinge.xinblog.common.utils.Constant;
import studio.xinge.xinblog.common.utils.R;
import studio.xinge.xinblog.common.utils.ReturnCode;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 针对于博客用户，对外开放接口
 * 需要注意并发问题
 * 对DB写操作异步
 *
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/7/8
 * @Description
 */
@RestController
@RequestMapping("blogUser")
@RefreshScope
@Slf4j
public class BlogUserController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private MyHashOperations myHashOperations;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private IndexService indexService;

    @Autowired
    private ExecutorService threadPool;

    @Value("${blog.cache.ttl.hours}")
    private int blogCacheTTLHours;

    @Value("${cache.update.threshold}")
    private int updateThreshold;

    /**
     * 详情页浏览：
     * 高并发访问，分布式锁控制从DB读数据
     * 1.对不存在数据做空值保护
     * 2.每次浏览，缓存中浏览数+1
     * Cache直接更新
     * 异步更新
     *
     * @param id
     * @return R
     * @Author xinge
     * @Description
     * @Date 2022/7/4
     */
    @RequestMapping("/view/{id}")
    public R view(@PathVariable("id") String id) throws InterruptedException {
        String key = Constant.BLOG_KEY + id;
        String lockName = Constant.BLOG_LOCK + id;
        checkCacheExist(key, id);
//      缓存不存在，查DB，更新缓存；
//      分布式锁，没锁线程自旋等待其他线程释放；
        RLock lock = redissonClient.getLock(lockName);
        BlogEntity blog = null;
        try {
            lock.lock(1000, TimeUnit.MILLISECONDS);
//                高并发下，再次判断缓存是否存在
            checkCacheExist(key, id);
//                todo 状态正常筛选
            blog = blogService.getOne(new QueryWrapper<BlogEntity>().eq("status", Constant.BlogStatus.NORMAL.getValue()).eq("id", id));
            if (null != blog) {
                updateViewNum(key, id, blog);
            } else {
//                    第一次查询，对不存在的值做处理
                myHashOperations.setHash(key, id, Constant.BLOG_NOT_EXIST, blogCacheTTLHours, TimeUnit.HOURS);
                return R.error(ReturnCode.BLOG_NOT_EXIST);
            }
        } catch (Exception e) {
            log.error("query DB error", e);
        } finally {
            lock.unlock();
        }

        return R.ok().put("blog", blog);
    }

    /**
     * 检查缓存中是否已有数据
     * 1.没有数据返回空
     * 2.有数据，判断类型
     * 字符型：返回错误提示。
     * Blog：更新访问量，返回正常。
     *
     * @param key
     * @param id
     * @return R
     * @Author xinge
     * @Description
     * @Date 2022/7/8
     */
    private R checkCacheExist(String key, String id) {
        Object entity = myHashOperations.get(key, id);
        if (null != entity) {
//            不存在博客的处理
            if (entity.getClass().equals(String.class)) {
                if (entity.toString().equals(Constant.BLOG_NOT_EXIST)) {
                    return R.error(ReturnCode.BLOG_NOT_EXIST);
                }
            }
            updateViewNum(key, id, (BlogEntity) entity);
            return R.ok().put("blog", entity);
        }
        return null;
    }


    /**
     * 更新访问量
     * 1.cache实时更新
     * 2.DB，积累到阈值，异步线程池中去更新
     *
     * @param key
     * @param hashkey
     * @param entity
     * @Author xinge
     * @Description
     * @Date 2022/7/5
     */
    private void updateViewNum(String key, String hashkey, BlogEntity entity) {
        Integer viewNum = entity.getViewNum();
        viewNum++;
        entity.setViewNum(viewNum);
        myHashOperations.setHash(key, hashkey, entity, blogCacheTTLHours, TimeUnit.HOURS);
//        积累到阈值，提交异步任务更新
        Integer finalViewNum = viewNum;
        if (finalViewNum % updateThreshold == 0) {
            threadPool.submit(() -> {
                blogService.updateById(entity);
            });
        }
    }

    /**
     * 获取首页数据
     * 一次DB查询所有（state正常），整理3类数据
     * 1.置顶，top字段筛选
     * 2.最新，update_time逆序排序
     * 3.热门，view_num逆序排序
     *
     * @return R
     * @Author xinge
     * @Description
     * @Date 2022/7/4
     */
    @RequestMapping("/indexData")
    public R indexData(int from, int to) {
        List topList = (List<BlogEntity>) myHashOperations.get(Constant.BLOG_INDEX_CACHE + "topList", "topList");
        if (null == topList) {
//          缓存不存在时,手动触发
            indexService.getData();
            topList = (List<BlogEntity>) myHashOperations.get(Constant.BLOG_INDEX_CACHE + "topList", "topList");
        }
        List<BlogEntity> newestList = (List<BlogEntity>) myHashOperations.get(Constant.BLOG_INDEX_CACHE + "newestList", "newestList");
        List<BlogEntity> hotList = (List<BlogEntity>) myHashOperations.get(Constant.BLOG_INDEX_CACHE + "hotList", "hotList");

        HashMap<String, Object> indexData = new HashMap<>();
        indexData.put("topList", getSubList(topList, from, to));
        indexData.put("newestList", getSubList(newestList, from, to));
        indexData.put("hotList", getSubList(hotList, from, to));
        return R.ok().put("indexData", indexData);
    }

    private List<BlogEntity> getSubList(List list, int from, int to) {
        if (from > list.size() - 1 || to > list.size() - 1 || from > to) {
            from = 0;
            if (list.size() < 3) {
                to = list.size();
            } else {
                to = 3;
            }
        }
        return list.subList(from, to);
    }

}
