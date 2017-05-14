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
public class MaResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = ResultCode.SUCCESS.getVal();
    private T data;

    private long currentTimeMillis = System.currentTimeMillis();

    public MaResult() {
    }

    public MaResult(String msg) {
        this.code = ResultCode.ERROR.getVal();
        this.msg = msg;
    }

    public MaResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public MaResult(ResultCode resultCode) {
        this.code = resultCode.getVal();
        this.msg = resultCode.getDesc();
    }

    public MaResult(T data) {
        this.data = data;
        this.code = ResultCode.SUCCESS.getVal();
    }

    public MaResult setResultCode(ResultCode resultCode) {
        this.code = resultCode.getVal();
        this.msg = resultCode.getDesc();
        this.data = null;
        return this;
    }

    public MaResult setMsg(String msg) {
        if (StringUtils.isEmpty(msg)) return this;
        this.msg = msg;
        return this;
    }

    public MaResult setErrorMsg(String msg) {
        if (StringUtils.isEmpty(msg)) return this;
        this.code = ResultCode.ERROR.getVal();
        this.msg = msg;
        this.data = null;
        return this;
    }

    public static MaResult<JSONObject> init() {
        return new MaResult<JSONObject>(ResultCode.SUCCESS);
    }

    public static <T> MaResult<T> init(T data) {
        return new MaResult<T>(data);
    }

    /**数据校验信息返回*/
    public static MaResult<JSONObject> invalid(ResultCode resultCode){
        return new MaResult<JSONObject>(resultCode);
    }


    public static MaResult error(ResultCode resultCode) {
        return new MaResult(resultCode);
    }

    public static MaResult error(Logger logger, Throwable e) {
        if (logger != null) {
            logger.error("服务异常", e);
        }
        return new MaResult(ResultCode.ERROR);
    }

    public static MaResult error(Logger logger, ResultCode rc, Throwable e) {
        if (logger != null) {
            logger.error("服务异常", e);
        }
        return new MaResult(rc == null ? ResultCode.ERROR : rc);
    }

    public static MaResult error(Logger logger, String msg, Object... obj) {
        if (logger != null) {
            logger.error(msg, obj);
        }
        return new MaResult(msg);
    }

    public static MaResult error(Logger logger, Throwable e, String msg, Object... obj) {
        if (logger != null) {
            logger.error(msg, obj, e);
        }
        return new MaResult(msg);
    }

    public boolean fail() {
        return !success();
    }
    public boolean success() {
        return this.getCode() == ResultCode.SUCCESS.getVal();
    }
}