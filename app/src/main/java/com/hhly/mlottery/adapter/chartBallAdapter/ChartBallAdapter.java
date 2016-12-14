package com.hhly.mlottery.adapter.chartBallAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewAdapter;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewHolder;
import com.hhly.mlottery.util.ToastTools;

import java.util.List;

/**
 * desc:聊球记录adapter
 * Created by 107_tangrr on 2016/12/7 0007.
 */

public class ChartBallAdapter extends BaseRecyclerViewAdapter {
    Context mContext;
    List<String> mData;
    private PopupWindow mPopupWindow;

    public ChartBallAdapter(Context context, List<String> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        RecyclerView.ViewHolder holder = null;
        System.out.println("xxxxx viewType: " + viewType);
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_char_ball_content, parent, false);
                holder = new ViewHolderMsg(view);
                break;
            case 1:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_char_ball_content_me, parent, false);
                holder = new ViewHolderMe(view);
                break;
        }
        return holder;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int[] getItemLayouts() {
        return new int[0];
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                ViewHolderMsg viewHolderMsg = (ViewHolderMsg) holder;
                viewHolderMsg.tv_name.setText(mData.get(position));
                final View v = viewHolderMsg.tv_name;
                viewHolderMsg.iv_user_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopup(v);
                    }
                });
                break;
            case 1:
                ViewHolderMe viewHolderMe = (ViewHolderMe) holder;
                viewHolderMe.tv_nickname_me.setText(mData.get(position));
                break;
        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
        // TODO 显示返回item类型  如果为自己的id，则返回1
        if (position == 0 || position == 5) {
            return 1;
        } else {
            return 0;
        }
    }


    // 其它消息ViewHolder
    class ViewHolderMsg extends RecyclerView.ViewHolder {
        TextView tv_name;
        ImageView iv_user_icon;

        public ViewHolderMsg(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_nickname);
            iv_user_icon = (ImageView) view.findViewById(R.id.iv_user_icon);
        }
    }

    // 我的消息ViewHolder
    class ViewHolderMe extends RecyclerView.ViewHolder {
        TextView tv_nickname_me;
        ImageView iv_me_icon;

        public ViewHolderMe(View itemView) {
            super(itemView);
            tv_nickname_me = (TextView) itemView.findViewById(R.id.tv_nickname_me);
            iv_me_icon = (ImageView) itemView.findViewById(R.id.iv_me_icon);
        }
    }

    private void showPopup(View v) {
        View popupView = View.inflate(mContext, R.layout.item_chart_ball_fragment_popup, null);
        mPopupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.mipmap.chart_ball_report_bg));
        v.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = v.getMeasuredWidth();
        int popupHeight = v.getMeasuredHeight();
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        // 显示x,y轴的位置
        mPopupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight - (popupHeight / 2));

        // 艾特
        popupView.findViewById(R.id.tv_popup_aite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastTools.showQuick(mContext, "艾特");
                mPopupWindow.dismiss();
                // TODO  获取用户昵称 用蓝色字体颜色显示在输入框中

            }
        });

        // 举报
        popupView.findViewById(R.id.tv_popup_jubao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastTools.showQuick(mContext, "举报");
                mPopupWindow.dismiss();
                // TODO 弹出举报框 获取被举报昵称 和 自己的昵称显示在弹框中，并将举报内容提交到服务器
            }
        });
    }


}
