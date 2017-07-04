package com.hhly.mlottery.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.Match;
import com.hhly.mlottery.bean.MatchOdd;
import com.hhly.mlottery.bean.resultbean.ResultMatchDto;
import com.hhly.mlottery.callback.DateOnClickListener;
import com.hhly.mlottery.callback.FocusMatchClickListener;
import com.hhly.mlottery.callback.RecyclerViewItemClickListener;
import com.hhly.mlottery.frame.footballframe.FocusFragment;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.HandicapUtils;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.ResultDateUtil;

import java.util.List;


/**
 * Created by asus1 on 2016/4/8.
 */
public class ResultMultiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ResultMatchDto> datas;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    /**
     * 日期Index
     */
    public final static int VIEW_DATE_INDEX = 0;

    /**
     * 比赛Index
     */
    public final static int VIEW_MATCH_INDEX = 1;

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

    /**
     * 时间选择
     */
    private DateOnClickListener dateOnClickListener = null;

    public void setDateOnClickListener(DateOnClickListener dateOnClickListener) {
        this.dateOnClickListener = dateOnClickListener;
    }


    public ResultMultiAdapter(Context context, List<ResultMatchDto> datas, String teamLogoPre, String teamLogoSuff) {
        mLayoutInflater = LayoutInflater.from(context);
        this.datas = datas;
        this.mContext = context;
        this.teamLogoPre = teamLogoPre;
        this.teamLogoSuff = teamLogoSuff;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final View view;
        if (viewType == VIEW_DATE_INDEX) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_football_date_week, viewGroup, false);
            final DateViewHolder dateViewHolder = new DateViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dateOnClickListener != null) {
                        dateOnClickListener.onClick(view);
                    }
                }
            });
            return dateViewHolder;

        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_football, viewGroup, false);
            ResultViewHolder resultViewHolder = new ResultViewHolder(view);

            //将创建的View注册点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, (String) v.getTag());
                    }
                }
            });

            return resultViewHolder;
        }
    }


    @Override
    public int getItemViewType(int position) {

        if (0 == datas.get(position).getType()) {
            return VIEW_DATE_INDEX;
        } else if (1 == datas.get(position).getType()) {
            return VIEW_MATCH_INDEX;
        } else {
            return 100;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (datas.size() <= 0 || datas == null) {
            return;
        }

        ResultMatchDto resultMatchDto = datas.get(position);

        if (holder instanceof DateViewHolder) {
            DateViewHolder dateViewHolder = (DateViewHolder) holder;
            dateViewHolder.tv_date.setText(DateUtil.convertDateToNation(resultMatchDto.getDate()));
            dateViewHolder.tv_week.setText(ResultDateUtil.getWeekOfDate(DateUtil.parseDate(ResultDateUtil.getDate(0, resultMatchDto.getDate()))));

            boolean alet = PreferenceUtil.getBoolean(MyConstants.RBSECOND, true);
            boolean asize = PreferenceUtil.getBoolean(MyConstants.rbSizeBall, false);
            boolean eur = PreferenceUtil.getBoolean(MyConstants.RBOCOMPENSATE, true);
            boolean noshow = PreferenceUtil.getBoolean(MyConstants.RBNOTSHOW, false);
            // 隐藏赔率name
            if (noshow) {
                dateViewHolder.handicapName1.setVisibility(View.GONE);
                dateViewHolder.handicapName2.setVisibility(View.GONE);
            } else if ((asize && eur) || (asize && alet) || (eur && alet)) {
                dateViewHolder.handicapName1.setVisibility(View.VISIBLE);
                dateViewHolder.handicapName2.setVisibility(View.VISIBLE);
            } else {
                dateViewHolder.handicapName1.setVisibility(View.VISIBLE);
                dateViewHolder.handicapName2.setVisibility(View.GONE);
            }

            // 亚盘赔率
            if (alet) {
                dateViewHolder.handicapName1.setText(mContext.getResources().getString(R.string.roll_asialet));
            }
            // 大小盘赔率
            if (asize) {
                if (!alet) {
                    dateViewHolder.handicapName1.setText(mContext.getResources().getString(R.string.roll_asiasize));
                } else {
                    dateViewHolder.handicapName2.setText(mContext.getResources().getString(R.string.roll_asiasize));
                }
            }
            // 欧盘赔率
            if (eur) {
                if (!alet && !asize) {
                    dateViewHolder.handicapName1.setText(mContext.getResources().getString(R.string.roll_euro));
                } else {
                    dateViewHolder.handicapName2.setText(mContext.getResources().getString(R.string.roll_euro));
                }
            }

        } else if (holder instanceof ResultViewHolder) {
            if (resultMatchDto.getMatchs() != null) {
                holder.itemView.setTag(resultMatchDto.getMatchs().getThirdId());

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

                convert((ResultViewHolder) holder, resultMatchDto.getMatchs(), handicap);
            }
        }
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    /**
     * 日期ViewHolder
     */

    public static class DateViewHolder extends RecyclerView.ViewHolder {

        protected static final String TAG = "TextViewHolder";

        TextView tv_date;
        TextView tv_week;
        TextView handicapName1;
        TextView handicapName2;

        public DateViewHolder(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_week = (TextView) itemView.findViewById(R.id.tv_week);
            handicapName1 = (TextView) itemView.findViewById(R.id.tv_handicap_name1);
            handicapName2 = (TextView) itemView.findViewById(R.id.tv_handicap_name2);
        }
    }


    /**
     * 数据ViewHolder
     */

    static class ResultViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        TextView keeptime;

        TextView item_football_racename;
        TextView item_football_time;
        TextView item_football_frequency;
        TextView item_football_home_yc;
        TextView item_football_home_rc;
        TextView item_football_guest_rc;
        TextView item_football_guest_yc;
        TextView item_football_hometeam;
        TextView item_football_guestteam;
        LinearLayout item_football_content_ll;

        //关注
        ImageView Iv_guangzhu;

        ImageView home_icon;

        ImageView guest_icon;


        TextView item_home_half_score;
        TextView item_home_full_score;

        TextView item_guest_half_score;
        TextView item_guest_full_score;

        LinearLayout ll_half_score;
        LinearLayout ll_all_score;
        LinearLayout ll_odds_content1;
        LinearLayout ll_odds_content2;
        TextView oddsTop1;
        TextView oddsTop2;
        TextView tv_odds_center1;
        TextView tv_odds_center2;
        TextView tv_odds_bottom1;
        TextView tv_odds_bottom2;

        View view_line;
        TextView tv_item_desc;

        public ResultViewHolder(final View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view);

            keeptime = (TextView) itemView.findViewById(R.id.keeptime);

            item_football_racename = (TextView) itemView.findViewById(R.id.item_football_racename);
            item_football_time = (TextView) itemView.findViewById(R.id.item_football_time);
            item_football_frequency = (TextView) itemView.findViewById(R.id.item_football_frequency);
            item_football_home_yc = (TextView) itemView.findViewById(R.id.item_football_home_yc);
            item_football_home_rc = (TextView) itemView.findViewById(R.id.item_football_home_rc);
            item_football_guest_rc = (TextView) itemView.findViewById(R.id.item_football_guest_rc);
            item_football_guest_yc = (TextView) itemView.findViewById(R.id.item_football_guest_yc);
            item_football_hometeam = (TextView) itemView.findViewById(R.id.item_football_hometeam);
            item_football_guestteam = (TextView) itemView.findViewById(R.id.item_football_guestteam);
            item_football_content_ll = (LinearLayout) itemView.findViewById(R.id.item_football_content_ll);


            item_home_half_score = (TextView) itemView.findViewById(R.id.tv_home_half_score);
            item_home_full_score = (TextView) itemView.findViewById(R.id.tv_home_full_score);

            item_guest_half_score = (TextView) itemView.findViewById(R.id.tv_guest_half_score);
            item_guest_full_score = (TextView) itemView.findViewById(R.id.tv_guest_full_score);

            Iv_guangzhu = (ImageView) itemView.findViewById(R.id.Iv_guangzhu);
            home_icon = (ImageView) itemView.findViewById(R.id.home_icon);
            guest_icon = (ImageView) itemView.findViewById(R.id.guest_icon);

            ll_half_score = (LinearLayout) itemView.findViewById(R.id.ll_half_score);
            ll_all_score = (LinearLayout) itemView.findViewById(R.id.ll_all_score);
            ll_odds_content1 = (LinearLayout) itemView.findViewById(R.id.ll_odds_content1);
            ll_odds_content2 = (LinearLayout) itemView.findViewById(R.id.ll_odds_content2);
            oddsTop1 = (TextView) itemView.findViewById(R.id.tv_odds_top1);
            oddsTop2 = (TextView) itemView.findViewById(R.id.tv_odds_top2);
            tv_odds_center1 = (TextView) itemView.findViewById(R.id.tv_odds_center1);
            tv_odds_center2 = (TextView) itemView.findViewById(R.id.tv_odds_center2);
            tv_odds_bottom1 = (TextView) itemView.findViewById(R.id.tv_odds_bottom1);
            tv_odds_bottom2 = (TextView) itemView.findViewById(R.id.tv_odds_bottom2);

            view_line = itemView.findViewById(R.id.view_line);
            tv_item_desc = (TextView) itemView.findViewById(R.id.tv_item_desc);
        }

    }

    private void convert(final ResultViewHolder holder, final Match match, int handicap) {

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

        // 完场描述
        holder.view_line.setVisibility(TextUtils.isEmpty(match.getTxt()) ? View.GONE : View.VISIBLE);
        holder.tv_item_desc.setVisibility(TextUtils.isEmpty(match.getTxt()) ? View.GONE : View.VISIBLE);
        String name = match.getWinner() == Integer.parseInt(match.getHomeId()) ? match.getHometeam() : match.getGuestteam();
        holder.tv_item_desc.setText(match.getTxt() + "," + name + mContext.getResources().getString(R.string.roll_desc_txt));

        //主队url
        final String homelogourl = teamLogoPre + match.getHomeId() + teamLogoSuff;  //"http://pic.13322.com/basketball/team/135_135/29.png"
        //客队url
        final String guestlogourl = teamLogoPre + match.getGuestId().trim().trim() + teamLogoSuff;

        ImageLoader.load(mContext, homelogourl, R.mipmap.score_default).into(holder.home_icon);
        ImageLoader.load(mContext, guestlogourl, R.mipmap.score_default).into(holder.guest_icon);


        holder.item_football_racename.setText(match.getRacename());
        holder.item_football_racename.setTextColor(Color.parseColor(match.getRaceColor()));
        holder.item_football_time.setText(match.getTime());

        if ("0".equals(match.getStatusOrigin())) {// 未开
            holder.ll_half_score.setVisibility(View.INVISIBLE);
            holder.ll_all_score.setVisibility(View.INVISIBLE);

            holder.keeptime.setText(mContext.getResources().getString(R.string.tennis_match_not_start));
            holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.res_pl_color));
        } else if ("1".equals(match.getStatusOrigin())) {// 上半场
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

        } else if ("3".equals(match.getStatusOrigin())) {// 下半场

            holder.ll_half_score.setVisibility(View.VISIBLE);
            holder.ll_all_score.setVisibility(View.VISIBLE);


            holder.item_home_half_score.setVisibility(View.VISIBLE);
            holder.item_guest_half_score.setVisibility(View.VISIBLE);

            holder.item_home_full_score.setText(match.getHomeScore());
            holder.item_guest_full_score.setText(match.getGuestScore());
            holder.item_home_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
            holder.item_guest_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));


            try {
                int keeptime = Integer.parseInt(match.getKeepTime());
                if (keeptime > 90) {
                    holder.keeptime.setText("90+");
                    holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
                } else {

                    holder.keeptime.setText(keeptime + "");
                    holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
                }


                holder.item_home_half_score.setText(match.getHomeHalfScore());
                holder.item_guest_half_score.setText(match.getGuestHalfScore());


            } catch (Exception e) {

                holder.keeptime.setText("E");
                holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
            }

        } else if ("4".equals(match.getStatusOrigin())) {// 加时


            holder.ll_half_score.setVisibility(View.VISIBLE);
            holder.ll_all_score.setVisibility(View.VISIBLE);


            holder.item_home_half_score.setVisibility(View.VISIBLE);
            holder.item_guest_half_score.setVisibility(View.VISIBLE);

            holder.item_home_full_score.setText(match.getHomeScore());
            holder.item_guest_full_score.setText(match.getGuestScore());
            holder.item_home_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
            holder.item_guest_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));


            holder.item_home_half_score.setText(match.getHomeHalfScore());
            holder.item_guest_half_score.setText(match.getGuestHalfScore());


            holder.keeptime.setText(mContext.getString(R.string.immediate_status_overtime));
            holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));


        } else if ("5".equals(match.getStatusOrigin())) {// 点球

            holder.ll_half_score.setVisibility(View.VISIBLE);
            holder.ll_all_score.setVisibility(View.VISIBLE);


            holder.item_home_half_score.setVisibility(View.VISIBLE);
            holder.item_guest_half_score.setVisibility(View.VISIBLE);

            holder.item_home_full_score.setText(match.getHomeScore());
            holder.item_guest_full_score.setText(match.getGuestScore());
            holder.item_home_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
            holder.item_guest_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));


            holder.item_home_half_score.setText(match.getHomeHalfScore());
            holder.item_guest_half_score.setText(match.getGuestHalfScore());


            //  holder.item_football_full_score.setText(Html.fromHtml("<span><b>" + match.getHomeScore() + "</b></span><span>-</span><span><b>" + match.getGuestScore() + "</b></span>"));
            //  holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.bg_header));

            holder.keeptime.setText(mContext.getString(R.string.immediate_status_point));
            holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));


            //  holder.item_football_half_score.setText("(" + match.getHomeHalfScore() + ":" + match.getGuestHalfScore() + ")");
            //  holder.item_football_half_score.setVisibility(View.VISIBLE);


        } else if ("-1".equals(match.getStatusOrigin())) {// 完场

            holder.ll_half_score.setVisibility(View.VISIBLE);
            holder.ll_all_score.setVisibility(View.VISIBLE);
            holder.keeptime.setText(mContext.getResources().getString(R.string.finish_txt));
            holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));

            holder.item_home_half_score.setVisibility(View.VISIBLE);
            holder.item_guest_half_score.setVisibility(View.VISIBLE);

            holder.item_home_full_score.setText(match.getHomeScore());
            holder.item_guest_full_score.setText(match.getGuestScore());
            holder.item_home_full_score.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.item_guest_full_score.setTextColor(mContext.getResources().getColor(R.color.red));


            holder.item_home_half_score.setText(match.getHomeHalfScore());
            holder.item_guest_half_score.setText(match.getGuestHalfScore());


          /*  holder.item_football_full_score.setText(Html.fromHtml("<span><b>" + match.getHomeScore() + "</b></span><span>-</span><span><b>" + match.getGuestScore() + "</b></span>"));
            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.item_football_full_score.setVisibility(View.VISIBLE);

            holder.item_football_half_score.setText("(" + match.getHomeHalfScore() + ":" + match.getGuestHalfScore() + ")");
            holder.item_football_half_score.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.item_football_half_score.setVisibility(View.VISIBLE);*/

        } else if ("2".equals(match.getStatusOrigin())) {// 中场

            holder.ll_half_score.setVisibility(View.VISIBLE);
            holder.ll_all_score.setVisibility(View.VISIBLE);

            holder.item_home_half_score.setVisibility(View.VISIBLE);
            holder.item_guest_half_score.setVisibility(View.VISIBLE);

            holder.item_home_full_score.setText(match.getHomeScore());
            holder.item_guest_full_score.setText(match.getGuestScore());
            holder.item_home_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
            holder.item_guest_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));

            holder.item_home_half_score.setText(match.getHomeHalfScore());
            holder.item_guest_half_score.setText(match.getGuestHalfScore());


           /* holder.item_football_full_score.setText(Html.fromHtml("<span><b>" + match.getHomeScore() + "</b></span><span>-</span><span><b>" + match.getGuestScore() + "</b></span>"));
            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.bg_header));
            holder.item_football_full_score.setVisibility(View.VISIBLE);*/


            holder.keeptime.setText(mContext.getString(R.string.immediate_status_midfield));
            holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
