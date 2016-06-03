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
 * @date 2016/6/2 18:34
 * @des ${TODO}
 */
public class LiveHeadInfoFragment extends Fragment {

    private View mView;


    public static LiveHeadInfoFragment instance() {
        LiveHeadInfoFragment fragment = new LiveHeadInfoFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_live_headinfo, container, false);
        return mView;

    }
}
