package com.march.xhttp;

import com.babypat.extensions.retrofit.exception.ApiException;
import com.babypat.net.bean.WaterFallPage;
import com.common.library.llj.base.BaseResponse;
import com.common.library.llj.utils.ListUtil;

import java.util.List;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * CreateAt : 2017/7/5
 * Describe :
 *
 * @author chendong
 */
public class RxUtils {


    // 检测结果是不是正确，如果请求失败的话，将错误汇总
    private static <T extends BaseResponse> ObservableTransformer<T, T> filterOtherStatus() {
        return upstream -> upstream.filter(new Predicate<T>() {
            @Override
            public boolean test(@NonNull T t) throws Exception {
                if (t.getStatus() == 1) {
                    return true;
                } else {
                    throw new ApiException(ApiException.ERR_OTHER_STATUS, t);
                }
            }
        });
    }


    // 检测结果是不是正确，如果请求失败的话，将错误汇总
    private static <T extends BaseResponse> ObservableTransformer<T, T> filterEmptyResp() {
        return upstream -> upstream.filter(new Predicate<T>() {
            @Override
            public boolean test(@NonNull T response) throws Exception {
                if (response == null || response.getData() == null) {
                    return false;
                }
                if (response.getData() instanceof WaterFallPage) {
                    WaterFallPage waterFallPage = (WaterFallPage) response.getData();
                    if (ListUtil.isEmpty(waterFallPage.getList()))
                        return false;
                }
                return true;
            }
        });
    }

    // 将 BaseResp 转换为 Model
    private static <T extends BaseResponse<D>, D> ObservableTransformer<T, D> mapBaseResp() {
        return upstream -> upstream.map(new Function<T, D>() {
            @Override
            public D apply(@NonNull T t) throws Exception {
                return t.getData();
            }
        });
    }

    // 将 WaterFullPage 转换为 List
    private static <T extends BaseResponse<WaterFallPage<D>>, D> ObservableTransformer<T, List<D>> mapListResp() {
        return upstream -> upstream.map(new Function<T, List<D>>() {
            @Override
            public List<D> apply(@NonNull T t) throws Exception {
                return t.getData().getList();
            }
        });
    }

    // 发起请求，将普通结果，转换为可用实体,并将线程切换回主线程
    public static <T extends BaseResponse<D>, D> ObservableTransformer<T, D> transBaseResp() {
        return upstream ->
                upstream.subscribeOn(Schedulers.single())
                        .compose(RxUtils.filterOtherStatus())
                        .compose(RxUtils.mapBaseResp())
                        .observeOn(AndroidSchedulers.mainThread());
    }

    // 发起请求，将瀑布流结果，转换为可用实体,并将线程切换回主线程
    public static <T extends BaseResponse<WaterFallPage<D>>, D> ObservableTransformer<T, List<D>> transWaterFullPageResp() {
        return upstream ->
                upstream.subscribeOn(Schedulers.single())
                        .compose(RxUtils.filterOtherStatus())
                        .compose(RxUtils.mapListResp())
                        .observeOn(AndroidSchedulers.mainThread());
    }


}
