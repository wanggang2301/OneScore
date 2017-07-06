package com.hhly.mlottery.frame.footballframe;

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
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.FootBallInfoGridAdapter;
import com.hhly.mlottery.bean.footballDetails.database.DataBaseBean;
import com.hhly.mlottery.bean.footballDetails.database.LeagueDataBase;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:  足球资料库入口国际（沙滩赛事）
 * 作者:  wangg@13322.com
 * 时间:  2016/9/18 11:16
 */
public class FootballDatabaseInterFragment extends Fragment implements ExactSwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "FootballDatabaseInterFragment";
    private static final String TYPE_PARM = "TYPE_PARM";
    private static final int DATA_STATUS_LOADING = 1;
    private static final int DATA_STATUS_NODATA_INTER = 2;
    private static final int DATA_STATUS_NODATA_COUNRTY = 3;
    private static final int DATA_STATUS_SUCCESS_INTER = 4;
    private static final int DATA_STATUS_SUCCESS_COUNTRY = 5;
    private static final int DATA_STATUS_ERROR = -1;


    private static final int ROWNUM = 4;

    private ExactSwipeRefreshLayout mExactSwipeRefreshLayout;
    private FootBallInfoGridAdapter mFootBallInfoGridAdapterInternatrion;
    private FootBallInfoGridAdapter mFootBallInfoGridAdapterInterShaTan;

    private RadioGroup radioGroup;


    private GridView gridviewInter;
    private GridView gridviewShaTan;

    private LinearLayout ll_loading;
    private LinearLayout ll_net_error;
    private LinearLayout ll_nodata_country;
    private LinearLayout ll_nodata_inter;

    private FrameLayout fl_inter;
    private FrameLayout fl_country;

    private TextView network_exception_reload_btn;

    private View mView;
    private int mType;

    private List<DataBaseBean> internation;
    private List<DataBaseBean> shatannation;

    private Context mContext;

    public static FootballDatabaseInterFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_PARM, type);
        FootballDatabaseInterFragment footballDatabaseInterFragment = new FootballDatabaseInterFragment();
        footballDatabaseInterFragment.setArguments(bundle);
        return footballDatabaseInterFragment;
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
        mView = inflater.inflate(R.layout.fragment_football_inter_infomation, container, false);

        mContext = getActivity();

        initView();
        return mView;
    }

    private void initView() {
        radioGroup = (RadioGroup) mView.findViewById(R.id.radio_group);

        gridviewInter = (GridView) mView.findViewById(R.id.gridview_inter);
        gridviewShaTan = (GridView) mView.findViewById(R.id.gridview_shatan);


        mExactSwipeRefreshLayout = (ExactSwipeRefreshLayout) mView.findViewById(R.id.info_swiperefreshlayout);
        mExactSwipeRefreshLayout.setOnRefreshListener(this);
        mExactSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mExactSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), StaticValues.REFRASH_OFFSET_END));

        ll_loading = (LinearLayout) mView.findViewById(R.id.info_loading_ll);

        ll_net_error = (LinearLayout) mView.findViewById(R.id.network_error_ll);
        ll_nodata_inter = (LinearLayout) mView.findViewById(R.id.no_datas_ll);
        ll_nodata_country = (LinearLayout) mView.findViewById(R.id.info_nodata_country);

        fl_inter = (FrameLayout) mView.findViewById(R.id.fl_inter);
        fl_country = (FrameLayout) mView.findViewById(R.id.fl_country);

        network_exception_reload_btn = (TextView) mView.findViewById(R.id.network_error_btn);

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


        gridviewShaTan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        if (!isTop(gridviewShaTan)) {
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
                    radioGroup.setVisibility(View.VISIBLE);


                    mExactSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    gridviewInter.setVisibility(View.GONE);

                    ll_net_error.setVisibility(View.GONE);
                    ll_nodata_inter.setVisibility(View.VISIBLE);

                    mExactSwipeRefreshLayout.setRefreshing(false);

                    break;

                case DATA_STATUS_NODATA_COUNRTY:

                    radioGroup.setVisibility(View.VISIBLE);


                    mExactSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    gridviewShaTan.setVisibility(View.GONE);

                    ll_net_error.setVisibility(View.GONE);
                    ll_nodata_country.setVisibility(View.VISIBLE);

                    mExactSwipeRefreshLayout.setRefreshing(false);
                    break;

                case DATA_STATUS_SUCCESS_INTER:
                    ll_loading.setVisibility(View.GONE);
                    radioGroup.setVisibility(View.VISIBLE);

                    mExactSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    gridviewInter.setVisibility(View.VISIBLE);
                    ll_net_error.setVisibility(View.GONE);
                    ll_nodata_inter.setVisibility(View.GONE);

                    mExactSwipeRefreshLayout.setRefreshing(false);
                    break;


                case DATA_STATUS_SUCCESS_COUNTRY:
                    ll_loading.setVisibility(View.GONE);
                    radioGroup.setVisibility(View.VISIBLE);

                    mExactSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    gridviewShaTan.setVisibility(View.VISIBLE);

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
        params.put("index", mType + "");
        L.d(TAG, mType + "");

        String url = BaseURLs.URL_FOOTBALL_DATABASE;

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
                    if (json.getNation().get(0).getLeagueMenues() != null && json.getNation().get(0).getLeagueMenues().size() > 0) {
                        shatannation = json.getNation().get(0).getLeagueMenues();
                        int size = shatannation.size() % ROWNUM == 0 ? 0 : ROWNUM - shatannation.size() % ROWNUM;
                        for (int i = 0; i < size; i++) {
                            shatannation.add(new DataBaseBean("", "", "", ""));
                        }
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


        //洲际赛事
        if (internation != null && internation.size() > 0) {
            mFootBallInfoGridAdapterInternatrion = new FootBallInfoGridAdapter(mContext, internation);
            gridviewInter.setAdapter(mFootBallInfoGridAdapterInternatrion);
            mHandler.sendEmptyMessage(DATA_STATUS_SUCCESS_INTER);
        } else {
            mHandler.sendEmptyMessage(DATA_STATUS_NODATA_INTER);
        }


        //沙滩赛事
        if (shatannation != null && shatannation.size() > 0) {
            mFootBallInfoGridAdapterInterShaTan = new FootBallInfoGridAdapter(mContext, shatannation);
            gridviewShaTan.setAdapter(mFootBallInfoGridAdapterInterShaTan);
            mHandler.sendEmptyMessage(DATA_STATUS_SUCCESS_INTER);
        } else {
            mHandler.sendEmptyMessage(DATA_STATUS_NODATA_INTER);
        }


    }

    @Override
    public void onRefresh() {
        L.d(TAG, "下拉刷新");
        new Handler().postDelayed(mLoadingDataThread, 1000);
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

}
