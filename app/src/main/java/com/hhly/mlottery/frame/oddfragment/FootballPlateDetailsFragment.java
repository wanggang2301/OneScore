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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.cpiadapter.FootballPlateDetailsLeftAdapter;
import com.hhly.mlottery.adapter.cpiadapter.FootballPlateDetailsRightAdapter;
import com.hhly.mlottery.bean.enums.StatusEnum;
import com.hhly.mlottery.bean.oddsbean.OddsDataInfo;
import com.hhly.mlottery.bean.oddsbean.OddsDetailsDataInfo;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.footballframe.OddsFragment;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.EmptyView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 足球比赛指数详情
 * <p>
 * Created by Loshine on 2016/6/29.
 */
public class FootballPlateDetailsFragment extends Fragment {

    private static final String LEFT_LIST = "leftList";
    private static final String ODD_TYPE = "oddType";
    private static final String THIRD_ID = "thirdId";
    private static final String POSITION = "position";

    RecyclerView mLeftRecyclerView;
    RecyclerView mRightRecyclerView;

    View mLeftFootView;
    EmptyView mEmptyView;
    View mRightContentLayout;

    TextView mLeftTitle;
    TextView mCenterTitle;
    TextView mRightTitle;

    private FootballPlateDetailsLeftAdapter mLeftAdapter;
    private FootballPlateDetailsRightAdapter mRightAdapter;

    private String oddType;
    private String thirdId;
    private String companyId; // 选中的公司
    private boolean isFirstLoad = true;
    private List<OddsDataInfo.ListOddEntity> leftList; // 左侧列表数据源
    private List<OddsDetailsDataInfo.DetailsEntity> rightList; // 右侧列表数据源
    private List<OddsDetailsDataInfo.DetailsEntity.DataDetailsEntity> rightConvertList; // 右侧转换后的数据源

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
            oddType = args.getString(ODD_TYPE);
            thirdId = args.getString(THIRD_ID);
            int position = args.getInt(POSITION);
            companyId = leftList.get(position).getId();
            checkedPosition(position);
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

        initTitle(view);

        mLeftRecyclerView = (RecyclerView) view.findViewById(R.id.left_recycler_view);
        mRightRecyclerView = (RecyclerView) view.findViewById(R.id.right_recycler_view);

        mRightContentLayout = view.findViewById(R.id.content);

        mLeftAdapter = new FootballPlateDetailsLeftAdapter(leftList);
        mLeftAdapter.addFooterView(mLeftFootView);
        mLeftAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                checkedPosition(i);
                mLeftAdapter.notifyDataSetChanged();
                // 请求右侧数据
                companyId = leftList.get(i).getId();
                loadData();
            }
        });
        mLeftRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mLeftRecyclerView.setAdapter(mLeftAdapter);

        mLeftFootView.findViewById(R.id.odds_back_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mParentFragment != null) {
                    mParentFragment.getChildFragmentManager()
                            .popBackStack();
                }
            }
        });

        mEmptyView = (EmptyView) view.findViewById(R.id.empty_view);
        mEmptyView.setOnErrorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        rightList = new ArrayList<>();
        rightConvertList = new ArrayList<>();
        mRightAdapter = new FootballPlateDetailsRightAdapter(companyId,oddType, rightList, rightConvertList);
        mRightRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRightRecyclerView.setAdapter(mRightAdapter);

        final StickyRecyclerHeadersDecoration headersDecor =
                new StickyRecyclerHeadersDecoration(mRightAdapter);
        mRightRecyclerView.addItemDecoration(headersDecor);

        loadData();
    }

    private void initTitle(View view) {
        mLeftTitle = (TextView) view.findViewById(R.id.plate_home_details_txt_id);
        mCenterTitle = (TextView) view.findViewById(R.id.plate_dish_details_txt_id);
        mRightTitle = (TextView) view.findViewById(R.id.plate_guest_details_txt_id);

        switch (oddType) {
            case "1":
                mLeftTitle.setText(R.string.foot_odds_alet_left);
                mCenterTitle.setText(R.string.foot_odds_alet_middle);
                mRightTitle.setText(R.string.foot_odds_alet_right);
                break;
            case "3":
                mLeftTitle.setText(R.string.foot_odds_asize_left);
                mCenterTitle.setText(R.string.foot_odds_asize_middle);
                mRightTitle.setText(R.string.foot_odds_asize_right);
                break;
            case "2":
                mLeftTitle.setText(R.string.foot_odds_eu_left);
                mCenterTitle.setText(R.string.foot_odds_eu_middle);
                mRightTitle.setText(R.string.foot_odds_eu_right);
                break;
        }
    }

    private void checkedPosition(int i) {
        for (OddsDataInfo.ListOddEntity listOdd : leftList) {
            listOdd.setChecked(false);
        }
        OddsDataInfo.ListOddEntity listOddEntity = leftList.get(i);
        listOddEntity.setChecked(true);
        companyId = listOddEntity.getId();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mParentFragment = null;
    }


    public void loadData() {
        Map<String, String> myPostParams = new HashMap<>();
        myPostParams.put("companyId", companyId);
        myPostParams.put("oddType", oddType);
        myPostParams.put("thirdId", thirdId);

        rightList.clear();
        setStatus(StatusEnum.LOADING);

        VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_MATCHODD_DETAILS, myPostParams,
                new VolleyContentFast.ResponseSuccessListener<OddsDetailsDataInfo>() {
                    @Override
                    public void onResponse(OddsDetailsDataInfo jsonObject) {
                        if (jsonObject.getDetails() != null) {
                            rightList.clear();
                            rightList.addAll(jsonObject.getDetails());
                            mRightAdapter.refreshData();
                            setStatus(StatusEnum.NORMAL);
                        }
                        setStatus(StatusEnum.NORMAL);
                    }
                },
                new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        rightList.clear();
                        setStatus(StatusEnum.ERROR);
                    }
                },
                OddsDetailsDataInfo.class);
    }

    private void setStatus(@StatusEnum.Status int status) {
        if (status == StatusEnum.LOADING || status == StatusEnum.ERROR) {
            mRightRecyclerView.setVisibility(isFirstLoad ? View.VISIBLE : View.GONE);
            if (isFirstLoad) isFirstLoad = false;
            mEmptyView.setVisibility(View.VISIBLE);
        } else if (status == StatusEnum.NORMAL && rightList.size() > 0) {
            mRightRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        } else {
            mRightRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
        mEmptyView.setStatus(status);
        mRightAdapter.notifyDataSetChanged();
        mRightRecyclerView.scrollToPosition(0);
    }

    public static FootballPlateDetailsFragment newInstance(String oddType,
                                                           String thirdId,
                                                           int position,
                                                           ArrayList<OddsDataInfo.ListOddEntity> items) {

        Bundle args = new Bundle();
        args.putString(ODD_TYPE, oddType);
        args.putString(THIRD_ID, thirdId);
        args.putInt(POSITION, position);
        args.putParcelableArrayList(LEFT_LIST, items);
        FootballPlateDetailsFragment fragment = new FootballPlateDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
