package com.hhly.mlottery.frame.tennisfrag.datailsfrag;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.TennisBallDetailsActivity;
import com.hhly.mlottery.bean.tennisball.datails.analysis.DataCompareBean;
import com.hhly.mlottery.bean.tennisball.datails.analysis.MatchListBean;
import com.hhly.mlottery.bean.tennisball.datails.analysis.RankScoreBean;
import com.hhly.mlottery.bean.tennisball.datails.analysis.TennisAnalysisBean;
import com.hhly.mlottery.util.DateUtil;

/**
 * 网球内页分析
 * 20170323 tangrr
 */
public class TennisAnalysisFrag extends Fragment implements View.OnClickListener {
    private static final String TENNIS_DATAILS_THIRDID = "tennis_details_third_id";
    private final int LOADING = 1;
    private final int SUCCESS = 2;
    private final int ERROR = 3;
    private final int NOTO_DATA = 4;

    private String mThirdId;
    private boolean isSingle;// 是否单人比赛
    private Activity mContext;

    private View mView, contentView, notDataView;
    private TextView integral_count, integral_total, integral_change, integral_ranking_old, integral_ranking, integral_name, fight_guest_score5, fight_guest_score4;
    private TextView fight_guest_score3, fight_guest_score2, fight_guest_score1, fight_home_score5, fight_home_score4, fight_home_score3, fight_home_score2, fight_home_score1;
    private TextView fight_guest_name2, fight_guest_name, fight_guest_total_score, fight_home_total_score, fight_home_name2, fight_home_name, fight_field, fight_time;
    private TextView fight_date, fight_match_name, record_title, record_name, record_score, record_field, record_match_start, record_match_name, record_date, data_guest_name2_score;
    private TextView data_guest_name_score, data_pk, data_home_name2_score, data_home_name_score, data_guest_name2, data_guest_name, data_home_name2, data_home_name;
    private TextView data_not_data, record_not_data, fight_title, fight_not_data, integral_not_data, record_name2;
    private LinearLayout integral_title, data_title, ll_integral_content, ll_fight_content, ll_record_content, ll_data_content;


    public TennisAnalysisFrag() {

    }

