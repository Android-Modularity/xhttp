package com.march.xhttp.converts;

import com.march.common.utils.LgUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * CreateAt : 2018/1/8
 * Describe :
 *
 * @author chendong
 */
public final class StringConvertFactory extends Converter.Factory {


    public static StringConvertFactory create() {
        return new StringConvertFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == String.class) {
            return new Converter<ResponseBody, String>() {
                @Override
                public String convert(ResponseBody value) throws IOException {
                    LgUtils.e("use StringConvertFactory");
                    return value.string();
                }
            };
        }
        return super.responseBodyConverter(type, annotations, retrofit);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        if (type == String.class) {
            return new Converter<String, RequestBody>() {

                @Override
                public RequestBody convert(String value) throws IOException {
                    return RequestBody.create(MediaType.parse("text/plain"), value);
                }
            };
        }
        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }
}
