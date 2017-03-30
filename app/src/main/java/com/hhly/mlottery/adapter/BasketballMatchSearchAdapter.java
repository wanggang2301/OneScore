package com.hhly.mlottery.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketBallMatchSearchActivity;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.bean.BallMatchItemsBean;
import com.hhly.mlottery.bean.BasketballItemSearchBean;
import com.hhly.mlottery.callback.RecyclerViewItemClickListener;
import com.hhly.mlottery.frame.basketballframe.ImmedBasketballFragment;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;

import java.util.List;


/**
 * Created by yuely198 on 2017/3/26.
 */

public class BasketballMatchSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<BasketballItemSearchBean> mMatchdata;
    private LayoutInflater mLayoutInflater;

    private BasketballItemSearchBean.MatchOddsBean MatchOdds;
    private ImmediaViewHolder holder;
    private View view;

    public BasketballMatchSearchAdapter(Context mContext, List<BasketballItemSearchBean> mMatchdata) {
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mMatchdata = mMatchdata;
        this.mContext = mContext;
    }


    /**
     * 赛事item click
     */
    private RecyclerViewItemClickListener mOnItemClickListener = null;

    public void setmOnItemClickListener(RecyclerViewItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basketball_details_item, parent, false);
        holder = new ImmediaViewHolder(view);
        //将创建的View注册点击事件
     /*   view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, (String) v.getTag());
                }
            }
        });*/
        return holder;
    }


    /**
     * 数据ViewHolder
     */

    static class ImmediaViewHolder extends RecyclerView.ViewHolder {
        ImageView home_icon; //主队图标
        ImageView guest_icon; //客队图标

        ImageView mIv_guangzhu;//关注 星星

        TextView matches_name; //联赛名称
        TextView game_time; //比赛时间
        TextView st_time; //第几节
        TextView ongoing_time; // 单节时间

        TextView score_total;// 半全场比分
        TextView score_differ;//比分差

        TextView home_name;// 主队名称
        TextView guest_name;//客队名称

        TextView home_Ranking; //主队赛区
        TextView guest_Ranking; //客队赛区

        TextView basket_guest_all_score;
        TextView basket_score;
        TextView basket_home_all_score;

        TextView basket_half_score;// 主客队半场比分

        TextView basket_leftOdds;// 赔率 左
        TextView basket_rightOdds;// 赔率 右
        TextView basket_handicap;// 赔率盘口

        TextView backetball_total;//总
        TextView backetball_differ;//差

        TextView tv_a; // 赔率分割线
        TextView tv_b;

        TextView backetball_apos; // 秒针
        TextView basket_no_start; // 未开赛


        public ImmediaViewHolder(final View itemView) {
            super(itemView);
             home_icon = (ImageView) itemView.findViewById(R.id.home_icon);
//			holder.home_icon.setTag(childrenDataList.get(groupPosition).get(childPosition).getHomeLogoUrl());//url 标记

            guest_icon = (ImageView) itemView.findViewById(R.id.guest_icon);
//			holder.guest_icon.setTag(childrenDataList.get(groupPosition).get(childPosition).getGuestLogoUrl());//url 标记

            mIv_guangzhu = (ImageView) itemView.findViewById(R.id.Iv_guangzhu);


            matches_name = (TextView) itemView.findViewById(R.id.backetball_matches_name);
            game_time = (TextView) itemView.findViewById(R.id.backetball_game_time);
            st_time = (TextView) itemView.findViewById(R.id.backetball_st_time);
            ongoing_time = (TextView) itemView.findViewById(R.id.backetball_ongoing_time);
            score_total = (TextView) itemView.findViewById(R.id.backetball_score_total);
            score_differ = (TextView) itemView.findViewById(R.id.backetball_score_differ);
            home_name = (TextView) itemView.findViewById(R.id.home_name);
            guest_name = (TextView) itemView.findViewById(R.id.guest_name);

            home_Ranking = (TextView) itemView.findViewById(R.id.basket_home_Ranking);
            guest_Ranking = (TextView) itemView.findViewById(R.id.basket_guest_Ranking);

            basket_guest_all_score = (TextView) itemView.findViewById(R.id.basket_guest_all_score);
            basket_score = (TextView) itemView.findViewById(R.id.basket_score);
            basket_home_all_score = (TextView) itemView.findViewById(R.id.basket_home_all_score);

            basket_half_score = (TextView) itemView.findViewById(R.id.basket_half_score);

            basket_no_start = (TextView) itemView.findViewById(R.id.basket_no_start);

            /**
             * 适配4.1系统
             */
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion == 16) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(105, 65, 0, 0);
                basket_half_score.setLayoutParams(params);

                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params2.setMargins(120, 0, 10, 0);
//                holder.basket_score.setGravity(1);
                basket_score.setLayoutParams(params2);

                RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params3.setMargins(55, 0, 0, 0);
                basket_guest_all_score.setLayoutParams(params3);
            }
            basket_leftOdds = (TextView) itemView.findViewById(R.id.basket_leftOdds);
            basket_rightOdds = (TextView) itemView.findViewById(R.id.basket_rightOdds);
            basket_handicap = (TextView) itemView.findViewById(R.id.basket_handicap);

            backetball_differ = (TextView) itemView.findViewById(R.id.backetball_differ);
            backetball_total = (TextView) itemView.findViewById(R.id.backetball_total);

            tv_a = (TextView) itemView.findViewById(R.id.tv_a);
            tv_b = (TextView) itemView.findViewById(R.id.tv_b);

            backetball_apos = (TextView) itemView.findViewById(R.id.backetball_apos);


        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BasketballItemSearchBean match = mMatchdata.get(position);

        convert((BasketballMatchSearchAdapter.ImmediaViewHolder) holder, match);
    }

    private BasketBallMatchSearchActivity.BasketFocusClickListener mFocus; //关注监听回掉
    public void setmFocus(BasketBallMatchSearchActivity.BasketFocusClickListener mFocus) {
        this.mFocus = mFocus;
    }

    //处理数据
    private void convert(final ImmediaViewHolder holder, final BasketballItemSearchBean childredata) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BasketDetailsActivityTest.class);
                intent.putExtra(BasketDetailsActivityTest.BASKET_THIRD_ID, childredata.getThirdId());//跳转到详情
                intent.putExtra(BasketDetailsActivityTest.BASKET_MATCH_STATUS, childredata.getMatchStatus());//跳转到详情
                //用getActivity().startActivityForResult();不走onActivityResult ;
