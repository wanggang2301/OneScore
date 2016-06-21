package com.hhly.mlottery.adapter.cpiadapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.frame.oddfragment.CPIOddsListFragment;
import com.hhly.mlottery.widget.CpiOddsItemView;

import java.util.List;

/**
 * 足球指数 - 指数列表适配器
 * <p>
 * Created by loshine on 2016/6/21.
 */
public class CPIRecyclerListAdapter extends RecyclerView.Adapter<CPIRecyclerListAdapter.ViewHolder> {

    private String type;

    private List<NewOddsInfo.AllInfoBean> items;

    public CPIRecyclerListAdapter(@NonNull List<NewOddsInfo.AllInfoBean> items,
                                  @CPIOddsListFragment.Type String type) {
        this.items = items;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cpi_odds_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 直接调用 holder 的 绑定数据方法
        holder.bindData(items.get(position));
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
                mSecond.setVisibility(View.VISIBLE);
                mStatus.setVisibility(View.GONE);

                mScore.setText(matchInfo.getMatchResult());
            } else {
                // 时间改为默认颜色
                mTime.setTextColor(defaultTextColor);
                mSecond.setVisibility(View.INVISIBLE);
                mStatus.setVisibility(View.VISIBLE);
                if (intState == 0) {
                    // 未开，显示默认灰色
                    mScore.setText("VS");
                    mScore.setTextColor(defaultTextColor);
                    mStatus.setTextColor(defaultTextColor);
                } else {
                    // 结束或各种异常情况，显示红色
                    mScore.setTextColor(redColor);
                    mStatus.setTextColor(redColor);
                    mScore.setText(matchInfo.getMatchResult());
                }
            }

            setStatusText(intState);
            bindOdds(data);
        }

        /**
         * 绑定赔率信息
         *
         * @param data data
         */
        private void bindOdds(NewOddsInfo.AllInfoBean data) {
            mContainer.removeAllViews();
            List<NewOddsInfo.AllInfoBean.ComListBean> comList = data.getComList();
            for (NewOddsInfo.AllInfoBean.ComListBean item : comList) {
                CpiOddsItemView cpiOddsItemView = new CpiOddsItemView(mContext);
                cpiOddsItemView.bindData(item, type);
                mContainer.addView(cpiOddsItemView);
                if (comList.indexOf(item) != comList.size() - 1) {
                    cpiOddsItemView.showDivider();
                } else {
                    cpiOddsItemView.hideDivider();
                }
            }
        }

        /**
         * 设置标题文字
         */
        private void setTitleText() {
            switch (type) {
                case CPIOddsListFragment.TYPE_PLATE:
                    mLeftOddsTitle.setText(R.string.odd_home_txt);
                    mCenterOddsTitle.setText(R.string.odd_dish_txt);
                    mRightOddsTitle.setText(R.string.odd_guest_txt);
                    break;
                case CPIOddsListFragment.TYPE_BIG:
                    mLeftOddsTitle.setText(R.string.odd_home_big_txt);
                    mCenterOddsTitle.setText(R.string.odd_dish_txt);
                    mRightOddsTitle.setText(R.string.odd_guest_big_txt);
                    break;
                case CPIOddsListFragment.TYPE_OP:
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
        private void setStatusText(int status) {
            switch (status) {
                case 0:
                    mStatus.setText(R.string.not_start_short_txt);
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
}
