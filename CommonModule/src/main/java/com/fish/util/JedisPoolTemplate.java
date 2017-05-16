package com.fish.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.UnsupportedEncodingException;

/**
 * redis
 */
public class JedisPoolTemplate {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private JedisPool jedisPool;
	private String module;

	public void setModule(String module) {
		this.module = module;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	/**
	 * 保存到缓存
	 *
	 * @param key
	 *            key
	 * @param seconds
	 *            失效时间
	 * @param value
	 *            缓存对象
	 */
	public void putObject(String key, int seconds, Object value) {
		try {
			byte[] byKey = this.getKey(key).getBytes("utf-8");
			this.setex(byKey, seconds, SerializeUtils.serialize(value));
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}
	}


	/**
	 * 根据key得到缓存对象
	 *
	 * @param key
	 */
	public <T> T getObject(String key) {
		Object value = null;
		try {
			byte[] byKey = this.getKey(key).getBytes("utf-8");
			byte[] byValue = this.get(byKey);
			value = SerializeUtils.unserialize(byValue);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}
		return value == null ? null : (T) value;
	}

	/**
	 * 设置单个值
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public String set(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		if (jedis == null) {
			return null;
		}
		String result = null;
		boolean broken = false;
		try {
			result = jedis.set(this.getKey(key), value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			this.returnResource(jedis, broken);
		}
		return result;
	}

	public String set(String key, String value, int seconds) {
		Jedis jedis = jedisPool.getResource();
		if (jedis == null) {
			return null;
		}
		String result = null;
		boolean broken = false;
		try {
			result = jedis.set(this.getKey(key), value);
			jedis.expire(this.getKey(key), seconds);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			this.returnResource(jedis, broken);
		}
		return result;

	}

	/**
	 * 获取单个值
	 *
	 * @param key
	 * @return
	 */
	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		if (jedis == null) {
			return null;
		}
		String result = null;
		boolean broken = false;
		try {
			result = jedis.get(this.getKey(key));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			this.returnResource(jedis, broken);
		}
		return result;
	}

	public Long del(String key) {
		Jedis jedis = jedisPool.getResource();
		if (jedis == null) {
			return null;
		}
		Long result = null;
		boolean broken = false;
		try {
			result = jedis.del(this.getKey(key));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			this.returnResource(jedis, broken);
		}
		return result;
	}

	/**
	 * 获取单个值
	 *
	 * @param key
	 * @return
	 */
	private byte[] get(byte[] key) {
		Jedis jedis = jedisPool.getResource();
		if (jedis == null) {
			return null;
		}
		byte[] result = null;
		boolean broken = false;
		try {
			result = jedis.get(key);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			this.returnResource(jedis, broken);
		}
		return result;
	}

	/**
	 * 设置缓存值在多少秒后失效
	 *
	 * @param key
	 * @param seconds
	 * @param value
	 * @return
	 */
	private String setex(byte[] key, int seconds, byte[] value) {
		Jedis jedis = jedisPool.getResource();
		if (jedis == null) {
			return null;
		}
		String result = null;
		boolean broken = false;
		try {
			result = jedis.setex(key, seconds, value);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			broken = true;
		} finally {
			this.returnResource(jedis, broken);
		}
		return result;
	}

	/**
	 * 获取key
	 *
	 * @param key
	 * @return
	 */
	private String getKey(String key) {
		if (null == module || "".equals(module)) {
			return key;
		}
		return new StringBuilder(module).append(key).toString();
	}

	private void returnResource(Jedis jedis, boolean broken) {
		if (broken) {
			jedisPool.returnBrokenResource(jedis);
		} else {
			jedisPool.returnResource(jedis);
		}
	}
}
