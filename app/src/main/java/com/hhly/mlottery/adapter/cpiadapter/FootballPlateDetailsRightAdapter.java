package com.hhly.mlottery.adapter.cpiadapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.oddsbean.OddsDetailsDataInfo;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

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
    protected void convert(BaseViewHolder holder,
                           OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity dataDetailsEntity) {
//        final TextView timeAndScoreTextView = holder.getView(R.id.odds_details_timeAndscore_txt);
        final TextView leftTextView = holder.getView(R.id.odds_details_home_txt);
        final TextView centerTextView = holder.getView(R.id.odds_details_dish_txt);
        final TextView rightTextView = holder.getView(R.id.odds_details_guest_txt);
        final LinearLayout centerLayout = holder.getView(R.id.odds_details_dish_layout);

        // 时间与比分
        String score = dataDetailsEntity.getScore();
        holder.setText(R.id.odds_details_timeAndscore_txt, dataDetailsEntity.getTime()
                + (TextUtils.isEmpty(score) ? "" : "\n" + score));
        holder.setText(R.id.odds_details_home_txt,
                String.format(Locale.US, "%.2f", dataDetailsEntity.getHomeOdd()));
        holder.setText(R.id.odds_details_guest_txt,
                String.format(Locale.US, "%.2f", dataDetailsEntity.getGuestOdd()));

        // TODO: 绑定数据
    }

    /**
     * 刷新数据源
     */
    public void refreshData() {
        mData.clear();
        for (OddsDetailsDataInfo.DetailsEntity detail : details) {
            mData.addAll(detail.getDetails());
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
