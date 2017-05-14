package com.fish.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

/**
 * http请求工具类
 */
public abstract class HttpClientExecute {

	private final static Logger log = Logger.getLogger(HttpClientExecute.class);


	private static int ARRAY_SIZE_ZERO = 0;

	/**
	 * 封装http请求方法
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private static HttpResponse executeGet(String url, Map<String, Object> params) throws ClientProtocolException, IOException {

		DefaultHttpClient client = new DefaultHttpClient();

		StringBuilder urlBuilder = new StringBuilder(url);
		for (Map.Entry<String, Object> entry : params.entrySet()) {

			String key = entry.getKey();
			Object value = entry.getValue();

			if (urlBuilder.toString().contains("?")) {
				urlBuilder.append("&").append(key).append("=").append(value);
				continue;
			}
			urlBuilder.append(key).append("=").append(value);
		}

		HttpGet get = new HttpGet(url);

		//客户端向服务器发送请求
		HttpResponse response = client.execute(get);

		return response;
	}

	private static HttpResponse executePost(String url, Object object, Map<String, String> params) throws IOException {
		DefaultHttpClient client = new DefaultHttpClient();

		List<NameValuePair> listParams = new ArrayList<>();
		// 设置http请求参数
		addNameValuePair(listParams, object);


		//新建一个http请求
		HttpPost post = new HttpPost(url);

		if (null != params) {
			Set<Entry<String, String>> entries = params.entrySet();
			// 接收参数json列表
			JSONObject jsonParam = new JSONObject();
			for (Entry<String, String> entry : entries) {
				String key = entry.getKey();
				String value = entry.getValue();
				if(null != value && !"".equals(value)) {
					jsonParam.put(key, value);
					NameValuePair param = new BasicNameValuePair(key, null != value ? value.toString()  : "");
					listParams.add(param);
				}
			}
			StringEntity s = new StringEntity(jsonParam.toString(), "UTF-8");
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");// 发送json数据需要设置contentType
			post.setEntity(new UrlEncodedFormEntity(listParams, SystemContent.URL_ENCODED_FORM)); // 设置参数给Post
		}
		//客户端向服务器发送请求
		HttpResponse response = client.execute(post);

		return response;
	}

	/**
	 * 设置http请求参数
	 * @param listParams
	 * @param object
	 */
	private static void addNameValuePair(List<NameValuePair> listParams, Object object) {
		Field[] field = ReflectionUtils.getDeclaredField(object);
		for(int offset = 0; offset < field.length; offset++) {
			String fieldName = field[offset].getName();
			Object fieldValue = ReflectionUtils.getFieldValue(object, fieldName);
			if(null != fieldValue && !"".equals(fieldValue)) {
				NameValuePair param = new BasicNameValuePair(fieldName, null != fieldValue ? fieldValue.toString()  : "");
				listParams.add(param);
			}
		}
	}

	/**
	 * 将字符串转换为json对象
	 * @param strResult 需要转换的字符串
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getJSON(String strResult) throws JSONException {
		return JSONObject.parseObject(strResult);
	}

	/**
	 * 解析远程服务器返回的json数据，将解析结果保存到javabean对象中
	 * @param jsonObject 远程返回的数据对象
	 * @param object 将解析完的对象保存到javabean中
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	protected static void parserJson(JSONObject jsonObject, Object object)
			throws IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		// 获取对象所有属性
		Field[] fields = ReflectionUtils.getDeclaredField(object);
		for(int offset = 0; offset < fields.length; offset++) {
			Field field = fields[offset];
			String fieldName = field.getName();
			Class<?> fieldType = field.getType();
			if(!fieldName.startsWith("this") && !ReflectionUtils.isSerialVersionUID(fieldName)) {
				// string或基本数据类型,直接进行赋值操作，递归调用的出口
				if(fieldType.equals(String.class)) {
					// 暂时代码
					String fieldValue = "";
					try {
						fieldValue = jsonObject.getString(fieldName);
					} catch(Exception e) {
						//log.error("属性[" + fieldName + "] 赋值异常，请检查是否存在字段");
					}
					if(null != fieldValue && !"".equals(fieldValue)) {
						ReflectionUtils.setFieldValue(object, fieldName, fieldValue,
								new Class[]{String.class}, new String[]{fieldValue});
					}
				} else if(fieldType.equals(Double.class)) {
					Double fieldValue = jsonObject.getDouble(fieldName);
					if(null != fieldValue && !"".equals(fieldValue)) {
						ReflectionUtils.setFieldValue(object, fieldName, fieldValue,
								new Class[]{Double.class}, new Double[]{fieldValue});
					}
				} else if(fieldType.equals(Integer.class)) {
					Integer fieldValue = jsonObject.getInteger(fieldName);
					if(null != fieldValue && !"".equals(fieldValue)) {
						ReflectionUtils.setFieldValue(object, fieldName, fieldValue,
								new Class[]{Integer.class}, new Integer[]{fieldValue});
					}
				} else if(fieldType.equals(Boolean.class)) {
					Boolean fieldValue = jsonObject.getBoolean(fieldName);
					if(null != fieldValue && !"".equals(fieldValue)) {
						ReflectionUtils.setFieldValue(object, fieldName, fieldValue,
								new Class[]{Boolean.class}, new Boolean[]{fieldValue});
					}
				} else if(fieldType.equals(BigDecimal.class)) {
					BigDecimal fieldValue = null;
					try {
						fieldValue = BigDecimal.valueOf(jsonObject.getDouble(fieldName)).setScale(6);
					} catch (Exception e) {
						//log.error("属性[" + fieldName + "] 赋值异常，请检查是否存在字段");
					}
					if(null != fieldValue && !"".equals(fieldValue)) {
						ReflectionUtils.setFieldValue(object, fieldName, fieldValue,
								new Class[]{BigDecimal.class}, new BigDecimal[]{fieldValue});
					}
				} else if(fieldType.equals(Long.class)) {
					Long fieldValue = jsonObject.getLong(fieldName);
					if(null != fieldValue && !"".equals(fieldValue)) {
						ReflectionUtils.setFieldValue(object, fieldName, fieldValue,
								new Class[]{Long.class}, new Long[]{fieldValue});
					}
				} else if(fieldType.equals(List.class)) { // 集合类型，根据集合的泛型类型创建对象，然后递归
					setCollectionValue(jsonObject, field, object);
				} else if(fieldType.equals(Set.class)) { // 集合类型，根据集合的泛型类型创建对象，然后递归
					setCollectionValue(jsonObject, field, object);
				} else if(fieldType.equals(Map.class)) {

				} else { // 类类型（class），根据属性类型反射创建类，然后进行递归调用，解析json，并赋值到类中
					setClassValue(jsonObject, field, object);
				}
			}
		}
	}

	/**
	 * 设置请求服务器url地址
	 * @param obj
	 * @return
	 */
	protected abstract String setUrl(Object obj);

