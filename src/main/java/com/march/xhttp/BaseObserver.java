package com.march.xhttp;


import android.content.Context;
import android.text.TextUtils;

import com.babypat.BaoBaoBaseActivity;
import com.babypat.extensions.retrofit.exception.ApiException;
import com.babypat.util.L;
import com.babypat.util.ToastUtil;
import com.common.library.llj.base.BaseResponse;
import com.common.library.llj.utils.NetWorkUtil;
import com.google.gson.JsonParseException;
import com.march.recordsdk.camera.util.Log;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * CreateAt : 2017/7/5
 * Describe :
 *
 * @author chendong
 */
public class BaseObserver<T> implements Observer<T> {

    public static final String TAG = BaseObserver.class.getSimpleName();

    private Context mContext;

    private Disposable mDisposable;

    private boolean isShowDialog;

    public BaseObserver(Context context) {
        this.mContext = context;
        this.isShowDialog = true;
    }

    public BaseObserver(Context context, boolean isShowDialog) {
        this.mContext = context;
        this.isShowDialog = isShowDialog;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

        L.eWithTread(TAG, "onSubscribe");

        mDisposable = d;

        RetrofitManager.addRequest(mContext.hashCode(), mDisposable);

        if (isShowDialog && mContext != null) {
            // 由于不能保证订阅在主线程 showLoadingDialog() 方法要到主线程执行。
            ((BaoBaoBaseActivity) mContext).showLoadingDialog();
        }
    }

    @Override
    public void onNext(@NonNull T t) {
        L.eWithTread(TAG, "onNext -> " + t.toString());
    }

    @Override
    public void onError(@NonNull Throwable e) {
        L.eWithTread(TAG, "onError");
        dispatchException(e);
        onFinish();
    }

    @Override
    public void onComplete() {
        L.eWithTread(TAG, "onComplete");
        onFinish();
    }

    public void onFinish() {
        if (isShowDialog && mContext != null) {
            ((BaoBaoBaseActivity) mContext).dismissLoadingDialog();
        }
    }

    private boolean dispatchException(Throwable e) {
        e.printStackTrace();
        String errMsg;
        if (!NetWorkUtil.isNetworkConnected(mContext)) {
            errMsg = "网络未连接";
            ToastUtil.show(errMsg);
            return true;
        } else {
            // 错误类型补充
            if (e instanceof JsonParseException) {
                Log.e(TAG, "数据解析错误");
                return true;
            } else if (e instanceof ApiException) {
                return handleApiException((ApiException) e);
            }
        }
        return false;
    }


    private boolean handleApiException(ApiException e) {
        BaseResponse baseResponse = e.getBaseResponse();
        String errMsg;
        switch (e.getErrorCode()) {
            case ApiException.ERR_SUCCESS:
                return true;
            case ApiException.ERR_OTHER_STATUS: // success by other status
                if (!handleOtherStatusResp(baseResponse)) {
                    errMsg = "数据返回错误";
                    if (!TextUtils.isEmpty(e.getMessage())) {
                        errMsg = baseResponse.getMessage();
                    }
                    ToastUtil.show(errMsg);
                }

                return true;
            case ApiException.ERR_DATA_INVALID:
                return true;
        }
        return false;
    }

    protected boolean handleOtherStatusResp(BaseResponse baseResponse) {
        return false;
    }
}
