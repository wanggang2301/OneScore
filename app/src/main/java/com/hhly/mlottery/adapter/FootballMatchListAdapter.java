package com.hhly.mlottery.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballMatchActivity;
import com.hhly.mlottery.bean.FootballLotteryBean;
import com.hhly.mlottery.bean.infoCenterBean.ListEntity;

import java.util.List;

/**
 * Created by yuely198 on 2017/3/16.
 * 足球竞彩列表Adapter
 */

public class FootballMatchListAdapter extends BaseQuickAdapter<FootballLotteryBean.BettingListBean> {

    Context mContext;
    List<FootballLotteryBean.BettingListBean> datas;

    public FootballMatchListAdapter(Context context, int layoutResId, List<FootballLotteryBean.BettingListBean> data) {
        super(layoutResId, data);
        this.datas = data;
        this.mContext = context;
    }

    public void updateDatas(List<FootballLotteryBean.BettingListBean> bean, int i) {

        datas = bean;
    }
    /**
     * 条目监听
     */
    private FootballMatchActivity.BettingBuyClickListener mBuyClick; //关注监听回掉

    public void  setmBuyClick(FootballMatchActivity.BettingBuyClickListener mBuyClick) {
        this.mBuyClick = mBuyClick;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final FootballLotteryBean.BettingListBean listEntity) {

        LinearLayout itemOnclik=baseViewHolder.getView(R.id.football_match_child);

        itemOnclik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBuyClick != null) {
                    mBuyClick.BuyOnClick(view, listEntity.getMatchId());
                }
            }
        });

        if (getViewHolderPosition(baseViewHolder) == 0) {
            baseViewHolder.setVisible(R.id.tv_back, false);
        } else {
            baseViewHolder.setVisible(R.id.tv_back, true);
        }

        if (listEntity.getWeek() == 1) {
            baseViewHolder.setText(R.id.match_week, MyApp.getContext().getResources().getString(R.string.number_1));


        } else if (listEntity.getWeek() == 2) {
            baseViewHolder.setText(R.id.match_week, MyApp.getContext().getResources().getString(R.string.number_2));


        } else if (listEntity.getWeek() == 3) {

            baseViewHolder.setText(R.id.match_week, MyApp.getContext().getResources().getString(R.string.number_3));

        } else if (listEntity.getWeek() == 4) {

            baseViewHolder.setText(R.id.match_week, MyApp.getContext().getResources().getString(R.string.number_4));

        } else if (listEntity.getWeek() == 5) {

            baseViewHolder.setText(R.id.match_week, MyApp.getContext().getResources().getString(R.string.number_5));

        } else if (listEntity.getWeek() == 6) {

            baseViewHolder.setText(R.id.match_week, MyApp.getContext().getResources().getString(R.string.number_6));

        } else if (listEntity.getWeek() == 7) {

            baseViewHolder.setText(R.id.match_week, MyApp.getContext().getResources().getString(R.string.number_7));

        }


        baseViewHolder.setText(R.id.match_home, listEntity.getHomeName());
        baseViewHolder.setText(R.id.match_guest, listEntity.getGuestName());

        baseViewHolder.setText(R.id.textView12, listEntity.getSerNum());//比赛序号
        baseViewHolder.setText(R.id.match_leaguename, listEntity.getLeagueName());


        //baseViewHolder.setText(R.id.match_status, listEntity.getStatus());//比赛状态


        baseViewHolder.setText(R.id.match_time, listEntity.getStartTime());

        if (listEntity.getStatus() != null) {
            baseViewHolder.setVisible(R.id.match_socer, true);
            baseViewHolder.setVisible(R.id.item_football_frequency, false);
            if ("0".equals(listEntity.getStatus())) {// 未开
                baseViewHolder.setVisible(R.id.match_socer, false);
                baseViewHolder.setText(R.id.match_status, mContext.getString(R.string.snooker_state_no_start));
                baseViewHolder.setTextColor(R.id.match_status, mContext.getResources().getColor(R.color.content_txt_black));

                baseViewHolder.setTextColor(R.id.match_letwinodds, mContext.getResources().getColor(R.color.content_txt_black));
                baseViewHolder.setTextColor(R.id.match_letsameodds, mContext.getResources().getColor(R.color.content_txt_black));
                baseViewHolder.setTextColor(R.id.match_letloseodds, mContext.getResources().getColor(R.color.content_txt_black));
                baseViewHolder.setTextColor(R.id.winodds, mContext.getResources().getColor(R.color.content_txt_black));
                baseViewHolder.setTextColor(R.id.sameodds, mContext.getResources().getColor(R.color.content_txt_black));
                baseViewHolder.setTextColor(R.id.loseodds, mContext.getResources().getColor(R.color.content_txt_black));

            } else if ("1".equals(listEntity.getStatus())) {// 上半场
                baseViewHolder.setVisible(R.id.match_socer, false);
                baseViewHolder.setVisible(R.id.item_football_frequency, true);

                try {
                    int keeptime = Integer.parseInt(listEntity.getKeepTime());// 设置时间
                    if (keeptime > 45) {
                        baseViewHolder.setText(R.id.match_status, "45+");
                        baseViewHolder.setTextColor(R.id.match_status, mContext.getResources().getColor(R.color.football_keeptime));
                    } else {
                        baseViewHolder.setText(R.id.match_status, keeptime + "");
                        baseViewHolder.setTextColor(R.id.match_status, mContext.getResources().getColor(R.color.football_keeptime));
                    }
                } catch (Exception e) {

                    baseViewHolder.setText(R.id.match_status, "E");
                    baseViewHolder.setTextColor(R.id.match_status, mContext.getResources().getColor(R.color.betting_recommend_zhuanjia_spf_color));
                }

            } else if ("3".equals(listEntity.getStatus())) {// 下半场
                baseViewHolder.setVisible(R.id.match_socer, false);
                baseViewHolder.setVisible(R.id.item_football_frequency, true);
                try {
                    int keeptime = Integer.parseInt(listEntity.getKeepTime());// 设置时间
                    if (keeptime > 90) {

                        baseViewHolder.setText(R.id.match_status, "90+");
                        baseViewHolder.setTextColor(R.id.match_status, mContext.getResources().getColor(R.color.football_keeptime));
                    } else {
                        baseViewHolder.setText(R.id.match_status, keeptime + "");
                        baseViewHolder.setTextColor(R.id.match_status, mContext.getResources().getColor(R.color.football_keeptime));
                    }
                } catch (Exception e) {

                    baseViewHolder.setText(R.id.match_status, "E");
                    baseViewHolder.setTextColor(R.id.match_status, mContext.getResources().getColor(R.color.betting_recommend_zhuanjia_spf_color));
                }

            } else if ("4".equals(listEntity.getStatus())) {// 加时
                baseViewHolder.setVisible(R.id.item_football_frequency, false);
                baseViewHolder.setVisible(R.id.match_socer, false);
                baseViewHolder.setText(R.id.match_status, mContext.getString(R.string.immediate_status_overtime));
                baseViewHolder.setTextColor(R.id.match_status, mContext.getResources().getColor(R.color.betting_recommend_zhuanjia_spf_color));

            } else if ("5".equals(listEntity.getStatus())) {// 点球
                baseViewHolder.setVisible(R.id.item_football_frequency, false);
                baseViewHolder.setVisible(R.id.match_socer, false);
                baseViewHolder.setText(R.id.match_status, mContext.getString(R.string.immediate_status_point));
                baseViewHolder.setTextColor(R.id.match_status, mContext.getResources().getColor(R.color.betting_recommend_zhuanjia_spf_color));

            } else if ("-1".equals(listEntity.getStatus())) {// 完场
                baseViewHolder.setVisible(R.id.item_football_frequency, false);
                if (listEntity.getHomeScore() == null || listEntity.getGuestScore() == null) {
                    baseViewHolder.setVisible(R.id.match_socer, false);
                } else {
                    baseViewHolder.setVisible(R.id.match_socer, true);
                    baseViewHolder.setText(R.id.match_socer, listEntity.getHomeScore() + ":" + listEntity.getGuestScore());
                }
                baseViewHolder.setText(R.id.match_status, "完场");
                baseViewHolder.setTextColor(R.id.match_status, mContext.getResources().getColor(R.color.red));
                baseViewHolder.setTextColor(R.id.match_socer, mContext.getResources().getColor(R.color.red));

            } else if ("2".equals(listEntity.getStatus())) {// 中场
                baseViewHolder.setVisible(R.id.item_football_frequency, false);
                baseViewHolder.setVisible(R.id.match_socer, false);
                baseViewHolder.setText(R.id.match_status, mContext.getString(R.string.immediate_status_midfield));
                baseViewHolder.setTextColor(R.id.match_status, mContext.getResources().getColor(R.color.betting_recommend_zhuanjia_spf_color));


            } else if ("-10".equals(listEntity.getStatus())) {// 取消
                baseViewHolder.setVisible(R.id.item_football_frequency, false);
                baseViewHolder.setVisible(R.id.match_socer, false);
                baseViewHolder.setText(R.id.match_status, mContext.getString(R.string.immediate_status_cancel));
                baseViewHolder.setTextColor(R.id.match_status, mContext.getResources().getColor(R.color.red));


            } else if ("-11".equals(listEntity.getStatus())) {// 待定
                baseViewHolder.setVisible(R.id.item_football_frequency, false);
                baseViewHolder.setVisible(R.id.match_socer, false);
                baseViewHolder.setText(R.id.match_status, mContext.getString(R.string.immediate_status_hold));
                baseViewHolder.setTextColor(R.id.match_status, mContext.getResources().getColor(R.color.red));


            } else if ("-12".equals(listEntity.getStatus())) {// 腰斩
                baseViewHolder.setVisible(R.id.item_football_frequency, false);
                baseViewHolder.setVisible(R.id.match_socer, false);
                baseViewHolder.setText(R.id.match_status, mContext.getString(R.string.immediate_status_cut));
                baseViewHolder.setTextColor(R.id.match_status, mContext.getResources().getColor(R.color.red));


            } else if ("-13".equals(listEntity.getStatus())) {// 中断
                baseViewHolder.setVisible(R.id.item_football_frequency, false);
                baseViewHolder.setVisible(R.id.match_socer, false);
                baseViewHolder.setText(R.id.match_status, mContext.getString(R.string.immediate_status_mesomere));
                baseViewHolder.setTextColor(R.id.match_status, mContext.getResources().getColor(R.color.red));

            } else if ("-14".equals(listEntity.getStatus())) {// 推迟
                baseViewHolder.setVisible(R.id.item_football_frequency, false);
                baseViewHolder.setVisible(R.id.match_socer, false);
                baseViewHolder.setText(R.id.match_status, mContext.getString(R.string.immediate_status_postpone));
                baseViewHolder.setTextColor(R.id.match_status, mContext.getResources().getColor(R.color.red));

            }


            if ("1".equals(listEntity.getStatus()) || "3".equals(listEntity.getStatus())) {// 显示秒的闪烁
                baseViewHolder.setText(R.id.item_football_frequency, "\'");
                baseViewHolder.setVisible(R.id.item_football_frequency, true);

                final AlphaAnimation anim1 = new AlphaAnimation(1, 1);
                anim1.setDuration(500);
                final AlphaAnimation anim2 = new AlphaAnimation(0, 0);
                anim2.setDuration(500);
                anim1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        baseViewHolder.getView(R.id.item_football_frequency).startAnimation(anim2);
                    }
                });

                anim2.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        baseViewHolder.getView(R.id.item_football_frequency).startAnimation(anim1);

                    }
                });

                baseViewHolder.getView(R.id.item_football_frequency).startAnimation(anim1);
            } else {

                baseViewHolder.setText(R.id.item_football_frequency, "");
                baseViewHolder.setVisible(R.id.item_football_frequency, false);
                baseViewHolder.getView(R.id.item_football_frequency).clearAnimation();

            }


            baseViewHolder.setText(R.id.match_letnumber, listEntity.getLetNumber());//让球数
            if (listEntity.getLetWinOdds() != null) {
                baseViewHolder.setText(R.id.match_letwinodds, listEntity.getLetWinOdds());
            } else {
                baseViewHolder.setText(R.id.match_letwinodds, "-");
            }
            if (listEntity.getLetSameOdds() != null) {
                baseViewHolder.setText(R.id.match_letsameodds, listEntity.getLetSameOdds());
            } else {
                baseViewHolder.setText(R.id.match_letsameodds, "-");
            }
            if (listEntity.getLetLoseOdds() != null) {
                baseViewHolder.setText(R.id.match_letloseodds, listEntity.getLetLoseOdds());
            } else {
                baseViewHolder.setText(R.id.match_letloseodds, "-");
            }


            baseViewHolder.setText(R.id.match_number, "0");//非让球数
            if (listEntity.getWinOdds() != null) {
                baseViewHolder.setText(R.id.winodds, listEntity.getWinOdds());
            } else {
                baseViewHolder.setText(R.id.winodds, "-");
            }
            if (listEntity.getSameOdds() != null) {
                baseViewHolder.setText(R.id.sameodds, listEntity.getSameOdds());
            } else {
                baseViewHolder.setText(R.id.sameodds, "-");
            }
            if (listEntity.getLoseOdds() != null) {
                baseViewHolder.setText(R.id.loseodds, listEntity.getLoseOdds());
            } else {
                baseViewHolder.setText(R.id.loseodds, "-");
            }

            if (listEntity.getLetwinoddsColorId() == 0) {

            }
            if (listEntity.getLetwinoddsColorId() != 0) {
                baseViewHolder.setBackgroundColor(R.id.match_letwinodds, listEntity.getLetwinoddsColorId());
            }
            if (listEntity.getLetsameoddsColorId() != 0) {
                baseViewHolder.setBackgroundColor(R.id.match_letsameodds, listEntity.getLetsameoddsColorId());
            }
            if (listEntity.getLetloseoddsColorId() != 0) {
                baseViewHolder.setBackgroundColor(R.id.match_letloseodds, listEntity.getLetloseoddsColorId());
            }

            if (listEntity.getWinoddsColorId() != 0) {
                baseViewHolder.setBackgroundColor(R.id.winodds, listEntity.getWinoddsColorId());
            }
            if (listEntity.getSameoddsColorId() != 0) {
                baseViewHolder.setBackgroundColor(R.id.sameodds, listEntity.getSameoddsColorId());
            }
            if (listEntity.getLoseoddsColorId() != 0) {
                baseViewHolder.setBackgroundColor(R.id.loseodds, listEntity.getLoseoddsColorId());
            }


            if (listEntity.getStatus().equals("-1")) {
                if (listEntity.getGuestScore() != null && listEntity.getHomeScore() != null && listEntity.getLetNumber() != null) {

                    int homeScore = Integer.parseInt(listEntity.getHomeScore() + "");
                    int guestScore = Integer.parseInt(listEntity.getGuestScore() + "");
                    int letNumber = Integer.parseInt(listEntity.getLetNumber() + "");


                    if (homeScore + letNumber > guestScore) {  //主胜
                        baseViewHolder.setTextColor(R.id.match_letwinodds, mContext.getResources().getColor(R.color.red));
                        baseViewHolder.setTextColor(R.id.match_letsameodds, mContext.getResources().getColor(R.color.content_txt_black));
                        baseViewHolder.setTextColor(R.id.match_letloseodds, mContext.getResources().getColor(R.color.content_txt_black));
                    } else if (homeScore + letNumber < guestScore) { //客胜
                        baseViewHolder.setTextColor(R.id.match_letsameodds, mContext.getResources().getColor(R.color.content_txt_black));
                        baseViewHolder.setTextColor(R.id.match_letwinodds, mContext.getResources().getColor(R.color.content_txt_black));
                        baseViewHolder.setTextColor(R.id.match_letloseodds, mContext.getResources().getColor(R.color.red));
                    } else {
                        baseViewHolder.setTextColor(R.id.match_letloseodds, mContext.getResources().getColor(R.color.content_txt_black));
                        baseViewHolder.setTextColor(R.id.match_letwinodds, mContext.getResources().getColor(R.color.content_txt_black));
                        baseViewHolder.setTextColor(R.id.match_letsameodds, mContext.getResources().getColor(R.color.red));
                    }

                    if (homeScore > guestScore) {  //主胜
                        baseViewHolder.setTextColor(R.id.winodds, mContext.getResources().getColor(R.color.red));
                        baseViewHolder.setTextColor(R.id.sameodds, mContext.getResources().getColor(R.color.content_txt_black));
                        baseViewHolder.setTextColor(R.id.loseodds, mContext.getResources().getColor(R.color.content_txt_black));

                    } else if (homeScore < guestScore) { //客胜
                        baseViewHolder.setTextColor(R.id.winodds, mContext.getResources().getColor(R.color.content_txt_black));
                        baseViewHolder.setTextColor(R.id.sameodds, mContext.getResources().getColor(R.color.content_txt_black));
                        baseViewHolder.setTextColor(R.id.loseodds, mContext.getResources().getColor(R.color.red));
                    } else {
                        baseViewHolder.setTextColor(R.id.winodds, mContext.getResources().getColor(R.color.content_txt_black));
                        baseViewHolder.setTextColor(R.id.loseodds, mContext.getResources().getColor(R.color.content_txt_black));
                        baseViewHolder.setTextColor(R.id.sameodds, mContext.getResources().getColor(R.color.red));
                    }

                }

            } else {
                baseViewHolder.setTextColor(R.id.match_letwinodds, mContext.getResources().getColor(R.color.content_txt_black));
                baseViewHolder.setTextColor(R.id.match_letsameodds, mContext.getResources().getColor(R.color.content_txt_black));
                baseViewHolder.setTextColor(R.id.match_letloseodds, mContext.getResources().getColor(R.color.content_txt_black));
                baseViewHolder.setTextColor(R.id.winodds, mContext.getResources().getColor(R.color.content_txt_black));
                baseViewHolder.setTextColor(R.id.loseodds, mContext.getResources().getColor(R.color.content_txt_black));
                baseViewHolder.setTextColor(R.id.sameodds, mContext.getResources().getColor(R.color.content_txt_black));
            }
        } else {

            baseViewHolder.setText(R.id.match_status, "");
            baseViewHolder.setVisible(R.id.match_socer, false);
        }

    }


    public void updateData(List<FootballLotteryBean.BettingListBean> bettingList) {
        this.datas = bettingList;
    }
}
