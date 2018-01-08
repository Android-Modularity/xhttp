package com.march.xhttp.interceptor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.babypat.extensions.retrofit.ApiRequest;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * CreateAt : 2017/7/1
 * Describe : 自定义日志打印拦截器，扩展自 HttpLoggingInterceptor
 *
 * @author chendong
 */
public final class LogInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");
    public static final  String  TAG  = LogInterceptor.class.getSimpleName();

    public enum Level {
        NONE, // 不打印
        RESPONSE, // 只打印 response
        REQUEST, // 只打印 request
        BOTH //  都打印
    }

    private Level   level;
    private boolean isLogRequestHeaders;
    private boolean isLogResponseHeaders;

    public LogInterceptor(Level level,
                          boolean isLogRequestHeaders,
                          boolean isLogResponseHeaders) {
        this.level = level;
        this.isLogRequestHeaders = isLogRequestHeaders;
        this.isLogResponseHeaders = isLogResponseHeaders;
    }

    public LogInterceptor(Level level) {
        this(level, true, false);
    }

    public LogInterceptor() {
        this(Level.BOTH);
    }

    private void log(String msg) {
        Log.i(TAG + ApiRequest.NET_TAG, msg);
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }
        if (level == Level.BOTH || level == Level.REQUEST)
            logRequest(request);

        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            log("请求失败 msg = " + e.getMessage());
            throw e;
        }
        if (level == Level.BOTH || level == Level.RESPONSE)
            logResponse(response);
        return response;
    }

    private void logResponse(Response response) throws IOException {
        log("=============================== Response Start =======================================================\n");
        long startNs = System.nanoTime();
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        ResponseBody responseBody = response.body();
        log(" [" + response.request().method() + "] " + response.request().url());
        log("Cost:" + tookMs + "ms ,code = " + response.code() + " ,msg = " + response.message());
        // headers
        if (isLogResponseHeaders) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                log("headers:[ " + headers.name(i) + ": " + headers.value(i) + " ]");
            }
        }

        String body = "Response Body: ";
        if (responseBody == null) {
            body = body + "无";
        } else {
            long contentLength = responseBody.contentLength();
            String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
            log("body size = " + bodySize);
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            if (!isPlaintext(buffer)) {
                body = body + "不可读的 body, size = " + buffer.size() + "-byte";
            } else if (contentLength != 0 && charset != null) {
                body = body + buffer.clone().readString(charset);
            }
        }
        log(body);
        log("=============================== Response End =======================================================\n");
    }


    private void logRequest(Request request) throws IOException {
        log("=============================== Request Start =======================================================\n");
        log("[ " + request.method() + " ], url = " + request.url());
        Headers headers = request.headers();
        if (isLogRequestHeaders) {
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                log("headers:[ " + name + ": " + headers.value(i) + " ]");
            }
        }

        String body = "Request Body: ";
        RequestBody requestBody = request.body();
        if (requestBody == null) {
            body = body + "无";
        } else {
            if (isLogRequestHeaders) {
                if (requestBody.contentType() != null) {
                    log("headers:[ " + "Content-Type: " + requestBody.contentType() + " ]");
                }
                if (requestBody.contentLength() != -1) {
                    log("headers:[ " + "Content-Length: " + requestBody.contentLength() + " ]");
                }
            }
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            if (isPlaintext(buffer) && charset != null) {
                body = body + buffer.readString(charset);
            } else {
                body = body + "不可读的 body";
            }
        }
        log(body);
        log("=============================== Request End ===========================================\n");
    }


    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}
