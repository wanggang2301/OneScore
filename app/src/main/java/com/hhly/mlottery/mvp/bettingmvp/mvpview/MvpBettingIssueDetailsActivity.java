package com.hhly.mlottery.mvp.bettingmvp.mvpview;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.bettingadapter.BettingIssueAdapter;
import com.hhly.mlottery.adapter.bettingadapter.BettingRecommendSettingAdapter;
import com.hhly.mlottery.bean.bettingbean.BettingIssueFabuPalyBean;
import com.hhly.mlottery.bean.bettingbean.IssueCodeBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.SignUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.GrapeGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private TextView homeName;
    private TextView guestName;
    private RadioButton issuePriceCardA;
    private RadioButton issuePriceCardB;
    private RadioButton issuePriceCardC;
    private RadioButton issuePriceCardD;
    private RadioButton playLeftA;
    private RadioButton playMiddleA;
    private RadioButton playRightA;
    private ImageView playLeftImgA;
    private ImageView playMiddleImgA;
    private ImageView playRightImgA;

    private LinearLayout playOddsFirst;
    private LinearLayout playOddsSecond;
    private ImageView playLeftImgB;
    private ImageView playMiddleImgB;
    private ImageView playRightImgB;
    private RadioButton playLeftB;
    private RadioButton playMiddleB;
    private RadioButton playRightB;

    /**玩法赔率是否选中*/
    private boolean playLeftCheckA = false;
    private boolean playMiddleCheckA = false;
    private boolean playRightCheckA = false;
    private boolean playLeftCheckB = false;
    private boolean playMiddleCheckB = false;
    private boolean playRightCheckB = false;
    private BettingIssueAdapter mIssuePlayAdapter;
    private GrapeGridView issuePlayGrid;
    private List<BettingIssueFabuPalyBean.PromotionTypeVo.PromotionTypeListVo> listVo = new ArrayList<>();

    private String currPlay;//当前选中的玩法
    private LinearLayout toIssue;
    private Integer prositionPlay = 0;//当前玩法的角标（默认选中第一种）

    /**赔率选中状态*/
    private String chooseLeft = "1";
    private String chooseMiddle = "0";
    private String chooseRight = "-1";
    private RelativeLayout middleOddsSecondll;
    private RelativeLayout middleOddsFirstll;
    private TextView oddsPlayFirstTitle;
    private TextView oddsPlaySecondTitle;
    //    /**赔率是否可选（无赔率时不可点击）*/
//    private boolean checkOdds = false;

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

        toIssue = (LinearLayout)findViewById(R.id.to_issue);
        toIssue.setOnClickListener(this);

        oddsPlayFirstTitle = (TextView)findViewById(R.id.odds_play_first_title);
        oddsPlaySecondTitle = (TextView)findViewById(R.id.odds_play_second_title);

        issuePlayGrid = (GrapeGridView)findViewById(R.id.betting_issue_play_gridview);
        issuePlayGrid.setNumColumns(3);

        inputImg = (ImageView) findViewById(R.id.input_edit_img);

        tittleEdit = (EditText) findViewById(R.id.betting_issue_tittle_edit);

        detailsEdit = (EditText) findViewById(R.id.betting_issue_details_edit);
        detailsEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(300)});
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
                        detailsEdit.setHint("         最多输入300个字符！");
                    }else{
                        inputImg.setVisibility(View.GONE);
                        detailsEdit.setHint("");
                    }
                }
            }
        });
        jiageEdit = (EditText) findViewById(R.id.betting_issue_jiage_edit);
