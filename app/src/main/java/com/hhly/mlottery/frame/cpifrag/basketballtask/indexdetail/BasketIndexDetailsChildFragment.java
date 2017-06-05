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
        currComId = comId;

        L.d(TAG, "________thirdId=" + thirdId + "_comId=" + comId + "_oddType=" + oddType);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_basket_index_details_child, container, false);
        ButterKnife.bind(this, mView);
        cpiOddsTetailsRightRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        return mView;
    }

    @Override
    public BasketIndexDetailsContract.IndexDetailsChildPresenter initPresenter() {
        return new BasketIndexDetailsChildPresenter(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //loading
        mPresenter.showLoad();
        // mPresenter.showRequestData(comId, thirdId, oddType);
        mPresenter.showRequestData(currComId, thirdId, oddType);

        cpiTxtReLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.showLoad();
                // if (isFirstRequest) { //首次请求
                mPresenter.showRequestData(currComId, thirdId, oddType);
                // } else {

                // mPresenter.getRequestComOddsData(currComId, thirdId, oddType);
                //  }
            }
        });
    }

    @Override
    public void setTitle() {

        setRightOddTiTleName();
    }

    @Override
    public void showRequestSucess() {
        L.d(TAG, "________加载数据成功01。。。");

        cpiRightFlPlateNoData.setVisibility(View.GONE);
        cpiRightFlPlateNetworkError.setVisibility(View.GONE);
        cpiRightFlPlateLoading.setVisibility(View.GONE);
        cpiOddsTetailsRightRecyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public void showLeftListView() {
        final List<Map<String, String>> obList = toListViewParamList(mPresenter.getComLists());

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

                // mPresenter.getRequestComOddsData(obList.get(position).get("id"), thirdId, oddType);
                mPresenter.showRequestData(obList.get(position).get("id"), thirdId, oddType);

            }
        });
    }

    @Override
    public void showRightRecyclerView() {
        basketIndexDetailsChildAdapter = new BasketIndexDetailsChildAdapter(mActivity, mPresenter.getOddsLists(), oddType);
        cpiOddsTetailsRightRecyclerView.setAdapter(basketIndexDetailsChildAdapter);
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

   /* @Override
    public void onErrorComOddFromComId() {
        cpiRightFlPlateNoData.setVisibility(View.GONE);
        cpiRightFlPlateNetworkError.setVisibility(View.VISIBLE);
        cpiRightFlPlateLoading.setVisibility(View.GONE);
        cpiOddsTetailsRightRecyclerView.setVisibility(View.GONE);

        isFirstRequest = false;
    }*/

    /**
     * 右边赔率列表标题名字
     */
    private void setRightOddTiTleName() {
        if (BasketOddsTypeEnum.ASIALET.equals(oddType)) {
            //亚盘
            cpiDishDetailsTxtId.setVisibility(View.VISIBLE);

            cpiHomeDetailsTxtId.setText(R.string.basket_analyze_guest_win);
            cpiDishDetailsTxtId.setText(R.string.basket_analyze_dish);
            cpiGuestDetailsTxtId.setText(R.string.basket_analyze_home_win);

        } else if (BasketOddsTypeEnum.ASIASIZE.equals(oddType)) {
            //大小球
            cpiDishDetailsTxtId.setVisibility(View.VISIBLE);

            cpiHomeDetailsTxtId.setText(R.string.foot_odds_asize_left);
            cpiDishDetailsTxtId.setText(R.string.basket_analyze_dish);
            cpiGuestDetailsTxtId.setText(R.string.foot_odds_asize_right);
        } else if (BasketOddsTypeEnum.EURO.equals(oddType)) {
            //欧赔
            cpiHomeDetailsTxtId.setText(R.string.basket_analyze_guest_win);
            cpiDishDetailsTxtId.setVisibility(View.GONE);
            cpiGuestDetailsTxtId.setText(R.string.basket_analyze_home_win);
        }
    }


  /*  OddsTypeEnum.ASIALET:
            holder.setText(R.id.cpi_item_home_txt, mContext.getString(R.string.basket_analyze_guest_win));
                holder.setText(R.id.cpi_item_odds_txt, mContext.getString(R.string.basket_analyze_dish));
                holder.setVisible(R.id.cpi_item_odds_txt, true);
                holder.setText(R.id.cpi_item_guest_txt, mContext.getString(R.string.basket_analyze_home_win));
                break;
            case BasketOddsTypeEnum.ASIASIZE:
            holder.setText(R.id.cpi_item_home_txt, mContext.getString(R.string.foot_odds_asize_left));
                holder.setText(R.id.cpi_item_odds_txt, mContext.getString(R.string.basket_analyze_dish));
                holder.setVisible(R.id.cpi_item_odds_txt, true);
                holder.setText(R.id.cpi_item_guest_txt, mContext.getString(R.string.foot_odds_asize_right));
                break;
            case BasketOddsTypeEnum.EURO:
            holder.setText(R.id.cpi_item_home_txt, mContext.getString(R.string.basket_analyze_guest_win));
                holder.setText(R.id.cpi_item_odds_txt, mContext.getString(R.string.basket_analyze_dish));
                holder.setVisible(R.id.cpi_item_odds_txt, false);
                holder.setText(R.id.cpi_item_guest_txt, mContext.getString(R.string.basket_analyze_home_win));
                break;
}*/

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
