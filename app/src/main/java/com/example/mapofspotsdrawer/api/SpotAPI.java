package com.example.mapofspotsdrawer.api;

import com.example.mapofspotsdrawer.model.Spot;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface SpotAPI {
    @GET("api/spots/get-all")
    Call<List<Spot>> getAllSpots();

    @Multipart
    @POST("/api/spots/send-to-moderation")
    Call<ResponseBody> sendSpotToModeration(@Header("Authorization") String jwtToken,
                                            @Part List<MultipartBody.Part> files,
                                            @Part("spotDto") RequestBody spotDto);

    @GET("api/spots/get-in-radius?")
    Call<List<Spot>> getNearbySpots(@Query("lat") Double latitude,
                                    @Query("lon") Double longitude,
                                    @Query("radius") Double radius);
}
