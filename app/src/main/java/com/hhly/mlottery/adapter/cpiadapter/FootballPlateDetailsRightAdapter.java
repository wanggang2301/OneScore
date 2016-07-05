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
import com.hhly.mlottery.util.HandicapUtils;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * 足球详情 - 指数 - 指数详情右侧列表适配器
 * <p/>
 * Created by loshine on 2016/6/29.
 */
public class FootballPlateDetailsRightAdapter
        extends BaseQuickAdapter<OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity>
        implements StickyRecyclerHeadersAdapter<FootballPlateDetailsRightAdapter.HeaderViewHolder> {

    int red;
    int green;
    int grey;

    private String oddsType;
    private List<OddsDetailsDataInfo.DetailsEntity> details;

    public FootballPlateDetailsRightAdapter(String oddsType,
                                            List<OddsDetailsDataInfo.DetailsEntity> details,
                                            List<OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity> items) {
        super(R.layout.item_odds_details_child, items);
        this.oddsType = oddsType;
        this.details = details;
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

        // 时间与比分
        String score = dataDetailsEntity.getScore();
        holder.setText(R.id.odds_details_timeAndscore_txt, dataDetailsEntity.getTime()
                + (TextUtils.isEmpty(score) ? "" : "\n" + score));
        // 左侧
        holder.setText(R.id.odds_details_home_txt,
                String.format(Locale.US, "%.2f", dataDetailsEntity.getHomeOdd()));
        holder.setTextColor(R.id.odds_details_home_txt, getColor(dataDetailsEntity.getHomeColor()));
        // 右侧
        holder.setText(R.id.odds_details_guest_txt,
                String.format(Locale.US, "%.2f", dataDetailsEntity.getGuestOdd()));
        holder.setTextColor(R.id.odds_details_guest_txt, getColor(dataDetailsEntity.getGuestColor()));
        // 盘口
        String hand = String.format(Locale.US, "%.2f", dataDetailsEntity.getHand());
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
        holder.setTextColor(R.id.odds_details_dish_txt, getColor(dataDetailsEntity.getDishColor()));
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

    /**
     * 对数据进行遍历处理设置颜色
     */
    private void handleColor() {
        for (int i = 0; i < mData.size() - 1; i++) {
            OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity next = mData.get(i);
            OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity pre = mData.get(i + 1);
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
    private int getColor(String color) {
        maybeInitColor();
        if ("red".equals(color)) {
            return red;
        } else if ("green".equals(color)) {
            return green;
        } else if ("grey".equals(color)) {
            return grey;
        }
        return grey;
    }

    /**
     * 初始化颜色
     */
    private void maybeInitColor() {
        if (red == 0) {
            red = ContextCompat.getColor(mContext, R.color.homwe_lhc_red);
        }
        if (green == 0) {
            green = ContextCompat.getColor(mContext, R.color.tabhost);
        }
        if (grey == 0) {
            grey = ContextCompat.getColor(mContext, R.color.content_txt_dark_grad);
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
            mTextView.setText(title);
        }
    }
}
