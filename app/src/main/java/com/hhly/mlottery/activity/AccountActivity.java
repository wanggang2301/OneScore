package com.hhly.mlottery.activity;

import android.app.ProgressDialog;
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

import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends BaseActivity implements View.OnClickListener {

    private static final java.lang.String TAG = "AccountActivity";

    private ProgressDialog progressBar;

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
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        findViewById(R.id.public_img_back).setOnClickListener(this);
        findViewById(R.id.tv_logout).setOnClickListener(this);
        ((TextView)findViewById(R.id.tv_phone)).setText(AppConstants.register.getData().getUser().getNickName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_img_back: // 返回
                finish();
                break;
            case R.id.tv_logout: // 返回
                logout();
                break;
            default:
                break;
        }
    }

    private void logout() {

        progressBar.show();

        String url = BaseURLs.URL_LOGOUT;
        Map<String, String> param = new HashMap<>();
        param.put("loginToken" , AppConstants.register.getData().getLoginToken());
        param.put("deviceToken" , AppConstants.deviceToken);
        VolleyContentFast.requestJsonByPost(url, param, new VolleyContentFast.ResponseSuccessListener<Register>() {
            @Override
            public void onResponse(Register register) {

                progressBar.dismiss();
                if (register.getResult() == AccountResultCode.SUCC){
                    CommonUtils.saveRegisterInfo(null);
                    UiUtils.toast(MyApp.getInstance(), R.string.logout_succ);
                    setResult(RESULT_OK);
                    finish();
                }else{
                    CommonUtils.handlerRequestResult(register.getResult());
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                progressBar.dismiss();
                L.e(TAG , " 注销失败");
                UiUtils.toast(MyApp.getInstance() , R.string.logout_fail);
            }
        } , Register.class);
    }
}
