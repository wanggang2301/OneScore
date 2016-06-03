package com.hhly.mlottery.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.account.SendSmsCode;
import com.hhly.mlottery.impl.GetVerifyCodeCallBack;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.CountDown;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.net.account.OperateType;
import com.umeng.analytics.MobclickAgent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 找回密码界面
 */
public class FindPassWordActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private static final java.lang.String TAG = "FindPassWordActivity";
    public static final java.lang.String PHONE = "phone";
    public static final java.lang.String VERIFYCODE = "verifycode";
    private static final int RESET_PW = 100;
    private EditText et_username , et_verifycode;
    private TextView tv_nextstep , tv_verycode;
    private ImageView iv_delete;

    /**
     * 倒计时 默认60s , 间隔1s
     */
    private CountDown countDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass_word);

        initView();
        
    }

    @Override
    protected void onResume() {
        /**友盟页面统计*/
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(TAG);
        super.onResume();
        et_username.setFocusable(true);
        et_username.setFocusableInTouchMode(true);
        et_username.requestFocus();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() { //让软键盘延时弹出，以更好的加载Activity
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) et_username.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_username, 0);
            }

        }, 300);
    }

    private void initView() {

        countDown = CountDown.getDefault(new CountDown.CountDownCallback() {
            @Override
            public void onFinish() {
                enableVeryCode();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                if (tv_verycode != null){
                    if (tv_verycode.isClickable())
                        tv_verycode.setClickable(false);

                    L.d(TAG,millisUntilFinished/ CountDown.TIMEOUT_INTERVEL + "秒");
                    tv_verycode.setText(millisUntilFinished/ CountDown.TIMEOUT_INTERVEL + "秒");
                }
            }
        });

        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.public_txt_title)).setText(R.string.find_pw);

        tv_nextstep = (TextView) findViewById(R.id.tv_nextstep);
        tv_nextstep.setOnClickListener(this);
        findViewById(R.id.public_img_back).setOnClickListener(this);

        et_username = (EditText) findViewById(R.id.et_username);
        et_username.addTextChangedListener(this);

        et_verifycode = (EditText) findViewById(R.id.et_verifycode);
        tv_verycode = (TextView) findViewById(R.id.tv_verycode);
        tv_verycode.setOnClickListener(this);

        iv_delete = (ImageView) findViewById(R.id.iv_delete);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.public_img_back: // 返回
                MobclickAgent.onEvent(mContext, "FindPassWordActivity_Exit");
                finish();
                break;
            case R.id.iv_delete: // EditText 删除
                MobclickAgent.onEvent(mContext, "FindPassWordActivity_UserName_Delete");
                et_username.setText("");
                break;
            case R.id.tv_verycode: // 获取验证码
                MobclickAgent.onEvent(mContext, "FindPassWordActivity_VeryCode");
                getVerifyCode();
                break;
            case R.id.tv_nextstep: // 下一步
                MobclickAgent.onEvent(mContext, "FindPassWordActivity_NextStep");
                nextStep();

                break;

            default:
                break;
        }
    }

    /**
     * 下一步
     */
    private void nextStep() {

        String phone = et_username.getText().toString();
        String verifycode = et_verifycode.getText().toString();

        if (UiUtils.isMobileNO(this , phone)){
            if (UiUtils.checkVerifyCode(this , verifycode)){
                Intent i = new Intent(this , ResetPasswordActivity.class);
                i.putExtra(PHONE , phone);
                i.putExtra(VERIFYCODE , verifycode);
                startActivityForResult(i , RESET_PW);
            }
        }


    }

    private void getVerifyCode() {
        String phone = et_username.getText().toString();
        CommonUtils.getVerifyCode(this, phone, OperateType.TYPE_FORGET_PASSWORD ,new GetVerifyCodeCallBack() {
            @Override
            public void beforGet() {
                countDown.start();
            }

            @Override
            public void onGetResponce(SendSmsCode code) {
                resetCountDown();
            }

            @Override
            public void onGetError(VolleyContentFast.VolleyException exception) {
                resetCountDown();
            }
        });
    }

    private void resetCountDown() {
        cancelCountDown();
        enableVeryCode();
    }

    private void cancelCountDown(){
        if (countDown != null){
            countDown.cancel();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)){
            iv_delete.setVisibility(View.GONE);
        }else{
            iv_delete.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 重新获取验证码
     */
    private void enableVeryCode() {
        if (tv_verycode != null){
            tv_verycode.setText(R.string.resend);
            tv_verycode.setClickable(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_OK){
            switch (requestCode){
                case RESET_PW:
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    }
}
