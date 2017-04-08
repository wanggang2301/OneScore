package com.hhly.mlottery.frame.cpifrag.SnookerIndex.SnookerChildFragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.cpiadapter.CpiCompanyAdapter;
import com.hhly.mlottery.adapter.cpiadapter.SnookerIndexCompanyAdapter;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.bean.snookerbean.snookerIndexBean.SnookerIndexBean;
import com.hhly.mlottery.util.ToastTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 斯诺克指数列表公司筛选
 */
public class SnookerCompanyChooseDialogFragment extends DialogFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private static final String KEY_COMPANY_LIST = "company_list";

    TextView mTitleTextView;
    ListView mListView;
    Button mOkButton;
    private ArrayList<SnookerIndexBean.CompanyEntity> companyList;

    private OnFinishSelectionListener listener;


    public SnookerCompanyChooseDialogFragment() {
        // Required empty public constructor
    }

    public static SnookerCompanyChooseDialogFragment newInstance(ArrayList<SnookerIndexBean.CompanyEntity> companyList,
                                                                 OnFinishSelectionListener listener) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_COMPANY_LIST,  companyList);
        SnookerCompanyChooseDialogFragment fragment = new SnookerCompanyChooseDialogFragment();
        fragment.listener = listener;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            companyList = args.getParcelableArrayList(KEY_COMPANY_LIST);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.alert_dialog);

//        WindowManager.LayoutParams layoutParams = window.getAttributes();
//
//        layoutParams.height = (int) (DisplayUtil.getScreenHeight(getContext()) * 0.42);
//        layoutParams.width = (int) (DisplayUtil.getScreenWidth(getContext()) * 0.54);
//        layoutParams.gravity = Gravity.CENTER;
//
//        window.setAttributes(layoutParams);

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
        SnookerIndexCompanyAdapter adapter = new SnookerIndexCompanyAdapter(companyList);
        mListView.setAdapter(adapter);
        // 设置多选模式
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SnookerIndexBean.CompanyEntity company = companyList.get(position);
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
        for (SnookerIndexBean.CompanyEntity company : companyList) {
            if (company.isChecked()) checkedNum += 1;
        }
        return checkedNum;
    }

    public interface OnFinishSelectionListener {
        void onFinishSelection();
    }
}
