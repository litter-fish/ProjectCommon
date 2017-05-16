package com.fish.util;


public class RedisKeyGenner {

	public final static String PROJECT_KEY = "wp:";

	private final static String USER_VERIFY_CODE = "wp:verify:";

	private final static String USER_LOGIN_ID_KEY = "userLoginId:";

	private final static String OPEN_ID_KEY = "openId:";


	/**
	 * 验证码
	 * @param mobilePhone 手机号码
	 * @return key
	 */
	public static String userVerifyCode(String mobilePhone) {
		return  concat(USER_VERIFY_CODE, mobilePhone);
	}

	/**
	 * 用户登录redis key
	 * @param userId
	 * @return
	 */
	public static String userLogin(String userId) {
		return concat(USER_LOGIN_ID_KEY, userId);
	}

	public static String openId(String openId) {
		return concat(OPEN_ID_KEY, openId);
	}

	/**
	 * 连接key
	 * @param strs
	 * @return
	 */
	private static String concat(String... strs){
		StringBuilder sb = new StringBuilder(PROJECT_KEY);
		for (String str : strs) {
			sb.append(str);
		}
		return sb.toString();
	}
}
