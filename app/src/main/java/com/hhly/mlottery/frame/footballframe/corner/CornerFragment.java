package com.hhly.mlottery.frame.footballframe.corner;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballCornerActivity;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.adapter.ScheduleDateAdapter;
import com.hhly.mlottery.adapter.corner.CornerListAdapter;
import com.hhly.mlottery.bean.corner.CornerListBean;
import com.hhly.mlottery.bean.scheduleBean.ScheduleDate;
import com.hhly.mlottery.callback.CornerDateListerner;
import com.hhly.mlottery.callback.FocusMatchClickListener;
import com.hhly.mlottery.config.FootBallDetailTypeEnum;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.mvp.ViewFragment;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.HandMatchId;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hhly.mlottery.activity.FootballMatchDetailActivity.BUNDLE_PARAM_THIRDID;

/**
 * Created by mdy
 * 足球角球页面
 * date:2017/05/18
 */
public class CornerFragment extends ViewFragment<CornerContract.Presenter> implements CornerContract.View  {

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

    /**
     *日期选择
     */
    @BindView(R.id.corner_date_layout)
    LinearLayout mDateLayout;

    @BindView(R.id.corner_tv_date)
    TextView mTextDate;

    @BindView(R.id.corner_tv_week)
    TextView mTextWeek;

    private View mNoLoadingView; //没有更多
    private View mOnloadingView; //正在加载更多
    private LinearLayout mIsLoading; //加载中
    private LinearLayout mLoadingError; //加载失败
    private TextView mReLoad; //点击重试

    private ListView mDateListView;

    private ScheduleDateAdapter mDateAdapter;

    private List<ScheduleDate> mDatelist; // 日期

    private String mCurrentDate;
    private String mTitleDate;
    private static final int DATETOTAL = 7;

    private  int currentDatePosition = 1;

//    private boolean mFirst

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

        mCurrentDate=DateUtil.getMomentDate();
        mTitleDate=DateUtil.getMomentDate();

        mPresenter.refreshData(mType,currentDatePosition,false);

        mTextDate.setText(DateUtil.convertDateToNation(mTitleDate));
        mTextWeek.setText(DateUtil.getWeekOfXinQi(DateUtil.parseDate(mTitleDate)));
        initListDateAndWeek(mCurrentDate,currentDatePosition);

        mNodataLayout.setVisibility(View.GONE);
        mExceptionLayout.setVisibility(View.GONE);
        mProgressBarLayout.setVisibility(View.GONE);
        mBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExceptionLayout.setVisibility(View.GONE);
                mProgressBarLayout.setVisibility(View.VISIBLE);
                mPresenter.refreshData(mType,currentDatePosition ,false);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_DRAGGING){
                    mAdapter.setCurrentDate(currentDatePosition,true);
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialog);

                final AlertDialog alertDialog=builder.create();

                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View alertDialogView = inflater.inflate(R.layout.alertdialog, null);
                mDateListView = (ListView) alertDialogView.findViewById(R.id.listdate);
                initListDateAndWeek(mCurrentDate,currentDatePosition);

                mDateListView.setAdapter(mDateAdapter);
                mDateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        mPresenter.refreshData(mType,position,true);
                        alertDialog.dismiss();
                        mTextDate.setText(DateUtil.convertDateToNation(mDatelist.get(position).getDate()));
                        mTextWeek.setText(mDatelist.get(position).getWeek());
                        mTitleDate=mDatelist.get(position).getDate();
                    }
                });

                alertDialog.show();
                alertDialog.getWindow().setContentView(alertDialogView);
                alertDialog.setCanceledOnTouchOutside(true);
            }
        });
        if(mAdapter==null){
            mAdapter=new CornerListAdapter(mPresenter.getData(),getActivity(),mDatelist);
        }
        mOnloadingView=getActivity().getLayoutInflater().inflate(R.layout.onloading, (ViewGroup) mRecyclerView.getParent(),false);
        mIsLoading= (LinearLayout) mOnloadingView.findViewById(R.id.is_loading_more);
        mLoadingError= (LinearLayout) mOnloadingView.findViewById(R.id.re_load);
        mLoadingError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoadingError.setVisibility(View.GONE);
                mIsLoading.setVisibility(View.VISIBLE);
                mPresenter.refreshDataByPage(mType,currentDatePosition+1,false);
            }
        });
        mAdapter.setLoadingView(mOnloadingView);
        mAdapter.openLoadMore(mPresenter.getData().size()<=3?3:mPresenter.getData().size(),true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
//        if(mPresenter.getData().size()<3){
//            mAdapter.setOn
//        }
        //上拉加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.refreshDataByPage(mType,currentDatePosition+1,false);

                    }
                },1000);

            }
        });



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
                    intent.putExtra("current_ab", FootBallDetailTypeEnum.FOOT_DETAIL_LIVE);
                    getActivity().startActivity(intent);
                }
            }
        });

        mAdapter.setDateListener(new CornerDateListerner() {
            @Override
            public void setDate(String date ,int position) {
                mTextDate.setText(DateUtil.convertDateToNation(mDatelist.get(position).getDate()));
                mTextWeek.setText(mDatelist.get(position).getWeek());
                currentDatePosition=position;
            }
        });

