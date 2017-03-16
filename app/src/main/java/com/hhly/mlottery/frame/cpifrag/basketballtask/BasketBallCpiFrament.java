package com.hhly.mlottery.frame.cpifrag.basketballtask;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;

/**
 * @author wangg
 * @des:篮球指数
 * @date:2017/3/16
 */
public class BasketBallCpiFrament extends Fragment {

    public BasketBallCpiFrament() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basket_ball_cpi, container, false);
    }
}
