package com.example.goku.using.activity;


import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.goku.using.R;
import com.example.goku.using.adapter.AdapterLoja;
import com.example.goku.using.domain.WebUsingDomain;
import com.example.goku.using.fragment.LojaFragment;
import com.example.goku.using.fragment.MenuLojaFragment;
import com.example.goku.using.prefs.PreferencesManager;
import com.example.goku.using.service.CircleTransform;
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

public class LojaActivity extends BaseActivity {

    private static final String TAG = "Using_Loja_Acitivty";
    private String URL = "http://192.168.1.106:8080/WebUsing/ws/generic/loja_info/";

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

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter rvAdapter;
    private List<String> listFotoProduto;
    private List<String> listTitle;
    private List<String> listPreco;
    private List<String> listDesc;
	private List<String> listPromo;
    private List<String> oldPriceList;
    private List<String> porcentagemPreco;
    private ProgressBar bar;
    private String logoProduto;

    private static TextView textView;

   private DrawerLayout drawerLayouts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loja);

        setUpToolBar();
        setUpNavDrawer();

        bundle = getIntent().getExtras();

        listFotoProduto = new ArrayList<>();
        listPreco = new ArrayList<>();
        listTitle = new ArrayList<>();
        listDesc = new ArrayList<>();
        listPromo = new ArrayList<>();
        oldPriceList = new ArrayList<>();
        porcentagemPreco = new ArrayList<>();


        bar = (ProgressBar) findViewById(R.id.progressBarInclude);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        rvAdapter = new AdapterLoja( onClickProduto(), listFotoProduto, listTitle, listPreco,listPromo,oldPriceList,porcentagemPreco, this, bundle.getString("cpf_cnpj"));
        mRecyclerView.setAdapter(rvAdapter);

        if (bundle.getString("subcat") != null) {
            load_data(bundle.getString("subcat"), bundle.getString("cpf_cnpj"));
            load_data_filial(bundle.getString("subcat"), bundle.getString("cpf_cnpj"));
        }else{
            local_load_data(bundle.getString("cpf_cnpj"));
            local_load_data_filial(bundle.getString("cpf_cnpj"));
        }


        rBar = (RatingBar) findViewById(R.id.menu_frag_rating);


        lista = new ArrayList<>();
        lista.add(0, PreferencesManager.getString(LojaActivity.this, "cduser"));
        lista.add(1, bundle.getString("cpf_cnpj"));


        webUsingDomain = new WebUsingDomain();

        favorite = (ToggleButton) findViewById(R.id.loja_menu_favorite_toggle);
        imageView = (ImageView) findViewById(R.id.icone_menu_loja);
        ViewTelefone = (TextView) findViewById(R.id.loja_activity_telefone);
        ViewSite = (TextView) findViewById(R.id.loja_activity_site);
        ViewEmail = (TextView) findViewById(R.id.loja_activity_email);
        ViewRua = (TextView) findViewById(R.id.loja_activity_avenida);
        ViewCep = (TextView) findViewById(R.id.loja_activity_cep);
        ViewEstado = (TextView) findViewById(R.id.loja_activity_city);
        ViewDiasAbertura = (TextView) findViewById(R.id.loja_activity_dias_abertura);

        NavigationView navigationView = (NavigationView) findViewById(R.id.loja_activity_nav_view);
        //Definindo transparência para nav drawer da DIREITA
        //navigationView.getBackground().setAlpha(122);

        drawerLayouts = (DrawerLayout) findViewById(R.id.activity_menu);
        //Instanciando o FloatingActionButton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabFrag);

        //BT FAB para pesquisar ESPECIALIDADES

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayouts != null){
                    drawerLayouts.openDrawer(GravityCompat.END);
                }
            }
        });

        load_data(bundle.getString("cpf_cnpj"));
        load_data_filial(bundle.getString("cpf_cnpj"));
        List<String> ratingList = new ArrayList<>();
        ratingList.add(bundle.getString("cpf_cnpj"));
        rating(ratingList);
        isFav(lista);


        Button btAvaliar = (Button) findViewById(R.id.menu_frag_btAvaliar);

        btAvaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LojaActivity.this);
                View view = getLayoutInflater().inflate(R.layout.alert_dialog_rating, null);
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
                        lista.add(2, PreferencesManager.getString(LojaActivity.this, "cduser"));

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

    }

    private AdapterLoja.produtoOnClickListener onClickProduto(){
        return new AdapterLoja.produtoOnClickListener() {
            @Override
            public void onClickProduto(View view, int idx) {
                Bundle bundle = new Bundle();

                if (listFotoProduto != null && listFotoProduto.size() != 0) {
                    bundle.putString("itemP", listFotoProduto.get(idx));
                }

                if (listTitle != null && listTitle.size() != 0) {
                    bundle.putString("itemTitle", listTitle.get(idx));
                }

                if (listPreco != null && listPreco.size() != 0) {
                    bundle.putString("itemPreco", listPreco.get(idx));
                }

                if (listDesc != null && listDesc.size() != 0) {
                    bundle.putString("itemDesc", listDesc.get(idx));
                }


                    bundle.putString("produtoLogo", webUsingDomain.getLogoProduto());


                    bundle.putString("cnpj", LojaActivity.this.bundle.getString("cpf_cnpj"));

                Intent intent = new Intent(LojaActivity.this, ProdutoActivity.class);
                startActivity(intent.putExtras(bundle));
            }
        };
    }


    // ******************* INFORMAÇÕES DA LOJA **************************


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
                    String url = PreferencesManager.getString(LojaActivity.this, "URL") + "informacoes_loja/";
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
                        Picasso.with(LojaActivity.this).load(PreferencesManager.getString(LojaActivity.this,"URL_IMG") + "loja_logo/{foto}".replace("{foto}", bundle.getString("cpf_cnpj").replace("/","")+"/"+logo))
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
                    String url = PreferencesManager.getString(LojaActivity.this, "URL") + "info_filial/";
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
                        Picasso.with(LojaActivity.this).load(PreferencesManager.getString(LojaActivity.this,"URL_IMG") + "loja_logo/{foto}".replace("{foto}",bundle.getString("cpf_cnpj").replace("/","")+"/"+ logo))
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
                    String url = PreferencesManager.getString(LojaActivity.this, "URL" ) + "setrating";

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
                Toast.makeText(LojaActivity.this, s, Toast.LENGTH_LONG).show();
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
                    String url = PreferencesManager.getString(LojaActivity.this, "URL") + "rating";

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
                    String url = PreferencesManager.getString(LojaActivity.this, "URL") + "fav";

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

                    String url = PreferencesManager.getString(LojaActivity.this, "URL") + "favoritar";

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
                //Toast.makeText(LojaActivity.this, s, Toast.LENGTH_SHORT).show();
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

                    String url = PreferencesManager.getString(LojaActivity.this, "URL") + "desfavoritar";

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

    public interface clickToggle{
        public void onclickBtn();
    }




    // **************************** PRODUTOS DA LOJA ************************
    private void load_data(final String subcat,final String cpf_cnpj){
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                bar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                try{
                    List<String> lista = new ArrayList<>();
                    lista.add(0,subcat);
                    lista.add(1,cpf_cnpj);

                    Gson g = new Gson();

                    String url = PreferencesManager.getString(LojaActivity.this, "URL") + "lista_produto";
                    OkHttpClient client = new OkHttpClient();

                    Request.Builder builder = new Request.Builder();
                    builder.url(url);
                    MediaType mediaType =
                            MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(mediaType, g.toJson(lista));
                    builder.post(body);

                    Request request = builder.build();

                    okhttp3.Response response = client.newCall(request).execute();

                    String json = response.body().string();

                    Log.d(TAG, "getListaProduto: " + json);
                    return json;
                } catch (IOException e) {
                    e.printStackTrace();
                    return e.toString();
                }
            }

            @Override
            protected void onPostExecute(String s) {
                bar.setVisibility(View.INVISIBLE);
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    Log.d(TAG, "onPostExecute: " +  s);
                    //Instanciandos as Arrays com a capacitadade dos indices baseando-se na lista recebida da WebService
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);

                        if (jsonObject.getString("cd_link_foto_1") != null)
                            listFotoProduto.add(jsonObject.optString("cd_link_foto_1"));

                        if (jsonObject.getString("nm_produto") != null)
                            listTitle.add(jsonObject.optString("nm_produto"));

                        if (jsonObject.getString("vl_preco_cli") != null)
                            listPreco.add(jsonObject.optString("vl_preco_cli"));

                        if (jsonObject.getString("dc_completa_prod") != null)
                            listDesc.add(jsonObject.optString("dc_completa_prod"));

						listPromo.add(jsonObject.optString("if_promocao"));
                        oldPriceList.add(jsonObject.optString("precoStrike"));
                        porcentagemPreco.add(jsonObject.optString("porcentagemPromo"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onPostExecute: ERRO @@+>" + e.getMessage());
                }

                rvAdapter.notifyDataSetChanged();
                super.onPostExecute(s);
            }
        };
        task.execute();
    }
    private void local_load_data(final String cpf_cnpj){
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                bar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                try{

                    String url = PreferencesManager.getString(LojaActivity.this, "URL") + "getlista_local/";
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
                bar.setVisibility(View.INVISIBLE);
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    //Instanciandos as Arrays com a capacitadade dos indices baseando-se na lista recebida da WebService
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);

                        listFotoProduto.add(jsonObject.optString("cd_link_foto_1"));
                        listTitle.add(jsonObject.optString("nm_produto"));
                        listPreco.add(jsonObject.optString("vl_preco_cli"));
                        listDesc.add(jsonObject.optString("dc_completa_prod"));
						listPromo.add(jsonObject.optString("if_promocao"));
                        oldPriceList.add(jsonObject.optString("precoStrike"));
                        porcentagemPreco.add(jsonObject.optString("porcentagemPromo"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onPostExecute: ERRO@@+>" + e.getMessage());
                }

                rvAdapter.notifyDataSetChanged();
                super.onPostExecute(s);
            }
        };
        task.execute();
    }

    private void local_load_data_filial(final String cpf_cnpj){
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                bar.setVisibility(View.VISIBLE);
            }
            @Override
            protected String doInBackground(String... params) {
                try{

                    String url = PreferencesManager.getString(LojaActivity.this, "URL") + "getlista_local_filial/";
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
                bar.setVisibility(View.INVISIBLE);
                try {
                    Log.d(TAG, "onPostExecute: LISTA " + s);
                    JSONArray jsonArray = new JSONArray(s);
                    //Instanciandos as Arrays com a capacitadade dos indices baseando-se na lista recebida da WebService
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);

                        listFotoProduto.add(jsonObject.optString("cd_link_foto_1"));
                        listTitle.add(jsonObject.optString("nm_produto"));
                        listPreco.add(jsonObject.optString("vl_preco_cli"));
                        listDesc.add(jsonObject.optString("dc_completa_prod"));
						listPromo.add(jsonObject.optString("if_promocao"));
                        oldPriceList.add(jsonObject.optString("precoStrike"));
                        porcentagemPreco.add(jsonObject.optString("porcentagemPromo"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onPostExecute: ERRO@@+>" + e.getMessage() );
                }

                rvAdapter.notifyDataSetChanged();
                super.onPostExecute(s);
            }
        };
        task.execute();
    }

    private void load_data_filial(final String subcat, final String cpf_cnpj){
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                bar.setVisibility(View.VISIBLE);
            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    List<String> lista = new ArrayList<>();
                    lista.add(0,subcat);
                    lista.add(1,cpf_cnpj);

                    Gson g = new Gson();

                    String url = PreferencesManager.getString(LojaActivity.this, "URL") + "filial_lista_produto";
                    OkHttpClient client = new OkHttpClient();

                    Request.Builder builder = new Request.Builder();
                    builder.url(url);
                    MediaType mediaType =
                            MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(mediaType, g.toJson(lista));
                    builder.post(body);

                    Request request = builder.build();

                    okhttp3.Response response = client.newCall(request).execute();

                    String json = response.body().string();

                    Log.d(TAG, "getListaProduto: " + json);
                    return json;
                } catch (IOException e) {
                    e.printStackTrace();
                    return e.toString();
                }
            }

            @Override
            protected void onPostExecute(String s) {
                bar.setVisibility(View.INVISIBLE);
                try {
                    Log.d(TAG, "onPostExecute: FILIAL_LISTA_PRODUTO @@" + s);
                    JSONArray jsonArray = new JSONArray(s);
                    //Instanciandos as Arrays com a capacitadade dos indices baseando-se na lista recebida da WebService
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);


                        listFotoProduto.add(jsonObject.optString("cd_link_foto_1"));

                        listTitle.add(jsonObject.optString("nm_produto"));

                        listPreco.add(jsonObject.optString("vl_preco_cli"));

                        listDesc.add(jsonObject.optString("dc_completa_prod"));

						listPromo.add(jsonObject.optString("if_promocao"));

                        oldPriceList.add(jsonObject.optString("precoStrike"));

                        porcentagemPreco.add(jsonObject.optString("porcentagemPromo"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onPostExecute: ERRO@@+>" + e.getMessage());
                }

                rvAdapter.notifyDataSetChanged();
                super.onPostExecute(s);
            }
        };
        task.execute();
    }
}
