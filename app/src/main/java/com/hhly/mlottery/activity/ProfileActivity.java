package com.hhly.mlottery.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.ChoseHeadStartBean;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.util.net.account.AccountResultCode;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * @ClassName: OneScoreGit
 * @author:Administrator luyao
 * @Description:   个人中心
 * @data: 2016/7/11 17:53
 */
public class ProfileActivity extends Activity implements View.OnClickListener {

    public static String TAG = "ProfileActivity";
    /*昵称*/
    private TextView tv_nickname;

    /*头像*/
    private ImageView mHead_portrait;//头像

    /** 拍照获得图片Url */
    private Uri mCamerUri;

    /** 头像Bitmap */
    private Bitmap mHeadBitmap;

    /** 裁剪图片code */
    private final static int REQUEST_IMAGE_CROP = 2000;
    /** 相册获得图片code */
    private final static int REQUEST_IMAGE_CHOICE = 10;
    /** 拍照获得图片code */
    private final static int REQUEST_IMAGE_CAPTURE = 20;

    public static final int REQUESTCODE_CHOSE = 100;

    /*图片上传url*/
    String PUT_URL="http://file.13322.com/upload/uploadImage.do";

    /**图片裁剪宽度*/
    private int width = 300;

    /**图片裁剪高度*/
    private int height = 300;


    private ProgressDialog progressBar;

    private final static int Put_FAIL_PHOTO = 11;

    private final OkHttpClient client = new OkHttpClient();

    private static final String IMAGE_STORAGGEID = "/headview/image.png";
    private static final String IMAGE = "image.png";
    private static final String STORAGGEID = "/headview";
    private List<String> sexDatas=new ArrayList<>();

    private AlertDialog alertDialog;

