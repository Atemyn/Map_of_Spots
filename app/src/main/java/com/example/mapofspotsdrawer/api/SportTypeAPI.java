package com.example.mapofspotsdrawer.api;

import com.example.mapofspotsdrawer.model.SportType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SportTypeAPI {
    @GET("/api/sport-types/get-all")
    Call<List<SportType>> getAllSportTypes();
}
