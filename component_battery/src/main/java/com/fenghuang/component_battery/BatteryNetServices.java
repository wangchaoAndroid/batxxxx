package com.fenghuang.component_battery;

import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_battery.bean.HomeModel;
import com.fenghuang.component_battery.bean.WarnModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Create by wangchao on 2018/8/20 09:17
 */
public  interface BatteryNetServices {
    /**
     * 获取所有告警信息
     */
    @FormUrlEncoded
    @POST("getAlarmtabAllInterface")
    Observable<BaseEntery<List<WarnModel>>> getAlarmtabAll(@Field("token")String token);

    /**
     * 标记为已读
     */
    @FormUrlEncoded
    @POST("getAlarmtabByIdInterface")
    Observable<BaseEntery<WarnModel>> getAlarmtabById(@Field("token")String token,@Field("id")long id);

    /**
     * 首页信息
     */
    @FormUrlEncoded
    @POST("getHomeInterface")
    Observable<BaseEntery<HomeModel>> getHome(@Field("token")String token);
}
