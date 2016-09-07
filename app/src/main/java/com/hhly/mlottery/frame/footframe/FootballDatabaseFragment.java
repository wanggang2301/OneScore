package com.hhly.mlottery.frame.footframe;

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
import com.hhly.mlottery.adapter.football.FootBallExpandableGridAdapter;
import com.hhly.mlottery.adapter.football.FootBallInfoGridAdapter;
import com.hhly.mlottery.bean.footballDetails.database.DataBaseBean;
import com.hhly.mlottery.bean.footballDetails.database.LeagueDataBase;
import com.hhly.mlottery.bean.footballDetails.database.NationBean;
import com.hhly.mlottery.callback.BasketInfomationCallBack;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefrashLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Wangg
 * @Name：FootballDatabaseFragment
 * @Description:足球资料库
 * @Created on:2016/9/1  15:03.
 */
public class FootballDatabaseFragment extends Fragment implements ExactSwipeRefrashLayout.OnRefreshListener {
    private static final String TAG = "FootballDatabaseFragment";
    private static final String TYPE_PARM = "TYPE_PARM";
    private static final int HOT_MATCH = 0;
    private static final int DATA_STATUS_LOADING = 1;
    private static final int DATA_STATUS_NODATA_INTER = 2;
    private static final int DATA_STATUS_NODATA_COUNRTY = 3;
    private static final int DATA_STATUS_SUCCESS_INTER = 4;
    private static final int DATA_STATUS_SUCCESS_COUNTRY = 5;
    private static final int DATA_STATUS_ERROR = -1;

    private static final int ROWNUM = 4;

    private BasketInfomationCallBack basketInfomationCallBack;
    private ExactSwipeRefrashLayout mExactSwipeRefrashLayout;
    private FootBallInfoGridAdapter mFootBallInfoGridAdapterInternatrion;
    private FootBallExpandableGridAdapter mFootBallExpandableGridAdapter;
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
    private int mType;

    private List<List<NationBean>> nation;
    private List<DataBaseBean> internation;

    private Context mContext;

