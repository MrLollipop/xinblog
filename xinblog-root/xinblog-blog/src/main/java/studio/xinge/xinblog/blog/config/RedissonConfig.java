package studio.xinge.xinblog.blog.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/5/12
 * @Description
 */
@Configuration
public class RedissonConfig {

    @Autowired
    RedissonSingleConfig singleConfig;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.setTransportMode(TransportMode.NIO);
//        config.setThreads(0);
//        config.setNettyThreads(0);
        config.setCodec(new JsonJacksonCodec());

        SingleServerConfig singleServer = config.useSingleServer();
        singleServer.setAddress(singleConfig.getAddress());
        singleServer.setDatabase(singleConfig.getDatabase());
//            singleServer.setPassword("123456");
        singleServer.setConnectionMinimumIdleSize(singleConfig.getConnectionMinimumIdleSize());
        singleServer.setConnectionPoolSize(singleConfig.getConnectionPoolSize());
        singleServer.setIdleConnectionTimeout(singleConfig.getIdleConnectionTimeout());
        singleServer.setConnectTimeout(singleConfig.getConnectTimeout());
        singleServer.setTimeout(singleConfig.getTimeout());
        singleServer.setRetryAttempts(singleConfig.getRetryAttempts());
        singleServer.setRetryInterval(singleConfig.getRetryInterval());

        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

}


