package com.example.goku.using.fragment;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.goku.using.R;
import com.example.goku.using.activity.BaseActivity;
import com.example.goku.using.activity.MapsActivity;
import com.example.goku.using.domain.WebUsingDomain;
import com.example.goku.using.prefs.PreferencesManager;
import com.example.goku.using.service.CircleTransform;
import com.example.goku.using.service.WebUsing;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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


/**
 * Created by Goku on 22/06/2017.
 */

public class MenuLojaFragment extends Fragment{

    private static final String TAG = "UsingMenuLojaFragment";
    private String logo;
    private String telefone;
    private String site;
    private String rua;
    private String numero;
    private String cep;
    private String cidade;
    private String estado;
    private String email;
    private String dias_abertura;
     String URL_logo = "http://192.168.42.223:8080/WebUsing/img/loja_logo/{foto}";

    private TextView ViewTelefone;
    private TextView ViewSite;
    private TextView ViewEmail;
    private TextView ViewRua;
    private TextView ViewCep;
    private TextView ViewDiasAbertura;
    private TextView ViewEstado;
    private ImageView imageView;
    private ToggleButton favorite;
    private WebUsingDomain webUsingDomain;
    private Bundle bundle;
    private List<String> lista;
    private RatingBar rBar;
    private RatingBar rBat_btRating;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loja_menu, container, false);
        EventBus.getDefault().register(this);

        webUsingDomain = new WebUsingDomain();
        bundle = getActivity().getIntent().getExtras();
        rBar = (RatingBar) view.findViewById(R.id.menu_frag_rating);

        lista = new ArrayList<>();
        lista.add(0, PreferencesManager.getString(getContext(), "cduser"));
        lista.add(1, bundle.getString("cpf_cnpj"));

        if (savedInstanceState != null) {
            logo = savedInstanceState.getString("menuLogo");
        }

        favorite = (ToggleButton) view.findViewById(R.id.loja_menu_favorite_toggle);
        imageView = (ImageView) view.findViewById(R.id.icone_menu_loja);
        ViewTelefone = (TextView) view.findViewById(R.id.loja_activity_telefone);
        ViewSite = (TextView) view.findViewById(R.id.loja_activity_site);
        ViewEmail = (TextView) view.findViewById(R.id.loja_activity_email);
        ViewRua = (TextView) view.findViewById(R.id.loja_activity_avenida);
        ViewCep = (TextView) view.findViewById(R.id.loja_activity_cep);
        ViewEstado = (TextView) view.findViewById(R.id.loja_activity_city);
        ViewDiasAbertura = (TextView) view.findViewById(R.id.loja_activity_dias_abertura);

        load_data(bundle.getString("cpf_cnpj"));
        load_data_filial(bundle.getString("cpf_cnpj"));
        List<String> ratingList = new ArrayList<>();
        ratingList.add(bundle.getString("cpf_cnpj"));
        rating(ratingList);
        isFav(lista);

        Button btAvaliar = (Button) view.findViewById(R.id.menu_frag_btAvaliar);

                btAvaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = inflater.inflate(R.layout.alert_dialog_rating, null);
                rBat_btRating = (RatingBar) view.findViewById(R.id.dialog_rating_bar);
                Button btOk = (Button) view.findViewById(R.id.dialog_rating_btok);
                Button btCancel = (Button) view.findViewById(R.id.dialog_rating_btcancel);

                builder.setView(view);

                final Dialog dialog = builder.create();
                dialog.show();

                btOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       List<String> lista = new ArrayList<>();
                        lista.add(0, bundle.getString("cpf_cnpj"));
                        lista.add(1, String.valueOf(rBat_btRating.getRating()));
                        lista.add(2, PreferencesManager.getString(getContext(), "cduser"));

                        List<String> cpf = new ArrayList<>();
                        cpf.add(bundle.getString("cpf_cnpj"));
                        setRating(lista);
                        rating(cpf);

                        dialog.hide();
                    }
                });

                btCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.hide();
                    }
                });

            }
        });

        return view;
    }

    public void onEvent(WebUsingDomain webUsingDomain){}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("menuLogo", logo);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void load_data(final String cpf_cnpj){
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //bar.setVisibility(View.VISIBLE);
            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    String url = PreferencesManager.getString(getContext(), "URL") + "informacoes_loja/";
                    Gson g = new Gson();
                    OkHttpClient client = new OkHttpClient();
                    Request.Builder builder = new Request.Builder();
                    builder.url(url);

                    List<String> list = new ArrayList<>();
                    list.add(0, cpf_cnpj);

                    MediaType mediaType =
                            MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(mediaType, g.toJson(list));

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
                //bar.setVisibility(View.INVISIBLE);
                Log.d(TAG, " CLIENTE onPostExecute: " + s);
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        logo = jsonObject.optString("link_logotipo_cliente");
                        telefone = jsonObject.optString("nr_telefone1_cli");
                        email = jsonObject.optString("nm_email");
                        cep = jsonObject.optString("cd_cep_cli");
                        estado = jsonObject.optString("cd_estado_cli");
                        cidade = jsonObject.optString("cd_cidade_cli");
                        rua = jsonObject.optString("nm_rua");
                        numero = String.valueOf(jsonObject.optInt("nr_numero"));
                        site = jsonObject.optString("link_web");
                        dias_abertura = jsonObject.optString("dias_abertura_cli");
                    }
                    ViewTelefone.setText(telefone);
                    ViewEmail.setText(email);
                    ViewCep.setText("Cep: " + cep);
                    ViewEstado.setText(cidade + " - " + estado);
                    ViewRua.setText(rua + ", " + numero);
                    ViewSite.setText(site);
                    ViewDiasAbertura.setText(dias_abertura);
                    if (logo != null) {
                        Picasso.with(getActivity()).load(PreferencesManager.getString(getContext(),"URL_IMG") + "loja_logo/{foto}".replace("{foto}", logo))
                                .transform(new CircleTransform()).into(imageView);
                        webUsingDomain.setLogoProduto(logo);
                        EventBus.getDefault().post(webUsingDomain);
                    }

                } catch (JSONException e) {
                    Log.d(TAG, "onResponse: " + e);
                }
                super.onPostExecute(s);
            }
        };
        task.execute();
    }

    private void load_data_filial(final String cpf_cnpj){
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //bar.setVisibility(View.VISIBLE);
            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    String url = PreferencesManager.getString(getContext(), "URL") + "info_filial/";
                    Gson g = new Gson();
                    OkHttpClient client = new OkHttpClient();
                    Request.Builder builder = new Request.Builder();
                    builder.url(url);

                    List<String> list = new ArrayList<>();
                    list.add(0, cpf_cnpj);

                    MediaType mediaType =
                            MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(mediaType, g.toJson(list));

                    builder.post(body);

                    Request request = builder.build();
                    okhttp3.Response response = client.newCall(request).execute();

                    String jsonDeResposta = response.body().string();
                    Log.d(TAG, "RESPOSTA : @@-> +" +jsonDeResposta);
                    return jsonDeResposta;
                } catch (IOException e) {
                    e.printStackTrace();
                    return e.toString();
                }
            }
            @Override
            protected void onPostExecute(String s) {
                //bar.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "FILIAL RESPOSTA: @@-> +"+ s);
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        logo = jsonObject.getString("link_logotipo_cliente");

                        telefone = jsonObject.optString("nr_telefone1_cli");

                        email = jsonObject.optString("nm_email");

                        cep = jsonObject.optString("cd_cep_cli");

                        estado = jsonObject.optString("cd_estado_cli");

                        cidade = jsonObject.optString("cd_cidade_cli");

                        rua = jsonObject.optString("nm_rua");

                        numero = String.valueOf(jsonObject.optInt("nr_numero"));

                        site = jsonObject.optString("link_web");

                        dias_abertura = jsonObject.optString("dias_abertura_cli");
                    }
                    ViewTelefone.setText(telefone);
                    ViewEmail.setText(email);
                    ViewCep.setText("Cep: " + cep);
                    ViewEstado.setText(cidade + " - " + estado);
                    ViewRua.setText(rua);
                    ViewSite.setText(site);
                    ViewDiasAbertura.setText(dias_abertura);
                    if (logo != null) {
                        Picasso.with(getActivity()).load(PreferencesManager.getString(getContext(),"URL_IMG") + "loja_logo/{foto}".replace("{foto}", logo))
                                .transform(new CircleTransform()).into(imageView);
                        webUsingDomain.setLogoProduto(logo);
                        EventBus.getDefault().post(webUsingDomain);
                    }

                } catch (JSONException e) {
                    Log.d(TAG, "postExecute FILIAL: " + e);
                }
                super.onPostExecute(s);
            }
        };
        task.execute();
    }

    private void setRating(final List<String> dataUser){
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    Gson g = new Gson();
                    String url = PreferencesManager.getString(getContext(), "URL" ) + "setrating";

                    OkHttpClient client = new OkHttpClient();
                    Request.Builder builder = new Request.Builder();
                    builder.url(url);

                    MediaType mediaType =
                            MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(mediaType, g.toJson(dataUser));
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
                Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
            }
        };
        task.execute();
    }

    private void rating(final List<String> dataUser){
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    Gson g = new Gson();
                    String url = PreferencesManager.getString(getContext(), "URL") + "rating";

                    OkHttpClient client = new OkHttpClient();
                    Request.Builder builder = new Request.Builder();
                    builder.url(url);

                    MediaType mediaType =
                            MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(mediaType, g.toJson(dataUser));
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
                    Log.d(TAG, "onPostExecute: " + s);
                    JSONArray js = new JSONArray(s);
                    int cd_user = Integer.parseInt(js.getString(0));
                    float rating = Float.parseFloat(js.getString(1));
                    if (cd_user != 0 && rating != 0){
                        float finalRating = rating / cd_user;
                        rBar.setRating(finalRating);
                        Log.d(TAG, "onPostExecute: rating final -> " + finalRating );
						Log.d(TAG, "onPostExecute: cd_user -> " + cd_user );
						Log.d(TAG, "onPostExecute: rating -> " + rating);
					}

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        task.execute();
    }

    private void isFav(final List<String> dataUser){
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    Gson g = new Gson();
                    String url = PreferencesManager.getString(getContext(), "URL") + "fav";

                    OkHttpClient client = new OkHttpClient();
                    Request.Builder builder = new Request.Builder();
                    builder.url(url);

                    MediaType mediaType =
                            MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(mediaType, g.toJson(dataUser));
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

                if (s.equals("t")){
                    favorite.setChecked(true);
                }
                    favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked){
                                favoritar(lista);
                            }else {
                                desfavoritar(lista);
                            }
                        }
                    });
            }
        };
        task.execute();
    }

    private void favoritar(final List<String> dataUser){
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    Gson g = new Gson();

                    String url = PreferencesManager.getString(getContext(), "URL") + "favoritar";

                    OkHttpClient client = new OkHttpClient();
                    Request.Builder builder = new Request.Builder();
                    builder.url(url);

                    MediaType mediaType =
                            MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(mediaType, g.toJson(dataUser));

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
               Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }

    private void desfavoritar(final List<String> dataUser){
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    Gson g = new Gson();

                    String url = PreferencesManager.getString(getContext(), "URL") + "desfavoritar";

                    OkHttpClient client = new OkHttpClient();
                    Request.Builder builder = new Request.Builder();
                    builder.url(url);

                    MediaType mediaType =
                            MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(mediaType, g.toJson(dataUser));

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
            }
        };
        task.execute();
    }


}
