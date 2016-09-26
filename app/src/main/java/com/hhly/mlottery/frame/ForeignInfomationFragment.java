package com.hhly.mlottery.frame;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.ForeignInfomationAdapter;
import com.hhly.mlottery.bean.foreigninfomation.ForeignInfomationBean;
import com.hhly.mlottery.bean.foreigninfomation.OverseasInformationListBean;
import com.hhly.mlottery.callback.ForeignInfomationEvent;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefrashLayout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * @author wang gang
 * @date 2016/9/20 10:00
 * @des 资讯—境外新闻
 */
public class ForeignInfomationFragment extends Fragment implements ExactSwipeRefrashLayout.OnRefreshListener {

    private static final int DATA_STATUS_LOADING = 1;
    private static final int DATA_STATUS_SUCCESS = 2;
    private static final int DATA_STATUS_ERROR = -1;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    ExactSwipeRefrashLayout refresh;
    @BindView(R.id.network_exception_reload_btn)
    TextView networkExceptionReloadBtn;
    @BindView(R.id.network_exception_reload_layout)
    LinearLayout networkExceptionReloadLayout;
    @BindView(R.id.network_exception_layout)
    LinearLayout networkExceptionLayout;
    private View mView;
    private View moreView;  //加载更多
    private ForeignInfomationAdapter foreignInfomationAdapter;
    private List<Integer> list;
    private Context mContext;
    private boolean isCreated = false;//当前碎片是否createview
    private int pageSize = 1;

    private List<OverseasInformationListBean> mList;

    private ForeignTightCallBack foreignTightCallBack;


    public static ForeignInfomationFragment newInstance() {
        ForeignInfomationFragment fragment = new ForeignInfomationFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_foreign_infomation, container, false);
        moreView = inflater.inflate(R.layout.view_load_more, container, false);
        ButterKnife.bind(this, mView);
        mContext = getActivity();
         EventBus.getDefault().register(this);

        initView();
        if (getUserVisibleHint()) {
            onVisible();
        }
        return mView;
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        refresh.setOnRefreshListener(this);
        refresh.setColorSchemeResources(R.color.bg_header);
        refresh.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), StaticValues.REFRASH_OFFSET_END));
        isCreated = true;

    }


    private Runnable mLoadingDataThread = new Runnable() {
        @Override
        public void run() {
            loadData();
        }
    };

    private void loadData() {
        Map<String, String> params = new HashMap<>();
        params.put("pageNum", pageSize + "");
        String url = "http://192.168.31.178:8080/mlottery/core/overseasInformation.findOverseasInformation.do";
        VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<ForeignInfomationBean>() {
                    @Override
                    public void onResponse(ForeignInfomationBean foreignInfomationBean) {
                        if (!foreignInfomationBean.getResult().equals("200")) {
                            return;
                        }

                        L.d("2334", "請求成功");
                        mList = foreignInfomationBean.getOverseasInformationList();
                        initViewData();
                        mHandler.sendEmptyMessage(DATA_STATUS_SUCCESS);
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        mHandler.sendEmptyMessage(DATA_STATUS_ERROR);
                    }
                }, ForeignInfomationBean.class
        );
    }


    private void initViewData() {
        foreignInfomationAdapter = new ForeignInfomationAdapter(mContext, mList);
        recyclerView.setAdapter(foreignInfomationAdapter);
        foreignInfomationAdapter.openLoadMore(0, true);
        foreignInfomationAdapter.setLoadingView(moreView);
        foreignInfomationAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        pullUpLoadMoreData();
                    }
                });
            }
        });
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DATA_STATUS_LOADING:
                    refresh.setRefreshing(true);
                    refresh.setVisibility(View.VISIBLE);
                    networkExceptionLayout.setVisibility(View.GONE);
                    break;
                case DATA_STATUS_SUCCESS:
                    refresh.setRefreshing(false);
                    refresh.setVisibility(View.VISIBLE);
                    networkExceptionLayout.setVisibility(View.GONE);

                    break;
                case DATA_STATUS_ERROR:
                    refresh.setRefreshing(false);
                    refresh.setVisibility(View.GONE);
                    networkExceptionLayout.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };


    /**
     * 上拉加载更多
     */
    private void pullUpLoadMoreData() {
        pageSize++;
        Map<String, String> params = new HashMap<>();
        params.put("pageNum", pageSize + "");
        String url = "http://192.168.31.178:8080/mlottery/core/overseasInformation.findOverseasInformation.do";
        VolleyContentFast.requestJsonByGet(url, params, new VolleyContentFast.ResponseSuccessListener<ForeignInfomationBean>() {
                    @Override
                    public void onResponse(ForeignInfomationBean foreignInfomationBean) {
                        if (!foreignInfomationBean.getResult().equals("200")) {
                            return;
                        }
                        mList.addAll(foreignInfomationBean.getOverseasInformationList());
                        foreignInfomationAdapter.notifyDataChangedAfterLoadMore(true);
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                    }
                }, ForeignInfomationBean.class
        );
    }

    //fg切换时回调该方法
    //用来取消viewpager的预加载机制  减少初始化开销
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isCreated && getUserVisibleHint()) {
            onVisible();
        }
        if (!isCreated) {
            return;
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {
        mHandler.sendEmptyMessage(DATA_STATUS_LOADING);
        new Handler().postDelayed(mLoadingDataThread, 0);

        //mHandler.sendEmptyMessage(DATA_STATUS_ERROR);
    }


    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        pageSize = 1;
        new Handler().postDelayed(mLoadingDataThread, 5000);
    }

    /**
     * 网络异常重新请求
     */
    @OnClick(R.id.network_exception_reload_btn)
    public void onClick() {
        pageSize = 1;
        mHandler.sendEmptyMessage(DATA_STATUS_LOADING);
        new Handler().postDelayed(mLoadingDataThread, 5000);
/*
        Intent intent = new Intent(mContext, ForeignInfomationDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("focusable", false);
        intent.putExtra("detailsData", new OverseasInformationListBean());
        mContext.startActivity(intent);*/

    }

    /**
     * 国外资讯由详情页退出时返回点赞数量更新列表数据源
     */
    public void onEventMainThread(ForeignInfomationEvent foreignInfomationEvent) {
        int id = foreignInfomationEvent.getId();
        int tight = foreignInfomationEvent.getFavroite();
        Iterator<OverseasInformationListBean> iterator = mList.iterator();
        while (iterator.hasNext()) {

            OverseasInformationListBean o = iterator.next();
            if (o.getId() == id) {
                o.setFavorite(tight);
                break;
            }
        }

        foreignInfomationAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
          EventBus.getDefault().unregister(this);
    }
}
