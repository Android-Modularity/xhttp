package com.march.xhttp.converts;

import com.march.common.utils.LogUtils;
import com.march.xhttp.EasyRespBody;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.annotation.Nullable;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * CreateAt : 2018/1/12
 * Describe : 将原来的返回值转换成 EasyRespBody 方便操作
 *
 * @author chendong
 */
public class EasyRespConvertFactory extends Converter.Factory {

    public static EasyRespConvertFactory create() {
        return new EasyRespConvertFactory();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == EasyRespBody.class) {
            LogUtils.e("use EasyRespConvertFactory");
            return new Converter<ResponseBody, EasyRespBody>() {
                @Override
                public EasyRespBody convert(ResponseBody value) throws IOException {
                    return new EasyRespBody(value);
                }
            };
        }
        return super.responseBodyConverter(type, annotations, retrofit);
    }


}
