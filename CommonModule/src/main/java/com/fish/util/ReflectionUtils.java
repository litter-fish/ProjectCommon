package com.fish.util;

import com.alibaba.fastjson.JSONObject;
import com.fish.annotation.Alias;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * 反射工具类
 */
public class ReflectionUtils {


	private final static Logger log = LoggerFactory.getLogger(ReflectionUtils.class);

	/**
	 * 通过反射创建类构造方法
	 * @param clazz
	 * @return
	 * @see
	 * @since 1.7
	 * @exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static <T> T getOutConstructors(Class<?> clazz)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Constructor<?>[] constructors = clazz.getConstructors();
		// 获取类的名称
		String className = clazz.getSimpleName();
		T obj = (T) constructors[0].newInstance(new Object[]{});
		return obj;
	}

	/**
	 * 通过反射创建内部类构造方法
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 *
	 * @return
	 * @see
	 * @since 1.7
	 * @exception
	 */
	public static Object getInnerConstructors(Class<?> clazz, Object reqMessage)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		Constructor<?>[] constructors = ((Class<?>) clazz).getConstructors();
		// 获取类的名称
		String className = clazz.getSimpleName();
		Object obj = constructors[0].newInstance(new Object[]{reqMessage});
		return obj;
	}

	/**
	 * 循环向上转型, 获取对象的 DeclaredMethod
	 * @param object : 子类对象
	 * @param methodName : 父类中的方法名
	 * @param parameterTypes : 父类中的方法参数类型
	 * @return 父类中的方法对象
	 */
	private static Method getDeclaredMethod(Object object, String methodName, Class<?> ... parameterTypes) {
		Method method = null ;

		for(Class<?> clazz = object.getClass() ; clazz != Object.class ; clazz = clazz.getSuperclass()) {
			try {
				method = clazz.getDeclaredMethod(methodName, parameterTypes) ;
				return method ;
			} catch (Exception e) {
				//这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
				//如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
				if(log.isDebugEnabled())
					log.debug("获取对象的方法异常：" + e.getMessage());
			}
		}

		return null;
	}

	/**
	 * 直接调用对象方法, 而忽略修饰符(private, protected, default)
	 *
	 * @param object : 子类对象
	 * @param methodName : 父类中的方法名
	 * @param parameterTypes : 父类中的方法参数类型
	 * @param parameters : 父类中的方法参数
	 * @return 父类中方法的执行结果
	 */
	public static Object invokeMethod(Object object, String methodName, Class<?> [] parameterTypes, Object [] parameters) {
		//根据 对象、方法名和对应的方法参数 通过反射 调用上面的方法获取 Method 对象
		Method method = getDeclaredMethod(object, methodName, parameterTypes) ;
		try {
			if(null != method) {
				//抑制Java对方法进行检查,主要是针对私有方法而言
				method.setAccessible(true) ;
				//调用object 的 method 所代表的方法，其方法的参数是 parameters
				return method.invoke(object, parameters) ;
			} else {
				log.debug("属性：" + methodName.substring(3) + ",不存在，请检查");
			}
		} catch (IllegalArgumentException e) {
			log.error("属性：" + methodName.substring(3) + ", 异常请检查");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			log.error("属性：" + methodName.substring(3) + ", 异常请检查");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			log.error("属性：" + methodName.substring(3) + ", 异常请检查");
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 循环向上转型, 获取对象的 DeclaredField
	 * @param object : 子类对象
	 * @param fieldName : 父类中的属性名
	 * @return 父类中的属性对象
	 */
	public static Field getDeclaredField(Object object, String fieldName) {
		Class<?> clazz = object.getClass() ;
		return getDeclaredField(clazz, fieldName);
	}

	/**
	 *
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Field getDeclaredField(Class<?> clazz, String fieldName) {

		Field field = null ;
		for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {
			try {
				field = clazz.getDeclaredField(fieldName) ;
				return field ;
			} catch (Exception e) {
				//这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
				//如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
				//log.error("获取对象的属性异常：" + e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 循环向上转型, 获取对象的所有属性（包括继承的属性）
	 *
	 * @param object
	 * @return
	 */
	public static Field[] getAllDeclaredField(Object object) {

		Class<?> clazz = object.getClass() ;

		return getAllDeclaredField(clazz);
	}

	/**
	 * 循环向上转型, 获取对象的所有属性（包括继承的属性）
	 *
	 * @param clazz
	 * @return
	 */
	public static Field[] getAllDeclaredField(Class<?> clazz) {

		Field[] field = null;
		List<Field> fieldList = new ArrayList<Field>();
		for(; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				field = clazz.getDeclaredFields();
				fieldList.addAll(Arrays.asList(field));
			} catch (Exception e) {
				//这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
				//如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
				//log.error("获取对象的属性异常：" + e.getMessage());
			}
		}

		return fieldList.toArray(field);
	}

	/**
	 * 获取对象的所有属性
	 * @param object
	 * @return
	 */
	public static Field[] getDeclaredField(Object object) {

		Class<?> clazz = object.getClass() ;

		return getDeclaredField(clazz);
	}

	/**
	 * 获取对象的所有属性
	 * @param clazz
	 * @return
	 */
	public static Field[] getDeclaredField(Class<?> clazz) {

		Field[] field = clazz.getDeclaredFields();

		return field;
	}

	/**
	 * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
	 *
	 * @param object : 子类对象
	 * @param fieldName : 父类中的属性名
	 * @param value : 将要设置的值
	 */
	public static void setFieldValue(Object object, String fieldName, Object value) {

		//根据 对象和属性名通过反射 调用上面的方法获取 Field对象
		Field field = getDeclaredField(object, fieldName);
		try {
			//抑制Java对其的检查
			field.setAccessible(true) ;
			//将 object 中 field 所代表的值 设置为 value
			value = transformRealTypeValue(field, value);
			field.set(object, value) ;
		} catch (IllegalArgumentException e) {
			log.error("给对象属性［" + fieldName + "］赋值操作异常！");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			log.error("给对象属性［" + fieldName + "］赋值操作异常！");
			e.printStackTrace();
		}

	}

	public static Object transformRealTypeValue(Field field, Object value) {
		Class<?> fieldType = field.getType();
		if(fieldType.equals(Double.class) || fieldType.equals(double.class)) {
			if(!isNullObject(value)) {
				value = Double.parseDouble(value.toString());
			}
		} else if(fieldType.equals(long.class) || fieldType.equals(Long.class)) {
			if(!isNullObject(value)) {
				value =Long.valueOf(value.toString());
			}
		} else if(fieldType.equals(byte.class) || fieldType.equals(Byte.class)) {
			if(!isNullObject(value)) {
				value = Byte.valueOf(value.toString());
			}
		} else if(fieldType.equals(short.class) || fieldType.equals(Short.class)) {
			if(!isNullObject(value)) {
				value = Short.valueOf(value.toString());
			}
		} else if(fieldType.equals(float.class) || fieldType.equals(Float.class)) {
			if(!isNullObject(value)) {
				value = Float.valueOf(value.toString());
			}
		} else if(fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
			if(!isNullObject(value)) {
				value = Integer.valueOf(value.toString());
			}
		} else if(fieldType.equals(char.class) || fieldType.equals(Character.class)) {
			if(!isNullObject(value)) {
				value = value.toString().toCharArray()[0];
			}
		} else if(fieldType.equals(String.class)) {
			if(!isNullObject(value)) {
				value = value.toString();
			}
		} else if(fieldType.equals(BigDecimal.class)) {
			if(!isNullObject(value)) {
				value = new BigDecimal(value.toString());
			}
		} else {
			log.error("类型转换异常");
		}
		return value;
	}



	/**
	 * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
	 *
	 * @param clazz : 子类对象
	 * @param fieldName : 父类中的属性名
	 * @param value : 将要设置的值
	 */
	public static void setFieldValue(Class<?> clazz, String fieldName, Object value) {

		//根据 对象和属性名通过反射 调用上面的方法获取 Field对象
		Field field = getDeclaredField(clazz, fieldName);
		//抑制Java对其的检查
		field.setAccessible(true) ;
		try {
			//将 object 中 field 所代表的值 设置为 value
			field.set(clazz, value) ;
		} catch (IllegalArgumentException e) {
			log.error("给对象属性［" + fieldName + "］赋值操作异常！");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			log.error("给对象属性［" + fieldName + "］赋值操作异常！");
			e.printStackTrace();
		}

	}

	/**
	 * 反射获取属性泛型类型
	 * @param field
	 * @return
	 */
	public static Class<?> genericClazz(Field field) {
		// 得到所有的fields
		// 得到field的class及类型全路径
		Class<?> fieldClazz = field.getType();
		/*if (fieldClazz.isPrimitive()) {
			continue; // 【1】 //判断是否为基本类型
		}
		if (fieldClazz.getName().startsWith("java.lang")) {
			continue; // getName()返回field的类型全路径；
		}*/
		Class<?> genericClazz = null;
		if (fieldClazz.isAssignableFrom(List.class)) {
			Type fc = field.getGenericType(); // 关键的地方，如果是List类型，得到其Generic的类型
			/*if (fc == null) {
				continue;
			}*/
			// 【3】如果是泛型参数的类型
			if (fc instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) fc;
				genericClazz = (Class<?>) pt.getActualTypeArguments()[0]; // 【4】
				// 得到泛型里的class类型对象。
				/*m.put(f.getName(), genericClazz);

				Map<String, Class> m1 = prepareMap(genericClazz);

				m.putAll(m1);*/
				//System.out.println(genericClazz);
			}
		}
		return genericClazz;
	}

	/**
	 * 直接设置对象属性值, 经过 setter
	 *
	 * @param clazz : 子类对象
	 * @param fieldName : 父类中的属性名
	 * @param value : 将要设置的值
	 */
	public static Method setFieldValue(Class<?> clazz, String fieldName, Object value, Class<?> typeClass) {

		//根据 对象和属性名通过反射 调用上面的方法获取 Field对象
		Field field = getDeclaredField(clazz, fieldName) ;
		//抑制Java对其的检查
		field.setAccessible(true) ;
		try {
			//clazz.newInstance();
			//将 object 中 field 所代表的值 设置为 value
			// field.set(object, value) ;
			String methodName = genMethodName("set", fieldName);
			Method method = clazz.getDeclaredMethod(methodName, typeClass);
			return method;
		} catch (IllegalArgumentException e) {
			log.error("给对象属性［" + fieldName + "］赋值操作异常！");
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			log.error("给对象属性［" + fieldName + "］赋值操作异常！");
			e.printStackTrace();
		} catch (SecurityException e) {
			log.error("给对象属性［" + fieldName + "］赋值操作异常！");
			e.printStackTrace();
		}
		return null;
	}

	public static void setFieldValue(Object object, String fieldName, Object value, Class<?> typeClass,
									 Class<?> [] parameterTypes, Object [] parameters) {

		Class<?> clazz = object.getClass();

		//根据 对象和属性名通过反射 调用上面的方法获取 Field对象
		Field field = getDeclaredField(clazz, fieldName) ;
		//抑制Java对其的检查
		field.setAccessible(true) ;
		try {
			String methodName = genMethodName("set", fieldName);
			//Method method = clazz.getDeclaredMethod(methodName, typeClass);
			invokeMethod(object, methodName, parameterTypes, parameters);
		} catch (IllegalArgumentException e) {
			log.error("给对象属性［" + fieldName + "］赋值操作异常！");
			e.printStackTrace();
		} catch (SecurityException e) {
			log.error("给对象属性［" + fieldName + "］赋值操作异常！");
			e.printStackTrace();
		}
	}

	public static void setFieldValue(Object object, String fieldName, Object value,
									 Class<?>[] parameterTypes, Object[] parameters) {

		Class<?> clazz = object.getClass();

		// 根据 对象和属性名通过反射 调用上面的方法获取 Field对象
		Field field = getDeclaredField(clazz, fieldName);
		// 抑制Java对其的检查
		field.setAccessible(true);
		try {
			String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			// Method method = clazz.getDeclaredMethod(methodName, typeClass);
			invokeMethod(object, methodName, parameterTypes, parameters);
		} catch (IllegalArgumentException e) {
			log.error("给对象属性［" + fieldName + "］赋值操作异常！");
			e.printStackTrace();
		} catch (SecurityException e) {
			log.error("给对象属性［" + fieldName + "］赋值操作异常！");
			e.printStackTrace();
		}
	}



	/**
	 * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
	 * @param object : 子类对象
	 * @param fieldName : 父类中的属性名
	 * @return : 父类中的属性值
	 */
	public static Object getFieldValue(Object object, String fieldName) {

		//根据 对象和属性名通过反射 调用上面的方法获取 Field对象
		Field field = getDeclaredField(object, fieldName) ;
		//抑制Java对其的检查
		field.setAccessible(true) ;

		try {
			//获取 object 中 field 所代表的属性值
			return field.get(object) ;
		} catch(Exception e) {
			log.error("获取对象属性［" + fieldName + "］值操作异常！");
			e.printStackTrace() ;
		}

		return null;
	}

	/**
	 * 构造方法名称
	 *
	 * @param pre 方法前缀（get/set）
	 * @param fieldName 以“_”分割的属性名称
	 * @return
	 * @see
	 * @since 1.7
	 * @exception
	 */
	public static String genMethodName(String pre, String fieldName) {
		String fieldsName = pre;

		String[] fieldS = fieldName.split("_");
		if(fieldName.equalsIgnoreCase("RET")) {
			fieldsName += fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1).toLowerCase();
		} else {
			if(null == fieldS || 0 == fieldS.length ) {
				log.error("方法属性异常");
				return null;
			}
			if(1 == fieldS.length) {
				fieldsName += fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			} else {
				for(String ff : fieldS) {
					fieldsName += ff.substring(0, 1).toUpperCase() + ff.substring(1).toLowerCase();
				}
			}
		}

		return fieldsName;
	}

	/**
	 * 构造方法名称
	 *
	 * @param pre 方法前缀（get/set）
	 * @param fieldName 以“_”分割的属性名称
	 * @return
	 * @see
	 * @since 1.7
	 * @exception
	 */
	public static String genMethodName2(String pre, String fieldName) {
		String fieldsName = pre;

		String[] fieldS = fieldName.split("_");
		if(fieldName.equalsIgnoreCase("RET")) {
			fieldsName += fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1).toLowerCase();
		} else {
			if(null == fieldS || 0 == fieldS.length ) {
				log.error("方法属性异常");
				return null;
			}
			if(1 == fieldS.length) {
				fieldsName += fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1).toLowerCase();
			} else {
				for(String ff : fieldS) {
					fieldsName += ff.substring(0, 1).toUpperCase() + ff.substring(1).toLowerCase();
				}
			}
		}

		return fieldsName;
	}

	/**
	 * 判断字段类型是否是基本数据类型
	 *
	 * @param clazz
	 * @return
	 */
	public static boolean isBaseDataType(Class<?> clazz) {
		return (clazz.equals(String.class) || clazz.equals(Integer.class)
				|| clazz.equals(Byte.class) || clazz.equals(Long.class)
				|| clazz.equals(Double.class) || clazz.equals(Float.class)
				|| clazz.equals(Character.class) || clazz.equals(Short.class)
				|| clazz.equals(Boolean.class) || clazz.equals(BigDecimal.class) || clazz.isPrimitive());
	}


	public final static <T> T jsonToObject(String json, T t) {

		if (null == json || null == t) return null;

		JSONObject jsonObject = JSONObject.parseObject(json);

		Field[] fields = getAllDeclaredField(t);
		for (Field field : fields) {
			String fieldName = field.getName();
			if(isSerialVersionUID(fieldName)) continue;
			Object value = jsonObject.get(fieldName);
			if (null == value ) continue;
			try {
				field.setAccessible(true);
				field.set(t, value);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return t;
	}

	public final static <T> T jsonToObject(JSONObject json, Class<T> clazz) {

		if (null == clazz) return null;
		T instancece = null;
		try {
			instancece = clazz.newInstance();

			if (null == json) return instancece;

			for (Map.Entry<String, Object> entry : json.entrySet()) {
				String[] keys = entry.getKey().split("_");

				StringBuilder setter = new StringBuilder("set");
				for (String key : keys) {
					setter.append(key.substring(0, 1).toUpperCase()).append(key.substring(1));
				}

				Method setterMethod = clazz.getMethod(setter.toString(), new Class[]{String.class});
				setterMethod.setAccessible(true);
				setterMethod.invoke(instancece, new Object[]{entry.getValue().toString()});
			}

		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}


		return instancece;
	}

	public static final <T> String objectToJson(T t) {
		return JSONObject.toJSONString(t);
	}

	public static <T> void clone(T sourceObj, T destObj, boolean alias){
		if(null== sourceObj || null == destObj){
			return;
		}
		Field[] fields = getAllDeclaredField(sourceObj.getClass());
		Object value = null;
		for (Field field : fields) {
			try {
				String filedName = field.getName();
				if(isSerialVersionUID(filedName)) continue;

				Class<?> filedType = field.getType();

				if(alias){
					Alias al = field.getAnnotation(Alias.class);
					if(null != al){
						filedName = al.value();
						if(StringUtils.isNotBlank(al.type())) {
							filedType = Class.forName(al.type());
						}
					}
				}
				if(!isFieldName(destObj.getClass(), filedName)){
					continue;
				}
				value = getFieldValue(sourceObj, field.getName());
				if(null == value){
					continue;
				}
				setObjValueByField(destObj, filedType,filedName,value);
			} catch (Exception e) {
				log.warn("字段{}不存在,e:{}",field.getName(),e);
			}
		}
	}


	/**
	 *
	 * isFieldName(指定对象，判断该对象的属性是否存在，包含检测其父类)
	 * @param clazz clazz
	 * @param filedname 对象的属性名
	 * @return
	 * @throws Exception
	 * Boolean
	 * @exception
	 */
	public static Boolean isFieldName(Class<?> clazz ,String filedname) throws Exception{
		Field[] fields=clazz.getDeclaredFields();
		/**
		 * 循环遍历所有的元素，检测有没有这个名字
		 */
		boolean b=false;
		for (int i = 0; i < fields.length; i++) {
			if(fields[i].getName().equals(filedname)){
				return true;
			}
		}
		if ( clazz.getSuperclass ( ) != null){
			return ReflectionUtils.isFieldName(clazz.getSuperclass(), filedname);
		}
		return b;
	}

	/**
	 * 根据属性,给对象赋值
	 * @param name
	 * @return
	 */
	public static Object setObjValueByField(Object bean,Class<?> filedType ,String name, Object value) {
		Object result = null;
		try {
			String stringLetter = name.substring(0, 1).toUpperCase();
			String setName = "set" + stringLetter + name.substring(1);
			Method method = bean.getClass().getMethod(setName, filedType);
			String filedTypeName = filedType.getName();
			if (method != null) {
				if (value.getClass().getName().equals("org.json.JSONObject$Null")) {
				} else if (filedTypeName.equals(String.class.getName())) {
					method.invoke(bean, value.toString());
				} else if (filedTypeName.equals(int.class.getName()) || filedTypeName.equals(Integer.class.getName())) {
					method.invoke(bean, Integer.parseInt(StringUtils.isEmpty(value.toString()) ? "0" : value.toString()));
				} else if (filedTypeName.equals(float.class.getName()) || filedTypeName.equals(Float.class.getName())) {
					method.invoke(bean, Float.parseFloat(StringUtils.isEmpty(value.toString()) ? "0" : value.toString()));
				} else if (filedTypeName.equals(double.class.getName()) || filedTypeName.equals(Double.class.getName())) {
					method.invoke(bean, Double.parseDouble(StringUtils.isEmpty(value.toString()) ? "0" : value.toString()));
				} else if (filedTypeName.equals(long.class.getName()) || filedTypeName.equals(Long.class.getName())) {
					method.invoke(bean, Long.parseLong(StringUtils.isEmpty(value.toString()) ? "0" : value.toString()));
				} else if (filedTypeName.equals(Date.class.getName())) {
					if(value != null && value instanceof Long) {
						method.invoke(bean, new Date(((Long) value).longValue()));
					}else if(value instanceof Date){
						method.invoke(bean, value);
					}
				} else if (filedTypeName.equals(BigDecimal.class.getName())) {
					method.invoke(bean, StringUtils.isEmpty(value.toString()) ? null : BigDecimal.valueOf(Double.parseDouble(value.toString())));
				} else {
					method.invoke(bean, value);
				}
			}
			return bean;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}




	public static boolean isSerialVersionUID(String fieldName) {
		if ("serialVersionUID".equals(fieldName.trim())) {
			return true;
		}
		return false;
	}

	private static boolean isNullObject(Object object) {
		if(null != object && !"".equals(object) && !"null".equals(object) ){
			return false;
		}
		return true;
	}
	
}
