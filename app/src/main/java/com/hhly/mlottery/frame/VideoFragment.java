package com.hhly.mlottery.frame;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballActivity;
import com.hhly.mlottery.activity.PlayWebViewActivity;
import com.hhly.mlottery.adapter.videolive.PinnedHeaderExpandableAdapter;
import com.hhly.mlottery.bean.videobean.MatchVideoInfo;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.PinnedHeaderExpandableListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus1 on 2016/3/29.
 * 视屏
 */
public class VideoFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private Context mContext;
    private View mView;

    private ImageView public_img_back, public_btn_filter, public_btn_set, public_live_refresh;
    private TextView public_txt_title, live_no_data_txt;//标题，暂无数据

    Handler mLoadHandler = new Handler();


    private List<String> groupDataList = new ArrayList<>();//直播列表
    private List<List<MatchVideoInfo.MatchVideoEntity.SptVideoInfoDtoListEntity>> childDataList = new ArrayList<>();//直播子列表
    // 选择直播dialog
    private Dialog dialog_choose_channel_way;
    //动画效果
    private RotateAnimation ra;
    private PinnedHeaderExpandableListView explistview_live;//视频直播列表
    private PinnedHeaderExpandableAdapter pheadapter;//视频直播适配器

    private  List<MatchVideoInfo.MatchVideoEntity> groupMatchVideoList;
    private LinearLayout live_error_ll;//加载失败显示的layout
    private TextView live_error_btn;//重新加载网络的按钮
    private SwipeRefreshLayout mSwipeRefreshLayout;// 下拉刷新
    private final static int VIEW_STATUS_LOADING = 1;

    private TextView public_txt_left_title;
    private ListView listViews;

    @SuppressLint("ValidFragment")
    public VideoFragment(Context context) {
        this.mContext=context;
    }

    public VideoFragment(){}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_live_list, container, false);

        InitView();
        InitData();
        return mView;
    }



    public void InitView() {
        ra = new RotateAnimation(0.0f, 360.f, Animation.RELATIVE_TO_SELF, 0.55f, Animation.RELATIVE_TO_SELF, 0.55f);
        ra.setDuration(3000);
        mSwipeRefreshLayout = (SwipeRefreshLayout)mView.findViewById(R.id.live_swiperefreshlayout);// 数据板块，listview
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getActivity(), StaticValues.REFRASH_OFFSET_END));

        live_error_ll = (LinearLayout)mView.findViewById(R.id.live_error_ll);
        live_error_btn = (TextView)mView.findViewById(R.id.live_error_btn);
        live_error_btn.setOnClickListener(this);
        //标题
        public_txt_title = (TextView)mView.findViewById(R.id.public_txt_title);

        public_txt_left_title=(TextView)mView.findViewById(R.id.public_txt_left_title);

        //暂无数据
        live_no_data_txt = (TextView)mView.findViewById(R.id.live_no_data_txt);
        //筛选
        public_btn_filter = (ImageView)mView.findViewById(R.id.public_btn_filter);
        public_btn_filter.setVisibility(View.GONE);

        //设置
        public_btn_set = (ImageView)mView.findViewById(R.id.public_btn_set);
        public_btn_set.setVisibility(View.GONE);

        //刷新
        public_live_refresh = (ImageView)mView.findViewById(R.id.public_live_refresh);
        public_live_refresh.setVisibility(View.GONE);
        public_live_refresh.setOnClickListener(this);

        //返回
        public_img_back = (ImageView) mView.findViewById(R.id.public_img_back);
        public_img_back.setOnClickListener(this);
        //设置标题
       // public_txt_left_title.setText(R.string.frame_home_video_txt);
       // public_txt_left_title.setVisibility(View.VISIBLE);
        public_txt_title.setText(R.string.frame_home_video_txt);


        //级联列表listview
        explistview_live = (PinnedHeaderExpandableListView)mView.findViewById(R.id.explistview_live);
        //设置悬浮头部VIEW

        explistview_live.setHeaderView(getLayoutInflater(null).inflate(R.layout.item_live_header, explistview_live, false));
        explistview_live.setChildDivider(getResources().getDrawable(R.color.line_football_footer));
    }



    public void InitData() {
        mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_DETAIL_URL_MATCHVIDEO_DATA, new VolleyContentFast.ResponseSuccessListener<MatchVideoInfo>() {
            @Override
            public void onResponse(MatchVideoInfo json) {
                if (pheadapter != null) {//如果adapter不为空，清除数据（刷新的时候）
                    groupDataList = new ArrayList<>();
                    childDataList = new ArrayList<>();
                }
                if (json != null) {
                    //前缀
                    String stPreurl = json.getPreurl();
                    //后缀
                    String stFix = json.getFix();
                    groupMatchVideoList = json.getMatchVideo();
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
                            groupDataList.add(groupMatchVideoList.get(i).getDate());
                            //添加子view数据
                            childDataList.add(groupMatchVideoList.get(i).getSptVideoInfoDtoList());
                        }
                       // pheadapter = new PinnedHeaderExpandableAdapter(childDataList, groupDataList, mContext, explistview_live, stPreurl, stFix);
                        explistview_live.setAdapter(pheadapter);
                        pheadapter.notifyDataSetChanged();
                        for (int i = 0; i < mSize; i++) {
                            explistview_live.expandGroup(i); //设置 默认打开的 group
                        }

                    }

                }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                L.i("tjl", "====initFailed===错了" + exception.getErrorCode());

                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setVisibility(View.GONE);
                explistview_live.setVisibility(View.GONE);
                live_error_ll.setVisibility(View.VISIBLE);
                live_no_data_txt.setVisibility(View.GONE);
                Toast.makeText(mContext, R.string.exp_net_status_txt, Toast.LENGTH_SHORT).show();

            }
        }, MatchVideoInfo.class);
        explistview_live.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                //如果点击间隔在500毫秒不让点击
                if(UiUtils.onDoubClick()) {
                    //选择播放方式
                    showChannel_Play_Dialog(childDataList.get(groupPosition).get(childPosition).getChannel());
                }
                return true;
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back://返回
                ((FootballActivity) getActivity()).finish();

                break;
            case R.id.public_live_refresh://刷新网络
                public_live_refresh.setAnimation(ra);
                public_live_refresh.startAnimation(ra);
