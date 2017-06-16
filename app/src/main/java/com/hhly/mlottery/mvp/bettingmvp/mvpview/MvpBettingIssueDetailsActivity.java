package com.hhly.mlottery.mvp.bettingmvp.mvpview;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BaseActivity;
import com.hhly.mlottery.bean.bettingbean.BettingIssueFabuPalyBean;
import com.hhly.mlottery.bean.bettingbean.BettingListDataBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.SignUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by：XQyi on 2017/5/31 16:08
 * Use: 推介发布页面（view）
 */
public class MvpBettingIssueDetailsActivity extends Activity implements View.OnClickListener {
    /** 签名参数 */
    private String PARAM_MATCH_ID = "matchId";//比赛id matchId
    private String PARAM_USER_ID = "userId";//用户id
    private String PARAM_LOGIN_TOKEN = "loginToken";//logintoken
    private String PARAM_SIGN = "sign";//参数签名
    private String PARAM_LANG = "lang";
    private String PARAM_TIMEZONE = "timeZone";

    private ImageView mBack;
    private ImageView inputImg;
    private EditText tittleEdit;
    private EditText detailsEdit;
    private EditText jiageEdit;
    private String mMatchId;
    private TextView issueLeagueName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.betting_issue_activity);

        initView();
        initData();
    }

    private void initView(){
        TextView title = (TextView)findViewById(R.id.public_txt_title);
        title.setText("发布推介");
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);

        mBack = (ImageView) findViewById(R.id.public_img_back);
        mBack.setOnClickListener(this);

        inputImg = (ImageView) findViewById(R.id.input_edit_img);

        tittleEdit = (EditText) findViewById(R.id.betting_issue_tittle_edit);

        detailsEdit = (EditText) findViewById(R.id.betting_issue_details_edit);
        detailsEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        detailsEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //获得焦点时
                    inputImg.setVisibility(View.GONE);
                    detailsEdit.setHint("");
                }else{
                    if (TextUtils.isEmpty(detailsEdit.getText())) {
                        inputImg.setVisibility(View.VISIBLE);
                        detailsEdit.setHint("         最多输入100个字符！");
                    }else{
                        inputImg.setVisibility(View.GONE);
                        detailsEdit.setHint("");
                    }
                }
            }
        });
        jiageEdit = (EditText) findViewById(R.id.betting_issue_jiage_edit);
        jiageEdit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        issueLeagueName = (TextView) findViewById(R.id.betting_issue_leagueName);
    }

    public void initData(){
        mMatchId = getIntent().getStringExtra("matchId");

        String url = "http://192.168.10.242:8098/promotion/info/getpromotiontype";

        String userid = AppConstants.register.getUser() == null ? "" : AppConstants.register.getUser().getUserId();
        String token = AppConstants.register.getToken();

        Map<String ,String> mapPrament = new HashMap<>();
        mapPrament.put(PARAM_USER_ID , userid);
        mapPrament.put(PARAM_MATCH_ID , mMatchId);
        mapPrament.put(PARAM_LOGIN_TOKEN , token); //logintoken
        mapPrament.put(PARAM_LANG , MyApp.getLanguage());
        mapPrament.put(PARAM_TIMEZONE , AppConstants.timeZone + "");
        String signs = SignUtils.getSign("/promotion/info/getpromotiontype", mapPrament);

        Map<String ,String> map = new HashMap<>();
        map.put(PARAM_USER_ID , userid);//用户id
        map.put(PARAM_LOGIN_TOKEN , token); //logintoken
        map.put(PARAM_MATCH_ID , mMatchId);
        map.put(PARAM_SIGN , signs);

        VolleyContentFast.requestJsonByGet(url,map , new VolleyContentFast.ResponseSuccessListener<BettingIssueFabuPalyBean>() {
            @Override
            public void onResponse(BettingIssueFabuPalyBean jsonBean) {
                if (jsonBean.getCode() == 200) {
                    L.d("qwerqwer == >" , "请求成功");

                    if (jsonBean.getData() != null) {

                        issueLeagueName.setText(jsonBean.getData().getLeagueName());
                    }

                }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                L.d("qwerqwer == >" , "访问失败");
            }
        },BettingIssueFabuPalyBean.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_img_back:
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
        }
    }
}