//        startActivityForResult(intent, REQUEST_DETAILSCODE);
                intent.putExtra("currentfragment", 0);
                intent.putExtra(BasketDetailsActivityTest.BASKET_MATCH_LEAGUEID,childredata.getLeagueId());
                intent.putExtra(BasketDetailsActivityTest.BASKET_MATCH_MATCHTYPE,1);
                mContext.startActivity(intent);
            }
        });

        /**
         * 关注 监听
         */
        String focusIds = PreferenceUtil.getString("basket_focus_ids", "");
        String[] Ids = focusIds.split("[,]");
        holder.mIv_guangzhu.setBackgroundResource(R.mipmap.iconfont_guanzhu);
        holder.mIv_guangzhu.setTag(false);
        for (String id : Ids) {
            if (id.equals(childredata.getThirdId())) {
                holder.mIv_guangzhu.setBackgroundResource(R.mipmap.iconfont_guanzhu_hover);
                holder.mIv_guangzhu.setTag(true);
                break;
            }
        }
        holder.mIv_guangzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFocus != null) {
                    mFocus.FocusOnClick(v, childredata);
                }
            }
        });

        holder.basket_guest_all_score.setTag(childredata.getThirdId());
        holder.basket_home_all_score.setTag(childredata.getThirdId());
        ImageLoader.load(mContext, null, R.mipmap.basket_default).into(holder.home_icon);
        ImageLoader.load(mContext, null, R.mipmap.basket_default).into(holder.guest_icon);


        //赔率设置
        MatchOdds = childredata.getMatchOdds();
        if (MatchOdds != null) {

            boolean asize = PreferenceUtil.getBoolean(MyConstants.BASKETBALL_rbSizeBall, false); //大小球
            boolean eur = PreferenceUtil.getBoolean(MyConstants.BASKETBALL_RBOCOMPENSATE, false);//欧赔
            boolean alet = PreferenceUtil.getBoolean(MyConstants.BASKETBALL_RBSECOND, true); //亚盘
            boolean noshow = PreferenceUtil.getBoolean(MyConstants.BASKETBALL_RBNOTSHOW, false);//不显示

            /**
             * 大小球
             */
            if (MatchOdds.getAsiaSize() != null) {
                if (MatchOdds.getAsiaSize().getCrown() == null) {
                    BasketballItemSearchBean.MatchOddsBean.AsiaSizeBean.CrownBean mAsize = MatchOdds.getAsiaSize().getCrown();

                    if (mAsize == null) {
                        holder.basket_leftOdds.setText("");
                        holder.basket_rightOdds.setText("");
                        holder.basket_handicap.setText("");
                        holder.tv_a.setText("");
                        holder.tv_b.setText("");
                    } else if (mAsize != null) {

                        if (mAsize.getLeftOdds() != null && mAsize.getRightOdds() != null && mAsize.getHandicapValue() != null) {
                            if (mAsize.getLeftOdds().equals("-") || mAsize.getRightOdds().equals("-") || mAsize.getHandicapValue().equals("-")) {
                                holder.basket_leftOdds.setText("");
                                holder.basket_rightOdds.setText("");
                                holder.basket_handicap.setText(R.string.basket_handicap_feng);
                                holder.tv_a.setText("");
                                holder.tv_b.setText("");
                            } else {
                                holder.basket_leftOdds.setText(mAsize.getLeftOdds());
                                holder.basket_rightOdds.setText(mAsize.getRightOdds());
                                holder.basket_handicap.setText(mContext.getString(R.string.basket_odds_asize) + mAsize.getHandicapValue());
                                holder.tv_a.setText("|");
                                holder.tv_b.setText("|");
                            }
                        } else {
                            holder.basket_leftOdds.setText("");
                            holder.basket_rightOdds.setText("");
                            holder.basket_handicap.setText("");
                            holder.tv_a.setText("");
                            holder.tv_b.setText("");
                        }
                    }
                } else {
                    holder.basket_leftOdds.setText("");
                    holder.basket_rightOdds.setText("");
                    holder.basket_handicap.setText("");
                    holder.tv_a.setText("");
                    holder.tv_b.setText("");
                }
            } else {
                holder.basket_leftOdds.setText("");
                holder.basket_rightOdds.setText("");
                holder.basket_handicap.setText("");
                holder.tv_a.setText("");
                holder.tv_b.setText("");
            }

            if (noshow) {
                holder.basket_leftOdds.setText("");
                holder.basket_rightOdds.setText("");
                holder.basket_handicap.setText("");
                holder.tv_a.setText("");
                holder.tv_b.setText("");
            }
        } else if (MatchOdds == null) {
            holder.basket_leftOdds.setText("");
            holder.basket_rightOdds.setText("");
            holder.basket_handicap.setText("");
            holder.tv_a.setText("");
            holder.tv_b.setText("");
        }


        //比分数据设置
        if (childredata.getLeagueName() != null) {
            holder.matches_name.setText(childredata.getLeagueName());
            holder.matches_name.setTextColor(Color.parseColor(childredata.getLeagueColor()));
        }

        holder.game_time.setText(childredata.getTime());
        /**
         * 去除两端的空格，trim()；  防止-->  "XX   ..."情况
         */
        if (childredata.getHomeTeam() != null) {
            holder.home_name.setText(childredata.getHomeTeam().trim());
        } else {
            holder.home_name.setText("--");
        }
        if (childredata.getGuestTeam() != null) {
            holder.guest_name.setText(childredata.getGuestTeam().trim());
        } else {
            holder.guest_name.setText("--");
        }


        Integer score = Integer.parseInt(String.valueOf(childredata.getMatchStatus()));

        if (String.valueOf(childredata.getMatchStatus()) != null) {

            int scorehome;
            int scoreguest;
            int scorehomehalf;
            int scoreguesthalf;

            if (childredata.getMatchScore() != null) {
                if (childredata.getMatchScore().getRemainTime() == null) {
//                    holder.ongoing_time.setText("进行中");
                    holder.ongoing_time.setText("");
                } else {
                    holder.ongoing_time.setText(childredata.getMatchScore().getRemainTime());
                }
            } else {
                holder.ongoing_time.setText("");
            }

            //0:未开赛,1:一节,2:二节,5:1'OT，以此类推 -1:完场,-2:待定,-3:中断,-4:取消,-5:推迟,50中场
            if (score == 0 || score == -2 || score == -3 || score == -4 || score == -5) {

                scorehome = 0;
                scoreguest = 0;
                scorehomehalf = 0;
                scoreguesthalf = 0;
            } else {

                if (childredata.getMatchScore() == null) {
                    scorehome = 0;
                    scoreguest = 0;
                    scorehomehalf = 0;
                    scoreguesthalf = 0;
                } else {

                    scorehome = childredata.getMatchScore().getHomeScore();//主队分数
                    scoreguest = childredata.getMatchScore().getGuestScore();//客队分数

                    if (childredata.getSection() == 2) { // 只有两节比赛的情况
                        scorehomehalf = childredata.getMatchScore().getHome1();//主队半场得分
                        scoreguesthalf = childredata.getMatchScore().getGuest1();//客队半场得分
                    } else {
                        scorehomehalf = childredata.getMatchScore().getHome1() + childredata.getMatchScore().getHome2();//主队半场得分
                        scoreguesthalf = childredata.getMatchScore().getGuest1() + childredata.getMatchScore().getGuest2();//客队半场得分
                    }
                }
            }

            switch (score) {
                case 0: //未开赛

                    holder.backetball_differ.setVisibility(View.INVISIBLE);
                    holder.backetball_total.setVisibility(View.INVISIBLE);
                    holder.score_total.setVisibility(View.INVISIBLE);
                    holder.score_differ.setVisibility(View.INVISIBLE);
                    holder.basket_score.setText("");
                    holder.basket_no_start.setVisibility(View.VISIBLE);
//                    holder.basket_score.setBackgroundColor(Color.WHITE);// -------设置背景色 (防止背景复用比分)-------
                    holder.basket_guest_all_score.setText("");
                    holder.basket_home_all_score.setText("");
                    holder.basket_guest_all_score.setVisibility(View.GONE);
                    holder.basket_home_all_score.setVisibility(View.GONE);
//					holder.basket_score.setTextColor(Color.BLACK);
//                    holder.basket_score.setTextColor(Color.parseColor("#666666"));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_vs));
                    holder.basket_half_score.setVisibility(View.INVISIBLE);
                    holder.ongoing_time.setVisibility(View.GONE);
//					holder.backetball_apos.setVisibility(View.GONE);
                    holder.st_time.setText("");
                    settingdata(holder, 0);

                    break;
                case 1: //一节

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);

//					holder.basket_all_score.setText(scoreguest + ":" + scorehome);
//					holder.basket_all_score.setTextColor(Color.BLUE);
                    holder.basket_score.setText(":");
                    holder.basket_no_start.setVisibility(View.GONE);
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));//#0085E1
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (childredata.getMatchScore() != null) {

                        holder.score_total.setText(scorehome + scoreguest + "");
//                    holder.score_differ.setText(Math.abs(scorehome - scoreguest) + "");
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

//                        holder.basket_guest_all_score.setText(scoreguest + "");
//                        holder.basket_home_all_score.setText(scorehome + "");
////                        startAnima(childredata, score, holder);

  /*                      if (childredata.isHomeAnim()) {
                            scoreAnimation(holder.basket_home_all_score, childredata.getMatchScore().getHomeScore() + ""); //启动动画 客队
                            childredata.setIsHomeAnim(false);
                        }
//                        else{
////                            holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");
//                        }
                        if (childredata.isGuestAnim()) {
                            scoreAnimation(holder.basket_guest_all_score, childredata.getMatchScore().getGuestScore() + ""); //启动动画 主队
                            childredata.setIsGuestAnim(false);
                        }*/
//                        else{
////                            holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
//                        }

                        holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.VISIBLE);
                        holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuest1() + ":" + childredata.getMatchScore().getHome1() + ")");
                        if (childredata.getSection() == 2) {
                            holder.st_time.setText(R.string.basket_1st_half);
                        } else {
                            holder.st_time.setText(R.string.basket_1st);
                        }
                        holder.ongoing_time.setVisibility(View.VISIBLE);
                        holder.backetball_apos.setVisibility(View.VISIBLE);
                    } else {
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");

                        holder.basket_half_score.setText("");
//                        holder.basket_half_score.setVisibility(View.GONE);
                        holder.ongoing_time.setVisibility(View.GONE);
                        holder.backetball_apos.setVisibility(View.GONE);
                    }
