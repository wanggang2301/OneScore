package com.hhly.mlottery.frame.footballframe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BettingPayDetailsActivity;
import com.hhly.mlottery.adapter.bettingadapter.BettingIssueAdapter;
import com.hhly.mlottery.bean.bettingbean.BettingIssueBean;
import com.hhly.mlottery.config.BlurPopWin;
import com.hhly.mlottery.util.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by：XQyi on 2017/5/11 11:12
 * Use:内页推介页面
 */
public class BettingIssueFragment extends Fragment implements View.OnClickListener {

    private static final String THIRDID = "param1";
    private String mThirdid;
    private View mView;
    private List<BettingIssueBean> list;
    private BettingIssueAdapter mAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ImageView mYuyin;
    private ImageView mText;
    private ImageView mFabuOrQxiao;

    private boolean isFabu = true;//是否是要发布状态（默认true 点击发布）
    public static BettingIssueFragment newInstance(String thirdId) {
        BettingIssueFragment fragment = new BettingIssueFragment();
        Bundle args = new Bundle();
        args.putString(THIRDID , thirdId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mThirdid = getArguments().getString(THIRDID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.betting_issue_fragment , container ,false);
        initView();
        initData();
        return mView;
    }

    private void initView(){
        recyclerView = (RecyclerView) mView.findViewById(R.id.betting_issue_view);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        mYuyin = (ImageView) mView.findViewById(R.id.football_betting_yuyin_img);
        mYuyin.setOnClickListener(this);
        mText = (ImageView) mView.findViewById(R.id.football_betting_text_img);
        mText.setOnClickListener(this);
        mFabuOrQxiao = (ImageView) mView.findViewById(R.id.football_betting_fabuorquxiao_img);
        mFabuOrQxiao.setOnClickListener(this);
    }

    public void initData(){
        list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            BettingIssueBean data = new BettingIssueBean();
            data.setName("我是专家 " + i);
            list.add(data);
        }
        buyClicked();
        if (mAdapter == null) {
            mAdapter = new BettingIssueAdapter(getActivity() , list);
            recyclerView.setAdapter(mAdapter);
            mAdapter.setmBuyClick(mIssueBuyClickListener);
        }else{
            updataAdapter();
        }
    }
    private void updataAdapter(){
        if (mAdapter == null) {
            return;
        }
        mAdapter.setData(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.football_betting_yuyin_img:
                Toast.makeText(getActivity(), "发布语音", Toast.LENGTH_SHORT).show();
                setPopupWindow();
                break;
            case R.id.football_betting_text_img:
                Toast.makeText(getActivity(), "发布文字", Toast.LENGTH_SHORT).show();
                break;
            case R.id.football_betting_fabuorquxiao_img:
                if (isFabu) {
                    mYuyin.setVisibility(View.VISIBLE);
                    mText.setVisibility(View.VISIBLE);
                    mFabuOrQxiao.setBackgroundResource(R.mipmap.football_icon_quxiao);
                    Toast.makeText(getActivity(), "发布", Toast.LENGTH_SHORT).show();
                }else {
                    mYuyin.setVisibility(View.GONE);
                    mText.setVisibility(View.GONE);
                    mFabuOrQxiao.setBackgroundResource(R.mipmap.football_icon_fabu);
                    Toast.makeText(getActivity(), "取消", Toast.LENGTH_SHORT).show();
                }
                isFabu = !isFabu;
                break;
        }
    }
    private IssueBuyClickListener mIssueBuyClickListener;
    // 购买(查看)的点击监听
    public interface IssueBuyClickListener {
        void BuyOnClick(View view , String s);
    }
    /**
     * 购买(查看)的点击事件
     */
    public void buyClicked(){
        mIssueBuyClickListener = new IssueBuyClickListener() {
            @Override
            public void BuyOnClick(View view, String s) {
                L.d("qwer_name = ",s);
                Intent mIntent = new Intent(getActivity() , BettingPayDetailsActivity.class);
                startActivity(mIntent);
                getActivity().overridePendingTransition(R.anim.push_left_in , R.anim.push_fix_out);
            }
        };
    }

    public void setPopupWindow(){

        new BlurPopWin.Builder(getActivity())
                //Radius越大耗时越长,被图片处理图像越模糊
                .setRadius(2).
                //设置居中还是底部显示
                setshowAtLocationType(0).
                setShowCloseButton(true).
                setOutSideClickable(false)
                .onClick(new BlurPopWin.PopupCallback() {
                    @Override
                    public void onClick(@NonNull BlurPopWin blurPopWin) {
                        Toast.makeText(getActivity(), "点击了弹框", Toast.LENGTH_SHORT).show();
                        blurPopWin.dismiss();
                    }
                }).show(mYuyin);
    }
}
