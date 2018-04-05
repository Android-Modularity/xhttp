package com.march.xhttp.interceptor;

import com.march.common.utils.LogUtils;

import java.io.IOException;

import io.reactivex.annotations.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * CreateAt : 2018/1/12
 * Describe : base Interceptor
 *
 * @author chendong
 */
public abstract class AbstractInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        return proceedResponse(chain.proceed(proceedRequest(chain.request())));
    }

    protected @NonNull
    Request proceedRequest(Request request) {
        return request;
    }

    protected @NonNull
    Response proceedResponse(Response response) {
        return response;
    }
}
