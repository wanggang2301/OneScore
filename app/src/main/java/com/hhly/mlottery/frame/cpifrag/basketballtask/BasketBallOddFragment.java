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
                mPresenter.showRequestData("zh", "8", "", type);
            }
        });


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

        updateFilterData();

        refreshDateView(o.getData().getCurrDate());

        //处理公司数据
        handleCompanyData(o.getData().getCompany());
    }

    /**
     * 刷新数据
     *
     * @param date date, 形如 2016-06-21 可以为空
     */
    public void refreshData(String date) {
        mPresenter.showRequestData("zh", "8", date, type);
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
        ArrayList<BasketIndexBean.DataBean.CompanyBean> companyList = parentFragment.getCompanyList();
        if (companyList.isEmpty()) {
            companyList.addAll(companyBeen);
        }
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
        if (filterLeagueList != null) {
            for (BasketIndexBean.DataBean.AllInfoBean allInfo : sourceDataList) {
                if (filterLeagueList.indexOf(allInfo.getLeagueId()) >= 0) {
                    filterAllInfo(allInfo);
                }
            }
        } else {
            for (BasketIndexBean.DataBean.AllInfoBean allInfo : sourceDataList) {
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


        //先不管公司的筛选
      /*  NewOddsInfo.AllInfoBean clone = allInfo.clone();
        List<NewOddsInfo.AllInfoBean.ComListBean> comList = clone.getComList();
        ListIterator<NewOddsInfo.AllInfoBean.ComListBean> iterator = comList.listIterator();

        // 不能直接粗暴的 remove，因为持有的是引用也会把 default 中的修改掉
        while (iterator.hasNext()) {
            NewOddsInfo.AllInfoBean.ComListBean next = iterator.next();
            if (!isOddsShow(next)) {
                iterator.remove();
            }
        }*/


        destinationDataList.add(allInfo);

/*
        if (comList.size() > 0) {
            filterData.add(clone);
        }*/
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
        parentFragment = (BasketBallCpiFrament) getParentFragment();
    }
}
