package com.hhly.mlottery.frame.tennisfrag;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;

/**
 * desc:网球比分列表Tab页面
 * Created by 107_tangrr on 2017/2/20 0020.
 */

public class TennisBallTabFragment extends Fragment {

    private static final String TENNIS_TYPE = "tennis_type";
    private int type;

    private Context mContext;
    private View mView;

    public static TennisBallTabFragment newInstance(int type) {
        TennisBallTabFragment fragment = new TennisBallTabFragment();
        Bundle args = new Bundle();
        args.putInt(TENNIS_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(TENNIS_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mContext = getActivity();
        mView = View.inflate(mContext, R.layout.fragment_tennls_ball_tab, null);

        TextView tv_name = (TextView) mView.findViewById(R.id.tv_name);

        switch (type){
            case 0:
                tv_name.setText("即时");
                break;
            case 1:
                tv_name.setText("赛果");
                break;
            case 2:
                tv_name.setText("赛程");
                break;
            case 3:
                tv_name.setText("关注");
                break;
        }

        return mView;
    }
}
