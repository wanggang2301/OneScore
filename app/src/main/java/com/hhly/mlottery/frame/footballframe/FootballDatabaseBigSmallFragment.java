package com.hhly.mlottery.frame.footballframe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballTeamInfoActivity;
import com.hhly.mlottery.adapter.football.FootballDatabaseDetailsAdapter;
import com.hhly.mlottery.bean.footballDetails.footballdatabasebean.DatabaseBigSmallBean;
import com.hhly.mlottery.bean.footballDetails.footballdatabasebean.FootballDatabaseBigSmallBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.NoScrollListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description:
 * author: yixq
 * Created by A on 2016/10/9.
 * 足球资料库大小盘
 */

public class FootballDatabaseBigSmallFragment extends Fragment implements View.OnClickListener {

    private int BHtype = 1;//区分让分盘和大小盘
    private View mView;
    private static final String PARAM_ID = "leagueId";
    private static final String PARAM_LEAGUEDATA = "leagueDate";
    private static final String PARAM_TYPE = "type";
    private RadioGroup mRadioGroup;
    private RadioButton mAllRB;
    private RadioButton mHomeRB;
    private RadioButton mGuestRB;
    private NoScrollListView mListview;
    private LinearLayout mListData;
    private LinearLayout mLoadRefresh;
    private TextView mNodata;
    private FrameLayout mLoading;

    private final static int VIEW_STATUS_LOADING = 1; //加载中
    private final static int VIEW_STATUS_SUCCESS = 2; // 加载成功
    private final static int VIEW_STATUS_NET_ERROR = 3; // 请求失败
    private final static int VIEW_STATUS_NET_NODATA = 4; // 暂无数据

    private int mTypeSelect = 0;// 主客场选中type（默认全部）【All: 0 、 主场：1 、 客场：2】
    private String mLeagueData; // 赛季
    private String mLeagueId; // 联赛ID
    private String mType;//联赛类型

    private List<DatabaseBigSmallBean> mAllList = new ArrayList<>();
    private List<DatabaseBigSmallBean> mHomeList = new ArrayList<>();
    private List<DatabaseBigSmallBean> mGuestList = new ArrayList<>();

    private FootballDatabaseDetailsAdapter mAdapter;
    Handler mHandlerData = new Handler();

