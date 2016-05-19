package com.hhly.mlottery.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.bean.account.Register;

import java.util.UUID;

/**
 * Created by lyx on 2016/5/19.
 * 杂七杂八工具方法
 */
public class CommonUtils {

    private static final java.lang.String TAG = "CommonUtils";

    /**
     * 保存注册信息
     * @param register
     */
    public static void saveRegisterInfo(Register register){


        if (register == null){
            PreferenceUtil.commitString(AppConstants.SPKEY_USERID , "");
            PreferenceUtil.commitString(AppConstants.SPKEY_NICKNAME , "");
            PreferenceUtil.commitString(AppConstants.SPKEY_TOKEN , "");

            AppConstants.register = new Register();

        }else {
            PreferenceUtil.commitString(AppConstants.SPKEY_USERID , register.getData().getUser().getUserId());
            PreferenceUtil.commitString(AppConstants.SPKEY_NICKNAME , register.getData().getUser().getNickName());
            PreferenceUtil.commitString(AppConstants.SPKEY_TOKEN , register.getData().getToken());


            AppConstants.register = register;
        }

    }


    public static void initRegisterInfo(){
        Register.DataBean.UserBean userBean = new Register.DataBean.UserBean();
        userBean.setUserId(PreferenceUtil.getString(AppConstants.SPKEY_USERID , ""));
        userBean.setNickName(PreferenceUtil.getString(AppConstants.SPKEY_NICKNAME , ""));

        Register.DataBean dataBean = new Register.DataBean();
        dataBean.setToken(PreferenceUtil.getString(AppConstants.SPKEY_TOKEN , ""));
        dataBean.setUser(userBean);

        AppConstants.register = new Register(dataBean);
        L.d(TAG ,"init regsterinfo = "+ AppConstants.register.toString());
    }


    /**
     * 是否登录
     * @return
     */
    public static boolean isLogin(){
        boolean isLogin = false;
        if (!(TextUtils.isEmpty(AppConstants.register.getData().getToken()))
                && !(TextUtils.isEmpty(AppConstants.register.getData().getUser().getUserId()))){
            isLogin = true;
        }
        L.d(TAG, " 是否登录 = " + isLogin);
        return isLogin;
    }


    /**
     * 获取手机唯一标示
     * @return
     */
    public static String getDeviceToken() {
        TelephonyManager tm = (TelephonyManager) MyApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        String devId = tm.getDeviceId();
        if (!TextUtils.isEmpty(devId)) {
            return devId;
        }
        String uuid;
        WifiManager wifi = (WifiManager)MyApp.getInstance().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifi ? null : wifi.getConnectionInfo());
        if (info != null) {
            uuid = info.getMacAddress();
        } else {
            uuid = UUID.randomUUID().toString();
        }
        return uuid;
    }

}
