package com.hhly.mlottery.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 基本功能：图片加载工具
 * 创建：王杰
 * 创建时间：16/4/18
 * 邮箱：w489657152@gmail.com
 */
public class ImageLoader {

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FORWARD_SLASH = "/";

    /**
     * 加载网络图片
     *
     * @param context    context
     * @param url        图片url
     * @param defaultImg 默认图
     * @return ImageRequestBuilder
     */
    public static ImageRequestBuilder load(Context context,
                                           String url,
                                           @DrawableRes int defaultImg) {
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .placeholder(defaultImg)
                .error(defaultImg)
                .centerCrop()
                .crossFade();
        return new ImageRequestBuilder(builder);
    }

    /**
     * 直接加载网络图片
     *
     * @param context context
     * @param url     图片url
     * @return ImageRequestBuilder
     */
    public static ImageRequestBuilder load(Context context, String url) {
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .centerCrop()
                .crossFade();
        return new ImageRequestBuilder(builder);
    }

    /**
     * 直接加载网络图片, 并完整显示(fitCenter)
     *
     * @param context    context
     * @param url        url
     * @param defaultImg 默认图
     * @return ImageRequestBuilder
     */
    public static ImageRequestBuilder loadFitCenter(Context context,
                                                    String url,
                                                    @DrawableRes int defaultImg) {
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .placeholder(defaultImg)
                .error(defaultImg)
                .fitCenter()
                .crossFade();
        return new ImageRequestBuilder(builder);
    }

    /**
     * 直接加载网络图片, 并完整显示(fitCenter)
     *
     * @param context context
     * @param url     url
     * @return ImageRequestBuilder
     */
    public static ImageRequestBuilder loadFitCenter(Context context, String url) {
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .fitCenter()
                .crossFade();
        return new ImageRequestBuilder(builder);
    }

    /**
     * 直接加载SD卡图片
     *
     * @param context context
     * @param file    url
     * @return ImageRequestBuilder
     */
    public static ImageRequestBuilder loadFitCenter(Context context, File file) {
        DrawableRequestBuilder<File> builder = Glide.with(context)
                .load(file)
                .fitCenter()
                .crossFade();
        return new ImageRequestBuilder(builder);
    }

    /**
     * 加载资源图片, 并完整显示(fitCenter)
     *
     * @param context context
     * @param resId   资源文件Id
     * @return ImageRequestBuilder
     */
    public static ImageRequestBuilder loadFitCenter(Context context, int resId) {
        DrawableRequestBuilder<Uri> builder = Glide.with(context)
                .load(resourceIdToUri(context, resId))
                .fitCenter()
                .crossFade();
        return new ImageRequestBuilder(builder);
    }

    /**
     * 加载SD卡图片
     *
     * @param context context
     * @param file    file
     * @return ImageRequestBuilder
     */
    public static ImageRequestBuilder load(Context context, File file) {
        DrawableRequestBuilder<File> builder = Glide.with(context)
                .load(file)
                .centerCrop();
        return new ImageRequestBuilder(builder);
    }

    /**
     * 加载 SD 卡图片并重新设定大小
     *
     * @param context context
     * @param file    file
     * @param point   point
     * @return ImageRequestBuilder
     */
    public static ImageRequestBuilder loadResize(Context context,
                                                 File file,
                                                 Point point) {
        return loadResize(context, file, point.x, point.y);
    }

    /**
     * 加载 SD 卡图片并重新设定大小
     *
     * @param context context
     * @param file    file
     * @param width   长
     * @param height  宽
     * @return ImageRequestBuilder
     */
    public static ImageRequestBuilder loadResize(Context context,
                                                 File file,
                                                 int width,
                                                 int height) {
        DrawableRequestBuilder<File> builder = Glide.with(context)
                .load(file)
                .override(width, height)
                .centerCrop();
        return new ImageRequestBuilder(builder);
    }

    /**
     * 加载网络图片并设置大小
     *
     * @param context context
     * @param url     url
     * @param point   point
     * @return ImageRequestBuilder
     */
    public static ImageRequestBuilder loadResize(Context context,
                                                 String url,
                                                 Point point) {
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .centerCrop()
                .override(point.x, point.y)
                .crossFade();
        return new ImageRequestBuilder(builder);
    }

