package com.march.xhttp.cookie;

import com.march.common.manager.KVManager;
import com.march.common.utils.CheckUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * CreateAt : 2018/1/12
 * Describe :
 *
 * @author chendong
 */
public class CookieStoreImpl implements CookieStore {

    public static final String COOKIE_STORE_KEY = "COOKIE_STORE_KEY";

    private Map<String, List<Cookie>> mCookies;


    public CookieStoreImpl() {
        load();
    }

    private void save() {
        KVManager.getInst().put(COOKIE_STORE_KEY, mCookies);
    }

    private void load() {
        Object obj = KVManager.getInst().get(COOKIE_STORE_KEY, null);
        if (obj == null) {
            mCookies = new HashMap<>();
        } else {
            mCookies = (Map<String, List<Cookie>>) obj;
        }
    }

    @Override
    public synchronized void saveCookie(HttpUrl url, List<Cookie> cookies) {
        List<Cookie> oldCookies = mCookies.get(url.host());
        List<Cookie> needRemove = new ArrayList<>();
        for (Cookie newCookie : cookies) {
            for (Cookie oldCookie : oldCookies) {
                if (newCookie.name().equals(oldCookie.name())) {
                    needRemove.add(oldCookie);
                }
            }
        }
        oldCookies.removeAll(needRemove);
        oldCookies.addAll(cookies);
    }

    @Override
    public synchronized void saveCookie(HttpUrl url, Cookie cookie) {
        List<Cookie> cookies = mCookies.get(url.host());
        if(cookies == null){
            cookies = new ArrayList<>();
        }
        List<Cookie> needRemove = new ArrayList<>();
        for (Cookie item : cookies) {
            if (cookie.name().equals(item.name())) {
                needRemove.add(item);
            }
        }
        cookies.removeAll(needRemove);
        cookies.add(cookie);
        mCookies.put(url.host(),cookies);
        save();
    }

    @Override
    public synchronized List<Cookie> loadCookie(HttpUrl url) {
        List<Cookie> cookies = mCookies.get(url.host());
        if (cookies == null) {
            cookies = new ArrayList<>();
            mCookies.put(url.host(), cookies);
        }
        return cookies;
    }

    @Override
    public synchronized List<Cookie> getAllCookie() {
        List<Cookie> cookies = new ArrayList<>();
        Set<String> httpUrls = mCookies.keySet();
        for (String url : httpUrls) {
            cookies.addAll(mCookies.get(url));
        }
        return cookies;
    }

    @Override
    public List<Cookie> getCookie(HttpUrl url) {
        List<Cookie> cookies = new ArrayList<>();
        List<Cookie> urlCookies = mCookies.get(url.host());
        if (urlCookies != null) cookies.addAll(urlCookies);
        return cookies;
    }

    @Override
    public synchronized boolean removeCookie(HttpUrl url, Cookie cookie) {
        List<Cookie> cookies = mCookies.get(url.host());
        return (cookie != null) && cookies.remove(cookie);
    }

    @Override
    public synchronized boolean removeCookie(HttpUrl url) {
        return mCookies.remove(url.host()) != null;
    }

    @Override
    public synchronized boolean removeAllCookie() {
        mCookies.clear();
        return true;
    }

}
