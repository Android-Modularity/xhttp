package com.march.xhttp;

import android.view.View;

import com.babypat.R;
import com.babypat.TitleActivity;
import com.babypat.controller.family.model.UserBabyRelation;

import java.util.List;

import butterknife.OnClick;
import io.reactivex.annotations.NonNull;

/**
 * CreateAt : 2017/7/5
 * Describe :
 *
 * @author chendong
 */
public class MyActivity extends TitleActivity {


    @Override
    public int getLayoutId() {
        return R.layout.test_layout;
    }


    @Override
    public void initViews() {
        super.initViews();
        mLoadingDialog.setOnCancelListener(dialog -> RetrofitManager.cancelRequest(mActivity.hashCode()));
    }

    @OnClick({R.id.btn_request})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.btn_request:

                ApiRequest.getService()
                        .getUser()
                        .compose(RxUtils.transBaseResp())
                        .subscribeWith(new BaseObserver<>(mActivity));

                ApiRequest.getService()
                        .getBabyRelationList(100L, 10, 100L)
                        .doOnNext(userBabyRelationResp -> {
                            Long requestTime = userBabyRelationResp.getData().getRequestTime();
                        })
                        .compose(RxUtils.transWaterFullPageResp())
                        .subscribe(new BaseObserver<List<UserBabyRelation>>(mActivity) {
                            @Override
                            public void onNext(@NonNull List<UserBabyRelation> userBabyRelations) {
                                super.onNext(userBabyRelations);
                                // 处理
                            }
                        });


                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RetrofitManager.cancelRequest(mActivity.hashCode());
    }
}
