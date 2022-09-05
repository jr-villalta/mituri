package com.example.mituri.Clases;

public class SitioTuristico {

    private String IDBlog;
    private String Nombre;
    private String Pais;
    private String Code;
    private String Region;
    private String Coordenadas;
    private String Descripcion;
    private String Foto;
    private Usuario Usuario;

    public SitioTuristico(){

    }

    public SitioTuristico(String IDBlog, String nombre, String pais, String code, String region,
                          String coordenadas, String descripcion, String foto, com.example.mituri.Clases.Usuario usuario) {
        this.IDBlog = IDBlog;
        Nombre = nombre;
        Pais = pais;
        Code = code;
        Region = region;
        Coordenadas = coordenadas;
        Descripcion = descripcion;
        Foto = foto;
        Usuario = usuario;
    }

    public String getIDBlog() {
        return IDBlog;
    }

    public void setIDBlog(String IDBlog) {
        this.IDBlog = IDBlog;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getPais() {
        return Pais;
    }

    public void setPais(String pais) {
        Pais = pais;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getCoordenadas() {
        return Coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        Coordenadas = coordenadas;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    public com.example.mituri.Clases.Usuario getUsuario() {
        return Usuario;
    }

    public void setUsuario(com.example.mituri.Clases.Usuario usuario) {
        Usuario = usuario;
    }
}
