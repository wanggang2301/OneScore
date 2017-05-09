package com.hhly.mlottery.frame.basketballframe;

import android.annotation.TargetApi;
import android.os.Build;
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
import android.widget.ListView;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.adapter.basketball.BasketOddsAdapter;
import com.hhly.mlottery.bean.basket.basketdetails.BasketDetailOddsBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andy on 2016/4/13 11:24.
 * <p/>
 * 描述：篮球 欧赔亚盘 大小球
 */
public class BasketOddsFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final static int VIEW_STATUS_LOADING = 1;
    private final static int VIEW_STATUS_SUCCESS = 3;
    private final static int VIEW_STATUS_NET_ERROR = 4;

    private String mThirdId="100";
    private String mType="euro";
    private View mView;
    /**外层listview*/
    private ListView listView;

    /**
     * 异常界面
     */
    private LinearLayout mExceptionLayout;
    /**
     * 无数据的界面
     */
    private LinearLayout mNodataLayout;
    /**
     * 点击刷新
     */
    private TextView mBtnRefresh;
    /**
     * 加载中
     */
    private FrameLayout mProgressBarLayout;

    /**
     * 客胜
     */
    private TextView mTitleGuestWin;
    /**
     * 盘口
     */
    private TextView mTitleHandicap;

    /**
     * 主胜
     */
    private TextView mTitleHomeWin;


    /**
     * 主listviewAdapter
     */
    private BasketOddsAdapter mOddsAdapter;
    /**
     * 主list数据
     */
    private List<BasketDetailOddsBean.CompanyOddsEntity> mOddsCompanyList = new ArrayList<>();

    public BasketOddsFragment() {
    }

    public static BasketOddsFragment newInstance(String param1, String param2) {
        BasketOddsFragment fragment = new BasketOddsFragment();
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
            mThirdId=getArguments().getString(ARG_PARAM1);
            mType=getArguments().getString(ARG_PARAM2);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_basket_odds, container, false);

            initView();
            initData();

        return mView;
    }

    private Handler mViewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case VIEW_STATUS_LOADING:
                    mExceptionLayout.setVisibility(View.GONE);
                    mProgressBarLayout.setVisibility(View.VISIBLE);
                    mNodataLayout.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                    break;
                case VIEW_STATUS_SUCCESS:

                    mProgressBarLayout.setVisibility(View.GONE);
                    mExceptionLayout.setVisibility(View.GONE);
                    if(mOddsCompanyList.size()==0){
                        mNodataLayout.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }else{
                        mNodataLayout.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    }

                    break;
                case VIEW_STATUS_NET_ERROR:

                    mProgressBarLayout.setVisibility(View.GONE);
                    mNodataLayout.setVisibility(View.GONE);
                    mExceptionLayout.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initView() {

        listView = (ListView) mView.findViewById(R.id.basket_odds_listview);
        listView.setFocusable(false);

        //异常数据 处理
        mProgressBarLayout= (FrameLayout) mView.findViewById(R.id.basket_odds_progressbar);

        //无数据界面
        mNodataLayout= (LinearLayout) mView.findViewById(R.id.odds_nodata_container);
//        mNodataLayout.setBackgroundColor(getResources().getColor(R.color.black_title));
        //网络异常的
        mExceptionLayout= (LinearLayout) mView.findViewById(R.id.basket_odds_net_error);
        mBtnRefresh= (TextView) mView.findViewById(R.id.network_exception_reload_btn);
        mBtnRefresh.setOnClickListener(this);

        //不同界面对应不同的标题
        mTitleHandicap= (TextView) mView.findViewById(R.id.basket_odds_handicap);

        mTitleGuestWin= (TextView) mView.findViewById(R.id.basket_odds_guest_win);

        mTitleHomeWin= (TextView) mView.findViewById(R.id.basket_odds_home_win);

        if(mType.equals(BasketDetailsActivityTest.ODDS_EURO)){
            mTitleHandicap.setVisibility(View.GONE);
            mTitleGuestWin.setText(getActivity().getResources().getString(R.string.basket_analyze_guest_win));
            mTitleHomeWin.setText(getActivity().getResources().getString(R.string.basket_analyze_home_win));
        }
        else if(mType.equals(BasketDetailsActivityTest.ODDS_LET)){
            mTitleHandicap.setVisibility(View.VISIBLE);
            mTitleGuestWin.setText(getActivity().getResources().getString(R.string.basket_analyze_guest_win));
            mTitleHomeWin.setText(getActivity().getResources().getString(R.string.basket_analyze_home_win));
        }
        else if(mType.equals(BasketDetailsActivityTest.ODDS_SIZE)){
            mTitleHandicap.setVisibility(View.VISIBLE);
            mTitleGuestWin.setText(getActivity().getResources().getString(R.string.odd_home_big_txt));
            mTitleHomeWin.setText(getActivity().getResources().getString(R.string.odd_guest_big_txt));
        }


    }

    public void initData() {
        mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
        Map<String, String> params = new HashMap<>();
        params.put("oddsType", mType);
        params.put("thirdId", mThirdId);
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_BASKET_ODDS, params, new VolleyContentFast.ResponseSuccessListener<BasketDetailOddsBean>() {
            @Override
            public void onResponse(BasketDetailOddsBean oddsBean) {
                loadData(oddsBean);
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
            }
        }, BasketDetailOddsBean.class);
    }

    /**
     * 请求到网络数据后加载数据
     */
    private void loadData(BasketDetailOddsBean oddsBean) {
        if(getActivity()!=null){
            mOddsCompanyList=oddsBean.getCompanyOdds();
            mOddsAdapter = new BasketOddsAdapter(getActivity(), mOddsCompanyList,mType);//欧赔
            listView.setAdapter(mOddsAdapter);
            mViewHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
        }

    }
//

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.network_exception_reload_btn:
                MobclickAgent.onEvent(MyApp.getContext(), "BasketOddsFragment_NotNet");
                mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
                initData();
        }

    }
}
