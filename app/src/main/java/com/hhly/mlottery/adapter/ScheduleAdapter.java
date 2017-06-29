package com.hhly.mlottery.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.scheduleBean.OddsBean;
import com.hhly.mlottery.bean.scheduleBean.ScheduleMatchDto;
import com.hhly.mlottery.bean.scheduleBean.ScheduleMatchOdd;
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
 * Created by asus1 on 2016/4/7.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ScheduleMatchDto> datas;
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


    public ScheduleAdapter(Context context, List<ScheduleMatchDto> datas, String teamLogoPre, String teamLogoSuff) {
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
            ScheduleViewHolder resultViewHolder = new ScheduleViewHolder(view);

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


 /*   public void setVisibility(boolean isVisible){
        RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)itemView.getLayoutParams();
        if (isVisible){
            param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            param.width = LinearLayout.LayoutParams.MATCH_PARENT;
            itemView.setVisibility(View.VISIBLE);
        }else{
            itemView.setVisibility(View.GONE);
            param.height = 0;
            param.width = 0;
        }
        itemView.setLayoutParams(param);
    }*/


    @Override
    public int getItemViewType(int position) {

        return VIEW_MATCH_INDEX;

      /*  if (0 == datas.get(position).getType()) {
            return VIEW_DATE_INDEX;
        } else if (1 == datas.get(position).getType()) {
            return VIEW_MATCH_INDEX;
        } else {
            return 100;
        }*/
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        if (datas.size() <= 0 || datas == null) {
            return;
        }

        final ScheduleMatchDto scheduleMatchDto = datas.get(i);
        if (holder instanceof DateViewHolder) {

            DateViewHolder dateViewHolder = (DateViewHolder) holder;
            dateViewHolder.ll_date_select.setVisibility(View.GONE);

            dateViewHolder.tv_date.setText(DateUtil.convertDateToNation(scheduleMatchDto.getDate()));
            dateViewHolder.tv_week.setText(ResultDateUtil.getWeekOfDate(DateUtil.parseDate(ResultDateUtil.getDate(0, scheduleMatchDto.getDate()))));

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

        } else if (holder instanceof ScheduleViewHolder) {

            if (scheduleMatchDto.getSchmatchs() != null) {

                holder.itemView.setTag(scheduleMatchDto.getSchmatchs().getThirdId());

                ScheduleViewHolder scheduleViewHolder = (ScheduleViewHolder) holder;

                // 完场描述
                scheduleViewHolder.view_line.setVisibility(TextUtils.isEmpty(scheduleMatchDto.getSchmatchs().getTxt()) ? View.GONE : View.VISIBLE);
                scheduleViewHolder.tv_item_desc.setVisibility(TextUtils.isEmpty(scheduleMatchDto.getSchmatchs().getTxt()) ? View.GONE : View.VISIBLE);
                String name = scheduleMatchDto.getSchmatchs().getWinner() == scheduleMatchDto.getSchmatchs().getHomeId() ? scheduleMatchDto.getSchmatchs().getHometeam() : scheduleMatchDto.getSchmatchs().getGuestteam();
                scheduleViewHolder.tv_item_desc.setText(scheduleMatchDto.getSchmatchs().getTxt() + "," + name + mContext.getResources().getString(R.string.roll_desc_txt));

                //主队url
                final String homelogourl = teamLogoPre + scheduleMatchDto.getSchmatchs().getHomeId().trim() + teamLogoSuff;
                //客队url
                final String guestlogourl = teamLogoPre + scheduleMatchDto.getSchmatchs().getGuestId().trim().trim() + teamLogoSuff;

                if (mContext != null) {
                    ImageLoader.load(mContext, homelogourl, R.mipmap.score_default).into(scheduleViewHolder.home_icon);
                    ImageLoader.load(mContext, guestlogourl, R.mipmap.score_default).into(scheduleViewHolder.guest_icon);
                }

                scheduleViewHolder.item_football_racename.setText(scheduleMatchDto.getSchmatchs().getRacename());
                if (scheduleMatchDto.getSchmatchs().getRaceColor() != null) {
                    scheduleViewHolder.item_football_racename.setTextColor(Color.parseColor(scheduleMatchDto.getSchmatchs().getRaceColor()));
                }
                scheduleViewHolder.item_football_time.setText(scheduleMatchDto.getSchmatchs().getTime());

                scheduleViewHolder.ll_half_score.setVisibility(View.INVISIBLE);
                scheduleViewHolder.ll_all_score.setVisibility(View.INVISIBLE);

                // scheduleViewHolder.item_football_half_score.setVisibility(View.INVISIBLE);
//                scheduleViewHolder.keeptime.setVisibility(View.GONE);
                scheduleViewHolder.item_football_frequency.setVisibility(View.GONE);
                scheduleViewHolder.item_football_home_yc.setVisibility(View.GONE);
                scheduleViewHolder.item_football_home_rc.setVisibility(View.GONE);
                scheduleViewHolder.item_football_guest_rc.setVisibility(View.GONE);
                scheduleViewHolder.item_football_guest_yc.setVisibility(View.GONE);

                scheduleViewHolder.item_football_hometeam.setText(scheduleMatchDto.getSchmatchs().getHometeam());
                // scheduleViewHolder.item_football_full_score.setText("VS");
                // scheduleViewHolder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.version));


                scheduleViewHolder.item_football_guestteam.setText(scheduleMatchDto.getSchmatchs().getGuestteam());

                ScheduleMatchOdd scheduleMatchOdd = scheduleMatchDto.getSchmatchs().getMatchOdds();
                if (scheduleMatchOdd != null) {

                    boolean alet = PreferenceUtil.getBoolean(MyConstants.RBSECOND, true);
                    boolean asize = PreferenceUtil.getBoolean(MyConstants.rbSizeBall, false);
                    boolean eur = PreferenceUtil.getBoolean(MyConstants.RBOCOMPENSATE, true);
                    boolean noshow = PreferenceUtil.getBoolean(MyConstants.RBNOTSHOW, false);

                    // 隐藏赔率
                    if (noshow) {
                        scheduleViewHolder.ll_odds_content1.setVisibility(View.GONE);
                        scheduleViewHolder.ll_odds_content2.setVisibility(View.GONE);
                    } else if ((asize && eur) || (asize && alet) || (eur && alet)) {
                        scheduleViewHolder.ll_odds_content1.setVisibility(View.VISIBLE);
                        scheduleViewHolder.ll_odds_content2.setVisibility(View.VISIBLE);
                    } else {
                        scheduleViewHolder.ll_odds_content1.setVisibility(View.VISIBLE);
                        scheduleViewHolder.ll_odds_content2.setVisibility(View.GONE);
                    }

                    OddsBean asiaLet = scheduleMatchOdd.getAsiaLet();
                    OddsBean asiaSize = scheduleMatchOdd.getAsiaSize();
                    OddsBean euro = scheduleMatchOdd.getEuro();

                    // 亚盘赔率
                    if (alet) {
                        setOddsData(scheduleViewHolder.oddsTop1, scheduleViewHolder.tv_odds_center1, scheduleViewHolder.tv_odds_bottom1, asiaLet, 1);
                    }
                    // 大小盘赔率
                    if (asize) {
                        if (!alet) {
                            setOddsData(scheduleViewHolder.oddsTop1, scheduleViewHolder.tv_odds_center1, scheduleViewHolder.tv_odds_bottom1, asiaSize, 2);
                        } else {
                            setOddsData(scheduleViewHolder.oddsTop2, scheduleViewHolder.tv_odds_center2, scheduleViewHolder.tv_odds_bottom2, asiaSize, 2);
                        }
                    }
                    // 欧盘赔率
                    if (eur) {
                        if (!alet && !asize) {
                            setOddsData(scheduleViewHolder.oddsTop1, scheduleViewHolder.tv_odds_center1, scheduleViewHolder.tv_odds_bottom1, euro, 3);
                        } else {
                            setOddsData(scheduleViewHolder.oddsTop2, scheduleViewHolder.tv_odds_center2, scheduleViewHolder.tv_odds_bottom2, euro, 3);
                        }
                    }
                }

                String focusIds = PreferenceUtil.getString(FocusFragment.FOCUS_ISD, "");
                String[] idArray = focusIds.split("[,]");
                for (String id : idArray) {
                    if (id.equals(scheduleMatchDto.getSchmatchs().getThirdId())) {
                        scheduleViewHolder.Iv_guangzhu.setImageResource(R.mipmap.football_focus);
                        scheduleViewHolder.Iv_guangzhu.setTag(true);
                        break;
                    } else {
                        scheduleViewHolder.Iv_guangzhu.setImageResource(R.mipmap.football_nomal);
                        scheduleViewHolder.Iv_guangzhu.setTag(false);
                    }
                }

                scheduleViewHolder.Iv_guangzhu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mFocusMatchClickListener != null) {
                            mFocusMatchClickListener.onClick(v, scheduleMatchDto.getSchmatchs().getThirdId());
                        }
                    }
                });

                // 比赛状态
                switch (scheduleMatchDto.getSchmatchs().getStatusOrigin()) {
                    case "0": // 未开赛
                        scheduleViewHolder.keeptime.setText(mContext.getResources().getString(R.string.tennis_match_not_start));
                        scheduleViewHolder.keeptime.setTextColor(mContext.getResources().getColor(R.color.res_pl_color));
                        break;
                    case "1": // 上半场进行时间
                        scheduleViewHolder.keeptime.setText("E");
                        scheduleViewHolder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
                        break;
                    case "2": // 中场
                        scheduleViewHolder.keeptime.setText(mContext.getString(R.string.immediate_status_midfield));
                        scheduleViewHolder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
                        break;
                    case "3": // 下半场进行时间
                        scheduleViewHolder.keeptime.setText("E");
                        scheduleViewHolder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
                        break;
                    case "4": // 加时
                        scheduleViewHolder.keeptime.setText(mContext.getString(R.string.immediate_status_overtime));
                        scheduleViewHolder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
                        break;
                    case "5": // 点球
                        scheduleViewHolder.keeptime.setText(mContext.getString(R.string.immediate_status_point));
                        scheduleViewHolder.keeptime.setTextColor(mContext.getResources().getColor(R.color.football_keeptime));
                        break;
                    case "-1": // 完场
                        scheduleViewHolder.keeptime.setText(mContext.getResources().getString(R.string.finish_txt));
                        scheduleViewHolder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));
                        break;
                    case "-10": // 取消
                        scheduleViewHolder.keeptime.setText(mContext.getString(R.string.immediate_status_cancel));
                        scheduleViewHolder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));
                        break;
                    case "-11": // 待定
                        scheduleViewHolder.keeptime.setText(mContext.getString(R.string.immediate_status_hold));
                        scheduleViewHolder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));
                        break;
                    case "-12": // 腰斩
                        scheduleViewHolder.keeptime.setText(mContext.getString(R.string.immediate_status_cut));
                        scheduleViewHolder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));
                        break;
                    case "-13": // 中断
                        scheduleViewHolder.keeptime.setText(mContext.getString(R.string.immediate_status_mesomere));
                        scheduleViewHolder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));
                        break;
                    case "-14": // 推迟
                        scheduleViewHolder.keeptime.setText(mContext.getString(R.string.immediate_status_postpone));
                        scheduleViewHolder.keeptime.setTextColor(mContext.getResources().getColor(R.color.red));
                        break;
                }

            }

        }
    }

    /**
     * 设置盘口数据
     *
     * @param topView
     * @param centerView
     * @param bottomView
     * @param odd
     */
    private void setOddsData(TextView topView, TextView centerView, TextView bottomView, OddsBean odd, int type) {
        if (odd == null) {
            topView.setText(" ");
            centerView.setText(" ");
            bottomView.setText(" ");
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
                handicapValue = " ";
                break;
        }

        topView.setText(odd.getLeftOdds() != null ? odd.getLeftOdds() : " ");
        centerView.setText(handicapValue != null ? handicapValue : " ");
        bottomView.setText(odd.getRightOdds() != null ? odd.getRightOdds() : " ");

        topView.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));
        bottomView.setTextColor(mContext.getResources().getColor(R.color.content_txt_light_grad));
        centerView.setTextColor(mContext.getResources().getColor(R.color.content_txt_black));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public void updateDatas(List<ScheduleMatchDto> mList) {
        this.datas = mList;
    }


    /**
     * 日期ViewHolder
     */

    public static class DateViewHolder extends RecyclerView.ViewHolder {

        protected static final String TAG = "TextViewHolder";

        LinearLayout ll_date_select;
        TextView tv_date;
        TextView tv_week;
        TextView handicapName1;
        TextView handicapName2;

        public DateViewHolder(View itemView) {
            super(itemView);
            ll_date_select = (LinearLayout) itemView.findViewById(R.id.ll_date_select);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_week = (TextView) itemView.findViewById(R.id.tv_week);
            handicapName1 = (TextView) itemView.findViewById(R.id.tv_handicap_name1);
            handicapName2 = (TextView) itemView.findViewById(R.id.tv_handicap_name2);
        }
    }

    /**
     * 数据ViewHolder
     */

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        TextView keeptime;

        TextView item_football_racename;
        TextView item_football_time;
        // TextView item_football_half_score;
        TextView item_football_frequency;
        TextView item_football_home_yc;
        TextView item_football_home_rc;
        TextView item_football_guest_rc;
        TextView item_football_guest_yc;
        TextView item_football_hometeam;
        // TextView item_football_full_score;
        TextView item_football_guestteam;
        //        TextView item_football_left_odds;
//        TextView item_football_handicap_value;
//        TextView item_football_right_odds;
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

        public ScheduleViewHolder(final View itemView) {
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
//            item_football_left_odds = (TextView) itemView.findViewById(R.id.item_football_left_odds);
//            item_football_handicap_value = (TextView) itemView.findViewById(R.id.item_football_handicap_value);
//            item_football_right_odds = (TextView) itemView.findViewById(R.id.item_football_right_odds);


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
}
