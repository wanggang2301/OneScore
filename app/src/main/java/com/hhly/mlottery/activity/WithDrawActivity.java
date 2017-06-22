package com.hhly.mlottery.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.withdrawandbindcard.WithDrawFragment;

/**
 * 提现页面
 * crated by mdy155 on 2017/06/10
 */
public class WithDrawActivity extends BaseActivity {

    private String mBalance;
    private WithDrawFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_with_draw);
        if(getIntent().getExtras()!=null){
            mBalance= (String) getIntent().getExtras().get("balance");
        }

        FragmentManager fragmentManager=getSupportFragmentManager();

        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        mFragment=WithDrawFragment.newInstance(mBalance,"");
        fragmentTransaction.replace(R.id.withdraw_container,mFragment);
        fragmentTransaction.commit();
    }
}
