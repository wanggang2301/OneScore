package com.hhly.mlottery.adapter.corner;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.ScheduleDateAdapter;
import com.hhly.mlottery.bean.corner.CornerListBean;
import com.hhly.mlottery.bean.scheduleBean.ScheduleDate;
import com.hhly.mlottery.callback.CornerDateListerner;
import com.hhly.mlottery.callback.FocusMatchClickListener;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 描    述：角球列表适配器
 * 作    者：mady@13322.com
 * 时    间：2017/5/19
 */
public class CornerListAdapter extends BaseQuickAdapter<CornerListBean.CornerEntity> {

    Context mContext;
    private int mCurruntDatePosition;
    private CornerDateListerner mDateListerner;
    private boolean mChange=true;

    List<ScheduleDate> mDatelist = new ArrayList<>();
    /**
     * 关注比赛
     */
    public FocusMatchClickListener mFocusMatchClickListener;

    public void setmFocusMatchClickListener(FocusMatchClickListener mFocusMatchClickListener) {
        this.mFocusMatchClickListener = mFocusMatchClickListener;
    }


    public CornerListAdapter(List<CornerListBean.CornerEntity> data, Context contxt ,List<ScheduleDate> mDateList) {
        super(R.layout.item_corner_list, data);
        this.mContext=contxt;
        this.mDatelist=mDateList;
    }

    public void setDateListener(CornerDateListerner listener){
        this.mDateListerner=listener;
    }
    /**
     * 通知Adapter当前分页页数
     */
    public void setCurrentDate(int mCurrentDatePosition ,boolean changeDate){
        this.mCurruntDatePosition=mCurrentDatePosition;
        mChange=changeDate;
    }


