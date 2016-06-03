package com.hhly.mlottery.frame.footframe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhly.mlottery.R;

/**
 * @author hhly204
 * @ClassName: TalkAboutBallFragment
 * @Description: 聊球页面
 * @date 2016-6-2
 */
public class TalkAboutBallFragment extends Fragment {
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_talkaboutballfragment, null);
        return mView;
    }
}
