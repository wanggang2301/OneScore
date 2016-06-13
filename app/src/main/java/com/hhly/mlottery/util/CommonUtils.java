package com.hhly.mlottery.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.EditText;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.bean.account.SendSmsCode;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.impl.GetVerifyCodeCallBack;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.net.account.AccountResultCode;

import java.util.HashMap;
import java.util.Map;
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
            PreferenceUtil.commitString(AppConstants.SPKEY_LOGINACCOUNT , "");

            AppConstants.register = new Register();

        }else {
            PreferenceUtil.commitString(AppConstants.SPKEY_USERID , register.getData().getUser().getUserId());
            PreferenceUtil.commitString(AppConstants.SPKEY_NICKNAME , register.getData().getUser().getNickName());
            PreferenceUtil.commitString(AppConstants.SPKEY_LOGINACCOUNT , register.getData().getUser().getLoginAccount());

            String token = register.getData().getLoginToken();
            L.d(TAG , " saveRegisterInfo   token = "+ token);
            PreferenceUtil.commitString(AppConstants.SPKEY_TOKEN , token);


            AppConstants.register = register;
        }

    }


    /**
     * 初始化注册信息（与登录信息一样）
     */
    public static void initRegisterInfo(){
        Register.DataBean.UserBean userBean = new Register.DataBean.UserBean();
        userBean.setUserId(PreferenceUtil.getString(AppConstants.SPKEY_USERID , ""));
        userBean.setNickName(PreferenceUtil.getString(AppConstants.SPKEY_NICKNAME , ""));
        userBean.setLoginAccount(PreferenceUtil.getString(AppConstants.SPKEY_LOGINACCOUNT , ""));

        Register.DataBean dataBean = new Register.DataBean();

        String token = PreferenceUtil.getString(AppConstants.SPKEY_TOKEN , "");
        L.e(TAG , " initRegisterInfo   token = "+ token);
        dataBean.setLoginToken(token);
        dataBean.setUser(userBean);

        AppConstants.register = new Register(dataBean);
        L.d(TAG ,"init regsterinfo = "+ AppConstants.register.toString());

        AppConstants.deviceToken = CommonUtils.getDeviceToken();
    }


    /**
     * 是否登录
     * @return
     */
    public static boolean isLogin(){
        boolean isLogin = false;
        if (!(TextUtils.isEmpty(AppConstants.register.getData().getLoginToken()))
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


    /**
     * 请求账户信息返回统一处理
     * @param rescode
     */
    @Deprecated
    public static void handlerRequestResult(int rescode){
        handlerRequestResult(rescode , null);
    }

    /**
     * 请求账户信息返回统一处理
     * @param rescode
     */
    public static void handlerRequestResult(int rescode , String defaultMessage){
        L.d(TAG , "handlerRequestResult rescode =  "+ rescode);
        switch (rescode){
            case AccountResultCode.SUCC:
                L.d(TAG , "handlerRequestResult succ ");
                break;
            case AccountResultCode.SYSTEM_ERROR:
            case AccountResultCode.PARAM_ERROR:
            case AccountResultCode.NO_AGGREEMENT:
            case AccountResultCode.PLATFORM_NOT_EXIST:
            case AccountResultCode.OPERATOR_TYPE_NOT_EXIST:
            case AccountResultCode.SERVER_ERROR:
                UiUtils.toast(MyApp.getInstance() , R.string.system_error);
                break;
            case AccountResultCode.INTERVEL_LESS:
                UiUtils.toast(MyApp.getInstance() , R.string.verify_time_limit);
                break;
            case AccountResultCode.MAIL_FORMAT_ERROR:
                UiUtils.toast(MyApp.getInstance() , R.string.mail_format_error);
                break;
            case AccountResultCode.MAIL_ALREADY_REGISTER:
                UiUtils.toast(MyApp.getInstance() , R.string.mail_already_register);
                break;
            case AccountResultCode.VERIFY_CODE_ERROR:
                UiUtils.toast(MyApp.getInstance() , R.string.verify_code_error);
                break;
            case AccountResultCode.USER_EXIST:
                UiUtils.toast(MyApp.getInstance() , R.string.user_exist);
                break;
            case AccountResultCode.VERIFY_CODE_INVALIDATE:
                UiUtils.toast(MyApp.getInstance() , R.string.verify_code_invalidate);
                break;
            case AccountResultCode.PHONE_ALREADY_EXIST:
                UiUtils.toast(MyApp.getInstance() , R.string.phone_already_exist);
                break;
            case AccountResultCode.USER_NOT_LOGIN:
                UiUtils.toast(MyApp.getInstance() , R.string.user_not_login);
                break;
            case AccountResultCode.PHONE_FORMAT_ERROR:
                UiUtils.toast(MyApp.getInstance() , R.string.PHONE_FORMAT_ERROR);
                break;
            case AccountResultCode.USER_NOT_EXIST:
                UiUtils.toast(MyApp.getInstance() , R.string.user_not_exist);
                break;
            case AccountResultCode.MESSAGE_SEND_FAIL:
                UiUtils.toast(MyApp.getInstance() , R.string.message_send_fail);
                break;
            case AccountResultCode.NICKNAME_EXIST:
                UiUtils.toast(MyApp.getInstance() , R.string.nickname_exist);
                break;
            case AccountResultCode.USERNAME_EXIST:
                UiUtils.toast(MyApp.getInstance() , R.string.username_exist);
                break;
            case AccountResultCode.USERNAME_ERROR:
                UiUtils.toast(MyApp.getInstance() , R.string.username_error);
                break;
            case AccountResultCode.GET_QQ_INFO_FAIL:
                UiUtils.toast(MyApp.getInstance() , R.string.get_qq_info_fail);
                break;
            case AccountResultCode.GET_WEIBO_INFO_FAIL:
                UiUtils.toast(MyApp.getInstance() , R.string.GET_WEIBO_INFO_FAIL);
                break;
            case AccountResultCode.REALNAMW_ERROR:
                UiUtils.toast(MyApp.getInstance() , R.string.realname_error);
                break;
            case AccountResultCode.ID_CARD_FORMAT_ERROR:
                UiUtils.toast(MyApp.getInstance() , R.string.id_card_format_error);
                break;
            case AccountResultCode.PASSWORD_ERROR:
                UiUtils.toast(MyApp.getInstance() , R.string.password_error);
                break;
            case AccountResultCode.USERNAME_PASS_ERROR:
                UiUtils.toast(MyApp.getInstance() , R.string.username_pass_error);
                break;
            case AccountResultCode.NICKNAME_SENSITIVE:
                UiUtils.toast(MyApp.getInstance() , R.string.nickname_sensitive);
                break;
            default:
                L.e(TAG , "未定义错误码 : rescode = "+ rescode + " , defaultMessage = "+ defaultMessage);
                if (!TextUtils.isEmpty(defaultMessage)){

                    UiUtils.toast(MyApp.getInstance() , defaultMessage);
                }
                break;
        }
    }


    /**
     * 获取验证码
     * @param ctx
     * @param phone
     * @param oprateType 操作类型
     * @param callBack
     */
    public static void getVerifyCode(Context ctx , String phone , String oprateType, final GetVerifyCodeCallBack callBack) {
        if (UiUtils.isMobileNO(ctx ,phone)){
            callBack.beforGet();
            String url = BaseURLs.URL_SENDSMSCODE;
            Map<String, String> param = new HashMap<>();
            param.put("phone" , phone);
            param.put("operateType" , oprateType);

            VolleyContentFast.requestJsonByPost(url,param, new VolleyContentFast.ResponseSuccessListener<SendSmsCode>() {
                @Override
                public void onResponse(SendSmsCode jsonObject) {
                    callBack.onGetResponce(jsonObject);
                    handlerRequestResult(jsonObject.getResult() , jsonObject.getMsg());
                }
            }, new VolleyContentFast.ResponseErrorListener() {
                @Override
                public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                    L.e(TAG,"发送验证码失败");
                    callBack.onGetError(exception);
                    UiUtils.toast(MyApp.getInstance() , R.string.immediate_unconection);
                }
            } , SendSmsCode.class);

        }
    }


    /**
     * 光标移动到结尾
     * @param ets
     */
    public static void selectionLast(EditText... ets){
        for (EditText et:ets
             ) {
            String pwd = et.getText().toString();
            if(!TextUtils.isEmpty(pwd)){
                et.setSelection(pwd.length());
            }
        }
    }

}
