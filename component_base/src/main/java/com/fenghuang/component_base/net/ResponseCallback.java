package com.fenghuang.component_base.net;


import android.text.TextUtils;
import android.util.Log;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.fenghuang.component_base.data.SPDataSource;
import com.fenghuang.component_base.tool.RxToast;
import com.fenghuang.component_base.utils.ActivityStackManager;
import com.fenghuang.component_base.utils.ContextManager;
import com.tencent.android.tpush.XGPushManager;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by wangchao on 2018/1/4 17:09
 */
public abstract class ResponseCallback<T extends BaseEntery> implements Observer<T>{

    public abstract void onSuccess(T value);

    public abstract void onFailture(String e);

    private Disposable mDisposable;

    @Override
    public void onNext(T value) {
        Log.e("1111",value + "");
        if(value == null) return;
        if(value.code == 1){
            onSuccess(value);
        }else if(value.code == 3){
            RxToast.error(value.msg + "");
            SPDataSource.put(ContextManager.getAppContext(),SPDataSource.USER_TOKEN,"");
            //注销信鸽
            XGPushManager.unregisterPush(ContextManager.getAppContext());
            //退到登录界面
            ActivityStackManager.getInstance().popAllActivity();
             CC.obtainBuilder("component_user")
                    .setContext(ContextManager.getAppContext())
                    .setActionName("toLoginActivity")
                    .build()
                    .call();
        } else {
            if(!TextUtils.isEmpty(value.msg)) onFailture(value.msg);
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onError(Throwable e) {
        String message = e.getMessage();
        Log.e("1111",e +"");
        if(message == null){
            message = "";
        }
        onFailture(message);
        mDisposable.dispose();
    }

    @Override
    public void onComplete() {
        mDisposable.dispose();
    }
}
