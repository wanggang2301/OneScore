package com.hhly.mlottery.adapter.corner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.mikephil.charting.formatter.ColorFormatter;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballCornerActivity;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.bean.corner.CornerListBean;
import com.hhly.mlottery.callback.FocusMatchClickListener;
import com.hhly.mlottery.callback.RecyclerViewItemClickListener;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.ToastTools;

import java.util.List;

import static com.hhly.mlottery.activity.FootballMatchDetailActivity.BUNDLE_PARAM_THIRDID;

/**
 * 描    述：角球列表适配器
 * 作    者：mady@13322.com
 * 时    间：2017/5/19
 */
public class CornerListAdapter extends BaseQuickAdapter<CornerListBean.CornerEntity> {

    Context mContext;
    /**
     * 关注比赛
     */
    public FocusMatchClickListener mFocusMatchClickListener;

    public void setmFocusMatchClickListener(FocusMatchClickListener mFocusMatchClickListener) {
        this.mFocusMatchClickListener = mFocusMatchClickListener;
    }


    public CornerListAdapter(List<CornerListBean.CornerEntity> data, Context contxt) {
        super(R.layout.item_corner_list, data);
        this.mContext=contxt;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final CornerListBean.CornerEntity bean) {

        CornerListBean.CornerEntity.CornerMatchInfoEntity entity=bean.getCornerMatchInfo();
        TextView match_type=holder.getView(R.id.tv_match_type);
        RelativeLayout rl_score=holder.getView(R.id.rl_score);
        TextView match_state_started=holder.getView(R.id.match_state_started);


        holder.setText(R.id.item_football_racename,entity.getRname());
        holder.setTextColor(R.id.item_football_racename, Color.parseColor(entity.getRc()==null?"#669900":entity.getRc()));
        holder.setText(R.id.item_football_date,entity.getDate()==null?"":entity.getDate().substring(5,10));
        holder.setText(R.id.item_football_time,entity.getTime());
        holder.setText(R.id.keeptime,entity.getKeep());
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
            holder.setText(R.id.item_football_home_rc,entity.getHr()+"");
        }else {
            holder.getView(R.id.item_football_home_rc).setVisibility(View.GONE);
        }
        if(entity.getHy()!=0){
            holder.setText(R.id.item_football_home_yc,entity.getHy()+"");
        }else {
            holder.getView(R.id.item_football_home_yc).setVisibility(View.GONE);
        }
        if(entity.getGr()!=0){
            holder.setText(R.id.item_football_guest_rc,entity.getGr()+"");
        }else {
            holder.getView(R.id.item_football_guest_rc).setVisibility(View.GONE);
        }
        if(entity.getGy()!=0){
            holder.setText(R.id.item_football_guest_yc,entity.getGy()+"");
        }else {
            holder.getView(R.id.item_football_guest_yc).setVisibility(View.GONE);
        }

        //比分
        holder.setText(R.id.tv_home_half_score,entity.getHhc()+"");
        holder.setText(R.id.tv_home_full_score,entity.getHc()+"");
        holder.setText(R.id.tv_guest_half_score,entity.getGhc()+"");
        holder.setText(R.id.tv_guest_full_score,entity.getGc()+"");

        if(entity.getStatus().equals("0")){ //未开
            rl_score.setVisibility(View.GONE);
            match_type.setVisibility(View.VISIBLE);
            match_state_started.setVisibility(View.GONE);

        }else if(entity.getStatus().equals(1)){ //上半场
            rl_score.setVisibility(View.VISIBLE);
            match_type.setVisibility(View.GONE);
            match_state_started.setVisibility(View.VISIBLE);
            match_state_started.setText(MyApp.getContext().getString(R.string.fragme_home_shangbanchang_text));
            holder.setText(R.id.corner_first_odd,bean.getOd().getCpOdd()+"");
            holder.setText(R.id.corner_immediate_odd,bean.getOd().getJsOdd()+"");
            holder.setText(R.id.corner_higher,bean.getOd().getHigher()+"");
            holder.setText(R.id.corner_lower,bean.getOd().getBelow()+"");

        }else if(entity.getStatus().equals("2")){ //中场
            rl_score.setVisibility(View.VISIBLE);
            match_type.setVisibility(View.GONE);
            match_state_started.setVisibility(View.VISIBLE);
            match_state_started.setText(MyApp.getContext().getString(R.string.fragme_home_zhongchang_text));
            holder.setText(R.id.corner_first_odd,bean.getOd().getCpOdd()+"");
            holder.setText(R.id.corner_immediate_odd,bean.getOd().getJsOdd()+"");
            holder.setText(R.id.corner_higher,bean.getOd().getHigher()+"");
            holder.setText(R.id.corner_lower,bean.getOd().getBelow()+"");
        }else if(entity.getStatus().equals("3")){ //下半场
            rl_score.setVisibility(View.VISIBLE);
            match_type.setVisibility(View.GONE);
            match_state_started.setVisibility(View.VISIBLE);
            match_state_started.setText(MyApp.getContext().getString(R.string.fragme_home_xiabanchang_text));
            holder.setText(R.id.corner_first_odd,bean.getHod().getCpOdd()+"");
            holder.setText(R.id.corner_immediate_odd,bean.getHod().getJsOdd()+"");
            holder.setText(R.id.corner_higher,bean.getHod().getHigher()+"");
            holder.setText(R.id.corner_lower,bean.getHod().getBelow()+"");
        }else { //完场之类的
            rl_score.setVisibility(View.VISIBLE);
            match_type.setVisibility(View.GONE);
            match_state_started.setVisibility(View.VISIBLE);
            match_state_started.setText(MyApp.getContext().getString(R.string.fragme_home_wanchang_text));
            holder.setText(R.id.corner_first_odd,bean.getHod().getCpOdd()+"");
            holder.setText(R.id.corner_immediate_odd,bean.getHod().getJsOdd()+"");
            holder.setText(R.id.corner_higher,bean.getHod().getHigher()+"");
            holder.setText(R.id.corner_lower,bean.getHod().getBelow()+"");

        }
        final TextView frequency=holder.getView(R.id.item_football_frequency);

        if ("1".equals(entity.getStatus()) || "3".equals(entity.getStatus())) {// 显示秒的闪烁
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
