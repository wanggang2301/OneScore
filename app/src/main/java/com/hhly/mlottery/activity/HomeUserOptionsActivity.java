package com.hhly.mlottery.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.ChoseHeadStartBean;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.bean.focusAndPush.BasketballConcernListBean;
import com.hhly.mlottery.bean.focusAndPush.ConcernBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.basketballframe.FocusBasketballFragment;
import com.hhly.mlottery.frame.footframe.FocusFragment;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.RongYunUtils;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.net.account.AccountResultCode;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;


/**
 * @ClassName: OneScoreGit
 * @author:Administrator luyao
 * @Description:  用户中心首页
 * @data: 2016/4/8 15:04
 */
public class HomeUserOptionsActivity extends Activity implements View.OnClickListener {

    public static String TAG = "HomeUserOptionsActivity";
    /**语言切换**/
    private RelativeLayout rl_language_frame;
    /**更多设置**/
    private RelativeLayout rl_setting_frame;
    /**反馈**/
    private RelativeLayout rl_user_feedback;
    private ProgressDialog progressBar;
    /**
     * 跳转其他Activity 的requestcode
     */
    public static final int REQUESTCODE_LOGIN = 100;
    public static final int REQUESTCODE_LOGOUT = 110;
    public static final int NOT_LOGGED_ON = 33;
    public static final int LOGGED_ON = 44;
    private TextView mTv_nickname;
    private ImageView mUser_image;
    private TextView mTv_logout;
    private Handler mViewHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NOT_LOGGED_ON:

                    break;
                case LOGGED_ON:
                    mTv_nickname.setText(AppConstants.register.getData().getUser().getNickName());
                   // ImageLoader.load(HomeUserOptionsActivity.this,AppConstants.register.getData().getUser().getHeadIcon()).into(mUser_image);
                    Glide.with(HomeUserOptionsActivity.this)
                            .load(AppConstants.register.getData().getUser().getHeadIcon())
                            .error(R.mipmap.center_head)
                            .into(mUser_image);
                    mTv_nickname.setEnabled(false);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
        initView();

    }
    /**
     * 初始化控件
     */
    private void initView() {

        progressBar = new ProgressDialog(this);
        progressBar.setMessage(getResources().getString(R.string.logouting));


        setContentView(R.layout.home_user_options_mian);
      /*  mTv_logout = (TextView) findViewById(R.id.tv_logout);
        mTv_logout.setOnClickListener(this);*/
        findViewById(R.id.public_img_back).setOnClickListener(this);
        //昵称
        mTv_nickname = (TextView) findViewById(R.id.tv_nickname);
        mTv_nickname.setOnClickListener(this);
        /*mLogin = (TextView) findViewById(R.id.login);
        mLogin.setOnClickListener(this);*/
        //头像
        mUser_image = (ImageView) findViewById(R.id.user_info_image);
        mUser_image.setOnClickListener(this);
             /*判断登录状态*/
        if (CommonUtils.isLogin()) {
            mViewHandler.sendEmptyMessage(LOGGED_ON);
        } else {
            mTv_nickname.setText(R.string.Login_register);
            mUser_image.setImageResource(R.mipmap.center_head);
        }
        rl_language_frame = (RelativeLayout) findViewById(R.id.rl_language_frame);
        rl_language_frame.setOnClickListener(this);
        rl_setting_frame = (RelativeLayout) findViewById(R.id.rl_setting_frame);
        rl_setting_frame.setOnClickListener(this);


        rl_user_feedback = (RelativeLayout) findViewById(R.id.rl_user_feedback);
        rl_user_feedback.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_language_frame:// 语言切换
                MobclickAgent.onEvent(HomeUserOptionsActivity.this, "LanguageChanger");
                Intent intent = new Intent(HomeUserOptionsActivity.this, HomeLanguageActivity.class);
                startActivity(intent);
                break;
          /*  case R.id.rl_about_frame:// 关于我们
                Intent intent2 = new Intent(HomeUserOptionsActivity.this, HomeAboutActivity.class);
                startActivity(intent2);
                MobclickAgent.onEvent(mContext, "AboutWe");
                break;*/
            case R.id.rl_setting_frame://更多設置
                MobclickAgent.onEvent(HomeUserOptionsActivity.this, "HomeUserOptionsActivity_settings");
                Intent intent2 = new Intent(HomeUserOptionsActivity.this, MoreSettingsActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_user_feedback:// 反馈
                MobclickAgent.onEvent(HomeUserOptionsActivity.this, "UserFeedback");
                startActivity(new Intent(HomeUserOptionsActivity.this, FeedbackActivity.class));
                break;
            case R.id.public_img_back:// 返回
                MobclickAgent.onEvent(HomeUserOptionsActivity.this, "HomePagerUserSetting_Exit");
                finish();
                break;
            case R.id.tv_nickname:// 登录
                MobclickAgent.onEvent(HomeUserOptionsActivity.this, "LoginActivity_Start");

                goToLoginActivity();

                break;
          /*  case R.id.tv_logout: // 退出登录
                MobclickAgent.onEvent(mContext, "AccountActivity_ExitLogin");
                showDialog();
                break;*/
            case R.id.user_info_image: //用户信息
                MobclickAgent.onEvent(HomeUserOptionsActivity.this, "ProfileActivity_Start");
                if (CommonUtils.isLogin()) {
                    startActivity(new Intent(this, ProfileActivity.class));
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                    //UiUtils.toast(MyApp.getInstance(), "请先登录");
                }

                break;
        }
    }

    private void goToLoginActivity() {
        startActivityForResult(new Intent(this, LoginActivity.class), REQUESTCODE_LOGIN);
    }

    private void showDialog() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(HomeUserOptionsActivity.this, R.style.AppThemeDialog);//  android.R.style.Theme_Material_Light_Dialog
        builder.setCancelable(false);// 设置对话框以外不可点击
        builder.setTitle("");// 提示标题
        builder.setMessage(R.string.logout_check);// 提示内容
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
        android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



