package studio.xinge.xinblog.blog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/7/2
 * @Description
 */
@Component
@ConfigurationProperties("single")
@Data
public class RedissonSingleConfig {
    //可以用"rediss://"来启用SSL连接
    private String address;
    private String password;
    private int database;
    //    最小保持连接数
    private int connectionMinimumIdleSize;
    //    连接池大小
    private int connectionPoolSize;
    //    限制连接释放时间
    private int idleConnectionTimeout;
    //    连接超时 毫秒
    private int connectTimeout;
    //    命令超时 毫秒
    private int timeout;
    //    命令重试次数
    private int retryAttempts;
    //    命令重试间隔 毫秒
    private int retryInterval;
}


