package com.hhly.mlottery.util;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.widget.Toast;

/**
 * ToastTools
 *
 * @author lzf 2015-4-13 下午5:30:18
 * @version 1.0
 * @function: Toast 提示工具
 */
public class ToastTools {
    public static final String TAG = "ToastTools";
    public static Toast toast = null;

    /**
     * 快速弹出不延时
     */
    public static void showQuick(Context context, String message) {
        if (null == toast) {
            toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        }
        toast.setText(message);
        toast.show();
    }

    /**
     * 弹出不延时
     *
     * @param context   context
     * @param stringRes StringRes
     */
    public static void showQuick(Context context, @StringRes int stringRes) {
        if (null == toast) {
            toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        }
        toast.setText(stringRes);
        toast.show();
    }

    /**
     * 快速弹出不延时居中
     */
    public static void showQuickCenter(Context context, String message) {
        if (null == toast) {
            toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.setText(message);
        toast.show();

    }

}
