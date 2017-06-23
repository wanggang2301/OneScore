package com.hhly.mlottery.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.IsTestLoginBean;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.ConstantPool;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.IssueResultEventBus;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.LoadingResultEventBusEntity;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.PayChargeMoneyResultEventBus;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.PayDetailsResultEventBus;
import com.hhly.mlottery.util.AccessTokenKeeper;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.FocusUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.ShareConstants;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.cipher.MD5Util;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.net.account.AccountResultCode;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;


/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    /**
     * 跳转其他Activity 的requestcode
     */
    private final int REQUESTCODE_LOGIN = 100;
    //    private final int REQUESTCODE_LOGOUT = 110;
    private final int REQUESTCODE_FINDPW = 200;

    private final String TAG = "LoginActivity";

    private EditText et_username, et_password;
    private ImageView iv_eye;
    private ProgressDialog progressBar;

    private LinearLayout mLogin_display;
    private ImageView mLogin_qq;
    private ImageView mLogin_sina;
    private ImageView mLogin_weixin;
    private boolean isCoustom;
    private boolean isBettingDetail;
    private TextView tv_forgetpw;
    private String language;
    private TextView login_more;
    private String inputParament;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getIntent().getExtras() != null) {
            isCoustom = getIntent().getBooleanExtra("custom", false);
            isBettingDetail = getIntent().getBooleanExtra(ConstantPool.BETTING_LOAD, false);
            inputParament = getIntent().getStringExtra(ConstantPool.PUBLIC_INPUT_PARAMEMT);
        }

        //应UI要求，把状态栏设置成透明的
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Timer().schedule(new TimerTask() { //让软键盘延时弹出，以更好的加载Activity
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) et_username.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_username, 0);
            }

        }, 300);

    }

    private void initView() {
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage(getResources().getString(R.string.logining));


        iv_eye = (ImageView) findViewById(R.id.iv_eye);
        iv_eye.setOnClickListener(this);
        findViewById(R.id.tv_login).setOnClickListener(this);
        findViewById(R.id.public_img_back).setOnClickListener(this);
        findViewById(R.id.iv_delete).setOnClickListener(this);

        et_username = (EditText) findViewById(R.id.et_username);
        et_username.setTypeface(Typeface.SANS_SERIF);
        et_username.addTextChangedListener(this);

        et_password = (EditText) findViewById(R.id.et_password);
        et_password.setTypeface(Typeface.SANS_SERIF);
        findViewById(R.id.tv_register).setOnClickListener(this);

        login_more = (TextView) findViewById(R.id.login_more);

        //第三方qq登录
        mLogin_qq = (ImageView) findViewById(R.id.login_qq);
        mLogin_qq.setOnClickListener(this);
        tv_forgetpw = (TextView) findViewById(R.id.tv_forgetpw);
        tv_forgetpw.setOnClickListener(this);

        //第三方新浪微博登录
        mLogin_sina = (ImageView) findViewById(R.id.login_sina);
        mLogin_sina.setOnClickListener(this);
        //第三方微信登录
        mLogin_weixin = (ImageView) findViewById(R.id.login_weixin);
        mLogin_weixin.setOnClickListener(this);

        //第三方登录图标显示
        mLogin_display = (LinearLayout) findViewById(R.id.login_display);

        switch (MyApp.isPackageName) {
            case AppConstants.PACKGER_NAME_ZH:
                tv_forgetpw.setVisibility(View.VISIBLE);
                break;
            case AppConstants.PACKGER_NAME_TH:
                tv_forgetpw.setVisibility(View.GONE);
                break;
            case AppConstants.PACKGER_NAME_VN:
                tv_forgetpw.setVisibility(View.GONE);
                break;
            case AppConstants.PACKGER_NAME_VN_HN:
                tv_forgetpw.setVisibility(View.GONE);
                break;
            case AppConstants.PACKGER_NAME_UK:
                tv_forgetpw.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back: // 返回
                MobclickAgent.onEvent(mContext, "LoginActivity_Exit");
                PreferenceUtil.commitString("code", "");
                finish();
                break;
            case R.id.tv_register: // 注册
                MobclickAgent.onEvent(mContext, "RegisterActivity_Start");
                startActivityForResult(new Intent(this, RegisterActivity.class), REQUESTCODE_LOGIN);
                break;
            case R.id.iv_delete: // EditText 删除
                MobclickAgent.onEvent(mContext, "LoginActivity_UserName_Delete");
                et_username.setText("");
                break;
            case R.id.iv_eye:  // 显示密码
                MobclickAgent.onEvent(mContext, "LoginActivity_PassWord_isHide");
                int inputType = et_password.getInputType();
                if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_eye.setImageResource(R.mipmap.new_close_eye);
                } else {
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                    iv_eye.setImageResource(R.mipmap.new_open_eye);
                }
                et_password.setTypeface(Typeface.SANS_SERIF);
                // 光标移动到结尾
                DeviceInfo.selectionLast(et_password);

                break;
            case R.id.tv_login: // 登录
                MobclickAgent.onEvent(mContext, "LoginActivity_LoginOk");
                login();
                // 关闭软键盘
                CyUtils.hideKeyBoard(this);
                break;
            case R.id.tv_forgetpw:
                MobclickAgent.onEvent(mContext, "LoginActivity_FindPassWord");
                startActivityForResult(new Intent(this, FindPassWordActivity.class), REQUESTCODE_FINDPW);
                break;
            default:
                break;
        }

    }



    /**
     * 登录
     */
    private void login() {
        String userName = et_username.getText().toString();
        String passWord = et_password.getText().toString();
        PreferenceUtil.commitString("et_password", passWord);
        if (userName.isEmpty()) {
            UiUtils.toast(LoginActivity.this, R.string.account_cannot_be_empty);
            return;
        } else {
            if (UiUtils.checkPassword_JustLength(this, passWord)) {
                // 登录
                progressBar.show();
                final String url = BaseURLs.URL_LOGIN;

                Map<String, String> param = new HashMap<>();
                param.put("phoneNum", userName);
                param.put("password", MD5Util.getMD5(passWord));

                if (MyApp.isLanguage.equals("rCN")) {
                    // 如果是中文简体的语言环境
                    language = "langzh";
                } else if (MyApp.isLanguage.equals("rTW")) {
                    // 如果是中文繁体的语言环境
                    language = "langzh-TW";
                }

                String sign = DeviceInfo.getSign("/user/login" + language + "password" + MD5Util.getMD5(passWord) + "phoneNum" + userName + "timeZone8");
                param.put("sign", sign);
                setResult(RESULT_OK);

                VolleyContentFast.requestJsonByPost(url, param, new VolleyContentFast.ResponseSuccessListener<Register>() {
                    @Override
                    public void onResponse(Register register) {

                        progressBar.dismiss();
                        if (Integer.parseInt(register.getCode()) == AccountResultCode.SUCC) {
                            login_more.setVisibility(View.GONE);

                            UiUtils.toast(MyApp.getInstance(), R.string.login_succ);
                            DeviceInfo.saveRegisterInfo(register);
                            PreferenceUtil.commitBoolean("three_login", false);
                            setResult(RESULT_OK);
                            //给服务器发送注册成功后用户id和渠道id（用来统计留存率）
                            sendUserInfoToServer(register);
                            if (isCoustom) {
//                                 PreferenceUtil.commitBoolean("custom_red_dot" , false);
                                startActivity(new Intent(LoginActivity.this, CustomActivity.class));
                            }
                            if (isBettingDetail) {
                                EventBus.getDefault().post(new LoadingResultEventBusEntity(true));
                            }

                            if (inputParament != null){loadResult(inputParament);}

                            finish();
                            EventBus.getDefault().post(register);
                            PreferenceUtil.commitString(AppConstants.HEADICON,register.getUser().getImageSrc());
                            FocusUtils.getFootballUserFocus(register.getUser().getUserId());
                            FocusUtils.getBasketballUserConcern(register.getUser().getUserId());

                        } else if (Integer.parseInt(register.getCode()) == AccountResultCode.USERNAME_PASS_ERROR) {

                            if (Integer.parseInt(register.getFailureAmount()) == 3) {

                                login_more.setVisibility(View.VISIBLE);
                            }
                            UiUtils.toast(MyApp.getInstance(), R.string.username_pass_error);

                        } else {
                            DeviceInfo.handlerRequestResult(Integer.parseInt(register.getCode()), "系统错误");
                            login_more.setVisibility(View.GONE);
                        }
                    }

                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        progressBar.dismiss();
                        L.e(TAG, " 登录失败");
                        UiUtils.toast(LoginActivity.this, R.string.foot_neterror);
                        login_more.setVisibility(View.GONE);

                    }

                }, Register.class);
            }

        }

    }

    /**
     * 调取登录页面完成登录后反回处理
     */
    private void loadResult(String type){

        switch (type){
            case ConstantPool.PAY_DETAILS_RESULT://返回支付
                EventBus.getDefault().post(new PayDetailsResultEventBus(true));
                break;
            case ConstantPool.PAY_CHARGE_MONEY_RESULT://返回充值
                EventBus.getDefault().post(new PayChargeMoneyResultEventBus(true));
                break;
            case ConstantPool.PAY_ISSUE_RESULT://返回发布
                EventBus.getDefault().post(new IssueResultEventBus(true));
                break;

        }
    }


    private void sendUserInfoToServer(Register register) {
        final String url = BaseURLs.USER_ACTION_ANALYSIS_URL;
        final Map<String, String> params = new HashMap<>();
        params.put("userid", register.getUser().getUserId());
        String CHANNEL_ID;
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);

            if (appInfo.metaData != null) {
                CHANNEL_ID = appInfo.metaData.getString("UMENG_CHANNEL");
                params.put("appType", "appLogin");
                params.put("channel", CHANNEL_ID);
            } else {
                //获取不到渠道号id的时候
                params.put("appType", "appLoginInfo");
                //没有渠道号id的话，服务器指定要传这个“vnp56ams”参数
                params.put("channel", "vnp56ams");
            }

        } catch (PackageManager.NameNotFoundException e) {
            params.put("appType", "appLoginInfo");
            params.put("channel", "vnp56ams");
            e.printStackTrace();
        }

        VolleyContentFast.requestStringByPost(url, params, new VolleyContentFast.ResponseSuccessListener<String>() {
            @Override
            public void onResponse(String jsonObject) {
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (progressBar.isShowing()) {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case HomeUserOptionsActivity.REQUESTCODE_LOGIN:
                    L.i(TAG, "注册成功返回");
                    setResult(RESULT_OK);
                    finish();
                    break;
                case REQUESTCODE_FINDPW:
                    L.i(TAG, "忘记密码成功返回");
                    break;
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

