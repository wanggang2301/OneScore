package com.hhly.mlottery.util.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @param <T>
 * @author chenml
 * @ClassName: CommonAdapter
 * @Description: 万能适配Adapter
 * @date 2015-10-15 上午11:14:02
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    private int layoutId;

    private boolean mIsNull;

    public CommonAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext = context;
        this.mDatas = datas;
        this.layoutId = layoutId;
    }


    public void addData(List<T> lis) {
        mDatas.clear();
        mDatas.addAll(lis);
    }

    public void addDataOnly(List<T> lis) {
        mDatas.addAll(lis);
    }

    @Override
    public int getCount() {
//		return  mDatas.size();
        return (mDatas != null) ? mDatas.size() : 0;
    }

    @Override
    public T getItem(int arg0) {
        return mDatas.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
                layoutId, position);
        convert(holder, getItem(position));
        newconvert(holder, getItem(position),position);
        return holder.getConvertView();
    }

    public void newconvert(ViewHolder holder, T t, int position) {

    }

    public abstract void convert(ViewHolder holder, T t);

}
