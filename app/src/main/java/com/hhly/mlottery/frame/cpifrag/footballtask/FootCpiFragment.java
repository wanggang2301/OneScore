package com.hhly.mlottery.frame.cpifrag.footballtask;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.CpiFiltrateActivity;
import com.hhly.mlottery.base.BaseWebSocketFragment;
import com.hhly.mlottery.bean.enums.OddsTypeEnum;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.bean.websocket.WebSocketCPIResult;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.BaseUserTopics;
import com.hhly.mlottery.config.FootBallMatchFilterTypeEnum;
import com.hhly.mlottery.frame.BallType;
import com.hhly.mlottery.frame.cpifrag.CloseCpiWebSocketEventBus;
import com.hhly.mlottery.frame.oddfragment.CompanyChooseDialogFragment;
import com.hhly.mlottery.frame.oddfragment.DateChooseDialogFragment;
import com.hhly.mlottery.frame.scorefrag.ScoreSwitchFg;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.widget.BallChoiceArrayAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class FootCpiFragment extends BaseWebSocketFragment {


    /*  private static final int FOOTBALL = 0;
      private static final int BASKETBALL = 1;
      private static final int SNOOKER = 2;*/
    private static final int startFilterRequestCode = 10086;


    private View mViewLnflater;
    private Context mContext;
    LinearLayout mDateLayout; // 日期布局
    TextView mDateTextView; // 日期 TextView

    ImageView mCompanyButton, mFilterButton; // 公司和筛选按钮

    TabLayout mTabLayout; // tabLayout
    SwipeRefreshLayout mRefreshLayout; // SwipeRefreshLayout
    ViewPager mViewPager; // viewPage

    private List<CPIOddsFragment2> mFragments; // Fragments
    private ArrayList<NewOddsInfo.CompanyBean> companyList; // 公司数据源
    private LinkedList<String> filterList; // 过滤信息数据源

    private DateChooseDialogFragment mDateChooseDialogFragment; // 日期选择
    private CompanyChooseDialogFragment mCompanyChooseDialogFragment; // 公司选择

    private String currentDate; // 当前日期
    private String choosenDate; // 选中日期

    private LinearLayout d_header;
    private LinearLayout ll_match_select;

    private TextView tv_match_name;
    private ImageView iv_match;
    private String[] mItems;


    public static FootCpiFragment newInstance() {
        return new FootCpiFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        setTopic("USER.topic.indexcenter");
        setTopic(BaseUserTopics.indexFootball);
        setWebSocketUri(BaseURLs.WS_SERVICE);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewLnflater = inflater.inflate(R.layout.fragment_foot_cpi, container, false);

        mContext = getActivity();
        return mViewLnflater;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 创建公司 List
        companyList = new ArrayList<>();
//        filterList = new LinkedList<>();


        ll_match_select = (LinearLayout) view.findViewById(R.id.ll_match_select);
        tv_match_name = (TextView) view.findViewById(R.id.tv_match_name);
        iv_match = (ImageView) view.findViewById(R.id.iv_match);
        d_header = (LinearLayout) view.findViewById(R.id.d_heasder);
        tv_match_name.setText(getResources().getString(R.string.football_txt));

        mItems = getResources().getStringArray(R.array.zhishu_select);


        // 显示时间的布局和 TextView
        mDateLayout = (LinearLayout) view.findViewById(R.id.public_date_layout);
        mDateTextView = (TextView) view.findViewById(R.id.public_txt_date);
        mDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateChooseDialog();
            }
        });

        // 右上角公司和联赛筛选
        mCompanyButton = (ImageView) view.findViewById(R.id.public_img_company);
        mCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCompanyChooseDialog();
            }
        });
        mFilterButton = (ImageView) view.findViewById(R.id.public_img_filter);
        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CpiFiltrateActivity.class);
                intent.putExtra("fileterTags", (Serializable) getCurrentFragment().getFilterTagList());
                intent.putExtra("linkedListChecked", filterList);
                startActivityForResult(intent, startFilterRequestCode);
            }
        });

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.cpi_refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshAllChildFragments();
                connectWebSocket();
            }
        });

        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mViewPager = (ViewPager) view.findViewById(R.id.cpi_viewpager);

        initViewPager();

        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
            }
        });

        initEvent();
        connectWebSocket();
    }


    private void initEvent() {
        ll_match_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_match.setImageResource(R.mipmap.nav_icon_up);
                backgroundAlpha(getActivity(), 0.5f);
                popWindow(v);
            }
        });


    }

    private void popWindow(final View v) {
        final View mView = View.inflate(mContext, R.layout.pop_select, null);
        // 创建ArrayAdapter对象
        BallChoiceArrayAdapter mAdapter = new BallChoiceArrayAdapter(mContext, mItems, BallType.FOOTBALL);

        ListView listview = (ListView) mView.findViewById(R.id.match_type);
        listview.setAdapter(mAdapter);


        final PopupWindow popupWindow = new PopupWindow(mView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.showAsDropDown(d_header);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                L.d("websocket123", ">>>>>>>>足球指数关闭");

                closeWebSocket();
                EventBus.getDefault().post(new ScoreSwitchFg(position));

                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                iv_match.setImageResource(R.mipmap.nav_icon_cbb);
                backgroundAlpha(getActivity(), 1f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != startFilterRequestCode || resultCode != Activity.RESULT_CANCELED || data == null) {
            return;
        }
        ArrayList<String> checkedIdList = (ArrayList<String>) data.getSerializableExtra("key");
        if (filterList == null) filterList = new LinkedList<>();
        filterList.clear();
        filterList.addAll(checkedIdList);

        PreferenceUtil.setDataList(FootBallMatchFilterTypeEnum.FOOT_INDEX, filterList);

        for (CPIOddsFragment2 fragment : mFragments) {
            fragment.updateFilterData();
        }
    }

    /**
     * 显示日期选择 dialog
     */
    private void showDateChooseDialog() {
        maybeInitDateChooseDialog();
        if (!mDateChooseDialogFragment.isVisible()) {
            mDateChooseDialogFragment.show(getChildFragmentManager(), "dateChooseFragment");
        }
    }

    /**
     * 获取公司列表
     *
     * @return 公司列表
     */
    public ArrayList<NewOddsInfo.CompanyBean> getCompanyList() {
        return companyList;
    }

    /**
     * 获取过滤信息
     *
     * @return 过滤信息
     */
    public LinkedList<String> getFilterList() {
        return filterList;
    }

    /**
     * 日期选择 Fragment 初始化
     */
    private void maybeInitDateChooseDialog() {
        if (mDateChooseDialogFragment != null) return;
        mDateChooseDialogFragment = DateChooseDialogFragment.newInstance(currentDate,
                new DateChooseDialogFragment.OnDateChooseListener() {
                    @Override
                    public void onDateChoose(String date) {
                        choosenDate = date;
                        mDateTextView.setText(DateUtil.convertDateToNation(date));
                        setRefreshing(true);
                        refreshAllChildFragments();
                    }
                });
    }

    /**
     * 设置当前日期
     *
     * @param currentDate currentDate, 形如 2016-06-23
     */
    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
        if (TextUtils.isEmpty(choosenDate)) {

            mDateTextView.setText(DateUtil.convertDateToNation(currentDate));


            if (!mDateLayout.isShown()) mDateLayout.setVisibility(View.VISIBLE);
            if (!mDateTextView.isShown()) mDateTextView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置刷新状态
     *
     * @param b 是否正在刷新
     */
    public void setRefreshing(boolean b) {
        mRefreshLayout.setRefreshing(b);
    }

    /**
     * 获取当前前台显示的 ViewPager 中的 Fragment
     *
     * @return CPIOddsListFragment
     */
    public CPIOddsFragment2 getCurrentFragment() {
        return mFragments.get(mViewPager.getCurrentItem());
    }

    /**
     * 刷新所有子 fragment
     */
    public void refreshAllChildFragments() {
//        mFragments.get(mViewPager.getCurrentItem()).refreshData(currentDate);
        for (CPIOddsFragment2 fragment : mFragments) {
            // 未选日期的时候 choosenDate 为null，则请求当天数据
            // 选择日期之后 choosenDate 为选择的日期，请求选择日期的数据
            fragment.refreshData(choosenDate);
        }
    }

    /**
     * 初始化 ViewPager
     */
    private void initViewPager() {
        mFragments = new ArrayList<>();
        mFragments.add(CPIOddsFragment2.newInstance(OddsTypeEnum.PLATE));
        mFragments.add(CPIOddsFragment2.newInstance(OddsTypeEnum.BIG));
        mFragments.add(CPIOddsFragment2.newInstance(OddsTypeEnum.OP));

        CPIPagerAdapter pagerAdapter = new CPIPagerAdapter(getChildFragmentManager());
        mViewPager.setOffscreenPageLimit(3);//设置预加载页面的个数。
        mViewPager.setAdapter(pagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * 隐藏 View
     *
     * @param view  view
     * @param idRes id
     */
    private void hideView(View view, @IdRes int idRes) {
        view.findViewById(idRes).setVisibility(View.GONE);
    }

    /**
     * 显示公司选择
     */
    public void showCompanyChooseDialog() {
        maybeInitCompanyChooseDialog();
        if (!mCompanyChooseDialogFragment.isVisible()) {
            mCompanyChooseDialogFragment.show(getChildFragmentManager(), "companyChooseDialog");
        }

    }

    /**
     * 初始化公司选择Dialog
     */
    private void maybeInitCompanyChooseDialog() {
        if (mCompanyChooseDialogFragment == null) {
            mCompanyChooseDialogFragment = CompanyChooseDialogFragment.newInstance(companyList,
                    new CompanyChooseDialogFragment.OnFinishSelectionListener() {
                        @Override
                        public void onFinishSelection() {
                            for (CPIOddsFragment2 fragment : mFragments) {
                                fragment.updateFilterData();
                            }
                        }
                    });
        }
    }


    /**
     * 处理 websocket 传来的数据
     *
     * @param jsonString jsonString
     */
    private void handleMessage(String jsonString) {

        JSONObject jsonObject = JSON.parseObject(jsonString);
        int type = jsonObject.getIntValue("type");
        // 根据 type 判断推送数据类型，1 - 时间和状态，2 - 赔率数据，3 - 比分
        if (type == WebSocketCPIResult.TYPE_TIME_STATUS) {
            updateTimeAndStatus(jsonString);
        } else if (type == WebSocketCPIResult.TYPE_ODDS) {
            updateOdds(jsonString);
        } else if (type == WebSocketCPIResult.TYPE_SCORE) {
            updateScore(jsonString);
        }
    }

    /**
     * 更新比分数据
     *
     * @param jsonString jsonString
     */
    private void updateScore(String jsonString) {
        //比分模拟推送
//            jsonString = "{'data':{'matchResult':'80:80'},'thirdId':'337551','type':3}  ";
        WebSocketCPIResult<WebSocketCPIResult.UpdateScore> result =
                WebSocketCPIResult.getScoreFromJson(jsonString);
        // 更新三个 Fragment 中对应的 List 中的对应 thirdId 的比赛的数据
        for (CPIOddsFragment2 fragment : mFragments) {
            fragment.updateScore(result);
        }
    }

    /**
     * 更新赔率数据
     *
     * @param jsonString jsonString
     */
    private void updateOdds(String jsonString) {
        //赔率模拟数据
//            jsonString = "{'data':[{'comId':'3','leftOdds':'0.25','mediumOdds':'1.75','oddType':'2','rightOdds':'0.25','uptime':'18:40'}],'thirdId':'339608','type':2}  ";
        WebSocketCPIResult<List<WebSocketCPIResult.UpdateOdds>> result =
                WebSocketCPIResult.getOddsFromJson(jsonString);
        // 更新三个 Fragment 中对应的 List 中的对应 thirdId 的比赛的数据
        for (CPIOddsFragment2 fragment : mFragments) {
            fragment.updateOdds(result);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        closeWebSocket();
    }

    /**
     * 更新时间和状态
     *
     * @param jsonString jsonString
     */
    private void updateTimeAndStatus(String jsonString) {
        //时间模拟数据
//        jsonString = "{'data':{'keepTime':49,'statusOrigin':3},'thirdId':'349114','type':1}  ";
        WebSocketCPIResult<WebSocketCPIResult.UpdateTimeAndStatus> result =
                WebSocketCPIResult.getTimeAndStatusFromJson(jsonString);
        // 更新三个 Fragment 中对应的 List 中的对应 thirdId 的比赛的数据
        for (CPIOddsFragment2 fragment : mFragments) {
            fragment.updateTimeAndStatus(result);
        }
    }

    public void showRightButton() {
        mCompanyButton.setVisibility(View.VISIBLE);
        mFilterButton.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onTextResult(final String text) {

        L.d("websocket123", "_______足球指数推送==" + text);


        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                handleMessage(text);
            }
        });
    }

    @Override
    protected void onConnectFail() {

    }

    @Override
    protected void onDisconnected() {

    }

    @Override
    protected void onConnected() {

    }

    /**
     * ViewPager 适配器
     */
    class CPIPagerAdapter extends FragmentStatePagerAdapter {

        public CPIPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CPIOddsFragment2 getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.odd_plate_rb_txt);
                case 1:
                    return getString(R.string.asiasize);
                case 2:
                    return getString(R.string.odd_op_rb_txt);
                default:
                    return getString(R.string.odd_plate_rb_txt);
            }
        }
    }

    public void onEventMainThread(CloseCpiWebSocketEventBus closeWebSocketEventBus) {

        if (closeWebSocketEventBus.isVisible()) {
            L.d("websocket123", "_______足球 指数 关闭 fg");
            closeWebSocket();
        } else {
            if (closeWebSocketEventBus.getIndex() == BallType.FOOTBALL) {
                L.d("websocket123", "______足球 指数 打开 fg");

                connectWebSocket();
            }
        }
    }
}