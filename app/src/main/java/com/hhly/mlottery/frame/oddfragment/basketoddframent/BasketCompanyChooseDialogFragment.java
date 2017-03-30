package com.hhly.mlottery.frame.oddfragment.basketoddframent;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.cpiadapter.basket.BasketCpiCompanyAdapter;
import com.hhly.mlottery.bean.basket.index.BasketIndexBean;
import com.hhly.mlottery.util.ToastTools;

import java.util.ArrayList;

/**
 * @author: Wangg
 * @Name：BasketCompanyChooseDialogFragment
 * @Description:
 * @Created on:2017/3/21  11:49.
 */

public class BasketCompanyChooseDialogFragment extends DialogFragment {

    private static final String KEY_COMPANY_LIST = "company_list";

    TextView mTitleTextView;
    ListView mListView;
    Button mOkButton;

    private ArrayList<BasketIndexBean.DataBean.CompanyBean> companyList;

    private OnFinishSelectionListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            companyList = args.getParcelableArrayList(KEY_COMPANY_LIST);
        }
    }

    public BasketCompanyChooseDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.alert_dialog);

        mTitleTextView = (TextView) dialog.findViewById(R.id.titleView);
        mTitleTextView.setText(R.string.odd_company_txt);

        // 确认按钮
        mOkButton = (Button) dialog.findViewById(R.id.cpi_btn_ok);
        mOkButton.setVisibility(View.VISIBLE);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onFinishSelection();
                }
                dismiss();
            }
        });

        dialog.findViewById(R.id.cpi_view_id).setVisibility(View.VISIBLE);

        mListView = (ListView) dialog.findViewById(R.id.listdate);
        initListView();

        return dialog;
    }

    /**
     * 初始化 ListView
     */
    private void initListView() {
        BasketCpiCompanyAdapter adapter = new BasketCpiCompanyAdapter(companyList);
        mListView.setAdapter(adapter);
        // 设置多选模式
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BasketIndexBean.DataBean.CompanyBean company = companyList.get(position);
                if (getCheckedNum() == 1 && company.isChecked()) {
                    ToastTools.showQuick(MyApp.getContext(), getString(R.string.at_least_one_company));
                    return;
                }
                company.setChecked(!company.isChecked());
                View selectedView = view.findViewById(R.id.item_img_checked);
                selectedView.setSelected(!selectedView.isSelected());
            }
        });
    }


    public int getCheckedNum() {
        int checkedNum = 0;
        for (BasketIndexBean.DataBean.CompanyBean company : companyList) {
            if (company.isChecked()) checkedNum += 1;
        }
        return checkedNum;
    }


    public static BasketCompanyChooseDialogFragment newInstance(ArrayList<BasketIndexBean.DataBean.CompanyBean> companyList,
                                                                OnFinishSelectionListener listener) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_COMPANY_LIST, companyList);
        BasketCompanyChooseDialogFragment fragment = new BasketCompanyChooseDialogFragment();
        fragment.listener = listener;
        fragment.setArguments(args);
        return fragment;
    }


    public interface OnFinishSelectionListener {
        void onFinishSelection();
    }
}
