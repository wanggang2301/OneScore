package com.hhly.mlottery.adapter.football;

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
import com.hhly.mlottery.bean.footballDetails.FootballIntegralResult;
import com.hhly.mlottery.bean.footballDetails.FootballRankingData;
import com.hhly.mlottery.frame.footballframe.FootballDatabaseIntegralFragment;
import com.hhly.mlottery.util.DisplayUtil;

import java.util.List;
import java.util.Locale;

/**
 * 描    述：篮球数据库排行适配器
 * 作    者：longs@13322.com
 * 时    间：2016/8/11
 */
public class FootballDatabaseIntegralAdapter
        extends BaseSectionQuickAdapter<FootballDatabaseIntegralAdapter.Section> {

    private FootballDatabaseIntegralFragment.FootballTeamIntegralDetailsClickListener footballTeamIntegralDetailsClickListener;
    public void setFootballTeamIntegralDetailsClickListener(FootballDatabaseIntegralFragment.FootballTeamIntegralDetailsClickListener footballTeamIntegralDetailsClickListener){
        this.footballTeamIntegralDetailsClickListener = footballTeamIntegralDetailsClickListener;
    }
    private static final int TYPE_FORM_TITLE = 1;

    private int type = FootballIntegralResult.SINGLE_LEAGUE;

    public FootballDatabaseIntegralAdapter(List<Section> data) {
        super(R.layout.item_football_database_integral_team, R.layout.item_basket_datatbase_ranking_title, data);
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
            case FootballIntegralResult.CUP:
                textView.setGravity(Gravity.CENTER);
                textView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) textView.getLayoutParams();
                layoutParams.topMargin = DisplayUtil.dip2px(mContext, 10);
                textView.setLayoutParams(layoutParams);
                break;
            case FootballIntegralResult.MULTI_PART_LEAGUE:
                textView.setGravity(GravityCompat.START);
                textView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                break;
            case FootballIntegralResult.SINGLE_LEAGUE:
                break;
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, Section section) {
        final FootballRankingData team = section.t;

        if (team == null) return;

        TextView rank = holder.getView(R.id.rank);
        TextView name = holder.getView(R.id.name);

        int ranking = team.getRank();
        String teamIntegral = team.getScore() + "";

        holder.setText(R.id.rank, String.format(Locale.getDefault(), "%d", ranking))
                .setText(R.id.name, team.getName())
                .setText(R.id.match_num, String.format(Locale.getDefault(), "%d", team.getRound()))
                .setText(R.id.win_equ_lose, String.format(Locale.getDefault(), "%d/%d/%d", team.getWin(),team.getEqu(), team.getFail()))
                .setText(R.id.win_loss, team.getGoal() + "/" + team.getLoss())
                .setText(R.id.win_offset, String.format(Locale.getDefault(), "%d", team.getAbs()))
                .setText(R.id.recent, "0".equals(teamIntegral) ? "0" : teamIntegral);

        /**
         * 联赛标记前三名 ， 杯赛、小组赛（子联赛）标记前两名
         */
        if (type == 0) {
            if (ranking <= 3) {
                rank.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                rank.setBackgroundResource(R.drawable.basket_databae_round_dra);
                name.setTextColor(ContextCompat.getColor(mContext, R.color.gray));
            } else {
                rank.setTextColor(ContextCompat.getColor(mContext, R.color.content_txt_dark_grad));
                rank.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
                name.setTextColor(ContextCompat.getColor(mContext, R.color.content_txt_dark_grad));
            }
        }else{
            if (ranking <= 2) {
                rank.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                rank.setBackgroundResource(R.drawable.basket_databae_round_dra);
                name.setTextColor(ContextCompat.getColor(mContext, R.color.gray));
            } else {
                rank.setTextColor(ContextCompat.getColor(mContext, R.color.content_txt_dark_grad));
                rank.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
                name.setTextColor(ContextCompat.getColor(mContext, R.color.content_txt_dark_grad));
            }
        }
        holder.setOnClickListener(R.id.name, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(footballTeamIntegralDetailsClickListener != null){
                    footballTeamIntegralDetailsClickListener.IntegralDetailsOnClick(v,team);
                }
            }
        });
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FORM_TITLE)
            return new FormTitleViewHolder(getItemView(R.layout.item_football_database_integral, parent));
        return super.onCreateDefViewHolder(parent, viewType);
    }

    public static class Section extends SectionEntity<FootballRankingData> {

        public Section(boolean isHeader, String header) {
            super(isHeader, header);
        }

        public Section(FootballRankingData team) {
            super(team);
        }
    }

    public class FormTitleViewHolder extends BaseViewHolder {
        public FormTitleViewHolder(View view) {
            super(view);
        }
    }
}
