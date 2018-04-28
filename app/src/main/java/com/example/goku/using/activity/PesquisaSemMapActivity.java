package com.example.goku.using.activity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.goku.using.R;
import com.example.goku.using.adapter.AdapterLoja;
import com.example.goku.using.adapter.NoMapAdapter;
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
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PesquisaSemMapActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener, LocationListener {


	private static final long LOCATION_REFRESH_TIME = 120000;
	private static final float LOCATION_REFRESH_DISTANCE = 10;
	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter rvAdapter;

	private List<String> nomeLoja;
	private List<String> endereco;
	private List<String> Logo;
	private List<Float> distancia;
	private double myLat;
	private double myLongi;
	private String subcat;
	private CheckBox[] cList;
	private List<String> valuesToCheckBox;

	//Variáveis que serão utilizadas posteriormente
	private double[] lat;
	private double[] longi;
	private String[] cpf_cnpj;

	//TAG para os logs
	private String TAG = "noMAPA";

	//Objeto responsável por se conectaro a api de localização do GoogleClient
	private GoogleApiClient mGo;

	//Objeto responsável por requisitar a localização atual do Dispositivo
	private LocationRequest mLocationRequest;

	//Intervalos de atualizações da localização
	private static int UPDATE_INTERVAL = 1000; // 10 seg
	private static int FAST_INTERVAL = 1000;

	private Bundle bundle;
	private ProgressBar bar;

	private int branco;

	private LinearLayout ll;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pesquisa_sem_map);

		setUpToolBar();
		setUpNavDrawer();
		//Cores para as checkboxes
		branco = ContextCompat.getColor(this, R.color.branco);

		//LinearLayout para adicionar as CheckBoxs
		ll = (LinearLayout) findViewById(R.id.maps_activity_checks_boxs);

		bar = (ProgressBar) findViewById(R.id.progressBarInclude);

		bundle = getIntent().getExtras();
		if (bundle != null) {
			subcat = bundle.getString("subcat");
		}

		NavigationView navigationView = (NavigationView) findViewById(R.id.maps_activity_nav_check_box);
		//Definindo transparência para nav drawer da DIREITA
		navigationView.getBackground().setAlpha(122);

		drawerLayout = (DrawerLayout) findViewById(R.id.activity_menu);

		//BT FAB para pesquisar ESPECIALIDADES
		FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.maps_fab_nav_check);
		floatingActionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (drawerLayout != null){
					drawerLayout.openDrawer(GravityCompat.END);
				}
			}
		});

		Button btn_pesquisa = (Button) findViewById(R.id.maps_activity_pesquisar);
		//Estou checando se o usuário vem pelo bt localização, se vier eu desabilito as especialidades
		if (bundle != null) {
			load_checkbox(bundle.getString("subcat"));
		}else{
			floatingActionButton.setVisibility(View.INVISIBLE);
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, navigationView);
		}

		btn_pesquisa.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				//Define a posição baseado nos valores que foram definidos no objeto myLat, myLongi pegados no onLocation
				LatLng latLng = new LatLng(myLat, myLongi);


				int a = 0;
				//Checando se a lista com as Checkboxes está vazia
				if(cList != null) {
					for (int i = 0; i < cList.length; i++) {
						//Criando controlodar para saber a quantidade de Checkboxes habilitadas
						if (cList[i].isChecked()) {
							a++;
						}
					}
				}

				//Checando o controlodar para saber se alguma checkbox foi habilitada
				if (a != 0){
					Gson gson = new Gson();
					List<String> valuesChecked = new ArrayList<String>();

					//Adicionando a posição atual nos índices 0 e 1 para criar raio em km para exibição das lojas
					valuesChecked.add(0, String.valueOf(myLat));
					valuesChecked.add(1, String.valueOf(myLongi));

					//Definindo controlador para começar a partir de 2 pois nas posições anteriores se encontra a posição do dispositivo
					int b = 2;
					for(int i=0; i< cList.length; i++){

						//Adicionando somente os valores das checkboxes que foram habilitadas
						if (cList[i].isChecked()){
							valuesChecked.add(b++, valuesToCheckBox.get(i));
						}
					}
					//Executando o método que subirá os dados para fazer a busca no banco
					getLojasByEspec(gson.toJson(valuesChecked));
				}else{
					Log.d(TAG, "onClick: valuesToCheckBox is null.");
					load_data_comraio(myLat, myLongi, bundle.getString("subcat"));
				}

				drawerLayout.closeDrawer(GravityCompat.END);

			}
		});


		nomeLoja = new ArrayList<>();
		endereco = new ArrayList<>();
		Logo = new ArrayList<>();
		distancia = new ArrayList<>();

		mRecyclerView = (RecyclerView) findViewById(R.id.pesquisa_sem_mapa_recycler_view);

		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.setHasFixedSize(true);
		rvAdapter = new NoMapAdapter(nomeLoja, endereco, Logo, distancia, this,onClickLoja());
		mRecyclerView.setAdapter(rvAdapter);

		if (checkPlayServices()) {

			//Construindo o GoogleApi Client
			buildGoogleApiClient();

			//Criando a requisição para obter a posição atual do dispositivo
			createLocationRequest();

		}

	}

	private NoMapAdapter.onClickLoja onClickLoja (){
		return new NoMapAdapter.onClickLoja() {
			@Override
			public void onClickLoja(View view ,int position) {
				Bundle bundle = new Bundle();
				bundle.putString("cpf_cnpj", cpf_cnpj[position]);
				startActivity(new Intent(PesquisaSemMapActivity.this, LojaActivity.class).putExtras(bundle));

			}
		};
	}

	private boolean checkPlayServices() {
		GoogleApiAvailability googleApi = GoogleApiAvailability.getInstance();
		int resultCode = googleApi.isGooglePlayServicesAvailable(PesquisaSemMapActivity.this);
		if (resultCode != ConnectionResult.SUCCESS) {
			toast(this, "Cade a net?");
			if (googleApi.isUserResolvableError(resultCode)) {
				googleApi.getErrorDialog(PesquisaSemMapActivity.this, resultCode,
						zze.GOOGLE_PLAY_SERVICES_VERSION_CODE).show();
			}
			return false;
		} else {

			return true;
		}
	}

	//Método para construir a api GoogleClient
	private synchronized void buildGoogleApiClient() {
		mGo = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
	}

	//Criando a requisição de localização do dispositivo
	private void createLocationRequest() {
		mLocationRequest = new LocationRequest().create()
				.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setNumUpdates(1);

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
									PesquisaSemMapActivity.this, 1000);
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


	private class latLongiSub{
		double lati, longit;
		String subcat;
	}



	//Método responsável por buscar as lojas  dentro de um raio em km, a busca é feita a partir da subcategoria selecionada
	private void load_data_comraio(final double lati, final double longit, final String subcat){
		AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
			@Override
			protected void onPreExecute() {
				mRecyclerView.removeAllViews();
				nomeLoja.clear();
				lat = null;
				longi = null;
				cpf_cnpj = null;
				distancia.clear();
				Logo.clear();
				endereco.clear();
				bar.setVisibility(View.VISIBLE);
				super.onPreExecute();
			}
			@Override
			protected String doInBackground(String... params) {
				try{
					Gson g = new Gson();
					List<PesquisaSemMapActivity.latLongiSub> lista = new ArrayList<>();
					PesquisaSemMapActivity.latLongiSub a = new PesquisaSemMapActivity.latLongiSub();
					a.lati = lati;
					a.longit = longit;
					a.subcat = subcat;
					lista.add(a);
					String url = PreferencesManager.getString(PesquisaSemMapActivity.this, "URL") + "nomap";

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
					cpf_cnpj = new String[jsonArray.length()];

					//Instanciandos as Arrays com a capacitadade dos indices baseando-se na lista recebida da WebService
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.optJSONObject(i);
						nomeLoja.add(jsonObject.optString("nm_razao_social"));
						lat[i] = jsonObject.optDouble("latitude_cli");
						longi[i] = jsonObject.optDouble("longitu_cli");
						cpf_cnpj[i] = jsonObject.optString("nr_cpf_cnpj");
						distancia.add((float) jsonObject.optDouble("distanceInKm"));
						Logo.add(jsonObject.optString("link_logotipo_cliente"));
						endereco.add(jsonObject.optString("nm_rua") + ", " + jsonObject.optString("nr_numero") +
								". CEP: " + jsonObject.optString("cd_cep_cli") + ". " + jsonObject.optString("cd_cidade_cli") +"." );
					}

					bar.setVisibility(View.INVISIBLE);
					rvAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onPostExecute(s);
			}
		};
		task.execute();
	}
	@Override
	protected void onStop() {
		mGo.disconnect();
		super.onStop();
	}

	@Override
	protected void onStart() {
		super.onStart();
		mRecyclerView.removeAllViews();
		if (mGo != null) {
			mGo.connect();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		//Checa a disponibilidade dos serviços da api do GoogleApi
		if(checkPlayServices()) {
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
		if(checkPlayServices()) {
			if (mGo.isConnected()) {

			} else {
				//Caso não esteja conectado, chamamos o método que cria a requisição de localização
				createLocationRequest();
			}
		}
	}

	//Método que inicia as atualizações do posicionamento
	private void startLocationUpdates() {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
				(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		LocationServices.FusedLocationApi.requestLocationUpdates(mGo, mLocationRequest, this);
	}
	@Override
	public void onConnected(@Nullable Bundle bundle) {
		startLocationUpdates();

	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}

	@Override
	public void onLocationChanged(Location location) {
		if (location == null){
			Log.d(TAG, "onLocationChanged: location is null.");
		}else {
			mRecyclerView.removeAllViews();
			myLat = location.getLatitude();
			myLongi = location.getLongitude();
			load_data_comraio(myLat,myLongi,subcat);
		}
	}

	//Método responsável por selecionar todas as especialidades do banco e colocá-las no mapa para escolha do usuário
	private void load_checkbox(final String subcat){
		AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
			@Override
			protected void onPreExecute() {

				super.onPreExecute();


			}
			@Override
			protected String doInBackground(String... params) {
				try{
					String urlFiliais = PreferencesManager.getString(PesquisaSemMapActivity.this, "URL") +"especialidades/"+ subcat;
					OkHttpClient client = new OkHttpClient();
					Request request = new Request.Builder().url(urlFiliais).build();
					okhttp3.Response  response = client.newCall(request).execute();
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
					Log.d(TAG, "onPostExecute: " + s );

					JSONArray jsonArray = new JSONArray(s);
					valuesToCheckBox = new ArrayList<>();
					cList = new CheckBox[jsonArray.length()];

					//Instanciandos as Arrays com a capacitadade dos indices baseando-se na lista recebida da WebService
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.optJSONObject(i);
						CheckBox c = new CheckBox(PesquisaSemMapActivity.this);
						c.setPadding(10,10,10,10);
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

	//Método responsável por fazer a busca das lojas a partir de sua especialização
	private void getLojasByEspec(final String json){
		AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				mRecyclerView.removeAllViews();
				nomeLoja.clear();
				lat = null;
				longi = null;
				cpf_cnpj = null;
				distancia.clear();
				Logo.clear();
				endereco.clear();
				bar.setVisibility(View.VISIBLE);
			}
			@Override
			protected String doInBackground(String... params) {
				try{
					String urlFiliais = PreferencesManager.getString(PesquisaSemMapActivity.this, "URL") + "espec_list/";
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
				try{
					JSONArray jsonArray = new JSONArray(s);
					double lat[] = new double[jsonArray.length()];
					double longi[] = new double[jsonArray.length()];
					String icon[] = new String[jsonArray.length()];
					String cpf_cnpj[] = new String[jsonArray.length()];
					//Instanciandos as Arrays com a capacitadade dos indices baseando-se na lista recebida da WebService
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.optJSONObject(i);
						nomeLoja.add(jsonObject.optString("nm_razao_social"));
						lat[i] = jsonObject.optDouble("latitude_cli");
						longi[i] = jsonObject.optDouble("longitu_cli");
						cpf_cnpj[i] = jsonObject.optString("nr_cpf_cnpj");
						distancia.add((float) jsonObject.optDouble("distanceInKm"));
						Logo.add(jsonObject.optString("link_logotipo_cliente"));
						endereco.add(jsonObject.optString("nm_rua") + ", " + jsonObject.optString("nr_numero") +
								". CEP: " + jsonObject.optString("cd_cep_cli") + ". " + jsonObject.optString("cd_cidade_cli") +"." );
					}

					bar.setVisibility(View.INVISIBLE);
					rvAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		task.execute();
	}
}
