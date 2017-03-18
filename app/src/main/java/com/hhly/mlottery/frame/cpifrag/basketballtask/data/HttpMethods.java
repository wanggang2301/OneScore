package com.hhly.mlottery.frame.cpifrag.basketballtask.data;

import com.hhly.mlottery.bean.snookerbean.SnookerRankBean;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author: Wangg
 * @Name：HttpMethods
 * @Description:
 * @Created on:2017/3/17  15:53.
 */

public class HttpMethods {

    private static final String ENDPOINT = "http://m.1332255.com:81/";

    private static final int DEFAULT_TIMEOUT = 5;

    private ApiService apiService;

    private Retrofit retrofit;

    private HttpMethods() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient().newBuilder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(ENDPOINT)
                .build();

        apiService = retrofit.create(ApiService.class);
    }


    public void getFootballDetailsData(Subscriber<SnookerRankBean> subscriber, String lang, String timeZone, String pageSize, String pageNum) {
        apiService.getIndexCenter(lang, timeZone, pageSize, pageNum)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
