package com.custom.view.project.network;

import com.custom.view.project.common.UserInfo;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/3/24.
 */

/**
 *  通过@POST指明url，添加FormUrlEncoded，然后通过@Field添加参数即可。
 */
public interface GetUserByPost {

    @POST("login")
    @FormUrlEncoded
    Call<UserInfo> login(@Field("username") String username, @Field("password") String password);
}
