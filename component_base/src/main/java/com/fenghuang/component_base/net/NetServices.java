package com.fenghuang.component_base.net;

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
     * 上传设备信息
     * @param vender
     * @param model
     * @param osVersion
     * @param platform
     * @param androidId
     * @param src
     * @param isRoot
     * @param gp
     * @param installPath
     * @param mac
     * @return
     */
    @FormUrlEncoded
    @POST("lyuploadDeviceInterface")
    Observable<ResponseBody> uploadDeviceInfo(@Field("vender") String vender,
                                              @Field("model") String model,
                                              @Field("osVersion") String osVersion,
                                              @Field("platform") String platform,
                                              @Field("androidId") String androidId,
                                              @Field("src") String src,
                                              @Field("isRoot") int isRoot,
                                              @Field("gp") int gp,
                                              @Field("installPath") int installPath,
                                              @Field("mac") String mac, @Field("applyId") int appId
    );

    /**
     * 上传访问记录
     */
    @FormUrlEncoded
    @POST("lyuploadDeviceLogInterface")
    Observable<ResponseBody> uploadDeviceLog(@Field("clientStartTime") String clientStartTime,
                                             @Field("clientEndTime") String clientEndTime,
                                             @Field("id") String id);


    /**
     * 上传搜索记录
     * @param type
     * @param searchKey
     * @return
     */
    @FormUrlEncoded
    @POST("lyuploadSearchLogInterface")
    Observable<ResponseBody> uploadSearchLog(@Field("type") int type,
                                             @Field("searchKey") String searchKey);

    /**
     *
     * @param type   1, home 分类  2，点击文件
     * @param classifyType
     * @param fileName
     * @return
     */
    @FormUrlEncoded
    @POST("lyuploadClickDataInterface")
    Observable<ResponseBody> uploadClickData(@Field("type") int type,
                                             @Field("classifyType") int classifyType,
                                             @Field("fileName") String fileName);


    /**
     * 网络搜索接口
     * @param code  国家代码
     * @return
     */
    @FormUrlEncoded
    @POST("lygetUrlSerachInterface")
    Observable<ResponseBody> getUrlSerach(@Field("c") String code);


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


    @FormUrlEncoded
    @POST("lyuploadSafeBoxInterface")
    Observable<ResponseBody> uploadSafeBoxInterface(@Field("safeBoxList") String safeBoxList);



}
