package com.hhly.mlottery.adapter.basketball;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhly.mlottery.R;

import java.util.List;

public class SportsDialogAdapter extends BaseAdapter {

    private List<String> dList;

    private Context context;

    private int currentDatePosition = 0;

    public void updateDatas(int position) {
//        dList = mMatch;
        currentDatePosition = position;
    }

    public SportsDialogAdapter(List<String> dList, Context context, int position) {
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
        convertView = _LayoutInflater.inflate(R.layout.basket_sports_dialog_item, null);
        if (convertView != null) {

            TextView mSports = (TextView)convertView.findViewById(R.id.sports);
//            ((TextView) convertView.findViewById(R.id.sports)).setText(dList.get(position));
            mSports.setText(dList.get(position));

            if (position == currentDatePosition) {
//                convertView.setBackgroundColor(context.getResources().getColor(R.color.selected_bg));
                mSports.setTextColor(context.getResources().getColor(R.color.football_analyze_draw_color));

            }
        }
        return convertView;
    }

}
