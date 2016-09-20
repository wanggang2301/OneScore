package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.ForeignInfomationDetailsAdapter;
import com.hhly.mlottery.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 描述:  境外资讯详情Activity
 * 作者:  wangg@13322.com
 * 时间:  2016/9/12 12:11
 */

public class ForeignInfomationDetailsActivity extends BaseActivity {
    @BindView(R.id.recycler_view_details)
    RecyclerView recyclerViewDetails;
    @BindView(R.id.public_img_back)
    ImageView publicImgBack;
    @BindView(R.id.public_btn_set)
    ImageView publicBtnSet;
    @BindView(R.id.iv_tight)
    ImageView ivTight;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.public_txt_title)
    TextView publicTxtTitle;
    @BindView(R.id.header_layout)
    LinearLayout headerLayout;
    @BindView(R.id.civ_logo)
    CircleImageView civLogo;
    @BindView(R.id.tv_name_en)
    TextView tvNameEn;
    @BindView(R.id.tv_name_ch)
    TextView tvNameCh;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_tight)
    TextView tvTight;
    @BindView(R.id.iv_comment)
    ImageView ivComment;
    private ForeignInfomationDetailsAdapter foreignInfomationDetailsAdapter;
    private List<Integer> list;
    private boolean focusable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_infomation_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        focusable = intent.getBooleanExtra("focusable", false);
        initView();


        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        foreignInfomationDetailsAdapter = new ForeignInfomationDetailsAdapter(getApplicationContext(), list);
        recyclerViewDetails.setAdapter(foreignInfomationDetailsAdapter);
    }

    private void initView() {
        publicBtnSet.setVisibility(View.GONE);
        recyclerViewDetails.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        if (focusable) {
            recyclerViewDetails.setFocusable(true);
        } else {
            recyclerViewDetails.setFocusable(false);
        }
    }

    @OnClick({R.id.public_img_back, R.id.tv_comment, R.id.iv_photo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.public_img_back:
                this.finish();
                break;
            case R.id.tv_comment:
                break;

            case R.id.iv_photo:
                String url = "http://pic2.qinzhou360.com/qinzhoubbs/forum/201506/05/194201nvwbd3d1s7b1d38u.png";
                Intent intent = new Intent(ForeignInfomationDetailsActivity.this, PicturePreviewActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                break;
        }
    }
}
