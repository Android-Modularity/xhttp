package com.march.xhttp.exception;


/**
 * CreateAt : 2017/7/6
 * Describe : 异常
 *
 * @author chendong
 */
public class RequestException extends RuntimeException {

    public static final int ERR_NETWORK = 1; // 网络没有链连接

    private int code;
    private String msg;

    public RequestException(String message) {
        super(message);
    }

    public RequestException(int errorCode) {
        code = errorCode;
        switch (code) {
            case ERR_NETWORK:
                msg = "网络未连接";
                break;
        }
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
