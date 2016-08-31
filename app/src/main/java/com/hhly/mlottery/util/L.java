package com.hhly.mlottery.util;

import android.util.Log;

/**
 * Log统一管理类
 * 
 * @author way
 * 
 */
public class L
{

	private L()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	private static final String TAG = "LOG";

	// 下面四个是默认tag的函数
	public static void i(String msg)
	{
		if (AppConstants.isTestEnv)
			Log.i(TAG, msg);
	}

	public static void d(String msg)
	{
		if (AppConstants.isTestEnv)
			Log.d(TAG, msg);
	}

	public static void e(String msg)
	{
		if (AppConstants.isTestEnv)
			Log.e(TAG, msg);
	}

	public static void v(String msg)
	{
		if (AppConstants.isTestEnv)
			Log.v(TAG, msg);
	}

	// 下面是传入自定义tag的函数
	public static void i(String tag, String msg)
	{
		if (AppConstants.isTestEnv)
			Log.i(tag, msg);
	}

	public static void d(String tag, String msg)
	{
		if (AppConstants.isTestEnv)
			Log.d(tag, msg);
	}

	public static void e(String tag, String msg)
	{
		if (AppConstants.isTestEnv)
			Log.e(tag, msg);
	}

	public static void v(String tag, String msg)
	{
		if (AppConstants.isTestEnv)
			Log.v(tag, msg);
	}
	
	public static void w(String tag, String msg)
	{
		if (AppConstants.isTestEnv)
			Log.w(tag, msg);
	}
	public static void log(String tag, String msg) {
		if (AppConstants.isTestEnv) {
			Log.i(tag, msg);
		}
	}
	public static void log(String msg) {
		if (AppConstants.isTestEnv) {
			log(TAG, msg);
		}
	}
}