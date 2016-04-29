package com.hhly.mlottery.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by A on 2016/1/26.
 */
public class LruImageCache implements ImageLoader.ImageCache {

//    private LruCache<String, SoftReference<Bitmap>> lruCache;
      private LruCache<String, Bitmap> lruCache;

    private Bitmap mDefualtImg;

    public LruImageCache(Context context) {
        int maxSize = 4 * 1024 * 1024; //内存缓存最大值  4M
//        lruCache = new LruCache<String, SoftReference<Bitmap>>(maxSize) {
        lruCache = new LruCache<String, Bitmap>(maxSize) {
            /**
             * 复写sizeOf 方法，
             * @param key
             * @param value
             * @return
             */
            @Override
            protected int sizeOf(String key, Bitmap value) {

//                return value.get().getByteCount();//每个缓存entry的具体大小
                return value.getByteCount();
            }
        };


//        mDefualtImg = BitmapFactory.decodeResource(context.getResources(),R.mipmap.basket_default);

    }

    /**
     * 取图片
     * @param url
     * @return
     */
    @Override
    public Bitmap getBitmap(String url) {
        L.d("###从缓存里面找图片 url = "+url);
//        Bitmap bitmap = lruCache.get(url).get();
        if (lruCache.get(url) != null) {
//            System.out.println("###从缓存里面加载图片");
            return lruCache.get(url);
        }

        return null;
    }

    /**
     * 存图片
     * @param url
     * @param bitmap
     */
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        L.d("###存储到内存 url = "+url);
        if (bitmap != null) {
//            SoftReference<Bitmap> softReference = new SoftReference(bitmap);
            lruCache.put(url, bitmap);
        }
    }

}
