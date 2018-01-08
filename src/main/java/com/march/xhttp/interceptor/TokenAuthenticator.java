package com.march.xhttp.interceptor;


import android.support.annotation.NonNull;

import com.babypat.BaoBaoApplication;
import com.babypat.extensions.retrofit.ApiRequest;
import com.babypat.extensions.retrofit.TokenProvider;
import com.common.library.llj.base.BaseResponse;

import java.io.IOException;

import javax.annotation.Nullable;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * CreateAt : 2017/7/6
 * Describe : 返回401时检测到
 *
 * @author chendong
 */
public class TokenAuthenticator implements Authenticator {

    String newToken;

    @Nullable
    @Override
    public Request authenticate(@NonNull Route route, @NonNull Response response) throws IOException {

        TokenProvider tokenProvider = TokenProvider.getInst();
        BaoBaoApplication context = BaoBaoApplication.getInstance();
        String refreshToken = tokenProvider.readRefreshToken(context);

        ApiRequest.getService()
                .refreshToken(refreshToken)
                .map(BaseResponse::getData)
                .subscribe(token -> newToken = token, throwable -> {
                    // 刷新失败，登出
                });

        if (newToken != null) {
            tokenProvider.saveAccessToken(context, newToken);
            return response.request().newBuilder()
                    .header("token", tokenProvider.readAccessToken(context))
                    .build();
        } else {
            // 刷新失败，登出
        }
        return null;
    }
}
