package com.hhly.mlottery.frame.footballframe;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;

/**
 * @author wangg
 * @dete 2017/04/19
 * @des 足球内页直播暂无文字直播
 */
public class NoLiveTextFragment extends Fragment {


    public static NoLiveTextFragment newInstance() {
        NoLiveTextFragment noLiveTextFragment = new NoLiveTextFragment();
        return noLiveTextFragment;
    }

    public NoLiveTextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_no_live_text, container, false);
    }
}
