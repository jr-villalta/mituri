package com.example.mituri.ServiceUtils;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {

    private static Retrofit retrofitPais = null;
    private static Retrofit retrofitRegion = null;

    static Retrofit getPais(String urlBase){
        Log.d("Respuesta","getPais");
        if (retrofitPais==null){
            retrofitPais = new Retrofit.Builder()
                    .baseUrl(urlBase)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofitPais;
    }

    static Retrofit getRegion(String urlBase){
        Log.d("Respuesta","getRegion");
        if (retrofitRegion==null){
            retrofitRegion = new Retrofit.Builder()
                    .baseUrl(urlBase)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofitRegion;
    }

}
