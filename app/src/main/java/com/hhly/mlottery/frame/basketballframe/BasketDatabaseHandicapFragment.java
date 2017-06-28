package com.hhly.mlottery.frame.basketballframe;

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
import com.hhly.mlottery.activity.BasketballTeamActivity;
import com.hhly.mlottery.adapter.basketball.BasketDatabaseDetailsAdapter;
import com.hhly.mlottery.bean.basket.basketdatabase.BasketDatabaseHandicapBean;
import com.hhly.mlottery.bean.basket.basketdatabase.BasketDatabaseHandicapDetailsBean;
import com.hhly.mlottery.callback.BasketTeamParams;
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
 * Created by A on 2016/7/15.
 * 篮球资料库让分盘
 */
public class BasketDatabaseHandicapFragment extends Fragment implements View.OnClickListener {

    Handler mHandlerData = new Handler();
    private View mView;
    private static final String PARAM_ID = "leagueId";
    private static final String PARAM2_SEASON = "season";
    private RadioGroup mRadioGroup;
    private RadioButton mAllRadioButon;
    private RadioButton mHomeRadioButon;
    private RadioButton mGuestRadioButon;
    private NoScrollListView mListView;
    private BasketDatabaseDetailsAdapter mAdapter;
    private LinearLayout mListData;
    private LinearLayout mLoadRefresh; // 网络异常
    private TextView mNodata; // 暂无数据
    private FrameLayout mLoading;// 加载中 loading...

    private final static int VIEW_STATUS_LOADING = 1; //加载中
    private final static int VIEW_STATUS_SUCCESS = 2; // 加载成功
    private final static int VIEW_STATUS_NET_ERROR = 3; // 请求失败
    private final static int VIEW_STATUS_NET_NODATA = 4; // 暂无数据

    private List<BasketDatabaseHandicapDetailsBean> mAllList = new ArrayList<>();
    private List<BasketDatabaseHandicapDetailsBean> mHomeList = new ArrayList<>();
    private List<BasketDatabaseHandicapDetailsBean> mGuestList = new ArrayList<>();

    private int mTypeSelect = 0;// 主客场选中type（默认全部）【All: 0 、 主场：1 、 客场：2】
    private String mSeason; // 赛季
    private String mLeagueId; // 联赛ID
    private TextView mRefresh;

