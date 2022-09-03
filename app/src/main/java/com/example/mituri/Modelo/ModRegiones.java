package com.example.mituri.Modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModRegiones {

    @SerializedName("region")
    @Expose
    public String region;
    @SerializedName("country")
    @Expose
    public String country;

    public ModRegiones(String region, String country) {
        this.region = region;
        this.country = country;
    }

}
