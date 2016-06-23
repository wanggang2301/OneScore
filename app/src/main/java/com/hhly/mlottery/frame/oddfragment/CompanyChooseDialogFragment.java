package com.hhly.mlottery.frame.oddfragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.cpiadapter.CpiCompanyAdapter;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;

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

    public CompanyChooseDialogFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            companyList = args.getParcelableArrayList(KEY_COMPANY_LIST);
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

        // 标题
        mTitleTextView = (TextView) view.findViewById(R.id.titleView);
        mTitleTextView.setText(R.string.odd_company_txt);

        // 确认按钮
        mOkButton = (Button) view.findViewById(R.id.cpi_btn_ok);
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
        view.findViewById(R.id.cpi_view_id).setVisibility(View.VISIBLE);

        mListView = (ListView) view.findViewById(R.id.listdate);
        initListView();
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

    /**
     * 多选监听适配器
     */
    abstract class MultiChoiceModeListenerAdapter implements AbsListView.MultiChoiceModeListener {

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    }

    public interface OnFinishSelectionListener {
        void onFinishSelection();
    }
}
