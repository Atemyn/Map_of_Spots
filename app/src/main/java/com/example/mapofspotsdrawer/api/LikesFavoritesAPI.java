package com.example.mapofspotsdrawer.api;

import com.example.mapofspotsdrawer.model.SpotUserDto;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface LikesFavoritesAPI {
    @GET("/api/spots-users/get-info/{spotId}")
    Call<SpotUserDto> getLikesAndFavoritesForSpot(@Path("spotId") Long spotId,
                                                  @Header("Authorization") String jwtToken);

    @PATCH("/api/spots-users/change-like-state/{spotId}")
    Call<ResponseBody> changeLikeStateForSpot(@Path("spotId") Long spotId,
                                              @Header("Authorization") String jwtToken);

    @PATCH("/api/spots-users/change-favorite-state/{spotId}")
    Call<ResponseBody> changeFavoriteStateForSpot(@Path("spotId") Long spotId,
                                                  @Header("Authorization") String jwtToken);
}
