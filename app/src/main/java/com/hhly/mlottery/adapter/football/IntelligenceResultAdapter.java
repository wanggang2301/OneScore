package com.hhly.mlottery.adapter.football;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.intelligence.BigDataForecastData;
import com.hhly.mlottery.bean.intelligence.BigDataResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 描    述：
 * 作    者：mady@13322.com
 * 时    间：2016/8/11
 */
public class IntelligenceResultAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private Context context;
    private List<BigDataResult.GridViewEntity> list;

    public IntelligenceResultAdapter(Context context, List<BigDataResult.GridViewEntity> list) {
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
            convertView=inflater.inflate(R.layout.item_intelligence_gridview,parent,false);
            holder=new ViewHolder();
            holder.imageView= (ImageView) convertView.findViewById(R.id.item_result_image);
            holder.homeRate= (TextView) convertView.findViewById(R.id.item_tv_home_rate);
            holder.homePro= (ContentLoadingProgressBar) convertView.findViewById(R.id.item_pro_home_rate);
            holder.guestRate= (TextView) convertView.findViewById(R.id.item_tv_guest_rate);
            holder.guestPro= (ContentLoadingProgressBar) convertView.findViewById(R.id.item_pro_guest_rate);
            holder.description= (TextView) convertView.findViewById(R.id.item_tv_desc);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
            holder.homeRate.setText(list.get(position).getHome()==null?"--":list.get(position).getHome());
            holder.guestRate.setText(list.get(position).getGuest()==null?"--":list.get(position).getGuest());
            String home=list.get(position).getHome();
            if(null!=home){
                String a[]=home.split("%");
                int homeProgress= (int) Double.parseDouble(a[0]);
                holder.homePro.setProgress(homeProgress);
            }
            String guest=list.get(position).getGuest();
            if(null!=guest){
                String b[]=guest.split("%");
                int guestProgress= (int) Double.parseDouble(b[0]);
                holder.guestPro.setProgress(guestProgress);
            }

            switch (list.get(position).getType()){
                case 1:
                    holder.imageView.setImageResource(R.mipmap.result_winning_rate);
                    holder.description.setText(R.string.intelligent_win_rate);
                    break;
                case 2:
                    holder.imageView.setImageResource(R.mipmap.result_transmission_rate);
                    holder.description.setText(R.string.intelligent_lose_rate);
                    break;
                case 3:
                    holder.imageView.setImageResource(R.mipmap.result_big_rate);
                    holder.description.setText(R.string.intelligent_big_rate);
                    break;
                case 4:
                    holder.imageView.setImageResource(R.mipmap.result_small_rate);
                    holder.description.setText(R.string.intelligent_small_rate);
                    break;
                case 5:
                    holder.imageView.setImageResource(R.mipmap.result_goal_difference);
                    holder.description.setText(R.string.intelligent_goad_defference);
                    break;
                case 6:
                    holder.imageView.setImageResource(R.mipmap.result_goal_difference);
                    holder.description.setText(R.string.intellegent_lose_goal_def);
                    break;
                case 7:
                    holder.imageView.setImageResource(R.mipmap.result_goal_and_win);
                    holder.description.setText(R.string.intelligent_first_goal_win);
                    break;
                case 8:
                    holder.imageView.setImageResource(R.mipmap.result_goal_and_win);
                    holder.description.setText(R.string.intelligent_last_goal_win);
                    break;
                case 9:
                    holder.imageView.setImageResource(R.mipmap.result_no_goal);
                    holder.description.setText(R.string.intelligent_no_goal_rate);
                    break;
                case 10:
                    holder.imageView.setImageResource(R.mipmap.season_handicap_win); //赢盘率
                    holder.description.setText(context.getString(R.string.intelligent_rang)+list.get(position).getHandicap()+context.getString(R.string.intelligent_ball_win_rate));
                    break;
                case 11:
                    holder.imageView.setImageResource(R.mipmap.season_handicap_win); //输盘率
                    holder.description.setText(context.getString(R.string.intelligent_rang)+list.get(position).getHandicap()+context.getString(R.string.intelligent_ball_lose_rate));
                    break;
                case 12:
                    holder.imageView.setImageResource(R.mipmap.result_big_rate);
                    holder.description.setText(list.get(position).getHandicap()+context.getString(R.string.intelligent_big_rate));
                    break;
                case 13:
                    holder.imageView.setImageResource(R.mipmap.result_small_rate);
                    holder.description.setText(list.get(position).getHandicap()+context.getString(R.string.intelligent_small_rate));
                    break;
                case 14:
                    holder.imageView.setImageResource(R.mipmap.handicap_win);
                    holder.description.setText(context.getString(R.string.intelligent_rang)+list.get(position).getHandicap()+context.getString(R.string.intelligent_qiushenglv));
                    break;
                case 15:
                    holder.imageView.setImageResource(R.mipmap.handicap_draw);
                    holder.description.setText(context.getString(R.string.intelligent_rang)+list.get(position).getHandicap()+context.getString(R.string.intelligent_qiupinglv));
                    break;
                case 16:
                    holder.imageView.setImageResource(R.mipmap.handicap_lose);
                    holder.description.setText(context.getString(R.string.intelligent_rang)+list.get(position).getHandicap()+context.getString(R.string.intelligent_qiufulv));
                    break;

            }

        return convertView;
    }

    protected static class ViewHolder{
        private ImageView imageView;
        private TextView homeRate;
        private ContentLoadingProgressBar homePro;
        private TextView guestRate;
        private ContentLoadingProgressBar guestPro;
        private TextView description;
    }
}
