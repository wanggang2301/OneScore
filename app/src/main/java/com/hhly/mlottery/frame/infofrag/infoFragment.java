package com.hhly.mlottery.frame.infofrag;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.FragmentUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.widget.BallSelectArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 情报
 */
public class InfoFragment extends Fragment {

    private static final int FOOTBALL = 0;
    private static final int BASKETBALL = 1;
    private static final int SNOOKER = 2;

    private Spinner mSpinner;
    private View mView;

    private Context mContext;
    private String[] mItems = {"足球", "篮球", "斯洛克"};

    private int fragmentIndex = 0;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private List<Fragment> fragments = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_info, container, false);
        mContext = getActivity();
        initView();
        initEvent();
        return mView;
    }

    private void initView() {
        mSpinner = (Spinner) mView.findViewById(R.id.public_txt_left_spinner);
        mSpinner.setVisibility(View.VISIBLE);

        // mItems = {"足球", "斯洛克"};

        //mItems = getResources().getStringArray(R.array.ball_select);
        BallSelectArrayAdapter mAdapter = new BallSelectArrayAdapter(mContext, mItems);
        mSpinner.setAdapter(mAdapter);
        mSpinner.setSelection(0);


        fragments.add(new FootInfoFragment());
        fragments.add(new BasketInfoFragment());
        fragments.add(new SnookerInfoFragment());
    }

    private void initEvent() {
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switchFragment(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void switchFragment(int position) {
        fragmentIndex = position;// 当前fragment下标
        L.d("xxx", "当前Fragment下标：" + fragmentIndex);
        fragmentManager = getChildFragmentManager();
        currentFragment = FragmentUtils.switchFragment(fragmentManager, R.id.ly_content_info, currentFragment, fragments.get(position).getClass(), null, false, fragments.get(position).getClass().getSimpleName() + position, false);
    }
}
