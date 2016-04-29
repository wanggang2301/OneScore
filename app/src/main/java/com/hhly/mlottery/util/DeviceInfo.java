package com.hhly.mlottery.util;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

/**
 * 获取设备信息工具类
 */
public class DeviceInfo {

	/**
	 * 获取设备imei
 	 */

	public static String getDeviceId(Context context) {
		TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		String imei = TelephonyMgr.getDeviceId();
		return imei;
	}

	/**
	 * 获取设备imsi
 	 */
	public static String getSubscriberId(Context context) {
		TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		String imsi = TelephonyMgr.getSubscriberId();
		return imsi;
	}

	/**
	 * 获取型号
 	 */
	public static String getModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * 获取手机品牌，厂商
	 */
	public static String getManufacturer(){
		return android.os.Build.MANUFACTURER;
	}

	/**
	 * 获取手机系统版本
	 * @return
     */
	public static String getOSVersion(){
		return Build.VERSION.RELEASE;
	}

	/**
	 * 获取屏幕宽度
	 * @param context
	 * @return
	 */
	public static int getDisplayWidth(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

		int width = wm.getDefaultDisplay().getWidth();

		return width;
	}

	/**
	 * 获取屏幕高度
	 * @param context
	 * @return
	 */
	public static int getDisplayHeight(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

		int height = wm.getDefaultDisplay().getHeight();

		return height;
	}

}
