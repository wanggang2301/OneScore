package com.hhly.mlottery.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.cipher.MD5Util;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.net.account.AccountResultCode;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {


    private EditText et_username , et_password;
    private ImageView iv_eye;
    private ProgressDialog progressBar;
    private ImageView iv_delete;

    public static final int REQUESTCODE_FINDPW = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        initView();
    }


    @Override
    protected void onResume() {
        /**友盟页面统计*/
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("LoginActivity");
        super.onResume();

        // 自动弹出软键盘
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

    @Override
    public void onPause() {
        super.onPause();
        /**友盟页面统计*/
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("LoginActivity");
    }

    private void initView() {
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage(getResources().getString(R.string.logining));

        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.public_txt_title)).setText(R.string.login);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        iv_delete.setOnClickListener(this);

        iv_eye = (ImageView) findViewById(R.id.iv_eye);
        iv_eye.setOnClickListener(this);
        findViewById(R.id.tv_login).setOnClickListener(this);
        findViewById(R.id.public_img_back).setOnClickListener(this);


        et_username = (EditText) findViewById(R.id.et_username);

        et_username.addTextChangedListener(this);

        et_password = (EditText) findViewById(R.id.et_password);


        View tv_register = findViewById(R.id.tv_right);
        tv_register.setVisibility(View.VISIBLE);
        tv_register.setOnClickListener(this);

        findViewById(R.id.tv_forgetpw).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_img_back: // 返回
                MobclickAgent.onEvent(mContext, "LoginActivity_Exit");
                finish();
                break;
            case R.id.tv_right: // 注册
                MobclickAgent.onEvent(mContext, "RegisterActivity_Start");
                startActivityForResult(new Intent(this , RegisterActivity.class) , HomePagerActivity.REQUESTCODE_LOGIN);
                break;
            case R.id.iv_delete: // EditText 删除
                MobclickAgent.onEvent(mContext, "LoginActivity_UserName_Delete");
                et_username.setText("");
                break;
            case R.id.iv_eye:  // 显示密码
                MobclickAgent.onEvent(mContext, "LoginActivity_PassWord_isHide");
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
            case R.id.tv_login: // 登录
                MobclickAgent.onEvent(mContext, "LoginActivity_LoginOk");
                login();
                break;
            case R.id.tv_forgetpw:
                MobclickAgent.onEvent(mContext, "LoginActivity_FindPassWord");
                startActivityForResult(new Intent(this , FindPassWordActivity.class) , REQUESTCODE_FINDPW);
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

        if (UiUtils.isMobileNO(this ,userName)){
            if (UiUtils.checkPassword_JustLength(this , passWord)){
                // 登录
                progressBar.show();
                String url = BaseURLs.URL_LOGIN;
                Map<String, String> param = new HashMap<>();
                param.put("account" , userName);
                param.put("password" , MD5Util.getMD5(passWord));
                param.put("deviceToken" , AppConstants.deviceToken);

                VolleyContentFast.requestJsonByPost(url, param, new VolleyContentFast.ResponseSuccessListener<Register>() {
                    @Override
                    public void onResponse(Register register) {

                        progressBar.dismiss();

                        if (register.getResult() == AccountResultCode.SUCC){
                            UiUtils.toast(MyApp.getInstance(), R.string.login_succ);
                            CommonUtils.saveRegisterInfo(register);
                            setResult(RESULT_OK);
                            finish();
                        }else{
                            CommonUtils.handlerRequestResult(register.getResult() , register.getMsg());
                        }
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {

                        progressBar.dismiss();

                        L.e(TAG , " 登录失败");
                        UiUtils.toast(LoginActivity.this , R.string.immediate_unconection);
                    }
                } , Register.class);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (progressBar.isShowing()){
                L.d(TAG , " progressBar.isShowing() , return false");
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            switch (requestCode){
                case HomePagerActivity.REQUESTCODE_LOGIN:
                    L.i(TAG , "注册成功返回" );
                    setResult(RESULT_OK);
                    finish();
                    break;
                case REQUESTCODE_FINDPW:
                    L.i(TAG , "忘记密码成功返回" );
                    break;
            }
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
}

