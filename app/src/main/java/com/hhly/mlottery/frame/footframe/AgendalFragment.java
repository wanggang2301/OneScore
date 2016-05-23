package com.hhly.mlottery.frame.footframe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.FTRacePinnedHeaderExpandableAdapter;
import com.hhly.mlottery.bean.footballDetails.LeagueRoundInfo;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.ChoiceWheelUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.PinnedHeaderExpandableListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * * @ClassName: OneScore
 *
 * @author:Administrator luyao
 * @Description: 赛程Fragment
 * @data: 2016/3/29 16:32
 */
public class AgendalFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "AgendalFragment";
    public static final int ROUND_LOADING = 0;//轮数正在加载
    public static final int ROUND_NODATA = 1;//轮数没有数据
    public static final int ROUND_NETERROR = 2;//轮数网络错误
    public static final int ROUND_SUCESS = 3;//轮数加载成功
    public static final int ROUND_FAIL = 4;//轮数加载失败
    public static final int RACE_LOADING = 5;//赛程正在加载
    public static final int RACE_NODATA = 6;//赛程没有数据
    public static final int RACE_NETERROR = 7;//赛程网络错误
    public static final int RACE_SUCESS = 8;//赛程加载成功
    public static final int RACE_FAIL = 9;//赛程加载失败
    private static final String FRAGMENT_INDEX = "fragment_index";
    private View view;//整个视图
    private PinnedHeaderExpandableListView mlay_agendafg_lv;//赛程listview
    private RelativeLayout bottom_lay_round;//轮数加载成功时显示的视图

    private TextView mtv_agendafg_wheelcount;//底部显示的轮数
    private LinearLayout reloadwhenfail;//赛程数据加载失败时的视图
    private TextView race_data_reLoading;//赛程数据加载失败时，点击重新加载
    private TextView dataloding_ornodata;//赛程正在加载时或者没有数据时的视图
    private LinearLayout wheeldata_fail;//轮数数据加载失败时的视图
    private TextView wheel_data_reLoading;//轮数数据加载失败时，点击重新加载

    private List<LeagueRoundInfo.DataBean> LeagueRoundBean_list;//轮次实体集合;
    private List<LeagueRoundInfo.RaceBean> mRaceListBeansall;//组名和赛程集合
    private List<List<LeagueRoundInfo.RaceBean.ListBean>> mRaceListBeans = new ArrayList<>();//赛程集合
    private List<String> mgroupnameList = new ArrayList<>();//组名集合
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FTRacePinnedHeaderExpandableAdapter mExpandableAdapter;
    int mCurrentIndex; //轮次item的标识
    private String leagueId;//联赛id
    private String datas; //联赛赛季
    private String leagueType;  //联赛类型
    private   boolean isinitview=false;
    //收到广播  更新ui
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int position = intent.getIntExtra("position", 0);
            if ("2".equals(leagueType)) {
                mtv_agendafg_wheelcount.setText(LeagueRoundBean_list.get(position).getRound());


            } else {
                mtv_agendafg_wheelcount.setText(getActivity().getString(R.string.information_chdi_text) + LeagueRoundBean_list.get(position).getRound() + getActivity().getString(R.string.information_chround_text));
            }
            mCurrentIndex = position;
            if (mCurrentIndex == 0) {
                mBack_up.setBackgroundResource(R.mipmap.go_dwon);
                mGo_down.setBackgroundResource(R.mipmap.inforback);
            } else if (mCurrentIndex == LeagueRoundBean_list.size() - 1) {

                mGo_down.setBackgroundResource(R.mipmap.back_up);
                mBack_up.setBackgroundResource(R.mipmap.inforgo);
            } else {
                mBack_up.setBackgroundResource(R.mipmap.inforgo);
                mGo_down.setBackgroundResource(R.mipmap.inforback);
            }
            //这里需要传入的轮次即为x+1 联赛id，赛季
            getLeagueRaceDataFromNet(LeagueRoundBean_list.get(position).getRound(), leagueId, datas);//从网络获取赛程数据

        }
    };
    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ROUND_LOADING:
                    //轮数正在下载
                    bottom_lay_round.setVisibility(View.VISIBLE);
                   // bottom_lay_round.setVisibility(View.GONE);
                    wheeldata_fail.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(true);
                    break;
                case ROUND_NODATA:
                    //轮数暂没数据
                    bottom_lay_round.setVisibility(View.GONE);
                   // bottom_lay_round.setVisibility(View.GONE);
                    wheeldata_fail.setVisibility(View.GONE);
