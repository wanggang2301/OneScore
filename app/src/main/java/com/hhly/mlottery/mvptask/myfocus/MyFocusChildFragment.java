package com.hhly.mlottery.mvptask.myfocus;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;

/**
 * @author wangg
 * @desc 我的关注
 */
public class MyFocusChildFragment extends Fragment {


    public static MyFocusChildFragment newInstance() {
        MyFocusChildFragment myFocusChildFragment = new MyFocusChildFragment();
        return myFocusChildFragment;
    }

    public MyFocusChildFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_focus_child, container, false);
    }

}
