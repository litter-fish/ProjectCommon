package com.fish.dao;

import com.fish.constants.ResultCode;
import com.fish.exception.CommonException;
import org.apache.ibatis.executor.BatchResult;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository(value = "")
public class GenericMyBatisDao implements GenericDao {

	private Logger log = LoggerFactory.getLogger(getClass());

	private SqlSessionTemplate mybatisTemplate;

	public void setMybatisTemplate(SqlSessionTemplate mybatisTemplate) {
		this.mybatisTemplate = mybatisTemplate;
	}

	@Override
	public long generateSequence(String sqlNameWithNameSpace) {
		return mybatisTemplate.<Long> selectOne(sqlNameWithNameSpace);
	}

	/**
	 * @param sqlNameWithNameSpace
	 * @param obj
	 * @return
	 */
	@Override
	public <T> int insertAndReturnAffectedCount(String sqlNameWithNameSpace, T obj) {
		return mybatisTemplate.insert(sqlNameWithNameSpace, obj);
	}

	/**
	 * @param sqlNameWithNameSpace
	 * @param obj
	 */
	@Override
	public <T> int insertAndSetupId(String sqlNameWithNameSpace, T obj) {
		return mybatisTemplate.insert(sqlNameWithNameSpace, obj);
	}

	@Override
	public int update(String sqlNameWithNameSpace, Map<String, Object> param) {
		return mybatisTemplate.update(sqlNameWithNameSpace, param);
	}

	@Override
	public int delete(String sqlNameWithNameSpace, Object param) {
		return mybatisTemplate.delete(sqlNameWithNameSpace, param);
	}
	@Override
	public int updateByObj(String sqlNameWithNameSpace, Object param) {
		return mybatisTemplate.update(sqlNameWithNameSpace, param);
	}

	@Override
	public <T> T queryOne(String sqlNameWithNameSpace, Map<String, Object> map) {
		return mybatisTemplate.<T> selectOne(sqlNameWithNameSpace, map);
	}

	@Override
	public Object queryObject(String sqlNameWithNameSpace, Map<String, Object> map) {
		return mybatisTemplate.selectOne(sqlNameWithNameSpace, map);
	}

	@Override
	public <T> T queryOne(String sqlNameWithNameSpace, String idStr, int exceptionExceedCount) {
		List<T> list = mybatisTemplate.<T> selectList(sqlNameWithNameSpace, idStr);
		if (list.isEmpty()) {
			return null;
		}
		if (list.size() > exceptionExceedCount) {
			String msg = "{sqlId:'" + sqlNameWithNameSpace
					+ "', msg:'excute mybatis.selectList exceptionExceedCount fail-fast', exceptionExceedCount:"
					+ exceptionExceedCount + "}";
			log.error(msg);
			throw new CommonException(ResultCode.PARAMETER_ERROR, msg);
		} else {
			return list.get(0);
		}
	}

	@Override
	public <T> T queryOne(String sqlNameWithNameSpace, Map<String, Object> map, int exceptionExceedCount) {
		List<T> list = mybatisTemplate.<T> selectList(sqlNameWithNameSpace, map);
		if (list.isEmpty()) {
			return null;
		}
		if (list.size() > exceptionExceedCount) {
			String msg = "{sqlId:'" + sqlNameWithNameSpace
					+ "', msg:'mybatis.selectList exceptionExceedCount fail-fast', exceptionExceedCount:"
					+ exceptionExceedCount + "}";
			log.error(msg);
			throw new CommonException(ResultCode.PARAMETER_ERROR, msg);
		} else {
			return list.get(0);
		}
	}

