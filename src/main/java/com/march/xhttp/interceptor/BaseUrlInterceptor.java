package com.march.xhttp.interceptor;

import com.march.common.utils.CheckUtils;
import com.march.xhttp.Api;


import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * CreateAt : 2018/1/12
 * Describe : 动态 baseUrl
 *
 * @author chendong
 */
public class BaseUrlInterceptor extends AbstractInterceptor {

    @Override
    protected Request proceedRequest(Request request) {
        String header = request.header(Api.DOMAIN_KEY);
        if (CheckUtils.isEmpty(header)) {
            return super.proceedRequest(request);
        }
        String baseUrl = Api.getXHttpConfig().getBaseUrlMap().get(header);
        if (CheckUtils.isEmpty(baseUrl)) {
            return super.proceedRequest(request);
        }
        HttpUrl mapUrl = HttpUrl.parse(baseUrl);
        if (mapUrl == null) {
            return super.proceedRequest(request);
        }
        HttpUrl httpUrl = request.url().newBuilder()
                .scheme(mapUrl.scheme())
                .host(mapUrl.host())
                .port(mapUrl.port())
                .build();
        return request.newBuilder().url(httpUrl).build();
    }
}
