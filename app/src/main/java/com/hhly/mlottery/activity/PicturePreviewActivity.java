package com.hhly.mlottery.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hhly.mlottery.R;
import com.hhly.mlottery.callback.PicturePreviewCallBack;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.hhly.mlottery.widget.ZoomImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author: Wangg
 * @Name：PicturePreviewActivity
 * @Description: 国外资讯图片预览
 * @Created on:2016/9/1 3 15:00.
 */
public class PicturePreviewActivity extends BaseActivity {


    private final static String TAG = "picActivity";

    @BindView(R.id.zoom_view)
    ZoomImageView zoomView;
    @BindView(R.id.ll_info)
    LinearLayout llInfo;

    private Context ctx;
    private GestureDetector gestureDetector;
    private int widthPixels;
    private int heightPixels;

    private Bitmap bitmap;

    private PicturePreviewCallBack picturePreviewCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_preview);
        ButterKnife.bind(this);

        ctx = this;


        //  String url = "http://t11.baidu.com/it/u=705181412,1589083313&fm=72";

        String url = getIntent().getStringExtra("url");
       /* /*//* 缩略图存储在本地的地址 *//**//*
        final String smallPath = getIntent().getStringExtra("smallPath");
        final int identify = getIntent().getIntExtra("indentify", -1);*/

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        widthPixels = metrics.widthPixels;
        heightPixels = metrics.heightPixels;


        picturePreviewCallBack = new PicturePreviewCallBack() {
            @Override
            public void onClick() {
                finish();
            }
        };


        zoomView.setPicturePreviewCallBack(picturePreviewCallBack);


        File bigPicFile = new File(getLocalPath(url));
        if (bigPicFile.exists()) {/* 如果已经下载过了,直接从本地文件中读取 */

            L.d(TAG, "加载已存在SD卡图片");
            zoomView.setImageBitmap(zoomBitmap(BitmapFactory.decodeFile(getLocalPath(url)), widthPixels, heightPixels));

        } else if (!TextUtils.isEmpty(url)) {

            getBitMapFromUrl(url);

           /*

            if (TextUtils.isEmpty(smallPath) && identify != -1) {
                handle.setBackground(BitmapFactory.decodeResource(getResources(), identify));
            } else {
                handle.setBackground(BitmapFactory.decodeFile(smallPath));
            }
            handle.show();*/
        }


        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                L.d("112233", "onFling");
                float x = e2.getX() - e1.getX();
                if (x > 0) {
                    prePicture();
                } else if (x < 0) {
                    nextPicture();
                }
                return true;
            }


            @Override
            public boolean onDoubleTap(MotionEvent e) {
                L.d("112233", "onDoubleTap");


                return super.onDoubleTap(e);
            }
        });
    }

    protected void nextPicture() {
        // TODO Auto-generated method stub
    }

    protected void prePicture() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onResume() {
        super.onResume();
        // recycle();
    }

    public void recycle() {
        if (zoomView != null && zoomView.getDrawable() != null) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) zoomView.getDrawable();
            if (bitmapDrawable != null && bitmapDrawable.getBitmap() != null && !bitmapDrawable.getBitmap().isRecycled()) {
                bitmapDrawable.getBitmap().recycle();
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    L.d(TAG, "加载请求图片");
                    zoomView.setImageBitmap(zoomBitmap(bitmap, widthPixels, heightPixels));
                    break;
            }
        }
    };

    public void getBitMapFromUrl(final String url) {
        bitmap = null;
        VolleyContentFast.requestImage(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                bitmap = response;
                if (bitmap != null) {
                    savePhotoToSDCard(zoomBitmap(bitmap, widthPixels, heightPixels), getLocalPath(url));
                }

                handler.sendEmptyMessage(0);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        L.d("112233", "ccc======" + gestureDetector.onTouchEvent(event));
        return gestureDetector.onTouchEvent(event);
    }


    /**
     * Resize the bitmap
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        if (bitmap == null)
            return bitmap;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        if (scaleWidth < scaleHeight) {
            matrix.postScale(scaleWidth, scaleWidth);
        } else {
            matrix.postScale(scaleHeight, scaleHeight);
        }
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    public static String getLocalPath(String url) {
        String fileName = "temp.png";
        if (url != null) {
            if (url.contains("/")) {
                fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
            }
            if (fileName != null && fileName.contains("&")) {
                fileName = fileName.replaceAll("&", "");
            }
            if (fileName != null && fileName.contains("%")) {
                fileName = fileName.replaceAll("%", "");
            }
        }
        L.d(TAG, "路径-=" + Environment.getExternalStorageDirectory() + "/" + fileName);
        return Environment.getExternalStorageDirectory() + "/" + fileName;
    }

    /**
     * Save image to the SD card
     */
    public static void savePhotoToSDCard(Bitmap photoBitmap, String fullPath) {
        if (checkSDCardAvailable()) {
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            File photoFile = new File(fullPath);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    // if (photoBitmap != null && !photoBitmap.isRecycled()) {
                    // photoBitmap.recycle();
                    // }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean checkSDCardAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


  /*  @OnClick({R.id.zoom_view, R.id.ll_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zoom_view:

                Toast.makeText(getApplicationContext(), "view", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_info:
                L.d("112233", "ll_info关闭");
                finish();
                break;


        }
    }*/


}