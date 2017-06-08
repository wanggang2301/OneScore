package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.hhly.mlottery.R;
import com.hhly.mlottery.mvptask.subsrecord.SubsRecordFragment;


/**
 * @author wangg
 * @desc 订阅记录SubsRecordActivity
 * @date 2017 六一儿童节
 */

public class SubsRecordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subs_record);
        SubsRecordFragment subsRecordFragment = (SubsRecordFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (subsRecordFragment == null) {
            subsRecordFragment = SubsRecordFragment.newInstance();
            if (getSupportFragmentManager() != null && subsRecordFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.contentFrame, subsRecordFragment);
                transaction.commit();
            }
        }
    }
}
