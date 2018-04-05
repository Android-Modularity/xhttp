package com.march.xhttp;

import android.util.SparseArray;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.march.xhttp.config.XHttpConfig;
import com.march.xhttp.config.XHttpConfigService;
import com.march.xhttp.converts.StringConvertFactory;
import com.march.xhttp.cookie.CookieJarImpl;
import com.march.xhttp.cookie.CookieStoreImpl;
import com.march.xhttp.interceptor.BaseUrlInterceptor;
import com.march.xhttp.interceptor.HeaderInterceptor;
import com.march.xhttp.interceptor.LogInterceptor;
import com.march.xhttp.interceptor.NetWorkInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ListCompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * CreateAt : 2017/6/30
 * Describe : 单例，API请求管理
 *
 * @author chendong
 */
public class XHttp {

    public static final String NET_TAG = "|network";
    public static final String TAG = XHttp.class.getSimpleName();
    public static final String DOMAIN_KEY = "xhttp-domain";

    private static XHttp sInst;

    private Map<Class, Object> mServiceMap;

    private SparseArray<ListCompositeDisposable> mDisposableMap;

    private OkHttpClient mOkHttpClient;
    private Retrofit mRetrofit;

    private XHttpConfigService mXHttpConfigService;
    private XHttpConfig mXHttpConfig;

    public static void init(XHttpConfig xHttpConfig) {
        sInst = new XHttp(xHttpConfig, null);
    }

    public static void init(XHttpConfig xHttpConfig, XHttpConfigService service) {
        sInst = new XHttp(xHttpConfig, service);
    }

    public static XHttp getInst() {
        return sInst;
    }

    private XHttp(XHttpConfig xHttpConfig, XHttpConfigService service) {
        mXHttpConfig = xHttpConfig;
        mXHttpConfigService = service;
        mServiceMap = new HashMap<>();
        mDisposableMap = new SparseArray<>();
    }

    private void ensureInitClient() {
        if (mOkHttpClient == null) {
            mOkHttpClient = buildOkHttpClient();
        }
        if (mRetrofit == null) {
            mRetrofit = buildRetrofit(mOkHttpClient);
        }
    }

    @SuppressWarnings("unchecked")
    public static <S> S getService(Class<S> serviceClz) {
        try {
            XHttp inst = getInst();
            inst.ensureInitClient();
            Object apiService = inst.mServiceMap.get(serviceClz);
            if (apiService != null) {
                return (S) apiService;
            }
            S service = inst.mRetrofit.create(serviceClz);
            inst.mServiceMap.put(serviceClz, service);

            return service;
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    /**
     * 创建 OkHttpClient
     *
     * @return OkHttpClient
     */
    private OkHttpClient buildOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 连接超时
        builder.connectTimeout(5 * 1000, TimeUnit.MILLISECONDS);
        // 读超时
        builder.readTimeout(5 * 1000, TimeUnit.MILLISECONDS);
        // 写超时
        builder.writeTimeout(5 * 1000, TimeUnit.MILLISECONDS);
        // 失败后重试
        builder.retryOnConnectionFailure(true);

        // 检查网络
        builder.addInterceptor(new NetWorkInterceptor(mXHttpConfig.getContext()));
        // 动态 base url
        builder.addInterceptor(new BaseUrlInterceptor());
        // 用来添加全局 Header
        builder.addInterceptor(new HeaderInterceptor());
        // 进行日志打印，扩展自 HttpLoggingInterceptor
        builder.addInterceptor(new LogInterceptor());

        builder.cookieJar(new CookieJarImpl(new CookieStoreImpl()));

        // face book 调试框架
        builder.addNetworkInterceptor(new StethoInterceptor());
        // token校验，返回 403 时
        // builder.authenticator(new TokenAuthenticator());

        if (mXHttpConfigService != null) {
            mXHttpConfigService.buildOkHttp(builder);
        }
        return builder.build();
    }


    /**
     * 创建 retrofit
     *
     * @param okHttpClient client
     * @return retrofit
     */
    private Retrofit buildRetrofit(OkHttpClient okHttpClient) {
        final Retrofit.Builder builder = new Retrofit.Builder();
        // client
        builder.client(okHttpClient);
        // baseUrl
        builder.baseUrl(mXHttpConfig.getBaseUrl());
        // rxJava 调用 adapter
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));

        // 转换为 String
        builder.addConverterFactory(StringConvertFactory.create());
        // 转换为 Json Model
        builder.addConverterFactory(GsonConverterFactory.create(new Gson()));

        if (mXHttpConfigService != null) {
            mXHttpConfigService.buildRetrofit(builder);
        }
        return builder.build();
    }


    public XHttpConfig getXHttpConfig() {
        return mXHttpConfig;
    }

    //////////////////////////////  -- 请求队列管理 --  //////////////////////////////

    // 添加一个请求
    public void addRequest(int tag, Disposable disposable) {
        ListCompositeDisposable disposableContainer = mDisposableMap.get(tag);
        if (disposableContainer == null) {
            disposableContainer = new ListCompositeDisposable();
            mDisposableMap.put(tag, disposableContainer);
        }
        disposableContainer.add(disposable);
    }

    // 删除一个请求成功或失败的请求
    public void removeRequest(int tag, Disposable disposable) {
        ListCompositeDisposable disposableContainer = mDisposableMap.get(tag);
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        disposableContainer.delete(disposable);
    }

    // 取消指定 tag 的请求
    public void cancelRequest(int tag) {
        ListCompositeDisposable disposableContainer = mDisposableMap.get(tag);
        if (disposableContainer != null) {
            if (!disposableContainer.isDisposed()) {
                disposableContainer.dispose();
            }
            mDisposableMap.remove(tag);
        }
    }

    // 取消所有请求
    public void cancelAllRequest() {
        for (int i = 0; i < mDisposableMap.size(); i++) {
            cancelRequest(mDisposableMap.keyAt(i));
        }
    }


    @Override
    public String toString() {
        return "";
    }
}
