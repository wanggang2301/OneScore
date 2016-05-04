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
import com.hhly.mlottery.activity.BasketDetailsActivity;
import com.hhly.mlottery.activity.BasketOddsDetailsActivity;
import com.hhly.mlottery.bean.basket.BasketDetails.BasketDetailOddsBean;
import com.hhly.mlottery.bean.basket.BasketDetails.OddsDataEntity;
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
            holder.image_handicap= (ImageView) convertView.findViewById(R.id.item_basket_odds_image);

            convertView.setTag(holder);
        }
        else{
            holder= (ViewHolder) convertView.getTag();
        }
        if(mType.equals(EURO)){
            holder.handicap_layout.setVisibility(View.GONE);
        }
        List<OddsDataEntity> oddsData=mOddsList.get(position).getOddsData();
        holder.company.setText(mOddsList.get(position).getCompany());
        holder.guest_immediate_win.setText(oddsData.get(0).getLeftOdds());
        holder.guest_history_win.setText(oddsData.get(1).getLeftOdds());
        holder.home_immediate_win.setText(oddsData.get(0).getRightOdds());
        holder.home_history_win.setText(oddsData.get(1).getRightOdds());
        holder.immediate_handicap.setText(oddsData.get(0).getHandicapValue());
        holder.history_handicap.setText(oddsData.get(1).getHandicapValue());

        if(oddsData.get(0).getHandicapValueTrend()==1){//上升
            holder.image_handicap.setImageResource(R.mipmap.red);
            holder.immediate_handicap.setTextColor(context.getResources().getColor(R.color.odds_up));
        }
        else if(oddsData.get(0).getHandicapValueTrend()==-1){
            holder.image_handicap.setImageResource(R.mipmap.green);
            holder.immediate_handicap.setTextColor(context.getResources().getColor(R.color.odds_down));
        }
        else {
            holder.image_handicap.setVisibility(View.INVISIBLE);
            holder.immediate_handicap.setTextColor(context.getResources().getColor(R.color.white));
        }


        convertView.setOnClickListener(new ConvertViewClickListener(position));
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
            if(mType.equals(BasketDetailsActivity.ODDS_EURO)&&position==0){
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
        private ImageView image_handicap;
    }
}
