package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.multiplebean.MultipleByValueBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/***
 * 多屏观看介绍页面
 */

public class MultiScreenIntroduceActivity extends BaseActivity {


    @BindView(R.id.btn_enter)
    TextView btnEnter;

    private MultipleByValueBean multipleByValueBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
            multipleByValueBean = (MultipleByValueBean) getIntent().getExtras().get("thirdId");
        }

        setContentView(R.layout.activity_multi_screen_introduce);
        ButterKnife.bind(this);

       // PreferenceUtil.commitBoolean(AppConstants.ANIMATION_RED_KEY, true);// 记录红点功能
    }

    @OnClick(R.id.btn_enter)
    public void onClick() {
        Intent intent = new Intent(MultiScreenIntroduceActivity.this, MultiScreenViewingListActivity.class);
        if (multipleByValueBean != null) {
            intent.putExtra("thirdId", multipleByValueBean);
        }
        startActivity(intent);
        finish();
    }
}
