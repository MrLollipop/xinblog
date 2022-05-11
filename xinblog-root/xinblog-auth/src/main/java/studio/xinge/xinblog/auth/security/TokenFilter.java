package studio.xinge.xinblog.auth.security;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import studio.xinge.xinblog.auth.util.TokenManager;
import studio.xinge.xinblog.common.utils.Constant;
import studio.xinge.xinblog.common.utils.R;
import studio.xinge.xinblog.common.utils.ResponseUtil;
import studio.xinge.xinblog.common.utils.ReturnCode;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 请求拦截，判断token是否有效
 *
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/5/3
 * @Description
 */
@Component
@Slf4j
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private HashOperations hashOperations;

    /**
     * 每次请求判断token是否有效，是否拦截
     *
     * 校验逻辑：
     * 1.每次登录后，都会新生成一个token，存redis，并返回客户端。
     * 2.每次请求，客户端header中携带token进行校验。
     * 3.token无效或者为空，未登录状态，交由SpringSecurity校验；自己构造返回码会影响login。
     * 4.判断是否已注销（redis中缓存是否还在），已注销，则未登录状态，走SpringSecurity校验。
     * 5.token是否已过期？redis中是否也过期？token过期直接返回，redis中过期，需要注销用户，再返回。
     * 6./logout注销接口，清除缓存数据。
     *
     * 校验结果返回：
     * 1.FilterChain.doFilter由SpringSecurity校验
     * 有auth，有权限，正常放行
     * 有auth，无权限，返回未授权，AccessDeniedHandler处理
     * 无auth，返回未登录，AuthenticationEntryPoint处理
     * 2.自己构造返回码，通过Response对象返回
     *
     * @param request
     * @param response
     * @param filterChain
     * @Author xinge
     * @Description
     * @Date 2022/5/3
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("token");
        String mobile = "";
        String encryptPassword = "";
        Collection<GrantedAuthority> authorities = new ArrayList<>();

//      token为空或者无效，直接校验（不能直接返回，会影响login）；引导客户端登录取token
        if (StrUtil.isNotBlank(token) && tokenManager.isValid(token)) {
            mobile = tokenManager.getUsernameFromToken(token);
            LoginUser user = (LoginUser) hashOperations.get(Constant.TOKEN_KEY, mobile);
            String redis_token = "";
//            判断用户登录状态，登录则获取用户token；没登录，走校验返回。
            if (null != user) {
//                用户还在登录状态
                redis_token = user.getToken();
            } else {
//                用户已注销
                filterChain.doFilter(request, response);
                return;
            }

//          判断token是否过期
            if (tokenManager.isExpired(token)) {
                // 客户端token过期且redis中token同样过期，删除(注销)，直接返回；客户端重新登录获取token
                if (redis_token.equals(token)) {
                    hashOperations.delete(Constant.TOKEN_KEY, mobile);
                    ResponseUtil.out(response, R.error(ReturnCode.EXPIRED_TOKEN));
                    log.info("redis与客户端的token均已过期，已删除redis中token");
                    return;
                }
                // 客户端token过期但redis中token未过期，只直接返回，不走校验，不删redis中token
                ResponseUtil.out(response, R.error(ReturnCode.EXPIRED_TOKEN));
                log.info("客户端token已过期");
                return;
            }

//            获取登录时的用户权限及加密密码
            LoginUser loginUser = (LoginUser) hashOperations.get(Constant.TOKEN_KEY, mobile);
            if (null != loginUser) {
                encryptPassword = loginUser.getPassword();
                Collection<GrantedAuthority> authList = loginUser.getAuthorities();
                if (!authList.isEmpty()) {
                    authList.forEach(t -> {
                        authorities.add(t);
                    });
                }
            }
        } else {
            filterChain.doFilter(request, response);
            log.info("token无效或为空");
            return;
        }

//      构造auth上下文
        Authentication auth = new UsernamePasswordAuthenticationToken(mobile, encryptPassword, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);

    }
}
