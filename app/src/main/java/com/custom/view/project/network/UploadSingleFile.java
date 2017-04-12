package com.custom.view.project.network;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Administrator on 2017/3/24.
 */


/**
 * 这里@MultiPart的意思就是允许多个@Part了，我们这里使用了3个@Part，第一个我们准备上传个文件，
 * 使用了MultipartBody.Part类型，其余两个均为简单的键值对
 */
public interface UploadSingleFile {

    @Multipart
    @POST("uploadSingleFile")
    Call<Integer> uploadSingleFile(@Part MultipartBody.Part photo,
                                   @Part("params1") RequestBody params1, @Part("params2") RequestBody params2);
}
