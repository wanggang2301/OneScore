package com.hhly.mlottery.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.InfoCenterAdapter;
import com.hhly.mlottery.bean.infoCenterBean.InfoCenterBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.InfoCenterPW;

import static com.hhly.mlottery.R.id.tv_current_reLoading;

/**
 * 描  述：情报中心列表
 * 作  者：tangrr@13322.com
 * 时  间：2016/9/6
 */
public class InfoCenterActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private Activity mContext;
    private RecyclerView mRecyclerView;
    private TextView tv_data_content;
    private RelativeLayout rl_top;
    private InfoCenterBean mInfoCenterBean;// 测试数据
    private InfoCenterAdapter infoCenterAdapter;

    private int currentIndexDate;
    private InfoCenterPW mInfoCenterPW;
    private LinearLayout ll_error;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView iv_left;
    private ImageView iv_right;
    public FrameLayout fl_mask;
    private View emptyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_center);
        mContext = this;

        initView();
        initDate();
        initEvent();
    }

    private void initEvent() {
        infoCenterAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                int thirdId = mInfoCenterBean.intelligences.get(currentIndexDate).list.get(i).thirdId;
                L.d("xxx", "thirdId: " + thirdId);
                Intent intent = new Intent(mContext, FootballMatchDetailActivityTest.class);
                intent.putExtra(FootballMatchDetailActivityTest.BUNDLE_PARAM_THIRDID, String.valueOf(thirdId));
                intent.putExtra("info_center", 1);
                startActivity(intent);
            }
        });
    }

    private void initDate() {
        mSwipeRefreshLayout.setRefreshing(true);
        // 请求网络数据
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_INFO_CENTER, new VolleyContentFast.ResponseSuccessListener<InfoCenterBean>() {
            @Override
            public void onResponse(InfoCenterBean jsonObject) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (jsonObject != null) {
                    if (jsonObject.result == 200) {

                        mRecyclerView.setVisibility(View.VISIBLE);
                        iv_left.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.VISIBLE);
                        ll_error.setVisibility(View.GONE);

                        mInfoCenterBean = jsonObject;
                        getCurrentDateInfoIndex();

                        if(mInfoCenterBean.intelligences.get(currentIndexDate).list.size() == 0){
                            infoCenterAdapter.setEmptyView(emptyView);
                        }else{
                            infoCenterAdapter.getData().clear();
                            infoCenterAdapter.addData(mInfoCenterBean.intelligences.get(currentIndexDate).list);
                        }

                        tv_data_content.setText(getCurrentDate(mInfoCenterBean.intelligences.get(currentIndexDate).date, mInfoCenterBean.intelligences.get(currentIndexDate).count));

                        mInfoCenterPW = new InfoCenterPW(mContext, mInfoCenterBean.intelligences, currentIndexDate);

                        mInfoCenterBean.intelligences.get(currentIndexDate).isSelect = true;
                    } else {
                        mRecyclerView.setVisibility(View.GONE);
                        ll_error.setVisibility(View.VISIBLE);
                    }
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    ll_error.setVisibility(View.VISIBLE);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mSwipeRefreshLayout.setRefreshing(false);
                iv_left.setVisibility(View.GONE);
                iv_right.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                ll_error.setVisibility(View.VISIBLE);
            }
        }, InfoCenterBean.class);
    }

    private void getCurrentDateInfoIndex() {
        for (int i = 0, len = mInfoCenterBean.intelligences.size(); i < len; i++) {
            if (mInfoCenterBean.currentDate.equals(mInfoCenterBean.intelligences.get(i).date)) {
                currentIndexDate = i;
                return;
            }
        }
    }

    private void initView() {
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        TextView title = (TextView) findViewById(R.id.public_txt_title);
        title.setText(R.string.title_info_center);

        findViewById(R.id.public_img_back).setOnClickListener(this);
        findViewById(R.id.tv_current_reLoading).setOnClickListener(this);

        fl_mask = (FrameLayout) findViewById(R.id.fl_mask);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_left.setOnClickListener(this);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        iv_right.setOnClickListener(this);
        ll_error = (LinearLayout) findViewById(R.id.ll_loading_error);
        rl_top = (RelativeLayout) findViewById(R.id.rl_top);
        tv_data_content = (TextView) findViewById(R.id.tv_data_content);
        tv_data_content.setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);//设置布局管理器
        layoutManager.setOrientation(OrientationHelper.VERTICAL);//设置为垂直布局，这也是默认的

        infoCenterAdapter = new InfoCenterAdapter(mContext, R.layout.activity_info_item, null);
        mRecyclerView.setAdapter(infoCenterAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.info_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(mContext, StaticValues.REFRASH_OFFSET_END));

        emptyView = View.inflate(this,R.layout.layout_nodata,null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back:
                finish();
                break;
            case R.id.tv_data_content:
                mInfoCenterPW.showPopupWindow(rl_top);
                break;
            case tv_current_reLoading:
                initDate();
                break;
            case R.id.iv_left:
                if (currentIndexDate > 0) {
                    currentIndexDate--;
                }
                showSelectInfo(currentIndexDate);
                break;
            case R.id.iv_right:
                if (currentIndexDate < mInfoCenterBean.intelligences.size() - 1) {
                    currentIndexDate++;
                }
                showSelectInfo(currentIndexDate);
                break;
        }
    }

    public void showSelectInfo(int indexDate) {
        currentIndexDate = indexDate;
        infoCenterAdapter.getData().clear();
        if (mInfoCenterBean.intelligences.get(indexDate).count != 0) {
            infoCenterAdapter.addData(mInfoCenterBean.intelligences.get(indexDate).list);
        } else {
            infoCenterAdapter.setEmptyView(emptyView);
            infoCenterAdapter.notifyDataSetChanged();
        }
        tv_data_content.setText(getCurrentDate(mInfoCenterBean.intelligences.get(indexDate).date, mInfoCenterBean.intelligences.get(indexDate).count));

        if (indexDate <= 0) {
            findViewById(R.id.iv_left).setVisibility(View.GONE);
            findViewById(R.id.iv_right).setVisibility(View.VISIBLE);
        } else if (indexDate >= mInfoCenterBean.intelligences.size() - 1) {
            findViewById(R.id.iv_right).setVisibility(View.GONE);
            findViewById(R.id.iv_left).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.iv_left).setVisibility(View.VISIBLE);
            findViewById(R.id.iv_right).setVisibility(View.VISIBLE);
        }
        for (int i = 0, len = mInfoCenterBean.intelligences.size(); i < len; i++) {
            if (i == indexDate) {
                mInfoCenterBean.intelligences.get(i).isSelect = true;
            } else {
                mInfoCenterBean.intelligences.get(i).isSelect = false;
            }
        }
        mInfoCenterPW.notifyChanged(indexDate);
    }

    private String getCurrentDate(String date, int count) {
        if (date == null) {
            return null;
        }
        String weekDate = DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(date));
        return date + " " + weekDate + "(" + count + ")";
    }

    @Override
    public void onRefresh() {
        initDate();
    }
}
