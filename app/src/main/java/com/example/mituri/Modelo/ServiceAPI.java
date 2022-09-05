package com.example.mituri.Modelo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ServiceAPI {

    @GET("all/?key=93fb10d40352cfcd2ca2d831a1f4d264")
    Call<List<ModPaises>> getDatos();

    @GET("{CodePais}/all/?key=93fb10d40352cfcd2ca2d831a1f4d264")
    Call<List<ModRegiones>> find(@Path("CodePais") String CodePais);

}
