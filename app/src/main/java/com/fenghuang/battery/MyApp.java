package com.fenghuang.battery;

import android.app.Application;

import com.billy.cc.core.component.CC;
import com.fenghuang.component_base.base.BaseApp;
import com.fenghuang.component_base.tool.RxTool;
import com.fenghuang.component_base.utils.ContextManager;

/**
 * Create by wangchao on 2018/7/17 09:57
 */
public class MyApp extends BaseApp {
    @Override
    public void onCreate() {
        super.onCreate();
        CC.enableVerboseLog(true);
        CC.enableDebug(true);
        CC.init(this);
        CC.enableRemoteCC(true);
        RxTool.init(this);
        ContextManager.init(this);
    }
}
