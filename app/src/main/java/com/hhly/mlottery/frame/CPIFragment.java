package com.hhly.mlottery.frame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.CpiFiltrateActivity;
import com.hhly.mlottery.activity.FootballActivity;
import com.hhly.mlottery.base.BaseWebSocketFragment;
import com.hhly.mlottery.bean.enums.OddsTypeEnum;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.bean.websocket.WebSocketCPIResult;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.oddfragment.CPIOddsFragment;
import com.hhly.mlottery.frame.oddfragment.CompanyChooseDialogFragment;
import com.hhly.mlottery.frame.oddfragment.DateChooseDialogFragment;
import com.hhly.mlottery.util.DateUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/6/21.
 */
public class CPIFragment extends BaseWebSocketFragment {

    private static final int startFilterRequestCode = 10086;

    TextView mLeftTitle; // 左侧标题

    ImageView mBackButton; // 返回按钮

    LinearLayout mDateLayout; // 日期布局
    TextView mDateTextView; // 日期 TextView

    ImageView mCompanyButton, mFilterButton; // 公司和筛选按钮

    TabLayout mTabLayout; // tabLayout
    SwipeRefreshLayout mRefreshLayout; // SwipeRefreshLayout
    ViewPager mViewPager; // viewPage

    private List<CPIOddsFragment> mFragments; // Fragments
    private ArrayList<NewOddsInfo.CompanyBean> companyList; // 公司数据源
    private LinkedList<String> filterList; // 过滤信息数据源

    private DateChooseDialogFragment mDateChooseDialogFragment; // 日期选择
    private CompanyChooseDialogFragment mCompanyChooseDialogFragment; // 公司选择

    private String currentDate; // 当前日期
    private String choosenDate; // 选中日期

//    private URI socketUri;
//    private HappySocketClient socketClient; // WebSocket 客户端

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTopic("USER.topic.indexcenter");
        setWebSocketUri(BaseURLs.WS_SERVICE);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cpi, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 创建公司 List
        companyList = new ArrayList<>();
//        filterList = new LinkedList<>();

        // 隐藏中间标题
        hideView(view, R.id.public_txt_title);
        // 隐藏 筛选、设置、热门隐藏
        hideView(view, R.id.public_btn_filter);
        hideView(view, R.id.public_btn_set);
        hideView(view, R.id.public_img_hot);

        // 显示左侧标题
        mLeftTitle = (TextView) view.findViewById(R.id.public_txt_left_title);
        mLeftTitle.setVisibility(View.VISIBLE);
        mLeftTitle.setText(R.string.football_detail_odds_tab);

        // 返回
        mBackButton = (ImageView) view.findViewById(R.id.public_img_back);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    ((FootballActivity) getActivity()).eventBusPost();
                    getActivity().finish();
                }
            }
        });

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
//        mCompanyButton.setVisibility(View.VISIBLE);
        mCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCompanyChooseDialog();
            }
        });
        mFilterButton = (ImageView) view.findViewById(R.id.public_img_filter);
//        mFilterButton.setVisibility(View.VISIBLE);
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
//                refreshAllChildFragments();
            }
        });

//        startWebSocket();
        connectWebSocket();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != startFilterRequestCode
                || resultCode != Activity.RESULT_CANCELED
                || data == null) {
            return;
        }
        ArrayList<String> checkedIdList = (ArrayList<String>) data.getSerializableExtra("key");
        if (filterList == null) filterList = new LinkedList<>();
        filterList.clear();
        filterList.addAll(checkedIdList);
        for (CPIOddsFragment fragment : mFragments) {
            fragment.updateFilterData();
        }
    }

//    /**
//     * 开启 webSocket
//     */
//    private void startWebSocket() {
//        try {
//            socketUri = new URI(BaseURLs.URL_CPI_SOCKET);
//            socketClient = new HappySocketClient(socketUri, new Draft_17());
//            socketClient.setSocketResponseCloseListener(this);
//            socketClient.setSocketResponseErrorListener(this);
//            socketClient.setSocketResponseMessageListener(this);
//            socketClient.connect();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }

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
    public CPIOddsFragment getCurrentFragment() {
        return mFragments.get(mViewPager.getCurrentItem());
    }

    /**
     * 刷新所有子 fragment
     */
    public void refreshAllChildFragments() {
//        mFragments.get(mViewPager.getCurrentItem()).refreshData(currentDate);
        for (CPIOddsFragment fragment : mFragments) {
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
        mFragments.add(CPIOddsFragment.newInstance(OddsTypeEnum.PLATE));
        mFragments.add(CPIOddsFragment.newInstance(OddsTypeEnum.BIG));
        mFragments.add(CPIOddsFragment.newInstance(OddsTypeEnum.OP));

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
                            for (CPIOddsFragment fragment : mFragments) {
                                fragment.updateFilterData();
                            }
                        }
                    });
        }
    }

    public static CPIFragment newInstance() {
        return new CPIFragment();
    }

//    @Override
//    public void onMessage(String message) {
//        if (message.startsWith("CONNECTED")) {
//            String id = "android" + DeviceInfo.getDeviceId(MyApp.getContext());
//            id = MD5Util.getMD5(id);
//            // USER.topic.indexcenter
//            socketClient.send("SUBSCRIBE\nid:" + id + "\ndestination:/topic/USER.topic.indexcenter" + "\n\n");
//        } else if (message.startsWith("MESSAGE")) {
//            final String jsonString = message.substring(message.indexOf("{"), message.lastIndexOf("}") + 1);
//            mTabLayout.post(new Runnable() {
//                @Override
//                public void run() {
//                    handleMessage(jsonString);
//                }
//            });
//        }
//    }

//    @Override
//    public void onError(Exception exception) {
//        exception.printStackTrace();
//    }
//
//    @Override
//    public void onClose(String message) {
//        startWebSocket();
//    }

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
        for (CPIOddsFragment fragment : mFragments) {
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
        for (CPIOddsFragment fragment : mFragments) {
            fragment.updateOdds(result);
        }
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
        for (CPIOddsFragment fragment : mFragments) {
            fragment.updateTimeAndStatus(result);
        }
    }

    public void showRightButton() {
        mCompanyButton.setVisibility(View.VISIBLE);
        mFilterButton.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onTextResult(final String text) {
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
        public CPIOddsFragment getItem(int position) {
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
}
