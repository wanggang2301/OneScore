package com.hhly.mlottery.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.BallMatchItemsBean;
import com.hhly.mlottery.bean.MatchOdd;
import com.hhly.mlottery.callback.FocusMatchClickListener;
import com.hhly.mlottery.callback.RecyclerViewItemClickListener;
import com.hhly.mlottery.frame.footballframe.FocusFragment;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.HandicapUtils;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;

import java.util.List;

/**
 * Created by yuely198 on 2017/3/23.
 */

public class FootballMatchSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<BallMatchItemsBean> datas;
    private Context mContext;
    private LayoutInflater mLayoutInflater;


    private int handicap = 0;


    private String teamLogoPre;

    private String teamLogoSuff;


    /**
     * 关注比赛
     */
    public FocusMatchClickListener mFocusMatchClickListener;



    public void setmFocusMatchClickListener(FocusMatchClickListener mFocusMatchClickListener) {
        this.mFocusMatchClickListener = mFocusMatchClickListener;
    }


    /**
     * 赛事item click
     */
    private RecyclerViewItemClickListener mOnItemClickListener = null;

    public void setmOnItemClickListener(RecyclerViewItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public FootballMatchSearchAdapter(Context context, List<BallMatchItemsBean> datas, String teamLogoPre, String teamLogoSuff) {
        mLayoutInflater = LayoutInflater.from(context);
        this.datas = datas;
        this.mContext = context;
        this.teamLogoPre = teamLogoPre;
        this.teamLogoSuff = teamLogoSuff;
       // datas.get(1).getGuestteam();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_football, parent, false);
        FootballMatchSearchAdapter.ImmediaViewHolder immediaViewHolder = new FootballMatchSearchAdapter.ImmediaViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, (String) v.getTag());
                }
            }
        });
        return immediaViewHolder;
    }
    public void clearData(){
        datas.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BallMatchItemsBean match = datas.get(position);

        holder.itemView.setTag(match.getThirdId());

        if (PreferenceUtil.getBoolean(MyConstants.RBSECOND, true)) {
            handicap = 1;
        } else if (PreferenceUtil.getBoolean(MyConstants.rbSizeBall, false)) {
            handicap = 2;
        } else if (PreferenceUtil.getBoolean(MyConstants.RBOCOMPENSATE, false)) {
            handicap = 3;
        } else if (PreferenceUtil.getBoolean(MyConstants.RBNOTSHOW, false)) {
            handicap = 4;
        } else {
            handicap = 1;
        }
        convert((FootballMatchSearchAdapter.ImmediaViewHolder) holder, match, handicap);
    }

    @Override
    public int getItemCount() {
        return datas.size();

    }

    public void updateDatas(List<BallMatchItemsBean> mMatch) {
        datas = mMatch;
    }


    /**
     * 数据ViewHolder
     */

    static class ImmediaViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView keeptime;
        TextView item_football_racename;
        TextView item_football_date;
        TextView item_football_time;
        TextView item_football_frequency;
        TextView item_football_home_yc;
        TextView item_football_home_rc;
        TextView item_football_guest_rc;
        TextView item_football_guest_yc;
        TextView item_football_hometeam;
        TextView item_football_guestteam;
        LinearLayout item_football_content_ll;
        TextView item_home_half_score;
        TextView item_home_full_score;
        TextView item_guest_half_score;
        TextView item_guest_full_score;
        //关注
        ImageView Iv_guangzhu;
        ImageView home_icon;
        ImageView guest_icon;

        LinearLayout ll_half_score;
        LinearLayout ll_all_score;
        LinearLayout ll_odds_content1;
        LinearLayout ll_odds_content2;
        TextView tv_odds_top1;
        TextView tv_odds_top2;
        TextView tv_odds_center1;
        TextView tv_odds_center2;
        TextView tv_odds_bottom1;
        TextView tv_odds_bottom2;

        View view_line;
        TextView tv_item_desc;

        public ImmediaViewHolder(final View itemView) {
            super(itemView);


            cardView = (CardView) itemView.findViewById(R.id.card_view);
            keeptime = (TextView) itemView.findViewById(R.id.keeptime);
            item_football_racename = (TextView) itemView.findViewById(R.id.item_football_racename);
            item_football_date = (TextView) itemView.findViewById(R.id.item_football_date);
            item_football_time = (TextView) itemView.findViewById(R.id.item_football_time);
            item_home_half_score = (TextView) itemView.findViewById(R.id.tv_home_half_score);
            item_home_full_score = (TextView) itemView.findViewById(R.id.tv_home_full_score);
            item_guest_half_score = (TextView) itemView.findViewById(R.id.tv_guest_half_score);
            item_guest_full_score = (TextView) itemView.findViewById(R.id.tv_guest_full_score);

            item_football_frequency = (TextView) itemView.findViewById(R.id.item_football_frequency);
            item_football_home_yc = (TextView) itemView.findViewById(R.id.item_football_home_yc);
            item_football_home_rc = (TextView) itemView.findViewById(R.id.item_football_home_rc);
            item_football_guest_rc = (TextView) itemView.findViewById(R.id.item_football_guest_rc);
            item_football_guest_yc = (TextView) itemView.findViewById(R.id.item_football_guest_yc);
            item_football_hometeam = (TextView) itemView.findViewById(R.id.item_football_hometeam);
            item_football_guestteam = (TextView) itemView.findViewById(R.id.item_football_guestteam);
            item_football_content_ll = (LinearLayout) itemView.findViewById(R.id.item_football_content_ll);

            Iv_guangzhu = (ImageView) itemView.findViewById(R.id.Iv_guangzhu);
            home_icon = (ImageView) itemView.findViewById(R.id.home_icon);
            guest_icon = (ImageView) itemView.findViewById(R.id.guest_icon);

            ll_half_score = (LinearLayout) itemView.findViewById(R.id.ll_half_score);
            ll_all_score = (LinearLayout) itemView.findViewById(R.id.ll_all_score);
            ll_odds_content1 = (LinearLayout) itemView.findViewById(R.id.ll_odds_content1);
            ll_odds_content2 = (LinearLayout) itemView.findViewById(R.id.ll_odds_content2);
            tv_odds_top1 = (TextView) itemView.findViewById(R.id.tv_odds_top1);
            tv_odds_top2 = (TextView) itemView.findViewById(R.id.tv_odds_top2);
            tv_odds_center1 = (TextView) itemView.findViewById(R.id.tv_odds_center1);
            tv_odds_center2 = (TextView) itemView.findViewById(R.id.tv_odds_center2);
            tv_odds_bottom1 = (TextView) itemView.findViewById(R.id.tv_odds_bottom1);
            tv_odds_bottom2 = (TextView) itemView.findViewById(R.id.tv_odds_bottom2);

            view_line = itemView.findViewById(R.id.view_line);
            tv_item_desc = (TextView) itemView.findViewById(R.id.tv_item_desc);
        }

    }

    private void convert(final FootballMatchSearchAdapter.ImmediaViewHolder holder, final BallMatchItemsBean match, int handicap) {

        if (match.getItemBackGroundColorId() == R.color.item_football_event_yellow) {
            holder.item_football_content_ll.setBackgroundResource(match.getItemBackGroundColorId());

        } else {
            holder.item_football_content_ll.setBackgroundResource(R.color.white);
        }

        if (match.getHomeHalfScore() == null) {
            match.setHomeHalfScore("0");
        }

        if (match.getGuestHalfScore() == null) {
            match.setGuestHalfScore("0");
        }

        holder.item_football_hometeam.setText(match.getHometeam());
        holder.item_football_guestteam.setText(match.getGuestteam());

        //主队url
        final String homelogourl = teamLogoPre + match.getHomeId() + teamLogoSuff;
        //客队url
        final String guestlogourl = teamLogoPre + match.getGuestId() + teamLogoSuff;

        ImageLoader.load(mContext, homelogourl, R.mipmap.score_default).into(holder.home_icon);
        ImageLoader.load(mContext, guestlogourl, R.mipmap.score_default).into(holder.guest_icon);

        holder.item_football_racename.setText(match.getRacename());
        holder.item_football_racename.setTextColor(Color.parseColor(match.getRaceColor()));
        holder.item_football_time.setText(match.getTime());

        if (match.getDate() != null) {
            holder.item_football_date.setVisibility(View.VISIBLE);
            holder.item_football_date.setText(DateUtil.convertDateToNationMD(match.getDate().substring(5, 10)));//截取日月
        }

        // 完场描述
        holder.view_line.setVisibility(TextUtils.isEmpty(match.getTxt()) ? View.GONE : View.VISIBLE);
        holder.tv_item_desc.setVisibility(TextUtils.isEmpty(match.getTxt()) ? View.GONE : View.VISIBLE);
        String name = match.getWinner()+""== match.getHomeId() ? match.getHometeam() : match.getGuestteam();
        holder.tv_item_desc.setText(match.getTxt() + "," + name + mContext.getResources().getString(R.string.roll_desc_txt));

        switch (match.getStatusOrigin()) {
            case "0":// 未开
                holder.ll_half_score.setVisibility(View.INVISIBLE);
                holder.ll_all_score.setVisibility(View.INVISIBLE);

                holder.keeptime.setText(mContext.getResources().getString(R.string.tennis_match_not_start));
                holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.res_pl_color));
                break;
            case "1":// 上半场
                holder.ll_half_score.setVisibility(View.VISIBLE);
                holder.ll_all_score.setVisibility(View.VISIBLE);
                holder.item_home_half_score.setVisibility(View.INVISIBLE);
                holder.item_guest_half_score.setVisibility(View.INVISIBLE);
                holder.item_home_full_score.setText(match.getHomeScore());
                holder.item_guest_full_score.setText(match.getGuestScore());
                holder.item_home_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                holder.item_guest_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                try {
                    int keeptime = Integer.parseInt(match.getKeepTime());// 设置时间
                    holder.keeptime.setText(keeptime > 45 ? "45+" : String.valueOf(keeptime));
                    holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
                } catch (Exception e) {
                    holder.keeptime.setText("E");
                    holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
                }
                break;
            case "2":// 中场
                holder.ll_half_score.setVisibility(View.VISIBLE);
                holder.ll_all_score.setVisibility(View.VISIBLE);
                holder.item_home_full_score.setText(match.getHomeScore());
                holder.item_guest_full_score.setText(match.getGuestScore());
                holder.item_home_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                holder.item_guest_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                holder.keeptime.setText(mContext.getString(R.string.immediate_status_midfield));
                holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
                break;
            case "3":// 下半场
                holder.ll_half_score.setVisibility(View.VISIBLE);
                holder.ll_all_score.setVisibility(View.VISIBLE);
                holder.item_home_full_score.setText(match.getHomeScore());
                holder.item_guest_full_score.setText(match.getGuestScore());
                holder.item_home_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                holder.item_guest_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                try {
                    int keeptime = Integer.parseInt(match.getKeepTime());
                    holder.keeptime.setText(keeptime > 90 ? "90+" : String.valueOf(keeptime));
                    holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
                } catch (Exception e) {
                    holder.keeptime.setText("E");
                    holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
                }
                break;
            case "4":// 加时
                holder.ll_half_score.setVisibility(View.VISIBLE);
                holder.ll_all_score.setVisibility(View.VISIBLE);
                holder.item_home_full_score.setText(match.getHomeScore());
                holder.item_guest_full_score.setText(match.getGuestScore());
                holder.item_home_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                holder.item_guest_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                holder.keeptime.setText(mContext.getString(R.string.immediate_status_overtime));
                holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
                break;
            case "5":// 点球
                holder.ll_half_score.setVisibility(View.VISIBLE);
                holder.ll_all_score.setVisibility(View.VISIBLE);
                holder.item_home_full_score.setText(match.getHomeScore());
                holder.item_guest_full_score.setText(match.getGuestScore());
                holder.item_home_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                holder.item_guest_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                holder.keeptime.setText(mContext.getString(R.string.immediate_status_point));
                holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
                break;
            case "-1":// 完场
                holder.keeptime.setText(mContext.getResources().getString(R.string.finish_txt));
                holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.ll_half_score.setVisibility(View.VISIBLE);
                holder.ll_all_score.setVisibility(View.VISIBLE);
                holder.item_home_full_score.setText(match.getHomeScore());
                holder.item_guest_full_score.setText(match.getGuestScore());
                holder.item_home_full_score.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.item_guest_full_score.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.item_home_half_score.setVisibility(View.VISIBLE);
                holder.item_guest_half_score.setVisibility(View.VISIBLE);
                holder.item_home_half_score.setText(match.getHomeHalfScore());
                holder.item_guest_half_score.setText(match.getGuestHalfScore());
                break;
            case "-10":// 取消
                holder.ll_half_score.setVisibility(View.INVISIBLE);
                holder.ll_all_score.setVisibility(View.INVISIBLE);
                holder.keeptime.setText(mContext.getString(R.string.immediate_status_cancel));
                holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));
                break;
            case "-11":// 待定
                holder.ll_half_score.setVisibility(View.INVISIBLE);
                holder.ll_all_score.setVisibility(View.INVISIBLE);
                holder.keeptime.setText(mContext.getString(R.string.immediate_status_hold));
                holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));
                break;
            case "-12":// 腰斩
                holder.keeptime.setText(mContext.getString(R.string.immediate_status_cut));
                holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.ll_half_score.setVisibility(View.VISIBLE);
                holder.ll_all_score.setVisibility(View.VISIBLE);
                holder.item_home_full_score.setText(match.getHomeScore());
                holder.item_guest_full_score.setText(match.getGuestScore());
                holder.item_home_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                holder.item_guest_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                break;
            case "-13":// 中断
                holder.keeptime.setText(mContext.getString(R.string.immediate_status_mesomere));
                holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.ll_half_score.setVisibility(View.VISIBLE);
                holder.ll_all_score.setVisibility(View.VISIBLE);
                holder.item_home_full_score.setText(match.getHomeScore());
                holder.item_guest_full_score.setText(match.getGuestScore());
                holder.item_home_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                holder.item_guest_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
                break;
            case "-14":// 推迟
                holder.keeptime.setText(mContext.getString(R.string.immediate_status_postpone));
                holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.ll_half_score.setVisibility(View.INVISIBLE);
                holder.ll_all_score.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }

        if (match.getHomeTeamTextColorId() == R.color.red || match.getHomeTeamTextColorId() == R.color.content_txt_black) {
            holder.item_football_hometeam.setTextColor(mContext.getResources().getColor(match.getHomeTeamTextColorId()));
        } else {
            holder.item_football_hometeam.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
        }
        if (match.getGuestTeamTextColorId() == R.color.red || match.getGuestTeamTextColorId() == R.color.content_txt_black) {
            holder.item_football_hometeam.setTextColor(mContext.getResources().getColor(match.getGuestTeamTextColorId()));
        } else {
            holder.item_football_guestteam.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
        }

        if ("-10".equals(match.getStatusOrigin()) && "-11".equals(match.getStatusOrigin()) && "-14".equals(match.getStatusOrigin()) && !"0".equals(match.getStatusOrigin())
                && (match.getHomeScore() == null || match.getGuestScore() == null)) {

            holder.item_home_full_score.setText(0 + "");
            holder.item_guest_full_score.setText(0 + "");
            holder.item_home_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
            holder.item_guest_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
        }

        if ("1".equals(match.getStatusOrigin()) || "3".equals(match.getStatusOrigin())) {// 显示秒的闪烁
            holder.item_football_frequency.setText("\'");
            holder.item_football_frequency.setVisibility(View.VISIBLE);

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
                    holder.item_football_frequency.startAnimation(anim2);
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
                    holder.item_football_frequency.startAnimation(anim1);

                }
            });
            holder.item_football_frequency.startAnimation(anim1);
        } else {
            holder.item_football_frequency.setText("");
            holder.item_football_frequency.setVisibility(View.GONE);
            holder.item_football_frequency.clearAnimation();
        }

        if ("3".equals(match.getStatusOrigin()) || "2".equals(match.getStatusOrigin())) {// 显示右上角上半场比分

            holder.item_home_half_score.setVisibility(View.VISIBLE);
            holder.item_guest_half_score.setVisibility(View.VISIBLE);
            holder.item_home_half_score.setText(match.getHomeHalfScore());
            holder.item_guest_half_score.setText(match.getGuestHalfScore());

        } else if ("-12".equals(match.getStatusOrigin()) || "-13".equals(match.getStatusOrigin())) {// 中断和腰斩
            // 有中场比分显示中场比分
            if (match.getHomeHalfScore() != null && match.getGuestHalfScore() != null) {

                holder.item_home_half_score.setVisibility(View.VISIBLE);
                holder.item_guest_half_score.setVisibility(View.VISIBLE);
                holder.item_home_half_score.setText(match.getHomeHalfScore());
                holder.item_guest_half_score.setText(match.getGuestHalfScore());

            } else {

                holder.item_home_half_score.setVisibility(View.INVISIBLE);
                holder.item_guest_half_score.setVisibility(View.INVISIBLE);
            }
        } else {

            if ("-1".equals(match.getStatusOrigin())) {
                holder.item_home_half_score.setVisibility(View.VISIBLE);
                holder.item_guest_half_score.setVisibility(View.VISIBLE);


            } else {
                holder.item_home_half_score.setVisibility(View.INVISIBLE);
                holder.item_guest_half_score.setVisibility(View.INVISIBLE);
            }
        }


        if ("".equals(match.getHome_rc()) || "0".equals(match.getHome_rc()) || match.getHome_rc() == null) {
            holder.item_football_home_rc.setVisibility(View.GONE);
        } else {
            holder.item_football_home_rc.setText(match.getHome_rc());
            holder.item_football_home_rc.setVisibility(View.VISIBLE);
        }

        if ("".equals(match.getHome_yc()) || "0".equals(match.getHome_yc()) || match.getHome_yc() == null) {
            holder.item_football_home_yc.setVisibility(View.GONE);
        } else {
            holder.item_football_home_yc.setText(match.getHome_yc());
            holder.item_football_home_yc.setVisibility(View.VISIBLE);
        }


        if ("".equals(match.getGuest_rc()) || "0".equals(match.getGuest_rc()) || match.getGuest_rc() == null) {
            holder.item_football_guest_rc.setVisibility(View.GONE);
        } else {
            holder.item_football_guest_rc.setText(match.getGuest_rc());
            holder.item_football_guest_rc.setVisibility(View.VISIBLE);
        }
        if ("".equals(match.getGuest_yc()) || "0".equals(match.getGuest_yc()) || match.getGuest_yc() == null) {
            holder.item_football_guest_yc.setVisibility(View.GONE);
        } else {
            holder.item_football_guest_yc.setText(match.getGuest_yc());
            holder.item_football_guest_yc.setVisibility(View.VISIBLE);
        }

        MatchOdd asiaLet = match.getMatchOdds().get("asiaLet");
        MatchOdd asiaSize = match.getMatchOdds().get("asiaSize");
        MatchOdd euro = match.getMatchOdds().get("euro");

        //第一次进来根据文件选择选中哪个。默认是亚盘和欧赔
        boolean asize = PreferenceUtil.getBoolean(MyConstants.rbSizeBall, false);
        boolean eur = PreferenceUtil.getBoolean(MyConstants.RBOCOMPENSATE, true);
        boolean alet = PreferenceUtil.getBoolean(MyConstants.RBSECOND, true);
        boolean noshow = PreferenceUtil.getBoolean(MyConstants.RBNOTSHOW, false);

        // 隐藏赔率
        if (noshow) {
            holder.ll_odds_content1.setVisibility(View.GONE);
            holder.ll_odds_content2.setVisibility(View.GONE);
        } else if ((asize && eur) || (asize && alet) || (eur && alet)) {
            holder.ll_odds_content1.setVisibility(View.VISIBLE);
            holder.ll_odds_content2.setVisibility(View.VISIBLE);
        } else {
            holder.ll_odds_content1.setVisibility(View.VISIBLE);
            holder.ll_odds_content2.setVisibility(View.GONE);
        }

        // 亚盘赔率
        if (alet) {
            setOddsData(holder.tv_odds_top1, holder.tv_odds_center1, holder.tv_odds_bottom1, asiaLet, match, 1);
        }
        // 大小盘赔率
        if (asize) {
            if (!alet) {
                setOddsData(holder.tv_odds_top1, holder.tv_odds_center1, holder.tv_odds_bottom1, asiaSize, match, 2);
            } else {
                setOddsData(holder.tv_odds_top2, holder.tv_odds_center2, holder.tv_odds_bottom2, asiaSize, match, 2);
            }
        }
        // 欧盘赔率
        if (eur) {
            if (!alet && !asize) {
                setOddsData(holder.tv_odds_top1, holder.tv_odds_center1, holder.tv_odds_bottom1, euro, match, 3);
            } else {
                setOddsData(holder.tv_odds_top2, holder.tv_odds_center2, holder.tv_odds_bottom2, euro, match, 3);
            }
        }

        // 关注
        String focusIds = PreferenceUtil.getString(FocusFragment.FOCUS_ISD, "");
        String[] idArray = focusIds.split("[,]");

        for (String id : idArray) {
            if (id.equals(match.getThirdId())) {
                holder.Iv_guangzhu.setImageResource(R.mipmap.football_focus);
                holder.Iv_guangzhu.setTag(true);
                break;
            } else {
                holder.Iv_guangzhu.setImageResource(R.mipmap.football_nomal);
                holder.Iv_guangzhu.setTag(false);
            }
        }

        holder.Iv_guangzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFocusMatchClickListener != null) {
                    mFocusMatchClickListener.onClick(v, match.getThirdId());
                }
            }
        });
    }

    /**
     * 设置盘口数据
     *
     * @param topView
     * @param centerView
     * @param bottomView
     * @param odd
     */
    private void setOddsData(TextView topView, TextView centerView, TextView bottomView, MatchOdd odd, BallMatchItemsBean match, int type) {
        if (odd == null) {
            topView.setText("-");
            centerView.setText("-");
            bottomView.setText("-");
            return;
        }
        String handicapValue;
        switch (type) {
            case 1:
                handicapValue = HandicapUtils.changeHandicap(odd.getHandicapValue());
                break;
            case 2:
                handicapValue = HandicapUtils.changeHandicapByBigLittleBall(odd.getHandicapValue());
                break;
            case 3:
                handicapValue = odd.getMediumOdds();
                break;
            default:
                handicapValue = "-";
                break;
        }
        int keeptime = 0;
        if (match.getKeepTime() != null) {
            keeptime = Integer.parseInt(match.getKeepTime());
        }
        if ((!"-1".equals(match.getStatusOrigin()) && keeptime >= 89) || "-".equals(handicapValue) || "|".equals(handicapValue)) {// 封盘,完场不会有封盘的情况
            topView.setText("-");
            centerView.setText("-");
            bottomView.setText(mContext.getString(R.string.immediate_status_entertained));
        } else {
            centerView.setText(handicapValue);
            topView.setText(odd.getLeftOdds());
            bottomView.setText(odd.getRightOdds());
            topView.setTextColor(match.getLeftOddTextColorId() != 0 ? mContext.getResources().getColor(match.getLeftOddTextColorId()) : mContext.getResources().getColor(R.color.content_txt_light_grad));
            bottomView.setTextColor(match.getRightOddTextColorId() != 0 ? mContext.getResources().getColor(match.getRightOddTextColorId()) : mContext.getResources().getColor(R.color.content_txt_black));
            centerView.setTextColor(match.getMidOddTextColorId() != 0 ? mContext.getResources().getColor(match.getMidOddTextColorId()) : mContext.getResources().getColor(R.color.content_txt_light_grad));
        }
    }
}
