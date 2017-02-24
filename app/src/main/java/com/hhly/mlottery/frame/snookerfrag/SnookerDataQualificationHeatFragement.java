package com.hhly.mlottery.frame.snookerfrag;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.InformationDataAdapter;
import com.hhly.mlottery.adapter.snooker.ChoseHeadInformationAdapter;
import com.hhly.mlottery.adapter.snooker.PinnedHeaderExpandableAdapter;
import com.hhly.mlottery.bean.snookerbean.SnookerRaceListitemBean;
import com.hhly.mlottery.bean.snookerbean.SnookerRefrshBean;
import com.hhly.mlottery.bean.videobean.NewMatchVideoinfo;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.SnookerPinnedHeaderExpandableListView;
import com.hhly.mlottery.widget.GrapeGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by yuely198 on 2017/2/23.
 */

public class SnookerDataQualificationHeatFragement extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    /*标题，暂无数据*/
    private TextView live_no_data_txt;
    /*直播列表*/
    private List<String> groupDataList = new ArrayList<>();
    /*直播子列表*/
    private List<List<String>> childDataList = new ArrayList<>();//直播子列表
    private static final String QUALIFICATIONHEAT = "102";
    private static final String RACEHEAD = "103";
    boolean isAddHeadDatas = false;

    //动画效果
    private RotateAnimation ra;

    private static final int QUALIFICATIONS = 0;  //资格赛
    private static final int RACE = 1;         //正赛
    private static final int SUCCESSIVE = 2;    //历届冠军
    private static final int PROFILE = 3;          //赛事简介

    /*视频直播适配器*/
    private PinnedHeaderExpandableAdapter pheadapter;
    private List<NewMatchVideoinfo.MatchVideoBean> groupMatchVideoList;
    /*加载失败显示的layout*/
    private LinearLayout live_error_ll;
    /*重新加载网络的按钮*/
    private TextView live_error_btn;
    /*下拉刷新*/
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout1;
    private ChoseHeadInformationAdapter choseHeadInformationAdapter;

    List headDatas = new ArrayList<>();

    private Context mContext;
    private Activity mActivity;

    private static final String PARAM_ID = "leagueId";
    private static final String PARAM_TYPE = "type";

    private static final String TYPE_PARM = "TYPE_PARM";
    private int mType;
    private View view;
    private ListView mRecyclerView;
    // private GalleryAdapter galleryAdapter;
    private SnookerPinnedHeaderExpandableListView explistview_live;
    private GrapeGridView head_gridview;
    private TextView snooker_profile;
    private List<SnookerRaceListitemBean.DataBean.StageMapBean.StageInfoBean> stageInfo;
    private SnookerRaceListitemBean.DataBean.StageMapBean stageMap;
    private InformationDataAdapter informationDataAdapter;
    private LinearLayout snooker_race_male_gridview;
    private String currentStage="";
    private String mSeason="";

    public static SnookerDataQualificationHeatFragement newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_PARM, type);
        SnookerDataQualificationHeatFragement footballDatabaseFragment = new SnookerDataQualificationHeatFragement();
        footballDatabaseFragment.setArguments(bundle);
        return footballDatabaseFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(TYPE_PARM);
        }
        //注册EventBus
        EventBus.getDefault().register(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.snooker_race, container, false);
        mContext = mActivity;

        return view;
    }

    //赛事简介数据传输
    public void onEventMainThread(SnookerRefrshBean snookerRefrshBean) {
        mSeason = snookerRefrshBean.getSeason();
        upLeagueRace("", snookerRefrshBean.getSeason());
        isAddHeadDatas=false;
    }

    private void upLeagueRace(String secondTitle, String season) {

        String url = "http://m.1332255.com:81/mlottery/core/snookerData.findLeagueMatchList.do";

        final Map<String, String> map = new HashMap();
        map.put("leagueId", "319655");
        map.put("season", season);//默认不填是当前数据
        map.put("firstTitle", "103");//102.资格赛 103.正赛
        map.put("secondTitle", secondTitle);//联赛列表

        VolleyContentFast.requestJsonByPost(url, map, new VolleyContentFast.ResponseSuccessListener<SnookerRaceListitemBean>() {

            @Override
            public void onResponse(SnookerRaceListitemBean json) {
                if (json == null) {
                    return;
                }
                if (json.getResult() == 200) {

                    headDatas.clear();
                    //获取整体的头部数据
                    stageMap = json.getData().getStageMap();
                    //获取头部数据
                    stageInfo = json.getData().getStageMap().getStageInfo();

                    //获取默认的列赛列表头
                    //currentStage = json.getData().getStageMap().getCurrentStageNum();


                    //获取子列表数据
                    if (json.getData().getMatchList().size() == 0) {
                        explistview_live.setVisibility(View.GONE);
                        live_no_data_txt.setVisibility(View.VISIBLE);
                        live_error_ll.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                        snooker_race_male_gridview.setVisibility(View.GONE);
                        return;
                    } else {
                        live_no_data_txt.setVisibility(View.GONE);
                        live_error_ll.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        explistview_live.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setRefreshing(false);
                        snooker_race_male_gridview.setVisibility(View.VISIBLE);
                    }

                     if (stageInfo==null){

                         snooker_race_male_gridview.setVisibility(View.GONE);
                     }else {

                         for (int i = 0; i < stageInfo.size(); i++) {
                             //添加子头部数据
                             headDatas.add(json.getData().getStageMap().getStageInfo().get(i).getStage());
                         }
                         if (!isAddHeadDatas) {
                             choseHeadInformationAdapter = new ChoseHeadInformationAdapter(mContext, stageInfo, json.getData().getStageMap().getCurrentStageNum(), headDatas, R.layout.snooker_race_header_item);
                             head_gridview.setAdapter(choseHeadInformationAdapter);
                             isAddHeadDatas = true;
                         }
                     }

                    //获取子列表数据
                    for (int i = 0; i < json.getData().getMatchList().size(); i++) {
                        //获取默认赛季时间
                        mSeason=json.getData().getMatchList().get(i).getSeason();
                       // Log.i("aasdas>>>"," mSeason=="+mSeason);
                        //添加子view数据
                        childDataList.add(json.getData().getMatchList().get(i).getDetailedScoreList());
                    }
                    pheadapter = new PinnedHeaderExpandableAdapter(json.getData().getMatchList(), childDataList, mContext, explistview_live);
                    explistview_live.setAdapter(pheadapter);
                    pheadapter.notifyDataSetChanged();

                }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {


                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setVisibility(View.GONE);
                explistview_live.setVisibility(View.GONE);
                live_error_ll.setVisibility(View.VISIBLE);
                live_no_data_txt.setVisibility(View.GONE);
                Toast.makeText(mContext, R.string.exp_net_status_txt, Toast.LENGTH_SHORT).show();

            }
        }, SnookerRaceListitemBean.class);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (ListView) view.findViewById(R.id.header_item_view);

        snooker_race_male_gridview = (LinearLayout) view.findViewById(R.id.snooker_race_male_gridview);

        head_gridview = (GrapeGridView) view.findViewById(R.id.male_gridview);

        head_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                choseHeadInformationAdapter.setSeclection(position);

                currentStage=stageInfo.get(position).getNum()+"";
                Log.i("aasdas>>>","  gridView currentStage=="+currentStage);
                upLeagueRace(stageInfo.get(position).getNum() + "", mSeason);

                choseHeadInformationAdapter.notifyDataSetChanged();
            }
        });
        snooker_profile = (TextView) view.findViewById(R.id.snooker_profile);

         upLeagueRace("", "");

        //级联列表listview
        explistview_live = (SnookerPinnedHeaderExpandableListView) view.findViewById(R.id.explistview_live);
        //设置悬浮头部VIEW

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.live_swiperefreshlayout);// 数据板块，listview
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getActivity(), StaticValues.REFRASH_OFFSET_END));


        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getActivity(), StaticValues.REFRASH_OFFSET_END));

        live_error_ll = (LinearLayout) view.findViewById(R.id.live_error_ll);
        live_error_btn = (TextView) view.findViewById(R.id.live_error_btn);
        live_error_btn.setOnClickListener(this);
        //暂无数据
        live_no_data_txt = (TextView) view.findViewById(R.id.live_no_data_txt);

        live_error_ll = (LinearLayout) view.findViewById(R.id.live_error_ll);
        live_error_btn = (TextView) view.findViewById(R.id.live_error_btn);
        live_error_btn.setOnClickListener(this);
        //暂无数据
        live_no_data_txt = (TextView) view.findViewById(R.id.live_no_data_txt);
    }

    /*下拉刷新*/
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                   reFH();
            }
        }, 500);
    }

    public void reFH(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                upLeagueRace(currentStage, mSeason);
                //Log.i("aasdas>>>","re getNume=="+currentStage);
                //Log.i("aasdas>>>","re mSeason mSeason=="+mSeason);
            }
        }, 500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.live_error_btn:
                reFH();
                break;
            default:
                break;


        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销EventBus
        EventBus.getDefault().unregister(this);
    }
}
