package com.hhly.mlottery.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.net.account.AccountResultCode;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * 首页用户设置选项
 * Created by hhly107 on 2016/4/6.
 */
public class HomeUserOptionsActivity extends BaseActivity implements View.OnClickListener {

    /**语言切换**/
    private RelativeLayout rl_language_frame;
    /**关于我们**/
    private RelativeLayout rl_about_frame;
    /**反馈**/
    private RelativeLayout rl_user_feedback;
    private ProgressDialog progressBar;
    private com.nostra13.universalimageloader.core.ImageLoader universalImageLoader;
    private  DisplayImageOptions options;
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
                    //mTv_nickname.setVisibility(View.VISIBLE);
                    mTv_nickname.setText(AppConstants.register.getData().getUser().getNickName());

                    mTv_nickname.setEnabled(false);
                    mTv_logout.setVisibility(View.VISIBLE);
                    findViewById(R.id.view_top).setVisibility(View.VISIBLE);
                    findViewById(R.id.view_botom).setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，多图片使用565
                .showImageOnLoading(R.mipmap.center_head)   //默认图片
                .showImageForEmptyUri(R.mipmap.center_head)    //url爲空會显示该图片，自己放在drawable里面的
                .showImageOnFail(R.mipmap.center_head)// 加载失败显示的图片
                .resetViewBeforeLoading(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext).build();
        universalImageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance(); //初始化
        universalImageLoader.init(config);
        initView();

    }
    /**
     * 初始化控件
     */
    private void initView() {

        progressBar = new ProgressDialog(this);
        progressBar.setMessage(getResources().getString(R.string.logouting));


        setContentView(R.layout.home_user_options_mian);
        mTv_logout = (TextView) findViewById(R.id.tv_logout);
        mTv_logout.setOnClickListener(this);
        findViewById(R.id.public_img_back).setOnClickListener(this);
        //昵称
        mTv_nickname = (TextView) findViewById(R.id.tv_nickname);
        mTv_nickname.setOnClickListener(this);
        /*mLogin = (TextView) findViewById(R.id.login);
        mLogin.setOnClickListener(this);*/
        //头像
        mUser_image = (ImageView) findViewById(R.id.user_info_image);
        mUser_image.setOnClickListener(this);

        rl_language_frame = (RelativeLayout) findViewById(R.id.rl_language_frame);
        rl_language_frame.setOnClickListener(this);
        rl_about_frame = (RelativeLayout) findViewById(R.id.rl_about_frame);
        rl_about_frame.setOnClickListener(this);
        rl_user_feedback = (RelativeLayout) findViewById(R.id.rl_user_feedback);
        rl_user_feedback.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_language_frame:// 语言切换
                Intent intent = new Intent(HomeUserOptionsActivity.this, HomeLanguageActivity.class);
                startActivity(intent);
                MobclickAgent.onEvent(mContext, "LanguageChanger");
                break;
            case R.id.rl_about_frame:// 关于我们
                Intent intent2 = new Intent(HomeUserOptionsActivity.this, HomeAboutActivity.class);
                startActivity(intent2);
                MobclickAgent.onEvent(mContext, "AboutWe");
                break;
            case R.id.rl_user_feedback:// 反馈
                startActivity(new Intent(HomeUserOptionsActivity.this, FeedbackActivity.class));
                MobclickAgent.onEvent(mContext, "UserFeedback");
                break;
            case R.id.public_img_back:// 返回
                finish();
                MobclickAgent.onEvent(mContext, "HomePagerUserSetting_Exit");
                break;
            case R.id.tv_nickname:// 登录
                MobclickAgent.onEvent(mContext, "LoginActivity_Start");

                goToLoginActivity();

                break;
            case R.id.tv_logout: // 退出登录
                MobclickAgent.onEvent(mContext, "AccountActivity_ExitLogin");
                showDialog();
                break;
            case R.id.user_info_image: //用户信息
                MobclickAgent.onEvent(mContext, "ProfileActivity_Start");
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

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext, R.style.AppThemeDialog);//  android.R.style.Theme_Material_Light_Dialog
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
                    finish();
                } else {
                    CommonUtils.handlerRequestResult(register.getResult(), register.getMsg());
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

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("HomeUserOptionsActivity");
       // UiUtils.toast(MyApp.getInstance(), "我是个人用户页面");
        universalImageLoader.displayImage(PreferenceUtil.getString(AppConstants.HEADICON, ""), mUser_image, options);
         /*判断登录状态*/
        if (CommonUtils.isLogin()) {
            mViewHandler.sendEmptyMessage(LOGGED_ON);
        } else {
            mUser_image.setImageResource(R.mipmap.center_head);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("HomeUserOptionsActivity");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_LOGIN) {
                // 登录成功返回
                L.d(TAG, "登录成功");
                mViewHandler.sendEmptyMessage(LOGGED_ON);
                // iv_account.setImageResource(R.mipmap.login);
            } else if (requestCode == REQUESTCODE_LOGOUT) {
                L.d(TAG, "注销成功");
                // iv_account.setImageResource(R.mipmap.logout);
            }
        }
    }
}
