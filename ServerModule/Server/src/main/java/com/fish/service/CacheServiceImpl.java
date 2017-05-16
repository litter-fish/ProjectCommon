package com.fish.service;

import com.fish.entity.mysql.base.Areas;
import com.fish.service.impl.base.BaseServiceImpl;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class CacheServiceImpl extends BaseServiceImpl<Areas, Serializable> implements ICacheService {

	private final AtomicInteger i = new AtomicInteger();

	@Override
	public String findCache(String id) {

		return "request: " + id + ", response: " + i.getAndIncrement();
	}



}
