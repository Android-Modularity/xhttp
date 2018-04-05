package com.march.xhttp.extend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.march.common.utils.CheckUtils;
import com.march.common.utils.StreamUtils;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * CreateAt : 2018/3/8
 * Describe :
 *
 * @author chendongeiyo
 */
public class RxResp {


    public static ObservableTransformer<ResponseBody, File> toFile(final File file) {
        return new ObservableTransformer<ResponseBody, File>() {
            @Override
            public ObservableSource<File> apply(Observable<ResponseBody> upstream) {
                return upstream.map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody resp) throws Exception {
                        return StreamUtils.saveStreamToFile(file, resp.byteStream());
                    }
                });
            }
        };
    }


    public static ObservableTransformer<ResponseBody, Bitmap> toBitmap(final BitmapFactory.Options options) {
        return new ObservableTransformer<ResponseBody, Bitmap>() {
            @Override
            public ObservableSource<Bitmap> apply(Observable<ResponseBody> upstream) {
                return upstream.map(new Function<ResponseBody, Bitmap>() {
                    @Override
                    public Bitmap apply(ResponseBody resp) throws Exception {
                        byte[] bytes = StreamUtils.saveStreamToBytes(resp.byteStream());
                        if (!CheckUtils.isEmpty(bytes)) {
                            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                        }
                        return null;
                    }
                });
            }
        };
    }

}
