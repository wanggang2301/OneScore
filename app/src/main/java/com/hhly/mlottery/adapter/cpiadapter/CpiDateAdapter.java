package com.hhly.mlottery.adapter.cpiadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhly.mlottery.R;

import java.util.List;
import java.util.Map;

/**
 * Created by 103TJL on 2016/4/11.
 * 选择日期的适配器
 */
public class CpiDateAdapter extends BaseAdapter {

    private List<Map<String, String>> cpiDateList;

    private Context context;
    private int defItem;//当前的item的position(用于点击item设置item背景颜色)

    public CpiDateAdapter(Context context, List<Map<String, String>> cpiDateList) {
        super();
        this.context = context;
        this.cpiDateList = cpiDateList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return cpiDateList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return cpiDateList.get(position);
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
        convertView = _LayoutInflater.inflate(R.layout.item_dialog_layout, null);
        if (convertView != null) {
            ((TextView) convertView.findViewById(R.id.dialog_txt_cancel_list)).setText(cpiDateList.get(position).get("date"));
            ((TextView) convertView.findViewById(R.id.dialog_txt_cancel_list)).setTextColor(context.getResources().getColor(R.color.msg));
        }
        if (defItem == position) {//如果点击listview的当前的position相等
            //设置背景颜色
            convertView.setBackgroundResource(R.color.timeLineColor);
        } else {
            convertView.setBackgroundResource(R.color.transparent);
        }
        return convertView;
    }
}
