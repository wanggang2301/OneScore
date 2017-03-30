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
 * Created by Administrator on 2016/4/12 0012.
 */
public class InforPopuWindowdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private List<String> list;


    public InforPopuWindowdapter(Context context, List<String> list) {
        super();
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void addData(ArrayList<String> lis) {
        list.clear();
        list.addAll(lis);
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_popupwindow, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tv_list_item);
        tv.setText(list.get(position));
        return convertView;
    }

}
