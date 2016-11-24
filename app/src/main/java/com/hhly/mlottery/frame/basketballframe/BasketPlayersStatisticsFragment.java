package com.hhly.mlottery.frame.basketballframe;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;

/**
 * 球员统计fragmemt
 */
public class BasketPlayersStatisticsFragment extends Fragment {


    public static BasketPlayersStatisticsFragment newInstance() {
        BasketPlayersStatisticsFragment basketPlayersStatisticsFragment = new BasketPlayersStatisticsFragment();
        return basketPlayersStatisticsFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basket_players_statistics, container, false);
    }

}
