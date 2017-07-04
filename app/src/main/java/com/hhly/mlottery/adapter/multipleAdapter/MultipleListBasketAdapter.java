package com.hhly.mlottery.adapter.multipleAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.BasketAllOddBean;
import com.hhly.mlottery.bean.basket.BasketMatchBean;
import com.hhly.mlottery.bean.basket.BasketOddBean;
import com.hhly.mlottery.frame.basketballframe.MyRotateAnimation;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by yixq on 2017/1/5.
 * mail：yixq@13322.com
 * describe: 多屏动画篮球列表Adapter
 */

public class MultipleListBasketAdapter extends BaseQuickAdapter<BasketMatchBean> {

    private Context mContext;
    private List<BasketMatchBean> mData;



    public MultipleListBasketAdapter(Context context , List<BasketMatchBean> data){
        super(R.layout.multiple_basket_item, data);
        this.mContext = context;
        this.mData = data;
    }

//    public void setData(List<BasketMatchBean> matchdata) {
//
////        if (matchdata != null) {
////            int previousSize = this.mData.size();
////            this.mData.clear();
////            notifyItemRangeRemoved(0, previousSize);
////            this.mData.addAll(matchdata);
////            notifyItemRangeInserted(0, this.mData.size());
////        }
////        this.mData = matchdata;
//    }

    /**
     * 赛事item click
     */
    private MultipleRecyclerViewBasketItemClickListener mOnItemClickListener = null;