/**
     * 注销
     */

    private void logout() {

        progressBar.show();

        String url = BaseURLs.URL_LOGOUT;
        Map<String, String> param = new HashMap<>();
        param.put("loginToken", AppConstants.register.getData().getLoginToken());
        param.put("deviceToken", AppConstants.deviceToken);
        param.put("userId", AppConstants.register.getData().getUser().getUserId());
        VolleyContentFast.requestJsonByPost(url, param, new VolleyContentFast.ResponseSuccessListener<Register>() {
            @Override
            public void onResponse(Register register) {

                progressBar.dismiss();
                if (register.getResult() == AccountResultCode.SUCC || register.getResult() == AccountResultCode.USER_NOT_LOGIN) {
                    CommonUtils.saveRegisterInfo(null);
                    UiUtils.toast(MyApp.getInstance(), R.string.logout_succ);
                    PreferenceUtil.commitBoolean("three_login",false);
                    setResult(RESULT_OK);


                    RongIM.getInstance().logout();// 退出融云账号
                    RongIM.getInstance().disconnect();// 断开融云连接
                    RongYunUtils.isRongConnent = false;// 设置融云断开状态
                    PreferenceUtil.commitString(RongYunUtils.USER_TOKEN, "");// 清除用户本地token
                    PreferenceUtil.commitString(RongYunUtils.USER_ID, "");// 清除用户本地id
                    PreferenceUtil.commitString(FocusBasketballFragment.BASKET_FOCUS_IDS,""); //清空篮球关注列表
                    PreferenceUtil.commitString(FocusFragment.FOCUS_ISD,""); //清空足球关注列表
                    request(); //推送需要

//                    getFootballUserFocus(""); //注销时把未登录状态的用户id请求过来 .篮球不需要是因为篮球进行了预加载，会直接请求关注页面。足球没有。
//
                } else {
                    CommonUtils.handlerRequestResult(register.getResult(), register.getMsg());
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                progressBar.dismiss();
                //L.e(TAG, " 注销失败");
                UiUtils.toast(MyApp.getInstance(), R.string.immediate_unconection);
            }
        }, Register.class);
    }
   /* *//**
     * 用户注销把用户状态改成0
     */
    private void request() {
        String url="http://192.168.31.73:8080/mlottery/core/pushSetting.exitUpdateOnlile.do";
        Map<String ,String> params=new HashMap<>();
        params.put("deviceId",AppConstants.deviceToken);
        VolleyContentFast.requestJsonByPost(BaseURLs.EXIT_PUSH_ONLINE, params, new VolleyContentFast.ResponseSuccessListener<ConcernBean>() {
            @Override
            public void onResponse(ConcernBean jsonObject) {
                if(jsonObject.getResult().equals("200")){
                    //注销成功
                    Log.e("AAA","注销成功");

                }
                finish();

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

                finish();
            }
        },ConcernBean.class);
    }
    /**
     * 获取用户足球关注列表
     */
    private void getFootballUserFocus(String userId) {

        //devideID;
        String deviceId=AppConstants.deviceToken;
        //devicetoken 友盟。
        String umengDeviceToken=PreferenceUtil.getString(AppConstants.uMengDeviceToken,"");
        String appNo="11";
        String url="http://192.168.31.73:8080/mlottery/core/pushSetting.loginUserFindMatch.do";
        Map<String,String> params=new HashMap<>();
        params.put("appNo",appNo);
        params.put("userId",userId);
        params.put("deviceToken",umengDeviceToken);
        params.put("deviceId",deviceId);

        Log.e("CCC",umengDeviceToken);
        //volley请求
        VolleyContentFast.requestJsonByPost(BaseURLs.FOOTBALL_FIND_MATCH, params, new VolleyContentFast.ResponseSuccessListener<BasketballConcernListBean>() {
            @Override
            public void onResponse(BasketballConcernListBean jsonObject) {
                if(jsonObject.getResult().equals("200")){
                    Log.e("AAA","登陆后请求的足球关注列表");
                    //将关注写入文件
                    StringBuffer sb=new StringBuffer();
                    for(String thirdId:jsonObject.getConcerns()){
                        if("".equals(sb.toString())){
                            sb.append(thirdId);
                        }else {
                            sb.append(","+thirdId);
                        }
                    }
                    PreferenceUtil.commitString(FocusFragment.FOCUS_ISD,sb.toString());
                    finish();
                }else{
                    finish();
                }


            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                finish();

            }
        },BasketballConcernListBean.class);

    }
    public void onEventMainThread(ChoseHeadStartBean choseHeadStartBean){

        //ImageLoader.load(HomeUserOptionsActivity.this,choseHeadStartBean.startUrl,R.mipmap.center_head).into(mUser_image);
        Glide.with(HomeUserOptionsActivity.this)
                .load(choseHeadStartBean.startUrl)
                .error(R.mipmap.center_head)
                .into(mUser_image);
    }
    public void onEventMainThread(Register register){

        //ImageLoader.load(HomeUserOptionsActivity.this,register.getData().getUser().getHeadIcon()).into(mUser_image);
      Glide.with(HomeUserOptionsActivity.this)
              .load(register.getData().getUser().getHeadIcon())
              .error(R.mipmap.center_head)
              .into(mUser_image);
       mTv_nickname.setText(register.getData().getUser().getNickName());
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

        if(CommonUtils.isLogin()){
            mTv_nickname.setText(AppConstants.register.getData().getUser().getNickName());
        }else {
            mTv_nickname.setText(R.string.Login_register);
            mUser_image.setImageResource(R.mipmap.center_head);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_LOGIN) {
                // 登录成功返回
                L.d(TAG, "登录成功");
                // iv_account.setImageResource(R.mipmap.login);
            } else if (requestCode == REQUESTCODE_LOGOUT) {
                L.d(TAG, "注销成功");
                mTv_nickname.setText(R.string.Login_register);
                mUser_image.setImageResource(R.mipmap.center_head);
                // iv_account.setImageResource(R.mipmap.logout);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 刷新本地用户缓存
       // RongIM.getInstance().refreshUserInfoCache(new UserInfo(AppConstants.register.getData().getUser().getUserId(), AppConstants.register.getData().getUser().getNickName(), Uri.parse(PreferenceUtil.getString(AppConstants.HEADICON, "xxx"))));
       // RongIM.getInstance().refreshUserInfoCache(new UserInfo(AppConstants.register.getData().getUser().getUserId(), AppConstants.register.getData().getUser().getNickName(), Uri.parse(AppConstants.register.getData().getUser().getHeadIcon())));
        EventBus.getDefault().unregister(this);
    }

}
