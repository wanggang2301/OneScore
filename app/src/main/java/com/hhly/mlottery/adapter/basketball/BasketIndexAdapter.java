package com.hhly.mlottery.adapter.basketball;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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

    private static final String EURO_AVERAGE = "euro";  //欧赔平均


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


    @Override
    protected void convert(BaseViewHolder baseViewHolder, final BasketIndexBean.DataBean.AllInfoBean allInfoBean) {
        baseViewHolder.setText(R.id.cpi_item_leagueName_txt, allInfoBean.getLeagueName());
        baseViewHolder.setTextColor(R.id.cpi_item_leagueName_txt, Color.parseColor(allInfoBean.getLeagueColor()));
        baseViewHolder.setText(R.id.cpi_item_time_txt, allInfoBean.getTime());

        //篮球客队在前主队在后
        baseViewHolder.setText(R.id.cpi_host_team_txt, allInfoBean.getGuestTeam());
        baseViewHolder.setText(R.id.cpi_guest_team_txt, allInfoBean.getHomeTeam());


        if (TextUtils.isEmpty(allInfoBean.getRemainTime()) || allInfoBean.getRemainTime().equals("")) {
            baseViewHolder.setText(R.id.cpi_item_remainTime_txt, "");
            baseViewHolder.setText(R.id.cpi_item_seconds_txt, "");
        } else {
            baseViewHolder.setText(R.id.cpi_item_remainTime_txt, allInfoBean.getRemainTime());
            baseViewHolder.setText(R.id.cpi_item_seconds_txt, "'");
        }

        baseViewHolder.setText(R.id.cpi_score_txt, allInfoBean.getMatchResult());
        setSecondAnim(baseViewHolder.getView(R.id.cpi_item_seconds_txt));
        //allInfoBean.getMatchStatus()

        //0:未开赛,1:一节,2:二节,5:1'OT，以此类推 -1:完场,-2:待定,-3:中断,-4:取消,-5:推迟,50中场

        /**
         * section ==2
         *  staus =1,2   1st half   status=3,4   2nd half
         *
         section =4

         staus=1   1st   status  2  2nd  3 3rd  4  4th
         *
         *
         */

        // int section = 0;

        String statusTxt = "";
        String sectionTxt = "";

        switch (allInfoBean.getMatchStatus()) {
            case 0:
                baseViewHolder.setText(R.id.cpi_score_txt, "VS");
                baseViewHolder.setTextColor(R.id.cpi_score_txt, Color.parseColor("#222222"));
                baseViewHolder.setTextColor(R.id.tv_tag, Color.parseColor("#222222"));
                baseViewHolder.setText(R.id.cpi_item_remainTime_txt, "");
                baseViewHolder.setText(R.id.cpi_item_seconds_txt, "");
                statusTxt = mContext.getResources().getString(R.string.tennis_match_not_start);
                break;
            case 1:
                if (allInfoBean.getSection() == 2) {
                    sectionTxt = "1st half";
                } else if (allInfoBean.getSection() == 4) {
                    sectionTxt = "1st";
                }

                if (TextUtils.isEmpty(allInfoBean.getRemainTime()) || allInfoBean.getRemainTime().equals("")) {
                    baseViewHolder.setText(R.id.cpi_item_remainTime_txt, sectionTxt);
                } else {
                    baseViewHolder.setText(R.id.cpi_item_remainTime_txt, sectionTxt + " " + allInfoBean.getRemainTime());
                }

                baseViewHolder.setText(R.id.cpi_item_seconds_txt, "'");
                baseViewHolder.setTextColor(R.id.cpi_score_txt, Color.parseColor("#0090ff"));
                baseViewHolder.setTextColor(R.id.tv_tag, Color.parseColor("#0090ff"));

                break;
            case 2:
                if (allInfoBean.getSection() == 2) {
                    sectionTxt = "1st half";
                } else if (allInfoBean.getSection() == 4) {
                    sectionTxt = "2nd";
                }


                if (TextUtils.isEmpty(allInfoBean.getRemainTime()) || allInfoBean.getRemainTime().equals("")) {
                    baseViewHolder.setText(R.id.cpi_item_remainTime_txt, sectionTxt);
                } else {
                    baseViewHolder.setText(R.id.cpi_item_remainTime_txt, sectionTxt + " " + allInfoBean.getRemainTime());
                }

                baseViewHolder.setText(R.id.cpi_item_seconds_txt, "'");
                baseViewHolder.setTextColor(R.id.cpi_score_txt, Color.parseColor("#0090ff"));
                baseViewHolder.setTextColor(R.id.tv_tag, Color.parseColor("#0090ff"));

                break;

            case 3:
                if (allInfoBean.getSection() == 2) {
                    sectionTxt = "2nd half";
                } else if (allInfoBean.getSection() == 4) {
                    sectionTxt = "3rd";
                }

                if (TextUtils.isEmpty(allInfoBean.getRemainTime()) || allInfoBean.getRemainTime().equals("")) {
                    baseViewHolder.setText(R.id.cpi_item_remainTime_txt, sectionTxt);
                } else {
                    baseViewHolder.setText(R.id.cpi_item_remainTime_txt, sectionTxt + " " + allInfoBean.getRemainTime());
                }

                baseViewHolder.setText(R.id.cpi_item_seconds_txt, "'");
                baseViewHolder.setTextColor(R.id.cpi_score_txt, Color.parseColor("#0090ff"));
                baseViewHolder.setTextColor(R.id.tv_tag, Color.parseColor("#0090ff"));

                break;
            case 4:
                if (allInfoBean.getSection() == 2) {
                    sectionTxt = "2nd half";
                } else if (allInfoBean.getSection() == 4) {
                    sectionTxt = "4th";
                }
                if (TextUtils.isEmpty(allInfoBean.getRemainTime()) || allInfoBean.getRemainTime().equals("")) {
                    baseViewHolder.setText(R.id.cpi_item_remainTime_txt, sectionTxt);
                } else {
                    baseViewHolder.setText(R.id.cpi_item_remainTime_txt, sectionTxt + " " + allInfoBean.getRemainTime());
                }
                baseViewHolder.setText(R.id.cpi_item_seconds_txt, "'");
                baseViewHolder.setTextColor(R.id.cpi_score_txt, Color.parseColor("#0090ff"));
                baseViewHolder.setTextColor(R.id.tv_tag, Color.parseColor("#0090ff"));

                break;
            case 5:
                sectionTxt = "OT1";
                if (TextUtils.isEmpty(allInfoBean.getRemainTime()) || allInfoBean.getRemainTime().equals("")) {
                    baseViewHolder.setText(R.id.cpi_item_remainTime_txt, sectionTxt);
                } else {
                    baseViewHolder.setText(R.id.cpi_item_remainTime_txt, sectionTxt + " " + allInfoBean.getRemainTime());
                }
                baseViewHolder.setText(R.id.cpi_item_seconds_txt, "'");
                baseViewHolder.setTextColor(R.id.cpi_score_txt, Color.parseColor("#0090ff"));
                baseViewHolder.setTextColor(R.id.tv_tag, Color.parseColor("#0090ff"));

                break;
            case 6:
                sectionTxt = "OT2";
                if (TextUtils.isEmpty(allInfoBean.getRemainTime()) || allInfoBean.getRemainTime().equals("")) {
                    baseViewHolder.setText(R.id.cpi_item_remainTime_txt, sectionTxt);
                } else {
                    baseViewHolder.setText(R.id.cpi_item_remainTime_txt, sectionTxt + " " + allInfoBean.getRemainTime());
                }
                baseViewHolder.setText(R.id.cpi_item_seconds_txt, "'");
                baseViewHolder.setTextColor(R.id.cpi_score_txt, Color.parseColor("#0090ff"));
                baseViewHolder.setTextColor(R.id.tv_tag, Color.parseColor("#0090ff"));

                break;
            case 7:
                sectionTxt = "OT3";
                if (TextUtils.isEmpty(allInfoBean.getRemainTime()) || allInfoBean.getRemainTime().equals("")) {
                    baseViewHolder.setText(R.id.cpi_item_remainTime_txt, sectionTxt);
                } else {
                    baseViewHolder.setText(R.id.cpi_item_remainTime_txt, sectionTxt + " " + allInfoBean.getRemainTime());
                }
                baseViewHolder.setText(R.id.cpi_item_seconds_txt, "'");
                baseViewHolder.setTextColor(R.id.cpi_score_txt, Color.parseColor("#0090ff"));
                baseViewHolder.setTextColor(R.id.tv_tag, Color.parseColor("#0090ff"));

                break;
            case -1:
                statusTxt = mContext.getResources().getString(R.string.snooker_state_over_game);
                baseViewHolder.setTextColor(R.id.cpi_score_txt, Color.parseColor("#e40000"));
                baseViewHolder.setTextColor(R.id.tv_tag, Color.parseColor("#e40000"));
                baseViewHolder.setText(R.id.cpi_item_remainTime_txt, "");
                baseViewHolder.setText(R.id.cpi_item_seconds_txt, "");

                break;
            case -2:
                statusTxt = mContext.getResources().getString(R.string.tennis_match_dd);
                baseViewHolder.setText(R.id.cpi_score_txt, "VS");
                baseViewHolder.setTextColor(R.id.cpi_score_txt, Color.parseColor("#222222"));
                baseViewHolder.setTextColor(R.id.tv_tag, Color.parseColor("#222222"));
                baseViewHolder.setText(R.id.cpi_item_remainTime_txt, "");
                baseViewHolder.setText(R.id.cpi_item_seconds_txt, "");
                break;
            case -3:
                statusTxt = mContext.getResources().getString(R.string.tennis_match_zd);
                baseViewHolder.setTextColor(R.id.cpi_score_txt, Color.parseColor("#e40000"));
                baseViewHolder.setTextColor(R.id.tv_tag, Color.parseColor("#e40000"));
                baseViewHolder.setText(R.id.cpi_item_remainTime_txt, "");
                baseViewHolder.setText(R.id.cpi_item_seconds_txt, "");

                break;
            case -4:
                statusTxt = mContext.getResources().getString(R.string.basket_analyze_dialog_cancle);
                baseViewHolder.setText(R.id.cpi_score_txt, "VS");
                baseViewHolder.setTextColor(R.id.cpi_score_txt, Color.parseColor("#222222"));
                baseViewHolder.setTextColor(R.id.tv_tag, Color.parseColor("#222222"));
                baseViewHolder.setText(R.id.cpi_item_remainTime_txt, "");
                baseViewHolder.setText(R.id.cpi_item_seconds_txt, "");

                break;
            case -5:
                statusTxt = mContext.getResources().getString(R.string.tennis_match_tc);
                baseViewHolder.setText(R.id.cpi_score_txt, "VS");
                baseViewHolder.setTextColor(R.id.cpi_score_txt, Color.parseColor("#222222"));
                baseViewHolder.setTextColor(R.id.tv_tag, Color.parseColor("#222222"));
                baseViewHolder.setText(R.id.cpi_item_remainTime_txt, "");
                baseViewHolder.setText(R.id.cpi_item_seconds_txt, "");

                break;
            case 50:
                statusTxt = mContext.getResources().getString(R.string.paused_txt);
                baseViewHolder.setTextColor(R.id.cpi_score_txt, Color.parseColor("#0090ff"));
                baseViewHolder.setTextColor(R.id.tv_tag, Color.parseColor("#0090ff"));
                baseViewHolder.setText(R.id.cpi_item_remainTime_txt, "");
                baseViewHolder.setText(R.id.cpi_item_seconds_txt, "");
                break;
            default:
                break;
        }

        baseViewHolder.setText(R.id.tv_tag, statusTxt);

        bindOdds(baseViewHolder, allInfoBean);

        baseViewHolder.getView(R.id.ll_match).setOnClickListener(new View.OnClickListener() {
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
            if (!item.getComId().equals(EURO_AVERAGE)) {
                if (onOddIetmClickListener != null) {
                    indexOddsItemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onOddIetmClickListener.onOddItemCLick(allInfoBean.getThirdId(), item.getComId());
                        }
                    });
                }
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
                holder.setText(R.id.cpi_item_home_txt, mContext.getString(R.string.basket_analyze_guest_win));
                holder.setText(R.id.cpi_item_odds_txt, mContext.getString(R.string.basket_analyze_dish));
                holder.setVisible(R.id.cpi_item_odds_txt, true);

                holder.setText(R.id.cpi_item_guest_txt, mContext.getString(R.string.basket_analyze_home_win));
                break;
            case BasketOddsTypeEnum.ASIASIZE:
                holder.setText(R.id.cpi_item_home_txt, mContext.getString(R.string.foot_odds_asize_left));
                holder.setText(R.id.cpi_item_odds_txt, mContext.getString(R.string.foot_odds_asize_middle));
                holder.setVisible(R.id.cpi_item_odds_txt, true);
                holder.setText(R.id.cpi_item_guest_txt, mContext.getString(R.string.foot_odds_asize_right));
                break;
            case BasketOddsTypeEnum.EURO:
                holder.setText(R.id.cpi_item_home_txt, mContext.getString(R.string.basket_analyze_guest_win));
                holder.setText(R.id.cpi_item_odds_txt, mContext.getString(R.string.basket_analyze_dish));
                holder.setVisible(R.id.cpi_item_odds_txt, false);
                holder.setText(R.id.cpi_item_guest_txt, mContext.getString(R.string.basket_analyze_home_win));
                break;
        }
    }

    /**
     * 设置分钟动画效果
     */

    private void setSecondAnim(final View view) {
        final AlphaAnimation hideAnim = new AlphaAnimation(0, 0);
        hideAnim.setDuration(500);
        final AlphaAnimation showAnim = new AlphaAnimation(1, 1);
        showAnim.setDuration(500);
        hideAnim.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(showAnim);
            }
        });
        showAnim.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(hideAnim);
            }
        });
        view.startAnimation(hideAnim);
    }


    abstract class AnimationListenerAdapter implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
