package com.hhly.mlottery.adapter.football.teaminfoadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hhly.mlottery.R;

import java.util.List;

/**
 * @author: tangrr107
 * @Name：球队信息联赛名选择适配器
 * @Description:
 * @Created
 */

public class FootTeamArrayAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private List<String> mStringArray;

    private int index = 0;

    public FootTeamArrayAdapter(Context context, List<String> stringArray, int position) {
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
        tv.setText(mStringArray.get(position));
        if (position == index) {
            tv.setTextColor(mContext.getResources().getColor(R.color.radio_colors));
            tv.setBackgroundResource(R.color.snooker_rank_txt_top3);
        } else {
            tv.setTextColor(mContext.getResources().getColor(R.color.basket_parting_line));
            tv.setBackgroundResource(R.color.radio_colors);
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
        tv.setText(mStringArray.get(position));
        if (position == index) {
            tv.setTextColor(mContext.getResources().getColor(R.color.radio_colors));
            tv.setBackgroundResource(R.color.snooker_rank_txt_top3);
        } else {
            tv.setTextColor(mContext.getResources().getColor(R.color.basket_parting_line));
            tv.setBackgroundResource(R.color.radio_colors);
        }
        return convertView;
    }
}
