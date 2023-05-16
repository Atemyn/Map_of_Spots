package com.example.mapofspotsdrawer.api;

import com.example.mapofspotsdrawer.model.Spot;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SpotAPI {
    @GET("api/spots/get-all")
    Call<List<Spot>> getAllSpots();
}
