package com.hhly.mlottery.frame.basketballframe;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;

/**
 * 球队统计Fragment
 */
public class BasketTeamStatisticsFragment extends Fragment {


    public static BasketTeamStatisticsFragment newInstance() {
        BasketTeamStatisticsFragment basketTeamStatisticsFragment = new BasketTeamStatisticsFragment();
        return basketTeamStatisticsFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basket_team_statistics, container, false);
    }

}
