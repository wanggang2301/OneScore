package com.hhly.mlottery.frame.footballframe;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.adapter.basketball.BasketballDatabaseScheduleSectionAdapter;
import com.hhly.mlottery.adapter.basketball.SportsDialogAdapter;
import com.hhly.mlottery.adapter.football.FootballDatabaseScheduleSectionAdapter;
import com.hhly.mlottery.bean.footballDetails.database.DataBaseBean;
import com.hhly.mlottery.bean.footballDetails.footballdatabasebean.DataBean;
import com.hhly.mlottery.bean.footballDetails.footballdatabasebean.ScheduleBean;
import com.hhly.mlottery.bean.footballDetails.footballdatabasebean.ScheduleDatasBean;
import com.hhly.mlottery.bean.footballDetails.footballdatabasebean.ScheduleRaceBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.FootBallDetailTypeEnum;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.HandMatchId;
import com.hhly.mlottery.util.LocaleFactory;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * description:
 * author: yixq
 * Created by A on 2016/9/2.
 * 足球资料库赛程
 */
public class FootballDatabaseScheduleNewFragment extends Fragment implements View.OnClickListener {

    private static final String LEAGUE = "league";
    private static final String PARAM_ID = "leagueId";
    private static final String PARAM_DATE = "leagueDate";
    private static final String PARAM_MATCH_ROUND = "leagueRound";//二级
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_LEAGUE_GROUP = "leagueGroup"; // 一级


    private static final int STATUS_LOADING = 1;
    private static final int STATUS_ERROR = 2;
    private static final int STATUS_NO_DATA = 3;

    View mButtonFrame;
    TextView mTitleTextView;
    ImageView mLeftButton;
    ImageView mRightButton;

    View mEmptyView;
    ProgressBar mProgressBar;
    View mErrorLayout;
    TextView mRefreshTextView;
    TextView mNoDataTextView;

    RecyclerView mRecyclerView;

    private DataBaseBean league;
    private String mLeagueDate = null;

