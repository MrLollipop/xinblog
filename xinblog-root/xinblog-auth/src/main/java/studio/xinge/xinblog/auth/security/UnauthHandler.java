package studio.xinge.xinblog.auth.security;

import cn.hutool.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import studio.xinge.xinblog.common.utils.R;
import studio.xinge.xinblog.common.utils.ResponseUtil;
import studio.xinge.xinblog.common.utils.ReturnCode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截未授权操作
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/5/2
 * @Description
 */
@Component
@Slf4j
public class UnauthHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("用户未授权");
        response.setStatus(HttpStatus.HTTP_FORBIDDEN);
        ResponseUtil.out(response, R.error(ReturnCode.USER_UNAUTH));
    }
}
