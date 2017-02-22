package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.snookerfragment.SnookerFragment;

/**
 * Created by yixq on 2017/2/15.
 * mail：yixq@13322.com
 * describe: snooker使用fragment的测试act
 */

public class SnookerTestAct extends FragmentActivity{

    private SnookerFragment titleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.snooker_test_main_lay);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        titleFragment = new SnookerFragment();

        fragmentTransaction.add(R.id.test_snooker_fragment, titleFragment);
        fragmentTransaction.commit();

    }
}
