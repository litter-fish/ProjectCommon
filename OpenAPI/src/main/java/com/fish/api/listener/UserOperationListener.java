/**
 * @project h5web
 *
 * @copy 深圳市华康全景信息技术有限公司
 * 
 * @time  2016年10月9日 上午11:05:52
 * 
 */
package com.fish.api.listener;

import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by fish on 2017/5/13.
 */
@Component
public class UserOperationListener extends Thread {

    static final Logger LOGGER = LoggerFactory.getLogger(UserOperationListener.class);

    @Resource(name = "requestParamQueue")
    private LinkedBlockingQueue<JSONObject> requestParamQueue;

    @Override
    public void run() {
        while (true) {
            try {
                JSONObject jsonObj = requestParamQueue.take();

                String json = jsonObj == null ? null : jsonObj.toJSONString();

                LOGGER.info("json:{}", json);
            } catch (Exception e) {
                LOGGER.error("输出用户操作日志异常!", e);
            }
        }
    }

    @PostConstruct
    @Override
    public synchronized void start() {
        super.start();

        setName("用户操作日志监听线程");
        LOGGER.debug("UserOperationListener start success!");
    }
}