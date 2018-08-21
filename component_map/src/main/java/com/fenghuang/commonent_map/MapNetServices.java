package com.fenghuang.commonent_map;

import com.fenghuang.component_base.net.BaseEntery;

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
}
