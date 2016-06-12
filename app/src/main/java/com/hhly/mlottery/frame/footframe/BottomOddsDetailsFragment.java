package com.hhly.mlottery.frame.footframe;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.football.BottomOddsAdapter;
import com.hhly.mlottery.bean.footballDetails.BottomOdds;
import com.hhly.mlottery.bean.footballDetails.BottomOddsItem;
import com.hhly.mlottery.util.net.VolleyContentFast;

/**
 * 底部赔率弹窗
 * <p/>
 * Created by Administrator on 2016/5/11.
 */
public class BottomOddsDetailsFragment extends BottomSheetDialogFragment {

    private static final String BOTTOODDS_PARM = "BOTTOODDS_PARM";

    private static final String PARAM_TYPE = "type";

    public static final int ASIA = 0;
    public static final int BIG_SMALL_BALL = 1;

    public static final int EU = 2;

    private RecyclerView mRecyclerView;

    private int mType = ASIA;

    private BottomOddsItem mBottomOddsItem;


    private BottomOddsAdapter mAdapter;

    private View mView;

    private Context context;

    private ImageView close_image;

    private TextView first_odd;
    private TextView odd_title;

    public static BottomOddsDetailsFragment newInstance(BottomOddsItem b, int type) {
        Bundle args = new Bundle();
        args.putParcelable(BOTTOODDS_PARM, b);
        args.putInt(PARAM_TYPE, type);
        BottomOddsDetailsFragment fragment = new BottomOddsDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mBottomOddsItem = args.getParcelable(BOTTOODDS_PARM);
            mType = args.getInt(PARAM_TYPE);
        }
        this.context = getActivity();
    }

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        mView = View.inflate(getContext(), R.layout.fragment_bottom_odds_details, null);
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
                        BottomOddsDetailsFragment.this.dismiss();
                    }
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });
        }

        if (mType == ASIA) {
            odd_title.setText(context.getResources().getString(R.string.alet_first));
        } else if (mType == BIG_SMALL_BALL) {
            odd_title.setText(context.getResources().getString(R.string.asize_first));

        } else if (mType == EU) {
            odd_title.setText(context.getResources().getString(R.string.eu_first));
        }
        first_odd.setText(mBottomOddsItem.getHomeOdd() + " " + mBottomOddsItem.getHand() + " " + mBottomOddsItem.getGuestOdd());

        //  initOddsDetails();
        mAdapter = new BottomOddsAdapter(context, null);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        close_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private void initView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        close_image = (ImageView) mView.findViewById(R.id.close_image);
        first_odd = (TextView) mView.findViewById(R.id.first_odd);
        odd_title = (TextView) mView.findViewById(R.id.odd_title);
    }

    private void initOddsDetails() {

        // Map<String, String> params = new HashMap<>();
        //params.put("thirdId", mThirdId);
        String url = "http://192.168.31.70:8080/mlottery/core/footballBallList.ballListOverview.do?thirdId=336621&lang=zh";

        VolleyContentFast.requestJsonByGet(url, null,
                new VolleyContentFast.ResponseSuccessListener<BottomOdds>() {
                    @Override
                    public void onResponse(BottomOdds bottomOdds) {
                        if (!bottomOdds.getResult().equals("200")) {
                            return;
                        }


                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        //  mViewHandler.sendEmptyMessage(VIEW_STATUS_NET_ERROR);
                    }
                }, BottomOdds.class
        );
    }


}
