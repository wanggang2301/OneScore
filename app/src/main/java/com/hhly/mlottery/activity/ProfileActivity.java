package com.hhly.mlottery.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.ChoseHeadStartBean;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.net.account.AccountResultCode;

import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @ClassName: OneScoreGit
 * @author:Administrator luyao
 * @Description: 个人中心
 * @data: 2016/7/11 17:53
 */
public class ProfileActivity extends PictureSelectActivity implements View.OnClickListener {

    private String TAG = "ProfileActivity";
    /*昵称*/
    private TextView tv_nickname;

    /*头像*/
    private ImageView mHead_portrait;//头像

    private ProgressDialog progressBar;

    private final static int Put_FAIL_PHOTO = 11;

    private final OkHttpClient client = new OkHttpClient();


    private List<String> sexDatas = new ArrayList<>();

    private AlertDialog alertDialog;

    private PopupWindow mPopupWindow;


    private Handler mViewHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Put_FAIL_PHOTO:
                    UiUtils.toast(MyApp.getInstance(), R.string.picture_put_failed);
                    break;

                default:
                    break;
            }
        }
    };
    private TextView text_man;
    private TextView text_woman;
    private TextView text_noon;
    private ImageView woman_sex;
    private ImageView man_sex;
    private ImageView noon_sex;
    private WindowManager.LayoutParams lp;
    private WindowManager.LayoutParams lp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mViewHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        tv_nickname.setText(AppConstants.register.getUser().getNickName());
        /*性别入口关闭*/
 /*       if (AppConstants.register.getData().getUser().getSex() != null) {
            if (AppConstants.register.getData().getUser().getSex().equals("1")) {
                sexChange(R.color.home_logo_color, R.mipmap.man_sex, R.color.res_pl_color, R.mipmap.default_noon_sex, R.color.res_pl_color, R.mipmap.default_woman_sex);
            } else if (AppConstants.register.getData().getUser().getSex().equals("2")) {
                sexChange(R.color.res_pl_color, R.mipmap.default_man_sex, R.color.res_pl_color, R.mipmap.default_noon_sex, R.color.woman_sex, R.mipmap.woman_sex);
            } else {
                sexChange(R.color.res_pl_color, R.mipmap.default_man_sex, R.color.noon_sex, R.mipmap.noon_sex, R.color.res_pl_color, R.mipmap.default_woman_sex);
            }
        }
*/
    }

    private void initView() {

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage(getResources().getString(R.string.is_uploading));

        //设置头像
        mHead_portrait = (ImageView) findViewById(R.id.head_portrait);
        mHead_portrait.setOnClickListener(this);
        findViewById(R.id.modify_avatar).setOnClickListener(this);
        if (DeviceInfo.isLogin()) {
            Glide.with(getApplicationContext())
                    .load(    PreferenceUtil.getString(AppConstants.HEADICON,""))
                    .error(R.mipmap.center_head)
                    .into(mHead_portrait);
        }
        ((TextView) findViewById(R.id.public_txt_title)).setText(R.string.profile);
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        findViewById(R.id.public_img_back).setOnClickListener(this);
        findViewById(R.id.rl_nickname).setOnClickListener(this);
        findViewById(R.id.rl_modifypass).setOnClickListener(this);

        text_man = (TextView) findViewById(R.id.text_man);
        text_man.setOnClickListener(this);
        text_woman = (TextView) findViewById(R.id.text_woman);
        text_woman.setOnClickListener(this);
        text_noon = (TextView) findViewById(R.id.text_noon);
        text_noon.setOnClickListener(this);
        woman_sex = (ImageView) findViewById(R.id.woman_sex);
        man_sex = (ImageView) findViewById(R.id.man_sex);
        noon_sex = (ImageView) findViewById(R.id.noon_sex);


        //第三方登录时隐藏修改密码栏
        if (PreferenceUtil.getBoolean("three_login", false)) {
            findViewById(R.id.rl_modifypass).setVisibility(View.GONE);
            findViewById(R.id.account_number).setVisibility(View.GONE);
        }

        tv_nickname = ((TextView) findViewById(R.id.tv_nickname));
        ((TextView) findViewById(R.id.tv_account_real)).setText(PreferenceUtil.getString(AppConstants.SPKEY_LOGINACCOUNT, ""));


        // 设置裁剪图片结果监听
        this.setOnPictureSelectedListener(new OnPictureSelectedListener() {
            @Override
            public void onPictureSelected(Uri fileUri, Bitmap bitmap) {
                mHead_portrait.setImageBitmap(bitmap);

                String filePath = fileUri.getEncodedPath();
                String imagePath = Uri.decode(filePath);
                File file = new File(imagePath);
                doPostSycn(file);
                // Toast.makeText(mContext, "图片已经保存到:" + imagePath, Toast.LENGTH_LONG).show();
            }
        });
    }


    /*提示保存性别修改信息*/
    private void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this, R.style.AppThemeDialog);//  android.R.style.Theme_Material_Light_Dialog
        builder.setCancelable(false);// 设置对话框以外不可点击
        builder.setTitle("");// 提示标题
        builder.setMessage(R.string.confirm_the_selected_sex);// 提示内容
        builder.setPositiveButton(R.string.about_confirm, new DialogInterface.OnClickListener() {
            //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                modifyGender();
            }
        });
        builder.setNegativeButton(R.string.about_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back: // 返回
                MobclickAgent.onEvent(ProfileActivity.this, "ProfileActivity_Exit");
                if (sexDatas.size() == 0) {
                    finish();
                } else {
                    showDialog();

                }
                // finish();
                break;
            case R.id.rl_nickname: // 昵称栏
                MobclickAgent.onEvent(ProfileActivity.this, "ModifyNicknameActivity_Start");
                Intent intent = new Intent(ProfileActivity.this, ModifyNicknameActivity.class);
                intent.putExtra("nickname", tv_nickname.getText().toString());
                startActivity(intent);
                break;
            case R.id.rl_modifypass: // 修改密码
                MobclickAgent.onEvent(ProfileActivity.this, "ModifyPasswordActivity_Start");
                startActivity(new Intent(this, ModifyPasswordActivity.class));
                break;

            case R.id.modify_avatar: //修改头像
                selectPicture();
                break;


            case R.id.text_man:

                sexDatas.clear();
                sexDatas.add("1");
                sexChange(R.color.home_logo_color, R.mipmap.man_sex, R.color.res_pl_color, R.mipmap.default_noon_sex, R.color.res_pl_color, R.mipmap.default_woman_sex);
                break;
            case R.id.text_woman:
                sexDatas.clear();
                sexDatas.add("2");
                sexChange(R.color.res_pl_color, R.mipmap.default_man_sex, R.color.res_pl_color, R.mipmap.default_noon_sex, R.color.woman_sex, R.mipmap.woman_sex);
                break;
            case R.id.text_noon:
                sexDatas.clear();
                sexDatas.add("3");
                sexChange(R.color.res_pl_color, R.mipmap.default_man_sex, R.color.noon_sex, R.mipmap.noon_sex, R.color.res_pl_color, R.mipmap.default_woman_sex);
                break;
            case R.id. head_portrait:  //拍照
                selectPicture();
                break;


            default:
                break;
        }
    }

    //修改性别
    private void modifyGender() {
        progressBar.show();
        String url = BaseURLs.UPDATEUSERINFO;
        Map<String, String> param = new HashMap<>();
        param.put("account", PreferenceUtil.getString(AppConstants.SPKEY_LOGINACCOUNT, ""));
        param.put("loginToken", AppConstants.register.getToken());
        param.put("deviceToken", AppConstants.deviceToken);
        param.put("sex", sexDatas.get(0));

        VolleyContentFast.requestJsonByPost(url, param, new VolleyContentFast.ResponseSuccessListener<Register>() {
            @Override
            public void onResponse(Register register) {
                if (Integer.parseInt(register.getCode()) == AccountResultCode.SUCC) {
                    progressBar.dismiss();
                    finish();
                } else if (Integer.parseInt(register.getCode()) == AccountResultCode.USER_NOT_LOGIN) {
                    progressBar.dismiss();
                    UiUtils.toast(getApplicationContext(), R.string.name_invalid);
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(intent);

                } else {
                    progressBar.dismiss();
                    DeviceInfo.handlerRequestResult(Integer.parseInt(register.getCode()), "未知错误");
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                L.e(TAG, "上传性别失败");
                UiUtils.toast(ProfileActivity.this, R.string.foot_neterror_post_photo);
                progressBar.dismiss();
            }
        }, Register.class);

    }

    //统一选择性别状态
    private void sexChange(int home_logo_color, int default_woman_sex, int res_pl_color, int default_noon_sex, int res_pl_color1, int default_woman_sex1) {
        text_man.setTextColor(ProfileActivity.this.getResources().getColor(home_logo_color));
        man_sex.setImageResource(default_woman_sex);
        text_noon.setTextColor(getResources().getColor(res_pl_color));
        noon_sex.setImageResource(default_noon_sex);
        text_woman.setTextColor(getResources().getColor(res_pl_color1));
        woman_sex.setImageResource(default_woman_sex1);
    }

    public void onEventMainThread(ChoseHeadStartBean choseHeadStartBean) {

        Glide.with(getApplicationContext())
                .load(choseHeadStartBean.startUrl)
                .error(R.mipmap.center_head)
                .into(mHead_portrait);
    }


    /*图片上传*/
    //异步上传图片并且携带其他参数
    public void doPostSycn(File file) {
        progressBar.show();
        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/png"), file))
                    .addFormDataPart("userId", AppConstants.register.getUser().getUserId())
                    .addFormDataPart("loginToken", AppConstants.register.getToken())
                    .build();

            Request request = new Request.Builder()//建立请求
                    .url(BaseURLs.MODIFY_PICTURE)//请求的地址
                    .post(requestBody)//请求的内容（上面建立的requestBody）
                    .build();

            //发送异步请求，同步会报错，Android4.0以后禁止在主线程中进行耗时操作
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // UiUtils.toast(MyApp.getInstance(), R.string.picture_put_failed);
                    mViewHandler.sendEmptyMessage(Put_FAIL_PHOTO);
                    progressBar.dismiss();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    progressBar.dismiss();
                    String jsonString = response.body().string();
                    JSONObject jo = JSON.parseObject(jsonString);
                    if (response != null) {
                        if (jo.getString("code").equals("200")) {
                            String headerUrl = jo.getString("avatorURL");
                            EventBus.getDefault().post(new ChoseHeadStartBean(headerUrl));
                            AppConstants.register.getUser().setImageSrc(headerUrl);
                            PreferenceUtil.commitString(AppConstants.HEADICON,headerUrl);

                        }
                    }

                }

            });

        } catch (Exception e) {

            UiUtils.toast(MyApp.getInstance(), R.string.picture_resource_does_not_exist);
        }


    }



}
