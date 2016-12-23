package com.hhly.mlottery.adapter.chartBallAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.ChartballActivity;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewAdapter;
import com.hhly.mlottery.adapter.core.BaseRecyclerViewHolder;
import com.hhly.mlottery.bean.chart.ChartReceive;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.view.CircleImageView;

import java.util.List;
import java.util.Map;

/**
 * desc:聊球记录adapter
 * Created by 107_tangrr on 2016/12/7 0007.
 */

public class ChartBallAdapter extends BaseRecyclerViewAdapter {
    Context mContext;
    List<ChartReceive.DataBean.ChatHistoryBean> mData;
    private PopupWindow mPopupWindow;
    public static AdapterListener mAdapterListener;

    public ChartBallAdapter(Context context, List<ChartReceive.DataBean.ChatHistoryBean> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder = null;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (mData != null && mData.get(0) != null) {
            mData.get(0).setShowTime(true);// 默认第一条数据显示日期
        }
        switch (getItemViewType(position)) {
            case 0:
                ViewHolderMsg viewHolderMsg = (ViewHolderMsg) holder;
                viewHolderMsg.tv_name.setText(mData.get(position).getFromUser().getUserNick());
                try {
                    Glide.with(mContext).load(mData.get(position).getFromUser().getUserLogo()).placeholder(mContext.getResources().getDrawable(R.mipmap.center_head)).into(viewHolderMsg.bighead_view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final View v = viewHolderMsg.tv_name;
                viewHolderMsg.bighead_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopup(v, position);
                    }
                });
                if (mData.get(position).isShowTime()) {
                    viewHolderMsg.ll_time_content.setVisibility(View.VISIBLE);
                    viewHolderMsg.tv_time.setText(mData.get(position).getTime());
                } else {
                    viewHolderMsg.ll_time_content.setVisibility(View.GONE);
                }
                boolean isRn = false;
                int resource = -1;
                for (Map.Entry<String, Integer> entry : AppConstants.localMap.entrySet()) {
                    if (mData.get(position).getMessage() != null) {
                        if (mData.get(position).getMessage().equals(entry.getKey())) {
                            isRn = true;
                            resource = entry.getValue();
                        }
                    }
                }
                if (!isRn) {
                    if (mData.get(position).getMsgCode() == 2) {
                        viewHolderMsg.receive_text.setText(Html.fromHtml("<font color='#0090ff'>@" + mData.get(position).getToUser().getUserNick() + ":</font>" + mData.get(position).getMessage()));
                    } else {
                        viewHolderMsg.receive_text.setText(mData.get(position).getMessage());
                    }
                    viewHolderMsg.receive_image.setVisibility(View.GONE);
                    viewHolderMsg.receive_text.setVisibility(View.VISIBLE);
                } else {
                    if (resource != -1) {
                        viewHolderMsg.receive_image.setBackgroundResource(resource);
                    }
                    viewHolderMsg.receive_image.setVisibility(View.VISIBLE);
                    viewHolderMsg.receive_text.setVisibility(View.GONE);
                }
                break;
            case 1:// 自己
                ViewHolderMe viewHolderMe = (ViewHolderMe) holder;
                try {
                    Glide.with(mContext).load(mData.get(position).getFromUser().getUserLogo()).placeholder(mContext.getResources().getDrawable(R.mipmap.center_head)).into(viewHolderMe.my_bighead_view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                viewHolderMe.tv_nickname_me.setText(mData.get(position).getFromUser().getUserNick());
                if (mData.get(position).isShowTime()) {
                    viewHolderMe.ll_time_content_me.setVisibility(View.VISIBLE);
                    viewHolderMe.tv_time_me.setText(mData.get(position).getTime());
                } else {
                    viewHolderMe.ll_time_content_me.setVisibility(View.GONE);
                }
                boolean isRn_me = false;
                int resource_me = -1;
                for (Map.Entry<String, Integer> entry : AppConstants.localMap.entrySet()) {
                    if (mData.get(position).getMessage() != null) {
                        if (mData.get(position).getMessage().equals(entry.getKey())) {
                            isRn_me = true;
                            resource_me = entry.getValue();
                        }
                    }
                }
                if (!isRn_me) {
                    if (mData.get(position).getMsgCode() == 2) {
                        viewHolderMe.my_text.setText(Html.fromHtml("<font color='#0090ff'>@" + mData.get(position).getToUser().getUserNick() + ":</font>" + mData.get(position).getMessage()));
                    } else {
                        viewHolderMe.my_text.setText(mData.get(position).getMessage());
                    }
                    viewHolderMe.my_image.setVisibility(View.GONE);
                    viewHolderMe.my_text.setVisibility(View.VISIBLE);
                } else {
                    if (resource_me != -1) {
                        viewHolderMe.my_image.setBackgroundResource(resource_me);
                    }
                    viewHolderMe.my_image.setVisibility(View.VISIBLE);
                    viewHolderMe.my_text.setVisibility(View.GONE);
                }
                if (!mData.get(position).isSendSuccess()) {
                    viewHolderMe.iv_send_error.setVisibility(View.GONE);
                } else {
                    viewHolderMe.iv_send_error.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
        if(CommonUtils.isLogin()){
            if (mData.get(position).getFromUser().getUserId().equals(AppConstants.register.getData().getUser().getUserId())) {
                return 1;
            } else {
                return 0;
            }
        }else{
            return 0;
        }
    }

    // 其它消息ViewHolder
    class ViewHolderMsg extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView receive_image;
        CircleImageView bighead_view;
        TextView receive_text;
        TextView tv_time;
        LinearLayout ll_time_content;

        public ViewHolderMsg(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_nickname);
            bighead_view = (CircleImageView) view.findViewById(R.id.bighead_view);
            receive_text = (TextView) view.findViewById(R.id.receive_text);
            receive_image = (TextView) view.findViewById(R.id.receive_image);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            ll_time_content = (LinearLayout) view.findViewById(R.id.ll_time_content);
        }
    }

    // 我的消息ViewHolder
    class ViewHolderMe extends RecyclerView.ViewHolder {
        TextView tv_nickname_me;
        TextView my_text;
        TextView my_image;
        TextView tv_time_me;
        CircleImageView my_bighead_view;
        LinearLayout ll_time_content_me;
        ImageView iv_send_error;

        public ViewHolderMe(View itemView) {
            super(itemView);
            tv_nickname_me = (TextView) itemView.findViewById(R.id.tv_nickname_me);
            my_text = (TextView) itemView.findViewById(R.id.my_text);
            my_bighead_view = (CircleImageView) itemView.findViewById(R.id.my_bighead_view);
            my_image = (TextView) itemView.findViewById(R.id.my_image);
            tv_time_me = (TextView) itemView.findViewById(R.id.tv_time_me);
            ll_time_content_me = (LinearLayout) itemView.findViewById(R.id.ll_time_content_me);
            iv_send_error = (ImageView) itemView.findViewById(R.id.iv_send_error);
        }
    }

    private void showPopup(View v, final int index) {
        View popupView = View.inflate(mContext, R.layout.item_chart_ball_fragment_popup, null);
        mPopupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.mipmap.chart_ball_popup_report_bg));
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
                mPopupWindow.dismiss();
                Intent intent = new Intent(mContext, ChartballActivity.class);
                intent.putExtra("CALL_NAME", mData.get(index).getFromUser().getUserNick());
                intent.putExtra("CALL_USER_ID", mData.get(index).getFromUser().getUserId());
                mContext.startActivity(intent);
            }
        });

        // 举报
        popupView.findViewById(R.id.tv_popup_jubao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApp.getContext().sendBroadcast(new Intent("CLOSE_INPUT_ACTIVITY"));
                if (!CommonUtils.isLogin()) {
                    // 未登录
                    userLoginBack();
                } else {
                    mPopupWindow.dismiss();
                    showDialog(mData.get(index).getMsgId(), mData.get(index).getFromUser().getUserId(), mData.get(index).getFromUser().getUserNick());
                }
            }
        });
    }

    public interface AdapterListener {
        void shwoDialog(String msgId, String toUserId, String toUserNick);
        void userLoginBack();
    }

    public static void showDialog(String msgId, String toUserId, String toUserNick) {
        mAdapterListener.shwoDialog(msgId, toUserId, toUserNick);
    }

    public static void userLoginBack(){
        mAdapterListener.userLoginBack();
    }

    public void setShowDialogOnClickListener(AdapterListener listener) {
        mAdapterListener = listener;
    }
}
