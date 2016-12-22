package com.hhly.mlottery.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hhly.mlottery.view.LoadMoreRecyclerView;

/**
 * Created by yixq on 2016/11/24.
 * mail：yixq@13322.com
 * describe:
 */

public abstract  class LoadMoreRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    private int previousTotal = 0;
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int currentPage = 1;

    private LinearLayoutManager mLinearLayoutManager;

//    public LoadMoreRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
//        this.mLinearLayoutManager = linearLayoutManager;
//    }

    public LoadMoreRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager , boolean move ,int index , LoadMoreRecyclerView recyclerView) {
        this.mLinearLayoutManager = linearLayoutManager;
        this.isMove = move;
        this.mIndex = index;
        this.mRecyclerView = recyclerView;
    }

    private boolean isMove;
    private int mIndex;
    private LoadMoreRecyclerView mRecyclerView;
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= firstVisibleItem) {
            currentPage++;
            onLoadMore(currentPage);
            loading = true;
        }

        if (isMove){
            isMove = false;
            int n = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
            if ( 0 <= n && n < mRecyclerView.getChildCount()){
                int top = mRecyclerView.getChildAt(n).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        //在这里进行第二次滚动（最后的100米！）
        if (isMove){
            isMove = false;
            //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
            int n = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
            if ( 0 <= n && n < mRecyclerView.getChildCount()){
                //获取要置顶的项顶部离RecyclerView顶部的距离
                int top = mRecyclerView.getChildAt(n).getTop();
                //最后的移动
                mRecyclerView.scrollBy(0, top);
            }

        }
    }

    public abstract void onLoadMore(int currentPage);
}
