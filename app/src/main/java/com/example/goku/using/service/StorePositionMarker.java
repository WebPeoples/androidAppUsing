package com.example.goku.using.service;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Goku on 05/07/2017.
 */

public class StorePositionMarker implements ClusterItem {

    private LatLng latLng;
    private String title;

    public StorePositionMarker(LatLng latLng, String title) {
        this.latLng = latLng;
        this.title = title;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }
}
