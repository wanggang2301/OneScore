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
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.basketdetails.BasketEachTextLiveBean;
import com.hhly.mlottery.util.FragmentUtils;
import com.hhly.mlottery.util.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @BindView(R.id.textlive_notext)
    FrameLayout textliveNotext;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.fl_pb)
    FrameLayout flPb;
    @BindView(R.id.network_exception_reload_btn)
    TextView networkExceptionReloadBtn;

    @BindView(R.id.fl_error)
    FrameLayout flError;

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


    public static BasketLiveFragment newInstance() {
        BasketLiveFragment basketLiveFragment = new BasketLiveFragment();

        return basketLiveFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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


        flError.setVisibility(View.GONE);

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

    }


    public void onEventMainThread(BasketDetailMatchPreEventBus b) {
        if ("0".equals(b.getMsg())) {
            flContent.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            textliveNotext.setVisibility(View.VISIBLE);
            flPb.setVisibility(View.GONE);
            flError.setVisibility(View.GONE);

        } else if ("1".equals(b.getMsg())) {
            flContent.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.VISIBLE);
            textliveNotext.setVisibility(View.GONE);
            flPb.setVisibility(View.GONE);
            flError.setVisibility(View.GONE);

        } else if ("2".equals(b.getMsg())) {
            flContent.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            textliveNotext.setVisibility(View.GONE);
            flPb.setVisibility(View.GONE);
            flError.setVisibility(View.VISIBLE);
        }
    }

    public void switchFragment(int position) {

//        currentFramnet = FragmentUtils.switchFragment(fragmentManager, R.id.fl_content, currentFramnet, fragments.get(position).getClass(), null, false, fragments.get(position).getClass().getSimpleName() + position, false);
        FragmentUtils.replaceFragment(fragmentManager,R.id.fl_content,fragments.get(position));
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.network_exception_reload_btn)
    public void onClick() {
        flPb.setVisibility(View.VISIBLE);
        flError.setVisibility(View.GONE);

        EventBus.getDefault().post(new BasketDetailLiveTextRefresh("network_exception_reload_btn"));
    }
}
