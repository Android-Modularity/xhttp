package com.march.xhttp;


import android.util.SparseArray;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ListCompositeDisposable;

/**
 * CreateAt : 2017.10.10
 * Describe : 同一管理 Retrofit 请求
 *
 * @author chendong
 */
public class RetrofitManager {

    private static SparseArray<ListCompositeDisposable> mDisposableMap = new SparseArray<>();

    // 统一管理所有请求 - 添加请求
    public static void addRequest(int tag, Disposable disposable) {
        ListCompositeDisposable disposableContainer = mDisposableMap.get(tag);
        if (disposableContainer == null) {
            disposableContainer = new ListCompositeDisposable();
            mDisposableMap.put(tag, disposableContainer);
        }
        disposableContainer.add(disposable);
    }

    // 统一管理所有请求 - 取消请求
    public static void cancelRequest(int tag) {
        ListCompositeDisposable disposableContainer = mDisposableMap.get(tag);
        if (disposableContainer != null) {
            disposableContainer.dispose();
            mDisposableMap.remove(tag);
        }
    }

    // 统一管理所有请求 - 取消所有请求
    public static void cancelAllRequest() {
        for (int i = 0; i < mDisposableMap.size(); i++) {
            cancelRequest(mDisposableMap.keyAt(i));
        }
    }
}
