package studio.xinge.xinblog.auth.security;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import studio.xinge.xinblog.auth.util.TokenManager;
import studio.xinge.xinblog.common.utils.Constant;
import studio.xinge.xinblog.common.utils.R;
import studio.xinge.xinblog.common.utils.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注销成功拦截
 *
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/5/2
 * @Description
 */
@Component
@Slf4j
public class LogoutHandler implements LogoutSuccessHandler {

    @Autowired
    private HashOperations hashOperations;

    @Autowired
    private TokenManager tokenManager;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
        // TODO: 2022/5/2 session未使用 auth为null
        //        认证成功，得到用户信息
//        LoginUser loginUser = (LoginUser) auth.getPrincipal();
//        String mobile = loginUser.getUsername();

        String token = request.getHeader("token");
        if (StrUtil.isNotBlank(token)) {
            log.info("删除redis中token");
            String mobile = tokenManager.getUsernameFromToken(token);
            tokenManager.removeToken(token);
            hashOperations.delete(Constant.TOKEN_KEY, mobile);
        }
        ResponseUtil.out(response, R.ok("注销成功"));
    }
}
