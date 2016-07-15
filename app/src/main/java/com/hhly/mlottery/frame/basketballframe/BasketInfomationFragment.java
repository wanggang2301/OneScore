package com.hhly.mlottery.frame.basketballframe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;

/**
 * @author wang gang
 * @date 2016/7/14 17:54
 * @des 篮球资料库列表项
 */
public class BasketInfomationFragment extends Fragment {

    private View mView;


    public static BasketInfomationFragment newInstance() {
        BasketInfomationFragment basketInfomationFragment = new BasketInfomationFragment();
        return basketInfomationFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_basket_infomation, container, false);
        return mView;
    }
}
