package com.hhly.mlottery.frame.footframe;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.bean.footballDetails.DataStatisInfo;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 统计
 * Created by asus1 on 2015/12/29.
 */
public class StatisticsFragment extends Fragment {

    private View view;
    private Context mContext;

    private final int ERROR = -1;//访问失败
    private final int SUCCESS = 0;// 访问成功
    private final int STARTLOADING = 300;// 正在加载中


    private FrameLayout fl_cornerTrend_loading;// 正在加载中
    private FrameLayout fl_cornerTrend_networkError;// 访问失败

    private TextView reLoading;// 刷新
    private DataStatisInfo.HomeStatisEntity mHomeStatisEntity;//主队统计数据
    private DataStatisInfo.GuestStatisEntity mGuestStatisEntity;//客队统计数据
    private ProgressBar prohome_team, prohome_trapping, prohome_foul, prohome_offside, prohome_freehit, prohome_lineout;// 主队
    private ProgressBar proguest_team, proguest_trapping, proguest_foul, proguest_offside, proguest_freehit, proguest_lineout;// 客队
    private TextView home_team_txt, home_trapping_txt, home_foul_txt, home_offside_txt, home_freehit_txt, home_lineout_txt;
    private TextView guest_team_txt, guest_trapping_txt, guest_foul_txt, guest_offside_txt, guest_freehit_txt, guest_lineout_txt;//客队射门数
    private int dataSize;
    private ScrollView stadium_scrollview_id;//统计数据滑动的scrollview
    private RelativeLayout layout_match_bottom;//统计数据layout

