package com.hhly.mlottery.frame.footballframe;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hhly.mlottery.R;

/**
 * @author wangg
 * @des:足球内页分析fragment
 */
public class AnalyzeParentFragment extends Fragment {

    private FrameLayout flContent;
    private View mView;

    public AnalyzeParentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_analyze_parent, container, false);
        flContent = (FrameLayout) mView.findViewById(R.id.fl_content);
        return mView;
    }

}
