package com.hhly.mlottery.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class PullUpRefreshListView extends ListView implements AbsListView.OnScrollListener {
    private int listViewTouchAction;
    private static final int MAXIMUM_LIST_ITEMS_VIEWABLE = 99;
    /** 底部显示正在加载的页面 */
    private View footerView = null;
    /** 存储上下文 */
    private Context context;
    /** 上拉刷新的ListView的回调监听 */
    private MyPullUpListViewCallBack myPullUpListViewCallBack;
    /** 记录第一行Item的数值 */
//    private int firstVisibleItem;
    int downY;
    int upY;
    private boolean isBottom=false;
    public PullUpRefreshListView(Context context) {
        super(context);
        this.context = context;
        initListView();
    }

    public PullUpRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initListView();
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        listViewTouchAction = -1;
        // 为ListView设置滑动监听
        setOnScrollListener(this);
        // 去掉底部分割线
//        setFooterDividersEnabled(true);
    }

    /**
     * 初始化话底部页面
     */
    public void initBottomView(View mfooterView) {
        footerView=mfooterView;
        if (footerView != null) {
            addFooterView(footerView);
        }

    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {


    }

    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        if (footerView != null) {
            //判断可视Item是否能在当前页面完全显示
            if (visibleItemCount == totalItemCount) {
                // removeFooterView(footerView);
                footerView.setVisibility(View.GONE);//隐藏底部布局
            } else {
                // addFooterView(footerView);
                footerView.setVisibility(View.VISIBLE);//显示底部布局
            }
            if (firstVisibleItem+visibleItemCount==totalItemCount){
                isBottom=true;
            }else {
                isBottom=false;
            }
        }

    }

    public void setMyPullUpListViewCallBack(
            MyPullUpListViewCallBack myPullUpListViewCallBack) {
        this.myPullUpListViewCallBack = myPullUpListViewCallBack;
    }

    /**
     * 上拉刷新的ListView的回调监听
     *
     */
    public interface MyPullUpListViewCallBack {

        void scrollBottomState();
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN :
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_UP :
                upY= (int) ev.getY();
                if ((upY-downY)<0 && upY!=0){//向上滑动而且松手
                    if (isBottom){
                        myPullUpListViewCallBack.scrollBottomState();
                    }

                }
                break;

        }
        if (getAdapter() != null
                && getAdapter().getCount() > MAXIMUM_LIST_ITEMS_VIEWABLE) {
            if (listViewTouchAction == MotionEvent.ACTION_MOVE) {
                scrollBy(0, 1);
            }
            return  false;
        }
        return super.onTouchEvent(ev);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int newHeight = 0;
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            ListAdapter listAdapter = getAdapter();
            if (listAdapter != null && !listAdapter.isEmpty()) {
                int listPosition = 0;
                for (listPosition = 0; listPosition < listAdapter.getCount()
                        && listPosition < MAXIMUM_LIST_ITEMS_VIEWABLE; listPosition++) {
                    View listItem = listAdapter.getView(listPosition, null,
                            this);
                    // now it will not throw a NPE if listItem is a ViewGroup
                    // instance
                    if (listItem instanceof ViewGroup) {
                        listItem.setLayoutParams(new LayoutParams(
                                LayoutParams.WRAP_CONTENT,
                                LayoutParams.WRAP_CONTENT));
                    }
                    listItem.measure(widthMeasureSpec, heightMeasureSpec);
                    newHeight += listItem.getMeasuredHeight();
                }
                newHeight += getDividerHeight() * listPosition;
            }
            if ((heightMode == MeasureSpec.AT_MOST) && (newHeight > heightSize)) {
                if (newHeight > heightSize) {
                    newHeight = heightSize;
                }
            }
        } else {
            newHeight = getMeasuredHeight();
        }
        setMeasuredDimension(getMeasuredWidth(), newHeight);
    }
}