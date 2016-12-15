package com.hhly.mlottery.frame.chartBallFragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.chartBallAdapter.LocalGridAdapter;
import com.hhly.mlottery.bean.BarrageBean;
import com.hhly.mlottery.bean.chart.ChartReceive;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.widget.MyGridView;

import java.util.ArrayList;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * desc:本地表情fragment
 * Created by 107_tangrr on 2016/12/9 0009.
 */

public class LocalFragment extends Fragment {
    private static final String DATA_KEY = "mData";
    private ArrayList<Integer> mData;
    private View mView;
    private Context mContext;
    private MyGridView gridView;

    public static LocalFragment newInstance(ArrayList<Integer> iconlist) {
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList(DATA_KEY, iconlist);
        LocalFragment fragment = new LocalFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mData = args.getIntegerArrayList(DATA_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mView = View.inflate(mContext, R.layout.chart_ball_local_pager, null);
        initView();
        initEvent();
        return mView;
    }

    private void initEvent() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                for (Map.Entry<String, Integer> entry : AppConstants.localMap.entrySet()) {
                    if (entry.getValue().equals(mData.get(i))) {
                        // TODO 此处点击之后直接发送消息
                        // 向会话列表发送输入内容
                        EventBus.getDefault().post(new ChartReceive.DataBean.ChatHistoryBean(entry.getKey(),new ChartReceive.DataBean.ChatHistoryBean.FromUserBean(AppConstants.register.getData().getUser().getUserId()
                                ,AppConstants.register.getData().getUser().getHeadIcon(),AppConstants.register.getData().getUser().getNickName())));

                        // 用户自己的头像URL发向弹幕
                        EventBus.getDefault().post(new BarrageBean(AppConstants.register.getData().getUser().getHeadIcon(),entry.getKey()));

                        return;
                    }
                }
            }
        });
    }

    private void initView() {
        gridView = (MyGridView) mView.findViewById(R.id.local_page_item_gridView);
        gridView.setAdapter(new LocalGridAdapter(mContext, mData));
    }
}
