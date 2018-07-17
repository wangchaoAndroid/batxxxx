package com.fenghuang.battery;

import android.app.Application;

import com.billy.cc.core.component.CC;

/**
 * Create by wangchao on 2018/7/17 09:57
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CC.enableVerboseLog(true);
        CC.enableDebug(true);
        CC.init(this);
        CC.enableRemoteCC(true);
    }
}
