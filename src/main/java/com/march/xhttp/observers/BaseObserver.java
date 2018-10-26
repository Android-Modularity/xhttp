package com.march.xhttp.observers;

import com.march.xhttp.Api;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * CreateAt : 2017/7/5
 * Describe :
 *
 * @author chendong
 */
public class BaseObserver<T> implements Observer<T> {

    public static final String TAG = BaseObserver.class.getSimpleName();

    protected Disposable disposable;

    private int tag;

    public BaseObserver<T> next(Consumer<T> consumer) {
        nextConsumer = consumer;
        return this;
    }

    public BaseObserver<T> error(Consumer<Throwable> consumer) {
        errorConsumer = consumer;
        return this;
    }

    public BaseObserver<T> complete(Action action) {
        completeAction = action;
        return this;
    }

    public BaseObserver<T> finish(Action action) {
        finishAction = action;
        return this;
    }

    private Consumer<T> nextConsumer;
    private Consumer<Throwable> errorConsumer;
    private Action completeAction;
    private Action finishAction;

    public BaseObserver(Object host) {
        this.tag = host.hashCode();
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        disposable = d;
        Api.addRequest(tag, disposable);
    }

    @Override
    public void onNext(@NonNull T t) {
        if (nextConsumer != null) {
            try {
                nextConsumer.accept(t);
            } catch (Exception e) {
                onError(e);
            }
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        onFinish();
        if (errorConsumer != null) {
            try {
                errorConsumer.accept(e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void onComplete() {
        onFinish();
        if (completeAction != null) {
            try {
                completeAction.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // onError or onComplete
    public void onFinish() {
        Api.removeRequest(tag, disposable);

        if (finishAction != null) {
            try {
                finishAction.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
