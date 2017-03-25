package com.hhly.mlottery.frame.cpifrag.basketballtask;


import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.activity.BasketIndexDetailsActivity;
import com.hhly.mlottery.adapter.basketball.BasketIndexAdapter;
import com.hhly.mlottery.bean.basket.index.BasketIndexBean;
import com.hhly.mlottery.bean.enums.BasketOddsTypeEnum;
import com.hhly.mlottery.mvp.ViewFragment;
import com.hhly.mlottery.util.CollectionUtils;
import com.hhly.mlottery.util.L;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: wangg
 * @des: 篮球指数赔率Fragment
 * @date：2017/03/20 9:17
 */
public class BasketBallOddFragment extends ViewFragment<BasketBallContract.OddPresenter> implements BasketBallContract.OddView {

    private final String TAG = this.getClass().getSimpleName();

    private static final String ODD_TYPE = "ODD_TYPE";

    //默认公司筛选选中头两家公司
    private static final int DEFAULT_SELECTED_COMPANY = 2;

    @BindView(R.id.cpi_odds_recyclerView)
    RecyclerView cpiOddsRecyclerView;
    @BindView(R.id.cpi_txt_reLoading)
    TextView cpiTxtReLoading;
    @BindView(R.id.cpi_right_fl_plate_networkError)
    FrameLayout cpiRightFlPlateNetworkError;
    @BindView(R.id.cpi_right_fl_plate_noData)
    FrameLayout cpiRightFlPlateNoData;
    private String type;

    private BasketIndexAdapter mBasketIndexAdapter;

    private View mView;

    private Activity mActivity;

    private BasketBallCpiFrament parentFragment;

    private List<BasketIndexBean.DataBean.FileterTagsBean> fileterMatchTypeList;

    private List<BasketIndexBean.DataBean.AllInfoBean> sourceDataList; //源数据

    private List<BasketIndexBean.DataBean.AllInfoBean> destinationDataList; //目标数据


    public BasketBallOddFragment() {
    }

