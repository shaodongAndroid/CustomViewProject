package com.custom.view.project.network;

import com.custom.view.project.common.BaseModel;
import com.custom.view.project.common.LoginData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 少东 on 2017/3/7.
 */

public interface ApiService {
    @FormUrlEncoded
    @POST("/v1/login?c=login")
    Observable<BaseModel<LoginData>> login(@Field("phone") String phone, @Field("password") String password);

}