//					holder.backetball_apos.setVisibility(View.VISIBLE);
                    settingdata(holder, 1);
                    break;
                case 2: //二节

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

//					holder.basket_all_score.setText(scoreguest + ":" + scorehome);
//					holder.basket_all_score.setTextColor(Color.BLUE);
                    holder.basket_score.setText(":");
                    holder.basket_no_start.setVisibility(View.GONE);
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (childredata.getMatchScore() != null) {

                        holder.score_total.setText(scorehome + scoreguest + "");
//                    holder.score_differ.setText(Math.abs(scorehome - scoreguest) + "");
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

//                        holder.basket_guest_all_score.setText(scoreguest + "");
//                        holder.basket_home_all_score.setText(scorehome + "");
////                        startAnima(childredata, score, holder);
/*
                        if (childredata.isHomeAnim()) {
                            scoreAnimation(holder.basket_home_all_score, childredata.getMatchScore().getHomeScore() + ""); //启动动画 客队
                            childredata.setIsHomeAnim(false);
                        }
//                        else{
////                            holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");
//                        }
                        if (childredata.isGuestAnim()) {
                            scoreAnimation(holder.basket_guest_all_score, childredata.getMatchScore().getGuestScore() + ""); //启动动画 主队
                            childredata.setIsGuestAnim(false);
                        }*/
//                        else{
////                            holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
//                        }

                        holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.VISIBLE);
                        if (childredata.getSection() == 2) {
                            holder.st_time.setText(R.string.basket_1st_half);
                        } else {
                            holder.st_time.setText(R.string.basket_2nd);
                        }
