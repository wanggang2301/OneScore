package com.hhly.mlottery.frame.tennisfrag.datailsfrag;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.tennisball.TennisDatailsPlateAdapter;
import com.hhly.mlottery.util.L;

import java.util.ArrayList;
import java.util.List;

/**
 * 网球内页亚盘
 */
public class TennisPlateFrag extends Fragment {
    private static final String TENNIS_DATAILS_THIRDID = "tennis_details_third_id";

    private Activity mContext;
    private String mThirdId;

    private View mView;
    private View contentView;
    private View notDataView;
    private RecyclerView mRecyclerView;

    private List<String> mData = new ArrayList<>();
    private TennisDatailsPlateAdapter mAdapter;

    public TennisPlateFrag() {
    }

    public static TennisPlateFrag newInstance(String thirdid) {
        TennisPlateFrag fragment = new TennisPlateFrag();
        Bundle args = new Bundle();
        args.putString(TENNIS_DATAILS_THIRDID, thirdid);
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
        mView = inflater.inflate(R.layout.fragment_tennis_plate, container, false);

        initView();



        return mView;
    }

    private void initView() {
        contentView = mView.findViewById(R.id.tennis_datails_plate_content);
        notDataView = mView.findViewById(R.id.network_exception_layout);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.tennis_datails_plate_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mData.add("test");
        mData.add("test");
        mData.add("test");
        mData.add("test");
        mData.add("test");
        mData.add("test");
        mData.add("test");
        mData.add("test");
        mData.add("test");

        mAdapter = new TennisDatailsPlateAdapter(R.layout.fragment_tennis_plate_item,mData);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

}
