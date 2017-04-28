package com.hhly.mlottery.frame.footballteaminfo;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.teaminfoadapter.FootTeamArrayAdapter;
import com.hhly.mlottery.bean.footballteaminfo.FootTeamMatchOddBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * desc:足球球队盘口
 * Created by 107_tangrr on 2017/4/24 0021.
 */

public class TeamInfoOddsFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TEAM_ID = "teamId";
    private static final String LEAGUE_DATE = "leagueDate";
    private final int LOADING = 0;
    private final int SUCCESS = 1;
    private final int ERROR = 2;
    private final int NOTO_DATA = 3;

    private Activity mContext;
    private View mView;
    private NestedScrollView scroll_view;
    private LinearLayout networkExceptionLayout, letTheBallContent, sizeBallContent;
    private TextView networkExceptionReloadBtn, tvNotData;
    private FrameLayout fl_mask_view;
    private RelativeLayout dataSelect;
    private ImageView teamSelect;
    private ExactSwipeRefreshLayout refreshLayout;

    private TextView theBallBt, sizeBallBt, dataContent, matchTotal, matchZu, matchKe, msTotal, msZu, msKe, nsTotal, nsZu, nsKe, tsTotal, tsZu, odds2Over25Ke, tsKe, hdpwTotal, hdpwZu, hdpwKe, hdphTotal, hdphZu, hdphKe, voiTotal, voiZu, voiKe, hdplhTotal, hdplhZu, hdplhKe, hdplTotal, hdplZu, hdplKe, winrateTotal, winrateZu, winrateKe, loserateTotal, loserateZu, loserateKe, odds2MatchTotal, odds2MatchZu, odds2MatchKe, odds2VoerTotal, odds2VoerZu, odds2VoerKe, odds2VoiTotal, odds2VoiZu, odds2VoiKe, odds2UnderTotal, odds2UnderZu, odds2UnderKe, odds2OverRateTotal, odds2OverRateZu, odds2OverRateKe, odds2VoiRateTotal, odds2Over25Zu, odds2VoiRateZu, odds2VoiRateKe, odds2UnderRateTotal, odds2UnderRateZu, odds2UnderRateKe, odds2Under25Total, odds2Under25Zu, odds2Under25Ke, odds2Over25Total;

    private List<FootTeamMatchOddBean.CurrHDPBean> currHDPList = new ArrayList<>();
    private List<FootTeamMatchOddBean.CurrOUBean> currOUList = new ArrayList<>();
    private String mTeamId;
    private String leagueDate;
    private List<String> items = new ArrayList<>();
    private int currentIndex = 0;
    private boolean isTheBallBt = true;// 是否为让球盘

    public static TeamInfoOddsFragment newInstance(String thirdId) {
        TeamInfoOddsFragment fragment = new TeamInfoOddsFragment();
        Bundle args = new Bundle();
        args.putString(TEAM_ID, thirdId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTeamId = getArguments().getString(TEAM_ID);
            leagueDate = getArguments().getString(LEAGUE_DATE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_football_team_info_odds, container, false);

        initView();
        return mView;
    }

    private void setData() {
        // 让球盘
        FootTeamMatchOddBean.CurrHDPBean hdpBean = currHDPList.get(currentIndex);
        if (hdpBean != null && hdpBean.getData() != null) {
            // 比赛场次
            if (hdpBean.getData().getMatchCount() != null) {
                String[] split = hdpBean.getData().getMatchCount().split(",");
                if (split.length >= 3) {
                    matchTotal.setText(split[0]);
                    matchZu.setText(split[1]);
                    matchKe.setText(split[2]);
                } else {
                    matchTotal.setText("-");
                    matchZu.setText("-");
                    matchKe.setText("-");
                }
            }
            // 上盘
            if (hdpBean.getData().getMs() != null) {
                String[] split = hdpBean.getData().getMs().split(",");
                if (split.length >= 3) {
                    msTotal.setText(split[0]);
                    msZu.setText(split[1]);
                    msKe.setText(split[2]);
                } else {
                    msTotal.setText("-");
                    msZu.setText("-");
                    msKe.setText("-");
                }
            }
            // 平盘
            if (hdpBean.getData().getNs() != null) {
                String[] split = hdpBean.getData().getNs().split(",");
                if (split.length >= 3) {
                    nsTotal.setText(split[0]);
                    nsZu.setText(split[1]);
                    nsKe.setText(split[2]);
                } else {
                    nsTotal.setText("-");
                    nsZu.setText("-");
                    nsKe.setText("-");
                }
            }
            // 下盘
            if (hdpBean.getData().getTs() != null) {
                String[] split = hdpBean.getData().getTs().split(",");
                if (split.length >= 3) {
                    tsTotal.setText(split[0]);
                    tsZu.setText(split[1]);
                    tsKe.setText(split[2]);
                } else {
                    tsTotal.setText("-");
                    tsZu.setText("-");
                    tsKe.setText("-");
                }
            }
            // 赢盘
            if (hdpBean.getData().getHdpw() != null) {
                String[] split = hdpBean.getData().getHdpw().split(",");
                if (split.length >= 3) {
                    hdpwTotal.setText(split[0]);
                    hdpwZu.setText(split[1]);
                    hdpwKe.setText(split[2]);
                } else {
                    hdpwTotal.setText("-");
                    hdpwZu.setText("-");
                    hdpwKe.setText("-");
                }
            }
            // 赢半
            if (hdpBean.getData().getHdph() != null) {
                String[] split = hdpBean.getData().getHdph().split(",");
                if (split.length >= 3) {
                    hdphTotal.setText(split[0]);
                    hdphZu.setText(split[1]);
                    hdphKe.setText(split[2]);
                } else {
                    hdphTotal.setText("-");
                    hdphZu.setText("-");
                    hdphKe.setText("-");
                }
            }
            // 走盘
            if (hdpBean.getData().getVoi() != null) {
                String[] split = hdpBean.getData().getVoi().split(",");
                if (split.length >= 3) {
                    voiTotal.setText(split[0]);
                    voiZu.setText(split[1]);
                    voiKe.setText(split[2]);
                } else {
                    voiTotal.setText("-");
                    voiZu.setText("-");
                    voiKe.setText("-");
                }
            }
            // 输半
            if (hdpBean.getData().getHdplh() != null) {
                String[] split = hdpBean.getData().getHdplh().split(",");
                if (split.length >= 3) {
                    hdplhTotal.setText(split[0]);
                    hdplhZu.setText(split[1]);
                    hdplhKe.setText(split[2]);
                } else {
                    hdplhTotal.setText("-");
                    hdplhZu.setText("-");
                    hdplhKe.setText("-");
                }
            }
            // 输盘
            if (hdpBean.getData().getHdpl() != null) {
                String[] split = hdpBean.getData().getHdpl().split(",");
                if (split.length >= 3) {
                    hdplTotal.setText(split[0]);
                    hdplZu.setText(split[1]);
                    hdplKe.setText(split[2]);
                } else {
                    hdplTotal.setText("-");
                    hdplZu.setText("-");
                    hdplKe.setText("-");
                }
            }
            // 胜率
            if (hdpBean.getData().getWinrate() != null) {
                String[] split = hdpBean.getData().getWinrate().split(",");
                if (split.length >= 3) {
                    winrateTotal.setText(split[0]);
                    winrateZu.setText(split[1]);
                    winrateKe.setText(split[2]);
                } else {
                    winrateTotal.setText("-");
                    winrateZu.setText("-");
                    winrateKe.setText("-");
                }
            }
            // 负率
            if (hdpBean.getData().getLoserate() != null) {
                String[] split = hdpBean.getData().getLoserate().split(",");
                if (split.length >= 3) {
                    loserateTotal.setText(split[0]);
                    loserateZu.setText(split[1]);
                    loserateKe.setText(split[2]);
                } else {
                    loserateTotal.setText("-");
                    loserateZu.setText("-");
                    loserateKe.setText("-");
                }
            }
        }
        // 大小球
        FootTeamMatchOddBean.CurrOUBean ouBean = currOUList.get(currentIndex);
        if (ouBean != null && ouBean.getData() != null) {
            // 比赛场次
            if (ouBean.getData().getMatchCount() != null) {
                String[] split = ouBean.getData().getMatchCount().split(",");
                if (split.length >= 3) {
                    odds2MatchTotal.setText(split[0]);
                    odds2MatchZu.setText(split[1]);
                    odds2MatchKe.setText(split[2]);
                } else {
                    odds2MatchTotal.setText("-");
                    odds2MatchZu.setText("-");
                    odds2MatchKe.setText("-");
                }
            }
            // 大球
            if (ouBean.getData().getOver() != null) {
                String[] split = ouBean.getData().getOver().split(",");
                if (split.length >= 3) {
                    odds2VoerTotal.setText(split[0]);
                    odds2VoerZu.setText(split[1]);
                    odds2VoerKe.setText(split[2]);
                } else {
                    odds2VoerTotal.setText("-");
                    odds2VoerZu.setText("-");
                    odds2VoerKe.setText("-");
                }
            }
            // 走盘
            if (ouBean.getData().getVoi() != null) {
                String[] split = ouBean.getData().getVoi().split(",");
                if (split.length >= 3) {
                    odds2VoiTotal.setText(split[0]);
                    odds2VoiZu.setText(split[1]);
                    odds2VoiKe.setText(split[2]);
                } else {
                    odds2VoiTotal.setText("-");
                    odds2VoiZu.setText("-");
                    odds2VoiKe.setText("-");
                }
            }
            // 小球
            if (ouBean.getData().getUnder() != null) {
                String[] split = ouBean.getData().getUnder().split(",");
                if (split.length >= 3) {
                    odds2UnderTotal.setText(split[0]);
                    odds2UnderZu.setText(split[1]);
                    odds2UnderKe.setText(split[2]);
                } else {
                    odds2UnderTotal.setText("-");
                    odds2UnderZu.setText("-");
                    odds2UnderKe.setText("-");
                }
            }
            // 大球率
            if (ouBean.getData().getOverRate() != null) {
                String[] split = ouBean.getData().getOverRate().split(",");
                if (split.length >= 3) {
                    odds2OverRateTotal.setText(split[0]);
                    odds2OverRateZu.setText(split[1]);
                    odds2OverRateKe.setText(split[2]);
                } else {
                    odds2OverRateTotal.setText("-");
                    odds2OverRateZu.setText("-");
                    odds2OverRateKe.setText("-");
                }
            }
            // 走盘率
            if (ouBean.getData().getVoiRate() != null) {
                String[] split = ouBean.getData().getVoiRate().split(",");
                if (split.length >= 3) {
                    odds2VoiRateTotal.setText(split[0]);
                    odds2VoiRateZu.setText(split[1]);
                    odds2VoiRateKe.setText(split[2]);
                } else {
                    odds2VoiRateTotal.setText("-");
                    odds2VoiRateZu.setText("-");
                    odds2VoiRateKe.setText("-");
                }
            }
            // 小球率
            if (ouBean.getData().getUnderRate() != null) {
                String[] split = ouBean.getData().getUnderRate().split(",");
                if (split.length >= 3) {
                    odds2UnderRateTotal.setText(split[0]);
                    odds2UnderRateZu.setText(split[1]);
                    odds2UnderRateKe.setText(split[2]);
                } else {
                    odds2UnderRateTotal.setText("-");
                    odds2UnderRateZu.setText("-");
                    odds2UnderRateKe.setText("-");
                }
            }
            // 低于2.5
            if (ouBean.getData().getUnder25() != null) {
                String[] split = ouBean.getData().getUnder25().split(",");
                if (split.length >= 3) {
                    odds2Under25Total.setText(split[0]);
                    odds2Under25Zu.setText(split[1]);
                    odds2Under25Ke.setText(split[2]);
                } else {
                    odds2Under25Total.setText("-");
                    odds2Under25Zu.setText("-");
                    odds2Under25Ke.setText("-");
                }
            }
            // 高于2.5
            if (ouBean.getData().getOver25() != null) {
                String[] split = ouBean.getData().getOver25().split(",");
                if (split.length >= 3) {
                    odds2Over25Total.setText(split[0]);
                    odds2Over25Zu.setText(split[1]);
                    odds2Over25Ke.setText(split[2]);
                } else {
                    odds2Over25Total.setText("-");
                    odds2Over25Zu.setText("-");
                    odds2Over25Ke.setText("-");
                }
            }
        }
    }

    private void initData() {
        if (leagueDate == null) {
            setStatus(ERROR);
            return;
        }
        setStatus(LOADING);

        final Map<String, String> map = new HashMap<>();
        map.put("teamId", mTeamId);// 球队ID
        map.put("leagueDate", leagueDate);// 日期

        VolleyContentFast.requestJsonByGet(BaseURLs.FOOT_TEAM_ODDS_URL, map, new VolleyContentFast.ResponseSuccessListener<FootTeamMatchOddBean>() {
            @Override
            public void onResponse(FootTeamMatchOddBean json) {
                currentIndex = 0;
                if (json != null && json.getCode() == 200) {
                    currOUList.clear();
                    currHDPList.clear();
                    currOUList.addAll(json.getCurrOU());
                    currHDPList.addAll(json.getCurrHDP());

                    items.clear();
                    for (FootTeamMatchOddBean.CurrHDPBean currHDPBean : currHDPList) {
                        items.add(currHDPBean.getLgName());
                    }
                    if (items.size() != 0) {
                        dataContent.setText(items.get(0));
                    }

                    if (isTheBallBt) {
                        if (currHDPList.size() == 0) {
                            setStatus(NOTO_DATA);
                            return;
                        }
                    } else {
                        if (currOUList.size() == 0) {
                            setStatus(NOTO_DATA);
                            return;
                        }
                    }
                    setStatus(SUCCESS);
                    setData();
                } else {
                    setStatus(NOTO_DATA);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                setStatus(ERROR);
            }
        }, FootTeamMatchOddBean.class);
    }

    private void initView() {
        scroll_view = (NestedScrollView) mView.findViewById(R.id.scroll_view);
        fl_mask_view = (FrameLayout) mView.findViewById(R.id.fl_mask_view);

        theBallBt = (TextView) mView.findViewById(R.id.tv_the_ball_bt);
        theBallBt.setOnClickListener(this);
        sizeBallBt = (TextView) mView.findViewById(R.id.tv_size_ball_bt);
        sizeBallBt.setOnClickListener(this);

        dataSelect = (RelativeLayout) mView.findViewById(R.id.ll_data_select);
        dataSelect.setOnClickListener(this);
        dataContent = (TextView) mView.findViewById(R.id.tv_data_content);
        teamSelect = (ImageView) mView.findViewById(R.id.tv_team_select);

        // 让球盘
        letTheBallContent = (LinearLayout) mView.findViewById(R.id.ll_let_the_ball_content);
        matchTotal = (TextView) mView.findViewById(R.id.tv_odds_match_total);
        matchZu = (TextView) mView.findViewById(R.id.tv_odds_match_zu);
        matchKe = (TextView) mView.findViewById(R.id.tv_odds_match_ke);
        msTotal = (TextView) mView.findViewById(R.id.tv_odds_ms_total);
        msZu = (TextView) mView.findViewById(R.id.tv_odds_ms_zu);
        msKe = (TextView) mView.findViewById(R.id.tv_odds_ms_ke);
        nsTotal = (TextView) mView.findViewById(R.id.tv_odds_ns_total);
        nsZu = (TextView) mView.findViewById(R.id.tv_odds_ns_zu);
        nsKe = (TextView) mView.findViewById(R.id.tv_odds_ns_ke);
        tsTotal = (TextView) mView.findViewById(R.id.tv_odds_ts_total);
        tsZu = (TextView) mView.findViewById(R.id.tv_odds_ts_zu);
        tsKe = (TextView) mView.findViewById(R.id.tv_odds_ts_ke);
        hdpwTotal = (TextView) mView.findViewById(R.id.tv_odds_hdpw_total);
        hdpwZu = (TextView) mView.findViewById(R.id.tv_odds_hdpw_zu);
        hdpwKe = (TextView) mView.findViewById(R.id.tv_odds_hdpw_ke);
        hdphTotal = (TextView) mView.findViewById(R.id.tv_odds_hdph_total);
        hdphZu = (TextView) mView.findViewById(R.id.tv_odds_hdph_zu);
        hdphKe = (TextView) mView.findViewById(R.id.tv_odds_hdph_ke);
        voiTotal = (TextView) mView.findViewById(R.id.tv_odds_voi_total);
        voiZu = (TextView) mView.findViewById(R.id.tv_odds_voi_zu);
        voiKe = (TextView) mView.findViewById(R.id.tv_odds_voi_ke);
        hdplhTotal = (TextView) mView.findViewById(R.id.tv_odds_hdplh_total);
        hdplhZu = (TextView) mView.findViewById(R.id.tv_odds_hdplh_zu);
        hdplhKe = (TextView) mView.findViewById(R.id.tv_odds_hdplh_ke);
        hdplTotal = (TextView) mView.findViewById(R.id.tv_odds_hdpl_total);
        hdplZu = (TextView) mView.findViewById(R.id.tv_odds_hdpl_zu);
        hdplKe = (TextView) mView.findViewById(R.id.tv_odds_hdpl_ke);
        winrateTotal = (TextView) mView.findViewById(R.id.tv_odds_winrate_total);
        winrateZu = (TextView) mView.findViewById(R.id.tv_odds_winrate_zu);
        winrateKe = (TextView) mView.findViewById(R.id.tv_odds_winrate_ke);
        loserateTotal = (TextView) mView.findViewById(R.id.tv_odds_loserate_total);
        loserateZu = (TextView) mView.findViewById(R.id.tv_odds_loserate_zu);
        loserateKe = (TextView) mView.findViewById(R.id.tv_odds_loserate_ke);

        // 大小球
        sizeBallContent = (LinearLayout) mView.findViewById(R.id.ll_size_ball_content);
        odds2MatchTotal = (TextView) mView.findViewById(R.id.tv_odds2_match_total);
        odds2MatchZu = (TextView) mView.findViewById(R.id.tv_odds2_match_zu);
        odds2MatchKe = (TextView) mView.findViewById(R.id.tv_odds2_match_ke);
        odds2VoerTotal = (TextView) mView.findViewById(R.id.tv_odds2_voer_total);
        odds2VoerZu = (TextView) mView.findViewById(R.id.tv_odds2_voer_zu);
        odds2VoerKe = (TextView) mView.findViewById(R.id.tv_odds2_voer_ke);
        odds2VoiTotal = (TextView) mView.findViewById(R.id.tv_odds2_voi_total);
        odds2VoiZu = (TextView) mView.findViewById(R.id.tv_odds2_voi_zu);
        odds2VoiKe = (TextView) mView.findViewById(R.id.tv_odds2_voi_ke);
        odds2UnderTotal = (TextView) mView.findViewById(R.id.tv_odds2_under_total);
        odds2UnderZu = (TextView) mView.findViewById(R.id.tv_odds2_under_zu);
        odds2UnderKe = (TextView) mView.findViewById(R.id.tv_odds2_under_ke);
        odds2OverRateTotal = (TextView) mView.findViewById(R.id.tv_odds2_over_rate_total);
        odds2OverRateZu = (TextView) mView.findViewById(R.id.tv_odds2_over_rate_zu);
        odds2OverRateKe = (TextView) mView.findViewById(R.id.tv_odds2_over_rate_ke);
        odds2VoiRateTotal = (TextView) mView.findViewById(R.id.tv_odds2_voi_rate_total);
        odds2VoiRateZu = (TextView) mView.findViewById(R.id.tv_odds2_voi_rate_zu);
        odds2VoiRateKe = (TextView) mView.findViewById(R.id.tv_odds2_voi_rate_ke);
        odds2UnderRateTotal = (TextView) mView.findViewById(R.id.tv_odds2_under_rate_total);
        odds2UnderRateZu = (TextView) mView.findViewById(R.id.tv_odds2_under_rate_zu);
        odds2UnderRateKe = (TextView) mView.findViewById(R.id.tv_odds2_under_rate_ke);
        odds2Under25Total = (TextView) mView.findViewById(R.id.tv_odds2_under25_total);
        odds2Under25Zu = (TextView) mView.findViewById(R.id.tv_odds2_under25_zu);
        odds2Under25Ke = (TextView) mView.findViewById(R.id.tv_odds2_under25_ke);
        odds2Over25Total = (TextView) mView.findViewById(R.id.tv_odds2_over25_total);
        odds2Over25Zu = (TextView) mView.findViewById(R.id.tv_odds2_over25_zu);
        odds2Over25Ke = (TextView) mView.findViewById(R.id.tv_odds2_over25_ke);

        networkExceptionLayout = (LinearLayout) mView.findViewById(R.id.network_exception_layout);
        networkExceptionReloadBtn = (TextView) mView.findViewById(R.id.network_exception_reload_btn);
        networkExceptionReloadBtn.setOnClickListener(this);

        tvNotData = (TextView) mView.findViewById(R.id.tv_not_data);

        refreshLayout = (ExactSwipeRefreshLayout) mView.findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.bg_header);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(mContext, StaticValues.REFRASH_OFFSET_END));
    }

    public void updataList(String data) {
        leagueDate = data;
        initData();
    }

    // 设置页面显示状态
    private void setStatus(int status) {
        refreshLayout.setRefreshing(status == LOADING);
        scroll_view.setVisibility(status == SUCCESS ? View.VISIBLE : View.GONE);
        networkExceptionLayout.setVisibility(status == ERROR ? View.VISIBLE : View.GONE);
        tvNotData.setVisibility(status == NOTO_DATA ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.network_exception_reload_btn:
                scroll_view.setVisibility(View.VISIBLE);
                networkExceptionLayout.setVisibility(View.GONE);
                tvNotData.setVisibility(View.GONE);
                initData();
                break;
            case R.id.tv_the_ball_bt:
                isTheBallBt = true;
                theBallBt.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                theBallBt.setBackgroundColor(ContextCompat.getColor(mContext, R.color.snooker_rank_txt_top3));
                sizeBallBt.setTextColor(ContextCompat.getColor(mContext, R.color.title));
                sizeBallBt.setBackgroundColor(ContextCompat.getColor(mContext, R.color.bot));
                letTheBallContent.setVisibility(View.VISIBLE);
                sizeBallContent.setVisibility(View.GONE);
                break;
            case R.id.tv_size_ball_bt:
                isTheBallBt = false;
                sizeBallBt.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                sizeBallBt.setBackgroundColor(ContextCompat.getColor(mContext, R.color.snooker_rank_txt_top3));
                theBallBt.setTextColor(ContextCompat.getColor(mContext, R.color.title));
                theBallBt.setBackgroundColor(ContextCompat.getColor(mContext, R.color.bot));
                letTheBallContent.setVisibility(View.GONE);
                sizeBallContent.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_data_select:
                if (items.size() > 0) {
                    popWindow();
                    fl_mask_view.setAlpha(0.3f);
                    teamSelect.setBackgroundResource(R.mipmap.foot_team_info_select_not);
                }
                break;
        }
    }

    private void popWindow() {
        final View mView = View.inflate(mContext, R.layout.popup_foot_team_info, null);
        // 创建ArrayAdapter对象
        FootTeamArrayAdapter mAdapter;
        mAdapter = new FootTeamArrayAdapter(mContext, items, currentIndex);

        ListView listview = (ListView) mView.findViewById(R.id.match_type);
        listview.setAdapter(mAdapter);

        final PopupWindow popupWindow = new PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(dataSelect.getWidth());
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.showAsDropDown(dataSelect);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentIndex = position;
                dataContent.setText(items.get(position));
                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setData();
                fl_mask_view.setAlpha(0f);
                teamSelect.setBackgroundResource(R.mipmap.foot_team_info_select_ok);
            }
        });
    }

    @Override
    public void onRefresh() {
        initData();
    }
}
