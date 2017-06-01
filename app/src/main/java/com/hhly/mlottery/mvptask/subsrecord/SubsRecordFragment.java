package com.hhly.mlottery.mvptask.subsrecord;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;

/**
 * @author wangg
 * @desc 订阅记录SubsRecordFragment
 * @date 2017 六一儿童节
 */

public class SubsRecordFragment extends Fragment {


    public static SubsRecordFragment newInstance() {
        SubsRecordFragment subsRecordFragment = new SubsRecordFragment();
        return subsRecordFragment;
    }


    public SubsRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subs_record, container, false);
    }

}
