package com.fenghuang.battery;

import android.app.Application;
import android.app.Notification;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.multidex.MultiDex;

import com.billy.cc.core.component.CC;
import com.fenghuang.component_base.base.BaseApp;
import com.fenghuang.component_base.tool.RxTool;
import com.fenghuang.component_base.utils.ContextManager;
import com.tencent.android.tpush.XGCustomPushNotificationBuilder;
import com.tencent.android.tpush.XGPushManager;

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
        MultiDex.install(this);
        init();
    }

    public void init(){
        XGCustomPushNotificationBuilder build = new XGCustomPushNotificationBuilder();
        build.setSound(
                RingtoneManager.getActualDefaultRingtoneUri(
                        this, RingtoneManager.TYPE_NOTIFICATION)); // 设置声音

        // 设置自定义通知layout,通知背景等可以在layout里设置
        //build.setLayoutId(R.layout.layout_notification);
        // 设置自定义通知内容id
        //build.setLayoutTextId(R.id.ssid);
        // 设置自定义通知标题id
        build.setLayoutTitleId(R.id.title);
        // 设置自定义通知图片id
        build.setLayoutIconId(R.id.icon);
        // 设置自定义通知图片资源
        build.setLayoutIconDrawableId(R.mipmap.ic_launcher);
        // 设置状态栏的通知小图标
        build.setIcon(R.mipmap.ic_launcher);
        // 设置时间id
        build.setLayoutTimeId(R.id.time);
        // 若不设定以上自定义layout，又想简单指定通知栏图片资源
        build.setNotificationLargeIcon(R.mipmap.ic_launcher);
        // 客户端保存build_id
        XGPushManager.setDefaultNotificationBuilder(this, build);
    }
}