//        mCdt = new CountDownTimer(10000*1000, 1000*5 ) { //每两秒一次
//            @Override
//            public void onTick(long millisUntilFinished) {
//                mPresenter.refreshData(mType,currentDatePosition);
//            }
//            @Override
//            public void onFinish() {
//
//            }
//        };
//
//        mCdt.start();
    }

    /**
     * 初始化7天的日期
     *
     * @param s
     */
    private void initListDateAndWeek(String s, int position) {
        mDatelist = new ArrayList<ScheduleDate>();
        for (int i = 1; i >- 7; i--) {
            mDatelist.add(new ScheduleDate(DateUtil.getDate(i, s), DateUtil.getWeekOfXinQi(DateUtil.parseDate(DateUtil.getDate(i, s)))));
        }
        mDateAdapter = new ScheduleDateAdapter(mDatelist, getActivity(), position);
    }

    public void refreshDate(){
        mPresenter.refreshData(mType,currentDatePosition,false);
    }

    @Override
    public void onError() {

    }

    @Override
    public void recyclerNotify() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.setNewData(mPresenter.getData()); //点进去看下
        mAdapter.setLoadingView(mOnloadingView);
        mAdapter.openLoadMore(mPresenter.getData().size()<3?12:mPresenter.getData().size(),true);

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
    public void setDateListPosition(int position) {
        currentDatePosition=position;
        L.e("AAA",currentDatePosition+"");
    }

    @Override
    public void refreshShow(boolean refresh) {
        if(getActivity()!=null){
            ((FootballCornerActivity)getActivity()).setRefreshing(refresh);
        }
    }

    @Override
    public void showNoMoreData() {
        if(mNoLoadingView==null){
            mNoLoadingView=getActivity().getLayoutInflater().inflate(R.layout.nomoredata, (ViewGroup) mRecyclerView.getParent(),false);
        }
        mAdapter.notifyDataChangedAfterLoadMore(false);
        mAdapter.addFooterView(mNoLoadingView);


    }

    @Override
    public void showNextPage(List<CornerListBean.CornerEntity> cornerListBean) {
        mAdapter.notifyDataChangedAfterLoadMore(cornerListBean,true);
        mTextDate.setText(DateUtil.convertDateToNation(mDatelist.get(currentDatePosition).getDate()));
        mTextWeek.setText(mDatelist.get(currentDatePosition).getWeek());
        mAdapter.setCurrentDate(currentDatePosition,false);
    }

    @Override
    public void showNextPageError() {
        mIsLoading.setVisibility(View.GONE);
        mLoadingError.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if(mCdt!=null){
//            mCdt.cancel();
//        }
    }


    @Override
    public CornerContract.Presenter initPresenter() {
        return  new CornerPresenter(this);
    }


}
