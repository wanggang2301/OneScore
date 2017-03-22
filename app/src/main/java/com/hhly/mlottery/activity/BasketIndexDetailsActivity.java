package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.cpifrag.basketballtask.indexdetail.BasketIndexDetailsFragment;

public class BasketIndexDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_index_details);
        BasketIndexDetailsFragment basketIndexDetailsFragment = (BasketIndexDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        //  String taskId = getIntent().getStringExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID);

        if (basketIndexDetailsFragment == null) {
            basketIndexDetailsFragment = BasketIndexDetailsFragment.newInstance();

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
