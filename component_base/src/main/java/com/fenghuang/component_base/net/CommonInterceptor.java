package com.fenghuang.component_base.net;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Create by wangchao on 2017/11/13 09:33
 * 公共参数
 */
public class CommonInterceptor implements Interceptor {
    private static final String TAG = "CommonInterceptor";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        Request.Builder builder = oldRequest.newBuilder();
        HttpUrl.Builder urlBuild = oldRequest.url().newBuilder();
        Request newRequest = null;
        HashMap<String, String> signMap = new HashMap<String, String>();
        String method=oldRequest.method();
        if(METHOD_GET.equals(method)){

            //return chain.proceed(newRequest);
        }
        if(METHOD_POST.equals(method)){
            if (oldRequest.body() instanceof FormBody) {
                FormBody body = (FormBody) oldRequest.body();
                for (int i = 0; i < body.size(); i++) {
                    ILog.d(body.name(i) + "",body.value(i) + "");
                }
               // return chain.proceed(newRequest);
            }
        }
        ILog.d( TAG ,oldRequest.url() + "");
        return chain.proceed(oldRequest);

    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
