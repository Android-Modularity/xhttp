package com.march.xhttp;

import com.babypat.net.bean.StringResponse;
import com.babypat.net.bean.UserBabyRelationResp;
import com.babypat.net.bean.UserInfoDTOResp;
import com.babypat.net.param.BabyParam;
import com.common.library.llj.base.BaseResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * CreateAt : 2017/6/30
 * Describe :
 *
 * @author chendong
 */
public interface ApiService {

    String PATH_BABY_ID = "babyId";

    String GET_USER_INFO = "api/v1/user/info";

    String DELETE_RM_BABY         = "api/v2/baby/{" + PATH_BABY_ID + "}";
    String PUT_EDIT_BABY          = "api/v2/baby/doEdit/{" + PATH_BABY_ID + "}";
    String GET_BABY_RELATION_LIST = "api/v2/baby/relation/list";
    String GET_REFRESH_TOKEN      = "api/v2/token/refresh";


    @DELETE(DELETE_RM_BABY)
    Call<BaseResponse> deleteBaby(@Path(PATH_BABY_ID) Long babyId);

    @PUT(PUT_EDIT_BABY)
    Call<BaseResponse> putEditBaby(@Path(PATH_BABY_ID) Long babyId, @Body BabyParam babyParam);

    @GET(GET_USER_INFO)
    Observable<UserInfoDTOResp> getUser();

    @GET(GET_BABY_RELATION_LIST)
    Observable<UserBabyRelationResp> getBabyRelationList(@Query("babyId") Long babyId, @Query("limit") int limit, @Query("offset") Long offset);

    @GET(GET_REFRESH_TOKEN)
    Observable<StringResponse> refreshToken(@Query("refreshToken") String refreshToken);
}
