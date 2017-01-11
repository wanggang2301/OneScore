package com.hhly.mlottery.frame.numbersframe;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.PureViewPagerAdapter;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:香港开奖统计详情fragment
 * Created by 107_tangrr on 2017/1/11 0011.
 */

public class StartPagerFragment extends Fragment{

    private Context mContext;
    private View mView;
    String content;

    public static StartPagerFragment newInstance(String content) {
        StartPagerFragment fragment = new StartPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("content",content);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            content = getArguments().getString("content");
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.home_info_hk_start_pager_children, container, false);

        TextView tv = (TextView) mView.findViewById(R.id.tv_content);
        tv.setText(content);

        return mView;
    }
}
