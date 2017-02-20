package com.hhly.mlottery.frame.snooker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;


/**
 * @author wangg@13322.com
 * @desr 斯洛克排名
 * @date 2017/02/17
 */

public class SnookerRankFragment extends Fragment {


    public SnookerRankFragment() {
        // Required empty public constructor
    }

    public static SnookerRankFragment newInatance() {
        SnookerRankFragment snookerRankFragment = new SnookerRankFragment();
        return snookerRankFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_snooker_rank, container, false);
    }

}
