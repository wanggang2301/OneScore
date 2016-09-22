package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.foreigninfomation.OverseasInformationListBean;
import com.hhly.mlottery.frame.ForeignChatFragment;
import com.hhly.mlottery.util.CyUtils;
import com.umeng.analytics.MobclickAgent;


/**
 * 描述:  境外资讯详情Activity
 * 作者:  wangg@13322.com
 * 时间:  2016/9/12 12:11
 */

public class ForeignInfomationDetailsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mPublic_img_back;// 返回
    private TextView mPublic_txt_title;// 标题
    private String url;
    private String title;

    private OverseasInformationListBean oilbean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_infomation_details);
        if (getIntent().getExtras() != null) {
            oilbean = getIntent().getExtras().getParcelable("detailsData");
        }

        mPublic_img_back = (ImageView) findViewById(R.id.public_img_back);
        mPublic_txt_title = (TextView) findViewById(R.id.public_txt_title);
        mPublic_txt_title.setText("国外资讯");
        mPublic_img_back.setOnClickListener(this);

        initData();

    }

    //hhly2016999
    private void initData() {
        ForeignChatFragment foreignChatFragment = ForeignChatFragment.newInstance();
        foreignChatFragment.setOilBean(oilbean);

        CyUtils.addComment(foreignChatFragment, oilbean.getId() + "", oilbean.getContent(), true, true, getSupportFragmentManager(), R.id.scrollview);//添加评论碎片
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back://返回
                CyUtils.hideKeyBoard(this);
                // setResult(2, new Intent().putExtra("cmt_sum", ChatFragment.cmt_sum));
                MobclickAgent.onEvent(mContext, "Football_CounselCommentActivity_Exit");
                finish();
                break;
        }

    }
}
