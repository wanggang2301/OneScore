package com.hhly.mlottery.frame.datafrag;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.L;

/**
 * A simple {@link Fragment} subclass.
 */
public class SnookerInfomationFragment extends Fragment {


    public SnookerInfomationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_snooker_infomation, container, false);
    }

}
