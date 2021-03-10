package com.lamesa.lugu.model;

import java.io.Serializable;

public class ModelListCustom implements Serializable {

    String id;
    String imagen;
    String nombre;


    public ModelListCustom(){

    }

    public ModelListCustom(String id, String imagen, String nombre)  {

        this.id  = id;
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

    public String getId(){
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
