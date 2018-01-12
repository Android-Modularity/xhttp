package com.march.xhttp.examples;


import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * CreateAt : 2017/6/30
 * Describe :
 *
 * @author chendong
 */
public interface TestService {


    @GET("http://gank.io/api/search/query/listview/category/Android/count/10/page/1")
    Observable<String> search();

}
