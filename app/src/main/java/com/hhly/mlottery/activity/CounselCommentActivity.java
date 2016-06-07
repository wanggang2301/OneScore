package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.ChatFragment;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.L;
import com.umeng.analytics.MobclickAgent;

/**
 * lzf
 * 全部评论展示页面
 */
public class CounselCommentActivity extends BaseActivity implements OnClickListener {


    private ImageView mPublic_img_back;// 返回
    private TextView mPublic_txt_title;// 标题
    private String url;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselcomment);
        Intent intent = getIntent();
        url = intent.getStringExtra(CyUtils.INTENT_PARAMS_SID);
        L.i("lzfsouceidurl" + url);
        title = intent.getStringExtra(CyUtils.INTENT_PARAMS_TITLE);
        initView();

        CyUtils.addComment(new ChatFragment(),url, title, true, true, getSupportFragmentManager(), R.id.scrollview);
    }

    private void initView() {
        mPublic_img_back = (ImageView) findViewById(R.id.public_img_back);
        mPublic_txt_title = (TextView) findViewById(R.id.public_txt_title);
        mPublic_txt_title.setText(R.string.comment_title);
        mPublic_img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back://返回
                CyUtils.hideKeyBoard(this);
                setResult(2, new Intent().putExtra("cmt_sum", ChatFragment.cmt_sum));
                MobclickAgent.onEvent(mContext, "Football_CounselCommentActivity_Exit");
                finish();
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("Football_CounselCommentActivity");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("Football_CounselCommentActivity");
    }
}