package com.march.xhttp.config;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * CreateAt : 2018/3/8
 * Describe :
 *
 * @author chendong
 */
public interface InitAdapter {

    void buildOkHttp(OkHttpClient.Builder builder);

    void buildRetrofit(Retrofit.Builder builder);
}