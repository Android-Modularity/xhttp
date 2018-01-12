package com.march.xhttp.examples;

import java.io.IOException;

import javax.annotation.Nullable;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * CreateAt : 2018/1/8
 * Describe : 返回 401 回调
 *
 * @author chendong
 */
public class TokenAuthenticator implements okhttp3.Authenticator {

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @Nullable Response response) throws IOException {
        return null;
    }

}

