package studio.xinge.xinblog.auth.security;

import cn.hutool.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import studio.xinge.xinblog.common.utils.R;
import studio.xinge.xinblog.common.utils.ResponseUtil;
import studio.xinge.xinblog.common.utils.ReturnCode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败
 *
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/5/2
 * @Description
 */
@Component
@Slf4j
public class LoginFailHandler implements AuthenticationFailureHandler {

    /**
     * 根据异常类型，返回登录失败信息
     *
     * @param request
     * @param response
     * @param e
     * @Author xinge
     * @Description
     * @Date 2022/5/2
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        response.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
        String username = request.getParameter("username");
        if (e instanceof AccountExpiredException) {
            // 账号过期
            log.info("[登录失败] - 用户[{}]账号过期", username);
            ResponseUtil.out(response, R.error(ReturnCode.USER_ACCOUNT_EXPIRED));

        } else if (e instanceof BadCredentialsException) {
            // 密码错误
            log.info("[登录失败] - 用户[{}]密码错误", username);
            ResponseUtil.out(response, R.error(ReturnCode.USER_PASSWORD_ERROR));

        } else if (e instanceof CredentialsExpiredException) {
            // 密码过期
            log.info("[登录失败] - 用户[{}]密码过期", username);
            ResponseUtil.out(response, R.error(ReturnCode.USER_PASSWORD_EXPIRED));

        } else if (e instanceof DisabledException) {
            // 用户被禁用
            log.info("[登录失败] - 用户[{}]被禁用", username);
            ResponseUtil.out(response, R.error(ReturnCode.USER_DISABLED));

        } else if (e instanceof LockedException) {
            // 用户被锁定
            log.info("[登录失败] - 用户[{}]被锁定", username);
            ResponseUtil.out(response, R.error(ReturnCode.USER_LOCKED));

        } else if (e instanceof InternalAuthenticationServiceException) {
            // 内部错误
            log.error(String.format("[登录失败] - [%s]内部错误", username), e);
            ResponseUtil.out(response, R.error(ReturnCode.USER_LOGIN_FAIL));

        } else {
            // 其他错误
            log.error(String.format("[登录失败] - [%s]其他错误", username), e);
            ResponseUtil.out(response, R.error(ReturnCode.USER_LOGIN_FAIL));
        }
    }
}