//                    holder.st_time.setText("2nd");
                        holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuest2() + ":" + childredata.getMatchScore().getHome2() + ")");

                        holder.ongoing_time.setVisibility(View.VISIBLE);
                        holder.backetball_apos.setVisibility(View.VISIBLE);

                    } else {
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");

                        holder.basket_half_score.setText("");
//                        holder.basket_half_score.setVisibility(View.GONE);
                        holder.ongoing_time.setVisibility(View.GONE);
                        holder.backetball_apos.setVisibility(View.GONE);
                    }

//					holder.backetball_apos.setVisibility(View.VISIBLE);
                    settingdata(holder, 2);
                    break;
                case 3: //三节

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

//					holder.basket_all_score.setText(scoreguest + ":" + scorehome);
//					holder.basket_all_score.setTextColor(Color.BLUE);
                    holder.basket_score.setText(":");
                    holder.basket_no_start.setVisibility(View.GONE);
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (childredata.getMatchScore() != null) {

                        holder.score_total.setText((scorehomehalf + scoreguesthalf) + "/" + (scorehome + scoreguest));
//                    holder.score_differ.setText(Math.abs(scorehome - scoreguest) + "");
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

//                        holder.basket_guest_all_score.setText(scoreguest + "");
//                        holder.basket_home_all_score.setText(scorehome + "");
////                        startAnima(childredata, score, holder);
/*
                        if (childredata.isHomeAnim()) {
                            scoreAnimation(holder.basket_home_all_score, childredata.getMatchScore().getHomeScore() + ""); //启动动画 客队
                            childredata.setIsHomeAnim(false);
                        }
//                        else{
////                            holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");
//                        }
                        if (childredata.isGuestAnim()) {
                            scoreAnimation(holder.basket_guest_all_score, childredata.getMatchScore().getGuestScore() + ""); //启动动画 主队
                            childredata.setIsGuestAnim(false);
                        }*/
//                        else{
////                            holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
//                        }

                        holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.VISIBLE);
                        if (childredata.getSection() == 2) {
                            holder.st_time.setText(R.string.basket_2nd_half);
                            holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuest3() + ":" + childredata.getMatchScore().getHome3() + ")");
                        } else {
                            holder.st_time.setText(R.string.basket_3rd);
                            holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuest3() + ":" + childredata.getMatchScore().getHome3() + ")");
                        }

