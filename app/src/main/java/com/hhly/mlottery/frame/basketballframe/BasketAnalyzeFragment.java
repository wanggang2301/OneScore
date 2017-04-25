package com.hhly.mlottery.frame.basketballframe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketAnalyzeMoreRecordActivity;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.activity.BasketballDatabaseDetailsActivity;
import com.hhly.mlottery.bean.basket.basketdetails.BasketAnalyzeBean;
import com.hhly.mlottery.bean.basket.basketdetails.BasketAnalyzeContentBean;
import com.hhly.mlottery.bean.basket.basketdetails.BasketAnalyzeFutureMatchBean;
import com.hhly.mlottery.bean.basket.infomation.LeagueBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.adapter.CommonAdapter;
import com.hhly.mlottery.util.adapter.ViewHolder;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @author yixq
 * @Description: 篮球分析的 fragment
 */
public class BasketAnalyzeFragment extends Fragment {

    //    public static final int REQUEST_MORERECORD = 0x80;
    private View mView;
    private ProgressBar mProgressBar;//历史交锋进度条
    private TextView mBasketProgressbarGuest;
    private TextView mBasketProgressbarHome;
    private ImageView mRecentGuestImg1;
    private ImageView mRecentGuestImg2;
    private ImageView mRecentGuestImg3;
    private ImageView mRecentGuestImg4;
    private ImageView mRecentGuestImg5;
    private ImageView mRecentGuestImg6;
    private ImageView mRecentHomeImg1;
    private ImageView mRecentHomeImg2;
    private ImageView mRecentHomeImg3;
    private ImageView mRecentHomeImg4;
    private ImageView mRecentHomeImg5;
    private ImageView mRecentHomeImg6;
    private TextView mFutureGuestDate1;
    private TextView mFutureGuestDate2;
    private TextView mFutureGuestDate3;
    private TextView mFutureGuestName1;
    private TextView mFutureGuestName2;
    private TextView mFutureGuestName3;
    private ImageView mFutureGuestImg1;
    private ImageView mFutureGuestImg2;
    private ImageView mFutureGuestImg3;
    private TextView mFutureHomeDate1;
    private TextView mFutureHomeDate2;
    private TextView mFutureHomeDate3;
    private TextView mFutureHomeName1;
    private TextView mFutureHomeName2;
    private TextView mFutureHomeName3;
    private ImageView mFutureHomeImg1;
    private ImageView mFutureHomeImg2;
    private ImageView mFutureHomeImg3;
    private TextView mRankingGuestName;
    private TextView mRankingHomeName;
    private TextView mRankingGuestOverGame;
    private TextView mRankingHomeOverGame;
    private TextView mRankingGuestResult;
    private TextView mRankingHomeResult;
    private TextView mRankingGuestWinRate;
    private TextView mRankingHomeWinRate;
    private TextView mGuestScoreWinSix;
    private TextView mGuestScoreLoseSix;
    private TextView mHomeScoreWinSix;
    private TextView mHomeScoreLoseSix;

    private TextView mBasketAnalyzeMoreRecord;
    private NestedScrollView scrollView;

    Handler mHandler = new Handler();
    private String mThirdId;
    private TextView mScoreWin;
    private TextView mScoreLose;
//    private ExactSwipeRefrashLayout mRefresh;//下拉刷新

    //    private NestedListView mListView1;
//    private NestedListView mListView2;
    private TextView mTextLine;

    private LinearLayout mFutureLinearLayout;
    private TextView mFutureNodata;
    private LinearLayout mRunkingLinearLayout;
    private TextView mRunkingNodata;
    private LinearLayout mRecentLinearLayout;
    private TextView mRecentNodata;
    private LinearLayout mGuest1;
    private LinearLayout mGuest2;
    private LinearLayout mGuest3;
    private LinearLayout mHome1;
    private LinearLayout mHome2;
    private LinearLayout mHome3;
    private String mLeagueId;
    private Integer mMatchType;
    private TextView mMostData;

    private Context mContext;
    private Activity mActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = mActivity;


        mThirdId = ((BasketDetailsActivityTest) getActivity()).getmThirdId();
        L.d("mThirdId ==AAAAA===", mThirdId + "");
        mLeagueId = ((BasketDetailsActivityTest) getActivity()).getmLeagueId();
        mMatchType = ((BasketDetailsActivityTest) getActivity()).getmMatchType();

        mView = inflater.inflate(R.layout.basket_analysis, container, false);
        scrollView = (NestedScrollView) mView.findViewById(R.id.scroll);

        try {
            initView();
            mHandler.postDelayed(mRun, 500); // 加载数据
        } catch (Exception e) {
            L.e(e.getMessage());
        }


