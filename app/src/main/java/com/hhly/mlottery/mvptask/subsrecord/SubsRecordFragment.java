package com.hhly.mlottery.mvptask.subsrecord;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.mvp.ViewFragment;
import com.hhly.mlottery.mvptask.IContract;

/**
 * @author wangg
 * @desc 订阅记录SubsRecordFragment
 * @date 2017 六一儿童节
 */

public class SubsRecordFragment extends ViewFragment<IContract.ISubsRecordPresenter> implements IContract.IChildView {


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

    @Override
    public IContract.ISubsRecordPresenter initPresenter() {
        return new SubsRecordPresenter(this);
    }

    @Override
    public void loading() {

    }

    @Override
    public void responseData() {

    }


    @Override
    public void noData() {

    }

    @Override
    public void onError() {

    }


    //防止Activity内存泄漏
    @Override
    public boolean isActive() {
        return isAdded();
    }
}
