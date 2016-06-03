package com.hhly.mlottery.frame.footframe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;

/**
 * @author wang gang
 * @date 2016/6/3 9:53
 * @des ${TODO}
 */
public class PreHeadInfoFrament extends Fragment {

    private View view;


    public static  PreHeadInfoFrament instance(){
        PreHeadInfoFrament fragment=new PreHeadInfoFrament();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_pre_headinfo,container,false);
        return view;

    }
}
