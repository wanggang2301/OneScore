package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private static final java.lang.String TAG = "AccountActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initView();
    }

    private void initView() {
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

        String url = BaseURLs.URL_LOGOUT;
        Map<String, String> param = new HashMap<>();
        param.put("token" , AppConstants.register.getData().getToken());
        VolleyContentFast.requestJsonByPost(url, param, new VolleyContentFast.ResponseSuccessListener<Register>() {
            @Override
            public void onResponse(Register register) {
                if (register.getResult() == VolleyContentFast.ACCOUNT_SUCC){
                    CommonUtils.saveRegisterInfo(register);
                    setResult(RESULT_OK);
                    finish();
                }else{
                    UiUtils.toast(AccountActivity.this ,register.getMsg());
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                L.e(TAG , " 注销失败");
                UiUtils.toast(AccountActivity.this , R.string.logout_fail);
            }
        } , Register.class);
    }
}
