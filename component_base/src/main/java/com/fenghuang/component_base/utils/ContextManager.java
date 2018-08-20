package com.fenghuang.component_base.utils;

import android.app.Application;
import android.content.Context;

/**
 * Create by wangchao on 2018/8/20 09:33
 */
public final class ContextManager  {
    private static Context appContext;

    public static void init(Application base){
        appContext = base;
    }

    public static Context getAppContext(){
        if(appContext == null){
            throw new RuntimeException("context  not init");
        }
        return appContext;
    }
}
