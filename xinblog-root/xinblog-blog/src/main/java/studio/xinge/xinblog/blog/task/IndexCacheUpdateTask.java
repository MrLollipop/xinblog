package studio.xinge.xinblog.blog.task;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import studio.xinge.xinblog.blog.service.IndexService;

import java.util.concurrent.TimeUnit;

/**
 * 首页数据定时任务
 * 1.分布式锁控制单个微服务执行
 *
 * @Author xinge
 * @Description
 * @Date 2022/7/5
 */
@Component
@Slf4j
public class IndexCacheUpdateTask {

    @Value("${server.port}")
    private String port;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private IndexService indexService;

    @Scheduled(cron = "0 0/10 * * * ? ")
    public void trigger() throws InterruptedException {
        RLock lock = redissonClient.getLock("indexCacheUpdateTask");
        boolean tryLock = lock.tryLock(2, 1000, TimeUnit.MILLISECONDS);
        try {
            if (tryLock) {
                doTask();
            }
        } finally {
            if (tryLock) {
                if (lock.isLocked()) {
                    lock.unlock();
                    log.info("[{}]释放锁indexCacheUpdateTask", port);
                } else {
                    log.info("[{}]锁indexCacheUpdateTask已自动释放", port);
                }
            } else {
                log.info("[{}]本次不做indexCacheUpdateTask", port);
            }
        }
    }

    private void doTask() {
        log.info("[{}]开始indexCacheUpdateTask", port);
        indexService.getData();
        log.info("[{}]完成indexCacheUpdateTask", port);
    }
}
