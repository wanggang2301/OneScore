package com.hhly.mlottery.frame.tennisfrag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.TennisSettingActivity;
import com.hhly.mlottery.adapter.PureViewPagerAdapter;
import com.hhly.mlottery.base.BaseWebSocketFragment;
import com.hhly.mlottery.bean.tennisball.TennisEventBus;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.BaseUserTopics;
import com.hhly.mlottery.frame.BallType;
import com.hhly.mlottery.frame.scorefrag.CloseWebSocketEventBus;
import com.hhly.mlottery.frame.scorefrag.ScoreSwitchFg;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.widget.BallChoiceArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * desc:网球比分Frag
 * Created by 107_tangrr on 2017/2/20 0020.
 */

public class TennisBallScoreFragment extends BaseWebSocketFragment implements View.OnClickListener {

    private final int TENNIS_IMMEDIATE = 0;
    private final int TENNIS_RESULT = 1;
    private final int TENNIS_SCHEDULE = 2;
    private final int TENNIS_FOCUS = 3;

    private Activity mContext;
    private View mView;
    private String[] mItems;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private PureViewPagerAdapter pureViewPagerAdapter;
    private List<Fragment> fragments;
    public static List<String> titles;

    private LinearLayout d_header;
    private LinearLayout ll_match_select;

    private TextView tv_match_name;
    private ImageView iv_match;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setWebSocketUri(BaseURLs.WS_SERVICE);
//        setTopic("USER.topic.tennis.odd");
        setTopic(BaseUserTopics.oddsTennisMatch);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = View.inflate(mContext, R.layout.fragment_tennls_ball_score, null);

        initView();
        setupViewPager();
        initEvent();

        return mView;
    }

    private void initView() {
        mItems = getResources().getStringArray(R.array.bifen_select);

        mViewPager = (ViewPager) mView.findViewById(R.id.viewpager);

        mView.findViewById(R.id.public_btn_filter).setVisibility(View.INVISIBLE);
        mView.findViewById(R.id.public_btn_set).setVisibility(View.VISIBLE);
        mView.findViewById(R.id.public_btn_set).setOnClickListener(this);

        ll_match_select = (LinearLayout) mView.findViewById(R.id.ll_match_select);
        tv_match_name = (TextView) mView.findViewById(R.id.tv_match_name);
        iv_match = (ImageView) mView.findViewById(R.id.iv_match);
        d_header = (LinearLayout) mView.findViewById(R.id.d_heasder);

        tv_match_name.setText(getResources().getString(R.string.tennisball_txt));
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

    private void setupViewPager() {
        mTabLayout = (TabLayout) mView.findViewById(R.id.tabs);
        titles = new ArrayList<>();
        titles.add(getString(R.string.foot_jishi_txt));
        titles.add(getString(R.string.foot_saiguo_txt));
        titles.add(getString(R.string.foot_saicheng_txt));

        String focusId = PreferenceUtil.getString(AppConstants.TENNIS_BALL_FOCUS, null);
        if (!TextUtils.isEmpty(focusId)) {
            String[] focusIds = focusId.split(",");
            titles.add(getString(R.string.foot_details_focus) + "(" + focusIds.length + ")");
        } else {
            titles.add(getString(R.string.foot_details_focus));
        }

        fragments = new ArrayList<>();
        fragments.add(TennisBallSocketFragment.newInstance(TENNIS_IMMEDIATE));// 即时
        fragments.add(TennisBallTabFragment.newInstance(TENNIS_RESULT));// 赛果
        fragments.add(TennisBallTabFragment.newInstance(TENNIS_SCHEDULE));// 赛程
        fragments.add(TennisBallSocketFragment.newInstance(TENNIS_FOCUS));// 关注

        pureViewPagerAdapter = new PureViewPagerAdapter(fragments, titles, getChildFragmentManager());
        mViewPager.setAdapter(pureViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mViewPager.setOffscreenPageLimit(titles.size());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case TENNIS_IMMEDIATE:
                    case TENNIS_FOCUS:
                        // 重新请求数据
                        ((TennisBallSocketFragment) fragments.get(position)).refreshData();
                        break;
                    case TENNIS_RESULT:
                    case TENNIS_SCHEDULE:
                        // 刷新页面
                        ((TennisBallTabFragment) fragments.get(position)).refreshData();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        connectWebSocket();
    }

    @Override
    public void onPause() {
        super.onPause();
        closeWebSocket();
    }

    private void popWindow(final View v) {
        final View mView = View.inflate(mContext, R.layout.pop_select, null);
        // 创建ArrayAdapter对象
        BallChoiceArrayAdapter mAdapter = new BallChoiceArrayAdapter(mContext, mItems, BallType.TENNLS);

        ListView listview = (ListView) mView.findViewById(R.id.match_type);
        listview.setAdapter(mAdapter);


        final PopupWindow popupWindow = new PopupWindow(mView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.showAsDropDown(d_header);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                L.d("websocket123", ">>>>>>>>网球比分关闭");

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

    private void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    @Override

    protected void onTextResult(String text) {
        L.d("websocket123", "网球收到消息==" + text);
        if (TextUtils.isEmpty(text)) return;
        for (int i = 0; i < fragments.size(); i++) {
            switch (i) {
                case TENNIS_IMMEDIATE:
                case TENNIS_FOCUS:
                    ((TennisBallSocketFragment) fragments.get(i)).oddsDataChanger(text);
                    break;
                case TENNIS_RESULT:
                case TENNIS_SCHEDULE:
                    ((TennisBallTabFragment) fragments.get(i)).oddsDataChanger(text);
                    break;
            }
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeWebSocket();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(TennisEventBus event) {
        if (TextUtils.isEmpty(event.msg)) return;
        L.d("tennis", "event:" + event.msg);

        // 关注
        if ("tennisFocus".equals(event.msg)) {
            String focusId = PreferenceUtil.getString(AppConstants.TENNIS_BALL_FOCUS, null);
            if (!TextUtils.isEmpty(focusId)) {
                String[] focusIds = focusId.split(",");
                mTabLayout.getTabAt(TENNIS_FOCUS).setText(getString(R.string.foot_details_focus) + "(" + focusIds.length + ")");
            } else {
                mTabLayout.getTabAt(TENNIS_FOCUS).setText(getString(R.string.foot_details_focus));
            }
        }
        // 指数选择
        else if ("tennis_odds".equals(event.msg)) {
            for (int i = 0; i < fragments.size(); i++) {
                switch (i) {
                    case TENNIS_IMMEDIATE:
                    case TENNIS_FOCUS:
                        ((TennisBallSocketFragment) fragments.get(i)).oddsChanger();
                        break;
                    case TENNIS_RESULT:
                    case TENNIS_SCHEDULE:
                        ((TennisBallTabFragment) fragments.get(i)).oddsChanger();
                        break;
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.public_btn_set:
                Intent intent = new Intent(mContext, TennisSettingActivity.class);
                startActivity(intent);
                mContext.overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
                break;
        }
    }

    public void onEventMainThread(CloseWebSocketEventBus closeWebSocketEventBus) {

        if (closeWebSocketEventBus.isVisible()) {
            L.d("websocket123", "_________网球 比分 关闭 fg");
            closeWebSocket();
        } else {
            if (closeWebSocketEventBus.getIndex() == BallType.TENNLS) {
                L.d("websocket123", "_________网球 比分 打开 fg");
                connectWebSocket();
            }
        }
    }
}
