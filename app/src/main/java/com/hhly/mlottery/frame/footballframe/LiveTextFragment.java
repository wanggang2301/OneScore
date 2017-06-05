package com.hhly.mlottery.frame.footballframe;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.FootBallLiveTextAdapter;
import com.hhly.mlottery.bean.footballDetails.MatchTextLiveBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author wang gang
 * @date 2016/6/7 15:40
 * @des 足球内页改版聊球-赛中状态下文字直播详情
 */
public class LiveTextFragment extends Fragment {
    private static final String PARAM_TYPE = "type";

    private FootBallLiveTextAdapter mLiveTextAdapter;


    private static final String LIVETEXT_PARAM = "LIVETEXT_PARAM";
    private static final String LIVETEXT_TYPE = "LIVETEXT_TYPE";

    private View mView;
    private View bottomview;

    private Context context;

    private ImageView close_image;

    private ArrayList<MatchTextLiveBean> matchTextLiveBeans;
    private RecyclerView mRecyclerView;


    private String status;

    public static LiveTextFragment newInstance(ArrayList<MatchTextLiveBean> matchTextLiveBeans, String status) {
        LiveTextFragment fragment = new LiveTextFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(LIVETEXT_PARAM, matchTextLiveBeans);
        args.putString(LIVETEXT_TYPE, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        if (getArguments() != null) {
            matchTextLiveBeans = getArguments().getParcelableArrayList(LIVETEXT_PARAM);
            status = getArguments().getString(LIVETEXT_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = View.inflate(getContext(), R.layout.football_live_text, null);
        initView();

        filterLiveText(matchTextLiveBeans);
        mLiveTextAdapter = new FootBallLiveTextAdapter(getActivity(), matchTextLiveBeans);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setAdapter(mLiveTextAdapter);
  /*      close_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/

        return mView;
    }
/*

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mView = View.inflate(getContext(), R.layout.football_live_text, null);
        initView();
        dialog.setContentView(mView);
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) ((View) mView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
            bottomSheetBehavior.setHideable(true);
            bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    switch (newState) {
                        case BottomSheetBehavior.STATE_HIDDEN:
                            LiveTextFragment.this.dismiss();
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            bottomview.setVisibility(View.VISIBLE);
                            break;
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            bottomview.setVisibility(View.GONE);
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });
        }


    }*/


    private void initView() {
        //  close_image = (ImageView) mView.findViewById(R.id.close_image);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.timerecyclerView);
        // bottomview = mView.findViewById(R.id.bottomview);

    }

    public void updataLiveTextAdapter(List<MatchTextLiveBean> m) {
        matchTextLiveBeans = (ArrayList<MatchTextLiveBean>) m;
        mLiveTextAdapter.notifyDataSetChanged();
    }

    private void filterLiveText(ArrayList<MatchTextLiveBean> list) {

        //0显示  1 不显示

        Iterator<MatchTextLiveBean> iterator = list.iterator();
        while (iterator.hasNext()) {
            MatchTextLiveBean matchTextLiveBean = iterator.next();
            if ("1".equals(matchTextLiveBean.getShowId())) {
                iterator.remove();
            }
        }
    }
}
