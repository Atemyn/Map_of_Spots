package com.example.mapofspotsdrawer.retrofit;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.sql.Date;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;
    private final String serverURL;

    // Constructor with initializing retrofit field.
    public RetrofitService(String serverURL) {
        this.serverURL = serverURL;
        initializeRetrofit();
    }

    // Method that initializes Retrofit object.
    private void initializeRetrofit() {
        GsonBuilder builder = new GsonBuilder();

        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context)
                -> new Date(json.getAsJsonPrimitive().getAsLong()));
        retrofit = new Retrofit.Builder()
                // Server Url address that is defined in string resources.
                .baseUrl(serverURL)
                // Adding Converter Factory to process json.
                .addConverterFactory(GsonConverterFactory.create(builder.create()))
                .build();
    }

    // Getter method for retrofit field.
    public Retrofit getRetrofit() {
        return retrofit;
    }
}
