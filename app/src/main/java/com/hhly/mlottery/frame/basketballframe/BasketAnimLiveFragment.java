package com.hhly.mlottery.frame.basketballframe;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;

/**
 * 描述:  篮球内页动画直播
 * 作者:  wangg@13322.com
 * 时间:  2016/10/10 10:35
 */
public class BasketAnimLiveFragment extends Fragment {


    public BasketAnimLiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basket_anim_live, container, false);
    }

}
