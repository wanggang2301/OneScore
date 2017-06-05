package com.hhly.mlottery.mvptask.data;

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

    String testurl="http://192.168.10.242:8091/user/promotion/publishPromotions?userId=HHLY00000136&pageNum=1&pageSize=10&loginToken=eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJqd3QiLCJpYXQiOjE0OTY0ODU2MDAsInN1YiI6IntcImlkXCI6XCJISExZMDAwMDAxMzZcIixcInBob25lTnVtXCI6XCIxNTAxMzY5NzEwMVwifSJ9.l4jsTaz5tJM5Q4P3s_UK8US-S3HRfN-lfJZJ67XUS98&sign=70db4a00262f0a351a320c668437493612&lang=zh&timezone=8";

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
                .url(testurl);

        Log.d("bowlurl", url.toString());

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
