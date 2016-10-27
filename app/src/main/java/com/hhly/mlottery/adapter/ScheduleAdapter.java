package com.hhly.mlottery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.scheduleBean.AsiaLet;
import com.hhly.mlottery.bean.scheduleBean.AsiaSize;
import com.hhly.mlottery.bean.scheduleBean.Euro;
import com.hhly.mlottery.bean.scheduleBean.ScheduleMatchDto;
import com.hhly.mlottery.bean.scheduleBean.ScheduleMatchOdd;
import com.hhly.mlottery.callback.DateOnClickListener;
import com.hhly.mlottery.callback.FocusMatchClickListener;
import com.hhly.mlottery.callback.RecyclerViewItemClickListener;
import com.hhly.mlottery.frame.footframe.FocusFragment;
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_football_new, viewGroup, false);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        if (datas.size()<=0 || datas==null){
            return;
        }

        final ScheduleMatchDto scheduleMatchDto = datas.get(i);
        if (holder instanceof DateViewHolder) {
            ((DateViewHolder) holder).tv_date.setText(scheduleMatchDto.getDate());
            ((DateViewHolder) holder).tv_week.setText(ResultDateUtil.getWeekOfDate(DateUtil.parseDate(ResultDateUtil.getDate(0, scheduleMatchDto.getDate()))));

        } else if (holder instanceof ScheduleViewHolder) {

            if (scheduleMatchDto.getSchmatchs() != null) {

                holder.itemView.setTag(scheduleMatchDto.getSchmatchs().getThirdId());

                ScheduleViewHolder scheduleViewHolder = (ScheduleViewHolder) holder;


                //主队url
                final String homelogourl = teamLogoPre + scheduleMatchDto.getSchmatchs().getHomeId().trim() + teamLogoSuff;  //"http://pic.13322.com/basketball/team/135_135/29.png"
                //客队url
                final String guestlogourl = teamLogoPre + scheduleMatchDto.getSchmatchs().getGuestId().trim().trim() + teamLogoSuff;


                //scheduleViewHolder.home_icon.setTag(homelogourl);
                //scheduleViewHolder.guest_icon.setTag(guestlogourl);
                //ImagaeLoader -- 加载图片
                ImageLoader.load(mContext,homelogourl,R.mipmap.score_default).into(scheduleViewHolder.home_icon);

                ImageLoader.load(mContext,guestlogourl,R.mipmap.score_default).into(scheduleViewHolder.guest_icon);


                scheduleViewHolder.item_football_racename.setText(scheduleMatchDto.getSchmatchs().getRacename());
                scheduleViewHolder.item_football_racename.setTextColor(Color.parseColor(scheduleMatchDto.getSchmatchs().getRaceColor()));
                scheduleViewHolder.item_football_time.setText(scheduleMatchDto.getSchmatchs().getTime());
                scheduleViewHolder.item_football_half_score.setVisibility(View.INVISIBLE);
                scheduleViewHolder.keeptime.setVisibility(View.GONE);
                scheduleViewHolder.item_football_frequency.setVisibility(View.GONE);
                // scheduleViewHolder.item_football_right_mid_score.setVisibility(View.GONE);
                scheduleViewHolder.item_football_home_yc.setVisibility(View.GONE);
                scheduleViewHolder.item_football_home_rc.setVisibility(View.GONE);
                scheduleViewHolder.item_football_guest_rc.setVisibility(View.GONE);
                scheduleViewHolder.item_football_guest_yc.setVisibility(View.GONE);

                scheduleViewHolder.item_football_hometeam.setText(scheduleMatchDto.getSchmatchs().getHometeam());
                scheduleViewHolder.item_football_full_score.setText("VS");
                scheduleViewHolder.item_football_full_score.setTextColor(mContext.getResources().getColor(R.color.version));

                scheduleViewHolder.item_football_guestteam.setText(scheduleMatchDto.getSchmatchs().getGuestteam());

                ScheduleMatchOdd scheduleMatchOdd = scheduleMatchDto.getSchmatchs().getMatchOdds();
                if (scheduleMatchOdd != null) {

                    boolean asize = PreferenceUtil.getBoolean(MyConstants.rbSizeBall, false);
                    boolean eur = PreferenceUtil.getBoolean(MyConstants.RBOCOMPENSATE, false);
                    boolean alet = PreferenceUtil.getBoolean(MyConstants.RBSECOND, true);
                    boolean noshow = PreferenceUtil.getBoolean(MyConstants.RBNOTSHOW, false);

                    // 大小球
                    if (asize) {
                        AsiaSize asiaSize = scheduleMatchOdd.getAsiaSize();

                        if (asiaSize != null) {
                            scheduleViewHolder.item_football_left_odds.setText(asiaSize.getLeftOdds());
                            scheduleViewHolder.item_football_handicap_value.setText(HandicapUtils.changeHandicapByBigLittleBall(asiaSize.getHandicapValue()));
                            scheduleViewHolder.item_football_right_odds.setText(asiaSize.getRightOdds());
                        } else {
                            scheduleViewHolder.item_football_left_odds.setText("");
                            scheduleViewHolder.item_football_handicap_value.setText("");
                            scheduleViewHolder.item_football_right_odds.setText("");
                        }
                    }

                    // 欧赔
                    if (eur) {
                        Euro euro = scheduleMatchOdd.getEuro();
                        if (euro != null) {
                            scheduleViewHolder.item_football_left_odds.setText(euro.getLeftOdds());
                            scheduleViewHolder.item_football_handicap_value.setText(euro.getMediumOdds());
                            scheduleViewHolder.item_football_right_odds.setText(euro.getRightOdds());
                        } else {
                            scheduleViewHolder.item_football_left_odds.setText("");
                            scheduleViewHolder.item_football_handicap_value.setText("");
                            scheduleViewHolder.item_football_right_odds.setText("");
                        }
                    }

                    // 亚赔
                    if (alet) {
                        AsiaLet asiaLet = scheduleMatchOdd.getAsiaLet();
                        if (asiaLet != null) {
                            scheduleViewHolder.item_football_left_odds.setText(asiaLet.getLeftOdds());
                            scheduleViewHolder.item_football_handicap_value.setText(HandicapUtils.changeHandicap(asiaLet.getHandicapValue()));
                            scheduleViewHolder.item_football_right_odds.setText(asiaLet.getRightOdds());
                        } else {
                            scheduleViewHolder.item_football_left_odds.setText("");
                            scheduleViewHolder.item_football_handicap_value.setText("");
                            scheduleViewHolder.item_football_right_odds.setText("");
                        }
                    }

                    if (noshow) {

                        scheduleViewHolder.item_football_left_odds.setText("");
                        scheduleViewHolder.item_football_handicap_value.setText("");
                        scheduleViewHolder.item_football_right_odds.setText("");
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

            }

        }
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

        TextView tv_date;
        TextView tv_week;

        public DateViewHolder(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_week = (TextView) itemView.findViewById(R.id.tv_week);
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
        TextView item_football_half_score;
        TextView item_football_frequency;
        // TextView item_football_right_mid_score;
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
        //关注
        ImageView Iv_guangzhu;


        ImageView home_icon;
        ImageView guest_icon;

        public ScheduleViewHolder(final View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            keeptime = (TextView) itemView.findViewById(R.id.keeptime);

            item_football_racename = (TextView) itemView.findViewById(R.id.item_football_racename);
            item_football_time = (TextView) itemView.findViewById(R.id.item_football_time);
            item_football_half_score = (TextView) itemView.findViewById(R.id.item_football_half_score);
            item_football_frequency = (TextView) itemView.findViewById(R.id.item_football_frequency);
            // item_football_right_mid_score = (TextView) itemView.findViewById(R.id.item_football_right_mid_score);
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

            Iv_guangzhu = (ImageView) itemView.findViewById(R.id.Iv_guangzhu);

            home_icon = (ImageView) itemView.findViewById(R.id.home_icon);
            guest_icon = (ImageView) itemView.findViewById(R.id.guest_icon);
        }

    }
}
