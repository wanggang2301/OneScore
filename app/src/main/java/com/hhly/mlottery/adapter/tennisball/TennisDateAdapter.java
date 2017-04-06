package com.hhly.mlottery.adapter.tennisball;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.DateUtil;

import java.util.ArrayList;

/**
 * desc:网球列表日期选择
 * Created by 107_tangrr on 2017/2/21 0021.
 */

public class TennisDateAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mData;
    private String mCurrentData;

    public TennisDateAdapter(Context context, ArrayList<String> data, String currentData) {
        super();
        this.mContext = context;
        this.mData = data;
        this.mCurrentData = currentData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        TennisDateAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_dialog_tennis_layout, parent, false);
            viewHolder = new TennisDateAdapter.ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.dialog_txt_cancel_list);
            viewHolder.line = convertView.findViewById(R.id.line);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TennisDateAdapter.ViewHolder) convertView.getTag();
        }

        try {
            viewHolder.mTextView.setText(mData.get(position) + " " + DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(mData.get(position))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mCurrentData.equals(mData.get(position))) {
            // 设置背景颜色
            convertView.setBackgroundResource(R.color.whitesmoke);
        } else {
            convertView.setBackgroundResource(R.color.transparent);
        }

        if(position >= mData.size() - 1){
            viewHolder.line.setVisibility(View.GONE);
        }else{
            viewHolder.line.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    class ViewHolder {
        TextView mTextView;
        View line;
    }
}
