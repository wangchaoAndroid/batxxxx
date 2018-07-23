package com.fenghuang.component_base.base;

import android.app.Application;
import android.content.Context;

import com.fenghuang.component_base.tool.ImageLoader;

/**
 * Create by wangchao on 2018/7/23 09:50
 */
public class BaseApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ImageLoader.init(base);
    }

}
