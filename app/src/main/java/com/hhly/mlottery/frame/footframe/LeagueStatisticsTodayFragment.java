package com.hhly.mlottery.frame.footframe;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.LeagueStatisticsTodayAdapter;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.widget.NoScrollListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    private TextView tv_date1;
    private TextView tv_date2;

    private int year;
    private int month;
    private int day;

    private int currentSelectedDate;
    private List<Integer> data;

    private NoScrollListView listview;

    private int type;

    private TextView tv_rank;
    private TextView tv_race;
    private TextView tv_finish;
    private TextView tv_win;
    private TextView tv_plat;
    private TextView tv_loss;


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

        tv_rank = (TextView) mView.findViewById(R.id.tv_rank);
        tv_race = (TextView) mView.findViewById(R.id.tv_race);
        tv_finish = (TextView) mView.findViewById(R.id.tv_finish);
        tv_win = (TextView) mView.findViewById(R.id.tv_win);
        tv_plat = (TextView) mView.findViewById(R.id.tv_plat);
        tv_loss = (TextView) mView.findViewById(R.id.tv_loss);

        ll_league_win = (LinearLayout) mView.findViewById(R.id.ll_league_win);
        ll_league_flat = (LinearLayout) mView.findViewById(R.id.ll_league_flat);
        ll_league_loss = (LinearLayout) mView.findViewById(R.id.ll_league_loss);

        tv_rank.setText(mContext.getString(R.string.league_statistics_today_rank));
        tv_race.setText(mContext.getString(R.string.league_statistics_today_race));
        tv_finish.setText(mContext.getString(R.string.league_statistics_today_finish));

        if (type == 0) {
            tv_win.setText(mContext.getString(R.string.league_statistics_today_victor));
            tv_plat.setText(mContext.getString(R.string.league_statistics_today_flat));
            tv_loss.setText(mContext.getString(R.string.league_statistics_today_loss));
        } else if (type == 1) {
            tv_win.setText(mContext.getString(R.string.league_statistics_today_win));
            tv_plat.setText(mContext.getString(R.string.league_statistics_today_failed));
            tv_loss.setText(mContext.getString(R.string.league_statistics_today_go));
        } else if (type == 2) {
            tv_win.setText(mContext.getString(R.string.league_statistics_today_big));
            tv_plat.setText(mContext.getString(R.string.league_statistics_today_small));
            tv_loss.setText(mContext.getString(R.string.league_statistics_today_go));
        }

        tv_date1.setOnClickListener(this);
        tv_date2.setOnClickListener(this);
        ll_league_win.setOnClickListener(this);
        ll_league_flat.setOnClickListener(this);
        ll_league_loss.setOnClickListener(this);

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

            case R.id.tv_date1:

                currentSelectedDate = 0;
                Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
                Date mydate = new Date(); //获取当前日期Date对象
                mycalendar.setTime(mydate);////为Calendar对象设置时间为当前日期

                year = mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
                month = mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
                day = mycalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天

                DatePickerDialog dpd = new DatePickerDialog(mContext, Datelistener, year, month, day);

                dpd.show();//显示DatePickerDialog组件
                break;
            case R.id.tv_date2:
                currentSelectedDate = 1;
                Calendar mycalendar2 = Calendar.getInstance(Locale.CHINA);
                Date mydate2 = new Date(); //获取当前日期Date对象
                mycalendar2.setTime(mydate2);////为Calendar对象设置时间为当前日期

                year = mycalendar2.get(Calendar.YEAR); //获取Calendar对象中的年
                month = mycalendar2.get(Calendar.MONTH);//获取Calendar对象中的月
                day = mycalendar2.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天

                DatePickerDialog dpd2 = new DatePickerDialog(mContext, Datelistener, year, month, day);

                dpd2.show();//显示DatePickerDialog组件
                break;

            case R.id.ll_league_win:

                Collections.reverse(data);
                leagueStatisticsTodayAdapter.notifyDataSetChanged();

                break;

            case R.id.ll_league_flat:
                Collections.reverse(data);
                leagueStatisticsTodayAdapter.notifyDataSetChanged();
                break;

            case R.id.ll_league_loss:
                Collections.reverse(data);
                leagueStatisticsTodayAdapter.notifyDataSetChanged();
                break;
        }
    }

    private DatePickerDialog.OnDateSetListener Datelistener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int myyear, int monthOfYear, int dayOfMonth) {
            year = myyear;
            month = monthOfYear;
            day = dayOfMonth;
            //更新日期
            L.d("223", view.getId() + "");
            updateDate();
        }

        private void updateDate() {
            //在TextView上显示日期
            if (currentSelectedDate == 0) {
                tv_date1.setText(year + "-" + (month + 1) + "-" + day);
            } else if (currentSelectedDate == 1) {
                tv_date2.setText(year + "-" + (month + 1) + "-" + day);

            }
        }
    };


}
