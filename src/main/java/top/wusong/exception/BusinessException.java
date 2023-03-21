package top.wusong.exception;



public class BusinessException extends RuntimeException {
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BusinessException(String msg) {
        this.msg = msg;
    }

    public BusinessException(String message, String msg) {
        super(message);
        this.msg = msg;
    }

    public BusinessException(String message, Throwable cause, String msg) {
        super(message, cause);
        this.msg = msg;
    }

    public BusinessException(Throwable cause, String msg) {
        super(cause);
        this.msg = msg;
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String msg) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.msg = msg;
    }
}
