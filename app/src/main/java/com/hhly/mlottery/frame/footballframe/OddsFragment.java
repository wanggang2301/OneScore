package com.hhly.mlottery.frame.footballframe;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.enums.OddsTypeEnum;
import com.hhly.mlottery.frame.oddfragment.FootballPlateDetailsFragment;
import com.hhly.mlottery.frame.oddfragment.FootballPlateFragment;
import com.umeng.analytics.MobclickAgent;

/**
 * 足球内页改版指数fragment
 * tjl
 */
public class OddsFragment extends Fragment {

    private Context mContext;
    private RadioGroup mRadioGroup;

    private FragmentManager fragmentManager;
    //    private Fragment fragment;
    private FootballPlateFragment mPlateFragment;
    private FootballPlateDetailsFragment mDetailsFragment;
    private boolean isVisible;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        return inflater.inflate(R.layout.fragment_odds, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitView(view);
    }

    public OddsFragment(){}

    public static OddsFragment newInstance() {
        OddsFragment fragment = new OddsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void InitView(View view) {
        mRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    // 亚盘
                    case R.id.odd_plate_btn:
                        MobclickAgent.onEvent(mContext, "Football_MatchData_OddPlateBtn");
                        mPlateFragment = FootballPlateFragment.newInstance(OddsTypeEnum.PLATE);
                        break;
                    // 大小球
                    case R.id.odd_big_btn:
                        MobclickAgent.onEvent(mContext, "Football_MatchData_OddBigBtn");
                        mPlateFragment = FootballPlateFragment.newInstance(OddsTypeEnum.BIG);
                        break;
                    // 欧盘
                    case R.id.odd_op_btn:
                        MobclickAgent.onEvent(mContext, "Football_MatchData_OddOpBtn");
                        mPlateFragment = FootballPlateFragment.newInstance(OddsTypeEnum.OP);
                        break;
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.odd_content_fragment, mPlateFragment)
                        .commit();
            }
        });

        mPlateFragment = FootballPlateFragment.newInstance(OddsTypeEnum.PLATE);
        fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.odd_content_fragment, mPlateFragment).commit();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //如果是显示
        //相当于Fragment的onResume
        //如果是隐藏
        //相当于Fragment的onPause
        isVisible = isVisibleToUser;

        if(isVisibleToUser){
            mPlateFragment.loadData();
        }
    }

    /**
     * 刷新指数
     */
    public void oddPlateRefresh() {
        //如果是显示才让刷新
        if (isVisible && mPlateFragment.isVisible()) {
            mPlateFragment.loadData();
        }
    }

    /**
     * 显示详情
     *
     * @param fragment fragment
     */
    public void showDetails(@NonNull FootballPlateDetailsFragment fragment) {
        mDetailsFragment = fragment;
        getChildFragmentManager().beginTransaction()
                .replace(R.id.odd_content_fragment, mDetailsFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * 显示列表
     */
    public void showList() {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.odd_content_fragment, mPlateFragment)
                .commit();
    }
}
