package com.hhly.mlottery.frame.footballframe;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.LeagueStatisticsTodayRecyclerViewAdapter;
import com.hhly.mlottery.bean.LeagueStatisticsTodayBean;
import com.hhly.mlottery.bean.LeagueStatisticsTodayChildBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.LeagueStatisticsTodayFlatBigToSmallComparator;
import com.hhly.mlottery.util.LeagueStatisticsTodayFlatSmallToBigComparator;
import com.hhly.mlottery.util.LeagueStatisticsTodayLossBigToSmallComparator;
import com.hhly.mlottery.util.LeagueStatisticsTodayLossSmallToBigComparator;
import com.hhly.mlottery.util.LeagueStatisticsTodayWinBigToSmallComparator;
import com.hhly.mlottery.util.LeagueStatisticsTodayWinSmallToBigComparator;
import com.hhly.mlottery.util.NoDoubleClickUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Wangg
 * @Name：LeagueStatisticsTodayFragment
 * @Description: 今日联赛统计Fragment
 * @Created on:2016/9/1  15:28.
 */
public class LeagueStatisticsTodayFragment extends Fragment implements View.OnClickListener, ExactSwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "LeagueStatisticsTodayFragment";
    private static final Integer TAB0 = 0; //胜平负
    private static final Integer TAB1 = 1; //亚盘
    private static final Integer TAB2 = 2; //大小盘
    private static final Integer TAB3 = 3; //半场亚盘
    private static final Integer TAB4 = 4; //半场大小

    private static final int DATA_STATUS_LOADING = 1;
    private static final int DATA_STATUS_NODATA = 2;
    private static final int DATA_STATUS_SUCCESS = 3;
    private static final int DATA_STATUS_ERROR = -1;

    private static final String TYPE = "type";
    private Context mContext;
    private View mView;

    private LeagueStatisticsTodayRecyclerViewAdapter leagueStatisticsTodayRecyclerViewAdapter;

    private LinearLayout ll_league_win;
    private LinearLayout ll_league_flat;
    private LinearLayout ll_league_loss;
    private LinearLayout ll_riqi1;
    private LinearLayout ll_riqi2;

    private TextView tv_date1;
    private TextView tv_date2;
    private TextView tv_week1;
    private TextView tv_week2;

    private RecyclerView recyclerView;

    private int handicap;

    private TextView tv_rank;
    private TextView tv_race;
    private TextView tv_finish;
    private TextView tv_win;
    private TextView tv_flat;
    private TextView tv_loss;
    private ImageView iv_win;
    private ImageView iv_flat;
    private ImageView iv_loss;

    private TextView tv_tip;
    private TextView network_exception_reload_btn;

    private boolean isSortBigToSmall = false;

    private boolean isSortTab1 = false;
    private boolean isSortTab2 = true;
    private boolean isSortTab3 = true;

    private FrameLayout fl_loading;
    private FrameLayout fl_nodata;
    private LinearLayout ll_showdata;
    private LinearLayout network_exception_layout;
    private ExactSwipeRefreshLayout mExactSwipeRefreshLayout;

    private List<LeagueStatisticsTodayChildBean> mLeagueStatisticsTodayChildBeans;
    private String startDate = "";
    private String endDate = "";
    private String maxDate = "";

    private boolean isPrepared = false;
    private boolean isVisible = false;

    public LeagueStatisticsTodayFragment() {
    }

    public static LeagueStatisticsTodayFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        LeagueStatisticsTodayFragment leagueStatisticsTodayFragment = new LeagueStatisticsTodayFragment();
        leagueStatisticsTodayFragment.setArguments(bundle);
        return leagueStatisticsTodayFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_league_statistics_today, container, false);

        if (getArguments() != null) {
            handicap = getArguments().getInt(TYPE);
        }
        mContext = getActivity();
        initView();
        isPrepared = true;

        loadData();
        return mView;
    }


    private void initView() {
        fl_loading = (FrameLayout) mView.findViewById(R.id.fl_loading);
        fl_nodata = (FrameLayout) mView.findViewById(R.id.fl_nodata);
        ll_showdata = (LinearLayout) mView.findViewById(R.id.ll_showData);
        network_exception_layout = (LinearLayout) mView.findViewById(R.id.network_exception_layout);

        network_exception_reload_btn = (TextView) mView.findViewById(R.id.network_exception_reload_btn);
        mExactSwipeRefreshLayout = (ExactSwipeRefreshLayout) mView.findViewById(R.id.league_swiperefreshlayout);
        mExactSwipeRefreshLayout.setOnRefreshListener(this);
        mExactSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);

        mExactSwipeRefreshLayout.setEnabled(false);
        mExactSwipeRefreshLayout.setRefreshing(false);

        tv_date1 = (TextView) mView.findViewById(R.id.tv_date1);
        tv_date2 = (TextView) mView.findViewById(R.id.tv_date2);
        tv_week1 = (TextView) mView.findViewById(R.id.tv_week1);
        tv_week2 = (TextView) mView.findViewById(R.id.tv_week2);

        tv_rank = (TextView) mView.findViewById(R.id.tv_rank);
        tv_race = (TextView) mView.findViewById(R.id.tv_race);
        tv_finish = (TextView) mView.findViewById(R.id.tv_finish);
        tv_win = (TextView) mView.findViewById(R.id.tv_win);
        tv_flat = (TextView) mView.findViewById(R.id.tv_flat);
        tv_loss = (TextView) mView.findViewById(R.id.tv_loss);
        tv_tip = (TextView) mView.findViewById(R.id.league_statistics_today_tip);

        iv_win = (ImageView) mView.findViewById(R.id.iv_win);
        iv_flat = (ImageView) mView.findViewById(R.id.iv_flat);
        iv_loss = (ImageView) mView.findViewById(R.id.iv_loss);

        ll_league_win = (LinearLayout) mView.findViewById(R.id.ll_league_win);
        ll_league_flat = (LinearLayout) mView.findViewById(R.id.ll_league_flat);
        ll_league_loss = (LinearLayout) mView.findViewById(R.id.ll_league_loss);
        ll_riqi1 = (LinearLayout) mView.findViewById(R.id.ll_riqi1);
        ll_riqi2 = (LinearLayout) mView.findViewById(R.id.ll_riqi2);

        tv_rank.setText(mContext.getString(R.string.league_statistics_today_rank));
        tv_race.setText(mContext.getString(R.string.league_statistics_today_race));
        tv_finish.setText(mContext.getString(R.string.league_statistics_today_finish));


        if (handicap == TAB0) {
            tv_win.setText(mContext.getString(R.string.league_statistics_today_victor));
            tv_flat.setText(mContext.getString(R.string.league_statistics_today_flat));
            tv_loss.setText(mContext.getString(R.string.league_statistics_today_loss));

            String tip = "<font color='#666666'>" + mContext.getString(R.string.league_statistics_today_tip1) + "<b></font></b>"
                    + "<font color='#ff0000'>" + mContext.getString(R.string.league_statistics_today_victor) + "、"
                    + mContext.getString(R.string.league_statistics_today_flat) + "、" + mContext.getString(R.string.league_statistics_today_loss)
                    + "<b></font></b>" + "<font color='#666666'>" + mContext.getString(R.string.league_statistics_today_tip3) + "<b></font></b>";
            tv_tip.setText(Html.fromHtml(tip));


        } else if (handicap == TAB1 || handicap == TAB3) {
            tv_win.setText(mContext.getString(R.string.league_statistics_today_win));
            tv_flat.setText(mContext.getString(R.string.league_statistics_today_failed));
            tv_loss.setText(mContext.getString(R.string.league_statistics_today_go));

            String tip = "<font color='#666666'>" + mContext.getString(R.string.league_statistics_today_tip1) + "<b></font></b>"
                    + "<font color='#ff0000'>" + mContext.getString(R.string.league_statistics_today_win) + "、"
                    + mContext.getString(R.string.league_statistics_today_failed) + "、" + mContext.getString(R.string.league_statistics_today_go)
                    + "<b></font></b>" + "<font color='#666666'>" + mContext.getString(R.string.league_statistics_today_tip3) + "<b></font></b>";
            tv_tip.setText(Html.fromHtml(tip));

        } else if (handicap == TAB2 || handicap == TAB4) {
            tv_win.setText(mContext.getString(R.string.league_statistics_today_big));
            tv_flat.setText(mContext.getString(R.string.league_statistics_today_small));
            tv_loss.setText(mContext.getString(R.string.league_statistics_today_go));

            String tip = "<font color='#666666'>" + mContext.getString(R.string.league_statistics_today_tip1) + "<b></font></b>"
                    + "<font color='#ff0000'>" + mContext.getString(R.string.league_statistics_today_big) + "、"
                    + mContext.getString(R.string.league_statistics_today_small) + "、" + mContext.getString(R.string.league_statistics_today_go)
                    + "<b></font></b>" + "<font color='#666666'>" + mContext.getString(R.string.league_statistics_today_tip3) + "<b></font></b>";
            tv_tip.setText(Html.fromHtml(tip));
        }

        network_exception_reload_btn.setOnClickListener(this);
        ll_league_win.setOnClickListener(this);
        ll_league_flat.setOnClickListener(this);
        ll_league_loss.setOnClickListener(this);
        ll_riqi1.setOnClickListener(this);
        ll_riqi2.setOnClickListener(this);

        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DATA_STATUS_LOADING:
                    fl_loading.setVisibility(View.VISIBLE);
                    fl_nodata.setVisibility(View.GONE);
                    ll_showdata.setVisibility(View.GONE);
                    network_exception_layout.setVisibility(View.GONE);
                    break;
                case DATA_STATUS_NODATA:
                    fl_loading.setVisibility(View.GONE);
                    fl_nodata.setVisibility(View.VISIBLE);
                    ll_showdata.setVisibility(View.GONE);
                    network_exception_layout.setVisibility(View.GONE);
                    break;

                case DATA_STATUS_SUCCESS:
                    fl_loading.setVisibility(View.GONE);
                    fl_nodata.setVisibility(View.GONE);
                    ll_showdata.setVisibility(View.VISIBLE);
                    network_exception_layout.setVisibility(View.GONE);
                    mExactSwipeRefreshLayout.setRefreshing(false);
                    mExactSwipeRefreshLayout.setEnabled(true);
                    initViewData();
                    break;

                case DATA_STATUS_ERROR:
                    fl_loading.setVisibility(View.GONE);
                    fl_nodata.setVisibility(View.GONE);
                    ll_showdata.setVisibility(View.GONE);
                    network_exception_layout.setVisibility(View.VISIBLE);
                    mExactSwipeRefreshLayout.setRefreshing(false);
                    break;

            }
        }
    };


    private void loadData() {
       /* if (!isPrepared || !isVisible) {
            return;
        }*/

        mHandler.sendEmptyMessage(DATA_STATUS_LOADING);
        new Handler().postDelayed(mLoadingDataThread, 0);
    }


    private Runnable mLoadingDataThread = new Runnable() {
        @Override
        public void run() {
            lazyLoad();
        }
    };

    private void resetStatus() {

        ll_league_win.setBackgroundResource(R.drawable.tv_shape);
        tv_win.setTextColor(mContext.getResources().getColor(R.color.white));
        iv_win.setBackgroundResource(R.mipmap.league_down);
        ll_league_flat.setBackgroundResource(R.drawable.tv_shape2);
        tv_flat.setTextColor(mContext.getResources().getColor(R.color.mdy_666));
        iv_flat.setBackgroundResource(R.mipmap.league_defalt);
        ll_league_loss.setBackgroundResource(R.drawable.tv_shape2);
        tv_loss.setTextColor(mContext.getResources().getColor(R.color.mdy_666));
        iv_loss.setBackgroundResource(R.mipmap.league_defalt);

        isSortBigToSmall = false;
        isSortTab1 = false;
        isSortTab2 = true;
        isSortTab3 = true;

    }

    private void lazyLoad() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("handicap", handicap + "");

        String url = BaseURLs.URL_LEAGUESTATISTICSTODAY;

        // String url = "http://192.168.10.242:8181/mlottery/core/toDayMatchStatistics.findTodayMatchStatistics.do";

        VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<LeagueStatisticsTodayBean>() {
            @Override
            public void onResponse(LeagueStatisticsTodayBean json) {
                if (!"200".equals(json.getResult())) {
                    mHandler.sendEmptyMessage(DATA_STATUS_ERROR);
                    return;
                }

                if ("".equals(startDate) && "".equals(endDate)) {
                    maxDate = json.getEndDate();
                }
                startDate = json.getStartDate();
                endDate = json.getEndDate();
                mLeagueStatisticsTodayChildBeans = json.getStatistics();
                mHandler.sendEmptyMessage(DATA_STATUS_SUCCESS);

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(DATA_STATUS_ERROR);
            }
        }, LeagueStatisticsTodayBean.class);
    }

    private void initViewData() {
        resetStatus();

        setData(startDate, tv_date1, tv_week1);
        setData(endDate, tv_date2, tv_week2);
        leagueStatisticsTodayRecyclerViewAdapter = new LeagueStatisticsTodayRecyclerViewAdapter(mContext, mLeagueStatisticsTodayChildBeans,handicap);
        recyclerView.setAdapter(leagueStatisticsTodayRecyclerViewAdapter);
    }

    private void setData(String date, TextView tv_date, TextView tv_week) {
        tv_date.setText(DateUtil.format(DateUtil.parseDate(date, "yyyy-MM-dd"), "yyyy-MM-dd"));
        tv_week.setText(DateUtil.getWeekOfXinQi(DateUtil.parseDate(date, "yyyy-MM-dd")));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_riqi1:
                if (!NoDoubleClickUtils.isDoubleClick()) {
                    showDialog(0, tv_date1.getText().toString(), tv_date1, tv_week1);
                }
                break;
            case R.id.ll_riqi2:
                if (!NoDoubleClickUtils.isDoubleClick()) {
                    showDialog(1, tv_date2.getText().toString(), tv_date2, tv_week2);
                }
                break;
            case R.id.ll_league_win:
                ll_league_win.setBackgroundResource(R.drawable.tv_shape);
                tv_win.setTextColor(mContext.getResources().getColor(R.color.white));
                if (isSortTab1) {
                    isSortBigToSmall = true;
                    isSortTab1 = false;
                    isSortTab2 = true;
                    isSortTab3 = true;
                }

                if (isSortBigToSmall) {
                    iv_win.setBackgroundResource(R.mipmap.league_down);
                    isSortBigToSmall = false;
                    Collections.sort(mLeagueStatisticsTodayChildBeans, new LeagueStatisticsTodayWinBigToSmallComparator());
                } else {
                    iv_win.setBackgroundResource(R.mipmap.league_up);
                    isSortBigToSmall = true;
                    Collections.sort(mLeagueStatisticsTodayChildBeans, new LeagueStatisticsTodayWinSmallToBigComparator());
                }

                ll_league_flat.setBackgroundResource(R.drawable.tv_shape2);
                tv_flat.setTextColor(mContext.getResources().getColor(R.color.mdy_666));
                iv_flat.setBackgroundResource(R.mipmap.league_defalt);
                ll_league_loss.setBackgroundResource(R.drawable.tv_shape2);
                tv_loss.setTextColor(mContext.getResources().getColor(R.color.mdy_666));
                iv_loss.setBackgroundResource(R.mipmap.league_defalt);

                leagueStatisticsTodayRecyclerViewAdapter.notifyDataSetChanged();
                break;

            case R.id.ll_league_flat:

                ll_league_win.setBackgroundResource(R.drawable.tv_shape2);
                tv_win.setTextColor(mContext.getResources().getColor(R.color.mdy_666));
                iv_win.setBackgroundResource(R.mipmap.league_defalt);
                ll_league_flat.setBackgroundResource(R.drawable.tv_shape);
                tv_flat.setTextColor(mContext.getResources().getColor(R.color.white));

                if (isSortTab2) {
                    isSortBigToSmall = true;
                    isSortTab1 = true;
                    isSortTab2 = false;
                    isSortTab3 = true;
                }

                if (isSortBigToSmall) {
                    iv_flat.setBackgroundResource(R.mipmap.league_down);
                    isSortBigToSmall = false;

                    if (handicap==0) {
                        Collections.sort(mLeagueStatisticsTodayChildBeans, new LeagueStatisticsTodayFlatBigToSmallComparator());
                    }else {
                        Collections.sort(mLeagueStatisticsTodayChildBeans, new LeagueStatisticsTodayLossBigToSmallComparator());

                    }

                } else {
                    iv_flat.setBackgroundResource(R.mipmap.league_up);
                    isSortBigToSmall = true;

                    if (handicap==0) {
                        Collections.sort(mLeagueStatisticsTodayChildBeans, new LeagueStatisticsTodayFlatSmallToBigComparator());
                    }else {
                        Collections.sort(mLeagueStatisticsTodayChildBeans, new LeagueStatisticsTodayLossSmallToBigComparator());

                    }
                }

                ll_league_loss.setBackgroundResource(R.drawable.tv_shape2);
                tv_loss.setTextColor(mContext.getResources().getColor(R.color.mdy_666));
                iv_loss.setBackgroundResource(R.mipmap.league_defalt);
                leagueStatisticsTodayRecyclerViewAdapter.notifyDataSetChanged();
                break;

            case R.id.ll_league_loss:

                ll_league_win.setBackgroundResource(R.drawable.tv_shape2);
                tv_win.setTextColor(mContext.getResources().getColor(R.color.mdy_666));
                iv_win.setBackgroundResource(R.mipmap.league_defalt);
                ll_league_flat.setBackgroundResource(R.drawable.tv_shape2);
                tv_flat.setTextColor(mContext.getResources().getColor(R.color.mdy_666));
                iv_flat.setBackgroundResource(R.mipmap.league_defalt);
                ll_league_loss.setBackgroundResource(R.drawable.tv_shape);
                tv_loss.setTextColor(mContext.getResources().getColor(R.color.white));

                if (isSortTab3) {
                    isSortBigToSmall = true;
                    isSortTab1 = true;
                    isSortTab2 = true;
                    isSortTab3 = false;
                }

                if (isSortBigToSmall) {
                    iv_loss.setBackgroundResource(R.mipmap.league_down);
                    isSortBigToSmall = false;
                    if (handicap==0) {
                        Collections.sort(mLeagueStatisticsTodayChildBeans, new LeagueStatisticsTodayLossBigToSmallComparator());
                    }else {
                        Collections.sort(mLeagueStatisticsTodayChildBeans, new LeagueStatisticsTodayFlatBigToSmallComparator());
                    }

                } else {
                    iv_loss.setBackgroundResource(R.mipmap.league_up);
                    isSortBigToSmall = true;
                    if (handicap==0) {
                        Collections.sort(mLeagueStatisticsTodayChildBeans, new LeagueStatisticsTodayLossSmallToBigComparator());
                    }else {
                        Collections.sort(mLeagueStatisticsTodayChildBeans, new LeagueStatisticsTodayFlatSmallToBigComparator());
                    }
                }

                leagueStatisticsTodayRecyclerViewAdapter.notifyDataSetChanged();
                break;

            case R.id.network_exception_reload_btn:
                mHandler.sendEmptyMessage(DATA_STATUS_LOADING);
                new Handler().postDelayed(mLoadingDataThread, 0);
                break;
        }
    }

    @Override
    public void onRefresh() {

        L.d(TAG, "下拉刷新");
        new Handler().postDelayed(mLoadingDataThread, 1000);
    }

    private void showDialog(final int type, String currendate, final TextView tv_date, final TextView tv_week) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
        final AlertDialog alertDialog = builder.create();
        LayoutInflater infla = LayoutInflater.from(mContext);
        View alertDialogView = infla.inflate(R.layout.dialog_calendar, null);
        final MaterialCalendarView widget = (MaterialCalendarView) alertDialogView.findViewById(R.id.calendarView);

        TextView tv_cancel = (TextView) alertDialogView.findViewById(R.id.tv_cancel);
        TextView tv_ok = (TextView) alertDialogView.findViewById(R.id.tv_ok);

        Date date = DateUtil.parseDate(currendate, "yyyy-MM-dd");
        widget.setDateSelected(date, true);
        widget.setCurrentDate(date);

        String[] weekDays = {mContext.getResources().getString(R.string.number_xinqi7), mContext.getResources().getString(R.string.number_xinqi1),
                mContext.getResources().getString(R.string.number_xinqi2), mContext.getResources().getString(R.string.number_xinqi3),
                mContext.getResources().getString(R.string.number_xinqi4), mContext.getResources().getString(R.string.number_xinqi5),
                mContext.getResources().getString(R.string.number_xinqi6)};
        widget.setWeekDayLabels(weekDays);
        widget.setSelectionColor(mContext.getResources().getColor(R.color.blue));

        //日期最大值不超过首次请求日期最大值
        widget.state().edit().setMaximumDate(DateUtil.parseDate(maxDate, "yyyy-MM-dd")).commit();

        //日期最小值=当前选择区间后面值往前推一年
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.parseDate(endDate, "yyyy-MM-dd"));
        calendar.add(Calendar.YEAR, -1);
        widget.state().edit().setMinimumDate(calendar).commit();

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDay date = widget.getSelectedDate();
                tv_date.setText(DateUtil.format(date.getDate(), "yyyy-MM-dd"));
                tv_week.setText(DateUtil.getWeekOfXinQi(date.getDate()));
                if (type == 0) {
                    startDate = DateUtil.format(date.getDate(), "yyyy-MM-dd");
                } else {
                    endDate = DateUtil.format(date.getDate(), "yyyy-MM-dd");
                }

                alertDialog.dismiss();
                mHandler.sendEmptyMessage(DATA_STATUS_LOADING);
                new Handler().postDelayed(mLoadingDataThread, 0);

            }
        });

        alertDialog.show();
        alertDialog.getWindow().setContentView(alertDialogView);
        alertDialog.setCanceledOnTouchOutside(true);
    }

    //fragment每次请求当前数据，目前暂时没有使用
  /*  @Override
    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mHandler.sendEmptyMessage(DATA_STATUS_LOADING);

        if (getUserVisibleHint()) {
            isVisible = true;

            loadData();
        } else {
            isVisible = false;
        }
    }*/
}
