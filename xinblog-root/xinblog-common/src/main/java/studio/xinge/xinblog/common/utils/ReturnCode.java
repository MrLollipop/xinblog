package studio.xinge.xinblog.common.utils;

/**
 *
 * @Author xinge
 * @Description
 * @Date 2021/12/18
 * 格式：10XXX()
 * 10XXX：全局
 * 11XXX：博客微服务
 * 12XXX：管理台
 * 13XXX：第三方
*/
public enum ReturnCode {

    SUCCESS(10000, "SUCCESS!"),
    SYSTEM_ERROR(10001, "系统错误，请联系管理员"),
    PARAM_ERROR(10002, "参数错误！"),
    SYSTEM_BUSY(10003, "系统繁忙，请稍后再试！"),
    OPERATE_TOO_QUICK(10004, "操作太频繁，请稍后再试！"),
    SERVICE_NOT_WORK(10005, "服务不可用，请稍后再试！"),

    USER_UNLOGIN(14001, "未登录用户，无法操作"),
    USER_LOGIN_FAIL(14002, "用户登录失败"),
    USER_ACCOUNT_EXPIRED(14003, "用户账户已过期"),
    USER_PASSWORD_ERROR(14004, "用户密码错误"),
    USER_PASSWORD_EXPIRED(14005, "用户密码过期"),
    USER_DISABLED(14006, "用户账号已禁用"),
    USER_LOCKED(14007, "用户账号已被锁定"),
    USER_UNAUTH(14008, "用户未被授权此操作"),
    USER_SESSION_TIMEOUT(14009, "用户会话已超时"),
    USER_MEANWHILE_MAX_LOGIN(14010, "用户最大同时登录数"),
    NONE_TOKEN(14011, "用户请求未携带TOKEN"),
    INVALID_TOKEN(14012, "用户TOKEN无效，请登录获取"),
    EXPIRED_TOKEN(14013, "用户TOKEN已过期，请重新登录获取"),

    BATCHINSERT_ERROR(11001, "批量插入错误"),
    UPDATE_FAIL(11002, "批量更新失败"),
    DELETE_FAIL(11003, "批量删除失败"),
    BLOG_NOT_EXIST(11004, "此博客不存在"),
    TOP_BOLG_LIMIT(11005, "置顶博客数已达上限")
    ;

    private int code;

    private String msg;

    ReturnCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
