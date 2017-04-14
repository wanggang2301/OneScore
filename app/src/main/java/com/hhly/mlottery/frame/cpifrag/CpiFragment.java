package com.hhly.mlottery.frame.cpifrag;


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
import com.hhly.mlottery.frame.cpifrag.SnookerIndex.SIndexFragment;
import com.hhly.mlottery.frame.cpifrag.basketballtask.BasketBallCpiFrament;
import com.hhly.mlottery.frame.cpifrag.footballtask.FootCpiFragment;
import com.hhly.mlottery.frame.scorefrag.ScoreSwitchFg;
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

    private Activity mActivity;

    Bundle arg1;
    Bundle arg2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cpi2, container, false);
        mContext = mActivity;
        initView();
        return mView;
    }

    private void initView() {
        fragments.add(FootCpiFragment.newInstance());
        fragments.add(BasketBallCpiFrament.newInstace());
        arg1 = new Bundle();
        arg2 = new Bundle();
        arg1.putInt("param1", BallType.SNOOKER);
        arg2.putInt("param1", BallType.TENNLS);

        fragments.add(SIndexFragment.newInstance());
        fragments.add(SIndexFragment.newInstance());
        switchFragment(BallType.FOOTBALL);
    }

    public void onEventMainThread(ScoreSwitchFg scoreSwitchFg) {
        switchFragment(scoreSwitchFg.getPosition());
    }


    public void switchFragment(int position) {
        fragmentIndex = position;// 当前fragment下标
        L.d("xxx", "当前Fragment下标：" + fragmentIndex);
        fragmentManager = getChildFragmentManager();
        if (position == BallType.SNOOKER) {
            currentFragment = FragmentUtils.switchFragment(fragmentManager, R.id.ly_content_cpi, currentFragment, fragments.get(position).getClass(), arg1, false, fragments.get(position).getClass().getSimpleName() + position, true);
        } else if (position == BallType.TENNLS) {
            currentFragment = FragmentUtils.switchFragment(fragmentManager, R.id.ly_content_cpi, currentFragment, fragments.get(position).getClass(), arg2, false, fragments.get(position).getClass().getSimpleName() + position, true);
        } else {
            currentFragment = FragmentUtils.switchFragment(fragmentManager, R.id.ly_content_cpi, currentFragment, fragments.get(position).getClass(), null, false, fragments.get(position).getClass().getSimpleName() + position, true);
        }
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
        EventBus.getDefault().post(new CloseCpiWebSocketEventBus(hidden, fragmentIndex));
    }
}
