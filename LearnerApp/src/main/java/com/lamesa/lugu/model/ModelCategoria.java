package com.lamesa.lugu.model;

import java.io.Serializable;

public class ModelCategoria implements Serializable {

    String imagen;
    String nombre;


    public ModelCategoria(){

    }

    public ModelCategoria(String imagen, String nombre)  {

        this.imagen = imagen;
        this.nombre = nombre;

    }




    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
