package com.hhly.mlottery.frame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballActivity;

/**
 * Created by asus1 on 2016/3/29.
 * 数据
 */
public class DataFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private View mView;

    private ImageView public_img_back, public_btn_filter, public_btn_set;
    private TextView public_txt_title, live_no_data_txt;//标题，暂无数据

    @SuppressLint("ValidFragment")
    public DataFragment(Context context) {
        this.mContext=context;
    }
    public DataFragment(){}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.frag_data,container,false);

        initView();
        initData();

        return mView;
    }


    public void initView() {

        //标题
        public_txt_title = (TextView) mView.findViewById(R.id.public_txt_title);
        //暂无数据
        live_no_data_txt = (TextView) mView.findViewById(R.id.live_no_data_txt);
        //筛选
        public_btn_filter = (ImageView) mView.findViewById(R.id.public_btn_filter);
        public_btn_filter.setVisibility(View.GONE);

        //设置
        public_btn_set = (ImageView) mView.findViewById(R.id.public_btn_set);
        public_btn_set.setVisibility(View.GONE);

        public_img_back = (ImageView) mView.findViewById(R.id.public_img_back);
        public_img_back.setOnClickListener(this);
    }

    private void initData(){
        public_txt_title.setText("数据中心");
        live_no_data_txt.setText("暂无数据");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.public_img_back:
                ((FootballActivity) getActivity()).finish();
                break;
            default:
                break;
        }
    }
}