/*

            holder.item_football_half_score.setText("(" + match.getHomeHalfScore() + ":" + match.getGuestHalfScore() + ")");
            holder.item_football_half_score.setVisibility(View.VISIBLE);
*/


        } else if ("-10".equals(match.getStatusOrigin())) {// 取消


            holder.ll_half_score.setVisibility(View.INVISIBLE);
            holder.ll_all_score.setVisibility(View.INVISIBLE);


       /*     holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.bg_header));
            holder.item_football_full_score.setText("VS");
            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.content_txt_grad));
            holder.item_football_full_score.setVisibility(View.VISIBLE);*/


            holder.keeptime.setText(mContext.getString(R.string.immediate_status_cancel));
            holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));

            // holder.item_football_half_score.setVisibility(View.INVISIBLE);


        } else if ("-11".equals(match.getStatusOrigin())) {// 待定

            holder.ll_half_score.setVisibility(View.INVISIBLE);
            holder.ll_all_score.setVisibility(View.INVISIBLE);


            // holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.bg_header));
            holder.keeptime.setText(mContext.getString(R.string.immediate_status_hold));
            holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));
          /*  holder.item_football_full_score.setText("VS");
            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.content_txt_grad));
            holder.item_football_full_score.setVisibility(View.VISIBLE);


            holder.item_football_half_score.setVisibility(View.INVISIBLE);*/

        } else if ("-12".equals(match.getStatusOrigin())) {// 腰斩
            holder.keeptime.setText(mContext.getString(R.string.immediate_status_cut));
            holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));

            holder.ll_half_score.setVisibility(View.VISIBLE);
            holder.ll_all_score.setVisibility(View.VISIBLE);

            holder.item_home_half_score.setVisibility(View.INVISIBLE);
            holder.item_guest_half_score.setVisibility(View.INVISIBLE);
            holder.item_home_full_score.setText(match.getHomeScore());
            holder.item_guest_full_score.setText(match.getGuestScore());
            holder.item_home_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
            holder.item_guest_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));


           /* holder.item_football_full_score.setText(Html.fromHtml("<span><b>" + match.getHomeScore() + "</b></span><span>-</span><span><b>" + match.getGuestScore() + "</b></span>"));
            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.bg_header));
            holder.item_football_full_score.setVisibility(View.VISIBLE);*/


        } else if ("-13".equals(match.getStatusOrigin())) {// 中断

            holder.keeptime.setText(mContext.getString(R.string.immediate_status_mesomere));
            holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));


            holder.ll_half_score.setVisibility(View.VISIBLE);
            holder.ll_all_score.setVisibility(View.VISIBLE);

            holder.item_home_half_score.setVisibility(View.INVISIBLE);
            holder.item_guest_half_score.setVisibility(View.INVISIBLE);
            holder.item_home_full_score.setText(match.getHomeScore());
            holder.item_guest_full_score.setText(match.getGuestScore());
            holder.item_home_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));
            holder.item_guest_full_score.setTextColor(mContext.getResources().getColor(R.color.basket_score));



          /*  holder.item_football_full_score.setText(Html.fromHtml("<span><b>" + match.getHomeScore() + "</b></span><span>-</span><span><b>" + match.getGuestScore() + "</b></span>"));
            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.bg_header));
            holder.item_football_full_score.setVisibility(View.VISIBLE);*/


        } else if ("-14".equals(match.getStatusOrigin())) {// 推迟


            holder.keeptime.setText(mContext.getString(R.string.immediate_status_postpone));
            holder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));


            //  holder.item_football_full_score.setText(match.getHomeScore() + "-" + match.getGuestScore());
            //   holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.bg_header));


            holder.ll_half_score.setVisibility(View.INVISIBLE);
            holder.ll_all_score.setVisibility(View.INVISIBLE);




           /* holder.item_football_full_score.setText("VS");
            holder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.content_txt_grad));
            holder.item_football_full_score.setVisibility(View.VISIBLE);
            holder.item_football_half_score.setVisibility(View.INVISIBLE);
*/

        }

        if (match.getHomeTeamTextColorId() == R.color.red || match.getHomeTeamTextColorId() == R.color.content_txt_black) {
            holder.item_football_hometeam.setTextColor(match.getHomeTeamTextColorId());

        } else {
            holder.item_football_hometeam.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
        }
        if (match.getGuestTeamTextColorId() == R.color.red || match.getGuestTeamTextColorId() == R.color.content_txt_black) {
            holder.item_football_hometeam.setTextColor(match.getGuestTeamTextColorId());


        } else {
            holder.item_football_guestteam.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
        }

        if ("-10".equals(match.getStatusOrigin()) && "-11".equals(match.getStatusOrigin()) && "-14".equals(match.getStatusOrigin()) && !"0".equals(match.getStatusOrigin())
                && (match.getHomeScore() == null || match.getGuestScore() == null)) {
            //  holder.item_football_full_score.setText(Html.fromHtml("<span><b>0</b></span><span>-</span><span><b>0</b></span>"));
            holder.item_home_full_score.setText(0 + "");
            holder.item_guest_full_score.setText(0 + "");
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
            holder.item_home_half_score.setText(match.getHomeHalfScore());
            holder.item_guest_half_score.setText(match.getGuestHalfScore());

            // holder.item_football_half_score.setText("(" + match.getHomeHalfScore() + "-" + match.getGuestHalfScore() + ")");
            //holder.item_football_half_score.setVisibility(View.VISIBLE);


        } else if ("-12".equals(match.getStatusOrigin()) || "-13".equals(match.getStatusOrigin())) {// 中断和腰斩
            // 有中场比分显示中场比分
            if (match.getHomeHalfScore() != null && match.getGuestHalfScore() != null) {
                //  holder.item_football_half_score.setText("(" + match.getHomeHalfScore() + "-" + match.getGuestHalfScore() + ")");
                // holder.item_football_half_score.setVisibility(View.VISIBLE);

                holder.item_home_half_score.setText(match.getHomeHalfScore());
                holder.item_guest_half_score.setText(match.getGuestHalfScore());

            } else {
                // holder.item_football_half_score.setVisibility(View.INVISIBLE);

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

        // 盘口计算
        boolean alet = PreferenceUtil.getBoolean(MyConstants.RBSECOND, true);
        boolean asize = PreferenceUtil.getBoolean(MyConstants.rbSizeBall, false);
        boolean eur = PreferenceUtil.getBoolean(MyConstants.RBOCOMPENSATE, true);
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

        MatchOdd asiaLet = match.getMatchOdds().get("asiaLet");
        MatchOdd asiaSize = match.getMatchOdds().get("asiaSize");
        MatchOdd euro = match.getMatchOdds().get("euro");

        // 亚盘赔率
        if (alet) {
            setOddsData(holder.oddsTop1, holder.tv_odds_center1, holder.tv_odds_bottom1, asiaLet, match, 1);
        }
        // 大小盘赔率
        if (asize) {
            if (!alet) {
                setOddsData(holder.oddsTop1, holder.tv_odds_center1, holder.tv_odds_bottom1, asiaSize, match, 2);
            } else {
                setOddsData(holder.oddsTop2, holder.tv_odds_center2, holder.tv_odds_bottom2, asiaSize, match, 2);
            }
        }
        // 欧盘赔率
        if (eur) {
            if (!alet && !asize) {
                setOddsData(holder.oddsTop1, holder.tv_odds_center1, holder.tv_odds_bottom1, euro, match, 3);
            } else {
                setOddsData(holder.oddsTop2, holder.tv_odds_center2, holder.tv_odds_bottom2, euro, match, 3);
            }
        }

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

    private void setOddsData(TextView topView, TextView centerView, TextView bottomView, MatchOdd odds, Match match, int type) {

        if (odds == null) {
            topView.setText(" ");
            centerView.setText(" ");
            bottomView.setText(" ");
            return;
        }
        String handicapValue;
        switch (type) {
            case 1:
                handicapValue = HandicapUtils.changeHandicap(odds.getHandicapValue());
                break;
            case 2:
                handicapValue = HandicapUtils.changeHandicapByBigLittleBall(odds.getHandicapValue());
                break;
            case 3:
                handicapValue = odds.getMediumOdds();
                break;
            default:
                handicapValue = " ";
                break;
        }

        int keeptime = 0;
        if (match.getKeepTime() != null) {
            keeptime = Integer.parseInt(match.getKeepTime());
        }

        if ((!"-1".equals(match.getStatusOrigin()) && keeptime >= 89) || "-".equals(handicapValue) || "|".equals(handicapValue)) {// 封盘,完场不会有封盘的情况
            topView.setText(mContext.getString(R.string.immediate_status_entertained));
            centerView.setText(" ");
            bottomView.setText(" ");
        } else {
            centerView.setText(handicapValue);
            topView.setText(odds.getLeftOdds());
            bottomView.setText(odds.getRightOdds());
            topView.setTextColor(match.getLeftOddTextColorId() != 0 ? match.getLeftOddTextColorId() : mContext.getResources().getColor(R.color.content_txt_light_grad));
            bottomView.setTextColor(match.getRightOddTextColorId() != 0 ? match.getRightOddTextColorId() : mContext.getResources().getColor(R.color.content_txt_light_grad));
            centerView.setTextColor(match.getMidOddTextColorId() != 0 ? match.getMidOddTextColorId() : mContext.getResources().getColor(R.color.content_txt_black));
        }
    }

    public void updateDatas(List<ResultMatchDto> mMatch) {
        datas = mMatch;
    }
}
