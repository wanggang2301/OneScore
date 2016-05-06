package com.hhly.mlottery.frame.footframe;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballInformationActivity;
import com.hhly.mlottery.adapter.IntegralExpandableAdapter;
import com.hhly.mlottery.bean.footballDetails.IntegralBean.LangueScoreBean;
import com.hhly.mlottery.widget.ExactSwipeRefrashLayout;
import com.hhly.mlottery.widget.PinnedHeaderExpandableListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: OneScore
 * @author:Administrator luyao
 * @Description: 积分榜Fragment
 * @data: 2016/3/29 16:32
 */
public class IntegralFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "IntegralFragment";


    private static View view;
    private static Context context;
    private static LinearLayout mErrorLayout;//网络失败
    private TextView mReloadBtn;//刷新
    private static ExactSwipeRefrashLayout mSwipeRefreshLayout;//下拉刷新
    private static boolean isLoadedData = false; // 判断是否加载过数据
    private static String mLeagueId;
    private static TextView mTxt_title;
    private boolean isNetSuccess = true;// 告诉筛选页面数据是否加载成功
    private String mLeagueType;  //联赛类型
    private TextView mReload_btn;//重新加载按钮
    private List<String> mGroupDataList = new ArrayList<>();//小组列表
    private List<List<LangueScoreBean.ListBean>> mChildrenDataList = new ArrayList<>();//小组数据
    private PinnedHeaderExpandableListView explistview_live;//列表显示数据
    private IntegralExpandableAdapter mExpandableAdapter;//数据显示适配器
    private RelativeLayout mNoDataLayout;//无赛事显示
    private final static int VIEW_STATUS_LOADING = 11;
    private final static int VIEW_STATUS_SUCCESS = 33;
    private static final int VIEW_STATUS_NET_ERROR = 44;
    private static final int VIEW_STATUS_NO_DATA = 55;
    private Handler mViewHandler = new Handler() {
        public void handleMessage(Message msg) {
            isNetSuccess = true;
            switch (msg.what) {
                case VIEW_STATUS_LOADING:
                    mErrorLayout.setVisibility(View.GONE);
                    mNoDataLayout.setVisibility(View.GONE);
                    //mListView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    if (isLoadedData) {
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                    mErrorLayout.setVisibility(View.GONE);
                    break;
                case VIEW_STATUS_SUCCESS:
                    //mScreen.setVisibility(View.VISIBLE);
                    mNoDataLayout.setVisibility(View.GONE);
                    mErrorLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    //mListView.setVisibility(View.VISIBLE);

                    break;

                case VIEW_STATUS_NO_DATA:
                    mNoDataLayout.setVisibility(View.VISIBLE);
                    //mListView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mErrorLayout.setVisibility(View.GONE);
//                    mLine.setVisibility(View.INVISIBLE);
                    break;
                case VIEW_STATUS_NET_ERROR:
                    mNoDataLayout.setVisibility(View.GONE);
                    //mListView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mErrorLayout.setVisibility(View.VISIBLE);
//                    mLine.setVisibility(View.INVISIBLE);
                    break;
                default:
                    break;
            }
        }
    };

    public static IntegralFragment newInstance() {

        IntegralFragment fragment = new IntegralFragment();
        return fragment;
    }


    /*public IntegralFragment(List<String> groupDataList, List<List<IntegralBean.LangueScoreBean.ListBean>> childDataList, String leagueType) {
        this.mLeagueType=leagueType;
        this.mGroupDataList = groupDataList;java.lang.Object bvos;
        this.mChildrenDataList = childDataList;
        Log.v(TAG, "integral_groupDataList==========" + mGroupDataList);
        Log.v(TAG, "integral_childDataList==========" + mChildrenDataList);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.football_integral, container, false);
        context = getActivity();

        initView();
        mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
        return view;
    }

    public void initData(List<String> groupDataList, List<List<LangueScoreBean.ListBean>> childDataList, String leagueType) {


        this.mGroupDataList = groupDataList;
        this.mChildrenDataList = childDataList;
        this.mLeagueType = leagueType;

        if (mGroupDataList.isEmpty()) {
            mViewHandler.sendEmptyMessage(VIEW_STATUS_NO_DATA);
        } else {
            mExpandableAdapter = new IntegralExpandableAdapter(childDataList, groupDataList, context, explistview_live, leagueType);
            //Log.v(TAG, "integral_groupDataList==========" + groupDataList);
            // Log.v(TAG, "integral_childDataList==========" + childDataList);
            explistview_live.setAdapter(mExpandableAdapter);
            //默认展开所有数据
            for (int i = 0; i < mGroupDataList.size(); i++) {
                explistview_live.expandGroup(i);
            }

            explistview_live.setGroupIndicator(null);
            mExpandableAdapter.notifyDataSetChanged();

            isLoadedData = true;
            mViewHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
        }

    }


    private void initView() {

        //获取ListView
        explistview_live = (PinnedHeaderExpandableListView) view.findViewById(R.id.football_integral_lv);
        //设置悬浮头部VIEW
        // explistview_live.setHeaderView(getActivity().getLayoutInflater().inflate(R.layout.integral_items_group, explistview_live, false));

        //加载图片状态
        mErrorLayout = (LinearLayout) view.findViewById(R.id.network_exception_layout);
        mReloadBtn = (TextView) view.findViewById(R.id.network_exception_reload_btn);
        mReload_btn = (TextView) view.findViewById(R.id.network_exception_reload_btn);
        mReload_btn.setOnClickListener(this);

        //下拉刷新
        mSwipeRefreshLayout = (ExactSwipeRefrashLayout) view.findViewById(R.id.football_infor_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //没有数据
        mNoDataLayout = (RelativeLayout) view.findViewById(R.id.football_integral_unfocus_ll);
        //获取联赛简称指标
        mTxt_title = (TextView) view.findViewById(R.id.txt_title);
        //  initData(mGroupDataList, mChildrenDataList, mLeagueType, "");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.network_exception_reload_btn:
                mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
                // initData();
                if (getActivity() != null) {
                    ((FootballInformationActivity) getActivity()).intiData(false);
                }
                break;
        }


    }

    @Override
    public void onRefresh() {
        if (getActivity() != null) {
            ((FootballInformationActivity) getActivity()).intiData(false);
        }
    }


    public void requestFail() {
        mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("IntegralFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("IntegralFragment");
    }
}
