package com.hhly.mlottery.frame.scorefrag;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.widget.BallSelectArrayAdapter;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class SnookerScoreFragment extends Fragment {

    private String[] mItems = {"足球", "篮球", "斯洛克"};
    private Spinner mSpinner;

    private View view;
    private Context mContext;

    /**
     * 帅选
     */
    private ImageView mFilterImgBtn;// 筛选
    /**
     * 设置
     */
    private ImageView mSetImgBtn;// 设置




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        mContext = getActivity();
        view = inflater.inflate(R.layout.fragment_snooker_score, container, false);


        initView();
        initEvent();


        return view;
    }

    private void initView() {
        mSpinner = (Spinner) view.findViewById(R.id.public_txt_left_spinner);
        mSpinner.setVisibility(View.VISIBLE);
        BallSelectArrayAdapter mAdapter = new BallSelectArrayAdapter(mContext, mItems);
        mSpinner.setAdapter(mAdapter);
        mSpinner.setSelection(2, true);

        // 筛选
        mFilterImgBtn = (ImageView) view.findViewById(R.id.public_btn_filter);
        mSetImgBtn = (ImageView) view.findViewById(R.id.public_btn_set);
        mSetImgBtn.setVisibility(View.GONE);
        mFilterImgBtn.setVisibility(View.GONE);
    }


    private void initEvent() {
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                L.d("wangg", "斯洛克===vvvvvvvvvvv==" + position);

                EventBus.getDefault().post(new ScoreSwitchFg(2, position));


                // switchFragment(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
