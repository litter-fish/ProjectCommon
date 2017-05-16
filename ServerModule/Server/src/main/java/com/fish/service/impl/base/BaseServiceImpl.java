package com.fish.service.impl.base;

import com.fish.dao.CommonDao;
import com.fish.service.base.IBaseService;
import com.fish.util.UidHelper;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 服务基类
 * @param <T>
 * @param <PK>
 */
public class BaseServiceImpl<T,PK extends Serializable> extends CommonDao implements IBaseService<T,PK> {

	/**对应的xml namespace*/
	protected String xmlNameSpace;
	/**对应的实体类*/
	protected Class<T> entityClass;


	public BaseServiceImpl() {
		Class<?> c = getClass();
		Type type = c.getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
			this.entityClass = (Class<T>) parameterizedType[0];
			xmlNameSpace = String.format("%s", entityClass.getCanonicalName());
		}
	}


	@Override
	public T findById(PK id) {
		return commonReadDao.queryOne(formatSqlName("selectByPrimaryKey"), id);
	}

	@Override
	public T findByMultipleId(String[] culms, Object[] values) {

		Map<String, Object> param = Maps.newHashMap();
		for (int offset = 0; offset < culms.length; offset++) {
			param.put(culms[offset], values[offset]);
		}
		return commonReadDao.queryOne(formatSqlName("selectByMultiplePrimaryKey"), param);
	}

	@Override
	public int save(T t) {
		return commonWriteDao.insertAndReturnAffectedCount(formatSqlName("insertSelective"), t);
	}

	@Override
	public int editByPK(T t) {
		return commonWriteDao.updateByObj(formatSqlName("updateByMultiplePrimaryKeySelective"), t);
	}


	@Override
	public String generatePK() {
		return buildPK();
	}

	protected String buildPK() {
		return UidHelper.getInstance().generate();
	}


	/**
	 * 返回  sqlNameWithNameSpace
	 * @param sqlName
	 * @return
	 */
	protected String formatSqlName(String sqlName){
		return String.format("%s.%s", xmlNameSpace, sqlName);
	}
}
