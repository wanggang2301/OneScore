package com.hhly.mlottery.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.InvitedBean;
import com.hhly.mlottery.bean.ShareBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.ShareFragment;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by yuely198 on 2017/1/13.
 * 获取邀请码页面
 */

public class InvitedActivity extends BaseActivity implements View.OnClickListener{

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

    private void initData() {
        Map<String, String> param = new HashMap<>();

        param.put("userId", AppConstants.register.getData().getUser().getUserId());
        Log.i("sada","ada"+ AppConstants.register.getData().getUser().getUserId());
        //String url="http://m.1332255.com:81/mlottery/core/androidUserCenter.getInviteCode.do";

        VolleyContentFast.requestJsonByGet(BaseURLs.INVITED_RUL, param, new VolleyContentFast.ResponseSuccessListener<InvitedBean>() {
            @Override
            public void onResponse(InvitedBean bean) {

                if (bean.getResult() == 0) {
                    copy_text = bean.getData().getInviteCode();
                    iv_invited_number.setText(bean.getData().getInviteCode());

                }else{
                    iv_invited_number.setText(R.string.invitation_code_acquisition_failed);
                }


            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
               // UiUtils.toast(InvitedActivity.this, R.string.picture_put_failed);
                iv_invited_number.setText(R.string.invitation_code_acquisition_failed);

            }
        }, InvitedBean.class);

    }

    private void initView() {

        setContentView(R.layout.activity_invited);
        public_txt_title = (TextView) findViewById(R.id.public_txt_title);
        public_txt_title.setText(R.string.invite_friends);
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        public_btn_save = (Button) findViewById(R.id.public_btn_save);
        public_btn_save.setVisibility(View.VISIBLE);
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
                shareBean.setSummary("【" + AppConstants.register.getData().getUser().getUserId() + "】" + "请您一起看比赛，输入邀请码，优惠多多哦。");
                shareBean.setTarget_url(BaseURLs.INVITED_ACTIVITY_URL + "?userId=" + AppConstants.register.getData().getUser().getUserId());
                shareBean.setCopy(shareBean.getTarget_url());

                ShareFragment shareFragment = ShareFragment.newInstance(shareBean);
                shareFragment.show(getSupportFragmentManager(), "InvitedActivity");
                break;
            case R.id.iv_number_copy:

                    ClipboardManager cmb = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(copy_text+"");
                    UiUtils.toast(getApplicationContext(),R.string.copy_text);
                break;
            default:
                break;




        }
    }
}
