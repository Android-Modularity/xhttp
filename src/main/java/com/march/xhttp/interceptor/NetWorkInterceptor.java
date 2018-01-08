package com.march.xhttp.interceptor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * CreateAt : 2017/7/1
 * Describe : 提前检测网络
 *
 * @author chendong
 */
public class NetWorkInterceptor implements Interceptor {

    private Context mContext;

    public NetWorkInterceptor(Context context) {
        mContext = context;
    }

    private class NetWorkException extends IOException {
        NetWorkException() {
            super("网络未连接");
        }
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if (!isNetworkConnected()) {
            throw new NetWorkException();
        } else {
            return chain.proceed(request);
        }
    }


    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && NetworkInfo.State.CONNECTED.equals(activeNetworkInfo.getState());
        } else {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo != null && anInfo.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}