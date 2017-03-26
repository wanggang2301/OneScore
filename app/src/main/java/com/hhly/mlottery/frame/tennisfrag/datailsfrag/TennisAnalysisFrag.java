package com.hhly.mlottery.frame.tennisfrag.datailsfrag;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;

/**
 * 网球内页分析
 * 20170323 tangrr
 */
public class TennisAnalysisFrag extends Fragment {
    private static final String TENNIS_DATAILS_THIRDID = "tennis_details_third_id";

    private String mThirdId;
    private Activity mContext;

    private View mView;
    private View contentView;
    private View notDataView;

    public TennisAnalysisFrag() {

    }

    public static TennisAnalysisFrag newInstance(String thirdId) {
        TennisAnalysisFrag fragment = new TennisAnalysisFrag();
        Bundle args = new Bundle();
        args.putString(TENNIS_DATAILS_THIRDID, thirdId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mThirdId = getArguments().getString(TENNIS_DATAILS_THIRDID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_tennis_analysis, container, false);

        initView();
        initEvent();

        return mView;
    }

    private void initEvent() {

    }

    private void initView() {
        contentView = mView.findViewById(R.id.tennis_datails_analysis_scroll);
        notDataView = mView.findViewById(R.id.network_exception_layout);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }
}
