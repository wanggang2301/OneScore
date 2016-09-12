package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.ForeignInfomationAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForeignInfomationActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ForeignInfomationAdapter foreignInfomationAdapter;

    private List<Integer> list;
    private View moreView;  //加载更多


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_foreign_infomation);
        ButterKnife.bind(this);


        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        setList();

        foreignInfomationAdapter = new ForeignInfomationAdapter(getApplicationContext(), list);

        recyclerView.setAdapter(foreignInfomationAdapter);

        moreView = getLayoutInflater().inflate(R.layout.view_load_more, (ViewGroup) recyclerView.getParent(), false);


        foreignInfomationAdapter.openLoadMore(0, true);

        foreignInfomationAdapter.setLoadingView(moreView);


        foreignInfomationAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {

                        for (int i = 0; i < 10; i++) {
                            list.add(new Random().nextInt(50));
                        }
                        foreignInfomationAdapter.notifyDataChangedAfterLoadMore(true);
                    }
                });
            }
        });
    }

    private void setList() {

        list = new ArrayList<>();
        for (int i = 1; i < 20; i++) {
            list.add(i);
        }
    }
}
