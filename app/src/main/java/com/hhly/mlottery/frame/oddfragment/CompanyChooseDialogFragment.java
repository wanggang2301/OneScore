package com.hhly.mlottery.frame.oddfragment;

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
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.cpiadapter.CpiCompanyAdapter;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;

import java.util.ArrayList;

/**
 * 公司选择
 * <p>
 * Created by loshine on 2016/6/23.
 */
public class CompanyChooseDialogFragment extends DialogFragment {

    private static final String KEY_COMPANY_LIST = "company_list";

    TextView mTitleTextView;
    ListView mListView;
    Button mOkButton;

    private ArrayList<NewOddsInfo.CompanyBean> companyList;

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

        mListView = (ListView) view.findViewById(R.id.listdate);

        // 确认按钮
        mOkButton = (Button) view.findViewById(R.id.cpi_btn_ok);
        mOkButton.setVisibility(View.VISIBLE);
        view.findViewById(R.id.cpi_view_id).setVisibility(View.VISIBLE);

        CpiCompanyAdapter adapter = new CpiCompanyAdapter(getContext(), companyList);
        mListView.setAdapter(adapter);
        // 设置 listView 的 item 不能被获取焦点,焦点由 listView 里的控件获得
        mListView.setItemsCanFocus(false);
        //设置多选
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, final int position, long arg3) {
                CheckedTextView checkedTextView = (CheckedTextView)
                        parent.getChildAt(position).findViewById(R.id.item_checkedTextView);
                ImageView itemImageView = (ImageView)
                        parent.getChildAt(position).findViewById(R.id.item_img_checked);
                checkedTextView.setChecked(!checkedTextView.isChecked());
                // 如果是选中
                if (checkedTextView.isChecked()) {
                    itemImageView.setBackgroundResource(R.mipmap.cpi_img_select_true);
                } else {
                    itemImageView.setBackgroundResource(R.mipmap.cpi_img_select);
                }
            }
        });
    }

    public static CompanyChooseDialogFragment newInstance(ArrayList<NewOddsInfo.CompanyBean> companyList) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_COMPANY_LIST, companyList);
        CompanyChooseDialogFragment fragment = new CompanyChooseDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
