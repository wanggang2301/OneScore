package com.hhly.mlottery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhly.mlottery.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 描    述：赛季选择下拉列表的listview
 * 作    者：mady@13322.com
 * 时    间：2017/6/25
 */
public class BasketSeasonSelectAdapter extends BaseAdapter{

    private List<String> seasons=new ArrayList<>();
    private Context context;

    private int currentDatePosition = 0;

    public BasketSeasonSelectAdapter(List<String> seasons, Context context, int currentDatePosition) {

        this.seasons = seasons;
        this.context = context;
        this.currentDatePosition = currentDatePosition;
    }

    @Override
    public int getCount() {
        return seasons.size();
    }

    @Override
    public Object getItem(int i) {
        return seasons.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(context);
        convertView = _LayoutInflater.inflate(R.layout.item_basket_season, null);
        if (convertView != null) {
            ((TextView) convertView.findViewById(R.id.item_basket_season)).setText(seasons.get(position));
            if (position == currentDatePosition) {
//                convertView.setBackgroundColor(context.getResources().getColor(R.color.recycler_view_shadow));
                convertView.setBackground(context.getResources().getDrawable(R.drawable.four_radius_check));
            }else{
                convertView.setBackground(context.getResources().getDrawable(R.drawable.four_radius));
//                convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        }
        return convertView;
    }
}
