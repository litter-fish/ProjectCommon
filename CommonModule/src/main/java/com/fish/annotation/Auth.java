package com.fish.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {

	/**
	 * 判断是否需要Auth
	 * @return
	 */
	boolean need() default true;

	/**
	 * 判断是否需要加载缓存信息
	 * @return
	 */
	boolean loadCache() default true;
}
