package com.hhly.mlottery.frame.basketballframe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;

/**
 * 描    述：篮球资料库 - 排行
 * 作    者：longs@13322.com
 * 时    间：2016/8/9
 */
public class BasketDatabaseRankingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basket_database_ranking, container, false);
    }

    public static BasketDatabaseRankingFragment newInstance() {
        
        Bundle args = new Bundle();
        
        BasketDatabaseRankingFragment fragment = new BasketDatabaseRankingFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