    private PopupWindow mPopupWindow;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("application/octet-stream");


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
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        tv_nickname.setText(AppConstants.register.getData().getUser().getNickName());
        if(AppConstants.register.getData().getUser().getSex()!=null){
            Log.i("sdadasdas","getSex"+AppConstants.register.getData().getUser().getSex());
            if(AppConstants.register.getData().getUser().getSex().equals("1")){
                sexChange(R.color.home_logo_color,R.mipmap.man_sex,R.color.res_pl_color,R.mipmap.default_noon_sex,R.color.res_pl_color,R.mipmap.default_woman_sex);
            }else if(AppConstants.register.getData().getUser().getSex().equals("2")){
                sexChange(R.color.res_pl_color,R.mipmap.default_man_sex,R.color.res_pl_color,R.mipmap.default_noon_sex,R.color.woman_sex,R.mipmap.woman_sex);
            }else {
                sexChange(R.color.res_pl_color,R.mipmap.default_man_sex,R.color.noon_sex,R.mipmap.noon_sex,R.color.res_pl_color,R.mipmap.default_woman_sex);
            }
        }

    }

    private void initView() {

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage(getResources().getString(R.string.is_uploading));

        //设置头像
        mHead_portrait = (ImageView) findViewById(R.id.head_portrait);
        mHead_portrait.setOnClickListener(this);
        findViewById(R.id.modify_avatar).setOnClickListener(this);
       if (CommonUtils.isLogin()){
            //ImageLoader.load(ProfileActivity.this,AppConstants.register.getData().getUser().getHeadIcon(),R.mipmap.center_head).into(mHead_portrait);
           Glide.with(ProfileActivity.this)
                   .load(AppConstants.register.getData().getUser().getHeadIcon())
                   .error(R.mipmap.center_head)
                   .into(mHead_portrait);
        }
        //universalImageLoader.displayImage(AppConstants.register.getData().getUser().getHeadIcon(), mHead_portrait, options);

      /*  if (Environment
                .getExternalStoragePublicDirectory(IMAGE_STORAGGEID).exists()) {
            Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(IMAGE_STORAGGEID).toString());
            mHead_portrait.setImageBitmap(bm);
        } else {
            universalImageLoader.displayImage(AppConstants.register.getData().getUser().getHeadIcon(), mHead_portrait, options);
        }*/
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
        if(PreferenceUtil.getBoolean("three_login",false)){
            findViewById(R.id.rl_modifypass).setVisibility(View.GONE);
            findViewById(R.id.account_number).setVisibility(View.GONE);
        }
        tv_nickname = ((TextView) findViewById(R.id.tv_nickname));
        ((TextView) findViewById(R.id.tv_account_real)).setText(AppConstants.register.getData().getUser().getLoginAccount());
    }
    /*提示保存性别修改信息*/
    private void showDialog() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ProfileActivity.this, R.style.AppThemeDialog);//  android.R.style.Theme_Material_Light_Dialog
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
                if(sexDatas.size()==0){
                    finish();
                }else{
                    showDialog();

                }
                // finish();
                break;
            case R.id.rl_nickname: // 昵称栏
                MobclickAgent.onEvent(ProfileActivity.this, "ModifyNicknameActivity_Start");
                // startActivity(new Intent(this, ModifyNicknameActivity.class));
                Intent intent = new Intent(ProfileActivity.this, ModifyNicknameActivity.class);
                intent.putExtra("nickname", tv_nickname.getText().toString());
                startActivity(intent);
                break;
            case R.id.rl_modifypass: // 修改密码
                MobclickAgent.onEvent(ProfileActivity.this, "ModifyPasswordActivity_Start");
                startActivity(new Intent(this, ModifyPasswordActivity.class));
                break;
            case R.id.head_portrait:///显示全图
 /*                Intent intent2 = new Intent(ProfileActivity.this, EnlargePhotoActivity.class);
                 startActivity(intent2);
//                Intent intent2 = new Intent(ProfileActivity.this, PicturePreviewActivity.class);
//                 intent2.putExtra("url", PreferenceUtil.getString(AppConstants.HEADICON, ""));
//                 startActivity(intent2);*/
                backgroundAlpha(0.5f);
                setHeadView(v);
                break;
            case R.id.modify_avatar: //修改头像
                backgroundAlpha(0.5f);
                setHeadView(v);
                break;
        /*    case R.id.tv_photograph:  //拍照
                doTakePhoto();
               *//* Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.
                        getExternalStorageDirectory(), "temp.jpg")));
                startActivityForResult(camera, CAMERA_REQUEST_CODE);*//*

                break;
            case R.id.tv_chosephoto:  //相册中选取
                doPickPhotoFromGallery();
               *//* Intent picture = new Intent(Intent.ACTION_PICK, null);
                picture.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                startActivityForResult(picture, ALBUM_REQUEST_CODE);*//*
                break;*/
            case R.id.tv_chose_start_photo:
                Intent intent3 = new Intent(ProfileActivity.this, AvatarSelectionActivity.class);
                startActivityForResult(intent3,REQUESTCODE_CHOSE);
                mPopupWindow.dismiss();
                break;
            case R.id.tv_cancel:  //取消

                mPopupWindow. dismiss();
                break;
            case R.id.text_man:

               // man_sex.setImageResource(R.mipmap.man_sex);
                sexDatas.clear();
                sexDatas.add("1");
                sexChange(R.color.home_logo_color,R.mipmap.man_sex,R.color.res_pl_color,R.mipmap.default_noon_sex,R.color.res_pl_color,R.mipmap.default_woman_sex);
                break;
            case R.id.text_woman:
                sexDatas.clear();
                sexDatas.add("2");
                sexChange(R.color.res_pl_color,R.mipmap.default_man_sex,R.color.res_pl_color,R.mipmap.default_noon_sex,R.color.woman_sex,R.mipmap.woman_sex);
                break;
            case R.id.text_noon:
                sexDatas.clear();
                sexDatas.add("3");
                sexChange(R.color.res_pl_color,R.mipmap.default_man_sex,R.color.noon_sex,R.mipmap.noon_sex,R.color.res_pl_color,R.mipmap.default_woman_sex);
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
                param.put("account", AppConstants.register.getData().getUser().getLoginAccount());
                param.put("loginToken", AppConstants.register.getData().getLoginToken());
                param.put("deviceToken", AppConstants.deviceToken);
                param.put("sex", sexDatas.get(0).toString());

                VolleyContentFast.requestJsonByPost(url, param, new VolleyContentFast.ResponseSuccessListener<Register>() {
                    @Override
                    public void onResponse(Register register) {
                        if (register.getResult() == AccountResultCode.SUCC) {
                            // CommonUtils.saveRegisterInfo(register);
                            AppConstants.register.getData().getUser().setSex(sexDatas.get(0));
                            PreferenceUtil.commitString(AppConstants.SEX, register.getData().getUser().getSex());
                            Log.i("smsdas","getSex>>>>>>>"+register.getData().getUser().getSex());
                            progressBar.dismiss();
                            finish();
                        } else if (register.getResult() == AccountResultCode.USER_NOT_LOGIN) {
                            progressBar.dismiss();
                            UiUtils.toast(getApplicationContext() ,R.string.name_invalid);
                            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                            startActivity(intent);

                        } else {
                            progressBar.dismiss();
                            CommonUtils.handlerRequestResult(register.getResult(), register.getMsg());
                        }
                    }
                }, new VolleyContentFast.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                        L.e(TAG, "上传性别失败");
                     UiUtils.toast(ProfileActivity.this, R.string.foot_neterror_post_photo);
                        progressBar.dismiss();
                        finish();
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
    //性别选择状态改变

    /**
     * 开启拍照功能
     */
    private void doTakePhoto() {
        Intent captrueIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        File picFile = getPicFile();
        if (picFile != null) {
            captrueIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picFile));
            startActivityForResult(captrueIntent, REQUEST_IMAGE_CAPTURE);
            mCamerUri = Uri.fromFile(picFile);
        }
    }
    /**
     * 获得图片缓存文件
     */
    private File getPicFile() {
        File photoFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (!photoFile.exists()) {
            UiUtils.toast(MyApp.getInstance(),R.string.sd_card_not_exist);
        }
        File file = new File(photoFile.getAbsolutePath() + "/dcim" + System.currentTimeMillis() + ".jpg");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public void onEventMainThread(ChoseHeadStartBean choseHeadStartBean){

        ImageLoader.load(ProfileActivity.this,choseHeadStartBean.startUrl).into(mHead_portrait);
    }
    /**
     * 开启从相册获得图片功能
     */
    private void doPickPhotoFromGallery() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*"); // 设置文件类型
            startActivityForResult(intent, REQUEST_IMAGE_CHOICE);// 转到图库
        } catch (Exception e) {
            UiUtils.toast(MyApp.getInstance(), R.string.install_the_gallery);
        }
    }
    /*
    * 设置头像
    */
    private void setHeadView(View v) {
        /// 找到布局文件
        LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.avatar_selection, null);
      //  layout.findViewById(R.id.tv_photograph).setOnClickListener(this);
      //  layout.findViewById(R.id.tv_chosephoto).setOnClickListener(this);
        layout.findViewById(R.id.tv_chose_start_photo).setOnClickListener(this);
        layout.findViewById(R.id.tv_cancel).setOnClickListener(this);
        // 实例化一个PopuWindow对象
        mPopupWindow = new PopupWindow(v);
        // 设置弹框的宽度为布局文件的宽
        mPopupWindow.setWidth( DisplayUtil.getScreenWidth(ProfileActivity.this));
        // 高度随着内容变化
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置背景颜色变暗
        lp1 = getWindow().getAttributes();
        lp1.alpha = 0.5f;
        // 设置点击弹框外部，弹框消失
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new poponDismissListener());
        // 设置焦点
        mPopupWindow.setFocusable(true);
        // 设置所在布局
        mPopupWindow.setContentView(layout);
        // 设置弹框出现的位置，在v的正下方横轴偏移textview的宽度，为了对齐~纵轴不偏移
       // popupWindow.showAsDropDown(mTxt_title1);
        mPopupWindow.showAtLocation(ProfileActivity.this.findViewById(R.id.profile_main),
                Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               // UiUtils.toast(getApplicationContext(),":我被点击了");
                return true;
            }
        });

    }
    class poponDismissListener implements PopupWindow.OnDismissListener{
        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            //Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
         lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }


    File outFile = null;
    Bitmap bitmap = null;
    File outDir = null;

    /**
     * 1取出拍照的结果存储到手机内存则指定的文件夹， 再从文件加下取出展示到界面
     * 2从相册中取出图片，压缩，展示到界面 。
     */
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_IMAGE_CHOICE:
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                }
                if (uri == null) {
                    return;
                }
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(ProfileActivity.this.getContentResolver(), uri);
                    if (bitmap.getWidth() < width || bitmap.getHeight() < height) {
                        UiUtils.toast(MyApp.getInstance(),R.string.see_your_face);
                        bitmap.recycle();
                        System.gc();
                        //successNull();
                        return;
                    }
                } catch (Exception e) {
                    //error(e);
                    return;
                }
                doZoomImage(uri, width, height);
                break;
            case REQUEST_IMAGE_CAPTURE:
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(ProfileActivity.this.getContentResolver(), mCamerUri);
                    if (bitmap.getWidth() < width || bitmap.getHeight() < height) {
                       // successNull();
                        UiUtils.toast(MyApp.getInstance(), R.string.see_your_face);
                        bitmap.recycle();
                        System.gc();
                        return;
                    }
                } catch (Exception e) {
                   // error(e);
                    return;
                }
                doZoomImage(mCamerUri, width, height);
                break;
            case REQUEST_IMAGE_CROP:
                Log.i(TAG, "相册裁剪成功");
                Log.i(TAG, "裁剪以后 [ " + data + " ]");
                if (data == null) {
                    // 显示之前额图片,或者显示默认的图片
                    return;
                }
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    if (photo!=null)

                    {
                        photo.compress(Bitmap.CompressFormat.PNG, 75, stream);// (0-100)压缩文件

                    //此处可以把Bitmap保存到sd卡中，具体请看：http://www.cnblogs.com/linjiqin/archive/2011/12/28/2304940.html
                    try {

                        String state = Environment.getExternalStorageState();
                        if (state.equals(Environment.MEDIA_MOUNTED)) {
                            // 这个路径，在手机内存下创建一个定制的文件夹，把图片存在其中。
                            outDir = Environment.getExternalStoragePublicDirectory(STORAGGEID);
                        } else {
                            if (ProfileActivity.this != null) {
                                outDir = this.getFilesDir();
                            }
                        }
                        if (!outDir.exists()) {
                            outDir.mkdirs();
                        }
                        // 保存图片
                        try {
                            outFile = new File(outDir, IMAGE);
                            FileOutputStream fos = new FileOutputStream(outFile);
                            //PreferenceUtil.commitString("image_url", outFile.toString());
                            boolean flag = photo.compress(Bitmap.CompressFormat.JPEG,
                                    100, fos);// 把数据写入文件
                            Log.i("1", "flag=" + flag);
                            if (flag) {
                                Log.i(TAG,"图片已保存至:" + outFile.getAbsolutePath());
                                 mHead_portrait.setImageBitmap(photo);
                                 doPostSycn(PUT_URL,outFile);//上传图片

                            } else {
                                UiUtils.toast(MyApp.getInstance(), R.string.picture_save_failed);
                            }
                        } catch (FileNotFoundException e) {
                            UiUtils.toast(MyApp.getInstance(), R.string.save_file_not_found);
                            throw new RuntimeException(e);
                        }


                    } catch (Exception e) {
                        UiUtils.toast(MyApp.getInstance(), R.string.save_file_not_found);
                        e.printStackTrace();
                    }
                    mPopupWindow. dismiss();
                    mHead_portrait.setImageBitmap(photo); //把图片显示在ImageView控件上
                    photo.recycle();
                    System.gc();
                   }
                }
                break;
            default:
                break;
        }
    }*/

     /*图片上传*/
     //异步上传图片并且携带其他参数
     public void doPostSycn(String url,File file){
         progressBar.show();

         RequestBody requestBody = new MultipartBuilder() //建立请求的内容

                 .type(MultipartBuilder.FORM)//表单形式
                 .addFormDataPart("imageFile", "imageFile", RequestBody.create(MEDIA_TYPE_PNG, file))
                 .build();

         Request request = new Request.Builder()//建立请求
                 .url(url)//请求的地址
                 .post(requestBody)//请求的内容（上面建立的requestBody）
                 .build();

         //发送异步请求，同步会报错，Android4.0以后禁止在主线程中进行耗时操作
         client.newCall(request).enqueue(new Callback() {
             @Override
             public void onFailure(Request request, IOException e) {
                // UiUtils.toast(MyApp.getInstance(), R.string.picture_put_failed);
                 mViewHandler.sendEmptyMessage(Put_FAIL_PHOTO);
                 progressBar.dismiss();
                 Log.d(TAG, "onFailure: "+e.getMessage());
             }

             @Override
             public void onResponse(Response response) throws IOException {
                 Log.d(TAG, "onResponse: ");

                  String jsonString=response.body().string();
                  JSONObject jo = JSON.parseObject(jsonString);
                  //UiUtils.toast(MyApp.getInstance(), jo.toString());
                  String headerUrl = jo.getString("url");
                 if(!headerUrl.isEmpty()){
                     putPhotoUrl(headerUrl);
                 }else{
                     UiUtils.toast(MyApp.getInstance(), R.string.picture_put_failed);
                 }

             }


         });
     }

    /*上传图片url  后台绑定*/

    private void putPhotoUrl(String headerUrl) {

        //final String url = BaseURLs.UPDATEHEADICON;

        Map<String, String> param = new HashMap<>();

        Log.d(TAG, AppConstants.deviceToken);
        param.put("deviceToken", AppConstants.deviceToken);

        Log.d(TAG, PreferenceUtil.getString(AppConstants.SPKEY_TOKEN, ""));
        param.put("loginToken", PreferenceUtil.getString(AppConstants.SPKEY_TOKEN, ""));

        Log.d(TAG, AppConstants.deviceToken);
        param.put("imgUrl", headerUrl);

        VolleyContentFast.requestJsonByPost(BaseURLs.UPDATEHEADICON, param, new VolleyContentFast.ResponseSuccessListener<Register>() {
            @Override
            public void onResponse(Register register) {
                progressBar.dismiss();
                if (register.getResult() == AccountResultCode.SUCC) {
                    UiUtils.toast(MyApp.getInstance(), R.string.picture_put_success);
                    register.getData().getUser().setLoginAccount(AppConstants.register.getData().getLoginToken());
                    CommonUtils.saveRegisterInfo(register);

                    PreferenceUtil.commitString(AppConstants.HEADICON, register.getData().getUser().getHeadIcon().toString());
                    Glide.with(ProfileActivity.this)
                            .load(register.getData().getUser().getHeadIcon())
                            .error(R.mipmap.center_head)
                            .into(mHead_portrait);
                    //ImageLoader.load(ProfileActivity.this,register.getData().getUser().getHeadIcon(),R.mipmap.center_head).into(mHead_portrait);

                } else {
                    CommonUtils.handlerRequestResult(register.getResult(), register.getMsg());
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                progressBar.dismiss();
                L.e(TAG, "图片上传失败");
                UiUtils.toast(ProfileActivity.this, R.string.picture_put_failed);
            }
        }, Register.class);

    }

    private Uri fileUri;
    /**
     * 开启图片裁剪功能
     */
    public void doZoomImage(Uri uri, int width, int height) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            if (Build.VERSION.SDK_INT >= 23){
                Bitmap bmp = null;
                String str = "";
                try {
                    bmp = MediaStore.Images.Media.getBitmap(ProfileActivity.this.getContentResolver(), uri);
                    str = MediaStore.Images.Media.insertImage(ProfileActivity.this.getContentResolver(), bmp, "", "");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                fileUri = Uri.parse(str);
                intent.setDataAndType(fileUri, "image/*");
            }else{
                intent.setDataAndType(uri, "image/*");
            }

            // 设置裁剪
            intent.putExtra("crop", "true");
            intent.putExtra("outputX", width);
            intent.putExtra("outputY", height);
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", width);
            intent.putExtra("aspectY", height);
//            intent.putExtra("aspectX", 1);
//            intent.putExtra("aspectY", 1);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_IMAGE_CROP);
        } catch (Exception e) {
            UiUtils.toast(MyApp.getInstance(),R.string.install_the_gallery);
        }
    }


    /**
     * 压缩图片
     */
    private Bitmap createThumbnail(String filepath, int i) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = i;
        return BitmapFactory.decodeFile(filepath, options);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 刷新本地用户缓存
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(AppConstants.register.getData().getUser().getUserId(), AppConstants.register.getData().getUser().getNickName(), Uri.parse(PreferenceUtil.getString(AppConstants.HEADICON, "xxx"))));
        EventBus.getDefault().unregister(this);
    }

}
