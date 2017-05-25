package com.hhly.mlottery.frame.cpifrag.footballtask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.CpiDetailsActivity;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.adapter.cpiadapter.CPIRecyclerListAdapter;
import com.hhly.mlottery.bean.enums.OddsTypeEnum;
import com.hhly.mlottery.bean.enums.StatusEnum;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.bean.websocket.WebSocketCPIResult;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.FootBallMatchFilterTypeEnum;
import com.hhly.mlottery.util.HandMatchId;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.EmptyView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * @author: Wangg
 * @Name：CPIOddsFragment22
 * @Description:
 * @Created on:2017/2/14  18:19.
 */

public class CPIOddsFragment2 extends Fragment {
    private static final String KEY_TYPE = "type"; // 类型

    private String type; // 该 Fragment 的类型
    private List<NewOddsInfo.AllInfoBean> defaultData; // 列表数据源
    private List<NewOddsInfo.AllInfoBean> filterData; // 过滤后的数据源
    private List<NewOddsInfo.FileterTagsBean> filterTagList; // 过滤信息
    private List<NewOddsInfo.CompanyBean> companyList; // 初次加载需要使用自己的 companyList
    private CPIRecyclerListAdapter mAdapter; // 适配器

    RecyclerView mRecyclerView;
    EmptyView mEmptyView;

    private FootCpiFragment parentFragment;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // attach 的时候获取父 Fragment
        parentFragment = (FootCpiFragment) getParentFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            type = args.getString(KEY_TYPE, OddsTypeEnum.PLATE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cpi_odds, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // findViews
        mRecyclerView = (RecyclerView) view.findViewById(R.id.cpi_odds_recyclerView);
        initEmptyView();
        initRecyclerView();
        refreshData(null);
    }

