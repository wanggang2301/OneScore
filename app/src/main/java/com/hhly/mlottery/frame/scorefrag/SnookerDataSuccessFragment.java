package com.hhly.mlottery.frame.scorefrag;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.snooker.SnookerSuccessPinnedHeaderExpandleAdapter;
import com.hhly.mlottery.bean.snookerbean.SnookerSuccessBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.SnookerPinnedHeaderExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by yuely198 on 2017/2/28.
 * 斯诺克赛事内页 历届冠军
 */

public class SnookerDataSuccessFragment extends Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{
        /*标题，暂无数据*/
        private TextView live_no_data_txt;

        private List<List<?>> childDataList = new ArrayList<>();//直播子列表



        /*视频直播适配器*/
        private SnookerSuccessPinnedHeaderExpandleAdapter pheadapter;
        /*加载失败显示的layout*/
        private LinearLayout live_error_ll;
        /*重新加载网络的按钮*/
        private TextView live_error_btn;
        /*下拉刷新*/
        private SwipeRefreshLayout mSwipeRefreshLayout;


        private Context mContext;
        private Activity mActivity;

        private static final String PARAM_ID = "leagueId";
        private static final String PARAM_TYPE = "type";

        private static final String TYPE_PARM = "TYPE_PARM";
        private int mType;
        private String mLeagueId;
        private View view;
        // private GalleryAdapter galleryAdapter;
        private SnookerPinnedHeaderExpandableListView explistview_live;

        private SegmentedGroup segmented5;
        private HorizontalScrollView snooker_race_male_gridview;
        private LinearLayout snooker_race_time_head;
        private String currentStage = "";
        private TextView live_pr_no_data_txt;
        private TextView lay_agendafg;
    private TextView snooker_day;
    private TextView snooker_time;

    public static SnookerDataSuccessFragment newInstance(int type, String leagueId) {
            Bundle bundle = new Bundle();
            bundle.putInt(TYPE_PARM, type);
            bundle.putString(PARAM_ID, leagueId);
            SnookerDataSuccessFragment footballDatabaseFragment = new SnookerDataSuccessFragment();
            footballDatabaseFragment.setArguments(bundle);
            return footballDatabaseFragment;

        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mType = getArguments().getInt(TYPE_PARM);
                mLeagueId = getArguments().getString(PARAM_ID);
            }
            upLeagueRace();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            view = inflater.inflate(R.layout.snooker_race, container, false);
            mContext = mActivity;
            return view;
        }
        private void upLeagueRace() {

            final Map<String, String> map = new HashMap();
            map.put("leagueId", mLeagueId);


            VolleyContentFast.requestJsonByPost(BaseURLs.SNOOKER_FINDPREVIOUSWINNERS, map, new VolleyContentFast.ResponseSuccessListener<SnookerSuccessBean>() {

                @Override
                public void onResponse(SnookerSuccessBean json) {
                    if (json == null) {
                        return;
                    }
                    if (json.getResult() == 200) {

                        //获取子列表数据
                        if (json.getData().size() == 0) {
                            explistview_live.setVisibility(View.GONE);
                            live_no_data_txt.setVisibility(View.VISIBLE);
                            live_error_ll.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setRefreshing(false);
                            snooker_race_time_head.setVisibility(View.GONE);
                            live_pr_no_data_txt.setVisibility(View.GONE);
                            lay_agendafg.setVisibility(View.GONE);
                            return;
                        } else {
                            live_pr_no_data_txt.setVisibility(View.GONE);
                            live_no_data_txt.setVisibility(View.GONE);
                            live_error_ll.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            explistview_live.setVisibility(View.VISIBLE);
                            mSwipeRefreshLayout.setRefreshing(false);
                            snooker_race_time_head.setVisibility(View.VISIBLE);
                            lay_agendafg.setVisibility(View.VISIBLE);
                        }
                        //获取子列表数据
                        for (int i = 0; i < json.getData().size(); i++) {
                            //添加子view数据
                            childDataList.add(json.getData().get(i).getDetailedScoreList());
                        }
                        pheadapter = new SnookerSuccessPinnedHeaderExpandleAdapter(json.getData(), childDataList, mContext, explistview_live);
                        explistview_live.setAdapter(pheadapter);
                        pheadapter.notifyDataSetChanged();
                    }
                }
            }, new VolleyContentFast.ResponseErrorListener() {
                @Override
                public void onErrorResponse(VolleyContentFast.VolleyException exception) {

                    live_pr_no_data_txt.setVisibility(View.GONE);
                    snooker_race_time_head.setVisibility(View.GONE);
                    lay_agendafg.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    explistview_live.setVisibility(View.GONE);
                    live_error_ll.setVisibility(View.VISIBLE);
                    live_no_data_txt.setVisibility(View.GONE);
                    Toast.makeText(mContext, R.string.exp_net_status_txt, Toast.LENGTH_SHORT).show();

                }
            }, SnookerSuccessBean.class);
        }


        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

            segmented5 = (SegmentedGroup) view.findViewById(R.id.segmented5);
            //级联列表listview
            explistview_live = (SnookerPinnedHeaderExpandableListView) view.findViewById(R.id.explistview_live);
            //设置悬浮头部VIEW
            //列表头部条目
            snooker_race_male_gridview = (HorizontalScrollView) view.findViewById(R.id.snooker_race_male_gridview);
            snooker_race_male_gridview.setVisibility(View.GONE);
            //列表头部条目
            snooker_race_time_head = (LinearLayout) view.findViewById(R.id.snooker_race_time_head);

            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.live_swiperefreshlayout);// 数据板块，listview
            mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getActivity(), StaticValues.REFRASH_OFFSET_END));

            live_error_ll = (LinearLayout) view.findViewById(R.id.live_error_ll);
            live_error_btn = (TextView) view.findViewById(R.id.live_error_btn);
            live_error_btn.setOnClickListener(this);
            //暂无数据
            live_no_data_txt = (TextView) view.findViewById(R.id.live_no_data_txt);

            live_error_ll = (LinearLayout) view.findViewById(R.id.live_error_ll);
            live_error_btn = (TextView) view.findViewById(R.id.live_error_btn);
            live_error_btn.setOnClickListener(this);
            //赛事简介的暂无数据
            live_pr_no_data_txt = (TextView) view.findViewById(R.id.live_pr_no_data_txt);
            //赛事头部线条
            lay_agendafg = (TextView) view.findViewById(R.id.lay_agendafg);

            //日期
            snooker_day = (TextView) view.findViewById(R.id.snooker_day);
            snooker_day.setText(R.string.basket_database_details_season);
            snooker_time = (TextView) view.findViewById(R.id.snooker_time);
            snooker_time.setText(R.string.snooker_game_time);

        }


        /*下拉刷新*/
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
               /* upLeagueRace("0");*/
                    reFH();
                }
            }, 500);
        }

        public void reFH() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                        upLeagueRace();
                }
            }, 500);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
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
        }
}