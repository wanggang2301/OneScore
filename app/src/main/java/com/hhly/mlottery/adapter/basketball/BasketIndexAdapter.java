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
        super(R.layout.item_index_basket, data);
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
        baseViewHolder.setText(R.id.cpi_score_txt, allInfoBean.getMatchResult());


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
                        onOddIetmClickListener.onOddItemCLick(allInfoBean, item);

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
        void onOddItemCLick(BasketIndexBean.DataBean.AllInfoBean allInfoBean, BasketIndexBean.DataBean.AllInfoBean.MatchOddsBean item);
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
