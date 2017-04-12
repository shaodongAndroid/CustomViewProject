package com.custom.view.project.network;

import com.custom.view.project.common.UserInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2017/3/24.
 */


/**
 * 方法接收一个username参数，并且我们的@GET注解中使用{username}声明了访问路径，
 * 这里你可以把{username}当做占位符，而实际运行中会通过@PATH("username")所标注的参数进行替换。
 */
public interface GetUserByPath {

    @GET("{userName}")
    Call<UserInfo> getUser(@Path("userName") String userName);
}
