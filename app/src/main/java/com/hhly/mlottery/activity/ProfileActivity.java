package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.AppConstants;
import com.umeng.analytics.MobclickAgent;

/**
 * 个人
 */
public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    private static final int MODIFY_NICKNAME = 400;
    private TextView tv_nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_nickname.setText(AppConstants.register.getData().getUser().getNickName());
    }

    private void initView() {

        ((TextView)findViewById(R.id.public_txt_title)).setText(R.string.profile);
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        findViewById(R.id.public_img_back).setOnClickListener(this);
        findViewById(R.id.rl_nickname).setOnClickListener(this);
        tv_nickname = ((TextView)findViewById(R.id.tv_nickname));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_img_back: // 返回
                MobclickAgent.onEvent(mContext, "ProfileActivity_Exit");
                finish();
                break;
            case R.id.rl_nickname: // 昵称栏
                startActivityForResult(new Intent(this , ModifyNicknameActivity.class) , MODIFY_NICKNAME);
                break;
            default:
                break;
        }
    }

}
