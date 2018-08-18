package com.fenghuang.component_base.net;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by wangchao on 2017/12/25 11:07
 */
public class RetrofitManager {
    private static volatile RetrofitManager instance;
    private static final int CONNECT_TIME_OUT = 10;
    private static final int READ_TIME_OUT = 10;

    private RetrofitManager(){}

    public static RetrofitManager getInstance(){
        if(instance == null){
            synchronized (RetrofitManager.class){
                if (instance == null){
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }



    private OkHttpClient.Builder setOkhttpClientBuilder(){
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(new CommonInterceptor())//公共参数
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT,TimeUnit.SECONDS)
                .writeTimeout(READ_TIME_OUT,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);

        return okhttpClientBuilder;
    }

    public Retrofit initRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(Api.baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(setOkhttpClientBuilder().build())
                .build();
    }



}
