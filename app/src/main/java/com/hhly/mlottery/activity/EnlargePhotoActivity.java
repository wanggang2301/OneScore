package com.hhly.mlottery.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.account.Register;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.CommonUtils;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: OneScoreGit
 * @author:Administrator luyao
 * @Description:   头像大图片显示
 * @data: 2016/8/22 17:53
 */
public class EnlargePhotoActivity extends  BaseActivity implements View.OnClickListener {

    /*网络获取图片*/
    private ImageView mEblarge_photo;

    /*设置*/
    private ImageView mPublic_btn_set;

    /*本地保存图片*/
    private View local_preservation;

    /** 相册获得图片code */
    private final static int REQUEST_IMAGE_CHOICE = 10;


    /** 裁剪图片code */
    private final static int REQUEST_IMAGE_CROP = 2000;

    /**图片裁剪宽度*/
    private int width = 300;

    /**图片裁剪高度*/
    private int height = 300;

    /*图片上传url*/
    String PUT_URL="http://file.13322.com/upload/uploadImage.do";

    private final static int Put_FAIL_PHOTO = 11;

    private ProgressDialog progressBar;

    private static final String IMAGE = "image.png";
    private static final String STORAGGEID = "/headview";
    private static final String STORAGGEPHOTO= "/headview/nativ";
    private final OkHttpClient client = new OkHttpClient();
    File putoutFile = null;
    File outFile = null;
    Bitmap bitmap = null;
    File outDir = null;

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("application/octet-stream");
    private File mPutDir;

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
    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enlarge_photo);

        initView();
        initData();
    }
     /*加载网络获取图片*/
    private void initData() {
        ImageLoader.load(mContext,PreferenceUtil.getString(AppConstants.HEADICON, ""),R.mipmap.center_head).into(mEblarge_photo);

    }

    private void initView() {
        /* progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage(getResources().getString(R.string.logining));*/

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage(getResources().getString(R.string.is_uploading));

        ((TextView) findViewById(R.id.public_txt_title)).setText(R.string.avatar_settings);
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);
        mPublic_btn_set = (ImageView) findViewById(R.id.public_btn_set);
        mPublic_btn_set.setImageResource(R.mipmap.head_details);
        mPublic_btn_set.setOnClickListener(this);
        findViewById(R.id.public_img_back).setOnClickListener(this);
        //头像
        mEblarge_photo = (ImageView) findViewById(R.id.eblarge_photo);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case  R.id.public_img_back:
                finish();
                break;
            case R.id.public_btn_set:
                popWindow(v);
                break;
        }

    }
   /*设置框*/
    private void popWindow(View v) {

        final View mView = View.inflate(getApplicationContext(), R.layout.football_details, null);
        ((ImageView) mView.findViewById(R.id.football_item_focus_iv)).setVisibility(View.GONE);
        ((TextView) mView.findViewById(R.id.football_item_focus_tv)).setText(R.string.chose_for_telephone);

        ((ImageView) mView.findViewById(R.id.share_image)).setVisibility(View.GONE);
        ((TextView) mView.findViewById(R.id.share_text)).setText(R.string.picture_save_telephone);
        final PopupWindow popupWindow = new PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        int[] location = new int[2];
        v.getLocationOnScreen(location);

        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] - v.getWidth() - v.getPaddingRight(), location[1] + v.getHeight());         //  popupWindow.showAsDropDown(v,-10,0);
        //从图片中选择
        (mView.findViewById(R.id.football_item_focus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "足球！", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

                try {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*"); // 设置文件类型
                    startActivityForResult(intent, REQUEST_IMAGE_CHOICE);// 转到图库
                } catch (Exception e) {
                    UiUtils.toast(MyApp.getInstance(), R.string.install_the_gallery);
                }

            }
        });


        local_preservation = mView.findViewById(R.id.football_item_share);
        local_preservation.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

             SimpleTarget target = new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        // do something with the bitmap
                        // for demonstration purposes, let's just set it to an ImageView
                        photo = bitmap;
                    }


                };
                    Glide.with(mContext)
                            .load(PreferenceUtil.getString(AppConstants.HEADICON, ""))
                            .into(target);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 75, stream);// (0-100)压缩文件
                    //此处可以把Bitmap保存到sd卡中，具体请看：http://www.cnblogs.com/linjiqin/archive/2011/12/28/2304940.html
                    try {
                        String state = Environment.getExternalStorageState();
                        if (state.equals(Environment.MEDIA_MOUNTED)) {
                            // 这个路径，在手机内存下创建一个定制的文件夹，把图片存在其中。
                            mPutDir = Environment.getExternalStoragePublicDirectory(STORAGGEPHOTO);
                        } else {
                         /*   if (EnlargePhotoActivity.this != null) {
                                mPutDir = this.getFilesDir();
                            }*/
                        }
                        if (!mPutDir.exists()) {
                            mPutDir.mkdirs();
                        }
                        // 保存图片
                        try {
                            putoutFile = new File(mPutDir, System.currentTimeMillis()+IMAGE);
                            FileOutputStream fos = new FileOutputStream(putoutFile);
                            //PreferenceUtil.commitString("image_url", outFile.toString());
                            boolean flag = photo.compress(Bitmap.CompressFormat.PNG,
                                    100, fos);// 把数据写入文件
                            Log.i("1", "flag=" + flag);
                            if (flag) {
                                UiUtils.toast(MyApp.getInstance(),getResources().getString(R.string.picture_save)+ mPutDir.toString());

                            } else {
                                UiUtils.toast(MyApp.getInstance(), R.string.picture_save_failed);
                            }
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        });

    }


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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(EnlargePhotoActivity.this.getContentResolver(), uri);
                    if (bitmap.getWidth() < width || bitmap.getHeight() < height) {
                        UiUtils.toast(MyApp.getInstance(), R.string.see_your_face);
                        //successNull();
                        return;
                    }
                } catch (Exception e) {
                    //error(e);
                    return;
                }
                doZoomImage(uri, width, height);
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
                    if (photo!=null){
                        photo.compress(Bitmap.CompressFormat.PNG, 75, stream);// (0-100)压缩文件
                    }
                    //此处可以把Bitmap保存到sd卡中，具体请看：http://www.cnblogs.com/linjiqin/archive/2011/12/28/2304940.html
                    try {
                        String state = Environment.getExternalStorageState();
                        if (state.equals(Environment.MEDIA_MOUNTED)) {
                            // 这个路径，在手机内存下创建一个定制的文件夹，把图片存在其中。
                            outDir = Environment.getExternalStoragePublicDirectory(STORAGGEID);
                        } else {
                            if (EnlargePhotoActivity.this != null) {
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
                                Log.i(TAG,R.string.picture_save + outFile.getAbsolutePath());
                                //BaseURLs.UPLOADIMAGE
                                doPostSycn(PUT_URL,outFile);

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
                   // mEblarge_photo.setImageBitmap(photo);
                }
                break;
            default:
                break;
        }
    }

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
                    return;// UiUtils.toast(MyApp.getInstance(), R.string.picture_put_failed);
                }

            }


        });
    }

     /*上传图片url  后台绑定*/

    private void putPhotoUrl(String headerUrl) {

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
                    progressBar.dismiss();
                    UiUtils.toast(MyApp.getInstance(), R.string.picture_put_success);
                    // CommonUtils.saveRegisterInfo(register);
                    ImageLoader.load(mContext,register.getData().getUser().getHeadIcon().toString()).into(mEblarge_photo);
                    PreferenceUtil.commitString(AppConstants.HEADICON, register.getData().getUser().getHeadIcon().toString());
                } else {
                    CommonUtils.handlerRequestResult(register.getResult(), register.getMsg());
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                progressBar.dismiss();
                L.e(TAG, "图片上传失败");
                UiUtils.toast(MyApp.getInstance(), R.string.picture_put_failed);
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
                    bmp = MediaStore.Images.Media.getBitmap(EnlargePhotoActivity.this.getContentResolver(), uri);
                    str = MediaStore.Images.Media.insertImage(EnlargePhotoActivity.this.getContentResolver(), bmp, "", "");
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
}
