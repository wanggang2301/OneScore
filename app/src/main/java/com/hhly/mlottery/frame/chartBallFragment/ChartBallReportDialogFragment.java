package com.hhly.mlottery.frame.chartBallFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.CyUtils;
import com.hhly.mlottery.util.ToastTools;

/**
 * desc:举报DialogFragment
 * Created by 107_tangrr on 2016/12/14 0014.
 */

public class ChartBallReportDialogFragment extends DialogFragment implements View.OnClickListener {
    private static final String MSG_ID = "msgId";
    private static final String NICK_NAME = "nickName";
    private static final String TO_USER_ID = "toUserId";
    private static final String TO_NICK_NAME = "toNickName";

    private Activity mContext;
    private String mMsgId;
    private String mNickName;
    private String mToUserId;
    private String mToNickName;

    private TextView tv_to_nickname;
    private TextView tv_nickname;

    public static ChartBallReportDialogFragment newInstance(String msgId, String nickName, String toUserId, String toNickName) {
        Bundle args = new Bundle();
        args.putString(MSG_ID, msgId);
        args.putString(NICK_NAME, nickName);
        args.putString(TO_USER_ID, toUserId);
        args.putString(TO_NICK_NAME, toNickName);
        ChartBallReportDialogFragment fragment = new ChartBallReportDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mMsgId = args.getString(MSG_ID);
            mNickName = args.getString(NICK_NAME);
            mToUserId = args.getString(TO_USER_ID);
            mToNickName = args.getString(TO_NICK_NAME);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext, R.layout.dialog_chart_ball_report, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = alertDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initView(view);

        return alertDialog;
    }

    private void initView(View view) {
        tv_to_nickname = (TextView) view.findViewById(R.id.tv_to_nickname);
        tv_to_nickname.setText(mToNickName);
        tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
        tv_nickname.setText(mNickName);
        view.findViewById(R.id.bt_submit).setOnClickListener(this);
        view.findViewById(R.id.bt_close).setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_submit:
                ToastTools.showQuick(mContext,"提交");
                // TODO 获取举报内容 并上传到服务器

                break;
            case R.id.bt_close:
                getDialog().dismiss();
                // TODO 关闭键盘
                break;
        }
    }
}
