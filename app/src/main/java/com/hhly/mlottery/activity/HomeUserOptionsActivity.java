package com.hhly.mlottery.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.code.microlog4android.appender.AbstractAppender;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.ChoseHeadStartBean;
import com.hhly.mlottery.bean.SpecialistBean;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.LoadingResultEventBusEntity;
import com.hhly.mlottery.mvp.bettingmvp.mvpview.MvpChargeMoneyActivity;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.FocusUtils;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.cipher.MD5Util;
import com.hhly.mlottery.util.net.UnitsUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.net.account.AccountResultCode;
import com.hhly.mlottery.util.net.account.CustomEvent;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * @ClassName: OneScoreGit
 * @author:Administrator luyao
 * @Description: 个人中心首页
 * @data: 2016/4/8 15:04
 */
public class HomeUserOptionsActivity extends Activity implements View.OnClickListener {

    private String TAG = "HomeUserOptionsActivity";

    /**我的关注*/
//    private RelativeLayout rl_focus;
//    private  View mFocus_RedDot; //关注红点
    /**
     * 我的关注红点
     */
//    boolean mShowRedDot=false;
    boolean mInvitedShowRedDot = true;
    /*邀请码红点*/
    /**
     * 我的定制
     */
    private RelativeLayout rl_custom;
    /**
     * 语言切换
     **/
    private RelativeLayout rl_language_frame;
    /**
     * 更多设置
     **/
    private RelativeLayout rl_setting_frame;
    /**
     * 反馈
     **/
    private RelativeLayout rl_user_feedback;
    private ProgressDialog progressBar;


    //审核中状态

    boolean is_inaudit = false;

    /**
     * 跳转其他Activity 的requestcode
     */
    public static final int REQUESTCODE_LOGIN = 100;
    public static final int REQUESTCODE_LOGOUT = 110;
    public static final int NOT_LOGGED_ON = 33;
    public static final int LOGGED_ON = 44;
    /**我的关注的红点*/
//    public static final String SHOW_RED="show_focus_red_dot";
    /**
     * 我的关注的红点
     */
    private final String INVITED_SHOW_RED = "show_invited_red_dot";

    private TextView mTv_nickname;
    private ImageView mUser_image;
    //    private View mRedDot;
//    private TextView mTv_logout;
    private Handler mViewHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NOT_LOGGED_ON:

                    break;
                case LOGGED_ON:
                    mTv_nickname.setText(AppConstants.register.getUser().getNickName());
                    // ImageLoader.load(HomeUserOptionsActivity.this,AppConstants.register.getData().getUser().getHeadIcon()).into(mUser_image);
                    Glide.with(getApplicationContext())
                            .load(AppConstants.register.getUser().getImageSrc())
                            .error(R.mipmap.center_head)
                            .into(mUser_image);

                    Log.i("register","isLogin"+AppConstants.register.getUser().getImageSrc());
                    mTv_nickname.setEnabled(false);
                    break;
                default:
                    break;
            }
        }
    };
    private RelativeLayout rl_setting_invited;
    private View invited_red_dot_view;
    private RelativeLayout rl_my_subscribe;
    private RelativeLayout rl_my_apply;
    private RelativeLayout rl_my_promotion;
    private LinearLayout my_balance;
    private LinearLayout my_income;
    private ImageView rl_setting_frame1;
    private RelativeLayout account_details;
    private LinearLayout not_login_balance;
    private LinearLayout not_login_payable;
    private LinearLayout available_balance_rl;
    private LinearLayout cash_balance_payable_rl;
    private TextView in_audit;
    private TextView available_balance;
    private TextView cash_balance_payable;
    private TextView subscribe_tv;
    private TextView promotion_tv;
    private TextView recharge_bt;
    private Context context;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        context = this;
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

        rl_setting_invited = (RelativeLayout) findViewById(R.id.rl_setting_invited);
        rl_setting_invited.setOnClickListener(this);

        //审核中
        in_audit = (TextView) findViewById(R.id.in_audit);

             /*判断登录状态*/
        if (DeviceInfo.isLogin()) {
            mViewHandler.sendEmptyMessage(LOGGED_ON);
        } else {
            mTv_nickname.setText(R.string.Login_register);
            mUser_image.setImageResource(R.mipmap.center_head);
            in_audit.setText("");
        }

        rl_custom = (RelativeLayout) findViewById(R.id.rl_custom);
        rl_custom.setOnClickListener(this);
        //我的关注
