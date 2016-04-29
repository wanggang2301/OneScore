package com.hhly.mlottery.adapter;

import android.content.Context;
import android.view.View;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.resultbean.ResultMatchDto;
import com.hhly.mlottery.callback.DateOnClickListener;
import com.hhly.mlottery.callback.FocusClickListener;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.ImmediateUtils;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.ResultDateUtil;
import com.hhly.mlottery.util.adapter.MultiItemCommonAdapter;
import com.hhly.mlottery.util.adapter.MultiItemTypeSupport;
import com.hhly.mlottery.util.adapter.ViewHolder;

import java.util.List;

/**
 * Created by chenml on 2016/1/19.
 */
public class ResultMultiInternationAdapter extends MultiItemCommonAdapter<ResultMatchDto> {

    public final static int VIEW_DATE_INDEX = 0;
    public final static int VIEW_MATCH_INDEX = 1;

    private DateOnClickListener dateOnClickListener;

    public void setDateOnClickListener(DateOnClickListener dateOnClickListener) {
        this.dateOnClickListener = dateOnClickListener;
    }

    private int mItemPaddingRight;

    private int handicap = 0;

    public void setItemPaddingRight(int mItemPaddingRight) {
        this.mItemPaddingRight = mItemPaddingRight;
    }

    private FocusClickListener focusClickListener;//

    public FocusClickListener getFocusClickListener() {
        return focusClickListener;
    }

    public void setFocusClickListener(FocusClickListener focusClickListener) {
        this.focusClickListener = focusClickListener;
    }

    public ResultMultiInternationAdapter(Context context, List<ResultMatchDto> datas, MultiItemTypeSupport<ResultMatchDto> multiItemTypeSupport) {
        super(context, datas, multiItemTypeSupport);
    }


    @Override
    public void convert(ViewHolder holder, ResultMatchDto resultMatchDto) {
        if (resultMatchDto.getType() == ResultMultiAdapter.VIEW_DATE_INDEX) {
            holder.setOnClickListener(R.id.item_data_layout, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dateOnClickListener != null) {
                        dateOnClickListener.onClick(v);
                    }

                }
            });
            holder.setText(R.id.item_data, resultMatchDto.getDate());
            holder.setText(R.id.item_xingqi, ResultDateUtil.getWeekOfDate(DateUtil.parseDate(ResultDateUtil.getDate(0, resultMatchDto.getDate()))));
        } else {
//            if (handicap == 0) {
            if (PreferenceUtil.getBoolean(MyConstants.RBSECOND, true)) {
                handicap = 1;
            } else if (PreferenceUtil.getBoolean(MyConstants.rbSizeBall, false)) {
                handicap = 2;
            } else if (PreferenceUtil.getBoolean(MyConstants.RBOCOMPENSATE, false)) {
                handicap = 3;
            } else if (PreferenceUtil.getBoolean(MyConstants.RBNOTSHOW, false)) {
                handicap = 4;
            } else {
                handicap = 1;
            }
//            }
            ImmediateUtils.interConvert(holder, resultMatchDto.getMatchs(), mContext, handicap, focusClickListener, mItemPaddingRight);
        }
    }

    public static class ResultMultiItemTypeSupport implements MultiItemTypeSupport<ResultMatchDto> {

        @Override
        public int getLayoutId(int position, ResultMatchDto resultMatchDto) {
            if (resultMatchDto.getType() == ResultMultiAdapter.VIEW_DATE_INDEX) {
                return R.layout.item_football_date;
            } else {
                return R.layout.item_football_international;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int postion, ResultMatchDto resultMatchDto) {
            return resultMatchDto.getType();
        }

    }

    public void updateDatas(List<ResultMatchDto> mMatch) {
        mDatas = mMatch;
    }
}
