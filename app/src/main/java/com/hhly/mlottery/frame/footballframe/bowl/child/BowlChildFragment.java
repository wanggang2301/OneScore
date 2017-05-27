package com.hhly.mlottery.frame.footballframe.bowl.child;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BowlChildFragment extends Fragment {

    private final static String ODD_TYPE = "odd_type";
    private final static String THIRDID = "thirdId";

    private String mThirdId;
    private String oddType;

    public static BowlChildFragment newInstance(String thirdId, String oddType) {
        BowlChildFragment bowlChildFragment = new BowlChildFragment();
        Bundle bundle = new Bundle();
        bundle.putString(THIRDID, thirdId);
        bundle.putString(ODD_TYPE, oddType);
        bowlChildFragment.setArguments(bundle);
        return bowlChildFragment;
    }


    public BowlChildFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mThirdId = getArguments().getString(THIRDID);
            oddType = getArguments().getString(ODD_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_bowl_child, container, false);
    }

}
