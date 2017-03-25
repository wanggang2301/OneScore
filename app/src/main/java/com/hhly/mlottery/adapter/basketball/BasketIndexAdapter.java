package com.hhly.mlottery.adapter.basketball;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.index.BasketIndexBean;
import com.hhly.mlottery.bean.enums.BasketOddsTypeEnum;
import com.hhly.mlottery.widget.IndexOddsItemView;

import java.util.List;


/**
 * @author: Wangg
 * @Name：BasketIndexAdapter
 * @Description:
 * @Created on:2017/3/20  16:24.
 */

public class BasketIndexAdapter extends BaseQuickAdapter<BasketIndexBean.DataBean.AllInfoBean> {

    private List<BasketIndexBean.DataBean.AllInfoBean> list;

    private Context mContext;

    private String type;

    private OnItemClickListener onItemClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnOddIetmClickListener(BasketIndexAdapter.onOddIetmClickListener onOddIetmClickListener) {
        this.onOddIetmClickListener = onOddIetmClickListener;
    }

    private onOddIetmClickListener onOddIetmClickListener;


    public BasketIndexAdapter(Context context, List<BasketIndexBean.DataBean.AllInfoBean> data, String type) {
        super(R.layout.odd_test, data);
        this.type = type;
        mContext = context;
    }


    /* public BasketIndexAdapter(Context context) {
        list = new ArrayList<>();
    }*/

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final BasketIndexBean.DataBean.AllInfoBean allInfoBean) {
        baseViewHolder.setText(R.id.cpi_item_leagueName_txt, allInfoBean.getLeagueName());
        baseViewHolder.setTextColor(R.id.cpi_item_leagueName_txt, Color.parseColor(allInfoBean.getLeagueColor()));
        baseViewHolder.setText(R.id.cpi_item_time_txt, allInfoBean.getTime());
        baseViewHolder.setText(R.id.cpi_host_team_txt, allInfoBean.getHomeTeam());
        baseViewHolder.setText(R.id.cpi_guest_team_txt, allInfoBean.getGuestTeam());
        baseViewHolder.setText(R.id.cpi_item_remainTime_txt, allInfoBean.getRemainTime());
        baseViewHolder.setText(R.id.cpi_score_txt, allInfoBean.getMatchResult());

        //allInfoBean.getMatchStatus()

        //0:未开赛,1:一节,2:二节,5:1'OT，以此类推 -1:完场,-2:待定,-3:中断,-4:取消,-5:推迟,50中场

        /**
         * section ==2
         * staus =1,2   1st half   status=3,4   2nd half
         section =4

         staus=1   1st   2  2nd  3 3rd  4  4th
         *
         *
         */

        int section = 2;

        String statusTxt = "";

        switch (allInfoBean.getMatchStatus()) {
            case 0:
                baseViewHolder.setText(R.id.cpi_score_txt, "VS");

                statusTxt = mContext.getResources().getString(R.string.tennis_match_not_start);
                break;
            case 1:
                if (section == 2) {
                    statusTxt = "1st half";
                } else {
                    statusTxt = "1st";
                }

                break;
            case 2:
                if (section == 2) {
                    statusTxt = "1st half";
                } else {
                    statusTxt = "2nd";
                }
                break;

            case 3:

                if (section == 2) {
                    statusTxt = "2nd half";
                } else {
                    statusTxt = "2rd";

                }


                break;
            case 4:
                if (section == 2) {
                    statusTxt = "2nd half";
                } else {
                    statusTxt = "4th";
                }
                break;
            case 5:
                statusTxt = "OT1";
                break;
            case 6:
                statusTxt = "OT2";

                break;
            case 7:
                statusTxt = "OT3";

                break;
            case -1:
                statusTxt = mContext.getResources().getString(R.string.snooker_state_over_game);

                break;
            case -2:
                statusTxt = mContext.getResources().getString(R.string.tennis_match_dd);
                baseViewHolder.setText(R.id.cpi_score_txt, "VS");

                break;
            case -3:
                statusTxt = mContext.getResources().getString(R.string.tennis_match_zd);
                break;
            case -4:
                statusTxt = mContext.getResources().getString(R.string.basket_analyze_dialog_cancle);
                baseViewHolder.setText(R.id.cpi_score_txt, "VS");

                break;
            case -5:
                statusTxt = mContext.getResources().getString(R.string.tennis_match_tc);
                baseViewHolder.setText(R.id.cpi_score_txt, "VS");

                break;
            case 50:
                statusTxt = mContext.getResources().getString(R.string.paused_txt);
                break;
            default:
                break;
        }

        baseViewHolder.setText(R.id.tv_tag, statusTxt);

        bindOdds(baseViewHolder, allInfoBean);

        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(allInfoBean);
                }
            }
        });

    }


    /**
     * 绑定赔率信息
     */
    private void bindOdds(BaseViewHolder holder, final BasketIndexBean.DataBean.AllInfoBean allInfoBean) {
        LinearLayout container = holder.getView(R.id.odds_container);

        setTitleText(holder);

        container.removeAllViews();
        IndexOddsItemView indexOddsItemView = null;
        List<BasketIndexBean.DataBean.AllInfoBean.MatchOddsBean> comList = allInfoBean.getMatchOdds();


        for (final BasketIndexBean.DataBean.AllInfoBean.MatchOddsBean item : comList) {
            indexOddsItemView = new IndexOddsItemView(mContext);
            indexOddsItemView.bindData(item, type);
            if (onOddIetmClickListener != null) {

                indexOddsItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onOddIetmClickListener.onOddItemCLick(allInfoBean.getThirdId(), item.getComId());

                    }
                });
            }


            container.addView(indexOddsItemView);
        }
        // 最后一个隐藏底部分割线
        if (indexOddsItemView != null) {
            indexOddsItemView.hideDivider();
        }
    }


    public interface OnItemClickListener {
        void onItemClick(BasketIndexBean.DataBean.AllInfoBean allInfoBean);
    }

    public interface onOddIetmClickListener {
        void onOddItemCLick(String thirdId, String comId);
    }


    /**
     * 设置标题文字
     */
    private void setTitleText(BaseViewHolder holder) {
        switch (type) {
            case BasketOddsTypeEnum.ASIALET:
                holder.setText(R.id.cpi_item_home_txt, mContext.getString(R.string.foot_odds_alet_left));
                holder.setText(R.id.cpi_item_odds_txt, mContext.getString(R.string.foot_odds_alet_middle));
                holder.setText(R.id.cpi_item_guest_txt, mContext.getString(R.string.foot_odds_alet_right));
                break;
            case BasketOddsTypeEnum.ASIASIZE:
                holder.setText(R.id.cpi_item_home_txt, mContext.getString(R.string.foot_odds_asize_left));
                holder.setText(R.id.cpi_item_odds_txt, mContext.getString(R.string.foot_odds_asize_middle));
                holder.setText(R.id.cpi_item_guest_txt, mContext.getString(R.string.foot_odds_asize_right));
                break;
            case BasketOddsTypeEnum.EURO:
                holder.setText(R.id.cpi_item_home_txt, mContext.getString(R.string.foot_odds_eu_left));
                holder.setText(R.id.cpi_item_odds_txt, mContext.getString(R.string.foot_odds_eu_middle));
                holder.setText(R.id.cpi_item_guest_txt, mContext.getString(R.string.foot_odds_eu_right));
                break;
        }


        //item_index_basket

   /* public void addAll(List<BasketIndexBean.DataBean.AllInfoBean> infoBean) {
        list.addAll(infoBean);
    }*/
    }
}
