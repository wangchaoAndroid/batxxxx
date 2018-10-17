package com.fenghuang.component_base.download;

import android.os.Build;
import android.text.TextUtils;

import com.fenghuang.component_base.net.BaseEntery;
import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.net.ParamSignUtils;
import com.fenghuang.component_base.net.ResponseCallback;
import com.fenghuang.component_base.net.RetrofitManager;
import com.fenghuang.component_base.utils.ContextManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Create by wangchao on 2018/9/6 09:47
 */
public  class UploadLogManager {



    private static UploadLogManager mUploadLogManager;

    private  UpgradeNetservices mUpgradeNetservices;

    private UploadLogManager(){
        mUpgradeNetservices = RetrofitManager.getInstance().initRetrofit().create(UpgradeNetservices.class);
    }
    public static UploadLogManager getInstance(){
        if(mUploadLogManager == null){
            mUploadLogManager = new UploadLogManager();
        }
        return mUploadLogManager;
    }


    /**
     * 获取更新信息
     * @param versionName
     * @param callback
     */
    public void upgradeInfo(String versionName ,final ResponseCallback<BaseEntery<ApkUpgradeInfo>> callback){
        mUpgradeNetservices.upgradeInfo(versionName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }









}
