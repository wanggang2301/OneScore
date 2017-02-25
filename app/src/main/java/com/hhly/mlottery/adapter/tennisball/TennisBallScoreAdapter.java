package com.hhly.mlottery.adapter.tennisball;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.tennisball.MatchDataBean;
import com.hhly.mlottery.callback.TennisFocusCallBack;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.FocusUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;

import java.util.Iterator;
import java.util.List;

/**
 * desc:网球比分适配
 * Created by 107_tangrr on 2017/2/21 0021.
 */

public class TennisBallScoreAdapter extends BaseQuickAdapter<MatchDataBean> {

    private int mType;
    private TennisFocusCallBack tennisFocusCallBack;

    public void setTennisFocusCallBack(TennisFocusCallBack callBack){
        this.tennisFocusCallBack = callBack;
    }

    public TennisBallScoreAdapter(int layoutResId, List<MatchDataBean> data, int type) {
        super(layoutResId, data);
        this.mType = type;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final MatchDataBean matchDataBean) {
        // 联赛名
        baseViewHolder.setText(R.id.tv_match_name, matchDataBean.getLeagueName());

        // -6 P2退赛,-5 P1退赛,-4 待定,-3 推迟,-2 中断,-1 完,0 未开始,>0 进行中
        switch (matchDataBean.getMatchStatus()) {
            case -6:
                baseViewHolder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.tennis_match_p2));
                baseViewHolder.setTextColor(R.id.tv_match_state, mContext.getResources().getColor(R.color.number_blue));
                break;
            case -5:
                baseViewHolder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.tennis_match_p1));
                baseViewHolder.setTextColor(R.id.tv_match_state, mContext.getResources().getColor(R.color.number_blue));
                break;
            case -4:
                baseViewHolder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.tennis_match_dd));
                baseViewHolder.setTextColor(R.id.tv_match_state, mContext.getResources().getColor(R.color.number_blue));
                break;
            case -3:
                baseViewHolder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.tennis_match_tc));
                baseViewHolder.setTextColor(R.id.tv_match_state, mContext.getResources().getColor(R.color.number_blue));
                break;
            case -2:
                baseViewHolder.setText(R.id.tv_match_state, mContext.getResources().getString(R.string.tennis_match_zd));
                baseViewHolder.setTextColor(R.id.tv_match_state, mContext.getResources().getColor(R.color.number_blue));
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
                baseViewHolder.setTextColor(R.id.tv_match_state, mContext.getResources().getColor(R.color.number_blue));
                break;
        }
        // 时间
        String time = "";
        if (matchDataBean.getTime() != null) {
            try {
                time = matchDataBean.getTime().substring(0, matchDataBean.getTime().lastIndexOf(":"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        baseViewHolder.setText(R.id.tv_match_date, time);
        // 主客队name
        baseViewHolder.setText(R.id.tv_home_name, matchDataBean.getHomePlayerName());
        baseViewHolder.setText(R.id.tv_guest_name, matchDataBean.getAwayPlayerName());
        if (TextUtils.isEmpty(matchDataBean.getHomePlayerName2()) && TextUtils.isEmpty(matchDataBean.getAwayPlayerName2())) {
            // 单打
            baseViewHolder.getView(R.id.tv_home_name2).setVisibility(View.GONE);
            baseViewHolder.getView(R.id.tv_guest_name2).setVisibility(View.GONE);
        } else {
            // 双打
            baseViewHolder.getView(R.id.tv_home_name2).setVisibility(View.VISIBLE);
            baseViewHolder.getView(R.id.tv_guest_name2).setVisibility(View.VISIBLE);
            baseViewHolder.setText(R.id.tv_home_name2, matchDataBean.getHomePlayerName2());
            baseViewHolder.setText(R.id.tv_guest_name2, matchDataBean.getAwayPlayerName2());
        }
        // 局数得分
        if (matchDataBean.getMatchScore().getHomeSetScore1() == 0 && matchDataBean.getMatchScore().getAwaySetScore1() == 0) {
            baseViewHolder.setText(R.id.tv_home_score1, " ");
            baseViewHolder.setText(R.id.tv_guest_score1, " ");
        } else {
            baseViewHolder.setText(R.id.tv_home_score1, String.valueOf(matchDataBean.getMatchScore().getHomeSetScore1()));
            baseViewHolder.setText(R.id.tv_guest_score1, String.valueOf(matchDataBean.getMatchScore().getAwaySetScore1()));
        }
        if (matchDataBean.getMatchScore().getHomeSetScore2() == 0 && matchDataBean.getMatchScore().getAwaySetScore2() == 0) {
            baseViewHolder.setText(R.id.tv_home_score2, " ");
            baseViewHolder.setText(R.id.tv_guest_score2, " ");
        } else {
            baseViewHolder.setText(R.id.tv_home_score2, String.valueOf(matchDataBean.getMatchScore().getHomeSetScore2()));
            baseViewHolder.setText(R.id.tv_guest_score2, String.valueOf(matchDataBean.getMatchScore().getAwaySetScore2()));
        }
        if (matchDataBean.getMatchScore().getHomeSetScore3() == 0 && matchDataBean.getMatchScore().getAwaySetScore3() == 0) {
            baseViewHolder.setText(R.id.tv_home_score3, " ");
            baseViewHolder.setText(R.id.tv_guest_score3, " ");
        } else {
            baseViewHolder.setText(R.id.tv_home_score3, String.valueOf(matchDataBean.getMatchScore().getHomeSetScore3()));
            baseViewHolder.setText(R.id.tv_guest_score3, String.valueOf(matchDataBean.getMatchScore().getAwaySetScore3()));
        }
        if (matchDataBean.getMatchScore().getHomeSetScore4() == 0 && matchDataBean.getMatchScore().getAwaySetScore4() == 0) {
            baseViewHolder.setText(R.id.tv_home_score4, " ");
            baseViewHolder.setText(R.id.tv_guest_score4, " ");
        } else {
            baseViewHolder.setText(R.id.tv_home_score4, String.valueOf(matchDataBean.getMatchScore().getHomeSetScore4()));
            baseViewHolder.setText(R.id.tv_guest_score4, String.valueOf(matchDataBean.getMatchScore().getAwaySetScore4()));
        }
        if (matchDataBean.getMatchScore().getHomeSetScore5() == 0 && matchDataBean.getMatchScore().getAwaySetScore5() == 0) {
            baseViewHolder.setText(R.id.tv_home_score5, " ");
            baseViewHolder.setText(R.id.tv_guest_score5, " ");
        } else {
            baseViewHolder.setText(R.id.tv_home_score5, String.valueOf(matchDataBean.getMatchScore().getHomeSetScore5()));
            baseViewHolder.setText(R.id.tv_guest_score5, String.valueOf(matchDataBean.getMatchScore().getAwaySetScore5()));
        }
        // 总得分
        baseViewHolder.setText(R.id.tv_home_count, String.valueOf(matchDataBean.getMatchScore().getHomeTotalScore()));
        baseViewHolder.setText(R.id.tv_guest_count, String.valueOf(matchDataBean.getMatchScore().getAwayTotalScore()));
        // 关注
        final ImageView mView = baseViewHolder.getView(R.id.iv_match_focus);
        // 设置用户已关注的赛事
        if (FocusUtils.isTennisFocusId(matchDataBean.getMatchId())) {
            matchDataBean.setFocus(true);
            mView.setImageResource(R.mipmap.tennis_focus_select);
        } else {
            matchDataBean.setFocus(false);
            settingFocusStart(matchDataBean.getMatchStatus(), matchDataBean.isFocus(), mView);
        }
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!FocusUtils.isTennisFocusId(matchDataBean.getMatchId())) {
                    FocusUtils.addTennisFocusId(matchDataBean.getMatchId());
                    matchDataBean.setFocus(true);
                } else {
                    FocusUtils.delTennisFocusId(matchDataBean.getMatchId());
                    matchDataBean.setFocus(false);

                    if (mType == 3) {
                        Iterator iterator = mData.iterator();
                        while(iterator.hasNext()){
                            MatchDataBean dataBean = (MatchDataBean) iterator.next();
                            if (dataBean.getMatchId().equals(matchDataBean.getMatchId())) {
                                iterator.remove();
                                notifyDataSetChanged();
                                if(mData.size() == 0 && tennisFocusCallBack != null){
                                    tennisFocusCallBack.notifyDataRefresh();
                                }
                                break;
                            }
                        }
                    }
                }

                settingFocusStart(matchDataBean.getMatchStatus(), matchDataBean.isFocus(), mView);

                L.d("xxxxx", "关注id: " + PreferenceUtil.getString(AppConstants.TENNIS_BALL_FOCUS, ""));
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