//        jiageEdit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        issueLeagueName = (TextView) findViewById(R.id.betting_issue_leagueName);
        homeName = (TextView) findViewById(R.id.betting_issue_homename);
        guestName = (TextView) findViewById(R.id.betting_issue_guestname);

        issuePriceCardA = (RadioButton)findViewById(R.id.issue_price_card_a);
        issuePriceCardA.setOnClickListener(this);
        issuePriceCardB = (RadioButton)findViewById(R.id.issue_price_card_b);
        issuePriceCardB.setOnClickListener(this);
        issuePriceCardC = (RadioButton)findViewById(R.id.issue_price_card_c);
        issuePriceCardC.setOnClickListener(this);
        issuePriceCardD = (RadioButton)findViewById(R.id.issue_price_card_d);
        issuePriceCardD.setOnClickListener(this);

        issuePriceCardA.setChecked(false);
        issuePriceCardB.setChecked(false);
        issuePriceCardC.setChecked(false);
        issuePriceCardD.setChecked(false);

        jiageEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    jiageEdit.setCursorVisible(true);// 再次点击显示光标
                }
                issuePriceCardA.setChecked(false);
                issuePriceCardB.setChecked(false);
                issuePriceCardC.setChecked(false);
                issuePriceCardD.setChecked(false);
                return false;
            }
        });

        playOddsFirst = (LinearLayout)findViewById(R.id.betting_issue_play_odds_first);
        playOddsSecond = (LinearLayout)findViewById(R.id.betting_issue_play_odds_second);
        playOddsFirst.setVisibility(View.GONE);
        playOddsSecond.setVisibility(View.GONE);


        playLeftImgA = (ImageView)findViewById(R.id.issue_play_left_img_a);
        playMiddleImgA = (ImageView)findViewById(R.id.issue_play_middle_img_a);
        playRightImgA = (ImageView)findViewById(R.id.issue_play_right_img_a);
        playLeftA = (RadioButton)findViewById(R.id.issue_play_left_a);
        playMiddleA = (RadioButton)findViewById(R.id.issue_play_middle_a);
        playRightA = (RadioButton)findViewById(R.id.issue_play_right_a);
        playLeftA.setChecked(false);
        playMiddleA.setChecked(false);
        playRightA.setChecked(false);


        playLeftImgB = (ImageView)findViewById(R.id.issue_play_left_img_b);
        playMiddleImgB = (ImageView)findViewById(R.id.issue_play_middle_img_b);
        playRightImgB = (ImageView)findViewById(R.id.issue_play_right_img_b);
        playLeftB = (RadioButton)findViewById(R.id.issue_play_left_b);
        playMiddleB = (RadioButton)findViewById(R.id.issue_play_middle_b);
        playRightB = (RadioButton)findViewById(R.id.issue_play_right_b);
        playLeftB.setChecked(false);
        playMiddleB.setChecked(false);
        playRightB.setChecked(false);

        playLeftA.setOnClickListener(this);
        playMiddleA.setOnClickListener(this);
        playRightA.setOnClickListener(this);
        playLeftB.setOnClickListener(this);
        playMiddleB.setOnClickListener(this);
        playRightB.setOnClickListener(this);

        middleOddsFirstll = (RelativeLayout)findViewById(R.id.issue_middle_odds_first);
        middleOddsSecondll = (RelativeLayout)findViewById(R.id.issue_middle_odds_second);

        L.d("qwer===>>rr " , playLeftA.isChecked() + "");
    }

    public void initData(){
        mMatchId = getIntent().getStringExtra("matchId");

//        String url = "http://192.168.10.242:8098/promotion/info/getpromotiontype";
        String url = BaseURLs.URI_BETTING_ISSUE_PLAY;

        String userid = AppConstants.register.getUser() == null ? "" : AppConstants.register.getUser().getUserId();
        String token = AppConstants.register.getToken();

        Map<String ,String> mapPrament = new HashMap<>();
        mapPrament.put(PARAM_USER_ID , userid);
        mapPrament.put(PARAM_MATCH_ID , mMatchId);
        mapPrament.put(PARAM_LOGIN_TOKEN , token); //logintoken
        mapPrament.put(PARAM_LANG , MyApp.getLanguage());
        mapPrament.put(PARAM_TIMEZONE , AppConstants.timeZone + "");
        String signs = SignUtils.getSign(BaseURLs.PARAMENT_ISSUE_PLAY, mapPrament);

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
                        //TODO==============================
                        BettingIssueFabuPalyBean.PromotionTypeVo playOddsData = jsonBean.getData();
                        issueLeagueName.setText(filtrateNull(playOddsData.getLeagueName()));
                        homeName.setText(filtrateNull(playOddsData.getHomeTeam()));
                        guestName.setText(filtrateNull(playOddsData.getGuestTeam()));

                        leagueId = playOddsData.getLeagueId();//获得联赛id

                        if (playOddsData.getPromotionTypeList() != null) {

                            listVo = playOddsData.getPromotionTypeList();

                            BettingIssueFabuPalyBean.PromotionTypeVo.PromotionTypeListVo datafirst = listVo.get(0);

                            currPlay = datafirst.getTypeName();
                            prositionPlay = 0;

                            setIssueChickListener();
                            mIssuePlayAdapter = new BettingIssueAdapter(getApplicationContext() , listVo,filtrateNull(datafirst.getTypeName()) , R.layout.betting_issue_play_item);
                            mIssuePlayAdapter.setIssueClickChangeListener(issueClickChangeListener);
                            issuePlayGrid.setAdapter(mIssuePlayAdapter);

                            if (datafirst.getOddsList() != null && datafirst.getOddsList().size() != 0) {

//                                if (datafirst.getTypeName().equals("竞彩单关")) {
                                if (datafirst.getOddsList().size() == 2) {
                                    playOddsSecond.setVisibility(View.VISIBLE);
                                }else{
                                    playOddsSecond.setVisibility(View.GONE);
                                }

                                playOddsFirst.setVisibility(View.VISIBLE);

                                switch (datafirst.getOddsList().size()){
                                    case 1:
                                        setPlayOdds(datafirst.getOddsList().get(0), null);
                                        break;
                                    case 2:
                                        setPlayOdds(datafirst.getOddsList().get(0), datafirst.getOddsList().get(1));
                                        break;
                                }
                            }
                        }

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

    private BettingIssueAdapter.IssueClickChangeListener issueClickChangeListener;
    private void setIssueChickListener(){
        issueClickChangeListener = new BettingIssueAdapter.IssueClickChangeListener() {
            @Override
            public void onClick(CompoundButton buttonView, String typeName , int prosition) {

//                if (typeName.equals("竞彩单关")) {
//                    playOddsSecond.setVisibility(View.VISIBLE);
//                }else{
//                    playOddsSecond.setVisibility(View.GONE);
//                }
                for (BettingIssueFabuPalyBean.PromotionTypeVo.PromotionTypeListVo voData : listVo){
                    if (voData.getTypeName().equals(typeName)) {
                        if (voData.getOddsList() != null){
                            switch (voData.getOddsList().size()){
                                case 1:
                                    playOddsSecond.setVisibility(View.GONE);
                                    setPlayOdds(voData.getOddsList().get(0), null);
                                    break;
                                case 2:
                                    playOddsSecond.setVisibility(View.VISIBLE);
                                    setPlayOdds(voData.getOddsList().get(0), voData.getOddsList().get(1));
                                    break;
                            }
                        }
                    }
                }

                /**
                 * 切换玩法时复位
                 */
                if (!currPlay.equals(typeName)) {
                    rootCurrView();
                }
                currPlay = typeName;
                prositionPlay = prosition;
                mIssuePlayAdapter.notifyDataSetChanged();
            }
        };
    }

    /**玩法是否可选两个(是否竞彩单关)*/
    private boolean middleFiest = false;
    private boolean middleSecond = false;

    private void setPlayOdds(BettingIssueFabuPalyBean.PromotionTypeVo.PromotionTypeListVo.PromotionOddsVo  oddsVoFirst , BettingIssueFabuPalyBean.PromotionTypeVo.PromotionTypeListVo.PromotionOddsVo  oddsVoSecond){

        if (oddsVoFirst != null) {
            playLeftA.setText(filtrateNull(oddsVoFirst.getLeftTitle()) + " (" + filtrateNull(oddsVoFirst.getLeftOdds()) + ")");
            playMiddleA.setText(filtrateNull(oddsVoFirst.getMidTitle()) + " (" + filtrateNull(oddsVoFirst.getMidOdds()) + ")");
            playRightA.setText(filtrateNull(oddsVoFirst.getRightTitle()) + " (" + filtrateNull(oddsVoFirst.getRightOdds()) + ")");
            oddsPlayFirstTitle.setText(filtrateNull(oddsVoFirst.getTitle()));
            if (oddsVoFirst.getMidTitle() == null || oddsVoFirst.getMidTitle().equals("")) {
                middleOddsFirstll.setVisibility(View.GONE);
                middleFiest = true;
            }else{
                middleOddsFirstll.setVisibility(View.VISIBLE);
                middleFiest = false;
            }
        }
        if (oddsVoSecond != null) {
            playLeftB.setText(filtrateNull(oddsVoSecond.getLeftTitle()) + " (" + filtrateNull(oddsVoSecond.getLeftOdds()) + ")");
            playMiddleB.setText(filtrateNull(oddsVoSecond.getMidTitle()) + " (" + filtrateNull(oddsVoSecond.getMidOdds()) + ")");
            playRightB.setText(filtrateNull(oddsVoSecond.getRightTitle()) + " (" + filtrateNull(oddsVoSecond.getRightOdds()) + ")");
            oddsPlaySecondTitle.setText(filtrateNull(oddsVoSecond.getTitle()));

            if (oddsVoSecond.getMidTitle() == null || oddsVoSecond.getMidTitle().equals("")) {
                middleOddsSecondll.setVisibility(View.GONE);
                middleSecond = true;
            }else{
                middleOddsSecondll.setVisibility(View.VISIBLE);
                middleSecond = false;
            }
        }

    }

    /**竞彩单关时 上下两组赔率只能单选*/
    private boolean oddsFirstCheck = true;
    private boolean oddsSecondCheck = true;


    private boolean otherOddsPlayFirst = true;
    private boolean otherOddsPlaySecond = true;
    private boolean oddsLeftA = false;
    private boolean oddsRightA = false;
    private boolean oddsLeftB = false;
    private boolean oddsRightB = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_img_back:
                finish();
                overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.to_issue:
//                Toast.makeText(this, "to_issue", Toast.LENGTH_SHORT).show();
                issueCheck();
                break;
            case R.id.issue_play_left_a:

                if (middleFiest) {
                    if (otherOddsPlayFirst) {
                        if (oddsLeftA) {
                            playLeftA.setChecked(false);
                            playLeftImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                            setOddsData(null , chooseLeft , "1" , false);
                        }else{
                            playLeftA.setChecked(true);
                            playLeftImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.checked_blue));

                            if (listVo != null && listVo.size() != 0) {
                                setOddsData(listVo.get(prositionPlay).getOddsList().get(0),chooseLeft ,"1" ,true);
                            }
                            if (oddsRightA) {
                                playRightA.setChecked(false);
                                playRightImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                                oddsRightA = !oddsRightA;
                            }
                        }
                        oddsLeftA = !oddsLeftA;

                        if (oddsLeftA || oddsRightA) {
                            otherOddsPlaySecond = false;
                        }else{
                            otherOddsPlaySecond = true;
                        }
                    }else{
                        playLeftA.setChecked(false);
                        playLeftImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                    }
                }else{
                    if (oddsFirstCheck) {
                        if(playMiddleCheckA && playRightCheckA){
                            Toast.makeText(this, "最多选择两项a", Toast.LENGTH_SHORT).show();
                            playLeftA.setChecked(false);
                            playLeftImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                        }else{
                            if (playLeftCheckA) {
                                playLeftA.setChecked(false);
                                playLeftImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));

                                setOddsData(null , chooseLeft , "0" , false);
                            }else{
                                playLeftA.setChecked(true);
                                playLeftImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.checked_blue));

                                if (listVo != null && listVo.size() != 0) {
                                    setOddsData(listVo.get(prositionPlay).getOddsList().get(0),chooseLeft ,"0" ,true);
                                }
