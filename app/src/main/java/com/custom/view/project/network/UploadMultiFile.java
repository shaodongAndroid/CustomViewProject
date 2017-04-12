package com.custom.view.project.network;

import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by Administrator on 2017/3/24.
 */


/**
 * 这里使用了一个新的注解@PartMap，这个注解用于标识一个Map，Map的key为String类型，
 * 代表上传的键值对的key(与服务器接受的key对应),value即为RequestBody，有点类似@Part的封装版本。
 */
public interface UploadMultiFile {

    @Multipart
    @POST("uploadMultiFile")
    Call<Integer> uploadSingleFile(@PartMap Map<String, RequestBody> params,
                                   @Part("params1") RequestBody params1, @Part("params2") RequestBody params2);
}
