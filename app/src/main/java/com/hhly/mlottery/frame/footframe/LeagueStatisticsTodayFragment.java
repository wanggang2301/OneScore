package com.hhly.mlottery.frame.footframe;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.LeagueStatisticsTodayAdapter;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.NoDoubleClickUtils;
import com.hhly.mlottery.widget.NoScrollListView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeagueStatisticsTodayFragment extends Fragment implements View.OnClickListener {

    private static final String TYPE = "type";
    private Context mContext;
    private View mView;
    private RecyclerView recyclerView;
    private LeagueStatisticsTodayAdapter leagueStatisticsTodayAdapter;
    private LinearLayout ll_league_win;
    private LinearLayout ll_league_flat;
    private LinearLayout ll_league_loss;
    private LinearLayout ll_riqi1;
    private LinearLayout ll_riqi2;

    private TextView tv_date1;
    private TextView tv_date2;
    private TextView tv_week1;
    private TextView tv_week2;


    private int currentSelectedDate;
    private List<Integer> data;

    private NoScrollListView listview;

    private int type;

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

    private boolean isSortBigToSmall = false;

    private boolean isSortTab1 = false;
    private boolean isSortTab2 = true;
    private boolean isSortTab3 = true;

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
            type = getArguments().getInt(TYPE);
        }

        mContext = getActivity();
        initView();

        loadData();

        return mView;
    }


    private void initView() {
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


        if (type == 0) {
            tv_win.setText(mContext.getString(R.string.league_statistics_today_victor));
            tv_flat.setText(mContext.getString(R.string.league_statistics_today_flat));
            tv_loss.setText(mContext.getString(R.string.league_statistics_today_loss));

            String tip = "<font color='#666666'>" + mContext.getString(R.string.league_statistics_today_tip1) + "<b></font></b>"
                    + "<font color='#ff0000'>" + mContext.getString(R.string.league_statistics_today_victor) + "、"
                    + mContext.getString(R.string.league_statistics_today_flat) + "、" + mContext.getString(R.string.league_statistics_today_loss)
                    + "<b></font></b>" + "<font color='#666666'>" + mContext.getString(R.string.league_statistics_today_tip3) + "<b></font></b>";
            tv_tip.setText(Html.fromHtml(tip));


        } else if (type == 1) {
            tv_win.setText(mContext.getString(R.string.league_statistics_today_win));
            tv_flat.setText(mContext.getString(R.string.league_statistics_today_failed));
            tv_loss.setText(mContext.getString(R.string.league_statistics_today_go));

            String tip = "<font color='#666666'>" + mContext.getString(R.string.league_statistics_today_tip1) + "<b></font></b>"
                    + "<font color='#ff0000'>" + mContext.getString(R.string.league_statistics_today_win) + "、"
                    + mContext.getString(R.string.league_statistics_today_failed) + "、" + mContext.getString(R.string.league_statistics_today_go)
                    + "<b></font></b>" + "<font color='#666666'>" + mContext.getString(R.string.league_statistics_today_tip3) + "<b></font></b>";
            tv_tip.setText(Html.fromHtml(tip));

        } else if (type == 2) {
            tv_win.setText(mContext.getString(R.string.league_statistics_today_big));
            tv_flat.setText(mContext.getString(R.string.league_statistics_today_small));
            tv_loss.setText(mContext.getString(R.string.league_statistics_today_go));

            String tip = "<font color='#666666'>" + mContext.getString(R.string.league_statistics_today_tip1) + "<b></font></b>"
                    + "<font color='#ff0000'>" + mContext.getString(R.string.league_statistics_today_big) + "、"
                    + mContext.getString(R.string.league_statistics_today_small) + "、" + mContext.getString(R.string.league_statistics_today_go)
                    + "<b></font></b>" + "<font color='#666666'>" + mContext.getString(R.string.league_statistics_today_tip3) + "<b></font></b>";
            tv_tip.setText(Html.fromHtml(tip));

        }


        ll_league_win.setOnClickListener(this);
        ll_league_flat.setOnClickListener(this);
        ll_league_loss.setOnClickListener(this);
        ll_riqi1.setOnClickListener(this);
        ll_riqi2.setOnClickListener(this);

        listview = (NoScrollListView) mView.findViewById(R.id.listview);
        listview.setFocusable(false);
    }

    private void loadData() {
        data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add(i);
        }

        leagueStatisticsTodayAdapter = new LeagueStatisticsTodayAdapter(mContext, data, R.layout.item_fragment_league_statistics_today);
        listview.setAdapter(leagueStatisticsTodayAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_riqi1:
                if (!NoDoubleClickUtils.isDoubleClick()) {
                    showDialog(tv_date1.getText().toString(), tv_date1, tv_week1);
                }
                break;
            case R.id.ll_riqi2:
                if (!NoDoubleClickUtils.isDoubleClick()) {
                    showDialog(tv_date2.getText().toString(), tv_date2, tv_week2);
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
                } else {
                    iv_win.setBackgroundResource(R.mipmap.league_up);
                    isSortBigToSmall = true;
                }

                ll_league_flat.setBackgroundResource(R.drawable.tv_shape2);
                tv_flat.setTextColor(mContext.getResources().getColor(R.color.mdy_666));
                iv_flat.setBackgroundResource(R.mipmap.league_defalt);

                ll_league_loss.setBackgroundResource(R.drawable.tv_shape2);
                tv_loss.setTextColor(mContext.getResources().getColor(R.color.mdy_666));
                iv_loss.setBackgroundResource(R.mipmap.league_defalt);

                Collections.reverse(data);
                leagueStatisticsTodayAdapter.notifyDataSetChanged();

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
                } else {
                    iv_flat.setBackgroundResource(R.mipmap.league_up);
                    isSortBigToSmall = true;
                }


                ll_league_loss.setBackgroundResource(R.drawable.tv_shape2);
                tv_loss.setTextColor(mContext.getResources().getColor(R.color.mdy_666));
                iv_loss.setBackgroundResource(R.mipmap.league_defalt);


                Collections.reverse(data);
                leagueStatisticsTodayAdapter.notifyDataSetChanged();
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
                } else {
                    iv_loss.setBackgroundResource(R.mipmap.league_up);
                    isSortBigToSmall = true;
                }


                Collections.reverse(data);
                leagueStatisticsTodayAdapter.notifyDataSetChanged();
                break;
        }
    }


    private void showDialog(String currendate, final TextView tv_date, final TextView tv_week) {
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

        String[] dd = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        widget.setWeekDayLabels(dd);


        widget.setSelectionColor(mContext.getResources().getColor(R.color.blue));

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
                alertDialog.dismiss();
                Collections.reverse(data);
                leagueStatisticsTodayAdapter.notifyDataSetChanged();

            }
        });

        alertDialog.show();
        alertDialog.getWindow().setContentView(alertDialogView);
        alertDialog.setCanceledOnTouchOutside(true);
    }

}