//                        else{
//                            setOddsData(null,chooseLeft ,"0" ,true);
//                        }
                            }
                            playLeftCheckA = !playLeftCheckA;
                        }

                        if (playLeftCheckA || playMiddleCheckA || playRightCheckA) {
                            oddsSecondCheck = false;
                        }else{
                            oddsSecondCheck = true;
                        }
                    }else{
                        playLeftA.setChecked(false);
                        playLeftImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                    }
                }

                break;
            case R.id.issue_play_middle_a:

                if (oddsFirstCheck) {

                    if (playLeftCheckA && playRightCheckA) {
                        Toast.makeText(this, "最多选择两项a", Toast.LENGTH_SHORT).show();
                        playMiddleA.setChecked(false);
                        playMiddleImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                    }else{
                        if (playMiddleCheckA) {
                            playMiddleA.setChecked(false);
                            playMiddleImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                            setOddsData(null,chooseMiddle ,"0" ,false);
                        }else{
                            playMiddleA.setChecked(true);
                            playMiddleImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.checked_blue));

                            if (listVo != null && listVo.size() != 0) {
                                setOddsData(listVo.get(prositionPlay).getOddsList().get(0),chooseMiddle ,"0" ,true);
                            }
//                        else{
//                            setOddsData(null,chooseMiddle ,"0" ,true);
//                        }
                        }
                        playMiddleCheckA = !playMiddleCheckA;
                    }
                    if (playLeftCheckA || playMiddleCheckA || playRightCheckA) {
                        oddsSecondCheck = false;
                    }else{
                        oddsSecondCheck = true;
                    }
                }else{
                    playMiddleA.setChecked(false);
                    playMiddleImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                }
                break;
            case R.id.issue_play_right_a:
