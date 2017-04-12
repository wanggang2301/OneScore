package com.hhly.mlottery.frame.basketballframe;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.basketdatabase.MatchStage;
import com.hhly.mlottery.bean.basket.basketdatabase.ScheduleResult;
import com.hhly.mlottery.util.CollectionUtils;
import com.hhly.mlottery.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 描    述：联赛赛程阶段选择dialog
 * 作    者：longs@13322.com
 * 时    间：2016/8/10
 */
public class LeagueMatchStageChooseDialogFragment extends DialogFragment {

    private static final String KEY_RESULT = "scheduled_result";

    RecyclerView mFirstRecyclerView;
    RecyclerView mSecondRecyclerView;

    private ScheduleResult mResult;
    private List<MatchStage> mSecondStageList;

    private OnChooseOkListener mOnChooseOkListener;

    public void setOnChooseOkListener(OnChooseOkListener onChooseOkListener) {
        mOnChooseOkListener = onChooseOkListener;
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
        View view = View.inflate(getContext(), R.layout.basket_database_league_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        final List<MatchStage> firstStages = mResult.getSearchCondition();
        mSecondStageList = new ArrayList<>();
        List<MatchStage> secondStages = firstStages.get(mResult.getFirstStageIndex()).getStages();
        if (CollectionUtils.notEmpty(secondStages)) {
            mSecondStageList.addAll(secondStages);
        }
        clearFirstSelected();
        clearSecondSelected();
        // 将选中的设为 selected
        firstStages.get(mResult.getFirstStageIndex()).setSelected(true);
        if (CollectionUtils.notEmpty(mSecondStageList))
            mSecondStageList.get(mResult.getSecondStageIndex()).setSelected(true);

        mFirstRecyclerView = (RecyclerView) view.findViewById(R.id.first_recycler_view);
        GridLayoutManager firstGridLayoutManager =
                new GridLayoutManager(getContext(), firstStages.size());
        mFirstRecyclerView.setLayoutManager(firstGridLayoutManager);
        final FirstAdapter firstAdapter = new FirstAdapter(firstStages);
        mFirstRecyclerView.setAdapter(firstAdapter);

        mSecondRecyclerView = (RecyclerView) view.findViewById(R.id.second_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        mSecondRecyclerView.setLayoutManager(gridLayoutManager);
        final SecondAdapter secondAdapter = new SecondAdapter(mSecondStageList);
        mSecondRecyclerView.setAdapter(secondAdapter);

        firstAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(MatchStage stage) {
                // 清除 selected 标记
                clearFirstSelected();
                // 点击的设为 selected
                stage.setSelected(true);
                // 通知刷新
                firstAdapter.notifyDataSetChanged();
                // 更换 secondRecyclerView 的数据源
                mSecondStageList.clear();
                if (CollectionUtils.notEmpty(stage.getStages())) {
                    mSecondStageList.addAll(stage.getStages());
                }
                secondAdapter.notifyDataSetChanged();
            }
        });

        secondAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(MatchStage stage) {
                clearSecondSelected();
                stage.setSelected(true);
                secondAdapter.notifyDataSetChanged();
            }
        });

        view.findViewById(R.id.text_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });

        view.findViewById(R.id.text_confirm)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnChooseOkListener != null) {
                            List<MatchStage> searchCondition = mResult.getSearchCondition();
                            String firstStageId = null;
                            String secondStageId = null;
                            for (MatchStage stage : searchCondition) {
                                if (stage.isSelected()) {
                                    firstStageId = stage.getStageId();
                                    if (stage.isHasSecondStage()) {
                                        for (MatchStage matchStage : stage.getStages()) {
                                            if (matchStage.isSelected())
                                                secondStageId = matchStage.getStageId();
                                        }
                                    }
                                }
                            }
                            mOnChooseOkListener.onChooseOk(firstStageId, secondStageId);
                        }
                        dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    private void clearFirstSelected() {
        for (MatchStage matchStage : mResult.getSearchCondition()) {
            matchStage.setSelected(false);
        }
    }

    private void clearSecondSelected() {
        for (MatchStage matchStage : mSecondStageList) {
            matchStage.setSelected(false);
        }
    }

    public static LeagueMatchStageChooseDialogFragment newInstance(ScheduleResult result) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_RESULT, result);
        LeagueMatchStageChooseDialogFragment fragment = new LeagueMatchStageChooseDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    class FirstAdapter extends BaseQuickAdapter<MatchStage> {

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        public FirstAdapter(List<MatchStage> data) {
            super(R.layout.item_match_stage_first, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, final MatchStage matchStage) {
            TextView textView = holder.getView(R.id.first_text);

            int position = holder.getAdapterPosition();
            textView.setSelected(matchStage.isSelected());
            if (position == 0) {
                textView.setBackgroundResource(R.drawable.bg_item_match_stage_first_left);
            } else if (position == mData.size() - 1) {
                textView.setBackgroundResource(R.drawable.bg_item_match_stage_first_right);
            } else {
                textView.setBackgroundResource(R.drawable.bg_item_match_stage_first_normal);
            }
            if(DateUtil.isValidDateYMD(matchStage.getStageName()) || DateUtil.isValidDateYM(matchStage.getStageName())){
                textView.setText(DateUtil.convertDateToNationYD(matchStage.getStageName()));
            }else{
                textView.setText(matchStage.getStageName());
            }
            if (mOnItemClickListener != null) {
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(matchStage);
                    }
                });
            }
        }
    }

    class SecondAdapter extends BaseQuickAdapter<MatchStage> {

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        public SecondAdapter(List<MatchStage> data) {
            super(R.layout.item_match_stage_second, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, final MatchStage matchStage) {
            TextView textView = holder.getView(R.id.second_text);

            textView.setSelected(matchStage.isSelected());
            if(DateUtil.isValidDateYMD(matchStage.getStageName()) || DateUtil.isValidDateYM(matchStage.getStageName())){
                textView.setText(DateUtil.convertDateToNationYD(matchStage.getStageName()));
            }else{
                textView.setText(matchStage.getStageName());
            }
            if (mOnItemClickListener != null) {
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(matchStage);
                    }
                });
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(MatchStage stage);
    }

    public interface OnChooseOkListener {
        void onChooseOk(String firstStageId, String secondStageId);
    }
}
