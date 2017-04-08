package com.hhly.mlottery.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.cpifrag.tennisfragment.TennisIndexDetailsFragment;

/**
 * 描    述：网球指数列表详情页
 * 作    者：mady@13322.com
 * 时    间：2017/4/6
 */
public class TennisIndexDetailsActivity extends BaseActivity  {
    String thirdId;
    String comId;
    String oddType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            thirdId = getIntent().getStringExtra("thirdId");
            comId = getIntent().getStringExtra("comId");
            oddType = getIntent().getStringExtra("oddType");
        }


     /*   //测试
        thirdId = "4432567";
        comId = "2";
        oddType = "asiaLet";
*/

        setContentView(R.layout.activity_tennis_index_tetails);
      TennisIndexDetailsFragment tennisIndexDetailsFragment = (TennisIndexDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        //  String taskId = getIntent().getStringExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID);

        if (tennisIndexDetailsFragment == null) {
            tennisIndexDetailsFragment =TennisIndexDetailsFragment.newInstance(thirdId,comId,oddType);

         /*   if (getIntent().hasExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID)) {
                Bundle bundle = new Bundle();
                bundle.putString(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId);
                addEditTaskFragment.setArguments(bundle);
            } else {
            }*/

            if (getSupportFragmentManager() != null && tennisIndexDetailsFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.contentFrame, tennisIndexDetailsFragment);
                transaction.commit();
            }
        }
    }
}