//                L.d("qwer1128==>>right_start" , playLeftCheckA + " == " +playMiddleCheckA + " == " + playRightCheckA);
                if (middleFiest) {
                    if (otherOddsPlayFirst) {
                        if (oddsRightA) {
                            playRightA.setChecked(false);
                            playRightImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                            setOddsData(null , chooseRight , "1" , false);
                        }else{
                            playRightA.setChecked(true);
                            playRightImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.checked_blue));
                            if (listVo != null && listVo.size() != 0) {
                                setOddsData(listVo.get(prositionPlay).getOddsList().get(0),chooseRight ,"1" ,true);
                            }
                            if (oddsLeftA) {
                                playLeftA.setChecked(false);
                                playLeftImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                                oddsLeftA = !oddsLeftA;
                            }
                        }
                        oddsRightA = !oddsRightA;

                        if (oddsLeftA || oddsRightA) {
                            otherOddsPlaySecond = false;
                        }else{
                            otherOddsPlaySecond = true;
                        }
                    }else{
                        playRightA.setChecked(false);
                        playRightImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                    }
                }else{

                    if (oddsFirstCheck) {

                        if (playLeftCheckA && playMiddleCheckA) {
                            Toast.makeText(this, "最多选择两项a", Toast.LENGTH_SHORT).show();
                            playRightA.setChecked(false);
                            playRightImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                        }else{
                            if (playRightCheckA) {
                                playRightA.setChecked(false);
                                playRightImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                                setOddsData(null , chooseRight , "0" , false);
                            }else{
                                playRightA.setChecked(true);
                                playRightImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.checked_blue));
                                if (listVo != null && listVo.size() != 0) {
                                    setOddsData(listVo.get(prositionPlay).getOddsList().get(0),chooseRight ,"0" ,true);
                                }
//                        else{
//                            setOddsData(null,chooseRight ,"0" ,true);
//                        }
                            }
                            playRightCheckA = !playRightCheckA;
                        }
                        if (playLeftCheckA || playMiddleCheckA || playRightCheckA) {
                            oddsSecondCheck = false;
                        }else{
                            oddsSecondCheck = true;
                        }
                    }else{
                        playRightA.setChecked(false);
                        playRightImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                    }
                }
