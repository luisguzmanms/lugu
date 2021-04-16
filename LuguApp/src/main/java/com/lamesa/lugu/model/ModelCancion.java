package com.lamesa.lugu.model;

import java.io.Serializable;

public class ModelCancion implements Serializable {


    String id;
    String artista;
    String cancion;
    String categoria;
    String linkYT;

    public ModelCancion() {

    }

    public ModelCancion(String id, String artista, String cancion, String categoria, String linkYT) {

        this.id = id;
        this.cancion = cancion;
        this.artista = artista;
        this.linkYT = linkYT;
        this.categoria = categoria;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCancion() {
        return cancion;
    }

    public void setCancion(String cancion) {
        this.cancion = cancion;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getLinkYT() {
        return linkYT;
    }

    public void setLinkYT(String linkYT) {
        this.linkYT = linkYT;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

}
