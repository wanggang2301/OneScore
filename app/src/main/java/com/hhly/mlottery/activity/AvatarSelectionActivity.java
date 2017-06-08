package com.hhly.mlottery.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.ChoseFailStartManAdapter;
import com.hhly.mlottery.adapter.ChoseFailStartWomanAdapter;
import com.hhly.mlottery.adapter.ChoseStartManAdapter;
import com.hhly.mlottery.adapter.ChoseStartWomanAdapter;
import com.hhly.mlottery.bean.ChoseHeadStartBean;
import com.hhly.mlottery.bean.ChoseStartBean;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.cipher.MD5Util;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.net.account.AccountResultCode;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;


/**
 * Created by yuely198 on 2016/11/14.
 * 选择球头像
 */

public class AvatarSelectionActivity extends Activity implements View.OnClickListener {


    private TextView public_txt_title;
    private TextView tv_right;
    private GridView male_gridview;
    private List<ChoseStartBean.DataBean.MaleBean> mMaleDatas;
    private List<ChoseStartBean.DataBean.FemaleBean> mFemaleDatas;
    private ChoseStartManAdapter choseStartManAdapter;//足球风采adapter
    private ChoseStartWomanAdapter choseStartAdapter;//足球宝贝adapter

    private ProgressDialog progressBar;
    private String CupChicked;
    //    private String mCupChickedMan = null;
//    private String mCupChickedWoman = null;//选中的id
    private TextView start_famle_size;
    private TextView start_male_size;

    private GridView famle_gridview;

    List<String> male = new ArrayList<>();
    List<String> female = new ArrayList<>();
    //    private ListDatasSaveUtils listMaleDatasSaveUtils;
//    private List<List<ChoseStartBean.DataBean.MaleBean>> maleDatas = new ArrayList<>();
    private ChoseFailStartManAdapter choseFailStartManAdapter;
    private ChoseFailStartWomanAdapter choseFailStartWomanAdapter;
    private LinearLayout text_times_title1;
    private ImageView ib_operate_more;
    private String language;

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

