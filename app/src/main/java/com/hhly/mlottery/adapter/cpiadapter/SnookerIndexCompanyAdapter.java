package com.hhly.mlottery.adapter.cpiadapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.snookerbean.snookerIndexBean.SnookerIndexBean;

import java.util.List;

/**
 * 描    述：
 * 作    者：mady@13322.com
 * 时    间：2017/3/23
 */
public class SnookerIndexCompanyAdapter extends BaseAdapter {

    private List<SnookerIndexBean.CompanyEntity> mCompanyBean;

    public SnookerIndexCompanyAdapter(List<SnookerIndexBean.CompanyEntity> companyList) {
        this.mCompanyBean = companyList;
    }

    @Override
    public int getCount() {
        return mCompanyBean.size();
    }

    @Override
    public Object getItem(int position) {
        return mCompanyBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewItem item;
        Context context = parent.getContext();
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_dialog_company, parent, false);
            item = new ListViewItem(convertView);
            convertView.setTag(item);
        } else {
            item = (ListViewItem) convertView.getTag();
        }
        item.checkedTextView.setText(mCompanyBean.get(position).getComName());
        item.checkedTextView.setChecked(mCompanyBean.get(position).isChecked());
        item.checkedTextView.setTextColor(ContextCompat.getColor(context, R.color.msg));
        // 如果checkedTextView有选中的就给设置选中的背景
        if (item.checkedTextView.isChecked()) {
            item.cpi_img_checked.setSelected(true);
        } else {
            item.cpi_img_checked.setSelected(false);
        }
        return convertView;
    }
    private static class ListViewItem {

        public CheckedTextView checkedTextView;
        // 是否选中的图片
        private ImageView cpi_img_checked;

        public ListViewItem(View v) {
            checkedTextView = (CheckedTextView) v.findViewById(R.id.item_checkedTextView);
            cpi_img_checked = (ImageView) v.findViewById(R.id.item_img_checked);
        }
    }
}
