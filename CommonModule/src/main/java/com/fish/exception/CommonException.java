package com.fish.exception;

import com.fish.constants.ResultCode;
import lombok.Getter;


public class CommonException extends RuntimeException {

    private static final long serialVersionUID = -3620827757855210270L;

    protected Throwable rootcause;

    protected Object[] args;

    @Getter
    protected int code;

    public CommonException(int code, String message) {
        super(message);
        this.code = code;
    }

    public CommonException(ResultCode resultCode) {
        super(resultCode.getDesc());
        this.code = resultCode.getVal();
    }

    public CommonException(ResultCode resultCode, String msg) {
        super(resultCode.getDesc() + " " + msg == null ? "" : msg);
        this.code = resultCode.getVal();
    }

    public CommonException(Throwable cause) {
        rootcause = cause;
    }

    public CommonException(String message, Throwable cause) {
        super(message);
        rootcause = cause;
    }

    public CommonException(String message, Throwable cause, Object... args) {
        super(message);
        rootcause = cause;
        this.args = args;
    }

    public String msg() {
        if (rootcause == null)
            return super.getMessage();
        else
            return super.getMessage() + "; cause exception is: \n\t" + rootcause.toString();
    }
}