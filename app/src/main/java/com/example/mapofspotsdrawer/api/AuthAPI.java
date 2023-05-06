package com.example.mapofspotsdrawer.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthAPI {
    @POST("/api/auth/register")
    Call<ResponseBody> registerUser(@Body RequestBody requestBody);

    @POST("/api/auth/login")
    Call<ResponseBody> loginUser(@Body RequestBody requestBody);
}
