package com.custom.view.project.network;

import com.custom.view.project.common.UserInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by 少东 on 2017/1/18.
 */

public interface UserInfoService {
    @GET("user/{}/repos")
    Call<UserInfo> getUser(@Path("user") String user);
}
