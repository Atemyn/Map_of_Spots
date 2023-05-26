package com.example.mapofspotsdrawer.api;

import com.example.mapofspotsdrawer.model.SpotUserDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface LikesFavoritesAPI {
    @GET("/api/spots-users/get-info/{spotId}")
    Call<SpotUserDto> getLikesAndFavoritesForSpot(@Path("spotId") int spotId,
                                                  @Header("Authorization") String jwtToken);
}
