package com.fenghuang.component_base.net;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Create by wangchao on 2018/1/4 17:09
 */
public abstract class ResponseCallback<T>  implements Observer<T>{

    public abstract void onSuccess(T value);

    public abstract void onFailture(String e);

    private Disposable mDisposable;

    @Override
    public void onNext(T value) {
        onSuccess(value);
    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onError(Throwable e) {
        onFailture(e.getMessage());
        mDisposable.dispose();
    }

    @Override
    public void onComplete() {
        mDisposable.dispose();
    }
}