//                    holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuest3() + ":" + childredata.getMatchScore().getHome3() + ")");
//                    holder.st_time.setText("3rd");
                        holder.ongoing_time.setVisibility(View.VISIBLE);
                        holder.backetball_apos.setVisibility(View.VISIBLE);
                    } else {
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");

                        holder.basket_half_score.setText("");
//                        holder.basket_half_score.setVisibility(View.GONE);
                        holder.ongoing_time.setVisibility(View.GONE);
                        holder.backetball_apos.setVisibility(View.GONE);
                    }

//					holder.backetball_apos.setVisibility(View.VISIBLE);
                    settingdata(holder, 3);
                    break;
                case 4: //四节

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

//					holder.basket_all_score.setText(scoreguest + ":" + scorehome);
//					holder.basket_all_score.setTextColor(Color.BLUE);
                    holder.basket_score.setText(":");
                    holder.basket_no_start.setVisibility(View.GONE);
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (childredata.getMatchScore() != null) {

                        holder.score_total.setText((scorehomehalf + scoreguesthalf) + "/" + (scorehome + scoreguest));
//                    holder.score_differ.setText(Math.abs(scorehome - scoreguest) + "");
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

//                        holder.basket_guest_all_score.setText(scoreguest + "");
//                        holder.basket_home_all_score.setText(scorehome + "");
////                        startAnima(childredata, score, holder);
/*
                        if (childredata.isHomeAnim()) {
                            scoreAnimation(holder.basket_home_all_score, childredata.getMatchScore().getHomeScore() + ""); //启动动画 客队
                            childredata.setIsHomeAnim(false);
                        }
//                        else{
////                            holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");
//                        }
                        if (childredata.isGuestAnim()) {
                            scoreAnimation(holder.basket_guest_all_score, childredata.getMatchScore().getGuestScore() + ""); //启动动画 主队
                            childredata.setIsGuestAnim(false);
                        }*/
//                        else{
////                            holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
//                        }

                        holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.VISIBLE);
                        holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuest4() + ":" + childredata.getMatchScore().getHome4() + ")");

                        if (childredata.getSection() == 2) {
                            holder.st_time.setText(R.string.basket_2nd_half);
                            holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuest3() + ":" + childredata.getMatchScore().getHome3() + ")");
                        } else {
                            holder.st_time.setText(R.string.basket_4th);
                            holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuest4() + ":" + childredata.getMatchScore().getHome4() + ")");
                        }
//                    holder.st_time.setText("4th");
                        holder.ongoing_time.setVisibility(View.VISIBLE);
                        holder.backetball_apos.setVisibility(View.VISIBLE);
                    } else {
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");

                        holder.basket_half_score.setText("");
//                        holder.basket_half_score.setVisibility(View.GONE);
                        holder.ongoing_time.setVisibility(View.GONE);
                        holder.backetball_apos.setVisibility(View.GONE);
                    }

//					holder.backetball_apos.setVisibility(View.VISIBLE);
                    settingdata(holder, 4);
                    break;
                case 5: //加时1

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

//					holder.basket_all_score.setText(scoreguest + ":" + scorehome);
//					holder.basket_all_score.setTextColor(Color.BLUE);
                    holder.basket_score.setText(":");
                    holder.basket_no_start.setVisibility(View.GONE);
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (childredata.getMatchScore() != null) {

                        holder.score_total.setText((scorehomehalf + scoreguesthalf) + "/" + (scorehome + scoreguest));
//                    holder.score_differ.setText(Math.abs(scorehome - scoreguest) + "");
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

//                        holder.basket_guest_all_score.setText(scoreguest + "");
//                        holder.basket_home_all_score.setText(scorehome + "");
////                        startAnima(childredata, score, holder);

                /*        if (childredata.isHomeAnim()) {
                            scoreAnimation(holder.basket_home_all_score, childredata.getMatchScore().getHomeScore() + ""); //启动动画 客队
                            childredata.setIsHomeAnim(false);
                        }
//                        else{
////                            holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");
//                        }
                        if (childredata.isGuestAnim()) {
                            scoreAnimation(holder.basket_guest_all_score, childredata.getMatchScore().getGuestScore() + ""); //启动动画 主队
                            childredata.setIsGuestAnim(false);
                        }*/
//                        else{
////                            holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
//                        }

                        holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.VISIBLE);
                        holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuestOt1() + ":" + childredata.getMatchScore().getHomeOt1() + ")");
                        holder.st_time.setText(R.string.basket_OT1);
                        holder.ongoing_time.setVisibility(View.VISIBLE);
                        holder.backetball_apos.setVisibility(View.VISIBLE);
                    } else {
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");

                        holder.basket_half_score.setText("");
//                        holder.basket_half_score.setVisibility(View.GONE);
                        holder.ongoing_time.setVisibility(View.GONE);
                        holder.backetball_apos.setVisibility(View.GONE);
                    }

