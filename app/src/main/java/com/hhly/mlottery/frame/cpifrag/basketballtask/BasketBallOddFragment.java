package com.hhly.mlottery.frame.cpifrag.basketballtask;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.activity.BasketIndexDetailsActivity;
import com.hhly.mlottery.adapter.basketball.BasketIndexAdapter;
import com.hhly.mlottery.bean.basket.index.BasketIndexBean;
import com.hhly.mlottery.bean.enums.BasketOddsTypeEnum;
import com.hhly.mlottery.bean.websocket.WebBasketMatch;
import com.hhly.mlottery.bean.websocket.WebBasketOdds5;
import com.hhly.mlottery.config.FootBallMatchFilterTypeEnum;
import com.hhly.mlottery.mvp.ViewFragment;
import com.hhly.mlottery.util.CollectionUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.StringUtils;

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

    private static final String EURO_AVERAGE = "euro";  //欧赔平均

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
    public BasketBallContract.OddPresenter initPresenter() {
        return new BasketBallOddPresenter(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fileterMatchTypeList = new ArrayList<>();

        sourceDataList = new ArrayList<>();

        destinationDataList = new ArrayList<>();

        mPresenter.showLoad();
        mPresenter.showRequestData("", type);


        cpiOddsRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        ((DefaultItemAnimator) cpiOddsRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
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
        mHandler.sendEmptyMessage(0);
        L.d(TAG, "加载中_____________");

    }

    @Override
    public void showResponseDataView() {
        L.d(TAG, "请求数据成功_____________" + type);


        BasketIndexBean b = mPresenter.getRequestData();


        saveCurrentDate(b.getData().getFilerDate());

        //获取筛选的list
        fileterMatchTypeList.clear();
        fileterMatchTypeList.addAll(b.getData().getFileterTags());

        sourceDataList.clear();
        sourceDataList.addAll(b.getData().getAllInfo());


        //处理公司数据  请求到数据成功后已经将需要默认选择的公司数据处理好了
        handleCompanyData(b.getData().getCompany());

        refreshDateView(b.getData().getCurrDate());
        updateFilterData();

        parentFragment.showRightButton();

        mHandler.sendEmptyMessage(1);

    }

    private void saveCurrentDate(String date) {
        if (!PreferenceUtil.getString(FootBallMatchFilterTypeEnum.BASKET_INDEX_DATE, "").equals(date)) {
            PreferenceUtil.removeKey(FootBallMatchFilterTypeEnum.BASKET_INDEX);
            PreferenceUtil.commitString(FootBallMatchFilterTypeEnum.BASKET_INDEX_DATE, date);
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    parentFragment.setRefreshVisible();
                    cpiOddsRecyclerView.setVisibility(View.GONE);
                    cpiRightFlPlateNetworkError.setVisibility(View.GONE);
                    cpiRightFlPlateNoData.setVisibility(View.GONE);
                    break;

                case 1:
                    cpiOddsRecyclerView.setVisibility(View.VISIBLE);
                    cpiRightFlPlateNetworkError.setVisibility(View.GONE);
                    cpiRightFlPlateNoData.setVisibility(View.GONE);
                    parentFragment.setRefreshHide();
                    break;

                case 2:
                    parentFragment.setRefreshHide();
                    cpiOddsRecyclerView.setVisibility(View.GONE);
                    cpiRightFlPlateNetworkError.setVisibility(View.GONE);
                    cpiRightFlPlateNoData.setVisibility(View.VISIBLE);
                    break;

                case -1:
                    parentFragment.setRefreshHide();
                    cpiOddsRecyclerView.setVisibility(View.GONE);
                    cpiRightFlPlateNetworkError.setVisibility(View.VISIBLE);
                    cpiRightFlPlateNoData.setVisibility(View.GONE);
                    break;


                default:
                    break;
            }

        }
    };

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
    public void noData(String date) {

        L.d(TAG, "没有数据_____________");
        fileterMatchTypeList.clear();
        if (!StringUtils.isEmpty(date)) {
            refreshDateView(date);
            parentFragment.showRightButton();
        }

        mHandler.sendEmptyMessage(2);

    }

    @Override
    public void onError() {
        L.d(TAG, "请求出错_____________");
        mHandler.sendEmptyMessage(-1);

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

        L.d("xxxccc", "欧赔2==" + sourceDataList.size());

        //第一次请求
        if (filterLeagueList != null) {

            L.d("saveId",   "第二次筛选");

            for (BasketIndexBean.DataBean.AllInfoBean allInfo : sourceDataList) {

                if (filterLeagueList.indexOf(allInfo.getLeagueId()) >= 0) {
                    filterAllInfo(allInfo);
                }
            }
        } else {

            if (PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.BASKET_INDEX).size() > 0) {
                List<String> list = PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.BASKET_INDEX);
                for (BasketIndexBean.DataBean.AllInfoBean allInfo : sourceDataList) {
                    for (String id : list) {
                        if (allInfo.getLeagueId().equals(id)) {
                            filterAllInfo(allInfo);
                        }
                    }
                }

                L.d("saveId",   "取本地存储");

            } else {
                L.d("saveId",   "没有本地存储");

                for (BasketIndexBean.DataBean.AllInfoBean allInfo : sourceDataList) {  //每一场赛事
                    if (allInfo.isHot()) {
                        filterAllInfo(allInfo);   //默认显示热门的比赛
                    }
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

            //欧赔特殊处理平均的赔率
            if (EURO_AVERAGE.equals(matchOddsBean.getComId())) {
                matchOddsBeanList.add(matchOddsBean);
            }

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
        allInfoBean.setRemainTime(allInfo.getRemainTime());
        allInfoBean.setHot(allInfo.isHot());
        allInfoBean.setMatchResult(allInfo.getMatchResult());
        allInfoBean.setSection(allInfo.getSection());

        destinationDataList.add(allInfoBean);
    }


    public void updatePushScoreAndStatus(WebBasketMatch mWebBasketMatch) {
        updateSourceScoreAndStatus(mWebBasketMatch, true);  //源数据
        updateSourceScoreAndStatus(mWebBasketMatch, false); //目标数据
    }


    private void updateSourceScoreAndStatus(WebBasketMatch mWebBasketMatch, boolean isSourceData) {
        List<BasketIndexBean.DataBean.AllInfoBean> allInfoBeanList = getDataList(isSourceData);
        if (allInfoBeanList == null) return;
        for (BasketIndexBean.DataBean.AllInfoBean item : allInfoBeanList) {
            if (item.getThirdId().equals(mWebBasketMatch.getThirdId())) {
                item.setMatchStatus(Integer.parseInt(mWebBasketMatch.getData().get("matchStatus")));

                //备注：篮球比分   客队分数:主队分数
                item.setMatchResult(mWebBasketMatch.getData().get("guestScore") + ":" + mWebBasketMatch.getData().get("homeScore"));
                item.setRemainTime(mWebBasketMatch.getData().get("remainTime"));

                if (!isSourceData) {
                    mBasketIndexAdapter.notifyItemChanged(allInfoBeanList.indexOf(item));
                }
            }
        }
    }


    public void updatePushOdd(String thirdId, String oddType, WebBasketOdds5 odd) {
        if (odd != null) {
            updateOdds(thirdId, oddType, odd, true);  //更新源数据
            updateOdds(thirdId, oddType, odd, false); //更新目标数据
        }
    }

    private void updateOdds(String thirdId, String oddType, WebBasketOdds5 odd, boolean isSourceData) {
        List<BasketIndexBean.DataBean.AllInfoBean> allInfoBeanList = getDataList(isSourceData);
        if (allInfoBeanList == null) return;
        switch (oddType) {
            case BasketOddsTypeEnum.ASIALET:
                setMatchAsiaLetOdds(thirdId, allInfoBeanList, odd, isSourceData);
                break;
            case BasketOddsTypeEnum.ASIASIZE:
                setMatchAsiaAsizeOdds(thirdId, allInfoBeanList, odd, isSourceData);
                break;
            case BasketOddsTypeEnum.EURO:
                //欧赔暂时逻辑不清楚

                break;
        }
    }

    //亚盘
    private void setMatchAsiaLetOdds(String thirdId, List<BasketIndexBean.DataBean.AllInfoBean> allInfoBeanList, WebBasketOdds5 odd, boolean isSourceData) {
        for (BasketIndexBean.DataBean.AllInfoBean item : allInfoBeanList) {  //循环赛事
            if (item.getThirdId().equals(thirdId)) {
                for (BasketIndexBean.DataBean.AllInfoBean.MatchOddsBean m : item.getMatchOdds()) {  //遍历公司赔率
                    setAsiaLet(m, odd);
                }
                if (!isSourceData) {
                    mBasketIndexAdapter.notifyItemChanged(allInfoBeanList.indexOf(item));
                }
            }
        }
    }

    private void setAsiaLet(BasketIndexBean.DataBean.AllInfoBean.MatchOddsBean m, WebBasketOdds5 odd) {
        switch (m.getComId()) {  //macauslot（1），easybets（2），crown（CROWN）（3），bet365（8），vcbet（9）
            case "1":
                if (odd.getMacauslot() != null) {
                    setOddNowValue(m, odd.getMacauslot().get("leftOdds"), odd.getMacauslot().get("rightOdds"), odd.getMacauslot().get("handicapValue"));
                }
                break;
            case "2":
                if (odd.getEasybets() != null) {
                    setOddNowValue(m, odd.getEasybets().get("leftOdds"), odd.getEasybets().get("rightOdds"), odd.getEasybets().get("handicapValue"));
                }
                break;
            case "3":
                if (odd.getCrown() != null) {
                    setOddNowValue(m, odd.getCrown().get("leftOdds"), odd.getCrown().get("rightOdds"), odd.getCrown().get("handicapValue"));
                }
                break;
            case "8":
                if (odd.getBet365() != null) {
                    setOddNowValue(m, odd.getBet365().get("leftOdds"), odd.getBet365().get("rightOdds"), odd.getBet365().get("handicapValue"));
                }
                break;
            case "9":
                if (odd.getVcbet() != null) {
                    setOddNowValue(m, odd.getVcbet().get("leftOdds"), odd.getVcbet().get("rightOdds"), odd.getVcbet().get("handicapValue"));
                }
                break;
        }
    }

    //大小球
    private void setMatchAsiaAsizeOdds(String thirdId, List<BasketIndexBean.DataBean.AllInfoBean> allInfoBeanList, WebBasketOdds5 odd, boolean isSourceData) {
        for (BasketIndexBean.DataBean.AllInfoBean item : allInfoBeanList) {  //循环赛事
            if (item.getThirdId().equals(thirdId)) {
                for (BasketIndexBean.DataBean.AllInfoBean.MatchOddsBean m : item.getMatchOdds()) {  //遍历公司赔率
                    setAsiaSize(m, odd);
                }
                if (!isSourceData) {
                    mBasketIndexAdapter.notifyItemChanged(allInfoBeanList.indexOf(item));
                }
            }
        }
    }

    private void setAsiaSize(BasketIndexBean.DataBean.AllInfoBean.MatchOddsBean m, WebBasketOdds5 odd) {
        switch (m.getComId()) {  //大小：macauslot（4），easybets（5），crown（CROWN）（6），bet365（11），vcbet（12）
            case "4":
                if (odd.getMacauslot() != null) {
                    setOddNowValue(m, odd.getMacauslot().get("leftOdds"), odd.getMacauslot().get("rightOdds"), odd.getMacauslot().get("handicapValue"));
                }
                break;
            case "5":
                if (odd.getEasybets() != null) {
                    setOddNowValue(m, odd.getEasybets().get("leftOdds"), odd.getEasybets().get("rightOdds"), odd.getEasybets().get("handicapValue"));
                }
                break;
            case "6":
                if (odd.getCrown() != null) {
                    setOddNowValue(m, odd.getCrown().get("leftOdds"), odd.getCrown().get("rightOdds"), odd.getCrown().get("handicapValue"));
                }
                break;
            case "11":
                if (odd.getBet365() != null) {
                    setOddNowValue(m, odd.getBet365().get("leftOdds"), odd.getBet365().get("rightOdds"), odd.getBet365().get("handicapValue"));
                }
                break;
            case "12":
                if (odd.getVcbet() != null) {
                    setOddNowValue(m, odd.getVcbet().get("leftOdds"), odd.getVcbet().get("rightOdds"), odd.getVcbet().get("handicapValue"));
                }
                break;
        }


    }


    //欧赔
    private void setMatchEuroOdds(String thirdId, List<BasketIndexBean.DataBean.AllInfoBean> allInfoBeanList, WebBasketOdds5 odd, boolean isSourceData) {
        for (BasketIndexBean.DataBean.AllInfoBean item : allInfoBeanList) {  //循环赛事
            if (item.getThirdId().equals(thirdId)) {
                for (BasketIndexBean.DataBean.AllInfoBean.MatchOddsBean m : item.getMatchOdds()) {  //遍历公司赔率
                    setEuro(m, odd);
                }
                if (!isSourceData) {
                    mBasketIndexAdapter.notifyItemChanged(allInfoBeanList.indexOf(item));
                }
            }
        }
    }

    private void setEuro(BasketIndexBean.DataBean.AllInfoBean.MatchOddsBean m, WebBasketOdds5 odd) {
        if (EURO_AVERAGE.equals(m.getComId())) {
            if (odd.getEuro() != null) {
                setOddNowValue(m, odd.getEuro().get("leftOdds"), odd.getEuro().get("rightOdds"), odd.getEuro().get("handicapValue"));
            }
        }
    }

    private void setOddNowValue(BasketIndexBean.DataBean.AllInfoBean.MatchOddsBean m, String left, String right, String handicap) {
        if (!StringUtils.isEmpty(left) && !StringUtils.isEmpty(right) && !StringUtils.isEmpty(handicap)) {
            if (m.getOddsData() != null && m.getOddsData().size() > 0) {

                m.getOddsData().get(0).setLeftOddsTrend(setOddTrend(Float.parseFloat(m.getOddsData().get(0).getLeftOdds()), Float.parseFloat(left)));
                m.getOddsData().get(0).setLeftOdds(left);

                m.getOddsData().get(0).setLeftOddsTrend(setOddTrend(Float.parseFloat(m.getOddsData().get(0).getRightOdds()), Float.parseFloat(right)));
                m.getOddsData().get(0).setRightOdds(right);

                m.getOddsData().get(0).setLeftOddsTrend(setOddTrend(Float.parseFloat(m.getOddsData().get(0).getHandicapValue()), Float.parseFloat(handicap)));
                m.getOddsData().get(0).setHandicapValue(handicap);
            }
        }
    }


    private int setOddTrend(Float pre, Float curr) {
        if (curr > pre) {
            return 1;
        } else if (curr == pre) {
            return 0;
        } else {
            return -1;
        }
    }


    private List<BasketIndexBean.DataBean.AllInfoBean> getDataList(boolean isSourceData) {
        return isSourceData ? sourceDataList : destinationDataList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
        parentFragment = (BasketBallCpiFrament) getParentFragment();
    }
}
