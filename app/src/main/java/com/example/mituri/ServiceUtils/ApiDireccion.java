package com.example.mituri.ServiceUtils;

import android.util.Log;

import com.example.mituri.Modelo.ServiceAPI;

public class ApiDireccion {

    public static final String base = "http://battuta.medunes.net/api/";

    public static ServiceAPI getServicePais(){
        return ApiUtils.getPais(base+"country/").create(ServiceAPI.class);
    }

    public static ServiceAPI getServiceRegion(){
        Log.d("Respuesta","getService");
        return ApiUtils.getRegion(base+"region/").create(ServiceAPI.class);
    }

}