    public static BasketBallOddFragment newInstance(String type) {
        BasketBallOddFragment basketBallOddFragment = new BasketBallOddFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ODD_TYPE, type);
        basketBallOddFragment.setArguments(bundle);
        return basketBallOddFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(ODD_TYPE, BasketOddsTypeEnum.ASIALET);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_basket_ball_odd, container, false);
        ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fileterMatchTypeList = new ArrayList<>();

        sourceDataList = new ArrayList<>();

        destinationDataList = new ArrayList<>();

        mPresenter = new BasketBallOddPresenter(this);


        mPresenter.showLoad();
        mPresenter.showRequestData("", type);


        cpiOddsRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mBasketIndexAdapter = new BasketIndexAdapter(mActivity, destinationDataList, type);

        cpiOddsRecyclerView.setAdapter(mBasketIndexAdapter);

        //篮球内页跳转
        mBasketIndexAdapter.setOnItemClickListener(new BasketIndexAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BasketIndexBean.DataBean.AllInfoBean allInfoBean) {
                Intent intent = new Intent(mActivity, BasketDetailsActivityTest.class);
                intent.putExtra("thirdId", allInfoBean.getThirdId());
                intent.putExtra("MatchStatus", allInfoBean.getMatchStatus());
                intent.putExtra("leagueId", allInfoBean.getLeagueId());
                intent.putExtra("matchType", allInfoBean.getMatchType());
                intent.putExtra("isAddMultiViewHide", true);
                mActivity.startActivity(intent);

            }
        });

        //篮球指数内页跳转
        mBasketIndexAdapter.setOnOddIetmClickListener(new BasketIndexAdapter.onOddIetmClickListener() {
            @Override
            public void onOddItemCLick(String thirdId, String comId) {
                Intent intent = new Intent(mActivity, BasketIndexDetailsActivity.class);
                intent.putExtra("thirdId", thirdId);
                intent.putExtra("comId", comId);
                intent.putExtra("oddType", type);
                mActivity.startActivity(intent);
            }
        });


        cpiTxtReLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.showLoad();
                parentFragment.refreshAllChildFragments();
            }
        });


    }

    @Override
    public void showLoadView() {
        parentFragment.setRefreshVisible();
        cpiOddsRecyclerView.setVisibility(View.GONE);
        cpiRightFlPlateNetworkError.setVisibility(View.GONE);
        cpiRightFlPlateNoData.setVisibility(View.GONE);

        L.d(TAG, "加载中_____________");

    }


    @Override
    public void showResponseDataView() {
        L.d(TAG, "请求数据成功_____________");


        BasketIndexBean b = mPresenter.getRequestData();

        //获取筛选的list
        fileterMatchTypeList.clear();
        fileterMatchTypeList.addAll(b.getData().getFileterTags());

        sourceDataList.clear();
        sourceDataList.addAll(b.getData().getAllInfo());

        parentFragment.showRightButton();

        //处理公司数据  请求到数据成功后已经将需要默认选择的公司数据处理好了
        handleCompanyData(b.getData().getCompany());

        refreshDateView(b.getData().getCurrDate());
        updateFilterData();

        cpiOddsRecyclerView.setVisibility(View.VISIBLE);
        cpiRightFlPlateNetworkError.setVisibility(View.GONE);
        cpiRightFlPlateNoData.setVisibility(View.GONE);
        parentFragment.setRefreshHide();

    }

    /**
     * 日期切换刷新数据
     *
     * @param date date, 形如 2016-06-21 可以为空
     */
    public void refreshData(String date) {
        L.d(TAG, "下拉刷新___________" + date + type);
        mPresenter.showRequestData(date, type);
    }


    /**
     * 刷新日期View
     */
    private void refreshDateView(String date) {
        if (parentFragment.getCurrentFragment() == BasketBallOddFragment.this) {
            // 获取当前日期并返回给 CPINewFragment
            parentFragment.setCurrentDate(date);
        }
    }


    @Override
    public void noData() {

        L.d(TAG, "没有数据_____________");

        parentFragment.setRefreshHide();
        cpiOddsRecyclerView.setVisibility(View.GONE);
        cpiRightFlPlateNetworkError.setVisibility(View.GONE);
        cpiRightFlPlateNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError() {
        Toast.makeText(mActivity, "出错", Toast.LENGTH_SHORT).show();
        L.d(TAG, "请求出错_____________");

        parentFragment.setRefreshHide();
        cpiOddsRecyclerView.setVisibility(View.GONE);
        cpiRightFlPlateNetworkError.setVisibility(View.VISIBLE);
        cpiRightFlPlateNoData.setVisibility(View.GONE);
    }

    private void handleCompanyData(List<BasketIndexBean.DataBean.CompanyBean> currList) {


        int size = currList.size();
        size = size < DEFAULT_SELECTED_COMPANY ? size : DEFAULT_SELECTED_COMPANY;
        for (int i = 0; i < size; i++) {
            currList.get(i).setChecked(true);
        }

        //记住上一次的公司结果
        if (CollectionUtils.notEmpty(parentFragment.getCompanyMap().get(type))) {
            List<BasketIndexBean.DataBean.CompanyBean> prelist = parentFragment.getCompanyMap().get(type);

            for (BasketIndexBean.DataBean.CompanyBean companyBean : currList) {
                for (BasketIndexBean.DataBean.CompanyBean d : prelist) {
                    if (d.isChecked() && companyBean.getComId().equals(d.getComId())) {
                        companyBean.setChecked(true);
                        break;
                    } else {
                        companyBean.setChecked(false);
                    }
                }
            }

        }
        parentFragment.getCompanyMap().put(type, currList);

    }

    public List<BasketIndexBean.DataBean.FileterTagsBean> getFilterTagList() {
        return fileterMatchTypeList;
    }


    /**
     * 更新过滤数据源（注意要过滤公司赔率信息）
     */
    public void updateFilterData() {
        destinationDataList.clear();

        //赛事的筛选,获取父赛事的筛选结果
        LinkedList<String> filterLeagueList = parentFragment.getFilterLeagueList();


        //第一次请求
        if (filterLeagueList != null) {
            for (BasketIndexBean.DataBean.AllInfoBean allInfo : sourceDataList) {
                if (filterLeagueList.indexOf(allInfo.getLeagueId()) >= 0) {
                    filterAllInfo(allInfo);
                }
            }
        } else {
            for (BasketIndexBean.DataBean.AllInfoBean allInfo : sourceDataList) {  //每一场赛事
                if (allInfo.isHot()) {
                    filterAllInfo(allInfo);   //默认显示热门的比赛
                }
            }
        }

        mBasketIndexAdapter.notifyDataSetChanged();
        cpiOddsRecyclerView.scrollToPosition(0);
    }

    /**
     * 过滤单条信息
     *
     * @param allInfo allInfoBean
     */
    private void filterAllInfo(BasketIndexBean.DataBean.AllInfoBean allInfo) {
        //获取各个赔率下的公司list
        List<BasketIndexBean.DataBean.CompanyBean> companyList = parentFragment.getCompanyMap().get(type);

        List<BasketIndexBean.DataBean.AllInfoBean.MatchOddsBean> matchOddsBeanList = new ArrayList<>();   //每一场比赛的赔率公司

        for (BasketIndexBean.DataBean.AllInfoBean.MatchOddsBean matchOddsBean : allInfo.getMatchOdds()) {
            for (BasketIndexBean.DataBean.CompanyBean companyBean : companyList) {
                if (companyBean.isChecked() && companyBean.getComId().equals(matchOddsBean.getComId())) {
                    matchOddsBeanList.add(matchOddsBean);
                }
            }
        }

        //防止对象复用
        BasketIndexBean.DataBean.AllInfoBean allInfoBean = new BasketIndexBean.DataBean.AllInfoBean();
        allInfoBean.setMatchOdds(matchOddsBeanList);
        allInfoBean.setThirdId(allInfo.getThirdId());
        allInfoBean.setHomeTeam(allInfo.getHomeTeam());
        allInfoBean.setGuestTeam(allInfo.getGuestTeam());
        allInfoBean.setLeagueId(allInfo.getLeagueId());
        allInfoBean.setLeagueName(allInfo.getLeagueName());
        allInfoBean.setLeagueColor(allInfo.getLeagueColor());
        allInfoBean.setDate(allInfo.getDate());
        allInfoBean.setTime(allInfo.getTime());
        allInfoBean.setMatchStatus(allInfo.getMatchStatus());
        allInfoBean.setHot(allInfo.isHot());
        allInfoBean.setMatchResult(allInfo.getMatchResult());

        destinationDataList.add(allInfoBean);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
        parentFragment = (BasketBallCpiFrament) getParentFragment();
    }
}
