package studio.xinge.xinblog.common.utils;

import cn.hutool.json.JSONUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/4/29
 * @Description 用HttpServletResponse对象返回JSON数据
 */
public class ResponseUtil {

    public static void out (HttpServletResponse response, Object data) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.parse(data));
        response.getWriter().flush();
    }
}
