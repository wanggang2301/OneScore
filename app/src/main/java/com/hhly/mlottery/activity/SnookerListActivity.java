package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.snooker.SnookerRecyclerAdapter;
import com.hhly.mlottery.bean.snookerbean.SnookerLeaguesBean;
import com.hhly.mlottery.bean.snookerbean.SnookerListBean;
import com.hhly.mlottery.bean.snookerbean.SnookerMatchesBean;
import com.hhly.mlottery.util.SnookerSettingEvent;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ExactSwipeRefrashLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;


/**
 * Created by yixq on 2016/11/15.
 * mail：yixq@13322.com
 * describe:
 */

public class SnookerListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private SnookerRecyclerAdapter mAdapter;
    private ImageView mFilterImage;
    private ImageView mSettingImage;
    private LinearLayout mErrorLayout;
    private TextView mRefreshText;
    private TextView mNoData;

    //显示状态
    private static final int SHOW_STATUS_LOADING = 1;//加载中
    private static final int SHOW_STATUS_ERROR = 2;//加载失败
    private static final int SHOW_STATUS_NO_DATA = 3;//暂无数据
    private ExactSwipeRefrashLayout mRefresh;

    private ImageView mBackImage;
    private List<SnookerMatchesBean> currentAllData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.snooker_list_activity);

        //注册
        EventBus.getDefault().register(SnookerListActivity.this);

        initView();

        initData();

    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SnookerListActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //网络异常
        mErrorLayout = (LinearLayout) findViewById(R.id.snooker_error_layout);
        //点击刷新
        mRefreshText = (TextView) findViewById(R.id.snooker_reloading_txt);
        mRefreshText.setOnClickListener(this);
        //暂无数据
        mNoData = (TextView) findViewById(R.id.snooker_nodata_txt);

        mRefresh = (ExactSwipeRefrashLayout) findViewById(R.id.snooker_refresh_layout);
        mRefresh.setColorSchemeResources(R.color.tabhost);
        mRefresh.setOnRefreshListener(this);

        //隐藏公共头部多余图标
        mFilterImage = (ImageView) findViewById(R.id.public_btn_filter);
        mFilterImage.setVisibility(View.GONE);

        mBackImage = (ImageView) findViewById(R.id.public_img_back);
        mBackImage.setOnClickListener(this);

        mSettingImage = (ImageView) findViewById(R.id.public_btn_set);
        mSettingImage.setOnClickListener(this);

    }

    private void setStatus(int status) {
        mErrorLayout.setVisibility(status == SHOW_STATUS_ERROR ? View.VISIBLE : View.GONE);
        mNoData.setVisibility(status == SHOW_STATUS_NO_DATA ? View.VISIBLE : View.GONE);

    }


    private void initData() {
        mRefresh.setRefreshing(true);
        String url = "http://192.168.31.33:8080/mlottery/core/snookerMatch.getFirstSnookerMatch.do";
        Map<String, String> map = new HashMap();

        VolleyContentFast.requestJsonByGet(url, new VolleyContentFast.ResponseSuccessListener<SnookerListBean>() {
            @Override
            public void onResponse(SnookerListBean jsonBean) {
                mRefresh.setRefreshing(false);
                List<SnookerLeaguesBean> dataBean = jsonBean.getData().getLeagues();

                //数据加载
                setData(dataBean);
                Toast.makeText(mContext, "yxq>>>>>> " + dataBean.get(0).getDate() + "---" + dataBean.get(0).getName(), Toast.LENGTH_SHORT).show();

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mRefresh.setRefreshing(false);
                Toast.makeText(mContext, "yxq>>>>>> " + "---", Toast.LENGTH_SHORT).show();
            }
        }, SnookerListBean.class);
    }

    private String currentDate = "";
    private String currentLeaguesId = "";

    private void setData(List<SnookerLeaguesBean> dataBean) {

        currentAllData = new ArrayList<>();
        for (int i = 0; i < dataBean.size(); i++) {
            /**
             * Type = 0 类型一， Item == 日期
             */
            if (currentDate.equals("") || !currentDate.equals(dataBean.get(i).getDate())) {

                SnookerMatchesBean mMatchData = new SnookerMatchesBean();
                mMatchData.setItemType(0);
                mMatchData.setItemDate(dataBean.get(i).getDate());
                mMatchData.setItemLeaguesName("--");
                mMatchData.setItemLeaguesId("--");
                currentAllData.add(mMatchData);

                currentDate = dataBean.get(i).getDate();
            }

            /**
             * Type = 1 类型二， Item == 赛事
             */
            if (currentLeaguesId.equals("") || !currentLeaguesId.equals(dataBean.get(i).getLeaguesId())) {

                SnookerMatchesBean mLeaguesData = new SnookerMatchesBean();
                mLeaguesData.setItemType(1);
                mLeaguesData.setItemDate("--");
                mLeaguesData.setItemLeaguesName(dataBean.get(i).getName());
                mLeaguesData.setItemLeaguesId("--");
                currentAllData.add(mLeaguesData);

                currentLeaguesId = dataBean.get(i).getLeaguesId();
            }

            /**
             * Type = 2 类型三， Item == 比赛
             */
            List<SnookerMatchesBean> datas = dataBean.get(i).getMatches();
            for (int j = 0; j < datas.size(); j++) {
                datas.get(j).setItemType(2);
                currentAllData.add(datas.get(j));
            }
        }

        if (mAdapter == null) {
            mAdapter = new SnookerRecyclerAdapter(mContext, currentAllData);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            updateAdapter();
        }
    }

    /**
     * \
     */
    public void updateAdapter() {
        if (mAdapter == null) {
            return;
        }
        mAdapter.updateDatas(currentAllData);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefresh.setRefreshing(false);


            }
        }, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.snooker_reloading_txt:
                MobclickAgent.onEvent(mContext, "Snooker_refresh");
                Toast.makeText(mContext, "点击了刷新···", Toast.LENGTH_SHORT).show();
                break;
            case R.id.public_img_back:
                MobclickAgent.onEvent(mContext, "Snooker_Exit");
                finish();
                break;
            case R.id.public_btn_set:
                MobclickAgent.onEvent(mContext, "Snooker_Setting");

                Intent intent = new Intent(SnookerListActivity.this, SnookerSettingActivity.class);
                startActivity(intent);
                SnookerListActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                break;
        }
    }

    /**
     * 设置返回
     */
    public void onEventMainThread(SnookerSettingEvent snookerSettingEvent) {
        Toast.makeText(mContext, snookerSettingEvent.getmMsg() + " = yxq------", Toast.LENGTH_SHORT).show();
        updateAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册
        EventBus.getDefault().unregister(this);
    }
}
