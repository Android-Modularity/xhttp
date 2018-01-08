package com.march.xhttp.exception;

import com.common.library.llj.base.BaseResponse;

/**
 * CreateAt : 2017/7/6
 * Describe :
 *
 * @author chendong
 */
public class ApiException extends Exception {

    public static final int ERR_SUCCESS      = 1; // 成功
    public static final int ERR_OTHER_STATUS = 2; // 状态码异常
    public static final int ERR_DATA_INVALID = 3; // 数据异常

    private BaseResponse mBaseResponse;
    private int mErrorCode;

    public ApiException(String message) {
        super(message);
    }

    public ApiException() {
    }

    public ApiException(String message, BaseResponse baseResponse) {
        super(message);
        mBaseResponse = baseResponse;
    }

    public ApiException(String message, BaseResponse baseResponse, int errorCode) {
        super(message);
        mBaseResponse = baseResponse;
        mErrorCode = errorCode;
    }

    public ApiException(int errorCode,BaseResponse baseResponse) {
        mBaseResponse = baseResponse;
        mErrorCode = errorCode;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public BaseResponse getBaseResponse() {
        return mBaseResponse;
    }
}
