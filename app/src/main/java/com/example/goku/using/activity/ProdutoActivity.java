package com.example.goku.using.activity;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.goku.using.R;
import com.example.goku.using.adapter.ProdutoPagerAdapter;
import com.example.goku.using.domain.CatSubFragDomain;
import com.example.goku.using.domain.UsingDB;
import com.example.goku.using.prefs.PreferencesManager;
import com.example.goku.using.service.WebUsing;
import com.github.kimkevin.cachepot.CachePot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class ProdutoActivity extends BaseActivity {

    private ArrayList<String> produtoFotos;
    private String[] fotosProduto;
    private final String TAG = "ProdutoActivity";
    private CatSubFragDomain ct;
    private ViewPager g;
    private CirclePageIndicator t;
    private int cor;
    private ProgressBar bar;
    private Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto);
        setUpToolBar();
        setUpNavDrawer();
        produtoFotos = new ArrayList<>();
        cor = ContextCompat.getColor(this, R.color.rosa);


        // VÁRIAVEIS QUE SERÃO UTILIZADAS
        UsingDB db = new UsingDB(this);
        bundle = getIntent().getExtras();
        String url = PreferencesManager.getString(this, "URL_IMG");
        String foto_empresa = null;
        g = (ViewPager) findViewById(R.id.produto_activity_viewpager);
        t = (CirclePageIndicator) findViewById(R.id.titles);

        // g.setAdapter(new ProdutoPagerAdapter(this, ));
        // CHECANDO SE O BUNDLE É NULO PARA EVITAR EXCEÇÕES
        if (bundle != null) {
            Log.d(TAG, "onCreate: @++" + bundle.getString("produtoLogo"));
            foto_empresa = bundle.getString("produtoLogo");
        }

        // SELECIONANDO OS DADOS QUE FORAM GUARDADOS NO BANCO PELA LojaFragment
        load_data(bundle.getString("itemP"));

        // INSTANCIANDO CONTÉUDOS UI
        TextView nomeProduto = (TextView) findViewById(R.id.produto_activity_nomeProduto);
        TextView precoProduto = (TextView) findViewById(R.id.produto_activity_precoProduto);
        TextView descProduto = (TextView) findViewById(R.id.produto_activity_descProduto);
        //ImageView fotoProduto = (ImageView) findViewById(R.id.produto_activty_fotoProduto);
        ImageView logoEmpresa = (ImageView) findViewById(R.id.produto_activity_logoEmpresa);

        // DEFININDO OS VALORES DOS CONTEÚDOS UI
        if (bundle != null) {
            nomeProduto.setText(bundle.getString("itemTitle"));
            precoProduto.setText("R$" + bundle.getString("itemPreco"));
            descProduto.setText(bundle.getString("itemDesc"));
        }
            /*Picasso.with(this)
                    .load("http://192.168.1.106:8080/WebUsing/img/produtos/{foto}".replace("{foto}", ct.getLogo()))
                    .into(fotoProduto);*/


        if (foto_empresa != null)
            Picasso.with(this)
                    .load(url + "loja_logo/" + bundle.getString("cnpj").replace("/", "") + "/" + "{foto}".replace("{foto}", foto_empresa))
                    .into(logoEmpresa);
        Log.d(TAG, "onCreate: " + foto_empresa);


        g.setAdapter(new ProdutoPagerAdapter(ProdutoActivity.this, produtoFotos,bundle.getString("cnpj") ));
        t.setFillColor(cor);
        t.setViewPager(g);
        g.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                t.onPageSelected(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }


    private void load_data(final String foto) {
        final AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String url = PreferencesManager.getString(ProdutoActivity.this, "URL") + "fotos_produtos/" + foto;

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
                    JSONArray lista = new JSONArray(s);
                    String foto2 = null;
                    String foto3 = null;
                    String foto4 = null;
                    String foto5 = null;
                    for (int i = 0; i < lista.length(); i++) {
                        try {
                            JSONObject item = lista.getJSONObject(i);
                            foto2 = item.optString("cd_link_foto_2");
                            foto3 = item.optString("cd_link_foto_3");
                            foto4 = item.optString("cd_link_foto_4");
                            foto5 = item.optString("cd_link_foto_5");
                            Log.d(TAG, "cd_link_foto_2: " + foto2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    int i = 0;
                    i++;
                    produtoFotos.add(bundle.getString("itemP"));
                    Log.d(TAG, "onPostExecute: " + produtoFotos.get(0));
                    if (foto2 != null && foto2 != "") {
                        produtoFotos.add(foto2);
                        i++;
                    }
                    if (foto3 != null && foto3 != "") {
                        produtoFotos.add(foto3);
                        i++;
                    }
                    if (foto4 != null && foto4 != "") {
                        produtoFotos.add(foto4);
                        i++;
                    }
                    if (foto5 != null && foto5 != "") {
                        produtoFotos.add(foto5);
                        i++;
                    }
                    fotosProduto = new String[produtoFotos.size()];
                    for (int a = 0; a < produtoFotos.size(); a++) {
                        fotosProduto[a] = produtoFotos.get(a);
                    }

                    g.getAdapter().notifyDataSetChanged();
                    t.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        task.execute();
    }
}
