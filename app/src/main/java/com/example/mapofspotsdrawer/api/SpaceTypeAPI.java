package com.example.mapofspotsdrawer.api;

import com.example.mapofspotsdrawer.model.SpaceType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SpaceTypeAPI {
    @GET("/api/space-types/get-all")
    Call<List<SpaceType>> getAllSpaceTypes();
}
