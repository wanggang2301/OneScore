package com.hhly.mlottery.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.basketballframe.FocusBasketballFragment;
import com.hhly.mlottery.frame.footballframe.FocusFragment;
import com.hhly.mlottery.util.FragmentUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的关注界面
 * created by ：155马东运
 */
public class MyFocusActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.my_focus_radio_group)
    RadioGroup mFocusGroup;
    @BindView(R.id.public_img_back)
    ImageView mImgBack;

    TextView mSelectGameFocused; //点击关注比赛


    FragmentManager fragmentManager;
    private List<Fragment> fragments = new ArrayList<>();

    private FocusFragment mFootballFocus;
    private FocusBasketballFragment mBasketballFocus;
    private Fragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_focus);
        ButterKnife.bind(this);
        setListener();
        initView();
    }

    private void setListener() {
        mFocusGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.my_focus_basket:
                        switchFragment(0);
                        break;
                    case R.id.my_focus_football:
                        switchFragment(1);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initView(){
        mImgBack.setOnClickListener(this);
        fragmentManager=getSupportFragmentManager();
        mFootballFocus=FocusFragment.newInstance("","");
        mBasketballFocus= FocusBasketballFragment.newInstance(0);
        fragments.add(mBasketballFocus);
        fragments.add(mFootballFocus);
        switchFragment(0);

    }


    public void switchFragment(int position) {

        currentFragment = FragmentUtils.switchFragment(fragmentManager, R.id.my_focus_container, currentFragment, fragments.get(position).getClass(), null, false, fragments.get(position).getClass().getSimpleName() + position, false);

    }
    public void replaceFragments(int position){
        FragmentUtils.replaceFragment(fragmentManager,R.id.my_focus_container,fragments.get(position));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_img_back:
                MobclickAgent.onEvent(this, "MyFocusActivity_Exit");
                finish();
                break;
        }
    }
}
