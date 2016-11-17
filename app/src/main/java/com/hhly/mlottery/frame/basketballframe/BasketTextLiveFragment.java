package com.hhly.mlottery.frame.basketballframe;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.basketball.BasketBallTextLiveAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasketTextLiveFragment extends Fragment {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private View mView;

    private BasketBallTextLiveAdapter mBasketBallTextLiveAdapter;

    public static BasketTextLiveFragment newInstance() {
        BasketTextLiveFragment basketTextLiveFragment = new BasketTextLiveFragment();
        return basketTextLiveFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_basket_text_live, container, false);
        ButterKnife.bind(this, mView);
        initView();
        return mView;
    }

    List<String> list;

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");
        list.add("123");


        mBasketBallTextLiveAdapter = new BasketBallTextLiveAdapter(R.layout.item_basket_text_live, list);
        recyclerView.setAdapter(mBasketBallTextLiveAdapter);

        mBasketBallTextLiveAdapter.openLoadMore(0, true);



       /* mBasketBallTextLiveAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                list.add("大家好");
                list.add("大家好");
                list.add("大家好");
                list.add("大家好");
                list.add("大家好");
                list.add("大家好");
                list.add("大家好");
                list.add("大家好");
                list.add("大家好");
                list.add("大家好");nnnn
                list.add("大家好");
                list.add("大家好");
                mBasketBallTextLiveAdapter.notifyDataSetChanged();


            }
        });*/

    }

}
