package com.hhly.mlottery.frame.footframe;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballMatchDetailActivityTest;
import com.hhly.mlottery.adapter.football.BottomOddsAdapter;
import com.hhly.mlottery.bean.footballDetails.BottomOddsDetails;
import com.hhly.mlottery.bean.footballDetails.BottomOddsDetailsItem;
import com.hhly.mlottery.bean.footballDetails.BottomOddsItem;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 底部赔率弹窗
 * <p/>
 * Created by Administrator on 2016/5/11.
 */
public class BottomOddsDetailsFragment extends BottomSheetDialogFragment {


    private final int ERROR = -1;//访问失败
    private final int SUCCESS = 0;// 访问成功
    private final int STARTLOADING = 1;// 正在加载中
    private final int NODATA = 2;// 正在加载中


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

    /**
     * 重新加载
     */
    private FrameLayout fl_loading;

    /**
     * 加载出错
     */
    private FrameLayout fl_net_error;


    /**
     * 没有数据
     */

    private FrameLayout fl_no_data;

    /**
     * 内容部分
     */
    private LinearLayout ll_content;

    private List<BottomOddsDetailsItem> mBottomOddsDetailsItemList;


    private TextView reLoading;

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
        first_odd.setText(" " + mBottomOddsItem.getLeft() + " " + mBottomOddsItem.getMiddle() + " " + mBottomOddsItem.getRight());

        initOddsDetails();

        close_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case STARTLOADING:// 正在加载中
                    fl_loading.setVisibility(View.VISIBLE);
                    fl_net_error.setVisibility(View.GONE);
                    ll_content.setVisibility(View.GONE);
                    fl_no_data.setVisibility(View.GONE);
                    break;
                case SUCCESS:// 加载成功
                    fl_loading.setVisibility(View.GONE);
                    fl_net_error.setVisibility(View.GONE);
                    fl_no_data.setVisibility(View.GONE);
                    ll_content.setVisibility(View.VISIBLE);
                    break;

                case NODATA:
                    fl_loading.setVisibility(View.GONE);
                    fl_net_error.setVisibility(View.GONE);
                    ll_content.setVisibility(View.GONE);
                    fl_no_data.setVisibility(View.VISIBLE);
                    break;
                case ERROR:// 加载失败
                    fl_loading.setVisibility(View.GONE);
                    fl_net_error.setVisibility(View.VISIBLE);
                    ll_content.setVisibility(View.GONE);
                    fl_no_data.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private void initView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        close_image = (ImageView) mView.findViewById(R.id.close_image);
        first_odd = (TextView) mView.findViewById(R.id.first_odd);
        odd_title = (TextView) mView.findViewById(R.id.odd_title);
        fl_loading = (FrameLayout) mView.findViewById(R.id.fl_loading);
        fl_net_error = (FrameLayout) mView.findViewById(R.id.fl_networkError);
        fl_no_data = (FrameLayout) mView.findViewById(R.id.fl_nodata);
        ll_content = (LinearLayout) mView.findViewById(R.id.ll_content);
        reLoading = (TextView) mView.findViewById(R.id.reLoading);


        reLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initOddsDetails();
            }
        });
    }

    /**
     * 请求赔率详细数据
     */
    private void initOddsDetails() {
        if (getActivity() == null) {
            return;
        }

        mHandler.sendEmptyMessage(STARTLOADING);// 正在加载数据中

        Map<String, String> params = new HashMap<>();
        params.put("thirdId", ((FootballMatchDetailActivityTest) getActivity()).mThirdId);
        //
        params.put("oddType", mType + "");

        //String url = "http://192.168.10.242:8181/mlottery/core/footballBallList.ballListDetail.do";

        VolleyContentFast.requestJsonByGet(BaseURLs.URL_FOOTBALL_DETAIL_BALLLISTDETAIL_INFO, params,
                new VolleyContentFast.ResponseSuccessListener<BottomOddsDetails>() {
                    @Override
                    public void onResponse(BottomOddsDetails bottomOddsDetails) {
                        if (!"200".equals(bottomOddsDetails.getResult())) {
                            return;
                        }

                        if (bottomOddsDetails.getMatchoddlist() == null || bottomOddsDetails.getMatchoddlist().size() <= 0) {
                            L.d("ffffff", "无数据");
                            mHandler.sendEmptyMessage(NODATA);
                            return;
                        }

                        mBottomOddsDetailsItemList = bottomOddsDetails.getMatchoddlist();

                        loadData();

                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        L.d("ffffff", "错误");

                        mHandler.sendEmptyMessage(ERROR);
                    }
                }, BottomOddsDetails.class
        );


    }

    /**
     * 加载数据
     */
    private void loadData() {
        mAdapter = new BottomOddsAdapter(context, mBottomOddsDetailsItemList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mHandler.sendEmptyMessage(SUCCESS);

    }

    public void updateAdapyter(BottomOddsDetailsItem bottomOddsDetailsItem) {
        mBottomOddsDetailsItemList.add(0, bottomOddsDetailsItem);
        mAdapter.notifyDataSetChanged();
    }


}
