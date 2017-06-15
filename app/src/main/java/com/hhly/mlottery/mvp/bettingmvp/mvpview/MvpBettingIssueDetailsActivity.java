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

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BaseActivity;

/**
 * Created by：XQyi on 2017/5/31 16:08
 * Use: 推介发布页面（view）
 */
public class MvpBettingIssueDetailsActivity extends Activity implements View.OnClickListener {

    private ImageView mBack;
    private ImageView inputImg;
    private EditText tittleEdit;
    private EditText detailsEdit;
    private EditText jiageEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.betting_issue_activity);

        initView();
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