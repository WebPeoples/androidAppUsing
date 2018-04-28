package com.example.goku.using.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.goku.using.R;
import com.example.goku.using.adapter.PromoRecyclerAdapter;
import com.example.goku.using.domain.WebUsingDomain;
import com.example.goku.using.fragment.Communicate;
import com.example.goku.using.fragment.PromoFragment_direita;
import com.example.goku.using.fragment.PromoFragment_esquerda;
import com.example.goku.using.prefs.PreferencesManager;
import com.github.kimkevin.cachepot.CachePot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class PromoActivity extends BaseActivity {


    private DrawerLayout drawerLayouts;

    private FragmentTransaction ftDir;
    private FragmentTransaction ftEsq;
    private PromoFragment_direita promoDir;

    private RecyclerView mRecyclerView;
    boolean w;
    private ProgressBar bar;
    private final String TAG = "Promo";
    private List<String> fotoProduto;
    private List<String> fotoCliente;
    private List<String> preco;
    private List<String> nm_produto;
    private List<String> desc;
    public RecyclerView.Adapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);

        EventBus.getDefault().register(this);
        promoDir = new PromoFragment_direita();

        fotoCliente = new ArrayList<>();
        fotoProduto = new ArrayList<>();
        preco = new ArrayList<>();
        nm_produto= new ArrayList<>();
        desc = new ArrayList<>();
        bar = (ProgressBar) findViewById(R.id.progressBarInclude);

        if (w){
          //ew.setVisibility(View.INVISIBLE);
        }else{
            Toast.makeText(this, "Os valores não foram passados de uma frag para outra.", Toast.LENGTH_SHORT).show();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.fav_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        rvAdapter = new PromoRecyclerAdapter(this, onClickPromo(), fotoCliente, fotoProduto, preco);
        mRecyclerView.setAdapter(rvAdapter);

        Log.d(TAG, "onCreateView: " + CachePot.getInstance().pop(String.class));

        setUpToolBar();
        setUpNavDrawer();

        NavigationView navigationView = (NavigationView) findViewById(R.id.promo_ac_nav);
        //Definindo transparência para nav drawer da DIREITA
        //navigationView.getBackground().setAlpha(122);

        drawerLayouts = (DrawerLayout) findViewById(R.id.activity_menu);
        //Instanciando o FloatingActionButton
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.promo_fab);

        //BT FAB para pesquisar ESPECIALIDADES

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayouts != null){
                    drawerLayouts.openDrawer(GravityCompat.END);
                }
            }
        });


        Button btAlimentos = (Button) findViewById(R.id.promo_activity_btAlimentos);
        Button btVestu = (Button) findViewById(R.id.promo_activity_btVestu);
        Button btBeb = (Button) findViewById(R.id.promo_activity_btBebida);
        Button btServicos = (Button) findViewById(R.id.promo_activity_btServicos);
        Button btSaude = (Button) findViewById(R.id.promo_activity_btSaude);
        Button btVarejo = (Button) findViewById(R.id.promo_activity_btVarejo);
        Button btManu = (Button) findViewById(R.id.promo_activity_btManu);
        Button btLazer = (Button) findViewById(R.id.promo_activity_btLazer);

        btAlimentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRecyclerView.removeAllViews();
                load_data_filter("ALIME");
                load_data_filter_filial("ALIME");
                drawerLayouts.closeDrawer(GravityCompat.END);
            }
        });

        btVestu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.removeAllViews();
                load_data_filter("VESTU");
                load_data_filter_filial("VESTU");
                drawerLayouts.closeDrawer(GravityCompat.END);
            }
        });

        btBeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.removeAllViews();
               load_data_filter("BEBIDA"); rvAdapter.notifyDataSetChanged();
               load_data_filter_filial("BEBIDA");
                drawerLayouts.closeDrawer(GravityCompat.END);
            }
        });
        btServicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.removeAllViews();
                load_data_filter("SERVIC");
                rvAdapter.notifyDataSetChanged();
                load_data_filter_filial("SERVIC");
                drawerLayouts.closeDrawer(GravityCompat.END);
            }
        });

        btSaude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.removeAllViews();
                load_data_filter("SAUDE");
                rvAdapter.notifyDataSetChanged();
                load_data_filter_filial("SAUDE");
                drawerLayouts.closeDrawer(GravityCompat.END);
            }
        });

        btVarejo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.removeAllViews();
                load_data_filter("VAREJO");
                rvAdapter.notifyDataSetChanged();
                load_data_filter_filial("VAREJO");
                drawerLayouts.closeDrawer(GravityCompat.END);
            }
        });
        btManu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btLazer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.removeAllViews();
                load_data_filter("ELETRO");
                rvAdapter.notifyDataSetChanged();
                load_data_filter_filial("ELETRO");
                drawerLayouts.closeDrawer(GravityCompat.END);
            }
        });

        /*ftEsq = getSupportFragmentManager().beginTransaction();
        ftEsq.replace(R.id.promo_frame_esquerda, new PromoFragment_esquerda());
        ftEsq.commit();

        ftDir = getSupportFragmentManager().beginTransaction();
        ftDir.replace(R.id.promo_frame_direita, promoDir);
        ftDir.commit();

        FrameLayout l = (FrameLayout) findViewById(R.id.promo_frame_esquerda);
        FrameLayout fD = (FrameLayout) findViewById(R.id.promo_frame_direita);

        //Definindo os parâmetros de layout(width, height) dos Fragments como zero,
        // porque o flag é setado inicialmente como TRUE sendo assim, sua visibilidade é invisível
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,0);
        l.setLayoutParams(layoutParams);
        //Definindo os parâmetros de layout(width, height) dos Fragments como match_parent,
        // porque o flag é setado inicialmente como TRUE sendo assim, sua visibilidade é visível
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        fD.setLayoutParams(layoutParams2);*/

       /* final boolean[] t = {false};
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.promo_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (t[0]) {

                    FrameLayout l = (FrameLayout) findViewById(R.id.promo_frame_esquerda);
                    FrameLayout fD = (FrameLayout) findViewById(R.id.promo_frame_direita);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,0);
                    l.setLayoutParams(layoutParams);

                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout
                            .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    fD.setLayoutParams(layoutParams2);


                    l.setVisibility(View.INVISIBLE);
                    t[0] = false;

                }else if(!t[0]){
                    FrameLayout l = (FrameLayout) findViewById(R.id.promo_frame_esquerda);
                    FrameLayout fD = (FrameLayout) findViewById(R.id.promo_frame_direita);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout
                            .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    layoutParams.weight = 1;
                    l.setLayoutParams(layoutParams);

                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout
                            .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    layoutParams2.weight = 1;
                    fD.setLayoutParams(layoutParams2);

                    l.setVisibility(View.VISIBLE);
                    t[0] = true;
                }
            }
        });*/
    }

    private PromoRecyclerAdapter.PromoOnClickListener onClickPromo() {
        return new PromoRecyclerAdapter.PromoOnClickListener() {
            @Override
            public void onClickPromo(View view, int idx) {
                Bundle bundle = new Bundle();
                bundle.putString("itemP", fotoProduto.get(idx));
                bundle.putString("itemTitle", nm_produto.get(idx));
                bundle.putString("itemPreco", preco.get(idx));
                bundle.putString("itemDesc", desc.get(idx));
                bundle.putString("produtoLogo", fotoCliente.get(idx));
                Intent intent = new Intent(PromoActivity.this, ProdutoActivity.class);
                startActivity(intent.putExtras(bundle));
            }
        };
    }

    public void onEvent(WebUsingDomain w){

    }

    @Override
    public void onStart() {
        super.onStart();
        mRecyclerView.removeAllViews();
        load_data();
        load_data_filial();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void load_data(){
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                bar.setVisibility(View.VISIBLE);
                fotoCliente.clear();
                fotoProduto.clear();
                preco.clear();
                nm_produto.clear();
                desc.clear();
            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    String url = PreferencesManager.getString(PromoActivity.this, "URL") + "promo/";

                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder().url(url).build();

                    okhttp3.Response  response = client.newCall(request).execute();
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
                Log.d(TAG, "onPostExecute: PROMO @@" + s);
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    //Instanciandos as Arrays com a capacitadade dos indices baseando-se na lista recebida da WebService
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        fotoProduto.add(jsonObject.optString("cd_link_foto_1"));
                        fotoCliente.add(jsonObject.optString("link_logotipo_cliente"));
                        preco.add(jsonObject.optString("vl_preco_cli"));
                        nm_produto.add(jsonObject.optString("nm_produto"));
                        desc.add(jsonObject.optString("dc_completa_prod"));


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
                fotoProduto.clear();
                preco.clear();
                nm_produto.clear();
                desc.clear();
            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    String url = PreferencesManager.getString(PromoActivity.this, "URL" )+ "promo_fil/";

                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder().url(url).build();

                    okhttp3.Response  response = client.newCall(request).execute();
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
                    //Instanciandos as Arrays com a capacitadade dos indices baseando-se na lista recebida da WebService
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        fotoProduto.add(jsonObject.optString("cd_link_foto_1"));
                        fotoCliente.add(jsonObject.optString("link_logotipo_cliente"));
                        preco.add(jsonObject.optString("vl_preco_cli"));
                        nm_produto.add(jsonObject.optString("nm_produto"));
                        desc.add(jsonObject.optString("dc_completa_prod"));
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

    public void load_data_filter(final String cat){
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                bar.setVisibility(View.VISIBLE);
                fotoCliente.clear();
                fotoProduto.clear();
                preco.clear();
                nm_produto.clear();
                desc.clear();
            }

            @Override
            protected String doInBackground(String... params) {
                try{
                    String url = PreferencesManager.getString(PromoActivity.this, "URL") + "promo/" + cat;

                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder().url(url).build();

                    okhttp3.Response  response = client.newCall(request).execute();
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
                    //Instanciandos as Arrays com a capacitadade dos indices baseando-se na lista recebida da WebService
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        fotoProduto.add(jsonObject.optString("cd_link_foto_1"));
                        fotoCliente.add(jsonObject.optString("link_logotipo_cliente"));
                        preco.add(jsonObject.optString("vl_preco_cli"));
                        nm_produto.add(jsonObject.optString("nm_produto"));
                        desc.add(jsonObject.optString("dc_completa_prod"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onPostExecute: ERRO JSON @@++" + e.getMessage());
                }
                rvAdapter.notifyDataSetChanged();
                super.onPostExecute(s);
            }
        };
        task.execute();
    }

    public void load_data_filter_filial(final String cat){
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                bar.setVisibility(View.VISIBLE);
                fotoCliente.clear();
                fotoProduto.clear();
                preco.clear();
                nm_produto.clear();
                desc.clear();
            }

            @Override
            protected String doInBackground(String... params) {
                try{
                    String url = PreferencesManager.getString(PromoActivity.this, "URL") + "promo_fil/" + cat;

                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder().url(url).build();

                    okhttp3.Response  response = client.newCall(request).execute();
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
                    //Instanciandos as Arrays com a capacitadade dos indices baseando-se na lista recebida da WebService
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        fotoProduto.add(jsonObject.optString("cd_link_foto_1"));
                        fotoCliente.add(jsonObject.optString("link_logotipo_cliente"));
                        preco.add(jsonObject.optString("vl_preco_cli"));
                        nm_produto.add(jsonObject.optString("nm_produto"));
                        desc.add(jsonObject.optString("dc_completa_prod"));
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "onPostExecute: ERRO JSON @@++" + e.getMessage());
                }
                rvAdapter.notifyDataSetChanged();
                super.onPostExecute(s);
            }
        };
        task.execute();
    }

}
