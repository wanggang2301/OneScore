package com.hhly.mlottery.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.InfoCenterActivity;
import com.hhly.mlottery.adapter.InfoCenterSelectAdapter;
import com.hhly.mlottery.bean.infoCenterBean.IntelligencesEntity;
import com.hhly.mlottery.util.L;

import java.util.List;

/**
 * 描  述：情报中心日期选择
 * 作  者：tangrr@13322.com
 * 时  间：2016/9/7
 */

public class InfoCenterPW extends PopupWindow {
    private RecyclerView mRecyclerView;
    private List<IntelligencesEntity> datas;
    private Activity mContext;
    private InfoCenterSelectAdapter infoCenterSelectAdapter;
    private int currentIndex;
    private final int width;

    public InfoCenterPW(final Activity context, List<IntelligencesEntity> list, int index) {
        this.mContext = context;
        this.datas = list;
        this.currentIndex = index;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.activity_info_data_select, null);
        this.setContentView(mRecyclerView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(mContext, 1f);
            }
        });

        initRecyclerView();

        width = mContext.getWindowManager().getDefaultDisplay().getWidth();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        infoCenterSelectAdapter = new InfoCenterSelectAdapter(R.layout.activity_info_data_item, datas);
        mRecyclerView.setAdapter(infoCenterSelectAdapter);
        mRecyclerView.scrollToPosition(currentIndex);

        infoCenterSelectAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                InfoCenterPW.this.dismiss();
                ((InfoCenterActivity) mContext).showSelectInfo(i);
                infoCenterSelectAdapter.notifyDataSetChanged();
//                mRecyclerView.scrollToPosition(i);

//                view.measure(0,0);
//                float measuredWidth = view.getX();
//
//                L.d("xxx","measuredWidth: " + measuredWidth);
//
//
//                Toast.makeText(mContext, "xxx:" + (width/2-measuredWidth), Toast.LENGTH_SHORT).show();
//                mRecyclerView.scrollTo(500,0);
//                mRecyclerView.scrollBy(500,0);

            }
        });
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 0);// 以下拉方式显示popupwindow
            backgroundAlpha(mContext, 0.5f);
        } else {
            this.dismiss();
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    private void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    public void notifyChanged(int indexPosition) {
        infoCenterSelectAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(indexPosition);
    }
}
