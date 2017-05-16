package com.fish.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fish.annotation.Auth;
import com.fish.constants.ResultCode;
import com.fish.util.JedisPoolTemplate;
import com.fish.util.RedisKeyGenner;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 公共拦截器
 */
public abstract class CommonInterceptor extends HandlerInterceptorAdapter {

	private final static Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);

	protected final static String WP_COOKIE_OPENID_UUID = "wpCookieOpenId";

	protected final static String WP_COOKIE_TOKEN = "wpCookieToken";

	/**
	 * 判断请求是否需要认证，当且仅当请求配置了Auth注解且参数need为false,localCache参数也为false的时候，说明该请求连接不需要认证
	 * 否则需要认证
	 * @param request
	 * @param handler
	 * @return true：需要认证， false：不需要认证
	 */
	protected Boolean isNeedAuth(HttpServletRequest request, Object handler, JedisPoolTemplate jedisPoolTemplate) {

		logger.debug("prepare url:{}", request.getServletPath());
		if (!(handler instanceof HandlerMethod)) return false;

		HandlerMethod method = (HandlerMethod) handler;
		Auth auth = method.getMethodAnnotation(Auth.class);
		logger.debug("auth : {}", null != auth && (auth.need() | auth.loadCache()));

		if (null == auth) return true;
		// 是否需要缓存
		boolean loadCache = auth.loadCache();
		// url是否需要拦截
		boolean need = auth.need();

		if (need | loadCache) return true;

		return false;
	}


	/**
	 * 根据cookie中保存的token获取缓存中的用户信息，判断该token用户是否已经登录，
	 *
	 * 如果cookie取不到，返回错误码：4000，前端需要再次调用自动登录请求，自动登录成功或注册成功后会将token写入cookie中
	 *
	 * 请开发人员确保这两个接口中写入了token。thanks
	 *
	 * @param request
	 * @param jedisPoolTemplate
	 * @return
	 */
	protected Boolean isAutoLogin(HttpServletRequest request, JedisPoolTemplate jedisPoolTemplate) {

		Cookie cookie = getCookies(request, WP_COOKIE_TOKEN);
		if (null == cookie) {
			logger.error("Get token from cookie fail : {}, token is not null", ResultCode.INVALID_WECHAT_SEESION.getDesc());
			return false;
		}

		// 判断用户是否已经登录
		String cacheKey = RedisKeyGenner.userLogin(cookie.getValue());

		Object cacheUser = jedisPoolTemplate.get(cacheKey);
		if (null != cacheUser) {

			JSONObject json = JSONObject.parseObject(cacheUser.toString());
			int bizType = json.getInteger("bizType");

			logger.debug("Get Login Cache : [{}]", cacheUser.toString());
			return true;
		}
		logger.error("cache invalid : [{}]", cacheKey);
		return false;
	}


	/**
	 * 根据cookie名称获取cookie
	 * @return
	 */
	protected Cookie getCookies(HttpServletRequest request, final String cookieName){
		Cookie[] cookies = request.getCookies();
		logger.info("login token req.getCookies() : {}", JSON.toJSONString(cookies));
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				if (cookieName.equals(cookie.getName())) {
					logger.info("Get login token from cookie name:[{}], value:[{}]", cookie.getName(), cookie.getValue());
					return cookie;
				}
			}
		}
		return null;
	}

	/**
	 * 写回响应
	 *
	 * @param response 响应
	 * @param json Json数据
	 */
	protected Boolean writer(HttpServletResponse response, String json) {
		if (StringUtils.isBlank(json))
			return Boolean.TRUE;
		response.setCharacterEncoding("utf-8");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write(json);
			writer.flush();
		} catch (Exception e) {
			logger.error("response写回json数据异常! json:{}", json, e);
		} finally {
			if (null != writer) {
				writer.close();
			}
		}
		return Boolean.FALSE;
	}
}
