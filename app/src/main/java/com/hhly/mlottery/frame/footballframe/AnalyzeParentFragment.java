package com.hhly.mlottery.frame.footballframe;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.config.FootBallDetailTypeEnum;
import com.hhly.mlottery.util.FragmentUtils;
import com.hhly.mlottery.util.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangg
 * @name AnalyzeParentFragment
 * @date:2017/05/10 16:40
 * @des:足球内页分析fragment(由分析和情报组成)
 */
public class AnalyzeParentFragment extends Fragment {

    private final static String PARA_THIRDID = "thirdId";
    private final static String PARA_SELECTED = "seleted";
    private final static int INFO = 0;
    private final static int ANALYSE = 1;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.analyze_bigdata)
    RadioButton analyzeBigdata;
    @BindView(R.id.analyze)
    RadioButton analyze;
    private View mView;
    private String mThirdId;
    private List<Fragment> fragments;
    private Fragment currentFragment;
    private FragmentManager fragmentManager;
    private int currentTab = FootBallDetailTypeEnum.FOOT_DETAIL_ANALYSIS;  //默认分析

    //大数据
    private IntelligenceFragment mIntelligenceFragment;
    //分析
    private AnalyzeFragment mAnalyzeFragment;

    public AnalyzeParentFragment() {}

    public static AnalyzeParentFragment newInstance(String thirdId, int currentTab) {
        AnalyzeParentFragment analyzeParentFragment = new AnalyzeParentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARA_THIRDID, thirdId);
        bundle.putInt(PARA_SELECTED, currentTab);
        analyzeParentFragment.setArguments(bundle);
        return analyzeParentFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mThirdId = getArguments().getString(PARA_THIRDID);
            currentTab = getArguments().getInt(PARA_SELECTED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_analyze_parent, container, false);
        ButterKnife.bind(this, mView);
        initEvent();
        return mView;
    }


    private void initEvent() {
        mIntelligenceFragment = IntelligenceFragment.newInstance();
        mAnalyzeFragment = AnalyzeFragment.newInstance();

        fragments = new ArrayList<>();
        fragments.add(mIntelligenceFragment);
        fragments.add(mAnalyzeFragment);

        switch (currentTab) {
            case FootBallDetailTypeEnum.FOOT_DETAIL_INFOCENTER:
                analyze.setChecked(false);
                analyzeBigdata.setChecked(true);
                switchFragment(INFO);
                break;
            case FootBallDetailTypeEnum.FOOT_DETAIL_ANALYSIS:
                analyze.setChecked(true);
                analyzeBigdata.setChecked(false);
                switchFragment(ANALYSE);
                break;
            default:
                analyze.setChecked(true);
                analyzeBigdata.setChecked(false);
                switchFragment(ANALYSE);
                break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.analyze_bigdata:
                        switchFragment(INFO);
                        break;
                    case R.id.analyze:
                        switchFragment(ANALYSE);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void switchFragment(int position) {
        fragmentManager = getChildFragmentManager();
        currentFragment = FragmentUtils.switchFragment(fragmentManager, R.id.fl_analyse, currentFragment, fragments.get(position).getClass(), null, false, fragments.get(position).getClass().getSimpleName() + position, false);
    }

    public void initData() {
        mAnalyzeFragment.initData();// 分析下拉刷新
        mIntelligenceFragment.initData();// 情报刷新
    }
}
