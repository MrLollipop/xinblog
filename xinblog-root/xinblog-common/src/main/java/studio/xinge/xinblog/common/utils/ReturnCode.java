package studio.xinge.xinblog.common.utils;

public enum ReturnCode {

    SUCCESS(10000, "SUCCESS!"),
    UNKNOW_ERROR(20000, "未知错误，请联系管理员"),
    BATCHINSERT_ERROR(30001, "批量插入错误")
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