    /**
     * 加载网络图片并设置大小
     *
     * @param context context
     * @param url     url
     * @param width   width
     * @param height  height
     * @return ImageRequestBuilder
     */
    public static ImageRequestBuilder loadResize(Context context,
                                                 String url,
                                                 int width,
                                                 int height) {
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .centerCrop()
                .override(width, height)
                .crossFade();
        return new ImageRequestBuilder(builder);
    }

    /**
     * 加载 drawable 图片
     *
     * @param context context
     * @param resId   资源文件Id
     * @return ImageRequestBuilder
     */
    public static ImageRequestBuilder load(Context context, int resId) {
        DrawableRequestBuilder<Uri> builder = Glide.with(context)
                .load(resourceIdToUri(context, resId))
                .crossFade();
        return new ImageRequestBuilder(builder);
    }

    /**
     * 加载drawable图片显示为圆形图片
     *
     * @param context context
     * @param resId   资源文件Id
     * @return ImageRequestBuilder
     */
    public static ImageRequestBuilder loadCircle(Context context, int resId) {
        DrawableRequestBuilder<Uri> builder = Glide.with(context)
                .load(resourceIdToUri(context, resId))
                .crossFade()
                .bitmapTransform(new CropCircleTransformation(context));
        return new ImageRequestBuilder(builder);
    }

    /**
     * 加载网络图片显示为圆形图片
     *
     * @param context context
     * @param url     url
     * @return ImageRequestBuilder
     */
    public static ImageRequestBuilder loadCircle(Context context, String url) {
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                //.centerCrop()//网友反馈，设置此属性可能不起作用,在有些设备上可能会不能显示为圆形。
                .bitmapTransform(new CropCircleTransformation(context))
                .crossFade();
        return new ImageRequestBuilder(builder);
    }

    /**
     * 加载SD卡图片显示为圆形图片
     *
     * @param context context
     * @param file    file
     */
    public static ImageRequestBuilder loadCircle(Context context, File file) {
        DrawableRequestBuilder<File> builder = Glide.with(context)
                .load(file)
                //.centerCrop()
                .bitmapTransform(new CropCircleTransformation(context));
        return new ImageRequestBuilder(builder);
    }

    public static ImageRequestBuilder loadCircle(Context context,
                                                 String url,
                                                 @DrawableRes int defaultImg) {
        DrawableRequestBuilder<String> builder = Glide.with(context)
                .load(url)
                .placeholder(defaultImg)
                .error(defaultImg)
                //.centerCrop()//网友反馈，设置此属性可能不起作用,在有些设备上可能会不能显示为圆形。
                .bitmapTransform(new CropCircleTransformation(context))
                .crossFade();
        return new ImageRequestBuilder(builder);
    }

    /**
     * 下载图片
     *
     * @param context context
     * @param url     url
     */
    public static void download(final Context context,
                                String url,
                                final ImageDownloadCallback callback) {
        Observable<File> observable = Observable.from(Glide.with(context)
                .load(url)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL));
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {
                        callback.onDownloadComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDownLoadFailed(e);
                    }

                    @Override
                    public void onNext(File file) {
                        savePicture(context, file, callback);
                    }
                });
    }

    // 将资源 ID 转为 Uri
    public static Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FORWARD_SLASH + resourceId);
    }

    /**
     * 存储图片
     *
     * @param context  context
     * @param file     file
     * @param callback callback
     */
    private static void savePicture(Context context, File file, ImageDownloadCallback callback) {

        File filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (filesDir == null) {
            try {
                throw new FileNotFoundException("外置存储空间不存在");
            } catch (Exception e) {
                e.printStackTrace();
                callback.onDownLoadFailed(e);
            }
        } else {
            String originName = file.getName();
            String name = originName.substring(0, originName.indexOf(".")) + ".png";
            File picFile = new File(filesDir.getPath() + File.separator + name);
            try {
                // 首先保存图片
                StreamUtils.copy(file, picFile);
                // 其次把文件插入到系统图库
                MediaStore.Images.Media.insertImage(context.getContentResolver(), picFile.getAbsolutePath(), name, null);
                // 最后通知图库更新
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filesDir)));
                callback.onDownLoadSuccess(picFile);
            } catch (IOException e) {
                e.printStackTrace();
                callback.onDownLoadFailed(e);
            }
        }
    }

    public interface ImageDownloadCallback {

        void onDownLoadSuccess(File file);

        void onDownLoadFailed(Throwable t);

        void onDownloadComplete();
    }
}
