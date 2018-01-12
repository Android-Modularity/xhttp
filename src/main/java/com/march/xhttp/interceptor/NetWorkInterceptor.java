package com.march.xhttp.interceptor;

import android.content.Context;

import com.march.common.utils.NetUtils;
import com.march.xhttp.exception.RequestException;

import okhttp3.Request;

/**
 * CreateAt : 2017/7/1
 * Describe : 提前检测网络
 *
 * @author chendong
 */
public class NetWorkInterceptor extends AbstractInterceptor {

    private Context mContext;

    public NetWorkInterceptor(Context context) {
        mContext = context;
    }

    @Override
    protected Request proceedRequest(Request request) {
        if (!NetUtils.isNetworkConnected(mContext)) {
            throw new RequestException(RequestException.ERR_NETWORK);
        }
        return super.proceedRequest(request);
    }
}