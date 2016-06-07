package com.hhly.mlottery.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.account.Register;
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

/**
 * 我的账户界面
 */
public class AccountActivity extends BaseActivity implements View.OnClickListener {

    private static final java.lang.String TAG = "AccountActivity";

    private ProgressDialog progressBar;
    private TextView tv_nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initView();
    }

    private void initView() {

        progressBar = new ProgressDialog(this);
        progressBar.setMessage(getResources().getString(R.string.logouting));

        ((TextView)findViewById(R.id.public_txt_title)).setText(R.string.my);
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setOnClickListener(this);
        findViewById(R.id.public_img_back).setOnClickListener(this);
        findViewById(R.id.tv_logout).setOnClickListener(this);
        tv_nickname = ((TextView)findViewById(R.id.tv_nickname));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_img_back: // 返回
                MobclickAgent.onEvent(mContext, "AccountActivity_Exit");
                finish();
                break;
            case R.id.tv_logout: // 返回
                MobclickAgent.onEvent(mContext, "AccountActivity_ExitLogin");
                showDialog();
                break;
            case R.id.public_btn_set: // 昵称头像栏
                startActivity(new Intent(this , ProfileActivity.class));
                break;
            default:
                break;
        }
    }

    private void showDialog(){
        AlertDialog dialog = new AlertDialog.Builder(AccountActivity.this)
                .setMessage(getResources().getString(R.string.logout_check))
                .setPositiveButton(R.string.about_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton(R.string.about_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();
        dialog.show();
    }

    /**
     * 注销
     */
    private void logout() {

        progressBar.show();

        String url = BaseURLs.URL_LOGOUT;
        Map<String, String> param = new HashMap<>();
        param.put("loginToken" , AppConstants.register.getData().getLoginToken());
        param.put("deviceToken" , AppConstants.deviceToken);
        param.put("userId" , AppConstants.register.getData().getUser().getUserId());
        VolleyContentFast.requestJsonByPost(url, param, new VolleyContentFast.ResponseSuccessListener<Register>() {
            @Override
            public void onResponse(Register register) {

                progressBar.dismiss();
                if (register.getResult() == AccountResultCode.SUCC || register.getResult() == AccountResultCode.USER_NOT_LOGIN){
                    CommonUtils.saveRegisterInfo(null);
                    UiUtils.toast(MyApp.getInstance(), R.string.logout_succ);
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
                L.e(TAG , " 注销失败");
                UiUtils.toast(MyApp.getInstance() , R.string.immediate_unconection);
            }
        } , Register.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**友盟页面统计*/
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("AccountActivity");
        tv_nickname.setText(AppConstants.register.getData().getUser().getNickName());
    }

    @Override
    public void onPause() {
        super.onPause();
        /**友盟页面统计*/
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("AccountActivity");
    }
}
