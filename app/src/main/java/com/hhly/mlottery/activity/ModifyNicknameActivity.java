package com.hhly.mlottery.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.account.BaseBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.UiUtils;
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
        /**友盟页面统计*/
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("RegisterActivity");
        super.onResume();
        et_nickname.setFocusable(true);
        et_nickname.setFocusableInTouchMode(true);
        et_nickname.requestFocus();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() { //让软键盘延时弹出，以更好的加载Activity
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) et_nickname.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_nickname, 0);
            }

        }, 300);
    }

    private void initView() {

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage(getResources().getString(R.string.logining));

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
                 param.put("loginToken", AppConstants.register.getData().getLoginToken());
                 param.put("deviceToken", AppConstants.deviceToken);
                 param.put("nickname", nickName);

                 VolleyContentFast.requestJsonByPost(url, param, new VolleyContentFast.ResponseSuccessListener<BaseBean>() {
                     @Override
                     public void onResponse(BaseBean bean) {

                         progressBar.dismiss();

                         if (bean.getResult() == AccountResultCode.SUCC) {
                             UiUtils.toast(MyApp.getInstance(), R.string.modify_nickname_succ);
                             AppConstants.register.getData().getUser().setNickName(nickName);
                             CommonUtils.saveRegisterInfo(AppConstants.register);
                             finish();
                         } else if (bean.getResult() == AccountResultCode.USER_NOT_LOGIN) {
                             UiUtils.toast(getApplicationContext() ,R.string.name_invalid);
                             Intent intent = new Intent(ModifyNicknameActivity.this, LoginActivity.class);
                             startActivity(intent);
                         } else {
                             CommonUtils.handlerRequestResult(bean.getResult(), bean.getMsg());
                         }
                     }
                 }, new VolleyContentFast.ResponseErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyContentFast.VolleyException exception) {

                         progressBar.dismiss();

                         L.e(TAG, " 修改nickName失败");
                         UiUtils.toast(ModifyNicknameActivity.this, R.string.immediate_unconection);
                     }
                 }, BaseBean.class);
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
