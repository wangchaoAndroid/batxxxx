package com.fenghuang.component_base.download;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Create by wangchao on 2017/12/13 14:56
 * 下载进度回调
 */
public abstract class Callback implements Observer<ResponseBody> {
    public abstract void onProgress(int progress);

    public abstract void onFail(String msg);


    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(ResponseBody value) {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onError(Throwable e) {
        onFail(e.getMessage());
    }
}
