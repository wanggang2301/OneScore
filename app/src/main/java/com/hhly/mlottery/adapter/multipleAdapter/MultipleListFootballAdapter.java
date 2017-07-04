package com.hhly.mlottery.adapter.multipleAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.hhly.mlottery.bean.Match;
import com.hhly.mlottery.bean.MatchOdd;
import com.hhly.mlottery.callback.FocusMatchClickListener;
import com.hhly.mlottery.frame.footballframe.FocusFragment;
import com.hhly.mlottery.util.HandicapUtils;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;

import java.util.List;

/**
 * Created by yixq on 2017/1/5.
 * mail：yixq@13322.com
 * describe: 多屏动画足球列表Adapter
 */

public class MultipleListFootballAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //    Vector<Boolean> checkVector;
//    private List<Boolean> isClicks;
    List<Match> datas;
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
    private MultipleRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setmOnItemClickListener(MultipleRecyclerViewItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface MultipleRecyclerViewItemClickListener {
        void onItemClick(View view, String data, int pos, Match matchData);
    }


    public MultipleListFootballAdapter(Context context, List<Match> datas, String teamLogoPre, String teamLogoSuff) {
        mLayoutInflater = LayoutInflater.from(context);
        this.datas = datas;
        this.mContext = context;
        this.teamLogoPre = teamLogoPre;
        this.teamLogoSuff = teamLogoSuff;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.multiple_football_item, viewGroup, false);
        MultipleListFootballAdapter.ImmediaViewHolder immediaViewHolder = new MultipleListFootballAdapter.ImmediaViewHolder(view);

//        //将创建的View注册点击事件
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mOnItemClickListener != null) {
//                    mOnItemClickListener.onItemClick(v, (String) v.getTag());
//                }
//            }
//        });
        return immediaViewHolder;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Match match = datas.get(position);

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

        ImmediaViewHolder immediaViewHolder = (ImmediaViewHolder) holder;

//        if (checkVector.get(position)) {
////        if (isClicks.get(position)) {
//            immediaViewHolder.item_football_content_ll.setBackgroundResource(R.color.multiple_item_bg_color);
////            immediaViewHolder.item_football_content_ll.setBackgroundResource(R.color.white);
//        }else{
//            immediaViewHolder.item_football_content_ll.setBackgroundResource(R.color.white);
//        }

        if (match.isFootballChicks()) {
            immediaViewHolder.item_football_content_ll.setBackgroundResource(R.color.multiple_item_bg_color);
//            immediaViewHolder.item_football_content_ll.setBackgroundResource(R.color.white);
        } else {
            immediaViewHolder.item_football_content_ll.setBackgroundResource(R.color.white);
        }

        if (mOnItemClickListener != null) {
            //将创建的View注册点击事件
            immediaViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                checkVector.set(position , !checkVector.get(position));
////                    for (int i = 0; i < isClicks.size(); i++) {
////                        isClicks.set(i, false);
////                    }
////                    isClicks.set(position, true);
//                    notifyDataSetChanged();
                    mOnItemClickListener.onItemClick(v, (String) v.getTag(), position, match);
                }
            });
        }


        convert((MultipleListFootballAdapter.ImmediaViewHolder) holder, match, handicap);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void updateDatas(List<Match> mMatch) {
        datas = mMatch;
    }


    /**
     * 数据ViewHolder
     */

    public static class ImmediaViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        TextView keeptime;

        TextView item_football_racename;
        TextView item_football_time;
        TextView item_football_half_score;
        TextView item_football_frequency;
        //   TextView item_football_right_mid_score;
        TextView item_football_home_yc;
        TextView item_football_home_rc;
        TextView item_football_guest_rc;
        TextView item_football_guest_yc;
        TextView item_football_hometeam;
        TextView item_football_full_score;
        TextView item_football_guestteam;
        TextView item_football_left_odds;
        TextView item_football_handicap_value;
        TextView item_football_right_odds;
        RelativeLayout item_football_odds_layout;
        LinearLayout item_football_content_ll;

        //关注
        ImageView Iv_guangzhu;


        ImageView home_icon;

        ImageView guest_icon;

        public ImmediaViewHolder(final View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view);
            keeptime = (TextView) itemView.findViewById(R.id.keeptime);
            item_football_racename = (TextView) itemView.findViewById(R.id.item_football_racename);
            item_football_time = (TextView) itemView.findViewById(R.id.item_football_time);
            item_football_half_score = (TextView) itemView.findViewById(R.id.item_football_half_score);
            item_football_frequency = (TextView) itemView.findViewById(R.id.item_football_frequency);
            //   item_football_right_mid_score = (TextView) itemView.findViewById(R.id.item_football_right_mid_score);
            item_football_home_yc = (TextView) itemView.findViewById(R.id.item_football_home_yc);
            item_football_home_rc = (TextView) itemView.findViewById(R.id.item_football_home_rc);
            item_football_guest_rc = (TextView) itemView.findViewById(R.id.item_football_guest_rc);
            item_football_guest_yc = (TextView) itemView.findViewById(R.id.item_football_guest_yc);
            item_football_hometeam = (TextView) itemView.findViewById(R.id.item_football_hometeam);
            item_football_full_score = (TextView) itemView.findViewById(R.id.item_football_full_score);
            item_football_guestteam = (TextView) itemView.findViewById(R.id.item_football_guestteam);
            item_football_left_odds = (TextView) itemView.findViewById(R.id.item_football_left_odds);
            item_football_handicap_value = (TextView) itemView.findViewById(R.id.item_football_handicap_value);
            item_football_right_odds = (TextView) itemView.findViewById(R.id.item_football_right_odds);
            item_football_odds_layout = (RelativeLayout) itemView.findViewById(R.id.item_football_odds_layout);
            item_football_content_ll = (LinearLayout) itemView.findViewById(R.id.item_football_content_ll);

            Iv_guangzhu = (ImageView) itemView.findViewById(R.id.Iv_guangzhu);
            home_icon = (ImageView) itemView.findViewById(R.id.home_icon);
            guest_icon = (ImageView) itemView.findViewById(R.id.guest_icon);
        }

    }

    private void convert(final MultipleListFootballAdapter.ImmediaViewHolder holder, final Match match, int handicap) {

//        if (match.getItemBackGroundColorId() == R.color.item_football_event_yellow) {
//            holder.item_football_content_ll.setBackgroundResource(match.getItemBackGroundColorId());
//
//        } else {
//            holder.item_football_content_ll.setBackgroundResource(R.color.white);
//        }

        if (match.getHomeHalfScore() == null) {
            match.setHomeHalfScore("0");
        }

        if (match.getGuestHalfScore() == null) {
            match.setGuestHalfScore("0");
        }

        holder.item_football_hometeam.setText(match.getHometeam());
        holder.item_football_guestteam.setText(match.getGuestteam());

        //主队url
        final String homelogourl = teamLogoPre + match.getHomeId().trim() + teamLogoSuff;

        //客队url
        final String guestlogourl = teamLogoPre + match.getGuestId().trim() + teamLogoSuff;


        // holder.home_icon.setTag(homelogourl);
        // holder.guest_icon.setTag(guestlogourl);
        //ImagaeLoader -- 加载图片
        ImageLoader.load(mContext, homelogourl, R.mipmap.score_default).into(holder.home_icon);
        ImageLoader.load(mContext, guestlogourl, R.mipmap.score_default).into(holder.guest_icon);

        holder.item_football_racename.setText(match.getRacename());
        holder.item_football_racename.setTextColor(Color.parseColor(match.getRaceColor()));
        holder.item_football_time.setText(match.getTime());
        holder.item_football_half_score.setTextColor(mContext.getResources().getColor(R.color.text_about_color));
        holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.text_about_color));


        if ("0".equals(match.getStatusOrigin())) {// 未开
            holder.item_football_full_score.setText("VS");

            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.content_txt_grad));
            // holder.setFakeBoldText(R.id.item_football_full_score, false);


            holder.keeptime.setVisibility(View.GONE);


        } else if ("1".equals(match.getStatusOrigin())) {// 上半场

            holder.item_football_full_score.setText(Html.fromHtml("<span><b>" + match.getHomeScore() + "</b></span><span>-</span><span><b>" + match.getGuestScore() + "</b></span>"));
            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.bg_header));
            try {
                int keeptime = Integer.parseInt(match.getKeepTime());// 设置时间
                if (keeptime > 45) {
                    holder.keeptime.setText("45+");
                    holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));

                } else {
                    holder.keeptime.setText(keeptime + "");
                    holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
                }
            } catch (Exception e) {

                holder.keeptime.setText("E");
                holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
            }
            holder.keeptime.setVisibility(View.VISIBLE);

        } else if ("3".equals(match.getStatusOrigin())) {// 下半场
            holder.item_football_full_score.setText(Html.fromHtml("<span><b>" + match.getHomeScore() + "</b></span><span>-</span><span><b>" + match.getGuestScore() + "</b></span>"));
            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.bg_header));

            try {
                int keeptime = Integer.parseInt(match.getKeepTime());
                if (keeptime > 90) {
                    holder.keeptime.setText("90+");
                    holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
                } else {

                    holder.keeptime.setText(keeptime + "");
                    holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
                }
            } catch (Exception e) {

                holder.keeptime.setText("E");
                holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
            }

            holder.keeptime.setVisibility(View.VISIBLE);
        } else if ("4".equals(match.getStatusOrigin())) {// 加时
            holder.item_football_full_score.setText(Html.fromHtml("<span><b>" + match.getHomeScore() + "</b></span><span>-</span><span><b>" + match.getGuestScore() + "</b></span>"));
            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.bg_header));

            holder.keeptime.setText(mContext.getString(R.string.immediate_status_overtime));
            holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));

            holder.keeptime.setVisibility(View.VISIBLE);
        } else if ("5".equals(match.getStatusOrigin())) {// 点球


            holder.item_football_full_score.setText(Html.fromHtml("<span><b>" + match.getHomeScore() + "</b></span><span>-</span><span><b>" + match.getGuestScore() + "</b></span>"));
            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.bg_header));

            holder.keeptime.setText(mContext.getString(R.string.immediate_status_point));
            holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));

            holder.keeptime.setVisibility(View.VISIBLE);

        } else if ("-1".equals(match.getStatusOrigin())) {// 完场

            holder.keeptime.setVisibility(View.GONE);


            holder.item_football_full_score.setText(Html.fromHtml("<span><b>" + match.getHomeScore() + "</b></span><span>-</span><span><b>" + match.getGuestScore() + "</b></span>"));
            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.red));

            holder.item_football_half_score.setText("(" + match.getHomeHalfScore() + ":" + match.getGuestHalfScore() + ")");
            holder.item_football_half_score.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.item_football_half_score.setVisibility(View.VISIBLE);

        } else if ("2".equals(match.getStatusOrigin())) {// 中场
            holder.item_football_full_score.setText(Html.fromHtml("<span><b>" + match.getHomeScore() + "</b></span><span>-</span><span><b>" + match.getGuestScore() + "</b></span>"));
            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.bg_header));
            holder.keeptime.setText(mContext.getString(R.string.immediate_status_midfield));
            holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
            holder.keeptime.setVisibility(View.VISIBLE);

        } else if ("-10".equals(match.getStatusOrigin())) {// 取消
            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.bg_header));
            // holder.setFakeBoldText(R.id.item_football_full_score, false);
            holder.item_football_full_score.setText("VS");
            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.content_txt_grad));
            holder.keeptime.setVisibility(View.VISIBLE);
            holder.keeptime.setText(mContext.getString(R.string.immediate_status_cancel));
            holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));
        } else if ("-11".equals(match.getStatusOrigin())) {// 待定
            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.bg_header));
            // holder.setFakeBoldText(R.id.item_football_full_score, false);
            holder.keeptime.setText(mContext.getString(R.string.immediate_status_hold));
            holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.keeptime.setVisibility(View.VISIBLE);
            holder.item_football_full_score.setText("VS");
            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.content_txt_grad));
        } else if ("-12".equals(match.getStatusOrigin())) {// 腰斩
            holder.keeptime.setText(mContext.getString(R.string.immediate_status_cut));
            holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.item_football_full_score.setText(Html.fromHtml("<span><b>" + match.getHomeScore() + "</b></span><span>-</span><span><b>" + match.getGuestScore() + "</b></span>"));
            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.bg_header));
            holder.keeptime.setVisibility(View.VISIBLE);

        } else if ("-13".equals(match.getStatusOrigin())) {// 中断

            holder.keeptime.setText(mContext.getString(R.string.immediate_status_mesomere));
            holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.item_football_full_score.setText(Html.fromHtml("<span><b>" + match.getHomeScore() + "</b></span><span>-</span><span><b>" + match.getGuestScore() + "</b></span>"));
            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.bg_header));
            holder.keeptime.setVisibility(View.VISIBLE);


        } else if ("-14".equals(match.getStatusOrigin())) {// 推迟

            holder.keeptime.setText(mContext.getString(R.string.immediate_status_postpone));
            holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.item_football_full_score.setText(match.getHomeScore() + "-" + match.getGuestScore());
            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.bg_header));
            holder.keeptime.setVisibility(View.VISIBLE);

            holder.item_football_full_score.setText("VS");
            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.content_txt_grad));
            holder.item_football_full_score.setVisibility(View.VISIBLE);
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
            holder.item_football_full_score.setText(Html.fromHtml("<span><b>0</b></span><span>-</span><span><b>0</b></span>"));
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

            holder.item_football_half_score.setText("(" + match.getHomeHalfScore() + ":" + match.getGuestHalfScore() + ")");
            holder.item_football_half_score.setVisibility(View.VISIBLE);


        } else if ("-12".equals(match.getStatusOrigin()) || "-13".equals(match.getStatusOrigin())) {// 中断和腰斩
            // 有中场比分显示中场比分
            if (match.getHomeHalfScore() != null && match.getGuestHalfScore() != null) {
                holder.item_football_half_score.setText("(" + match.getHomeHalfScore() + ":" + match.getGuestHalfScore() + ")");
                holder.item_football_half_score.setVisibility(View.VISIBLE);
            } else {
                holder.item_football_half_score.setVisibility(View.INVISIBLE);
            }
        } else {

            if ("-1".equals(match.getStatusOrigin())) {
                holder.item_football_half_score.setVisibility(View.VISIBLE);

            } else {
                holder.item_football_half_score.setVisibility(View.INVISIBLE);
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


        holder.item_football_odds_layout.setVisibility(View.VISIBLE);
        holder.item_football_left_odds.setVisibility(View.VISIBLE);
        holder.item_football_handicap_value.setVisibility(View.VISIBLE);
        holder.item_football_right_odds.setVisibility(View.VISIBLE);

        // 盘口计算
        if (handicap == 1) {// 亚盘

            MatchOdd odd = match.getMatchOdds().get("asiaLet");
            if (odd == null) {
                holder.item_football_left_odds.setVisibility(View.GONE);
                holder.item_football_handicap_value.setVisibility(View.GONE);
                holder.item_football_right_odds.setVisibility(View.GONE);
            } else {

                holder.item_football_odds_layout.setVisibility(View.VISIBLE);


                String handicapValue = odd.getHandicapValue();

                int keeptime = 0;
                if (match.getKeepTime() != null) {
                    keeptime = Integer.parseInt(match.getKeepTime());
                }

                if ((!"-1".equals(match.getStatusOrigin()) && keeptime >= 89) || "-".equals(handicapValue) || "|".equals(handicapValue)) {// 封盘,完场不会有封盘的情况

                    holder.item_football_left_odds.setVisibility(View.GONE);
                    holder.item_football_handicap_value.setVisibility(View.GONE);
                    holder.item_football_right_odds.setText(mContext.getString(R.string.immediate_status_entertained));
                } else {
                    holder.item_football_left_odds.setVisibility(View.VISIBLE);
                    holder.item_football_handicap_value.setVisibility(View.VISIBLE);
                    holder.item_football_right_odds.setVisibility(View.VISIBLE);


                    holder.item_football_handicap_value.setText(HandicapUtils.changeHandicap(handicapValue));
                    holder.item_football_left_odds.setText(odd.getLeftOdds());
                    holder.item_football_right_odds.setText(odd.getRightOdds());
                    if (match.getLeftOddTextColorId() != 0) {

                        holder.item_football_left_odds.setTextColor(mContext.getResources().getColor(match.getLeftOddTextColorId()));

                    }
                    if (match.getRightOddTextColorId() != 0) {
                        holder.item_football_right_odds.setTextColor(mContext.getResources().getColor(match.getRightOddTextColorId()));

                    }
                    if (match.getMidOddTextColorId() != 0) {

                        //  holder.item_football_handicap_value.set  match.getMidOddTextColorId()

                        holder.item_football_handicap_value.setTextColor(mContext.getResources().getColor(match.getMidOddTextColorId()));


                    }
                }

                if ("-1".equals(match.getStatusOrigin())) {// 完场不会有封盘的情况


                    try {
                        float homeScore = Float.parseFloat(match.getHomeScore());
                        float guestScore = Float.parseFloat(match.getGuestScore());
                        float handicapValueF = Float.parseFloat(handicapValue);
                        float re = homeScore - guestScore - handicapValueF;
                        if (re > 0) {// 注意这是亚盘的


                            holder.item_football_left_odds.setTextColor(mContext.getResources().getColor(R.color.white));
                            holder.item_football_left_odds.setBackgroundResource(R.color.resultcol);
                            holder.item_football_handicap_value.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
                            holder.item_football_handicap_value.setBackgroundResource(R.color.transparent);
                            holder.item_football_right_odds.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));
                            holder.item_football_right_odds.setBackgroundResource(R.color.transparent);

                        } else if (re < 0) {
                            holder.item_football_left_odds.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));
                            holder.item_football_left_odds.setBackgroundResource(R.color.transparent);
                            holder.item_football_handicap_value.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
                            holder.item_football_handicap_value.setBackgroundResource(R.color.transparent);
                            holder.item_football_right_odds.setTextColor(mContext.getResources().getColor(R.color.white));
                            holder.item_football_right_odds.setBackgroundResource(R.color.resultcol);
                        } else {

                            holder.item_football_left_odds.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));
                            holder.item_football_left_odds.setBackgroundResource(R.color.transparent);
                            holder.item_football_handicap_value.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
                            holder.item_football_handicap_value.setBackgroundResource(R.color.transparent);
                            holder.item_football_right_odds.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));
                            holder.item_football_right_odds.setBackgroundResource(R.color.transparent);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    if (match.getLeftOddTextColorId() == 0) {
                        holder.item_football_left_odds.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));

                    } else {
                        holder.item_football_left_odds.setTextColor(mContext.getResources().getColor(match.getLeftOddTextColorId()));
                    }

                    if (match.getRightOddTextColorId() == 0) {

                        holder.item_football_handicap_value.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));

                    } else {
                        holder.item_football_handicap_value.setTextColor(mContext.getResources().getColor(match.getMidOddTextColorId()));
                    }

                    if (match.getMidOddTextColorId() == 0) {
                        holder.item_football_right_odds.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));

                    } else {
                        holder.item_football_right_odds.setTextColor(mContext.getResources().getColor(match.getRightOddTextColorId()));
                    }

                    holder.item_football_left_odds.setBackgroundResource(R.color.transparent);
                    holder.item_football_handicap_value.setBackgroundResource(R.color.transparent);
                    holder.item_football_right_odds.setBackgroundResource(R.color.transparent);


                }

            }
        } else if (handicap == 2) {// 大小球
            MatchOdd odd = match.getMatchOdds().get("asiaSize");
            if (odd == null) {
                holder.item_football_left_odds.setVisibility(View.GONE);
                holder.item_football_handicap_value.setVisibility(View.GONE);
                holder.item_football_right_odds.setVisibility(View.GONE);


            } else {
                holder.item_football_odds_layout.setVisibility(View.VISIBLE);


                String handicapValue = odd.getHandicapValue();
                int keeptime = 0;
                if (match.getKeepTime() != null) {
                    keeptime = Integer.parseInt(match.getKeepTime());
                }
                if ((!"-1".equals(match.getStatusOrigin()) && keeptime >= 89) || "-".equals(handicapValue) || "|".equals(handicapValue)) {// 封盘

                    holder.item_football_left_odds.setVisibility(View.GONE);
                    holder.item_football_handicap_value.setVisibility(View.GONE);
                    holder.item_football_right_odds.setText(mContext.getString(R.string.immediate_status_entertained));
                } else {

                    holder.item_football_left_odds.setVisibility(View.VISIBLE);
                    holder.item_football_handicap_value.setVisibility(View.VISIBLE);
                    holder.item_football_right_odds.setVisibility(View.VISIBLE);
                    holder.item_football_handicap_value.setText(HandicapUtils.changeHandicapByBigLittleBall(handicapValue));
                    holder.item_football_left_odds.setText(odd.getLeftOdds());
                    holder.item_football_right_odds.setText(odd.getRightOdds());


                    if (match.getLeftOddTextColorId() != 0) {
                        holder.item_football_left_odds.setTextColor(mContext.getResources().getColor(match.getLeftOddTextColorId()));
                    }
                    if (match.getRightOddTextColorId() != 0) {
                        holder.item_football_right_odds.setTextColor(mContext.getResources().getColor(match.getRightOddTextColorId()));
                    }

                    if (match.getMidOddTextColorId() != 0) {

                        holder.item_football_handicap_value.setTextColor(mContext.getResources().getColor(match.getMidOddTextColorId()));

                    }
                }

                if ("-1".equals(match.getStatusOrigin())) {// 完场不会有封盘的情况
                    try {
                        float homeScore = Float.parseFloat(match.getHomeScore());
                        float guestScore = Float.parseFloat(match.getGuestScore());
                        float handicapValueF = Float.parseFloat(handicapValue);
                        float re = homeScore + guestScore - handicapValueF;
                        if (re > 0) {


                            holder.item_football_left_odds.setTextColor(mContext.getResources().getColor(R.color.white));
                            holder.item_football_left_odds.setBackgroundResource(R.color.resultcol);
                            holder.item_football_handicap_value.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
                            holder.item_football_handicap_value.setBackgroundResource(R.color.transparent);
                            holder.item_football_right_odds.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));
                            holder.item_football_right_odds.setBackgroundResource(R.color.transparent);


                        } else if (re < 0) {

                            holder.item_football_left_odds.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));
                            holder.item_football_left_odds.setBackgroundResource(R.color.transparent);
                            holder.item_football_handicap_value.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
                            holder.item_football_handicap_value.setBackgroundResource(R.color.transparent);
                            holder.item_football_right_odds.setTextColor(mContext.getResources().getColor(R.color.white));
                            holder.item_football_right_odds.setBackgroundResource(R.color.resultcol);


                        } else {

                            holder.item_football_left_odds.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));
                            holder.item_football_left_odds.setBackgroundResource(R.color.transparent);
                            holder.item_football_handicap_value.setTextColor(mContext.getResources().getColor(R.color.black));
                            holder.item_football_handicap_value.setBackgroundResource(R.color.transparent);
                            holder.item_football_right_odds.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));
                            holder.item_football_right_odds.setBackgroundResource(R.color.transparent);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {


                    if (match.getLeftOddTextColorId() == 0) {
                        holder.item_football_left_odds.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));

                    } else {
                        holder.item_football_left_odds.setTextColor(mContext.getResources().getColor(match.getLeftOddTextColorId()));


                    }

                    if (match.getRightOddTextColorId() == 0) {

                        holder.item_football_handicap_value.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));

                    } else {
                        holder.item_football_handicap_value.setTextColor(mContext.getResources().getColor(match.getMidOddTextColorId()));
                    }

                    if (match.getMidOddTextColorId() == 0) {
                        holder.item_football_right_odds.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));
                    } else {
                        holder.item_football_right_odds.setTextColor(mContext.getResources().getColor(match.getRightOddTextColorId()));
                    }
                    holder.item_football_left_odds.setBackgroundResource(R.color.transparent);
                    holder.item_football_handicap_value.setBackgroundResource(R.color.transparent);
                    holder.item_football_right_odds.setBackgroundResource(R.color.transparent);

                }

            }
        } else if (handicap == 3) {// 欧赔
            MatchOdd odd = match.getMatchOdds().get("euro");
            if (odd == null) {

                holder.item_football_left_odds.setVisibility(View.GONE);
                holder.item_football_handicap_value.setVisibility(View.GONE);
                holder.item_football_right_odds.setVisibility(View.GONE);

            } else {
                String leftOdds = odd.getLeftOdds();
                int keeptime = 0;
                if (match.getKeepTime() != null) {
                    keeptime = Integer.parseInt(match.getKeepTime());
                }
                if ((!"-1".equals(match.getStatusOrigin()) && keeptime >= 89) || "-".equals(leftOdds) || "|".equals(leftOdds)) {// 封盘
                    holder.item_football_left_odds.setVisibility(View.GONE);
                    holder.item_football_handicap_value.setVisibility(View.GONE);
                    holder.item_football_right_odds.setText(mContext.getString(R.string.immediate_status_entertained));
                } else {

                    holder.item_football_handicap_value.setText(odd.getMediumOdds());
                    holder.item_football_left_odds.setText(odd.getLeftOdds());
                    holder.item_football_right_odds.setText(odd.getRightOdds());

                    if (match.getLeftOddTextColorId() != 0) {
                        holder.item_football_left_odds.setTextColor(mContext.getResources().getColor(match.getLeftOddTextColorId()));

                    }
                    if (match.getRightOddTextColorId() != 0) {
                        holder.item_football_right_odds.setTextColor(mContext.getResources().getColor(match.getRightOddTextColorId()));

                    }
                    if (match.getMidOddTextColorId() != 0) {
                        holder.item_football_handicap_value.setTextColor(mContext.getResources().getColor(match.getMidOddTextColorId()));
                    }
                }
                // 完场赔率颜色

                if ("-1".equals(match.getStatusOrigin())) {
                    try {
                        int homeScore = Integer.parseInt(match.getHomeScore());
                        int guestScore = Integer.parseInt(match.getGuestScore());

                        if (homeScore > guestScore) {


                            holder.item_football_left_odds.setTextColor(mContext.getResources().getColor(R.color.white));
                            holder.item_football_left_odds.setBackgroundResource(R.color.resultcol);
                            holder.item_football_handicap_value.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));
                            holder.item_football_handicap_value.setBackgroundResource(R.color.transparent);
                            holder.item_football_right_odds.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));
                            holder.item_football_right_odds.setBackgroundResource(R.color.transparent);

                        } else if (homeScore < guestScore) {

                            holder.item_football_left_odds.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));
                            holder.item_football_left_odds.setBackgroundResource(R.color.transparent);
                            holder.item_football_handicap_value.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));
                            holder.item_football_handicap_value.setBackgroundResource(R.color.transparent);
                            holder.item_football_right_odds.setTextColor(mContext.getResources().getColor(R.color.white));
                            holder.item_football_right_odds.setBackgroundResource(R.color.resultcol);

                        } else {

                            holder.item_football_left_odds.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));
                            holder.item_football_left_odds.setBackgroundResource(R.color.transparent);
                            holder.item_football_handicap_value.setTextColor(mContext.getResources().getColor(R.color.white));
                            holder.item_football_handicap_value.setBackgroundResource(R.color.resultcol);
                            holder.item_football_right_odds.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));
                            holder.item_football_right_odds.setBackgroundResource(R.color.transparent);


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (match.getLeftOddTextColorId() == 0) {

                        holder.item_football_left_odds.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));


                    } else {

                        holder.item_football_left_odds.setTextColor(mContext.getResources().getColor(match.getLeftOddTextColorId()));


                    }

                    if (match.getRightOddTextColorId() == 0) {
                        holder.item_football_handicap_value.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));


                    } else {
                        holder.item_football_handicap_value.setTextColor(mContext.getResources().getColor(match.getMidOddTextColorId()));

                    }

                    if (match.getMidOddTextColorId() == 0) {
                        holder.item_football_right_odds.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));

                    } else {

                        holder.item_football_right_odds.setTextColor(mContext.getResources().getColor(match.getRightOddTextColorId()));

                    }

                    holder.item_football_left_odds.setBackgroundResource(R.color.transparent);
                    holder.item_football_handicap_value.setBackgroundResource(R.color.transparent);
                    holder.item_football_right_odds.setBackgroundResource(R.color.transparent);
                }
            }
        } else {

            holder.item_football_left_odds.setVisibility(View.GONE);
            holder.item_football_handicap_value.setVisibility(View.GONE);
            holder.item_football_right_odds.setVisibility(View.GONE);
        }

        String focusIds = PreferenceUtil.getString(FocusFragment.FOCUS_ISD, "");
        Log.e("BBB", "取得时候" + focusIds);
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


}
