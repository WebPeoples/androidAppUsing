package com.example.goku.using.domain;

/**
 * Created by Goku on 30/06/2017.
 */

public class WebUsingDomain {


    private String nome;
    private String icone;
    private double lat;
    private double longi;

    private boolean v;

    public boolean isV() {
        return v;
    }

    public void setV(boolean v) {
        this.v = v;
    }

    public static String getLogo() {
        return logo;
    }

    public static void setLogo(String logo) {
        WebUsingDomain.logo = logo;
    }

    private static String logo;

    private String logoProduto;

    public String getLogoProduto() {
        return logoProduto;
    }

    public void setLogoProduto(String logoProduto) {
        this.logoProduto = logoProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    private static String cod_cat;

    public static String getCod_cat() {
        return cod_cat;
    }

    public static void setCod_cat(String cod_cat) {
        WebUsingDomain.cod_cat = cod_cat;
    }

    private static String logoLoja;

    public static String getLogoLoja() {
        return logoLoja;
    }

    public static void setLogoLoja(String logoLoja) {
        WebUsingDomain.logoLoja = logoLoja;
    }

    private String fotosLojas;

    public WebUsingDomain(String fotosLojas) {

        this.fotosLojas = fotosLojas;
    }

    public String getFotosLojas() {
        return fotosLojas;
    }

    public void setFotosLojas(String fotosLojas) {
        this.fotosLojas = fotosLojas;
    }


    private String fotosProdutosLoja;

    public WebUsingDomain() {
    }

    public String getFotosProdutosLoja() {

        return fotosProdutosLoja;
    }

    public void setFotosProdutosLoja(String fotosProdutosLoja) {
        this.fotosProdutosLoja = fotosProdutosLoja;
    }
}
