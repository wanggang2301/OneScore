package com.hhly.mlottery.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;


import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.ChoseStartAdapter;
import com.hhly.mlottery.adapter.ChoseStartManAdapter;
import com.hhly.mlottery.bean.ChoseStartBean;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.net.account.AccountResultCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuely198 on 2016/11/14.
 */

public class AvatarSelectionActivity extends  BaseActivity implements  View.OnClickListener{

    private TextView public_txt_title;
    private TextView tv_right;
    private GridView male_gridview;
    private GridView famle_gridview;
    private List<ChoseStartBean.DataBean.MaleBean> mMaleDatas;
    private List<ChoseStartBean.DataBean.FemaleBean> mFemaleDatas;
    private ChoseStartManAdapter choseStartManAdapter;//足球风采adapter
    private ChoseStartAdapter choseStartAdapter;//足球宝贝adapter

    private ProgressDialog progressBar;

    private List<String> mCupChicked=new ArrayList<>(); //选中的id
    private TextView start_famle_size;
    private TextView start_male_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        loadData();
    }


    private void loadData() {

        VolleyContentFast.requestJsonByGet(BaseURLs.FINDHEADICONS, null, new VolleyContentFast.ResponseSuccessListener<ChoseStartBean>() {

            @Override
            public void onResponse(final ChoseStartBean json) {

                if(json.getResult()==200){

                        mMaleDatas = json.getData().getMale();
                        //start_male_size.setText(mMaleDatas.size());

                    //足球风采
                    for (int i = 0; i< mMaleDatas.size(); i++){
                        mMaleDatas.get(i).setIsChecked(false);
                    }
                    if (choseStartManAdapter==null){
                        choseStartManAdapter = new ChoseStartManAdapter(mContext,json.getData().getMale(), R.layout.avatar_start_head_child);
                        choseStartManAdapter.setOnCheckListener(onCheckmanListener);
                        male_gridview.setAdapter(choseStartManAdapter);
                      }

                    //足球宝贝
                    mFemaleDatas = json.getData().getFemale();
                   // start_famle_size.setText(mFemaleDatas.size());
                    for (int i = 0; i< mFemaleDatas.size(); i++){
                        mFemaleDatas.get(i).setIsChecked(false);
                    }
                    if (choseStartAdapter==null){
                        choseStartAdapter = new ChoseStartAdapter(mContext, json.getData().getFemale(), R.layout.avatar_start_head_child);
                        choseStartAdapter.setOnCheckListener(onCheckListener);
                        famle_gridview.setAdapter(choseStartAdapter);
                    }
                   }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
            }
        }, ChoseStartBean.class);
    }

    ChoseStartAdapter.OnCheckListener onCheckListener =  new ChoseStartAdapter.OnCheckListener() {
        @Override
        public void onCheck(ChoseStartBean.DataBean.FemaleBean mFilter) {
          //  UiUtils.toast(getApplicationContext(),"mFilter"+mFilter.toString());

            if (!mFilter.isChecked()) {//不选中->选中
                mCupChicked.add(mFilter.getHeadIcon());
                mFilter.setIsChecked(true);
            } else {//选中->不选中
                mCupChicked.remove(mFilter.getHeadIcon());
                mFilter.setIsChecked(false);
            }
        }

    };
    ChoseStartManAdapter.OnCheckListener onCheckmanListener =  new ChoseStartManAdapter.OnCheckListener() {
        @Override
        public void onCheck(ChoseStartBean.DataBean.MaleBean mFilter) {
          //  UiUtils.toast(getApplicationContext(),"mFilter"+mFilter.toString());

            if (!mFilter.isChecked()) {//不选中->选中
                mCupChicked.add(mFilter.getHeadIcon());
                mFilter.setIsChecked(true);
            } else {//选中->不选中
                mCupChicked.remove(mFilter.getHeadIcon());
                mFilter.setIsChecked(false);
            }
        }

    };

    private void initView() {


        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage(getResources().getString(R.string.is_uploading));

       setContentView(R.layout.avatar_selection_heade);

   /*     chose_head = (RecyclerView) findViewById(R.id.chose_head);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        chose_head.setLayoutManager(gridLayoutManager);
        chose_woman_head = (RecyclerView) findViewById(R.id.chose_woman_head);
        GridLayoutManager gridLayoutManager1=new GridLayoutManager(this,3);
        chose_woman_head.setLayoutManager(gridLayoutManager1);*/
/*
      /*  famle_gridview = (GridView) findViewById(R.id.famle_gridview);
        male_gridview = (GridView) findViewById(R.id.male_gridview);*//*
*/
        famle_gridview = (GridView) findViewById(R.id.famle_gridview);
        male_gridview = (GridView) findViewById(R.id.male_gridview);
        findViewById(R.id.public_img_back).setOnClickListener(this);
        public_txt_title = (TextView) findViewById(R.id.public_txt_title);
        public_txt_title.setText(getResources().getString(R.string.chose_head_phot));
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setOnClickListener(this);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(getResources().getString(R.string.complete));
        //足球宝贝的个数
        start_famle_size = (TextView) findViewById(R.id.start_famle_size);
        //球星风采个数
        start_male_size = (TextView) findViewById(R.id.start_male_size);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_img_back:
                finish();
            break;
            case R.id.tv_right:
            //请求后台进行账户绑定
                if (mCupChicked.size()==1){
                    putPhotoUrl(mCupChicked.get(0));
                   // ((ProfileActivity)getApplicationContext()).setHeadPhoto(mCupChicked.get(0));
                    System.out.print("ChoseURL>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+mCupChicked.get(0));
                    finish();
                }else if(mCupChicked.size()==0){

                }else {
                    UiUtils.toast(getApplicationContext(),R.string.can_only_choose_a_picture);
                }
            break;
            default:
              break;

        }
    }


    /*上传图片url  后台绑定*/

    private void putPhotoUrl(final String headerUrl) {

        //final String url = BaseURLs.UPDATEHEADICON;

        Map<String, String> param = new HashMap<>();

        Log.d(TAG, AppConstants.deviceToken);
        param.put("deviceToken", AppConstants.deviceToken);

        Log.d(TAG, PreferenceUtil.getString(AppConstants.SPKEY_TOKEN, ""));
        param.put("loginToken", PreferenceUtil.getString(AppConstants.SPKEY_TOKEN, ""));

        Log.d(TAG, AppConstants.deviceToken);
        param.put("imgUrl", headerUrl);
        VolleyContentFast.requestJsonByPost(BaseURLs.UPDATEHEADICON, param, new VolleyContentFast.ResponseSuccessListener<Register>() {
            @Override
            public void onResponse(Register register) {
                progressBar.dismiss();
                if (register.getResult() == AccountResultCode.SUCC) {
                    UiUtils.toast(MyApp.getInstance(), R.string.picture_put_success);
                    // CommonUtils.saveRegisterInfo(register);
                    PreferenceUtil.commitString(AppConstants.HEADICON, register.getData().getUser().getHeadIcon().toString());
                    AppConstants.register.getData().getUser().setHeadIcon(headerUrl);
                    Intent intent=new Intent();
                    intent.putExtra("imgUrl",register.getData().getUser().getHeadIcon().toString());
                    setResult(RESULT_OK,intent);
                    System.out.print("ChoseURL>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+register.getData().getUser().getHeadIcon().toString());
                 //  ImageLoader.load(mContext,register.getData().getUser().getHeadIcon(),R.mipmap.center_head).into(mHead_portrait);

                } else {
                    CommonUtils.handlerRequestResult(register.getResult(), register.getMsg());
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                progressBar.dismiss();
                L.e(TAG, "图片上传失败");
                UiUtils.toast(AvatarSelectionActivity.this, R.string.picture_put_failed);
            }
        }, Register.class);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ProfileActivity.REQUESTCODE_CHOSE:
                    L.i(TAG, "头像上传成功");
                    setResult(ProfileActivity.RESULT_OK);
                    finish();
                    break;
            }
        }
    }
}
