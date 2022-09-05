package com.example.mituri.Modelo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ServiceAPI {

    @GET("all/?key=8e07aa7a15864d27093aed6b174b977f")
    Call<List<ModPaises>> getDatos();

    @GET("{CodePais}/all/?key=8e07aa7a15864d27093aed6b174b977f")
    Call<List<ModRegiones>> find(@Path("CodePais") String CodePais);

}
