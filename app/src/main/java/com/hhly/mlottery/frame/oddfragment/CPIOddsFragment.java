package com.hhly.mlottery.frame.oddfragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.cpiadapter.CPIRecyclerViewAdapter;
import com.hhly.mlottery.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 103TJL on 2016/4/6.
 * 新版 亚盘，大小球，欧赔
 */
public class CPIOddsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private View mView;
    private Context mContext;
    private RecyclerView cpi_odds_recyclerView;
    private CPIRecyclerViewAdapter cpiRecyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<Object> newsList;

    public static CPIOddsFragment newInstance(String param1, String param2) {
        CPIOddsFragment fragment = new CPIOddsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_cpi_odds, container, false);//item_cpi_odds
        System.out.println(">>>zhi"+mParam1);
//        InitView();
//        InitData();
//        cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(null, mContext);
//        cpi_odds_recyclerView.setHasFixedSize(true);
//        cpi_odds_recyclerView.setLayoutManager(linearLayoutManager);
//        cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
        return mView;
    }

    private void InitView() {
        linearLayoutManager = new LinearLayoutManager(mContext);
        cpi_odds_recyclerView = (RecyclerView) mView.findViewById(R.id.cpi_odds_recyclerView);
    }
    public void InitData(String data,int cpiNumber) {
        UiUtils.toast(mContext,"dd>>"+data+"<<"+cpiNumber);
        if(cpiNumber==0){//亚盘
            //data
        }else if(cpiNumber==1){//大小
            //
        }else if(cpiNumber==2){//欧赔
            //
        }
        newsList = new ArrayList<>();

    }


}
