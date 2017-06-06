package com.hhly.mlottery.util;

import android.util.Log;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.bean.focusAndPush.BasketballConcernListBean;
import com.hhly.mlottery.bean.focusAndPush.CancelConcernBean;
import com.hhly.mlottery.bean.focusAndPush.ConcernBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.basketballframe.basketnewfragment.BasketballFocusNewFragment;
import com.hhly.mlottery.frame.footballframe.FocusFragment;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.umeng.message.UmengRegistrar;

import java.util.HashMap;
import java.util.Map;

/**
 * 描    述：足球篮球关注的请求关注列表方法
 * 作    者：mady@13322.com
 * 时    间：2017/1/6
 */
public class FocusUtils {

    /**
     * 登录时获取用户足球关注列表，未登录的存在本地，则不需要请求。单点登录
     */
    public static void getFootballUserFocus(String userId) {

        //devideID;
        String deviceId = AppConstants.deviceToken;
        //devicetoken 友盟。
        String umengDeviceToken = PreferenceUtil.getString(AppConstants.uMengDeviceToken, "");
        String appNo = "11";
        String url = "http://192.168.31.73:8080/mlottery/core/pushSetting.loginUserFindMatch.do";
        Map<String, String> params = new HashMap<>();
        params.put("appNo", appNo);
        params.put("userId", userId);
        params.put("deviceToken", umengDeviceToken);
        params.put("deviceId", deviceId);

        Log.e("CCC", umengDeviceToken);
        //volley请求
        VolleyContentFast.requestJsonByPost(BaseURLs.FOOTBALL_FIND_MATCH, params, new VolleyContentFast.ResponseSuccessListener<BasketballConcernListBean>() {
            @Override
            public void onResponse(BasketballConcernListBean jsonObject) {
                if (jsonObject.getResult().equals("200")) {
                    Log.e("AAA", "登陆后请求的足球关注列表");
                    //将关注写入文件
                    StringBuffer sb = new StringBuffer();
                    for (String thirdId : jsonObject.getConcerns()) {
                        if ("".equals(sb.toString())) {
                            sb.append(thirdId);
                        } else {
                            sb.append("," + thirdId);
                        }
                    }
                    PreferenceUtil.commitString(FocusFragment.FOCUS_ISD, sb.toString());
                }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {


            }
        }, BasketballConcernListBean.class);

    }

    /**
     * 获取用户篮球关注列表
     */
    public static void getBasketballUserConcern(String userId) {

        String url = " http://192.168.31.68:8080/mlottery/core/androidBasketballMatch.findConcernVsThirdIds.do";
        String deviceId = AppConstants.deviceToken;
        //devicetoken 友盟。
        String umengDeviceToken = PreferenceUtil.getString(AppConstants.uMengDeviceToken, "");
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        Log.e("AAA", userId + "用户名");
        params.put("deviceId", deviceId);
//        params.put("deviceToken",umengDeviceToken);
        VolleyContentFast.requestJsonByPost(BaseURLs.BASKET_FIND_MATCH, params, new VolleyContentFast.ResponseSuccessListener<BasketballConcernListBean>() {
            @Override
            public void onResponse(BasketballConcernListBean jsonObject) {
                if (jsonObject.getResult().equals("200")) {
                    Log.e("AAA", "登陆后请求的篮球关注列表");
                    //将关注写入文件
                    StringBuffer sb = new StringBuffer();
                    for (String thirdId : jsonObject.getConcerns()) {
                        if ("".equals(sb.toString())) {
                            sb.append(thirdId);
                        } else {
                            sb.append("," + thirdId);
                        }
                    }
                    PreferenceUtil.commitString(BasketballFocusNewFragment.BASKET_FOCUS_IDS, sb.toString());
                }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {


            }
        }, BasketballConcernListBean.class);


    }

    /**
     * z足球点击关注，添加focusId
     *
     * @param thirdId
     */
    public static void addFocusId(String thirdId) {
        String focusIds = PreferenceUtil.getString(FocusFragment.FOCUS_ISD, "");
        if ("".equals(focusIds)) {
            PreferenceUtil.commitString(FocusFragment.FOCUS_ISD, thirdId);
        } else {
            PreferenceUtil.commitString(FocusFragment.FOCUS_ISD, focusIds + "," + thirdId);
        }
        //把用户id,deviceId,deviceToken 传给服务器
        String deviceId = AppConstants.deviceToken;
        String uMengDeviceToken = PreferenceUtil.getString(AppConstants.uMengDeviceToken, "");
        Log.e("AAA", uMengDeviceToken + "???");
        String userId = "";
        if (AppConstants.register != null && AppConstants.register != null && AppConstants.register.getUser() != null) {
            userId = AppConstants.register.getUser().getUserId();
        }
        //thirdId
        String url = "http://192.168.31.73:8080/mlottery/core/pushSetting.followMatch.do";
        Map<String, String> params = new HashMap<>();

        params.put("deviceId", deviceId);
        params.put("deviceToken", uMengDeviceToken);// 第一次获取的时候可能没得到
        //这样可以再次尝试获取deviceToken
        params.put("userId", userId);
        params.put("follow", "true");
        params.put("thirdId", thirdId);
        params.put("appNo", "11"); //固定国内版11

        VolleyContentFast.requestJsonByPost(BaseURLs.FOOTBALL_ADD_FOCUS, params, new VolleyContentFast.ResponseSuccessListener<ConcernBean>() {
            @Override
            public void onResponse(ConcernBean concernBean) {
                Log.e("AAAA", "concern");

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

            }
        }, ConcernBean.class);


    }

    /**
     * 足球关注删除focusId
     *
     * @param thirdId
     */
    public static void deleteFocusId(String thirdId) {
        String focusIds = PreferenceUtil.getString(FocusFragment.FOCUS_ISD, "");
        String[] idArray = focusIds.split("[,]");
        StringBuffer sb = new StringBuffer();
        for (String id : idArray) {
            if (!id.equals(thirdId)) {
                if ("".equals(sb.toString())) {
                    sb.append(id);
                } else {
                    sb.append("," + id);
                }

            }
        }
        PreferenceUtil.commitString(FocusFragment.FOCUS_ISD, sb.toString());

        //请求后台
        String deviceId = AppConstants.deviceToken;
        String uMengDeviceToken = PreferenceUtil.getString(AppConstants.uMengDeviceToken, "");
        String userId = "";
        if (AppConstants.register != null && AppConstants.register != null && AppConstants.register.getUser() != null) {
            userId = AppConstants.register.getUser().getUserId();
        }
        //thirdId
        String url = "http://192.168.31.73:8080/mlottery/core/pushSetting.followMatch.do";
        Map<String, String> params = new HashMap<>();

        params.put("deviceId", deviceId);
        params.put("deviceToken", uMengDeviceToken == "" ? UmengRegistrar.getRegistrationId(MyApp.getContext()) : uMengDeviceToken);// 第一次获取的时候可能没得到
        //这样可以再次尝试获取deviceToken
        params.put("userId", userId);
        params.put("follow", "false");
        params.put("thirdId", thirdId);
        params.put("appNo", "11"); //固定国内版11

        VolleyContentFast.requestJsonByPost(BaseURLs.FOOTBALL_ADD_FOCUS, params, new VolleyContentFast.ResponseSuccessListener<CancelConcernBean>() {
            @Override
            public void onResponse(CancelConcernBean jsonObject) {

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

            }
        }, CancelConcernBean.class);

    }

    /**
     * 足球判断thirdId是否已经关注
     *
     * @param thirdId
     * @return true已关注，false还没关注
     */
    public static boolean isFocusId(String thirdId) {
        String focusIds = PreferenceUtil.getString(FocusFragment.FOCUS_ISD, "");

        if ("".equals(focusIds)) {
            return false;
        } else {
            String[] focusIdArray = focusIds.split("[,]");

            boolean isFocus = false;
            for (String focusId : focusIdArray) {
                if (focusId.equals(thirdId)) {
                    isFocus = true;
                    break;
                }
            }
            return isFocus;
        }
    }

    /**
     * 添加篮球focusId
     *
     * @param thirdId
     */
    public static void addBasketFocusId(String thirdId) {
        String focusIds = PreferenceUtil.getString(BasketballFocusNewFragment.BASKET_FOCUS_IDS, "");
        if ("".equals(focusIds)) {
            PreferenceUtil.commitString(BasketballFocusNewFragment.BASKET_FOCUS_IDS, thirdId);
        } else {
            PreferenceUtil.commitString(BasketballFocusNewFragment.BASKET_FOCUS_IDS, focusIds + "," + thirdId);
        }
        //把用户id,deviceId,deviceToken 传给服务器
        String deviceId = AppConstants.deviceToken;
        String uMengDeviceToken = PreferenceUtil.getString(AppConstants.uMengDeviceToken, "");
        String userId = "";
        if (AppConstants.register != null && AppConstants.register != null && AppConstants.register.getUser() != null) {
            userId = AppConstants.register.getUser().getUserId();
        }
        String isPushFocus = PreferenceUtil.getBoolean(MyConstants.BASKETBALL_PUSH_FOCUS, true) == true ? "true" : "false";
        //thirdId
        String url = "http://192.168.31.68:8080/mlottery/core/androidBasketballMatch.customConcernVS.do";
        Map<String, String> params = new HashMap<>();

        params.put("deviceId", deviceId);
        params.put("deviceToken", uMengDeviceToken == "" ? UmengRegistrar.getRegistrationId(MyApp.getContext()) : uMengDeviceToken);// 第一次获取的时候可能没得到
        //这样可以再次尝试获取deviceToken
        params.put("userId", userId);
        params.put("isNotice", isPushFocus);
        params.put("concernThirdIds", thirdId);
        Log.e("AAA", thirdId + "");

        VolleyContentFast.requestJsonByPost(BaseURLs.BASKETBALL_ADD_FOCUS, params, new VolleyContentFast.ResponseSuccessListener<ConcernBean>() {
            @Override
            public void onResponse(ConcernBean concernBean) {
                Log.e("AAAA", "concern");

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

            }
        }, ConcernBean.class);


    }


    /**
     * 删除篮球focusId
     *
     * @param thirdId
     */
    public static void deleteBasketFocusId(String thirdId) {
        String focusIds = PreferenceUtil.getString(BasketballFocusNewFragment.BASKET_FOCUS_IDS, "");
        String[] idArray = focusIds.split("[,]");
        StringBuffer sb = new StringBuffer();
        for (String id : idArray) {
            if (!id.equals(thirdId)) {
                if ("".equals(sb.toString())) {
                    sb.append(id);
                } else {
                    sb.append("," + id);
                }

            }
        }
        PreferenceUtil.commitString(BasketballFocusNewFragment.BASKET_FOCUS_IDS, sb.toString());

        String deviceId = AppConstants.deviceToken;
        String userId = "";
        if (AppConstants.register != null && AppConstants.register!= null && AppConstants.register.getUser() != null) {
            userId = AppConstants.register.getUser().getUserId();
        }
        //请求后台
        Map<String, String> params = new HashMap<>();
        String url = " http://192.168.31.68:8080/mlottery/core/androidBasketballMatch.cancelCustomConcernVS.do";
        params.put("userId", userId);
        params.put("deviceId", deviceId);
        params.put("cancelThirdIds", thirdId);

        VolleyContentFast.requestJsonByPost(BaseURLs.BASKETBALL_DELETE_FOCUS, params, new VolleyContentFast.ResponseSuccessListener<CancelConcernBean>() {
            @Override
            public void onResponse(CancelConcernBean jsonObject) {

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

            }
        }, CancelConcernBean.class);
    }

    /**
     * 判断thirdId是否已经关注
     *
     * @param thirdId
     * @return true已关注，false还没关注
     */
    public static boolean isBasketFocusId(String thirdId) {
        String focusIds = PreferenceUtil.getString(BasketballFocusNewFragment.BASKET_FOCUS_IDS, "");

        if ("".equals(focusIds)) {
            return false;
        } else {
            String[] focusIdArray = focusIds.split(",");

            boolean isFocus = false;
            for (String focusId : focusIdArray) {
                if (focusId.equals(thirdId)) {
                    isFocus = true;
                    break;
                }
            }
            return isFocus;
        }
    }

    /**
     * 添加网球关注
     *
     * @param thirdId
     */
    public static void addTennisFocusId(String thirdId) {
        String focusIds = PreferenceUtil.getString(AppConstants.TENNIS_BALL_FOCUS, "");
        if ("".equals(focusIds)) {
            PreferenceUtil.commitString(AppConstants.TENNIS_BALL_FOCUS, thirdId);
        } else {
            PreferenceUtil.commitString(AppConstants.TENNIS_BALL_FOCUS, focusIds + "," + thirdId);
        }
    }

    /**
     * 删除网球关注
     *
     * @param thirdId
     */
    public static void delTennisFocusId(String thirdId) {
        String focusIds = PreferenceUtil.getString(AppConstants.TENNIS_BALL_FOCUS, "");
        String[] idArray = focusIds.split("[,]");
        StringBuffer sb = new StringBuffer();
        for (String id : idArray) {
            if (!id.equals(thirdId)) {
                if ("".equals(sb.toString())) {
                    sb.append(id);
                } else {
                    sb.append("," + id);
                }

            }
        }
        PreferenceUtil.commitString(AppConstants.TENNIS_BALL_FOCUS, sb.toString());
    }

    /**
     * 判断网球id是否关注
     *
     * @param thirdId
     * @return
     */
    public static boolean isTennisFocusId(String thirdId) {
        String focusIds = PreferenceUtil.getString(AppConstants.TENNIS_BALL_FOCUS, "");
        if ("".equals(focusIds)) {
            return false;
        } else {
            String[] focusIdArray = focusIds.split(",");
            boolean isFocus = false;
            for (String focusId : focusIdArray) {
                if (focusId.equals(thirdId)) {
                    isFocus = true;
                    break;
                }
            }
            return isFocus;
        }
    }
}
