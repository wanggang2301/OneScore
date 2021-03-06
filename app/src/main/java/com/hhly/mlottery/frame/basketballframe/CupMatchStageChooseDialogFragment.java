package com.hhly.mlottery.frame.basketballframe;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.basketdatabase.MatchStage;
import com.hhly.mlottery.bean.basket.basketdatabase.StageResult;

import java.util.List;

/**
 * 描    述：杯赛阶段选择Dialog
 * 作    者：longs@13322.com
 * 时    间：2016/8/9
 */
public class CupMatchStageChooseDialogFragment extends DialogFragment {

    private static final String KEY_RESULT = "scheduled_result";

    TextView mTextView;
    RecyclerView mRecyclerView;

    private StageResult mResult;
    private DialogAdapter mAdapter;
    private BaseQuickAdapter.OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public void setOnRecyclerViewItemClickListener(BaseQuickAdapter.OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mResult = args.getParcelable(KEY_RESULT);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.recycler_dialog);

        mTextView = (TextView) dialog.findViewById(R.id.title);
        mTextView.setText(R.string.basket_database_details_stage);
        mRecyclerView = (RecyclerView) dialog.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new DialogAdapter(mResult.getSearchCondition());
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                if (mOnRecyclerViewItemClickListener != null) {
                    mOnRecyclerViewItemClickListener.onItemClick(view, i);
                }
                dialog.dismiss();
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        return dialog;
    }

    public static CupMatchStageChooseDialogFragment newInstance(StageResult result) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_RESULT, result);
        CupMatchStageChooseDialogFragment fragment = new CupMatchStageChooseDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    class DialogAdapter extends BaseQuickAdapter<MatchStage> {

        public DialogAdapter(List<MatchStage> data) {
            super(R.layout.item_dialog_layout, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, MatchStage matchStage) {
            holder.setText(R.id.dialog_txt_cancel_list, matchStage.getStageName())
                    .setVisible(R.id.line, true);
            if (holder.getAdapterPosition() == mResult.getFirstStageId()) {
                holder.setTextColor(R.id.dialog_txt_cancel_list,
                        ContextCompat.getColor(mContext, R.color.colorPrimary));
            } else {
                holder.setTextColor(R.id.dialog_txt_cancel_list,
                        ContextCompat.getColor(mContext, R.color.msg));
            }
        }
    }
}