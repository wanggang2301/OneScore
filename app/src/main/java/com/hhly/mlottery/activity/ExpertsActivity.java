package com.hhly.mlottery.activity;

import android.content.Intent;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.ExpertsListAdapter;
import com.hhly.mlottery.bean.MostExpertBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuely198 on 2017/4/12.
 * 专家专栏详情页
 */

public class ExpertsActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private final String EXPERT_ID = "expertId";

    private ImageView ex_image;//专家头像
    private TextView ex_name;//专家名称
    private TextView ex_zhong;
    private TextView ex_text;
    private RecyclerView ex_recyclerview;
    private MostExpertBean.ExpertBean expertDatas;
    private List<MostExpertBean.InfoArrayBean> infoArrayDatas = new ArrayList<>();
    private ExpertsListAdapter expertsListAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView match_no_data_txt;
    private LinearLayout match_error_btn;

    private final static int VIEW_STATUS_LOADING = 11;
    private final static int VIEW_STATUS_SUCCESS = 33;
    private static final int VIEW_STATUS_NET_ERROR = 44;
    private static final int VIEW_STATUS_NO_DATA = 55;

    public final static String INTENT_PARAM_THIRDID = "thirdId";
    public final static String INTENT_PARAM_JUMPURL = "key";
    public final static String INTENT_PARAM_TYPE = "type";
    public final static String INTENT_PARAM_TITLE = "infoTypeName";
    private View headView;
    private String expertId;

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
    private LinearLayout px_line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_new);

        if (getIntent() != null) {
            expertId = getIntent().getStringExtra(EXPERT_ID);
        }

        initView();
        initData();
        intiEvent();

    }

    private void intiEvent() {

        expertsListAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                L.d("experts", "index: " + i);
                gotoWebActivity(i);

            }


        });
    }
    //跳转H5

    private void gotoWebActivity(int i) {

        Intent intent = new Intent(ExpertsActivity.this, WebActivity.class);

        String infoUrl = infoArrayDatas.get(i).getInfoUrl();
        String subTitle = infoArrayDatas.get(i).getSubTitle();
        String picUrl = infoArrayDatas.get(i).getPicUrl();
        String title = infoArrayDatas.get(i).getTitle();
        int thirdId = infoArrayDatas.get(i).getThirdId();
        int matchType = infoArrayDatas.get(i).getMatchType();

        intent.putExtra(INTENT_PARAM_TYPE, matchType == 0 ? 2 : matchType);
        intent.putExtra(INTENT_PARAM_THIRDID, String.valueOf(thirdId));
        intent.putExtra(INTENT_PARAM_TITLE, mContext.getResources().getString(R.string.share_recommend));//头部名称
        intent.putExtra(INTENT_PARAM_JUMPURL, infoUrl);
        intent.putExtra("title", title);
        intent.putExtra("subtitle", subTitle);
        intent.putExtra("imageurl", picUrl);
        intent.putExtra("iscomment", "1");
        startActivity(intent);
    }

    // 请求网络数据
    private void initData() {
        // mSwipeRefreshLayout.setRefreshing(true);
        // mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
        Map<String, String> param = new HashMap<>();
        param.put(EXPERT_ID, expertId);

        VolleyContentFast.requestJsonByGet(BaseURLs.EXPERTS_LIST_URL, param, new VolleyContentFast.ResponseSuccessListener<MostExpertBean>() {
            @Override
            public void onResponse(MostExpertBean jsonObject) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (jsonObject != null) {

                    if (jsonObject.getInfoArray().isEmpty()) {

                        mViewHandler.sendEmptyMessage(VIEW_STATUS_NO_DATA);
                        return;
                    }


                    expertDatas = jsonObject.getExpert();  //获取头部数据

                    if (expertDatas != null) {
                        setHeaderDatas(expertDatas);
                    }
                    infoArrayDatas.clear();
                    infoArrayDatas.addAll(jsonObject.getInfoArray());
                    //填充数据
                    expertsListAdapter.notifyDataSetChanged();
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
        }, MostExpertBean.class);


    }

    private void initView() {

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.match_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getApplicationContext(), StaticValues.REFRASH_OFFSET_END));



        LayoutInflater layoutInflater = this.getLayoutInflater();
        headView = layoutInflater.inflate(R.layout.activity_experts_head_view, null);
        headView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //暂无数据
        match_no_data_txt = (TextView) findViewById(R.id.match_no_data_txt);

        //网络异常
        match_error_btn = (LinearLayout) findViewById(R.id.match_error_ll);
        findViewById(R.id.match_error_btn).setOnClickListener(this);

        px_line = (LinearLayout) findViewById(R.id.px_line);
        px_line.setVisibility(View.GONE);

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

        expertsListAdapter = new ExpertsListAdapter(ExpertsActivity.this, R.layout.activity_expert_child, infoArrayDatas);
        ex_recyclerview.setAdapter(expertsListAdapter);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.public_img_back:
                finish();
                break;

            case R.id.match_error_btn:
                initData();
                break;

            default:
                break;
        }

    }
    /*加载头部数据*/
    public void setHeaderDatas(MostExpertBean.ExpertBean headerDatas) {
        try {
            Glide.with(getApplicationContext()).load(headerDatas.getIcon()).into(ex_image);
            ex_name.setText(headerDatas.getTitle());
            ex_zhong.setText(headerDatas.getLastWeekResults());
            ex_text.setText("\t\t\t\t" + headerDatas.getDes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {

        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewHandler.removeCallbacksAndMessages(null);
    }
}
