package com.hhly.mlottery.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.ExpertreQuestBean;
import com.hhly.mlottery.bean.SpecialistBean;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.FootBallDetailTypeEnum;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.cipher.MD5Util;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.net.account.AccountResultCode;
import com.hhly.mlottery.util.net.account.RegisterType;
import com.hhly.mlottery.view.FlowLayout;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * Created by yuely198 on 2017/6/1.
 * 申请专家页面
 */

public class ApplicationSpecialistActivity extends BaseActivity implements View.OnClickListener {


    private TextView public_txt_title;
    private ImageView set_rd_alet;
    boolean isChecked = false;
    private EditText specalist_edittext;
    private ImageView specialist_pen;
    private EditText real_name;
    private LinearLayout specalist_error_tv;
    private TextView specalist_tv;
    private LinearLayout error_prompt;
    private EditText good_league;
    private TextView tv_comfirm;
    private GridView gridview;

    // 标签云父布局
    private FlowLayout mFlowLayout;
    // 标签名称列表
    private List<String> labelNameList;
    private TextView immediate_authentication;
    private EditText id_datas;
    private String new_leauue;
    private LinearLayout to_examine;
    private ScrollView scrollview;
    private String expert;
    private LinearLayout agreement;
    private TextView specialist_tv2;
    private LinearLayout good_league_rl;
    private ImageView success_image;
    private TextView shen_good_legue;
    private TextView symptomSelectedNameTv;
    private String language;
    private TextView specalist_error_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null && getIntent().getExtras() != null) {
            expert = getIntent().getExtras().getString("expert", "");
        }

        initView();

    }


    private void initView() {

        setContentView(R.layout.activity_application_specialist);

        public_txt_title = (TextView) findViewById(R.id.public_txt_title);

        public_txt_title.setText("申请专家");

        findViewById(R.id.public_img_back).setOnClickListener(this);
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);


        set_rd_alet = (ImageView) findViewById(R.id.set_rd_alet);
        set_rd_alet.setOnClickListener(this);
        //真实姓名
        real_name = (EditText) findViewById(R.id.real_name);
        //身份证
        id_datas = (EditText) findViewById(R.id.id_datas);

        //擅长联赛
        good_league = (EditText) findViewById(R.id.good_league);
        //确认按钮
        tv_comfirm = (TextView) findViewById(R.id.tv_comfirm);
        tv_comfirm.setOnClickListener(this);
        //错误信息
        specalist_error_tv = (LinearLayout) findViewById(R.id.specalist_error_tv);
        specalist_error_text = (TextView) findViewById(R.id.specalist_error_text);
        //超出限制提示
        error_prompt = (LinearLayout) findViewById(R.id.error_prompt);

        //立即认证
        immediate_authentication = (TextView) findViewById(R.id.immediate_authentication);
        immediate_authentication.setOnClickListener(this);
        //限制字数显示
        specalist_tv = (TextView) findViewById(R.id.specalist_tv);

        specialist_pen = (ImageView) findViewById(R.id.specialist_pen);
        //专家简介
        specalist_edittext = (EditText) findViewById(R.id.specalist_edittext);
   /*     specalist_edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if (isFocus){
                    specialist_pen.setVisibility(View.GONE);

                }else{
                    specialist_pen.setVisibility(View.VISIBLE);
                }

            }
        });*/
        specalist_edittext.addTextChangedListener(new TextWatcher() {

            private CharSequence temp;
            private int editStart;
            private int editEnd;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                temp = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                editStart = specalist_edittext.getSelectionStart();
                editEnd = specalist_edittext.getSelectionEnd();
                specalist_tv.setText(temp.length() + "/1000");
                if (temp.length() > 1000) {
                    error_prompt.setVisibility(View.VISIBLE);
                    specalist_tv.setTextColor(getResources().getColor(R.color.foot_team_name_score3));
                } else {
                    error_prompt.setVisibility(View.GONE);
                    specalist_tv.setTextColor(getResources().getColor(R.color.snooker_line));
                }

            }
        });
        shen_good_legue = (TextView) findViewById(R.id.shen_good_legue);
        good_league_rl = (LinearLayout) findViewById(R.id.good_league_rl);
        //添加标签
        mFlowLayout = (FlowLayout) findViewById(R.id.fly_symptom_one);

        //审核页面
        to_examine = (LinearLayout) findViewById(R.id.to_examine);
        success_image = (ImageView) findViewById(R.id.success_image);

        agreement = (LinearLayout) findViewById(R.id.agreement);

        scrollview = (ScrollView) findViewById(R.id.scrollview);
        if (expert.equals("1")) {//审核通过

            expertrequest();

        } else if (expert.equals("0")) {//未审核
            to_examine.setVisibility(View.GONE);
            scrollview.setVisibility(View.VISIBLE);
            success_image.setVisibility(View.GONE);
            good_league_rl.setVisibility(View.VISIBLE);
            shen_good_legue.setVisibility(View.GONE);
        } else if (expert.equals("2")) {  //审核中
            to_examine.setVisibility(View.VISIBLE);
            scrollview.setVisibility(View.GONE);
        } else if (expert.equals("3")) {   //审核失败

            expertrequest();
        }

        //付费协议

        specialist_tv2 = (TextView) findViewById(R.id.specialist_tv2);
        specialist_tv2.setOnClickListener(this);
    }

    //审核信息



    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.public_img_back:
                finish();
                break;
            case R.id.set_rd_alet:

                if (isChecked) {
                    set_rd_alet.setImageResource(R.mipmap.chose);
                    isChecked = false;
                } else {
                    isChecked = true;
                    set_rd_alet.setImageResource(R.mipmap.chosed);

                }


                break;
            case R.id.tv_comfirm:
                if (!good_league.getText().toString().equals("")) {
                    SplitString(good_league.getText().toString());
                }


                break;
            case R.id.immediate_authentication:
                if (isChecked) {
                    if (real_name.getText().toString() != null && id_datas.getText().toString() != null && good_league.getText().toString() != null && specalist_edittext.getText().toString() != null) {
                        specalist_error_tv.setVisibility(View.GONE);
                        comfirm(real_name.getText().toString(), id_datas.getText().toString(), new_leauue, specalist_edittext.getText().toString());
                    } else {
                        specalist_error_tv.setVisibility(View.VISIBLE);
                    }

                } else {
                    UiUtils.toast(this, "请认真阅读推荐付费协议");
                }
                break;
            case R.id.specialist_tv2:

                gotoWebActivity();

                break;

            default:
                break;
        }

    }
    private void SplitString1(String text) {
        labelNameList = new ArrayList<>();

        String[] str = text.split("，");


        for (int i = 0; i < str.length; i++) {
            labelNameList.add(str[i]);
        }
        addChildLabel1(labelNameList);
        new_leauue = good_league.getText().toString();
        good_league.setText("");
    }


    private void SplitString(String text) {
        labelNameList = new ArrayList<>();

        String[] str = text.split("，");


        for (int i = 0; i < str.length; i++) {
            labelNameList.add(str[i]);
        }
        addChildLabel(labelNameList);
        new_leauue = good_league.getText().toString();
        good_league.setText("");
    }

    /*添加子标签*/
    private void addChildLabel(List<String> labelNameList) {
        // 数据源为空则返回
        if (labelNameList == null || labelNameList.size() == 0) {
            return;
        }
        // 遍历数据
        for (String labelName : this.labelNameList) {
            // 指定子标签布局
            final View labelView = LayoutInflater.from(this).inflate(R.layout.layout_child_selected_label, null);

            symptomSelectedNameTv = (TextView) labelView.findViewById(R.id.tv_symptom_selected_name);
            // 清除事件
            labelView.findViewById(R.id.tv_symptom_selected_name).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mFlowLayout != null && labelView != null)
                        mFlowLayout.removeView(labelView);
                }
            });
            if (symptomSelectedNameTv != null && labelName != null && !labelName.trim().equals("")) {
                symptomSelectedNameTv.setText(labelName);
            }
            if (mFlowLayout != null && labelView != null) {
                mFlowLayout.addView(labelView);
            }
        }
        // 刷新界面
        mFlowLayout.requestLayout();
        mFlowLayout.invalidate();
    }
    private void addChildLabel1(List<String> labelNameList) {
        // 数据源为空则返回
        if (labelNameList == null || labelNameList.size() == 0) {
            return;
        }
        // 遍历数据
        for (String labelName : this.labelNameList) {
            // 指定子标签布局
            final View labelView = LayoutInflater.from(this).inflate(R.layout.layout_child_selected_label, null);

            symptomSelectedNameTv = (TextView) labelView.findViewById(R.id.tv_symptom_selected_name);

            if (symptomSelectedNameTv != null && labelName != null && !labelName.trim().equals("")) {
                symptomSelectedNameTv.setText(labelName);
            }
            if (mFlowLayout != null && labelView != null) {
                mFlowLayout.addView(labelView);
            }
        }
        // 刷新界面
        mFlowLayout.requestLayout();
        mFlowLayout.invalidate();
    }

    /*专家认证请求*/

    private void expertrequest(){


        //String url = BaseURLs.URL_REGISTER;
        Map<String, String> param = new HashMap<>();
        param.put("userId", AppConstants.register.getUser().getUserId());
        param.put("loginToken", AppConstants.register.getToken());

        if (MyApp.isLanguage.equals("rCN")) {
            // 如果是中文简体的语言环境
            language = "langzh";
        } else if (MyApp.isLanguage.equals("rTW")) {
            // 如果是中文繁体的语言环境
            language="langzh-TW";
        }

        String sign = DeviceInfo.getSign("/user/expertInfo" +language+"loginToken"+AppConstants.register.getToken()+"timeZone8"+"userId" + AppConstants.register.getUser().getUserId());
        param.put("sign", sign);

        VolleyContentFast.requestJsonByGet("http://192.168.10.242:8099/user/expertInfo", param, new VolleyContentFast.ResponseSuccessListener<ExpertreQuestBean>() {
            @Override
            public void onResponse(ExpertreQuestBean requestBean) {

                if (requestBean != null && requestBean.getCode().equals("200")) {

                    if (requestBean.getUserInfo().getIsExpert()==3) {
                        specalist_error_tv.setVisibility(View.VISIBLE);
                        specalist_error_text.setText(requestBean.getUserInfo().getApproveIdea());
                        success_image.setVisibility(View.GONE);
                    }else{
                        success_image.setVisibility(View.VISIBLE);
                    }

                    to_examine.setVisibility(View.GONE);
                    scrollview.setVisibility(View.VISIBLE);

                    good_league_rl.setVisibility(View.GONE);
                    real_name.setText(requestBean.getUserInfo().getRealName());
                    id_datas.setText(requestBean.getUserInfo().getIdCard());
                    specalist_edittext.setText(requestBean.getUserInfo().getIntroduce());
                    SplitString1(requestBean.getUserInfo().getSkillfulLeague());

                    real_name.setKeyListener(null);
                    id_datas.setKeyListener(null);
                    specalist_edittext.setKeyListener(null);
                    good_league.setKeyListener(null);
                    agreement.setVisibility(View.GONE);
                    immediate_authentication.setVisibility(View.GONE);
                    findViewById(R.id.tv_2).setVisibility(View.GONE);
                    findViewById(R.id.tv_1).setVisibility(View.GONE);
                    shen_good_legue.setVisibility(View.VISIBLE);
                    symptomSelectedNameTv.setKeyListener(null);

                } else {

                    L.e(TAG, "成功请求，注册失败");
                    DeviceInfo.handlerRequestResult(Integer.parseInt(requestBean.getCode()), "未知错误");
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {


            }
        }, ExpertreQuestBean.class);




    }


    private void comfirm(String real_name, String id_datas, String good_league, String specalist_edittext) {


        //String url = BaseURLs.URL_REGISTER;
        Map<String, String> param = new HashMap<>();
        param.put("userId", AppConstants.register.getUser().getUserId());
        param.put("realName", real_name);//姓名
        param.put("idCard", id_datas);//身份证号码
        param.put("introduce", specalist_edittext);//专家简介
        param.put("skillfulLeague", good_league);//擅长联赛
        param.put("loginToken", AppConstants.register.getToken());
        String sign = DeviceInfo.getSign("/user/expertAuth" + "idCard" + id_datas + "introduce" + specalist_edittext + "langzh" + "loginToken" + AppConstants.register.getToken() + "realName" + real_name + "skillfulLeague" + good_league + "timeZone8" + "userId" + AppConstants.register.getUser().getUserId());
        param.put("sign", sign);

        VolleyContentFast.requestJsonByPost("http://192.168.10.242:8099/user/expertAuth", param, new VolleyContentFast.ResponseSuccessListener<Register>() {
            @Override
            public void onResponse(Register register) {

                if (register != null && Integer.parseInt(register.getCode()) == AccountResultCode.EXPERT_CERTIFICATION_AUDIT) {
                    scrollview.setVisibility(View.GONE);
                    to_examine.setVisibility(View.VISIBLE);
                    EventBus.getDefault().post(new SpecialistBean("1"));

                } else {
                    scrollview.setVisibility(View.VISIBLE);
                    to_examine.setVisibility(View.GONE);
                    L.e(TAG, "成功请求，注册失败");
                    DeviceInfo.handlerRequestResult(Integer.parseInt(register.getCode()), "未知错误");
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {


            }
        }, Register.class);

    }

    public final static String INTENT_PARAM_JUMPURL = "key";
    public final static String INTENT_PARAM_TITLE = "infoTypeName";
    //跳转H5

    private void gotoWebActivity() {

        Intent intent = new Intent(ApplicationSpecialistActivity.this, WebActivity.class);
        intent.putExtra(INTENT_PARAM_TITLE, "认真付费推荐协议");//头部名称
        intent.putExtra(INTENT_PARAM_JUMPURL, "http://m.1332255.com:81/recommended/argeement.html#/");
        intent.putExtra("title", "认真付费推荐协议");
        intent.putExtra("subtitle", "推荐协议");
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        new Timer().schedule(new TimerTask() { //让软键盘延时弹出，以更好的加载Activity
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) real_name.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(real_name, 0);
            }

        }, 300);
    }
}
