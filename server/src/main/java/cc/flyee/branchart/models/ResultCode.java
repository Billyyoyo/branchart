package cc.flyee.branchart.models;

public enum ResultCode {
    SUCCESS(0, "Success"),
    ERROR_UNKNOWN(-1, "unknown"),
    ERROR_INTERNAL(10000, "internal"),
    ERROR_SIGNITURE(10001, "signiture"),
    ERROR_NO_LOGIN(10002, "no_login"),
    ERROR_LOGIN_NAME(10003, "login_name"),
    ERROR_PASSWORD(10004, "password"),
    ERROR_NO_UUID(10005, "no_uuid"),
    ERROR_NO_SIGN(10006, "no_sign"),
    ERROR_FORBID(10007, "forbid"),
    ERROR_NO_PERMISSION(10008, "no_permission"),
    ERROR_ACCOUNT_EXIST(10009, "account_exist"),
    ERROR_ACCOUNT_NOT_EXIST(10010, "account_not_exist"),
    ERROR_LOGINNAME_NULL(10011, "loginname_null"),
    ERROR_PASSWORD_NULL(10012, "password_null"),
    ERROR_ARTICLE_NOT_EXIST(10013, "article_not_exist"),
    ;

    private int code;
    private String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
