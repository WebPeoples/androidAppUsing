package com.example.goku.using.activity;

import android.Manifest;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.goku.using.R;
import com.example.goku.using.domain.CatSubFragDomain;
import com.example.goku.using.domain.UsingDB;
import com.example.goku.using.domain.WebUsingMapsDomain;
import com.example.goku.using.prefs.PreferencesManager;
import com.example.goku.using.service.WebUsing;
import com.github.kimkevin.cachepot.CachePot;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class MenuActivity extends BaseActivity {

    private GoogleApiClient mGo;
    private static final String TAG = "LOG";
    private String URL = "http://192.168.1.106:8080/WebUsing/ws/generic/";

    private UsingDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setUpToolBar();
        setUpNavDrawer();
        db = new UsingDB(this);


        //Navegar para busca sem mapa
        Button buscarSemMapa = (Button) findViewById(R.id.menu_viag);
        buscarSemMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesManager.setBoolean(MenuActivity.this,"isNoMap", true);
                Intent intent = new Intent(getBaseContext(), CategoriaActivity.class);
                ActivityOptionsCompat opt = ActivityOptionsCompat.makeCustomAnimation(MenuActivity.this,
                        R.anim.slide_in_left, R.anim.slide_out_left);
                ActivityCompat.startActivity(MenuActivity.this, intent, opt.toBundle());
                MenuActivity.this.finish();
            }
        });

        //Navegar para a categoria
        Button buscar = (Button) findViewById(R.id.menu_buscar);
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PreferencesManager.setBoolean(MenuActivity.this,"isNoMap", false);
                Intent intent = new Intent(getBaseContext(), CategoriaActivity.class);
                ActivityOptionsCompat opt = ActivityOptionsCompat.makeCustomAnimation(MenuActivity.this,
                        R.anim.slide_in_left, R.anim.slide_out_left);
                ActivityCompat.startActivity(MenuActivity.this, intent, opt.toBundle());
                MenuActivity.this.finish();
            }
        });

        //Navegar para favoritos
        Button favorito = (Button) findViewById(R.id.menu_fav);
        favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), FavoritoActivity.class);
                ActivityOptionsCompat opt = ActivityOptionsCompat.makeCustomAnimation(MenuActivity.this,
                        R.anim.slide_in_left, R.anim.slide_out_left);
                ActivityCompat.startActivity(MenuActivity.this, intent, opt.toBundle());
                MenuActivity.this.finish();
            }
        });

        //Navegar para promoções
        Button promo = (Button) findViewById(R.id.menu_promo);
        promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), PromoActivity.class);
                ActivityOptionsCompat opt = ActivityOptionsCompat.makeCustomAnimation(MenuActivity.this,
                        R.anim.slide_in_left, R.anim.slide_out_left);
                ActivityCompat.startActivity(MenuActivity.this, intent, opt.toBundle());
                MenuActivity.this.finish();
            }
        });

        //Navegar para configurações
        Button settings = (Button) findViewById(R.id.menu_config);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
                ActivityOptionsCompat opt = ActivityOptionsCompat.makeCustomAnimation(MenuActivity.this,
                        R.anim.slide_in_left, R.anim.slide_out_left);
                ActivityCompat.startActivity(MenuActivity.this, intent, opt.toBundle());
                MenuActivity.this.finish();
            }
        });

        //Navegar para Localização
        Button local = (Button) findViewById(R.id.menu_local);
        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                ActivityOptionsCompat opt = ActivityOptionsCompat.makeCustomAnimation(MenuActivity.this,
                        R.anim.slide_in_left, R.anim.slide_out_left);
                ActivityCompat.startActivity(MenuActivity.this, intent, opt.toBundle());
                MenuActivity.this.finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*if (PreferencesManager.getString(this, "username").equals("")){
            startActivity(new Intent(this, LoginActivity.class));
        }*/
    }
}

