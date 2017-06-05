package com.hhly.mlottery.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.InvitedBean;
import com.hhly.mlottery.bean.ShareBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.ShareFragment;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import static com.hhly.mlottery.R.id.public_btn_set;

/**
 * Created by yuely198 on 2017/1/13.
 * 获取邀请码页面
 */

public class InvitedActivity extends BaseActivity implements View.OnClickListener {

    private TextView iv_invited_number;
    private TextView iv_number_copy;
    private TextView public_txt_title;
    private Button public_btn_save;
    private String copy_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }


    //是否获取邀请码成功
    private boolean isInvitedSucess = false;

    private void initData() {
        Map<String, String> param = new HashMap<>();

        param.put("userId", AppConstants.register.getUser().getUserId());
        // param.put("userId", "hhly91757");
        L.i("yly123", "用户userId===" + AppConstants.register.getUser().getUserId());
        //String url="http://m.1332255.com:81/mlottery/core/androidUserCenter.getInviteCode.do";

        VolleyContentFast.requestJsonByGet(BaseURLs.INVITED_RUL, param, new VolleyContentFast.ResponseSuccessListener<InvitedBean>() {
            @Override
            public void onResponse(InvitedBean bean) {

                if (bean.getResult() == 0) {

                    if (!TextUtils.isEmpty(bean.getData().getInviteCode())) {
                        copy_text = bean.getData().getInviteCode();

                        L.i("yly123", "邀请码===" + bean.getData().getInviteCode());

                        iv_invited_number.setText(bean.getData().getInviteCode());
                        iv_number_copy.setText(getApplicationContext().getResources().getString(R.string.copy_tv));
                        isInvitedSucess = true;
                    } else {
                        iv_invited_number.setText(R.string.invitation_code_acquisition_failed);
                        iv_number_copy.setText(getApplicationContext().getResources().getString(R.string.exp_refresh_txt));
                        isInvitedSucess = false;
                    }


                } else {
                    iv_invited_number.setText(R.string.invitation_code_acquisition_failed);
                    iv_number_copy.setText(getApplicationContext().getResources().getString(R.string.exp_refresh_txt));
                    isInvitedSucess = false;
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                // UiUtils.toast(InvitedActivity.this, R.string.picture_put_failed);
                iv_invited_number.setText(R.string.invitation_code_acquisition_failed);
                iv_number_copy.setText(getApplicationContext().getResources().getString(R.string.exp_refresh_txt));
                isInvitedSucess = false;
            }
        }, InvitedBean.class);

        //
    }

    private void initView() {

        setContentView(R.layout.activity_invited);
        public_txt_title = (TextView) findViewById(R.id.public_txt_title);
        public_txt_title.setText(R.string.invite_friends);
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(public_btn_set).setVisibility(View.GONE);
        public_btn_save = (Button) findViewById(R.id.public_btn_save);

        if (DeviceInfo.isZH()) {
            public_btn_save.setVisibility(View.VISIBLE);
        } else {
            public_btn_save.setVisibility(View.GONE);
        }

        public_btn_save.setOnClickListener(this);
        public_btn_save.setText(R.string.foot_details_share);
        public_btn_save.setTextColor(getResources().getColor(R.color.white));

        findViewById(R.id.public_img_back).setOnClickListener(this);

        iv_invited_number = (TextView) findViewById(R.id.iv_invited_number);

        iv_number_copy = (TextView) findViewById(R.id.iv_number_copy);
        iv_number_copy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back:
                MobclickAgent.onEvent(this, "InvitedActivity_Exit");
                finish();
                break;
            case R.id.public_btn_save:  //分享
                MobclickAgent.onEvent(this, "InvitedActivity_Save");
                ShareBean shareBean = new ShareBean();
                shareBean.setTitle(getApplicationContext().getResources().getString(R.string.please_invited));
                shareBean.setSummary("【" + AppConstants.register.getUser().getUserId() + "】" + getResources().getString(R.string.invited_summary));
                shareBean.setTarget_url(BaseURLs.INVITED_ACTIVITY_URL + "?userId=" + AppConstants.register.getUser().getUserId());
                shareBean.setCopy(shareBean.getTarget_url());

                ShareFragment shareFragment = ShareFragment.newInstance(shareBean);
                shareFragment.show(getSupportFragmentManager(), "InvitedActivity");
                break;
            case R.id.iv_number_copy:

                if (isInvitedSucess) {
                    ClipboardManager cmb = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(copy_text + "");
                    UiUtils.toast(getApplicationContext(), R.string.copy_text);
                } else {
                    initData();
                }
                break;
            default:
                break;


        }
    }
}
