package com.fish.bo;

import com.alibaba.fastjson.JSONObject;
import com.fish.constants.ResultCode;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.Serializable;

/**
 * Created by fish on 2017/5/14.
 */
@SuppressWarnings("rawtypes")
@Data
public class CommonResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = ResultCode.SUCCESS.getVal();
    private T data;

    private long currentTimeMillis = System.currentTimeMillis();

    public CommonResult() {
    }

    public CommonResult(String msg) {
        this.code = ResultCode.ERROR.getVal();
        this.msg = msg;
    }

    public CommonResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CommonResult(ResultCode resultCode) {
        this.code = resultCode.getVal();
        this.msg = resultCode.getDesc();
    }

    public CommonResult(T data) {
        this.data = data;
        this.code = ResultCode.SUCCESS.getVal();
    }

    public CommonResult setResultCode(ResultCode resultCode) {
        this.code = resultCode.getVal();
        this.msg = resultCode.getDesc();
        this.data = null;
        return this;
    }

    public CommonResult setMsg(String msg) {
        if (StringUtils.isEmpty(msg)) return this;
        this.msg = msg;
        return this;
    }

    public CommonResult setErrorMsg(String msg) {
        if (StringUtils.isEmpty(msg)) return this;
        this.code = ResultCode.ERROR.getVal();
        this.msg = msg;
        this.data = null;
        return this;
    }

    public static CommonResult<JSONObject> init() {
        return new CommonResult<JSONObject>(ResultCode.SUCCESS);
    }

    public static <T> CommonResult<T> init(T data) {
        return new CommonResult<T>(data);
    }

    /**数据校验信息返回*/
    public static CommonResult<JSONObject> invalid(ResultCode resultCode){
        return new CommonResult<JSONObject>(resultCode);
    }


    public static CommonResult error(ResultCode resultCode) {
        return new CommonResult(resultCode);
    }

    public static CommonResult error(Logger logger, Throwable e) {
        if (logger != null) {
            logger.error("服务异常", e);
        }
        return new CommonResult(ResultCode.ERROR);
    }

    public static CommonResult error(Logger logger, ResultCode rc, Throwable e) {
        if (logger != null) {
            logger.error("服务异常", e);
        }
        return new CommonResult(rc == null ? ResultCode.ERROR : rc);
    }

    public static CommonResult error(Logger logger, String msg, Object... obj) {
        if (logger != null) {
            logger.error(msg, obj);
        }
        return new CommonResult(msg);
    }

    public static CommonResult error(Logger logger, Throwable e, String msg, Object... obj) {
        if (logger != null) {
            logger.error(msg, obj, e);
        }
        return new CommonResult(msg);
    }

    public boolean fail() {
        return !success();
    }
    public boolean success() {
        return this.getCode() == ResultCode.SUCCESS.getVal();
    }
}