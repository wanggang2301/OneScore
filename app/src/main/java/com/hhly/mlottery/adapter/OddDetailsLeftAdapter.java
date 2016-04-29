package com.hhly.mlottery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.oddsbean.OddsDataInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by 103TJL on 2016/3/10.
 * 详情左边listview数据adapter
 */
public class OddDetailsLeftAdapter extends BaseAdapter {

    private List<Map<String, String>> oddListLeft;

    private Context context;
    private int defItem;//当前的item的position(用于点击item设置item背景颜色)

    public OddDetailsLeftAdapter(Context context, List<Map<String, String>> oddListLeft) {
        super();
        this.context = context;
        this.oddListLeft = oddListLeft;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return oddListLeft.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return oddListLeft.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    /**根据选中的position更改item背景颜色*/
    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater _LayoutInflater = LayoutInflater.from(context);
        convertView = _LayoutInflater.inflate(R.layout.item_odds_left, null);
        if (convertView != null) {
            ((TextView) convertView.findViewById(R.id.odds_left_txt)).setText(oddListLeft.get(position).get("name"));
        }
        if (defItem == position) {//如果点击listview的当前的position相等
            //设置背景颜色
            convertView.setBackgroundResource(R.color.yellow);
            ((TextView) convertView.findViewById(R.id.odds_left_txt)).setTextColor(context.getResources().getColor(R.color.msg));
        } else {
            convertView.setBackgroundResource(R.color.transparent);
        }
        return convertView;
    }
}
