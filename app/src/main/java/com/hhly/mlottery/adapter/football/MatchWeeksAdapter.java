package com.hhly.mlottery.adapter.football;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.DateUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by yuely198 on 2017/5/23.
 * 带周几的日期选择器
 */

public class MatchWeeksAdapter extends BaseAdapter {

    private List<Map<String, String>> cpiDateList;

    private Context context;
    private int defItem;//当前的item的position(用于点击item设置item背景颜色)

    public MatchWeeksAdapter(Context context, List<Map<String, String>> cpiDateList) {
        super();
        this.context = context;
        this.cpiDateList = cpiDateList;
    }

    @Override
    public int getCount() {
        return cpiDateList.size();
    }

    @Override
    public Object getItem(int position) {
        return cpiDateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 根据选中的 position 更改 item_event_half_finish 背景颜色
     */
    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        MatchWeeksAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_dialog_layout, parent, false);
            viewHolder = new MatchWeeksAdapter.ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.dialog_txt_cancel_list);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MatchWeeksAdapter.ViewHolder) convertView.getTag();
        }


        viewHolder.mTextView.setText(DateUtil.convertDateToNation(cpiDateList.get(position).get("date"))+"\t\t\t"+ DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(DateUtil.convertDateToNation(cpiDateList.get(position).get("date")))));

        if (defItem == position) {//如果点击listview的当前的position相等
            // 设置背景颜色
            convertView.setBackgroundResource(R.color.whitesmoke);
        } else {
            convertView.setBackgroundResource(R.color.transparent);
        }
        return convertView;
    }

    class ViewHolder {
        TextView mTextView;
    }
}
