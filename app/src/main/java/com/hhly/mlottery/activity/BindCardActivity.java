package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.withdrawandbindcard.BindCardFragment;
import com.hhly.mlottery.frame.withdrawandbindcard.WithDrawFragment;

/**
 * 描    述：绑定银行卡页面
 * 作    者：mady@13322.com
 * 时    间：2017/6/10
 */
public class BindCardActivity extends BaseActivity {

    private String mName;
    private BindCardFragment mFragment;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_card);
        if(getIntent().getExtras()!=null){
            mName= (String) getIntent().getExtras().get("cardName");
        }
        FragmentManager fragmentManager=getSupportFragmentManager();

        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        mFragment= BindCardFragment.newInstance(mName);
        fragmentTransaction.replace(R.id.bind_card_container,mFragment);
        fragmentTransaction.commit();
    }
}
