package com.fish.aop;

import com.alibaba.fastjson.JSONObject;
import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 用户操作日志切面
 */
@Aspect
public class ControllerAspect {

    final static Logger LOGGER = LoggerFactory.getLogger(ControllerAspect.class);

    @Setter
    protected LinkedBlockingQueue<JSONObject> requestParamQueue;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void httpAspect() {

    }

    @Around("httpAspect()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;

        long start = System.currentTimeMillis();
        Object obj = null;
        try {
            obj = point.proceed(point.getArgs());
            return obj;
        } catch (Throwable throwable) {
            throw throwable;
        } finally {
            long end = System.currentTimeMillis();
            writeLog(request, methodSignature, point.getArgs(), obj, (end - start));
        }
    }

    protected void writeLog(HttpServletRequest request, MethodSignature methodSignature, Object[] args, Object obj, long times) {
        try {
            JSONObject paramObj = buildParams(args, methodSignature);
            String resultJson = buildResults(obj);
            JSONObject jsonObj = new JSONObject();

            jsonObj.put("userId", "");
            jsonObj.put("path", request.getServletPath());
            jsonObj.put("sessionId", request.getSession().getId());
            jsonObj.put("param", paramObj);
            jsonObj.put("rst", resultJson);
            jsonObj.put("times", times);
            boolean offer = requestParamQueue.offer(jsonObj);
            if (!offer) {
                LOGGER.info("requestParamQueue offer result is false!");
            }
        } catch (Exception e) {
            LOGGER.error("write log error!", e);
        }
    }

    protected JSONObject buildParams(Object[] args, MethodSignature methodSignature) {
        String[] parameterNames = methodSignature.getParameterNames();
        JSONObject params = new JSONObject();
        if (args == null || parameterNames == null || args.length == 0) {
            return params;
        }
        for (int i = 0; i < args.length; i++) {
            Object obj = args[i];
            // 请求头跳过
            if (obj == null || !(obj instanceof Serializable) || obj instanceof HttpHeaders || obj instanceof ServletRequest || obj instanceof HttpServletResponse)
                continue;
            params.put(parameterNames[i], obj);
        }
        return params;
    }

	@SuppressWarnings("unchecked")
	protected String buildResults(Object object) {
    	if(object instanceof HttpEntity){
    		HttpEntity<Json> entity = (HttpEntity<Json>)object;
    		System.err.println(entity.getBody().value());
    	    return entity.getBody().value();
    	}
        if (object == null || !(object instanceof Serializable)) return null;
        try {
            return JSONObject.toJSONString(object);
        } catch (Exception e) {
            LOGGER.error("构建响应结果异常!", e);
        }
        return null;
    }
}
