package com.example.mituri.Modelo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ServiceAPI {

    @GET("all/?key=b4fec2c5ace515d11943eab9ffeb12cb")
    Call<List<ModPaises>> getDatos();

    @GET("{CodePais}/all/?key=b4fec2c5ace515d11943eab9ffeb12cb")
    Call<List<ModRegiones>> find(@Path("CodePais") String CodePais);

}
