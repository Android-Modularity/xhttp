package com.march.xhttp.exception;


/**
 * CreateAt : 2017/7/6
 * Describe : 异常
 *
 * @author chendong
 */
public class RequestException extends RuntimeException {

    public static final int ERR_NETWORK = 1; // 网络没有链连接

    private int mErrorCode;
    private String msg;

    public RequestException(String message) {
        super(message);
    }

    public RequestException(int errorCode) {
        mErrorCode = errorCode;
        switch (mErrorCode) {
            case ERR_NETWORK:
                msg = "网络未连接";
                break;
        }
    }

    public int getErrorCode() {
        return mErrorCode;
    }


    public String getMsg() {
        return msg;
    }

}
