package com.hhly.mlottery.frame.footframe;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballMatchDetailActivityTest;
import com.hhly.mlottery.adapter.LiveTextAdapter;
import com.hhly.mlottery.bean.footballDetails.MatchTextLiveBean;
import com.hhly.mlottery.bean.footballDetails.PreLiveText;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author wang gang
 * @date 2016/6/8 9:31
 * @des ${TODO}
 */
public class FinishMatchLiveTextFragment extends BottomSheetDialogFragment implements AbsListView.OnScrollListener {

    private static String TAG = "FinishMatchLiveTextFragment";

    private static final String PARAM_TYPE = "type";

    private LiveTextAdapter mLiveTextAdapter;


    private static final String FINISHMATCHLIVETEXT_PARAM = "FINISHMATCHLIVETEXT_PARAM";
    private static final String FINISHMATCHLIVETEXT_TYPE = "FINISHMATCHLIVETEXT_TYPE";

    private View mView;

    private Context mContext;

    private ImageView close_image;

    private ArrayList<MatchTextLiveBean> matchTextLiveBeansList;
    private static ArrayList<MatchTextLiveBean> preLiveTextList;

    private ListView mListView;


    private static final int ERROR = -1;//访问失败
    private static final int SUCCESS = 0;// 访问成功
    private static final int STARTLOADING = 1;// 数据加载中
    private static final int NODATA = 2;// 没有更多数据

    private View moreView;  //加载更多

    private int lastItem;

    private int count;

    private static int pageId;

    private SwipeRefreshLayout mRefreshLayout;//下拉刷新页面


    private String status;

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
            matchTextLiveBeansList = getArguments().getParcelableArrayList(FINISHMATCHLIVETEXT_PARAM);
            status = getArguments().getString(FINISHMATCHLIVETEXT_TYPE);
        }
    }

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        mView = View.inflate(mContext, R.layout.live_text, null);  //文字直播
        moreView = View.inflate(mContext, R.layout.load, null);


        initView();

        initData();
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
                        FinishMatchLiveTextFragment.this.dismiss();
                    }
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {

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


    private void initView() {
        close_image = (ImageView) mView.findViewById(R.id.close_image);
        mListView = (ListView) mView.findViewById(R.id.timelistview);
    }

    private void initData() {
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        //只有文字直播的listview滑到顶部才可以下拉刷新
                        if (mListView.getFirstVisiblePosition() != 0) {
                            //  mRefreshLayout.setEnabled(false);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // mRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });

        pageId = 1;

        if (matchTextLiveBeansList.size() > 0) {
            if ("3".equals(matchTextLiveBeansList.get(0).getCode())) {
                //999代表完场  time位置显示黄色“完场”
                matchTextLiveBeansList.add(0, new MatchTextLiveBean("", "", "0", "0", "4", "99999999", mContext.getResources().getString(R.string.matchFinished_txt) + "", "", "", "0", "", ""));
            }
        }
        filterLiveText(matchTextLiveBeansList);

        count = matchTextLiveBeansList.size();
        mListView.addFooterView(moreView);

        mLiveTextAdapter = new LiveTextAdapter(getActivity(), matchTextLiveBeansList);
        mListView.setAdapter(mLiveTextAdapter);
        mListView.setOnScrollListener(this);

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastItem = firstVisibleItem + visibleItemCount - 1;  //减1是因为上面加了个addFooterView
    }

    private boolean isSucessedLoad = true;


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //下拉到空闲是，且最后一个item的数等于数据的总数时，进行更新

        if (lastItem == count && scrollState == this.SCROLL_STATE_IDLE) {
            moreView.setVisibility(view.VISIBLE);

            if (isSucessedLoad) {
                mHandler.sendEmptyMessage(STARTLOADING);
            }

        }
    }


    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STARTLOADING:
                    isSucessedLoad = false;
                    loadMoreData();  //加载更多数据，这里可以使用异步加载
                    break;
                case SUCCESS:
                    mLiveTextAdapter.notifyDataSetChanged();
                    moreView.setVisibility(View.GONE);
                    break;
                case NODATA:
                    Toast.makeText(getActivity(), mContext.getResources().getString(R.string.no_data_txt), Toast.LENGTH_SHORT).show();
                    mListView.removeFooterView(moreView); //移除底部视图
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), mContext.getResources().getString(R.string.exp_net_status_txt), Toast.LENGTH_SHORT).show();
                    //timeListView.removeFooterView(moreView); //移除底部视图
                    break;
                default:
                    break;
            }
        }
    };

    private void loadMoreData() { //加载更多数据
        String url = BaseURLs.URL_FOOTBALL_LIVE_TEXT_INFO;
        // 设置参数
        if (getActivity() == null) {
            return;
        }
        String mThirdId = ((FootballMatchDetailActivityTest) getActivity()).mThirdId;
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

                    if (preLiveTextList.size() > 0) {
                        filterLiveText(preLiveTextList);
                        matchTextLiveBeansList.addAll(preLiveTextList);
                        count = matchTextLiveBeansList.size();
                        mHandler.sendEmptyMessage(SUCCESS);// 访问成功
                    } else {
                        mHandler.sendEmptyMessage(NODATA);
                    }

                } else {
                    // 后台没请求到数据
                    mHandler.sendEmptyMessage(NODATA);
                }
                isSucessedLoad = true;
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(ERROR);// 访问失败
                isSucessedLoad = true;
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
