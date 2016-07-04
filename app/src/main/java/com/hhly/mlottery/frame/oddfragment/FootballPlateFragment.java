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

import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballMatchDetailActivityTest;
import com.hhly.mlottery.adapter.cpiadapter.FootballPlateAdapter;
import com.hhly.mlottery.bean.enums.OddsTypeEnum;
import com.hhly.mlottery.bean.enums.StatusEnum;
import com.hhly.mlottery.bean.oddsbean.OddsDataInfo;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.footframe.OddsFragment;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.EmptyView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 足球详情指数列表
 * <p/>
 * Created by loshine on 2016/6/28.
 */
public class FootballPlateFragment extends Fragment {

    private static final String TYPE = "type";

    RecyclerView mRecyclerView;
    EmptyView mEmptyView;

    private FootballMatchDetailActivityTest mActivity;

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

        initEmptyView();

        mAdapter.setEmptyView(mEmptyView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mActivity = (FootballMatchDetailActivityTest) getActivity();

        loadData();
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
        VolleyContentFast.requestJsonByPost(BaseURLs.URL_FOOTBALL_MATCHODD, myPostParams,
                new VolleyContentFast.ResponseSuccessListener<OddsDataInfo>() {
                    @Override
                    public void onResponse(OddsDataInfo jsonObject) {
                        items.clear();
                        items.addAll(jsonObject.getListOdd());
                        mAdapter.notifyDataSetChanged();
                        setStatus(StatusEnum.NORMAL);
                    }
                },
                new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        VolleyError volleyError = exception.getVolleyError();
                        volleyError.printStackTrace();
                        setStatus(StatusEnum.ERROR);
                    }
                },
                OddsDataInfo.class);
    }

    public void setStatus(@StatusEnum.Status int status) {
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

    /**
     * 初始化 EmptyView
     */
    private void initEmptyView() {
        mEmptyView = new EmptyView(getContext());
        mEmptyView.setOnErrorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        RecyclerView.LayoutParams layoutParams =
                new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        mEmptyView.setLayoutParams(layoutParams);
    }

    public static FootballPlateFragment newInstance(@OddsTypeEnum.OddsType String type) {

        Bundle args = new Bundle();
        args.putString(TYPE, type);
        FootballPlateFragment fragment = new FootballPlateFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
