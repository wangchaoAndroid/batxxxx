package com.fenghuang.componentm_mall;

import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.componentm_mall.bean.Product;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Create by wangchao on 2018/8/23 13:38
 */
public interface MallNetServices  {


    /**
     * 首页信息
     */
    @FormUrlEncoded
    @POST("getBatteryAllInterface")
    Observable<BaseEntery<List<Product>>> getBatteryGoods(@Field("token")String token);

    /**
     * 支付
     */
    @FormUrlEncoded
    @POST("purchaseInterface")
    Observable<BaseEntery> purchase(@Field("token")String token,@Field("batteryId") String batteryId
        ,@Field("productNumber") String productNumber);


    /**
     * 绑定电池
     */
    @FormUrlEncoded
    @POST("batteryBindingInterface")
    Observable<BaseEntery> bindBattery(@Field("token")String token
            ,@Field("productNumber") String productNumber);
}
