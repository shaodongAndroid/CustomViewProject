package com.custom.view.project.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.custom.view.project.R;
import com.custom.view.project.common.LoginData;
import com.custom.view.project.common.RxObserver;
import com.custom.view.project.common.UserInfo;
import com.custom.view.project.network.ApiService;
import com.custom.view.project.network.GetUserByPath;
import com.custom.view.project.network.GetUserByPost;
import com.custom.view.project.network.IUserBiz;
import com.custom.view.project.network.UserInfoService;
import com.custom.view.project.util.Log;
import com.custom.view.project.util.NetworkUtils;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.reflect.Proxy.newProxyInstance;

public class RetrofitActivity extends AppCompatActivity {

    // @BindColor(R.color.cardview_dark_background)
    private TextView tvText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        ButterKnife.bind(this);
        initData(UserInfoService.class);
        // initData(UserInfo.class);
    }


    private void initData(final Class<?> service) {

        UserInfoService userInfoService = (UserInfoService) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service },
            new InvocationHandler() {
                @Override public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable {
                    Log.d("-----------getDeclaringClass = "+method.getDeclaringClass());
                    Log.d("-----------Object.class = "+Object.class);
                    return null;
                }
            });
        userInfoService.getUser("");
    }


    @OnClick({R.id.btn_get_data, R.id.btn_upload_data}) // R.id.btn_get_by_path, R.id.btn_get_by_query
    public void onBtnClick(View view){
        switch (view.getId()){
            case R.id.btn_get:
                useGet();
                break;
            case R.id.btn_get_by_path:
                useGetAndPath();
                break;
            case R.id.btn_get_by_query:
                useGetAndQuery();
                break;
            case R.id.btn_post:
                usePost();
                break;
            case R.id.btn_single:
                uploadSingleFile();
                break;
            case R.id.btn_multi:
                uploadMultiFile();
                break;
            case R.id.btn_get_data:
                retrofitGetData();
                break;
        }
    }

    private void test(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
            .callFactory(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

        newProxyInstance(GetUserByPath.class.getClassLoader(),
            new Class<?>[] { GetUserByPath.class }, new InvocationHandler() {
                @Override public Object invoke(Object o, Method method, Object[] objects)
                    throws Throwable {
                    return null;
                }
            });
    }

    /**
     *可以看到，可以在Map中put进一个或多个文件，键值对等，当然你也可以分开，单独的键值对也可以使用@Part
     */
    private void uploadMultiFile(){
        File file = new File(Environment.getExternalStorageDirectory(), "messenger_01.png");
        RequestBody photo = RequestBody.create(MediaType.parse("image/png"), file);
        Map<String,RequestBody> photos = new HashMap<>();
        photos.put("photos\"; filename=\"icon.png", photo);
        photos.put("username",  RequestBody.create(null, "abc"));

        // Call<User> call = userBiz.registerUser(photos, RequestBody.create(null, "123"));
    }
    private void uploadSingleFile(){
        File file = new File(Environment.getExternalStorageDirectory(), "icon.png");
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("photos", "icon.png", photoRequestBody);

        // Call<UserInfo> call = userBiz.registerUser(photo, RequestBody.create(null, "abc"), RequestBody.create(null, "123"));
    }

    /**
     * 两种requestBody，一个是FormBody，一个是MultipartBody，前者以表单的方式传递简单的键值对，
     * 后者以POST表单的方式上传文件可以携带参数，retrofit也二者也有对应的注解
     */
    private void usePost(){
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        GetUserByPost getUserByPost = retrofit.create(GetUserByPost.class);
        Call<UserInfo> login = getUserByPost.login("userName", "password");
        login.enqueue(new Callback<UserInfo>() {
            @Override public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {

            }

            @Override public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        });
    }

    /**
     *
     * 查询参数的设置@Query
     * http://baseurl/users?sortby=username
     *
     * http://baseurl/users?sortby=id
     *这样我们就完成了参数的指定，当然相同的方式也适用于POST，只需要把注解修改为@POST即可。
     */
    private void useGetAndQuery() {
        /**
         * public interface IUserBiz
         {
            @GET("users")
            Call<List<User>> getUsersBySort(@Query("sortby") String sort);
         }
         */

        // Call<List<User>> call = userBiz.getUsersBySort("username");
    }

    /**
     * 动态的url访问@PATH
     *
     * 用于访问zhy的信息 http://192.168.1.102:8080/springmvc_users/user/dsd
     *
     * 用于访问lmj的信息 http://192.168.1.102:8080/springmvc_users/user/lmj
     */
    private void useGetAndPath() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.31.242:8080/springmvc_users/user/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        GetUserByPath getUserByPath = retrofit.create(GetUserByPath.class);
        Call<UserInfo> dsdCall = getUserByPath.getUser("dsd");
        // Call<UserInfo> lmjCall = getUserByPath.getUser("lmj");
        dsdCall.enqueue(new Callback<UserInfo>() {
            @Override public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {

            }

            @Override public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        });

    }


    /**
     * 一般的get请求
     */
    private void useGet() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.31.242:8080/springmvc_users/user/")
            .addConverterFactory(GsonConverterFactory.create()) //GsonConverterFactory完成对象的转化
            .build();

        IUserBiz iUserBiz = retrofit.create(IUserBiz.class);
        Call<List<UserInfo>> users = iUserBiz.getUsers();
        users.enqueue(new Callback<List<UserInfo>>() {
            @Override
            public void onResponse(Call<List<UserInfo>> call, Response<List<UserInfo>> response) {
                Log.d("-----------response.body() = "+response.body());
            }

            @Override public void onFailure(Call<List<UserInfo>> call, Throwable t) {

            }
        });
        try {
            users.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void retrofitGetData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserInfoService userInfoService = retrofit.create(UserInfoService.class);
        Call<UserInfo> call = userInfoService.getUser("");
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                UserInfo result = response.body();
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        });
    }

    public void testRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

       ApiService Api = retrofit.create(ApiService.class);
        Api.login("","").compose(NetworkUtils.<LoginData>handleResult())
        .subscribe(new RxObserver<LoginData>(this, "加载中") {
            @Override
            public void onNexted(LoginData loginData) {

            }

            @Override
            public void onError(String message) {

            }
        });

    }
}
