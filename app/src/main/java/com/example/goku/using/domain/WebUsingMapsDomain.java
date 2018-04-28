package com.example.goku.using.domain;

/**
 * Created by Goku on 02/07/2017.
 */

public class WebUsingMapsDomain {

    public double[] lat;
    public double[] longi;
    public String[] icone;

    public double[] getLat() {
        return lat;
    }

    public void setLat(double[] lat) {
        this.lat = lat;
    }

    public double[] getLongi() {
        return longi;
    }

    public void setLongi(double[] longi) {
        this.longi = longi;
    }

    public String[] getIcone() {
        return icone;
    }

    public void setIcone(String[] icone) {
        this.icone = icone;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public int tamanho;


}
