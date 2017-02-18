package com.hhly.mlottery.frame.snooker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;

/**
 * @author wangg@13322.com
 * @desr 斯洛克赛事
 */
public class SnookerMatchFragment extends Fragment {


    public SnookerMatchFragment() {
        // Required empty public constructor
    }


    public static SnookerMatchFragment newInstance() {
        SnookerMatchFragment snookerMatchFragment = new SnookerMatchFragment();

        return snookerMatchFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_snooker_match, container, false);
    }

}
