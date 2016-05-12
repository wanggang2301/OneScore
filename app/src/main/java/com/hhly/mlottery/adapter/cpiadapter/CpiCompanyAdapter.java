package com.hhly.mlottery.adapter.cpiadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.frame.CPIFragment;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 103TJL on 2016/4/11.
 * 选择公司的适配器
 */
public class CpiCompanyAdapter extends BaseAdapter {

    //    private List<Map<String, String>> cpiCompanyList;
    private List<NewOddsInfo.CompanyBean> mCompanyBean;
    private Context context;
    private LayoutInflater mInflater;
    private ListView mListView;

    public CpiCompanyAdapter(Context context, List<NewOddsInfo.CompanyBean> mCompanyBean, ListView mListView) {
        super();
        this.context = context;
        this.mCompanyBean = mCompanyBean;
        this.mListView = mListView;
        this.mInflater = LayoutInflater.from(context);
//        this.mCheckedTextView = mCheckedTextView;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mCompanyBean.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mCompanyBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    /**
     * 根据选中的position选中
     */
//    public void setDefSelect(List<Integer> defItemList, boolean isTrue) {
//        this.defItemList = defItemList;
//        this.isTrue = isTrue;
//        notifyDataSetChanged();
//    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_dialog_company, null);
            item = new ListViewItem(convertView);
            convertView.setTag(item);
        } else {
            item = (ListViewItem) convertView.getTag();
        }
        item.checkedTextView.setText(mCompanyBean.get(position).getComName());
        if (CPIFragment.booleanList.size() > 0) {
            mListView.setItemChecked(position, CPIFragment.booleanList.get(position));
            item.checkedTextView.setChecked(CPIFragment.booleanList.get(position));
        } else {
            //默认皇冠和浩博
            if (position == 0) {
                mListView.setItemChecked(position, true);
                item.checkedTextView.setChecked(true);
            }
            if (position == 1) {
                mListView.setItemChecked(position, true);
                item.checkedTextView.setChecked(true);

            }
        }


        return convertView;
    }

    private static class ListViewItem {
        public CheckedTextView checkedTextView;

        public ListViewItem(View v) {
            checkedTextView = (CheckedTextView) v.findViewById(R.id.item_checkedTextView);
        }
    }
}
