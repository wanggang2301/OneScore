package com.hhly.mlottery.frame.snookerFragment;

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
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.SnookerMatchDetailActivity;
import com.hhly.mlottery.adapter.snooker.SnookerOddsAdapter;
import com.hhly.mlottery.bean.snookerbean.snookerDetail.SnookerOddsBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * * created by mdy155
 * date 2017.2/16
 * 斯诺克亚盘页面
 */
public class SnookerLetFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    /**
     * 比赛ID
     */
    private String mThirdId;
    /**
     * 赔率类型
     */
    private String mOddsType;

    @BindView(R.id.snooker_odds_title_home)
    TextView mHomeText;
    @BindView(R.id.snooker_odds_title_handicap)
    TextView mHandicap;
    @BindView(R.id.snooker_odds_title_guest)
    TextView mGuestText;
    @BindView(R.id.snooker_odds_recyclerView)
    RecyclerView mRecyclerView;
    private View mView;

    /**
     * 异常界面
     */
    @BindView(R.id.snooker_odds_networkError)
    FrameLayout mExceptionLayout;
    /**
     * 无数据的界面
     */
    @BindView(R.id.snooker_odds_noData)
    FrameLayout mNodataLayout;
    /**
     * 点击刷新
     */
    @BindView(R.id.snooker_odds_reLoading)
    TextView mBtnRefresh;
    /**
     * 加载中
     */
    @BindView(R.id.snooker_odds_progressbar)
    FrameLayout mProgressBarLayout;

    private SnookerOddsAdapter mAdapter;

    private final static int VIEW_STATUS_LOADING = 1;
    private final static int VIEW_STATUS_SUCCESS = 2;
    private final static int VIEW_STATUS_NET_ERROR = 3;
    private final static int VIEW_STATUS_NO_DATA=4;

    /**
     * recycleView的数据源
     */
    private List<SnookerOddsBean.ListOddEntity> mListOdds=new ArrayList<>();
    private SnookerOddsBean mData;


    public SnookerLetFragment() {

    }

    public static SnookerLetFragment newInstance(String thirdId, String oddsType) {
        SnookerLetFragment fragment = new SnookerLetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, thirdId);
        args.putString(ARG_PARAM2, oddsType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mThirdId = getArguments().getString(ARG_PARAM1);
            mOddsType = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_snooker_let, container, false);

        ButterKnife.bind(this,mView);
        initView();
        initData();

        return mView;
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case VIEW_STATUS_LOADING:
                    mExceptionLayout.setVisibility(View.GONE);
                    mProgressBarLayout.setVisibility(View.VISIBLE);
                    mNodataLayout.setVisibility(View.GONE);
                    break;
                case VIEW_STATUS_SUCCESS:
                    mProgressBarLayout.setVisibility(View.GONE);
                    mExceptionLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    break;
                case VIEW_STATUS_NET_ERROR:
                    mProgressBarLayout.setVisibility(View.GONE);
                    mNodataLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                    mExceptionLayout.setVisibility(View.VISIBLE);
                    break;
                // 无数据界面
                case VIEW_STATUS_NO_DATA:
                    mNodataLayout.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    break;
            }
        }
    };


    private void initView(){

        mProgressBarLayout.setVisibility(View.GONE);
        mNodataLayout.setVisibility(View.GONE);
        mExceptionLayout.setVisibility(View.GONE);

        if(mOddsType.equals(SnookerMatchDetailActivity.ODDS_LET)){
            //标题
            mHomeText.setText(getResources().getString(R.string.snooker_odds_peilv));
            mGuestText.setText(getResources().getString(R.string.snooker_odds_peilv));

        }else if(mOddsType.equals(SnookerMatchDetailActivity.ODDS_SIZE)){

            mHomeText.setText(getResources().getString(R.string.odd_home_big_txt));
            mGuestText.setText(getResources().getString(R.string.odd_guest_big_txt));
        }
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        mAdapter=new SnookerOddsAdapter(mListOdds);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mBtnRefresh.setOnClickListener(this);

    }

    public void initData(){

        handler.sendEmptyMessage(VIEW_STATUS_LOADING);

        Map<String,String> params=new HashMap<>();
        params.put("thirdId",mThirdId);
        params.put("oddType",mOddsType);
        VolleyContentFast.requestJsonByGet(BaseURLs.SNOOKER_ODDS_URL,params, new VolleyContentFast.ResponseSuccessListener<SnookerOddsBean>() {
            @Override
            public void onResponse(SnookerOddsBean jsonObject) {

                if(jsonObject.getResult()==200){


                    mData=jsonObject;
                    if(mData.getListOdd()==null||mData.getListOdd().size()==0){
                        handler.sendEmptyMessage(VIEW_STATUS_NO_DATA);
                    }else {
                        handler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
                        loadData();
                    }


                }
            }


        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                handler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);

            }
        }, SnookerOddsBean.class);


    }

    /**
     * 展示数据
     */
    private void loadData(){
        mListOdds.clear();
        mListOdds.addAll(mData.getListOdd());
        mAdapter.notifyDataSetChanged();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.snooker_odds_reLoading:
                initData();
                break;
        }

    }
}
