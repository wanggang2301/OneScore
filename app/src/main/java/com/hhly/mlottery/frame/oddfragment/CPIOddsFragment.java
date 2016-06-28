package com.hhly.mlottery.frame.oddfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.CpiDetailsActivity;
import com.hhly.mlottery.activity.FootballMatchDetailActivityTest;
import com.hhly.mlottery.adapter.cpiadapter.CPIRecyclerListAdapter;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.bean.websocket.WebSocketCPIResult;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.CPIFragment;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * 赔率列表 Fragment
 * <p/>
 * Created by loshine on 2016/6/21.
 */
public class CPIOddsFragment extends Fragment {

    public static final String TYPE_PLATE = "plate"; // 亚盘
    public static final String TYPE_BIG = "big"; // 大小球
    public static final String TYPE_OP = "op"; // 欧赔

    private static final int ERROR = -1; // 访问失败
    private static final int SUCCESS = 1; // 访问成功
    private static final int NODATA = 0; // 暂无数据

    @IntDef({ERROR, SUCCESS, NODATA})
    @Retention(RetentionPolicy.SOURCE)
    @interface Status {
    }

    private static final String KEY_TYPE = "type"; // 类型

    private String type; // 该 Fragment 的类型
    private List<NewOddsInfo.AllInfoBean> defaultData; // 列表数据源
    private List<NewOddsInfo.AllInfoBean> filterData; // 过滤后的数据源
    private List<NewOddsInfo.FileterTagsBean> filterTagList; // 过滤信息
    private CPIRecyclerListAdapter mAdapter; // 适配器

    RecyclerView mRecyclerView;
    FrameLayout mLoadingLayout; // 正在加载中
    FrameLayout mFailedLayout; // 加载失败
    FrameLayout mNoDataLayout; // 暂无数据
    TextView mRefreshTextView; // 刷新

    private CPIFragment parentFragment;
    private CompanyChooseDialogFragment mCompanyChooseDialogFragment;

