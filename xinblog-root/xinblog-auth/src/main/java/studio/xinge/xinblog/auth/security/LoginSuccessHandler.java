package studio.xinge.xinblog.auth.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import studio.xinge.xinblog.auth.util.TokenManager;
import studio.xinge.xinblog.common.utils.Constant;
import studio.xinge.xinblog.common.utils.R;
import studio.xinge.xinblog.common.utils.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 登录成功，生成token并返回前端
 *
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/5/2
 * @Description
 */
@Component
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private HashOperations hashOperations;

    @Value("${redis.token.expired.days}")
    private Long redisTokenExpiredDays = 7L;

    /**
     * 登录成功，生成token并返回前端
     *
     * @param request
     * @param response
     * @param auth
     * @Author xinge
     * @Description
     * @Date 2022/5/2
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
        log.info("login success.");
//        认证成功，得到用户信息
        LoginUser loginUser = (LoginUser) auth.getPrincipal();
        String username = loginUser.getUsername();
//        根据用户名生成token
        String token = tokenManager.createToken(username);
        log.info("create token:{}, by:{}", token, username);
        loginUser.setToken(token);
//        放入redis用户信息
        hashOperations.put(Constant.TOKEN_KEY, username, loginUser);
//        返回token
        ResponseUtil.out(response, R.ok("登录成功").put("token", token));
    }
}
