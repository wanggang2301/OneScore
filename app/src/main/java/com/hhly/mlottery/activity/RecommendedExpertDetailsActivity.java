package com.hhly.mlottery.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.ExpertsListAdapter;
import com.hhly.mlottery.adapter.RecomenHeadAdapter;
import com.hhly.mlottery.adapter.custom.RecommendArticlesAdapter;
import com.hhly.mlottery.bean.MostExpertBean;
import com.hhly.mlottery.bean.RecomeHeadBean;
import com.hhly.mlottery.bean.RecommendationExpertBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.SignUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.sina.weibo.sdk.component.view.AppProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuely198 on 2017/6/8.
 * 推荐专家详情页
 */

public class RecommendedExpertDetailsActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private final String EXPERT_ID = "expertId";

    private ImageView ex_image;//专家头像
    private TextView ex_name;//专家名称
    private TextView ex_zhong;
    private TextView ex_text;
    private RecyclerView ex_recyclerview;
    private MostExpertBean.ExpertBean expertDatas;
    private List<MostExpertBean.InfoArrayBean> infoArrayDatas = new ArrayList<>();
    private RecomenHeadAdapter recomenHeadAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView match_no_data_txt;
    private LinearLayout match_error_btn;
    private LinearLayout px_line;
    private List<RecommendationExpertBean.ExpertPromotionsBean.ListBean> listBeanList;
    private View view;
    private ProgressBar progressBar;
    private TextView loadmore_text;
    private View emptyView;
    private final static int VIEW_STATUS_LOADING = 11;
    private final static int VIEW_STATUS_SUCCESS = 33;
    private static final int VIEW_STATUS_NET_ERROR = 44;
    private static final int VIEW_STATUS_NO_DATA = 55;

    private static final int PAGE_SIZE = 10;
    private int pageNum = 1;
    private String expertId;

    private View mOnloadingView;
    private View mNoLoadingView;

    private Handler mViewHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VIEW_STATUS_LOADING:
                    match_error_btn.setVisibility(View.GONE);
                    match_no_data_txt.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(true);
                    match_error_btn.setVisibility(View.GONE);
                    px_line.setVisibility(View.GONE);
                    break;
                case VIEW_STATUS_SUCCESS:
                    match_no_data_txt.setVisibility(View.GONE);
                    match_error_btn.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    px_line.setVisibility(View.VISIBLE);
                    break;

                case VIEW_STATUS_NO_DATA:
                    match_no_data_txt.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    match_error_btn.setVisibility(View.GONE);
                    px_line.setVisibility(View.GONE);
                    break;
                case VIEW_STATUS_NET_ERROR:
                    match_no_data_txt.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    match_error_btn.setVisibility(View.VISIBLE);
                    px_line.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };
    private boolean hasNextPage;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experts);
        mContext = this;
        if (getIntent() != null) {
            expertId = getIntent().getStringExtra(EXPERT_ID);
        }

        initView();
        initData();
        initHeadData();
        // intiEvent();

    }

    private void initEvent() {
        //上拉加载更多
        recomenHeadAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (hasNextPage) {
                            pullUpLoadMoreData();
                        } else {
                            Toast.makeText(mContext, mContext.getResources().getText(R.string.nodata_txt), Toast.LENGTH_SHORT).show();
//                            mOnloadingView.findViewById(R.id.loading_text)
                            recomenHeadAdapter.addFooterView(mNoLoadingView);
                        }
                    }
                }, 1000);
            }
        });

    }


    // 请求网络数据
    private void initData() {

        Map<String, String> mapPrament = new HashMap<>();
        mapPrament.put("userId", AppConstants.register.getUser().getUserId());
        mapPrament.put("pageNum", pageNum + "");
        mapPrament.put("pageSize", PAGE_SIZE + "");
        mapPrament.put("expertId", expertId);
        if (MyApp.isLanguage.equals("rCN")) {
            // 如果是中文简体的语言环境
            mapPrament.put("lang", "zh");
        } else if (MyApp.isLanguage.equals("rTW")) {
            // 如果是中文繁体的语言环境
            mapPrament.put("lang", "zh-TW");
        }
        mapPrament.put("timeZone", "8");
        String signs = SignUtils.getSign("/promotion/info/expertPromotions", mapPrament);

        Map<String, String> param = new HashMap<>();
        param.put("userId", AppConstants.register.getUser().getUserId());
        param.put("pageNum", pageNum + "");
        param.put("pageSize", PAGE_SIZE + "");
        param.put("expertId", expertId);
        param.put("sign", signs);


        VolleyContentFast.requestJsonByGet(BaseURLs.EXPERTPROMOTIONS, param, new VolleyContentFast.ResponseSuccessListener<RecommendationExpertBean>() {

            @Override
            public void onResponse(RecommendationExpertBean jsonObject) {
                if (jsonObject != null) {

                    if (jsonObject.getExpertPromotions().getList() == null) {
                        mViewHandler.sendEmptyMessage(VIEW_STATUS_NO_DATA);
                        return;
                    }
                    hasNextPage = jsonObject.getExpertPromotions().isHasNextPage();


                    listBeanList = new ArrayList<>();
                    listBeanList.addAll(jsonObject.getExpertPromotions().getList());

                    if (recomenHeadAdapter == null) {
                        recomenHeadAdapter = new RecomenHeadAdapter(mContext, listBeanList);
                        recomenHeadAdapter.setLoadingView(view);
                        ex_recyclerview.setAdapter(recomenHeadAdapter);
                        recomenHeadAdapter.openLoadMore(0, true);
                        initEvent();

                    } else {

                        upDataAdapter();
                    }

                } else {
                    mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
            }
        }, RecommendationExpertBean.class);


    }

    private void upDataAdapter() {

        if (recomenHeadAdapter == null) {
            return;
        }
        recomenHeadAdapter.updateData(listBeanList);
        recomenHeadAdapter.notifyDataSetChanged();

    }

    private void pullUpLoadMoreData() {

        // mSwipeRefreshLayout.setRefreshing(true);
        // mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
        pageNum += 1;
        Map<String, String> mapPrament = new HashMap<>();
        mapPrament.put("userId", AppConstants.register.getUser().getUserId());
        mapPrament.put("pageNum", pageNum + "");
        mapPrament.put("pageSize", PAGE_SIZE + "");
        mapPrament.put("expertId", expertId);
        if (MyApp.isLanguage.equals("rCN")) {
            // 如果是中文简体的语言环境
            mapPrament.put("lang", "zh");
        } else if (MyApp.isLanguage.equals("rTW")) {
            // 如果是中文繁体的语言环境
            mapPrament.put("lang", "zh-TW");
        }
        mapPrament.put("timeZone", "8");
        String signs = SignUtils.getSign("/promotion/info/expertPromotions", mapPrament);

        Map<String, String> param = new HashMap<>();
        param.put("userId", AppConstants.register.getUser().getUserId());
        param.put("pageNum", pageNum + "");
        param.put("pageSize", PAGE_SIZE + "");
        param.put("expertId", expertId);
        param.put("sign", signs);


        VolleyContentFast.requestJsonByGet(BaseURLs.EXPERTPROMOTIONS, param, new VolleyContentFast.ResponseSuccessListener<RecommendationExpertBean>() {

            @Override
            public void onResponse(RecommendationExpertBean jsonObject) {

                if (jsonObject.getExpertPromotions() != null) {

                    if (jsonObject.getExpertPromotions().getList() != null && jsonObject.getExpertPromotions().getList().size() > 0) {

                        hasNextPage = jsonObject.getExpertPromotions().isHasNextPage();

                        if (jsonObject.getExpertPromotions().getList().size() == 0) {

                            recomenHeadAdapter.notifyDataChangedAfterLoadMore(false);
                            recomenHeadAdapter.addFooterView(mNoLoadingView);

                        } else {
                            recomenHeadAdapter.notifyDataChangedAfterLoadMore(jsonObject.getExpertPromotions().getList(), true);
                        }
                    }

                } else {
                    mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
            }
        }, RecommendationExpertBean.class);


    }

    // 请求头部数据
    private void initHeadData() {


        Map<String, String> mapPrament = new HashMap<>();
        mapPrament.put("userId", AppConstants.register.getUser().getUserId());
        mapPrament.put("expertId", expertId);
        if (MyApp.isLanguage.equals("rCN")) {
            // 如果是中文简体的语言环境
            mapPrament.put("lang", "zh");
        } else if (MyApp.isLanguage.equals("rTW")) {
            // 如果是中文繁体的语言环境
            mapPrament.put("lang", "zh-TW");
        }
        mapPrament.put("timeZone", "8");
        String signs = SignUtils.getSign("/user/expertIntroduct", mapPrament);

        Map<String, String> param = new HashMap<>();
        param.put("userId", AppConstants.register.getUser().getUserId());
        param.put("expertId", expertId);
        param.put("sign", signs);


        VolleyContentFast.requestJsonByGet(BaseURLs.EXPERTINTRODUCT, param, new VolleyContentFast.ResponseSuccessListener<RecomeHeadBean>() {
            @Override
            public void onResponse(RecomeHeadBean jsonObject) {

                if (jsonObject != null) {

                    if (jsonObject.getUserInfo() == null) {

                        mViewHandler.sendEmptyMessage(VIEW_STATUS_NO_DATA);
                        return;
                    }
                    setHeaderDatas(jsonObject.getUserInfo());
                    mViewHandler.sendEmptyMessage(VIEW_STATUS_SUCCESS);
                } else {
                    mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
            }
        }, RecomeHeadBean.class);


    }

    private void initView() {

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.match_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getApplicationContext(), StaticValues.REFRASH_OFFSET_END));

        LayoutInflater layoutInflater = this.getLayoutInflater();
        view = layoutInflater.inflate(R.layout.view_load_more, null);

        //上来加载view
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        loadmore_text = (TextView) view.findViewById(R.id.loadmore_text);


        //暂无数据
        match_no_data_txt = (TextView) findViewById(R.id.match_no_data_txt);

        //网络异常
        match_error_btn = (LinearLayout) findViewById(R.id.match_error_ll);
        findViewById(R.id.match_error_btn).setOnClickListener(this);

        px_line = (LinearLayout) findViewById(R.id.px_line);
        px_line.setVisibility(View.GONE);
        emptyView = View.inflate(this, R.layout.layout_nodata, null);


        TextView public_txt_title = (TextView) findViewById(R.id.public_txt_title);
        public_txt_title.setText(getString(R.string.home_expert_title_name));
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        findViewById(R.id.public_img_back).setOnClickListener(this);
        ex_image = (ImageView) findViewById(R.id.ex_image);
        ex_name = (TextView) findViewById(R.id.ex_name);
        ex_zhong = (TextView) findViewById(R.id.ex_zhong);
        ex_text = (TextView) findViewById(R.id.ex_text);

        ex_recyclerview = (RecyclerView) findViewById(R.id.ex_recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        ex_recyclerview.setLayoutManager(layoutManager);//设置布局管理器
        ex_recyclerview.setNestedScrollingEnabled(false);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);//设置为垂直布局，这也是默认的

        mOnloadingView = getLayoutInflater().inflate(R.layout.onloading, (ViewGroup) ex_recyclerview.getParent(), false);
        mNoLoadingView = getLayoutInflater().inflate(R.layout.nomoredata, (ViewGroup) ex_recyclerview.getParent(), false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.public_img_back:
                finish();
                break;

            case R.id.match_error_btn:
                initData();
                initHeadData();
                break;

            default:
                break;

        }

    }

    /*加载头部数据*/
    public void setHeaderDatas(RecomeHeadBean.UserInfoBean headerDatas) {
        try {
            Glide.with(getApplicationContext()).load(headerDatas.getImageSrc()).into(ex_image);
            ex_name.setText(headerDatas.getNickname());
            ex_zhong.setText(headerDatas.getSkillfulLeague() + "");
            ex_text.setText("\t\t\t\t" + headerDatas.getIntroduce());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        initData();
        initHeadData();
    }
}
