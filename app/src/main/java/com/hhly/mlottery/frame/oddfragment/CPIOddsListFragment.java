package com.hhly.mlottery.frame.oddfragment;

import android.content.Context;
import android.os.Bundle;
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

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.cpiadapter.CPIRecyclerListAdapter;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.CPINewFragment;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 赔率列表 Fragment
 * <p>
 * Created by loshine on 2016/6/21.
 */
public class CPIOddsListFragment extends Fragment {

    public static final String TYPE_PLATE = "plate"; // 亚盘
    public static final String TYPE_BIG = "big"; // 大小球
    public static final String TYPE_OP = "op"; // 欧赔

    private static final int ERROR = -1; // 访问失败
    private static final int SUCCESS = 0; // 访问成功
    private static final int STARTLOADING = 1; // 数据加载中
    private static final int NODATA = 400; // 暂无数据
    private static final int NODATA_CHILD = 500; // 里面内容暂无数据

    private static final String KEY_TYPE = "type"; // 类型

    private String type; // 该 Fragment 的类型
    private List<NewOddsInfo.AllInfoBean> datas; // 列表数据源
    private CPIRecyclerListAdapter mAdapter; // 适配器

    RecyclerView mRecyclerView;
    FrameLayout mLoadingLayout; // 正在加载中
    FrameLayout mFailedLayout; // 加载失败
    FrameLayout mNoDataLayout; // 暂无数据
    TextView mRefreshTextView; // 刷新

    private CPINewFragment parentFragment;

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
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentFragment = (CPINewFragment) getParentFragment();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        datas = new ArrayList<>();
        mAdapter = new CPIRecyclerListAdapter(datas, type);
        mRecyclerView.setAdapter(mAdapter);
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
                        // 获取当前日期并返回给 CPINewFragment
                        String currentDate = jsonObject.getCurrDate().trim();
                        parentFragment.setCurrentDate(currentDate);
                        datas.clear();
                        datas.addAll(jsonObject.getAllInfo());
                        mAdapter.notifyDataSetChanged();
                        parentFragment.setRefreshing(false);
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        exception.getVolleyError().printStackTrace();
                        parentFragment.setRefreshing(false);
                    }
                }, NewOddsInfo.class);
    }

    /**
     * 工厂方法
     *
     * @param type @Type 限制的类型，TYPE_PLATE, TYPE_BIG, TYPE_OP
     * @return CPIOddsListFragment
     */
    public static CPIOddsListFragment newInstance(@Type String type) {

        Bundle args = new Bundle();
        args.putString(KEY_TYPE, type);
        CPIOddsListFragment fragment = new CPIOddsListFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
