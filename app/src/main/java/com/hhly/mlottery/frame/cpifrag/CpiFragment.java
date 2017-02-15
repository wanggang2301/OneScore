package com.hhly.mlottery.frame.cpifrag;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.scorefrag.ScoreSwitchFg;
import com.hhly.mlottery.frame.scorefrag.SnookerScoreFragment;
import com.hhly.mlottery.util.FragmentUtils;
import com.hhly.mlottery.util.L;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 指数
 */
public class CpiFragment extends Fragment {

    private View mView;
    private Context mContext;

    private int fragmentIndex = 0;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private List<Fragment> fragments = new ArrayList<>();

    private FootCpiFragment footCpiFragment;

    private SnookerScoreFragment basketBallScoreFragment;

    private SnookerScoreFragment snookerScoreFragment;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_cpi2, container, false);
        mContext = getActivity();
        initView();
        return mView;
    }

    private void initView() {
        basketBallScoreFragment = new SnookerScoreFragment();
        snookerScoreFragment = new SnookerScoreFragment();

        fragments.add(FootCpiFragment.newInstance());
      //  fragments.add(basketBallScoreFragment);
        //  fragments.add(snookerScoreFragment);
        switchFragment(0);
    }


    public void onEventMainThread(ScoreSwitchFg scoreSwitchFg) {
        switchFragment(scoreSwitchFg.getPosition());
    }


    public void switchFragment(int position) {
        fragmentIndex = position;// 当前fragment下标
        L.d("xxx", "当前Fragment下标：" + fragmentIndex);
        fragmentManager = getChildFragmentManager();
        currentFragment = FragmentUtils.switchFragment(fragmentManager, R.id.ly_content_cpi, currentFragment, fragments.get(position).getClass(), null, false, fragments.get(position).getClass().getSimpleName() + position, true);
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
}
