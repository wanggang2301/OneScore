package com.hhly.mlottery.frame;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

/**
 * @author wang gang
 * @date 2016/9/20 10:00
 * @des 资讯—境外新闻
 */
public class ForeignInfomationFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private View mView;
    private View moreView;  //加载更多
    private ForeignInfomationAdapter foreignInfomationAdapter;
    private List<Integer> list;
    private Context mContext;
    private boolean isCreated = false;//当前碎片是否createview
    private int pageSize = 0;

    public static ForeignInfomationFragment newInstance() {
        ForeignInfomationFragment fragment = new ForeignInfomationFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_foreign_infomation, container, false);
        moreView = inflater.inflate(R.layout.view_load_more, container, false);
        ButterKnife.bind(this, mView);
        mContext = getActivity();
        initView();
        if (getUserVisibleHint()) {
            onVisible();
        }
        return mView;
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        isCreated = true;
    }

    private void loadData() {
        setList();
        foreignInfomationAdapter = new ForeignInfomationAdapter(mContext, list);
        recyclerView.setAdapter(foreignInfomationAdapter);
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


    //fg切换时回调该方法
    //用来取消viewpager的预加载机制  减少初始化开销
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isCreated && getUserVisibleHint()) {
            onVisible();
        }
        if (!isCreated) {
            return;
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {
        loadData();
    }
}
