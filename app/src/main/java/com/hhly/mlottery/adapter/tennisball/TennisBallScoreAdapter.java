package com.hhly.mlottery.adapter.tennisball;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.tennisball.MatchDataBean;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.ToastTools;

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
    protected void convert(BaseViewHolder baseViewHolder, final MatchDataBean matchDataBean) {
        baseViewHolder.setText(R.id.tv_match_name, matchDataBean.getLeagueName());

        // -6 P2退赛,-5 P1退赛,-4 待定,-3 推迟,-2 中断,-1 完,0 未开始,>0 进行中
        switch (matchDataBean.getMatchStatus()) {
            case -6:
                baseViewHolder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.tennis_match_p2));
                baseViewHolder.setTextColor(R.id.tv_match_state, mContext.getResources().getColor(R.color.number_red));
                break;
            case -5:
                baseViewHolder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.tennis_match_p1));
                baseViewHolder.setTextColor(R.id.tv_match_state, mContext.getResources().getColor(R.color.number_red));
                break;
            case -4:
                baseViewHolder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.tennis_match_dd));
                baseViewHolder.setTextColor(R.id.tv_match_state, mContext.getResources().getColor(R.color.number_red));
                break;
            case -3:
                baseViewHolder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.tennis_match_tc));
                baseViewHolder.setTextColor(R.id.tv_match_state, mContext.getResources().getColor(R.color.number_red));
                break;
            case -2:
                baseViewHolder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.tennis_match_zd));
                baseViewHolder.setTextColor(R.id.tv_match_state, mContext.getResources().getColor(R.color.number_red));
                break;
            case -1:
                baseViewHolder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.tennis_match_over));
                baseViewHolder.setTextColor(R.id.tv_match_state, mContext.getResources().getColor(R.color.number_red));
                break;
            case 0:
                baseViewHolder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.tennis_match_not_start));
                baseViewHolder.setTextColor(R.id.tv_match_state, mContext.getResources().getColor(R.color.mdy_666));
                break;
            default:
                baseViewHolder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.tennis_match_join));
                baseViewHolder.setTextColor(R.id.tv_match_state, mContext.getResources().getColor(R.color.number_green));
                break;
        }

        String time = "";
        if (matchDataBean.getTime() != null) {
            try {
                time = matchDataBean.getTime().substring(0, matchDataBean.getTime().lastIndexOf(":"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        baseViewHolder.setText(R.id.tv_match_date, time);
        baseViewHolder.setText(R.id.tv_home_name, matchDataBean.getHomePlayerName());
        baseViewHolder.setText(R.id.tv_guest_name, matchDataBean.getAwayPlayerName());
        baseViewHolder.setText(R.id.tv_home_count, matchDataBean.getMatchScore().getHomeTotalScore() + "");
        baseViewHolder.setText(R.id.tv_guest_count, matchDataBean.getMatchScore().getAwayTotalScore() + "");

        final ImageView mView = baseViewHolder.getView(R.id.iv_match_focus);
        settingFocusStart(matchDataBean.getMatchStatus(), matchDataBean.isFocus(), mView);

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matchDataBean.setFocus(!matchDataBean.isFocus());
                settingFocusStart(matchDataBean.getMatchStatus(), matchDataBean.isFocus(), mView);
            }
        });
    }

    private void settingFocusStart(int status, boolean isFocus, ImageView view) {
        if (isFocus) {
            view.setImageResource(R.mipmap.tennis_focus_select);
        } else {
            view.setImageResource(status == 0 ? R.mipmap.tennis_focus_noto : R.mipmap.tennis_focus_blue);
        }
    }
}
