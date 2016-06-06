package com.hhly.mlottery.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.account.BaseBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.cipher.MD5Util;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.net.account.AccountResultCode;
import com.hhly.mlottery.util.net.account.AccountType;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 重置密码
 */
public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_password ;
    private ImageView iv_eye;
    private TextView tv_comfirm;

    private ProgressDialog progressBar;
    private String phone;
    private String verifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initView();
        initData();
    }

    @Override
    protected void onResume() {
        /**友盟页面统计*/
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        et_password.setFocusable(true);
        et_password.setFocusableInTouchMode(true);
        et_password.requestFocus();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() { //让软键盘延时弹出，以更好的加载Activity
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) et_password.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_password, 0);
            }

        }, 300);
    }


    private void initData() {
        phone = getIntent().getStringExtra(FindPassWordActivity.PHONE);
        verifyCode = getIntent().getStringExtra(FindPassWordActivity.VERIFYCODE);
    }


    private void initView() {

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage(getResources().getString(R.string.reseting_password));

        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.public_txt_title)).setText(R.string.reset_password);

        tv_comfirm = (TextView) findViewById(R.id.tv_comfirm);
        tv_comfirm.setOnClickListener(this);
        findViewById(R.id.public_img_back).setOnClickListener(this);

        et_password = (EditText) findViewById(R.id.et_password);
        iv_eye = (ImageView) findViewById(R.id.iv_eye);
        iv_eye.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_img_back: // 返回
                MobclickAgent.onEvent(mContext, "ResetPasswordActivity_Exit");
                finish();
                break;
            case R.id.iv_eye:  // 显示密码
                MobclickAgent.onEvent(mContext, "ResetPasswordActivity_PassWord_isHide");
                int inputType = et_password.getInputType();
                if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_eye.setImageResource(R.mipmap.close_eye);
                }else{
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_eye.setImageResource(R.mipmap.open_eye);
                }
                // 光标移动到结尾
                String pwd = et_password.getText().toString();
                if(!TextUtils.isEmpty(pwd)){
                    et_password.setSelection(pwd.length());
                }
                break;
            case R.id.tv_comfirm:
                MobclickAgent.onEvent(mContext, "ResetPasswordActivity_Reset_Password_Confirm");
                modifyPassword();
                break;
            default:
                break;
        }
    }

    private void modifyPassword() {
        String pwd = et_password.getText().toString();
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
                    UiUtils.toast(ResetPasswordActivity.this , R.string.immediate_unconection);
                }
            } , BaseBean.class);
        }
    }
}
