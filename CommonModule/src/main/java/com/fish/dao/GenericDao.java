package com.fish.dao;

import org.apache.ibatis.executor.BatchResult;

import java.util.List;
import java.util.Map;

public interface GenericDao {

	long generateSequence(String sqlNameWithNameSpace);

	<T> int insertAndReturnAffectedCount(String sqlNameWithNameSpace, T obj);

	<T> int insertAndSetupId(String sqlNameWithNameSpace, T obj);

	int update(String sqlNameWithNameSpace, Map<String, Object> param);
	
	int delete(String sqlNameWithNameSpace, Object param);

	int updateByObj(String sqlNameWithNameSpace, Object param);

	<T> T queryUniqueById(String statement, long id);

	<T> T queryUniqueById(String statement, String idStr);

	<T> T queryUnique(String sqlNameWithNameSpace, Map<String, Object> map);

	<T> T queryOne(String statement, String idStr, int exceptionExceedCount);

	<T> T queryOne(String sqlNameWithNameSpace, Map<String, Object> map);

	<T> T queryOne(String sqlNameWithNameSpace, Map<String, Object> map, int exceptionExceedCount);

	Object queryObject(String sqlNameWithNameSpace, Map<String, Object> map);

	<T> T queryOneByObject(String sqlNameWithNameSpace, String mapKey, Object mapValue);

	int queryCount(String sqlNameWithNameSpace, Map<String, Object> map);

	int queryInt(String sqlNameWithNameSpace, Map<String, Object> map);

	<T> List<T> queryList(String sqlNameWithNameSpace, Map<String, Object> map, DataPage<T> page);

	<T> DataPage<T> queryPage(String countSqlNameWithNameSpace, String rsSqlNameWithNameSpace, Map<String, Object> map, DataPage<T> page);

	<T> List<T> queryList(String sqlNameWithNameSpace, Map<String, Object> map);

	<T> List<T> queryList(String sqlNameWithNameSpace, Map<String, Object> map, int exceptionExceedCount);

	<K, V> Map<K, V> selectOneToMap(String sqlNameWithNameSpace, Map<K, V> param);

	List<BatchResult> flushStatements();

	/**下面添加在查询时使用object作为入参	 */
	<T> T queryUnique(String sqlNameWithNameSpace, Object param);

	<T> T queryOne(String sqlNameWithNameSpace, Object param);

	<T> T queryOne(String sqlNameWithNameSpace, Object param, int exceptionExceedCount);

	Object queryObject(String sqlNameWithNameSpace, Object param);

	int queryCount(String sqlNameWithNameSpace, Object param);

    int queryInt(String sqlNameWithNameSpace, Object param);

    <T> List<T> queryList(String sqlNameWithNameSpace, Object param, DataPage<T> page);

    <T> DataPage<T> queryPage(String countSqlNameWithNameSpace, String rsSqlNameWithNameSpace, Object param, DataPage<T> page);

    <T> List<T> queryList(String sqlNameWithNameSpace, Object param);

    <T> List<T> queryList(String sqlNameWithNameSpace, Object param, int exceptionExceedCount);

    <K, V> Map<K, V> selectOneToMap(String sqlNameWithNameSpace, Object param);
	
}
