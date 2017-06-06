package com.hhly.mlottery.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hhly.mlottery.R;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.umeng.analytics.MobclickAgent;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;


/**
 * @author chenml
 * @Desc 用户反馈界面
 */
public class FeedbackActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "FeedbackActivity";
    private final int ED_MAX_LENGHT = 200;

    private Button submitBtn;
    private EditText contentEt;
    private TextView textLenghtTv;

    private ProgressDialog progressDialog;


    private final String reg = "^([a-z]|[A-Z]|[0-9]|[\u2E80-\u9FFF]){3,}|@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?|[wap.]{4}|[www.]{4}|[blog.]{5}|[bbs.]{4}|[.com]{4}|[.cn]{3}|[.net]{4}|[.org]{4}|[http://]{7}|[ftp://]{6}$";

    private Pattern pattern = Pattern.compile(reg);
    //输入表情前的光标位置
    private int cursorPos;
    //输入表情前EditText中的文本
    private String tmp;
    //是否重置了EditText的内容
    private boolean resetText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        $(R.id.public_btn_set).setVisibility(View.GONE);
        $(R.id.public_btn_filter).setVisibility(View.GONE);
        $(R.id.public_img_back).setOnClickListener(this);

        TextView titleTv = $(R.id.public_txt_title);
        titleTv.setText(R.string.user_feedback);


        submitBtn = $(R.id.public_btn_save);

        submitBtn.setVisibility(View.VISIBLE);
        submitBtn.setText(R.string.feedback_submit);
        submitBtn.setOnClickListener(this);

        submitBtn.setTextColor(getResources().getColor(R.color.content_txt_light_grad));
        submitBtn.setEnabled(false);

        contentEt = $(R.id.feedback_content_ed);
        textLenghtTv = $(R.id.text_lenght_tv);


        textLenghtTv.setText("0/" + ED_MAX_LENGHT);

        contentEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                L.d(TAG, " beforeTextChanged ");
                L.d(TAG, "s = " + s);
                L.d(TAG, "start = " + start);
                L.d(TAG, "after = " + after);
                L.d(TAG, "count = " + count);
                if (!resetText) {
                    cursorPos = contentEt.getSelectionEnd();
                    tmp = s.toString();//这里用s.toString()而不直接用s是因为如果用s，那么，tmp和s在内存中指向的是同一个地址，s改变了，tmp也就改变了，那么表情过滤就失败了
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                L.d(TAG, " onTextChanged ");
                L.d(TAG, "s = " + s);
                L.d(TAG, "start = " + start);
                L.d(TAG, "before = " + before);
                L.d(TAG, "count = " + count);

                if (s.length() > 0) {
                    submitBtn.setEnabled(true);
                    submitBtn.setTextColor(getResources().getColor(R.color.white));
                } else {
                    submitBtn.setEnabled(false);
                    submitBtn.setTextColor(getResources().getColor(R.color.content_txt_light_grad));
                }
                contentEt.getText().toString().length();
                textLenghtTv.setText(contentEt.getText().toString().length() + "/" + ED_MAX_LENGHT);
            }

            @Override
            public void afterTextChanged(Editable s) {
                L.d(TAG, " afterTextChanged ");
                L.d(TAG, "s.length = " + s.length());
            }
        });


        contentEt.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

//        contentEt.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);


        progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle(R.string.loading_txt);

        progressDialog.setMessage(getResources().getString(R.string.loading_txt));

    }


    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
