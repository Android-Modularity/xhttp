package com.march.xhttp.examples;

import com.march.xhttp.XHttp;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * CreateAt : 2018/1/8
 * Describe :
 *
 * @author chendong
 */
public class TestPage {

    public void test() {
         XHttp.getService(TestService.class).search()
                .map(new Function<String, Boolean>() {
                        @Override
                        public Boolean apply(String s) throws Exception {
                            return s.length() > 0;
                        }
                    }).subscribe(new Observer<Boolean>() {
             @Override
             public void onSubscribe(Disposable d) {

             }

             @Override
             public void onNext(Boolean aBoolean) {

             }

             @Override
             public void onError(Throwable e) {

             }

             @Override
             public void onComplete() {

             }
         });

    }
}
