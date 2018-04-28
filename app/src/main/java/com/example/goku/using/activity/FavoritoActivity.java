package com.example.goku.using.activity;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.goku.using.R;
import com.example.goku.using.adapter.FavRecyclerAdapter;
import com.example.goku.using.domain.WebUsingDomain;
import com.example.goku.using.fragment.FavFragment_direita;
import com.example.goku.using.fragment.FavFragment_esquerda;
import com.example.goku.using.prefs.PreferencesManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FavoritoActivity extends BaseActivity {

    private DrawerLayout drawerLayouts;

    private RecyclerView mRecyclerView;
    RecyclerView.Adapter rvAdapter;
    ProgressBar bar;

    List<WebUsingDomain> list;
    private List<String> fotoCliente;
    private List<String> nr_cpf_cnpj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorito);

        setUpNavDrawer();
        setUpToolBar();

        fotoCliente = new ArrayList<>();
        nr_cpf_cnpj = new ArrayList<>();

        bar = (ProgressBar) findViewById(R.id.progressBarInclude);
        bar.setVisibility(View.VISIBLE);


        mRecyclerView = (RecyclerView) findViewById(R.id.fav_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(FavoritoActivity.this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        rvAdapter = new FavRecyclerAdapter(onClickLoja(), fotoCliente , FavoritoActivity.this, nr_cpf_cnpj);
        mRecyclerView.setAdapter(rvAdapter);


        NavigationView navigationView = (NavigationView) findViewById(R.id.favorito_ac_nav);
        //Definindo transparência para nav drawer da DIREITA
       // navigationView.getBackground().setAlpha(122);

        drawerLayouts = (DrawerLayout) findViewById(R.id.activity_menu);
        //Instanciando o FloatingActionButton
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fav_fab);

        //BT FAB para pesquisar ESPECIALIDADES

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayouts != null){
                    drawerLayouts.openDrawer(GravityCompat.END);
                }
            }
        });

        Button btAlimentos = (Button) findViewById(R.id.fav_ac_btalimentos);
        Button btVestu = (Button) findViewById(R.id.fav_ac_btVestu);
        Button btBeb = (Button) findViewById(R.id.fav_ac_btBebida);
        Button btServicos = (Button) findViewById(R.id.fav_ac_btServico);
        Button btSaude = (Button) findViewById(R.id.fav_ac_btSaude);
        Button btVarejo = (Button) findViewById(R.id.fav_ac_btVarejo);
        Button btManu = (Button) findViewById(R.id.fav_ac_btManu);
        Button btEletro = (Button) findViewById(R.id.fav_ac_btEletro);

        btAlimentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> lista = new ArrayList<String>();
                lista.add(0, PreferencesManager.getString(FavoritoActivity.this, "cduser"));
                lista.add(1, "ALIME");
                load_data_filter(lista);

                List<String> listaFilial = new ArrayList<String>();
                listaFilial.add(0, PreferencesManager.getString(FavoritoActivity.this, "cduser"));
                listaFilial.add(1, "ALIME");
                load_data_filter_filial(listaFilial);
                limparRecycler();
                drawerLayouts.closeDrawer(GravityCompat.END);

            }
        });


        btVestu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> lista = new ArrayList<String>();
                lista.add(0, PreferencesManager.getString(FavoritoActivity.this, "cduser"));
                lista.add(1, "VESTU");
                load_data_filter(lista);

                List<String> listaFilial = new ArrayList<String>();
                listaFilial.add(0, PreferencesManager.getString(FavoritoActivity.this, "cduser"));
                listaFilial.add(1, "VESTU");
                load_data_filter_filial(listaFilial);
                limparRecycler();
                drawerLayouts.closeDrawer(GravityCompat.END);
            }
        });

        btBeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> lista = new ArrayList<String>();
                lista.add(0, PreferencesManager.getString(FavoritoActivity.this, "cduser"));
                lista.add(1, "BEBIDA");
                load_data_filter(lista);

                List<String> listaFilial = new ArrayList<String>();
                listaFilial.add(0, PreferencesManager.getString(FavoritoActivity.this, "cduser"));
                listaFilial.add(1, "BEBIDA");
                load_data_filter_filial(listaFilial);
                limparRecycler();
                drawerLayouts.closeDrawer(GravityCompat.END);
            }
        });
        btServicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> lista = new ArrayList<String>();
                lista.add(0, PreferencesManager.getString(FavoritoActivity.this, "cduser"));
                lista.add(1, "SERVIC");
                load_data_filter(lista);

                List<String> listaFilial = new ArrayList<String>();
                listaFilial.add(0, PreferencesManager.getString(FavoritoActivity.this, "cduser"));
                listaFilial.add(1, "SERVIC");
                load_data_filter_filial(listaFilial);

               limparRecycler();
                drawerLayouts.closeDrawer(GravityCompat.END);
            }
        });

        btSaude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> lista = new ArrayList<String>();
                lista.add(0, PreferencesManager.getString(FavoritoActivity.this, "cduser"));
                lista.add(1, "SAUDE");
                load_data_filter(lista);

                List<String> listaFilial = new ArrayList<String>();
                listaFilial.add(0, PreferencesManager.getString(FavoritoActivity.this, "cduser"));
                listaFilial.add(1, "SAUDE");
                load_data_filter_filial(listaFilial);

                limparRecycler();
                drawerLayouts.closeDrawer(GravityCompat.END);
            }
        });

        btVarejo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> lista = new ArrayList<String>();
                lista.add(0, PreferencesManager.getString(FavoritoActivity.this, "cduser"));
                lista.add(1, "VAREJO");
                load_data_filter(lista);

                List<String> listaFilial = new ArrayList<String>();
                listaFilial.add(0, PreferencesManager.getString(FavoritoActivity.this, "cduser"));
                listaFilial.add(1, "VAREJO");
                load_data_filter_filial(listaFilial);

                limparRecycler();
                drawerLayouts.closeDrawer(GravityCompat.END);
            }
        });
        btManu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btEletro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> lista = new ArrayList<String>();
                lista.add(0, PreferencesManager.getString(FavoritoActivity.this, "cduser"));
                lista.add(1, "ELETRO");
                load_data_filter(lista);

                List<String> listaFilial = new ArrayList<String>();
                listaFilial.add(0, PreferencesManager.getString(FavoritoActivity.this, "cduser"));
                listaFilial.add(1, "ELETRO");
                load_data_filter_filial(listaFilial);

               limparRecycler();
                drawerLayouts.closeDrawer(GravityCompat.END);
            }
        });

        //Criando e construindo o Fragment responsável pelo menu na tela de Favoritos
       /* FragmentTransaction ftEsq = getSupportFragmentManager().beginTransaction();
        ftEsq.replace(R.id.fav_frame_esquerda, new FavFragment_esquerda());
        ftEsq.commit();

        //Criancod e construindo o Fragment responsável pela lista de lojas favoritas na tela de Favoritos
        FragmentTransaction ftDir = getSupportFragmentManager().beginTransaction();
        ftDir.replace(R.id.fav_frame_direita, new FavFragment_direita());
        ftDir.commit();*/

        //Flag responsável por controlar a visibilidade da Fragment esquerda com o menu na tela de Favoritos
        //final boolean[] t = {true};

        //Instanciando o FloatingActionButton
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fav_fab);

        /*//Chamando listener do FAB
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Verificamos o valor atual do flag criado fora do listener
                //Caso o listener nunca tenha sido chamado o menu estará visivel pois seu valor é TRUE
                if (t[0]) {

                    //Instanciando os framelayouts que contém as Fragments da tela de favoritos
                    FrameLayout l = (FrameLayout) findViewById(R.id.fav_frame_esquerda);
                    FrameLayout fD = (FrameLayout) findViewById(R.id.fav_frame_direita);

                    //Definindo os parâmetros de layout(width, height) dos Fragments como zero,
                    // porque o flag é setado inicialmente como TRUE sendo assim, sua visibilidade é invisível
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
                    l.setLayoutParams(layoutParams);
                    //Definindo os parâmetros de layout(width, height) dos Fragments como zero,
                    // porque o flag é setado inicialmente como TRUE sendo assim, sua visibilidade é visível
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout
                            .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    fD.setLayoutParams(layoutParams2);

                    //Após definirmos a largura e altura como 0 logo acima, tornamos invisível a Fragment esqueda
                    //para garantir que nada interferirá na Fragment direita
                    l.setVisibility(View.INVISIBLE);

                    //Em seguida definimos o flag como FALSE porque na próxima vez que
                    // o listener for chamado(usuário apertar o FAB), ele não executará a mesma função
                    t[0] = false;

                }
                //Verificamos o valor atual do flag criado fora do listener
                //Caso o listener já tenha sido chamado seu valor será False
                else if (!t[0]) {
                    //Instanciando os framelayouts que contém as Fragments da tela de favoritos
                    FrameLayout l = (FrameLayout) findViewById(R.id.fav_frame_esquerda);
                    FrameLayout fD = (FrameLayout) findViewById(R.id.fav_frame_direita);

                    //Definindo os parâmetros de layout(width, height) dos Fragments como match_parent,
                    // porque o flag foi setado como FALSE sendo assim, sua visibilidade é visível
                    LinearLayout.LayoutParams layoutParams = new LinearLayout
                            .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    //Definimos o peso como 1 para tornar a proporção da largura e altura
                    // das duas Fragment iguais
                    layoutParams.weight = 1;
                    l.setLayoutParams(layoutParams);

                    //Definindo os parâmetros de layout(width, height) dos Fragments como match_parent,
                    // porque o flag foi setado como FALSE sendo assim, sua visibilidade é visível
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout
                            .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    //Definimos o peso como 1 para tornar a proporção da largura e altura
                    // das duas Fragment iguais
                    layoutParams2.weight = 1;
                    fD.setLayoutParams(layoutParams2);

                    //Tornamos a o Fragment esquerdo visível por via das dúvidas
                    l.setVisibility(View.VISIBLE);

                    //Em seguida definimos o flag como TRUE porque na próxima vez que
                    // o listener for chamado(usuário apertar o FAB), ele não executará a mesma função
                    t[0] = true;
                }
            }
                });*/
            }

    public void limparRecycler(){
        mRecyclerView.removeAllViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        mRecyclerView.removeAllViews();
        load_data();
        load_data_filial();
    }

    private FavRecyclerAdapter.LojaOnClickListener onClickLoja() {
        return new FavRecyclerAdapter.LojaOnClickListener() {
            @Override
            public void onClickLoja(View view, int idx) {
                Toast.makeText(FavoritoActivity.this, nr_cpf_cnpj.get(idx), Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("cpf_cnpj", nr_cpf_cnpj.get(idx));
                startActivity(new Intent(FavoritoActivity.this, LojaActivity.class).putExtras(bundle));
            }
        };
    }

    private void load_data(){
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                bar.setVisibility(View.VISIBLE);
                fotoCliente.clear();
                nr_cpf_cnpj.clear();


            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    String url = PreferencesManager.getString(FavoritoActivity.this, "URL") + "selectClientesFavoritos/"
                            + PreferencesManager.getString(FavoritoActivity.this, "cduser");

                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder().url(url).build();

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
                bar.setVisibility(View.INVISIBLE);

                try {
                    JSONArray jsonArray = new JSONArray(s);
                    //Instanciandos as Arrays com a capacitadade dos indices baseando-se na lista recebida da WebService
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        fotoCliente.add(jsonObject.getString("link_logotipo_cliente"));
                        nr_cpf_cnpj.add(jsonObject.getString("nr_cpf_cnpj"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                rvAdapter.notifyDataSetChanged();
                super.onPostExecute(s);
            }
        };
        task.execute();
    }

    private void load_data_filial(){
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                bar.setVisibility(View.VISIBLE);
                fotoCliente.clear();
                nr_cpf_cnpj.clear();


            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    String url = PreferencesManager.getString(FavoritoActivity.this, "URL") + "selectFiliaisFavoritos/"
                            + PreferencesManager.getString(FavoritoActivity.this, "cduser");

                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder().url(url).build();

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
                bar.setVisibility(View.INVISIBLE);

                try {
                    JSONArray jsonArray = new JSONArray(s);
                    //Instanciandos as Arrays com a capacitadade dos indices baseando-se na lista recebida da WebService
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        fotoCliente.add(jsonObject.getString("link_logotipo_cliente"));
                        nr_cpf_cnpj.add(jsonObject.getString("nr_cpf_cnpj"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                rvAdapter.notifyDataSetChanged();
                super.onPostExecute(s);
            }
        };
        task.execute();
    }

    public void load_data_filter(final List<String> dataUser){
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                bar.setVisibility(View.VISIBLE);
                fotoCliente.clear();
                nr_cpf_cnpj.clear();

            }

            @Override
            protected String doInBackground(String... params) {
                try{
                    String url = PreferencesManager.getString(FavoritoActivity.this, "URL") + "favFilter";
                    Gson g = new Gson();

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
                bar.setVisibility(View.INVISIBLE);
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    //Instanciandos as Arrays com a capacitadade dos indices baseando-se na lista recebida da WebService
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);

                        fotoCliente.add(jsonObject.getString("link_logotipo_cliente"));
                        nr_cpf_cnpj.add(jsonObject.getString("nr_cpf_cnpj"));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rvAdapter.notifyDataSetChanged();
                super.onPostExecute(s);
            }
        };
        task.execute();
    }

    public void load_data_filter_filial(final List<String> dataUser){
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                bar.setVisibility(View.VISIBLE);
                fotoCliente.clear();
                nr_cpf_cnpj.clear();

            }

            @Override
            protected String doInBackground(String... params) {
                try{
                    String url = PreferencesManager.getString(FavoritoActivity.this, "URL") + "favFilialFilter";
                    Gson g = new Gson();

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
                bar.setVisibility(View.INVISIBLE);
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    //Instanciandos as Arrays com a capacitadade dos indices baseando-se na lista recebida da WebService
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);

                        fotoCliente.add(jsonObject.getString("link_logotipo_cliente"));
                        nr_cpf_cnpj.add(jsonObject.getString("nr_cpf_cnpj"));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rvAdapter.notifyDataSetChanged();
                super.onPostExecute(s);
            }
        };
        task.execute();
    }

    }



