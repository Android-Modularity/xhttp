package com.march.xhttp.interceptor;

/**
 * CreateAt : 2017/7/1
 * Describe :
 *
 * @author chendong
 */

import com.babypat.extensions.retrofit.RetrofitManager;
import com.babypat.util.L;

import java.io.IOException;
import java.util.Map;

import io.reactivex.annotations.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * CreateAt : 2017/7/1
 * Describe : 添加全局 header
 *
 * @author chendong
 */
public final class HeaderInterceptor implements Interceptor {

    public static final String TAG = HeaderInterceptor.class.getSimpleName();

    // 到达 netWorkInterceptor 时，默认 header 已经添加，不能使用替换的方式，要使用 addHeader 的方式
    // 到达 interceptor 时，还没有添加默认 header ,可以直接替换原来的 header ，后面会追加默认 header
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        L.e(TAG, "添加全局 header ");
        Request.Builder builder = request.newBuilder();
        Map<String, String> headersMap = RetrofitManager.getHeadersMap();
        for (String key : headersMap.keySet()) {
            builder.addHeader(key, headersMap.get(key));
        }
        Response response;
        try {
            response = chain.proceed(builder.build());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return response;
    }
}
