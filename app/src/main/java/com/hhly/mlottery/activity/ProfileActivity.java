package com.hhly.mlottery.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.UiUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 个人
 */
public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_nickname;//昵称
    private ImageView mHead_portrait;//头像

    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final String IMAGE_STORAGGEID = "/headview/image.jpg";
    private static final String IMAGE = "image.jpg";
    private static final String STORAGGEID = "/headview";
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int ALBUM_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 3;


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
       /* if (Environment
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
                //setHeadView();

                break;

            default:
                break;
        }
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
            case ALBUM_REQUEST_CODE:
                Log.i(TAG, "相册，开始裁剪");
                Log.i(TAG, "相册 [ " + data + " ]");
                if (data == null) {
                    return;
                }
                startCrop(data.getData());
                break;
            case CAMERA_REQUEST_CODE:
                Log.i(TAG, "相机, 开始裁剪");
                File picture = new File(Environment.getExternalStorageDirectory()
                        + "/temp.jpg");
                startCrop(Uri.fromFile(picture));
                break;
            case CROP_REQUEST_CODE:
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
                                UiUtils.toast(MyApp.getInstance(), "图片保存成功");
                            } else {
                                UiUtils.toast(MyApp.getInstance(), "图片保存失败!");
                            }
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mHead_portrait.setImageBitmap(photo); //把图片显示在ImageView控件上
                }
                break;
            default:
                break;
        }
    }

    /**
     * 开始裁剪
     *
     * @param
     */
    private void startCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");//调用Android系统自带的一个图片剪裁页面,
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");//进行修剪
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 500);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_REQUEST_CODE);
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
