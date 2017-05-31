package com.hhly.mlottery.frame.footballframe.mvptask.myfocus;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.hhly.mlottery.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wangg
 * @desc 我的关注
 * @date 2017/05/31
 */
public class MyFocusFragment extends Fragment {


    @BindView(R.id.focus_back)
    ImageView focusBack;
    @BindView(R.id.focus_radio_group)
    RadioGroup focusRadioGroup;
    @BindView(R.id.focus_search)
    ImageView focusSearch;

    public static MyFocusFragment newInstance() {
        MyFocusFragment myFocusFragment = new MyFocusFragment();
        return myFocusFragment;
    }

    public MyFocusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_focus, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.focus_back, R.id.focus_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.focus_back:
                break;
            case R.id.focus_search:
                break;
        }
    }
}