    public static TennisAnalysisFrag newInstance(String thirdId) {
        TennisAnalysisFrag fragment = new TennisAnalysisFrag();
        Bundle args = new Bundle();
        args.putString(TENNIS_DATAILS_THIRDID, thirdId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mThirdId = getArguments().getString(TENNIS_DATAILS_THIRDID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_tennis_analysis, container, false);

        initView();
        initEvent();
        return mView;
    }

    private void initEvent() {

    }

    private void initView() {
        // 无数据
        notDataView = mView.findViewById(R.id.network_exception_layout);
        mView.findViewById(R.id.network_exception_reload_btn).setOnClickListener(this);
        // 数据内容
        contentView = mView.findViewById(R.id.tennis_datails_analysis_scroll);
        // 积分排名
        integral_not_data = (TextView) mView.findViewById(R.id.tv_not_data_integral);
        integral_title = (LinearLayout) mView.findViewById(R.id.ll_tennis_datails_title_integral);
        ll_integral_content = (LinearLayout) mView.findViewById(R.id.ll_integral_content);
        integralItemView();
        // 交手记录
        fight_not_data = (TextView) mView.findViewById(R.id.tv_not_data_fight);
        fight_title = (TextView) mView.findViewById(R.id.tv_fight_title);
        ll_fight_content = (LinearLayout) mView.findViewById(R.id.ll_fight_content);
        fightItemView();
        // 近期战绩
        record_not_data = (TextView) mView.findViewById(R.id.tv_not_data_record);
        ll_record_content = (LinearLayout) mView.findViewById(R.id.ll_record_content);
        recordTopItemView();
        recordItemView();
        // 数据对比
        data_not_data = (TextView) mView.findViewById(R.id.tv_not_data_data);
        data_title = (LinearLayout) mView.findViewById(R.id.ll_tennis_datails_title_data);
        data_home_name = (TextView) mView.findViewById(R.id.tennis_datails_home_name);
        data_home_name2 = (TextView) mView.findViewById(R.id.tennis_datails_home_name2);
        data_guest_name = (TextView) mView.findViewById(R.id.tennis_datails_guest_name);
        data_guest_name2 = (TextView) mView.findViewById(R.id.tennis_datails_guest_name2);
        ll_data_content = (LinearLayout) mView.findViewById(R.id.ll_data_content);
        dataItemView();
    }

    // 设置页面显示状态
    public void setStatus(int status) {
        notDataView.setVisibility(status == ERROR ? View.VISIBLE : View.GONE);
        contentView.setVisibility(status == SUCCESS ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

    // 刷新数据
    public void updataChange(TennisAnalysisBean.DataBean data) {
        setStatus(SUCCESS);
        setAnaylysisData(data);
    }

    // 数据对比itemView
    private View dataItemView() {
        View view = View.inflate(mContext, R.layout.tennis_datails_data_item, null);
        data_home_name_score = (TextView) view.findViewById(R.id.tennis_datails_home_name_score);
        data_home_name2_score = (TextView) view.findViewById(R.id.tennis_datails_home_name2_score);
        data_pk = (TextView) view.findViewById(R.id.tennis_datails_data_pk);
        data_guest_name_score = (TextView) view.findViewById(R.id.tennis_datails_guest_name_score);
        data_guest_name2_score = (TextView) view.findViewById(R.id.tennis_datails_guest_name2_score);
        return view;
    }

    // 近期战绩itemView
    private View recordItemView() {
        View view = View.inflate(mContext, R.layout.tennis_datails_record_content_item, null);
        record_date = (TextView) view.findViewById(R.id.tv_analysis_record_date);
        record_match_name = (TextView) view.findViewById(R.id.tv_analysis_record_match_name);
        record_match_start = (TextView) view.findViewById(R.id.tv_analysis_record_match_start);
        record_field = (TextView) view.findViewById(R.id.tv_analysis_record_field);
        record_score = (TextView) view.findViewById(R.id.tv_analysis_record_score);
        record_name = (TextView) view.findViewById(R.id.tv_analysis_record_name);
        record_name2 = (TextView) view.findViewById(R.id.tv_analysis_record_name2);
        return view;
    }

    // 近期战绩itemView_TOP
    private View recordTopItemView() {
        View view = View.inflate(mContext, R.layout.tennis_datails_record_top_item, null);
        record_title = (TextView) view.findViewById(R.id.tennis_datails_analysis_record_top_title);
        return view;
    }

    // 近期战绩 无数据item
    private View recordNotDataView() {
        return View.inflate(mContext, R.layout.fragment_tennis_analysis_item, null);
    }

    // 交手记录itemView
    private View fightItemView() {
        View view = View.inflate(mContext, R.layout.tennis_datails_fight_item, null);
        fight_match_name = (TextView) view.findViewById(R.id.tennis_fight_match_name);
        fight_date = (TextView) view.findViewById(R.id.tennis_fight_date);
        fight_time = (TextView) view.findViewById(R.id.tennis_fight_time);
        fight_field = (TextView) view.findViewById(R.id.tennis_fight_field);
        fight_home_name = (TextView) view.findViewById(R.id.tennis_fight_home_name);
        fight_home_name2 = (TextView) view.findViewById(R.id.tennis_fight_home_name2);
        fight_home_total_score = (TextView) view.findViewById(R.id.tennis_fight_home_total_score);
        fight_guest_total_score = (TextView) view.findViewById(R.id.tennis_fight_guest_total_score);
        fight_guest_name = (TextView) view.findViewById(R.id.tennis_fight_guest_name);
        fight_guest_name2 = (TextView) view.findViewById(R.id.tennis_fight_guest_name2);
        fight_home_score1 = (TextView) view.findViewById(R.id.tennis_fight_home_score1);
        fight_home_score2 = (TextView) view.findViewById(R.id.tennis_fight_home_score2);
        fight_home_score3 = (TextView) view.findViewById(R.id.tennis_fight_home_score3);
        fight_home_score4 = (TextView) view.findViewById(R.id.tennis_fight_home_score4);
        fight_home_score5 = (TextView) view.findViewById(R.id.tennis_fight_home_score5);
        fight_guest_score1 = (TextView) view.findViewById(R.id.tennis_fight_guest_score1);
        fight_guest_score2 = (TextView) view.findViewById(R.id.tennis_fight_guest_score2);
        fight_guest_score3 = (TextView) view.findViewById(R.id.tennis_fight_guest_score3);
        fight_guest_score4 = (TextView) view.findViewById(R.id.tennis_fight_guest_score4);
        fight_guest_score5 = (TextView) view.findViewById(R.id.tennis_fight_guest_score5);
        return view;
    }

    // 积分排名itemView
    private View integralItemView() {
        View view = View.inflate(mContext, R.layout.tennis_datails_integral_item, null);
        integral_name = (TextView) view.findViewById(R.id.tennis_datails_integral_name);
        integral_ranking = (TextView) view.findViewById(R.id.tennis_datails_integral_ranking);
        integral_ranking_old = (TextView) view.findViewById(R.id.tennis_datails_integral_ranking_old);
        integral_change = (TextView) view.findViewById(R.id.tennis_datails_integral_change);
        integral_total = (TextView) view.findViewById(R.id.tennis_datails_integral_total);
        integral_count = (TextView) view.findViewById(R.id.tennis_datails_integral_count);
        return view;
    }

    // 数据内容显示
    private void setAnaylysisData(TennisAnalysisBean.DataBean data) {
        if (data == null) return;

        String homeName = "";
        String guestName = "";
        // 比赛类型： 1男子、2女子、3男双、4女双、5混双
        if (data.getMatchInfo() != null) {
            int matchType = data.getMatchInfo().getMatchType();
            switch (matchType) {
                case 1:
                case 2:
                    isSingle = true;
                    break;
                case 3:
                case 4:
                case 5:
                    isSingle = false;
                    break;
                default:
                    isSingle = false;
                    break;
            }
            if (data.getMatchInfo().getHomePlayer1() != null) {
                if (isSingle) {
                    homeName = data.getMatchInfo().getHomePlayer1().getName();
                } else {
                    String homePlayer2Name = "";
                    if (data.getMatchInfo().getHomePlayer2() != null) {
                        homePlayer2Name = "/" + data.getMatchInfo().getHomePlayer2().getName();
                    }
                    homeName = data.getMatchInfo().getHomePlayer1().getName() + homePlayer2Name;
                }
            }
            if (data.getMatchInfo().getGuestPlayer1() != null) {
                if (isSingle) {
                    guestName = data.getMatchInfo().getGuestPlayer1().getName();
                } else {
                    String guestPlayer2Name = "";
                    if (data.getMatchInfo().getGuestPlayer2() != null) {
                        guestPlayer2Name = "/" + data.getMatchInfo().getGuestPlayer2().getName();
                    }
                    guestName = data.getMatchInfo().getGuestPlayer1().getName() + guestPlayer2Name;
                }
            }
        }

        if (ll_integral_content != null) {
            ll_integral_content.removeAllViews();
        }
        if (ll_fight_content != null) {
            ll_fight_content.removeAllViews();
        }
        if (ll_record_content != null) {
            ll_record_content.removeAllViews();
        }
        if (ll_data_content != null) {
            ll_data_content.removeAllViews();
        }

        // 积分排名
        if (data.getRankScore() != null && data.getRankScore().size() != 0) {
            integral_not_data.setVisibility(View.GONE);
            integral_title.setVisibility(View.VISIBLE);
            for (int i = 0; i < data.getRankScore().size(); i++) {
                ll_integral_content.addView(integralItemView());
                RankScoreBean rankScoreBean = data.getRankScore().get(i);
                if (rankScoreBean != null) {
                    integral_name.setText(rankScoreBean.getPlayerName() == null ? "" : rankScoreBean.getPlayerName());
                    integral_ranking.setText(rankScoreBean.getRanking() == null ? "" : rankScoreBean.getRanking());
                    integral_ranking_old.setText(rankScoreBean.getOldRanking() == null ? "" : rankScoreBean.getOldRanking());
                    integral_change.setText(rankScoreBean.getRankingChange() == null ? "" : rankScoreBean.getRankingChange());
                    integral_total.setText(rankScoreBean.getTotalIntegral() == null ? "" : rankScoreBean.getTotalIntegral());
                    integral_count.setText(rankScoreBean.getNumberOfEntries() == null ? "" : rankScoreBean.getNumberOfEntries());
                }
            }
        } else {
            integral_not_data.setVisibility(View.VISIBLE);
            integral_title.setVisibility(View.GONE);
        }

        // 交手记录
        if (data.getMatchRecord() != null && data.getMatchRecord().getMatchList() != null && data.getMatchRecord().getMatchList().size() != 0) {
            fight_not_data.setVisibility(View.GONE);
            fight_title.setVisibility(View.VISIBLE);

            String title = mContext.getResources().getString(R.string.tennis_datails_fight_title);
            String titleDesc = String.format(title, data.getMatchRecord().getTotalTimes(), homeName, data.getMatchRecord().getPlayerWin(), data.getMatchRecord().getPlayerFail(), data.getMatchRecord().getWinRate());
            fight_title.setText(titleDesc);

            for (int i = 0; i < data.getMatchRecord().getMatchList().size(); i++) {
                ll_fight_content.addView(fightItemView());
                MatchListBean matchListBean = data.getMatchRecord().getMatchList().get(i);
                fight_match_name.setText((matchListBean.getLeagueName() == null ? "" : matchListBean.getLeagueName()) + " " + getRole(matchListBean.getRole()));
                fight_date.setText(matchListBean.getStartDate() == null ? "" : DateUtil.convertDateToNation(matchListBean.getStartDate()));
                fight_time.setText(matchListBean.getStartTime() == null ? "" : matchListBean.getStartTime());
                fight_field.setText(getSite(matchListBean.getSiteType()));
                fight_home_name.setText(matchListBean.getHomePlayer1().getName());
                fight_guest_name.setText(matchListBean.getGuestPlayer1().getName());
                fight_home_total_score.setText(String.valueOf(matchListBean.getMatchScore().getHomeTotalScore()));
                fight_guest_total_score.setText(String.valueOf(matchListBean.getMatchScore().getAwayTotalScore()));
                if (isSingle) {
                    // 单人
                    fight_home_name2.setVisibility(View.GONE);
                    fight_guest_name2.setVisibility(View.GONE);
                } else {
                    // 双人
                    fight_home_name2.setVisibility(View.VISIBLE);
                    fight_guest_name2.setVisibility(View.VISIBLE);
                    fight_home_name2.setText(matchListBean.getHomePlayer2().getName());
                    fight_guest_name2.setText(matchListBean.getGuestPlayer2().getName());
                }
                if (matchListBean.getMatchScore().getHomeSetScore1() == 0 && matchListBean.getMatchScore().getAwaySetScore1() == 0) {
                    fight_home_score1.setText("");
                    fight_guest_score1.setText("");
                } else {
                    fight_home_score1.setText(String.valueOf(matchListBean.getMatchScore().getHomeSetScore1()));
                    fight_guest_score1.setText(String.valueOf(matchListBean.getMatchScore().getAwaySetScore1()));
                }
                if (matchListBean.getMatchScore().getHomeSetScore2() == 0 && matchListBean.getMatchScore().getAwaySetScore2() == 0) {
                    fight_home_score2.setText("");
                    fight_guest_score2.setText("");
                } else {
                    fight_home_score2.setText(String.valueOf(matchListBean.getMatchScore().getHomeSetScore2()));
                    fight_guest_score2.setText(String.valueOf(matchListBean.getMatchScore().getAwaySetScore2()));
                }
                if (matchListBean.getMatchScore().getHomeSetScore3() == 0 && matchListBean.getMatchScore().getAwaySetScore3() == 0) {
                    fight_home_score3.setText("");
                    fight_guest_score3.setText("");
                } else {
                    fight_home_score3.setText(String.valueOf(matchListBean.getMatchScore().getHomeSetScore3()));
                    fight_guest_score3.setText(String.valueOf(matchListBean.getMatchScore().getAwaySetScore3()));
                }
                if (matchListBean.getMatchScore().getHomeSetScore4() == 0 && matchListBean.getMatchScore().getAwaySetScore4() == 0) {
                    fight_home_score4.setText("");
                    fight_guest_score4.setText("");
                } else {
                    fight_home_score4.setText(String.valueOf(matchListBean.getMatchScore().getHomeSetScore4()));
                    fight_guest_score4.setText(String.valueOf(matchListBean.getMatchScore().getAwaySetScore4()));
                }
                if (matchListBean.getMatchScore().getHomeSetScore5() == 0 && matchListBean.getMatchScore().getAwaySetScore5() == 0) {
                    fight_home_score5.setText("");
                    fight_guest_score5.setText("");
                } else {
                    fight_home_score5.setText(String.valueOf(matchListBean.getMatchScore().getHomeSetScore5()));
                    fight_guest_score5.setText(String.valueOf(matchListBean.getMatchScore().getAwaySetScore5()));
                }
            }
        } else {
            fight_not_data.setVisibility(View.VISIBLE);
            fight_title.setVisibility(View.GONE);
        }

        // 近期战绩
        if (data.getRecentMatch() != null) {
            record_not_data.setVisibility(View.GONE);

            // 主队
            if (data.getRecentMatch().getHomePlayerRecentMatch() != null) {
                ll_record_content.addView(recordTopItemView());
                String title = mContext.getResources().getString(R.string.tennis_datails_ranking_title);
                String titleDesc = String.format(title, data.getRecentMatch().getHomePlayerRecentMatch().getTotalTimes(), homeName, data.getRecentMatch().getHomePlayerRecentMatch().getPlayerWin(), data.getRecentMatch().getHomePlayerRecentMatch().getPlayerFail(), data.getRecentMatch().getHomePlayerRecentMatch().getWinRate());
                record_title.setText(titleDesc);

                if (data.getRecentMatch().getHomePlayerRecentMatch().getMatchList() == null || data.getRecentMatch().getHomePlayerRecentMatch().getMatchList().size() == 0) {
                    ll_record_content.addView(recordNotDataView());
                } else {
                    for (int i = 0; i < data.getRecentMatch().getHomePlayerRecentMatch().getMatchList().size(); i++) {
                        ll_record_content.addView(recordItemView());
                        MatchListBean matchListBean = data.getRecentMatch().getHomePlayerRecentMatch().getMatchList().get(i);
                        record_date.setText(matchListBean.getStartDate() == null ? "" : DateUtil.convertDateToNation(matchListBean.getStartDate()));
                        record_match_name.setText(matchListBean.getLeagueName() == null ? "" : matchListBean.getLeagueName());
                        record_match_start.setText(getRole(matchListBean.getRole()));
                        record_field.setText(getSite(matchListBean.getSiteType()));
                        record_score.setText(matchListBean.getMatchScore().getHomeTotalScore() + "-" + matchListBean.getMatchScore().getAwayTotalScore());
                        record_name.setText(matchListBean.getGuestPlayer1().getName() == null ? "" : matchListBean.getGuestPlayer1().getName());
                        if (isSingle) {
                            record_name2.setVisibility(View.GONE);
                        } else {
                            record_name2.setVisibility(View.VISIBLE);
                            record_name2.setText(matchListBean.getGuestPlayer2().getName() == null ? "" : matchListBean.getGuestPlayer2().getName());
                        }
                    }
                }
            }

            // 客队
            if (data.getRecentMatch().getGuestPlayerRecentMatch() != null) {
                ll_record_content.addView(recordTopItemView());
                String title = mContext.getResources().getString(R.string.tennis_datails_ranking_title);
                String titleDesc = String.format(title, data.getRecentMatch().getGuestPlayerRecentMatch().getTotalTimes(), guestName, data.getRecentMatch().getGuestPlayerRecentMatch().getPlayerWin(), data.getRecentMatch().getGuestPlayerRecentMatch().getPlayerFail(), data.getRecentMatch().getGuestPlayerRecentMatch().getWinRate());
                record_title.setText(titleDesc);

                if (data.getRecentMatch().getGuestPlayerRecentMatch().getMatchList() == null || data.getRecentMatch().getGuestPlayerRecentMatch().getMatchList().size() == 0) {
                    ll_record_content.addView(recordNotDataView());
                } else {
                    for (int i = 0; i < data.getRecentMatch().getGuestPlayerRecentMatch().getMatchList().size(); i++) {
                        ll_record_content.addView(recordItemView());
                        MatchListBean matchListBean = data.getRecentMatch().getGuestPlayerRecentMatch().getMatchList().get(i);
                        record_date.setText(matchListBean.getStartDate() == null ? "" : DateUtil.convertDateToNation(matchListBean.getStartDate()));
                        record_match_name.setText(matchListBean.getLeagueName() == null ? "" : matchListBean.getLeagueName());
                        record_match_start.setText(getRole(matchListBean.getRole()));
                        record_field.setText(getSite(matchListBean.getSiteType()));
                        record_score.setText(matchListBean.getMatchScore().getHomeTotalScore() + "-" + matchListBean.getMatchScore().getAwayTotalScore());
                        record_name.setText(matchListBean.getGuestPlayer1().getName() == null ? "" : matchListBean.getGuestPlayer1().getName());
                        if (isSingle) {
                            record_name2.setVisibility(View.GONE);
                        } else {
                            record_name2.setVisibility(View.VISIBLE);
                            record_name2.setText(matchListBean.getGuestPlayer2().getName() == null ? "" : matchListBean.getGuestPlayer2().getName());
                        }
                    }
                }
            }
        } else {
            record_not_data.setVisibility(View.VISIBLE);
        }

        // 数据对比
        if (data.getDataCompare() != null && data.getDataCompare().size() != 0) {
            data_not_data.setVisibility(View.GONE);
            data_title.setVisibility(View.VISIBLE);

            for (int i = 0; i < data.getDataCompare().size(); i++) {
                DataCompareBean dataCompareBean = data.getDataCompare().get(i);
                if (dataCompareBean.getStatus() == 0) {
                    data_home_name.setText(dataCompareBean.getHome1() == null ? "-" : dataCompareBean.getHome1());
                    data_guest_name.setText(dataCompareBean.getGuest1() == null ? "-" : dataCompareBean.getGuest1());
                    if (isSingle) {
                        data_home_name2.setVisibility(View.GONE);
                        data_guest_name2.setVisibility(View.GONE);
                    } else {
                        data_home_name2.setVisibility(View.VISIBLE);
                        data_guest_name2.setVisibility(View.VISIBLE);
                        data_home_name2.setText(dataCompareBean.getHome2() == null ? "-" : dataCompareBean.getHome2());
                        data_guest_name2.setText(dataCompareBean.getGuest2() == null ? "-" : dataCompareBean.getGuest2());
                    }
                    continue;
                }
                ll_data_content.addView(dataItemView());
                data_home_name_score.setText(dataCompareBean.getHome1() == null ? "-" : dataCompareBean.getHome1());
                data_guest_name_score.setText(dataCompareBean.getGuest1() == null ? "-" : dataCompareBean.getGuest1());
                data_pk.setText(getStatus(dataCompareBean.getStatus()));
                if (isSingle) {
                    data_home_name2_score.setVisibility(View.GONE);
                    data_guest_name2_score.setVisibility(View.GONE);
                } else {
                    data_home_name2_score.setVisibility(View.VISIBLE);
                    data_guest_name2_score.setVisibility(View.VISIBLE);
                    data_home_name2_score.setText(dataCompareBean.getHome2() == null ? "-" : dataCompareBean.getHome2());
                    data_guest_name2_score.setText(dataCompareBean.getGuest2() == null ? "-" : dataCompareBean.getGuest2());
                }
            }
        } else {
            data_not_data.setVisibility(View.VISIBLE);
            data_title.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.network_exception_reload_btn:
                ((TennisBallDetailsActivity) mContext).onRefresh();
                break;
        }
    }

    private String getRole(int id) {
        switch (id) {
            case 1:
                return mContext.getResources().getString(R.string.tennis_datails_role1);
            case 2:
                return mContext.getResources().getString(R.string.tennis_datails_role2);
            case 3:
                return mContext.getResources().getString(R.string.tennis_datails_role3);
            case 4:
                return mContext.getResources().getString(R.string.tennis_datails_role4);
            case 5:
                return mContext.getResources().getString(R.string.tennis_datails_role5);
            case 6:
                return mContext.getResources().getString(R.string.tennis_datails_role6);
            case 7:
                return mContext.getResources().getString(R.string.tennis_datails_role7);
            case 8:
                return mContext.getResources().getString(R.string.tennis_datails_role8);
            case 9:
                return mContext.getResources().getString(R.string.tennis_datails_role9);
            case 10:
                return mContext.getResources().getString(R.string.tennis_datails_role10);
            case 11:
                return mContext.getResources().getString(R.string.tennis_datails_role11);
            case 12:
                return mContext.getResources().getString(R.string.tennis_datails_role12);
            case 13:
                return mContext.getResources().getString(R.string.tennis_datails_role13);
            case 14:
                return mContext.getResources().getString(R.string.tennis_datails_role14);
            default:
                return "";
        }
    }

    private String getSite(int id) {
        switch (id) {
            case 10:
                return mContext.getResources().getString(R.string.tennis_datails_site1);
            case 11:
                return mContext.getResources().getString(R.string.tennis_datails_site2);
            case 12:
                return mContext.getResources().getString(R.string.tennis_datails_site3);
            case 20:
                return mContext.getResources().getString(R.string.tennis_datails_site4);
            case 21:
                return mContext.getResources().getString(R.string.tennis_datails_site5);
            case 22:
                return mContext.getResources().getString(R.string.tennis_datails_site6);
            case 30:
                return mContext.getResources().getString(R.string.tennis_datails_site7);
            case 31:
                return mContext.getResources().getString(R.string.tennis_datails_site8);
            case 32:
                return mContext.getResources().getString(R.string.tennis_datails_site9);
            case 40:
                return mContext.getResources().getString(R.string.tennis_datails_site10);
            case 41:
                return mContext.getResources().getString(R.string.tennis_datails_site11);
            case 42:
                return mContext.getResources().getString(R.string.tennis_datails_site12);
            default:
                return "";
        }
    }

    private String getStatus(int id) {
        switch (id) {
            case 1:
                return mContext.getResources().getString(R.string.tennis_datails_status1);
            case 2:
                return mContext.getResources().getString(R.string.tennis_datails_status2);
            case 3:
                return mContext.getResources().getString(R.string.tennis_datails_status3);
            case 4:
                return mContext.getResources().getString(R.string.tennis_datails_status4);
            case 5:
                return mContext.getResources().getString(R.string.tennis_datails_status5);
            case 6:
                return mContext.getResources().getString(R.string.tennis_datails_status6);
            case 7:
                return mContext.getResources().getString(R.string.tennis_datails_status7);
            case 8:
                return mContext.getResources().getString(R.string.tennis_datails_status8);
            case 9:
                return mContext.getResources().getString(R.string.tennis_datails_status9);
            case 10:
                return mContext.getResources().getString(R.string.tennis_datails_status10);
            default:
                return "";
        }
    }
}
