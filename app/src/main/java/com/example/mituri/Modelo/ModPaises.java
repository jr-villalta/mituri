package com.example.mituri.Modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModPaises {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("code")
    @Expose
    public String code;

    public ModPaises(String name, String code) {
        this.name = name;
        this.code = code;
    }

}
