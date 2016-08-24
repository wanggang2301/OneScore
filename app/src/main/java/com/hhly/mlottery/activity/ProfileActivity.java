package com.hhly.mlottery.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
import com.hhly.mlottery.util.DisplayUtil;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 个人
 */
public class ProfileActivity extends BaseActivity implements View.OnClickListener {

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


    /**图片裁剪宽度*/
    private int width = 300;

    /**图片裁剪高度*/
    private int height = 300;

    private final OkHttpClient client = new OkHttpClient();

    private static final String IMAGE_STORAGGEID = "/headview/image.png";
    private static final String IMAGE = "image.png";
    private static final String STORAGGEID = "/headview";

    private PopupWindow mPopupWindow;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("application/octet-stream");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_nickname.setText(AppConstants.register.getData().getUser().getNickName());
    }

    private void initView() {
        //设置头像
        mHead_portrait = (ImageView) findViewById(R.id.head_portrait);
        mHead_portrait.setOnClickListener(this);
        findViewById(R.id.modify_avatar).setOnClickListener(this);

      /*  if (Environment
                .getExternalStoragePublicDirectory(IMAGE_STORAGGEID).exists()) {
            Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(IMAGE_STORAGGEID).toString());
            mHead_portrait.setImageBitmap(bm);
        } else {
            mHead_portrait.setImageResource(R.mipmap.smallhead);

        }*/
        ((TextView) findViewById(R.id.public_txt_title)).setText(R.string.profile);
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        findViewById(R.id.public_img_back).setOnClickListener(this);
        findViewById(R.id.rl_nickname).setOnClickListener(this);
        findViewById(R.id.rl_modifypass).setOnClickListener(this);
        //第三方登录时隐藏修改密码栏
        if(PreferenceUtil.getBoolean("three_login",false)){
            findViewById(R.id.rl_modifypass).setVisibility(View.GONE);
            findViewById(R.id.account_number).setVisibility(View.GONE);
        }
        tv_nickname = ((TextView) findViewById(R.id.tv_nickname));
        ((TextView) findViewById(R.id.tv_account_real)).setText(AppConstants.register.getData().getUser().getLoginAccount());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back: // 返回
                MobclickAgent.onEvent(mContext, "ProfileActivity_Exit");
                finish();
                break;
            case R.id.rl_nickname: // 昵称栏
                MobclickAgent.onEvent(mContext, "ModifyNicknameActivity_Start");
                // startActivity(new Intent(this, ModifyNicknameActivity.class));
                Intent intent = new Intent(ProfileActivity.this, ModifyNicknameActivity.class);
                intent.putExtra("nickname", tv_nickname.getText().toString());
                startActivity(intent);
                break;
            case R.id.rl_modifypass: // 修改密码
                MobclickAgent.onEvent(mContext, "ModifyPasswordActivity_Start");
                startActivity(new Intent(this, ModifyPasswordActivity.class));
                break;

            case R.id.head_portrait:
                MobclickAgent.onEvent(mContext, "ProfileActivity_SetHead");

                break;
            case R.id.modify_avatar: //修改头像

              setHeadView(v);
                break;
            case R.id.tv_photograph:  //拍照
                doTakePhoto();
               /* Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.
                        getExternalStorageDirectory(), "temp.jpg")));
                startActivityForResult(camera, CAMERA_REQUEST_CODE);*/

                break;
            case R.id.tv_chosephoto:  //相册中选取
                doPickPhotoFromGallery();
               /* Intent picture = new Intent(Intent.ACTION_PICK, null);
                picture.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                startActivityForResult(picture, ALBUM_REQUEST_CODE);*/
                break;
            case R.id.tv_cancel:  //取消
                mPopupWindow. dismiss();
                break;
            default:
                break;
        }
    }

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
            UiUtils.toast(MyApp.getInstance(), "sd卡不存在");
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

    /**
     * 开启从相册获得图片功能
     */
    private void doPickPhotoFromGallery() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*"); // 设置文件类型
            startActivityForResult(intent, REQUEST_IMAGE_CHOICE);// 转到图库
        } catch (Exception e) {
            UiUtils.toast(MyApp.getInstance(), "请先安装图库");
        }
    }
    /*
    * 设置头像
    */
    private void setHeadView(View v) {
        /// 找到布局文件
        LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.avatar_selection, null);
        layout.findViewById(R.id.tv_photograph).setOnClickListener(this);
        layout.findViewById(R.id.tv_chosephoto).setOnClickListener(this);
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
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        // 设置点击弹框外部，弹框消失
        mPopupWindow.setOutsideTouchable(true);
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

                return true;
            }
        });

    }

    File outFile = null;
    Bitmap bitmap = null;
    File outDir = null;

    /**
     * 1取出拍照的结果存储到手机内存则指定的文件夹， 再从文件加下取出展示到界面
     * 2从相册中取出图片，压缩，展示到界面 。
     */
    @Override
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
                        UiUtils.toast(MyApp.getInstance(), "这张照片太小看不清你的绝世容颜");
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
                        UiUtils.toast(MyApp.getInstance(), "这张照片太小看不清你的绝世容颜");
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
                                //BaseURLs.UPLOADIMAGE

                                 String url="http://file.13322.com/upload/uploadImage.do";
                                 doPostSycn(url,outFile);
                                //System.out.println("url>>>>>>>>>>>>>>>>>>>>>>>"+outFile.toString());
                                //putPhotoUrl("http://pic.13322.com/basketball/team/135_135/1.png");

                            } else {
                                UiUtils.toast(MyApp.getInstance(), "图片保存失败!");
                            }
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mPopupWindow. dismiss();
                    mHead_portrait.setImageBitmap(photo); //把图片显示在ImageView控件上
                }
                break;
            default:
                break;
        }
    }
     /*图片上传*/
     //异步上传图片并且携带其他参数
     public void doPostSycn(String url,File file){


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
                     UiUtils.toast(MyApp.getInstance(), "图片上传失败!");
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

                if (register.getResult() == AccountResultCode.SUCC) {
                    UiUtils.toast(MyApp.getInstance(), "图片上传成功");
                   // CommonUtils.saveRegisterInfo(register);
                    PreferenceUtil.commitString(AppConstants.HEADICON, register.getData().getUser().getHeadIcon().toString());
                } else {
                    CommonUtils.handlerRequestResult(register.getResult(), register.getMsg());
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

                L.e(TAG, "图片上传失败");
                UiUtils.toast(ProfileActivity.this, R.string.immediate_unconection);
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
            UiUtils.toast(MyApp.getInstance(), "请先安装图库");
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


}
