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
 * 情报 Fragment
 * <p>
 * Created by loshine on 2016/7/18.
 */
public class IntelligenceFragment extends Fragment {

    private static final String KEY_THIRD_ID = "thirdId";

    ImageView mDottedLine1;
    ImageView mDottedLine2;

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
                        if (jsonObject.getResult() != 200) return;
                        mBigDataForecast = jsonObject.getBigDataForecast();
                        if (mBigDataForecast == null) return;
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

                        refreshFactorUI();
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
     * 初始化 View
     *
     * @param view
     */
    private void initViews(View view) {
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

        view.findViewById(R.id.diy_text)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntelligenceComputeMethodDialogFragment.newInstance(mBigDataForecast, mFactor)
                                .show(getChildFragmentManager(), "computeMethod");
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

            double homeWinPercent = oddsInfo.getHomeWinPercent();
            double sizeWinPercent = oddsInfo.getSizeWinPercent();
            double asiaWinPercent = oddsInfo.getAsiaWinPercent();

            hostTextView.setText(StringFormatUtils.toPercentString(homeWinPercent));
            sizeTextView.setText(StringFormatUtils.toPercentString(sizeWinPercent));
            asiaTextView.setText(StringFormatUtils.toPercentString(asiaWinPercent));

            hostProgress.setProgress((int) (homeWinPercent * 100));
            sizeProgress.setProgress((int) (sizeWinPercent * 100));
            asiaProgress.setProgress((int) (asiaWinPercent * 100));
        }
    }

    /**
     * 刷新DIY算法UI
     */
    public void refreshFactorUI() {
        double hostWinRate = mFactor.computeHostWinRate(mBigDataForecast);
        mHostProgress.setProgress((int) (hostWinRate * 100));
        double sizeWinRate = mFactor.computeSizeWinRate(mBigDataForecast);
        mSizeProgress.setProgress((int) (sizeWinRate * 100));
        double asiaWinRate = mFactor.computeAsiaWinRate(mBigDataForecast);
        mAsiaProgress.setProgress((int) (asiaWinRate * 100));
    }

    public static IntelligenceFragment newInstance(String thirdId) {

        Bundle args = new Bundle();
        args.putString(KEY_THIRD_ID, thirdId);
        IntelligenceFragment fragment = new IntelligenceFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
