package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 首页用户设置选项
 * Created by hhly107 on 2016/4/6.
 */
public class HomeUserOptionsActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_language_frame;// 语言切换
    private RelativeLayout rl_about_frame;// 关于我们
    private RelativeLayout rl_user_feedback;// 反馈

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        setContentView(R.layout.home_user_options_mian);

        ImageView public_img_back = (ImageView) findViewById(R.id.public_img_back);// 返回图标
        public_img_back.setImageDrawable(getResources().getDrawable(R.mipmap.back));
        public_img_back.setOnClickListener(this);

        TextView public_txt_title = (TextView) findViewById(R.id.public_txt_title);
        public_txt_title.setText(getResources().getString(R.string.foot_setting_txt));

        ImageView public_btn_filter = (ImageView) findViewById(R.id.public_btn_filter);
        public_btn_filter.setVisibility(View.GONE);
        ImageView public_btn_set = (ImageView) findViewById(R.id.public_btn_set);
        public_btn_set.setVisibility(View.GONE);

        rl_language_frame = (RelativeLayout) findViewById(R.id.rl_language_frame);
        rl_language_frame.setOnClickListener(this);
        rl_about_frame = (RelativeLayout) findViewById(R.id.rl_about_frame);
        rl_about_frame.setOnClickListener(this);
        rl_user_feedback = (RelativeLayout) findViewById(R.id.rl_user_feedback);
        rl_user_feedback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_language_frame:// 语言切换
                Intent intent = new Intent(HomeUserOptionsActivity.this,HomeLanguageActivity.class);
                startActivity(intent);
                MobclickAgent.onEvent(mContext,"LanguageChanger");
                break;
            case R.id.rl_about_frame:// 关于我们
                Intent intent2 = new Intent(HomeUserOptionsActivity.this,HomeAboutActivity.class);
                startActivity(intent2);
                MobclickAgent.onEvent(mContext,"AboutWe");
                break;
            case R.id.rl_user_feedback:// 反馈
                startActivity(new Intent(HomeUserOptionsActivity.this,FeedbackActivity.class));
                MobclickAgent.onEvent(mContext,"UserFeedback");
                break;
            case R.id.public_img_back:// 返回
                finish();
                MobclickAgent.onEvent(mContext,"HomePagerUserSetting_Exit");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("HomeUserOptionsActivity");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("HomeUserOptionsActivity");
    }
}