    private void initEmptyView() {
        mEmptyView = new EmptyView(getContext());
        mEmptyView.setLoadingType(EmptyView.LOADING_TYPE_TXT);
        mEmptyView.setOnErrorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentFragment.setRefreshing(true);
                parentFragment.refreshAllChildFragments();
            }
        });
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mEmptyView.setLayoutParams(layoutParams);
        setStatus(StatusEnum.LOADING);
    }

    /**
     * 初始化 RecyclerView
     */
    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        defaultData = new ArrayList<>();
        filterData = new ArrayList<>();
        filterTagList = new ArrayList<>();
        mAdapter = new CPIRecyclerListAdapter(filterData, parentFragment.getCompanyList(), type);
        mAdapter.setEmptyView(mEmptyView);
        // RecyclerView Item 单击
        mAdapter.setOnItemClickListener(new CPIRecyclerListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NewOddsInfo.AllInfoBean item) {

                if (HandMatchId.handId(getContext(), item.getMatchInfo().getMatchId())) {


                    Intent intent = new Intent(getContext(), FootballMatchDetailActivity.class);
                    intent.putExtra("thirdId", item.getMatchInfo().getMatchId());
                    getContext().startActivity(intent);
                }
            }
        });
        // 每个 Item 内部一条赔率的单击
        mAdapter.setOnOddsClickListener(new CPIRecyclerListAdapter.OnOddsClickListener() {
            @Override
            public void onOddsClick(NewOddsInfo.AllInfoBean item, NewOddsInfo.AllInfoBean.ComListBean odds) {
                //赔率公司(id,name,thirdId)
                List<Map<String, String>> obList = item.toListViewParamList();


                //点击指数页面，传值给详情界面
                Intent intent = new Intent(getContext(), CpiDetailsActivity.class);
                intent.putExtra("obListEntity", (Serializable) obList);   //两行赔率
                intent.putExtra("comId", odds.getComId());  //公司Id
                intent.putExtra("positionNunber", item.getComList().indexOf(odds) + "");
                intent.putExtra("stType", type);
                getContext().startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    public List<NewOddsInfo.FileterTagsBean> getFilterTagList() {
        return filterTagList;
    }

    /**
     * 刷新数据
     *
     * @param date date, 形如 2016-06-21 可以为空
     */
    public void refreshData(String date) {
        Map<String, String> map = new HashMap<>();

        if (OddsTypeEnum.PLATE.equals(type)) {
            map.put("type", "1");
        } else if (OddsTypeEnum.BIG.equals(type)) {
            map.put("type", "3");
        } else if (OddsTypeEnum.OP.equals(type)) {
            map.put("type", "2");
        }

        if (!TextUtils.isEmpty(date)) {
            map.put("date", date);
        }

        setStatus(StatusEnum.LOADING);
        VolleyContentFast.requestJsonByGet(BaseURLs.URL_NEW_ODDS, map,
                new VolleyContentFast.ResponseSuccessListener<NewOddsInfo>() {
                    @Override
                    public void onResponse(NewOddsInfo jsonObject) {
                        if (jsonObject.getCode() == 500) {
                            setStatus(StatusEnum.ERROR);
                            refreshOver();
                            return;
                        }

                        //本地存储当天日期
                        saveCurrentDate(jsonObject.getFilerDate());

                        List<NewOddsInfo.AllInfoBean> allInfo = jsonObject.getAllInfo();

                        // 公司数据
                        handleCompany(jsonObject.getCompany());

                        // 更新原始数据源
                        defaultData.clear();
                        defaultData.addAll(allInfo);

                        filterTagList.clear();
                        filterTagList.addAll(jsonObject.getFileterTags());

                        // 更新过滤后的数据源
                        updateFilterData();
                        setStatus(StatusEnum.NORMAL);
                        // 只有当前的 Fragment 刷新成功才可以停止刷新行为
                        refreshOverWithDate(jsonObject.getCurrDate().trim());
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        setStatus(StatusEnum.ERROR);
                        VolleyError volleyError = exception.getVolleyError();
                        if (volleyError != null) volleyError.printStackTrace();
                        refreshOver();
                    }
                }, NewOddsInfo.class);
    }

    private void saveCurrentDate(String date) {
        if (OddsTypeEnum.PLATE.equals(type)) {
            if (!PreferenceUtil.getString(FootBallMatchFilterTypeEnum.FOOT_INDEX_DATE_PLATE, "").equals(date)) {
                PreferenceUtil.removeKey(FootBallMatchFilterTypeEnum.FOOT_INDEX);
                PreferenceUtil.commitString(FootBallMatchFilterTypeEnum.FOOT_INDEX_DATE_PLATE, date);
            }
        } else if (OddsTypeEnum.BIG.equals(type)) {
            if (!PreferenceUtil.getString(FootBallMatchFilterTypeEnum.FOOT_INDEX_DATE_BIG, "").equals(date)) {
                PreferenceUtil.removeKey(FootBallMatchFilterTypeEnum.FOOT_INDEX);
                PreferenceUtil.commitString(FootBallMatchFilterTypeEnum.FOOT_INDEX_DATE_BIG, date);
            }
        } else if (OddsTypeEnum.OP.equals(type)) {
            if (!PreferenceUtil.getString(FootBallMatchFilterTypeEnum.FOOT_INDEX_DATE_OP, "").equals(date)) {
                PreferenceUtil.removeKey(FootBallMatchFilterTypeEnum.FOOT_INDEX);
                PreferenceUtil.commitString(FootBallMatchFilterTypeEnum.FOOT_INDEX_DATE_OP, date);
            }
        }
    }

    /**
     * 更新过滤数据源（注意要过滤公司赔率信息）
     */
    public void updateFilterData() {
        filterData.clear();
        LinkedList<String> filterList = parentFragment.getFilterList();

        if (filterList != null) {
            for (NewOddsInfo.AllInfoBean allInfo : defaultData) {
                if (filterList.indexOf(allInfo.getLeagueId()) >= 0) {
                    filterAllInfo(allInfo);
                }
            }
        } else {
            if (PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.FOOT_INDEX).size() > 0) {
                List<String> list = PreferenceUtil.getDataList(FootBallMatchFilterTypeEnum.FOOT_INDEX);
                for (NewOddsInfo.AllInfoBean allInfo : defaultData) {
                    for (String id : list) {
                        if (allInfo.getLeagueId().equals(id)) {
                            filterAllInfo(allInfo);
                        }
                    }
                }
            } else {
                for (NewOddsInfo.AllInfoBean allInfo : defaultData) {
                    if (allInfo.isHot()) {
                        filterAllInfo(allInfo);
                    }
                }
            }
        }
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
    }

    /**
     * 过滤单条信息
     *
     * @param allInfo allInfoBean
     */
    private void filterAllInfo(NewOddsInfo.AllInfoBean allInfo) {
        NewOddsInfo.AllInfoBean clone = allInfo.clone();


        //公司list
        List<NewOddsInfo.AllInfoBean.ComListBean> comList = clone.getComList();

        //单个公司
        ListIterator<NewOddsInfo.AllInfoBean.ComListBean> iterator = comList.listIterator();


        // 不能直接粗暴的 remove，因为持有的是引用也会把 default 中的修改掉
        while (iterator.hasNext()) {  //遍历这个公司
            NewOddsInfo.AllInfoBean.ComListBean next = iterator.next();

            if (!isOddsShow(next)) {

                iterator.remove();
            }
        }
        if (comList.size() > 0) {
            filterData.add(clone);
        }
    }

    /**
     * 处理公司数据
     *
     * @param company company
     */
    private void handleCompany(List<NewOddsInfo.CompanyBean> company) {
        ArrayList<NewOddsInfo.CompanyBean> companyList = parentFragment.getCompanyList();
        parentFragment.showRightButton();
        if (companyList.isEmpty()) {

            // 默认选中头两个公司
            int size = company.size();
            size = size <= 2 ? size : 2;
            for (int i = 0; i < size; i++) {
                company.get(i).setIsChecked(true);
            }
            companyList.addAll(company);
        }
    }

    /**
     * 更新赔率
     *
     * @param result 数据
     */
    public void updateOdds(WebSocketCPIResult<List<WebSocketCPIResult.UpdateOdds>> result) {
        // 1. 先找到当前 fragment 类型的 赔率数据
        List<WebSocketCPIResult.UpdateOdds> data = result.getData();
        for (WebSocketCPIResult.UpdateOdds odds : data) {
            // 更新 DefaultData 和 FilterData
            updateOddsIfRequire(odds, result.getThirdId(), true);
            updateOddsIfRequire(odds, result.getThirdId(), false);
        }
    }

    /**
     * 更新时间和状态
     *
     * @param result 数据
     */
    public void updateTimeAndStatus(WebSocketCPIResult<WebSocketCPIResult.UpdateTimeAndStatus> result) {
        updateSourceTimeAndStatus(result, true);
        updateSourceTimeAndStatus(result, false);
    }

    /**
     * 更新比分
     *
     * @param result result
     */
    public void updateScore(WebSocketCPIResult<WebSocketCPIResult.UpdateScore> result) {
        updateSourceScore(result, true);
        updateSourceScore(result, false);
    }

    /**
     * 设置状态
     *
     * @param status status
     */
    public void setStatus(@StatusEnum.Status int status) {
        if (status == StatusEnum.ERROR) {
            filterData.clear();
            mAdapter.notifyDataSetChanged();
        }
        mEmptyView.setStatus(status);
    }

    /**
     * 当前显示的 Fragment 刷新成功时调用 parentFragment 停止刷新并传递日期
     *
     * @param currentDate 日期
     */
    private void refreshOverWithDate(String currentDate) {
        if (parentFragment.getCurrentFragment() == CPIOddsFragment2.this) {
            // 获取当前日期并返回给 CPINewFragment
            parentFragment.setCurrentDate(currentDate);
            parentFragment.setRefreshing(false);
        }
    }

    /**
     * 当前显示的 Fragment 刷新成功时调用 parentFragment 停止刷新
     */
    private void refreshOver() {
        if (parentFragment.getCurrentFragment() == CPIOddsFragment2.this) {
            parentFragment.setRefreshing(false);
        }
    }

    /**
     * 把 1，2，3 转化为该 Fragment 对应的 TypeString
     *
     * @param oddType oddType
     * @return type
     */
    private String convertTypeString(String oddType) {
        // 1 - 亚盘，2 - 欧赔，3 - 大小球
        if ("1".equals(oddType)) {
            return OddsTypeEnum.PLATE;
        } else if ("2".equals(oddType)) {
            return OddsTypeEnum.OP;
        } else {
            return OddsTypeEnum.BIG;
        }
    }

    /**
     * 需要更新的赔率更新
     *
     * @param odds 赔率
     */
    private void updateOddsIfRequire(WebSocketCPIResult.UpdateOdds odds,
                                     String thirdId,
                                     boolean isDefault) {
        String oddTypeString = convertTypeString(odds.getOddType());
        // 赔率类型和 Fragment 类型一致
        if (oddTypeString.equals(type)) {
            // 选择要更新的数据源
            List<NewOddsInfo.AllInfoBean> infoBeanList = getDataSource(isDefault);
            // 2. 遍历数据源更新数据
            if (infoBeanList == null) return;
            for (NewOddsInfo.AllInfoBean item : infoBeanList) {
                NewOddsInfo.AllInfoBean.MatchInfoBean matchInfo = item.getMatchInfo();
                // 找到赛事 ID 相等的赛事
                if (matchInfo.getMatchId().equals(thirdId)) {
                    if (item.getComList() == null) continue;
                    for (NewOddsInfo.AllInfoBean.ComListBean comListBean : item.getComList()) {
                        String comId = comListBean.getComId();
                        if (comId.equals(odds.getComId())) {
                            comListBean.updateCurrLevel(odds);
                            // 赔率是显示的才通知刷新
                            if (!isDefault && isOddsShow(comListBean)) {
                                mAdapter.notifyItemChanged(filterData.indexOf(item));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 更新本身的数据源和过滤后的数据源的时间和状态
     *
     * @param result    result
     * @param isDefault isDefault
     */
    private void updateSourceTimeAndStatus(WebSocketCPIResult<WebSocketCPIResult.UpdateTimeAndStatus> result,
                                           boolean isDefault) {
        List<NewOddsInfo.AllInfoBean> infoBeanList = getDataSource(isDefault);
        if (infoBeanList == null) return;
        for (NewOddsInfo.AllInfoBean item : infoBeanList) {
            NewOddsInfo.AllInfoBean.MatchInfoBean matchInfo = item.getMatchInfo();
            // 找到赛事 ID 相等的更新时间和状态
            if (matchInfo.getMatchId().equals(result.getThirdId())) {
                WebSocketCPIResult.UpdateTimeAndStatus data = result.getData();
                int statusOrigin = data.getStatusOrigin();
                int keepTime = data.getKeepTime();
                if (keepTime != 0) {
                    matchInfo.setOpenTime(keepTime + "");
                }
                matchInfo.setMatchState(statusOrigin + "");
                if (!isDefault) {
                    mAdapter.notifyItemChanged(infoBeanList.indexOf(item));
                }
            }
        }
    }

    /**
     * 更新本身的数据源和过滤后的数据源的比分
     *
     * @param result    result
     * @param isDefault isDefault
     */
    private void updateSourceScore(WebSocketCPIResult<WebSocketCPIResult.UpdateScore> result,
                                   boolean isDefault) {
        List<NewOddsInfo.AllInfoBean> infoBeanList = getDataSource(isDefault);
        if (infoBeanList == null) return;
        for (NewOddsInfo.AllInfoBean item : infoBeanList) {
            NewOddsInfo.AllInfoBean.MatchInfoBean matchInfo = item.getMatchInfo();
            // 找到赛事 ID 相等的更新比分
            if (matchInfo.getMatchId().equals(result.getThirdId())) {
                WebSocketCPIResult.UpdateScore data = result.getData();
                matchInfo.setMatchResult(data.getMatchResult());
                if (!isDefault) {
                    mAdapter.notifyItemChanged(infoBeanList.indexOf(item));
                }
            }
        }
    }

    /**
     * 是否是显示的赔率
     *
     * @param comListBean ComListBean
     * @return 是否显示
     */
    private boolean

    isOddsShow(NewOddsInfo.AllInfoBean.ComListBean comListBean) {

        //源数据列表中的一个公司

        boolean show = false;

        //获取筛选的公司列表
        ArrayList<NewOddsInfo.CompanyBean> companyList = parentFragment.getCompanyList();


        if (companyList != null && companyList.size() > 0) {

            for (NewOddsInfo.CompanyBean company : companyList) {

                //如果筛选的列表中的公司的被选中的和源数据中的公司Id相同

                if (comListBean.getComId().equals(company.getComId()) && company.isChecked()) {
                    show = true;
                }
            }
        } else {

        }
        return show;
    }

    /**
     * 获取数据源
     *
     * @param isDefault isDefault
     * @return 数据源
     */
    private List<NewOddsInfo.AllInfoBean> getDataSource(boolean isDefault) {
        return isDefault ? defaultData : filterData;
    }

    /**
     * 工厂方法
     *
     * @param type @Type 限制的类型，PLATE, BIG, OP
     * @return CPIOddsListFragment
     */
    public static CPIOddsFragment2 newInstance(@OddsTypeEnum.OddsType String type) {

        Bundle args = new Bundle();
        args.putString(KEY_TYPE, type);
        CPIOddsFragment2 fragment = new CPIOddsFragment2();
        fragment.setArguments(args);
        return fragment;
    }
}