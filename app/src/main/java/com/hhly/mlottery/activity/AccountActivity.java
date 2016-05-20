package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.net.account.AccountResultCode;

import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private static final java.lang.String TAG = "AccountActivity";

    private SwipeRefreshLayout swiperefreshlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        swiperefreshlayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swiperefreshlayout.setColorSchemeResources(R.color.bg_header);
        swiperefreshlayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(AccountActivity.this, StaticValues.REFRASH_OFFSET_END));
        swiperefreshlayout.setEnabled(false);

        initView();
    }

    private void initView() {
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

        swiperefreshlayout.setRefreshing(true);

        String url = BaseURLs.URL_LOGOUT;
        Map<String, String> param = new HashMap<>();
        param.put("loginToken" , AppConstants.register.getData().getLoginToken());
        param.put("deviceToken" , AppConstants.deviceToken);
        VolleyContentFast.requestJsonByPost(url, param, new VolleyContentFast.ResponseSuccessListener<Register>() {
            @Override
            public void onResponse(Register register) {

                swiperefreshlayout.setRefreshing(false);

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
                swiperefreshlayout.setRefreshing(false);
                L.e(TAG , " 注销失败");
                UiUtils.toast(AccountActivity.this , R.string.logout_fail);
            }
        } , Register.class);
    }
}