    public static StatisticsFragment newInstance() {
        StatisticsFragment fragment = new StatisticsFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.statis_trend, container, false);
        mContext = getActivity();
        initView();
        initEvent();
        return view;
    }

    /**
     * 初始化布局
     */
    private void initView() {

        fl_cornerTrend_loading = (FrameLayout) view.findViewById(R.id.fl_cornerTrend_loading);
        fl_cornerTrend_networkError = (FrameLayout) view.findViewById(R.id.fl_cornerTrend_networkError);
        layout_match_bottom = (RelativeLayout) view.findViewById(R.id.layout_match_bottom);
        reLoading = (TextView) view.findViewById(R.id.reLoading);// 刷新
        reLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        //addtjl

        //主（客）队进度条
        prohome_team = (ProgressBar) view.findViewById(R.id.prohome_team);
        proguest_team = (ProgressBar) view.findViewById(R.id.proguest_team);

        prohome_trapping = (ProgressBar) view.findViewById(R.id.prohome_trapping);
        proguest_trapping = (ProgressBar) view.findViewById(R.id.proguest_trapping);

        prohome_foul = (ProgressBar) view.findViewById(R.id.prohome_foul);
        proguest_foul = (ProgressBar) view.findViewById(R.id.proguest_foul);

        prohome_offside = (ProgressBar) view.findViewById(R.id.prohome_offside);
        proguest_offside = (ProgressBar) view.findViewById(R.id.proguest_offside);

        prohome_freehit = (ProgressBar) view.findViewById(R.id.prohome_freeHit);
        proguest_freehit = (ProgressBar) view.findViewById(R.id.proguest_freeHit);

        prohome_lineout = (ProgressBar) view.findViewById(R.id.prohome_lineout);
        proguest_lineout = (ProgressBar) view.findViewById(R.id.proguest_lineout);


        //主（客）队进度数
        home_team_txt = (TextView) view.findViewById(R.id.home_team_txt);
        guest_team_txt = (TextView) view.findViewById(R.id.guest_team_txt);

        home_trapping_txt = (TextView) view.findViewById(R.id.home_trapping_txt);
        guest_trapping_txt = (TextView) view.findViewById(R.id.guest_trapping_txt);

        home_foul_txt = (TextView) view.findViewById(R.id.home_foul_txt);
        guest_foul_txt = (TextView) view.findViewById(R.id.guest_foul_txt);

        home_offside_txt = (TextView) view.findViewById(R.id.home_offside_txt);
        guest_offside_txt = (TextView) view.findViewById(R.id.guest_offside_txt);

        home_freehit_txt = (TextView) view.findViewById(R.id.home_freeHit_txt);
        guest_freehit_txt = (TextView) view.findViewById(R.id.guest_freeHit_txt);

        home_lineout_txt = (TextView) view.findViewById(R.id.home_lineout_txt);
        guest_lineout_txt = (TextView) view.findViewById(R.id.guest_lineout_txt);

        stadium_scrollview_id = (ScrollView) view.findViewById(R.id.stadium_scrollview_id);

        //addtjlend

    }
    private boolean isHttpData = false;// 是否有请求到后台数据
    /**
     * 初始化数据
     */
    public void initData() {
        boolean mStart = StadiumFragment.isStart;// 判断是否完场


        //正在比赛中
        if (!mStart) {
            // 显示统计数据
            fl_cornerTrend_loading.setVisibility(View.GONE);
            fl_cornerTrend_networkError.setVisibility(View.GONE);
            layout_match_bottom.setVisibility(View.VISIBLE);
            initJson(1);
        } else {//完场
            if(!isHttpData){//请求到，执行
                // 请求后台数据
                getVolleyData();
            }

        }
    }

    /**
     * 请求后台数据
     *
     * @return
     */
    public void getVolleyData() {

        L.d("456","总计");
        mHandler.sendEmptyMessage(STARTLOADING);// 正在加载中
        Map<String, String> map = new HashMap<>();
        if (getActivity() == null) {
            return;
        } else {
            map.put("thirdId", ((FootballMatchDetailActivity) getActivity()).mThirdId);

            L.i("dddffdfd");

            VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_DETAIL_STATISTICAL_DATA_INFO, map, new VolleyContentFast.ResponseSuccessListener<DataStatisInfo>() {
                @Override
                public void onResponse(DataStatisInfo json) {
                    if (json != null && "200".equals(json.getResult())) {
                        mHomeStatisEntity = json.getHomeStatis();
                        mGuestStatisEntity = json.getGuestStatis();
                        initJson(-1);//初始化json数据，绑定txt
                        mHandler.sendEmptyMessage(SUCCESS);// 访问成功
                    }
                }
            }, new VolleyContentFast.ResponseErrorListener() {
                @Override
                public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                    L.i("init", "====initFailed===" + exception.getVolleyError().getMessage());
                    mHandler.sendEmptyMessage(ERROR);
                }
            }, DataStatisInfo.class);
        }

    }
    //初始化json数据，绑定txt
    public void initJson(int id) {
        if (id == 1) {//正在比赛
            //射门
            initStatisData(StadiumFragment.mathchStatisInfo.getHome_shoot_door(), 1);
            initStatisData(StadiumFragment.mathchStatisInfo.getGuest_shoot_door(), 10);
            //扑救
            initStatisData(StadiumFragment.mathchStatisInfo.getHome_rescue(), 2);
            initStatisData(StadiumFragment.mathchStatisInfo.getGuest_rescue(), 20);
            //犯规
            initStatisData(StadiumFragment.mathchStatisInfo.getHome_foul(), 3);
            initStatisData(StadiumFragment.mathchStatisInfo.getGuest_foul(), 30);
            //越位
            initStatisData(StadiumFragment.mathchStatisInfo.getHome_away(), 4);
            initStatisData(StadiumFragment.mathchStatisInfo.getGuest_away(), 40);
            //任意球
            initStatisData(StadiumFragment.mathchStatisInfo.getHome_free_kick(), 5);
            initStatisData(StadiumFragment.mathchStatisInfo.getGuest_free_kick(), 50);
            //界外球
            initStatisData(StadiumFragment.mathchStatisInfo.getHome_lineOut(), 6);
            initStatisData(StadiumFragment.mathchStatisInfo.getGuest_lineOut(), 60);

            ///////////////////////////////////////////////////////////
            guest_team_txt.setText(StadiumFragment.mathchStatisInfo.getGuest_shoot_door() + "");
            home_team_txt.setText(StadiumFragment.mathchStatisInfo.getHome_shoot_door() + "");

            guest_trapping_txt.setText(StadiumFragment.mathchStatisInfo.getGuest_rescue() + "");
            home_trapping_txt.setText(StadiumFragment.mathchStatisInfo.getHome_rescue() + "");

            guest_foul_txt.setText(StadiumFragment.mathchStatisInfo.getGuest_foul() + "");
            home_foul_txt.setText(StadiumFragment.mathchStatisInfo.getHome_foul() + "");

            guest_offside_txt.setText(StadiumFragment.mathchStatisInfo.getGuest_away() + "");
            home_offside_txt.setText(StadiumFragment.mathchStatisInfo.getHome_away() + "");

            guest_freehit_txt.setText(StadiumFragment.mathchStatisInfo.getGuest_free_kick() + "");
            home_freehit_txt.setText(StadiumFragment.mathchStatisInfo.getHome_free_kick() + "");

            guest_lineout_txt.setText(StadiumFragment.mathchStatisInfo.getGuest_lineOut() + "");
            home_lineout_txt.setText(StadiumFragment.mathchStatisInfo.getHome_lineOut() + "");


        } else if (id == -1) {//完场
            //射门
            initStatisData(mHomeStatisEntity.getAllShots(), 1);
            initStatisData(mGuestStatisEntity.getAllShots(), 10);
            //扑救
            initStatisData(mHomeStatisEntity.getTrapping(), 2);
            initStatisData(mGuestStatisEntity.getTrapping(), 20);
            //犯规
            initStatisData(mHomeStatisEntity.getFoul(), 3);
            initStatisData(mGuestStatisEntity.getFoul(), 30);
            //越位
            initStatisData(mHomeStatisEntity.getOffside(), 4);
            initStatisData(mGuestStatisEntity.getOffside(), 40);
            //任意球
            initStatisData(mHomeStatisEntity.getFreeHit(), 5);
            initStatisData(mGuestStatisEntity.getFreeHit(), 50);
            //界外球
            initStatisData(mHomeStatisEntity.getLineOut(), 6);
            initStatisData(mGuestStatisEntity.getLineOut(), 60);

            home_team_txt.setText(mHomeStatisEntity.getAllShots() + "");
            guest_team_txt.setText(mGuestStatisEntity.getAllShots() + "");

            home_trapping_txt.setText(mHomeStatisEntity.getTrapping() + "");
            guest_trapping_txt.setText(mGuestStatisEntity.getTrapping() + "");

            home_foul_txt.setText(mHomeStatisEntity.getFoul() + "");
            guest_foul_txt.setText(mGuestStatisEntity.getFoul() + "");

            guest_offside_txt.setText(mGuestStatisEntity.getOffside() + "");
            home_offside_txt.setText(mHomeStatisEntity.getOffside() + "");

            guest_freehit_txt.setText(mGuestStatisEntity.getFreeHit() + "");
            home_freehit_txt.setText(mHomeStatisEntity.getFreeHit() + "");


            guest_lineout_txt.setText(mGuestStatisEntity.getLineOut() + "");
            home_lineout_txt.setText(mHomeStatisEntity.getLineOut() + "");

        }
    }

    /**
     * 得到统计数据展现
     */
    public void initStatisData(final int data, final int number) {
        switch (number) {
            case 1://射门
                if (data % 2 == 0) {
                    dataSize = data;
                    dataSize = dataSize * 10;
                    findData(dataSize, 1);
                } else if (data % 2 == 1) {
                    dataSize = data + 1;
                    dataSize = dataSize * 10;
                    findData(dataSize, 1);
                }
                break;
            case 10:
                if (data % 2 == 0) {
                    dataSize = data;
                    dataSize = dataSize * 10;
                    findData(dataSize, 10);
                } else if (data % 2 == 1) {
                    int dataSize = data + 1;
                    dataSize = dataSize * 10;
                    findData(dataSize, 10);
                }
                break;
            case 2://扑救
                dataSize = data;
                dataSize = dataSize * 10;
                findData(dataSize, 2);
                break;
            case 20:
                dataSize = data;
                dataSize = dataSize * 10;
                findData(dataSize, 20);
                break;
            case 3://犯规
                if (data % 2 == 0) {
                    dataSize = data;
                    dataSize = dataSize * 10;
                    findData(dataSize, 3);
                } else if (data % 2 == 1) {
                    int dataSize = data + 1;
                    dataSize = dataSize * 10;
                    findData(dataSize, 3);
                }
                break;
            case 30:
                if (data % 2 == 0) {
                    dataSize = data;
                    dataSize = dataSize * 10;
                    findData(dataSize, 30);
                } else if (data % 2 == 1) {
                    int dataSize = data + 1;
                    dataSize = dataSize * 10;
                    findData(dataSize, 30);
                }
                break;
            case 4://越位
                dataSize = data;
                dataSize = dataSize * 10;
                findData(dataSize, 4);
                break;
            case 40:
                dataSize = data;
                dataSize = dataSize * 10;
                findData(dataSize, 40);
                break;
            case 5://任意球
                if (data % 2 == 0) {
                    dataSize = data;
                    dataSize = dataSize * 10;
                    findData(dataSize, 5);
                } else if (data % 2 == 1) {
                    int dataSize = data + 1;
                    dataSize = dataSize * 10;
                    findData(dataSize, 5);
                }
                break;
            case 50:
                if (data % 2 == 0) {
                    dataSize = data;
                    dataSize = dataSize * 10;
                    findData(dataSize, 50);
                } else if (data % 2 == 1) {
                    int dataSize = data + 1;
                    dataSize = dataSize * 10;
                    findData(dataSize, 50);
                }
                break;
            case 6://界外球
                if (data % 3 == 0) {
                    dataSize = data;
                    dataSize = dataSize * 10;
                    findData(dataSize, 6);
                } else if (data % 3 == 2) {
                    int dataSize = data + 1;
                    dataSize = dataSize * 10;
                    findData(dataSize, 6);
                } else if (data % 3 == 1) {
                    int dataSize = data + 2;
                    dataSize = dataSize * 10;
                    findData(dataSize, 6);
                }
                break;
            case 60:
                if (data % 3 == 0) {
                    dataSize = data;
                    dataSize = dataSize * 10;
                    findData(dataSize, 60);
                } else if (data % 3 == 2) {
                    int dataSize = data + 1;
                    dataSize = dataSize * 10;
                    findData(dataSize, 60);
                } else if (data % 3 == 1) {
                    int dataSize = data + 2;
                    dataSize = dataSize * 10;
                    findData(dataSize, 60);
                }
                break;
            default:
                break;

        }
    }
    /**
     * 根据判断增加进度条传值
     *
     * @param dataNum
     */
    public void findData(final int dataNum, final int messNumber) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i <= dataNum; i++) {
                    Message message = Message.obtain();
                    message.what = messNumber;
                    message.obj = i;
                    if (message == null | mHandler == null) {
                        return;
                    } else {
                        mHandler.sendMessage(message);
                    }

//                    try {
//                        Thread.sleep(6);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }

                }
            }
        }, 0);

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case STARTLOADING:// 正在加载中
                    fl_cornerTrend_loading.setVisibility(View.VISIBLE);
                    fl_cornerTrend_networkError.setVisibility(View.GONE);
                    layout_match_bottom.setVisibility(View.GONE);
                    break;
                case SUCCESS:// 访问成功
                    fl_cornerTrend_loading.setVisibility(View.GONE);
                    fl_cornerTrend_networkError.setVisibility(View.GONE);
                    layout_match_bottom.setVisibility(View.VISIBLE);
                    isHttpData = true;

                    break;
                case ERROR:// 访问失败
                    fl_cornerTrend_loading.setVisibility(View.GONE);
                    fl_cornerTrend_networkError.setVisibility(View.VISIBLE);
                    layout_match_bottom.setVisibility(View.GONE);
                    isHttpData = false;
                    break;
                case 1://主队射门
                    int p1 = (Integer) msg.obj;

                    prohome_team.setProgress(p1);

                    break;
                case 10://客队射门
                    int p10 = (Integer) msg.obj;
                    proguest_team.setProgress(p10);
                    break;
                case 2://主队扑救
                    int p2 = (Integer) msg.obj;
                    prohome_trapping.setProgress(p2);

                    break;
                case 20://客队扑救
                    int p20 = (Integer) msg.obj;
                    proguest_trapping.setProgress(p20);

                    break;
                case 3://主队犯规
                    int p3 = (Integer) msg.obj;
                    prohome_foul.setProgress(p3);

                    break;
                case 30://客队犯规
                    int p30 = (Integer) msg.obj;
                    proguest_foul.setProgress(p30);

                    break;
                case 4://主队越位
                    int p4 = (Integer) msg.obj;
                    prohome_offside.setProgress(p4);

                    break;
                case 40://客队越位
                    int p40 = (Integer) msg.obj;
                    proguest_offside.setProgress(p40);

                    break;
                case 5://主队任意球
                    int p5 = (Integer) msg.obj;
                    prohome_freehit.setProgress(p5);

                    break;
                case 50://客队任意球
                    int p50 = (Integer) msg.obj;
                    proguest_freehit.setProgress(p50);

                    break;
                case 6://主队界外球
                    int p6 = (Integer) msg.obj;
                    prohome_lineout.setProgress(p6);

                    break;
                case 60://客队界外球
                    int p60 = (Integer) msg.obj;
                    proguest_lineout.setProgress(p60);

                    break;
                case 100://正在比赛
                    initJson(1);
                    break;
                case 200://完场
                    getVolleyData();//请求json
                    break;
                default:
                    break;
            }
        }
    };
    public void initEvent() {
        stadium_scrollview_id.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        //处于顶部
                        if (stadium_scrollview_id.getScrollY() != 0) {
                            ((FootballMatchDetailActivity) getActivity()).mRefreshLayout.setEnabled(false);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        ((FootballMatchDetailActivity) getActivity()).mRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });
    }



}
