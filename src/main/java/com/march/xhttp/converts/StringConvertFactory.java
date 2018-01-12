package com.march.xhttp.converts;

import com.march.common.utils.LogUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.annotation.Nullable;

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

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == String.class) {
            return new Converter<ResponseBody, String>() {
                @Override
                public String convert(ResponseBody value) throws IOException {
                    LogUtils.e("use StringConvertFactory");
                    return value.string();
                }
            };
        }
        return super.responseBodyConverter(type, annotations, retrofit);
    }
}
