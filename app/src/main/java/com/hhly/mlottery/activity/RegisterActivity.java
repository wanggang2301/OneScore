package com.hhly.mlottery.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.bean.account.SendSmsCode;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.impl.GetVerifyCodeCallBack;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CountDown;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.cipher.MD5Util;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.net.account.AccountResultCode;
import com.hhly.mlottery.util.net.account.OperateType;
import com.hhly.mlottery.util.net.account.RegisterType;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * 注册界面
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener, TextWatcher {


    public static final String TAG="RegisterActivity";
    private EditText et_username, et_password, et_verifycode;
    private TextView tv_register, tv_verycode;
    private ImageView iv_eye, iv_delete;


    /**
     * 倒计时 默认60s , 间隔1s
     */
    private CountDown countDown;
    public static final int TIMEOUT = 59699;
    public static final int TIMEOUT_INTERVEL = 1000;
    private ProgressDialog progressBar;

    //新需求，点击获取验证码的时候显示一个progressbar,服务器返回结果后再开始倒计时。
    private ProgressBar mProgressBar;
    private EditText invited_number;
    private LinearLayout invited;
    private String language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         switch (MyApp.isPackageName){

             case AppConstants.PACKGER_NAME_ZH:
                 //setContentView(R.layout.activity_overseas_register);
                 setContentView(R.layout.activity_register);
                 break;
             case AppConstants.PACKGER_NAME_TH:
                 setContentView(R.layout.activity_overseas_register);
                 break;
             case AppConstants.PACKGER_NAME_VN:
                 setContentView(R.layout.activity_overseas_register);
                 break;
             case AppConstants.PACKGER_NAME_VN_HN:
                 setContentView(R.layout.activity_overseas_register);
                 break;
             case AppConstants.PACKGER_NAME_UK:
                 setContentView(R.layout.activity_overseas_register);
                 break;
             default:
                 break;
         }


        initView();
        initData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        et_username.setFocusable(true);
        et_username.setFocusableInTouchMode(true);
        et_username.requestFocus();

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
        progressBar.setMessage(getResources().getString(R.string.registering));

        mProgressBar = (ProgressBar) findViewById(R.id.register_activity_pb);

        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.public_txt_title)).setText(R.string.register);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        iv_delete.setOnClickListener(this);

        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);
        findViewById(R.id.public_img_back).setOnClickListener(this);
        iv_eye = (ImageView) findViewById(R.id.iv_eye);
        iv_eye.setOnClickListener(this);

        et_username = (EditText) findViewById(R.id.et_username);
        et_username.addTextChangedListener(this);

        et_password = (EditText) findViewById(R.id.et_password);
        et_password.setTypeface(Typeface.SANS_SERIF);
        et_verifycode = (EditText) findViewById(R.id.et_verifycode);
        tv_verycode = (TextView) findViewById(R.id.tv_verycode);
        tv_verycode.setOnClickListener(this);

        //获取邀请码
        invited_number = (EditText) findViewById(R.id.invited_number);

        invited = (LinearLayout) findViewById(R.id.invited);
        invited.setVisibility(View.GONE);
    }

    private void initData() {

        countDown = CountDown.getDefault(TIMEOUT, TIMEOUT_INTERVEL, new CountDown.CountDownCallback() {
            @Override
            public void onFinish() {
                enableVeryCode();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                if (tv_verycode != null) {
                    if (tv_verycode.isClickable())
                        tv_verycode.setClickable(false);

                    //L.d(TAG, millisUntilFinished / CountDown.TIMEOUT_INTERVEL + "秒");
                    tv_verycode.setText(millisUntilFinished / CountDown.TIMEOUT_INTERVEL + "秒");
                }
            }
        });

    }

    /**
     * 重新获取验证码
     */
    private void enableVeryCode() {
        if (tv_verycode != null) {
            tv_verycode.setText(R.string.resend);
            tv_verycode.setClickable(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back: // 返回
                MobclickAgent.onEvent(mContext, "RegisterActivity_Exit");
                finish();
                break;
            case R.id.tv_register: // 注册
                MobclickAgent.onEvent(mContext, "RegisterActivity_RegisterOK");
                String userName = et_username.getText().toString();
                String passWord = et_password.getText().toString();
                String verifyCode = et_verifycode.getText().toString();

                if (AppConstants.isGOKeyboard){
                    if (UiUtils.checkPassword(this, passWord)) {
                        // 登录
//                            UiUtils.toast(this,"register");
                        register(userName, verifyCode, passWord);
                    }
                }else{
                    if (UiUtils.isMobileNO(this, userName)) {
                        if (UiUtils.checkVerifyCode(this, verifyCode)) {
                            if (UiUtils.checkPassword(this, passWord)) {
                                // 登录
//                            UiUtils.toast(this,"register");
                                register(userName, verifyCode, passWord);
                            }
                        }
                    }
                }

                break;
            case R.id.iv_delete: // EditText 删除
                MobclickAgent.onEvent(mContext, "RegisterActivity_UserName_Delete");
                et_username.setText("");
                break;
            case R.id.iv_eye:  // 显示密码
                MobclickAgent.onEvent(mContext, "RegisterActivity_PassWord_isHide");
                int inputType = et_password.getInputType();
                if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_eye.setImageResource(R.mipmap.close_eye);
                } else {
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_eye.setImageResource(R.mipmap.open_eye);
                }
                et_password.setTypeface(Typeface.SANS_SERIF);
                // 光标移动到结尾
                DeviceInfo.selectionLast(et_password);

                break;
            case R.id.tv_verycode:
                MobclickAgent.onEvent(mContext, "RegisterActivity_VeryCode");
                getVerifyCode();
                break;
            default:
                break;
        }

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

    /**
     * 注册
     *
     * @param userName
     * @param verifyCode
     * @param passWord
     */
    private void register(String userName, String verifyCode, final String passWord) {

        tv_register.setClickable(false);
        progressBar.show();
        Resources res = getResources();
     /*
     *
     * 限制注册密码规则
     *
     */
        if (UiUtils.checkPassword(RegisterActivity.this, passWord, false,
                res.getString(R.string.input_password_new),
                res.getString(R.string.new_pw) + res.getString(R.string.pwd_format))) {

            String url = BaseURLs.URL_REGISTER;
            Map<String, String> param = new HashMap<>();
            param.put("phoneNum", userName);
            param.put("password", MD5Util.getMD5(passWord));
            param.put("sms", verifyCode);
            if (MyApp.isLanguage.equals("rCN")) {
                // 如果是中文简体的语言环境
                language = "langzh";
            } else if (MyApp.isLanguage.equals("rTW")) {
                // 如果是中文繁体的语言环境
                language="langzh-TW";
            }
            String sign=DeviceInfo.getSign("/user/register"+language+"password"+MD5Util.getMD5(passWord)+"phoneNum"+userName+"sms"+verifyCode+"timeZone8");
            param.put("sign",sign);

            VolleyContentFast.requestJsonByPost(url, param, new VolleyContentFast.ResponseSuccessListener<Register>() {
                @Override
                public void onResponse(Register register) {

                    tv_register.setClickable(true);
                    progressBar.dismiss();

                    if (register != null && Integer.parseInt(register.getCode())== AccountResultCode.SUCC) {
                       DeviceInfo.saveRegisterInfo(register);
                        UiUtils.toast(MyApp.getInstance(), R.string.register_succ);
                        PreferenceUtil.commitString("et_password", passWord);
                        EventBus.getDefault().post(register);
                        //给服务器发送注册成功后用户id和渠道id（用来统计留存率）
                        //sendUserInfoToServer(register);

                        L.d(TAG, "注册成功");
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        countDown.cancel();
                        L.e(TAG, "成功请求，注册失败");
                        DeviceInfo.handlerRequestResult(Integer.parseInt(register.getCode()), "未知错误");
                        enableVeryCode();
                    }
                }
            }, new VolleyContentFast.ResponseErrorListener() {
                @Override
                public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                    countDown.cancel();
                    progressBar.dismiss();
                    tv_register.setClickable(true);
                    L.e(TAG, "注册失败");
                    UiUtils.toast(RegisterActivity.this, R.string.immediate_unconection);
                }
            }, Register.class);
        }
    }



    /**
     * 给服务器发送注册成功后用户id和渠道id（用来统计留存率）
     * @param register
     */
    private void sendUserInfoToServer(Register register) {

        String url = BaseURLs.USER_ACTION_ANALYSIS_URL;
        Map<String,String> pramas = new HashMap<>();
        pramas.put("appType","appRegist");
        pramas.put("userid",register.getUser().getUserId());
        String CHANNEL_ID;
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);

            if (appInfo.metaData != null) {
                CHANNEL_ID = appInfo.metaData.getString("UMENG_CHANNEL");
                pramas.put("channel", CHANNEL_ID);
            }else {
                pramas.put("channel","vnp56ams");
            }

        } catch (PackageManager.NameNotFoundException e) {
            pramas.put("channel","vnp56ams");
            e.printStackTrace();
        }

        VolleyContentFast.requestStringByPost(url, pramas, new VolleyContentFast.ResponseSuccessListener<String>() {
            @Override
            public void onResponse(String jsonObject) {
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
            }
        });
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {


        String phone = et_username.getText().toString();
        DeviceInfo.getVerifyCode(this, phone, OperateType.REGISTER, new GetVerifyCodeCallBack() {
            @Override
            public void beforGet() {
                //countDown.start();
                //隐藏软键盘，让progressbar显示出来

                InputMethodManager inputManager = (InputMethodManager) et_username.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(et_username.getWindowToken(), 0);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onGetResponce(SendSmsCode code) {
               // L.e(TAG, "code>>>>>>>>>>>" + code.getResult());
                mProgressBar.setVisibility(View.GONE);
                countDown.start();
//              // 正常情况下要1min后才能重新发验证码，但是遇到下面几种情况可以点击重发
                if (Integer.parseInt(code.getCode()) == AccountResultCode.SUCC) {

                    UiUtils.toast(MyApp.getInstance(), R.string.send_register_succ);
                } else if (Integer.parseInt(code.getCode())  == AccountResultCode.PHONE_ALREADY_EXIST
                        || Integer.parseInt(code.getCode())  == AccountResultCode.PHONE_FORMAT_ERROR
                        || Integer.parseInt(code.getCode())  == AccountResultCode.MESSAGE_SEND_FAIL
                        ||Integer.parseInt(code.getCode()) ==AccountResultCode.ONLY_FIVE_EACHDAY
                        ||Integer.parseInt(code.getCode()) ==AccountResultCode.USERNAME_EXIST) {
                    countDown.cancel();
                    //tv_verycode.setText(R.string.resend);
                    //tv_verycode.setClickable(true);
                    enableVeryCode();
                }
            }

            @Override
            public void onGetError(VolleyContentFast.VolleyException exception) {
                countDown.cancel();
                tv_verycode.setText(R.string.resend);
                tv_verycode.setClickable(true);
                enableVeryCode();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDown != null) {
            countDown.cancel();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) {
            iv_delete.setVisibility(View.GONE);
        } else {
            iv_delete.setVisibility(View.VISIBLE);
        }
    }
}
