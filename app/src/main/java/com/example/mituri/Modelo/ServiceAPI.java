package com.example.mituri.Modelo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ServiceAPI {

    @GET("all/?key=652369b7d842cf85d981a8076e10329e")
    Call<List<ModPaises>> getDatos();

    @GET("{CodePais}/all/?key=652369b7d842cf85d981a8076e10329e")
    Call<List<ModRegiones>> find(@Path("CodePais") String CodePais);

}
