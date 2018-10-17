package com.fenghuang.component_base.download;

import com.fenghuang.component_base.net.BaseEntery;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Create by wangchao on 2018/9/20 09:07
 */
public interface UpgradeNetservices {

    @FormUrlEncoded
    @POST("getAppVersionInterface")
    Observable<BaseEntery<ApkUpgradeInfo>> upgradeInfo(@Field("appVersion") String appVersion);
}
