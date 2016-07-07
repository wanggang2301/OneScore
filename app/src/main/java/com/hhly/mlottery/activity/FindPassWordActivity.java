package com.hhly.mlottery.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.account.BaseBean;
import com.hhly.mlottery.bean.account.SendSmsCode;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.impl.GetVerifyCodeCallBack;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.CountDown;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.cipher.MD5Util;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.net.account.AccountResultCode;
import com.hhly.mlottery.util.net.account.AccountType;
import com.hhly.mlottery.util.net.account.OperateType;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 找回密码界面
 */
public class FindPassWordActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private static final java.lang.String TAG = "FindPassWordActivity";
    public static final java.lang.String PHONE = "phone";
    public static final java.lang.String VERIFYCODE = "verifycode";
    private static final int RESET_PW = 300;
    private EditText et_username , et_verifycode ,et_password;
    private TextView tv_verycode ,tv_comfirm;
    private ImageView iv_delete , iv_eye;
    private ProgressDialog progressBar;

    private ProgressBar mProgressBar;

    /**
     * 倒计时 默认60s , 间隔1s
     */
    private CountDown countDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass_word);

        initView();
        
    }

    @Override
    protected void onResume() {
        /**友盟页面统计*/
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        et_username.setFocusable(true);
        et_username.setFocusableInTouchMode(true);
        et_username.requestFocus();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() { //让软键盘延时弹出，以更好的加载Activity
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) et_username.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_username, 0);
            }

        }, 300);
    }

    private void initView() {

        countDown = CountDown.getDefault(new CountDown.CountDownCallback() {
            @Override
            public void onFinish() {
                enableVeryCode();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                if (tv_verycode != null){
                    if (tv_verycode.isClickable())
                        tv_verycode.setClickable(false);

                    L.d(TAG,millisUntilFinished/ CountDown.TIMEOUT_INTERVEL + "秒");
                    tv_verycode.setText(millisUntilFinished/ CountDown.TIMEOUT_INTERVEL + "秒");
                }
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.findpassword_activity_pb);

        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.public_txt_title)).setText(R.string.find_pw);

        findViewById(R.id.public_img_back).setOnClickListener(this);

        et_username = (EditText) findViewById(R.id.et_username);
        et_username.addTextChangedListener(this);


        et_verifycode = (EditText) findViewById(R.id.et_verifycode);
        tv_verycode = (TextView) findViewById(R.id.tv_verycode);
        tv_verycode.setOnClickListener(this);

        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        iv_delete.setOnClickListener(this);

        tv_comfirm = (TextView) findViewById(R.id.tv_comfirm);
        tv_comfirm.setOnClickListener(this);

        et_password = (EditText) findViewById(R.id.et_password);
        iv_eye = (ImageView) findViewById(R.id.iv_eye);
        iv_eye.setOnClickListener(this);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage(getResources().getString(R.string.reseting_password));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_img_back: // 返回
                MobclickAgent.onEvent(mContext, "FindPassWordActivity_Exit");
                finish();
                break;
            case R.id.iv_delete: // EditText 删除
                MobclickAgent.onEvent(mContext, "FindPassWordActivity_UserName_Delete");
                et_username.setText("");
                break;
            case R.id.tv_verycode: // 获取验证码
                MobclickAgent.onEvent(mContext, "FindPassWordActivity_VeryCode");
                getVerifyCode();
                break;
            case R.id.iv_eye:  // 显示密码
                MobclickAgent.onEvent(mContext, "FindPassWordActivity_PassWord_isHide");
                int inputType = et_password.getInputType();
                if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_eye.setImageResource(R.mipmap.close_eye);
                }else{
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_eye.setImageResource(R.mipmap.open_eye);
                }

                // 光标移动到结尾
                CommonUtils.selectionLast(et_password);
                break;
            case R.id.tv_comfirm:
                MobclickAgent.onEvent(mContext, "FindPassWordActivity_Reset_Password_Confirm");
                modifyPassword();
                break;

            default:
                break;
        }
    }


    private void modifyPassword() {

        String phone = et_username.getText().toString();
        String verifyCode = et_verifycode.getText().toString();
        String pwd = et_password.getText().toString();

        if (UiUtils.isMobileNO(this , phone)){
            if (UiUtils.checkVerifyCode(this , verifyCode)){

                if (UiUtils.checkPassword(this , pwd)){
                    progressBar.show();

                    String url = BaseURLs.URL_RESETPASSWORD;
                    Map<String, String> param = new HashMap<>();
                    param.put("phone" , phone);
                    param.put("accountType" , AccountType.TYPE_PHONE);
                    param.put("newPassword" , MD5Util.getMD5(pwd));
                    param.put("smsCode" , verifyCode);

                    VolleyContentFast.requestJsonByPost(url,param, new VolleyContentFast.ResponseSuccessListener<BaseBean>() {
                        @Override
                        public void onResponse(BaseBean reset) {
                            progressBar.dismiss();

                            if (reset != null&& reset.getResult() == AccountResultCode.SUCC){
                                UiUtils.toast(MyApp.getInstance(), R.string.reset_password_succ);
                                L.d(TAG,"重置密码成功");
                                setResult(RESULT_OK);
                                finish();
                            }else{
                                L.e(TAG,"成功请求，重置密码失败");
                                CommonUtils.handlerRequestResult(reset.getResult() , reset.getMsg());
                            }
                        }
                    }, new VolleyContentFast.ResponseErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                            progressBar.dismiss();
                            L.e(TAG,"重置密码失败");
                            UiUtils.toast(FindPassWordActivity.this , R.string.immediate_unconection);
                        }
                    } , BaseBean.class);
                }
            }
        }
    }

    private void getVerifyCode() {
        String phone = et_username.getText().toString();
        CommonUtils.getVerifyCode(this, phone, OperateType.TYPE_FORGET_PASSWORD ,new GetVerifyCodeCallBack() {
            @Override
            public void beforGet() {
                //countDown.start();

                InputMethodManager inputManager = (InputMethodManager) et_username.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(et_username.getWindowToken(), 0);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onGetResponce(SendSmsCode code) {

                mProgressBar.setVisibility(View.GONE);
                countDown.start();
                // 正常情况下要1min后才能重新发验证码，但是遇到下面几种情况可以点击重发
                if (code.getResult() == AccountResultCode.SUCC) {
                    UiUtils.toast(MyApp.getInstance(), R.string.send_register_succ);
                } else if(code.getResult() == AccountResultCode.PHONE_FORMAT_ERROR
                        || code.getResult() == AccountResultCode.MESSAGE_SEND_FAIL
                        ||code.getResult()==AccountResultCode.ONLY_FIVE_EACHDAY
                        ||code.getResult()==AccountResultCode.USER_NOT_EXIST
                        ){
                    resetCountDown();
                }
            }

            @Override
            public void onGetError(VolleyContentFast.VolleyException exception) {
                resetCountDown();
            }
        });
    }

    private void resetCountDown() {
        cancelCountDown();
        enableVeryCode();
    }

    private void cancelCountDown(){
        if (countDown != null){
            countDown.cancel();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)){
            iv_delete.setVisibility(View.GONE);
        }else{
            iv_delete.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 重新获取验证码
     */
    private void enableVeryCode() {
        if (tv_verycode != null){
            tv_verycode.setText(R.string.resend);
            tv_verycode.setClickable(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            switch (requestCode){
                case RESET_PW:
                    L.d(TAG , " 重置密码成功返回");
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    }
}
