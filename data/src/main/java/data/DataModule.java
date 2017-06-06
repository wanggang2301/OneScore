package data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import data.api.UserCenterApiService;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import data.repository.UserCenterRepository;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author: Wangg
 * @name：xxx
 * @description: xxx
 * @created on:2017/4/8  14:33.
 */

@Module
public class DataModule {

    // API 地址
    private final String mApiHostUrl;
    private final Context context;

    private final String timeZone;

    private final String lang;

    private static final int DEFAULT_TIMEOUT = 5;

    public DataModule(Context context, String mApiHostUrl, String timeZone, String lang) {
        this.mApiHostUrl = mApiHostUrl;
        this.context = context;
        this.timeZone = timeZone;
        this.lang = lang;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(DataInterceptor dataInterceptor) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient().newBuilder()
                .addInterceptor(dataInterceptor);
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        return httpClientBuilder.build();
    }


    @Provides
    @Singleton
    DataInterceptor provideDataInterceptor() {
        DataInterceptor dataInterceptor = new DataInterceptor(timeZone, lang);
        return dataInterceptor;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(mApiHostUrl)
                .build();
    }

    @Provides
    @Singleton
    UserCenterApiService provideBasketIndexApi(Retrofit retrofit) {
        return retrofit.create(UserCenterApiService.class);
    }


    @Provides
    @Singleton
    UserCenterRepository provideBasketIndexReposeitory(UserCenterApiService basketIndexUserCenterApiService) {
        return new UserCenterRepository(basketIndexUserCenterApiService);
    }




}
