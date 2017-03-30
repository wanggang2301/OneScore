package com.hhly.mlottery.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.infomation.SnookerPlayerBean;
import com.hhly.mlottery.view.SpecifiedTextsColorTextView;

import java.util.List;

/**
 * @author: Wangg
 * @Name：SnookerInforSerachAdapter
 * @Description:
 * @Created on:2017/2/22  10:26.
 */

public class SnookerInforSerachAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<SnookerPlayerBean.WorldRankingListBean> worldRankingListBeen;
    private SpecifiedTextsColorTextView mTv;
    private Context mContext;
    private String et_keyword;

    public SnookerInforSerachAdapter(Context context, List<SnookerPlayerBean.WorldRankingListBean> worldRankingListBeen, String et_keyword) {
        super();
        this.mContext = context;
        this.worldRankingListBeen = worldRankingListBeen;
        this.et_keyword = et_keyword;
    }

    @Override
    public int getCount() {

        return worldRankingListBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void clearData() {
        worldRankingListBeen.clear();
        notifyDataSetChanged();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        inflater = LayoutInflater.from(mContext);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.basketball_serach_item, null);

        }
        mTv = (SpecifiedTextsColorTextView) convertView.findViewById(R.id.serach_item);
        mTv.setSpecifiedTextsColor(worldRankingListBeen.get(position).getName(), et_keyword, Color.parseColor("#0090FF"));

        return convertView;
    }
}
