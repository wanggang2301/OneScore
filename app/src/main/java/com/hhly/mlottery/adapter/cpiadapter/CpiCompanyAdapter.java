package com.hhly.mlottery.adapter.cpiadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;

import java.util.List;

/**
 * Created by 103TJL on 2016/4/11.
 * 选择公司的适配器
 */
public class CpiCompanyAdapter extends BaseAdapter {

    //    private List<Map<String, String>> cpiCompanyList;
    private List<NewOddsInfo.CompanyBean> mCompanyBean;
    private Context context;
    private LayoutInflater mInflater;

    public CpiCompanyAdapter(Context context, List<NewOddsInfo.CompanyBean> mCompanyBean) {
        super();
        this.context = context;
        this.mCompanyBean = mCompanyBean;
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
        item.checkedTextView.setChecked(mCompanyBean.get(position).isChecked());
        item.checkedTextView.setTextColor(context.getResources().getColor(R.color.msg));
        //如果checkedTextView有选中的就给设置选中的背景
        if (item.checkedTextView.isChecked()) {
//            item.cpi_img_checked.setBackground(context.getResources().getDrawable(R.mipmap.cpi_img_select_true));
            item.cpi_img_checked.setSelected(true);
        } else {
//            item.cpi_img_checked.setBackground(context.getResources().getDrawable(R.mipmap.cpi_img_select));
//            item.cpi_img_checked.setBackgroundResource(R.mipmap.cpi_img_select);
            item.cpi_img_checked.setSelected(false);
        }
        return convertView;
    }

    private static class ListViewItem {
        public CheckedTextView checkedTextView;
        //是否选中的图片
        private ImageView cpi_img_checked;

        public ListViewItem(View v) {
            checkedTextView = (CheckedTextView) v.findViewById(R.id.item_checkedTextView);
            cpi_img_checked = (ImageView) v.findViewById(R.id.item_img_checked);
        }
    }
}
