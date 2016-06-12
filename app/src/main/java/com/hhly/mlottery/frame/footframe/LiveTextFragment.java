package com.hhly.mlottery.frame.footframe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballMatchDetailActivity;
import com.hhly.mlottery.adapter.LiveTextAdapter;
import com.hhly.mlottery.bean.footballDetails.MatchTextLiveBean;
import com.hhly.mlottery.bean.footballDetails.PreLiveText;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by asus1 on 2015/12/28.
 */
@SuppressLint("ValidFragment")
public class LiveTextFragment extends Fragment implements AbsListView.OnScrollListener {
    private  Context mContext;

    private View mView;

    private ListView timeListView;

    private static final String LIVETEXT_PARAM = "LIVETEXT_PARAM";

    private static LiveTextAdapter timeAdp;

    private static List<MatchTextLiveBean> list;
    private static List<MatchTextLiveBean> preLiveTextList;
    private static String livestatus;


    private static final int ERROR = -1;//访问失败
    private static final int SUCCESS = 0;// 访问成功
    private static final int STARTLOADING = 1;// 数据加载中
    private static final int NODATA = 2;// 没有更多数据

    private View moreView;  //加载更多

    private int lastItem;

    private int count;

    private static int pageId;

    private SwipeRefreshLayout mRefreshLayout;//下拉刷新页面



    @SuppressLint("ValidFragment")
    public LiveTextFragment(Context mContext,List<MatchTextLiveBean> matchTextLiveBeanList, String status) {
        this.mContext = mContext;
        pageId = 1;

        if (matchTextLiveBeanList.size() > 0) {
            if ("3".equals(matchTextLiveBeanList.get(0).getCode())) {
                //999代表完场  time位置显示黄色“完场”
                matchTextLiveBeanList.add(0, new MatchTextLiveBean("", "", "0", "0", "4", "99999999", mContext.getResources().getString(R.string.matchFinished_txt)+"", "", "", "0", "", ""));
            }
        }
       list = filterLiveText(matchTextLiveBeanList);

        livestatus = status;

    }

    public LiveTextFragment() {
    }



    public void updateAdapter(List<MatchTextLiveBean> matchTextLiveBeanList, String status){
        if (matchTextLiveBeanList.size() > 0) {
            if ("3".equals(matchTextLiveBeanList.get(0).getCode())) {
                //999代表完场  time位置显示黄色“完场”
                matchTextLiveBeanList.add(0, new MatchTextLiveBean("", "", "0", "0", "4", "99999999", mContext.getResources().getString(R.string.matchFinished_txt)+"", "", "", "0", "", ""));
            }
        }
        list = filterLiveText(matchTextLiveBeanList);
        livestatus = status;
        timeAdp.notifyDataSetChanged();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.live_text, container, false);
        moreView = inflater.inflate(R.layout.load, null);
        mContext = MyApp.getContext();
        initView();
        initData();
        return mView;
    }

    private void initView() {
        timeListView = (ListView) mView.findViewById(R.id.timelistview);
        count = list.size();

        mRefreshLayout = ((FootballMatchDetailActivity) getActivity()).mRefreshLayout;
        //解决与下拉刷新的冲突
        timeListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        //只有文字直播的listview滑到顶部才可以下拉刷新
                        if (timeListView.getFirstVisiblePosition() != 0) {
                            mRefreshLayout.setEnabled(false);
                        }

                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });
    }

    private void initData() {
        timeAdp = new LiveTextAdapter(getActivity(), list);
        if ("-1".equals(livestatus)) {
            timeListView.addFooterView(moreView);
        }

        timeListView.setAdapter(timeAdp);
        timeListView.setOnScrollListener(this);

    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastItem = firstVisibleItem + visibleItemCount - 1;  //减1是因为上面加了个addFooterView
    }
    private  boolean isSucessedLoad=true;

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
                    isSucessedLoad=false;
                    loadMoreData();  //加载更多数据，这里可以使用异步加载
                    break;
                case SUCCESS:
                    timeAdp.notifyDataSetChanged();
                    moreView.setVisibility(View.GONE);
                    break;
                case NODATA:
                    Toast.makeText(getActivity(), mContext.getResources().getString(R.string.no_data_txt), Toast.LENGTH_SHORT).show();
                    timeListView.removeFooterView(moreView); //移除底部视图
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
        if(getActivity()==null){
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
                    preLiveTextList = jsonObject.getLive();// 获取当前页的下一页数据

                    if (preLiveTextList.size() > 0) {
                        list.addAll(filterLiveText(preLiveTextList));
                        count = list.size();
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

    public LiveTextAdapter getTimeAdapter() {

      /*  if (list.size() > 20) {
            moreView.setVisibility(View.VISIBLE);
        } else {
            moreView.setVisibility(View.GONE);
        }*/

        return timeAdp;
    }

    private  List<MatchTextLiveBean> filterLiveText(List<MatchTextLiveBean> list) {

        //0显示  1 不显示

        Iterator<MatchTextLiveBean> iterator = list.iterator();
        while (iterator.hasNext()) {
            MatchTextLiveBean matchTextLiveBean = iterator.next();
            if ("1".equals(matchTextLiveBean.getShowId())) {
                iterator.remove();
            }
        }

        return list;
    }
}
