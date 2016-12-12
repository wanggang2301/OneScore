package com.hhly.mlottery.util;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.hhly.mlottery.R;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * description：图片加载工具类
 * author：pz
 * data：2016/10/24
 */
public class ImageLoaderUtils {

    public static void displayScaleImage(Context context, final PhotoView imageView, String url, final PhotoViewAttacher photoViewAttacher) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }

        Glide.with(context).
                load(url)
                .placeholder(R.mipmap.football_gif_default)
                .error(R.mipmap.football_gif_default)
                .into(new GlideDrawableImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        if (photoViewAttacher != null) {
                            photoViewAttacher.update();
                        }
                    }
                });
    }


}
