package com.hhly.mlottery.frame.tennisfrag;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.tennisball.TennisDateAdapter;

import java.util.ArrayList;

/**
 * 网球日期选择DialogFarg
 */
public class TennisDateChooseDialogFragment extends DialogFragment {

    ListView mListView;
    private String currentDate;
    private ArrayList<String> dateList;

    private OnDateChooseListener mOnDateChooseListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            currentDate = args.getString("currentDate");
            dateList = args.getStringArrayList("dataList");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.alert_dialog_tennis_data);

        mListView = (ListView) dialog.findViewById(R.id.list_view);

        TennisDateAdapter tennisDataAdapter = new TennisDateAdapter(getContext(), dateList, currentDate);

        mListView.setAdapter(tennisDataAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnDateChooseListener != null) {
                    mOnDateChooseListener.onDateChoose(dateList.get(position));
                    dismiss();
                }
            }
        });

        return dialog;
    }


    public static TennisDateChooseDialogFragment newInstance(ArrayList<String> data, String currentDate, OnDateChooseListener listener) {
        Bundle args = new Bundle();
        args.putString("currentDate", currentDate);
        args.putStringArrayList("dataList", data);
        TennisDateChooseDialogFragment fragment = new TennisDateChooseDialogFragment();
        fragment.mOnDateChooseListener = listener;
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnDateChooseListener {
        void onDateChoose(String date);
    }
}
