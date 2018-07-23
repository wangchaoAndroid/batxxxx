package com.fenghuang.component_base.tool;

import android.content.Context;

import com.bumptech.glide.request.RequestOptions;
import com.fenghuang.component_base.helper.GlideRoundTransform;

/**
 * Create by wangchao on 2018/7/23 09:57
 */
public class RequestOptionsManager {
    private static final int DEFAULT_RADIUS = 20;
    private RequestOptions mRequestOptions;
    private static Context mContext;
    public static synchronized RequestOptions init(Context context){
        mContext = context;
        return new RequestOptions().transform(new GlideRoundTransform(mContext,DEFAULT_RADIUS));

    }

    public void setRadius(int radius){
        mRequestOptions.transform(new GlideRoundTransform(mContext,radius));
    }

    public void setDefaultRadius(){
        mRequestOptions.transform(new GlideRoundTransform(mContext,DEFAULT_RADIUS));
    }



}
