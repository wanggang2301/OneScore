package com.hhly.mlottery.frame.footballframe;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hhly.mlottery.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuely198 on 2017/3/16.
 * 竞彩详情页
 */

public class CompetitionAnalysisFragment extends Fragment implements View.OnClickListener {


    private static final String MATCH_THIRDID = "thirdId";
    private String thirdId;
    private View mView;
    private RadioGroup radioGroup;

    private Activity mActivity;
    private Context mContext;
    private RecyclerView recyclerView;
    private List<String> datas;


    public static CompetitionAnalysisFragment newInstance(String thirdId) {
        CompetitionAnalysisFragment fragment = new CompetitionAnalysisFragment();
        Bundle args = new Bundle();
        args.putString(MATCH_THIRDID, thirdId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            thirdId = getArguments().getString(MATCH_THIRDID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        mView = inflater.inflate(R.layout.competition_analysis, container, false);
        mContext = mActivity;
        initView();
        initData();
        return mView;


    }

    private void initData() {


        datas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            datas.add(i + "");
        }
        CompetitionAnalysisAdapter competitionAnalysisAdapter = new CompetitionAnalysisAdapter(mContext, R.layout.football_competitionanalysis, datas);
        recyclerView.setAdapter(competitionAnalysisAdapter);
    }

    private void initView() {

        radioGroup = (RadioGroup) mView.findViewById(R.id.radio_group);


        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.live_event:
                        //  if (!eventType.equals("0")) {
                        mView.findViewById(R.id.compe_give).setVisibility(View.GONE);
                        // }

                        break;
                    case R.id.live_statistics:
                        // if (!eventType.equals("0")) {
                        mView.findViewById(R.id.compe_give).setVisibility(View.VISIBLE);

                        // }
                        break;
                    default:
                        break;
                }
            }
        });


    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }
}
