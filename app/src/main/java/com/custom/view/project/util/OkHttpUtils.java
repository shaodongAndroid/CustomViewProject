package com.custom.view.project.util;

import android.util.TimeUtils;
import com.custom.view.project.BuildConfig;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 少东 on 2017/1/17.
 */

public class OkHttpUtils {

    private static OkHttpUtils mInstance ;

    private static OkHttpClient httpClient ;

    static {
        mInstance = new OkHttpUtils();
    }

    public static OkHttpClient getHttpClient(){
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        if(httpClientBuilder.interceptors() != null){
            httpClientBuilder.interceptors().clear();
        }

        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                String path = request.url().encodedPath();
                Log.d("------------path = "+path);
                String query = request.url().query();
                Map<String, Object> queryParam = null ;
                if(query != null){
                    queryParam = new HashMap<>();
                    String queryEntity[] = query.split("&");
                    for (int i=0; i<queryEntity.length; i++){
                        queryParam.put(queryEntity[i].split("=")[0],
                            queryEntity[i].split("=")[1]);
                    }
                }
                if(BuildConfig.DEBUG){
                    Log.d("-----------queryParam = "+queryParam.toString());
                }

                // String signature = make ;
                Request interRequest = chain.request().newBuilder()
                    .headers(Headers.of()).build();

                return null;
            }
        }).connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS);

        httpClient = httpClientBuilder.build();
        return httpClient ;
    }

    private static Retrofit mRetrofit ;

    public static Retrofit getRetrofit(){
        mRetrofit = new Retrofit.Builder()
            .baseUrl("")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create()).build();

        return mRetrofit ;
    }

}
