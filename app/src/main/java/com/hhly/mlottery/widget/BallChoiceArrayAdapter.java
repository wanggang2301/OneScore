package com.hhly.mlottery.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hhly.mlottery.R;

/**
 * @author: Wangg
 * @Name：BallChoiceArrayAdapter
 * @Description:
 * @Created on:2017/2/15  15:39.
 */

public class BallChoiceArrayAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private String[] mStringArray;

    private int index = 0;

    public BallChoiceArrayAdapter(Context context, String[] stringArray, int position) {
        super(context, R.layout.item_ball_choice, stringArray);
        mContext = context;
        mStringArray = stringArray;
        index = position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        //修改Spinner展开后的字体颜色
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_ball_choice, parent, false);
        }

        //此处text1是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(R.id.tv);
        tv.setText(mStringArray[position]);
        if (position == index) {
            tv.setTextColor(mContext.getResources().getColor(R.color.more_record));
        } else {
            tv.setTextColor(mContext.getResources().getColor(R.color.basket_parting_line));
        }


        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 修改Spinner选择后结果的字体颜色
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_ball_choice, parent, false);
        }

        //此处text1是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(R.id.tv);
        tv.setText(mStringArray[position]);
        if (position == index) {
            tv.setTextColor(mContext.getResources().getColor(R.color.more_record));
        } else {
            tv.setTextColor(mContext.getResources().getColor(R.color.basket_parting_line));
        }
        return convertView;
    }
}
