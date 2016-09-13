package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.ForeignInfomationDetailsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForeignInfomationDetailsActivity extends BaseActivity {

    @BindView(R.id.recycler_view_details)
    RecyclerView recyclerViewDetails;
    private ForeignInfomationDetailsAdapter foreignInfomationDetailsAdapter;

    private List<Integer> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_infomation_details);
        ButterKnife.bind(this);

        recyclerViewDetails.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        list=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        foreignInfomationDetailsAdapter = new ForeignInfomationDetailsAdapter(getApplicationContext(), list);

        recyclerViewDetails.setAdapter(foreignInfomationDetailsAdapter);
    }
}
