package com.hhly.mlottery.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * ToastTools
 * 
 * @author lzf 2015-4-13 下午5:30:18
 * @function: Toast 提示工具
 * 
 * 
 * @version 1.0
 */
public class ToastTools {
	public static final String TAG = "ToastTools";
	public static Toast toast = null;

	/**
	 * 快速弹出不延时
	 */
	public static void ShowQuick(Context context, String message) {
		if (null == toast) {
			toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
		}
		toast.setText(message);
		toast.show();
	}

	/**
	 * 快速弹出不延时居中
	 */
	public static void ShowQuickCenter(Context context, String message) {
		if (null == toast) {
			toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
		}
		toast.setText(message);
		toast.show();

	}

}
