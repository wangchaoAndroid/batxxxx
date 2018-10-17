package com.fenghuang.component_base.download;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Create by wangchao on 2017/12/13 09:25
 * 下拉okhttp进度拦截
 */
public class ProgressHelper {

    public static OkHttpClient.Builder addProgress(final ProgressListener progressListener){
        if (progressListener == null){
            throw new IllegalArgumentException("progressListener is null");
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //添加拦截器，自定义ResponseBody，添加下载进度
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(
                        new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();
            }
        });
        return builder;
    }

}
