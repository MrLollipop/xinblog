package studio.xinge.xinblog.common.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月27日 下午9:59:27
 */

public class R extends HashMap<String, Object> implements Serializable {

    private static final long serialVersionUID = 4312963867922223530L;

    private R() {
    }

    private R(ReturnCode returnCode) {
        this.put("code", returnCode.getCode());
        this.put("msg", returnCode.getMsg());
    }

    public static R error() {
        return error(ReturnCode.SYSTEM_ERROR);
    }

    public static R error(ReturnCode returnCode) {
        return new R(returnCode);
    }

    public static R ok() {
        return new R(ReturnCode.SUCCESS);
    }

    public static R ok(String msg) {
        R r = R.ok();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = R.ok();
        r.putAll(map);
        return r;
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 判断返回码是否相同
     * 若code值相同，返回true
     * 反之，false
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass().equals(R.class)){
            if (((R) o).get("code").equals(this.get("code"))) {
                return true;
            }
        }
        return false;
    }
}
