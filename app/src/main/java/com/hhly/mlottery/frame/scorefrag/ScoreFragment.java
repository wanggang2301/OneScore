package com.hhly.mlottery.frame.scorefrag;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.BallType;
import com.hhly.mlottery.frame.tennisfrag.TennisBallScoreFragment;
import com.hhly.mlottery.util.FragmentUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreFragment extends Fragment {

    private View mView;
    private Context mContext;

    private int fragmentIndex = 0;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private List<Fragment> fragments = new ArrayList<>();

    private FootBallScoreFragment footBallScoreFragment;

    private BasketBallScoreFragment basketBallScoreFragment;

    private TennisBallScoreFragment tennisBallScoreFragment;

    private SnookerListScoreFragment snookerScoreFragment;

    private Activity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_score, container, false);
        mContext = mActivity;
        initView();
        return mView;
    }

    private void initView() {
        footBallScoreFragment = new FootBallScoreFragment();
        basketBallScoreFragment = new BasketBallScoreFragment();
        snookerScoreFragment = new SnookerListScoreFragment();
        tennisBallScoreFragment = new TennisBallScoreFragment();

        fragments.add(footBallScoreFragment);
        fragments.add(basketBallScoreFragment);
        fragments.add(snookerScoreFragment);
        fragments.add(tennisBallScoreFragment);

        /**
         * 默认先选择足球
         */
        switchFragment(BallType.FOOTBALL);
        PreferenceUtil.commitInt("matchType", BallType.FOOTBALL);
    }


    public void onEventMainThread(ScoreSwitchFg scoreSwitchFg) {

        switchFragment(scoreSwitchFg.getPosition());
    }


    public void switchFragment(int position) {
        fragmentIndex = position;// 当前fragment下标
        PreferenceUtil.commitInt("matchChoiceType", position);

        L.d("xxx", "当前Fragment下标：" + fragmentIndex);
        fragmentManager = getChildFragmentManager();
        currentFragment = FragmentUtils.switchFragment(fragmentManager, R.id.ly_content_score, currentFragment, fragments.get(position).getClass(), null, false, fragments.get(position).getClass().getSimpleName() + position, true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        EventBus.getDefault().post(new CloseWebSocketEventBus(hidden, fragmentIndex));

    }
}
