package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.cpifrag.basketballtask.indexdetail.BasketIndexDetailsFragment;

/**
 * @author: Wangg
 * @name：xxx
 * @description: 篮球指数内页
 * @created on:2017/3/30  10:40.
 */

public class BasketIndexDetailsActivity extends BaseActivity {
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

        setContentView(R.layout.activity_basket_index_details);
        BasketIndexDetailsFragment basketIndexDetailsFragment = (BasketIndexDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        //  String taskId = getIntent().getStringExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID);

        if (basketIndexDetailsFragment == null) {
            basketIndexDetailsFragment = BasketIndexDetailsFragment.newInstance(thirdId, comId, oddType);

         /*   if (getIntent().hasExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID)) {
                Bundle bundle = new Bundle();
                bundle.putString(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId);
                addEditTaskFragment.setArguments(bundle);
            } else {
            }*/

            if (getSupportFragmentManager() != null && basketIndexDetailsFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.contentFrame, basketIndexDetailsFragment);
                transaction.commit();
            }
        }
    }
}
