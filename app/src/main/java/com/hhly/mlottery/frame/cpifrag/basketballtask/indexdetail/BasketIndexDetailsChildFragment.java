package com.hhly.mlottery.frame.cpifrag.basketballtask.indexdetail;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.OddDetailsLeftAdapter;
import com.hhly.mlottery.adapter.basketball.BasketIndexDetailsChildAdapter;
import com.hhly.mlottery.bean.basket.index.BasketIndexDetailsBean;
import com.hhly.mlottery.bean.enums.BasketOddsTypeEnum;
import com.hhly.mlottery.mvp.ViewFragment;
import com.hhly.mlottery.util.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author wangg
 * @desc 篮球指数详情child_frament
 * @date 2017/03/22
 */
public class BasketIndexDetailsChildFragment extends ViewFragment<BasketIndexDetailsContract.IndexDetailsChildPresenter> implements BasketIndexDetailsContract.IndexDetailsChildView {

    private final String TAG = this.getClass().getSimpleName();


    @BindView(R.id.cpi_tails_left_listview)
    ListView cpiTailsLeftListview;
    @BindView(R.id.cpi_right_fl_plate_loading)
    FrameLayout cpiRightFlPlateLoading;
    @BindView(R.id.cpi_txt_reLoading)
    TextView cpiTxtReLoading;
    @BindView(R.id.cpi_right_fl_plate_networkError)
    FrameLayout cpiRightFlPlateNetworkError;
    @BindView(R.id.cpi_right_fl_plate_noData)
    FrameLayout cpiRightFlPlateNoData;
    @BindView(R.id.cpi_home_details_txt_id)
    TextView cpiHomeDetailsTxtId;
    @BindView(R.id.cpi_dish_details_txt_id)
    TextView cpiDishDetailsTxtId;
    @BindView(R.id.cpi_guest_details_txt_id)
    TextView cpiGuestDetailsTxtId;
    @BindView(R.id.cpi_odds_tetails_right_recyclerview)
    RecyclerView cpiOddsTetailsRightRecyclerView;
    private View mView;

    private String thirdId, comId, oddType;
    private Activity mActivity;

    private int currPositon = 0;  //从列表穿过来的公司在左侧的位置

    private OddDetailsLeftAdapter oddDetailsLeftAdapter;
    private BasketIndexDetailsChildAdapter basketIndexDetailsChildAdapter;

    private String currComId;
    private boolean isFirstRequest = false;


    public BasketIndexDetailsChildFragment() {
    }

    public static BasketIndexDetailsChildFragment newInstance(String thirdId, String comId, String oddType) {
        BasketIndexDetailsChildFragment basketIndexDetailsChildFragment = new BasketIndexDetailsChildFragment();
        Bundle bundle = new Bundle();
        bundle.putString("thirdId", thirdId);
        bundle.putString("comId", comId);
        bundle.putString("oddType", oddType);
        basketIndexDetailsChildFragment.setArguments(bundle);
        return basketIndexDetailsChildFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            thirdId = getArguments().getString("thirdId");
            comId = getArguments().getString("comId");
            oddType = getArguments().getString("oddType");
        }

        L.d(TAG, "________thirdId=" + thirdId);
        L.d(TAG, "________comId=" + comId);
        L.d(TAG, "________oddType=" + oddType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_basket_index_details_child, container, false);
        ButterKnife.bind(this, mView);
        cpiOddsTetailsRightRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new BasketIndexDetailsChildPresenter(this);

        //loading
        mPresenter.showLoad();
        mPresenter.showRequestData(comId, thirdId, oddType);

        cpiTxtReLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.showLoad();
                if (isFirstRequest) {
                    mPresenter.showRequestData(comId, thirdId, oddType);
                } else {
                    mPresenter.getRequestComOddsData(currComId, thirdId, oddType);
                }
            }
        });
    }

    @Override
    public void showRequestDataView(BasketIndexDetailsBean b) {

        setRightOddTiTleName();
        //处理左边的公司数据
        handleLeftData(b.getComLists());
        //处理右边的赔率数据
        handleRightData(b.getOddsData());

        cpiRightFlPlateNoData.setVisibility(View.GONE);
        cpiRightFlPlateNetworkError.setVisibility(View.GONE);
        cpiRightFlPlateLoading.setVisibility(View.GONE);
        cpiOddsTetailsRightRecyclerView.setVisibility(View.VISIBLE);

        L.d(TAG, "________加载数据成功01。。。");

    }


    //只获取根据ComId请求过来的公司赔率列表
    @Override
    public void getComOddsFromComId(BasketIndexDetailsBean b) {
        handleRightData(b.getOddsData());
        cpiRightFlPlateNoData.setVisibility(View.GONE);
        cpiRightFlPlateNetworkError.setVisibility(View.GONE);
        cpiRightFlPlateLoading.setVisibility(View.GONE);
        cpiOddsTetailsRightRecyclerView.setVisibility(View.VISIBLE);
        L.d(TAG, "________加载数据成功02。。。");

    }

    @Override
    public void showLoadView() {
        cpiRightFlPlateNoData.setVisibility(View.GONE);
        cpiRightFlPlateNetworkError.setVisibility(View.GONE);
        cpiRightFlPlateLoading.setVisibility(View.VISIBLE);
        cpiOddsTetailsRightRecyclerView.setVisibility(View.GONE);

        L.d(TAG, "________加载数据中。。。");
    }

    @Override
    public void noDataView() {
        cpiRightFlPlateNoData.setVisibility(View.VISIBLE);
        cpiRightFlPlateNetworkError.setVisibility(View.GONE);
        cpiRightFlPlateLoading.setVisibility(View.GONE);
        cpiOddsTetailsRightRecyclerView.setVisibility(View.GONE);

        L.d(TAG, "________没有数据。。。");

    }

    @Override
    public void onError() {
        cpiRightFlPlateNoData.setVisibility(View.GONE);
        cpiRightFlPlateNetworkError.setVisibility(View.VISIBLE);
        cpiRightFlPlateLoading.setVisibility(View.GONE);
        cpiOddsTetailsRightRecyclerView.setVisibility(View.GONE);

        L.d(TAG, "________数据出错。。。");

        isFirstRequest = true;
    }

    @Override
    public void onErrorComOddFromComId() {
        cpiRightFlPlateNoData.setVisibility(View.GONE);
        cpiRightFlPlateNetworkError.setVisibility(View.VISIBLE);
        cpiRightFlPlateLoading.setVisibility(View.GONE);
        cpiOddsTetailsRightRecyclerView.setVisibility(View.GONE);

        isFirstRequest = false;
    }

    /**
     * 右边赔率列表标题名字
     */
    private void setRightOddTiTleName() {
        if (BasketOddsTypeEnum.ASIALET.equals(oddType)) {
            //亚盘
            cpiHomeDetailsTxtId.setText(R.string.foot_odds_alet_left);
            cpiDishDetailsTxtId.setText(R.string.foot_odds_alet_middle);
            cpiGuestDetailsTxtId.setText(R.string.foot_odds_alet_right);

        } else if (BasketOddsTypeEnum.ASIASIZE.equals(oddType)) {
            //大小球
            cpiHomeDetailsTxtId.setText(R.string.foot_odds_asize_left);
            cpiDishDetailsTxtId.setText(R.string.foot_odds_asize_middle);
            cpiGuestDetailsTxtId.setText(R.string.foot_odds_asize_right);
        } else if (BasketOddsTypeEnum.EURO.equals(oddType)) {
            //欧赔
            cpiHomeDetailsTxtId.setText(R.string.foot_odds_eu_left);
            cpiDishDetailsTxtId.setText(R.string.foot_odds_eu_middle);
            cpiGuestDetailsTxtId.setText(R.string.foot_odds_eu_right);
        }
    }


    /**
     * 新版详情左边的数据
     */
    private void handleLeftData(List<BasketIndexDetailsBean.ComListsBean> comListsBeen) {
        final List<Map<String, String>> obList = toListViewParamList(comListsBeen);
        oddDetailsLeftAdapter = new OddDetailsLeftAdapter(mActivity, obList);
        cpiTailsLeftListview.setAdapter(oddDetailsLeftAdapter);
        //根据传过去的postion更改选中的item选中背景

        oddDetailsLeftAdapter.setSelect(currPositon);

        // 详情左边list点击事件
        cpiTailsLeftListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //根据传过去的postion更改选中的item选中背景
                oddDetailsLeftAdapter.setSelect(position);
                currComId = obList.get(position).get("id");
                mPresenter.showLoad();

                mPresenter.getRequestComOddsData(obList.get(position).get("id"), thirdId, oddType);

            }
        });
    }


    //右边的数据需要接口切换请求
    private void handleRightData(List<BasketIndexDetailsBean.OddsDataBean> oddsDataBeanList) {
        basketIndexDetailsChildAdapter = new BasketIndexDetailsChildAdapter(mActivity, oddsDataBeanList, oddType);
        cpiOddsTetailsRightRecyclerView.setAdapter(basketIndexDetailsChildAdapter);
    }


    /**
     * 转化为 Odds 需要的数据规格
     *
     * @return ArrayList
     */
    public ArrayList<Map<String, String>> toListViewParamList(List<BasketIndexDetailsBean.ComListsBean> comListsBeen) {
        ArrayList<Map<String, String>> obList = new ArrayList<>();
        for (int i = 0; i < comListsBeen.size(); i++) {
            Map<String, String> obMap = new HashMap<>();
            obMap.put("id", comListsBeen.get(i).getComId());
            obMap.put("name", comListsBeen.get(i).getComName());
            obMap.put("thirdid", thirdId);
            if (comId.equals(comListsBeen.get(i).getComId())) {
                currPositon = i;
            }

            obList.add(obMap);
        }
        return obList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }
}
