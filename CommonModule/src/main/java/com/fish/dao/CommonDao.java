/**  
 * ---------------------------------------------------------------------------
 * Copyright (c) 2016, 深圳市华康全景信息技术有限公司- All Rights Reserved.
 * Project Name:internet-hospital-dao  
 * File Name:CommonDao.java  
 * Package Name:com.hk.internet.hospital.dao.base
 * Author   chenzhiyu
 * Date:2016年11月28日下午5:40:56
 * ---------------------------------------------------------------------------  
*/  
  
package com.fish.dao;

import lombok.Getter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;


/**
 * 注：所有的dao都使用同一数据源
 */
@Repository
@Getter
public class CommonDao {

	@Resource(name = "commonReadDao")
	protected GenericDao commonReadDao;
	@Resource(name = "commonWriteDao")
	protected GenericDao commonWriteDao;

	@Resource(name = "commonWriteTransactionTemplate")
	protected TransactionTemplate orderWriteTransactionTemplate;
}
  
