package com.hhly.mlottery.adapter.snooker;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.enums.TennisOddsTypeEnum;
import com.hhly.mlottery.bean.snookerbean.snookerIndexBean.SnookerIndexBean;
import com.hhly.mlottery.frame.BallType;
import com.hhly.mlottery.frame.cpifrag.SnookerIndex.SIndexFragment;
import com.hhly.mlottery.widget.SnookerIndexItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * 描    述：斯诺克指数列表
 * 作    者：mady@13322.com
 * 时    间：2017/3/20
 */
public class SnookerIndexAdapter extends BaseQuickAdapter<SnookerIndexBean.AllInfoEntity> {

    Context context;
    private String mType;
    private int mBallType;
    private TextView home;
    private TextView handicap;
    private TextView guest;
    private SnookerOddsOnClick onOddsClickListener; // 赔率点击监听
    List<SnookerIndexBean.CompanyEntity> companies;

    List<String> toDetailCompanies=new ArrayList<>();//传给详情页的公司

    public SnookerIndexAdapter(List<SnookerIndexBean.AllInfoEntity> data,List<SnookerIndexBean.CompanyEntity> companies, Context context,String type,int ballType) {
        super(R.layout.item_snooker_index, data);
        this.context=context;
        mType=type;
        this.companies=companies;
        this.mBallType=ballType;
    }

    public void setOnOddsClickListener(SnookerOddsOnClick onOddsClickListener) {
        this.onOddsClickListener = onOddsClickListener;
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

        holder.setText(R.id.cpi_host_team_txt,allInfoEntity.getMatchInfo().getMatchHomeName());
        holder.setText(R.id.cpi_guest_team_txt,allInfoEntity.getMatchInfo().getMatchGuestName());
        holder.setText(R.id.cpi_score_txt,allInfoEntity.getMatchInfo().getMatchResult()==null?"VS":allInfoEntity.getMatchInfo().getMatchResult());
        setStatus(holder,allInfoEntity);
        if(mBallType==BallType.SNOOKER){
            if(mType.equals(SIndexFragment.ODDS_EURO)){
                home.setText(context.getString(R.string.odd_home_op_txt));
                guest.setText(context.getString(R.string.odd_guest_op_txt));
                handicap.setVisibility(View.GONE);
            }else if(mType.equals(SIndexFragment.ODDS_LET)){ //亚盘
                home.setText(context.getString(R.string.odd_home_op_txt));
                handicap.setText(context.getString(R.string.odd_dish_txt));
                guest.setText(context.getString(R.string.odd_guest_op_txt));
            }else if(mType.equals(SIndexFragment.ODDS_SIZE)){
                home.setText(context.getString(R.string.odd_home_big_txt));
                handicap.setText(context.getString(R.string.odd_dish_txt));
                guest.setText(context.getString(R.string.odd_guest_big_txt));
            }else if (mType.equals(SIndexFragment.SINGLE_DOUBLE)){ //单双
                home.setText(context.getString(R.string.number_bjsc_dan));
                handicap.setVisibility(View.GONE);
                guest.setText(context.getString(R.string.number_bjsc_suang));
            }
        }else { //网球
            if(mType.equals(TennisOddsTypeEnum.EURO)){
                home.setText(context.getString(R.string.odd_home_op_txt));
                guest.setText(context.getString(R.string.odd_guest_op_txt));
                handicap.setVisibility(View.GONE);
            }else if(mType.equals(TennisOddsTypeEnum.ASIALET)){ //亚盘
                home.setText(context.getString(R.string.odd_home_op_txt));
                handicap.setText(context.getString(R.string.odd_dish_txt));
                guest.setText(context.getString(R.string.odd_guest_op_txt));
            }else if(mType.equals(TennisOddsTypeEnum.ASIASIZE)){
                home.setText(context.getString(R.string.odd_home_big_txt));
                handicap.setText(context.getString(R.string.odd_dish_txt));
                guest.setText(context.getString(R.string.odd_guest_big_txt));
            }
        }


        //绑定赔率
        bindOdds(holder,allInfoEntity);

    }

