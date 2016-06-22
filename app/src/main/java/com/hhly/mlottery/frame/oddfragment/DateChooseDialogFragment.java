package com.hhly.mlottery.frame.oddfragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.cpiadapter.CpiDateAdapter;
import com.hhly.mlottery.util.UiUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日期选择
 * <p>
 * Created by loshine on 2016/6/22.
 */
public class DateChooseDialogFragment extends DialogFragment {

    TextView mTitleTextView;
    ListView mListView;
    Button mOkButton;

    private String currentDate;
    private int selectedPosition = 7; // 默认选中当天
    private List<Map<String, String>> dateList;

    private OnDateChooseListener mOnDateChooseListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            currentDate = args.getString("currentDate");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        return inflater.inflate(R.layout.alertdialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitleTextView = (TextView) view.findViewById(R.id.titleView);
        mListView = (ListView) view.findViewById(R.id.listdate);
        mOkButton = (Button) view.findViewById(R.id.cpi_btn_ok);

        mOkButton.setVisibility(View.GONE);

        mTitleTextView.setText(R.string.tip);

        maybeInitDateList();

        final CpiDateAdapter cpiDateAdapter = new CpiDateAdapter(getContext(), dateList);
        mListView.setAdapter(cpiDateAdapter);
        cpiDateAdapter.setDefSelect(selectedPosition);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnDateChooseListener != null) {
                    mOnDateChooseListener.onDateChoose(dateList.get(position).get("date"));
                    selectedPosition = position;
                    cpiDateAdapter.setDefSelect(position);
                    dismiss();
                }
            }
        });
    }

    /**
     * 初始化日期列表
     */
    private void maybeInitDateList() {
        if (dateList == null) {
            dateList = new ArrayList<>();
        } else {
            dateList.clear();
        }
        for (int i = -6; i < 8; i++) {
            addDate(i);
        }
    }

    /**
     * 添加日期
     *
     * @param offset offset
     */
    private void addDate(int offset) {
        Map<String, String> map = new HashMap<>();
        map.put("date", UiUtils.getDate(currentDate, offset));
        dateList.add(map);
    }

    public static DateChooseDialogFragment newInstance(String currentDate,
                                                       OnDateChooseListener listener) {

        Bundle args = new Bundle();
        args.putString("currentDate", currentDate);
        DateChooseDialogFragment fragment = new DateChooseDialogFragment();
        fragment.mOnDateChooseListener = listener;
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnDateChooseListener {
        void onDateChoose(String date);
    }
}
