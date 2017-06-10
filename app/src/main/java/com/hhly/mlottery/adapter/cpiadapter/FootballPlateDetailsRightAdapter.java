package com.hhly.mlottery.adapter.cpiadapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.oddsbean.OddsDetailsDataInfo;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.HandicapUtils;
import com.hhly.mlottery.util.L;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/6/29.
 */
public class FootballPlateDetailsRightAdapter
        extends BaseQuickAdapter<OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity>
        implements StickyRecyclerHeadersAdapter<FootballPlateDetailsRightAdapter.HeaderViewHolder> {

    int red;
    int green;
    int black;
    int white;
    int grey;

    private String oddsType;
    private String companyId;
    private List<OddsDetailsDataInfo.DetailsEntity> details;

    public FootballPlateDetailsRightAdapter(String companyId, String oddsType,
                                            List<OddsDetailsDataInfo.DetailsEntity> details,
                                            List<OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity> items) {
        super(R.layout.item_odds_details_child, items);
        this.oddsType = oddsType;
        this.details = details;
        this.companyId = companyId;
        refreshData();
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = super.onCreateDefViewHolder(parent, viewType);
        View centerLayout = holder.getView(R.id.odds_details_dish_layout);
        if ("2".equals(oddsType)) {
            TableRow.LayoutParams params =
                    new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT);
            params.weight = 3;
            params.rightMargin = 1;
            centerLayout.setLayoutParams(params);
        }
        return holder;
    }

    @Override
    protected void convert(BaseViewHolder holder,
                           OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity dataDetailsEntity) {
        maybeInitColor();
        // 时间
        String score = dataDetailsEntity.getScore();
        holder.setText(R.id.odds_details_timeAndscore_txt, dataDetailsEntity.getTime()
                + (TextUtils.isEmpty(score) ? "" : "\n" + score));
        holder.setBackgroundColor(R.id.odds_details_timeAndscore_txt,
                dataDetailsEntity.isScoreChanged() ? red : 0);
        holder.setTextColor(R.id.odds_details_timeAndscore_txt,
                dataDetailsEntity.isScoreChanged() ? white : grey);
        // 左侧
        holder.setText(R.id.odds_details_home_txt,
                String.format(Locale.US, "%.2f", dataDetailsEntity.getHomeOdd()));
        holder.setTextColor(R.id.odds_details_home_txt, getTextColor(dataDetailsEntity.getHomeColor()));
        // 右侧
        holder.setText(R.id.odds_details_guest_txt,
                String.format(Locale.US, "%.2f", dataDetailsEntity.getGuestOdd()));
        holder.setTextColor(R.id.odds_details_guest_txt, getTextColor(dataDetailsEntity.getGuestColor()));
        // 盘口
        String hand = String.format(Locale.US, "%.2f", dataDetailsEntity.getHand());


        if (companyId.equals("0")) { // 产品需求没搞好只能这样处理一下
            holder.setText(R.id.odds_details_dish_txt, hand);
        } else {
            if ("1".equals(oddsType)) {
                // 亚盘
                holder.setText(R.id.odds_details_dish_txt, HandicapUtils.changeHandicap(hand));
            } else if ("3".equals(oddsType)) {
                // 大小球
                holder.setText(R.id.odds_details_dish_txt, HandicapUtils.changeHandicapByBigLittleBall(hand));
            } else {
                // 欧赔
                holder.setText(R.id.odds_details_dish_txt, hand);
            }
        }

        setDishColor(holder, dataDetailsEntity.getDishColor());
    }

    /**
     * 刷新数据源
     */
    public void refreshData() {
        mData.clear();
        for (OddsDetailsDataInfo.DetailsEntity detail : details) {
            mData.addAll(detail.getDetails());
        }
        Collections.reverse(mData);
        handleColor();
    }

    private void setDishColor(BaseViewHolder holder, String color) {
        if ("1".equals(oddsType) || "3".equals(oddsType)) {
            holder.setBackgroundColor(R.id.odds_details_dish_txt, getBackgroundColor(color));
            if ("red".equals(color) || "green".equals(color)) {
                holder.setTextColor(R.id.odds_details_dish_txt, white);
            } else {
                holder.setTextColor(R.id.odds_details_dish_txt, black);
            }

        } else {
            holder.setTextColor(R.id.odds_details_dish_txt, getTextColor(color));
        }
    }

    /**
     * 对数据进行遍历处理设置颜色
     */
    private void handleColor() {
        for (int i = 0; i < mData.size() - 1; i++) {
            OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity next = mData.get(i);
            OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity pre = mData.get(i + 1);
            // 处理比分
            String nextScore = next.getScore();
            boolean changed = nextScore != null && !nextScore.isEmpty()
                    && !nextScore.equals(pre.getScore());
            next.setScoreChanged(changed);
            // 处理赔率
            double nextHomeOdd = next.getHomeOdd();
            double preHomeOdd = pre.getHomeOdd();
            if (nextHomeOdd > preHomeOdd) {
                next.setHomeColor("red");
            } else if (nextHomeOdd == preHomeOdd) {
                next.setHomeColor("grey");
            } else {
                next.setHomeColor("green");
            }
            double nextGuestOdd = next.getGuestOdd();
            double preGuestOdd = pre.getGuestOdd();
            if (nextGuestOdd > preGuestOdd) {
                next.setGuestColor("red");
            } else if (nextGuestOdd == preGuestOdd) {
                next.setGuestColor("grey");
            } else {
                next.setGuestColor("green");
            }
            double nextHand = next.getHand();
            double preHand = pre.getHand();
            if (nextHand > preHand) {
                next.setDishColor("red");
            } else if (nextHand == preHand) {
                next.setDishColor("grey");
            } else {
                next.setDishColor("green");
            }
        }
    }

    /**
     * 根据字符串获取对应颜色
     *
     * @param color color
     * @return intColor
     */
    private int getTextColor(String color) {
        if ("red".equals(color)) {
            return red;
        } else if ("green".equals(color)) {
            return green;
        } else if ("grey".equals(color)) {
            return black;
        }
        return black;
    }

    /**
     * 根据字符串获取背景色
     *
     * @param color color
     * @return intColor
     */
    private int getBackgroundColor(String color) {
        if ("red".equals(color)) {
            return red;
        } else if ("green".equals(color)) {
            return green;
        } else if ("grey".equals(color)) {
            return 0;
        }
        return 0;
    }

    /**
     * 初始化颜色
     */
    private void maybeInitColor() {
        if (red == 0) {
            red = ContextCompat.getColor(mContext, R.color.odds_details);
        }
        if (green == 0) {
            green = ContextCompat.getColor(mContext, R.color.odds_down_bg);
        }
        if (black == 0) {
            black = ContextCompat.getColor(mContext, R.color.content_txt_black);
        }
        if (white == 0) {
            white = ContextCompat.getColor(mContext, R.color.white);
        }
        if (grey == 0) {
            grey = ContextCompat.getColor(mContext, R.color.version);
        }
    }

    @Override
    public long getHeaderId(int position) {
        for (OddsDetailsDataInfo.DetailsEntity detail : details) {
            if (detail.getDetails().indexOf(mData.get(position)) >= 0) {
                return detail.hashCode();
            }
        }
        return 0;
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_odds_header, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder holder, int position) {
        for (OddsDetailsDataInfo.DetailsEntity detail : details) {
            if (detail.getDetails().indexOf(mData.get(position)) >= 0) {
                holder.bindData(detail.getDate());
            }
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.odds_details_data_txt);
        }

        public void bindData(String title) {
            mTextView.setText(DateUtil.convertDateToNation(title));
        }
    }
}
