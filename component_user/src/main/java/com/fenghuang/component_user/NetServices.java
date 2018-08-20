package com.fenghuang.component_user;

import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_user.bean.Neiboor;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Create by wangchao on 2017/12/25 13:46
 */
public interface NetServices {

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("loginInterface")
    Observable<BaseEntery<LoginModel>> login(@Field("account") String account,
                                 @Field("passWord") String passWord,
                                 @Field("xgToken") String xgToken
    );

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("regeditInterface")
    Observable<BaseEntery> regeist(@Field("account") String account,
                                     @Field("passWord") String passWord,
                                     @Field("nickName") String nickName,
                                    @Field("smsCode") String smsCode);


    /**
     * 验证码
     * @return
     */
    @FormUrlEncoded
    @POST("sendCodeInterface")
    Observable<BaseEntery<String>> sendCode(@Field("phone") String phone);



    /**
     * 上传缩略图
     * @param params
     * @param parts
     * @return
     */
    @Multipart
    @POST("lyuploadImg")
    Observable<ResponseBody> uploadImg(@PartMap Map<String, RequestBody> params,
                                       @Part List<MultipartBody.Part> parts);


    /**
     * 退出登录
     * @return
     */
    @FormUrlEncoded
    @POST("logoutInterface")
    Observable<BaseEntery> logout(@Field("token") String token);


    /**
     * 找回密码
     * @return
     */
    @FormUrlEncoded
    @POST("retrievePasswordInterface")
    Observable<BaseEntery> retrievePassword(@Field("account") String account,@Field("code") String code,@Field("newPassWord") String newPassWord);




    /**
     * 获得附近店铺
     * @return
     */
    @FormUrlEncoded
    @POST("getNearbyShopInterface")
    Observable<BaseEntery<List<Neiboor>>> getNearbyShop(@Field("token") String token,
                                                        @Field("range") int range,
                                                        @Field("currentLongitude") double currentLongitude,
                                                        @Field("currentLatitudes") double currentLatitudes);
}
