package com.hhly.mlottery.adapter.chartBallFragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.ChartballActivity;
import com.hhly.mlottery.adapter.chartBallAdapter.EmojiGridAdapter;
import com.hhly.mlottery.adapter.chartBallAdapter.LocalGridAdapter;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.ToastTools;
import com.hhly.mlottery.widget.MyGridView;

import java.util.ArrayList;
import java.util.Map;

import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.github.rockerhieu.emojicon.EmojiconTextView;

/**
 * desc:emoji表情fragment
 * Created by 107_tangrr on 2016/12/9 0009.
 */

public class EmojiFragment extends Fragment {
    private static final String DATA_KEY = "mData";
    private ArrayList<String> mData;
    private View mView;
    private Context mContext;
    private MyGridView gridView;
    private EmojiconEditText mEditText;

    public static EmojiFragment newInstance(ArrayList<String> iconlist) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(DATA_KEY, iconlist);
        EmojiFragment fragment = new EmojiFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mData = args.getStringArrayList(DATA_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mView = View.inflate(mContext, R.layout.chart_ball_emoji_pager, null);
        initView();
        initEvent();
        return mView;
    }

    private void initEvent() {

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = mEditText.getText().toString();
                text = text + mData.get(i);
                mEditText.setText(text);

                // TODO  当点击的为删除键时 删除单个字符
//                int index = mEditText.getSelectionStart();   //获取Edittext光标所在位置
//                String str = mEditText.getText().toString();
//                if (!str.equals("")) {//判断输入框不为空，执行删除
//                    mEditText.getText().delete(index - 1, index);
//                }
            }
        });
    }

    private void initView() {
        gridView = (MyGridView) mView.findViewById(R.id.emoji_page_item_gridView);
        gridView.setAdapter(new EmojiGridAdapter(mContext, mData));

        mEditText = ((ChartballActivity) mContext).mEditText;
    }
}
