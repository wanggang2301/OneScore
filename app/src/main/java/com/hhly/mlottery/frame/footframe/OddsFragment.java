package com.hhly.mlottery.frame.footframe;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.oddfragment.PlateFragment;
import com.umeng.analytics.MobclickAgent;

/**
 * 指数fragment
 * tjl
 */
public class OddsFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private View mView;
    private Context mContext;

    private Button odd_plate_btn, odd_op_btn, odd_big_btn;

    private FragmentManager fragmentManager;
    //    private Fragment fragment;
    private PlateFragment mPlateFragment;
    private boolean isVisible;

    public static OddsFragment newInstance(String param1, String param2) {
        OddsFragment fragment = new OddsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.fragment_odds, container, false);
        InitView();
        return mView;
    }

    private void InitView() {
//        mPlateFragment = new PlateFragment();// 默认选中’亚盘‘
        //亚盘 欧赔，大小
        odd_plate_btn = (Button) mView.findViewById(R.id.odd_plate_btn);
        odd_op_btn = (Button) mView.findViewById(R.id.odd_op_btn);
        odd_big_btn = (Button) mView.findViewById(R.id.odd_big_btn);

        odd_plate_btn.setOnClickListener(this);
        odd_op_btn.setOnClickListener(this);
        odd_big_btn.setOnClickListener(this);

        odd_plate_btn.setSelected(true);// 默认选中’亚盘‘
        odd_plate_btn.setTextColor(ContextCompat.getColor(mContext, R.color.tabhost__selected_text));//默认选中的颜色
        mPlateFragment = new PlateFragment();// 默认选中’亚盘‘
        Bundle bundle = new Bundle();
        bundle.putString("key1", "one");
        mPlateFragment.setArguments(bundle);
        fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.odd_content_fragment, mPlateFragment).commit();
        odd_plate_btn.setClickable(false);// 默认让’亚盘‘不可点

    }

    @Override
    public void onClick(View v) {
        odd_plate_btn.setSelected(false);
        odd_op_btn.setSelected(false);
        odd_big_btn.setSelected(false);

        v.setSelected(true);
        switch (v.getId()) {
            case R.id.odd_plate_btn://亚盘
                MobclickAgent.onEvent(mContext, "Football_MatchData_OddPlateBtn");
                odd_plate_btn.setTextColor(ContextCompat.getColor(mContext, R.color.tabhost__selected_text));
                odd_op_btn.setTextColor(ContextCompat.getColor(mContext, R.color.tabhost_unselected_text));
                odd_big_btn.setTextColor(ContextCompat.getColor(mContext, R.color.tabhost_unselected_text));

                odd_plate_btn.setClickable(false);
                odd_op_btn.setClickable(true);
                odd_big_btn.setClickable(true);
                mPlateFragment = new PlateFragment();
                Bundle bundle = new Bundle();
                bundle.putString("key1", "one");
                mPlateFragment.setArguments(bundle);
                break;
            case R.id.odd_op_btn://欧赔
                MobclickAgent.onEvent(mContext, "Football_MatchData_OddOpBtn");
                odd_plate_btn.setTextColor(ContextCompat.getColor(mContext, R.color.tabhost_unselected_text));
                odd_op_btn.setTextColor(ContextCompat.getColor(mContext, R.color.tabhost__selected_text));
                odd_big_btn.setTextColor(ContextCompat.getColor(mContext, R.color.tabhost_unselected_text));

                odd_plate_btn.setClickable(true);
                odd_op_btn.setClickable(false);
                odd_big_btn.setClickable(true);
                //欧赔
                mPlateFragment = new PlateFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString("key2", "two");
                mPlateFragment.setArguments(bundle2);

                break;
            case R.id.odd_big_btn://大小
                MobclickAgent.onEvent(mContext, "Football_MatchData_OddBigBtn");
                odd_plate_btn.setTextColor(ContextCompat.getColor(mContext, R.color.tabhost_unselected_text));
                odd_op_btn.setTextColor(ContextCompat.getColor(mContext, R.color.tabhost_unselected_text));
                odd_big_btn.setTextColor(ContextCompat.getColor(mContext, R.color.tabhost__selected_text));

                odd_plate_btn.setClickable(true);
                odd_op_btn.setClickable(true);
                odd_big_btn.setClickable(false);
                //大小球
                mPlateFragment = new PlateFragment();
                Bundle bundle3 = new Bundle();
                bundle3.putString("key3", "three");
                mPlateFragment.setArguments(bundle3);
                break;
            default:
                break;
        }
        fragmentManager.beginTransaction().replace(R.id.odd_content_fragment, mPlateFragment).commit();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {//如果是显示
            //相当于Fragment的onResume
            isVisible = true;
        } else {//如果是隐藏
            //相当于Fragment的onPause
            isVisible = false;
        }
    }

    /**
     * 刷新指数
     */
    public void oddPlateRefresh() {
        //如果是显示才让刷新
        if (isVisible) {
            mPlateFragment.InitData();
        } else {
            //刷新无效
        }
    }
}
