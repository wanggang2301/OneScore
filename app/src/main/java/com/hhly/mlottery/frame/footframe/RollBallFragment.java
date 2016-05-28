package com.hhly.mlottery.frame.footframe;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.RollBallAdapter;

/**
 * @author wang gang
 * @date 2016/5/24 17:07
 * @des 足球比分滚球
 */
public class RollBallFragment extends Fragment {

    private LinearLayoutManager mLayoutManager;

    private RecyclerView mRecyclerView;

    private RollBallAdapter mRollBallAdapter;

    private View mView;

    private Context mContext;

    public static RollBallFragment newInstance() {
        RollBallFragment rollBallFragment = new RollBallFragment();
        return rollBallFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.football_roll, container, false);
        initViews();
        loadDatas();
        return mView;
    }


    private void initViews() {
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview_roll);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void loadDatas(){

//        mRollBallAdapter=new RollBallAdapter(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRollBallAdapter);


    }
}
