package com.march.xhttp.observers;

import android.content.Context;

import com.march.common.model.WeakContext;
import com.march.xhttp.XHttp;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * CreateAt : 2017/7/5
 * Describe :
 *
 * @author chendong
 */
public class BaseObserver<T> implements Observer<T> {

    public static final String TAG = BaseObserver.class.getSimpleName();

    private WeakContext mContext;

    private Disposable mDisposable;

    public BaseObserver(Context context) {
        this.mContext = new WeakContext(context);
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        mDisposable = d;
        XHttp.getInst().addRequest(mContext.hashCode(), mDisposable);
    }

    @Override
    public void onNext(@NonNull T t) {

    }

    @Override
    public void onError(@NonNull Throwable e) {
        onFinish();
    }

    @Override
    public void onComplete() {
        onFinish();
    }

    // onError or onComplete
    public void onFinish() {
        XHttp.getInst().removeRequest(mContext.hashCode(), mDisposable);
    }

}