    public static BasketDatabaseHandicapFragment newInstance(String leagueId , String season) {
        BasketDatabaseHandicapFragment fragment = new BasketDatabaseHandicapFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_ID, leagueId);
        args.putString(PARAM2_SEASON, season);
        fragment.setArguments(args);
        return fragment;
    }
    /**
     * 外部调用更新数据
     */
    public void upDate(){
        mHandlerData.postDelayed(mRun, 500); // 加载数据
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLeagueId = getArguments().getString(PARAM_ID);
        mSeason = getArguments().getString(PARAM2_SEASON);

    }

    public void setSeason(String season){
        this.mSeason = season;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.basket_database_details_handicap, container, false);

        initView();
        setIntegralDetailsOnClick();
        mHandlerData.postDelayed(mRun, 500); // 加载数据
        return mView;
    }

    private Runnable mRun = new Runnable() {
        @Override
        public void run() {
            initData();
        }
    };

    private void initView(){

        mRadioGroup = (RadioGroup)mView.findViewById(R.id.gendergroup);
        mAllRadioButon = (RadioButton)mView.findViewById(R.id.basket_database_details_all);
        mAllRadioButon.setChecked(true);//设置默认选中（all）
        mHomeRadioButon = (RadioButton)mView.findViewById(R.id.basket_database_details_home);
        mGuestRadioButon = (RadioButton)mView.findViewById(R.id.basket_database_details_guest);

        mListView = (NoScrollListView)mView.findViewById(R.id.basket_database_handicap_list);
        mListView.setFocusable(false);//让listview失去焦点，处理加载时listview置顶情况.

        mListData = (LinearLayout)mView.findViewById(R.id.basket_database_details_data);
        mListData.setVisibility(View.GONE);
        mLoadRefresh = (LinearLayout)mView.findViewById(R.id.basket_database_details_refresh);
        mNodata = (TextView)mView.findViewById(R.id.basket_database_details_nodata);
        mLoading = (FrameLayout)mView.findViewById(R.id.basket_database_loading_details);

        mRefresh = (TextView)mView.findViewById(R.id.reLoadin);
        mRefresh.setOnClickListener(this);

        RadioGroupOnClick();//切换按钮
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

    private void initData(){

        mHandler.sendEmptyMessage(VIEW_STATUS_LOADING); // loading....

        //http://192.168.31.43:8888/mlottery/core/basketballData.findAsiaLet.do?lang=zh&season=10-11&leagueId=138
//        String url = "http://192.168.31.43:8888/mlottery/core/basketballData.findAsiaLet.do";
        String url = BaseURLs.URL_BASKET_DATABASE_HANDICAP_DETAILS;
        Map<String, String> params = new HashMap<>();
        if (!mSeason.equals("-1")) {
            params.put("season" , mSeason);
        }
        params.put("leagueId", mLeagueId);
        VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<BasketDatabaseHandicapBean>() {

            @Override
            public void onResponse(BasketDatabaseHandicapBean bean) {

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
                        mHandler.sendEmptyMessage(VIEW_STATUS_NET_NODATA); // 暂无数据
                    }else{
                        mAdapter = new BasketDatabaseDetailsAdapter(getContext() , mAllList, R.layout.basket_database_details_item);
                        mAdapter.setBasketballHandicpDetailsClickListener(basketballHandicpDetailsClickListener);
                        mListView.setAdapter(mAdapter);
                        mHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
                    }
                }else{
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
        }, BasketDatabaseHandicapBean.class);

    }

    public void updataAdapter(int type){

        if (type == 0) {
            mAdapter.updateDatas(mAllList);
            mAdapter.notifyDataSetChanged();
//            L.d("mAllList.size()=====>>> " , mAllList.size()+"");
        } else if (type == 1) {
            mAdapter.updateDatas(mHomeList);
            mAdapter.notifyDataSetChanged();
//            L.d("mHomeList.size()=====>>> ", mHomeList.size() + "");
        } else if(type == 2){
            mAdapter.updateDatas(mGuestList);
            mAdapter.notifyDataSetChanged();
//            L.d("mGuestList.size()=====>>> ", mGuestList.size() + "");
        }
    }

    /**
     * RadioGroup的点击（全部、主、客切换）
     */
    private void RadioGroupOnClick(){
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == mAllRadioButon.getId()) {
                    mTypeSelect = 0;
                }else if(checkedId == mHomeRadioButon.getId()){
                    mTypeSelect = 1;
                }else if(checkedId == mGuestRadioButon.getId()){
                    mTypeSelect = 2;
                }
                updataAdapter(mTypeSelect);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reLoadin:
                mHandlerData.postDelayed(mRun , 500);
                break;
        }
    }

    private BasketballHandicpDetailsClickListener basketballHandicpDetailsClickListener;
    public interface BasketballHandicpDetailsClickListener {
        void IntegralDetailsOnClick(View view, BasketDatabaseHandicapDetailsBean teamData);
    }
    private void setIntegralDetailsOnClick(){
        basketballHandicpDetailsClickListener = new BasketballHandicpDetailsClickListener() {
            @Override
            public void IntegralDetailsOnClick(View view, BasketDatabaseHandicapDetailsBean teamData) {
                Intent intent=new Intent(getActivity(), BasketballTeamActivity.class);
                intent.putExtra(BasketTeamParams.LEAGUE_ID,mLeagueId);
                intent.putExtra(BasketTeamParams.TEAM_ID,teamData.getTeamId());
                startActivity(intent);
            }
        };
    }
}
