package studio.xinge.xinblog.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/7/5
 * @Description
 */
@Configuration
public class BlogThreadPool {

    private int cpuNum = Runtime.getRuntime().availableProcessors();

    @Value("${pool.queue.size}")
    private int queueSize;

    @Bean
    public ExecutorService threadPool() {
        return new ThreadPoolExecutor(cpuNum, cpuNum * 2, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueSize), new ThreadPoolExecutor.CallerRunsPolicy());
    }
}