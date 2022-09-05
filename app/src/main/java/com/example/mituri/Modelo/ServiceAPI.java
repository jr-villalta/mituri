package com.example.mituri.Modelo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ServiceAPI {

    @GET("all/?key=5c9c835ea67ced9b3a6b3faec2837397")
    Call<List<ModPaises>> getDatos();
    @GET("{CodePais}/all/?key=5c9c835ea67ced9b3a6b3faec2837397")
    Call<List<ModRegiones>> find(@Path("CodePais") String CodePais);

}
