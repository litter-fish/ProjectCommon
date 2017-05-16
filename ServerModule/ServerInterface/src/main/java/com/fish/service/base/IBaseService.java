package com.fish.service.base;

import java.io.Serializable;

/**
 * 业务层服务接口基类
 */
public interface IBaseService<T,PK extends Serializable> {

	/**
	 * 查询单条记录
	 * @param id
	 * @return
	 */
	T findById(PK id);

	T findByMultipleId(String[] culms, Object[] values);

	/**
	 * 保存
	 * @param t
	 * @return
	 */
	int save(T t);

	/**
	 * 根据主键修改记录
	 * @param t
	 * @return
	 */
	int editByPK(T t);

	/**
	 * 生成主键
	 * @return
	 */
	String generatePK();
}
