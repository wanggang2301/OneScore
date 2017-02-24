package com.hhly.mlottery.frame.snooker;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.snooker.SnookerMatchAdapter;
import com.hhly.mlottery.bean.snookerbean.SnookerMatchBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.List;

/**
 * @author wangg@13322.com
 * @desr 斯洛克赛事
 * @date 2017/02/17
 */
public class SnookerMatchFragment extends Fragment {

    private static final int REQUEST_LOAD = 1;
    private static final int REQUEST_SUCESS = 2;
    private static final int REQUEST_ERROR = -1;
    private static final int REQUEST_NODATA = 0;


    private View mView;
    private Context mContext;
    private RecyclerView recyclerView;

    private SnookerMatchAdapter mSnookerMatchAdapter;
    private List<SnookerMatchBean.DataBean> dataBeanList;
    private TextView network_exception_reload_btn;
    private FrameLayout fl_nodata;
    private LinearLayout ll_error;
    private ExactSwipeRefreshLayout refresh;

    public SnookerMatchFragment() {
        // Required empty public constructor
    }


    public static SnookerMatchFragment newInstance() {
        SnookerMatchFragment snookerMatchFragment = new SnookerMatchFragment();

        return snookerMatchFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_snooker_match, container, false);
        initView();

        mHandler.sendEmptyMessage(REQUEST_LOAD);
        new Handler().postDelayed(mloadData, 500);
        return mView;
    }


    Runnable mloadData = new Runnable() {
        @Override
        public void run() {
            RequestData();
        }
    };


    private void initView() {

        refresh = (ExactSwipeRefreshLayout) mView.findViewById(R.id.refresh);
        refresh.setColorSchemeResources(R.color.bg_header);
        refresh.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), StaticValues.REFRASH_OFFSET_END));
        fl_nodata = (FrameLayout) mView.findViewById(R.id.fl_nodata);
        ll_error = (LinearLayout) mView.findViewById(R.id.network_exception_layout);
        network_exception_reload_btn = (TextView) mView.findViewById(R.id.network_exception_reload_btn);
        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        network_exception_reload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(REQUEST_LOAD);
                new Handler().postDelayed(mloadData, 500);
            }
        });

    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case REQUEST_LOAD:
                    refresh.setRefreshing(true);
                    refresh.setVisibility(View.VISIBLE);
                    fl_nodata.setVisibility(View.GONE);
                    ll_error.setVisibility(View.GONE);
                    break;

                case REQUEST_SUCESS:
                    refresh.setRefreshing(false);
                    refresh.setVisibility(View.VISIBLE);
                    fl_nodata.setVisibility(View.GONE);
                    ll_error.setVisibility(View.GONE);
                    break;

                case REQUEST_NODATA:
                    refresh.setVisibility(View.GONE);
                    refresh.setRefreshing(false);
                    fl_nodata.setVisibility(View.VISIBLE);
                    ll_error.setVisibility(View.GONE);

                    break;

                case REQUEST_ERROR:
                    refresh.setVisibility(View.GONE);
                    refresh.setRefreshing(false);
                    fl_nodata.setVisibility(View.GONE);
                    ll_error.setVisibility(View.VISIBLE);
                    break;


            }


        }
    };

    private void RequestData() {

        //BaseURLs.URL_SNOOKER_INFO_MATCH
       // String url = "http://192.168.31.1:8080/mlottery/core/mlottery/snookerData.findAppLeagueList.do";

        VolleyContentFast.requestJsonByGet(BaseURLs.URL_SNOOKER_INFO_MATCH, null, new VolleyContentFast.ResponseSuccessListener<SnookerMatchBean>() {
            @Override
            public void onResponse(SnookerMatchBean jsonObject) {
                if (jsonObject != null) {
                    if (jsonObject.getResult() == 200) {

                        dataBeanList = jsonObject.getData();
                        L.d("snooker", "成功");
                        initViewData();
                        mHandler.sendEmptyMessage(REQUEST_SUCESS);
                        //请求成功

                    } else if (jsonObject.getResult() == 400) {
                        //无数据
                        mHandler.sendEmptyMessage(REQUEST_NODATA);
                    } else if (jsonObject.getResult() == 500) {
                        //错误
                        mHandler.sendEmptyMessage(REQUEST_ERROR);
                    }
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(REQUEST_ERROR);

            }
        }, SnookerMatchBean.class);
    }

    private void initViewData() {
        mSnookerMatchAdapter = new SnookerMatchAdapter(mContext, dataBeanList);
        recyclerView.setAdapter(mSnookerMatchAdapter);
    }

}