	@Override
	public <T> T queryOneByObject(String sqlNameWithNameSpace, String mapKey, Object mapValue) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(mapKey, mapValue);
		return mybatisTemplate.selectOne(sqlNameWithNameSpace, paramMap);
	}

	@Override
	public <T> T queryUniqueById(String statement, long id) {
		return mybatisTemplate.selectOne(statement, id);
	}

	@Override
	public <T> T queryUniqueById(String statement, String idStr) {
		return mybatisTemplate.selectOne(statement, idStr);
	}

	@Override
	public <T> T queryUnique(String sqlNameWithNameSpace, Map<String, Object> map) {
		return mybatisTemplate.<T> selectOne(sqlNameWithNameSpace, map);
	}

	/**
	 * select count(*) where xxx
	 * 
	 * @param sqlNameWithNameSpace
	 * @param map
	 * @return
	 */
	@Override
	public int queryCount(String sqlNameWithNameSpace, Map<String, Object> map) {
		return mybatisTemplate.<Integer> selectOne(sqlNameWithNameSpace, map);
	}

	/**
	 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6302954
	 * 
	 * @param sqlNameWithNameSpace
	 * @param map
	 * @return
	 */
	public int queryInt(String sqlNameWithNameSpace, Map<String, Object> map) {
		return mybatisTemplate.<Integer> selectOne(sqlNameWithNameSpace, map);
	}

	/**
	 * select * where
	 * 
	 * @param sqlNameWithNameSpace
	 * @param map
	 * @return
	 */
	@Override
	public <T> List<T> queryList(String sqlNameWithNameSpace, Map<String, Object> map) {
		return mybatisTemplate.selectList(sqlNameWithNameSpace, map);
	}

	@Override
	public <T> List<T> queryList(String sqlNameWithNameSpace, Map<String, Object> map, int exceptionExceedCount) {
		List<T> list = this.queryList(sqlNameWithNameSpace, map);
		if (list.size() > exceptionExceedCount) {
			String msg = "{sqlId:'" + sqlNameWithNameSpace + "', msg:'list fail-fast', exceptionExceedCount:"
					+ exceptionExceedCount + "}";
			log.error(msg);
			throw new CommonException(ResultCode.PARAMETER_ERROR, msg);
		}
		return list;
	}

	@Override
	public <K, V> Map<K, V> selectOneToMap(String sqlNameWithNameSpace, Map<K, V> param) {
		return mybatisTemplate.selectOne(sqlNameWithNameSpace, param);
	}

	@Override
	public <T> DataPage<T> queryPage(String countSqlNameWithNameSpace, String rsSqlNameWithNameSpace,
									 Map<String, Object> paramMap, DataPage<T> page) {
		if (page.isNeedTotalCount()) {
			int count = this.queryCount(countSqlNameWithNameSpace, paramMap);
			page.setTotalCount(count);
		}

		if (page.isNeedData()) {
			if (page.isNeedTotalCount()) {
				if (page.getTotalCount() > 0) {
					List<T> resultList = this.queryList(rsSqlNameWithNameSpace, paramMap, page);
					page.setDataList(resultList);
				} else {
					page.setDataList(new ArrayList<T>());
				}
			}
			if (!page.isNeedTotalCount()) {
				List<T> resultList = this.queryList(rsSqlNameWithNameSpace, paramMap, page);
				page.setDataList(resultList);
			}
		}
		return page;
	}

	public List<BatchResult> flushStatements() {
		return mybatisTemplate.flushStatements();
	}

	public <T> List<T> queryList(String sqlNameWithNameSpace, Map<String, Object> map, DataPage<T> page) {
		map.put("startIndex", page.getFirst());
		map.put("endIndex", page.getEndIndex());
		map.put("pageSize", page.getPageSize());
		return queryList(sqlNameWithNameSpace, map);
	}
	/**下面添加在查询时使用object作为入参  */
    @Override
    public <T> T queryUnique(String sqlNameWithNameSpace, Object param) {
        return mybatisTemplate.<T> selectOne(sqlNameWithNameSpace, param);
    }

    @Override
    public <T> T queryOne(String sqlNameWithNameSpace, Object param) {
        return mybatisTemplate.<T> selectOne(sqlNameWithNameSpace, param);
    }

    @Override
    public <T> T queryOne(String sqlNameWithNameSpace, Object param,
						  int exceptionExceedCount) {
        List<T> list = mybatisTemplate.<T> selectList(sqlNameWithNameSpace, param);
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() > exceptionExceedCount) {
            String msg = "{sqlId:'" + sqlNameWithNameSpace
                    + "', msg:'mybatis.selectList exceptionExceedCount fail-fast', exceptionExceedCount:"
                    + exceptionExceedCount + "}";
            log.error(msg);
			throw new CommonException(ResultCode.PARAMETER_ERROR, msg);
        } else {
            return list.get(0);
        }
    }

    @Override
    public Object queryObject(String sqlNameWithNameSpace, Object param) {
        return mybatisTemplate.selectOne(sqlNameWithNameSpace, param);
    }

    @Override
    public int queryCount(String sqlNameWithNameSpace, Object param) {
        return mybatisTemplate.<Integer> selectOne(sqlNameWithNameSpace, param);
    }

    @Override
    public int queryInt(String sqlNameWithNameSpace, Object param) {
        return mybatisTemplate.<Integer> selectOne(sqlNameWithNameSpace, param);
    }

    @Override
    public <T> List<T> queryList(String sqlNameWithNameSpace, Object param,
								 DataPage<T> page) {
        Map map = new HashMap();
        map.put("startIndex", page.getFirst());
        map.put("endIndex", page.getEndIndex());
        map.put("pageSize", page.getPageSize());
        map.put("param", param);
        return queryList(sqlNameWithNameSpace, map);
    }

    @Override
    public <T> DataPage<T> queryPage(String countSqlNameWithNameSpace,
									 String rsSqlNameWithNameSpace, Object param, DataPage<T> page) {
        if (page.isNeedTotalCount()) {
            int count = this.queryCount(countSqlNameWithNameSpace, param);
            page.setTotalCount(count);
        }

        if (page.isNeedData()) {
            if (page.isNeedTotalCount()) {
                if (page.getTotalCount() > 0) {
                    List<T> resultList = this.queryList(rsSqlNameWithNameSpace, param, page);
                    page.setDataList(resultList);
                } else {
                    page.setDataList(new ArrayList<T>());
                }
            }
            if (!page.isNeedTotalCount()) {
                List<T> resultList = this.queryList(rsSqlNameWithNameSpace, param, page);
                page.setDataList(resultList);
            }
        }
        return page;
    }

    @Override
    public <T> List<T> queryList(String sqlNameWithNameSpace, Object param) {
        return mybatisTemplate.selectList(sqlNameWithNameSpace, param);
    }

    @Override
    public <T> List<T> queryList(String sqlNameWithNameSpace, Object param,
								 int exceptionExceedCount) {
        List<T> list = this.queryList(sqlNameWithNameSpace, param);
        if (list.size() > exceptionExceedCount) {
            String msg = "{sqlId:'" + sqlNameWithNameSpace + "', msg:'list fail-fast', exceptionExceedCount:"
                    + exceptionExceedCount + "}";
            log.error(msg);
			throw new CommonException(ResultCode.PARAMETER_ERROR, msg);
        }
        return list;
    }

    @Override
    public <K, V> Map<K, V> selectOneToMap(String sqlNameWithNameSpace,
										   Object param) {
        return mybatisTemplate.selectOne(sqlNameWithNameSpace, param);
    }
}