//                    mtv_agendafg_wheelcount.setText("暂无数据");
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case ROUND_NETERROR:
                    //轮数网络错误
                    bottom_lay_round.setVisibility(View.GONE);
                    wheeldata_fail.setVisibility(View.VISIBLE);
//                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);

                    break;
                case ROUND_SUCESS:
                    //轮数下载成功
                    if(LeagueRoundBean_list!=null){
                        bottom_lay_round.setVisibility(View.VISIBLE);
                    }
                    wheeldata_fail.setVisibility(View.GONE);
                    if ("2".equals(leagueType)) {
                        mtv_agendafg_wheelcount.setText(LeagueRoundBean_list.get(mCurrentIndex).getRound());

                    } else {
                        mtv_agendafg_wheelcount.setText(getActivity().getString(R.string.information_chdi_text) + LeagueRoundBean_list.get(mCurrentIndex).getRound() + getActivity().getString(R.string.information_chround_text));
                    }

                    mSwipeRefreshLayout.setRefreshing(false);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    break;
                case ROUND_FAIL:
                    //轮数下载失败
                    bottom_lay_round.setVisibility(View.GONE);
                    wheeldata_fail.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);

                case RACE_LOADING:
                    //赛程正在下载
                    mlay_agendafg_lv.setVisibility(View.VISIBLE);
                   // mlay_agendafg_lv.setVisibility(View.GONE);
                    reloadwhenfail.setVisibility(View.GONE);
                    //dataloding_ornodata.setVisibility(View.VISIBLE);
                   // dataloding_ornodata.setText(getActivity().getString(R.string.loading_txt));
//                    ToastTools.ShowQuickCenter(getActivity(), "赛程正在下载");
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(true);
                    mGo_down.setEnabled(false);
                    mBack_up.setEnabled(false);
                    mtv_agendafg_wheelcount.setEnabled(false);
                    break;
                case RACE_NODATA:
                    //赛程暂没数据
                    mlay_agendafg_lv.setVisibility(View.GONE);
                    reloadwhenfail.setVisibility(View.GONE);
                    dataloding_ornodata.setVisibility(View.VISIBLE);
                    dataloding_ornodata.setText(R.string.nodata);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mGo_down.setEnabled(true);
                    mBack_up.setEnabled(true);
                    mtv_agendafg_wheelcount.setEnabled(true);
                    break;
                case RACE_NETERROR:
                    //赛程网络错误

                    mlay_agendafg_lv.setVisibility(View.GONE);
                    dataloding_ornodata.setVisibility(View.GONE);
                    reloadwhenfail.setVisibility(View.VISIBLE);
//                    ToastTools.ShowQuickCenter(getActivity(), "赛程网络错误");
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mGo_down.setEnabled(true);
                    mBack_up.setEnabled(true);
                    mtv_agendafg_wheelcount.setEnabled(true);
                    break;
                case RACE_SUCESS:
                    //轮数下载成功
//                    adaputer = new LeagueRaceAdaputer(getActivity(), mRaceListBeans, R.layout.agendafragment_lv_item);
//                    mlay_agendafg_lv.setAdapter(adaputer);
                    mlay_agendafg_lv.setVisibility(View.VISIBLE);
                    dataloding_ornodata.setVisibility(View.GONE);
                    reloadwhenfail.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mGo_down.setEnabled(true);
                    mBack_up.setEnabled(true);
                    mtv_agendafg_wheelcount.setEnabled(true);


                    break;
                case RACE_FAIL:
                    //赛程下载失败
                    mlay_agendafg_lv.setVisibility(View.GONE);
                    dataloding_ornodata.setVisibility(View.GONE);
                    reloadwhenfail.setVisibility(View.VISIBLE);
