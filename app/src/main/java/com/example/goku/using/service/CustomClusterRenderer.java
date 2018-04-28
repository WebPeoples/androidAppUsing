package com.example.goku.using.service;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by Goku on 05/07/2017.
 */

public class CustomClusterRenderer extends DefaultClusterRenderer<StorePositionMarker>{

    private final Context context;

    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<StorePositionMarker> cluster, Context context1){
        super(context, map, cluster);
        this.context = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(StorePositionMarker item, MarkerOptions markerOptions) {

    }
}
