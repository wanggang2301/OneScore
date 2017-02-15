package com.hhly.mlottery.frame.datafrag;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketballInformationSerachActivity;
import com.hhly.mlottery.activity.FootballInformationSerachActivity;
import com.hhly.mlottery.util.FragmentUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.widget.BallSelectArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 资料库
 * wangg
 */
public class DataFragment extends Fragment implements View.OnClickListener {

    private static final int FOOTBALL = 0;
    private static final int BASKETBALL = 1;
    private static final int SNOOKER = 2;


    private ImageView publicBtnSet;
    private Spinner mSpinner;
    private View mView;

    private Context mContext;
    private String[] mItems;

    private int fragmentIndex = 0;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private List<Fragment> fragments = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_data, container, false);
        mContext = getActivity();
        initView();
        initEvent();
        return mView;
    }

    private void initView() {

        mItems = getResources().getStringArray(R.array.ziliaoku_select);


        mSpinner = (Spinner) mView.findViewById(R.id.public_txt_left_spinner);
        mSpinner.setVisibility(View.VISIBLE);

        // mItems = {"足球", "斯洛克"};

        //mItems = getResources().getStringArray(R.array.ball_select);
        BallSelectArrayAdapter mAdapter = new BallSelectArrayAdapter(mContext, mItems);
        mSpinner.setAdapter(mAdapter);
        mSpinner.setSelection(0);


        publicBtnSet = (ImageView) mView.findViewById(R.id.public_btn_set);
        publicBtnSet.setOnClickListener(this);
        publicBtnSet.setImageResource(R.mipmap.info_search);

        fragments.add(new FootballInfomationFragment());
        fragments.add(new BasketballInfomationFragment());
        // fragments.add(new SnookerInfomationFragment());
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
        currentFragment = FragmentUtils.switchFragment(fragmentManager, R.id.ly_content_data, currentFragment, fragments.get(position).getClass(), null, false, fragments.get(position).getClass().getSimpleName() + position, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_btn_set:

                if (fragmentIndex == FOOTBALL) {
                    Intent intent = new Intent(mContext, FootballInformationSerachActivity.class);
                    startActivity(intent);
                } else if (fragmentIndex == BASKETBALL) {
                    Intent intent = new Intent(mContext, BasketballInformationSerachActivity.class);
                    startActivity(intent);
                } else if (fragmentIndex == SNOOKER) {

                }


                break;
        }

    }
}
