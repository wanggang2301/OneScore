package com.hhly.mlottery.frame.snookerframe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.SnookerMatchDetailActivity;
import com.hhly.mlottery.adapter.ScheduleDateAdapter;
import com.hhly.mlottery.adapter.snooker.SnookerListAdapter;
import com.hhly.mlottery.bean.scheduleBean.ScheduleDate;
import com.hhly.mlottery.bean.snookerbean.snookerschedulebean.SnookerEventsBean;
import com.hhly.mlottery.bean.snookerbean.snookerschedulebean.SnookerScheuleBean;
import com.hhly.mlottery.callback.DateOnClickListener;
import com.hhly.mlottery.callback.RecyclerViewItemClickListener;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.SnookerSettingEvent;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.view.LoadMoreRecyclerView;
import com.hhly.mlottery.widget.ExactSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by yixq on 2017/2/16.
 * mail：yixq@13322.com
 * describe:snooker 赛程列表
 */

public class SnookerScheduleFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    private Context mContext;
    private View mView;
    private LoadMoreRecyclerView mRecyclerView;
    private LinearLayout mErrorLayout;
    private TextView mRefreshText;
    private TextView mNoData;
    private ExactSwipeRefreshLayout mRefresh;
    private LinearLayoutManager linearLayoutManager;
    private DateOnClickListener mDateOnClickListener;

    private List<SnookerEventsBean> allData;//页面所有数据
    private SnookerListAdapter mSnookerListAdapter;

    private boolean isFirstLoadDate = true; // 记录是否第一次加载数据（用于初始化日期，日期切换后不再初始化）
    private String mCurrentDate;
    private static int currentDatePosition = 0;//记录当前日期选择器中日期的位置
    private static final int DATENUM = 7;
    /**
     * 标记不同类型item
     */
    private int DATETYPE = 0;
    private int LEAGUETYPE = 1;
    private int MATCHTYPE = 2;
    //显示状态
    private static final int SHOW_STATUS_LOADING = 1;//加载中
    private static final int SHOW_STATUS_ERROR = 2;//加载失败
    private static final int SHOW_STATUS_NO_DATA = 3;//暂无数据
    private static final int SHOW_STATUS_SUCCESS = 4;//加载成功
    private final static int SHOW_STATUS_REFRESH_ONCLICK = 5;//点击刷新
    private final static int SHOW_STATUS_CURRENT_ONDATA = 6;//当前日期无数据
    private LinearLayout mLoading;
    private TextView mCurrentNoData;
    private List<ScheduleDate> mDatelist;
    private ListView mDateListView;

    private boolean isDataChick = true;//日期是否可选择（可点击）加载数据时不可切换日期

    public static SnookerScheduleFragment newInstance() {
        Bundle args = new Bundle();
        SnookerScheduleFragment fragment = new SnookerScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.snooker_schedule_fragment , container , false);
        initView();
        setStatus(SHOW_STATUS_LOADING);
        initData(currentDatePosition);
        return mView;
    }
    public void loadData(){
//        Toast.makeText(mContext, "外部更新数据", Toast.LENGTH_SHORT).show();
        updateAdapter();
    }
    private void initView() {

        mRecyclerView = (LoadMoreRecyclerView) mView.findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //网络异常
        mErrorLayout = (LinearLayout) mView.findViewById(R.id.snooker_error_layout);
        //点击刷新
        mRefreshText = (TextView) mView.findViewById(R.id.snooker_reloading_txt);
        mRefreshText.setOnClickListener(this);
        //暂无数据
        mNoData = (TextView) mView.findViewById(R.id.snooker_nodata_txt);
        //加载中...
        mLoading = (LinearLayout) mView.findViewById(R.id.snooker_loading_ll);

        //当前日期下无数据
        mCurrentNoData = (TextView) mView.findViewById(R.id.snooker_current_nodata);

        mRefresh = (ExactSwipeRefreshLayout) mView.findViewById(R.id.snooker_refresh_layout);
        mRefresh.setColorSchemeResources(R.color.bg_header);
        mRefresh.setOnRefreshListener(this);
        mRefresh.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getActivity(), StaticValues.REFRASH_OFFSET_END));
    }

    /**
     * 设置显示状态
     * @param status
     */
    private void setStatus(int status) {

        if (status == SHOW_STATUS_LOADING) {
            mRefresh.setVisibility(View.VISIBLE);
            mRefresh.setRefreshing(true);
        } else if (status == SHOW_STATUS_SUCCESS) {
            mRefresh.setVisibility(View.VISIBLE);
            mRefresh.setRefreshing(false);
        } else if (status == SHOW_STATUS_REFRESH_ONCLICK) {
            mRefresh.setVisibility(View.GONE);
            mRefresh.setRefreshing(true);
        } else if(status == SHOW_STATUS_CURRENT_ONDATA){
            mRefresh.setVisibility(View.VISIBLE);
            mRefresh.setRefreshing(false);
        }else {
            mRefresh.setVisibility(View.GONE);
            mRefresh.setRefreshing(false);
        }
        mLoading.setVisibility((status == SHOW_STATUS_REFRESH_ONCLICK) ? View.VISIBLE : View.GONE);
        mErrorLayout.setVisibility(status == SHOW_STATUS_ERROR ? View.VISIBLE : View.GONE);
        mNoData.setVisibility(status == SHOW_STATUS_NO_DATA ? View.VISIBLE : View.GONE);
        mCurrentNoData.setVisibility(status == SHOW_STATUS_CURRENT_ONDATA ? View.VISIBLE : View.GONE);
    }

    private void initData(final int position){
        isDataChick = false;
//        String url = "http://192.168.31.12:8080/mlottery/core/snookerMatch.getSnookerEvents.do";//http://192.168.31.12:8080/mlottery/core/snookerMatch.getSnookerEvents.do?lang=zh&date=2017-02-16
        Map<String ,String> mapUrl = new HashMap<>();
        if (mCurrentDate != null) {
            List mDatelist = new ArrayList<String>();
            for (int i = 0; i < DATENUM; i++) {
                mDatelist.add(DateUtil.getDate(i, mCurrentDate));
            }
            mapUrl.put("date", mDatelist.get(position).toString());
        }

        VolleyContentFast.requestJsonByGet(BaseURLs.SNOOKER_SCHEDULE_URL, mapUrl, new VolleyContentFast.ResponseSuccessListener<SnookerScheuleBean>() {
            @Override
            public void onResponse(SnookerScheuleBean jsonData) {

                if (jsonData == null || jsonData.getData() == null ) {
                    setStatus(SHOW_STATUS_NO_DATA);
                    return;
                }
                if (jsonData.getData().getDate() == null || jsonData.getData().getDate().equals("")) {
                    setStatus(SHOW_STATUS_NO_DATA);
                    return;
                }
                isDataChick = true;
                currentDatePosition = position;

                if (isFirstLoadDate) {

                    mCurrentDate = jsonData.getData().getDate();

                    initListDateAndWeek(mCurrentDate, position);
                    isFirstLoadDate = false;
                }
                if ((jsonData.getData().getEventsBattle() == null || jsonData.getData().getEventsBattle().size() == 0)
                        && (jsonData.getData().getDate() != null && !jsonData.getData().getDate().equals(""))) {

                    allData = new ArrayList<>();
                    SnookerEventsBean riqiItem = new SnookerEventsBean();
                    riqiItem.setItemType(DATETYPE);
                    riqiItem.setItemDate(jsonData.getData().getDate());
                    allData.add(riqiItem);
                    if (mSnookerListAdapter == null) {
                        mSnookerListAdapter = new SnookerListAdapter(mContext, allData);
                        mRecyclerView.setAdapter(mSnookerListAdapter);
                        mSnookerListAdapter.setDateOnClickListener(mDateOnClickListener);
                    } else {
                        updateAdapter();
                    }
                    setStatus(SHOW_STATUS_CURRENT_ONDATA);
                    return;
                }
                L.d("qwer====>> " , jsonData.getData().getDate() + " <==> " + jsonData.getData().getEventsBattle().size());
                //记录所有比赛的list 用于联赛分类
                List<SnookerEventsBean> datalist = jsonData.getData().getEventsBattle();
                //记录某个比赛所在的联赛，用于相同联赛的比赛分类
                String currentLeague = "";
                allData = new ArrayList<>();

                SnookerEventsBean riqiItem = new SnookerEventsBean();
                riqiItem.setItemType(DATETYPE);
                riqiItem.setItemDate(jsonData.getData().getDate());
                allData.add(riqiItem);

                for (SnookerEventsBean all : datalist) {

                    if (currentLeague.equals("") || !all.getLeagueName().equals(currentLeague)) {
                        SnookerEventsBean leagueName = new SnookerEventsBean();
                        leagueName.setItemType(LEAGUETYPE);
                        leagueName.setItemLeaguesName(all.getLeagueName());
                        allData.add(leagueName);
                        currentLeague = all.getLeagueName();
                    }

                    all.setItemType(MATCHTYPE);
                    allData.add(all);
                }

                if (mSnookerListAdapter == null) {
                    mSnookerListAdapter = new SnookerListAdapter(mContext, allData);
                    mRecyclerView.setAdapter(mSnookerListAdapter);
                    mSnookerListAdapter.setDateOnClickListener(mDateOnClickListener);

                } else {
                    updateAdapter();
                }
                mSnookerListAdapter.setmOnItemClickListener(new RecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, String data) {
                        Intent intent = new Intent(getActivity(), SnookerMatchDetailActivity.class);
                        intent.putExtra("matchId" , data);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                    }
                });
                setStatus(SHOW_STATUS_SUCCESS);
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                setStatus(SHOW_STATUS_ERROR);
            }
        },SnookerScheuleBean.class);
        choiceDateList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.snooker_reloading_txt:
                Toast.makeText(mContext, "点击了刷新···", Toast.LENGTH_SHORT).show();
                setStatus(SHOW_STATUS_REFRESH_ONCLICK);
                initData(currentDatePosition);
                break;

        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setStatus(SHOW_STATUS_LOADING);
                initData(currentDatePosition);
            }
        }, 1000);
    }

    private void choiceDateList() {
        mDateOnClickListener = new DateOnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDataChick) {
//                    Toast.makeText(mContext, "点击日期选择", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);
                    final AlertDialog alertDialog = builder.create();
                    LayoutInflater infla = LayoutInflater.from(getActivity());
                    View alertDialogView = infla.inflate(R.layout.alertdialog, null);
                    mDateListView = (ListView) alertDialogView.findViewById(R.id.listdate);
                    initListDateAndWeek(mCurrentDate,  currentDatePosition);
                    mDateListView.setAdapter(mDateAdapter);
                    mDateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
                            setStatus(SHOW_STATUS_LOADING);
                            initData(position);
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                    alertDialog.getWindow().setContentView(alertDialogView);
                    alertDialog.setCanceledOnTouchOutside(true);
                }else{
                    Toast.makeText(mContext, getActivity().getResources().getString(R.string.snooker_loading), Toast.LENGTH_SHORT).show();
                }

            }
        };
    }
    /**
     * 初始化7天的日期
     *
     * @param s
     */
    private ScheduleDateAdapter mDateAdapter;
    private void initListDateAndWeek(String s, int position) {
        mDatelist = new ArrayList<ScheduleDate>();
        for (int i = 0; i < 7; i++) {
            mDatelist.add(new ScheduleDate(DateUtil.getDate(i, s), DateUtil.getWeekOfXinQi(DateUtil.parseDate(DateUtil.getDate(i, s)))));
        }
        mDateAdapter = new ScheduleDateAdapter(mDatelist, mContext, position);
    }
    /**
     * 更新数据
     */
    public void updateAdapter() {
        if (mSnookerListAdapter == null) {
            return;
        }
        mSnookerListAdapter.updateDatas(allData);
        mSnookerListAdapter.notifyDataSetChanged();
    }
    /**
     * 设置返回
     */
    public void onEventMainThread(SnookerSettingEvent snookerSettingEvent) {
        updateAdapter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
