package studio.xinge.xinblog.blog.task;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import studio.xinge.xinblog.blog.entity.TTag;
import studio.xinge.xinblog.blog.service.IndexService;
import studio.xinge.xinblog.blog.service.TTagService;
import studio.xinge.xinblog.blog.util.MyHashOperations;
import studio.xinge.xinblog.common.utils.Constant;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 标签缓存刷新定时任务
 * 1.分布式锁控制单个微服务执行
 *
 * @Author xinge
 * @Description
 * @Date 2022/7/5
 */
@Component
@Slf4j
public class TagCacheUpdateTask {

    @Value("${server.port}")
    private String port;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private TTagService tagService;

    @Scheduled(cron = "5 0/10 * * * ? ")
    public void trigger() throws InterruptedException {
        RLock lock = redissonClient.getLock("tagCacheUpdateTask");
        boolean tryLock = lock.tryLock(2, 1000, TimeUnit.MILLISECONDS);
        try {
            if (tryLock) {
                doTask();
            }
        } finally {
            if (tryLock) {
                lock.unlock();
                log.info("[{}]释放锁tagCacheUpdateTask", port);
            } else {
                log.info("[{}]本次不做tagCacheUpdateTask", port);
            }
        }
    }

    private void doTask() {
        log.info("[{}]开始tagCacheUpdateTask", port);
        tagService.saveTagCache();
        tagService.blogGroupByTag();
        log.info("[{}]结束tagCacheUpdateTask", port);
    }
}
