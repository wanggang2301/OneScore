package com.hhly.mlottery.frame.basketballframe;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivity;
import com.hhly.mlottery.adapter.basketball.BasketOddsAdapter;
import com.hhly.mlottery.adapter.basketball.BasketOddsDetailsAdapter;
import com.hhly.mlottery.bean.basket.BasketDetails.BasketDetailOddsBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.ObservableListView;
import com.hhly.mlottery.widget.ExactSwipeRefrashLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by andy on 2016/4/13 11:24.
 * <p/>
 * 描述：篮球 欧赔亚盘 大小球
 */
public class BasketOddsFragment extends BasketDetailsBaseFragment<ObservableListView> implements SwipeRefreshLayout.OnRefreshListener ,View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final static int VIEW_STATUS_LOADING = 1;
    private final static int VIEW_STATUS_SUCCESS = 3;
    private final static int VIEW_STATUS_NET_ERROR = 4;

    private String mThirdId="100";
    private String mType="euro";
    private int mItemCount;//listview Item的数量
    private int itemHeight; //一个listview的item的高度。
    private int h;//屏幕的绝对高度
    private View paddingviewButtom;//footer view

    private int mFooterHeight;
    private View mView;
    /**外层listview*/
    private ObservableListView listView;

    /**
     * 异常界面
     */
    private LinearLayout mExceptionLayout;
    /**
     * 无数据的界面
     */
    private LinearLayout mNodataLayout;


    /**
     * 刷新
     */
    private RelativeLayout mProgressBarLayout;
    /**下拉刷新界面*/
    private ExactSwipeRefrashLayout mRefreshLayout;

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
    private BasketOddsDetailsAdapter mRightAdapter;
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

                    break;
                case VIEW_STATUS_SUCCESS:
                  //footer view's height is  the screen's  height - title's height(2*53)-item's count*53
                    if(listView.getFooterViewsCount()==1){ //从网络异常到加载成功。footerview的高度需要重新计算。所以先remove掉。再加。
                        listView.removeFooterView(paddingviewButtom);
                    }
                    if(listView.getFooterViewsCount()==0&&getActivity()!=null){//防止刷新的时候不停的加  防止getActivity() 为null
                        paddingviewButtom=new View(getActivity());
                        AbsListView.LayoutParams lp1 = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,mFooterHeight);

                        paddingviewButtom.setLayoutParams(lp1);
                        paddingviewButtom.setBackgroundColor(getResources().getColor(R.color.black_title));
                        listView.addFooterView(paddingviewButtom);
                    }

                    listView.setAdapter(mOddsAdapter);
                    mOddsAdapter.notifyDataSetChanged();

                    mProgressBarLayout.setVisibility(View.GONE);
                    mExceptionLayout.setVisibility(View.GONE);

                    if(mOddsCompanyList.size()!=0&&getActivity()!=null){//有数据则显示分割线
                        listView.setDivider(getActivity().getResources().getDrawable(R.color.basket_odds_divider));
                        listView.setDividerHeight(1);
                    }
                    break;
                case VIEW_STATUS_NET_ERROR:
                    if(listView.getFooterViewsCount()==1){   // 从有数据到网络异常   footerview的高度需要重新计算。所以先remove掉。再加。
                        listView.removeFooterView(paddingviewButtom);
                    }
                    if(listView.getFooterViewsCount()==0&&getActivity()!=null){//防止刷新的时候不停的加

                        paddingviewButtom=new View(getActivity());

                        AbsListView.LayoutParams lp1 = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,mFooterHeight);

                        paddingviewButtom.setLayoutParams(lp1);
                        paddingviewButtom.setBackgroundColor(getResources().getColor(R.color.black_title));
                        listView.addFooterView(paddingviewButtom);
                    }
                    listView.setAdapter(mOddsAdapter);

                    mProgressBarLayout.setVisibility(View.GONE);
                    mNodataLayout.setVisibility(View.GONE);
                    mExceptionLayout.setVisibility(View.VISIBLE);
                    if(!mOddsCompanyList.isEmpty()){
                          mOddsCompanyList.clear();
                        mOddsAdapter.notifyDataSetChanged();
                    }
                    if(getActivity()!=null){
                        listView.setDivider(getActivity().getResources().getDrawable(R.color.black_title));//设置灰色分割线
                        listView.setDividerHeight(1);
                    }
                    break;
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initView() {

         itemHeight=getResources().getDimensionPixelSize(R.dimen.item_height);//53dp
         WindowManager wm= (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
         h=wm.getDefaultDisplay().getHeight();

        //异常数据 处理
        mProgressBarLayout= (RelativeLayout) mView.findViewById(R.id.basket_odds_progressbar);
        listView = (ObservableListView) mView.findViewById(R.id.scroll);
        //头部
        View paddingView = new View(getActivity());
        final int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                flexibleSpaceImageHeight);
        paddingView.setLayoutParams(lp);

        // This is required to disable header's list selector effect
        paddingView.setClickable(true);
       //标题
        View viewTitle = View.inflate(getActivity(), R.layout.basket_odds_title,null);
        //欧赔亚盘大小球标题设置
        mTitleHandicap= (TextView) viewTitle.findViewById(R.id.basket_odds_handicap);

        mTitleGuestWin= (TextView) viewTitle.findViewById(R.id.basket_odds_guest_win);

        mTitleHomeWin= (TextView) viewTitle.findViewById(R.id.basket_odds_home_win);

        if(mType.equals(BasketDetailsActivity.ODDS_EURO)){
            mTitleHandicap.setVisibility(View.GONE);
            mTitleGuestWin.setText(getActivity().getResources().getString(R.string.basket_analyze_guest_win));
            mTitleHomeWin.setText(getActivity().getResources().getString(R.string.basket_analyze_home_win));
        }
        else if(mType.equals(BasketDetailsActivity.ODDS_LET)){
            mTitleHandicap.setVisibility(View.VISIBLE);
            mTitleGuestWin.setText(getActivity().getResources().getString(R.string.basket_analyze_guest_win));
            mTitleHomeWin.setText(getActivity().getResources().getString(R.string.basket_analyze_home_win));
        }
        else if(mType.equals(BasketDetailsActivity.ODDS_SIZE)){
            mTitleHandicap.setVisibility(View.VISIBLE);
            mTitleGuestWin.setText(getActivity().getResources().getString(R.string.odd_home_big_txt));
            mTitleHomeWin.setText(getActivity().getResources().getString(R.string.odd_guest_big_txt));
        }

        listView.addHeaderView(paddingView);
        listView.addHeaderView(viewTitle);

        //无数据界面
        View noDataView =View.inflate(getActivity(), R.layout.basket_odds_nodata,null);
        mNodataLayout= (LinearLayout) noDataView.findViewById(R.id.odds_nodata_container);
        mNodataLayout.setBackgroundColor(getResources().getColor(R.color.black_title));
        mNodataLayout.setVisibility(View.GONE);
        listView.addHeaderView(noDataView);

        //网络异常的
        View errorView=View.inflate(getActivity(), R.layout.basket_odds_new_error,null);
        mExceptionLayout= (LinearLayout) errorView.findViewById(R.id.basket_odds_net_error);
        mExceptionLayout.setBackgroundColor(getResources().getColor(R.color.black_title));
        mExceptionLayout.setVisibility(View.GONE);
        listView.addHeaderView(errorView);
        //点击刷新
        errorView.findViewById(R.id.network_exception_reload_btn).setOnClickListener(BasketOddsFragment.this);

        listView.setTouchInterceptionViewGroup((ViewGroup) mView.findViewById(R.id.basket_odds_root));

        listView.setScrollViewCallbacks(this);

        listView.setSelectionFromTop(0, 0);
        updateFlexibleSpace(0, mView);

        mRefreshLayout= (ExactSwipeRefrashLayout) mView.findViewById(R.id.basket_euro_refreshlayout);
        mRefreshLayout.setColorSchemeResources(R.color.tabhost);
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void initData() {
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
                mFooterHeight=h-(2+3)*itemHeight;//网络异常界面的高度等于3个item
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
            mItemCount=mOddsCompanyList.size();
            mFooterHeight=h-(2+mItemCount)*itemHeight;
            mOddsAdapter = new BasketOddsAdapter(getActivity(), mOddsCompanyList,mType);//欧赔
            //显示无数据界面
            if(mOddsCompanyList.size()==0){
                mNodataLayout.setVisibility(View.VISIBLE);
                listView.setDivider(getActivity().getResources().getDrawable(R.color.black_title));
                listView.setDividerHeight(1);
                mFooterHeight=h-(2+3)*itemHeight;//没数据的界面的高度等于3个item
            }
            mViewHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
        }

    }
