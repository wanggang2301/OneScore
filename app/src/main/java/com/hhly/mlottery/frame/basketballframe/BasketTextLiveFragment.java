package com.hhly.mlottery.frame.basketballframe;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.basketball.BasketBallTextLiveAdapter;
import com.hhly.mlottery.bean.basket.basketdetails.BasketEachTextLiveBean;
import com.hhly.mlottery.bean.basket.basketdetails.BasketTextLiveBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangg
 * @name BasketTextLiveFragment
 * 篮球直播标签之文字直播
 */
public class BasketTextLiveFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private View mView;

    private BasketBallTextLiveAdapter mBasketBallTextLiveAdapter;

    private View listfooter_more;
    private View emptyView;
    private TextView mLoadMore;//加载更多

    private boolean isRequestFinish = true;//是否加载评论结束

    private ProgressBar mProgressBarRefresh;

    private ProgressBar mProgressBar;//上拉加载的进度条

    private boolean isFirstRequestData = false;

    private String mThirdId;

    private List<BasketEachTextLiveBean> data;


    public static BasketTextLiveFragment newInstance(String thirdId) {
        BasketTextLiveFragment basketTextLiveFragment = new BasketTextLiveFragment();
        Bundle bundle = new Bundle();
        bundle.putString("thirdId", thirdId);
        basketTextLiveFragment.setArguments(bundle);
        return basketTextLiveFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mThirdId = getArguments().getString("thirdId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_basket_text_live, container, false);

        listfooter_more = getActivity().getLayoutInflater().inflate(R.layout.listfooter_more, container, false);

        emptyView = getActivity().getLayoutInflater().inflate(R.layout.layout_nodata, container, false);

      //  initView();

       // loadData();

        return mView;
    }

    private void initView() {
        //mView的控件
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        mProgressBarRefresh = (ProgressBar) mView.findViewById(R.id.pull_to_refresh_progress);
        mProgressBarRefresh.setVisibility(View.GONE);

        //加载更多控件初始化
        mLoadMore = (TextView) listfooter_more.findViewById(R.id.load_more);
        mProgressBar = (ProgressBar) listfooter_more.findViewById(R.id.pull_to_refresh_progress);

        TextView bottomline = (TextView) listfooter_more.findViewById(R.id.bottomline);
        bottomline.setVisibility(View.GONE);

        mLoadMore.setText(R.string.foot_loadmore);  //加载更多
        listfooter_more.setVisibility(View.GONE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);

        //设置Adapter
        if (getActivity() != null) {
            mBasketBallTextLiveAdapter = new BasketBallTextLiveAdapter(R.layout.item_basket_text_live, null, getActivity());
            mRecyclerView.setAdapter(mBasketBallTextLiveAdapter);

            mBasketBallTextLiveAdapter.setPullUpLoading(new BasketBallTextLiveAdapter.PullUpLoading() {
                @Override
                public void onPullUpLoading(String id) {
                    pullUpLoadMore(id);
                }
            });
        }

        listfooter_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRequestFinish) {//上一个请求完成才执行这个 不然一直往上拉，会连续发多个请求
                    if (!mLoadMore.getText().equals(getResources().getString(R.string.foot_nomoredata))) {//没有更多数据的时候，上拉不再发起请求
                        // getRequestTextLiveData();
                    }
                }
            }
        });

        mBasketBallTextLiveAdapter.addFooterView(listfooter_more);
    }

    /**
     * 上拉加载更多文字直播数据
     */
    public void pullUpLoadMore(String id) {
        if (isRequestFinish) {//上一个请求完成才执行这个 不然一直往上拉，会连续发多个请求
            //请求下一页数据
            if (!mLoadMore.getText().equals(getResources().getString(R.string.foot_nomoredata))) {//没有更多数据的时候，上拉不再发起请求
                //  getRequestTextLiveData(id);
            }
        }
    }


    Handler textLiveHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                isRequestFinish = true;
                mProgressBar.setVisibility(View.GONE);
                mLoadMore.setText(R.string.foot_loadmore);
                mBasketBallTextLiveAdapter.getData().clear();
                mBasketBallTextLiveAdapter.addData(data);
                mBasketBallTextLiveAdapter.notifyDataSetChanged();
            }

        }
    };

    /**
     * 分页查询文字直播数据
     */
    public void getRequestTextLiveData(String id) {
        mProgressBar.setVisibility(View.VISIBLE);
        isRequestFinish = false;
        mLoadMore.setText(R.string.foot_loadingmore);  //正在加载更多...

        if (isFirstRequestData) {
            Map<String, String> params = new HashMap<>();
            params.put("thirdId", "3666697");
            params.put("id", id); //首次请求id
            VolleyContentFast.requestJsonByPost(BaseURLs.BASKET_DETAIL_TEXTLIVE, params, new VolleyContentFast.ResponseSuccessListener<BasketTextLiveBean>() {
                @Override
                public void onResponse(BasketTextLiveBean basketTextLiveBean) {
                    if (basketTextLiveBean == null || 200 != basketTextLiveBean.getResult()) {
                        return;
                    }

                    // data = basketTextLiveBean.getData();
                    // L.d(TAG, "请求成功");
                    // L.d(TAG, basketTextLiveBean.getData().get(0).getEventContent());
                    textLiveHandler.sendEmptyMessage(0);

                }
            }, new VolleyContentFast.ResponseErrorListener() {
                @Override
                public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                }
            }, BasketTextLiveBean.class);

        } else {
            isFirstRequestData = true;
            isRequestFinish = true;
            mProgressBar.setVisibility(View.GONE);
            mLoadMore.setText(R.string.foot_neterror);
        }

    }


    /**
     * 首次进入请求文字直播
     */
    public void loadData() {
        mProgressBarRefresh.setVisibility(View.VISIBLE);  //请求转圈圈正在请求数据

        Map<String, String> params = new HashMap<>();
        params.put("thirdId", "3666697");
        params.put("id", "0"); //首次请求id=0
        VolleyContentFast.requestJsonByPost(BaseURLs.BASKET_DETAIL_TEXTLIVE, params, new VolleyContentFast.ResponseSuccessListener<BasketTextLiveBean>() {
            @Override
            public void onResponse(BasketTextLiveBean basketTextLiveBean) {
                if (basketTextLiveBean == null || 200 != basketTextLiveBean.getResult()) {
                    return;
                }

                data = basketTextLiveBean.getData();

                mProgressBarRefresh.setVisibility(View.GONE);
                mLoadMore.setText(R.string.foot_loadmore);   //加载更多

                if (data != null && data.size() > 4) {
                    listfooter_more.setVisibility(View.VISIBLE);
                } else {
                    listfooter_more.setVisibility(View.GONE);
                }
                if (data.size() == 0) {//，没请求到数据 mNoData显示
                    mBasketBallTextLiveAdapter.setEmptyView(emptyView);
                } else {
                    mBasketBallTextLiveAdapter.getData().clear();
                    mBasketBallTextLiveAdapter.addData(data);
                    mBasketBallTextLiveAdapter.notifyDataSetChanged();
                    mRecyclerView.smoothScrollToPosition(0);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
            }
        }, BasketTextLiveBean.class);

    }

}
