package com.example.goku.using.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.goku.using.R;
import com.example.goku.using.domain.CatSubFragDomain;
import com.example.goku.using.domain.UsingDB;
import com.example.goku.using.prefs.PreferencesManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.zze;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.ClusterItem;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MapsActivity extends BaseActivity
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ClusterItem {

    //Variável que contém o mapa em si
    private GoogleMap mMap;

    //TAG para os logs
    private String TAG = "MAPA";

    //Variáveis que serão utilizadas posteriormente
    private double[] lat;
    private double[] longi;
    private double myLat;
    private double myLongi;
    private String[] icon;
    private String[] cpf_cnpj;
    private CheckBox[] cList;
    private String URL_icon;
    private List<Marker> markList;
    private List<String> valuesToCheckBox;
    private boolean flag;
    private Bundle bundle;
    private LinearLayout ll;
    private int height;
    private int width;
    private DrawerLayout drawerLayout;
    boolean firstTime = true;

    //Objeto responsável por se conectaro a api de localização do GoogleClient
    private GoogleApiClient mGo;

    //Objeto circle responsável por ser o desenho da posição do dispositivo no mapa
    private Circle circle;
    private CatSubFragDomain ct;

    //Objeto responsável por requisitar a localização atual do Dispositivo
    private LocationRequest mLocationRequest;

    //Elementos UI
    private FloatingActionButton fab;
    private FloatingActionButton fab_refresh;
    private int cor;
    private int branco;
    private boolean count;

    //Intervalos de atualizações da localização
    private static int UPDATE_INTERVAL = 40000; // 10 seg
    private static int FAST_INTERVAL = 5000;


    private Set<PicassoMaker> poiTargets = new HashSet<PicassoMaker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        bundle = getIntent().getExtras();

        setUpToolBar();
        setUpNavDrawer();

        //LinearLayout para adicionar as CheckBoxs
        ll = (LinearLayout) findViewById(R.id.maps_activity_checks_boxs);

        //Cores para as checkboxes
        branco = ContextCompat.getColor(this, R.color.branco);

        //Flag para pegar a posição atual do dispositivo somente uma vez para criar o raio em km de exibição das lojas
        flag = true;


        NavigationView navigationView = (NavigationView) findViewById(R.id.maps_activity_nav_check_box);
        //Definindo transparência para nav drawer da DIREITA
        navigationView.getBackground().setAlpha(122);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_menu);

        //BT FAB para pesquisar ESPECIALIDADES
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.maps_fab_nav_check);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout != null) {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });

        Button btn_pesquisa = (Button) findViewById(R.id.maps_activity_pesquisar);
        //Estou checando se o usuário vem pelo bt localização, se vier eu desabilito as especialidades
        if (bundle != null) {
            load_checkbox(bundle.getString("subcat"));
        } else {
            floatingActionButton.setVisibility(View.INVISIBLE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, navigationView);
        }


        btn_pesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();

                //Define a posição baseado nos valores que foram definidos no objeto myLat, myLongi pegados no onLocation
                LatLng latLng = new LatLng(myLat, myLongi);

                //Verificamos se o Circle está vazio, se não estiver, esvaziamos
                if (circle != null) {
                    circle.remove();
                }
                CircleOptions mapcircle;

                //Adicionando o circulo do local onde estiver o dispositivo atualmente
                mapcircle = new CircleOptions().center(latLng);
                mapcircle.fillColor(cor);
                mapcircle.radius(10);

                //Adiciona o Circle no mapa
                circle = mMap.addCircle(mapcircle);
                //Define a cor no Circle (a cor foi configurado no método onCreate)
                circle.setStrokeColor(cor);
                markList = new ArrayList<Marker>();

                int a = 0;
                //Checando se a lista com as Checkboxes está vazia
                if (cList != null) {
                    for (int i = 0; i < cList.length; i++) {
                        //Criando controlodar para saber a quantidade de Checkboxes habilitadas
                        if (cList[i].isChecked()) {
                            a++;
                        }
                    }
                }

                //Checando o controlodar para saber se alguma checkbox foi habilitada
                if (a != 0) {
                    Gson gson = new Gson();
                    List<String> valuesChecked = new ArrayList<String>();

                    //Adicionando a posição atual nos índices 0 e 1 para criar raio em km para exibição das lojas
                    valuesChecked.add(0, String.valueOf(myLat));
                    valuesChecked.add(1, String.valueOf(myLongi));

                    //Definindo controlador para começar a partir de 2 pois nas posições anteriores se encontra a posição do dispositivo
                    int b = 2;
                    for (int i = 0; i < cList.length; i++) {

                        //Adicionando somente os valores das checkboxes que foram habilitadas
                        if (cList[i].isChecked()) {
                            valuesChecked.add(b++, valuesToCheckBox.get(i));
                        }
                    }
                    //Executando o método que subirá os dados para fazer a busca no banco
                    getLojasByEspec(gson.toJson(valuesChecked));
                } else {
                    Log.d(TAG, "onClick: valuesToCheckBox is null.");
                    load_data_comraio(myLat, myLongi, bundle.getString("subcat"));
                }

                drawerLayout.closeDrawer(GravityCompat.END);

            }
        });
        markList = new ArrayList<>();

        //Instanciando a cor rosa que será utilizada para o Circle
        cor = ContextCompat.getColor(this, R.color.rosa);
        fab_refresh = (FloatingActionButton) findViewById(R.id.maps_fab_att);
        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();

                //Define a posição baseado nos valores que foram definidos no objeto myLat, myLongi pegados no onLocation
                LatLng latLng = new LatLng(myLat, myLongi);
                //Verificamos se o Circle está vazio, se não estiver, esvaziamos
                if (circle != null) {
                    circle.remove();
                }
                CircleOptions mapcircle;
                //Adicionando o circulo do local onde estiver o dispositivo atualmente
                mapcircle = new CircleOptions().center(latLng);
                mapcircle.fillColor(cor);
                mapcircle.radius(10);
                //Adiciona o Circle no mapa
                circle = mMap.addCircle(mapcircle);
                //Define a cor no Circle (a cor foi configurado no método onCreate)
                circle.setStrokeColor(cor);
                markList = new ArrayList<Marker>();
                if (bundle != null) {
                    int a = 0;
                    //Checando se a lista com as Checkboxes está vazia
                    if (cList != null) {
                        for (int i = 0; i < cList.length; i++) {
                            //Criando controlodar para saber a quantidade de Checkboxes habilitadas
                            if (cList[i].isChecked()) {
                                a++;
                            }
                        }
                    }

                    //Checando o controlodar para saber se alguma checkbox foi habilitada
                    if (a != 0) {
                        Gson gson = new Gson();
                        List<String> valuesChecked = new ArrayList<String>();

                        //Adicionando a posição atual nos índices 0 e 1 para criar raio em km para exibição das lojas
                        valuesChecked.add(0, String.valueOf(myLat));
                        valuesChecked.add(1, String.valueOf(myLongi));

                        //Definindo controlador para começar a partir de 2 pois nas posições anteriores se encontra a posição do dispositivo
                        int b = 2;
                        for (int i = 0; i < cList.length; i++) {

                            //Adicionando somente os valores das checkboxes que foram habilitadas
                            if (cList[i].isChecked()) {
                                valuesChecked.add(b++, valuesToCheckBox.get(i));
                            }
                        }
                        //Executando o método que subirá os dados para fazer a busca no banco
                        getLojasByEspec(gson.toJson(valuesChecked));
                    } else {
                        Log.d(TAG, "onClick: valuesToCheckBox is null.");
                        load_data_comraio(myLat, myLongi, bundle.getString("subcat"));
                    }

                } else {
                    lojasLocalAc();
                }
            }
        });

        //Instanciando o FloatingActionButton que leva o usuário para sua posição
        fab = (FloatingActionButton) findViewById(R.id.maps_fab);

        //Criando um listener para o FAB
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Checando se há permissão para obter a posição precisa do dispositivo
                if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                //Chamando o serviço de localização da Api GoogleClient
                Location l = LocationServices.FusedLocationApi.getLastLocation(mGo);


                //Chamando o método que reposiciona a câmera do mapa baseado na posição atual do usuário
                setMapLocation(l);
            }
        });

        //Primeiro nós precisamos verificar a disponibilidade dos serviços google
        if (checkPlayServices()) {

            //Construindo o GoogleApi Client
            buildGoogleApiClient();

            //Criando a requisição para obter a posição atual do dispositivo
            createLocationRequest();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void addCircle() {
        mMap.clear();
        LatLng latLng = new LatLng(myLat, myLongi);
        //Verificamos se o Circle está vazio, se não estiver, esvaziamos
        if (circle != null) {
            circle.remove();
        }
        CircleOptions mapcircle;
        //Adicionando o circulo do local onde estiver o dispositivo atualmente
        mapcircle = new CircleOptions().center(latLng);
        mapcircle.fillColor(cor);
        mapcircle.radius(10);
        //Adiciona o Circle no mapa
        circle = mMap.addCircle(mapcircle);
        //Define a cor no Circle (a cor foi configurado no método onCreate)
        circle.setStrokeColor(cor);
        //toast(MapsActivity.this, "Latitude: " + lat.latitude);
        markList = new ArrayList<>();
    }

    //Método que será executado quando o mapa estiver pronto
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (bundle == null) {
            lojasLocalAc();
        }
        //Definindo uma função para o clique nas marcas das lojas
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("cpf_cnpj", marker.getTitle());
                if (bundle != null) {
                    bundle1.putString("subcat", bundle.getString("subcat"));
                }
                Intent intent = new Intent(getApplicationContext(), LojaActivity.class);
                startActivity(intent.putExtras(bundle1));
                return true;
            }
        });
    }

    //Método que será executado quando a conexão com o serviço da api GoogleClient
    @Override
    public void onConnected(@Nullable Bundle bundlea) {

        //Checa se há permissão para obter a localização atual do dispositivo
        if (ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //Checa se o GPS está ativado, caso não esteja, ele envia o usuário até as configurações de location
          /*  LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                toast(getApplicationContext(), "Ative o GPS.");
            } else {*/
        //Caso o GPS esteja habilitado. Chamamos o método que atualiza a posição atual do dispositivo
        startLocationUpdates();

        //}
    }

    //Método que é chamado caso a conexão seja supendida
    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(MapsActivity.this, "Conexão interrompida.", Toast.LENGTH_SHORT).show();
    }

    //Método que é chamado caso a conexão falhe
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(MapsActivity.this, "Erro ao conectar: " + connectionResult, Toast.LENGTH_SHORT).show();
    }

    //Método que é chamado quando a posição do usuário é alterada
    @Override
    public void onLocationChanged(Location location) {
        //Verificamos se a localização é nula
        if (location == null) {
            Log.d(TAG, "onLocationChanged: location is null.");
        } else {
            myLat = location.getLatitude();
            myLongi = location.getLongitude();
            float[] l = new float[1];
            android.location.Location.distanceBetween(myLat, myLongi, -23.6527622, -46.7124, l);


            Log.d(TAG, "onCreate: " + l[0]);

            if (flag) {
                flag = false;
                if (bundle != null) {
                    load_data_comraio(myLat, myLongi, bundle.getString("subcat"));
                }
            }

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            //Atualizando a câmera para a posição do dispositivo
            CameraUpdate up = CameraUpdateFactory.newLatLngZoom(latLng, 16);
            //Anima a câmera para a posição atual do dispositivo

            if (firstTime) {
                firstTime = false;
                toast(MapsActivity.this, "Mudando a camêra");

                mMap.animateCamera(up, 100, new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void onCancel() {
                    }
                });
            }
            //Verificamos se o Circle está vazio, se não estiver, esvaziamos
            if (circle != null) {
                circle.remove();
            }
            CircleOptions mapcircle;
            //Adicionando o circulo do local onde estiver o dispositivo atualmente
            mapcircle = new CircleOptions().center(latLng);
            mapcircle.fillColor(cor);
            mapcircle.radius(10);
            //Adiciona o Circle no mapa
            circle = mMap.addCircle(mapcircle);
            //Define a cor no Circle (a cor foi configurado no método onCreate)
            circle.setStrokeColor(cor);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        URL_icon = PreferencesManager.getString(this, "URL_IMG") + "loja_icon/{icon}";
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //Convertendo 40 pixels em dp
        height = getDPI(40, metrics);
        width = getDPI(40, metrics);

        //Checa a conexão com a internet
        if (testInternet(this)) {
            toast(this, "Internet OK.");
        } else {
            WarningDialog("Sua conexão com a internet está ruim. Utilize uma conexão melhor para que tenhamos mais precisão" +
                    " na localização do seu dispositivo.");
        }


        //Checa se o GPS está ativado, caso não esteja, ele envia o usuário até as configurações de location
          /*  LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                toast(this, "Ative o GPS.");
            } else {*/

        //Se o gps estiver ligado, conectamos a api google services
        if (mGo != null) {
            mGo.connect();
        }
        //}
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Checa a disponibilidade dos serviços da api do GoogleApi
        if (checkPlayServices()) {
            //Checa se o dispositivo está conectado aos serviços da GoogleApi
            if (mGo.isConnected()) {
                //Atualiza a posição do dispositivo
                startLocationUpdates();
            } else {
                //Caso não esteja conectado aos serviços da GoogleApi ele conecta
                mGo.connect();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Checa a disponibilidade dos serviços da api do GoogleApi
        if (checkPlayServices()) {
            if (mGo.isConnected()) {

            } else {
                //Caso não esteja conectado, chamamos o método que cria a requisição de localização
                createLocationRequest();
            }
        }
    }

    @Override
    protected void onStop() {
        mGo.disconnect();
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (bundle != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    LatLng lat = mMap.getCameraPosition().target;


                    load_data_comraio(lat.latitude, lat.longitude, bundle.getString("subcat"));
                    addCircle();
                    break;
                case MotionEvent.ACTION_UP:
                    LatLng lata = mMap.getCameraPosition().target;


                    load_data_comraio(lata.latitude, lata.longitude, bundle.getString("subcat"));
                    addCircle();
                    break;
            }
        }

        return super.dispatchTouchEvent(ev);

    }

    //  MÉTODOS QUE NÃO ESTÃO SOBRECARREGADOS (@Override)
    // --------------------------------------------------------------------------------------
    //Método que inicia as atualizações do posicionamento
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGo, mLocationRequest, this);
    }

    //Método responsável por reposicionar a câmera do mapa no local atual do dispositivo
    //Ativado pelo FloatingActionButton que foi instanciado no método onCreate
    private void setMapLocation(Location l) {
        if (mMap != null && l != null) {
            LatLng latLng = new LatLng(l.getLatitude(), l.getLongitude());
            CameraUpdate up = CameraUpdateFactory.newLatLngZoom(latLng, 16);
            mMap.animateCamera(up);
            if (circle != null) {
                circle.remove();
            }
            CircleOptions mapcircle;
            mapcircle = new CircleOptions().center(latLng);
            mapcircle.fillColor(cor);
            mapcircle.radius(10);
            circle = mMap.addCircle(mapcircle);
            circle.setStrokeColor(cor);
        }
    }

    //Criando a requisição de localização do dispositivo
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest().create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FAST_INTERVAL);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGo, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    MapsActivity.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    //Método para construir a api GoogleClient
    private synchronized void buildGoogleApiClient() {
        mGo = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    //Método para verificar os serviços da googlePlay no dispositivo
    private boolean checkPlayServices() {
        GoogleApiAvailability googleApi = GoogleApiAvailability.getInstance();
        int resultCode = googleApi.isGooglePlayServicesAvailable(MapsActivity.this);
        if (resultCode != ConnectionResult.SUCCESS) {
            toast(this, "Cade a net?");
            if (googleApi.isUserResolvableError(resultCode)) {
                googleApi.getErrorDialog(MapsActivity.this, resultCode,
                        zze.GOOGLE_PLAY_SERVICES_VERSION_CODE).show();
            }
            return false;
        } else {

            return true;
        }
    }

    private class latLongiSub {
        double lati, longit;
        String subcat;
    }

    //Método responsável por buscar as lojas  dentro de um raio em km, a busca é feita a partir da subcategoria selecionada
    private void load_data_comraio(final double lati, final double longit, final String subcat) {
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    Gson g = new Gson();
                    List<latLongiSub> lista = new ArrayList<>();
                    latLongiSub a = new latLongiSub();
                    a.lati = lati;
                    a.longit = longit;
                    a.subcat = subcat;
                    lista.add(a);
                    String url = PreferencesManager.getString(MapsActivity.this, "URL");


                    OkHttpClient client = new OkHttpClient();
                    Request.Builder builder = new Request.Builder();
                    builder.url(url);

                    MediaType mediaType =
                            MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(mediaType, g.toJson(lista));

                    builder.post(body);

                    Request request = builder.build();
                    okhttp3.Response response = client.newCall(request).execute();

                    String jsonDeResposta = response.body().string();
                    return jsonDeResposta;
                } catch (IOException e) {
                    e.printStackTrace();
                    return e.toString();
                }
            }

            @Override
            protected void onPostExecute(String s) {
                try {
                    Log.d(TAG, "onPostExecute: resposta -> " + s);
                    JSONArray jsonArray = new JSONArray(s);
                    lat = new double[jsonArray.length()];
                    longi = new double[jsonArray.length()];
                    icon = new String[jsonArray.length()];
                    cpf_cnpj = new String[jsonArray.length()];

                    //Instanciandos as Arrays com a capacitadade dos indices baseando-se na lista recebida da WebService
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        lat[i] = jsonObject.optDouble("latitude_cli");
                        longi[i] = jsonObject.optDouble("longitu_cli");
                        icon[i] = jsonObject.optString("icon_logo_maps");
                        cpf_cnpj[i] = jsonObject.optString("nr_cpf_cnpj");
                    }
                    //Verificamos se a Array que contém os valores da Latitude das CustomMarks do mapa
                    if (lat != null) {
                        PicassoMaker pk;
                        //Laço para criar as custom marks baseado no que foi requisitado no banco de dados
                        for (int i = 0; i < lat.length; i++) {
                            //Definindo a posição das CustomMarks com as Arrays lat e longi
                            LatLng markPosition = new LatLng(lat[i], longi[i]);
                            markList.add(mMap.addMarker(new MarkerOptions().position(markPosition).title(cpf_cnpj[i])));
                            pk = new PicassoMaker(markList.get(i));
                            poiTargets.add(pk);
                            //Carregando as imagem
                            Log.d(TAG, "onPostExecute: " + URL_icon.replace("{icon}", cpf_cnpj[i].replace("/", "") + "/" + icon[i]));
                            Picasso.with(MapsActivity.this).load(URL_icon.replace("{icon}", cpf_cnpj[i].replace("/", "") + "/" + icon[i])).resize(width, height).into(pk);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onPostExecute(s);
            }
        };
        task.execute();
    }

    //Método responsável por buscar as lojas sem um raio em km pois o usuário chegou até o mapa pelo bt localização
    private void lojasLocalAc() {
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String url = PreferencesManager.getString(MapsActivity.this, "URL") + "lojaslocalac";

                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder().url(url).build();
                    okhttp3.Response response = client.newCall(request).execute();
                    String json = response.body().string();
                    return json;
                } catch (IOException e) {
                    e.printStackTrace();
                    return e.toString();
                }
            }

            @Override
            protected void onPostExecute(String s) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    lat = new double[jsonArray.length()];
                    longi = new double[jsonArray.length()];
                    icon = new String[jsonArray.length()];
                    cpf_cnpj = new String[jsonArray.length()];

                    //Instanciandos as Arrays com a capacitadade dos indices baseando-se na lista recebida da WebService
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        lat[i] = jsonObject.optDouble("latitude_cli");
                        longi[i] = jsonObject.optDouble("longitu_cli");
                        icon[i] = jsonObject.optString("icon_logo_maps");
                        cpf_cnpj[i] = jsonObject.optString("nr_cpf_cnpj");

                        getToronto(myLat, myLongi, lat[i], longi[i]);
                    }


                    //haversine(-23.669478, -46.7988492, -23.6527622,-46.7124);

                    //Verificamos se a Array que contém os valores da Latitude das CustomMarks do mapa
                    if (lat != null) {
                        PicassoMaker pk;
                        //Laço para criar as custom marks baseado no que foi requisitado no banco de dados
                        for (int i = 0; i < lat.length; i++) {
                            //Definindo a posição das CustomMarks com as Arrays lat e longi
                            LatLng markPosition = new LatLng(lat[i], longi[i]);
                            markList.add(mMap.addMarker(new MarkerOptions().position(markPosition).title(cpf_cnpj[i])));
                            pk = new PicassoMaker(markList.get(i));
                            poiTargets.add(pk);
                            //Carregando as imagem
                            Picasso.with(MapsActivity.this).load(URL_icon.replace("{icon}", cpf_cnpj[i].replace("/", "") + "/" + icon[i])).resize(width, height).into(pk);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onPostExecute(s);
            }
        };
        task.execute();
    }

    private void getToronto(final double latStart, final double longiStart, final double latEnd, final double longiEnd) {
        AsyncTask<StringBuilder, StringBuilder, StringBuilder> task = new AsyncTask<StringBuilder, StringBuilder, StringBuilder>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected StringBuilder doInBackground(StringBuilder... params) {
                try {


                    String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + latStart + "," + longiStart + "&destination=" + latEnd + "," + longiEnd + "&key=AIzaSyDrr1N1U7yF994a59Hr8MuV2Gg-jigpG6E";

                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder().url(url).build();
                    okhttp3.Response response = client.newCall(request).execute();
                    String json = response.body().string();
                    StringBuilder s = new StringBuilder(json);
                    return s;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(StringBuilder s) {

                if (s != null) {
                    try {

                        JSONObject js = new JSONObject(s.toString());
                        JSONArray list = js.getJSONArray("routes");
                        JSONObject legs = list.getJSONObject(0);
                        JSONArray distance = legs.getJSONArray("legs");
                        JSONObject text = distance.getJSONObject(0);
                        JSONObject km = text.getJSONObject("distance");

                        Log.d(TAG, "onPostExecute: getToronto" + km.getString("text"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onPostExecute(s);
            }
        };
        task.execute();
    }


    public static Double distanceBetween(LatLng point1, LatLng point2) {
        if (point1 == null || point2 == null) {
            return null;
        }

        return SphericalUtil.computeDistanceBetween(point1, point2);
    }

    public void haversine(double lat1, double lon1, double lat2, double lon2) {
        double Rad = 6372.8; //Earth's Radius In kilometers
        // TODO Auto-generated method stub
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double haverdistanceKM = Rad * c;

        Log.d(TAG, "onCreate: " + haverdistanceKM);
    }

    //Método responsável por fazer a busca das lojas a partir de sua especialização
    private void getLojasByEspec(final String json) {
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String urlFiliais = PreferencesManager.getString(MapsActivity.this, "URL") + "espec_list/";
                    OkHttpClient client = new OkHttpClient();
                    Request.Builder builder = new Request.Builder();
                    builder.url(urlFiliais);

                    MediaType mediaType =
                            MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(mediaType, json);

                    builder.post(body);

                    Request request = builder.build();
                    okhttp3.Response response = client.newCall(request).execute();

                    String jsonDeResposta = response.body().string();
                    return jsonDeResposta;
                } catch (IOException e) {
                    e.printStackTrace();
                    return e.toString();
                }
            }

            @Override
            protected void onPostExecute(String s) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    double lat[] = new double[jsonArray.length()];
                    double longi[] = new double[jsonArray.length()];
                    String icon[] = new String[jsonArray.length()];
                    String cpf_cnpj[] = new String[jsonArray.length()];
                    //Instanciandos as Arrays com a capacitadade dos indices baseando-se na lista recebida da WebService
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        lat[i] = jsonObject.optDouble("latitude_cli");
                        longi[i] = jsonObject.optDouble("longitu_cli");
                        icon[i] = jsonObject.optString("icon_logo_maps");
                        cpf_cnpj[i] = jsonObject.optString("nr_cpf_cnpj");
                    }
                    //Verificamos se a Array que contém os valores da Latitude das CustomMarks do mapa
                    if (lat != null) {
                        PicassoMaker pk;
                        List<Marker> markList = new ArrayList<>();
                        //Laço para criar as custom marks baseado no que foi requisitado no banco de dados
                        for (int i = 0; i < lat.length; i++) {
                            //Definindo a posição das CustomMarks com as Arrays lat e longi
                            LatLng markPosition = new LatLng(lat[i], longi[i]);
                            markList.add(mMap.addMarker(new MarkerOptions().position(markPosition).title(cpf_cnpj[i])));
                            pk = new PicassoMaker(markList.get(i));
                            poiTargets.add(pk);
                            //Carregando as imagem
                            Picasso.with(MapsActivity.this).load(URL_icon.replace("{icon}", cpf_cnpj[i].replace("/", "") + "/" + icon[i])).resize(width, height).into(pk);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        task.execute();
    }

    //Método responsável por selecionar todas as especialidades do banco e colocá-las no mapa para escolha do usuário
    private void load_checkbox(final String subcat) {
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String urlFiliais = PreferencesManager.getString(MapsActivity.this, "URL") + "especialidades/" + subcat;
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(urlFiliais).build();
                    okhttp3.Response response = client.newCall(request).execute();
                    String json = response.body().string();
                    return json;
                } catch (IOException e) {
                    e.printStackTrace();
                    return e.toString();
                }
            }

            @Override
            protected void onPostExecute(String s) {
                try {

                   /* LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);*/
                    Log.d(TAG, "onPostExecute: " + s);

                    JSONArray jsonArray = new JSONArray(s);
                    valuesToCheckBox = new ArrayList<>();
                    cList = new CheckBox[jsonArray.length()];

                    //Instanciandos as Arrays com a capacitadade dos indices baseando-se na lista recebida da WebService
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        CheckBox c = new CheckBox(MapsActivity.this);
                        c.setPadding(10, 10, 10, 10);
                        valuesToCheckBox.add(jsonObject.optString("cd_especialidade_sub_cat"));

                        c.setText(jsonObject.optString("nm_especialidade_sub_cat"));
                        c.setTextColor(branco);
                        c.setTextSize(17);
                        c.setId(i);
                        cList[i] = c;
                        ll.addView(cList[i]);
                        Log.d(TAG, "onPostExecute: load check box " + i);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onPostExecute(s);
            }
        };
        task.execute();
    }

    @Override
    public LatLng getPosition() {
        return null;
    }

    private class PicassoMaker implements Target {
        Marker marker;
        MarkerOptions opt;

        public PicassoMaker(Marker marker) {
            Log.d("test: ", "init marker");
            this.marker = marker;
        }

        @Override
        public int hashCode() {
            return marker.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof PicassoMaker) {
                Marker marker = ((PicassoMaker) o).marker;
                return marker.equals(marker);
            } else {
                return false;
            }
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
            poiTargets.remove(this);
            Log.d(TAG, "onBitmapLoaded:  @+ Set bitmap for " + marker.getTitle() + " PK size: #" + poiTargets.size());
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.d("test: ", "@+ [ERROR] Não foi possível definir bitmap para " + marker.getTitle());
            poiTargets.remove(this);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Log.d("test: ", "bitmap preload");
        }
    }
}