package com.hhly.mlottery.frame.basketballframe;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;

/**
 * @author: Wangg
 * @Name：BasketLiveFragment
 * @Description: 籃球內頁文字直播Fragment
 * @Created on:2016/11/15.
 */


public class BasketLiveFragment extends Fragment {


    public BasketLiveFragment() {
        // Required empty public constructor
    }

    public static BasketLiveFragment newInstance() {
        BasketLiveFragment basketLiveFragment = new BasketLiveFragment();
        return basketLiveFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basket_live, container, false);
    }

}