    /**
     * 定义注解限制类型
     */
    @StringDef({TYPE_PLATE, TYPE_BIG, TYPE_OP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            type = args.getString(KEY_TYPE, TYPE_PLATE);
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
        mLoadingLayout = (FrameLayout) view.findViewById(R.id.cpi_fl_plate_loading);
        mFailedLayout = (FrameLayout) view.findViewById(R.id.cpi_fl_plate_networkError);
        mNoDataLayout = (FrameLayout) view.findViewById(R.id.cpi_fl_plate_noData);
        mRefreshTextView = (TextView) view.findViewById(R.id.cpi_plate_reLoading);

        initRecyclerView();
        mRefreshTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentFragment.setRefreshing(true);
                parentFragment.refreshAllChildFragments();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // attach 的时候获取父 Fragment
        parentFragment = (CPIFragment) getParentFragment();
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
        // RecyclerView Item 单击
        mAdapter.setOnItemClickListener(new CPIRecyclerListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NewOddsInfo.AllInfoBean item) {
                Intent intent = new Intent(getContext(), FootballMatchDetailActivityTest.class);
                intent.putExtra("thirdId", item.getMatchInfo().getMatchId());
                getContext().startActivity(intent);
            }
        });
        // 每个 Item 内部一条赔率的单击
        mAdapter.setOnOddsClickListener(new CPIRecyclerListAdapter.OnOddsClickListener() {
            @Override
            public void onOddsClick(NewOddsInfo.AllInfoBean item,
                                    NewOddsInfo.AllInfoBean.ComListBean odds) {
                List<Map<String, String>> obList = item.toListViewParamList();
                //点击指数页面，传值给详情界面
                Intent intent = new Intent(getContext(), CpiDetailsActivity.class);
                intent.putExtra("obListEntity", (Serializable) obList);
                intent.putExtra("comId", odds.getComId());
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

        if (TYPE_PLATE.equals(type)) {
            map.put("type", "1");
        } else if (TYPE_BIG.equals(type)) {
            map.put("type", "3");
        } else if (TYPE_OP.equals(type)) {
            map.put("type", "2");
        }

        if (!TextUtils.isEmpty(date)) {
            map.put("date", date);
        }

        VolleyContentFast.requestJsonByGet(BaseURLs.URL_NEW_ODDS, map,
                new VolleyContentFast.ResponseSuccessListener<NewOddsInfo>() {
                    @Override
                    public void onResponse(NewOddsInfo jsonObject) {
                        if (jsonObject.getCode() == 500) {
                            setStatus(ERROR);
                            Toast.makeText(getContext(),
                                    "服务器内部错误", Toast.LENGTH_SHORT).show();
                            refreshOver();
                            return;
                        }
                        List<NewOddsInfo.AllInfoBean> allInfo = jsonObject.getAllInfo();
                        if (allInfo.size() == 0) {
                            // 无数据
                            setStatus(NODATA);
                            refreshOver();
                            return;
                        }

                        // 公司数据
                        handleCompany(jsonObject.getCompany());

                        // 更新原始数据源
                        defaultData.clear();
                        defaultData.addAll(allInfo);

                        filterTagList.clear();
                        filterTagList.addAll(jsonObject.getFileterTags());

                        // 更新过滤后的数据源
                        updateFilterData();
                        setStatus(SUCCESS);
                        // 只有当前的 Fragment 刷新成功才可以停止刷新行为
                        refreshOverWithDate(jsonObject.getCurrDate().trim());
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        setStatus(ERROR);
                        VolleyError volleyError = exception.getVolleyError();
                        Toast.makeText(getContext(),
                                volleyError.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        volleyError.printStackTrace();
                        refreshOver();
                    }
                }, NewOddsInfo.class);
    }

    /**
     * 更新过滤数据源（注意要过滤公司赔率信息）
     */
    public void updateFilterData() {
        filterData.clear();
        LinkedList<String> filterList = parentFragment.getFilterList();
        for (NewOddsInfo.AllInfoBean allInfo : defaultData) {
            if (filterList == null || filterList.isEmpty()) {
                if (allInfo.isHot()) {
                    filterAllInfo(allInfo);
                }
            } else {
                if (filterList.indexOf(allInfo.getLeagueId()) >= 0) {
                    filterAllInfo(allInfo);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 过滤单条信息
     *
     * @param allInfo allInfoBean
     */
    private void filterAllInfo(NewOddsInfo.AllInfoBean allInfo) {
        NewOddsInfo.AllInfoBean clone = allInfo.clone();
        List<NewOddsInfo.AllInfoBean.ComListBean> comList = clone.getComList();
        ListIterator<NewOddsInfo.AllInfoBean.ComListBean> iterator = comList.listIterator();

        // 不能直接粗暴的 remove，因为持有的是引用也会把 default 中的修改掉
        while (iterator.hasNext()) {
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
        ArrayList<NewOddsInfo.CompanyBean> companyList =
                parentFragment.getCompanyList();
        if (companyList.isEmpty()
                && parentFragment.getCurrentFragment() == CPIOddsFragment.this) {

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
    public void setStatus(@Status int status) {
        mFailedLayout.setVisibility(status == ERROR ? View.VISIBLE : View.GONE);
        mNoDataLayout.setVisibility(status == NODATA ? View.VISIBLE : View.GONE);
    }

    /**
     * 当前显示的 Fragment 刷新成功时调用 parentFragment 停止刷新并传递日期
     *
     * @param currentDate 日期
     */
    private void refreshOverWithDate(String currentDate) {
        if (parentFragment.getCurrentFragment() == CPIOddsFragment.this) {
            // 获取当前日期并返回给 CPINewFragment
            parentFragment.setCurrentDate(currentDate);
            parentFragment.setRefreshing(false);
        }
    }

    /**
     * 当前显示的 Fragment 刷新成功时调用 parentFragment 停止刷新
     */
    private void refreshOver() {
        if (parentFragment.getCurrentFragment() == CPIOddsFragment.this) {
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
            return TYPE_PLATE;
        } else if ("2".equals(oddType)) {
            return TYPE_OP;
        } else {
            return TYPE_BIG;
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
            for (NewOddsInfo.AllInfoBean item : infoBeanList) {
                NewOddsInfo.AllInfoBean.MatchInfoBean matchInfo = item.getMatchInfo();
                // 找到赛事 ID 相等的赛事
                if (matchInfo.getMatchId().equals(thirdId)) {
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
        for (NewOddsInfo.AllInfoBean item : infoBeanList) {
            NewOddsInfo.AllInfoBean.MatchInfoBean matchInfo = item.getMatchInfo();
            // 找到赛事 ID 相等的更新时间和状态
            if (matchInfo.getMatchId().equals(result.getThirdId())) {
                WebSocketCPIResult.UpdateTimeAndStatus data = result.getData();
                int statusOrigin = data.getStatusOrigin();
                matchInfo.setOpenTime(data.getKeepTime() + "");
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
        for (NewOddsInfo.AllInfoBean item : infoBeanList) {
            NewOddsInfo.AllInfoBean.MatchInfoBean matchInfo = item.getMatchInfo();
            // 找到赛事 ID 相等的更新比分
            if (matchInfo.getMatchId().equals(result.getThirdId())) {
                WebSocketCPIResult.UpdateScore data = result.getData();
                matchInfo.setMatchResult(data.getMatchResult());
                if (!isDefault) {
                    mAdapter.notifyItemChanged(defaultData.indexOf(item));
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
    private boolean isOddsShow(NewOddsInfo.AllInfoBean.ComListBean comListBean) {
        boolean show = false;
        for (NewOddsInfo.CompanyBean company : parentFragment.getCompanyList()) {
            if (comListBean.getComId().equals(company.getComId()) && company.isChecked()) {
                show = true;
            }
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
     * @param type @Type 限制的类型，TYPE_PLATE, TYPE_BIG, TYPE_OP
     * @return CPIOddsListFragment
     */
    public static CPIOddsFragment newInstance(@Type String type) {

        Bundle args = new Bundle();
        args.putString(KEY_TYPE, type);
        CPIOddsFragment fragment = new CPIOddsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