//					holder.backetball_apos.setVisibility(View.VISIBLE);
                    settingdata(holder, 5);
                    break;
                case 6: //加时2

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

//					holder.basket_all_score.setText(scoreguest + ":" + scorehome);
//					holder.basket_all_score.setTextColor(Color.BLUE);
                    holder.basket_score.setText(":");
                    holder.basket_no_start.setVisibility(View.GONE);
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (childredata.getMatchScore() != null) {

                        holder.score_total.setText((scorehomehalf + scoreguesthalf) + "/" + (scorehome + scoreguest));
//                    holder.score_differ.setText(Math.abs(scorehome - scoreguest) + "");
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

//                        holder.basket_guest_all_score.setText(scoreguest + "");
//                        holder.basket_home_all_score.setText(scorehome + "");
////                        startAnima(childredata, score, holder);

                  /*      if (childredata.isHomeAnim()) {
                            scoreAnimation(holder.basket_home_all_score, childredata.getMatchScore().getHomeScore() + ""); //启动动画 客队
                            childredata.setIsHomeAnim(false);
                        }
//                        else{
////                            holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");
//                        }
                        if (childredata.isGuestAnim()) {
                            scoreAnimation(holder.basket_guest_all_score, childredata.getMatchScore().getGuestScore() + ""); //启动动画 主队
                            childredata.setIsGuestAnim(false);
                        }*/
//                        else{
////                            holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
//                        }

                        holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.VISIBLE);
                        holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuestOt2() + ":" + childredata.getMatchScore().getHomeOt2() + ")");
                        holder.st_time.setText(R.string.basket_OT2);
                        holder.ongoing_time.setVisibility(View.VISIBLE);
                        holder.backetball_apos.setVisibility(View.VISIBLE);
                    } else {
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");

                        holder.basket_half_score.setText("");
//                        holder.basket_half_score.setVisibility(View.GONE);
                        holder.ongoing_time.setVisibility(View.GONE);
                        holder.backetball_apos.setVisibility(View.GONE);
                    }

//					holder.backetball_apos.setVisibility(View.VISIBLE);
                    settingdata(holder, 6);
                    break;
                case 7: //加时3

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

//					holder.basket_all_score.setText(scoreguest + ":" + scorehome);
//					holder.basket_all_score.setTextColor(Color.BLUE);
                    holder.basket_score.setText(":");
                    holder.basket_no_start.setVisibility(View.GONE);
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (childredata.getMatchScore() != null) {

                        holder.score_total.setText((scorehomehalf + scoreguesthalf) + "/" + (scorehome + scoreguest));
//                    holder.score_differ.setText(Math.abs(scorehome - scoreguest) + "");
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

//                        holder.basket_guest_all_score.setText(scoreguest + "");
//                        holder.basket_home_all_score.setText(scorehome + "");
////                        startAnima(childredata, score, holder);
/*
                        if (childredata.isHomeAnim()) {
                            scoreAnimation(holder.basket_home_all_score, childredata.getMatchScore().getHomeScore() + ""); //启动动画 客队
                            childredata.setIsHomeAnim(false);
                        }
//                        else{
////                            holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");
//                        }
                        if (childredata.isGuestAnim()) {
                            scoreAnimation(holder.basket_guest_all_score, childredata.getMatchScore().getGuestScore() + ""); //启动动画 主队
                            childredata.setIsGuestAnim(false);
                        }*/
//                        else{
////                            holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
//                        }

                        holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.VISIBLE);
                        holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuestOt3() + ":" + childredata.getMatchScore().getHomeOt3() + ")");
                        holder.st_time.setText(R.string.basket_OT3);
                        holder.ongoing_time.setVisibility(View.VISIBLE);
                        holder.backetball_apos.setVisibility(View.VISIBLE);
                    } else {
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");

                        holder.basket_half_score.setText("");
//                        holder.basket_half_score.setVisibility(View.GONE);
                        holder.ongoing_time.setVisibility(View.GONE);
                        holder.backetball_apos.setVisibility(View.GONE);
                    }

//					holder.backetball_apos.setVisibility(View.VISIBLE);
                    settingdata(holder, 7);
                    break;
                case 50: //中场

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

//					holder.basket_all_score.setText(scoreguest + ":" + scorehome);
//					holder.basket_all_score.setTextColor(Color.BLUE);
                    holder.basket_score.setText(":");
                    holder.basket_no_start.setVisibility(View.GONE);
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (childredata.getMatchScore() != null) {

                        holder.score_total.setText((scorehomehalf + scoreguesthalf) + "/" + (scorehome + scoreguest));
//                    holder.score_differ.setText(Math.abs(scorehome - scoreguest) + "");
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

                        holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.INVISIBLE);
