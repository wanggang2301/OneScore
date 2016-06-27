package com.hhly.mlottery.adapter.cpiadapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.frame.oddfragment.CPIOddsFragment;
import com.hhly.mlottery.widget.CpiOddsItemView;

import java.util.List;

/**
 * 足球指数 - 指数列表适配器
 * <p>
 * Created by loshine on 2016/6/21.
 */
public class CPIRecyclerListAdapter extends RecyclerView.Adapter<CPIRecyclerListAdapter.ViewHolder> {

    private String type; // 类型

    private List<NewOddsInfo.AllInfoBean> items; // 数据源
    private List<NewOddsInfo.CompanyBean> companies; // 过滤的公司

    private OnItemClickListener onItemClickListener; // 监听器
    private OnOddsClickListener onOddsClickListener; // 赔率点击监听

    public void setOnOddsClickListener(OnOddsClickListener onOddsClickListener) {
        this.onOddsClickListener = onOddsClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CPIRecyclerListAdapter(@NonNull List<NewOddsInfo.AllInfoBean> items,
                                  @NonNull List<NewOddsInfo.CompanyBean> companies,
                                  @CPIOddsFragment.Type String type) {
        this.items = items;
        this.companies = companies;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cpi_odds_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // 直接调用 holder 的 绑定数据方法
        holder.bindData(items.get(position));
        // 设置点击监听
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(items.get(holder.getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        // 由于动画会在 viewholder 回收的时候取消，所以在 attachedToWindow 中需要重新显示
        holder.setSecondAnim();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private Context mContext;

        TextView mLeagueName; // 联盟名称
        TextView mTime; // 比赛时间
        TextView mStatus; // 比赛状态
        TextView mSecond; // 分钟标记

        TextView mHostTeam; // 主队名称
        TextView mGuestTeam; // 客队名称
        TextView mScore; // 比分

        TextView mLeftOddsTitle; // 左标题
        TextView mCenterOddsTitle; // 中标题
        TextView mRightOddsTitle; // 右标题

        LinearLayout mContainer; // 赔率信息容器

        int defaultTextColor;
        int primaryColor;
        int redColor;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();

            mLeagueName = (TextView) itemView.findViewById(R.id.cpi_item_leagueName_txt);
            mTime = (TextView) itemView.findViewById(R.id.cpi_item_time_txt);
            mStatus = (TextView) itemView.findViewById(R.id.tv_tag);
            mSecond = (TextView) itemView.findViewById(R.id.cpi_item_seconds_txt);

            mHostTeam = (TextView) itemView.findViewById(R.id.cpi_host_team_txt);
            mGuestTeam = (TextView) itemView.findViewById(R.id.cpi_guest_team_txt);
            mScore = (TextView) itemView.findViewById(R.id.cpi_score_txt);

            mLeftOddsTitle = (TextView) itemView.findViewById(R.id.cpi_item_home_txt);
            mCenterOddsTitle = (TextView) itemView.findViewById(R.id.cpi_item_odds_txt);
            mRightOddsTitle = (TextView) itemView.findViewById(R.id.cpi_item_guest_txt);

            mContainer = (LinearLayout) itemView.findViewById(R.id.odds_container);

            setTitleText();
        }

        /**
         * 设置分钟动画效果
         */
        private void setSecondAnim() {
            final AlphaAnimation hideAnim = new AlphaAnimation(0, 0);
            hideAnim.setDuration(500);
            final AlphaAnimation showAnim = new AlphaAnimation(1, 1);
            showAnim.setDuration(500);
            hideAnim.setAnimationListener(new AnimationListenerAdapter() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    mSecond.startAnimation(showAnim);
                }
            });
            showAnim.setAnimationListener(new AnimationListenerAdapter() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    mSecond.startAnimation(hideAnim);
                }
            });
            mSecond.startAnimation(hideAnim);
        }

        /**
         * 绑定数据
         *
         * @param data data
         */
        public void bindData(NewOddsInfo.AllInfoBean data) {
            maybeInitColors();

            // 联盟名称
            mLeagueName.setText(data.getLeagueName());
            mLeagueName.setTextColor(Color.parseColor(data.getLeagueColor()));

            // 开始时间(或正在进行中的时间)
            NewOddsInfo.AllInfoBean.MatchInfoBean matchInfo = data.getMatchInfo();
            mTime.setText(matchInfo.getOpenTime());
            mHostTeam.setText(matchInfo.getMatchHomeName());
            mGuestTeam.setText(matchInfo.getMatchGuestName());
            int intState = Integer.parseInt(matchInfo.getMatchState());
            // 大于 0 则表示进行中
            if (intState > 0) {
                // 比分、时间改为蓝色，显示分钟标记，隐藏状态文字
                mTime.setTextColor(primaryColor);
                mScore.setTextColor(primaryColor);
                mSecond.setText("\'");
                // 显示中场状态，其它状态不显示
                if (intState == 2) {
                    mStatus.setVisibility(View.VISIBLE);
                } else {
                    mStatus.setVisibility(View.GONE);
                }
                mStatus.setTextColor(primaryColor);
                mScore.setText(matchInfo.getMatchResult());
            } else {
                // 时间改为默认颜色
                mTime.setTextColor(defaultTextColor);
                mSecond.setText("");
                mStatus.setVisibility(View.VISIBLE);
                if (intState == 0) {
                    // 未开，显示默认灰色
                    mScore.setText(R.string.basket_VS);
                    mScore.setTextColor(defaultTextColor);
                    mStatus.setTextColor(defaultTextColor);
                } else {
                    // 结束或各种异常情况，显示红色
                    mScore.setTextColor(redColor);
                    mStatus.setTextColor(redColor);
                    mScore.setText(matchInfo.getMatchResult());
                }
            }

            setStatusText(matchInfo, intState);
            bindOdds(data);
        }

        /**
         * 绑定赔率信息
         *
         * @param data data
         */
        private void bindOdds(final NewOddsInfo.AllInfoBean data) {
            mContainer.removeAllViews();
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
                    mContainer.addView(cpiOddsItemView);
                }
            }
            // 最后一个隐藏底部分割线
            if (cpiOddsItemView != null) {
                cpiOddsItemView.hideDivider();
            }
        }

        /**
         * 设置标题文字
         */
        private void setTitleText() {
            switch (type) {
                case CPIOddsFragment.TYPE_PLATE:
                    mLeftOddsTitle.setText(R.string.odd_home_txt);
                    mCenterOddsTitle.setText(R.string.odd_dish_txt);
                    mRightOddsTitle.setText(R.string.odd_guest_txt);
                    break;
                case CPIOddsFragment.TYPE_BIG:
                    mLeftOddsTitle.setText(R.string.odd_home_big_txt);
                    mCenterOddsTitle.setText(R.string.odd_dish_txt);
                    mRightOddsTitle.setText(R.string.odd_guest_big_txt);
                    break;
                case CPIOddsFragment.TYPE_OP:
                    mLeftOddsTitle.setText(R.string.odd_home_op_txt);
                    mCenterOddsTitle.setText(R.string.odd_dish_op_txt);
                    mRightOddsTitle.setText(R.string.odd_guest_op_txt);
                    break;
            }
        }

        /**
         * 设置状态文字
         *
         * @param status status
         */
        private void setStatusText(NewOddsInfo.AllInfoBean.MatchInfoBean matchInfo, int status) {
            // 上半场 > 45 显示 45+
            // 下班场 > 90 显示 90+
            if (status > 0) {
                try {
                    int minute = Integer.parseInt(matchInfo.getOpenTime());
                    if (minute > 45) {
                        mTime.setText(R.string.forty_five_plus);
                    } else if (minute > 90) {
                        mTime.setText(R.string.ninety_plus);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            switch (status) {
                case 0:
                    mStatus.setText(R.string.not_start_short_txt);
                    break;
                case 1:
                    // 上半场
                    mStatus.setText(R.string.fragme_home_shangbanchang_text);
                    break;
                case 2:
                    // 中场
                    mStatus.setText(R.string.fragme_home_zhongchang_text);
                    break;
                case 3:
                    // 下半场
                    mStatus.setText(R.string.fragme_home_xiabanchang_text);
                    break;
                case 4:
                    // 加时
                    mStatus.setText(R.string.fragme_home_jiaqiu_text);
                    break;
                case 5:
                    // 点球
                    mStatus.setText(R.string.fragme_home_dianqiu_text);
                    break;
                case -1:
                    mStatus.setText(R.string.fragme_home_wanchang_text);
                    break;
                case -10:
                    mStatus.setText(R.string.fragme_home_quxiao_text);
                    break;
                case -11:
                    mStatus.setText(R.string.fragme_home_daiding_text);
                    break;
                case -12:
                    mStatus.setText(R.string.fragme_home_yaozhan_text);
                    break;
                case -13:
                    mStatus.setText(R.string.fragme_home_zhongduan_text);
                    break;
                case -14:
                    mStatus.setText(R.string.fragme_home_tuichi_text);
                    break;
            }
        }

        /**
         * 加载颜色
         */
        private void maybeInitColors() {
            if (defaultTextColor == 0) {
                defaultTextColor = mScore.getCurrentTextColor();
            }
            if (primaryColor == 0) {
                primaryColor = ContextCompat.getColor(mContext, R.color.colorPrimary);
            }
            if (redColor == 0) {
                redColor = ContextCompat.getColor(mContext, R.color.analyze_left);
            }
        }
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
