package com.hhly.mlottery.frame.footframe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.intelligence.BigDataForecast;
import com.hhly.mlottery.bean.intelligence.BigDataForecastData;
import com.hhly.mlottery.bean.intelligence.BigDataForecastFactor;
import com.hhly.mlottery.bean.intelligence.BigDataResult;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.StringFormatUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.RoundProgressBar;

import java.util.HashMap;
import java.util.Map;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/7/18.
 */
public class IntelligenceFragment extends Fragment {

    private static final String KEY_THIRD_ID = "thirdId";

    private static final int TYPE_HOST = 0;
    private static final int TYPE_SIZE = 1;
    private static final int TYPE_ASIA = 2;

    ImageView mDottedLine1;
    ImageView mDottedLine2;

    View mDiyComputeMethodView;

    TextView mHostAlert; /* 采样不足警告 */
    TextView mSizeAlert; /* 采样不足警告 */
    TextView mAsiaAlert; /* 采样不足警告 */

    RoundProgressBar mHostProgress;
    RoundProgressBar mSizeProgress;
    RoundProgressBar mAsiaProgress;

    TextView mHistoryHostWin;
    TextView mHistorySizeWin;
    TextView mHistoryAsiaWin;
    ContentLoadingProgressBar mHistoryHostWinProgress;
    ContentLoadingProgressBar mHistorySizeWinProgress;
    ContentLoadingProgressBar mHistoryAsiaWinProgress;

    TextView mHostRecentHostWin;
    TextView mHostRecentSizeWin;
    TextView mHostRecentAsiaWin;
    ContentLoadingProgressBar mHostRecentHostWinProgress;
    ContentLoadingProgressBar mHostRecentSizeWinProgress;
    ContentLoadingProgressBar mHostRecentAsiaWinProgress;

    TextView mGuestRecentHostWin;
    TextView mGuestRecentSizeWin;
    TextView mGuestRecentAsiaWin;
    ContentLoadingProgressBar mGuestRecentHostWinProgress;
    ContentLoadingProgressBar mGuestRecentSizeWinProgress;
    ContentLoadingProgressBar mGuestRecentAsiaWinProgress;

    private IntelligenceComputeMethodDialogFragment mDialog;

