package com.hhly.mlottery.frame.chartballfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;

/**
 * desc:聊球本地图片界面
 */
public class LocalIconFragment extends Fragment implements View.OnClickListener {

    public static LocalIconFragment newInstance(){
        return new LocalIconFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chartball,container);
    }

    @Override
    public void onClick(View view) {

    }
}
