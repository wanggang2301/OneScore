package com.hhly.mlottery.frame.footballframe;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.adapter.FootBallLiveTextAdapter;
import com.hhly.mlottery.bean.footballDetails.MatchTextLiveBean;
import com.hhly.mlottery.bean.footballDetails.PreLiveText;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author wang gang
 * @date 2016/6/28 15:26
 * @des 足球直播完场文字直播
 */
public class FinishMatchLiveTextFragment extends Fragment {
    private static String TAG = "FinishMatchLiveTextFragment";

    private static final String PARAM_TYPE = "type";

    private FootBallLiveTextAdapter mLiveTextAdapter;


    private static final String FINISHMATCHLIVETEXT_PARAM = "FINISHMATCHLIVETEXT_PARAM";
    private static final String FINISHMATCHLIVETEXT_TYPE = "FINISHMATCHLIVETEXT_TYPE";

    private View mView;

    private Context mContext;

    private ImageView close_image;

    private ArrayList<MatchTextLiveBean> matchTextLiveBeansList;
    private ArrayList<MatchTextLiveBean> dataList;
    private static ArrayList<MatchTextLiveBean> preLiveTextList;

    private RecyclerView mRecyclerView;


    private static final int ERROR = -1;//访问失败
    private static final int SUCCESS = 0;// 访问成功
    private static final int STARTLOADING = 1;// 数据加载中
    private static final int NODATA = 2;// 没有更多数据

    private View moreView;  //加载更多
    // private View bottomview;


    private int lastItem;

    private int count;

    private int pageId;

    private SwipeRefreshLayout mRefreshLayout;//下拉刷新页面


    private String status;

    private BottomSheetBehavior bottomSheetBehavior;


    public static FinishMatchLiveTextFragment newInstance(ArrayList<MatchTextLiveBean> matchTextLiveBeans, String status) {
        FinishMatchLiveTextFragment fragment = new FinishMatchLiveTextFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(FINISHMATCHLIVETEXT_PARAM, matchTextLiveBeans);
        args.putString(FINISHMATCHLIVETEXT_TYPE, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = MyApp.getContext();
        if (getArguments() != null) {
            dataList = getArguments().getParcelableArrayList(FINISHMATCHLIVETEXT_PARAM);
            status = getArguments().getString(FINISHMATCHLIVETEXT_TYPE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // moreView = inflater.inflate(R.layout.finish_live_text_loadmore, container, false);
        mView = View.inflate(mContext, R.layout.football_live_text, null);  //文字直播

        moreView = View.inflate(mContext, R.layout.load, null);  //文字直播
        initView();
        initData();

        return mView;
    }

   /* @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mView = View.inflate(mContext, R.layout.football_live_text, null);  //文字直播

        moreView = View.inflate(mContext, R.layout.load, null);  //文字直播
        initView();
        initData();

        dialog.setContentView(mView);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) mView.getParent()).getLayoutParams();


        final CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
            bottomSheetBehavior.setHideable(true);
            bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {

                    switch (newState) {
                        case BottomSheetBehavior.STATE_HIDDEN:
                            FinishMatchLiveTextFragment.this.dismiss();
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


        close_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
*/

    private void initView() {
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width3 = dm.widthPixels;

        int left = width3 / 2 - DisplayUtil.dip2px(mContext, 120) / 2;
        moreView.setPadding(left, 0, 0, 0);


        // close_image = (ImageView) mView.findViewById(R.id.close_image);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.timerecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // bottomview = mView.findViewById(R.id.bottomview);
    }

    private void initData() {

        matchTextLiveBeansList = new ArrayList<>();
        pageId = 1;
        for (MatchTextLiveBean m : dataList) {
            matchTextLiveBeansList.add(m);
        }


        if (matchTextLiveBeansList.size() > 0) {
            if ("3".equals(matchTextLiveBeansList.get(0).getCode()) || "20".equals(matchTextLiveBeansList.get(0).getCode())) {
                //999代表完场  time位置显示黄色“完场”
                matchTextLiveBeansList.add(0, new MatchTextLiveBean("", "", "0", "0", "4", "99999999", mContext.getResources().getString(R.string.matchFinished_txt) + "", "", "", "0", "", "", "", ""));
            }
        }
        filterLiveText(matchTextLiveBeansList);
        count = matchTextLiveBeansList.size();
        mLiveTextAdapter = new FootBallLiveTextAdapter(getActivity(), matchTextLiveBeansList);


        mRecyclerView.setAdapter(mLiveTextAdapter);

        mLiveTextAdapter.openLoadMore(0, true);

        mLiveTextAdapter.setLoadingView(moreView);

        mLiveTextAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreData();
                    }
                });
            }
        });

    }


    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STARTLOADING:
                    break;
                case SUCCESS:
                    mLiveTextAdapter.notifyDataChangedAfterLoadMore(true);
                    break;
                case NODATA:
                    mLiveTextAdapter.notifyDataChangedAfterLoadMore(false);
                    if (mContext != null) {
                        Toast.makeText(getActivity(), mContext.getResources().getString(R.string.no_data_txt), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ERROR:
                    mLiveTextAdapter.notifyDataChangedAfterLoadMore(true);
                    if (mContext != null) {
                        Toast.makeText(getActivity(), mContext.getResources().getString(R.string.exp_net_status_txt), Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private synchronized void loadMoreData() { //加载更多数据
        String url = BaseURLs.URL_FOOTBALL_LIVE_TEXT_INFO;
        // 设置参数
        if (getActivity() == null) {
            return;
        }


        String mThirdId = ((FootballMatchDetailActivity) getActivity()).mThirdId;


        pageId = pageId + 1;

        Map<String, String> myPostParams = new HashMap<>();
        myPostParams.put("thirdId", mThirdId);
        myPostParams.put("textId", String.valueOf(pageId));

        VolleyContentFast.requestJsonByPost(url, myPostParams, new VolleyContentFast.ResponseSuccessListener<PreLiveText>() {
            @Override
            public void onResponse(PreLiveText jsonObject) {
                // 访问成功
                if (jsonObject != null && "200".equals(jsonObject.getResult())) {
                    preLiveTextList = new ArrayList<MatchTextLiveBean>();
                    preLiveTextList = (ArrayList<MatchTextLiveBean>) jsonObject.getLive();// 获取当前页的下一页数据

                    L.d("ddffgg", "加载更多成功");

                    if (preLiveTextList.size() > 0) {
                        filterLiveText(preLiveTextList);
                        matchTextLiveBeansList.addAll(preLiveTextList);
                        mHandler.sendEmptyMessage(SUCCESS);// 访问成功

                    } else {
                        mHandler.sendEmptyMessage(NODATA);
                    }

                } else {
                    // 后台没请求到数据
                    mHandler.sendEmptyMessage(NODATA);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(ERROR);// 访问失败
            }
        }, PreLiveText.class);
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
