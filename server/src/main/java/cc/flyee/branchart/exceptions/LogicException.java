package cc.flyee.branchart.exceptions;

import cc.flyee.branchart.models.ResultCode;

public class LogicException extends RuntimeException {

    private int errorCode;
    private String errorMsg;

    public LogicException(ResultCode error) {
        super(error.getMsg());
        this.errorCode = error.getCode();
        this.errorMsg = error.getMsg();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
