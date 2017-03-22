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
import android.widget.Button;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketDetailsActivityTest;
import com.hhly.mlottery.activity.BasketIndexDetailsActivity;
import com.hhly.mlottery.adapter.basketball.BasketIndexAdapter;
import com.hhly.mlottery.bean.basket.index.BasketIndexBean;
import com.hhly.mlottery.bean.enums.BasketOddsTypeEnum;
import com.hhly.mlottery.mvp.ViewFragment;
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

    private static final String ODD_TYPE = "ODD_TYPE";

    //默认公司筛选选中头两家公司
    private static final int DEFAULT_SELECTED_COMPANY = 2;

    @BindView(R.id.btn_test)
    Button btnTest;
    @BindView(R.id.cpi_odds_recyclerView)
    RecyclerView cpiOddsRecyclerView;
    private String type;

    private BasketIndexAdapter mBasketIndexAdapter;

    private View mView;

    private Activity mActivity;

    private BasketBallCpiFrament parentFragment;

    private List<BasketIndexBean.DataBean.FileterTagsBean> fileterTagsBeanList;

    private List<BasketIndexBean.DataBean.AllInfoBean> sourceDataList; //源数据

    private List<BasketIndexBean.DataBean.AllInfoBean> destinationDataList; //目标数据

    //过滤后的列表数据
    private List<BasketIndexBean.DataBean.AllInfoBean> fileterAllInfoBeanList;


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

        fileterAllInfoBeanList = new ArrayList<>();

        fileterTagsBeanList = new ArrayList<>();

        sourceDataList = new ArrayList<>();

        destinationDataList = new ArrayList<>();

        mPresenter = new BasketBallOddPresenter(this);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示数据
                mPresenter.showRequestData("", type);
            }
        });

        mPresenter.showRequestData("", type);

        cpiOddsRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        mBasketIndexAdapter = new BasketIndexAdapter(mActivity, destinationDataList, type);

        mBasketIndexAdapter.setOnItemClickListener(new BasketIndexAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BasketIndexBean.DataBean.AllInfoBean allInfoBean) {
                Intent intent = new Intent(mActivity, BasketDetailsActivityTest.class);
            }
        });

        mBasketIndexAdapter.setOnOddIetmClickListener(new BasketIndexAdapter.onOddIetmClickListener() {
            @Override
            public void onOddItemCLick(BasketIndexBean.DataBean.AllInfoBean allInfoBean, BasketIndexBean.DataBean.AllInfoBean.MatchOddsBean item) {

              /*  Intent intent = new Intent(mActivity, BasketIndexDetailsActivity.class);
                intent.putExtra("obListEntity", (Serializable) obList);
                intent.putExtra("comId", item.getComId());
                intent.putExtra("positionNunber", item.get().indexOf(odds) + "");
                intent.putExtra("stType", type);*/
                Intent intent = new Intent(mActivity, BasketIndexDetailsActivity.class);
                mActivity.startActivity(intent);

            }
        });

        cpiOddsRecyclerView.setAdapter(mBasketIndexAdapter);
    }

    @Override
    public void showLoadView() {
        Toast.makeText(mActivity, "加载", Toast.LENGTH_SHORT).show();
        L.d("bingd", "加载中_____________");

    }

    @Override
    public void showRequestDataView(BasketIndexBean o) {
        Toast.makeText(mActivity, "成功", Toast.LENGTH_SHORT).show();
        L.d("bingd", "成功_____________" + o.getData().getCompany().get(0).getComName());


        //获取筛选的list
        fileterTagsBeanList.clear();
        fileterTagsBeanList.addAll(o.getData().getFileterTags());

        sourceDataList.clear();
        sourceDataList.addAll(o.getData().getAllInfo());


        //处理公司数据  请求到数据成功后已经将需要默认选择的公司数据处理好了
        handleCompanyData(o.getData().getCompany());
        refreshDateView(o.getData().getCurrDate());

        updateFilterData();


    }

    /**
     * 刷新数据
     *
     * @param date date, 形如 2016-06-21 可以为空
     */
    public void refreshData(String date) {
        mPresenter.showRequestData(date, type);
    }


    /**
     * 刷新日期View
     */
    private void refreshDateView(String date) {
        if (parentFragment.getCurrentFragment() == BasketBallOddFragment.this) {
            // 获取当前日期并返回给 CPINewFragment
            parentFragment.setCurrentDate(date);
            parentFragment.setRefreshing(false);
        }
    }


    @Override
    public void noData() {

    }

    @Override
    public void onError() {
        Toast.makeText(mActivity, "出错", Toast.LENGTH_SHORT).show();
        L.d("bingd", "出错_____________");
    }

    private void handleCompanyData(List<BasketIndexBean.DataBean.CompanyBean> companyBeen) {
        parentFragment.showRightButton();
        // ArrayList<BasketIndexBean.DataBean.CompanyBean> companyList = parentFragment.getCompanyList(type);
        //companyList.clear(); //清除以前的公司数据 因为公司数据各个赔率是单独的
        //companyList.addAll(companyBeen);

        L.d("comp", type + "_________" + companyBeen.size());

        int size = companyBeen.size();
        size = size < DEFAULT_SELECTED_COMPANY ? size : DEFAULT_SELECTED_COMPANY;

        for (int i = 0; i < size; i++) {
            companyBeen.get(i).setChecked(true);
        }


        parentFragment.getCompanyMap().put(type, companyBeen);

    }

    public List<BasketIndexBean.DataBean.FileterTagsBean> getFilterTagList() {
        return fileterTagsBeanList;
    }


    /**
     * 更新过滤数据源（注意要过滤公司赔率信息）
     */
    public void updateFilterData() {
        destinationDataList.clear();

        //赛事的筛选
        LinkedList<String> filterLeagueList = parentFragment.getFilterLeagueList();

        L.d("datasize", "sourceDataList前===" + sourceDataList.size() + "");

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

        L.d("datasize", "sourceDataList后===" + sourceDataList.size() + "");

        L.d("datasize", "destinationDataList===" + destinationDataList.size() + "");


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