//        rl_focus= (RelativeLayout) findViewById(R.id.rl_my_focus);
//        rl_focus.setOnClickListener(this);

        rl_language_frame = (RelativeLayout) findViewById(R.id.rl_language_frame);
        rl_language_frame.setOnClickListener(this);
    /*    rl_setting_frame = (RelativeLayout) findViewById(R.id.rl_setting_frame);
        rl_setting_frame.setOnClickListener(this);*/
        rl_setting_frame1 = (ImageView) findViewById(R.id.rl_setting_frame);
        rl_setting_frame1.setOnClickListener(this);
        rl_user_feedback = (RelativeLayout) findViewById(R.id.rl_user_feedback);
        rl_user_feedback.setOnClickListener(this);

        /**我的定制红点*/
//        mRedDot = findViewById(R.id.custom_red_dot_view);
//        boolean currenRedDot = PreferenceUtil.getBoolean("custom_red_dot" , true);
//        if (currenRedDot) {
//            mRedDot.setVisibility(View.VISIBLE);
//        }else{
//            mRedDot.setVisibility(View.GONE);
//        }
        /**我的关注红点*/
//        mFocus_RedDot=findViewById(R.id.focus_red_dot_view);
//        mShowRedDot=PreferenceUtil.getBoolean(SHOW_RED,false);
//        if(mShowRedDot){
//            mFocus_RedDot.setVisibility(View.VISIBLE);
//        }else {
//            mFocus_RedDot.setVisibility(View.GONE);
//        }
        /*邀请码红点*/

        invited_red_dot_view = findViewById(R.id.invited_red_dot_view);
        mInvitedShowRedDot = PreferenceUtil.getBoolean(INVITED_SHOW_RED, true);
        if (mInvitedShowRedDot) {
            invited_red_dot_view.setVisibility(View.GONE);
        } else {
            invited_red_dot_view.setVisibility(View.GONE);
        }

        //订阅记录
        rl_my_subscribe = (RelativeLayout) findViewById(R.id.rl_my_subscribe);
        rl_my_subscribe.setOnClickListener(this);

        //推介文章
        rl_my_promotion = (RelativeLayout) findViewById(R.id.rl_my_promotion);
        rl_my_promotion.setOnClickListener(this);

        //申请专家
        rl_my_apply = (RelativeLayout) findViewById(R.id.rl_my_apply);
        rl_my_apply.setOnClickListener(this);

        //余额
        my_balance = (LinearLayout) findViewById(R.id.my_balance);
        my_balance.setOnClickListener(this);
        //收入
        my_income = (LinearLayout) findViewById(R.id.my_income);
        my_income.setOnClickListener(this);


        //账户明细
        account_details = (RelativeLayout) findViewById(R.id.account_details);
        account_details.setOnClickListener(this);


        //登录和为登录显示账户信息
        not_login_balance = (LinearLayout) findViewById(R.id.not_login_balance);
        not_login_payable = (LinearLayout) findViewById(R.id.not_login_payable);

        available_balance_rl = (LinearLayout) findViewById(R.id.available_balance_rl);
        cash_balance_payable_rl = (LinearLayout) findViewById(R.id.cash_balance_payable_rl);

        //可用余额
        available_balance = (TextView) findViewById(R.id.available_balance);
        //可提现余额
        cash_balance_payable = (TextView) findViewById(R.id.cash_balance_payable);

        //订阅个数
        subscribe_tv = (TextView) findViewById(R.id.subscribe_tv);
        //推荐文章个数
        promotion_tv = (TextView) findViewById(R.id.promotion_tv);

        recharge_bt = (TextView) findViewById(R.id.recharge_bt);
        recharge_bt.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_custom:
                if (DeviceInfo.isLogin()) {
                    PreferenceUtil.commitBoolean("custom_red_dot", false);
//                    mRedDot.setVisibility(View.GONE);
                    startActivity(new Intent(HomeUserOptionsActivity.this, CustomActivity.class));
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("custom", true); //传 true  表示我的定制进入登录  完成后直接进入定制界面
                    startActivity(intent);
                }
                break;
            case R.id.rl_language_frame:// 语言切换
                MobclickAgent.onEvent(HomeUserOptionsActivity.this, "LanguageChanger");
                Intent intent = new Intent(HomeUserOptionsActivity.this, HomeLanguageActivity.class);
                startActivity(intent);
                break;
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
                if (!DeviceInfo.isLogin()) {
                    goToLoginActivity();
                }


                break;
            case R.id.user_info_image: //用户信息
                MobclickAgent.onEvent(HomeUserOptionsActivity.this, "ProfileActivity_Start");
                if (DeviceInfo.isLogin()) {
                    startActivity(new Intent(this, ProfileActivity.class));
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                    //UiUtils.toast(MyApp.getInstance(), "请先登录");
                }

                break;
            case R.id.rl_my_subscribe: //订阅记录
                if (DeviceInfo.isLogin()) {
                    startActivity(new Intent(this, SubsRecordActivity.class));
                } else {
                    UiUtils.toast(getApplicationContext(), R.string.please_login_first);
                }
                break;

            case R.id.rl_my_promotion:     //推介文章
                if (DeviceInfo.isLogin()) {
                    //0 未审核  1.审核通过  2.审核中  3.审核不通过
                    if ("1".equals(AppConstants.register.getUser().getIsExpert())) {
                        startActivity(new Intent(this, RecommendArticlesActivity.class));
                    } else {
                        startActivity(new Intent(this, NotRecommendExpertActivity.class));
                    }

                } else {
                    UiUtils.toast(getApplicationContext(), R.string.please_login_first);
                }

                break;

            case R.id.rl_my_apply:     //申请专家
                if (DeviceInfo.isLogin()) {
                    Intent intent1 = new Intent(HomeUserOptionsActivity.this, ApplicationSpecialistActivity.class);
                    startActivity(intent1);

                } else {
                    UiUtils.toast(getApplicationContext(), R.string.please_login_first);
                }
                break;
            case R.id.my_balance:      //余额
                if (DeviceInfo.isLogin()) {


                } else {
                    UiUtils.toast(getApplicationContext(), R.string.please_login_first);
                }
                break;
            case R.id.my_income:    //收入
                if (DeviceInfo.isLogin()) {


                } else {
                    UiUtils.toast(getApplicationContext(), R.string.please_login_first);
                }
                break;
            case R.id.account_details:    //账户明细
                if (DeviceInfo.isLogin()) {

                    //startActivity(new Intent(this, AccountDetailActivity.class));
                    startActivity(new Intent(this, RecommendedExpertDetailsActivity.class));
                } else {
                    UiUtils.toast(getApplicationContext(), R.string.please_login_first);
                }
                break;
            case R.id.recharge_bt:    //充值
                if (DeviceInfo.isLogin()) {
                    startActivity(new Intent(this, MvpChargeMoneyActivity.class));

                } else {
                    UiUtils.toast(getApplicationContext(), R.string.please_login_first);
                }
                break;


            default:
                break;
        }
    }

    private void goToLoginActivity() {
        startActivityForResult(new Intent(this, LoginActivity.class), REQUESTCODE_LOGIN);
    }

    /**
     * 专家认证返回
     *
     * @param
     */
    public void onEventMainThread(SpecialistBean bean) {
        // 0 未审核  1.审核通过  2.审核中  3.审核不通过
        in_audit.setVisibility(View.VISIBLE);
        if (bean.getCode() == 1) {
            in_audit.setText("审核通过");
        } else if (bean.getCode() == 2) {
            in_audit.setText("审核中");
        } else if (bean.getCode() == 3) {
            in_audit.setText("审核不通过");
        } else if (bean.getCode() == 0) {
            in_audit.setText("未审核");
        }
        PreferenceUtil.commitInt(AppConstants.ISEXPERT,bean.getCode());
    }

    /**
     * 定制页面返回
     *
     * @param event
     */
    public void onEventMainThread(CustomEvent event) {
//        if (PreferenceUtil.getBoolean("custom_red_dot" , true)) {
//            mRedDot.setVisibility(View.VISIBLE);
//        }else{
//            mRedDot.setVisibility(View.GONE);
//        }
    }

    public void onEventMainThread(ChoseHeadStartBean choseHeadStartBean) {
        //ImageLoader.load(HomeUserOptionsActivity.this,choseHeadStartBean.startUrl,R.mipmap.center_head).into(mUser_image);
        Glide.with(getApplicationContext())
                .load(choseHeadStartBean.startUrl)
                .error(R.mipmap.center_head)
                .into(mUser_image);

        //Log.i("register","onResume=="+PreferenceUtil.getString(AppConstants.HEADICON,""));
    }


    public void onEventMainThread(Register register) {

        Glide.with(context)
                .load(register.getUser().getImageSrc())
                .error(R.mipmap.center_head)
                .into(mUser_image);
        mTv_nickname.setText(register.getUser().getNickName());
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);

        if (DeviceInfo.isLogin()) {
            // 0 未审核  1.审核通过  2.审核中  3.审核不通过

            login();

            if (AppConstants.register.getUser().getIsExpert() == 0) {
                in_audit.setText("未审核");
            } else if (AppConstants.register.getUser().getIsExpert() == 1) {
                in_audit.setText("审核通过");
            } else if (AppConstants.register.getUser().getIsExpert() == 2) {
                in_audit.setText("审核中");
            } else if (AppConstants.register.getUser().getIsExpert() == 3) {
                in_audit.setText("审核不通过");
            } else {
                in_audit.setText("");
            }

            available_balance_rl.setVisibility(View.VISIBLE);
            cash_balance_payable_rl.setVisibility(View.VISIBLE);
            available_balance.setText(UnitsUtil.fenToYuan(AppConstants.register.getUser().getAvailableBalance()));
            cash_balance_payable.setText(UnitsUtil.fenToYuan(AppConstants.register.getUser().getCashBalance()));
            subscribe_tv.setText(AppConstants.register.getUser().getBuyCount());
            promotion_tv.setText(AppConstants.register.getUser().getPushCount());
            not_login_balance.setVisibility(View.GONE);
            not_login_payable.setVisibility(View.GONE);
            mTv_nickname.setText(AppConstants.register.getUser().getNickName());


        } else {
            in_audit.setText("");
            available_balance_rl.setVisibility(View.GONE);
            cash_balance_payable_rl.setVisibility(View.GONE);
            not_login_balance.setVisibility(View.VISIBLE);
            not_login_payable.setVisibility(View.VISIBLE);
            subscribe_tv.setText("");
            promotion_tv.setText("");
            mTv_nickname.setText(R.string.Login_register);
            mUser_image.setImageResource(R.mipmap.center_head);
        }
    }





    /**
     * 登录
     */
    private void login() {

                final String url = BaseURLs.URL_LOGIN;

                Map<String, String> param = new HashMap<>();
                param.put("phoneNum", AppConstants.register.getUser().getPhoneNum());
                param.put("password", PreferenceUtil.getString("et_password", ""));

                if (MyApp.isLanguage.equals("rCN")) {
                    // 如果是中文简体的语言环境
                    language = "langzh";
                } else if (MyApp.isLanguage.equals("rTW")) {
                    // 如果是中文繁体的语言环境
                    language = "langzh-TW";
                }

                String sign = DeviceInfo.getSign("/user/login" + language + "password" + MD5Util.getMD5( PreferenceUtil.getString("et_password", "")) + "phoneNum" + AppConstants.register.getUser().getPhoneNum() + "timeZone8");
                param.put("sign", sign);
                setResult(RESULT_OK);

                VolleyContentFast.requestJsonByPost(url, param, new VolleyContentFast.ResponseSuccessListener<Register>() {
                    @Override
                    public void onResponse(Register register) {

                        progressBar.dismiss();
                        if (Integer.parseInt(register.getCode()) == AccountResultCode.SUCC) {

                            available_balance.setText(UnitsUtil.fenToYuan(register.getUser().getAvailableBalance()));
                            cash_balance_payable.setText(UnitsUtil.fenToYuan(register.getUser().getCashBalance()));

                        }else{

                            return;
                        }
                    }

                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {


                    }

                }, Register.class);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
}
