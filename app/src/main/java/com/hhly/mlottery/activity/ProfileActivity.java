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
        findViewById(R.id.rl_modifypass).setOnClickListener(this);
        tv_nickname = ((TextView)findViewById(R.id.tv_nickname));
        ((TextView)findViewById(R.id.tv_account_real)).setText(AppConstants.register.getData().getUser().getLoginAccount());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_img_back: // 返回
                MobclickAgent.onEvent(mContext, "ProfileActivity_Exit");
                finish();
                break;
            case R.id.rl_nickname: // 昵称栏
               // startActivity(new Intent(this, ModifyNicknameActivity.class));
                Intent intent = new Intent(ProfileActivity.this, ModifyNicknameActivity.class);
                intent.putExtra("nickname", tv_nickname.getText().toString());//传递联赛ID
                startActivity(intent);
                break;
            case R.id.rl_modifypass: // 修改密码
                startActivity(new Intent(this , ModifyPasswordActivity.class));
                break;
            default:
                break;
        }
    }

}
