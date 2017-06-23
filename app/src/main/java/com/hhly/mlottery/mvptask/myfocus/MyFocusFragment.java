package com.hhly.mlottery.mvptask.myfocus;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.hhly.mlottery.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wangg
 * @desc 我的关注
 * @date 2017/05/31
 */
public class MyFocusFragment extends Fragment {

    private Activity mActivity;

    @BindView(R.id.focus_back)
    ImageView focusBack;
    @BindView(R.id.focus_radio_group)
    RadioGroup focusRadioGroup;
    @BindView(R.id.focus_search)
    ImageView focusSearch;


    private FragmentManager fragmentManager;
    private List<Fragment> fragments;
    private Fragment currentFragment;

    public static MyFocusFragment newInstance() {
        MyFocusFragment myFocusFragment = new MyFocusFragment();
        return myFocusFragment;
    }

    public MyFocusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_focus, container, false);
        ButterKnife.bind(this, view);

        initEvent();
        return view;
    }


    private void initEvent() {

        //gifMatchStart.setImageResource(R.mipmap.football_match_start_gif);
        fragments = new ArrayList<>();
        fragments.add(MyFocusChildFragment.newInstance());
        fragments.add(MyFocusChildFragment.newInstance());

        focusRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                int id = group.getCheckedRadioButtonId();
                int currenIndex = 0;
                switch (id) {
                    case R.id.focus_football:
                        currenIndex = 0;
                        break;
                    case R.id.focus_basket:
                        currenIndex = 1;
                        break;
                }

                switchContent(currentFragment, fragments.get(currenIndex));
            }
        });

        switchFragment(0);

    }


    private void switchFragment(int position) {
        fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.myfocus_content, fragments.get(position));
        fragmentTransaction.commit();
        currentFragment = fragments.get(position);
    }


    private void switchContent(Fragment from, Fragment to) {
        if (currentFragment != to) {
            currentFragment = to;
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                fragmentTransaction.hide(from).add(R.id.myfocus_content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                fragmentTransaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }


    @OnClick({R.id.focus_back, R.id.focus_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.focus_back:
                mActivity.finish();
                break;
            case R.id.focus_search:
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }
}