    public static FootballDatabaseFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_PARM, type);
        FootballDatabaseFragment footballDatabaseFragment = new FootballDatabaseFragment();
        footballDatabaseFragment.setArguments(bundle);
        return footballDatabaseFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(TYPE_PARM);
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
        mExactSwipeRefrashLayout = (ExactSwipeRefrashLayout) mView.findViewById(R.id.info_swiperefreshlayout);
        mExactSwipeRefrashLayout.setOnRefreshListener(this);
        mExactSwipeRefrashLayout.setColorSchemeResources(R.color.bg_header);
        mExactSwipeRefrashLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), StaticValues.REFRASH_OFFSET_END));

        ll_loading = (LinearLayout) mView.findViewById(R.id.info_loading_ll);
        ll_net_error = (LinearLayout) mView.findViewById(R.id.network_exception_layout);
        ll_nodata_inter = (LinearLayout) mView.findViewById(R.id.info_nodata_inter);
        ll_nodata_country = (LinearLayout) mView.findViewById(R.id.info_nodata_country);

        fl_inter = (FrameLayout) mView.findViewById(R.id.fl_inter);
        fl_country = (FrameLayout) mView.findViewById(R.id.fl_country);

        network_exception_reload_btn = (TextView) mView.findViewById(R.id.network_exception_reload_btn);

        mExactSwipeRefrashLayout.setEnabled(false);

        gridviewInter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        if (!isTop(gridviewInter)) {
                            mExactSwipeRefrashLayout.setEnabled(false);
                        } else {
                            mExactSwipeRefrashLayout.setEnabled(true);
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
                            mExactSwipeRefrashLayout.setEnabled(false);
                        } else {
                            mExactSwipeRefrashLayout.setEnabled(true);
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

        if (HOT_MATCH == mType) {
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
                    mExactSwipeRefrashLayout.setVisibility(View.GONE);
                    ll_net_error.setVisibility(View.GONE);
                    mExactSwipeRefrashLayout.setRefreshing(true);
                    break;

                case DATA_STATUS_NODATA_INTER:
                    ll_loading.setVisibility(View.GONE);
                    if (!(mType == HOT_MATCH)) {
                        radioGroup.setVisibility(View.VISIBLE);
                    }

                    mExactSwipeRefrashLayout.setVisibility(View.VISIBLE);
                    gridviewInter.setVisibility(View.GONE);

                    ll_net_error.setVisibility(View.GONE);
                    ll_nodata_inter.setVisibility(View.VISIBLE);

                    mExactSwipeRefrashLayout.setRefreshing(false);

                    break;

                case DATA_STATUS_NODATA_COUNRTY:
                    if (!(mType == HOT_MATCH)) {
                        radioGroup.setVisibility(View.VISIBLE);
                    }

                    mExactSwipeRefrashLayout.setVisibility(View.VISIBLE);
                    expandableGridView.setVisibility(View.GONE);

                    ll_net_error.setVisibility(View.GONE);
                    ll_nodata_country.setVisibility(View.VISIBLE);

                    mExactSwipeRefrashLayout.setRefreshing(false);
                    break;

                case DATA_STATUS_SUCCESS_INTER:
                    ll_loading.setVisibility(View.GONE);
                    if (!(mType == HOT_MATCH)) {
                        radioGroup.setVisibility(View.VISIBLE);
                    }

                    mExactSwipeRefrashLayout.setVisibility(View.VISIBLE);
                    gridviewInter.setVisibility(View.VISIBLE);
                    ll_net_error.setVisibility(View.GONE);
                    ll_nodata_inter.setVisibility(View.GONE);

                    mExactSwipeRefrashLayout.setRefreshing(false);
                    break;


                case DATA_STATUS_SUCCESS_COUNTRY:
                    ll_loading.setVisibility(View.GONE);
                    if (!(mType == HOT_MATCH)) {
                        radioGroup.setVisibility(View.VISIBLE);
                    }

                    mExactSwipeRefrashLayout.setVisibility(View.VISIBLE);
                    expandableGridView.setVisibility(View.VISIBLE);

                    ll_net_error.setVisibility(View.GONE);
                    ll_nodata_country.setVisibility(View.GONE);

                    mExactSwipeRefrashLayout.setRefreshing(false);
                    break;

                case DATA_STATUS_ERROR:
                    ll_loading.setVisibility(View.GONE);
                    radioGroup.setVisibility(View.GONE);
                    mExactSwipeRefrashLayout.setVisibility(View.GONE);
                    ll_net_error.setVisibility(View.VISIBLE);
                    mExactSwipeRefrashLayout.setRefreshing(false);
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
        params.put("index", mType + "");
        L.d(TAG, mType + "");

        // String url = BaseURLs.URL_FOOTBALL_DATABASE;
        String url = "http://192.168.31.8:8080/mlottery/core/androidLeagueData.findAndroidDataMenu.do";

        VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<LeagueDataBase>() {

            @Override
            public void onResponse(LeagueDataBase json) {

                if (!"200".equals(json.getCode())) {
                    mHandler.sendEmptyMessage(DATA_STATUS_ERROR);
                    return;
                }

                if (json.getInternation() != null && json.getInternation().size() > 0) {
                    internation = json.getInternation();
                    int size = internation.size() % ROWNUM == 0 ? 0 : ROWNUM - internation.size() % ROWNUM;
                    for (int i = 0; i < size; i++) {
                        internation.add(new DataBaseBean("", "", "", ""));
                    }
                }

                if (json.getNation() != null && json.getNation().size() > 0) {
                    nation = new ArrayList<>();
                    for (int t = 0; t < Math.ceil((double) json.getNation().size() / ROWNUM); t++) {
                        List<NationBean> list = new ArrayList<>();
                        for (int j = 0; j < ROWNUM; j++) {
                            if ((j + ROWNUM * t) < json.getNation().size()) {
                                list.add(json.getNation().get(j + ROWNUM * t));
                            }
                        }

                        nation.add(list);
                    }
                }
                initViewData();
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(DATA_STATUS_ERROR);
            }
        }, LeagueDataBase.class);
    }

    private void initViewData() {

        if (mContext == null) {
            return;
        }
        sign = -1;  //刷新重置
        childsign = -1;

        if (mType == HOT_MATCH) {//热门

            if (internation != null && internation.size() > 0) {
                mFootBallInfoGridAdapterInternatrion = new FootBallInfoGridAdapter(mContext, internation);
                gridviewInter.setAdapter(mFootBallInfoGridAdapterInternatrion);
                L.d("123456", "热门或国际有数据");
                mHandler.sendEmptyMessage(DATA_STATUS_SUCCESS_INTER);
            } else {
                L.d("123456", "热门或国际");
                mHandler.sendEmptyMessage(DATA_STATUS_NODATA_INTER);
            }

        } else { //欧洲、美洲、亚洲等

            //洲际赛事
            if (internation != null && internation.size() > 0) {
                mFootBallInfoGridAdapterInternatrion = new FootBallInfoGridAdapter(mContext, internation);
                gridviewInter.setAdapter(mFootBallInfoGridAdapterInternatrion);
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
                        mFootBallExpandableGridAdapter.isInitChildAdapter = false;

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

            if (nation != null && nation.size() > 0) {
                mFootBallExpandableGridAdapter = new FootBallExpandableGridAdapter(mContext, nation);
                mFootBallExpandableGridAdapter.setBasketInfomationCallBack(basketInfomationCallBack);
                expandableGridView.setAdapter(mFootBallExpandableGridAdapter);
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
