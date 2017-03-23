package com.hhly.mlottery.frame.tennisfrag.datailsfrag;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;

/**
 * 网球内页亚盘
 */
public class TennisPlateFrag extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    public TennisPlateFrag() {
        // Required empty public constructor
    }

    public static TennisPlateFrag newInstance(String param1) {
        TennisPlateFrag fragment = new TennisPlateFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tennis_plate, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