    @Override
    protected void convert(final BaseViewHolder holder, final CornerListBean.CornerEntity bean) {

        if(bean.getCornerMatchInfo().getDate()!=null&&bean.getCornerMatchInfo().getTime()!=null){
            bean.setDateTag(bean.getCornerMatchInfo().getDate());
            String date[]=bean.getCornerMatchInfo().getTime().split(":");

            if(Integer.parseInt(date[0])<10){ //10点之前。日期减1
                bean.setDateTag(DateUtil.getDate(-1,bean.getCornerMatchInfo().getDate())); //10点之前日期减1
            }
            if(date[0].equals("10")&&date[1].equals("00")){ //10点整的属于前一天
                bean.setDateTag(DateUtil.getDate(-1,bean.getCornerMatchInfo().getDate()));
            }
            if(!bean.getDateTag().equals(mDatelist.get(mCurruntDatePosition).getDate())&&mChange){ //item日期跟头部日期不一样，切不是刚分页。则把现在的日期传回去设置
                //并且把当前日期所在list的position传回去。不然的话。在下拉刷新的时候。还是最末页的加载数据。跟头部日期不符了就
//            ToastTools.showQuick(mContext,bean.getDateTag());

                for(int i=0;i<mDatelist.size();i++){
                    if(mDatelist.get(i).getDate().equals(bean.getDateTag())){
                        mDateListerner.setDate(bean.getDateTag(),i);
                    }
                }
            }
        }

        CornerListBean.CornerEntity.CornerMatchInfoEntity entity=bean.getCornerMatchInfo();
        TextView match_type=holder.getView(R.id.tv_match_type);
        RelativeLayout rl_score=holder.getView(R.id.rl_score);
        TextView match_state_started_half=holder.getView(R.id.match_state_started_half);
        TextView match_state_started_full=holder.getView(R.id.match_state_started_full);


        holder.setText(R.id.item_football_racename,entity.getRname());
        holder.setTextColor(R.id.item_football_racename, Color.parseColor(entity.getRc()==null?"#669900":entity.getRc()));
        holder.setText(R.id.item_football_date,entity.getDate()==null?"":entity.getDate().substring(5,10));
        holder.setText(R.id.item_football_time,entity.getTime());
        holder.setText(R.id.keeptime,entity.getKeep()==null?"":entity.getKeep().equals("0")?"":entity.getKeep());
        if(entity.getStatus().equals("2")){ //中场
            holder.setText(R.id.keeptime,mContext.getString(R.string.fragme_home_zhongchang_text));
        }
        if(entity.getStatus().equals("1")&&entity.getKeep()!=null&&Integer.parseInt(entity.getKeep())>45){ //上半场大于45
            holder.setText(R.id.keeptime,"45+");
        }
        if(entity.getStatus().equals("3")&&entity.getKeep()!=null&&Integer.parseInt(entity.getKeep())>90){ //上半场大于45
            holder.setText(R.id.keeptime,"90+");
        }
        //图标
        ImageView homeIcon=holder.getView(R.id.home_icon);
        ImageView guestIcon=holder.getView(R.id.guest_icon);
        ImageLoader.load(MyApp.getContext(),entity.getHpic(),R.mipmap.score_default).into(homeIcon);
        ImageLoader.load(MyApp.getContext(),entity.getGpic(),R.mipmap.score_default).into(guestIcon);
        //队名
        holder.setText(R.id.item_football_hometeam,entity.getHtn());
        holder.setText(R.id.item_football_guestteam,entity.getGtn());
        //红黄牌
        if(entity.getHr()!=0){
            holder.getView(R.id.item_football_home_rc).setVisibility(View.VISIBLE);
            holder.setText(R.id.item_football_home_rc,entity.getHr()+"");
        }else {
            holder.getView(R.id.item_football_home_rc).setVisibility(View.GONE);
        }
        if(entity.getHy()!=0){
            holder.getView(R.id.item_football_home_yc).setVisibility(View.VISIBLE);
            holder.setText(R.id.item_football_home_yc,entity.getHy()+"");
        }else {
            holder.getView(R.id.item_football_home_yc).setVisibility(View.GONE);
        }
        if(entity.getGr()!=0){
            holder.getView(R.id.item_football_guest_rc).setVisibility(View.VISIBLE);
            holder.setText(R.id.item_football_guest_rc,entity.getGr()+"");
        }else {
            holder.getView(R.id.item_football_guest_rc).setVisibility(View.GONE);
        }
        if(entity.getGy()!=0){
            holder.getView(R.id.item_football_guest_yc).setVisibility(View.VISIBLE);
            holder.setText(R.id.item_football_guest_yc,entity.getGy()+"");
        }else {
            holder.getView(R.id.item_football_guest_yc).setVisibility(View.GONE);
        }

        //比分

        holder.setText(R.id.tv_home_full_score,entity.getHc()+"");
        holder.setText(R.id.tv_guest_full_score,entity.getGc()+"");
        if(entity.getStatus().equals("1")){ //不显示半场角球
            holder.getView(R.id.tv_home_half_score).setVisibility(View.GONE);
            holder.getView(R.id.tv_guest_half_score).setVisibility(View.GONE);
        }else if(entity.getStatus().equals("2")||entity.getStatus().equals("3")||entity.getStatus().equals("-1")){
            holder.setText(R.id.tv_home_half_score,entity.getHhc()+"");
            holder.setText(R.id.tv_guest_half_score,entity.getGhc()+"");
            holder.getView(R.id.tv_home_half_score).setVisibility(View.VISIBLE);
            holder.getView(R.id.tv_guest_half_score).setVisibility(View.VISIBLE);
        }

        TextView mTv_immediate=holder.getView(R.id.corner_immediate_odd);
        TextView mTv_higher=holder.getView(R.id.corner_higher);
        TextView mTv_lower=holder.getView(R.id.corner_lower);



        holder.setText(R.id.corner_first_odd,bean.getOd().getCpOdd()+"");
        holder.setText(R.id.corner_immediate_odd,bean.getOd().getJsOdd()+"");
        holder.setText(R.id.corner_higher,bean.getOd().getHigher()+"");
        holder.setText(R.id.corner_lower,bean.getOd().getBelow()+"");

        String immediate=mTv_immediate.getText().toString();
        String higher=mTv_higher.getText().toString();
        String lower=mTv_lower.getText().toString();

        if(bean.getOd().getHigher()>Double.parseDouble(higher)){ //高于
            mTv_higher.setTextColor(ContextCompat.getColor(mContext,R.color.analyze_left));
        } else if(bean.getOd().getHigher()<Double.parseDouble(higher)){
            mTv_higher.setTextColor(ContextCompat.getColor(mContext,R.color.fall_color));
        } else {
            mTv_higher.setTextColor(ContextCompat.getColor(mContext,R.color.mdy_333));
        }

        if(bean.getOd().getJsOdd()>Double.parseDouble(immediate)){ //红升绿降
            mTv_immediate.setTextColor(ContextCompat.getColor(mContext,R.color.analyze_left));
        } else if(bean.getOd().getJsOdd()<Double.parseDouble(immediate)){
            mTv_immediate.setTextColor(ContextCompat.getColor(mContext,R.color.fall_color));
        } else {
            mTv_immediate.setTextColor(ContextCompat.getColor(mContext,R.color.mdy_333));
        }


        if(bean.getOd().getBelow()>Double.parseDouble(lower)){ //低于
            mTv_lower.setTextColor(ContextCompat.getColor(mContext,R.color.analyze_left));
        } else if(bean.getOd().getBelow()<Double.parseDouble(lower)){
            mTv_lower.setTextColor(ContextCompat.getColor(mContext,R.color.fall_color));
        } else {
            mTv_lower.setTextColor(ContextCompat.getColor(mContext,R.color.mdy_333));
        }


        if(entity.getStatus().equals("0")){ //未开
            rl_score.setVisibility(View.GONE);
            match_type.setVisibility(View.VISIBLE);
            match_state_started_half.setVisibility(View.GONE);
            match_state_started_full.setVisibility(View.GONE);

        }else if(entity.getStatus().equals("-1")){ //完场
            rl_score.setVisibility(View.VISIBLE);
            match_type.setVisibility(View.GONE);
            match_state_started_half.setVisibility(View.VISIBLE);
            match_state_started_full.setVisibility(View.VISIBLE);
            match_state_started_half.setText(MyApp.getContext().getString(R.string.number_info_half)); //半
            match_state_started_full.setText(MyApp.getContext().getString(R.string.number_info_full)); //全
            holder.setTextColor(R.id.tv_home_full_score, ContextCompat.getColor(mContext,R.color.red));
            holder.setTextColor(R.id.tv_guest_full_score,ContextCompat.getColor(mContext,R.color.red));
        }else { //半场
            rl_score.setVisibility(View.VISIBLE);
            match_type.setVisibility(View.GONE);
            if(entity.getStatus().equals("1")){
                match_state_started_half.setVisibility(View.GONE);
            }else{
                match_state_started_half.setVisibility(View.VISIBLE);
            }
            match_state_started_full.setVisibility(View.GONE);
            match_state_started_half.setText(MyApp.getContext().getString(R.string.number_info_half));
            holder.setTextColor(R.id.tv_home_full_score, ContextCompat.getColor(mContext,R.color.colorPrimary));
            holder.setTextColor(R.id.tv_guest_full_score,ContextCompat.getColor(mContext,R.color.colorPrimary));
        }

        final TextView frequency=holder.getView(R.id.item_football_frequency);

        if ("1".equals(entity.getStatus()) || "3".equals(entity.getStatus())&&!entity.getKeep().equals("")&&!entity.getKeep().equals("0")) {// 显示秒的闪烁
            //握手进场时给的时间是0.不显示
            frequency.setText("\'");
            frequency.setVisibility(View.VISIBLE);

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
                    frequency.startAnimation(anim2);
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
                    frequency.startAnimation(anim1);

                }
            });
            frequency.startAnimation(anim1);


        } else {
            frequency.setText("");
            frequency.setVisibility(View.GONE);
            frequency.clearAnimation();
        }
        ImageView focus=holder.getView(R.id.Iv_guangzhu);

        String focusIds = PreferenceUtil.getString(StaticValues.CORNER_FOCUS_ID, "");
        String[] idArray = focusIds.split("[,]");

        for (String id : idArray) {
            if (id.equals(bean.getMatchId()+"")) {
                focus.setImageResource(R.mipmap.football_focus);
                focus.setTag(true);
                break;
            } else {
                focus.setImageResource(R.mipmap.football_nomal);
                focus.setTag(false);
            }
        }

        focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFocusMatchClickListener != null) {
                    mFocusMatchClickListener.onClick(v, bean.getMatchId()+"");
                }
            }
        });


    }




}
