package com.hhly.mlottery.util;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;

import com.sohu.cyan.android.sdk.api.CallBack;
import com.sohu.cyan.android.sdk.api.Config;
import com.sohu.cyan.android.sdk.api.CyanSdk;
import com.sohu.cyan.android.sdk.entity.AccountInfo;
import com.sohu.cyan.android.sdk.exception.CyanException;
import com.sohu.cyan.android.sdk.http.CyanRequestListener;

/**
 * @author lzf
 * @ClassName:
 * @Description: 暢言工具類
 * @date
 */
public class CyUtils {
    public static boolean isLogin = false;

    //初始化畅言
    public static void initCy(Context context) {
        Config config = new Config();
        try {
            CyanSdk.register(context, "cyslrkBTR", "021bf43427836304a81c1ff382f326e3",
                    "http://10.2.58.251:8081/login-success.html", config);
            L.i("lzf初始化畅言成功");
        } catch (CyanException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            L.i("lzf初始化畅言失败");
        }
    }

    //单点登录
    public static void loginSso(String id, String nickname, CyanSdk sdk) {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.isv_refer_id = id;//应用自己的用户id
        accountInfo.nickname = nickname;//应用自己的用户昵称
        sdk.setAccountInfo(accountInfo, new CallBack() {

            @Override
            public void success() {
                // token
//                Set<String> set = CyanSdk.getCookie();
                L.i("lzf登录成功");
                isLogin = true;
            }

            @Override
            public void error(CyanException e) {
                L.i("lzf登录失败");
                isLogin = false;
            }
        });
    }

    //提交评论 需登录  否则提交失败
    public static void submitComment(long topicid, String comment, CyanSdk sdk, CyanRequestListener requestListener) {

        try {
            sdk.submitComment(topicid, comment, 3, "4", 5, 6.0f, "7", requestListener);

        } catch (CyanException e) {
            e.printStackTrace();
            L.i("lzfsendfail");
        }

    }

    /**
     * 隐藏原生键盘
     */
    public static  void hideKeyBoard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }
    /**
     * 获取设备EMEI 信息  手机唯一标识    因为单点登录不能同时有两个相同的用户名，否则评论失败（失败就失败嘛，还假成功，既回调了评论成功的接口，实际上数据没有评论出去）
     * 所以取设备唯一标识为用户名和id登录
     * **/
    public static String getImei(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String id = tm.getDeviceId();
            if (id != null) {
                return tm.getDeviceId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "9527";
    }
}
