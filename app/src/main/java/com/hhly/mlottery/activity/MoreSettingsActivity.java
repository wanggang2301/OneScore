package com.hhly.mlottery.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.bean.focusAndPush.ConcernBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.basketballframe.basketnewfragment.BasketballFocusNewFragment;
import com.hhly.mlottery.frame.footballframe.FocusFragment;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.net.account.AccountResultCode;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: OneScoreGit
 * @author:Administrator luyao
 * @Description:  更多设置
 * @data: 2016/11/15 15:04
 */

public class MoreSettingsActivity extends BaseActivity  implements View.OnClickListener{

    private final int REQUESTCODE_LOGIN = 100;
    private final int REQUESTCODE_LOGOUT = 110;
//    public static final int NOT_LOGGED_ON = 33;
//    public static final int LOGGED_ON = 44;

    private ProgressDialog progressBar;
    private TextView public_txt_title;
    private TextView tv_logout;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {

        progressBar = new ProgressDialog(this);
        progressBar.setMessage(getResources().getString(R.string.logouting));

        setContentView(R.layout.more_setting);

        findViewById(R.id.rl_about_frame).setOnClickListener(this);
        findViewById(R.id.tv_logout).setOnClickListener(this);

        tv_logout = (TextView) findViewById(R.id.tv_logout);

        findViewById(R.id.public_img_back).setOnClickListener(this);
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        public_txt_title = (TextView) findViewById(R.id.public_txt_title);
        public_txt_title.setText(getResources().getString(R.string.more_setting));



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.rl_about_frame:
                MobclickAgent.onEvent(mContext, "AboutWe");
                Intent intent2 = new Intent(MoreSettingsActivity.this, HomeAboutActivity.class);
                startActivity(intent2);
                MobclickAgent.onEvent(mContext, "AboutWe");
                break;
            case R.id.tv_logout: // 退出登录
                MobclickAgent.onEvent(mContext, "AccountActivity_ExitLogin");
                showDialog();
                break;
            case R.id.public_img_back:
                finish();
                break;
            default:
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(DeviceInfo.isLogin()){
            tv_logout.setVisibility(View.VISIBLE);
         //
            //   findViewById(R.id.view_botom).setVisibility(View.VISIBLE);
        }else{
            tv_logout.setVisibility(View.GONE);
         /*   findViewById(R.id.view_top).setVisibility(View.GONE);
            findViewById(R.id.view_botom).setVisibility(View.GONE);*/
            findViewById(R.id.hui_img).setVisibility(View.GONE);
            findViewById(R.id.view_botom).setVisibility(View.GONE);
        }
    }

    private void showDialog() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext, R.style.AppThemeDialog);//  android.R.style.Theme_Material_Light_Dialog
        builder.setCancelable(false);// 设置对话框以外不可点击
        builder.setTitle("");// 提示标题
        builder.setMessage(R.string.logout);// 提示内容
        builder.setPositiveButton(R.string.about_confirm, new DialogInterface.OnClickListener() {
            //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
               logout();
            }
        });
        builder.setNegativeButton(R.string.about_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }



    /**
     * 注销
     */
    private void logout() {

        progressBar.show();
        String url = BaseURLs.URL_LOGOUT;
        Map<String, String> param = new HashMap<>();
        param.put("userId", AppConstants.register.getUser().getUserId());
        param.put("loginToken", AppConstants.register.getToken());

        VolleyContentFast.requestJsonByGet(url, param, new VolleyContentFast.ResponseSuccessListener<Register>() {
            @Override
            public void onResponse(Register register) {

                progressBar.dismiss();
                if (Integer.parseInt(register.getCode())== AccountResultCode.SUCC || Integer.parseInt(register.getCode()) == AccountResultCode.USER_NOT_LOGIN) {
                    DeviceInfo.saveRegisterInfo(null);
                    UiUtils.toast(MyApp.getInstance(), R.string.logout_succ);

                    PreferenceUtil.commitBoolean("three_login",false);
                    setResult(RESULT_OK);

                    PreferenceUtil.commitString(BasketballFocusNewFragment.BASKET_FOCUS_IDS,""); //清空篮球关注列表
                    PreferenceUtil.commitString(FocusFragment.FOCUS_ISD,""); //清空足球关注列表

                    //清空定制列表id
                    PreferenceUtil.commitString(CustomListActivity.CUSTOM_LEAGUE_FOCUSID , "");
                    PreferenceUtil.commitString(CustomListActivity.CUSTOM_TEAM_FOCUSID , "");


                    request(); //推送需要
                    finish();
//                    getFootballUserFocus(""); //注销时把未登录状态的用户id请求过来 .篮球不需要是因为篮球进行了预加载，会直接请求关注页面。足球没有。
//
                } else {
                    DeviceInfo.handlerRequestResult(Integer.parseInt(register.getCode()), "未知错误");
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                progressBar.dismiss();
                L.e(TAG, " 注销失败");
                UiUtils.toast(MyApp.getInstance(), R.string.immediate_unconection);
            }
        }, Register.class);
    }
    /**
     * 用户注销把用户状态改成0
     */
    private void request() {
//        String url="http://192.168.31.73:8080/mlottery/core/pushSetting.exitUpdateOnlile.do";
        Map<String ,String> params=new HashMap<>();
        params.put("deviceId",AppConstants.deviceToken);
        VolleyContentFast.requestJsonByPost(BaseURLs.EXIT_PUSH_ONLINE, params, new VolleyContentFast.ResponseSuccessListener<ConcernBean>() {
            @Override
            public void onResponse(ConcernBean jsonObject) {
//                if(jsonObject.getResult().equals("200")){
//                    //注销成功
//                    L.d("AAA","注销成功");
//
//                }
               finish();

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

               finish();
            }
        },ConcernBean.class);
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUESTCODE_LOGIN) {
//                // 登录成功返回
//                L.d(TAG, "登录成功");
//
//          //      mViewHandler.sendEmptyMessage(LOGGED_ON);
//                // iv_account.setImageResource(R.mipmap.login);
//            } else if (requestCode == REQUESTCODE_LOGOUT) {
//                L.d(TAG, "注销成功");
//                // iv_account.setImageResource(R.mipmap.logout);
//            }
//        }
//    }
}
