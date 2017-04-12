package com.custom.view.project.network;

import com.custom.view.project.common.UserInfo;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2017/3/24.
 */

public interface IUserBiz {
    @GET("users")
    Call<List<UserInfo>> getUsers();
}
