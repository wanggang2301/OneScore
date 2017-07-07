package com.hhly.mlottery.frame.cpifrag.SnookerIndex.SnookerChildFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.SnookerMatchDetailActivity;
import com.hhly.mlottery.activity.TennisBallDetailsActivity;
import com.hhly.mlottery.activity.TennisIndexDetailsActivity;
import com.hhly.mlottery.adapter.snooker.SnookerIndexAdapter;
import com.hhly.mlottery.bean.snookerbean.SnookerScoreSocketBean;
import com.hhly.mlottery.bean.snookerbean.snookerIndexBean.SnookerIndexBean;
import com.hhly.mlottery.bean.snookerbean.snookerschedulebean.SnookerSocketOddsBean;
import com.hhly.mlottery.bean.tennisball.TennisSocketBean;
import com.hhly.mlottery.bean.tennisball.TennisSocketOddsBean;
import com.hhly.mlottery.frame.BallType;
import com.hhly.mlottery.frame.cpifrag.SnookerIndex.SIndexFragment;
import com.hhly.mlottery.mvp.ViewFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SnookerIndexChildFragment extends ViewFragment<SnookerIndexChildContract.Presenter> implements SnookerIndexChildContract.View {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String ARG_THIRDID = "thirdId";  //比赛id
    private final String ARG_COMPANY_ID = "comId"; //公司Id
    private final String ARG_TYPE = "oddType";

    private String mType; //亚盘大小球类型
    private String mTypeIndex;//类型对应的坐标
    private int mBallType;//斯诺克网球
    private View mView;

    @BindView(R.id.snooker_index_recycler)
    RecyclerView mRecyclerView;

    /**
     * 异常界面
     */
    @BindView(R.id.basket_odds_net_error)
    LinearLayout mExceptionLayout;
    /**
     * 无数据的界面
     */
    @BindView(R.id.nodata)
    TextView mNodataLayout;
    /**
     * 点击刷新
     */
    @BindView(R.id.network_exception_reload_btn)
    TextView mBtnRefresh;
    /**
     * 加载中
     */
    @BindView(R.id.basket_player_progressbar)
    FrameLayout mProgressBarLayout;

    private SnookerIndexAdapter mAdapter;

    private SIndexFragment parentFragment;
    /**
     * 当前日期
     */
    private String mDate = "";


    public SnookerIndexChildFragment() {
        // Required empty public constructor
    }

    public static SnookerIndexChildFragment newInstance(String type, int ballType) {
        SnookerIndexChildFragment fragment = new SnookerIndexChildFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, type);
        args.putInt(ARG_PARAM2, ballType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentFragment = (SIndexFragment) getParentFragment();
    }

    @Override
    public SnookerIndexChildContract.Presenter initPresenter() {
        return new SnookerIndexChildPresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_PARAM1);
            mBallType = getArguments().getInt(ARG_PARAM2);
        }
        //改成dagger

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_snooker_index_euro, container, false);

        ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNodataLayout.setVisibility(View.GONE);
        mExceptionLayout.setVisibility(View.GONE);
        mProgressBarLayout.setVisibility(View.GONE);
        mBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExceptionLayout.setVisibility(View.GONE);
                mProgressBarLayout.setVisibility(View.VISIBLE);
                parentFragment.refreshAllChildFragments();
            }
        });

        mAdapter = new SnookerIndexAdapter(mPresenter.getData(), parentFragment.getCompanyList(), mActivity, mType, mBallType);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        // 每个 Item 内部一条赔率的单击
        mAdapter.setOnOddsClickListener(new SnookerIndexAdapter.SnookerOddsOnClick() {
                                            @Override
                                            public void onOddsClick(String matchId, String comId) {

                                                if (mBallType == BallType.TENNLS) {
                                                    //点击指数页面，传值给详情界面
                                                    Intent intent = new Intent(getContext(), TennisIndexDetailsActivity.class);
                                                    intent.putExtra(ARG_THIRDID, matchId);
                                                    intent.putExtra(ARG_COMPANY_ID, comId);
                                                    intent.putExtra(ARG_TYPE, mType);
                                                    getContext().startActivity(intent);
                                                }


                                            }
                                        }
        );

        mPresenter.refreshByDate("", mType, mBallType);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                if (mBallType == BallType.SNOOKER) {
                    Intent intent = new Intent(getActivity(), SnookerMatchDetailActivity.class);
                    intent.putExtra("matchId", mPresenter.getData().get(i).getMatchInfo().getMatchId());
                    getActivity().startActivity(intent);
                } else if (mBallType == BallType.TENNLS) {
                    Intent intent = new Intent(getActivity(), TennisBallDetailsActivity.class);
                    intent.putExtra("thirdId", mPresenter.getData().get(i).getMatchInfo().getMatchId());
                    getActivity().startActivity(intent);
                }
            }
        });


    }

    /**
     * 根据选中的日期刷新
     *
     * @param date
     */
    public void refreshDate(String date) {
        mDate = date;
        mPresenter.refreshByDate(date, mType, mBallType);
    }

    @Override
    public void onError() {

    }

    @Override
    public void recyclerViewNotify() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
        mNodataLayout.setVisibility(View.GONE);
        mExceptionLayout.setVisibility(View.GONE);
        mProgressBarLayout.setVisibility(View.GONE);
        parentFragment.setRefreshing(false);
    }

    @Override
    public void setParentData(SnookerIndexBean bean) {

        parentFragment.setDateAndCompany(bean);
    }

    @Override
    public void showNoData(String error) {
        if (error.equals(SnookerIndexChildPresenter.NODATA)) {
            mNodataLayout.setVisibility(View.VISIBLE);
            mExceptionLayout.setVisibility(View.GONE);
            mProgressBarLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            parentFragment.setRefreshing(false);
        } else if (error.equals(SnookerIndexChildPresenter.NETERROR)) {
            mNodataLayout.setVisibility(View.GONE);
            mExceptionLayout.setVisibility(View.VISIBLE);
            mProgressBarLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            parentFragment.setRefreshing(false);
        }
    }

    @Override
    public void handleCompany(List<SnookerIndexBean.CompanyEntity> company) {
        // 默认选中头两个公司
        if (parentFragment.getCompanyList().size() == 0) { //只是第一次的时候进行此操作。下拉刷新不再更改公司选中状态
            int size = company.size();
            size = size <= 2 ? size : 2;
            for (int i = 0; i < size; i++) {
                company.get(i).setChecked(true);
            }
            parentFragment.setCompanyList((ArrayList<SnookerIndexBean.CompanyEntity>) company);
        }


    }

    @Override
    public List<SnookerIndexBean.CompanyEntity> getCompanyList() {
        return parentFragment.getCompanyList(); //尴尬的是公司列表在此类处理。传给了parent。又从parent传过来。呵呵。就这样吧
    }

    /**
     * 更新过滤数据源（注意要过滤公司赔率信息）
     */
    public void updateFilterData() {
        //筛选数据
        mPresenter.filterCompany();
    }

    /**
     * 斯诺克比分更新
     *
     * @param mScoreData
     */
    public void updateScore(SnookerScoreSocketBean mScoreData) {
        SnookerScoreSocketBean.SnookerScoreDataBean scoreData = mScoreData.getData();
        synchronized (this) {
            if (mPresenter != null && mPresenter.getData() != null) {
                for (SnookerIndexBean.AllInfoEntity match : mPresenter.getData()) {
                    if (match.getMatchInfo().getMatchId().equals(mScoreData.getThirdId())) {
                        if (match.getMatchInfo() != null) {
                            SnookerIndexBean.AllInfoEntity.MatchInfoEntity matchData = match.getMatchInfo();
                            updateItemData(matchData, scoreData);
                        } else {
                            /**
                             * 未开赛==>开赛 推送情况处理（状态更新）
                             */
                            SnookerIndexBean.AllInfoEntity.MatchInfoEntity newMatchData = new SnookerIndexBean.AllInfoEntity.MatchInfoEntity();
                            updateItemData(newMatchData, scoreData);//更新单条状态
                            match.setMatchInfo(newMatchData);//赋值，给为null的MatchScore设值
                        }
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * 网球比分更新
     *
     * @param mScoreData
     */
    public void updateTennisScore(TennisSocketBean mScoreData) {
        TennisSocketBean.DataObj scoreData = mScoreData.getDataObj();
        synchronized (this) {
            if (mPresenter != null && mPresenter.getData() != null) {
                for (SnookerIndexBean.AllInfoEntity match : mPresenter.getData()) {
                    if (match.getMatchInfo().getMatchId().equals(scoreData.getMatchId())) { //id相符合
                        if (match.getMatchInfo() != null) {
                            SnookerIndexBean.AllInfoEntity.MatchInfoEntity matchData = match.getMatchInfo();
                            updateTennisItemData(matchData, scoreData);
                        } else {
                            /**
                             * 未开赛==>开赛 推送情况处理（状态更新）
                             */
                            SnookerIndexBean.AllInfoEntity.MatchInfoEntity newMatchData = new SnookerIndexBean.AllInfoEntity.MatchInfoEntity();
                            updateTennisItemData(newMatchData, scoreData);//更新单条状态
                            match.setMatchInfo(newMatchData);//赋值，给为null的MatchScore设值
                        }
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * 网球更新单挑item的内容
     *
     * @param matchData
     * @param data
     */
    private void updateTennisItemData(SnookerIndexBean.AllInfoEntity.MatchInfoEntity matchData, TennisSocketBean.DataObj data) {
        String oneWin = "";
        String towWin = "";
        oneWin = data.getMatchScore().getHomeTotalScore() == 0 ? "0" : data.getMatchScore().getHomeTotalScore() + "";
        towWin = data.getMatchScore().getAwayTotalScore() == 0 ? "0" : data.getMatchScore().getAwayTotalScore() + "";
        matchData.setMatchResult(oneWin + ":" + towWin);

        matchData.setMatchState(data.getMatchStatus() + "");

    }

    /**
     * 斯诺克更新单条item的内容
     */
    private void updateItemData(SnookerIndexBean.AllInfoEntity.MatchInfoEntity matchData, SnookerScoreSocketBean.SnookerScoreDataBean data) {
        String oneWin = "";
        String towWin = "";
        String total = "";
        oneWin = data.getPlayerOnewin() == null ? "0" : data.getPlayerOnewin();
        towWin = data.getPlayerTwowin() == null ? "0" : data.getPlayerTwowin();
        total = data.getTotalGames() == null ? "0" : data.getTotalGames();
        matchData.setMatchResult(oneWin + "(" + total + ")" + towWin);

        if (data.getStatus() != null) {
            matchData.setMatchState(data.getStatus());
        }
    }

    /**
     * 斯诺克赔率更新
     *
     * @param mOddsData
     */
    public void updateOdds(SnookerSocketOddsBean mOddsData) {
        synchronized (this) {
            if (mPresenter != null && mPresenter.getData() != null) {
                for (SnookerIndexBean.AllInfoEntity match : mPresenter.getData()) {

                    if (match.getMatchInfo().getMatchId().equals(mOddsData.getThirdId())) {
                        if (match.getComList() != null && match.getComList().size() != 0) {
                            List<SnookerIndexBean.AllInfoEntity.ComListEntity> comlist = match.getComList();
                            updateOddsData(comlist, mOddsData);
                        } else {
                            /**
                             * 无赔率==>有赔率情况处理
                             */
                            List<SnookerIndexBean.AllInfoEntity.ComListEntity> newComlist = new ArrayList<>();
                            updateOddsData(newComlist, mOddsData);
                            match.setComList(newComlist);
                        }
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                    }
                }
            }

        }
    }

    /**
     * 网球赔率更新
     *
     * @param mOddsData
     */
    public void updateTennisOdds(TennisSocketOddsBean mOddsData) {
        synchronized (this) {
            if (mPresenter != null && mPresenter.getData() != null) {
                for (SnookerIndexBean.AllInfoEntity match : mPresenter.getData()) {

                    if (match.getMatchInfo().getMatchId().equals(mOddsData.getDataObj().getMatchId())) {
                        if (match.getComList() != null && match.getComList().size() != 0) {
                            List<SnookerIndexBean.AllInfoEntity.ComListEntity> comlist = match.getComList();
                            updateTennisOddsData(comlist, mOddsData);
                        } else {
                            /**
                             * 无赔率==>有赔率情况处理
                             */
                            List<SnookerIndexBean.AllInfoEntity.ComListEntity> newComlist = new ArrayList<>();
                            updateTennisOddsData(newComlist, mOddsData);
                            match.setComList(newComlist);
                        }
                        if (mAdapter != null) {
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                    }
                }
            }

        }
    }

    /**
     * 斯诺克赔率更新（单条数据更新）
     */
    private void updateOddsData(List<SnookerIndexBean.AllInfoEntity.ComListEntity> currentOddsDataList, SnookerSocketOddsBean socketOddsData) {

        if (socketOddsData.getData() != null) {

            SnookerSocketOddsBean.SnookerDataBean sockerData = socketOddsData.getData();

            boolean isNewCompany = true;//true :没有相同过 收到新公司赔率
            for (SnookerIndexBean.AllInfoEntity.ComListEntity currentOddsData : currentOddsDataList) {
                if (currentOddsData.getComName().equals(sockerData.getCompany())) {
                    isNewCompany = false;
                    break;
                }
            }
            if (isNewCompany) {//收到 原来没有的公司赔率
                SnookerIndexBean.AllInfoEntity.ComListEntity newComlist = new SnookerIndexBean.AllInfoEntity.ComListEntity();
                newComlist.setComName(sockerData.getCompany());
                SnookerIndexBean.AllInfoEntity.ComListEntity.LevelEntity currlevel = new SnookerIndexBean.AllInfoEntity.ComListEntity.LevelEntity();
                currlevel.setLeft(sockerData.getLeftOdds());
                currlevel.setRight(sockerData.getRightOdds());
                currlevel.setMiddle(sockerData.getHandicapValue());
                newComlist.setCurrLevel(currlevel);
                currentOddsDataList.add(newComlist);//添加新赔率
            } else {
                for (SnookerIndexBean.AllInfoEntity.ComListEntity currentOddsData : currentOddsDataList) {
                    if (sockerData.getCompany().equals(currentOddsData.getComName())) {
                        currentOddsData.getCurrLevel().setLeft(sockerData.getLeftOdds());
                        currentOddsData.getCurrLevel().setRight(sockerData.getRightOdds());
                        currentOddsData.getCurrLevel().setMiddle(sockerData.getHandicapValue());
                    }
                }
            }
        }
    }

    /**
     * 网球赔率更新（单条数据更新）
     */
    private void updateTennisOddsData(List<SnookerIndexBean.AllInfoEntity.ComListEntity> currentOddsDataList, TennisSocketOddsBean socketOddsData) {

        if (socketOddsData.getDataObj().getMatchOdd() != null) {

            TennisSocketOddsBean.DataObjBean sockerData = socketOddsData.getDataObj();

            boolean isNewCompany = false;//true :没有相同过 收到新公司赔率
            for (SnookerIndexBean.AllInfoEntity.ComListEntity currentOddsData : currentOddsDataList) {
                if (currentOddsData.getComName().equals(socketOddsData.getDataObj().getComName())) {
                    isNewCompany = false;
                    break;
                }
            }
            if (isNewCompany) {//收到 原来没有的公司赔率
                SnookerIndexBean.AllInfoEntity.ComListEntity newComlist = new SnookerIndexBean.AllInfoEntity.ComListEntity();
                newComlist.setComName(sockerData.getCompany());
                SnookerIndexBean.AllInfoEntity.ComListEntity.LevelEntity currlevel = new SnookerIndexBean.AllInfoEntity.ComListEntity.LevelEntity();
                SnookerIndexBean.AllInfoEntity.ComListEntity.LevelEntity prelevel = new SnookerIndexBean.AllInfoEntity.ComListEntity.LevelEntity();
                prelevel.setLeft(sockerData.getMatchOdd().getL()); //新公司的第一条是初赔。再受到的就走正常逻辑
                prelevel.setRight(sockerData.getMatchOdd().getR());
                prelevel.setMiddle(sockerData.getMatchOdd().getM());
                currlevel.setLeft(""); //新公司的第一条是初赔。及时赔率为空
                currlevel.setRight("");
                currlevel.setMiddle("");
                newComlist.setCurrLevel(currlevel);
                newComlist.setPreLevel(prelevel);
                currentOddsDataList.add(newComlist);//添加新赔率
            } else {
                for (SnookerIndexBean.AllInfoEntity.ComListEntity currentOddsData : currentOddsDataList) {
                    if (sockerData.getCompany().equals(currentOddsData.getComName())) {
                        currentOddsData.getCurrLevel().setLeft(sockerData.getMatchOdd().getL());
                        currentOddsData.getCurrLevel().setRight(sockerData.getMatchOdd().getR());
                        currentOddsData.getCurrLevel().setMiddle(sockerData.getMatchOdd().getM());
                    }
                }
            }
        }
    }
}
