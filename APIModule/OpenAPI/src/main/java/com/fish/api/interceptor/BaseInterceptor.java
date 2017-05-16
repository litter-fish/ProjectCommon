package com.fish.api.interceptor;

import com.alibaba.dubbo.rpc.RpcContext;
import com.fish.constants.ResultCode;
import com.fish.interceptor.CommonInterceptor;
import com.fish.util.JedisPoolTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by fish on 2017/5/13.
 */
public class BaseInterceptor extends CommonInterceptor {

    @Resource
    private JedisPoolTemplate jedisPoolTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RpcContext.getContext().setAttachment("bizType", "10");

        if (!isNeedAuth(request, handler, jedisPoolTemplate)) return super.preHandle(request, response, handler);

        if (!isAutoLogin(request, jedisPoolTemplate)) return writer(response, ResultCode.INVALID_WECHAT_SEESION.getJson());

        return super.preHandle(request, response, handler);
    }
}
