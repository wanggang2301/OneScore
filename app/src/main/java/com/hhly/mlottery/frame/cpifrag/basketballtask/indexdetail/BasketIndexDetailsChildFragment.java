package com.hhly.mlottery.frame.cpifrag.basketballtask.indexdetail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasketIndexDetailsChildFragment extends Fragment {


    public BasketIndexDetailsChildFragment() {
        // Required empty public constructor
    }

    public static BasketIndexDetailsChildFragment newInstance() {
        BasketIndexDetailsChildFragment basketIndexDetailsChildFragment = new BasketIndexDetailsChildFragment();

        return basketIndexDetailsChildFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basket_index_details_child, container, false);
    }

}
