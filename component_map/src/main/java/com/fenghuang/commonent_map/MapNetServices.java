package com.fenghuang.commonent_map;

import com.fenghuang.commonent_map.bean.PositionModel;
import com.fenghuang.commonent_map.bean.RouterModel;
import com.fenghuang.component_base.net.BaseEntery;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Create by wangchao on 2018/8/20 15:38
 */
public interface MapNetServices {
    /**
     * 开关围栏/位置死锁
     */
    @FormUrlEncoded
    @POST("switchEnclosureInterface")
    Observable<BaseEntery<FenchModel>> switchEnclosure(@Field("token")String token,
                                   @Field("status")int status,
                                   @Field("type")int type);


    /**
     *获取轨迹
     */
    @FormUrlEncoded
    @POST("getNavigationByMaxMinTimeInterface")
    Observable<BaseEntery<List<RouterModel>>> getNavigation(@Field("token")String token,
                                                            @Field("startTime")String startTime,
                                                            @Field("endTime")String endTime);

    /**
     *获取围栏状态
     */
    @FormUrlEncoded
    @POST("getStatusInterface")
    Observable<BaseEntery<PositionModel>> getStatus(@Field("token")String token);

    /**
     *获取当前位置
     */
    @FormUrlEncoded
    @POST("getLocationInterface")
    Observable<BaseEntery<FenchModel>> getLocation(@Field("token")String token);
}
