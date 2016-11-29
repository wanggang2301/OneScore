package com.hhly.mlottery.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;


import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.ChoseStartWomanAdapter;
import com.hhly.mlottery.adapter.ChoseStartManAdapter;
import com.hhly.mlottery.bean.ChoseHeadStartBean;
import com.hhly.mlottery.bean.ChoseStartBean;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.net.account.AccountResultCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by yuely198 on 2016/11/14.
 */

public class AvatarSelectionActivity extends  Activity implements  View.OnClickListener{

    private TextView public_txt_title;
    private TextView tv_right;
    private GridView male_gridview;
    private GridView famle_gridview;
    private List<ChoseStartBean.DataBean.MaleBean> mMaleDatas;
    private List<ChoseStartBean.DataBean.FemaleBean> mFemaleDatas;
    private ChoseStartManAdapter choseStartManAdapter;//足球风采adapter
    private ChoseStartWomanAdapter choseStartAdapter;//足球宝贝adapter

    private ProgressDialog progressBar;
    private String CupChicked;
    private String mCupChickedMan=null;
    private String mCupChickedWoman=null;//选中的id
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

                    start_male_size.setText(mMaleDatas.size()+"");

                    if (choseStartManAdapter==null){
                        choseStartManAdapter = new ChoseStartManAdapter(AvatarSelectionActivity.this,json.getData().getMale(), R.layout.avatar_start_head_child);
                        male_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                choseStartManAdapter.setSeclection(position);
                                choseStartAdapter.setSeclection(-1);
                                choseStartAdapter.notifyDataSetChanged();
                                choseStartManAdapter.notifyDataSetChanged();
                                CupChicked=null;
                                CupChicked=mMaleDatas.get(position).getHeadIcon();
                            }
                        });
                     //   choseStartManAdapter.setOnCheckListener(onCheckmanListener);
                        male_gridview.setAdapter(choseStartManAdapter);
                      }

                    //足球宝贝
                    mFemaleDatas = json.getData().getFemale();
                    start_famle_size.setText(mFemaleDatas.size()+"");
                    if (choseStartAdapter==null){
                        choseStartAdapter = new ChoseStartWomanAdapter(AvatarSelectionActivity.this, json.getData().getFemale(), R.layout.avatar_start_head_child);
                        famle_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                choseStartAdapter.setSeclection(position);
                                choseStartManAdapter.setSeclection(-1);
                                choseStartManAdapter.notifyDataSetChanged();
                                choseStartAdapter.notifyDataSetChanged();
                                CupChicked=null;
                                CupChicked=mFemaleDatas.get(position).getHeadIcon();
                            }
                        });
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



    private void initView() {


       setContentView(R.layout.avatar_selection_heade);

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
                if (CupChicked!=null){
                    putPhotoUrl(CupChicked);
                    finish();
                }else if(CupChicked==null){
                    UiUtils.toast(getApplicationContext(),"您还未选择头像");
                }
            break;
            default:
              break;

        }
    }


    /*上传图片url  后台绑定*/

    private void putPhotoUrl(final String headerUrl) {
        Map<String, String> param = new HashMap<>();

        param.put("deviceToken", AppConstants.deviceToken);

        param.put("loginToken", PreferenceUtil.getString(AppConstants.SPKEY_TOKEN, ""));

        param.put("imgUrl", headerUrl);
        VolleyContentFast.requestJsonByPost(BaseURLs.UPDATEHEADICON, param, new VolleyContentFast.ResponseSuccessListener<Register>() {
            @Override
            public void onResponse(Register register) {
              //  progressBar.dismiss();
                if (register.getResult() == AccountResultCode.SUCC) {
                    UiUtils.toast(MyApp.getInstance(), R.string.picture_put_success);
                    CommonUtils.saveRegisterInfo(register);
                    AppConstants.register.getData().getUser().setHeadIcon(headerUrl);
                    if (register.getData().getUser().getHeadIcon()!=null){
                        EventBus.getDefault().post(new ChoseHeadStartBean(headerUrl));
                    }
                } else {
                    CommonUtils.handlerRequestResult(register.getResult(), register.getMsg());
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                //UiUtils.toast(AvatarSelectionActivity.this, R.string.picture_put_failed);
            }
        }, Register.class);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
