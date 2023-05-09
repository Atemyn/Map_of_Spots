package com.example.mapofspotsdrawer.api;

import com.example.mapofspotsdrawer.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserAPI {
    @GET("api/get-user-info")
    Call<User> getData(@Header("Authorization") String jwtToken);
}
