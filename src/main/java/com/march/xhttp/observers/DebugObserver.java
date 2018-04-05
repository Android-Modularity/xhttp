package com.march.xhttp.observers;


import android.content.Context;

import com.march.common.utils.LogUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * CreateAt : 2018/1/12
 * Describe :
 *
 * @author chendong
 */
public class DebugObserver<T> extends BaseObserver<T> {

    public static final String TAG = DebugObserver.class.getSimpleName();

    public DebugObserver(Context context) {
        super(context);
    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        LogUtils.all(TAG, "DebugObserver onSubscribe");
    }

    @Override
    public void onNext(T t) {
        super.onNext(t);
        LogUtils.all(TAG, "DebugObserver", "onNext");
        LogUtils.all(TAG, "数据成功返回 ==> ", t);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        LogUtils.all(TAG, "DebugObserver", "onError");
        LogUtils.e(e);
    }

    @Override
    public void onComplete() {
        super.onComplete();
        LogUtils.all(TAG, "DebugObserver", "onComplete");
    }
}
