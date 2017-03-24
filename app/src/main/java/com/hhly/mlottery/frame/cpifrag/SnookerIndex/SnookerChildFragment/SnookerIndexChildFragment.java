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
import com.hhly.mlottery.activity.FootballAnalyzeDetailsActivity;
import com.hhly.mlottery.activity.SnookerMatchDetail;
import com.hhly.mlottery.adapter.snooker.SnookerIndexAdapter;
import com.hhly.mlottery.bean.snookerbean.snookerIndexBean.SnookerIndexBean;
import com.hhly.mlottery.frame.cpifrag.SnookerIndex.SIndexFragment;
import com.hhly.mlottery.mvp.ViewFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SnookerIndexChildFragment extends ViewFragment<SnookerIndexChildContract.Presenter> implements SnookerIndexChildContract.View {

    private static final String ARG_PARAM1 = "param1";


    private String mType;
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
    /**当前日期*/
    private String mDate="";

    public SnookerIndexChildFragment() {
        // Required empty public constructor
    }

    public static SnookerIndexChildFragment newInstance(String type) {
        SnookerIndexChildFragment fragment = new SnookerIndexChildFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentFragment= (SIndexFragment) getParentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_PARAM1);
        }
        //TODO:改成dagger
        mPresenter=new SnookerIndexChildPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_snooker_index_euro, container, false);

        ButterKnife.bind(this,mView);
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
                mPresenter.refreshByDate(mDate,mType);
            }
        });

        mAdapter=new SnookerIndexAdapter(mPresenter.getData(),mActivity,mType);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.refreshByDate("",mType);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent=new Intent(getActivity(), SnookerMatchDetail.class);
                intent.putExtra("matchId",mPresenter.getData().get(i).getMatchInfo().getMatchId());
                getActivity().startActivity(intent);
            }
        });


    }

    /**
     * 根据选中的日期刷新
     * @param date
     */
    public void refreshDate(String date){
        mDate=date;
        mPresenter.refreshByDate(date,mType);
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
        if(error.equals(SnookerIndexChildPresenter.NODATA)){
            mNodataLayout.setVisibility(View.VISIBLE);
            mExceptionLayout.setVisibility(View.GONE);
            mProgressBarLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            parentFragment.setRefreshing(false);
        }
        else if(error.equals(SnookerIndexChildPresenter.NETERROR)){
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
        if(parentFragment.getCompanyList().size()==0){ //只是第一次的时候进行此操作。下拉刷新不再更改公司选中状态
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

}
