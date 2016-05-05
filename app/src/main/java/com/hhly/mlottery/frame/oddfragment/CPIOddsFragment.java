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
import com.hhly.mlottery.bean.UpdateInfo;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private NewOddsInfo mNewOddsInfo;
    private List<NewOddsInfo.AllInfoBean> mAllInfoBean ;

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
        InitView();
        switchd();
        return mView;
    }

    private void InitView() {
        linearLayoutManager = new LinearLayoutManager(mContext);
        cpi_odds_recyclerView = (RecyclerView) mView.findViewById(R.id.cpi_odds_recyclerView);
        cpi_odds_recyclerView.setHasFixedSize(true);
        cpi_odds_recyclerView.setLayoutManager(linearLayoutManager);
    }
    public void InitData(String date, final int cpiNumber1,final int cpiNumber2,final int cpiNumber3) {
        System.out.println(">>>dd"+"dd>>" + date + "<<" + cpiNumber1+cpiNumber2+cpiNumber3);
        //http://192.168.10.242:8989/mlottery/core/footBallIndexCenter.findIosIndexCenter.do?date=2016-05-04&lang=zh&type=1
        String stUrl = "http://192.168.10.242:8989/mlottery/core/footBallIndexCenter.findIosIndexCenter.do?";
        Map<String, String> map = new HashMap<>();
//        System.out.println(">>>date+" + date);

        if (cpiNumber1 == 0) {
            //亚盘
            map.put("date", date);
            map.put("type", "1");
        } else if (cpiNumber2 == 1) {
            //大小
            map.put("date", date);
            map.put("type", "3");
        }else if (cpiNumber3 == 2) {
            //欧赔
            map.put("date", date);
            map.put("type", "2");
        }
        // 2、连接服务器
        VolleyContentFast.requestJsonByPost(stUrl, map,new VolleyContentFast.ResponseSuccessListener<NewOddsInfo>() {
            @Override
            public synchronized void onResponse(final NewOddsInfo json) {
                if (json != null) {
                    mAllInfoBean = json.getAllInfo();
                    if(mAllInfoBean!=null && mAllInfoBean.size()>0){
                        if(cpiNumber1==0){
                            cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBean, mContext,"plate");
                            cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
                        }else if(cpiNumber2==1){
                            cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBean, mContext,"big");
                            cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
                        } else if(cpiNumber3==2){
                            cpiRecyclerViewAdapter = new CPIRecyclerViewAdapter(mAllInfoBean, mContext,"op");
                            cpi_odds_recyclerView.setAdapter(cpiRecyclerViewAdapter);
                        }

                    }

                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
            }
        }, NewOddsInfo.class);
    }

    public void switchd() {
        switch (mParam1) {
            case "plate":
                //亚盘
                InitData(UiUtils.getDay(0), 0,55,55);
                break;
            case "big":
                //大小
                InitData(UiUtils.getDay(0), 55,1,55);
                break;
            case "op":
                //欧赔
                InitData(UiUtils.getDay(0), 55,55,2);
                break;
            default:
                break;


        }
    }

}
