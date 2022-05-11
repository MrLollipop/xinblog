package studio.xinge.xinblog.auth.security;

import cn.hutool.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import studio.xinge.xinblog.common.utils.R;
import studio.xinge.xinblog.common.utils.ResponseUtil;
import studio.xinge.xinblog.common.utils.ReturnCode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截未登录的请求
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/4/29
 * @Description
 */
@Component
@Slf4j
public class UnloginHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("用户未登录");
        response.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
        ResponseUtil.out(response, R.error(ReturnCode.USER_UNLOGIN));
    }
}