//

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.network_exception_reload_btn:
                MobclickAgent.onEvent(MyApp.getContext(),"BasketOddsFragment_NotNet");
                mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
                initData();

                BasketDetailsActivity basketDetailsActivity= (BasketDetailsActivity) getActivity();
                basketDetailsActivity.refreshData();
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setScrollY(int scrollY, int threshold) {
        ObservableListView listView;
        View view = getView();
        if (view == null) {
            return;
        }
            listView = (ObservableListView) view.findViewById(R.id.scroll);
        if (listView == null) {
            return;
        }
        View firstVisibleChild = listView.getChildAt(0);
        if (firstVisibleChild != null) {
            int offset = scrollY;
            int position = 0;
            listView.setSelectionFromTop(position, -offset);
        }

    }

    @Override
    protected void updateFlexibleSpace(int scrollY, View view) {

        // Also pass this event to parent Activity
        BasketDetailsActivity parentActivity =
                (BasketDetailsActivity) getActivity();
        if (parentActivity != null) {
                parentActivity.onScrollChanged(scrollY, (ObservableListView) view.findViewById(R.id.scroll));
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                initData();
                BasketDetailsActivity basketDetailsActivity= (BasketDetailsActivity) getActivity();
                basketDetailsActivity.refreshData();
            }
        }, 1000);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("BasketOddsFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("BasketOddsFragment");
    }
}
