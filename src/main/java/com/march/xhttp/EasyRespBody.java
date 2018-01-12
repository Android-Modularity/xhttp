package com.march.xhttp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.march.common.utils.CheckUtils;
import com.march.common.utils.FileUtils;
import com.march.common.utils.StreamUtils;

import java.io.File;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * CreateAt : 2018/1/12
 * Describe :
 *
 * @author chendong
 */
public class EasyRespBody {

    private ResponseBody originResp;

    public EasyRespBody(ResponseBody originResp) {
        this.originResp = originResp;
    }

    public static ObservableTransformer<EasyRespBody, File> toFileRx(final File file) {
        return new ObservableTransformer<EasyRespBody, File>() {
            @Override
            public ObservableSource<File> apply(Observable<EasyRespBody> upstream) {
                return upstream.map(new Function<EasyRespBody, File>() {
                    @Override
                    public File apply(EasyRespBody easyRespBody) throws Exception {
                        return easyRespBody.toFile(file);
                    }
                });
            }
        };
    }

    public static ObservableTransformer<EasyRespBody, Bitmap> toBitmapRx(final File file, final BitmapFactory.Options options) {
        return new ObservableTransformer<EasyRespBody, Bitmap>() {
            @Override
            public ObservableSource<Bitmap> apply(Observable<EasyRespBody> upstream) {
                return upstream.map(new Function<EasyRespBody, Bitmap>() {
                    @Override
                    public Bitmap apply(EasyRespBody easyRespBody) throws Exception {
                        return easyRespBody.toBitmap(file, options);
                    }
                });
            }
        };
    }


    public @Nullable
    File toFile(File file) {
        return StreamUtils.saveStreamToFile(file, originResp.byteStream()) ? file : null;
    }


    public @Nullable
    Bitmap toBitmap(File filePath, BitmapFactory.Options options) {
        File file = toFile(filePath);
        if (CheckUtils.isEmpty(file)) {
            return null;
        }
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }
}
