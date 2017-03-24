package com.hhly.mlottery.adapter.snooker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketOddsDetailsActivity;
import com.hhly.mlottery.activity.LoginActivity;
import com.hhly.mlottery.bean.snookerbean.snookerIndexBean.SnookerIndexBean;
import com.hhly.mlottery.frame.cpifrag.SnookerIndex.SIndexFragment;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.widget.SnookerIndexItemView;

import java.util.List;

/**
 * 描    述：斯诺克指数列表
 * 作    者：mady@13322.com
 * 时    间：2017/3/20
 */
public class SnookerIndexAdapter extends BaseQuickAdapter<SnookerIndexBean.AllInfoEntity> {

    Context context;
    private String mType;
    private TextView home;
    private TextView handicap;
    private TextView guest;

    public SnookerIndexAdapter(List<SnookerIndexBean.AllInfoEntity> data, Context context,String type) {
        super(R.layout.item_snooker_index, data);
        this.context=context;
        mType=type;
    }

    @Override
    protected void convert(BaseViewHolder holder, SnookerIndexBean.AllInfoEntity allInfoEntity) {
        holder.setText(R.id.cpi_item_leagueName_txt,allInfoEntity.getLeagueName());
        if(allInfoEntity.getLeagueColor()!=null){
            holder.setTextColor(R.id.cpi_item_leagueName_txt, Color.parseColor(allInfoEntity.getLeagueColor()));
        }
        home=holder.getView(R.id.cpi_item_home_txt);
        handicap=holder.getView(R.id.cpi_item_odds_txt);
        guest=holder.getView(R.id.cpi_item_guest_txt);
        holder.setText(R.id.cpi_item_time_txt,allInfoEntity.getMatchInfo().getOpenTime());
        holder.setText(R.id.tv_tag,allInfoEntity.getMatchInfo().getMatchState()==null?"":allInfoEntity.getMatchInfo().getMatchState()); //状态
        holder.setText(R.id.cpi_host_team_txt,allInfoEntity.getMatchInfo().getMatchHomeName());
        holder.setText(R.id.cpi_guest_team_txt,allInfoEntity.getMatchInfo().getMatchGuestName());
        holder.setText(R.id.cpi_score_txt,allInfoEntity.getMatchInfo().getMatchResult()==null?"VS":allInfoEntity.getMatchInfo().getMatchResult());
        if(mType.equals(SIndexFragment.ODDS_EURO)){
            home.setText(context.getString(R.string.odd_home_op_txt));
            guest.setText(context.getString(R.string.odd_guest_op_txt));
        }else if(mType.equals(SIndexFragment.ODDS_LET)){ //亚盘
            home.setText(context.getString(R.string.odd_home_op_txt));
            handicap.setText(context.getString(R.string.odd_dish_txt));
            guest.setText(context.getString(R.string.odd_guest_op_txt));
        }else if(mType.equals(SIndexFragment.ODDS_SIZE)){
            home.setText(context.getString(R.string.odd_home_big_txt));
            handicap.setText(context.getString(R.string.odd_dish_txt));
            guest.setText(context.getString(R.string.odd_guest_big_txt));
        }else if(mType.equals(SIndexFragment.SINGLE_DOUBLE)){ //单双
            home.setText(context.getString(R.string.number_bjsc_dan));
            guest.setText(context.getString(R.string.number_bjsc_suang));
        }

        //绑定赔率
        bindOdds(holder,allInfoEntity);

    }

    /**
     * 绑定赔率事件
     */
    private void bindOdds(BaseViewHolder holder,SnookerIndexBean.AllInfoEntity data){
        LinearLayout container=holder.getView(R.id.snooker_odds_container);
        container.removeAllViews();
//        CpiOddsItemView cpiOddsItemView = null;
//        List<NewOddsInfo.AllInfoBean.ComListBean> comList = data.getComList();
//        for (final NewOddsInfo.AllInfoBean.ComListBean item : comList) {
//            if (item.belongToShow(companies)) {
//                cpiOddsItemView = new CpiOddsItemView(mContext);
//                cpiOddsItemView.bindData(item, type);
//                if (onOddsClickListener != null) {
//                    cpiOddsItemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            onOddsClickListener.onOddsClick(data, item);
//                        }
//                    });
//                }
//                container.addView(cpiOddsItemView);
//            }
//        }
//        // 最后一个隐藏底部分割线
//        if (cpiOddsItemView != null) {
//            cpiOddsItemView.hideDivider();
//        }
        SnookerIndexItemView itemView=null;
        List<SnookerIndexBean.AllInfoEntity.ComListEntity> comList=data.getComList();
        for(SnookerIndexBean.AllInfoEntity.ComListEntity item : comList){
            itemView=new SnookerIndexItemView(context);
            itemView.bindData(item,mType);
            container.addView(itemView);
            //点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                }
            });
        }
        // 最后一个隐藏底部分割线
        if(itemView!=null){
            itemView.hideDivider();
        }

    }

    /**
     * 赔率item的点击事件
     */
    public interface SnookerOddsOnClick{
        void onOddsClich(SnookerIndexBean.AllInfoEntity allInfoEntity,SnookerIndexBean.AllInfoEntity.ComListEntity entity);
    }

}
