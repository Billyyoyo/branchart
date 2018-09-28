package cc.flyee.branchart.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultEntry implements Serializable {

    private int code;
    private String msg;
    private Object data;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResultEntry(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultEntry(int code, String msg, Object data) {
        this(code, msg);
        this.data = data;
    }

    public ResultEntry(ResultCode code) {
        this(code.getCode(), code.getMsg());
    }

    public ResultEntry(ResultCode code, Object data) {
        this(code.getCode(), code.getMsg());
        this.data = data;
    }

}
