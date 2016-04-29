package com.hhly.mlottery.activity;

import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.videolive.PinnedHeaderExpandableAdapter;
import com.hhly.mlottery.bean.videobean.MatchVideoInfo;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 103TJL on 2016/1/13.
 * 直播频道列表
 */
public class LiveChannelActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_live_list);
        InitView();
        InitData();

    }

    public void InitView() {
        ra = new RotateAnimation(0.0f, 360.f, Animation.RELATIVE_TO_SELF, 0.55f, Animation.RELATIVE_TO_SELF, 0.55f);
        ra.setDuration(3000);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.live_swiperefreshlayout);// 数据板块，listview
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(mContext, StaticValues.REFRASH_OFFSET_END));

        live_error_ll = (LinearLayout) findViewById(R.id.live_error_ll);
        live_error_btn = (TextView) findViewById(R.id.live_error_btn);
        live_error_btn.setOnClickListener(this);
        //标题
        public_txt_title = (TextView) findViewById(R.id.public_txt_title);
        //暂无数据
        live_no_data_txt = (TextView) findViewById(R.id.live_no_data_txt);
        //筛选
        public_btn_filter = (ImageView) findViewById(R.id.public_btn_filter);
        public_btn_filter.setVisibility(View.GONE);

        //设置
        public_btn_set = (ImageView) findViewById(R.id.public_btn_set);
        public_btn_set.setVisibility(View.GONE);

        //刷新
        public_live_refresh = (ImageView) findViewById(R.id.public_live_refresh);
        public_live_refresh.setVisibility(View.VISIBLE);
        public_live_refresh.setOnClickListener(this);
        //返回
        public_img_back = (ImageView) findViewById(R.id.public_img_back);
        public_img_back.setOnClickListener(this);
        public_img_back.setImageResource(R.mipmap.number_back_icon);
        //设置标题
        public_txt_title.setText(R.string.frame_home_video_txt);
        //级联列表listview
        explistview_live = (PinnedHeaderExpandableListView) findViewById(R.id.explistview_live);
        //设置悬浮头部VIEW
        explistview_live.setHeaderView(getLayoutInflater().inflate(R.layout.item_live_header, explistview_live, false));
        explistview_live.setChildDivider(getResources().getDrawable(R.color.line_football_footer));
    }



    public void InitData() {
        mViewHandler.sendEmptyMessage(VIEW_STATUS_LOADING);
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_DETAIL_URL_MATCHVIDEO_DATA, new VolleyContentFast.ResponseSuccessListener<MatchVideoInfo>() {
            @Override
            public void onResponse(MatchVideoInfo json) {
                if(pheadapter !=null){//如果adapter不为空，清除数据（刷新的时候）
                    groupDataList=new ArrayList<>() ;
                    childDataList=new ArrayList<>();
                }
                if (json != null) {
                    //前缀
                    String stPreurl = json.getPreurl();
                    //后缀
                    String stFix = json.getFix();
                    groupMatchVideoList= json.getMatchVideo();
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
                            pheadapter = new PinnedHeaderExpandableAdapter(childDataList, groupDataList, mContext, explistview_live, stPreurl, stFix);
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
//        String jsonString="{'fix':'.png','preurl':'http://pic.13322.com/icons/teams/100/tm_logo_','matchVideo':[{'date':'2016-01-21','sptVideoInfoDtoList':[{'matchId':'274477','hmId':'10085','awId':'15156','hometeam':'莱方法是昂','guestteam':'fff搞搞搞','matchDate':'2016-01-21','matchTime':'11:00','racename':'看看','round':'','statusOrigin':'0','channel':[{'name':'QQ一样直播','url':'http://m.13322.com'}]},{'matchId':'276381','hmId':'17382','awId':'1332','hometeam':'分公司','guestteam':'帕丘嗯嗯卡','matchDate':'2016-01-21','matchTime':'11:00','racename':'墨西哥杯','round':'','statusOrigin':'1','channel':[{'name':'QQ直播','url':'http://m.13322.com'}]}]},{'date':'2016-01-22','sptVideoInfoDtoList':[{'matchId':'274477','hmId':'10085','awId':'15156','hometeam':'莱昂','guestteam':'内卡萨','matchDate':'2016-01-21','matchTime':'11:00','racename':'墨西哥杯','round':'','statusOrigin':'0','channel':[{'name':'QQ直播','url':'http://m.13322.com'}]},{'matchId':'276381','hmId':'17382','awId':'1332','hometeam':'塞拉亚','guestteam':'帕丘卡','matchDate':'2016-01-21','matchTime':'11:00','racename':'墨西哥杯','round':'','statusOrigin':'1','channel':[{'name':'QQ直播','url':'http://m.13322.com'}]}]},{'date':'2016-01-23','sptVideoInfoDtoList':[{'matchId':'274477','hmId':'10085','awId':'15156','hometeam':'莱昂','guestteam':'内卡萨','matchDate':'2016-01-21','matchTime':'11:00','racename':'墨西哥杯','round':'','statusOrigin':'1','channel':[{'name':'QQ直播','url':'http://m.13322.com'}]},{'matchId':'276381','hmId':'17382','awId':'1332','hometeam':'塞拉亚','guestteam':'帕丘卡','matchDate':'2016-01-21','matchTime':'11:00','racename':'墨西哥杯','round':'','statusOrigin':'1','channel':[{'name':'QQ直播','url':'http://m.13322.com'}]}]},{'date':'2016-01-24','sptVideoInfoDtoList':[{'matchId':'274477','hmId':'10085','awId':'15156','hometeam':'莱昂','guestteam':'内卡萨','matchDate':'2016-01-21','matchTime':'11:00','racename':'墨西哥杯','round':'','statusOrigin':'1','channel':[{'name':'QQ直播','url':'http://m.13322.com'}]},{'matchId':'276381','hmId':'17382','awId':'1332','hometeam':'塞拉亚','guestteam':'帕丘卡','matchDate':'2016-01-21','matchTime':'11:00','racename':'墨西哥杯','round':'','statusOrigin':'1','channel':[{'name':'QQ直播','url':'http://m.13322.com'}]}]},{'date':'2016-01-25','sptVideoInfoDtoList':[{'matchId':'274477','hmId':'10085','awId':'15156','hometeam':'莱昂dsa','guestteam':'内卡dd萨','matchDate':'2016-01-21','matchTime':'11:00','racename':'墨西a哥杯','round':'','statusOrigin':'0','channel':[{'name':'QQdd直播','url':'http://m.13322.com'}]},{'matchId':'276381','hmId':'17382','awId':'1332','hometeam':'塞拉aaa亚','guestteam':'帕丘sa卡','matchDate':'2016-01-21','matchTime':'11:00','racename':'墨西rrr哥杯','round':'','statusOrigin':'7','channel':[{'name':'QQ00直播','url':'http://m.13322.com'}]}]},{'date':'2016-01-26','sptVideoInfoDtoList':[{'matchId':'274477','hmId':'10085','awId':'15156','hometeam':'莱00昂','guestteam':'内1更改卡萨','matchDate':'2016-01-21','matchTime':'11:00','racename':'墨西哥哈哈杯','round':'','statusOrigin':'2','channel':[{'name':'QQ直播','url':'http://m.13322.com'}]},{'matchId':'276381','hmId':'17382','awId':'1332','hometeam':'塞22额拉亚','guestteam':'帕丘卡','matchDate':'2016-01-21','matchTime':'11:00','racename':'墨西哥杯','round':'','statusOrigin':'1','channel':[{'name':'QQ直方法播','url':'http://m.13322.com'}]}]},{'date':'2016-01-27','sptVideoInfoDtoList':[{'matchId':'274477','hmId':'10085','awId':'15156','hometeam':'莱苏打水昂','guestteam':'内卡随时萨','matchDate':'2016-01-21','matchTime':'11:00','racename':'墨西哥杯','round':'','statusOrigin':'0','channel':[{'name':'QQ直播','url':'http://m.13322.com'}]},{'matchId':'276381','hmId':'17382','awId':'1332','hometeam':'塞拉亚','guestteam':'帕丘卡','matchDate':'2016-01-21','matchTime':'11:00','racename':'墨西哥杯','round':'','statusOrigin':'1','channel':[{'name':'QQ直播','url':'http://m.13322.com'}]}]}]}";
////        String jsonString = "{'fix':'.png','preurl':'http://pic.13322.com/icons/teams/100/tm_logo_','matchVideo':[]}";
//        Gson gson = new Gson();
//
//        MatchVideoInfo json = gson.fromJson(jsonString, MatchVideoInfo.class);
//        //前缀
//        String stPreurl = json.getPreurl();
//        //后缀
//        String stFix = json.getFix();
//        List<MatchVideoInfo.MatchVideoEntity> groupMatchVideoList = json.getMatchVideo();
//        if (groupMatchVideoList.size() == 0) {
//            explistview_live.setVisibility(View.GONE);
//            live_no_data_txt.setVisibility(View.VISIBLE);
//        }else{
//        int mSize = groupMatchVideoList.size();
//        for (int i = 0; i < mSize; i++) {
//            //循环添加父view数据
//            groupDataList.add(groupMatchVideoList.get(i).getDate());
//            //添加子view数据
//            childDataList.add(groupMatchVideoList.get(i).getSptVideoInfoDtoList());
//        }
//        pheadapter = new PinnedHeaderExpandableAdapter(childDataList, groupDataList, mContext, explistview_live, stPreurl, stFix);
//        explistview_live.setAdapter(pheadapter);
//        pheadapter.notifyDataSetChanged();
//        for (int i = 0; i < mSize; i++) {
//            explistview_live.expandGroup(i); //设置 默认打开的 group
//        }
//    }
        explistview_live.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                //选择播放方式
                showChannel_Play_Dialog(childDataList.get(groupPosition).get(childPosition).getChannel());
                return true;
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back://返回
                finish();
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

//    private Runnable mLoadingDataThread = new Runnable() {
//        @Override
//        public void run() {
//            InitData();
//        }
//    };

    /**
     * 选择播放的方式
     */
    private void showChannel_Play_Dialog(final List<MatchVideoInfo.MatchVideoEntity.SptVideoInfoDtoListEntity.ChannelEntity> channelList) {
        dialog_choose_channel_way = new Dialog(this, R.style.MyDialogStyle);
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

        ListView listViews = (ListView) dialog_choose_channel_way.findViewById(R.id.listview);
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
}
