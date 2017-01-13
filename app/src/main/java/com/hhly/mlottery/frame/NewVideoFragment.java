package com.hhly.mlottery.frame;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballActivity;
import com.hhly.mlottery.adapter.InforFragmentAdapter;
import com.hhly.mlottery.frame.video.BaskballVideoFragemnt;
import com.hhly.mlottery.frame.video.ComprehensiveVideoFragment;
import com.hhly.mlottery.frame.video.FootballVideoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: OneScoreGit
 * @author:Administrator luyao
 * @Description: 新版视频直播
 * @data: 2016/9/5 16:11
 */
public class NewVideoFragment extends Fragment  implements View.OnClickListener{

    private TextView mPublic_txt_title;
    private ViewPager mViewpager;
    private TabLayout mTabs;
    private List<String> mTitles;
    private List<Fragment> mFragments;

    private final int COMPREHENSIVE_FRAGMENT = 0;
    private final int FOOTBALL_FRAGMENT = 1;
    private final int BASKBALL_FRAGMENT = 2;
    private InforFragmentAdapter mPureViewPagerAdapter;
    private View mView;
    private Activity mActivity;
    private Context mContext;
    private ImageView mScroll_refresh;
    private int mOffsetPixels=1;
    private ComprehensiveVideoFragment mComprehensiveVideoFragment;
    private FootballVideoFragment mFootballVideoFragment;
    private BaskballVideoFragemnt mBaskballVideoFragemnt;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mContext = mActivity;
        mView = View.inflate(mContext, R.layout.video, null);

        //初始化视图
        initView();
        setupViewPager();

        return mView;
    }


    private void initView() {


        mViewpager = (ViewPager) mView.findViewById(R.id.viewpager);

        mPublic_txt_title = (TextView) mView.findViewById(R.id.public_txt_title);
        mPublic_txt_title.setText(getString(R.string.direct_seeding));

        mView. findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        mView.findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        //返回键
        mView.findViewById(R.id.public_img_back).setOnClickListener(this);

        //悬浮刷新
        mScroll_refresh = (ImageView) mView.findViewById(R.id.scroll_refresh);
        mScroll_refresh.setOnClickListener(this);
    }

    private void setupViewPager() {

        mTabs = (TabLayout) mView.findViewById(R.id.tabs);
        mTitles = new ArrayList<>();
        mTitles.add(getString(R.string.comprehensive));
        mTitles.add(getString(R.string.football));
        mTitles.add(getString(R.string.baskball));


        mFragments = new ArrayList<>();
        mComprehensiveVideoFragment = ComprehensiveVideoFragment.newInstance(COMPREHENSIVE_FRAGMENT);
        mFootballVideoFragment = FootballVideoFragment.newInstance(FOOTBALL_FRAGMENT);
        mBaskballVideoFragemnt = BaskballVideoFragemnt.newInstance(BASKBALL_FRAGMENT);
        mFragments.add(mComprehensiveVideoFragment);
        mFragments.add(mFootballVideoFragment);
        mFragments.add(mBaskballVideoFragemnt);

        mPureViewPagerAdapter = new InforFragmentAdapter(getChildFragmentManager(),mFragments, mTitles );


        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {



            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (positionOffsetPixels == 0) {
                    switch (position) {
                        case COMPREHENSIVE_FRAGMENT:
                            mOffsetPixels = 1;
                         //((RollBallFragment) fragments.get(position)).feedAdapter();
                            break;
                        case FOOTBALL_FRAGMENT:
                            mOffsetPixels=2;
                            //((FootballVideoFragment) mFragments.get(position)).reFH();
                            break;
                        case BASKBALL_FRAGMENT:
                            mOffsetPixels=3;
                           // ((BaskballVideoFragemnt) mFragments.get(position)).reFH();
                            break;

                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewpager.setAdapter(mPureViewPagerAdapter);
        mTabs.setupWithViewPager(mViewpager);
        mTabs.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabs.setTabMode(TabLayout.MODE_FIXED);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.public_img_back:
                ((FootballActivity)getActivity()).eventBusPost();
                ((FootballActivity) getActivity()).finish();
                break;
            case R.id.scroll_refresh:
                setStartAnmotion(v);
                if(mOffsetPixels==1){
                    mComprehensiveVideoFragment.reFH();//悬浮刷新综合数据
                }else if(mOffsetPixels==2) {
                   mFootballVideoFragment.reFH();//悬浮刷新足球数据
                }else if(mOffsetPixels==3) {
                    mBaskballVideoFragemnt.reFH();//悬浮刷新篮球数据
                }else {
                    return;
                }
                break;
            default:
                break;
        }
    }

    private void setStartAnmotion(View v) {
         RotateAnimation animation =new RotateAnimation(0f,360f, Animation.RELATIVE_TO_SELF,
                0.5f,Animation.RELATIVE_TO_SELF,0.5f);
          animation.setDuration(1000);
         v.startAnimation(animation);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (Activity) context;
    }
}