    public void setmOnItemClickListener(MultipleRecyclerViewBasketItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    public interface MultipleRecyclerViewBasketItemClickListener {
        void onItemClick(View view, String data, int pos, BasketMatchBean matchData);
    }


    @Override
    public int getItemViewType(int position) {
        if (mData == null && mData.size() == 0) {
            return super.getItemViewType(position);
        }

        return mData.size();

    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        BaseViewHolder holder = null;

        //item详情
        view = LayoutInflater.from(mContext).inflate(R.layout.multiple_basket_item, parent, false);
        holder = new MultipleListBasketAdapter.ViewHolderItem(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int positions) {

        if (mData == null) {
            return;
        }

        //赛事
        final BasketMatchBean matchData = mData.get(positions);

        holder.itemView.setTag(matchData.getThirdId());

        MultipleListBasketAdapter.ViewHolderItem viewHolderItem = (MultipleListBasketAdapter.ViewHolderItem) holder;

        if (matchData.isBasketChicks()) {
            viewHolderItem.multipleItemll.setBackgroundResource(R.color.multiple_item_bg_color);
        }else{
            viewHolderItem.multipleItemll.setBackgroundResource(R.color.white);
        }

        if (mOnItemClickListener != null) {
            //将创建的View注册点击事件
            viewHolderItem.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, (String) v.getTag(), positions , matchData);
                }
            });
        }

        setItemData(viewHolderItem , mData.get(positions));

    }

    /**
     * 设置比分赔率数据
     */
    private Map<String, BasketAllOddBean> MatchOdds;
    private void setItemData(final MultipleListBasketAdapter.ViewHolderItem holder, BasketMatchBean data) {

        holder.basket_guest_all_score.setTag(data.getThirdId());
        holder.basket_home_all_score.setTag(data.getThirdId());


        //主队url
        final String homelogourl = data.getHomeLogoUrl();  //"http://pic.13322.com/basketball/team/135_135/29.png"
        //客队url
        final String guestlogourl = data.getGuestLogoUrl();

        /**
         * 设置tag 、默认图片
         */
        if (mContext != null){
            ImageLoader.load(mContext, homelogourl, R.mipmap.basket_default).into(holder.home_icon);
            ImageLoader.load(mContext, guestlogourl, R.mipmap.basket_default).into(holder.guest_icon);
        }


        //赔率设置
        MatchOdds = data.getMatchOdds();
        if (MatchOdds != null) {

            boolean asize = PreferenceUtil.getBoolean(MyConstants.BASKETBALL_rbSizeBall, false); //大小球
            boolean eur = PreferenceUtil.getBoolean(MyConstants.BASKETBALL_RBOCOMPENSATE, false);//欧赔
            boolean alet = PreferenceUtil.getBoolean(MyConstants.BASKETBALL_RBSECOND, true); //亚盘
            boolean noshow = PreferenceUtil.getBoolean(MyConstants.BASKETBALL_RBNOTSHOW, false);//不显示

            /**
             * 大小球
             */
            if (asize) {

                if (MatchOdds.get("asiaSize") != null) {
                    BasketOddBean mAsize = MatchOdds.get("asiaSize").getCrown();

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
            }
            /**
             * 欧赔
             */
            if (eur) {
                if (MatchOdds.get("euro") != null) {
                    BasketOddBean mEur = MatchOdds.get("euro").getEuro();

                    if (mEur == null) {
                        holder.basket_leftOdds.setText("");
                        holder.basket_rightOdds.setText("");
                        holder.basket_handicap.setText("");
                        holder.tv_a.setText("");
                        holder.tv_b.setText("");
                    } else if (mEur != null) {

                        if (mEur.getLeftOdds() != null && mEur.getRightOdds() != null) {
                            if (mEur.getLeftOdds().equals("-") || mEur.getRightOdds().equals("-")) {
                                holder.basket_leftOdds.setText("");
                                holder.basket_rightOdds.setText("");
                                holder.basket_handicap.setText(R.string.basket_handicap_feng);
                                holder.tv_a.setText("");
                                holder.tv_b.setText("");
                            } else {
                                holder.basket_leftOdds.setText(mEur.getLeftOdds());
                                holder.basket_rightOdds.setText(mEur.getRightOdds());
                                holder.basket_handicap.setText(mContext.getString(R.string.basket_odds_eur));
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
            }
            /**
             * 亚盘
             */
            if (alet) {
                if (MatchOdds.get("asiaLet") != null) {
                    BasketOddBean mAlet = MatchOdds.get("asiaLet").getCrown();

                    if (mAlet == null) {
                        holder.basket_leftOdds.setText("");
                        holder.basket_rightOdds.setText("");
                        holder.basket_handicap.setText("");
                        holder.tv_a.setText("");
                        holder.tv_b.setText("");
                    } else if (mAlet != null) {

                        if (mAlet.getLeftOdds() != null && mAlet.getRightOdds() != null && mAlet.getHandicapValue() != null) {
                            if (mAlet.getLeftOdds().equals("-") || mAlet.getRightOdds().equals("-") || mAlet.getHandicapValue().equals("-")) {
                                holder.basket_leftOdds.setText("");
                                holder.basket_rightOdds.setText("");
                                holder.basket_handicap.setText(R.string.basket_handicap_feng);
                                holder.tv_a.setText("");
                                holder.tv_b.setText("");
                            } else {
                                holder.basket_leftOdds.setText(mAlet.getLeftOdds());
                                holder.basket_rightOdds.setText(mAlet.getRightOdds());

                                if (mAlet.getHandicapValue() != null && !mAlet.getHandicapValue().equals("")) {

                                    Double aletData = Double.parseDouble(mAlet.getHandicapValue()); // 转换为 int 型 不行(null)？？
                                    if (aletData > 0) {
                                        holder.basket_handicap.setText(mContext.getString(R.string.basket_odds_alet) + "-" + aletData + "");
                                    } else {
                                        holder.basket_handicap.setText(mContext.getString(R.string.basket_odds_alet) + "+" + Math.abs(aletData) + "");//绝对值
                                    }
                                }else{
                                    holder.basket_handicap.setText("--");//绝对值
                                }
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
            }
            /**
             * 不显示
             */
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
        if (data.getLeagueName() != null) {
            holder.matches_name.setText(data.getLeagueName());
            holder.matches_name.setTextColor(Color.parseColor(data.getLeagueColor()));
        }

        holder.game_time.setText(data.getTime());
        /**
         * 去除两端的空格，trim()；  防止-->  "XX   ..."情况
         */
        if (data.getHomeTeam() != null) {
            holder.home_name.setText(data.getHomeTeam().trim());
        } else {
            holder.home_name.setText("--");
        }
        if (data.getGuestTeam() != null) {
            holder.guest_name.setText(data.getGuestTeam().trim());
        } else {
            holder.guest_name.setText("--");
        }

        if (data.getGuestRanking().length() == 0) {
            holder.guest_ranking.setText("");
        } else {
            holder.guest_ranking.setText("[" + data.getGuestRanking() + "]");
        }
        if (data.getHomeRanking().length() == 0) {
            holder.home_ranking.setText("");
        } else {
            holder.home_ranking.setText("[" + data.getHomeRanking() + "]");
        }

        Integer score = Integer.parseInt(data.getMatchStatus());

        if (data.getMatchStatus() != null) {

            int scorehome;
            int scoreguest;
            int scorehomehalf;
            int scoreguesthalf;

            if (data.getMatchScore() != null) {
                if (data.getMatchScore().getRemainTime() == null) {
                    holder.ongoing_time.setText("");
                } else {
                    holder.ongoing_time.setText(data.getMatchScore().getRemainTime());
                }
            } else {
                holder.ongoing_time.setText("");
            }

            /**0:未开赛,1:一节,2:二节,5:1'OT，以此类推 -1:完场,-2:待定,-3:中断,-4:取消,-5:推迟,50中场*/
            if (score == 0 || score == -2 || score == -3 || score == -4 || score == -5) {

                scorehome = 0;
                scoreguest = 0;
                scorehomehalf = 0;
                scoreguesthalf = 0;
            } else {

                if (data.getMatchScore() == null) {
                    scorehome = 0;
                    scoreguest = 0;
                    scorehomehalf = 0;
                    scoreguesthalf = 0;
                } else {

                    scorehome = data.getMatchScore().getHomeScore();//主队分数
                    scoreguest = data.getMatchScore().getGuestScore();//客队分数

                    if (data.getSection() == 2) { // 只有两节比赛的情况
                        scorehomehalf = data.getMatchScore().getHome1();//主队半场得分
                        scoreguesthalf = data.getMatchScore().getGuest1();//客队半场得分
                    } else {
                        scorehomehalf = data.getMatchScore().getHome1() + data.getMatchScore().getHome2();//主队半场得分
                        scoreguesthalf = data.getMatchScore().getGuest1() + data.getMatchScore().getGuest2();//客队半场得分
                    }
                }
            }

            switch (score) {
                case 0: //未开赛

                    holder.backetball_differ.setVisibility(View.INVISIBLE);
                    holder.backetball_total.setVisibility(View.INVISIBLE);
                    holder.score_total.setVisibility(View.INVISIBLE);
                    holder.score_differ.setVisibility(View.INVISIBLE);
                    holder.basket_score.setText(R.string.basket_VS);
//                    holder.basket_score.setBackgroundColor(Color.WHITE);// -------设置背景色 (防止背景复用比分)-------
                    holder.basket_guest_all_score.setText("");
                    holder.basket_home_all_score.setText("");
                    holder.basket_guest_all_score.setVisibility(View.GONE);
                    holder.basket_home_all_score.setVisibility(View.GONE);
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

                    holder.basket_score.setText(":");
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));//#0085E1
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (data.getMatchScore() != null) {

                        holder.score_total.setText(scorehome + scoreguest + "");
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

                        if (data.isHomeAnim()) {
                            scoreAnimation(holder.basket_home_all_score, data.getMatchScore().getHomeScore() + ""); //启动动画 客队
                            data.setIsHomeAnim(false);
                        }
                        if (data.isGuestAnim()) {
                            scoreAnimation(holder.basket_guest_all_score, data.getMatchScore().getGuestScore() + ""); //启动动画 主队
                            data.setIsGuestAnim(false);
                        }

                        holder.basket_guest_all_score.setText(data.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(data.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.VISIBLE);
                        holder.basket_half_score.setText("(" + data.getMatchScore().getGuest1() + ":" + data.getMatchScore().getHome1() + ")");
                        if (data.getSection() == 2) {
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
                        holder.ongoing_time.setVisibility(View.GONE);
                        holder.backetball_apos.setVisibility(View.GONE);
                    }
                    settingdata(holder, 1);
                    break;
                case 2: //二节

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

                    holder.basket_score.setText(":");
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (data.getMatchScore() != null) {

                        holder.score_total.setText(scorehome + scoreguest + "");
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

                        if (data.isHomeAnim()) {
                            scoreAnimation(holder.basket_home_all_score, data.getMatchScore().getHomeScore() + ""); //启动动画 客队
                            data.setIsHomeAnim(false);
                        }
                        if (data.isGuestAnim()) {
                            scoreAnimation(holder.basket_guest_all_score, data.getMatchScore().getGuestScore() + ""); //启动动画 主队
                            data.setIsGuestAnim(false);
                        }

                        holder.basket_guest_all_score.setText(data.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(data.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.VISIBLE);
                        if (data.getSection() == 2) {
                            holder.st_time.setText(R.string.basket_1st_half);
                        } else {
                            holder.st_time.setText(R.string.basket_2nd);
                        }
                        holder.basket_half_score.setText("(" + data.getMatchScore().getGuest2() + ":" + data.getMatchScore().getHome2() + ")");

                        holder.ongoing_time.setVisibility(View.VISIBLE);
                        holder.backetball_apos.setVisibility(View.VISIBLE);

                    } else {
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");

                        holder.basket_half_score.setText("");
                        holder.ongoing_time.setVisibility(View.GONE);
                        holder.backetball_apos.setVisibility(View.GONE);
                    }

                    settingdata(holder, 2);
                    break;
                case 3: //三节

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

                    holder.basket_score.setText(":");
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (data.getMatchScore() != null) {

                        holder.score_total.setText((scorehomehalf + scoreguesthalf) + "/" + (scorehome + scoreguest));
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }


                        if (data.isHomeAnim()) {
                            scoreAnimation(holder.basket_home_all_score, data.getMatchScore().getHomeScore() + ""); //启动动画 客队
                            data.setIsHomeAnim(false);
                        }
                        if (data.isGuestAnim()) {
                            scoreAnimation(holder.basket_guest_all_score, data.getMatchScore().getGuestScore() + ""); //启动动画 主队
                            data.setIsGuestAnim(false);
                        }

                        holder.basket_guest_all_score.setText(data.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(data.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.VISIBLE);
                        if (data.getSection() == 2) {
                            holder.st_time.setText(R.string.basket_2nd_half);
                            holder.basket_half_score.setText("(" + data.getMatchScore().getGuest3() + ":" + data.getMatchScore().getHome3() + ")");
                        } else {
                            holder.st_time.setText(R.string.basket_3rd);
                            holder.basket_half_score.setText("(" + data.getMatchScore().getGuest3() + ":" + data.getMatchScore().getHome3() + ")");
                        }

                        holder.ongoing_time.setVisibility(View.VISIBLE);
                        holder.backetball_apos.setVisibility(View.VISIBLE);
                    } else {
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");

                        holder.basket_half_score.setText("");
                        holder.ongoing_time.setVisibility(View.GONE);
                        holder.backetball_apos.setVisibility(View.GONE);
                    }

                    settingdata(holder, 3);
                    break;
                case 4: //四节

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

                    holder.basket_score.setText(":");
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (data.getMatchScore() != null) {

                        holder.score_total.setText((scorehomehalf + scoreguesthalf) + "/" + (scorehome + scoreguest));
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

                        if (data.isHomeAnim()) {
                            scoreAnimation(holder.basket_home_all_score, data.getMatchScore().getHomeScore() + ""); //启动动画 客队
                            data.setIsHomeAnim(false);
                        }
                        if (data.isGuestAnim()) {
                            scoreAnimation(holder.basket_guest_all_score, data.getMatchScore().getGuestScore() + ""); //启动动画 主队
                            data.setIsGuestAnim(false);
                        }

                        holder.basket_guest_all_score.setText(data.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(data.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.VISIBLE);
                        holder.basket_half_score.setText("(" + data.getMatchScore().getGuest4() + ":" + data.getMatchScore().getHome4() + ")");

                        if (data.getSection() == 2) {
                            holder.st_time.setText(R.string.basket_2nd_half);
                            holder.basket_half_score.setText("(" + data.getMatchScore().getGuest3() + ":" + data.getMatchScore().getHome3() + ")");
                        } else {
                            holder.st_time.setText(R.string.basket_4th);
                            holder.basket_half_score.setText("(" + data.getMatchScore().getGuest4() + ":" + data.getMatchScore().getHome4() + ")");
                        }
                        holder.ongoing_time.setVisibility(View.VISIBLE);
                        holder.backetball_apos.setVisibility(View.VISIBLE);
                    } else {
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");

                        holder.basket_half_score.setText("");
                        holder.ongoing_time.setVisibility(View.GONE);
                        holder.backetball_apos.setVisibility(View.GONE);
                    }

                    settingdata(holder, 4);
                    break;
                case 5: //加时1

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

                    holder.basket_score.setText(":");
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (data.getMatchScore() != null) {

                        holder.score_total.setText((scorehomehalf + scoreguesthalf) + "/" + (scorehome + scoreguest));
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }


                        if (data.isHomeAnim()) {
                            scoreAnimation(holder.basket_home_all_score, data.getMatchScore().getHomeScore() + ""); //启动动画 客队
                            data.setIsHomeAnim(false);
                        }
                        if (data.isGuestAnim()) {
                            scoreAnimation(holder.basket_guest_all_score, data.getMatchScore().getGuestScore() + ""); //启动动画 主队
                            data.setIsGuestAnim(false);
                        }

                        holder.basket_guest_all_score.setText(data.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(data.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.VISIBLE);
                        holder.basket_half_score.setText("(" + data.getMatchScore().getGuestOt1() + ":" + data.getMatchScore().getHomeOt1() + ")");
                        holder.st_time.setText(R.string.basket_OT1);
                        holder.ongoing_time.setVisibility(View.VISIBLE);
                        holder.backetball_apos.setVisibility(View.VISIBLE);
                    } else {
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");

                        holder.basket_half_score.setText("");
                        holder.ongoing_time.setVisibility(View.GONE);
                        holder.backetball_apos.setVisibility(View.GONE);
                    }

                    settingdata(holder, 5);
                    break;
                case 6: //加时2

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

                    holder.basket_score.setText(":");
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (data.getMatchScore() != null) {

                        holder.score_total.setText((scorehomehalf + scoreguesthalf) + "/" + (scorehome + scoreguest));
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

                        if (data.isHomeAnim()) {
                            scoreAnimation(holder.basket_home_all_score, data.getMatchScore().getHomeScore() + ""); //启动动画 客队
                            data.setIsHomeAnim(false);
                        }
                        if (data.isGuestAnim()) {
                            scoreAnimation(holder.basket_guest_all_score, data.getMatchScore().getGuestScore() + ""); //启动动画 主队
                            data.setIsGuestAnim(false);
                        }

                        holder.basket_guest_all_score.setText(data.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(data.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.VISIBLE);
                        holder.basket_half_score.setText("(" + data.getMatchScore().getGuestOt2() + ":" + data.getMatchScore().getHomeOt2() + ")");
                        holder.st_time.setText(R.string.basket_OT2);
                        holder.ongoing_time.setVisibility(View.VISIBLE);
                        holder.backetball_apos.setVisibility(View.VISIBLE);
                    } else {
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");

                        holder.basket_half_score.setText("");
                        holder.ongoing_time.setVisibility(View.GONE);
                        holder.backetball_apos.setVisibility(View.GONE);
                    }

                    settingdata(holder, 6);
                    break;
                case 7: //加时3

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

                    holder.basket_score.setText(":");
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (data.getMatchScore() != null) {

                        holder.score_total.setText((scorehomehalf + scoreguesthalf) + "/" + (scorehome + scoreguest));
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }


                        if (data.isHomeAnim()) {
                            scoreAnimation(holder.basket_home_all_score, data.getMatchScore().getHomeScore() + ""); //启动动画 客队
                            data.setIsHomeAnim(false);
                        }
                        if (data.isGuestAnim()) {
                            scoreAnimation(holder.basket_guest_all_score, data.getMatchScore().getGuestScore() + ""); //启动动画 主队
                            data.setIsGuestAnim(false);
                        }

                        holder.basket_guest_all_score.setText(data.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(data.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.VISIBLE);
                        holder.basket_half_score.setText("(" + data.getMatchScore().getGuestOt3() + ":" + data.getMatchScore().getHomeOt3() + ")");
                        holder.st_time.setText(R.string.basket_OT3);
                        holder.ongoing_time.setVisibility(View.VISIBLE);
                        holder.backetball_apos.setVisibility(View.VISIBLE);
                    } else {
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");

                        holder.basket_half_score.setText("");
                        holder.ongoing_time.setVisibility(View.GONE);
                        holder.backetball_apos.setVisibility(View.GONE);
                    }

                    settingdata(holder, 7);
                    break;
                case 50: //中场

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

                    holder.basket_score.setText(":");
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    if (data.getMatchScore() != null) {

                        holder.score_total.setText((scorehomehalf + scoreguesthalf) + "/" + (scorehome + scoreguest));
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

                        holder.basket_guest_all_score.setText(data.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(data.getMatchScore().getHomeScore() + "");

                        holder.basket_half_score.setVisibility(View.INVISIBLE);
                        holder.st_time.setText(R.string.basket_half_time);

                    } else {
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");
                        holder.basket_half_score.setVisibility(View.GONE);
                    }

                    holder.ongoing_time.setVisibility(View.GONE);
                    settingdata(holder, 50);
                    break;
                case -1: //完场

                    holder.backetball_differ.setVisibility(View.VISIBLE);
                    holder.backetball_total.setVisibility(View.VISIBLE);
                    holder.score_total.setVisibility(View.VISIBLE);
                    holder.score_differ.setVisibility(View.VISIBLE);

                    //当借口无数据时 显示“--”
                    if (data.getMatchScore() != null) {
                        holder.score_total.setText((scorehomehalf + scoreguesthalf) + "/" + (scorehome + scoreguest));
                        if (scorehome - scoreguest > 0) {
                            holder.score_differ.setText("" + (scorehome - scoreguest)); // 不取绝对值
                        } else {
                            holder.score_differ.setText(scorehome - scoreguest + "");
                        }

                        holder.basket_guest_all_score.setText(data.getMatchScore().getGuestScore() + "");
                        holder.basket_home_all_score.setText(data.getMatchScore().getHomeScore() + "");
                    } else {
                        holder.score_total.setText("--");
                        holder.score_differ.setText("--");
                        holder.basket_guest_all_score.setText("--");
                        holder.basket_home_all_score.setText("--");
                    }
                    holder.basket_score.setText(":");
                    holder.basket_guest_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_over_score));
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_over_score));
                    holder.basket_home_all_score.setTextColor(mContext.getResources().getColor(R.color.basket_over_score));
                    holder.basket_guest_all_score.setVisibility(View.VISIBLE);
                    holder.basket_home_all_score.setVisibility(View.VISIBLE);

                    holder.basket_half_score.setVisibility(View.INVISIBLE);

                    holder.st_time.setText("");
                    holder.ongoing_time.setVisibility(View.GONE);

                    settingdata(holder, -1);
                    break;
                case -2: //待定

                    holder.backetball_differ.setVisibility(View.INVISIBLE);
                    holder.backetball_total.setVisibility(View.INVISIBLE);
                    holder.score_total.setVisibility(View.INVISIBLE);
                    holder.score_differ.setVisibility(View.INVISIBLE);

                    holder.basket_score.setText(R.string.basket_VS);

                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_vs));
                    holder.basket_guest_all_score.setText("");
                    holder.basket_home_all_score.setText("");
                    holder.basket_guest_all_score.setVisibility(View.GONE);
                    holder.basket_home_all_score.setVisibility(View.GONE);

                    holder.basket_half_score.setVisibility(View.INVISIBLE);
                    holder.st_time.setText(R.string.basket_undetermined);
                    holder.ongoing_time.setVisibility(View.GONE);
                    settingdata(holder, -2);
                    break;
                case -3: //中断

                    holder.backetball_differ.setVisibility(View.INVISIBLE);
                    holder.backetball_total.setVisibility(View.INVISIBLE);
                    holder.ongoing_time.setVisibility(View.GONE);
                    holder.basket_half_score.setVisibility(View.INVISIBLE);
                    holder.basket_guest_all_score.setText(data.getMatchScore().getGuestScore() + "");
                    holder.basket_home_all_score.setText(data.getMatchScore().getHomeScore() + "");
                    holder.st_time.setText(R.string.basket_interrupt);
                    settingdata(holder, -3);
                    break;
                case -4: //取消

                    holder.backetball_differ.setVisibility(View.INVISIBLE);
                    holder.backetball_total.setVisibility(View.INVISIBLE);
                    holder.score_total.setVisibility(View.INVISIBLE);
                    holder.score_differ.setVisibility(View.INVISIBLE);

                    holder.basket_score.setText(R.string.basket_VS);
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_vs));
                    holder.basket_guest_all_score.setText("");
                    holder.basket_home_all_score.setText("");
                    holder.basket_guest_all_score.setVisibility(View.GONE);
                    holder.basket_home_all_score.setVisibility(View.GONE);

                    holder.basket_half_score.setVisibility(View.INVISIBLE);
                    holder.st_time.setText(R.string.basket_cancel);
                    holder.ongoing_time.setVisibility(View.GONE);
                    settingdata(holder, -4);
                    break;
                case -5: //推迟

                    holder.backetball_differ.setVisibility(View.INVISIBLE);
                    holder.backetball_total.setVisibility(View.INVISIBLE);
                    holder.score_total.setVisibility(View.INVISIBLE);
                    holder.score_differ.setVisibility(View.INVISIBLE);

                    holder.basket_score.setText(R.string.basket_VS);
                    holder.basket_score.setTextColor(mContext.getResources().getColor(R.color.basket_vs));
                    holder.basket_guest_all_score.setText("");
                    holder.basket_home_all_score.setText("");
                    holder.basket_guest_all_score.setVisibility(View.GONE);
                    holder.basket_home_all_score.setVisibility(View.GONE);

                    holder.basket_half_score.setVisibility(View.INVISIBLE);
                    holder.st_time.setText(R.string.basket_postpone);
                    holder.ongoing_time.setVisibility(View.GONE);
                    settingdata(holder, -5);
                    break;
                default:
                    break;
            }
        }

        if (score > 0 && score < 8 && data.getMatchScore() != null && data.getMatchScore().getRemainTime() != null) {// 显示秒的闪烁
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
        } else {
            holder.backetball_apos.setText("");
            holder.backetball_apos.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 是否允许显示最新的数字。
     */
    private boolean enableRefresh;

    //比分动画
    public void scoreAnimation(final TextView scoreView, final String score) {
        //翻转动画 （RotateAnimation-->自定义X轴翻转）
        enableRefresh = true;
        MyRotateAnimation rotateAnim = null;
        float cX = scoreView.getWidth() / 2.0f;
        float cY = scoreView.getHeight() / 2.0f;

        rotateAnim = new MyRotateAnimation(cX, cY, MyRotateAnimation.ROTATE_DECREASE);
        rotateAnim.setInterpolatedTimeListener(new MyRotateAnimation.InterpolatedTimeListener() {
            @Override
            public void interpolatedTime(float interpolatedTime) {
                // 监听到翻转进度过半时，更新显示内容。
                if (enableRefresh && interpolatedTime > 0.5f) {
                    scoreView.setText(score);
                    enableRefresh = false;
                }
            }
        });
        rotateAnim.setFillAfter(true);
        scoreView.startAnimation(rotateAnim);
    }

    //设置界面score
    public void settingdata(MultipleListBasketAdapter.ViewHolderItem holder, int score) {

        /************************** 不删 *****************************/
//        boolean fullscore = PreferenceUtil.getBoolean(MyConstants.HALF_FULL_SCORE, true); //半全场比分
//        boolean differscore = PreferenceUtil.getBoolean(MyConstants.SCORE_DIFFERENCE, true);//总分差
//        boolean singscore = PreferenceUtil.getBoolean(MyConstants.SINGLE_SCORE, true); //单节比分
//        boolean ranking = PreferenceUtil.getBoolean(MyConstants.HOST_RANKING, true);//排名

        /** 设置比分 分差全部显示*/
        boolean fullscore = true; //半全场比分
        boolean differscore = true;//总分差
        boolean singscore = true; //单节比分
        boolean ranking = true;//排名


        /**0:未开赛,1:一节,2:二节,5:1'OT，以此类推 -1:完场,-2:待定,-3:中断,-4:取消,-5:推迟,50中场*/

        if (fullscore) {

            if (score == 0 || score == -2 || score == -3 || score == -4 || score == -5) {
                holder.backetball_total.setVisibility(View.GONE);
                holder.score_total.setVisibility(View.GONE);

            } else {
                holder.backetball_total.setVisibility(View.VISIBLE);
                holder.score_total.setVisibility(View.VISIBLE);
            }
        } else {
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
            holder.guest_ranking.setVisibility(View.VISIBLE);
            holder.home_ranking.setVisibility(View.VISIBLE);
        } else {
            holder.guest_ranking.setVisibility(View.INVISIBLE);
            holder.home_ranking.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        return super.getItemView(layoutResId, parent);
    }

    class ViewHolderItem extends BaseViewHolder {

        private final ImageView home_icon;
        private final ImageView guest_icon;
        private final TextView matches_name;
        private final TextView game_time;
        private final TextView st_time;
        private final TextView ongoing_time;
        private final TextView score_total;
        private final TextView score_differ;
        private final TextView home_name;
        private final TextView guest_name;
        private final TextView home_ranking;
        private final TextView guest_ranking;
        private final TextView basket_guest_all_score;
        private final TextView basket_home_all_score;
        private final TextView basket_score;
        private final TextView basket_leftOdds;
        private final TextView basket_rightOdds;
        private final TextView basket_half_score;
        private final TextView basket_handicap;
        private final TextView backetball_differ;
        private final TextView backetball_total;
        private final TextView tv_a;
        private final TextView tv_b;
        private final TextView backetball_apos;
        private final LinearLayout multipleItemll;

        public ViewHolderItem(View itemView) {
            super(itemView);

            home_icon = (ImageView) itemView.findViewById(R.id.home_icon);
            guest_icon = (ImageView) itemView.findViewById(R.id.guest_icon);

            itemView.findViewById(R.id.Iv_guangzhu).setVisibility(View.INVISIBLE);

            matches_name = (TextView) itemView.findViewById(R.id.backetball_matches_name);
            game_time = (TextView) itemView.findViewById(R.id.backetball_game_time);
            st_time = (TextView) itemView.findViewById(R.id.backetball_st_time);
            ongoing_time = (TextView) itemView.findViewById(R.id.backetball_ongoing_time);
            score_total = (TextView) itemView.findViewById(R.id.backetball_score_total);
            score_differ = (TextView) itemView.findViewById(R.id.backetball_score_differ);
            home_name = (TextView) itemView.findViewById(R.id.home_name);
            guest_name = (TextView) itemView.findViewById(R.id.guest_name);
            home_ranking = (TextView) itemView.findViewById(R.id.basket_home_Ranking);
            guest_ranking = (TextView) itemView.findViewById(R.id.basket_guest_Ranking);
            basket_guest_all_score = (TextView) itemView.findViewById(R.id.basket_guest_all_score);
            basket_score = (TextView) itemView.findViewById(R.id.basket_score);
            basket_home_all_score = (TextView) itemView.findViewById(R.id.basket_home_all_score);

            basket_half_score = (TextView) itemView.findViewById(R.id.basket_half_score);
            multipleItemll = (LinearLayout) itemView.findViewById(R.id.multiple_match_item);


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
    protected void convert(BaseViewHolder baseViewHolder, BasketMatchBean o) {

    }
}
