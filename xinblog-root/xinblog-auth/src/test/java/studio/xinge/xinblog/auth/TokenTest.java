package studio.xinge.xinblog.auth;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import studio.xinge.xinblog.auth.security.LoginUser;
import studio.xinge.xinblog.auth.util.TokenManager;
import studio.xinge.xinblog.common.utils.Constant;

import java.util.concurrent.TimeUnit;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/5/3
 * @Description
 */
@SpringBootTest
@Slf4j
public class TokenTest {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private HashOperations hashOperations;

    @Test
    public void createToken() {
        String token = tokenManager.createToken("15605811685");
        log.info(token);
    }

    @Test
    public void validate() {
        String token = tokenManager.createToken("15600001111");
        log.info(token);
        System.out.println(tokenManager.isValid("token"));
    }

    @Test
    public void expired() throws InterruptedException {
        String token = tokenManager.createToken("15600001111");
        log.info(token);
        TimeUnit.SECONDS.sleep(1);
        log.info("begin to compare");
        System.out.println(tokenManager.isExpired(token));
    }

    @Test
    public void getRedisToken() {
        LoginUser loginUser = (LoginUser) hashOperations.get(Constant.TOKEN_KEY, "15600001000");
        hashOperations.delete(Constant.TOKEN_KEY, "15600001000");
        if (loginUser!=null) {
            System.out.println(loginUser);
        } else {
            System.out.println("user is null");
        }

    }
}
