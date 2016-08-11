package com.hhly.mlottery.adapter.basketball;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.basketdatabase.RankingGroup;
import com.hhly.mlottery.bean.basket.basketdatabase.RankingResult;
import com.hhly.mlottery.bean.basket.basketdatabase.RankingTeam;
import com.hhly.mlottery.util.CollectionUtils;
import com.hhly.mlottery.util.DisplayUtil;

import java.util.List;

/**
 * 描    述：篮球数据库排行适配器
 * 作    者：longs@13322.com
 * 时    间：2016/8/11
 */
public class BasketballDatabaseRankingAdapter
        extends BaseSectionQuickAdapter<BasketballDatabaseRankingAdapter.Section> {

    private int type;

    public BasketballDatabaseRankingAdapter(@RankingResult.Type int type, List<Section> data) {
        super(R.layout.item_basket_datatbase_ranking, R.layout.item_basket_datatbase_ranking_title, data);
        this.type = type;
    }

    @Override
    protected void convertHead(BaseViewHolder holder, Section section) {
        TextView textView = holder.getView(R.id.title);
        textView.setText(section.header);
        switch (type) {
            case RankingResult.CUP:
                textView.setGravity(Gravity.CENTER);
                textView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) textView.getLayoutParams();
                layoutParams.topMargin = DisplayUtil.dip2px(mContext, 10);
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
        RankingGroup rankingGroup = section.t;
        LinearLayout teamContainer = holder.getView(R.id.team_container);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        List<RankingTeam> datas = rankingGroup.getDatas();
        if (CollectionUtils.notEmpty(datas)) {
            for (RankingTeam team : datas) {
                teamContainer.addView(produceTeamItemView(inflater, teamContainer, team));
            }
        }
    }

    private View produceTeamItemView(LayoutInflater inflater, ViewGroup container, RankingTeam team) {
        View view = inflater.inflate(R.layout.item_basket_datatbase_ranking_team, container, false);
        TextView rank = (TextView) view.findViewById(R.id.rank);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView matchNum = (TextView) view.findViewById(R.id.match_num);
        TextView winLose = (TextView) view.findViewById(R.id.win_lose);
        TextView winRate = (TextView) view.findViewById(R.id.win_rate);
        TextView winOffset = (TextView) view.findViewById(R.id.win_offset);
        TextView recent = (TextView) view.findViewById(R.id.recent);
        rank.setText(team.getRanking() + "");
        name.setText(team.getTeamName());
        matchNum.setText(team.getFinishedMatch() + "");
        winLose.setText(team.getWinMatch() + "/" + team.getLoseMatch());
        winRate.setText(team.getWinRate() + "");
        winOffset.setText(team.getGameBehind() + "");
        recent.setText(team.getGameBehind() + "");
        return view;
    }

    public static class Section extends SectionEntity<RankingGroup> {

        public Section(boolean isHeader, String header) {
            super(isHeader, header);
        }

        public Section(RankingGroup group) {
            super(group);
        }
    }
}
