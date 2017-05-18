package com.fish.listener;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 */
public class CommonOperationListener extends Thread {
	static final Logger LOGGER = LoggerFactory.getLogger(CommonOperationListener.class);

	@Resource
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

	@Override
	public synchronized void start() {
		super.start();

		setName("用户操作日志监听线程");
		LOGGER.debug("UserOperationListener start success!");
	}
}