    private List<BasketballDatabaseScheduleSectionAdapter.Section> mSections;
    private List<FootballDatabaseScheduleSectionAdapter.Section> mSectionsNew;
    private BasketballDatabaseScheduleSectionAdapter mAdapter;
    private FootballDatabaseScheduleSectionAdapter mAdapterNew;
    private List<DataBean> mRoundString;
    private String mLeagueRound = "";
    private String mLeagueGroup = "";
    private boolean isLoad = false;//是否可选择
    private String url ;
    private int currentPosition = -1; // 当前选中 （默认选中第一项）
    private int currentFirstPosition = -1;  //一级菜单选中
    private int currentSecondPosition = -1; //二级菜单选中
    private ScheduleBean mResultNew; // json 数据
    private RecyclerView mFirstRecyclerView;
    private RecyclerView mSecondRecyclerView;
    private List<String> mFirstData;
    private List<String> mSecondData;
    private boolean chooseSecond = false;
    private List<DataBean> arr;
    private Integer[] array;
    private boolean isCup;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            league = args.getParcelable(LEAGUE);
            mLeagueDate = args.getString(PARAM_DATE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mEmptyView = inflater.inflate(R.layout.basket_database_empty_layout, container, false);
        return inflater.inflate(R.layout.fragment_basket_database_schedule, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mButtonFrame = LayoutInflater.from(view.getContext())
                .inflate(R.layout.layout_basket_database_choose, (ViewGroup) view, false);
        ViewGroup.LayoutParams layoutParams = mEmptyView.getLayoutParams();
        layoutParams.height = DisplayUtil.dip2px(getContext(), 178);
        mEmptyView.setLayoutParams(layoutParams);

        mTitleTextView = (TextView) mButtonFrame.findViewById(R.id.title_button);
        mLeftButton = (ImageView) mButtonFrame.findViewById(R.id.left_button);
        mRightButton = (ImageView) mButtonFrame.findViewById(R.id.right_button);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        initEmptyView();

        setDetailsOnClick();
        initRecycler();

        mTitleTextView.setOnClickListener(this);
        mLeftButton.setOnClickListener(this);
        mRightButton.setOnClickListener(this);

        initData(true);
    }

    private void initEmptyView() {
        mProgressBar = (ProgressBar) mEmptyView.findViewById(R.id.progress);
        mErrorLayout = mEmptyView.findViewById(R.id.error_layout);
        mRefreshTextView = (TextView) mEmptyView.findViewById(R.id.reloading_txt);
        mNoDataTextView = (TextView) mEmptyView.findViewById(R.id.no_data_txt);

        mRefreshTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData(false);
            }
        });
    }

    /**
     * 加载前一项（左点击）
     * @param currPosition
     */
    private void loadLeft(int currPosition){
        url = BaseURLs.URL_FOOTBALL_DATABASE_SCHEDULE_UNFIRST; //选择后的URL
        if (chooseSecond) {
            currentSecondPosition = currPosition - 1;
            secondIndex = currPosition - 1;
            if (currentSecondPosition <= 0) {
                mLeagueRound = arr.get(0).getRound();
            }else{
                mLeagueRound = arr.get(currentSecondPosition).getRound();
            }
            handleHeadViewNew(arr ,currentSecondPosition);
            initData(false);
        }else{
            currentPosition = currPosition - 1;
            if (currentPosition <= 0) {
                if (isCup) {
                    mLeagueRound = "0";
                }  else{
                    mLeagueRound = "1";
                }
            }else{
                if (isCup) {
                    mLeagueRound = currentPosition + "";
                }else{
                    mLeagueRound = currentPosition + 1 +"";
                }
            }
            handleHeadViewNew(mRoundString ,currentPosition);
            initData(false);
        }
    }
    /**
     * 加载后一项（右点击）
     * @param currPosition
     */
    private void loadRight(int currPosition){
        url = BaseURLs.URL_FOOTBALL_DATABASE_SCHEDULE_UNFIRST; //选择后的URL
        if (chooseSecond) {
            currentSecondPosition = currPosition + 1;
            secondIndex = currPosition + 1;
            if (currentSecondPosition >= arr.size()-1) {
                mLeagueRound = arr.get(arr.size()-1).getRound();
            }else{
                mLeagueRound = arr.get(currentSecondPosition).getRound();
            }
            handleHeadViewNew(arr ,currentSecondPosition);
            initData(false);
        }else{
            currentPosition = currPosition + 1;
            if (currentPosition >= mRoundString.size()-1) {
                if (isCup) {
                    mLeagueRound = mRoundString.size() -1 + "";
                }else{
                    mLeagueRound = mRoundString.size() +"";
                }

                mLeagueRound = isCup? mRoundString.size()-1 + "" : mRoundString.size() + "";
            }else{
                if (isCup) {
                    mLeagueRound = currentPosition + "";
                }else{
                    mLeagueRound = currentPosition + 1 +"";
                }
            }
            handleHeadViewNew(mRoundString ,currentPosition);
            initData(false);
        }
    }

    /**
     * 刷新数据
     */
    public void update(boolean type) {
//        url = null;
        initData(type);
    }

    /**
     * 设置状态
     *
     * @param status
     */
    public void setStatus(int status) {
        mNoDataTextView.setVisibility(status == STATUS_NO_DATA ? View.VISIBLE : View.GONE);
        mProgressBar.setVisibility(status == STATUS_LOADING ? View.VISIBLE : View.GONE);
        mErrorLayout.setVisibility(status == STATUS_ERROR ? View.VISIBLE : View.GONE);
    }

    private void initData(final boolean isNewLoad){

        mSectionsNew.clear();
        mAdapterNew.notifyDataSetChanged();
        setStatus(STATUS_LOADING);
        isLoad = false;
        Map<String , String> map = new HashMap();
        map.put(PARAM_ID , league.getLeagueId());
        map.put(PARAM_TYPE , league.getKind());

        /**
         * URL 切换
         */
        if (url == null || url == "" || isNewLoad) {
            url = BaseURLs.URL_FOOTBALL_DATABASE_SCHEDULE_FIRST; //第一次进入的url
            //初始化参数，第一次请求不带参数
            mLeagueGroup = null;
            mLeagueRound = null;
        }else{
            //过滤第一次请求数据不成功刷新时 仍用第一次请求接口（ps：二次接口不带参无数据）
            url = ((mLeagueGroup == null || mLeagueGroup.equals("")) && (mLeagueRound == null || mLeagueRound.equals("")))
                    ? BaseURLs.URL_FOOTBALL_DATABASE_SCHEDULE_FIRST : BaseURLs.URL_FOOTBALL_DATABASE_SCHEDULE_UNFIRST;
            //如果是第一次接口，清空参数
            if (url.equals(BaseURLs.URL_FOOTBALL_DATABASE_SCHEDULE_FIRST)) {
                mLeagueGroup = null;
                mLeagueRound = null;
            }
        }
        if (mLeagueGroup != null && !mLeagueGroup.equals("")) {
            map.put(PARAM_LEAGUE_GROUP , mLeagueGroup + "");
        }
        if (mLeagueDate != null) {
            map.put(PARAM_DATE , mLeagueDate);
        }
        if (mLeagueRound != null && !mLeagueRound.equals("")) {
            map.put(PARAM_MATCH_ROUND , mLeagueRound);
        }
        /**
         * 这里只能用post（ps：用get请求是参数带中文，4.4手机请求不到数据...）
         */
        //http://192.168.31.8:8080/mlottery/core/androidLeagueData.findAndroidLeagueRound.do?lang=zh&leagueId=60&type=0&timeZone=8
//        String murl = "192.168.31.8:8080/mlottery/core/androidLeagueData.findAndroidLeagueRound.do";
        VolleyContentFast.requestJsonByPost(url, map,
                new VolleyContentFast.ResponseSuccessListener<ScheduleBean>() {
                    @Override
                    public void onResponse(ScheduleBean result) {
                        if (result == null || result.getRace() == null) {

                            setStatus(STATUS_NO_DATA);
//                            mButtonFrame.setVisibility(View.GONE);
                            return;
                        }
//                        mButtonFrame.setVisibility(View.VISIBLE);
                        if (result.getCurrentGroup() != null && result.getType() == 1) {
                            mResultNew = result;
                            chooseSecond = true;
                        }
                        if (result.getType() == 2) {
                            isCup = true;
                        }

                        /**
                         * 第一次加载成功后赋值（ps：防止加载后直接刷新无数据（url不同））
                         */
                        if (mLeagueRound == null || mLeagueRound.equals("")) {
                            mLeagueRound = result.getCurrentRoundIndex() + "";
                        }
                        if (mLeagueGroup == null || mLeagueGroup.equals("")) {
                            mLeagueGroup = result.getCurrentGroup();
                        }
                        if (result.getData() != null) {
                            mRoundString = new ArrayList<DataBean>();
                            if (result.getCurrentGroup() != null) {
                                mRoundString = result.getData().get(result.getCurrentGroup());
                                arr =  result.getData().get(result.getCurrentGroup());
                            }else{
                                mRoundString = result.getData().get("emp");
                                arr =  result.getData().get(result.getCurrentGroup());
                            }
                            /**
                             * 获得一级菜单数据
                             */
                            mFirstData = new ArrayList<>();
                            Set key = result.getData().keySet();//得到map所有的key
                            Iterator iter = key.iterator();//遍历key ==》 list
                            while (iter.hasNext()) {
                                String keyName = (String)iter.next();
                                mFirstData.add(keyName);
                            }
                            if (mFirstData != null) {
                                for (int i = 0; i < mFirstData.size(); i++) {
                                    if (mFirstData.get(i).equals(result.getCurrentGroup())) {
                                        currentFirstPosition = i;
                                        firstIndex = i;
                                    }
                                }
                            }
                            /**
                             * 获得二级菜单数据
                             */
                            mSecondData = new ArrayList<>();
                            for (int i = 0; i < mRoundString.size(); i++) {
                                mSecondData.add(mRoundString.get(i).getRound());
                            }
                            currentSecondPosition = result.getCurrentRoundIndex()-1;
                            secondIndex = result.getCurrentRoundIndex()-1;
                            /**
                             * 获得子菜单默认选中项
                             */
                            array = new Integer[mFirstData.size()];
                            for (int i = 0; i < mFirstData.size(); i++) {
                                for (int j = 0; j < result.getData().get(mFirstData.get(i)).size(); j++) {
                                    if (result.getData().get(mFirstData.get(i)).get(j).getIsCurrent() == 1) {
                                        array[i] = j;
                                    }
                                }
                            }
                        }
                        if (mRoundString != null && mRoundString.size() != 0) {
                            isLoad = true;
                        }
                        if (isNewLoad || currentPosition == -1) {
                            currentPosition = result.getType()==2 ? result.getCurrentRoundIndex() : result.getCurrentRoundIndex()-1;
                            handleHeadViewNew(mRoundString ,currentPosition);
                        }
                        handleDataNew(result.getRace());
                        mAdapterNew.notifyDataSetChanged();
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException e) {
                        isLoad = false;
                        VolleyError error = e.getVolleyError();
                        if (error != null) error.printStackTrace();
                        setStatus(STATUS_ERROR);
                    }
                }, ScheduleBean.class);
    }

    /**
     *   选择按钮
     * @param data  阶段选择内容
     * @param currnIndex 当前选择的position
     */
    private void handleHeadViewNew(List<DataBean> data , Integer currnIndex){

        if (data == null || data.size() == 0) {
            mTitleTextView.setText("");
        }else{
            if (currnIndex < 0 || currnIndex >= data.size()) {
                mTitleTextView.setText(data.get(0).getRound());
            }else{
                mTitleTextView.setText(data.get(currnIndex).getRound());
            }
        }
        mLeftButton.setVisibility(currnIndex <= 0 ? View.GONE : View.VISIBLE);
        mRightButton.setVisibility(currnIndex >= data.size()-1 ? View.GONE : View.VISIBLE);
    }

    /**
     * 列表数据
     * @param matchData
     */
    private void handleDataNew(List<ScheduleRaceBean> matchData) {
        if (matchData != null && matchData.size() != 0) {
            for (ScheduleRaceBean matchDay : matchData) {
                SimpleDateFormat dateFormat;
                if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) { //国内
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd E", LocaleFactory.get());
                } else {
                    dateFormat = new SimpleDateFormat("dd-MM-yyyy E", LocaleFactory.get());
                }
                mSectionsNew.add(new FootballDatabaseScheduleSectionAdapter
                        .Section(true, dateFormat.format(matchDay.getDay())));
                for (ScheduleDatasBean match : matchDay.getDatas()) {
                    mSectionsNew.add(new FootballDatabaseScheduleSectionAdapter
                            .Section(match));
                }
            }
        } else {
            setStatus(STATUS_NO_DATA);
//            mButtonFrame.setVisibility(View.GONE);
        }
    }

    /**
     * 选中器弹框
     */

    private int firstIndex;
    private int secondIndex;

    public void setDialog(){
        /**
         * type == 1 子联赛（二级菜单）
         */
        if (chooseSecond) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.basket_database_league_dialog, null);
            TextView titleView = (TextView) view.findViewById(R.id.title);
            titleView.setText(getString(R.string.football_database_details_stage));

            final AlertDialog mAlertDialog = mBuilder.create();
            mAlertDialog.setCanceledOnTouchOutside(true);//设置空白处点击 dialog消失

            mFirstRecyclerView = (RecyclerView) view.findViewById(R.id.first_recycler_view);
            GridLayoutManager firstGridLayoutManager = new GridLayoutManager(getContext(), 3);
            mFirstRecyclerView.setLayoutManager(firstGridLayoutManager);
            final FirstAdapter firstAdapter = new FirstAdapter(mFirstData , firstIndex);
            mFirstRecyclerView.setAdapter(firstAdapter);

            mSecondRecyclerView = (RecyclerView) view.findViewById(R.id.second_recycler_view);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 5);
            mSecondRecyclerView.setLayoutManager(gridLayoutManager);
            final SecondAdapter secondAdapter = new SecondAdapter(mSecondData , secondIndex);
            mSecondRecyclerView.setAdapter(secondAdapter);

            firstAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    currentFirstPosition=position;
                    firstAdapter.notifyDataSetChanged();
                    mSecondData.clear();
                    List<DataBean> newSecondData = mResultNew.getData().get(mFirstData.get(position));
                    for (int i = 0; i < newSecondData.size(); i++) {
                        mSecondData.add(newSecondData.get(i).getRound());
                    }
                    currentSecondPosition = array[position];
                    secondAdapter.notifyDataSetChanged();
                }
            });

            secondAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {

                    currentSecondPosition=position;
                    array[currentFirstPosition] = position;
                    secondAdapter.notifyDataSetChanged();
                }
            });

            view.findViewById(R.id.text_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlertDialog.dismiss();
                }
            });
            view.findViewById(R.id.text_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    url = BaseURLs.URL_FOOTBALL_DATABASE_SCHEDULE_UNFIRST; //选择后的URL

                    firstIndex = currentFirstPosition;
                    secondIndex = currentSecondPosition;

                    mLeagueGroup = mFirstData.get(currentFirstPosition);
                    mLeagueRound = mSecondData.get(currentSecondPosition);
                    arr = mResultNew.getData().get(mFirstData.get(currentFirstPosition));

                    handleHeadViewNew(arr,currentSecondPosition);
                    initData(false);
                    mAlertDialog.dismiss();
                }
            });

            mAlertDialog.show();
            mAlertDialog.getWindow().setContentView(view);

        }else{
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.football_scheduledialog, null);
            TextView titleView = (TextView) view.findViewById(R.id.headerView);
            titleView.setText(getString(R.string.football_database_details_stage));

            final List<String> data = new ArrayList<>();
            String[] mArray = new String[mRoundString.size()];
            for (int i = 0; i < mRoundString.size(); i++) {
                mArray[i] = mRoundString.get(i).getRound();
            }
            Collections.addAll(data, mArray);

            final SportsDialogAdapter mDialogAdapter = new SportsDialogAdapter(data, getContext(), currentPosition);

            final AlertDialog mAlertDialog = mBuilder.create();
            mAlertDialog.setCanceledOnTouchOutside(true);//设置空白处点击 dialog消失

            /**
             * 根据List数据条数加载不同的ListView （数据多加载可滑动 ScrollTouchListview）
             */
            ScrollView scrollview = (ScrollView) view.findViewById(R.id.basket_sports_scroll);//数据多时显示
            ListView listview = (ListView) view.findViewById(R.id.sport_date);//数据少时显示
            listview.setAdapter(mDialogAdapter);
            listview.setSelection(currentPosition);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    currentPosition = position;
                    mDialogAdapter.updateDatas(position);
                    mDialogAdapter.notifyDataSetChanged();
                    mAlertDialog.dismiss();

                    if (isCup) {
                        mLeagueRound = position + "";
                    }else{
                        mLeagueRound = position + 1 +"";
                    }
                    url = BaseURLs.URL_FOOTBALL_DATABASE_SCHEDULE_UNFIRST; //选择后的URL
                    handleHeadViewNew(mRoundString ,currentPosition);
                    initData(false);
                }
            });
            scrollview.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
            mAlertDialog.show();
            mAlertDialog.getWindow().setContentView(view);
        }
    }

    private void initRecycler() {
        mSections = new ArrayList<>();
        mAdapter = new BasketballDatabaseScheduleSectionAdapter(mSections);
        mAdapter.addHeaderView(mButtonFrame);
        mAdapter.setEmptyView(true, mEmptyView);

        mSectionsNew = new ArrayList<>();
        mAdapterNew = new FootballDatabaseScheduleSectionAdapter(mSectionsNew);
        mAdapterNew.setFootballTeamDetailsClickListener(footballTeamDetailsClickListener);
        mAdapterNew.addHeaderView(mButtonFrame);
        mAdapterNew.setEmptyView(true , mEmptyView);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setAdapter(mAdapterNew);
        mRecyclerView.setLayoutManager(manager);
    }

    public static FootballDatabaseScheduleNewFragment newInstance(DataBaseBean leagueBean, String season) {

        Bundle args = new Bundle();
        args.putParcelable(LEAGUE, leagueBean);
        args.putString(PARAM_DATE, season);
        FootballDatabaseScheduleNewFragment fragment = new FootballDatabaseScheduleNewFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public void setSeason(String season) {
        this.mLeagueDate = season;
        Bundle args = getArguments();
        if (args != null) args.putString(PARAM_DATE, season);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_button:
                if (isLoad) {
                    setDialog();
                }
                break;
            case R.id.left_button:
                if (isLoad) {
                    if (chooseSecond) {
                        loadLeft(currentSecondPosition);
                    }else{
                        loadLeft(currentPosition);
                    }
                }
                break;
            case R.id.right_button:
                if (isLoad) {
                    if (chooseSecond) {
                        loadRight(currentSecondPosition);
                    }else{
                        loadRight(currentPosition);
                    }
                }
                break;
        }

    }

    class FirstAdapter extends BaseQuickAdapter<String> {

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }
        public FirstAdapter(List<String> data , int position) {
            super(R.layout.item_match_stage_first, data);
            currentFirstPosition = position;
        }
        @Override
        protected void convert(BaseViewHolder holder, final String match) {
            final TextView textView = holder.getView(R.id.first_text);

            final int position = holder.getAdapterPosition();
            if (position == 0) {
                textView.setBackgroundResource(R.drawable.bg_item_match_stage_first_left);
            } else if (position == mData.size() - 1) {
                textView.setBackgroundResource(R.drawable.bg_item_match_stage_first_right);
            } else {
                textView.setBackgroundResource(R.drawable.bg_item_match_stage_first_normal);
            }

            if (position == currentFirstPosition) {
                textView.setSelected(true);
            }else{
                textView.setSelected(false);
            }

            textView.setText(match);

            if (mOnItemClickListener != null) {
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(position);
                    }
                });
            }
        }
    }

    class SecondAdapter extends BaseQuickAdapter<String> {

        private OnItemClickListener mOnItemClickListener;
        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }
        public SecondAdapter(List<String> data , int position) {
            super(R.layout.item_match_stage_second, data);
            currentSecondPosition = position;
        }
        @Override
        protected void convert(BaseViewHolder holder, final String match) {
            final TextView textView = holder.getView(R.id.second_text);
            final int position = holder.getAdapterPosition();
            if (position== currentSecondPosition) {
                textView.setSelected(true);
            }else{
                textView.setSelected(false);
            }
            textView.setText(match);
            if (mOnItemClickListener != null) {
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(position);
                    }
                });
            }
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    //点击内页跳转
    private FootballTeamDetailsClickListener footballTeamDetailsClickListener;
    public interface FootballTeamDetailsClickListener {
        void DetailsOnClick(View view, ScheduleDatasBean matchData);
    }
    private void setDetailsOnClick(){
        footballTeamDetailsClickListener = new FootballTeamDetailsClickListener() {
            @Override
            public void DetailsOnClick(View view, ScheduleDatasBean matchData) {
                    if (matchData.getGuestId() != null) {
                        if(HandMatchId.handId(getActivity(), matchData.getMatchId()+"")) {

                            Intent intent = new Intent(getActivity(), FootballMatchDetailActivity.class);
                            intent.putExtra("thirdId", matchData.getMatchId() + "");
                            intent.putExtra(FootBallDetailTypeEnum.CURRENT_TAB_KEY, FootBallDetailTypeEnum.FOOT_DETAIL_LIVE);
                            startActivity(intent);
                        }
                    }
            }
        };
    }
}
