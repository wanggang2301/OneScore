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
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.adapter.cpiadapter.FootballPlateAdapter;
import com.hhly.mlottery.bean.enums.OddsTypeEnum;
import com.hhly.mlottery.bean.enums.StatusEnum;
import com.hhly.mlottery.bean.oddsbean.OddsDataInfo;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.footballframe.OddsFragment;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.EmptyView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/6/28.
 */
public class FootballPlateFragment extends Fragment {

    private static final String TYPE = "type";

    RecyclerView mRecyclerView;
    EmptyView mEmptyView;
    View mContentView;
    TextView mLeftTitle;
    TextView mCenterTitle;
    TextView mRightTitle;

    private FootballMatchDetailActivity mActivity;

    private ArrayList<OddsDataInfo.ListOddEntity> items; // 指数数据源
    private FootballPlateAdapter mAdapter;
    private String type;

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
            type = args.getString(TYPE, OddsTypeEnum.PLATE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_football_plate, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initTitle(view);

        mContentView = view.findViewById(R.id.content);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        items = new ArrayList<>();
        mAdapter = new FootballPlateAdapter(type, items);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                FootballPlateDetailsFragment detailsFragment =
                        FootballPlateDetailsFragment.newInstance(convertType(), mActivity.mThirdId,
                                i, items);
                mParentFragment.showDetails(detailsFragment);
            }
        });

        mEmptyView = (EmptyView) view.findViewById(R.id.empty_view);
        mEmptyView.setOnErrorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mActivity = (FootballMatchDetailActivity) getActivity();

        loadData();
    }

    private void initTitle(View view) {
        mLeftTitle = (TextView) view.findViewById(R.id.left);
        mCenterTitle = (TextView) view.findViewById(R.id.center);
        mRightTitle = (TextView) view.findViewById(R.id.right);

        switch (type) {
            case OddsTypeEnum.PLATE:
                mLeftTitle.setText(R.string.foot_odds_alet_left);
                mCenterTitle.setText(R.string.foot_odds_alet_middle);
                mRightTitle.setText(R.string.foot_odds_alet_right);
                break;
            case OddsTypeEnum.BIG:
                mLeftTitle.setText(R.string.foot_odds_asize_left);
                mCenterTitle.setText(R.string.foot_odds_asize_middle);
                mRightTitle.setText(R.string.foot_odds_asize_right);
                break;
            case OddsTypeEnum.OP:
                mLeftTitle.setText(R.string.foot_odds_eu_left);
                mCenterTitle.setText(R.string.foot_odds_eu_middle);
                mRightTitle.setText(R.string.foot_odds_eu_right);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mParentFragment = null;
    }

    /**
     * 加载数据
     */
    public void loadData() {
        Map<String, String> myPostParams = new HashMap<>();
        myPostParams.put("thirdId", mActivity.mThirdId);
        myPostParams.put("oddType", convertType());

        setStatus(StatusEnum.LOADING);
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_MATCHODD, myPostParams,
                new VolleyContentFast.ResponseSuccessListener<OddsDataInfo>() {
                    @Override
                    public void onResponse(OddsDataInfo jsonObject) {
                        items.clear();
                        List<OddsDataInfo.ListOddEntity> listOdd = jsonObject.getListOdd();
                        if (listOdd != null) items.addAll(listOdd);
                        mAdapter.notifyDataSetChanged();
                        setStatus(StatusEnum.NORMAL);
                    }
                },
                new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        VolleyError volleyError = exception.getVolleyError();
                        if (volleyError != null) volleyError.printStackTrace();
                        setStatus(StatusEnum.ERROR);
                    }
                },
                OddsDataInfo.class);
    }

    private void setStatus(@StatusEnum.Status int status) {
        if (status == StatusEnum.LOADING || status == StatusEnum.ERROR) {
            mContentView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else if (status == StatusEnum.NORMAL && items.size() > 0) {
            mContentView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        } else {
            mContentView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
        mEmptyView.setStatus(status);
    }

    private String convertType() {
        switch (type) {
            case OddsTypeEnum.PLATE:
                return "1";
            case OddsTypeEnum.BIG:
                return "3";
            case OddsTypeEnum.OP:
                return "2";
            default:
                return "1";
        }
    }

    public static FootballPlateFragment newInstance(@OddsTypeEnum.OddsType String type) {

        Bundle args = new Bundle();
        args.putString(TYPE, type);
        FootballPlateFragment fragment = new FootballPlateFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
