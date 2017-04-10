package com.hhly.mlottery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.scheduleBean.ScheduleDate;
import com.hhly.mlottery.util.DateUtil;

import java.util.List;

public class ScheduleDateAdapter extends BaseAdapter {

    private List<ScheduleDate> dList;

    private Context context;

    private int currentDatePosition = 0;

    public ScheduleDateAdapter(List<ScheduleDate> dList, Context context, int position) {
        super();
        this.dList = dList;
        this.context = context;
        this.currentDatePosition = position;
    }

    @Override
    public int getCount() {
        return dList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(context);
        convertView = _LayoutInflater.inflate(R.layout.my_spinner_item, null);
        if (convertView != null) {
            ((TextView) convertView.findViewById(R.id.tv_riqi)).setText(DateUtil.convertDateToNation(dList.get(position).getDate()));
            ((TextView) convertView.findViewById(R.id.tv_xingqi)).setText(dList.get(position).getWeek());
            if (position == currentDatePosition) {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.selected_bg));
            }
        }
        return convertView;
    }

}
