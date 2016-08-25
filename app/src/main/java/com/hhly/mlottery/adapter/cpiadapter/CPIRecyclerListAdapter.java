package com.hhly.mlottery.adapter.cpiadapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.enums.OddsTypeEnum;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.widget.CpiOddsItemView;

import java.util.List;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/6/21.
 */
public class CPIRecyclerListAdapter extends BaseQuickAdapter<NewOddsInfo.AllInfoBean> {

    int defaultTextColor;
    int primaryColor;
    int redColor;

    private String type; // 类型

    private List<NewOddsInfo.CompanyBean> companies; // 过滤的公司

    private OnItemClickListener onItemClickListener; // 点击监听
    private OnOddsClickListener onOddsClickListener; // 赔率点击监听

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnOddsClickListener(OnOddsClickListener onOddsClickListener) {
        this.onOddsClickListener = onOddsClickListener;
    }

    public CPIRecyclerListAdapter(@NonNull List<NewOddsInfo.AllInfoBean> items,
                                  @NonNull List<NewOddsInfo.CompanyBean> companies,
                                  @OddsTypeEnum.OddsType String type) {
        super(R.layout.item_cpi_odds_new, items);
        this.companies = companies;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder holder, final NewOddsInfo.AllInfoBean allInfoBean) {
        maybeInitColors(holder);

        // 联盟名称
        holder.setText(R.id.cpi_item_leagueName_txt, allInfoBean.getLeagueName())
                .setTextColor(R.id.cpi_item_leagueName_txt,
                        Color.parseColor(allInfoBean.getLeagueColor()));

        // 开始时间(或正在进行中的时间)
        NewOddsInfo.AllInfoBean.MatchInfoBean matchInfo = allInfoBean.getMatchInfo();
        holder.setText(R.id.cpi_item_time_txt, matchInfo.getOpenTime())
                .setText(R.id.cpi_host_team_txt, matchInfo.getMatchHomeName())
                .setText(R.id.cpi_guest_team_txt, matchInfo.getMatchGuestName());

        // 比赛时间
        TextView time = holder.getView(R.id.cpi_item_time_txt);
        // 比赛状态
        TextView status = holder.getView(R.id.tv_tag);
        // 分钟标记
        TextView second = holder.getView(R.id.cpi_item_seconds_txt);
        // 比分
        TextView score = holder.getView(R.id.cpi_score_txt);

        int intState = Integer.parseInt(matchInfo.getMatchState());
        // 大于 0 则表示进行中
        if (intState > 0) {
            // 比分、时间改为蓝色，显示分钟标记，隐藏状态文字
            time.setTextColor(primaryColor);
            score.setTextColor(primaryColor);
            second.setText("\'");
            // 显示中场状态，其它状态不显示
            if (intState == 2) {
                status.setVisibility(View.VISIBLE);
            } else {
                status.setVisibility(View.GONE);
            }
            status.setTextColor(primaryColor);
            score.setText(matchInfo.getMatchResult());
        } else {
            // 时间改为默认颜色
            time.setTextColor(defaultTextColor);
            second.setText("");
            status.setVisibility(View.VISIBLE);
            // 取消、待定、推迟、未开始
            if (intState == 0 || intState == -10 || intState == -11 || intState == -14) {
                // 未开，显示默认灰色
                score.setText(R.string.basket_VS);
                score.setTextColor(defaultTextColor);
                status.setTextColor(defaultTextColor);
            } else {
                // 结束或各种异常情况，显示红色
                score.setText(matchInfo.getMatchResult());
                score.setTextColor(redColor);
                status.setTextColor(redColor);
            }
        }

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(allInfoBean);
                }
            });
        }

        setStatusText(holder, matchInfo, intState);
        bindOdds(holder, allInfoBean);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        // 由于动画会在 viewHolder 回收的时候取消，所以在 attachedToWindow 中需要重新显示
        View view = holder.itemView.findViewById(R.id.cpi_item_seconds_txt);
        if (view != null) {
            setSecondAnim(view);
        }
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = super.onCreateDefViewHolder(parent, viewType);
        setTitleText(holder);
        return holder;
    }

    /**
     * 加载颜色
     */
    private void maybeInitColors(BaseViewHolder holder) {
        if (defaultTextColor == 0) {
            TextView textView = holder.getView(R.id.cpi_score_txt);
            defaultTextColor = textView.getCurrentTextColor();
        }
        if (primaryColor == 0) {
            primaryColor = ContextCompat.getColor(mContext, R.color.colorPrimary);
        }
        if (redColor == 0) {
            redColor = ContextCompat.getColor(mContext, R.color.analyze_left);
        }
    }

    /**
     * 设置标题文字
     */
    private void setTitleText(BaseViewHolder holder) {
        switch (type) {
            case OddsTypeEnum.PLATE:
                holder.setText(R.id.cpi_item_home_txt, mContext.getString(R.string.foot_odds_alet_left));
                holder.setText(R.id.cpi_item_odds_txt, mContext.getString(R.string.foot_odds_alet_middle));
                holder.setText(R.id.cpi_item_guest_txt, mContext.getString(R.string.foot_odds_alet_right));
                break;
            case OddsTypeEnum.BIG:
                holder.setText(R.id.cpi_item_home_txt, mContext.getString(R.string.foot_odds_asize_left));
                holder.setText(R.id.cpi_item_odds_txt, mContext.getString(R.string.foot_odds_asize_middle));
                holder.setText(R.id.cpi_item_guest_txt, mContext.getString(R.string.foot_odds_asize_right));
                break;
            case OddsTypeEnum.OP:
                holder.setText(R.id.cpi_item_home_txt, mContext.getString(R.string.foot_odds_eu_left));
                holder.setText(R.id.cpi_item_odds_txt, mContext.getString(R.string.foot_odds_eu_middle));
                holder.setText(R.id.cpi_item_guest_txt, mContext.getString(R.string.foot_odds_eu_right));
                break;
        }
    }

    /**
     * 设置状态文字
     *
     * @param status status
     */
    private void setStatusText(BaseViewHolder holder,
                               NewOddsInfo.AllInfoBean.MatchInfoBean matchInfo, int status) {

        TextView timeTextView = holder.getView(R.id.cpi_item_time_txt);
        TextView statusTextView = holder.getView(R.id.tv_tag);
        TextView second = holder.getView(R.id.cpi_item_seconds_txt);
        // 上半场 > 45 显示 45+
        // 下半场 > 90 显示 90+

        if (status == 2) {
            timeTextView.setVisibility(View.GONE);
        } else {
            timeTextView.setVisibility(View.VISIBLE);
        }

        if (status > 0) {
            try {
                String openTime = matchInfo.getOpenTime();
                int minute = Integer.parseInt(openTime);
                if (status == 1 && minute > 45) {
                    timeTextView.setText(R.string.forty_five_plus);
                } else if (status == 3 && minute > 90) {
                    timeTextView.setText(R.string.ninety_plus);
                } else if (status == 1 || status == 3) {
                    if (!TextUtils.isEmpty(openTime)) {
                        timeTextView.setText(openTime);
                    }
                }

                if (status == 2) {
                    second.setText("");
                } else {
                    second.setText("\'");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        switch (status) {
            case 0:
                statusTextView.setText(R.string.not_start_short_txt);
                break;
            case 1:
                // 上半场
                statusTextView.setText(R.string.fragme_home_shangbanchang_text);
                break;
            case 2:
                // 中场
                statusTextView.setText(R.string.fragme_home_zhongchang_text);
                break;
            case 3:
                // 下半场
                statusTextView.setText(R.string.fragme_home_xiabanchang_text);
                break;
            case 4:
                // 加时
                statusTextView.setText(R.string.fragme_home_jiaqiu_text);
                break;
            case 5:
                // 点球
                statusTextView.setText(R.string.fragme_home_dianqiu_text);
                break;
            case -1:
                statusTextView.setText(R.string.fragme_home_wanchang_text);
                break;
            case -10:
                statusTextView.setText(R.string.fragme_home_quxiao_text);
                break;
            case -11:
                statusTextView.setText(R.string.fragme_home_daiding_text);
                break;
            case -12:
                statusTextView.setText(R.string.fragme_home_yaozhan_text);
                break;
            case -13:
                statusTextView.setText(R.string.fragme_home_zhongduan_text);
                break;
            case -14:
                statusTextView.setText(R.string.fragme_home_tuichi_text);
                break;
        }
    }

    /**
     * 绑定赔率信息
     *
     * @param data data
     */
    private void bindOdds(BaseViewHolder holder, final NewOddsInfo.AllInfoBean data) {
        LinearLayout container = holder.getView(R.id.odds_container);
        container.removeAllViews();
        CpiOddsItemView cpiOddsItemView = null;
        List<NewOddsInfo.AllInfoBean.ComListBean> comList = data.getComList();
        for (final NewOddsInfo.AllInfoBean.ComListBean item : comList) {
            if (item.belongToShow(companies)) {
                cpiOddsItemView = new CpiOddsItemView(mContext);
                cpiOddsItemView.bindData(item, type);
                if (onOddsClickListener != null) {
                    cpiOddsItemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onOddsClickListener.onOddsClick(data, item);
                        }
                    });
                }
                container.addView(cpiOddsItemView);
            }
        }
        // 最后一个隐藏底部分割线
        if (cpiOddsItemView != null) {
            cpiOddsItemView.hideDivider();
        }
    }

    /**
     * 设置分钟动画效果
     */
    private void setSecondAnim(final View view) {
        final AlphaAnimation hideAnim = new AlphaAnimation(0, 0);
        hideAnim.setDuration(500);
        final AlphaAnimation showAnim = new AlphaAnimation(1, 1);
        showAnim.setDuration(500);
        hideAnim.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(showAnim);
            }
        });
        showAnim.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(hideAnim);
            }
        });
        view.startAnimation(hideAnim);
    }

    public interface OnItemClickListener {
        void onItemClick(NewOddsInfo.AllInfoBean item);
    }

    public interface OnOddsClickListener {
        void onOddsClick(NewOddsInfo.AllInfoBean item, NewOddsInfo.AllInfoBean.ComListBean odds);
    }

    abstract class AnimationListenerAdapter implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
