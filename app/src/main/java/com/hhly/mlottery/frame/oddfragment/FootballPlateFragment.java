package com.hhly.mlottery.frame.oddfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballMatchDetailActivityTest;
import com.hhly.mlottery.adapter.cpiadapter.FootballPlateAdapter;
import com.hhly.mlottery.bean.enums.OddsTypeEnum;
import com.hhly.mlottery.bean.oddsbean.OddsDataInfo;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 足球详情指数列表
 * <p/>
 * Created by loshine on 2016/6/28.
 */
public class FootballPlateFragment extends Fragment {

    private static final String TYPE = "type";

    RecyclerView mRecyclerView;
    View mLoadingView;
    View mErrorView;

    private FootballMatchDetailActivityTest mActivity;

    private List<OddsDataInfo.ListOddEntity> items; // 指数数据源
    private FootballPlateAdapter mAdapter;
    private String type;

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
        mLoadingView = inflater.inflate(R.layout.layout_loading, container, false);
        mErrorView = inflater.inflate(R.layout.layout_net_error, container, false);
        mErrorView.findViewById(R.id.reloading_txt)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadData();
                    }
                });
        return inflater.inflate(R.layout.fragment_football_plate, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        items = new ArrayList<>();
        mAdapter = new FootballPlateAdapter(type, items);
        mAdapter.setEmptyView(mLoadingView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mActivity = (FootballMatchDetailActivityTest) getActivity();

        loadData();
    }

    /**
     * 加载数据
     */
    public void loadData() {
        Map<String, String> myPostParams = new HashMap<>();
        myPostParams.put("thirdId", mActivity.mThirdId);
        myPostParams.put("oddType", convertType());

        VolleyContentFast.requestJsonByPost(BaseURLs.URL_FOOTBALL_MATCHODD, myPostParams,
                new VolleyContentFast.ResponseSuccessListener<OddsDataInfo>() {
                    @Override
                    public void onResponse(OddsDataInfo jsonObject) {
                        items.clear();
                        items.addAll(jsonObject.getListOdd());
                        Log.d("count", jsonObject.getListOdd().size() + "");
                        Log.d("count", items.size() + "");
                        mAdapter.notifyDataSetChanged();
                    }
                },
                new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        VolleyError volleyError = exception.getVolleyError();
                        volleyError.printStackTrace();
                        Toast.makeText(MyApp.getContext(),
                                volleyError.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                OddsDataInfo.class);
    }

    public void setStatus() {

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity = null;
    }

    public static FootballPlateFragment newInstance(@OddsTypeEnum.OddsType String type) {

        Bundle args = new Bundle();
        args.putString(TYPE, type);
        FootballPlateFragment fragment = new FootballPlateFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