//                    ToastTools.ShowQuickCenter(getActivity(), "赛程下载失败");
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mGo_down.setEnabled(true);
                    mBack_up.setEnabled(true);
                    mtv_agendafg_wheelcount.setEnabled(true);
                    break;
            }
        }
    };
    private Context mContext;
    private TextView mBack_up;
    private TextView mGo_down;

    private boolean isLoadDataed=true;
    private LinearLayout mAgenda_left;
    private LinearLayout mAgenda_right;
    private TextView mReload_btn;

    public AgendalFragment() {


    }

    public static AgendalFragment newInstance() {
        AgendalFragment fragment = new AgendalFragment();
        return fragment;
    }

    /* public AgendalFragment(String stmLeaguesId, String datas, String leagueType) {

         this.leagueId = stmLeaguesId;
         this.datas = datas;
         this.leagueType = leagueType;
         Log.v(TAG, "AgendalFragment__======datas" + datas);
     }

 */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.agendafragment, null);

        //注册广播监听轮数对话框返回来的消息
        IntentFilter intentFilter = new IntentFilter("wheelcount");
        getActivity().registerReceiver(mBroadcastReceiver, intentFilter);

              initView();

        // getDatas(leagueId, leagueType, datas);
        mContext = getActivity();

        return view;
    }

    //从网络获取轮数数据
    public void getLeagueRoundDataFromNet(String leagueId, String leagueType, String leagueDate) {

        if (!isLoadDataed) {
            return;
        }

        this.leagueId = leagueId;
        this.leagueType = leagueType;
        this.datas = leagueDate;

       // handle.sendEmptyMessage(ROUND_LOADING);//正在加载数据

        String url = BaseURLs.URL_FOOTBALL_LEAGUEROUND;

        Map<String, String> map = new HashMap<>();
        //语言不用传，已封装好自动追加
        map.put("leagueId", leagueId);
        map.put("leagueDate", leagueDate);
        map.put("type", leagueType);

//        String urlx = "http://192.168.31.70:8181/mlottery/core/footballLeagueData.qLeagueRound.do?lang=zh&&leagueId=1";
        VolleyContentFast.requestJsonByGet(url, map, new VolleyContentFast.ResponseSuccessListener<LeagueRoundInfo>() {
            @Override
            public synchronized void onResponse(final LeagueRoundInfo json) {

                //请求成功
                if ("200".equals(json.getCode() + "")) {
                    isLoadDataed = false;
                    //没有数据
                    if (json == null) {
                        handle.sendEmptyMessage(ROUND_NODATA);//暂时没有数据
                    } else {
                        //有数据
                        //从返回的数据中获取round，保存好传给choicewheelutil展示
                        LeagueRoundBean_list = json.getData();
                        if (LeagueRoundBean_list.size() == 0) {
                            handle.sendEmptyMessage(ROUND_NODATA);//暂时没有数据
                        } else {
                            if (LeagueRoundBean_list.size() == 1) {
                                mBack_up.setBackgroundResource(R.mipmap.go_dwon);
                                mGo_down.setBackgroundResource(R.mipmap.back_up);
                            } else {
                                for (int n = 0; n < LeagueRoundBean_list.size(); n++) {
                                    if (LeagueRoundBean_list.get(n).isCurrent()) {
                                        mCurrentIndex = n;
                                        // Log.v(TAG, "mCurrentIndex" + mCurrentIndex);
                                    }
                                }

                                if (mCurrentIndex == 0 ) {
                                    mBack_up.setBackgroundResource(R.mipmap.go_dwon);
                                    mGo_down.setBackgroundResource(R.mipmap.inforback);
                                } else if (mCurrentIndex == LeagueRoundBean_list.size() - 1) {
                                    mGo_down.setBackgroundResource(R.mipmap.back_up);
                                    mBack_up.setBackgroundResource(R.mipmap.inforgo);
                                }else{
                                    mGo_down.setBackgroundResource(R.mipmap.inforback);
                                    mBack_up.setBackgroundResource(R.mipmap.inforgo);
                                }
                            }
//
                            handle.sendEmptyMessage(ROUND_SUCESS);//加载数据成功
                        }
                        addRaceData(json);
                    }
                } else {
                    //请求失败
                    handle.sendEmptyMessage(ROUND_FAIL);//请求失败;
                    handle.sendEmptyMessage(RACE_FAIL);//请求失败;
                }


            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                handle.sendEmptyMessage(ROUND_NETERROR);//网络错误
                handle.sendEmptyMessage(RACE_NETERROR);//网络错误

            }
        }, LeagueRoundInfo.class);
    }

    //添加赛程数据
    public void addRaceData(LeagueRoundInfo json) {
        //拿到组名和赛程集合  可做进一步操作
        mRaceListBeansall = json.getRace();
        int mSize = mRaceListBeansall.size();
        if (mSize == 0) {
            handle.sendEmptyMessage(RACE_NODATA);//暂时没有数据
        } else {
            mgroupnameList.clear();
            mRaceListBeans.clear();
            for (int i = 0; i < mSize; i++) {
                //添加组名数据
                mgroupnameList.add(mRaceListBeansall.get(i).getGroup());
                //添加组下listview的数据
                mRaceListBeans.add(mRaceListBeansall.get(i).getList());
            }
            mExpandableAdapter = new FTRacePinnedHeaderExpandableAdapter(mgroupnameList, mRaceListBeans, getActivity(), mlay_agendafg_lv);
            mlay_agendafg_lv.setAdapter(mExpandableAdapter);

            mExpandableAdapter.notifyDataSetChanged();
            for (int i = 0; i < mSize; i++) {
                mlay_agendafg_lv.expandGroup(i); //设置 默认打开的 group
            }
            handle.sendEmptyMessage(RACE_SUCESS);//加载成功;
        }


    }

    //从网络获取赛程数据
    private void getLeagueRaceDataFromNet(String leagueRound, String leagueId, String leagueDate) {

        handle.sendEmptyMessage(RACE_LOADING);//正在加载数据
        Map<String, String> map = new HashMap<>();
        //语言不用传，已封装好自动追加
        //联赛id
        map.put("leagueId", leagueId);
        //赛季
        map.put("leagueDate", leagueDate);
        //轮次
        map.put("leagueRound", leagueRound);

        map.put("type", leagueType);
//        String urlx = "http://192.168.31.70:8181/mlottery/core/footballLeagueData.qLeagueRace.do?lang=zh&&leagueId=1";
        VolleyContentFast.requestJsonByPost(BaseURLs.URL_FOOTBALL_LEAGUERACE, map, new VolleyContentFast.ResponseSuccessListener<LeagueRoundInfo>() {
            @Override
            public synchronized void onResponse(final LeagueRoundInfo json) {
                //请求成功
                if ("200".equals(json.getCode() + "")) {
                    //没有数据
                    if (json == null) {
                        handle.sendEmptyMessage(RACE_NODATA);//暂时没有数据
                    } else {
                        //有数据-赛程
                        addRaceData(json);

                    }
                } else {
                    //请求失败
                    handle.sendEmptyMessage(RACE_FAIL);//请求失败;
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                handle.sendEmptyMessage(RACE_NETERROR);//网络错误

            }
        }, LeagueRoundInfo.class);
    }


    public void initView() {

        mAgenda_left = (LinearLayout) view.findViewById(R.id.agenda_left);
        mAgenda_right = (LinearLayout) view.findViewById(R.id.agenda_right);
        mAgenda_left.setOnClickListener(this);
        mAgenda_right.setOnClickListener(this);
        mlay_agendafg_lv = (PinnedHeaderExpandableListView) view.findViewById(R.id.lay_agendafg_lv);

        mtv_agendafg_wheelcount = (TextView) view.findViewById(R.id.tv_agendafg_wheelcount);
        mtv_agendafg_wheelcount.setOnClickListener(this);
        mReload_btn = (TextView) view.findViewById(R.id.network_exception_reload_btn);
        //默认显示最后一轮，那么只能在这边请求数据,在把数据传给选中对话框显示
//        mtv_agendafg_wheelcount.setText(mchoiceList.get(mchoiceList.size() - 1) + "");
        reloadwhenfail = (LinearLayout) view.findViewById(R.id.network_exception_layout);

        bottom_lay_round = (RelativeLayout) view.findViewById(R.id.bottom_lay_round);
        race_data_reLoading = (TextView) view.findViewById(R.id.network_exception_reload_btn);
        race_data_reLoading.setOnClickListener(this);
        dataloding_ornodata = (TextView) view.findViewById(R.id.dataloding_ornodata);
        wheeldata_fail = (LinearLayout) view.findViewById(R.id.wheeldata_fail);
        wheel_data_reLoading = (TextView) view.findViewById(R.id.wheel_data_reLoading);
        wheel_data_reLoading.setOnClickListener(this);
        //下拉刷新控件
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.football_schedule_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //获取图标
        mBack_up = (TextView) view.findViewById(R.id.back_up);
        mGo_down = (TextView) view.findViewById(R.id.go_down);
        mGo_down.setOnClickListener(this);
        mBack_up.setOnClickListener(this);
        //getLeagueRoundDataFromNet(leagueId, leagueType, datas);//从网络获取轮数数据

         isinitview = true;
         handle.sendEmptyMessage(ROUND_NODATA);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //后退一轮
            case R.id.agenda_left:
                //无论item上是什么数据格式都可以实现
                mCurrentIndex--;
                if (mCurrentIndex >= 0) {
                    mGo_down.setBackgroundResource(R.mipmap.inforback);
                    if ("2".equals(leagueType)) {
                        mtv_agendafg_wheelcount.setText(LeagueRoundBean_list.get(mCurrentIndex).getRound());
                        getLeagueRaceDataFromNet(LeagueRoundBean_list.get(mCurrentIndex).getRound(), leagueId, datas);//从网络获取赛程数据
                    } else {
                        mtv_agendafg_wheelcount.setText(getActivity().getString(R.string.information_chdi_text) + LeagueRoundBean_list.get(mCurrentIndex).getRound() + getActivity().getString(R.string.information_chround_text));
                        //这里需要传入的轮次即为x+1 联赛id，赛季
                        getLeagueRaceDataFromNet(LeagueRoundBean_list.get(mCurrentIndex).getRound(), leagueId, datas);//从网络获取赛程数据

                    }

                    if (mCurrentIndex == 0) {
                        mBack_up.setBackgroundResource(R.mipmap.go_dwon);

                    } else {

                        mBack_up.setBackgroundResource(R.mipmap.inforgo);
                    }
                    // getLeagueRaceDataFromNet(LeagueRoundBean_list.get(mCurrentIndex).getRound(), leagueId, datas, BaseURLs.URL_FOOTBALL_LEAGUERACE);

                } else {
                    mCurrentIndex = 0;
                    //mBack_up.setImageResource(R.mipmap.go_dwon);

                }

                break;
            //选择轮数
            case R.id.tv_agendafg_wheelcount:
                MobclickAgent.onEvent(mContext,"Football_InformationFragment_Round");
                //无论item上是什么数据格式都可以实现
                if (LeagueRoundBean_list.size() != 0) {
                    new ChoiceWheelUtil(mContext, leagueType, mCurrentIndex, getActivity(), LeagueRoundBean_list).showChoiceWheelDialog();

                }


                break;
            //向前一轮
            case R.id.agenda_right:
                //无论item上是什么数据格式都可以实现
                mCurrentIndex++;
                if (LeagueRoundBean_list!=null &&mCurrentIndex <= LeagueRoundBean_list.size() - 1 ) {
                    mBack_up.setBackgroundResource(R.mipmap.inforgo);
                    //这里需要传入的轮次即为x+1 联赛id，赛季
                    if ("2".equals(leagueType)) {
                        getLeagueRaceDataFromNet(LeagueRoundBean_list.get(mCurrentIndex).getRound(), leagueId, datas);//从网络获取赛程数据
                        mtv_agendafg_wheelcount.setText(LeagueRoundBean_list.get(mCurrentIndex).getRound());

                    } else {
                        mtv_agendafg_wheelcount.setText(getActivity().getString(R.string.information_chdi_text) + LeagueRoundBean_list.get(mCurrentIndex).getRound() + getActivity().getString(R.string.information_chround_text));
                        getLeagueRaceDataFromNet(LeagueRoundBean_list.get(mCurrentIndex).getRound(), leagueId, datas);//从网络获取赛程数据

                    }

                            if (mCurrentIndex == LeagueRoundBean_list.size() - 1) {
                                mGo_down.setBackgroundResource(R.mipmap.back_up);

                            } else {

                                mGo_down.setBackgroundResource(R.mipmap.inforback);

                            }

                    // getLeagueRaceDataFromNet(LeagueRoundBean_list.get(mCurrentIndex).getRound(), leagueId, datas, BaseURLs.URL_FOOTBALL_LEAGUERACE);
                } else {
                    // mGo_down.setImageResource(R.mipmap.back_up);
                    mCurrentIndex = LeagueRoundBean_list.size() - 1;
                }
                break;
            //刷新赛程
            case R.id.network_exception_reload_btn:
                //这里需要传入底部轮次textview上显示的轮次 联赛id，赛季
//                handle.sendEmptyMessage(ROUND_LOADING);
//                handle.sendEmptyMessage(RACE_LOADING);
//                getLeagueRaceDataFromNet(LeagueRoundBean_list.get(mCurrentIndex).getRound(), leagueType, datas);//从网络获取赛程数据
                getLeagueRoundDataFromNet(leagueId, leagueType, datas);
                break;
            //刷新轮次
            case R.id.wheel_data_reLoading:
                //这里需要传入联赛id，赛季
                getLeagueRoundDataFromNet(leagueId, leagueType, datas);//从网络获取轮数数据
                break;
        }
    }

    @Override
    public void onRefresh() {
        //这里需要传入底部轮次textview上显示的轮次 联赛id，赛季
        getLeagueRaceDataFromNet(LeagueRoundBean_list.get(mCurrentIndex).getRound(), leagueId, datas);//从网络获取赛程数据
//        String leagueRound,String leagueId ,String leagueDate,String url
        // getLeagueRoundDataFromNet(leagueId,leagueType,datas);

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AgendalFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AgendalFragment");
    }
}
