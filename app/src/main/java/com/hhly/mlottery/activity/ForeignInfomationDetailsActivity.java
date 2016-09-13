package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.ForeignInfomationDetailsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private ForeignInfomationDetailsAdapter foreignInfomationDetailsAdapter;

    private List<Integer> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_infomation_details);
        ButterKnife.bind(this);
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
    }

    @OnClick({R.id.public_img_back, R.id.tv_comment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.public_img_back:
                this.finish();
                break;
            case R.id.tv_comment:
                break;
        }
    }
}
