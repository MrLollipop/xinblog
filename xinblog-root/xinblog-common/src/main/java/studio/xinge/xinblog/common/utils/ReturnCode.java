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
    BATCHINSERT_ERROR(11001, "批量插入错误"),
    UPDATE_FAIL(11002, "批量更新失败"),
    DELETE_FAIL(11003, "批量删除失败")
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