    /**
     * 绑定赔率事件
     */
    private void bindOdds(BaseViewHolder holder, final SnookerIndexBean.AllInfoEntity data){
        LinearLayout container=holder.getView(R.id.odds_container);
        container.removeAllViews();

        SnookerIndexItemView itemView=null;
        final List<SnookerIndexBean.AllInfoEntity.ComListEntity> comList=data.getComList();
        for(final SnookerIndexBean.AllInfoEntity.ComListEntity item : comList){
//            if(item.belongToShow(companies)){
//                toDetailCompanies.clear();
//                toDetailCompanies.add(item.getComName());
                itemView=new SnookerIndexItemView(context);
                itemView.bindData(item,mType,mBallType);
                container.addView(itemView);
                if(onOddsClickListener!=null){
                    //点击事件
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //
                            onOddsClickListener.onOddsClick(data.getMatchInfo().getMatchId(),item.getComId());
                        }
                    });
                }
//            }


        }
        // 最后一个隐藏底部分割线
        if(itemView!=null){
            itemView.hideDivider();
        }

    }

    /**
     * 设置比赛状态
     * @param holder
     * @param allInfoEntity
     */
    private void setStatus(BaseViewHolder holder, SnookerIndexBean.AllInfoEntity allInfoEntity){
        String state;
        if(allInfoEntity.getMatchInfo()!=null&&allInfoEntity.getMatchInfo().getMatchState()!=null){
            state=allInfoEntity.getMatchInfo().getMatchState();
            if(mBallType== BallType.TENNLS){
                switch (state){
                    case "-6": //p2退赛
                        holder.setText(R.id.tv_tag,context.getString(R.string.tennis_match_p2));
                        break;
                    case "-5": //p1退赛
                        holder.setText(R.id.tv_tag,context.getString(R.string.tennis_match_p1));
                        break;
                    case "-4": //待定
                        holder.setText(R.id.tv_tag,context.getString(R.string.tennis_match_dd));
                        break;
                    case "-3": //推迟
                        holder.setText(R.id.tv_tag,context.getString(R.string.tennis_match_tc));
                        break;
                    case "-2": //中断
                        holder.setText(R.id.tv_tag,context.getString(R.string.tennis_match_zd));
                        break;
                    case "-1": //完
                        holder.setText(R.id.tv_tag,context.getString(R.string.tennis_match_over));
                        break;
                    case "0": //未开始
                        holder.setText(R.id.tv_tag,context.getString(R.string.tennis_match_not_start));
                        holder.setText(R.id.cpi_score_txt,"VS");
                        break;
                    default: //进行中
                        holder.setText(R.id.tv_tag,context.getString(R.string.tennis_match_join));
                        break;
                }
            }else if(mBallType==BallType.SNOOKER){
                switch (state){
//                    0 暂停
//                    1 未开始
//                    2 结束
//                    3 进行中
//                    4 休息中
//                            -5 左退赛
//                            -6 右退赛

                    case "0":
                        holder.setText(R.id.tv_tag,context.getString(R.string.snooker_state_pause));
                        break;
                    case "1":
                        holder.setText(R.id.tv_tag,context.getString(R.string.snooker_state_no_start));
                        break;
                    case "2":
                        holder.setText(R.id.tv_tag,context.getString(R.string.snooker_state_over_game));
                        break;
                    case "3":
                        holder.setText(R.id.tv_tag,context.getString(R.string.snooker_state_have_ing));
                        break;
                    case "4":
                        holder.setText(R.id.tv_tag,context.getString(R.string.snooker_state_resting));
                        break;
                    case "-5":
                        holder.setText(R.id.tv_tag,context.getString(R.string.tennis_match_p1));
                        break;
                    case "-6":
                        holder.setText(R.id.tv_tag,context.getString(R.string.tennis_match_p2));
                        break;
                    default:
                        holder.setText(R.id.tv_tag,"");//不显示
                        break;
                }
            }

        }

    }

    /**
     * 赔率item的点击事件
     */
    public interface SnookerOddsOnClick{
        void onOddsClick(String matchId,String comId);
    }

}
