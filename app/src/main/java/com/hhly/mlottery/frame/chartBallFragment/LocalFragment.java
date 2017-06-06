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
import com.hhly.mlottery.activity.ChartballActivity;
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
    private static final String DATA_NAME = "mDataName";
    private ArrayList<Integer> mData;
    private ArrayList<String> listName;
    private View mView;
    private Context mContext;
    private MyGridView gridView;

    public static LocalFragment newInstance(ArrayList<Integer> iconlist , ArrayList<String> iconlistname) {
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList(DATA_KEY, iconlist);
        bundle.putStringArrayList(DATA_NAME, iconlistname);
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
            listName = args.getStringArrayList(DATA_NAME);
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
                        // 向会话列表发送输入内容
                        EventBus.getDefault().post(new ChartReceive.DataBean.ChatHistoryBean(null, 1, entry.getKey(), new ChartReceive.DataBean.ChatHistoryBean.FromUserBean(AppConstants.register.getUser().getUserId()
                                , AppConstants.register.getUser().getImageSrc(), AppConstants.register.getUser().getNickName()), new ChartReceive.DataBean.ChatHistoryBean.ToUser(), null));

                        ((ChartballActivity) mContext).hideKeyOrGallery();
                        return;
                    }
                }
            }
        });
    }

    private void initView() {
        gridView = (MyGridView) mView.findViewById(R.id.local_page_item_gridView);
        gridView.setAdapter(new LocalGridAdapter(mContext, mData, listName));
    }
}
