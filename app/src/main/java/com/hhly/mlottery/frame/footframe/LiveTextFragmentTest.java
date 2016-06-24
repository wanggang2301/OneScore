package com.hhly.mlottery.frame.footframe;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.LiveTextAdapter;
import com.hhly.mlottery.bean.footballDetails.MatchTextLiveBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author wang gang
 * @date 2016/6/7 15:40
 * @des 足球内页改版聊球-赛中状态下文字直播详情
 */
public class LiveTextFragmentTest extends BottomSheetDialogFragment {
    private static final String PARAM_TYPE = "type";

    private LiveTextAdapter mLiveTextAdapter;


    private static final String LIVETEXT_PARAM = "LIVETEXT_PARAM";
    private static final String LIVETEXT_TYPE = "LIVETEXT_TYPE";

    private View mView;

    private Context context;

    private ImageView close_image;

    private ArrayList<MatchTextLiveBean> matchTextLiveBeans;
    private ListView mListView;


    private String status;

    public static LiveTextFragmentTest newInstance(ArrayList<MatchTextLiveBean> matchTextLiveBeans, String status) {
        LiveTextFragmentTest fragment = new LiveTextFragmentTest();
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
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        mView = View.inflate(getContext(), R.layout.live_text, null);
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
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        LiveTextFragmentTest.this.dismiss();
                    }
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        }

        filterLiveText(matchTextLiveBeans);
        mLiveTextAdapter = new LiveTextAdapter(getActivity(), matchTextLiveBeans);
        mListView.setAdapter(mLiveTextAdapter);
        close_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private void initView() {
        close_image = (ImageView) mView.findViewById(R.id.close_image);
        mListView = (ListView) mView.findViewById(R.id.timelistview);
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
