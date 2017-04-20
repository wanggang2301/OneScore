package com.hhly.mlottery.adapter.basketball;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.activity.BasketOddsDetailsActivity;
import com.hhly.mlottery.bean.basket.basketdetails.BasketDetailOddsBean;
import com.hhly.mlottery.bean.basket.basketdetails.OddsDataEntity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andy on 2016/4/14 10:34.
 * <p/>
 * 描述：篮球指数
 */
public class BasketOddsAdapter  extends BaseAdapter{
    private LayoutInflater inflater;
    private Context context;
    private List<BasketDetailOddsBean.CompanyOddsEntity> mOddsList;
    private String  mType;
    private final String EURO="euro";

    public BasketOddsAdapter(Context context, List<BasketDetailOddsBean.CompanyOddsEntity> mOddsList,String mType) {
        this.context = context;
        this.mType=mType;
        inflater=LayoutInflater.from(this.context);
        this.mOddsList = mOddsList;
    }

    @Override
    public int getCount() {
        return mOddsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mOddsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
           convertView=inflater.inflate(R.layout.item_basket_odds,parent,false);
            holder=new ViewHolder();
            holder.company= (TextView) convertView.findViewById(R.id.item_basket_odds_company);
            holder.guest_immediate_win= (TextView) convertView.findViewById(R.id.item_basket_odds_guest_win_immediate);
            holder.guest_history_win= (TextView) convertView.findViewById(R.id.item_basket_odds_guest_win_history);
            holder.immediate_handicap= (TextView) convertView.findViewById(R.id.item_basket_odds_handicap_immediate);
            holder.history_handicap= (TextView) convertView.findViewById(R.id.item_basket_odds_handicap_history);
            holder.home_immediate_win= (TextView) convertView.findViewById(R.id.item_basket_odds_home_win_immediate);
            holder.home_history_win= (TextView) convertView.findViewById(R.id.item_basket_odds_home_win_history);
            holder.handicap_layout= (LinearLayout) convertView.findViewById(R.id.item_basket_odds_layout_handicap);
            holder.image_more= (ImageView) convertView.findViewById(R.id.item_image_basket_odds_more);

            convertView.setTag(holder);
        }
        else{
            holder= (ViewHolder) convertView.getTag();
        }
        if(mType.equals(EURO)){ //欧赔没有中间那一列盘口
            holder.handicap_layout.setVisibility(View.GONE);
        }
        if(mType.equals(EURO)&&mOddsList.get(position).getCompany().equals(context.getResources().getString(R.string.odds_equals))){
            holder.image_more.setVisibility(View.INVISIBLE);
        }else{
            holder.image_more.setVisibility(View.VISIBLE);
        }
        List<OddsDataEntity> oddsData=mOddsList.get(position).getOddsData();
        holder.company.setText(mOddsList.get(position).getCompany());
        if(oddsData!=null){
            if(oddsData.size()==2){

                holder.guest_immediate_win.setText(oddsData.get(0).getLeftOdds());
                holder.guest_history_win.setText(oddsData.get(1).getLeftOdds());
                holder.home_immediate_win.setText(oddsData.get(0).getRightOdds());
                holder.home_history_win.setText(oddsData.get(1).getRightOdds());

                holder.immediate_handicap.setText(oddsData.get(0).getHandicapValue());
                holder.history_handicap.setText(oddsData.get(1).getHandicapValue());

            }
           else if(oddsData.size()==1){//只有初赔
                holder.guest_history_win.setText(oddsData.get(0).getLeftOdds());
                holder.guest_immediate_win.setText("--");
                holder.home_history_win.setText(oddsData.get(0).getRightOdds());
                holder.home_immediate_win.setText("--");
                holder.history_handicap.setText(oddsData.get(0).getHandicapValue());
                holder.immediate_handicap.setText("--");
            }

            if(oddsData.get(0).getHandicapValueTrend()==1){//  盘口上升
                holder.immediate_handicap.setBackgroundColor(context.getResources().getColor(R.color.odds_up));
            }
            else if(oddsData.get(0).getHandicapValueTrend()==-1){
                holder.immediate_handicap.setBackgroundColor(context.getResources().getColor(R.color.odds_down));
            }
            else {
                holder.immediate_handicap.setBackgroundColor(context.getResources().getColor(R.color.transparency));
            }
            //左侧的颜色变化

            if(oddsData.get(0).getLeftOddsTrend()==1){ //上升
                holder.guest_immediate_win.setTextColor(context.getResources().getColor(R.color.odds_up));
            }
            else if(oddsData.get(0).getLeftOddsTrend()==-1){
                holder.guest_immediate_win.setTextColor(context.getResources().getColor(R.color.odds_down));
            }
            else{
                holder.guest_immediate_win.setTextColor(context.getResources().getColor(R.color.mdy_333));
            }
            //右侧数据的颜色变化
            if(oddsData.get(0).getRightOddsTrend()==1){
                holder.home_immediate_win.setTextColor(context.getResources().getColor(R.color.odds_up));
            }
            else if(oddsData.get(0).getRightOddsTrend()==-1){
                holder.home_immediate_win.setTextColor(context.getResources().getColor(R.color.odds_down));
            }
            else {
                holder.home_immediate_win.setTextColor(context.getResources().getColor(R.color.mdy_333));
            }

            convertView.setOnClickListener(new ConvertViewClickListener(position));
        }

        return convertView;


    }

    /**
     * convertview的点击事件的类
     */
    class ConvertViewClickListener implements View.OnClickListener{
        private int position;

        public ConvertViewClickListener(int position) {
            this.position = position;
        }
        public ConvertViewClickListener(){

        }

        @Override
        public void onClick(View v) {
            if(mType.equals(BasketDetailsActivityTest.ODDS_EURO)&&position==0){
                return;
            }
            MobclickAgent.onEvent(MyApp.getContext(),"BasketOddsDetailsActivity");
            Intent intent = new Intent(context, BasketOddsDetailsActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("type", mType);
            bundle.putInt("position",position);
            bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) mOddsList);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }
    protected static class ViewHolder {
        private TextView company;
        private TextView guest_immediate_win;
        private TextView guest_history_win;
        private TextView immediate_handicap;
        private TextView history_handicap;
        private TextView home_immediate_win;
        private TextView home_history_win;
        private LinearLayout handicap_layout;
        private ImageView image_more;

    }
}
