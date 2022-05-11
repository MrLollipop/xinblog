package studio.xinge.xinblog.auth.security;

import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.stereotype.Component;
import studio.xinge.xinblog.common.utils.R;
import studio.xinge.xinblog.common.utils.ResponseUtil;
import studio.xinge.xinblog.common.utils.ReturnCode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * session过期拦截
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/5/2
 * @Description
 */
@Component
public class SessionTimeoutHandler implements InvalidSessionStrategy {
    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ResponseUtil.out(response, R.error(ReturnCode.USER_SESSION_TIMEOUT));
    }
}