//        MobclickAgent.onPageStart("FeedbackActivity");
        //进入界面弹出键盘，输入域获取焦点。
        contentEt.setFocusable(true);
        contentEt.setFocusableInTouchMode(true);
        contentEt.requestFocus();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() { //让软键盘延时弹出，以更好的加载Activity
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) contentEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(contentEt, 0);
            }

        }, 300);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.public_img_back:
                finish();
                MobclickAgent.onEvent(mContext,"UserFeedback_Exit");
                break;
            case R.id.public_btn_save:
                MobclickAgent.onEvent(mContext,"UserFeedback_Save");
                submitBtn.setEnabled(false);
                progressDialog.show();
                try {
                    String id = DeviceInfo.getDeviceId(getApplicationContext());
                    String facturer = DeviceInfo.getManufacturer();
                    String model = DeviceInfo.getModel();
                    String systemVersion = DeviceInfo.getOSVersion();
                    L.d(TAG, "id = " + id);
                    L.d(TAG, "facturer = " + facturer);
                    L.d(TAG, "data.model = " + model);
                    L.d(TAG, "systemVersion = " + systemVersion);
                    String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                    L.d(TAG, "versionName = " + versionName);


                    Calendar calendar = Calendar.getInstance(Locale.CHINA);


                    String currentTime = DateFormat.format("yyyy-MM-dd HH:mm:ss", calendar.getTime()).toString();
                    L.d(TAG, "currentTime = " + currentTime);

//                    String url = "http://192.168.31.48:8888/mlottery/core/feedback.addFeedBack.do";

                    Map<String, String> params = new HashMap<>();
                    params.put("osName", "android");
                    params.put("osVersion", systemVersion);
                    params.put("deviceBrand", facturer);
                    params.put("deviceModel", model);
                    params.put("sendTime", currentTime);
                    params.put("content", contentEt.getText().toString());
                    params.put("deviceToken", id);
                    params.put("appVersion", versionName);
                    params.put("userId", PreferenceUtil.getString(AppConstants.SPKEY_USERID, ""));
                    if(AppConstants.register != null && AppConstants.register != null && AppConstants.register.getUser() != null){
                        params.put("nickName",AppConstants.register.getUser().getNickName());
                        params.put("userImg",AppConstants.register.getUser().getImageSrc());
                    }

                    VolleyContentFast.requestStringByPost(BaseURLs.URL_FEEDBACK_ADD, params, new VolleyContentFast.ResponseSuccessListener<String>() {
                        @Override
                        public void onResponse(String jsonString) {

                            JSONObject jsonObject = JSON.parseObject(jsonString);
                            String result = jsonObject.getString("result");

                            if ("200".equals(result)) {
                                progressDialog.cancel();
                                Toast.makeText(getApplicationContext(), R.string.feedback_toast_thx, Toast.LENGTH_LONG).show();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 300);
                            }

                        }
                    }, new VolleyContentFast.ResponseErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                            Toast.makeText(getApplicationContext(), R.string.exp_net_status_txt, Toast.LENGTH_LONG).show();
                            submitBtn.setEnabled(true);
                            progressDialog.cancel();
                        }
                    });
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public static class EmojiFilter {

        //检测是否有emoji字符
        public static boolean containsEmoji(String source) {
            if (source.equals(null)) {
                return false;
            }
            int len = source.length();
            for (int i = 0; i < len; i++) {
                char codePoint = source.charAt(i);
                if (isEmojiCharacter(codePoint)) {
                    return true;
                }
            }
            return false;
        }

        private static boolean isEmojiCharacter(char codePoint) {
            return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                    || (codePoint == 0xD)
                    || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                    || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                    || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
        }

        //过滤emoji 或者 其他非文字类型的字符
        public static String filterEmoji(String source) {
            //在传入的source后面加上一个空字符。返回的时候trim掉就OK了
            source += "";// 如果不包含，直接返回
            if (!containsEmoji(source)) {
                return source.trim();
            } else {
                StringBuilder buf = null;
                int len = source.length();
                for (int i = 0; i < len; i++) {
                    char codePoint = source.charAt(i);
                    if (isEmojiCharacter(codePoint)) {
                        if (buf == null) {
                            buf = new StringBuilder(source.length());
                        }
                        buf.append(codePoint);
                    }
                }
                if (buf == null) {//如果没有找到 emoji表情，则返回源字符串
                    return source;
                } else { //这里的意义在于尽可能少的toString，因为会重新生成字符串
                    if (buf.length() == len) {
                        buf = null;
                        return source;
                    } else {
                        return buf.toString();
                    }
                }
            }
        }
    }
}
