package com.march.xhttp.observers;


import com.march.common.utils.LogUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * CreateAt : 2018/1/12
 * Describe :
 *
 * @author chendong
 */
public class EmptyObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        // LogUtils.e(t == null ? "result is null" : t.toString());
    }

    @Override
    public void onError(Throwable e) {
        LogUtils.e(e);
    }

    @Override
    public void onComplete() {

    }
}
