package studio.xinge.xinblog.blog.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/5/12
 * @Description
 */
@Configuration
public class RedissonConfig {

    @Autowired
    SingleConfig singleConfig;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.setTransportMode(TransportMode.NIO);
//        默认值: 当前处理核数量 * 2
//        config.setThreads(0);
//        默认值: 当前处理核数量 * 2
//        config.setNettyThreads(0);
        config.setCodec(new JsonJacksonCodec());

        SingleServerConfig singleServer = config.useSingleServer();
        singleServer.setAddress(singleConfig.getAddress());
        singleServer.setDatabase(singleConfig.getDatabase());
        singleServer.setPassword(singleConfig.getPassword());
        singleServer.setConnectionMinimumIdleSize(singleConfig.getConnectionMinimumIdleSize());
        singleServer.setConnectionPoolSize(singleConfig.getConnectionPoolSize());
        singleServer.setIdleConnectionTimeout(singleConfig.getIdleConnectionTimeout());
        singleServer.setConnectTimeout(singleConfig.getConnectTimeout());
        singleServer.setTimeout(singleConfig.getTimeout());
        singleServer.setRetryAttempts(singleConfig.getRetryAttempts());
        singleServer.setRetryInterval(singleConfig.getRetryInterval());
        singleServer.setPingConnectionInterval(singleConfig.getPingConnectionInterval());

        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

}

@Component
@ConfigurationProperties("single")
@Data
class SingleConfig {
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
    //    心跳间隔
    private int pingConnectionInterval;

}

