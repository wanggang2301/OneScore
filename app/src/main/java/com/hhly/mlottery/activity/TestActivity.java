package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.footframe.TalkAboutBallFragment;

/**
 * @author
 * @ClassName:
 * @Description:
 * @date
 */
public class TestActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        TalkAboutBallFragment talkAboutBallFragment = new TalkAboutBallFragment();
        Bundle bundle = new Bundle();
        bundle.putString("param1", "335177");
        talkAboutBallFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.content, talkAboutBallFragment).commit();
    }
}
