package com.hhly.mlottery.frame.footballframe.corner;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballCornerActivity;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.adapter.corner.CornerListAdapter;
import com.hhly.mlottery.bean.corner.CornerListBean;
import com.hhly.mlottery.callback.FocusMatchClickListener;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.mvp.ViewFragment;
import com.hhly.mlottery.util.HandMatchId;
import com.hhly.mlottery.util.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hhly.mlottery.activity.FootballMatchDetailActivity.BUNDLE_PARAM_THIRDID;

/**
 * Created by mdy
 * 足球角球页面
 * date:2017/05/18
 */
public class CornerFragment extends ViewFragment<CornerContract.Presenter> implements CornerContract.View {

    View mView;
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

    @BindView(R.id.corner_list_recycler)
    RecyclerView mRecyclerView;

//    @BindView(R.id.corner_refresh_layout)
//    ExactSwipeRefreshLayout mRefreshLayout;

    private CornerListAdapter mAdapter;

    String mType;
    CountDownTimer mCdt;
    private static final String FRAGMENT_INDEX = "fragment_index";

    private FocusMatchClickListener mFocusClickListener;// 关注点击事件
    public CornerFragment() {
        // Required empty public constructor
    }


    public static CornerFragment getInstance(String type){
        CornerFragment cornerFragment=new CornerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_INDEX, type);
        cornerFragment.setArguments(bundle);
        return cornerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter=new CornerPresenter(this);
        if (getArguments() != null) {
            mType=getArguments().getString(FRAGMENT_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_corner,container,false);
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
                mPresenter.refreshData(mType);
            }
        });

        mAdapter=new CornerListAdapter(mPresenter.getData(),getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.refreshData(mType);

        mFocusClickListener = new FocusMatchClickListener() { // 关注按钮事件
            @Override
            public void onClick(View view, String third) {


                boolean isCheck = (Boolean) view.getTag();// 检查之前是否被选中
                if (!isCheck) {// 插入数据
                    String focusIds = PreferenceUtil.getString(StaticValues.CORNER_FOCUS_ID, "");
                    if ("".equals(focusIds)) {
                        PreferenceUtil.commitString(StaticValues.CORNER_FOCUS_ID, third);
                    } else {
                        PreferenceUtil.commitString(StaticValues.CORNER_FOCUS_ID, focusIds + "," + third);
                    }
                    // ((TextView) view).setText(R.string.cancel_favourite);
                    ((ImageView) view).setImageResource(R.mipmap.football_focus);

                    view.setTag(true);
                } else {// 删除
                    String focusIds = PreferenceUtil.getString(StaticValues.CORNER_FOCUS_ID, "");
                    String[] idArray = focusIds.split("[,]");
                    StringBuffer sb = new StringBuffer();
                    for (String id : idArray) {
                        if (!id.equals(third)) {
                            if ("".equals(sb.toString())) {
                                sb.append(id);
                            } else {
                                sb.append("," + id);
                            }

                        }
                    }
                    PreferenceUtil.commitString(StaticValues.CORNER_FOCUS_ID, sb.toString());
                    //  ((TextView) view).setText(R.string.favourite);
                    ((ImageView) view).setImageResource(R.mipmap.football_nomal);

                    view.setTag(false);
                }
                ((FootballCornerActivity)getActivity()).focusCallback();
            }

        };
        mAdapter.setmFocusMatchClickListener(mFocusClickListener);

        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                CornerListBean.CornerEntity entity=mPresenter.getData().get(i);

                if(HandMatchId.handId(getActivity(), entity.getMatchId()+"")) {


                    Intent intent = new Intent(getActivity(), FootballMatchDetailActivity.class);
                    intent.putExtra(BUNDLE_PARAM_THIRDID, entity.getMatchId() + "");
//                ToastTools.showQuick(getActivity(),entity.getMatchId()+"");
                    intent.putExtra("current_ab", 1);
                    getActivity().startActivity(intent);
                }
            }
        });

        mCdt = new CountDownTimer(10000*1000, 1000*5 ) { //每两秒一次
            @Override
            public void onTick(long millisUntilFinished) {
                mPresenter.refreshData(mType);
            }
            @Override
            public void onFinish() {

            }
        };

        mCdt.start();
    }

    public void refreshDate(){
        mPresenter.refreshData(mType);
    }

    @Override
    public void onError() {

    }

    @Override
    public void recyclerNotify() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
        mNodataLayout.setVisibility(View.GONE);
        mExceptionLayout.setVisibility(View.GONE);
        mProgressBarLayout.setVisibility(View.GONE);
        if(getActivity()!=null){
            ((FootballCornerActivity)getActivity()).setRefreshing(false);
        }
    }

    @Override
    public void showNoData(String error) {

        if (error.equals(NODATA)) {
            mNodataLayout.setVisibility(View.VISIBLE);
            mExceptionLayout.setVisibility(View.GONE);
            mProgressBarLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
        } else if (error.equals(NETERROR)) {
            mNodataLayout.setVisibility(View.GONE);
            mExceptionLayout.setVisibility(View.VISIBLE);
            mProgressBarLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
        }
        if(getActivity()!=null){
            ((FootballCornerActivity)getActivity()).setRefreshing(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mCdt!=null){
            mCdt.cancel();
        }
    }
}
