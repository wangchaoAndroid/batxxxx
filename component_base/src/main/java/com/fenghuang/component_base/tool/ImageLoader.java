package com.fenghuang.component_base.tool;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fenghuang.component_base.R;
import com.fenghuang.component_base.helper.GlideRoundTransform;

/**
 * Create by wangchao on 2018/7/23 09:45
 */
public final class ImageLoader  {
    private static Context mContext;
    private static RequestOptions myOptions;
    public static synchronized void init(Context context){
        myOptions =RequestOptionsManager.init(context);
        mContext = context;
    }


    public static void load(String path, ImageView view){
        if(mContext == null){
            throw new IllegalStateException("not init");
        }
        Glide.with(mContext)
                .load(path)
                .apply(myOptions)
                .into(view);
    }

    public static void load(int res ,ImageView view){
        if(mContext == null){
            throw new IllegalStateException("not init");
        }
        Glide.with(mContext)
                .load(res)
                .apply(myOptions)
                .into(view);
    }





}
