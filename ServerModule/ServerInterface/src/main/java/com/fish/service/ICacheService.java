package com.fish.service;

import com.fish.entity.mysql.base.Areas;
import com.fish.service.base.IBaseService;

import java.io.Serializable;

public interface ICacheService extends IBaseService<Areas, Serializable> {

	String findCache(String id);
}
