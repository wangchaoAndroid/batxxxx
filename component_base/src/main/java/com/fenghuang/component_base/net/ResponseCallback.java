package com.fenghuang.component_base.net;


import android.text.TextUtils;

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
        if(value == null) return;
        if(value.code == 1){
            onSuccess(value);
        }else {
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
