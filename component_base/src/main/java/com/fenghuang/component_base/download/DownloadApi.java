package com.fenghuang.component_base.download;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Create by wangchao on 2017/12/13 09:38
 */
public interface DownloadApi {
    @Streaming
    @GET
    Observable<ResponseBody> retrofitDownload(@Url String url);
}
