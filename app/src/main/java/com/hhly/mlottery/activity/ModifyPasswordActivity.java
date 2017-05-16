package com.hhly.mlottery.activity;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.account.BaseBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.cipher.MD5Util;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.net.account.AccountResultCode;
import com.hhly.mlottery.util.net.account.AccountType;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改密码
 */
public class ModifyPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_password_old, et_password_new, et_password_confirm;
    private ProgressDialog progressBar;
    private ImageView mIv_eye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        et_password_old.setFocusable(true);
        et_password_old.setFocusableInTouchMode(true);
        et_password_old.requestFocus();
    }

    private void initView() {

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage(getResources().getString(R.string.reseting_password));

        // ((CheckBox)findViewById(R.id.checkbox)).setOnCheckedChangeListener(this);
        ((TextView) findViewById(R.id.public_txt_title)).setText(R.string.modify_password);
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        findViewById(R.id.public_img_back).setOnClickListener(this);
        findViewById(R.id.tv_comfirm).setOnClickListener(this);


        mIv_eye = (ImageView) findViewById(R.id.iv_eye);
        mIv_eye.setOnClickListener(this);
        et_password_old = (EditText) findViewById(R.id.et_password_old);
        et_password_new = (EditText) findViewById(R.id.et_password_new);
        et_password_confirm = (EditText) findViewById(R.id.et_password_confirm);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back: // 返回
                MobclickAgent.onEvent(mContext, "ModifyPasswordActivity_Exit");
                finish();
                break;
            case R.id.tv_comfirm: // 返回
                MobclickAgent.onEvent(mContext, "ModifyPasswordActivity_confirm");
                modifyPassword();
                break;
            case R.id.iv_eye: // 返回
                MobclickAgent.onEvent(mContext, "ModifyPasswordActivity_eye");
                int old_inputType = et_password_old.getInputType();
                int new_inputType = et_password_new.getInputType();
                int confirm_inputType = et_password_confirm.getInputType();
                if (old_inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD && new_inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD && confirm_inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    et_password_old.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_password_new.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_password_confirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mIv_eye.setImageResource(R.mipmap.close_eye);
                } else {
                    et_password_old.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    et_password_new.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    et_password_confirm.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mIv_eye.setImageResource(R.mipmap.open_eye);
                }  // 光标移动到结尾
                DeviceInfo.selectionLast(et_password_old, et_password_new, et_password_confirm);

                break;
            default:
                break;
        }
    }

    private void modifyPassword() {
        String pwOld = et_password_old.getText().toString();
        String pwNew = et_password_new.getText().toString();
        String pwConfirm = et_password_confirm.getText().toString();

        Resources res = getResources();

        // 检查旧密码格式
        if (UiUtils.checkPassword_JustLength(ModifyPasswordActivity.this, pwOld,
                res.getString(R.string.old_pw) + res.getString(R.string.pwd_format_just_length))) {

            // 检查新密码格式
            if (UiUtils.checkPassword(ModifyPasswordActivity.this, pwNew, false,
                    res.getString(R.string.input_password_new),
                    res.getString(R.string.new_pw) + res.getString(R.string.pwd_format))) {

                // 检查新确认格式
                if (UiUtils.checkPassword(ModifyPasswordActivity.this, pwConfirm, false,
                        res.getString(R.string.input_password_confirm),
                        res.getString(R.string.comfirm_pw) + res.getString(R.string.pwd_format))) {

                    if (pwNew.equals(pwConfirm)) {

                        String url = BaseURLs.URL_CHANGEPASSWORD;
                        Map<String, String> param = new HashMap<>();
                        param.put("account", PreferenceUtil.getString(AppConstants.SPKEY_LOGINACCOUNT,""));
                        param.put("accountType", AccountType.TYPE_PHONE);
                        param.put("oldPassword", MD5Util.getMD5(pwOld));
                        param.put("newPassword", MD5Util.getMD5(pwNew));

                        VolleyContentFast.requestJsonByPost(url, param, new VolleyContentFast.ResponseSuccessListener<BaseBean>() {
                            @Override
                            public void onResponse(BaseBean reset) {
                                progressBar.dismiss();

                                if (reset != null && reset.getResult() == AccountResultCode.SUCC) {
                                    UiUtils.toast(MyApp.getInstance(), R.string.modify_password_succ);
                                    L.d(TAG, "修改密码成功");
                                    setResult(RESULT_OK);
                                    finish();
                                } else if (reset.getResult() == AccountResultCode.USERNAME_PASS_ERROR) {
                                    L.e(TAG, "成功请求，修改密码失败");
                                    UiUtils.toast(MyApp.getInstance(), R.string.username_original_pass_error);
                                    // CommonUtils.handlerRequestResult(reset.getResult() , reset.getMsg());
                                } else {
                                    DeviceInfo.handlerRequestResult(reset.getResult(), reset.getMsg());
                                }
                            }
                        }, new VolleyContentFast.ResponseErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                                progressBar.dismiss();
                                L.e(TAG, "修改密码失败");
                                UiUtils.toast(ModifyPasswordActivity.this, R.string.immediate_unconection);
                            }
                        }, BaseBean.class);

                    } else {
                        UiUtils.toast(getApplicationContext(), R.string.pw_not_same);
                    }

                }

            }

        }
    }

  /*  @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        L.d(TAG , " onCheckedChanged  , isCheck =  "+ isChecked);
        if (!isChecked){
            et_password_old.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            et_password_new.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            et_password_confirm.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }else{
            et_password_old.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            et_password_new.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            et_password_confirm.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }

        CommonUtils.selectionLast(et_password_old ,et_password_new,et_password_confirm );
    }*/
}
