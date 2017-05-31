package com.hhly.mlottery.frame.footballframe.bowl.fragmentparent;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.base.BaseWebSocketFragment;
import com.hhly.mlottery.frame.footballframe.bowl.fragmentchild.BowlChildFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangg
 * @desc 足球内页滚球
 * @date 2017/5/28
 */
public class BowlFragment extends BaseWebSocketFragment {

    private static final int OUER = 0;
    private static final int ALET = 1;
    private static final int ASIZE = 2;
    private static final int CORNER = 3;

    private static final int OUER_TYPE = 2;
    private static final int ALET_TYPE = 1;
    private static final int ASIZE_TYPE = 3;
    private static final int CORNER_TYPE = 4; //暂定为四

    @BindView(R.id.bowl_radio_group)
    RadioGroup bowlRadioGroup;

    private FragmentManager fragmentManager;
    private List<Fragment> fragments;
    private Fragment currentFragment;
    private String mThirId;

    private boolean is_ouer = false;
    private boolean is_alet = false;
    private boolean is_asize = false;
    private boolean is_corner = false;

    public static BowlFragment newInstance(String thirdId) {
        BowlFragment bowlFragment = new BowlFragment();
        Bundle bundle = new Bundle();
        bundle.putString("thirdId", thirdId);
        bowlFragment.setArguments(bundle);
        return bowlFragment;
    }

    public BowlFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mThirId = getArguments().getString("thirdId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bowl, container, false);
        ButterKnife.bind(this, view);
        initEvent();
        return view;
    }


    private void initEvent() {
        fragments = new ArrayList<>();
        fragments.add(BowlChildFragment.newInstance(mThirId, OUER_TYPE));
        fragments.add(BowlChildFragment.newInstance(mThirId, ALET_TYPE));
        fragments.add(BowlChildFragment.newInstance(mThirId, ASIZE_TYPE));
        fragments.add(BowlChildFragment.newInstance(mThirId, CORNER_TYPE));

        bowlRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int id = group.getCheckedRadioButtonId();
                int currenIndex = 0;
                switch (id) {
                    case R.id.bowl_ouer:
                        currenIndex = OUER;
                        break;
                    case R.id.bowl_alet:
                        currenIndex = ALET;
                        break;
                    case R.id.bowl_asize:
                        currenIndex = ASIZE;
                        break;
                    case R.id.bowl_corner:
                        currenIndex = CORNER;
                        break;
                }

                switchContent(currentFragment, fragments.get(currenIndex));
                setIsFragmentInit(currenIndex);
            }
        });

        switchFragment(OUER);
        is_ouer = true;
    }

    private void setIsFragmentInit(int index) {
        switch (index) {
            case OUER:
                is_ouer = true;
                break;
            case ALET:
                is_alet = true;
                break;
            case ASIZE:
                is_asize = true;
                break;
            case CORNER:
                is_corner = true;
                break;
        }
    }

    private void switchFragment(int position) {
        fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.bowl_fl, fragments.get(position));
        fragmentTransaction.commit();
        currentFragment = fragments.get(OUER);
    }



    private void switchContent(Fragment from, Fragment to) {
        if (currentFragment != to) {
            currentFragment = to;
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过

                fragmentTransaction.hide(from).add(R.id.bowl_fl, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                fragmentTransaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    @Override
    protected void onTextResult(String text) {

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
}
