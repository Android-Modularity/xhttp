package com.march.xhttp.cookie;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * CreateAt : 2018/1/12
 * Describe :
 *
 * @author chendong
 */
public class CookieJarImpl implements CookieJar {

    private CookieStore mCookieStore;

    public CookieJarImpl(CookieStore cookieStore) {
        mCookieStore = cookieStore;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        mCookieStore.saveCookie(url, cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return mCookieStore.loadCookie(url);
    }
}
