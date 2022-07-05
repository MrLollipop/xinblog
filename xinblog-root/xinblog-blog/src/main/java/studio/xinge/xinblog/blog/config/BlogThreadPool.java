package studio.xinge.xinblog.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/7/5
 * @Description
 */
@Component
public class BlogThreadPool {

    private int cpuNum = Runtime.getRuntime().availableProcessors();

    @Value("${pool.queue.size}")
    private int queueSize;

    private ExecutorService executorService;

    public ExecutorService getPool() {
        if (null == executorService) {
            executorService = new ThreadPoolExecutor(cpuNum, cpuNum * 2, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueSize), new ThreadPoolExecutor.CallerRunsPolicy());
        }
        return executorService;
    }
}