//					holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuest2() + ":" + childredata.getMatchScore().getHome2() + ")");
                        holder.st_time.setText(R.string.basket_half_time);

                    } else {
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");
                        holder.basket_half_score.setVisibility(View.GONE);
                    }

                    holder.ongoing_time.setVisibility(View.GONE);
//					holder.backetball_apos.setVisibility(View.GONE);
                    settingdata(holder, 50);
                    break;
                case -1: //完场

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

                    //当借口无数据时 显示“--”
                    if (childredata.getMatchScore() != null) {
                        holder.score_total.setText((scorehomehalf + scoreguesthalf) + "/" + (scorehome + scoreguest));
//                        holder.score_differ.setText(Math.abs(scorehome - scoreguest) + "");
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

                        holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");
                    } else {
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");
                    }

//					holder.basket_all_score.setText(scoreguest + ":" + scorehome);
//					holder.basket_all_score.setTextColor(Color.RED);
                    holder.basket_score.setText(":");
                    holder.basket_no_start.setVisibility(View.GONE);
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_over_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_over_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_over_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    holder.basket_half_score.setVisibility(View.INVISIBLE);
//					holder.basket_half_score.setText("(" + childredata.getMatchScore().getGuest4() + ":" + childredata.getMatchScore().getHome4() + ")");
//					holder.st_time.setVisibility(View.GONE);
                    holder.st_time.setText("");
                    holder.ongoing_time.setVisibility(View.GONE);
//					holder.backetball_apos.setVisibility(View.GONE);
                    settingdata(holder, -1);
                    break;
                case -2: //待定

                    holder.backetball_differ.setVisibility(View.INVISIBLE);
                    holder.backetball_total.setVisibility(View.INVISIBLE);
                    holder.score_total.setVisibility(View.INVISIBLE);
                    holder.score_differ.setVisibility(View.INVISIBLE);

//					holder.basket_all_score.setText("VS");
//					holder.basket_all_score.setTextColor(Color.BLACK);
                    holder.basket_score.setText("");
                    holder.basket_no_start.setVisibility(View.VISIBLE);
//                    holder.basket_score.setBackgroundColor(Color.WHITE);// -------设置背景色 (防止背景复用比分)-------
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_vs));
                    holder.basket_guest_all_score.setText("");
                    holder.basket_home_all_score.setText("");
                    holder.basket_guest_all_score.setVisibility(View.GONE);
                    holder.basket_home_all_score.setVisibility(View.GONE);
//					holder.basket_guest_all_score.setText(scoreguest);
//					holder.basket_home_all_score.setText(scorehome);
//					holder.basket_guest_all_score.setTextColor(Color.RED);
//					holder.basket_home_all_score.setTextColor(Color.RED);

                    holder.basket_half_score.setVisibility(View.INVISIBLE);
                    holder.st_time.setText(R.string.basket_undetermined);
                    holder.ongoing_time.setVisibility(View.GONE);
//					holder.backetball_apos.setVisibility(View.GONE);
                    settingdata(holder, -2);
                    break;
                case -3: //中断

                    holder.backetball_differ.setVisibility(View.INVISIBLE);
                    holder.backetball_total.setVisibility(View.INVISIBLE);
                    holder.ongoing_time.setVisibility(View.GONE);
                    holder.basket_score.setText("");
                    holder.basket_no_start.setVisibility(View.GONE);
//					holder.backetball_apos.setVisibility(View.GONE);
                    holder.basket_half_score.setVisibility(View.INVISIBLE);
                    holder.basket_guest_all_score.setText(childredata.getMatchScore().getGuestScore() + "");
                    holder.basket_home_all_score.setText(childredata.getMatchScore().getHomeScore() + "");
                    //TODO--------********************************
                    holder.st_time.setText(R.string.basket_interrupt);
                    settingdata(holder, -3);
                    break;
                case -4: //取消

                    holder.backetball_differ.setVisibility(View.INVISIBLE);
                    holder.backetball_total.setVisibility(View.INVISIBLE);
                    holder.score_total.setVisibility(View.INVISIBLE);
                    holder.score_differ.setVisibility(View.INVISIBLE);

//					holder.basket_all_score.setText("VS");
//					holder.basket_all_score.setTextColor(Color.BLACK);
                    holder.basket_score.setText("");
                    holder.basket_no_start.setVisibility(View.VISIBLE);
//                    holder.basket_score.setBackgroundColor(Color.WHITE);// -------设置背景色 (防止背景复用比分)-------
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_vs));
                    holder.basket_guest_all_score.setText("");
                    holder.basket_home_all_score.setText("");
                    holder.basket_guest_all_score.setVisibility(View.GONE);
                    holder.basket_home_all_score.setVisibility(View.GONE);

                    holder.basket_half_score.setVisibility(View.INVISIBLE);
                    holder.st_time.setText(R.string.basket_cancel);
                    holder.ongoing_time.setVisibility(View.GONE);
//					holder.backetball_apos.setVisibility(View.GONE);
                    settingdata(holder, -4);
                    break;
                case -5: //推迟

                    holder.backetball_differ.setVisibility(View.INVISIBLE);
                    holder.backetball_total.setVisibility(View.INVISIBLE);
                    holder.score_total.setVisibility(View.INVISIBLE);
                    holder.score_differ.setVisibility(View.INVISIBLE);

