package com.hhly.mlottery.adapter.basketball;

import android.content.Context;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;

/**
 * description:
 * author: yixq
 * Created by A on 2016/7/18.
 */
public class BasketDatabaseDetailsAdapter extends CommonAdapter<String> {

    private List mDatas;
    public BasketDatabaseDetailsAdapter(Context context, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
        this.mDatas = datas;
    }

    @Override
    public void convert(ViewHolder holder, String s) {

        holder.setText(R.id.basket_database_details_name , s);
    }
}
