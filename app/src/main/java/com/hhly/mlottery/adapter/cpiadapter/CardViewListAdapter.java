package com.hhly.mlottery.adapter.cpiadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by 103TJL on 2016/5/4.
 * cardview里面listview的适配器
 */
public class CardViewListAdapter extends BaseAdapter {
    //公司水位信息
    private List<NewOddsInfo.AllInfoBean.ComListBean> mComList;

    private Context context;
    private LayoutInflater mInflater;

    public CardViewListAdapter(Context context, List<NewOddsInfo.AllInfoBean.ComListBean> mComList) {
        super();
        this.context = context;
        this.mComList = mComList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mComList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mComList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_cpi_odds_list, null);
            item = new ListViewItem(convertView);
            convertView.setTag(item);
        } else {
            item = (ListViewItem) convertView.getTag();
        }
        //公司名称
        item.cpi_item_list_company_txt.setText(mComList.get(position).getComName());
        //即赔
        item.cpi_item_list_home_txt.setText(mComList.get(position).getCurrLevel().getLeft());
        item.cpi_item_list_odds_txt.setText(mComList.get(position).getCurrLevel().getMiddle());
        item.cpi_item_list_guest_txt.setText(mComList.get(position).getCurrLevel().getRight());
        //初赔
        item.cpi_item_list_home2_txt.setText(mComList.get(position).getPreLevel().getLeft());
        item.cpi_item_list_odds2_txt.setText(mComList.get(position).getPreLevel().getMiddle());
        item.cpi_item_list_guest2_txt.setText(mComList.get(position).getPreLevel().getRight());

        return convertView;
    }

    private static class ListViewItem {
        //公司名称
        public TextView cpi_item_list_company_txt;
        //即赔 主队，盘口，客队
        public TextView cpi_item_list_home_txt;
        public TextView cpi_item_list_odds_txt;
        public TextView cpi_item_list_guest_txt;
        //初赔 主队，盘口，客队
        public TextView cpi_item_list_home2_txt;
        public TextView cpi_item_list_odds2_txt;
        public TextView cpi_item_list_guest2_txt;

        public ListViewItem(View v) {
            cpi_item_list_company_txt = (TextView) v.findViewById(R.id.cpi_item_list_company_txt);

            cpi_item_list_home_txt = (TextView) v.findViewById(R.id.cpi_item_list_home_txt);
            cpi_item_list_odds_txt = (TextView) v.findViewById(R.id.cpi_item_list_odds_txt);
            cpi_item_list_guest_txt = (TextView) v.findViewById(R.id.cpi_item_list_guest_txt);

            cpi_item_list_home2_txt = (TextView) v.findViewById(R.id.cpi_item_list_home2_txt);
            cpi_item_list_odds2_txt = (TextView) v.findViewById(R.id.cpi_item_list_odds2_txt);
            cpi_item_list_guest2_txt = (TextView) v.findViewById(R.id.cpi_item_list_guest2_txt);
        }
    }

    /**
     * 清除数据
     */
    public void cardViewclearData() {
        // clear the data
        mComList.clear();
        notifyDataSetChanged();
    }
}
