package studio.xinge.xinblog.gateway.config;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import java.util.List;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2021/9/17
 * @Description 跨域处理
 */
@Configuration
public class CorsConfig {

    @Value("${allowedOrigin}")
    private String allowedOriginStr;

    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
//        corsConfiguration.addAllowedOrigin("*");
        List<String> allowedOriginList = StrUtil.split(allowedOriginStr, ",");
        corsConfiguration.setAllowedOrigins(allowedOriginList);
        corsConfiguration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", corsConfiguration);

        return  new CorsWebFilter(source);
    }
}
