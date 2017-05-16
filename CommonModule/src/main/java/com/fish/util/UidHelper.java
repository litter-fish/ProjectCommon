package com.fish.util;

import java.text.SimpleDateFormat;
import java.util.Date;


public class UidHelper {
	private static final UidHelper instance = new UidHelper();

	public static UidHelper getInstance() {
		return instance;
	}

	private static long counter = 0L;
	private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

	private static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	/**
	 * Unique across JVMs on this machine (unless they load this class in the
	 * same quater second - very unlikely)
	 */
	protected int getJVM() {
		return JVM;
	}

	/**
	 * Unique in a millisecond for this JVM instance (unless there are >
	 * Short.MAX_VALUE instances created in a millisecond)
	 */
	protected long getCount() {
		synchronized (UidHelper.class) {
			if (counter < 0)
				counter = 0;
			return counter++;
		}
	}

	/**
	 * Unique down to millisecond
	 */
	protected long getHiTime() {
		return System.currentTimeMillis() >>> 32;
	}

	protected String format(final long intval) {
		final String formatted = Long.toHexString(intval);
		final StringBuffer buf = new StringBuffer("00000000");
		buf.replace(8 - formatted.length(), 8, formatted);
		return buf.toString();
	}

	protected String formatCount(final long intval) {
		return  String.format("%04d", intval);
	}
	/**
	 * 生成4位随机验证码
	 *
	 * @return 验证码
	 */
	public final static String random() {
		return String.valueOf(1000 + (int) (Math.random() * 8999));
	}

	public String generate() {
		return new StringBuffer(22).append(new SimpleDateFormat(YYYYMMDDHHMMSS).format(new Date()))
				.append(random())
				.append(formatCount(getCount())).toString();
	}
}