                if (json.getResult() == 200) {

                    mMaleDatas = json.getData().getMale();
                    for (int i = 0; i < mMaleDatas.size(); i++) {
                        if (mMaleDatas.get(i).getHeadIcon() != null) {
                            PreferenceUtil.commitString("male" + i, mMaleDatas.get(i).getHeadIcon());
                        }
                    }
                    PreferenceUtil.commitString("maleSize", mMaleDatas.size() + "");

                    //start_male_size.setText(mMaleDatas.size());
                    start_male_size.setText(String.valueOf(mMaleDatas.size()));
                    ///maleDatas.add(mMaleDatas);
                    //listMaleDatasSaveUtils.setDataList("maleDatas",mMaleDatas);
                    if (choseStartManAdapter == null) {
                        choseStartManAdapter = new ChoseStartManAdapter(AvatarSelectionActivity.this, json.getData().getMale(), R.layout.avatar_start_head_child);
                        male_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                choseStartManAdapter.setSeclection(position);
                                choseStartAdapter.setSeclection(-1);
                                choseStartAdapter.notifyDataSetChanged();
                                choseStartManAdapter.notifyDataSetChanged();
                                CupChicked = null;
                                CupChicked = mMaleDatas.get(position).getHeadIcon();
                            }
                        });
                        male_gridview.setAdapter(choseStartManAdapter);
                    }

                    //足球宝贝
                    mFemaleDatas = json.getData().getFemale();
                    //listMaleDatasSaveUtils.setDataList("femaleDatas",mFemaleDatas);
                    for (int i = 0; i < mFemaleDatas.size(); i++) {
                        if (mMaleDatas.get(i).getHeadIcon() != null) {
                            PreferenceUtil.commitString("female" + i, mFemaleDatas.get(i).getHeadIcon());
                        }
                    }
                    PreferenceUtil.commitString("femaleSize", mFemaleDatas.size() + "");
                    start_famle_size.setText(String.valueOf(mFemaleDatas.size()));
                    if (choseStartAdapter == null) {
                        choseStartAdapter = new ChoseStartWomanAdapter(AvatarSelectionActivity.this, json.getData().getFemale(), R.layout.avatar_start_head_child);
                        famle_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                choseStartAdapter.setSeclection(position);
                                choseStartManAdapter.setSeclection(-1);
                                choseStartManAdapter.notifyDataSetChanged();
                                choseStartAdapter.notifyDataSetChanged();
                                CupChicked = null;
                                CupChicked = mFemaleDatas.get(position).getHeadIcon();
                            }
                        });
                        famle_gridview.setAdapter(choseStartAdapter);
                    }
                }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                if (!"".equals(PreferenceUtil.getString("maleSize", ""))) {
                    start_male_size.setText(PreferenceUtil.getString("maleSize", ""));
                    for (int i = 0; i < Integer.parseInt(PreferenceUtil.getString("maleSize", "")); i++) {
                        String url = PreferenceUtil.getString("male" + i, "");
                        male.add(url);
                    }
                    if (choseStartManAdapter == null) {
                        choseFailStartManAdapter = new ChoseFailStartManAdapter(AvatarSelectionActivity.this, male, R.layout.avatar_start_head_child);
                        male_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                choseFailStartManAdapter.setSeclection(position);
                                choseFailStartWomanAdapter.setSeclection(-1);
                                choseFailStartManAdapter.notifyDataSetChanged();
                                choseFailStartWomanAdapter.notifyDataSetChanged();
                                CupChicked = null;
                                CupChicked = male.get(position);
                            }
                        });
                        //   choseStartManAdapter.setOnCheckListener(onCheckmanListener);
                        male_gridview.setAdapter(choseFailStartManAdapter);
                    }

                }

                //足球宝贝
                if (!"".equals(PreferenceUtil.getString("femaleSize", ""))) {
                    start_famle_size.setText(PreferenceUtil.getString("femaleSize", ""));
                    for (int i = 0; i < Integer.parseInt(PreferenceUtil.getString("femaleSize", "")); i++) {
                        String url = PreferenceUtil.getString("female" + i, "");
                        female.add(url);
                    }
                    if (choseStartAdapter == null) {
                        choseFailStartWomanAdapter = new ChoseFailStartWomanAdapter(AvatarSelectionActivity.this, female, R.layout.avatar_start_head_child);
                        famle_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                choseFailStartWomanAdapter.setSeclection(position);
                                choseFailStartManAdapter.setSeclection(-1);
                                choseFailStartWomanAdapter.notifyDataSetChanged();
                                choseFailStartManAdapter.notifyDataSetChanged();
                                CupChicked = null;
                                CupChicked = female.get(position);
                            }
                        });
                        famle_gridview.setAdapter(choseFailStartWomanAdapter);
                    }
                }


            }
        }, ChoseStartBean.class);
    }


    private void initView() {
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage(getResources().getString(R.string.is_uploading));

//        listMaleDatasSaveUtils = new ListDatasSaveUtils(AvatarSelectionActivity.this, "Datas");
        setContentView(R.layout.avatar_selection_heade);

        text_times_title1 = (LinearLayout) findViewById(R.id.text_times_title1);
        text_times_title1.setVisibility(View.VISIBLE);
        text_times_title1.setOnClickListener(this);
        ib_operate_more = (ImageView) findViewById(R.id.ib_operate_more);
        ib_operate_more.setVisibility(View.GONE);

        famle_gridview = (GridView) findViewById(R.id.famle_gridview);
        male_gridview = (GridView) findViewById(R.id.male_gridview);
        findViewById(R.id.public_img_back).setOnClickListener(this);
        public_txt_title = (TextView) findViewById(R.id.public_txt_title);
        public_txt_title.setText(getResources().getString(R.string.chose_head_phot));
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(getResources().getString(R.string.complete));
        //足球宝贝的个数
        start_famle_size = (TextView) findViewById(R.id.start_famle_size);
        //球星风采个数
        start_male_size = (TextView) findViewById(R.id.start_male_size);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back:
                finish();
                break;
            case R.id.text_times_title1:
                //请求后台进行账户绑定
                if (CupChicked != null) {
                    EventBus.getDefault().post(new ChoseHeadStartBean(CupChicked));
                    AppConstants.register.getUser().setImageSrc(CupChicked);
                    putPhotoUrl(CupChicked);
                    finish();
                } else {
                    UiUtils.toast(getApplicationContext(), R.string.no_select_head);
                }
                break;
            default:
                break;

        }
    }


    /*上传图片url  后台绑定*/

    private void putPhotoUrl(final String headerUrl) {
        progressBar.show();
        Map<String, String> param = new HashMap<>();


        param.put("userId", AppConstants.register.getUser().getUserId());

        param.put("avatorURL",headerUrl);
        param.put("loginToken",AppConstants.register.getToken());


        if (MyApp.isLanguage.equals("rCN")) {
            // 如果是中文简体的语言环境
            language = "langzh";
        } else if (MyApp.isLanguage.equals("rTW")) {
            // 如果是中文繁体的语言环境
            language="langzh-TW";
        }


        String sign=DeviceInfo.getSign("/user/updateavatorbyurl"+"avatorURL"+headerUrl+language+"loginToken"+AppConstants.register.getToken()+"timeZone8"+"userId"+AppConstants.register.getUser().getUserId());
        param.put("sign",sign);
        VolleyContentFast.requestJsonByPost(BaseURLs.PUT_PHOTO_URL, param, new VolleyContentFast.ResponseSuccessListener<Register>() {
            @Override
            public void onResponse(Register register) {
                //  progressBar.dismiss();

                if (Integer.parseInt(register.getCode()) == AccountResultCode.SUCC) {
                    UiUtils.toast(MyApp.getInstance(), R.string.picture_put_success);
                   // register.getUser().setPhoneNum(PreferenceUtil.getString(AppConstants.SPKEY_LOGINACCOUNT, "aa"));
                   // DeviceInfo.saveRegisterInfo(register);
                    AppConstants.register.getUser().setImageSrc(headerUrl);
                    PreferenceUtil.commitString(AppConstants.HEADICON,headerUrl);
                    EventBus.getDefault().post(new ChoseHeadStartBean(headerUrl));
                    progressBar.dismiss();
                    finish();
                } else {
                    DeviceInfo.handlerRequestResult(Integer.parseInt(register.getCode()),"未知错误");
                    progressBar.dismiss();
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                progressBar.dismiss();
                UiUtils.toast(AvatarSelectionActivity.this, R.string.picture_put_failed);
            }
        }, Register.class);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
