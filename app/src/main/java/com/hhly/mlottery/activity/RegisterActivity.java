package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.bean.account.SendSmsCode;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.CountDown;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.AccountType;
import com.hhly.mlottery.util.net.OperateType;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册界面
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_username , et_password ,et_verifycode;
    private TextView tv_register , tv_verycode;
    private ImageView iv_eye;

    private CountDown countDown;
    private static final int TIMEOUT = 30499;
    private static final int TIMEOUT_INTERVEL = 1000;
    private SwipeRefreshLayout swiperefreshlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        initView();
        initData();
    }

    private void initView() {

        swiperefreshlayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swiperefreshlayout.setColorSchemeResources(R.color.bg_header);
        swiperefreshlayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(mContext, StaticValues.REFRASH_OFFSET_END));

        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.public_txt_title)).setText(R.string.register);
        findViewById(R.id.iv_delete).setOnClickListener(this);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);
        findViewById(R.id.public_img_back).setOnClickListener(this);
        iv_eye = (ImageView) findViewById(R.id.iv_eye);
        iv_eye.setOnClickListener(this);

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_verifycode = (EditText) findViewById(R.id.et_verifycode);
        tv_verycode = (TextView) findViewById(R.id.tv_verycode);
        tv_verycode.setOnClickListener(this);

    }

    private void initData() {
        countDown = new CountDown(TIMEOUT, TIMEOUT_INTERVEL, new CountDown.CountDownCallback() {
            @Override
            public void onFinish() {
                tv_verycode.setText(R.string.resend);
                tv_verycode.setClickable(true);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                if (tv_verycode.isClickable())
                    tv_verycode.setClickable(false);

                L.d(TAG,millisUntilFinished/ TIMEOUT_INTERVEL + "秒");
                tv_verycode.setText(millisUntilFinished/ TIMEOUT_INTERVEL + "秒");
            }
        });
    }

    boolean refresh = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_img_back: // 返回
                finish();
                break;
            case R.id.tv_register: // 注册

                swiperefreshlayout.setRefreshing(!refresh);

                String userName = et_username.getText().toString();
                String passWord = et_password.getText().toString();
                String verifyCode = et_verifycode.getText().toString();

                if (UiUtils.isMobileNO(this ,userName)){
                    if (UiUtils.checkVerifyCode(this , verifyCode)){
                        if (UiUtils.checkPassword(this , passWord)){
                            // 登录
//                            UiUtils.toast(this,"register");
                            register(userName , verifyCode , passWord);
                        }
                    }
                }
                // TODO
                break;
            case R.id.iv_delete: // EditText 删除
                et_username.setText("");
                break;
            case R.id.iv_eye:  // 显示密码
                int inputType = et_password.getInputType();
                if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_eye.setImageResource(R.mipmap.close_eye);
                }else{
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_eye.setImageResource(R.mipmap.open_eye);
                }
                break;
            case R.id.tv_verycode:
                getVerifyCode();
                break;
            default:
                break;
        }

    }

    /**
     * 注册
     * @param userName
     * @param verifyCode
     * @param passWord
     */
    private void register(String userName, String verifyCode, String passWord) {

//        swiperefreshlayout.setRefreshing(true);
        tv_register.setClickable(false);

        String url = BaseURLs.URL_REGISTER;
        Map<String, String> param = new HashMap<>();
        param.put("account" , userName);
        param.put("password" , passWord);
        param.put("accountType" , AccountType.TYPE_PHONE);
        param.put("smsCode" , verifyCode);

        VolleyContentFast.requestJsonByPost(url,param, new VolleyContentFast.ResponseSuccessListener<Register>() {
            @Override
            public void onResponse(Register register) {

//                swiperefreshlayout.setRefreshing(false);
                tv_register.setClickable(true);
                if (register != null&& register.getResult() == VolleyContentFast.ACCOUNT_SUCC){
                    CommonUtils.saveRegisterInfo(register);
                    L.d(TAG,"注册成功");
                    setResult(RESULT_OK);
                    finish();
                }else{
                    L.e(TAG,"成功请求，注册失败");
                    UiUtils.toast(RegisterActivity.this , R.string.register_fail);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
//                swiperefreshlayout.setRefreshing(false);
                tv_register.setClickable(true);
                L.e(TAG,"注册失败");
                UiUtils.toast(RegisterActivity.this , R.string.register_fail);
            }
        } , Register.class);
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        String phone = et_username.getText().toString();
        if (UiUtils.isMobileNO(this ,phone)){
            countDown.start();
            String url = BaseURLs.URL_SENDSMSCODE;
            Map<String, String> param = new HashMap<>();
            param.put("phone" , phone);
            param.put("operateType" , OperateType.TYPE_REGISTER);

            VolleyContentFast.requestJsonByPost(url,param, new VolleyContentFast.ResponseSuccessListener<SendSmsCode>() {
                @Override
                public void onResponse(SendSmsCode jsonObject) {
                    L.d(TAG,"发送验证码成功");
                }
            }, new VolleyContentFast.ResponseErrorListener() {
                @Override
                public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                    L.e(TAG,"发送验证码失败");
                }
            } , SendSmsCode.class);

        }
    }
}
