package com.march.xhttp.config;

import android.app.Application;
import android.content.Context;

import com.march.common.model.WeakContext;
import com.march.common.utils.CheckUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * CreateAt : 2018/1/12
 * Describe :
 *
 * - 多 baseUrl 支持
 * - common Header 支持
 *
 * @author chendong
 */
public class ApiConfig {

    private WeakContext mWeakContext;
    private String baseUrl;
    private Map<String, String> baseUrlMap = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();

    public static ApiConfig newConfig(Application application){
        return new ApiConfig(application);
    }

    private ApiConfig(Application application) {
        mWeakContext = new WeakContext(application);
    }

    // 添加通用 baseUrl
    public ApiConfig addBaseUrl(String baseUrl) {
        if (!CheckUtils.isEmpty(baseUrl)) {
            this.baseUrl = baseUrl;
        }
        return this;
    }

    // 添加多 baseUrl,使用 domain 区分
    public ApiConfig addBaseUrl(String domain, String baseUrl) {
        if (!CheckUtils.isAnyEmpty(domain, baseUrl)) {
            this.baseUrlMap.put(domain, baseUrl);
        }
        return this;
    }

    // 添加通用 header
    public ApiConfig addHeader(String key, String value) {
        if (!CheckUtils.isAnyEmpty(key, value)) {
            this.headers.put(key, value);
        }
        return this;
    }


    public Context getContext() {
        return mWeakContext.get();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Map<String, String> getBaseUrlMap() {
        return baseUrlMap;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
