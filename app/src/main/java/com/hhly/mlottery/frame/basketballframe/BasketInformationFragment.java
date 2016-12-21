package com.hhly.mlottery.frame.basketballframe;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.basketball.BasketInfoGridAdapter;
import com.hhly.mlottery.adapter.basketball.ExpandableGridAdapter;
import com.hhly.mlottery.bean.basket.infomation.LeagueAllBean;
import com.hhly.mlottery.bean.basket.infomation.LeagueBean;
import com.hhly.mlottery.bean.basket.infomation.NationalLeague;
import com.hhly.mlottery.callback.BasketInfomationCallBack;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wang gang
 * @date 2016/7/14 17:54
 * @des 篮球资料库列表项Fragment
 */
public class BasketInformationFragment extends Fragment implements ExactSwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "BasketInfomationFragment";
    private static final String TYPE_PARM = "TYPE_PARM";
    private static final String HOT_MATCH = "hot";
    private static final String INTEL_MATCH = "intl";
    private static final int DATA_STATUS_LOADING = 1;
    private static final int DATA_STATUS_NODATA_INTER = 2;
    private static final int DATA_STATUS_NODATA_COUNRTY = 3;
    private static final int DATA_STATUS_SUCCESS_INTER = 4;
    private static final int DATA_STATUS_SUCCESS_COUNTRY = 5;
    private static final int DATA_STATUS_ERROR = -1;
    private static final int NUM0 = 0;
    private static final int NUM1 = 1;
    private static final int NUM2 = 2;
    private static final int NUM3 = 3;

    private static final int ROWNUM = 4;


    private BasketInfomationCallBack basketInfomationCallBack;
    private ExactSwipeRefreshLayout mExactSwipeRefreshLayout;
    private BasketInfoGridAdapter mBasketInfoGridAdapterInter;
    private ExpandableGridAdapter mExpandableGridAdapter;
    private ExpandableListView expandableGridView;
    private RadioGroup radioGroup;
    private GridView gridviewInter;

    private LinearLayout ll_loading;
    private LinearLayout ll_net_error;
    private LinearLayout ll_nodata_country;
    private LinearLayout ll_nodata_inter;

    private FrameLayout fl_inter;
    private FrameLayout fl_country;

    private TextView network_exception_reload_btn;

    private int sign = -1;// 控制列表的展开
    private int childsign = -1;
    private View mView;
    private String mType;

    private List<List<NationalLeague>> interLeagues;
    private List<LeagueBean> hotLeagues;

    private Context mContext;

    public static BasketInformationFragment newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(TYPE_PARM, type);
        BasketInformationFragment basketInformationFragment = new BasketInformationFragment();
        basketInformationFragment.setArguments(bundle);
        return basketInformationFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE_PARM);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_basket_infomation, container, false);

        mContext = getActivity();

        initView();
        return mView;
    }

    private void initView() {
        radioGroup = (RadioGroup) mView.findViewById(R.id.radio_group);
        gridviewInter = (GridView) mView.findViewById(R.id.gridview_inter);
        expandableGridView = (ExpandableListView) mView.findViewById(R.id.listview);
        mExactSwipeRefreshLayout = (ExactSwipeRefreshLayout) mView.findViewById(R.id.info_swiperefreshlayout);
        mExactSwipeRefreshLayout.setOnRefreshListener(this);
        mExactSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mExactSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), StaticValues.REFRASH_OFFSET_END));

        ll_loading = (LinearLayout) mView.findViewById(R.id.info_loading_ll);
        ll_net_error = (LinearLayout) mView.findViewById(R.id.network_exception_layout);
        ll_nodata_inter = (LinearLayout) mView.findViewById(R.id.info_nodata_inter);
        ll_nodata_country = (LinearLayout) mView.findViewById(R.id.info_nodata_country);

        fl_inter = (FrameLayout) mView.findViewById(R.id.fl_inter);
        fl_country = (FrameLayout) mView.findViewById(R.id.fl_country);

        network_exception_reload_btn = (TextView) mView.findViewById(R.id.network_exception_reload_btn);

        mExactSwipeRefreshLayout.setEnabled(false);

        gridviewInter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        if (!isTop(gridviewInter)) {
                            mExactSwipeRefreshLayout.setEnabled(false);
                        } else {
                            mExactSwipeRefreshLayout.setEnabled(true);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:

                        break;
                }
                return false;
            }
        });


        expandableGridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        //只有listview滑到顶部才可以下拉刷新
                        if (!isTop(expandableGridView)) {
                            mExactSwipeRefreshLayout.setEnabled(false);
                        } else {
                            mExactSwipeRefreshLayout.setEnabled(true);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:


                        break;
                }
                return false;
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.info_inter_match:
                        fl_inter.setVisibility(View.VISIBLE);
                        fl_country.setVisibility(View.GONE);
                        break;
                    case R.id.info_country_match:
                        fl_inter.setVisibility(View.GONE);
                        fl_country.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });

        if (HOT_MATCH.equals(mType) || INTEL_MATCH.equals(mType)) {
            radioGroup.setVisibility(View.GONE);
        } else {
            radioGroup.setVisibility(View.VISIBLE);
        }

        network_exception_reload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(DATA_STATUS_LOADING);
                new Handler().postDelayed(mLoadingDataThread, 0);
            }
        });

        mHandler.sendEmptyMessage(DATA_STATUS_LOADING);
        new Handler().postDelayed(mLoadingDataThread, 0);

    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case DATA_STATUS_LOADING:
                    ll_loading.setVisibility(View.VISIBLE);
                    radioGroup.setVisibility(View.GONE);
                    mExactSwipeRefreshLayout.setVisibility(View.GONE);
                    ll_net_error.setVisibility(View.GONE);
                    mExactSwipeRefreshLayout.setRefreshing(true);
                    break;


                case DATA_STATUS_NODATA_INTER:
                    ll_loading.setVisibility(View.GONE);
                    if (!mType.equals(HOT_MATCH) && !mType.equals(INTEL_MATCH)) {
                        radioGroup.setVisibility(View.VISIBLE);
                    }

                    mExactSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    gridviewInter.setVisibility(View.GONE);

                    ll_net_error.setVisibility(View.GONE);
                    ll_nodata_inter.setVisibility(View.VISIBLE);

                    mExactSwipeRefreshLayout.setRefreshing(false);

                    break;

                case DATA_STATUS_NODATA_COUNRTY:
                    if (!mType.equals(HOT_MATCH) && !mType.equals(INTEL_MATCH)) {
                        radioGroup.setVisibility(View.VISIBLE);
                    }

                    mExactSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    expandableGridView.setVisibility(View.GONE);

                    ll_net_error.setVisibility(View.GONE);
                    ll_nodata_country.setVisibility(View.VISIBLE);

                    mExactSwipeRefreshLayout.setRefreshing(false);
                    break;

                case DATA_STATUS_SUCCESS_INTER:
                    ll_loading.setVisibility(View.GONE);
                    if (!mType.equals(HOT_MATCH) && !mType.equals(INTEL_MATCH)) {
                        radioGroup.setVisibility(View.VISIBLE);
                    }

                    mExactSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    gridviewInter.setVisibility(View.VISIBLE);
                    ll_net_error.setVisibility(View.GONE);
                    ll_nodata_inter.setVisibility(View.GONE);

                    mExactSwipeRefreshLayout.setRefreshing(false);
                    break;


                case DATA_STATUS_SUCCESS_COUNTRY:
                    ll_loading.setVisibility(View.GONE);
                    if (!mType.equals(HOT_MATCH) && !mType.equals(INTEL_MATCH)) {
                        radioGroup.setVisibility(View.VISIBLE);
                    }

                    mExactSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    expandableGridView.setVisibility(View.VISIBLE);

                    ll_net_error.setVisibility(View.GONE);
                    ll_nodata_country.setVisibility(View.GONE);

                    mExactSwipeRefreshLayout.setRefreshing(false);
                    break;

                case DATA_STATUS_ERROR:
                    ll_loading.setVisibility(View.GONE);
                    radioGroup.setVisibility(View.GONE);
                    mExactSwipeRefreshLayout.setVisibility(View.GONE);
                    ll_net_error.setVisibility(View.VISIBLE);
                    mExactSwipeRefreshLayout.setRefreshing(false);
                    break;

            }
        }
    };

    private Runnable mLoadingDataThread = new Runnable() {
        @Override
        public void run() {
            loadData();
        }
    };

    private void loadData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", mType);
        L.d(TAG, mType);

        String url = BaseURLs.URL_BASKET_INFORMATION;
        // String url = "http://192.168.31.43:8888/mlottery/core/basketballData.findLeagueHierarchy.do";

        VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<LeagueAllBean>() {

            @Override
            public void onResponse(LeagueAllBean json) {

                if (json.getSpecificLeague() != null && json.getSpecificLeague().size() > 0) {
                    hotLeagues = json.getSpecificLeague();

                    int size = hotLeagues.size() % ROWNUM == 0 ? 0 : ROWNUM - hotLeagues.size() % ROWNUM;
                    for (int i = 0; i < size; i++) {
                        hotLeagues.add(new LeagueBean("", "", ""));
                    }
                }

                if (json.getNationalLeague() != null && json.getNationalLeague().size() > 0) {
                    interLeagues = new ArrayList<>();
                    for (int t = 0; t < Math.ceil((double) json.getNationalLeague().size() / ROWNUM); t++) {
                        List<NationalLeague> list = new ArrayList<>();
                        for (int j = 0; j < ROWNUM; j++) {
                            if ((j + ROWNUM * t) < json.getNationalLeague().size()) {
                                list.add(json.getNationalLeague().get(j + ROWNUM * t));
                            }
                        }

                        interLeagues.add(list);
                    }
                }
                initViewData();
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(DATA_STATUS_ERROR);
            }
        }, LeagueAllBean.class);
    }

    private void initViewData() {

        if (mContext == null) {
            return;
        }
        sign = -1;  //刷新重置
        childsign = -1;

        if (mType.equals(HOT_MATCH) || mType.equals(INTEL_MATCH)) {//热门

            if (hotLeagues != null && hotLeagues.size() > 0) {
                mBasketInfoGridAdapterInter = new BasketInfoGridAdapter(mContext, hotLeagues);
                gridviewInter.setAdapter(mBasketInfoGridAdapterInter);
                L.d("123456", "热门或国际有数据");
                mHandler.sendEmptyMessage(DATA_STATUS_SUCCESS_INTER);
            } else {
                L.d("123456", "热门或国际");
                mHandler.sendEmptyMessage(DATA_STATUS_NODATA_INTER);
            }

        } else { //欧洲、美洲、亚洲等

            //洲际赛事
            if (hotLeagues != null && hotLeagues.size() > 0) {
                mBasketInfoGridAdapterInter = new BasketInfoGridAdapter(mContext, hotLeagues);
                gridviewInter.setAdapter(mBasketInfoGridAdapterInter);
                L.d("123456", "洲际有数据");
                mHandler.sendEmptyMessage(DATA_STATUS_SUCCESS_INTER);
            } else {
                L.d("123456", "洲际无数据");
                mHandler.sendEmptyMessage(DATA_STATUS_NODATA_INTER);
            }

            basketInfomationCallBack = new BasketInfomationCallBack() {

                @Override
                public void onClick(View view, int groupPosition, int child) {
                    if (sign == -1 && childsign == -1) {
                        // 展开被选的group
                        expandableGridView.expandGroup(groupPosition);
                        // 设置被选中的group置于顶端
                        expandableGridView.setSelectedGroup(groupPosition);
                        sign = groupPosition;
                        childsign = child;
                        L.d("147258", "第一次展开");

                    } else if (sign == groupPosition && child == childsign) {
                        expandableGridView.collapseGroup(sign);
                        sign = -1;
                        childsign = -1;

                        L.d("147258", "关闭");
                        mExpandableGridAdapter.isInitChildAdapter = false;

                    } else if (sign != groupPosition) {
                        expandableGridView.collapseGroup(sign);
                        //view.

                        // 展开被选的group
                        expandableGridView.expandGroup(groupPosition);
                        // 设置被选中的group置于顶端
                        expandableGridView.setSelectedGroup(groupPosition);
                        sign = groupPosition;
                        childsign = child;
                        L.i("112", "关闭在展开");

                    }
                    childsign = child;
                }
            };

            if (interLeagues != null && interLeagues.size() > 0) {
                mExpandableGridAdapter = new ExpandableGridAdapter(mContext, interLeagues);
                mExpandableGridAdapter.setBasketInfomationCallBack(basketInfomationCallBack);
                expandableGridView.setAdapter(mExpandableGridAdapter);
                L.d("123456", "国家有数据");

                mHandler.sendEmptyMessage(DATA_STATUS_SUCCESS_COUNTRY);
                // mHandler.sendEmptyMessage(DATA_STATUS_NODATA_COUNRTY);

            } else {
                mHandler.sendEmptyMessage(DATA_STATUS_NODATA_COUNRTY);
                L.d("123456", "国家无数据");
            }
        }
    }

    @Override
    public void onRefresh() {
        L.d(TAG, "下拉刷新");
        new Handler().postDelayed(mLoadingDataThread, 1000);
        //mExactSwipeRefrashLayout.setRefreshing(false);  //刷新消失
    }

    private boolean isTop(GridView listView) {
        if (listView.getCount() == 0) {
            return true;
        }
        View firstView = listView.getChildAt(0);
        if (firstView != null) {
            if (listView.getFirstVisiblePosition() == 0 && firstView.getTop() == listView.getListPaddingTop()) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    private boolean isTop(ListView listView) {
        if (listView.getCount() == 0) {
            return true;
        }
        View firstView = listView.getChildAt(0);
        if (firstView != null) {
            if (listView.getFirstVisiblePosition() == 0 && firstView.getTop() == listView.getListPaddingTop()) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }
}
