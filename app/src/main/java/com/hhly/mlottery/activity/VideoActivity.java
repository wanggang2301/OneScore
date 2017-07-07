package com.hhly.mlottery.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.videolive.NewPinnedHeaderExpandableAdapter;
import com.hhly.mlottery.bean.videobean.NewMatchVideoinfo;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuely198 on 2017/3/21.
 */

public class VideoActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    /*标题，暂无数据*/
    private TextView live_no_data_txt;
    /*直播列表*/
    private List<String> groupDataList = new ArrayList<>();
    /*直播子列表*/
    private List<List<NewMatchVideoinfo.MatchVideoBean.SptVideoMoreInfoDtoListBean>> childDataList = new ArrayList<>();//直播子列表
    /*选择直播dialog*/
    private Dialog dialog_choose_channel_way;
    //动画效果
    private RotateAnimation ra;
    /*视频直播列表*/
    private PinnedHeaderExpandableListView explistview_live;
    /*视频直播适配器*/
    private NewPinnedHeaderExpandableAdapter pheadapter;
    private List<NewMatchVideoinfo.MatchVideoBean> groupMatchVideoList;
    /*加载失败显示的layout*/
    private LinearLayout live_error_ll;
    /*重新加载网络的按钮*/
    private TextView live_error_btn;
    /*下拉刷新*/
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private final static int VIEW_STATUS_LOADING = 1;

    private ListView listViews;
    private static final String FRAGMENT_INDEX = "fragment_index";
    private Context mContext;
    private TextView mPublic_txt_title;
    /*定时刷新   60s*/
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            initData();
            mViewHandler.postDelayed(this, 1000 * 60);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_list);
        initView();
        initData();
        mContext = this;

    }



    @Override
    public void onResume() {
        super.onResume();
        mViewHandler.postDelayed(mRunnable, 1000 * 60);
    }


    private void initView() {
        ra = new RotateAnimation(0.0f, 360.f, Animation.RELATIVE_TO_SELF, 0.55f, Animation.RELATIVE_TO_SELF, 0.55f);
        ra.setDuration(3000);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.live_swiperefreshlayout);// 数据板块，listview
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mPublic_txt_title = (TextView) findViewById(R.id.public_txt_title);
        mPublic_txt_title.setText(getString(R.string.direct_seeding));

        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        //返回键
        findViewById(R.id.public_img_back).setOnClickListener(this);

        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(VideoActivity.this, StaticValues.REFRASH_OFFSET_END));

        live_error_ll = (LinearLayout) findViewById(R.id.live_error_ll);
        live_error_btn = (TextView) findViewById(R.id.live_error_btn);
        live_error_btn.setOnClickListener(this);

        //级联列表listview
        explistview_live = (PinnedHeaderExpandableListView) findViewById(R.id.explistview_live);
        //设置悬浮头部VIEW

        explistview_live.setHeaderView(getLayoutInflater().inflate(R.layout.item_live_header, explistview_live, false));
        explistview_live.setChildDivider(getResources().getDrawable(R.color.line_football_footer));
        //暂无数据
        live_no_data_txt = (TextView) findViewById(R.id.live_no_data_txt);

    }

    public void initData() {
        mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);

        //String url = "http://192.168.10.242:8181/mlottery/core/matchVideo.findAndroidVideoinfo.do";
        Map<String, String> myPostParams = new HashMap<>();
        //第二次请求需要日期
        myPostParams.put("liveType", "0");

        VolleyContentFast.requestJsonByGet(BaseURLs.VIDEOINFO, myPostParams, new VolleyContentFast.ResponseSuccessListener<NewMatchVideoinfo>() {
            @Override
            public void onResponse(NewMatchVideoinfo json) {
                if (pheadapter != null) {//如果adapter不为空，清除数据（刷新的时候）
                    groupDataList = new ArrayList<>();
                    childDataList = new ArrayList<>();
                }
                if (json != null) {
                    if (json.matchVideo != null) {
                        //前缀
                        String stPreurl = json.preurl;
                        //后缀
                        String stFix = json.fix;
                        groupMatchVideoList = json.matchVideo;
                        if (groupMatchVideoList.size() == 0) {
                            explistview_live.setVisibility(View.GONE);
                            live_no_data_txt.setVisibility(View.VISIBLE);
                            live_error_ll.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setRefreshing(false);
                        } else {

                            live_no_data_txt.setVisibility(View.GONE);
                            live_error_ll.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            explistview_live.setVisibility(View.VISIBLE);
                            mSwipeRefreshLayout.setRefreshing(false);
                            int mSize = groupMatchVideoList.size();

                            for (int i = 0; i < mSize; i++) {
                                //循环添加父view数据
                                groupDataList.add(groupMatchVideoList.get(i).date);
                                //添加子view数据
                                childDataList.add(groupMatchVideoList.get(i).sptVideoMoreInfoDtoList);

                            }
                            for (int j = 0; j < groupMatchVideoList.size(); j++) {
                                for (int k = 0; k < groupMatchVideoList.get(j).sptVideoMoreInfoDtoList.size(); k++) {
                                    NewMatchVideoinfo.MatchVideoBean.SptVideoMoreInfoDtoListBean bean = groupMatchVideoList.get(j).sptVideoMoreInfoDtoList.get(k);

                                    if ("0".equals(bean.displayType)) {   //对战
                                        if ("0".equals(bean.statusOrigin)) {//没直播
                                            childDataList.get(j).get(k).liveAndBFZ = 8;
                                        } else if (!"0".equals(bean.statusOrigin)) {//直播中
                                            childDataList.get(j).get(k).liveAndBFZ = 6;
                                        }

                                    } else if ("1".equals(bean.displayType)) {   //非对战
                                        if ("0".equals(bean.statusOrigin)) {//没直播
                                            childDataList.get(j).get(k).liveAndBFZ = 2;
                                        } else if (!"0".equals(bean.statusOrigin)) {//直播中
                                            childDataList.get(j).get(k).liveAndBFZ = 4;
                                        }
                                    }
                                }
                            }
                            pheadapter = new NewPinnedHeaderExpandableAdapter(childDataList, groupDataList, mContext, explistview_live, stPreurl, stFix);
                            explistview_live.setAdapter(pheadapter);
                            pheadapter.notifyDataSetChanged();
                            for (int i = 0; i < mSize; i++) {
                                explistview_live.expandGroup(i); //设置 默认打开的 group
                            }
                        }
                    }
                }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                L.i("tjl", "====initFailed===错了" + exception.getErrorCode());
                mViewHandler.removeCallbacks(mRunnable);
                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setVisibility(View.GONE);
                explistview_live.setVisibility(View.GONE);
                live_error_ll.setVisibility(View.VISIBLE);
                live_no_data_txt.setVisibility(View.GONE);
                Toast.makeText(mContext, R.string.exp_net_status_txt, Toast.LENGTH_SHORT).show();

            }
        }, NewMatchVideoinfo.class);
        explistview_live.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //如果点击间隔在500毫秒不让点击
                if (UiUtils.onDoubClick()) {
                    //选择播放方式
                    showChannel_Play_Dialog(childDataList.get(groupPosition).get(childPosition).channel);
                }
                return true;
            }
        });

    }

    /**
     * 选择播放的方式
     *
     * @param channelList
     */
    private void showChannel_Play_Dialog(final List<NewMatchVideoinfo.MatchVideoBean.SptVideoMoreInfoDtoListBean.ChannelBean> channelList) {
        dialog_choose_channel_way = new Dialog(mContext, R.style.MyDialogStyle);
        dialog_choose_channel_way.setContentView(R.layout.dialog_layout);
        dialog_choose_channel_way.setCanceledOnTouchOutside(true);

        List<Map<String, String>> nameList = new ArrayList<>();// 建立一个数组存储listview上显示的数据
        for (int m = 0; m < channelList.size(); m++) {// initData为一个list类型的数据源
            Map<String, String> nameMap = new HashMap<>();
            nameMap.put("name", channelList.get(m).name);
            nameMap.put("url", channelList.get(m).url);
            nameList.add(nameMap);
        }

        SimpleAdapter adapter = new SimpleAdapter(mContext, nameList, R.layout.item_dialog_layout, new String[]{"name"}, new int[]{R.id.dialog_txt_cancel_list});

        listViews = (ListView) dialog_choose_channel_way.findViewById(R.id.listview);
        listViews.setAdapter(adapter);
        listViews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, PlayWebViewActivity.class);
                intent.putExtra("url", channelList.get(position).url);
                startActivity(intent);
                dialog_choose_channel_way.cancel();
            }
        });
        //点击view关闭dialog
        dialog_choose_channel_way.findViewById(R.id.dialog_other_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_choose_channel_way.cancel();
            }
        });
        //点击取消关闭dialog
        dialog_choose_channel_way.findViewById(R.id.dialog_btn_cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog_choose_channel_way.cancel();
            }
        });
        dialog_choose_channel_way.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.live_swiperefreshlayout://刷新网络
/*                public_live_refresh.setAnimation(ra);
                public_live_refresh.startAnimation(ra);*/
//                InitData();
                reFH();
                break;
            case R.id.live_error_btn://刷新网络
//                mLoadHandler.post(mLoadingDataThread);
                reFH();
                break;

            case R.id.public_img_back:
                 finish();
                break;
            default:
                break;

        }
    }

    @Override
    public void onRefresh() {
        reFH();
    }

    /*定时刷新UI*/


    private Handler mViewHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VIEW_STATUS_LOADING:
                    mSwipeRefreshLayout.setRefreshing(true);
                    break;

            }
        }

    };

    public void reFH() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }, 500);
    }


    @Override
    public void onPause() {
        super.onPause();
        mViewHandler.removeCallbacks(mRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewHandler.removeCallbacksAndMessages(null);
    }
}
