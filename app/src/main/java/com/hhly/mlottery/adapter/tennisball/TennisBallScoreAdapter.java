package com.hhly.mlottery.adapter.tennisball;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.tennisball.MatchDataBean;

import java.util.List;

/**
 * desc:网球比分适配
 * Created by 107_tangrr on 2017/2/21 0021.
 */

public class TennisBallScoreAdapter extends BaseQuickAdapter<MatchDataBean> {

    public TennisBallScoreAdapter(int layoutResId, List<MatchDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MatchDataBean matchDataBean) {
        baseViewHolder.setText(R.id.tv_match_name,matchDataBean.getLeagueName());
        baseViewHolder.setText(R.id.tv_match_state,String.valueOf(matchDataBean.getMatchStatus()));
        baseViewHolder.setText(R.id.tv_match_date,matchDataBean.getTime());
        baseViewHolder.setText(R.id.tv_home_name,matchDataBean.getHomePlayerName());
        baseViewHolder.setText(R.id.tv_guest_name,matchDataBean.getAwayPlayerName());
        baseViewHolder.setText(R.id.tv_home_count,matchDataBean.getMatchScore().getHomeTotalScore() + "");
        baseViewHolder.setText(R.id.tv_guest_count,matchDataBean.getMatchScore().getAwayTotalScore() + "");
    }
}
