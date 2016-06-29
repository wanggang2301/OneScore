package com.hhly.mlottery.frame.oddfragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.cpiadapter.FootballPlateDetailsLeftAdapter;
import com.hhly.mlottery.bean.oddsbean.OddsDataInfo;
import com.hhly.mlottery.bean.oddsbean.OddsDetailsDataInfo;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.footframe.OddsFragment;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 足球比赛指数详情
 * <p/>
 * Created by Loshine on 2016/6/29.
 */
public class FootballPlateDetailsFragment extends Fragment {

    private static final String LEFT_LIST = "leftList";
    private static final String POSITION = "position";

    RecyclerView mLeftRecyclerView;
    RecyclerView mRightRecyclerView;

    View mLeftFootView;

    private FootballPlateDetailsLeftAdapter mLeftAdapter;

    private List<OddsDataInfo.ListOddEntity> leftList; // 左侧列表数据源
    private int position; // 选中位置

    private OddsFragment mParentFragment;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mParentFragment = (OddsFragment) getParentFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            leftList = args.getParcelableArrayList(LEFT_LIST);
            position = args.getInt(POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mLeftFootView = inflater.inflate(R.layout.layout_odds_left_foot, container, false);
        return inflater.inflate(R.layout.fragment_football_plate_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLeftRecyclerView = (RecyclerView) view.findViewById(R.id.left_recycler_view);
        mRightRecyclerView = (RecyclerView) view.findViewById(R.id.right_recycler_view);

        mLeftAdapter = new FootballPlateDetailsLeftAdapter(leftList);
        mLeftAdapter.addFooterView(mLeftFootView);
        mLeftAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                ToastTools.ShowQuick(MyApp.getContext(), mLeftAdapter.getItem(i).getName());
            }
        });
        mLeftRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mLeftRecyclerView.setAdapter(mLeftAdapter);

        mLeftFootView.findViewById(R.id.odds_back_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mParentFragment != null) {
                    mParentFragment.showList();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mParentFragment = null;
    }

    public void loadData() {
        Map<String, String> myPostParams = new HashMap<>();
//        myPostParams.put("companyId", position);
//        myPostParams.put("oddType", oddType);
//        myPostParams.put("thirdId", thirdId);

        VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_MATCHODD_DETAILS, myPostParams,
                new VolleyContentFast.ResponseSuccessListener<OddsDetailsDataInfo>() {
                    @Override
                    public void onResponse(OddsDetailsDataInfo jsonObject) {

                    }
                },
                new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {

                    }
                },
                OddsDetailsDataInfo.class);

    }

    public static FootballPlateDetailsFragment newInstance(int position, ArrayList<OddsDataInfo.ListOddEntity> items) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(LEFT_LIST, items);
        args.putInt(POSITION, position);
        FootballPlateDetailsFragment fragment = new FootballPlateDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
