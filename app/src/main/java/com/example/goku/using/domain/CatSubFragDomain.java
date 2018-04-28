package com.example.goku.using.domain;

import android.accessibilityservice.GestureDescription;

/**
 * Created by Goku on 20/06/2017.
 */

public class CatSubFragDomain {

    private static double myLongitude;
    private static double myLatitude;

    private double lat;
    private double longi;

    private String title;
    private String preco;
    private String logo;
    private String desc;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public CatSubFragDomain() {
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


    public CatSubFragDomain(double lat, double longi) {
        this.lat = lat;
        this.longi = longi;

    }

    private static int fabTab;
    private static int numFrag;

    public static int getNumFrag() {
        return numFrag;
    }

    public static void setNumFrag(int numFrag) {
        CatSubFragDomain.numFrag = numFrag;
    }

    public static int getFabTab() {
        return fabTab;
    }

    public static void setFabTab(int fabTab) {
        CatSubFragDomain.fabTab = fabTab;
    }


    public static double getMyLongitude() {
        return myLongitude;
    }

    public static void setMyLongitude(double myLongitude) {
        CatSubFragDomain.myLongitude = myLongitude;
    }

    public static double getMyLatitude() {
        return myLatitude;
    }

    public static void setMyLatitude(double myLatitude) {
        CatSubFragDomain.myLatitude = myLatitude;
    }


}
