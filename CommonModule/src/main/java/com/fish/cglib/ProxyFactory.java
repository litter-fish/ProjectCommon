package com.fish.cglib;

import com.fish.bo.CommonResult;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class ProxyFactory implements MethodInterceptor {

	private Object object;

	public ProxyFactory(Object object) {
		this.object = object;
	}

	public Object getProxyInstance() {

		Enhancer enhancer = new Enhancer();

		enhancer.setSuperclass(object.getClass());

		enhancer.setCallback(this);


		return enhancer.create();

	}



	@Override
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

		Object returnObject = method.invoke(object, objects);

		return returnObject;
	}

	public static void main(String[] args) {
		CommonResult maResult = new CommonResult();

		new ProxyFactory(maResult).getProxyInstance();

	}

}
