package studio.xinge.xinblog.auth.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;

import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/4/29
 * @Description
 */
@Component
public class TokenManager {

    @Value("${token.expried.days}")
    private int expiredDays = 1;
    private String tokenSignKey = "xinblog";

    /**
     * 生产token
     *
     * @param username
     * @return String
     * @Author xinge
     * @Description
     * @Date 2022/4/29
     */
    public String createToken(String username) {

        Map<String, Object> map = new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;

            {
                put("username", username);
                put(JWTPayload.EXPIRES_AT, DateUtil.offsetDay(new Date(), expiredDays).toJdkDate());
            }
        };

        return JWTUtil.createToken(map, tokenSignKey.getBytes());

    }

    /**
     * 从token中得到用户信息
     *
     * @param token
     * @return String
     * @Author xinge
     * @Description
     * @Date 2022/4/29
     */
    public String getUsernameFromToken(String token) {
        return (String) JWTUtil.parseToken(token).getPayload("username");
    }

    /**
     * 解析token是否合法
     * 格式不对，不合法
     * 算法不对，不合法
     *
     * @param token
     * @return boolean
     * @Author xinge
     * @Description
     * @Date 2022/5/3
     */
    public boolean isValid(String token) {
        try {
            JWTValidator.of(token).validateAlgorithm(JWTSignerUtil.hs256(tokenSignKey.getBytes()));
        } catch (ValidateException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 验证token是否过期
     * 当前时间大于exp即过期
     *
     * @param token
     * @return boolean
     * @Author xinge
     * @Description
     * @Date 2022/5/3
     */
    public boolean isExpired(String token) {
        try {
            JWTValidator.of(token).validateDate(new Date());
        } catch (ValidateException e) {
            return true;
        }
        return false;

    }

    /**
     * 移除token
     * 无需处理，客户端直接删除token即可
     *
     * @param token
     * @Author xinge
     * @Description
     * @Date 2022/4/29
     */
    public void removeToken(String token) {

    }
}
