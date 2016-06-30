package com.hhly.mlottery.frame.oddfragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.cpiadapter.CpiCompanyAdapter;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.util.DisplayUtil;

import java.util.ArrayList;

/**
 * 公司选择
 * <p/>
 * Created by loshine on 2016/6/23.
 */
public class CompanyChooseDialogFragment extends DialogFragment {

    private static final String KEY_COMPANY_LIST = "company_list";

    TextView mTitleTextView;
    ListView mListView;
    Button mOkButton;

    private ArrayList<NewOddsInfo.CompanyBean> companyList;

    private OnFinishSelectionListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            companyList = args.getParcelableArrayList(KEY_COMPANY_LIST);
        }
    }

    public CompanyChooseDialogFragment() {
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
        CpiCompanyAdapter adapter = new CpiCompanyAdapter(companyList);
        mListView.setAdapter(adapter);
        // 设置多选模式
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewOddsInfo.CompanyBean company = companyList.get(position);
                company.setIsChecked(!company.isChecked());
                View selectedView = view.findViewById(R.id.item_img_checked);
                selectedView.setSelected(!selectedView.isSelected());
            }
        });
    }

    public static CompanyChooseDialogFragment newInstance(ArrayList<NewOddsInfo.CompanyBean> companyList,
                                                          OnFinishSelectionListener listener) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_COMPANY_LIST, companyList);
        CompanyChooseDialogFragment fragment = new CompanyChooseDialogFragment();
        fragment.listener = listener;
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnFinishSelectionListener {
        void onFinishSelection();
    }
}
