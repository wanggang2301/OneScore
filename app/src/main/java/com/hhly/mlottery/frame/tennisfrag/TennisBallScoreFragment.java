package com.hhly.mlottery.frame.tennisfrag;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.hhly.mlottery.adapter.PureViewPagerAdapter;
import com.hhly.mlottery.base.BaseWebSocketFragment;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.scorefrag.ScoreSwitchFg;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.widget.BallChoiceArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * desc:网球比分Frag
 * Created by 107_tangrr on 2017/2/20 0020.
 */

public class TennisBallScoreFragment extends BaseWebSocketFragment {

    private static final int FOOTBALL = 0;
    private static final int BASKETBALL = 1;
    private static final int TENNLS = 3;

    private static final int TENNIS_IMMEDIATE = 0;
    private static final int TENNIS_RESULT = 1;
    private static final int TENNIS_SCHEDULE = 2;
    private static final int TENNIS_FOCUS = 3;

    private Context mContext;
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
        setTopic("USER.topic.tennis.score");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
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
        mView.findViewById(R.id.public_btn_set).setVisibility(View.INVISIBLE);

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
        titles.add(getString(R.string.foot_details_focus));

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

    private void popWindow(final View v) {
        final View mView = View.inflate(mContext, R.layout.pop_select, null);
        // 创建ArrayAdapter对象
        BallChoiceArrayAdapter mAdapter = new BallChoiceArrayAdapter(mContext, mItems, TENNLS);

        ListView listview = (ListView) mView.findViewById(R.id.match_type);
        listview.setAdapter(mAdapter);


        final PopupWindow popupWindow = new PopupWindow(mView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.showAsDropDown(d_header);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // tv_match_name.setText(((TextView) view.findViewById(R.id.tv)).getText().toString());
                //  iv_match.setImageResource(R.mipmap.nav_icon_cbb);
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
        L.d("xxxxx", "网球推送消息： " + text);
        ((TennisBallSocketFragment) fragments.get(TENNIS_IMMEDIATE)).socketDataChanged(text);
        ((TennisBallSocketFragment) fragments.get(TENNIS_FOCUS)).socketDataChanged(text);
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
    }
}
