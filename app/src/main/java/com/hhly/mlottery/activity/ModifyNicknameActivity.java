package com.hhly.mlottery.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
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
 * 修改昵称界面
 */
public class ModifyNicknameActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private EditText et_nickname;
    private Button public_btn_save;
    private ProgressDialog progressBar;
    private String mNickname;//传递过来的昵称
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.activity_modify_nickname);
        Intent i = getIntent();
        mNickname = i.getStringExtra("nickname");
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        et_nickname.setFocusable(true);
        et_nickname.setFocusableInTouchMode(true);
        et_nickname.requestFocus();

        new Timer().schedule(new TimerTask() { //让软键盘延时弹出，以更好的加载Activity
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) et_nickname.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_nickname, 0);
            }

        }, 300);
    }

    private void initView() {

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage(getResources().getString(R.string.modifying_nickname));

        ((TextView)findViewById(R.id.public_txt_title)).setText(R.string.modify_nickname);
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);

        public_btn_save = (Button) findViewById(R.id.public_btn_save);
        public_btn_save.setVisibility(View.VISIBLE);
        public_btn_save.setOnClickListener(this);
        public_btn_save.setTextColor(getResources().getColor(R.color.content_txt_light_grad));

        findViewById(R.id.public_img_back).setOnClickListener(this);

        et_nickname = (EditText) findViewById(R.id.et_nickname);
        et_nickname.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_img_back: // 返回
                MobclickAgent.onEvent(mContext, "ModifyNicknameActivity_Exit");
                finish();
                break;
            case R.id.public_btn_save: // 返回
                MobclickAgent.onEvent(mContext, "ModifyNicknameActivity_Save");
                modify();
                break;
            default:
                break;
        }
    }

    private void modify() {

        final String nickName = et_nickname.getText().toString();
        if (UiUtils.checkNickname(this , nickName)) {

             if(!mNickname.equals(nickName)) {
                 progressBar.show();
                 String url = BaseURLs.URL_EDITNICKNAME;
                 Map<String, String> param = new HashMap<>();
                 param.put("userId", AppConstants.register.getUser().getUserId());
                 param.put("nickname", nickName);
                 param.put("loginToken",AppConstants.register.getToken());
                 if (MyApp.isLanguage.equals("rCN")) {
                     // 如果是中文简体的语言环境
                     language = "langzh";
                 } else if (MyApp.isLanguage.equals("rTW")) {
                     // 如果是中文繁体的语言环境
                     language="langzh-TW";
                 }

                 String sign=DeviceInfo.getSign("/user/updatenickname"+language+"loginToken"+AppConstants.register.getToken()+"nickname"+nickName+"timeZone8"+"userId"+AppConstants.register.getUser().getUserId());
                 param.put("sign",sign);

                 VolleyContentFast.requestJsonByPost(url, param, new VolleyContentFast.ResponseSuccessListener<Register>() {
                     @Override
                     public void onResponse(Register bean) {

                         progressBar.dismiss();

                         if (Integer.parseInt(bean.getCode()) == AccountResultCode.SUCC) {
                             UiUtils.toast(MyApp.getInstance(), R.string.modify_nickname_succ);
                             AppConstants.register.getUser().setNickName(nickName);
                            // CommonUtils.saveRegisterInfo(AppConstants.register);
                            PreferenceUtil.commitString(AppConstants.SPKEY_NICKNAME, nickName);
                             finish();
                         } else if (Integer.parseInt(bean.getCode())  == AccountResultCode.TOKEN_INVALID) {
                             UiUtils.toast(getApplicationContext() ,R.string.name_invalid);
                             AppConstants.register.setToken(null);
                             Intent intent = new Intent(ModifyNicknameActivity.this, LoginActivity.class);
                             startActivity(intent);
                         } else {
                             DeviceInfo.handlerRequestResult(Integer.parseInt(bean.getCode()) , "未知错误");
                         }
                     }
                 }, new VolleyContentFast.ResponseErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyContentFast.VolleyException exception) {

                         progressBar.dismiss();

                         L.e(TAG, " 修改nickName失败");
                         UiUtils.toast(ModifyNicknameActivity.this, R.string.immediate_unconection);
                     }
                 }, Register.class);
             }else{
                 UiUtils.toast(getApplicationContext() ,R.string.pw_same_same);
             }
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            public_btn_save.setEnabled(true);
            public_btn_save.setTextColor(getResources().getColor(R.color.white));
        } else {
            public_btn_save.setEnabled(false);
            public_btn_save.setTextColor(getResources().getColor(R.color.content_txt_light_grad));
        }
    }
}
