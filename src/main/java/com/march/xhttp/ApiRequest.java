package com.march.xhttp;

import com.babypat.common.TokenAuthenticator;
import com.babypat.extensions.retrofit.interceptor.HeaderInterceptor;
import com.babypat.extensions.retrofit.interceptor.LogInterceptor;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

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
public class ApiRequest {

    public static final String NET_TAG = "|network";
    public static final String TAG     = ApiRequest.class.getSimpleName();

    private static final String BASE_URL = "http://api.ibbpp.com";

    private static ApiRequest sInst;
    private        ApiService mApiService;

    private static ApiRequest getInst() {
        if (sInst == null) {
            synchronized (ApiRequest.class) {
                if (sInst == null) {
                    sInst = new ApiRequest();
                }
            }
        }
        return sInst;
    }

    public static ApiService getService() {
        return getInst().mApiService;
    }

    private ApiRequest() {
        mApiService = buildRetrofit(buildOkHttpClient()).create(ApiService.class);
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
        // 清除 interceptors
        okHttpBuilder.interceptors().clear();
        okHttpBuilder.networkInterceptors().clear();

        // 自定义 Interceptor，用来添加全局 Header
        okHttpBuilder.addNetworkInterceptor(new HeaderInterceptor());
        // 自定义 Interceptor，进行日志打印，扩展自 HttpLoggingInterceptor
        okHttpBuilder.addNetworkInterceptor(new LogInterceptor(LogInterceptor.Level.BOTH));
        // 失败后重试
        okHttpBuilder.retryOnConnectionFailure(true);
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
        retrofitBuilder.baseUrl(BASE_URL);
        // rxJava 调用 adapter
        retrofitBuilder.addCallAdapterFactory(
                RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));
        // 数据转换 adapter
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create(new Gson()));

        return retrofitBuilder.build();
    }

}
