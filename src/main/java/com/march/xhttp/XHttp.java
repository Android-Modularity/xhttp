package com.march.xhttp;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.march.xhttp.converts.EasyRespConvertFactory;
import com.march.xhttp.converts.StringConvertFactory;
import com.march.xhttp.examples.TokenAuthenticator;
import com.march.xhttp.interceptor.BaseUrlInterceptor;
import com.march.xhttp.interceptor.HeaderInterceptor;
import com.march.xhttp.interceptor.LogInterceptor;
import com.march.xhttp.interceptor.NetWorkInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    private OkHttpClient mOkHttpClient;
    private Retrofit mRetrofit;
    private XHttpConfig mXHttpConfig;

    public static void init(XHttpConfig xHttpConfig) {
        sInst = new XHttp(xHttpConfig);
    }

    private XHttp(XHttpConfig xHttpConfig) {
        mXHttpConfig = xHttpConfig;
        mOkHttpClient = buildOkHttpClient();
        mRetrofit = buildRetrofit(mOkHttpClient);
        mServiceMap = new HashMap<>();
    }

    public static XHttp getInst() {
        return sInst;
    }


    @SuppressWarnings("unchecked")
    public static <S> S getService(Class<S> serviceClz) {
        try {
            Object apiService = getInst().mServiceMap.get(serviceClz);
            if (apiService != null) {
                return (S) apiService;
            }
            S service = getInst().mRetrofit.create(serviceClz);
            getInst().mServiceMap.put(serviceClz, service);
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
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        // 连接超时
        okHttpBuilder.connectTimeout(5 * 1000, TimeUnit.MILLISECONDS);
        // 读超时
        okHttpBuilder.readTimeout(5 * 1000, TimeUnit.MILLISECONDS);
        // 写超时
        okHttpBuilder.writeTimeout(5 * 1000, TimeUnit.MILLISECONDS);
        // 失败后重试
        okHttpBuilder.retryOnConnectionFailure(true);

        // 检查网络
        okHttpBuilder.addInterceptor(new NetWorkInterceptor(mXHttpConfig.getContext()));
        // 动态 base url
        okHttpBuilder.addInterceptor(new BaseUrlInterceptor());
        // 用来添加全局 Header
        okHttpBuilder.addInterceptor(new HeaderInterceptor());
        // 进行日志打印，扩展自 HttpLoggingInterceptor
        okHttpBuilder.addInterceptor(new LogInterceptor());

        // face book 调试框架
        okHttpBuilder.addNetworkInterceptor(new StethoInterceptor());
        // token校验，返回 403 时
        okHttpBuilder.authenticator(new TokenAuthenticator());
        return okHttpBuilder.build();
    }


    /**
     * 创建 retrofit
     *
     * @param okHttpClient client
     * @return retrofit
     */
    private Retrofit buildRetrofit(OkHttpClient okHttpClient) {
        final Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        // client
        retrofitBuilder.client(okHttpClient);
        // baseUrl
        retrofitBuilder.baseUrl(mXHttpConfig.getBaseUrl());
        // rxJava 调用 adapter
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));

        // 转换为 EasyRespBody
        retrofitBuilder.addConverterFactory(EasyRespConvertFactory.create());
        // 转换为 String
        retrofitBuilder.addConverterFactory(StringConvertFactory.create());
        // 转换为 Json Model
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create(new Gson()));

        return retrofitBuilder.build();
    }


    public XHttpConfig getXHttpConfig() {
        return mXHttpConfig;
    }
}