//                InitData();
                reFH();
                break;
            case R.id.live_error_btn://刷新网络
//                mLoadHandler.post(mLoadingDataThread);
                reFH();
                break;
            default:
                break;

        }
    }
    /**
     * 选择播放的方式
     */
    private void showChannel_Play_Dialog(final List<MatchVideoInfo.MatchVideoEntity.SptVideoInfoDtoListEntity.ChannelEntity> channelList) {
        dialog_choose_channel_way = new Dialog(mContext, R.style.MyDialogStyle);
        dialog_choose_channel_way.setContentView(R.layout.dialog_layout);
        dialog_choose_channel_way.setCanceledOnTouchOutside(true);

        List<Map<String, String>> nameList = new ArrayList<>();// 建立一个数组存储listview上显示的数据
        for (int m = 0; m < channelList.size(); m++) {// initData为一个list类型的数据源
            Map<String, String> nameMap = new HashMap<>();
            nameMap.put("name", channelList.get(m).getName());
            nameMap.put("url", channelList.get(m).getUrl());
            nameList.add(nameMap);
        }

        SimpleAdapter adapter = new SimpleAdapter(mContext, nameList, R.layout.item_dialog_layout, new String[]{"name"}, new int[]{R.id.dialog_txt_cancel_list});

        listViews = (ListView) dialog_choose_channel_way.findViewById(R.id.listview);
        listViews.setAdapter(adapter);
        listViews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, PlayWebViewActivity.class);
                intent.putExtra("url", channelList.get(position).getUrl());
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
    public void onRefresh() {
        reFH();
    }
    public void reFH(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InitData();
            }
        }, 500);
    }

    private Handler mViewHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VIEW_STATUS_LOADING:
                    mSwipeRefreshLayout.setRefreshing(true);
                    break;

            }
        }

    };
    private boolean isHidden;// 当前Fragment是否显示
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        if (hidden) {
            MobclickAgent.onPageEnd("VideoFragment");
        } else {
            MobclickAgent.onPageStart("VideoFragment");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("VideoFragment");
        L.d("video","onResume...");
    }

    @Override
    public void onPause() {
        super.onPause();
        if(!isHidden){
            MobclickAgent.onPageEnd("VideoFragment");
        }
    }
}




