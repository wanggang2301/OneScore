package com.hhly.mlottery.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.adapter.ChoseLeagueAdapter;
import com.hhly.mlottery.util.UiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yuely198 on 2017/6/1.
 * 申请专家页面
 */

public class ApplicationSpecialistActivity  extends  BaseActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{


    private TextView public_txt_title;
    private ImageView set_rd_alet;
    boolean isChecked=false;
    private EditText specalist_edittext;
    private ImageView specialist_pen;
    private EditText real_name;
    private LinearLayout specalist_error_tv;
    private TextView specalist_tv;
    private LinearLayout error_prompt;
    private EditText good_league;
    private TextView tv_comfirm;
    private GridView gridview;
    private ChoseLeagueAdapter choseLeagueAdapter;
    private List<String> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

    }



    private void initView() {

        setContentView(R.layout.activity_application_specialist);

        public_txt_title = (TextView) findViewById(R.id.public_txt_title);

        public_txt_title.setText("申请专家");

        findViewById(R.id.public_img_back).setOnClickListener(this);
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);





        gridview = (GridView) findViewById(R.id.gridview);

        set_rd_alet = (ImageView) findViewById(R.id.set_rd_alet);
        set_rd_alet.setOnClickListener(this);
         //真实姓名
        real_name = (EditText) findViewById(R.id.real_name);

        //擅长联赛
        good_league = (EditText) findViewById(R.id.good_league);
        //确认按钮
        tv_comfirm = (TextView) findViewById(R.id.tv_comfirm);
        tv_comfirm.setOnClickListener(this);
        //错误信息
        specalist_error_tv = (LinearLayout) findViewById(R.id.specalist_error_tv);
        //超出限制提示
        error_prompt = (LinearLayout) findViewById(R.id.error_prompt);

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
            private int editStart ;
            private int editEnd ;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                temp=charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                editStart=specalist_edittext.getSelectionStart();
                editEnd=specalist_edittext.getSelectionEnd();
                specalist_tv.setText(temp.length()+"/1000");
                if(temp.length()>1000){
                    error_prompt.setVisibility(View.VISIBLE);
                    specalist_tv.setTextColor(getResources().getColor(R.color.foot_team_name_score3));
                }else{
                    error_prompt.setVisibility(View.GONE);
                    specalist_tv.setTextColor(getResources().getColor(R.color.snooker_line));
                }

            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.public_img_back:
                finish();
                break;
            case R.id.set_rd_alet:

                if (isChecked){
                    set_rd_alet.setImageResource(R.mipmap.chose);
                    isChecked=false;
                }else{
                    isChecked=true;
                    set_rd_alet.setImageResource(R.mipmap.chosed);

                }


                break;
            case R.id.tv_comfirm:
                SplitString(good_league.getText().toString());

                break;



            default:
                break;



        }

    }

    private void SplitString(String text) {
        datas = new ArrayList<>();

        String[] str = text.split("，");

        for (int i = 0; i < str.length; i++) {
            datas.add(str[i]);
            //System.out.println(str[i]);
        }

        choseLeagueAdapter = new ChoseLeagueAdapter(this, datas, R.layout.choseleauge_item);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                datas.remove(i);
                choseLeagueAdapter.notifyDataSetChanged();
            }
        });
        gridview.setAdapter(choseLeagueAdapter);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

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
