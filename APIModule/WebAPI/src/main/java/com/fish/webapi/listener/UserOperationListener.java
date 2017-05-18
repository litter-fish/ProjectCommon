/**
 * @project h5web
 *
 * @copy 深圳市华康全景信息技术有限公司
 * 
 * @time  2016年10月9日 上午11:05:52
 * 
 */
package com.fish.webapi.listener;

import com.fish.listener.CommonOperationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by fish on 2017/5/13.
 */
@Component
public class UserOperationListener extends CommonOperationListener {

    static final Logger LOGGER = LoggerFactory.getLogger(UserOperationListener.class);

    @PostConstruct
    @Override
    public synchronized void start() {
        super.start();

        setName("用户操作日志监听线程");
        LOGGER.debug("UserOperationListener start success!");
    }
}