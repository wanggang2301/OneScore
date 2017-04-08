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
import android.util.Log;
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
import com.hhly.mlottery.util.AccessTokenKeeper;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.CyUtils;
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
    public static final int REQUESTCODE_LOGIN = 100;
    public static final int REQUESTCODE_LOGOUT = 110;

    private EditText et_username, et_password;
    private ImageView iv_eye;
    private ProgressDialog progressBar;

    public static final String TAG = "LoginActivity";

    public static final int REQUESTCODE_FINDPW = 200;
    private Tencent tencent;
    private IUiListener mIUiListener;
    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;
    private Oauth2AccessToken mOauth2AccessToken;
    public static IWXAPI mApi;
    private String mWeixincode;

    private final static int ALL_CLOSE = 1111;
    private final static int QQ_CLOSE = 111;
    private final static int WEIXIN_CLOSE = 222;
    private final static int SINA_CLOSE = 333;
    private final static int QQ_DISPLAY = 11;
    private final static int WEIXIN_DISPLAY = 22;
    private final static int SINA_DISPLAY = 33;
    private Handler mViewHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case QQ_DISPLAY:
                    mLogin_qq.setVisibility(View.VISIBLE);
                    mLogin_display.setVisibility(View.VISIBLE);
                    break;
                case WEIXIN_DISPLAY:
                    mLogin_weixin.setVisibility(View.VISIBLE);
                    mLogin_display.setVisibility(View.VISIBLE);
                    break;
                case SINA_DISPLAY:
                    mLogin_sina.setVisibility(View.VISIBLE);
                    mLogin_display.setVisibility(View.VISIBLE);
                    break;
                case QQ_CLOSE:
                    mLogin_qq.setVisibility(View.GONE);
                    mLogin_display.setVisibility(View.VISIBLE);
                    break;
                case WEIXIN_CLOSE:
                    mLogin_weixin.setVisibility(View.GONE);
                    mLogin_display.setVisibility(View.VISIBLE);
                    break;

                case SINA_CLOSE:
                    mLogin_sina.setVisibility(View.GONE);
                    mLogin_display.setVisibility(View.VISIBLE);
                    break;
                case ALL_CLOSE:
                    mLogin_display.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };
    private LinearLayout mLogin_display;
    private LinearLayout mLogin_three;
    private ImageView mLogin_qq;
    private ImageView mLogin_sina;
    private ImageView mLogin_weixin;
    private boolean isCoustom;
    private TextView tv_forgetpw;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_login);
        if (getIntent().getExtras() != null) {
            isCoustom = getIntent().getBooleanExtra("custom" , false);
        }

        //应UI要求，把状态栏设置成透明的
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        initView();
        TestingIsLogin();
    }

    private void TestingIsLogin() {

        VolleyContentFast.requestJsonByGet(BaseURLs.TESTINGISLOGIN, new VolleyContentFast.ResponseSuccessListener<IsTestLoginBean>() {
            @Override
            public void onResponse(IsTestLoginBean register) {


                if(register.data==null){

                   return;
                }else {

                    if (register.data.qq == 1) {
                        mViewHandler.sendEmptyMessage(QQ_DISPLAY);
                    }else{
                        mViewHandler.sendEmptyMessage(QQ_CLOSE);
                    }
                    if (register.data.weChat == 1){
                        mViewHandler.sendEmptyMessage(WEIXIN_DISPLAY);
                    }else{
                        mViewHandler.sendEmptyMessage(WEIXIN_CLOSE);
                    }
                    if (register.data.weibo == 1){
                        mViewHandler.sendEmptyMessage(SINA_DISPLAY);
                    }else{
                        mViewHandler.sendEmptyMessage(SINA_CLOSE);
                    }
                    if(register.data.qq != 1&&register.data.weibo != 1&&register.data.weChat != 1){
                        mViewHandler.sendEmptyMessage(ALL_CLOSE);
                    }
                }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                progressBar.dismiss();
                L.e(TAG, " 请求失败");
                // UiUtils.toast(LoginActivity.this, exception.toString());
                mViewHandler.sendEmptyMessage(ALL_CLOSE);
            }
        }, IsTestLoginBean.class);

    }


    @Override
    protected void onResume() {
        super.onResume();
        // UiUtils.toast(MyApp.getInstance(), "我是登录页面LoginActivity");
      /*  // 自动弹出软键盘
        et_username.setFocusable(true);
        et_username.setFocusableInTouchMode(true);
        et_username.requestFocus();*/

        Timer timer = new Timer();
        timer.schedule(new TimerTask() { //让软键盘延时弹出，以更好的加载Activity
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) et_username.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_username, 0);
            }

        }, 300);

        if (!PreferenceUtil.getString("code", "").isEmpty()) {
            mWeixincode = PreferenceUtil.getString("code", "");
            //UiUtils.toast(MyApp.getInstance(), "mWeixincode" + mWeixincode);
            getAccessTokenFromWeiXin();
        }
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
        mLogin_three = (LinearLayout) findViewById(R.id.login_three);

        switch (MyApp.isPackageName){
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
                CommonUtils.selectionLast(et_password);

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
            case R.id.login_qq:
                Log.i(TAG, "点击我了>>>>>>>>>>");

                QQlogin();
                break;

            case R.id.login_sina:
                isSinaLogin();
                break;
            case R.id.login_weixin:

                WeiXinLogin();

                break;

            default:
                break;
        }

    }

    /*第三方微信登录*/
    private void WeiXinLogin() {

        //api注册
        mApi = WXAPIFactory.createWXAPI(this, ShareConstants.WE_CHAT_APP_ID, true);
        mApi.registerApp(ShareConstants.WE_CHAT_APP_ID);

        if (!mApi.isWXAppInstalled()) {
            //提醒用户没有按照微信
            Toast.makeText(LoginActivity.this, R.string.login_no_weixin, Toast.LENGTH_SHORT).show();
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        //授权读取用户信息
        req.scope = "snsapi_userinfo";
        //自定义信息
        req.state = "wechat_sdk_demo_test";
        //向微信发送请求
        mApi.sendReq(req);
        // getAccessTokenFromWeiXin();
    }

    /*获取微信信息
      * 通过授权页面获取相应的code   然后请求微信官方接口 获取相信的token 和 id
      * https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
      *
      */
    public void getAccessTokenFromWeiXin() {

        String requestUrlAppId = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + ShareConstants.WE_CHAT_APP_ID;
        String requestUrlAppSecret = "&secret=" + ShareConstants.KEY_WEIXIN_APP_SECRET;
        String requestUrlCode = "&code=" + mWeixincode;
        String requestUrlLast = "&grant_type=authorization_code";

        String requestUrl = requestUrlAppId + requestUrlAppSecret + requestUrlCode + requestUrlLast;

        VolleyContentFast.requestStringNoLanguageByGet(requestUrl, null, new VolleyContentFast.ResponseSuccessListener<String>() {

            @Override
            public void onResponse(String jsonObject) {

                JSONObject jo = JSON.parseObject(jsonObject);
                //UiUtils.toast(MyApp.getInstance(), jo.toString());
                String access_token = jo.getString("access_token");
                String openid = jo.getString("openid");
                Map<String, String> param = new HashMap<>();
                param.put("accessToken", access_token);
                param.put("uid", openid);
                param.put("ip", CommonUtils.getIpAddress());
                param.put("deviceToken", CommonUtils.getDeviceToken());
                //调用公共的登录请求
                requestLogin(BaseURLs.URL_WEIXIN_LOGIN, param);

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                UiUtils.toast(LoginActivity.this, R.string.login_peak);
            }
        });


    }

    /*第三方QQ登录*/
    private void QQlogin() {

        tencent = Tencent.createInstance(ShareConstants.QQ_APP_ID, mContext);
        if (!tencent.isSessionValid()) {
            mIUiListener = new BaseUiListener();
            tencent.login(this, ShareConstants.SCOPE, mIUiListener);
        } else {
            tencent.logout(this);
        }
    }

    private void isSinaLogin() {
        // Weibo.
        mAuthInfo = new AuthInfo(mContext, ShareConstants.SINA, ShareConstants.REDIRECT_URL1, ShareConstants.SCOPE);
        mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);
        mSsoHandler.authorize(new AuthDialogListener());
        mOauth2AccessToken = AccessTokenKeeper.readAccessToken(LoginActivity.this);
    }

    /**
     * 新浪回调
     *
     * @author luyao
     */
    private class AuthDialogListener implements WeiboAuthListener {


        @Override
        public void onComplete(Bundle bundle) {

            if (bundle == null) {
                return;
            }

            //进行下一步授权
            mOauth2AccessToken = Oauth2AccessToken.parseAccessToken(bundle);
            //验证我们的令牌是否有效
            if (mOauth2AccessToken.isSessionValid()) {
                // UiUtils.toast(MyApp.getInstance(),"build"+bundle.toString());
                AccessTokenKeeper.writeAccessToken(LoginActivity.this, mOauth2AccessToken);
                Log.i(TAG, "认证成功>>>>>>>>>>>>>");
                String openID = bundle.getString("uid");
                String accessToken = bundle.getString("access_token");
                Map<String, String> param = new HashMap<>();
                param.put("accessToken", accessToken);
                param.put("uid", openID);
                param.put("ip", CommonUtils.getIpAddress());
                param.put("deviceToken", CommonUtils.getDeviceToken());
                requestLogin(BaseURLs.URL_SINA_LOGIN, param);

            } else {

                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                // String code = values.getString("code");
            }

        }

        @Override
        public void onWeiboException(WeiboException e) {
            Log.e(TAG, "认证失败>>>>>>>>>>>>>" + e.toString());

        }

        @Override
        public void onCancel() {
            Log.i(TAG, "认证取消>>>>>>>>>>>>>");
        }
    }

    /**
     * QQ回调
     *
     * @author luyao
     */
    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            Log.i(TAG, "回调成功>>>>>>>>>>>>>");
            // UiUtils.toast(MyApp.getInstance(), "三登录成功");
            if (response == null) {
                return;
            }

            try {

                String jsonString = response.toString();

                JSONObject jo = JSON.parseObject(jsonString);

                int ret = jo.getIntValue("ret");
                if (ret == 0) {
                    String openID = jo.getString("openid");
                    String accessToken = jo.getString("access_token");
                    String expires = jo.getString("expires_in");
                    tencent.setOpenId(openID);
                    tencent.setAccessToken(accessToken, expires);
                    Map<String, String> param = new HashMap<>();
                    param.put("accessToken", accessToken);
                    param.put("uid", openID);
                    param.put("ip", CommonUtils.getIpAddress());
                    param.put("deviceToken", CommonUtils.getDeviceToken());
                    /*将所需参数传给后台*/
                    requestLogin(BaseURLs.URL_QQ_LOGIN, param);

                }

            } catch (Exception e) {
                UiUtils.toast(LoginActivity.this, R.string.login_peak);
                UiUtils.toast(mContext, "e>>" + e.toString());

            }

        }

        @Override
        public void onError(UiError e) {
            Log.i(TAG, "回调失败>>>>>>>>>>>>>");
        }

        @Override
        public void onCancel() {
            Log.i(TAG, "回调取消>>>>>>>>>>>>>");
        }
    }

    /*公用登录请求*/
    public void requestLogin(String url, Map<String, String> param) {
        progressBar.show();
        VolleyContentFast.requestJsonByPost(url, param, new VolleyContentFast.ResponseSuccessListener<Register>() {
            @Override
            public void onResponse(Register register) {
                progressBar.dismiss();
                if (register == null) {
                    return;
                }
                UiUtils.toast(MyApp.getInstance(), R.string.login_succ);
                CommonUtils.saveRegisterInfo(register);
                Log.e(TAG, "register" + register.toString());
                //UiUtils.toast(MyApp.getInstance(), register.toString());
                PreferenceUtil.commitBoolean("three_login", true);
                PreferenceUtil.commitString("code", "");
                setResult(RESULT_OK);
                //给服务器发送注册成功后用户id和渠道id（用来统计留存率）
                sendUserInfoToServer(register);
                finish();
                if (register.getResult() == AccountResultCode.SUCC) {
                    UiUtils.toast(MyApp.getInstance(), R.string.login_succ);
                    CommonUtils.saveRegisterInfo(register);
                    EventBus.getDefault().post(register);
                    setResult(RESULT_OK);
                    //给服务器发送注册成功后用户id和渠道id（用来统计留存率）
                    sendUserInfoToServer(register);
                    FocusUtils.getFootballUserFocus(register.getData().getUser().getUserId());
                    FocusUtils.getBasketballUserConcern(register.getData().getUser().getUserId());
                    finish();
                } else {
                    CommonUtils.handlerRequestResult(register.getResult(), register.getMsg());
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                progressBar.dismiss();
                L.e(TAG, " 登录失败");
                UiUtils.toast(LoginActivity.this, R.string.login_peak);
                // UiUtils.toast(LoginActivity.this, exception.toString());
            }
        }, Register.class);


    }

    /**
     * 登录
     */
    private void login() {
        String userName = et_username.getText().toString();
        String passWord = et_password.getText().toString();
        Log.i("dasdasdas","userName==="+userName);
         if(userName.isEmpty()) {
             UiUtils.toast(LoginActivity.this, R.string.account_cannot_be_empty);
             return;
         }else{
             if (UiUtils.checkPassword_JustLength(this, passWord)) {
                 // 登录
                 progressBar.show();
                 final String url = BaseURLs.URL_LOGIN;
                 Map<String, String> param = new HashMap<>();
                 param.put("account", userName);
                 param.put("password", MD5Util.getMD5(passWord));
                 setResult(RESULT_OK);
                 Log.d(TAG, AppConstants.deviceToken);
                 param.put("deviceToken", AppConstants.deviceToken);

                 //防止用户恶意注册后先添加的字段，versioncode和versionname;
                 int versionCode = CommonUtils.getVersionCode();
                 param.put("versionCode", String.valueOf(versionCode));

                 Log.d(TAG, versionCode + "");

                 String versionName = CommonUtils.getVersionName();
                 param.put("versionName", versionName);
                 Log.d(TAG, versionName);

                 VolleyContentFast.requestJsonByPost(url, param, new VolleyContentFast.ResponseSuccessListener<Register>() {
                     @Override
                     public void onResponse(Register register) {

                         progressBar.dismiss();
                         if (register.getResult() == AccountResultCode.SUCC) {

                             UiUtils.toast(MyApp.getInstance(), R.string.login_succ);
                             CommonUtils.saveRegisterInfo(register);
                             PreferenceUtil.commitBoolean("three_login", false);
                             setResult(RESULT_OK);
                             //给服务器发送注册成功后用户id和渠道id（用来统计留存率）
                             sendUserInfoToServer(register);
                             PreferenceUtil.commitString(AppConstants.SPKEY_LOGINACCOUNT, register.getData().getUser().getLoginAccount());
                             if (isCoustom) {
//                                 PreferenceUtil.commitBoolean("custom_red_dot" , false);
                                 startActivity(new Intent(LoginActivity.this, CustomActivity.class));
                             }
                             finish();
                             EventBus.getDefault().post(register);
                             FocusUtils.getFootballUserFocus(register.getData().getUser().getUserId());
                             FocusUtils.getBasketballUserConcern(register.getData().getUser().getUserId());

                         } else {
                             CommonUtils.handlerRequestResult(register.getResult(), register.getMsg());
                         }
                     }
                 }, new VolleyContentFast.ResponseErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                         progressBar.dismiss();
                         L.e(TAG, " 登录失败");
                         UiUtils.toast(LoginActivity.this, R.string.foot_neterror);


                     }
                 }, Register.class);
             }

         }

    }


    private void sendUserInfoToServer(Register register) {
        final String url = BaseURLs.USER_ACTION_ANALYSIS_URL;
        final Map<String, String> params = new HashMap<>();
        params.put("userid", register.getData().getUser().getUserId());
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
                L.d(TAG, " progressBar.isShowing() , return false");
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.i(TAG, "我进来了");
        Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        if (resultCode == RESULT_OK) {
            L.i(TAG, "进来了");
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
        PreferenceUtil.commitString("code", "");
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
       /* if (TextUtils.isEmpty(s)){
            iv_delete.setVisibility(View.GONE);
        }else{
            iv_delete.setVisibility(View.VISIBLE);
        }*/
    }
}

