package studio.xinge.xinblog.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2021/12/9
 * @Description
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ThirdPartyApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThirdPartyApplication.class);
    }
}
