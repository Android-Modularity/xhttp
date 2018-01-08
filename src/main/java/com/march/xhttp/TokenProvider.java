package com.march.xhttp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * CreateAt : 2017/7/6
 * Describe : 持久化token存储
 *
 * @author chendong
 */
public class TokenProvider {

    private volatile static TokenProvider sInst;

    private String accessToken;
    private String refreshToken;

    public static TokenProvider getInst() {
        if (sInst == null) {
            synchronized (TokenProvider.class) {
                if (sInst == null) {
                    sInst = new TokenProvider();
                }
            }
        }
        return sInst;
    }

    private SharedPreferences getSp(Context context) {
        final String NAME = "TOKEN";
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public static final String AK = "AK";
    public static final String RK = "RK";


    public void saveAccessToken(Context context, String accessToken) {
        this.accessToken = accessToken;
        getSp(context).edit().putString(AK, accessToken).apply();
    }

    public String readAccessToken(Context context) {
        if (TextUtils.isEmpty(this.accessToken)) {
            this.accessToken = getSp(context).getString(AK, "");
        }
        return this.accessToken;
    }

    public void saveRefreshToken(Context context, String refreshToken) {
        this.refreshToken = refreshToken;
        getSp(context).edit().putString(RK, refreshToken).apply();
    }

    public String readRefreshToken(Context context) {
        if (TextUtils.isEmpty(this.refreshToken)) {
            this.refreshToken = getSp(context).getString(RK, "");
        }
        return this.refreshToken;
    }
}
