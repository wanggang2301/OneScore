package com.hhly.mlottery.frame.footframe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;

/**
 * @author wang gang
 * @date 2016/6/3 14:17
 * @des ${TODO}
 */
public class DetailsRollballFragment extends Fragment {
    private View mView;


    public static DetailsRollballFragment newInstance() {
        DetailsRollballFragment fragment = new DetailsRollballFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_details_rollball, container, false);
        return mView;

    }
}
