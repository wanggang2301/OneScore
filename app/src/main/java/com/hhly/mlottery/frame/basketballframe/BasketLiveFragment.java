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
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.basketdetails.BasketEachTextLiveBean;
import com.hhly.mlottery.util.FragmentUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private String mThirdId;

    public static BasketLiveFragment newInstance(String mThirdId) {
        BasketLiveFragment basketLiveFragment = new BasketLiveFragment();
        Bundle bundle = new Bundle();
        bundle.putString("thirdId", mThirdId);
        basketLiveFragment.setArguments(bundle);

        return basketLiveFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mThirdId = getArguments().getString("thirdId");
        }
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

        mBasketTextLiveFragment = BasketTextLiveFragment.newInstance(mThirdId);
        mBasketTeamStatisticsFragment = BasketTeamStatisticsFragment.newInstance();
        mBasketPlayersStatisticsFragment = BasketPlayersStatisticsFragment.newInstance();

        FragmentUtils.replaceFragment(fragmentManager, R.id.fl_content, mBasketTextLiveFragment);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.text_live:
                        FragmentUtils.replaceFragment(fragmentManager, R.id.fl_content, mBasketTextLiveFragment);
                        break;
                    case R.id.statistics_team:
                        FragmentUtils.replaceFragment(fragmentManager, R.id.fl_content, mBasketTeamStatisticsFragment);
                        break;
                    case R.id.statistics_players:
                        FragmentUtils.replaceFragment(fragmentManager, R.id.fl_content, mBasketPlayersStatisticsFragment);
                        break;
                    default:
                        break;
                }
            }
        });
    }



    /**
     * 文字直播推送更新
     */
    public void updateTextLive(BasketEachTextLiveBean basketEachTextLiveBean) {
        // data.add(basketEachTextLiveBean);
        Toast.makeText(getActivity(), basketEachTextLiveBean.getEventContent(), Toast.LENGTH_SHORT).show();
    }
}
