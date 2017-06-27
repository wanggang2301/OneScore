package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.hhly.mlottery.R;
import com.hhly.mlottery.mvptask.myfocus.MyFocusFragment;

/**
 * @author: Wangg
 * @name：xxx
 * @description: 我的关注
 * @created on:2017/5/31  15:55.
 */

public class MyFocusActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.focus_my);

        MyFocusFragment myFocusFragment = (MyFocusFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (myFocusFragment == null) {
            myFocusFragment = MyFocusFragment.newInstance(getIntent().getIntExtra("type",0));
            if (getSupportFragmentManager() != null && myFocusFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.contentFrame, myFocusFragment);
                transaction.commit();
            }
        }
    }
}
