package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.hhly.mlottery.R;
import com.hhly.mlottery.mvptask.recommendarticles.RecommendArticlesFragment;


/**
 * @author wangg
 * @desc 推介文章Activity
 * @date 2017/06/02
 */

public class RecommendArticlesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_articles);

        RecommendArticlesFragment recommendArticlesFragment = (RecommendArticlesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (recommendArticlesFragment == null) {
            recommendArticlesFragment = RecommendArticlesFragment.newInstance();
            if (getSupportFragmentManager() != null && recommendArticlesFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.contentFrame, recommendArticlesFragment);
                transaction.commit();
            }
        }

    }
}