        return mView;
    }

    /**
     * 子线程 处理数据加载
     */
    private Runnable mRun = new Runnable() {
        @Override
        public void run() {
            initData();
        }
    };

    private void initView() {
        mProgressBar = (ProgressBar) mView.findViewById(R.id.basket_progressbar);
        mBasketProgressbarGuest = (TextView) mView.findViewById(R.id.basket_progressbar_guest);

        mBasketProgressbarHome = (TextView) mView.findViewById(R.id.basket_progressbar_home);
        mRecentGuestImg1 = (ImageView) mView.findViewById(R.id.basket_img_recent_guest1);
        mRecentGuestImg2 = (ImageView) mView.findViewById(R.id.basket_img_recent_guest2);
        mRecentGuestImg3 = (ImageView) mView.findViewById(R.id.basket_img_recent_guest3);
        mRecentGuestImg4 = (ImageView) mView.findViewById(R.id.basket_img_recent_guest4);
        mRecentGuestImg5 = (ImageView) mView.findViewById(R.id.basket_img_recent_guest5);
        mRecentGuestImg6 = (ImageView) mView.findViewById(R.id.basket_img_recent_guest6);

        mRecentHomeImg1 = (ImageView) mView.findViewById(R.id.basket_img_recent_home1);
        mRecentHomeImg2 = (ImageView) mView.findViewById(R.id.basket_img_recent_home2);
        mRecentHomeImg3 = (ImageView) mView.findViewById(R.id.basket_img_recent_home3);
        mRecentHomeImg4 = (ImageView) mView.findViewById(R.id.basket_img_recent_home4);
        mRecentHomeImg5 = (ImageView) mView.findViewById(R.id.basket_img_recent_home5);
        mRecentHomeImg6 = (ImageView) mView.findViewById(R.id.basket_img_recent_home6);

        mFutureGuestDate1 = (TextView) mView.findViewById(R.id.basket_future_guest_date1);
        mFutureGuestDate2 = (TextView) mView.findViewById(R.id.basket_future_guest_date2);
        mFutureGuestDate3 = (TextView) mView.findViewById(R.id.basket_future_guest_date3);

        mFutureGuestName1 = (TextView) mView.findViewById(R.id.basket_future_guest_name1);
        mFutureGuestName2 = (TextView) mView.findViewById(R.id.basket_future_guest_name2);
        mFutureGuestName3 = (TextView) mView.findViewById(R.id.basket_future_guest_name3);

        mFutureGuestImg1 = (ImageView) mView.findViewById(R.id.basket_future_guest_image1);
        mFutureGuestImg2 = (ImageView) mView.findViewById(R.id.basket_future_guest_image2);
        mFutureGuestImg3 = (ImageView) mView.findViewById(R.id.basket_future_guest_image3);

        mFutureHomeDate1 = (TextView) mView.findViewById(R.id.basket_future_home_date1);
        mFutureHomeDate2 = (TextView) mView.findViewById(R.id.basket_future_home_date2);
        mFutureHomeDate3 = (TextView) mView.findViewById(R.id.basket_future_home_date3);

        mFutureHomeName1 = (TextView) mView.findViewById(R.id.basket_future_home_name1);
        mFutureHomeName2 = (TextView) mView.findViewById(R.id.basket_future_home_name2);
        mFutureHomeName3 = (TextView) mView.findViewById(R.id.basket_future_home_name3);

        mFutureHomeImg1 = (ImageView) mView.findViewById(R.id.basket_future_home_image1);
        mFutureHomeImg2 = (ImageView) mView.findViewById(R.id.basket_future_home_image2);
        mFutureHomeImg3 = (ImageView) mView.findViewById(R.id.basket_future_home_image3);

        mRankingGuestName = (TextView) mView.findViewById(R.id.basket_ranking_guest_name);
        mRankingHomeName = (TextView) mView.findViewById(R.id.basket_ranking_home_name);

        mRankingGuestOverGame = (TextView) mView.findViewById(R.id.basket_ranking_guest_overgame);
        mRankingHomeOverGame = (TextView) mView.findViewById(R.id.basket_ranking_home_overgame);

        mRankingGuestResult = (TextView) mView.findViewById(R.id.basket_ranking_guest_result);
        mRankingHomeResult = (TextView) mView.findViewById(R.id.basket_ranking_home_result);

        mRankingGuestWinRate = (TextView) mView.findViewById(R.id.basket_ranking_guest_winrate);
        mRankingHomeWinRate = (TextView) mView.findViewById(R.id.basket_ranking_home_winrate);

        mGuestScoreWinSix = (TextView) mView.findViewById(R.id.basket_guest_scorewinix);
        mGuestScoreLoseSix = (TextView) mView.findViewById(R.id.basket_guest_scorelosesix);

        mHomeScoreWinSix = (TextView) mView.findViewById(R.id.basket_home_scorewinsix);
        mHomeScoreLoseSix = (TextView) mView.findViewById(R.id.basket_home_scorelosesix);

        mScoreWin = (TextView) mView.findViewById(R.id.basket_score_win);
        mScoreLose = (TextView) mView.findViewById(R.id.basket_score_lose);

        mBasketAnalyzeMoreRecord = (TextView) mView.findViewById(R.id.basket_analyze_more_record);
        mBasketAnalyzeMoreRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(MyApp.getContext(), "BasketAnalyzeMoreRecordActivity");
                Intent intent = new Intent(getActivity(), BasketAnalyzeMoreRecordActivity.class);
                intent.putExtra(BasketAnalyzeMoreRecordActivity.BASKET_ANALYZE_THIRD_ID, mThirdId);//跳转到详情
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
            }
        });

        //分割线
        mTextLine = (TextView) mView.findViewById(R.id.basket_analyze_line);


        //未来比赛
        mFutureLinearLayout = (LinearLayout) mView.findViewById(R.id.basket_analyze_future);
        mFutureNodata = (TextView) mView.findViewById(R.id.basket_analyze_nodata1);

        //积分排名
        mRunkingLinearLayout = (LinearLayout) mView.findViewById(R.id.basket_analyze_runking);
        mRunkingNodata = (TextView) mView.findViewById(R.id.basket_analyze_nodata2);

        //最近表现
        mRecentLinearLayout = (LinearLayout) mView.findViewById(R.id.basket_analyze_scorewin);
        mRecentNodata = (TextView) mView.findViewById(R.id.basket_analyze_nodata3);

        mGuest1 = (LinearLayout) mView.findViewById(R.id.basket_analyze_guest1);
        mGuest2 = (LinearLayout) mView.findViewById(R.id.basket_analyze_guest2);
        mGuest3 = (LinearLayout) mView.findViewById(R.id.basket_analyze_guest3);
        mHome1 = (LinearLayout) mView.findViewById(R.id.basket_analyze_home1);
        mHome2 = (LinearLayout) mView.findViewById(R.id.basket_analyze_home2);
        mHome3 = (LinearLayout) mView.findViewById(R.id.basket_analyze_home3);

        mMostData = (TextView) mView.findViewById(R.id.basket_analyze_more_data);
    }

    /**
     * 跳转篮球资料库
     */
    private void setMostOnclick() {
        mMostData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BasketballDatabaseDetailsActivity.class);

                LeagueBean bean = new LeagueBean();
                bean.setLeagueId(mLeagueId);
                bean.setMatchType(mMatchType);
                intent.putExtra("league", bean);
                intent.putExtra("isRanking", true);
                startActivity(intent);
            }
        });
    }


    public void initData() {

//        String url = "http://192.168.10.242:8181/mlottery/core/basketballDetail.findAnalysis.do";
        String url = BaseURLs.URL_BASKET_ANALYZE;
        Map<String, String> map = new Hashtable<>();
        map.put("thirdId", mThirdId);
//        map.put("thirdId", "228110");
        VolleyContentFast.requestJsonByGet(url, map, new VolleyContentFast.ResponseSuccessListener<BasketAnalyzeBean>() {
            @Override
            public void onResponse(BasketAnalyzeBean json) {

                if (getActivity() == null || json == null) {
                    return;
                }

                setMostOnclick();

                List<BasketAnalyzeContentBean> mAnalyzeDatas = new ArrayList<>();
                mAnalyzeDatas.add(json.getGuestData());
                mAnalyzeDatas.add(json.getHomeData());
                if (json.getGuestData() != null && json.getHomeData() != null) {
                    //未来比赛
                    mFutureLinearLayout.setVisibility(View.VISIBLE);
                    mFutureNodata.setVisibility(View.GONE);
                    //积分排名
                    mRunkingLinearLayout.setVisibility(View.VISIBLE);
                    mRunkingNodata.setVisibility(View.GONE);
                    //最近表现
                    mRecentLinearLayout.setVisibility(View.VISIBLE);
                    mRecentNodata.setVisibility(View.GONE);

                    setDatas(mAnalyzeDatas);
                } else {
                    //未来比赛
                    mFutureLinearLayout.setVisibility(View.GONE);
                    mFutureNodata.setVisibility(View.VISIBLE);
                    //积分排名
                    mRunkingLinearLayout.setVisibility(View.GONE);
                    mRunkingNodata.setVisibility(View.VISIBLE);
                    //最近表现
                    mRecentLinearLayout.setVisibility(View.GONE);
                    mRecentNodata.setVisibility(View.VISIBLE);
                }

                //未来赛事中数据处理
                List<BasketAnalyzeFutureMatchBean> guestFuture = new ArrayList<>();
                List<BasketAnalyzeFutureMatchBean> homeFuture = new ArrayList<>();
                int guestNum;
                int homeNum;
                if (json.getGuestData() != null) {
                    guestFuture = json.getGuestData().getFutureMatch();
                    guestNum = guestFuture.size();
                } else {
                    guestNum = 0;
                }
                if (json.getHomeData() != null) {
                    homeFuture = json.getHomeData().getFutureMatch();
                    homeNum = homeFuture.size();
                } else {
                    homeNum = 0;
                }

                if (guestFuture.size() == 0 && homeFuture.size() == 0) {
                    //未来比赛
                    mFutureLinearLayout.setVisibility(View.GONE);
                    mFutureNodata.setVisibility(View.VISIBLE);
                } else {

                    //未来比赛
                    mFutureLinearLayout.setVisibility(View.VISIBLE);
                    mFutureNodata.setVisibility(View.GONE);

                    switch (guestNum) {
                        case 0:
                            if (homeNum >= 1) {
                                if (homeNum == 1) {
                                    mGuest1.setVisibility(View.INVISIBLE);
                                    mGuest2.setVisibility(View.GONE);
                                    mGuest3.setVisibility(View.GONE);
                                } else if (homeNum == 2) {
                                    mGuest1.setVisibility(View.INVISIBLE);
                                    mGuest2.setVisibility(View.INVISIBLE);
                                    mGuest3.setVisibility(View.GONE);
                                } else {
                                    mGuest1.setVisibility(View.INVISIBLE);
                                    mGuest2.setVisibility(View.INVISIBLE);
                                    mGuest3.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                mGuest1.setVisibility(View.GONE);
                                mGuest2.setVisibility(View.GONE);
                                mGuest3.setVisibility(View.GONE);
                            }
                            break;
                        case 1:
                            if (homeNum >= 2) {
                                if (homeNum == 2) {
                                    mGuest1.setVisibility(View.VISIBLE);
                                    mGuest2.setVisibility(View.INVISIBLE);
                                    mGuest3.setVisibility(View.GONE);
                                } else {
                                    mGuest1.setVisibility(View.VISIBLE);
                                    mGuest2.setVisibility(View.INVISIBLE);
                                    mGuest3.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                mGuest1.setVisibility(View.VISIBLE);
                                mGuest2.setVisibility(View.GONE);
                                mGuest3.setVisibility(View.GONE);
                            }
                            break;
                        case 2:
                            if (homeNum == 3) {
                                mGuest1.setVisibility(View.VISIBLE);
                                mGuest2.setVisibility(View.VISIBLE);
                                mGuest3.setVisibility(View.INVISIBLE);
                            } else {
                                mGuest1.setVisibility(View.VISIBLE);
                                mGuest2.setVisibility(View.VISIBLE);
                                mGuest3.setVisibility(View.GONE);
                            }
                            break;
                        case 3:
                            mGuest1.setVisibility(View.VISIBLE);
                            mGuest2.setVisibility(View.VISIBLE);
                            mGuest3.setVisibility(View.VISIBLE);
                            break;
                    }
                    switch (homeNum) {
                        case 0:
                            if (guestNum >= 1) {
                                if (guestNum == 1) {
                                    mHome1.setVisibility(View.INVISIBLE);
                                    mHome2.setVisibility(View.GONE);
                                    mHome3.setVisibility(View.GONE);
                                } else if (guestNum == 2) {
                                    mHome1.setVisibility(View.INVISIBLE);
                                    mHome2.setVisibility(View.INVISIBLE);
                                    mHome3.setVisibility(View.GONE);
                                } else {
                                    mHome1.setVisibility(View.INVISIBLE);
                                    mHome2.setVisibility(View.INVISIBLE);
                                    mHome3.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                mHome1.setVisibility(View.GONE);
                                mHome2.setVisibility(View.GONE);
                                mHome3.setVisibility(View.GONE);
                            }
                            break;
                        case 1:
                            if (guestNum >= 2) {
                                if (guestNum == 2) {
                                    mHome1.setVisibility(View.VISIBLE);
                                    mHome2.setVisibility(View.INVISIBLE);
                                    mHome3.setVisibility(View.GONE);
                                } else {
                                    mHome1.setVisibility(View.VISIBLE);
                                    mHome2.setVisibility(View.INVISIBLE);
                                    mHome3.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                mHome1.setVisibility(View.VISIBLE);
                                mHome2.setVisibility(View.GONE);
                                mHome3.setVisibility(View.GONE);
                            }
                            break;
                        case 2:
                            if (guestNum == 3) {
                                mHome1.setVisibility(View.VISIBLE);
                                mHome2.setVisibility(View.VISIBLE);
                                mHome3.setVisibility(View.INVISIBLE);
                            } else {
                                mHome1.setVisibility(View.VISIBLE);
                                mHome2.setVisibility(View.VISIBLE);
                                mHome3.setVisibility(View.GONE);
                            }
                            break;
                        case 3:
                            mHome1.setVisibility(View.VISIBLE);
                            mHome2.setVisibility(View.VISIBLE);
                            mHome3.setVisibility(View.VISIBLE);
                            break;
                    }

                    if (guestNum >= homeNum) {
                        setLineHeight(guestNum);
                    } else {
                        setLineHeight(homeNum);
                    }
                }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                setErrorDatas();
            }
        }, BasketAnalyzeBean.class);
    }

    /**
     * 设置分割线长度（根据未来比赛数据）
     *
     * @param num
     */
    private void setLineHeight(int num) {
        switch (num) {
            case 1:
                mTextLine.setLayoutParams(new LinearLayout.LayoutParams(1, getResources().getDimensionPixelSize(R.dimen.basket_analyze_line1)));
                break;
            case 2:
                mTextLine.setLayoutParams(new LinearLayout.LayoutParams(1, getResources().getDimensionPixelSize(R.dimen.basket_analyze_line2)));
                break;
            case 3:
                mTextLine.setLayoutParams(new LinearLayout.LayoutParams(1, getResources().getDimensionPixelSize(R.dimen.basket_analyze_line3)));
                break;
            default:
                break;
        }

    }

    /**
     * 请求数据失败设置
     */
    private void setErrorDatas() {

        mProgressBar.setProgress(50);
        mBasketProgressbarGuest.setText("--");
        mBasketProgressbarHome.setText("--");

        //设置textview位置（权重）
        mBasketProgressbarGuest.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        mBasketProgressbarHome.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        //未来比赛
        mFutureLinearLayout.setVisibility(View.GONE);
        mFutureNodata.setVisibility(View.VISIBLE);
        //积分排名
        mRunkingLinearLayout.setVisibility(View.GONE);
        mRunkingNodata.setVisibility(View.VISIBLE);
        //最近表现
        mRecentLinearLayout.setVisibility(View.GONE);
        mRecentNodata.setVisibility(View.VISIBLE);

    }

    /**
     * 数据设置
     *
     * @param mAnalyzeDatas
     */
    private void setDatas(List<BasketAnalyzeContentBean> mAnalyzeDatas) {

        if (mAnalyzeDatas == null) {
            return;
        }

        /**
         * 历史交锋
         */
        int guestWins = mAnalyzeDatas.get(0).getHistoryWin();
        int homeWins = mAnalyzeDatas.get(1).getHistoryWin();
        int progressNum;

        int guestNumWin;
        int homeNumWin;

        if (guestWins == 0 && homeWins == 0) {
            progressNum = 50;
        } else {
            progressNum = guestWins * 100 / (homeWins + guestWins);
        }

        if (guestWins == 0) {
            guestNumWin = 1;
        } else {
            guestNumWin = guestWins;
        }
        if (homeWins == 0) {
            homeNumWin = 1;
        } else {
            homeNumWin = homeWins;
        }

        /**
         * 过滤主客队历史交锋1-0 或 0-1 时 X胜显示位置会居中问题
         */
        if (guestWins != 0 && homeWins == 0) {
            mBasketProgressbarGuest.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 15));
            mBasketProgressbarHome.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0));
        } else if (guestWins == 0 && homeWins != 0) {
            mBasketProgressbarGuest.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0));
            mBasketProgressbarHome.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 15));
        } else {
            //设置textview位置（权重）
            mBasketProgressbarGuest.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, guestNumWin));
            mBasketProgressbarHome.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, homeNumWin));
        }
        mProgressBar.setProgress(progressNum);
        mBasketProgressbarGuest.setText(mAnalyzeDatas.get(0).getHistoryWin() + "" + getResources().getText(R.string.basket_analyze_win));
        mBasketProgressbarHome.setText(mAnalyzeDatas.get(1).getHistoryWin() + "" + getResources().getText(R.string.basket_analyze_win));

        /**
         * 近期战绩
         */
        //客队 近期战绩
        List<Integer> mRecentGuest = mAnalyzeDatas.get(0).getRecentLW();
        if (mRecentGuest != null && !mRecentGuest.equals("")) {
            if (mRecentGuest.size() != 0) {
                setRecent(mRecentGuestImg1, mRecentGuest.get(0));
                setRecent(mRecentGuestImg2, mRecentGuest.get(1));
                setRecent(mRecentGuestImg3, mRecentGuest.get(2));
                setRecent(mRecentGuestImg4, mRecentGuest.get(3));
                setRecent(mRecentGuestImg5, mRecentGuest.get(4));
                setRecent(mRecentGuestImg6, mRecentGuest.get(5));
            }
        }
        List<Integer> mRecentHome = mAnalyzeDatas.get(1).getRecentLW();
        if (mRecentHome != null && !mRecentHome.equals("")) {
            if (mRecentHome.size() != 0) {
                setRecent(mRecentHomeImg1, mRecentHome.get(0));
                setRecent(mRecentHomeImg2, mRecentHome.get(1));
                setRecent(mRecentHomeImg3, mRecentHome.get(2));
                setRecent(mRecentHomeImg4, mRecentHome.get(3));
                setRecent(mRecentHomeImg5, mRecentHome.get(4));
                setRecent(mRecentHomeImg6, mRecentHome.get(5));
            }
        }
        /**
         * 未来三场（未来比赛）
         */
        List<BasketAnalyzeFutureMatchBean> mFutureMatchGuest = mAnalyzeDatas.get(0).getFutureMatch();
        if (mFutureMatchGuest.size() != 0) {
            if (mFutureMatchGuest.size() == 1) {
                setFutureMatch(mFutureGuestDate1, mFutureGuestName1, mFutureGuestImg1, mFutureMatchGuest.get(0), true);
                setFutureMatch(mFutureGuestDate2, mFutureGuestName2, mFutureGuestImg2, null, false);
                setFutureMatch(mFutureGuestDate3, mFutureGuestName3, mFutureGuestImg3, null, false);
            } else if (mFutureMatchGuest.size() == 2) {
                setFutureMatch(mFutureGuestDate1, mFutureGuestName1, mFutureGuestImg1, mFutureMatchGuest.get(0), true);
                setFutureMatch(mFutureGuestDate2, mFutureGuestName2, mFutureGuestImg2, mFutureMatchGuest.get(1), true);
                setFutureMatch(mFutureGuestDate3, mFutureGuestName3, mFutureGuestImg3, null, false);
            } else if (mFutureMatchGuest.size() == 3) {
                setFutureMatch(mFutureGuestDate1, mFutureGuestName1, mFutureGuestImg1, mFutureMatchGuest.get(0), true);
                setFutureMatch(mFutureGuestDate2, mFutureGuestName2, mFutureGuestImg2, mFutureMatchGuest.get(1), true);
                setFutureMatch(mFutureGuestDate3, mFutureGuestName3, mFutureGuestImg3, mFutureMatchGuest.get(2), true);
            }
        }

        List<BasketAnalyzeFutureMatchBean> mFutureMatchHome = mAnalyzeDatas.get(1).getFutureMatch();
        if (mFutureMatchHome.size() != 0) {
            if (mFutureMatchHome.size() == 1) {
                setFutureMatch(mFutureHomeDate1, mFutureHomeName1, mFutureHomeImg1, mFutureMatchHome.get(0), true);
                setFutureMatch(mFutureHomeDate2, mFutureHomeName2, mFutureHomeImg2, null, false);
                setFutureMatch(mFutureHomeDate3, mFutureHomeName3, mFutureHomeImg3, null, false);
            } else if (mFutureMatchHome.size() == 2) {
                setFutureMatch(mFutureHomeDate1, mFutureHomeName1, mFutureHomeImg1, mFutureMatchHome.get(0), true);
                setFutureMatch(mFutureHomeDate2, mFutureHomeName2, mFutureHomeImg2, mFutureMatchHome.get(1), true);
                setFutureMatch(mFutureHomeDate3, mFutureHomeName3, mFutureHomeImg3, null, false);
            } else if (mFutureMatchHome.size() == 3) {
                setFutureMatch(mFutureHomeDate1, mFutureHomeName1, mFutureHomeImg1, mFutureMatchHome.get(0), true);
                setFutureMatch(mFutureHomeDate2, mFutureHomeName2, mFutureHomeImg2, mFutureMatchHome.get(1), true);
                setFutureMatch(mFutureHomeDate3, mFutureHomeName3, mFutureHomeImg3, mFutureMatchHome.get(2), true);
            }
        }
        /**
         * 积分排行
         */
        String mGuestRanking;//排名
        String mGuestTeam;//球队
        String mGuestMatchAll;//已赛
        String mGuestWins;//胜
        String mGuestLose;//负
        String mGuestWinRate;//胜率

        String mHomeRanking;//排名
        String mHomeTeam;//球队
        String mHomeMatchAll;//已赛
        String mHomeWins;//胜
        String mHomeLose;//负
        String mHomeWinRate;//胜率


        if (mAnalyzeDatas.get(0).getRanking() == null || mAnalyzeDatas.get(0).getRanking().equals("")) {
            mGuestRanking = "--";
        } else {
            mGuestRanking = mAnalyzeDatas.get(0).getRanking();
        }
        if (mAnalyzeDatas.get(0).getTeam() == null || mAnalyzeDatas.get(0).getTeam().equals("")) {
            mGuestTeam = "--";
        } else {
            mGuestTeam = mAnalyzeDatas.get(0).getTeam();
        }
        if (mAnalyzeDatas.get(0).getMatchAll() == null || mAnalyzeDatas.get(0).getMatchAll().equals("")) {
            mGuestMatchAll = "--";
        } else {
            mGuestMatchAll = mAnalyzeDatas.get(0).getMatchAll();
        }
        if (mAnalyzeDatas.get(0).getMatchWin() == null || mAnalyzeDatas.get(0).getMatchWin().equals("")) {
            mGuestWins = "--";
        } else {
            mGuestWins = mAnalyzeDatas.get(0).getMatchWin();
        }
        if (mAnalyzeDatas.get(0).getMatchLose() == null || mAnalyzeDatas.get(0).getMatchLose().equals("")) {
            mGuestLose = "--";
        } else {
            mGuestLose = mAnalyzeDatas.get(0).getMatchLose();
        }
        if (mAnalyzeDatas.get(0).getMatchWinRate() == null || mAnalyzeDatas.get(0).getMatchWinRate().equals("")) {
            mGuestWinRate = "--";
        } else {
            mGuestWinRate = mAnalyzeDatas.get(0).getMatchWinRate();
        }

        if (mAnalyzeDatas.get(1).getRanking() == null || mAnalyzeDatas.get(1).getRanking().equals("")) {
            mHomeRanking = "--";
        } else {
            mHomeRanking = mAnalyzeDatas.get(1).getRanking();
        }
        if (mAnalyzeDatas.get(1).getTeam() == null || mAnalyzeDatas.get(1).getTeam().equals("")) {
            mHomeTeam = "--";
        } else {
            mHomeTeam = mAnalyzeDatas.get(1).getTeam();
        }
        if (mAnalyzeDatas.get(1).getMatchAll() == null || mAnalyzeDatas.get(1).getMatchAll().equals("")) {
            mHomeMatchAll = "--";
        } else {
            mHomeMatchAll = mAnalyzeDatas.get(1).getMatchAll();
        }
        if (mAnalyzeDatas.get(1).getMatchWin() == null || mAnalyzeDatas.get(1).getMatchWin().equals("")) {
            mHomeWins = "--";
        } else {
            mHomeWins = mAnalyzeDatas.get(1).getMatchWin();
        }
        if (mAnalyzeDatas.get(1).getMatchLose() == null || mAnalyzeDatas.get(1).getMatchLose().equals("")) {
            mHomeLose = "--";
        } else {
            mHomeLose = mAnalyzeDatas.get(1).getMatchLose();
        }
        if (mAnalyzeDatas.get(1).getMatchWinRate() == null || mAnalyzeDatas.get(1).getMatchWinRate().equals("")) {
            mHomeWinRate = "--";
        } else {
            mHomeWinRate = mAnalyzeDatas.get(1).getMatchWinRate();
        }
        /**
         * 过滤无数据状态
         */
        if (mGuestRanking.equals("--") && mGuestTeam.equals("--") && mGuestMatchAll.equals("--") && mGuestWins.equals("--") && mGuestLose.equals("--") && mGuestWinRate.equals("--") && mHomeRanking.equals("--") && mHomeTeam.equals("--")
                && mHomeMatchAll.equals("--") && mHomeWins.equals("--") && mHomeLose.equals("--") && mHomeWinRate.equals("--")) {
            //积分排名
            mRunkingLinearLayout.setVisibility(View.GONE);
            mRunkingNodata.setVisibility(View.VISIBLE);
        } else {
            //积分排名
            mRunkingLinearLayout.setVisibility(View.VISIBLE);
            mRunkingNodata.setVisibility(View.GONE);
            //排名/球队
            mRankingGuestName.setText(mGuestRanking + "  " + mGuestTeam);
            mRankingHomeName.setText(mHomeRanking + "  " + mHomeTeam);
            if (mGuestRanking.equals("--") || mGuestTeam.equals("--")) {
                mRankingGuestName.setTextColor(getResources().getColor(R.color.black_details_ball_textcolor));
            } else {
                mRankingGuestName.setTextColor(getResources().getColor(R.color.mdy_666));
            }
            if (mHomeRanking.equals("--") || mHomeTeam.equals("--")) {
                mRankingHomeName.setTextColor(getResources().getColor(R.color.black_details_ball_textcolor));
            } else {
                mRankingHomeName.setTextColor(getResources().getColor(R.color.mdy_666));
            }
            //已赛
//        mRankingGuestOverGame.setText(mAnalyzeDatas.get(0).getMatchAll());
            mRankingGuestOverGame.setText(mGuestMatchAll);
            mRankingHomeOverGame.setText(mHomeMatchAll);
            //胜负
            mRankingGuestResult.setText(mGuestWins + "/" + mGuestLose);
            mRankingHomeResult.setText(mHomeWins + "/" + mHomeLose);
            //胜率
            mRankingGuestWinRate.setText(mGuestWinRate);
            mRankingHomeWinRate.setText(mHomeWinRate);
            if (mGuestWinRate.equals("--")) {
                mRankingGuestWinRate.setTextColor(getResources().getColor(R.color.black_details_ball_textcolor));
            } else {
                mRankingGuestWinRate.setTextColor(getResources().getColor(R.color.mdy_666));
            }
            if (mHomeWinRate.equals("--")) {
                mRankingHomeWinRate.setTextColor(getResources().getColor(R.color.black_details_ball_textcolor));
            } else {
                mRankingHomeWinRate.setTextColor(getResources().getColor(R.color.mdy_666));
            }
        }

        /**
         * 最近表现
         */

        String mGuestScoreWin;
        String mGuestScoreLose;
        String mHomeScoreWin;
        String mHomeScoreLose;

        Double guestWin;
        Double homeWin;
        Double guestLose;
        Double homeLose;

        if (mAnalyzeDatas.get(0).getScoreWinSix() == null || mAnalyzeDatas.get(0).getScoreWinSix().equals("")) {
            mGuestScoreWin = "--";
        } else {
            mGuestScoreWin = mAnalyzeDatas.get(0).getScoreWinSix();
        }
        if (mAnalyzeDatas.get(0).getScoreLoseSix() == null || mAnalyzeDatas.get(0).getScoreLoseSix().equals("")) {
            mGuestScoreLose = "--";
        } else {
            mGuestScoreLose = mAnalyzeDatas.get(0).getScoreLoseSix();
        }
        if (mAnalyzeDatas.get(1).getScoreWinSix() == null || mAnalyzeDatas.get(1).getScoreWinSix().equals("")) {
            mHomeScoreWin = "--";
        } else {
            mHomeScoreWin = mAnalyzeDatas.get(1).getScoreWinSix();
        }
        if (mAnalyzeDatas.get(1).getScoreLoseSix() == null || mAnalyzeDatas.get(1).getScoreLoseSix().equals("")) {
            mHomeScoreLose = "--";
        } else {
            mHomeScoreLose = mAnalyzeDatas.get(1).getScoreLoseSix();
        }

        mGuestScoreWinSix.setText(mGuestScoreWin);
        mHomeScoreWinSix.setText(mHomeScoreWin);
        mGuestScoreLoseSix.setText(mGuestScoreLose);
        mHomeScoreLoseSix.setText(mHomeScoreLose);

        /**
         * 设置最近表现平均分颜色
         */
        if (!mGuestScoreWin.equals("--")) {
            guestWin = Double.parseDouble(mGuestScoreWin);
        } else {
            guestWin = 0d;
        }
        if (!mHomeScoreWin.equals("--")) {
            homeWin = Double.parseDouble(mHomeScoreWin);
        } else {
            homeWin = 0d;
        }
        if (!mGuestScoreLose.equals("--")) {
            guestLose = Double.parseDouble(mGuestScoreLose);
        } else {
            guestLose = 0d;
        }
        if (!mHomeScoreLose.equals("--")) {
            homeLose = Double.parseDouble(mHomeScoreLose);
        } else {
            homeLose = 0d;
        }

        if (guestWin > homeWin) {
            mGuestScoreWinSix.setTextColor(getResources().getColor(R.color.mdy_666));
            mHomeScoreWinSix.setTextColor(getResources().getColor(R.color.black_details_ball_textcolor));
        } else if (guestWin < homeWin) {
            mHomeScoreWinSix.setTextColor(getResources().getColor(R.color.mdy_666));
            mGuestScoreWinSix.setTextColor(getResources().getColor(R.color.black_details_ball_textcolor));
        } else {
            mHomeScoreWinSix.setTextColor(getResources().getColor(R.color.black_details_ball_textcolor));
            mGuestScoreWinSix.setTextColor(getResources().getColor(R.color.black_details_ball_textcolor));
        }

        if (guestLose > homeLose) {
            mGuestScoreLoseSix.setTextColor(getResources().getColor(R.color.mdy_666));
            mHomeScoreLoseSix.setTextColor(getResources().getColor(R.color.black_details_ball_textcolor));
        } else if (guestLose < homeLose) {
            mHomeScoreLoseSix.setTextColor(getResources().getColor(R.color.mdy_666));
            mGuestScoreLoseSix.setTextColor(getResources().getColor(R.color.black_details_ball_textcolor));
        } else {
            mHomeScoreLoseSix.setTextColor(getResources().getColor(R.color.black_details_ball_textcolor));
            mGuestScoreLoseSix.setTextColor(getResources().getColor(R.color.black_details_ball_textcolor));
        }

        mScoreWin.setText(getResources().getText(R.string.basket_six_average_win_score));
        mScoreLose.setText(getResources().getText(R.string.basket_six_average_lost_score));

        if (mGuestScoreWin.equals("--") && mHomeScoreWin.equals("--") && mGuestScoreLose.equals("--") && mHomeScoreLose.equals("--")) {
            //最近表现
            mRecentLinearLayout.setVisibility(View.GONE);
            mRecentNodata.setVisibility(View.VISIBLE);
        } else {
            mRecentLinearLayout.setVisibility(View.VISIBLE);
            mRecentNodata.setVisibility(View.GONE);
        }
    }

    private void setRecent(ImageView mImage, int recent) {
        if (recent == 0) {
//            mImage.setBackground(getContext().getResources().getDrawable(R.mipmap.basket_lose));
            mImage.setBackgroundResource(R.mipmap.basket_lose);
        } else if (recent == 1) {
//            mImage.setBackground(getContext().getResources().getDrawable(R.mipmap.basket_win));
            mImage.setBackgroundResource(R.mipmap.basket_win);
        }
//        else {
//            mImage.setBackground(getContext().getResources().getDrawable(R.mipmap.basket_none));
//        }
    }

    private void setFutureMatch(TextView mTextData, TextView mTextName, ImageView mLogo, BasketAnalyzeFutureMatchBean mFutureMatch, boolean isValue) {

        if (isValue) {
            mTextData.setText(mFutureMatch.getDiffdays() + getResources().getText(R.string.basket_analyze_day));
            mTextName.setText(mFutureMatch.getTeam());
            if (mContext != null) {
                ImageLoader.load(mContext, mFutureMatch.getLogourl(), R.mipmap.basket_default).into(mLogo);
            }
        } else {
            mTextData.setText("--");
            mTextName.setText("--");
//            mLogo.setBackground(getResources().getDrawable(R.mipmap.basket_default));
            mLogo.setBackgroundResource(R.mipmap.basket_default);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    class FutureAdapter extends CommonAdapter<BasketAnalyzeFutureMatchBean> {

        public FutureAdapter(Context context, List<BasketAnalyzeFutureMatchBean> datas, int layoutId) {
            super(context, datas, layoutId);
            this.mContext = context;
        }

        @Override
        public void convert(ViewHolder holder, BasketAnalyzeFutureMatchBean data) {
            if (data != null) {

                ImageView image = (ImageView) holder.getConvertView().findViewById(R.id.basket_future_guest_3);
                holder.setText(R.id.basket_future_guest_1, data.getDiffdays() + getResources().getText(R.string.basket_analyze_day));
                holder.setText(R.id.basket_future_guest_2, data.getTeam());
                ImageLoader.load(mContext, data.getLogourl(), R.mipmap.basket_default).into(image);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;

    }
}
