package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.HashMap;
import java.util.Map;


/**
 *
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {


    private EditText et_username , et_password;
    private ImageView iv_eye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.public_txt_title)).setText(R.string.login);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        findViewById(R.id.iv_delete).setOnClickListener(this);

        iv_eye = (ImageView) findViewById(R.id.iv_eye);
        iv_eye.setOnClickListener(this);
        findViewById(R.id.tv_login).setOnClickListener(this);
        findViewById(R.id.public_img_back).setOnClickListener(this);


        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);


        View tv_register = findViewById(R.id.tv_right);
        tv_register.setVisibility(View.VISIBLE);
        tv_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_img_back: // 返回
                finish();
                break;
            case R.id.tv_right: // 注册
                startActivityForResult(new Intent(this , RegisterActivity.class) , HomePagerActivity.REQUESTCODE_LOGIN);
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
            case R.id.tv_login: // 登录
                login();
                break;
            default:
                break;
        }

    }

    private void login() {
        String userName = et_username.getText().toString();
        String passWord = et_password.getText().toString();

        if (UiUtils.isMobileNO(this ,userName)){
            if (UiUtils.checkPassword(this , passWord)){
                // 登录

                String url = BaseURLs.URL_LOGIN;
                Map<String, String> param = new HashMap<>();
                param.put("account" , userName);
                param.put("password" , passWord);
                param.put("deviceToken" , CommonUtils.getDeviceToken());

                VolleyContentFast.requestJsonByPost(url, param, new VolleyContentFast.ResponseSuccessListener<Register>() {
                    @Override
                    public void onResponse(Register register) {
                        if (register.getResult() == VolleyContentFast.ACCOUNT_SUCC){
                            CommonUtils.saveRegisterInfo(register);
                            setResult(RESULT_OK);
                            finish();
                        }else{
                            UiUtils.toast(LoginActivity.this ,register.getMsg());
                        }
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        L.e(TAG , " 登录失败");
                        UiUtils.toast(LoginActivity.this , R.string.login_fail);
                    }
                } , Register.class);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == HomePagerActivity.REQUESTCODE_LOGIN){
                // 注册成功返回
                setResult(RESULT_OK);
                finish();
            }
        }

    }
}