//					holder.basket_all_score.setText("VS");
//					holder.basket_all_score.setTextColor(Color.BLACK);
                    holder.basket_score.setText("");
                    holder.basket_no_start.setVisibility(View.VISIBLE);
//                    holder.basket_score.setBackgroundColor(Color.WHITE);// -------设置背景色 (防止背景复用比分)-------
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_vs));
                    holder.basket_guest_all_score.setText("");
                    holder.basket_home_all_score.setText("");
                    holder.basket_guest_all_score.setVisibility(View.GONE);
                    holder.basket_home_all_score.setVisibility(View.GONE);

                    holder.basket_half_score.setVisibility(View.INVISIBLE);
                    holder.st_time.setText(R.string.basket_postpone);
                    holder.ongoing_time.setVisibility(View.GONE);
//					holder.backetball_apos.setVisibility(View.GONE);
                    settingdata(holder, -5);
                    break;
                default:
                    break;
            }
        }

        if (score > 0 && score < 8 && childredata.getMatchScore() != null && childredata.getMatchScore().getRemainTime() != null) {// 显示秒的闪烁
            holder.backetball_apos.setText("\'");
            holder.backetball_apos.setVisibility(View.VISIBLE);

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
                    holder.backetball_apos.startAnimation(anim2);
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
                    holder.backetball_apos.startAnimation(anim1);
                }
            });
            holder.backetball_apos.startAnimation(anim1);
            // }
        } else {
            holder.backetball_apos.setText("");
            holder.backetball_apos.setVisibility(View.VISIBLE);
//			holder.backetball_apos.cancelAnimation(R.id.backetball_apos);
        }


    }


    //设置界面score
    public void settingdata(ImmediaViewHolder holder, int score) {

        boolean fullscore = PreferenceUtil.getBoolean(MyConstants.HALF_FULL_SCORE, true); //半全场比分
        boolean differscore = PreferenceUtil.getBoolean(MyConstants.SCORE_DIFFERENCE, true);//总分差
        boolean singscore = PreferenceUtil.getBoolean(MyConstants.SINGLE_SCORE, true); //单节比分
        boolean ranking = PreferenceUtil.getBoolean(MyConstants.HOST_RANKING, true);//排名

        //Integer score = Integer.parseInt(data.getMatchStatus());

        //0:未开赛,1:一节,2:二节,5:1'OT，以此类推 -1:完场,-2:待定,-3:中断,-4:取消,-5:推迟,50中场
        if (fullscore) {

            if (score == 0 || score == -2 || score == -3 || score == -4 || score == -5) {
//                holder.basket_guest_all_score.setVisibility(View.GONE);
//                holder.basket_score.setVisibility(View.VISIBLE);
//                holder.basket_home_all_score.setVisibility(View.GONE);
                holder.backetball_total.setVisibility(View.GONE);
                holder.score_total.setVisibility(View.GONE);

            } else {
//                holder.basket_guest_all_score.setVisibility(View.VISIBLE);
//                holder.basket_score.setVisibility(View.VISIBLE);
//                holder.basket_home_all_score.setVisibility(View.VISIBLE);
                holder.backetball_total.setVisibility(View.VISIBLE);
                holder.score_total.setVisibility(View.VISIBLE);
            }
        } else {
//            holder.basket_guest_all_score.setVisibility(View.INVISIBLE);
//            holder.basket_score.setVisibility(View.INVISIBLE);
//            holder.basket_home_all_score.setVisibility(View.INVISIBLE);
            holder.backetball_total.setVisibility(View.GONE);
            holder.score_total.setVisibility(View.GONE);
        }
        if (differscore) {

            if (score == 0 || score == -2 || score == -3 || score == -4 || score == -5) {
                holder.backetball_differ.setVisibility(View.INVISIBLE);
                holder.score_differ.setVisibility(View.INVISIBLE);
            } else {
                holder.backetball_differ.setVisibility(View.VISIBLE);
                holder.score_differ.setVisibility(View.VISIBLE);
            }
        } else {
            holder.backetball_differ.setVisibility(View.INVISIBLE);
            holder.score_differ.setVisibility(View.INVISIBLE);
        }
        if (singscore) {
            if (score == 0 || score == -2 || score == -3 || score == -4 || score == -5 || score == -1 || score == 50) {
                holder.basket_half_score.setVisibility(View.INVISIBLE);
            } else {
                holder.basket_half_score.setVisibility(View.VISIBLE);
            }
        } else {
            holder.basket_half_score.setVisibility(View.INVISIBLE);
        }
        if (ranking) {
            holder.guest_Ranking.setVisibility(View.VISIBLE);
            holder.home_Ranking.setVisibility(View.VISIBLE);
        } else {
            holder.guest_Ranking.setVisibility(View.INVISIBLE);
            holder.home_Ranking.setVisibility(View.INVISIBLE);
        }



    }

    @Override
    public int getItemCount() {
        return mMatchdata.size();
    }


}