    private String mThirdId;
    private BigDataForecast mBigDataForecast;
    private BigDataForecastFactor mFactor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mThirdId = args.getString(KEY_THIRD_ID);
        }
        if (mFactor == null) {
            mFactor = new BigDataForecastFactor();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intelligence, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

        Map<String, String> params = new HashMap<>();
        params.put(KEY_THIRD_ID, mThirdId);

        VolleyContentFast.requestJsonByGet(BaseURLs.URL_INTELLIGENCE_BIG_DATA, params,
                new VolleyContentFast.ResponseSuccessListener<BigDataResult>() {
                    @Override
                    public void onResponse(BigDataResult jsonObject) {
                        mBigDataForecast = jsonObject.getBigDataForecast();
                        if (jsonObject.getResult() != 200 || mBigDataForecast == null) {
                            refreshFactorUI(false);
                            setEmptyAlert();
                            mDiyComputeMethodView.setVisibility(View.GONE);
                            return;
                        }
                        mDiyComputeMethodView.setVisibility(View.VISIBLE);
                        BigDataForecastData battleHistory = mBigDataForecast.getBattleHistory();
                        BigDataForecastData homeRecent = mBigDataForecast.getHomeRecent();
                        BigDataForecastData guestRecent = mBigDataForecast.getGuestRecent();

                        setWinRate(battleHistory,
                                mHistoryHostWin, mHistorySizeWin, mHistoryAsiaWin,
                                mHistoryHostWinProgress, mHistorySizeWinProgress, mHistoryAsiaWinProgress);
                        setWinRate(homeRecent,
                                mHostRecentHostWin, mHostRecentSizeWin, mHostRecentAsiaWin,
                                mHostRecentHostWinProgress, mHostRecentSizeWinProgress, mHostRecentAsiaWinProgress);
                        setWinRate(guestRecent,
                                mGuestRecentHostWin, mGuestRecentSizeWin, mGuestRecentAsiaWin,
                                mGuestRecentHostWinProgress, mGuestRecentSizeWinProgress, mGuestRecentAsiaWinProgress);
                        refreshFactorUI(false);
                    }
                },
                new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        // TODO: 异常处理
                    }
                }, BigDataResult.class);
    }

    /**
     * 设置空提醒
     */
    private void setEmptyAlert() {
        mHistoryHostWin.setText("");
        mHistorySizeWin.setText("");
        mHistoryAsiaWin.setText("");

        mHostRecentHostWin.setText("");
        mHostRecentSizeWin.setText("");
        mHostRecentAsiaWin.setText("");

        mGuestRecentHostWin.setText("");
        mGuestRecentSizeWin.setText("");
        mGuestRecentAsiaWin.setText("");
    }

    /**
     * 初始化 View
     *
     * @param view
     */
    private void initViews(View view) {
        mHostAlert = (TextView) view.findViewById(R.id.host_alert);
        mSizeAlert = (TextView) view.findViewById(R.id.size_alert);
        mAsiaAlert = (TextView) view.findViewById(R.id.asia_alert);

        mDottedLine1 = (ImageView) view.findViewById(R.id.dotted_line1);
        mDottedLine2 = (ImageView) view.findViewById(R.id.dotted_line2);

        // 关闭硬件加速才显示虚线
        mDottedLine1.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mDottedLine2.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mHostProgress = (RoundProgressBar) view.findViewById(R.id.home_progress);
        mSizeProgress = (RoundProgressBar) view.findViewById(R.id.size_progress);
        mAsiaProgress = (RoundProgressBar) view.findViewById(R.id.asia_progress);

        mHistoryHostWin = (TextView) view.findViewById(R.id.history_host_win_rate);
        mHistorySizeWin = (TextView) view.findViewById(R.id.history_size_win_rate);
        mHistoryAsiaWin = (TextView) view.findViewById(R.id.history_asia_win_rate);

        mHostRecentHostWin = (TextView) view.findViewById(R.id.home_recent_home_win_rate);
        mHostRecentSizeWin = (TextView) view.findViewById(R.id.home_recent_size_win_rate);
        mHostRecentAsiaWin = (TextView) view.findViewById(R.id.home_recent_asia_win_rate);

        mGuestRecentHostWin = (TextView) view.findViewById(R.id.guest_recent_home_win_rate);
        mGuestRecentSizeWin = (TextView) view.findViewById(R.id.guest_recent_size_win_rate);
        mGuestRecentAsiaWin = (TextView) view.findViewById(R.id.guest_recent_asia_win_rate);

        mHistoryHostWinProgress = (ContentLoadingProgressBar)
                view.findViewById(R.id.history_host_win_progress);
        mHistorySizeWinProgress = (ContentLoadingProgressBar)
                view.findViewById(R.id.history_size_win_progress);
        mHistoryAsiaWinProgress = (ContentLoadingProgressBar)
                view.findViewById(R.id.history_asia_win_progress);

        mHostRecentHostWinProgress = (ContentLoadingProgressBar)
                view.findViewById(R.id.home_recent_home_win_rate_progress);
        mHostRecentSizeWinProgress = (ContentLoadingProgressBar)
                view.findViewById(R.id.home_recent_size_win_rate_progress);
        mHostRecentAsiaWinProgress = (ContentLoadingProgressBar)
                view.findViewById(R.id.home_recent_asia_win_rate_progress);

        mGuestRecentHostWinProgress = (ContentLoadingProgressBar)
                view.findViewById(R.id.guest_recent_home_win_rate_progress);
        mGuestRecentSizeWinProgress = (ContentLoadingProgressBar)
                view.findViewById(R.id.guest_recent_size_win_rate_progress);
        mGuestRecentAsiaWinProgress = (ContentLoadingProgressBar)
                view.findViewById(R.id.guest_recent_asia_win_rate_progress);

        mDiyComputeMethodView = view.findViewById(R.id.diy_text);
        mDiyComputeMethodView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog == null) {
                    mDialog = IntelligenceComputeMethodDialogFragment
                            .newInstance(mBigDataForecast, mFactor);
                }
                mDialog.show(getChildFragmentManager(), "computeMethod");
            }
        });

    }

    /**
     * 设置胜率文本
     *
     * @param oddsInfo     胜率内容实体
     * @param hostTextView 主胜
     * @param sizeTextView 大球
     * @param asiaTextView 赢盘
     */
    private void setWinRate(BigDataForecastData oddsInfo,
                            TextView hostTextView, TextView sizeTextView, TextView asiaTextView,
                            ProgressBar hostProgress, ProgressBar sizeProgress, ProgressBar asiaProgress) {
        if (oddsInfo != null) {

            Float homeWinPercent = oddsInfo.getHomeWinPercent();
            Float sizeWinPercent = oddsInfo.getSizeWinPercent();
            Float asiaWinPercent = oddsInfo.getAsiaWinPercent();

            setWinRate(homeWinPercent, hostTextView, hostProgress);
            setWinRate(sizeWinPercent, sizeTextView, sizeProgress);
            setWinRate(asiaWinPercent, asiaTextView, asiaProgress);
        }
    }

    /**
     * 设置单个胜率
     *
     * @param winRate
     * @param textView
     * @param progressBar
     */
    private void setWinRate(Float winRate, TextView textView, ProgressBar progressBar) {
        if (winRate != null) {
            textView.setText(StringFormatUtils.toPercentString(winRate));
            progressBar.setProgress((int) (winRate * 100));
        } else {
            textView.setText("");
        }
    }

    /**
     * 刷新DIY算法UI
     */
    public void refreshFactorUI(boolean ignoreNull) {
        if (mBigDataForecast == null) {
            hideProgress(mHostProgress, mHostAlert);
            hideProgress(mSizeProgress, mSizeAlert);
            hideProgress(mAsiaProgress, mAsiaAlert);
            return;
        }
        BigDataForecastData battleHistory = mBigDataForecast.getBattleHistory();
        if (!ignoreNull) {
            if (battleHistory == null) {
                hideProgress(mHostProgress, mHostAlert);
                hideProgress(mSizeProgress, mSizeAlert);
                hideProgress(mAsiaProgress, mAsiaAlert);
                return;
            }
            if (battleHistory.getHomeWinPercent() != null) {
                showProgress(mHostProgress, mHostAlert, TYPE_HOST);
            } else {
                hideProgress(mHostProgress, mHostAlert);
            }
            if (battleHistory.getSizeWinPercent() != null) {
                showProgress(mSizeProgress, mSizeAlert, TYPE_SIZE);
            } else {
                hideProgress(mSizeProgress, mSizeAlert);
            }
            if (battleHistory.getAsiaWinPercent() != null) {
                showProgress(mAsiaProgress, mAsiaAlert, TYPE_ASIA);
            } else {
                hideProgress(mAsiaProgress, mAsiaAlert);
            }
        } else {
            showProgress(mHostProgress, mHostAlert, TYPE_HOST);
            showProgress(mSizeProgress, mSizeAlert, TYPE_SIZE);
            showProgress(mAsiaProgress, mAsiaAlert, TYPE_ASIA);
        }
    }

    /**
     * 设置百分比
     *
     * @param progressBar
     * @param type        数据类型 0-Host，1-Size，2-Asia
     */
    private void showProgress(RoundProgressBar progressBar, View alertView, int type) {
        double winRate;
        if (type == TYPE_HOST) {
            winRate = mFactor.computeHostWinRate(mBigDataForecast);
        } else if (type == TYPE_SIZE) {
            winRate = mFactor.computeSizeWinRate(mBigDataForecast);
        } else {
            winRate = mFactor.computeAsiaWinRate(mBigDataForecast);
        }
        progressBar.setProgress((int) (winRate * 100));
        progressBar.setTextIsDisplayable(true);
        alertView.setVisibility(View.GONE);
    }

    private void hideProgress(RoundProgressBar progressBar, View alertView) {
        progressBar.setTextIsDisplayable(false);
        alertView.setVisibility(View.VISIBLE);
    }

    public static IntelligenceFragment newInstance(String thirdId) {

        Bundle args = new Bundle();
        args.putString(KEY_THIRD_ID, thirdId);
        IntelligenceFragment fragment = new IntelligenceFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
