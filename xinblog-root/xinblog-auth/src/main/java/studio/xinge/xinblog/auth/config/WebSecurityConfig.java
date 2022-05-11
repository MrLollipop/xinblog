package studio.xinge.xinblog.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import studio.xinge.xinblog.auth.security.*;
import javax.annotation.Resource;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/4/30
 * @Description
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailHandler loginFailHandler;

    @Autowired
    private LogoutHandler logoutHandler;

    @Autowired
    private SameUserMeanwhileLimit sameUserMeanwhileLimit;

    @Autowired
    private SessionTimeoutHandler sessionTimeoutHandler;

    @Autowired
    private UnloginHandler unloginHandler;

    @Autowired
    private UnauthHandler unauthHandler;

    @Autowired
    private TokenFilter tokenFilter;

    @Autowired
    private UserDetailsService userDetailsService;


    /**
     * 注入UserDetailService与加密器
     *
     * @param auth
     * @Author xinge
     * @Description
     * @Date 2022/4/30
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * 未授权用户退出路径
     *
     * @param http
     * @Author xinge
     * @Description
     * @Date 2022/4/30
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.headers().frameOptions().disable();

//        禁用跨站伪造
        http.csrf().disable();

        //使用jwt的Authentication,来解析过来的请求是否为有效token
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling()
                //匿名访问,没有登录的处理类
                .authenticationEntryPoint(unloginHandler)
                //登录后,访问没有权限处理类
                .accessDeniedHandler(unauthHandler);

        http.authorizeRequests()
                //这里表示"/any"和"/ignore"不需要权限校验
                /**
                 * antMatchers: ant的通配符规则
                 * ?	匹配任何单字符
                 * *	匹配0或者任意数量的字符，不包含"/"
                 * **	匹配0或者更多的目录，包含"/"
                 */
                .antMatchers("/ignore/**", "/login").permitAll()
                // 这里表示任何请求都需要校验认证(上面配置的放行)
                .anyRequest().authenticated();

        //配置登录,检测到用户未登录时跳转的url地址,登录放行
        http.formLogin()
                //需要跟前端表单的action地址一致
                .loginProcessingUrl("/login")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailHandler)
                .permitAll();

        //配置登出,登出放行,默认logout
        http.logout()
                .logoutSuccessHandler(logoutHandler)
                .permitAll();

        //配置取消session管理,又Jwt来获取用户状态,否则即使token无效,也会有session信息,依旧判断用户为登录状态
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                .invalidSessionStrategy(sessionTimeoutHandler) // 超时处理
//                .maximumSessions(1)//同一账号同时登录最大用户数
//                .expiredSessionStrategy(sameUserMeanwhileLimit); // 顶号处理

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
