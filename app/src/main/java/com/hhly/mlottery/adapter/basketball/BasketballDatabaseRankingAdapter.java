package com.hhly.mlottery.adapter.basketball;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.basketdatabase.RankingResult;
import com.hhly.mlottery.bean.basket.basketdatabase.RankingTeam;
import com.hhly.mlottery.frame.basketballframe.BasketDatabaseRankingFragment;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.StringUtils;

import java.util.List;
import java.util.Locale;

/**
 * 描    述：篮球数据库排行适配器
 * 作    者：longs@13322.com
 * 时    间：2016/8/11
 */
public class BasketballDatabaseRankingAdapter
        extends BaseSectionQuickAdapter<BasketballDatabaseRankingAdapter.Section> {

    private static final int TYPE_FORM_TITLE = 1;

    private int type = RankingResult.SINGLE_LEAGUE;
    private int matchType = 0;

    public void setMatchType(int matchType) {
        this.matchType = matchType;
    }

    public BasketballDatabaseRankingAdapter(List<Section> data) {
        super(R.layout.item_basket_datatbase_ranking_team, R.layout.item_basket_datatbase_ranking_title, data);
    }

    private BasketDatabaseRankingFragment.BasketballRankingDetailsClickListener basketballRankingDetailsClickListener;
    public void setBasketballRankingDetailsClickListener(BasketDatabaseRankingFragment.BasketballRankingDetailsClickListener basketballRankingDetailsClickListener){
        this.basketballRankingDetailsClickListener = basketballRankingDetailsClickListener;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected int getDefItemViewType(int position) {
        Section section = (Section) getData().get(position);
        if (!section.isHeader && section.t == null) return TYPE_FORM_TITLE;
        return super.getDefItemViewType(position);
    }

    @Override
    protected void convertHead(BaseViewHolder holder, Section section) {
        TextView textView = holder.getView(R.id.title);
        textView.setText(section.header);
        switch (type) {
            case RankingResult.CUP:
            case RankingResult.CUP_MULTI_STAGE:
                textView.setGravity(Gravity.CENTER);
                textView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) textView.getLayoutParams();
                layoutParams.topMargin = DisplayUtil.dip2px(mContext, 10);
                textView.setLayoutParams(layoutParams);
                break;
            case RankingResult.MULTI_PART_LEAGUE:
                textView.setGravity(GravityCompat.START);
                textView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                break;
            case RankingResult.SINGLE_LEAGUE:
                break;
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, Section section) {
        final RankingTeam team = section.t;

        if (holder instanceof FormTitleViewHolder) {
            if (0 == matchType) {
                holder.setVisible(R.id.basket_recent , true);
            }else{
                holder.setVisible(R.id.basket_recent , false);
            }
        }
        if (team == null) return;

        TextView rank = holder.getView(R.id.rank);
        TextView name = holder.getView(R.id.name);
        TextView recent = holder.getView(R.id.recent);

        int ranking = team.getRanking();
        String teamRecent = team.getRecent();

        holder.setText(R.id.rank, String.format(Locale.getDefault(), "%d", ranking))
                .setText(R.id.name, team.getTeamName())
                .setText(R.id.match_num, String.format(Locale.getDefault(), "%d", team.getFinishedMatch()))
                .setText(R.id.win_lose, String.format(Locale.getDefault(), "%d/%d", team.getWinMatch(), team.getLoseMatch()))
                .setText(R.id.win_rate, String.format(Locale.getDefault(), "%.1f", team.getWinRate()))
                .setText(R.id.win_offset, String.format(Locale.getDefault(), "%d", team.getGameBehind()))
                .setText(R.id.recent, "0".equals(teamRecent) ? "-" : teamRecent);

        if (0 == matchType) {
            holder.setVisible(R.id.recent , true);
        }else{
            holder.setVisible(R.id.recent , false);
        }


        if (ranking <= 3) {
            rank.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            rank.setBackgroundResource(R.drawable.basket_databae_round_dra);
            name.setTextColor(ContextCompat.getColor(mContext, R.color.gray));
        } else {
            rank.setTextColor(ContextCompat.getColor(mContext, R.color.content_txt_dark_grad));
            rank.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
            name.setTextColor(ContextCompat.getColor(mContext, R.color.content_txt_dark_grad));
        }

        if (StringUtils.isNotEmpty(teamRecent)) {
            if (teamRecent.contains("W")) {
                recent.setTextColor(ContextCompat.getColor(mContext, R.color.database_win_color));
            } else if (teamRecent.contains("L")) {
                recent.setTextColor(ContextCompat.getColor(mContext, R.color.database_lose_color));
            } else {
                recent.setTextColor(ContextCompat.getColor(mContext, R.color.content_txt_dark_grad));
            }
        }

        holder.setOnClickListener(R.id.name, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (basketballRankingDetailsClickListener != null) {
                    basketballRankingDetailsClickListener.IntegralDetailsOnClick(v , team);
                }
            }
        });
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FORM_TITLE)
            return new FormTitleViewHolder(getItemView(R.layout.item_basket_datatbase_ranking, parent));
        return super.onCreateDefViewHolder(parent, viewType);
    }

    public static class Section extends SectionEntity<RankingTeam> {

        public Section(boolean isHeader, String header) {
            super(isHeader, header);
        }

        public Section(RankingTeam team) {
            super(team);
        }
    }

    public class FormTitleViewHolder extends BaseViewHolder {
        public FormTitleViewHolder(View view) {
            super(view);
        }
    }
}
