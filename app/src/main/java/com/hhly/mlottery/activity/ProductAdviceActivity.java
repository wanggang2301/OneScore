package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.AdviceAdapter;
import com.hhly.mlottery.bean.productadvice.ProductAdviceBean;
import com.hhly.mlottery.callback.TheLikeOfProductAdviceListener;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 产品建议
 */
public class ProductAdviceActivity extends BaseActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{


    @BindView(R.id.public_txt_title)
    TextView title;
    @BindView(R.id.advice_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.advice_refresh)
    ExactSwipeRefreshLayout mRefreshLayout;

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


    private View mNoLoadingView; //没有更多
    private View mOnloadingView; //加载更多


    private final static int VIEW_STATUS_LOADING = 1;
    private final static int VIEW_STATUS_SUCCESS = 2;
    private final static int VIEW_STATUS_NET_ERROR = 3;
    private final static int VIEW_STATUS_NO_DATA=4;

    private AdviceAdapter mAdapter;
    private int currentPage=1;
    private int PAGE_SIZE=20; //每页的最大数量
    public final static  String LIKE_IDS="like_ids";
    public  static int RESULT_CODE=2; //给首页的返回code


    private List<ProductAdviceBean.DataEntity> data=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_advice);
        ButterKnife.bind(this);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        mProgressBarLayout.setVisibility(View.GONE);
        mNodataLayout.setVisibility(View.GONE);
        mExceptionLayout.setVisibility(View.GONE);

        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        findViewById(R.id.public_btn_write_advice).setVisibility(View.VISIBLE);
        findViewById(R.id.public_btn_write_advice).setOnClickListener(this);
        findViewById(R.id.public_img_back).setOnClickListener(this);
        mBtnRefresh.setOnClickListener(this);
        title.setText(R.string.title_product_advice);

        mOnloadingView=getLayoutInflater().inflate(R.layout.onloading, (ViewGroup) mRecyclerView.getParent(),false);

        mRefreshLayout.setColorSchemeResources(R.color.tabhost);
        mRefreshLayout.setOnRefreshListener(this);

        mAdapter=new AdviceAdapter(data,this);
        mAdapter.setLoadingView(mOnloadingView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.openLoadMore(PAGE_SIZE,true);


    }
    private void setListener(){
        //点赞监听
        mAdapter.setUserLikeClickListener(new TheLikeOfProductAdviceListener() {
            @Override
            public void onclick(View view, String id) {

            }
        });

        //上拉加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestByPage();
                    }
                },1000);

            }
        });
        //首次進來可以刷新
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        });

    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case VIEW_STATUS_LOADING:
                    mExceptionLayout.setVisibility(View.GONE);
                    mProgressBarLayout.setVisibility(View.VISIBLE);
                    mNodataLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                    break;
                case VIEW_STATUS_SUCCESS:
                    mProgressBarLayout.setVisibility(View.GONE);
                    mExceptionLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mNodataLayout.setVisibility(View.GONE);
                    break;
                case VIEW_STATUS_NET_ERROR:
                    mProgressBarLayout.setVisibility(View.GONE);
                    mNodataLayout.setVisibility(View.GONE);
                    mExceptionLayout.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    if(mRefreshLayout.isRefreshing()){
                        mRefreshLayout.setRefreshing(false);
                    }
                    break;
                // 无数据界面
                case VIEW_STATUS_NO_DATA:
                    mNodataLayout.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    break;

                case 5:
//                    mAdapter.notifyDataChangedAfterLoadMore(true);
//                    mAdapter.remove();
//                    mNoLoadingView.setVisibility(View.GONE);

                    break;
            }
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_btn_write_advice:
                startActivity(new Intent(this,FeedbackActivity.class));
                break;
            case R.id.public_img_back:
                setResult(RESULT_CODE);
                finish();
                break;
            case R.id.network_exception_reload_btn:
                handler.sendEmptyMessage(VIEW_STATUS_LOADING);
                initData();
                break;
        }
    }

    /**
     * 第一次请求
     */
    private void initData(){
        currentPage=1;
        String url="http://192.168.33.45:8080/mlottery/core/feedback.findAndroidFeedBackDetail.do";
        HashMap<String,String> params=new HashMap<>();
        params.put("currentPage",1+"");

        VolleyContentFast.requestJsonByGet(BaseURLs.PRODUCT_ADVICE_LIKE_LIST, params,new VolleyContentFast.ResponseSuccessListener<ProductAdviceBean>() {
            @Override
            public void onResponse(ProductAdviceBean jsonObject) {
                if(mRefreshLayout.isRefreshing()){
                    mRefreshLayout.setRefreshing(false);
                }

                mAdapter.openLoadMore(PAGE_SIZE,true);

                if(jsonObject.getResult()==200){
                    handler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
                   mAdapter.setNewData(jsonObject.getData());
                    mAdapter.notifyDataSetChanged();
                    if(jsonObject.getData().size()==0){
                        handler.sendEmptyMessage(VIEW_STATUS_NO_DATA);
                    }
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                handler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
            }
        },ProductAdviceBean.class);

    }

    /**
     * 分页请求
     */
    private void requestByPage(){
        String url="http://192.168.33.45:8080/mlottery/core/feedback.findAndroidFeedBackDetail.do";
        HashMap<String,String> params=new HashMap<>();
        currentPage=++currentPage;
        params.put("currentPage",currentPage+"");

        VolleyContentFast.requestJsonByGet(BaseURLs.PRODUCT_ADVICE_LIKE_LIST, params,new VolleyContentFast.ResponseSuccessListener<ProductAdviceBean>() {
            @Override
            public void onResponse(ProductAdviceBean jsonObject) {


                if(jsonObject.getResult()==200){

                    if(jsonObject.getData().size()==0){
                        if(mNoLoadingView==null){
                            mNoLoadingView=getLayoutInflater().inflate(R.layout.nomoredata, (ViewGroup) mRecyclerView.getParent(),false);
                        }
                        mAdapter.notifyDataChangedAfterLoadMore(false);
                        mAdapter.addFooterView(mNoLoadingView);
                    }
                    else {
                        mAdapter.notifyDataChangedAfterLoadMore(jsonObject.getData(),true);
                    }

                }else{

                }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

                Toast.makeText(ProductAdviceActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        },ProductAdviceBean.class);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            setResult(RESULT_CODE);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                initData();
            }
        },1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
