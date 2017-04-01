package com.hhly.mlottery.frame.tennisfrag.oddfragment;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhly.mlottery.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 103TJL on 2016/3/10.
 * 详情左边listview数据adapter
 */
public class TennisOddDetailsLeftAdapter extends BaseAdapter {

    int grey;
    int white;
    int blue;
    int black;

    private ArrayList<String> oddListLeft;

    private Context context;
    private int selectPosition;//当前的item的position(用于点击item设置item背景颜色)

    public TennisOddDetailsLeftAdapter(Context context, ArrayList<String> oddListLeft) {
        super();
        this.context = context;
        this.oddListLeft = oddListLeft;
    }

    @Override
    public int getCount() {
        return oddListLeft.size();
    }

    @Override
    public Object getItem(int position) {
        return oddListLeft.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 根据选中的position更改item背景颜色
     */
    public void setSelect(int position) {
        this.selectPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        maybeInitColor();
        LayoutInflater _LayoutInflater = LayoutInflater.from(context);
        convertView = _LayoutInflater.inflate(R.layout.item_odds_left, parent, false);
        TextView textView = (TextView) convertView.findViewById(R.id.odds_left_txt);
        View point = convertView.findViewById(R.id.left_circle);
        View container = convertView.findViewById(R.id.container);
        if (convertView != null) {
            textView.setText(oddListLeft.get(position));
        }

        boolean isSelect = position == selectPosition;

        textView.setTextColor(isSelect ? blue : black);
        point.setVisibility(isSelect ? View.VISIBLE : View.INVISIBLE);
        container.setBackgroundColor(isSelect ? white : grey);

        return convertView;
    }

    private void maybeInitColor() {
        if (grey == 0) {
            grey = ContextCompat.getColor(context, R.color.whitesmoke);
        }
        if (white == 0) {
            white = ContextCompat.getColor(context, R.color.white);
        }
        if (blue == 0) {
            blue = ContextCompat.getColor(context, R.color.colorPrimary);
        }
        if (black == 0) {
            black = ContextCompat.getColor(context, R.color.content_txt_black);
        }
    }
}