    public static FootballDatabaseBigSmallFragment newInstance(String leagueId ,String type , String leagueData) {

        FootballDatabaseBigSmallFragment fragment = new FootballDatabaseBigSmallFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_ID , leagueId);
        args.putString(PARAM_LEAGUEDATA, leagueData);
        args.putString(PARAM_TYPE , type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLeagueId = getArguments().getString(PARAM_ID);
        mLeagueData = getArguments().getString(PARAM_LEAGUEDATA);
        mType = getArguments().getString(PARAM_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.football_database_details_big_small , container ,false);

        initView();
        setBigSmallDetailsOnClick();
        mHandlerData.postDelayed(mRun, 500); // 加载数据
        return mView;
    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case VIEW_STATUS_LOADING:
                    mLoading.setVisibility(View.VISIBLE);
                    mListData.setVisibility(View.GONE);
                    mLoadRefresh.setVisibility(View.GONE);
                    mNodata.setVisibility(View.GONE);
                    break;
                case VIEW_STATUS_SUCCESS:
                    mLoading.setVisibility(View.GONE);
                    mListData.setVisibility(View.VISIBLE);
                    mLoadRefresh.setVisibility(View.GONE);
                    mNodata.setVisibility(View.GONE);
                    break;
                case VIEW_STATUS_NET_ERROR:
                    mLoading.setVisibility(View.GONE);
                    mListData.setVisibility(View.GONE);
                    mLoadRefresh.setVisibility(View.VISIBLE);
                    mNodata.setVisibility(View.GONE);
                    break;
                case VIEW_STATUS_NET_NODATA:
                    mLoading.setVisibility(View.GONE);
                    mListData.setVisibility(View.GONE);
                    mLoadRefresh.setVisibility(View.GONE);
                    mNodata.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    private void initView() {

        mRadioGroup = (RadioGroup)mView.findViewById(R.id.gendergroup);
        mAllRB = (RadioButton)mView.findViewById(R.id.football_database_details_all);
        mAllRB.setChecked(true);
        mHomeRB = (RadioButton)mView.findViewById(R.id.football_database_details_home);
        mGuestRB = (RadioButton)mView.findViewById(R.id.football_database_details_guest);

        mListview = (NoScrollListView)mView.findViewById(R.id.football_database_handicap_list);
        mListview.setFocusable(false);

        mListData = (LinearLayout)mView.findViewById(R.id.football_database_details_data);
        mListData.setVisibility(View.GONE);
        mLoadRefresh = (LinearLayout)mView.findViewById(R.id.football_database_details_refresh);
        mNodata = (TextView)mView.findViewById(R.id.football_database_details_nodata);
        mLoading = (FrameLayout)mView.findViewById(R.id.football_database_loading_details);

        TextView mRefresh = (TextView)mView.findViewById(R.id.reLoadin);
        mRefresh.setOnClickListener(this);
        RadioGroupOnClick();
    }

    private void initData(){

        mHandler.sendEmptyMessage(VIEW_STATUS_LOADING); // loading....

        String url = BaseURLs.URL_FOOTBALL_DATABASE_BIG_SMALL_DETAILS;
//        String url = "http://192.168.33.32:8080/mlottery/core/androidLeagueData.findAndroidLeagueOu.do";
        Map<String, String> params = new HashMap<>();
        if (!mLeagueData.equals("-1")) {
            params.put(PARAM_LEAGUEDATA , mLeagueData);
        }
        params.put(PARAM_ID, mLeagueId);
        params.put(PARAM_TYPE , mType);
        VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<FootballDatabaseBigSmallBean>() {

            @Override
            public void onResponse(FootballDatabaseBigSmallBean bean) {

                if (bean == null) {
                    mHandler.sendEmptyMessage(VIEW_STATUS_NET_NODATA); // 暂无数据
                    return;
                }

                if (bean.getAll() == null && bean.getHome() == null && bean.getGuest() == null) {
                    mHandler.sendEmptyMessage(VIEW_STATUS_NET_NODATA); // 暂无数据
                    return;
                }
                mAllList = bean.getAll(); //All -- 默认
                mHomeList = bean.getHome(); //主场
                mGuestList = bean.getGuest();//客场

                if (mAdapter == null) {
                    if (bean.getAll() == null || bean.getAll().size() == 0) {
                        mHandler.sendEmptyMessage(VIEW_STATUS_NET_NODATA);
                    }else{
                        mAdapter = new FootballDatabaseDetailsAdapter(getContext(), mAllList, R.layout.football_database_details_item , BHtype);
                        mAdapter.setFootballTeamBigSmallDetailsClickListener(footballTeamBigSmallDetailsClickListener);
                        mListview.setAdapter(mAdapter);
                        mHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
                    }
                } else {
                    if ((mTypeSelect == 0 && (bean.getAll() == null || bean.getAll().equals("") || bean.getAll().size() == 0))
                            || (mTypeSelect == 1 && (bean.getHome() == null || bean.getHome().equals("") || bean.getHome().size() == 0))
                            || (mTypeSelect == 2 && (bean.getGuest() == null || bean.getGuest().equals("") || bean.getGuest().size() == 0))) {
                        mHandler.sendEmptyMessage(VIEW_STATUS_NET_NODATA); // 暂无数据
                    }else{
                        updataAdapter(mTypeSelect);
                        mHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
                    }
                }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
            }
        }, FootballDatabaseBigSmallBean.class);
    }

    public void setLeagueData(String leagueData){
        this.mLeagueData = leagueData;
    }
    /**
     * 外部调用更新数据
     */
    public void upDate(){
        mHandlerData.postDelayed(mRun, 500); // 加载数据
    }

    private Runnable mRun = new Runnable() {
        @Override
        public void run() {
            initData();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reLoadin:
                mHandlerData.postDelayed(mRun , 500);
                break;
        }
    }
    /**
     * RadioGroup的点击（全部、主、客切换）
     */
    private void RadioGroupOnClick(){
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == mAllRB.getId()) {
                    mTypeSelect = 0;
                }else if(checkedId == mHomeRB.getId()){
                    mTypeSelect = 1;
                }else if(checkedId == mGuestRB.getId()){
                    mTypeSelect = 2;
                }
                updataAdapter(mTypeSelect);
            }
        });
    }

    public void updataAdapter(int type){
        if (type == 0) {
            mAdapter.updateDatas(mAllList);
            mAdapter.notifyDataSetChanged();
        } else if (type == 1) {
            mAdapter.updateDatas(mHomeList);
            mAdapter.notifyDataSetChanged();
        } else if(type == 2){
            mAdapter.updateDatas(mGuestList);
            mAdapter.notifyDataSetChanged();
        }
    }
    private FootballTeamBigSmallDetailsClickListener footballTeamBigSmallDetailsClickListener;
    // 购买(查看)的点击监听
    public interface FootballTeamBigSmallDetailsClickListener {
        void BigSmallDetailsOnClick(View view, DatabaseBigSmallBean beanData);
    }
    private void setBigSmallDetailsOnClick(){
        footballTeamBigSmallDetailsClickListener = new FootballTeamBigSmallDetailsClickListener() {
            @Override
            public void BigSmallDetailsOnClick(View view, DatabaseBigSmallBean beanData) {
                        Intent homeIntent = new Intent(getContext(), FootballTeamInfoActivity.class);
                        homeIntent.putExtra("TEAM_ID", beanData.getTeamId());
                        homeIntent.putExtra("TITLE_TEAM_NAME", beanData.getTeamName());
                        startActivity(homeIntent);
            }
        };
    }
}
