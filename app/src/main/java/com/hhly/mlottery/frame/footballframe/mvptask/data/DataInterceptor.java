package com.hhly.mlottery.frame.footballframe.mvptask.data;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/4/8  17:49.
 */

public class DataInterceptor implements okhttp3.Interceptor {

    private final String timeZone;

    private final String lang;

    public DataInterceptor(String timeZone, String lang) {
        this.timeZone = timeZone;
        this.lang = lang;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request oldRequest = chain.request();
        HttpUrl oldUrl = oldRequest.url();
        HttpUrl url = oldUrl.newBuilder()
                .addQueryParameter("lang", lang)
                .addQueryParameter("timeZone", timeZone)
                .build();

        Request.Builder requestBuilder = oldRequest.newBuilder()
                .url(url);

        Log.d("bowlurl", url.toString());

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