//                L.d("qwer1128==>>right_end" , playLeftCheckA + " == " +playMiddleCheckA + " == " + playRightCheckA);
                break;

            case R.id.issue_play_left_b:

                if (middleSecond) {
                    if (otherOddsPlaySecond) {
                        if (oddsLeftB) {
                            playLeftB.setChecked(false);
                            playLeftImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                            setOddsData(null , chooseLeft , "1" , false);
                        }else{
                            playLeftB.setChecked(true);
                            playLeftImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.checked_blue));
                            if (listVo != null && listVo.size() != 0) {
                                setOddsData(listVo.get(prositionPlay).getOddsList().get(1),chooseLeft ,"1" ,true);
                            }
                            if (oddsRightB) {
                                playRightB.setChecked(false);
                                playRightImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                                oddsRightB = !oddsRightB;
                            }
                        }
                        oddsLeftB = !oddsLeftB;
                        if (oddsLeftB || oddsRightB) {
                            otherOddsPlayFirst = false;
                        }else{
                            otherOddsPlayFirst = true;
                        }
                    }else{
                        playLeftB.setChecked(false);
                        playLeftImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                    }
                }else{

                    if (oddsSecondCheck) {

                        if (playMiddleCheckB && playRightCheckB) {
                            Toast.makeText(this, "最多选择两项b", Toast.LENGTH_SHORT).show();
                            playLeftB.setChecked(false);
                            playLeftImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                        }else {
                            if (playLeftCheckB) {
                                playLeftB.setChecked(false);
                                playLeftImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                                setOddsData(null , chooseLeft , "0" , false);
                            }else{
                                playLeftB.setChecked(true);
                                playLeftImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.checked_blue));
                                if (listVo != null && listVo.size() != 0) {
                                    setOddsData(listVo.get(prositionPlay).getOddsList().get(1),chooseLeft ,"0" ,true);
                                }
//                        else{
//                            setOddsData(null,chooseLeft ,"1" ,true);
//                        }
                            }
                            playLeftCheckB = !playLeftCheckB;
                        }
                        if (playLeftCheckB || playMiddleCheckB || playRightCheckB) {
                            oddsFirstCheck = false;
                        }else{
                            oddsFirstCheck = true;
                        }
                    }else{
                        playLeftB.setChecked(false);
                        playLeftImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                    }
                }
                break;
            case R.id.issue_play_middle_b:
                if (oddsSecondCheck) {

                    if (playLeftCheckB && playRightCheckB) {
                        Toast.makeText(this, "最多选择两项b", Toast.LENGTH_SHORT).show();
                        playMiddleB.setChecked(false);
                        playMiddleImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                    }else{
                        if (playMiddleCheckB) {
                            playMiddleB.setChecked(false);
                            playMiddleImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                            setOddsData(null , chooseMiddle , "0" , false);
                        }else{
                            playMiddleB.setChecked(true);
                            playMiddleImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.checked_blue));
                            if (listVo != null && listVo.size() != 0) {
                                setOddsData(listVo.get(prositionPlay).getOddsList().get(1),chooseMiddle ,"0" ,true);
                            }
//                        else{
//                            setOddsData(null,chooseMiddle ,"1" ,true);
//                        }
                        }
                        playMiddleCheckB = !playMiddleCheckB;
                    }
                    if (playLeftCheckB || playMiddleCheckB || playRightCheckB) {
                        oddsFirstCheck = false;
                    }else{
                        oddsFirstCheck = true;
                    }
                }else{
                    playMiddleB.setChecked(false);
                    playMiddleImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                }
                break;
            case R.id.issue_play_right_b:
                if (middleSecond) {
                    if (otherOddsPlaySecond) {
                        if (oddsRightB) {
                            playRightB.setChecked(false);
                            playRightImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                            setOddsData(null , chooseRight , "1" , false);
                        }else{
                            playRightB.setChecked(true);
                            playRightImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.checked_blue));
                            if (listVo != null && listVo.size() != 0) {
                                setOddsData(listVo.get(prositionPlay).getOddsList().get(1),chooseRight ,"1" ,true);
                            }
                            if (oddsLeftB) {
                                playLeftB.setChecked(false);
                                playLeftImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                                oddsLeftB = !oddsLeftB;
                            }
                        }
                        oddsRightB = !oddsRightB;
                        if (oddsLeftB || oddsRightB) {
                            otherOddsPlayFirst = false;
                        }else{
                            otherOddsPlayFirst = true;
                        }
                    }else{
                        playRightB.setChecked(false);
                        playRightImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                    }
                }else{

                    if (oddsSecondCheck) {

                        if (playLeftCheckB && playMiddleCheckB) {
                            Toast.makeText(this, "最多选择两项b", Toast.LENGTH_SHORT).show();
                            playRightB.setChecked(false);
                            playRightImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                        }else{
                            if (playRightCheckB) {
                                playRightB.setChecked(false);
                                playRightImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                                setOddsData(null , chooseRight , "0" , false);
                            }else{
                                playRightB.setChecked(true);
                                playRightImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.checked_blue));
                                if (listVo != null && listVo.size() != 0) {
                                    setOddsData(listVo.get(prositionPlay).getOddsList().get(1),chooseRight ,"0" ,true);
                                }
//                        else{
//                            setOddsData(null,chooseRight ,"1" ,true);
//                        }
                            }
                            playRightCheckB = !playRightCheckB;
                        }
                        if (playLeftCheckB || playMiddleCheckB || playRightCheckB) {
                            oddsFirstCheck = false;
                        }else{
                            oddsFirstCheck = true;
                        }
                    }else{
                        playRightB.setChecked(false);
                        playRightImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
                    }
                }
                break;
            case R.id.issue_price_card_a:
                issuePriceCardA.setChecked(true);
                issuePriceCardB.setChecked(false);
                issuePriceCardC.setChecked(false);
                issuePriceCardD.setChecked(false);
                jiageEdit.setText("10");
                jiageEdit.setSelection(jiageEdit.getText().length());
                break;
            case R.id.issue_price_card_b:
                issuePriceCardA.setChecked(false);
                issuePriceCardB.setChecked(true);
                issuePriceCardC.setChecked(false);
                issuePriceCardD.setChecked(false);
                jiageEdit.setText("20");
                jiageEdit.setSelection(jiageEdit.getText().length());
                break;
            case R.id.issue_price_card_c:
                issuePriceCardA.setChecked(false);
                issuePriceCardB.setChecked(false);
                issuePriceCardC.setChecked(true);
                issuePriceCardD.setChecked(false);
                jiageEdit.setText("50");
                jiageEdit.setSelection(jiageEdit.getText().length());
                break;
            case R.id.issue_price_card_d:
                issuePriceCardA.setChecked(false);
                issuePriceCardB.setChecked(false);
                issuePriceCardC.setChecked(false);
                issuePriceCardD.setChecked(true);
                jiageEdit.setText("100");
                jiageEdit.setSelection(jiageEdit.getText().length());
                break;

        }
    }

    private String filtrateNull(String str){

        if (str == null) {
            return "-";
        }else{
            return str;
        }
    }

    /**
     * 切换玩法时回复未选中状态
     */
    private void rootCurrView(){
        playLeftA.setChecked(false);
        playLeftImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
        playLeftB.setChecked(false);
        playLeftImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));

        playMiddleA.setChecked(false);
        playMiddleImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
        playMiddleB.setChecked(false);
        playMiddleImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));

        playRightA.setChecked(false);
        playRightImgA.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
        playRightB.setChecked(false);
        playRightImgB.setBackground(getApplicationContext().getResources().getDrawable(R.mipmap.unchecked_grey));
    }

    private List<String> chooseList = new ArrayList<>();

    /**
     *
     * @param oddsVo
     * @param chooseData
     * @param type 是否是竞彩单关（可否多选赔率） 0 是 1 不是
     * @param addChoose 赔率选中或者取消，true为选中
     */
    private void setOddsData(BettingIssueFabuPalyBean.PromotionTypeVo.PromotionTypeListVo.PromotionOddsVo oddsVo ,
                             String chooseData , String type , boolean addChoose){

        if (oddsVo != null) {
            this.type = oddsVo.getType();
            this.oddsId = oddsVo.getOddsId();
        }
        mChoose="";
        mChoose1="";
        switch (type){
            case "0":
                //竞彩单关 choose1会有值
                if (addChoose) {
                    chooseList.add(chooseData);
                }else{
                    if (chooseList.contains(chooseData)) {
                        chooseList.remove(chooseData);
                    }
                }
                if (chooseList.size() == 1) {

                    mChoose = chooseList.get(0);
                }else if(chooseList.size() == 2){

                    mChoose = chooseList.get(0);
                    mChoose1 = chooseList.get(1);
                }
                break;
            case "1":
                if (addChoose) {
                    mChoose = chooseData;
                }else{
                    mChoose = "";
                }
                break;
        }

//        switch (type){
//            case "0":
//                if (addChoose) {
//                    chooseList.add(chooseData);
//                }else{
//                    if (chooseList.contains(chooseData)) {
//                        chooseList.remove(chooseData);
//                    }
//                }
//
//                StringBuffer sb = new StringBuffer();
//                for (String choose : chooseList) {
//                    if (sb.length() == 0) {
//                        sb.append(choose);
//                    }else{
//                        sb.append("," + choose);
//                    }
//                }
//
//                mChoose = sb.toString();
//                break;
//            case "1":
//                if (addChoose) {
//                    choose1List.add(chooseData);
//                }else{
//                    if (choose1List.contains(chooseData)) {
//                        choose1List.remove(chooseData);
//                    }
//                }
//                StringBuffer sb1 = new StringBuffer();
//                for (String choose : choose1List) {
//                    if (sb1.length() == 0) {
//                        sb1.append(choose);
//                    }else{
//                        sb1.append("," + choose);
//                    }
//                }
//                mChoose1 = sb1.toString();
//                break;
//        }
    }


    private String title = ""; //标题
    private String context = "";//推介理由
    private String price = "";//推介方案价格
    private String leagueId = "";//联赛编号

    private String type = "";// 类型：0竞彩单关 1亚盘 2大小球 3半场亚盘 4半场大小球
    private String oddsId = "";//赔率编号
    private String mChoose = "";//推介选项，左：1，中：0，右：-1
    private String mChoose1 = "";//推介选项，左：1，中：0，右：-1

    private void issueCheck(){

//        String issueUrl = "http://192.168.10.242:8098/promotion/info/publicpromotion";
        String issueUrl = BaseURLs.URI_BETTING_TO_ISSUE;

        String userid = AppConstants.register.getUser() == null ? "" : AppConstants.register.getUser().getUserId();
        String token = AppConstants.register.getToken();

        title = tittleEdit.getText().toString();
        context = detailsEdit.getText().toString();
        price = jiageEdit.getText().toString();

//        title 标题
//        context 推介理由
//        price 推介方案价格
//        choose 推介选项，左：1，中：0，右：-1
//        choose1 推介选项，左：1，中：0，右：-1
//        matchId 比赛编号
//        leagueId 联赛编号
//        type 类型：0竞彩单关 1亚盘 2大小球 3半场亚盘 4半场大小球
//        oddsId 赔率编号

        Map<String ,String> mapPrament = new HashMap<>();
        mapPrament.put(PARAM_USER_ID , userid);
        mapPrament.put(PARAM_LOGIN_TOKEN , token); //logintoken
        mapPrament.put(PARAM_LANG , MyApp.getLanguage());
        mapPrament.put(PARAM_TIMEZONE , AppConstants.timeZone + "");
        mapPrament.put("title" , title);
        mapPrament.put("context" , context);
        mapPrament.put("price" ,price);
        mapPrament.put("choose" ,mChoose);
        mapPrament.put("choose1" ,mChoose1);
        mapPrament.put("matchId" ,mMatchId);
        mapPrament.put("leagueId" ,leagueId);
        mapPrament.put("type" ,type);
        mapPrament.put("oddsId" ,oddsId);
        String signs = SignUtils.getSign(BaseURLs.PARAMENT_TO_ISSUE, mapPrament);

        Map<String ,String> map = new HashMap<>();
        map.put(PARAM_USER_ID , userid);//用户id
        map.put(PARAM_LOGIN_TOKEN , token); //logintoken
        map.put("title" , title);
        map.put("context" , context);
        map.put("price" ,price);
        map.put("choose" ,mChoose);
        map.put("choose1" ,mChoose1);
        map.put("matchId" ,mMatchId);
        map.put("leagueId" ,leagueId);
        map.put("type" ,type);
        map.put("oddsId" ,oddsId);
        map.put(PARAM_SIGN , signs);

        L.d("qwer0620==>> " , "type= " + filtrateNull(type) + " oddsId= " + filtrateNull(oddsId) + " mChoose= " + filtrateNull(mChoose)
                + " mChoose1= "+ filtrateNull(mChoose1) + "========== title= " + filtrateNull(title) + " context= " + filtrateNull(context)
                + " price= " + price + " matchId= " + mMatchId  + " leagueId= " + leagueId);

        VolleyContentFast.requestJsonByPost(issueUrl,map , new VolleyContentFast.ResponseSuccessListener<IssueCodeBean>() {
            @Override
            public void onResponse(IssueCodeBean jsonBean) {
                if (jsonBean.getCode() == 200) {
                    L.d("qwerqwer == >" , "发布成功");
                    Toast.makeText(MvpBettingIssueDetailsActivity.this, "发布成功!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    switch (jsonBean.getCode()){
                        case 3012:
                            Toast.makeText(MvpBettingIssueDetailsActivity.this, "比赛开始前五分钟不可发布", Toast.LENGTH_SHORT).show();
                            break;
                        case 3013:
                            Toast.makeText(MvpBettingIssueDetailsActivity.this, "该用户的该种玩法已经发布过", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    L.d("qwerqwer == >" , "发布失败");
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                L.d("qwerqwer == >" , "访问失败");
            }
        },IssueCodeBean.class);
    }
}