package com.example.mituri.Clases;

public class Usuario {

    private String IDUsuario;
    private String Nombre;
    private String Apellido;
    private String Correo;
    private int Tipo_Usuario;

    public Usuario() {
    }

    public Usuario(String IDUsuario, String nombre, String apellido, String correo, int tipo_Usuario) {
        this.IDUsuario = IDUsuario;
        Nombre = nombre;
        Apellido = apellido;
        Correo = correo;
        Tipo_Usuario = tipo_Usuario;
    }

    public String getIDUsuario() {
        return IDUsuario;
    }

    public void setIDUsuario(String IDUsuario) {
        this.IDUsuario = IDUsuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public int getTipo_Usuario() {
        return Tipo_Usuario;
    }

    public void setTipo_Usuario(int tipo_Usuario) {
        Tipo_Usuario = tipo_Usuario;
    }
}
