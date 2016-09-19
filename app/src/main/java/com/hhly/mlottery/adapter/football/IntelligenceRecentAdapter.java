package com.hhly.mlottery.adapter.football;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.intelligence.BigDataResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 描    述：情报界面近期的数据
 * 作    者：mady@13322.com
 * 时    间：2016/8/16
 */
public class IntelligenceRecentAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private Context context;
    private List<BigDataResult.GridViewEntity> list;

    public IntelligenceRecentAdapter(Context context, List<BigDataResult.GridViewEntity> list) {
        this.context = context;
        inflater=LayoutInflater.from(context);
        if(list==null){
            list=new ArrayList<>();
        }
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_intelligent_recent,parent,false);
            holder=new ViewHolder();
            holder.imageView= (ImageView) convertView.findViewById(R.id.item_recent_imageview);
            holder.homePro= (ProgressBar) convertView.findViewById(R.id.item_recent_home_pro);
            holder.guestPro= (ProgressBar) convertView.findViewById(R.id.item_recent_guest_pro);
            holder.homeNum= (TextView) convertView.findViewById(R.id.item_recent_home_num);
            holder.guestNum= (TextView) convertView.findViewById(R.id.item_recent_guest_num);
            holder.description= (TextView) convertView.findViewById(R.id.item_recent_desc);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.homeNum.setText(list.get(position).getHome()==null?"--":list.get(position).getHome());
        holder.homePro.setProgress(Integer.parseInt(String.valueOf(list.get(position).getHome()==null?0:list.get(position).getHome())));
        holder.guestNum.setText(list.get(position).getGuest()==null?"--":list.get(position).getGuest());
        holder.guestPro.setProgress(Integer.parseInt(String.valueOf(list.get(position).getGuest()==null?0:list.get(position).getGuest())));
        switch (list.get(position).getType()){
            case 20:
                holder.imageView.setImageResource(R.mipmap.recent_win_number);
                holder.description.setText(R.string.intelligent_win_num);
                break;
            case 21:
                holder.imageView.setImageResource(R.mipmap.handicap_win_number);
                holder.description.setText(R.string.intelligent_let_win);
                break;
            case 22:
                holder.imageView.setImageResource(R.mipmap.continue_big_ball);
                holder.description.setText(R.string.intelligent_continue_big);
                break;
            case 23:
                holder.imageView.setImageResource(R.mipmap.continue_small_ball);
                holder.description.setText(R.string.intelligent_continue_small);
                break;
        }

        return convertView;
    }

    protected class ViewHolder{
        private ImageView imageView;
        private TextView homeNum;
        private ProgressBar homePro;
        private TextView guestNum;
        private ProgressBar guestPro;
        private TextView description;

    }
}
