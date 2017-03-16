package com.hhly.mlottery.frame.cpifrag.basketballtask;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.mvp.ViewFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasketBallOddFragment extends ViewFragment<BasketBallContract.OddPresenter> implements BasketBallContract.OddView {


    public BasketBallOddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basket_ball_odd, container, false);
    }

    @Override
    public void onError() {

    }
}
