package com.fish.controller;

import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.fastjson.JSONObject;
import com.fish.bo.MaResult;
import com.fish.constants.ResponseType;
import com.fish.constants.ResultCode;
import com.fish.exception.CommonException;
import com.fish.util.SystemContent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller层基础类
 */
public abstract class CommonController {

    final static Logger LOGGER = LoggerFactory.getLogger(CommonController.class);

    protected Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 请求对象
     */
    protected HttpServletRequest request;

    /**
     * 响应对象
     */
    protected HttpServletResponse response;
    
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    /**
     * 处理异常
     */
    @SuppressWarnings("unchecked")
	@ResponseBody
    @ExceptionHandler(Exception.class)
    public MaResult<JSONObject> handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        try {
            //获取session中存放的响应类型
            Object obj = request.getAttribute(SystemContent.FS_ATTR_SESSION_RES_TYPE);

            ResponseType responseType = null == obj || !(obj instanceof ResponseType) ? ResponseType.JSON : (ResponseType) obj;
            if (e instanceof CommonException) {
                CommonException ex = (CommonException) e;
                return new MaResult<>(ex.getCode(), ex.getMessage());
            }else if(e instanceof RpcException) {
                return MaResult.error(logger, ResultCode.DUBBO_SERVICE_ERROR, e);
            }
            // JSON
            if (responseType == ResponseType.JSON) {
                return MaResult.error(logger, e);
            }
            String servletPath = request.getServletPath();
            servletPath = StringUtils.isBlank(servletPath) ? request.getRequestURI() : servletPath;

            logger.error("服务地址:{}异常!", servletPath, e);

            // 页面跳转
            request.setAttribute("ex", getExceptionAllInformation(e));
            request.getRequestDispatcher("/error/error").forward(request, response);
            return null;
        } catch (Exception e1) {
            LOGGER.error("处理异常失败!", e1);
        }
        return MaResult.error(ResultCode.ERROR);
    }

    /**
     * 获取详细的异常信息
     *
     * @param ex
     * @return
     */
    protected String getExceptionAllInformation(Exception ex) {
        String sOut = "";
        StackTraceElement[] trace = ex.getStackTrace();
        for (StackTraceElement s : trace) {
            sOut += "&nbsp;&nbsp;&nbsp;&nbsp;" + s + "<br>";
        }
        return sOut;
    }

    /**
     * 获取ip地址
     * getIpAddr:(这里用一句话描述这个方法的作用).
     *
     * @param request
     */
    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("PRoxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.isNotEmpty(ip)) {
            ip = ip.trim();
        }
        return ip;
    }
}
  
