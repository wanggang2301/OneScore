package com.hhly.mlottery.frame.cpifrag.basketballtask.data;

import com.hhly.mlottery.bean.basket.index.BasketIndexBean;
import com.hhly.mlottery.bean.basket.index.BasketIndexDetailsBean;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.VolleyContentFast;

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

    //接口有分H5、android、IOS，在android环境下需要传入appType=1
    private static final String APP_TYPE = "1";

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


    public void getBasketIndexCenter(Subscriber<BasketIndexBean> subscriber, String date, String type) {
        apiService.getIndexCenter(appendLanguage(), timeZone(), date, type, APP_TYPE)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    public void getBasketIndexCenterDetails(Subscriber<BasketIndexDetailsBean> subscriber, String comId, String thirdId, String oddsType) {
        apiService.getIndexCenterDetails(appendLanguage(), timeZone(), comId, thirdId, oddsType, APP_TYPE)
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


    /**
     * 根据选择语言，改变推送接口语言环境
     *
     * @return
     */
    private String appendLanguage() {
        return VolleyContentFast.returenLanguage();
    }

    /**
     * 获取时区
     *
     * @return
     */
    private String timeZone() {
        L.d("timeZone", AppConstants.timeZone + "");

        return AppConstants.timeZone + "";
    }

}
