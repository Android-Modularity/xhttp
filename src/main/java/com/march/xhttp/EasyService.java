package com.march.xhttp;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * CreateAt : 2018/1/12
 * Describe : 一些内置的服务
 *
 * @author chendong
 */
public interface EasyService {

    @GET
    Observable<EasyRespBody> download(@Url String url);

}
