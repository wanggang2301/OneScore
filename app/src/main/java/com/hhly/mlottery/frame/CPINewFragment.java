package com.hhly.mlottery.frame;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.websocket.WebSocketCPIResult;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.oddfragment.CPIOddsListFragment;
import com.hhly.mlottery.frame.oddfragment.DateChooseDialogFragment;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.cipher.MD5Util;
import com.hhly.mlottery.util.websocket.HappySocketClient;

import org.java_websocket.drafts.Draft_17;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * 重构版 CPIFragment
 * <p/>
 * Created by loshine on 2016/6/21.
 */
public class CPINewFragment extends Fragment implements
        HappySocketClient.SocketResponseCloseListener,
        HappySocketClient.SocketResponseErrorListener,
        HappySocketClient.SocketResponseMessageListener {

    TextView mLeftTitle; // 左侧标题

    ImageView mBackButton; // 返回按钮

    LinearLayout mDateLayout; // 日期布局
    TextView mDateTextView; // 日期 TextView

    ImageView mCompanyButton, mFilterButton; // 公司和筛选按钮

    TabLayout mTabLayout; // tabLayout
    SwipeRefreshLayout mRefreshLayout; // SwipeRefreshLayout
    ViewPager mViewPager; // viewPage

    private List<CPIOddsListFragment> mFragments; // Fragments
    private DateChooseDialogFragment mDateChooseDialogFragment; // 日期选择

    private String currentDate; // 当前日期
    private String choosenDate; // 选中日期

    private URI socketUri;
    private HappySocketClient socketClient; // WebSocket 客户端

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cpi, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        mCompanyButton.setVisibility(View.VISIBLE);
        mCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : 公司筛选
                socketClient.close();
            }
        });
        mFilterButton = (ImageView) view.findViewById(R.id.public_img_filter);
        mFilterButton.setVisibility(View.VISIBLE);
        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : 联赛筛选
                socketClient.connect();
            }
        });

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.cpi_refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshAllChildFragments();
            }
        });

        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mViewPager = (ViewPager) view.findViewById(R.id.cpi_viewpager);

        initViewPager();

        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
                refreshAllChildFragments();
            }
        });

        startWebSocket();
    }

    /**
     * 开启 webSocket
     */
    private void startWebSocket() {
        try {
            socketUri = new URI(BaseURLs.URL_CPI_SOCKET);
            socketClient = new HappySocketClient(socketUri, new Draft_17());
            socketClient.setSocketResponseCloseListener(this);
            socketClient.setSocketResponseErrorListener(this);
            socketClient.setSocketResponseMessageListener(this);
            socketClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void showDateChooseDialog() {
        maybeInitDateChooseDialog();
        mDateChooseDialogFragment.show(getChildFragmentManager(), "dateChooseFragment");
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
                        mDateTextView.setText(date);
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
            mDateTextView.setText(currentDate);
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
    public CPIOddsListFragment getCurrentFragment() {
        return mFragments.get(mViewPager.getCurrentItem());
    }

    public void refreshAllChildFragments() {
//        mFragments.get(mViewPager.getCurrentItem()).refreshData(currentDate);
        for (CPIOddsListFragment fragment : mFragments) {
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
        mFragments.add(CPIOddsListFragment.newInstance(CPIOddsListFragment.TYPE_PLATE));
        mFragments.add(CPIOddsListFragment.newInstance(CPIOddsListFragment.TYPE_BIG));
        mFragments.add(CPIOddsListFragment.newInstance(CPIOddsListFragment.TYPE_OP));

        CPIPagerAdapter pagerAdapter = new CPIPagerAdapter(getChildFragmentManager());
        mViewPager.setOffscreenPageLimit(3);//设置预加载页面的个数。
        mViewPager.setAdapter(pagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void hideView(View view, @IdRes int idRes) {
        view.findViewById(idRes).setVisibility(View.GONE);
    }

    public static CPINewFragment newInstance() {
        return new CPINewFragment();
    }

    @Override
    public void onMessage(String message) {

        if (message.startsWith("CONNECTED")) {
            String id = "android" + DeviceInfo.getDeviceId(getActivity());
            id = MD5Util.getMD5(id);
            // USER.topic.indexcenter
            socketClient.send("SUBSCRIBE\nid:" + id + "\ndestination:/topic/USER.topic.indexcenter" + "\n\n");
        } else if (message.startsWith("MESSAGE")) {
            final String jsonString = message.substring(message.indexOf("{"), message.lastIndexOf("}") + 1);
            Log.d("CPINewFragment", jsonString);
            mTabLayout.post(new Runnable() {
                @Override
                public void run() {
                    handleMessage(jsonString);
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
        for (CPIOddsListFragment fragment : mFragments) {
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
        for (CPIOddsListFragment fragment : mFragments) {
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
//            jsonString = "{'data':{'keepTime':49,'statusOrigin':3},'thirdId':'338827','type':1}  ";
        WebSocketCPIResult<WebSocketCPIResult.UpdateTimeAndStatus> result =
                WebSocketCPIResult.getTimeAndStatusFromJson(jsonString);
        // 更新三个 Fragment 中对应的 List 中的对应 thirdId 的比赛的数据
        for (CPIOddsListFragment fragment : mFragments) {
            fragment.updateTimeAndStatus(result);
        }
    }

    @Override
    public void onError(Exception exception) {
        exception.printStackTrace();
    }

    @Override
    public void onClose(String message) {
        Log.d("CPINewFragment", "webSocket has been closed!");
        Log.d("CPINewFragment", message);
        startWebSocket();
    }

    /**
     * ViewPager 适配器
     */
    class CPIPagerAdapter extends FragmentStatePagerAdapter {

        public CPIPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CPIOddsListFragment getItem(int position) {
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
