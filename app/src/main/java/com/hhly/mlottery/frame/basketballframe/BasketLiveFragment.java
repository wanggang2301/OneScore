package com.hhly.mlottery.frame.basketballframe;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.basketdetails.BasketEachTextLiveBean;
import com.hhly.mlottery.util.FragmentUtils;
import com.hhly.mlottery.util.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * @author: Wangg
 * @Name：BasketLiveFragment
 * @Description: 籃球內頁文字直播Fragment
 * @Created on:2016/11/15.
 */

public class BasketLiveFragment extends Fragment {

    private static final String TAG = "BasketLiveFragment";
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.fl_content)
    FrameLayout flContent;

    private View mView;
    private Context mContext;

    private List<BasketEachTextLiveBean> data;

    private BasketTextLiveFragment mBasketTextLiveFragment;
    private BasketTeamStatisticsFragment mBasketTeamStatisticsFragment;
    private BasketPlayersStatisticsFragment mBasketPlayersStatisticsFragment;

    private FragmentManager fragmentManager;

    private Fragment currentFramnet;


    private List<Fragment> fragments = new ArrayList<>();

    private int currentFrag = 0;

//    private BasketDetailsLiveCallBack mBasketDetailsLiveCallBack;


    public static BasketLiveFragment newInstance() {
        BasketLiveFragment basketLiveFragment = new BasketLiveFragment();

        return basketLiveFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_basket_live, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, mView);

        initView();
        return mView;
    }


    private void initView() {

        fragmentManager = getChildFragmentManager();
        mBasketTextLiveFragment = BasketTextLiveFragment.newInstance();
        mBasketTeamStatisticsFragment = BasketTeamStatisticsFragment.newInstance();
        mBasketPlayersStatisticsFragment = BasketPlayersStatisticsFragment.newInstance();

        //  FragmentUtils.replaceFragment(fragmentManager, R.id.fl_content, mBasketTextLiveFragment);

        fragments.add(mBasketTextLiveFragment);
        fragments.add(mBasketTeamStatisticsFragment);
        fragments.add(mBasketPlayersStatisticsFragment);

        switchFragment(0);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.text_live:
                        //FragmentUtils.replaceFragment(fragmentManager, R.id.fl_content, mBasketTextLiveFragment);
                        currentFrag = 0;
                        switchFragment(0);

                        break;
                    case R.id.statistics_team:
                        // FragmentUtils.replaceFragment(fragmentManager, R.id.fl_content, mBasketTeamStatisticsFragment);
                        currentFrag = 1;

                        switchFragment(1);
                        break;
                    case R.id.statistics_players:
                        // FragmentUtils.replaceFragment(fragmentManager, R.id.fl_content, mBasketPlayersStatisticsFragment);

                        currentFrag = 2;

                        switchFragment(2);
                        break;
                    default:
                        break;
                }
            }
        });

//        mBasketDetailsLiveCallBack = new BasketDetailsLiveCallBack() {
//            @Override
//            public void onClick(String status) {
//
//            }
//        };

    }

    public void switchFragment(int position) {
        currentFramnet = FragmentUtils.switchFragment(fragmentManager, R.id.fl_content, currentFramnet, fragments.get(position).getClass(), null, false, fragments.get(position).getClass().getSimpleName() + position, false);
    }

    public void refresh() {
        if (currentFrag == 0) {
            EventBus.getDefault().post(new BasketDetailLiveTextRefresh("BasketTextLiveFragment"));
            L.d("zxcvbn", "文字直播");
        } else if (currentFrag == 1) {
            EventBus.getDefault().post(new BasketDetailTeamStatisticsRefresh(""));
            L.d("zxcvbn", "球队统计");

        } else if (currentFrag == 2) {
            EventBus.getDefault().post(new BasketDetailPlayersStatisticsRefresh(""));
            L.d("zxcvbn", "球员统计");
        }
    }
}