	/**
	 * 解析服务端返回json对象
	 * @param obj
	 */
	protected abstract void parserJson(Object obj, JSONObject jsonObject);

	/**
	 * 判断请求是否成功
	 * @param jsonObject
	 * @param object
	 * @return
	 */
	protected abstract boolean isSuc(JSONObject jsonObject, Object object);

	/**
	 * 调用服务端之前需要进行的操作
	 * @param object
	 * @return
	 * @throws Exception
	 */
	protected abstract Map<String, String> beforeRequest(Object object) throws Exception;

	/**
	 * 服务端返回数据之后数据的扩展
	 * @param object
	 */
	protected abstract void expand(Object object);

	/**
	 * json字符串解析的是一个数组，使用此方法赋值
	 * @param jsonObject 需要解析的json字符串
	 * @param field
	 * @param object
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private final static void setCollectionValue(JSONObject jsonObject, Field field, Object object)
			throws IllegalArgumentException, InstantiationException, IllegalAccessException,
			InvocationTargetException {

		Class<?> genericClazz = ReflectionUtils.genericClazz(field);
		String fieldName = field.getName();
		JSONArray array = jsonObject.getJSONArray(fieldName);
		if(null != array && ARRAY_SIZE_ZERO < array.size()) {
			Collection<Object> collection = null;
			Class<?>[] clazzs = new Class[1];
			Object[] objects = new Object[1];
			if (field.getType().equals(List.class)) {
				collection = new ArrayList<Object>();
				clazzs[0] = List.class;
			} else if (field.getType().equals(Set.class)) {
				collection = new HashSet<Object>();
				clazzs[0] = Set.class;
			} else if(field.getType().equals(Map.class)) {

			}
			for(int offset = 0; offset < array.size(); offset++) {
				JSONObject jsonObject2 = array.getJSONObject(offset);
				// 反射构造内部类对象
				Object obj = ReflectionUtils.getInnerConstructors(genericClazz, object);
				parserJson(jsonObject2, obj);
				collection.add(obj);
			}
			objects[0] = collection;
			// 赋值
			String fieldsName = ReflectionUtils.genMethodName("set", fieldName);
			ReflectionUtils.invokeMethod(object, fieldsName, clazzs, objects);
		}
	}

	/**
	 * 设置类对象值，
	 * @param jsonObject
	 * @param field
	 * @param object
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private final static void setClassValue(JSONObject jsonObject, Field field, Object object)
			throws IllegalArgumentException, InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String fieldName = field.getName();
		JSONObject classObject = jsonObject.getJSONObject(fieldName);
		if(null != classObject && !classObject.isEmpty()) {
			Class<?> genericClazz = field.getType();
			// 反射构造内部类对象
			Object obj = ReflectionUtils.getInnerConstructors(genericClazz, object);
			parserJson(classObject, obj);
			String fieldsName = ReflectionUtils.genMethodName("set", fieldName);
			ReflectionUtils.invokeMethod(object, fieldsName, new Class[]{genericClazz}, new Object[]{obj});
		}
	}


}
