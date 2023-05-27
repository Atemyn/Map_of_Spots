package com.example.mapofspotsdrawer.api;

import com.example.mapofspotsdrawer.model.User;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserAPI {
    @GET("api/get-user")
    Call<User> getUserInfo(@Header("Authorization") String jwtToken);
    @Multipart
    @POST("/api/image-service/upload-user-image")
    Call<ResponseBody> uploadUserImage(@Header("Authorization") String jwtToken,
                                       @Part MultipartBody.Part file);

}
