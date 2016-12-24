package com.hhly.mlottery.adapter.chartBallAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;

import java.util.ArrayList;

/**
 * desc:
 * Created by 107_tangrr on 2016/12/9 0009.
 */

public class LocalGridAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Integer> mData;
    ArrayList<String> mDataName;
    private ViewHolder mViewHolder;// ViewHolder

    public LocalGridAdapter(Context context, ArrayList<Integer> list,ArrayList<String> listname){
        this.mContext = context;
        this.mData = list;
        this.mDataName = listname;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.chart_ball_local_page_item_icon, null);
            mViewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon_home);
            mViewHolder.tv_icon_name = (TextView) convertView.findViewById(R.id.tv_icon_name);
            convertView.setTag(mViewHolder);
        }
        mViewHolder = (ViewHolder) convertView.getTag();

        mViewHolder.iv_icon.setBackgroundResource(mData.get(position));
        mViewHolder.tv_icon_name.setText(mDataName.get(position));

        return convertView;
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        ImageView iv_icon;
        TextView tv_icon_name;
    }
}
