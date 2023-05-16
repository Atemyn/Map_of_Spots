package com.example.mapofspotsdrawer.api;

import com.example.mapofspotsdrawer.model.SpotType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SpotTypeAPI {
    @GET("/api/spot-types/get-all")
    Call<List<SpotType>> getAllSpotTypes();
}
