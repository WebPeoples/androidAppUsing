package com.example.goku.using.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.goku.using.R;
import com.example.goku.using.activity.MapsActivity;
import com.example.goku.using.activity.ProdutoActivity;
import com.example.goku.using.activity.PromoActivity;
import com.example.goku.using.adapter.PromoRecyclerAdapter;
import com.example.goku.using.domain.WebUsingDomain;
import com.example.goku.using.prefs.PreferencesManager;
import com.github.kimkevin.cachepot.CachePot;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.example.goku.using.fragment.PromoFragment_direita;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 */
public class PromoFragment_direita extends Fragment {

    private RecyclerView mRecyclerView;
    public PromoFragment_direita() {
        // Required empty public constructor
    }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promo_fragment_direita, container, false);
        fotoCliente = new ArrayList<>();
        fotoProduto = new ArrayList<>();
        preco = new ArrayList<>();
        nm_produto= new ArrayList<>();
        desc = new ArrayList<>();
        bar = (ProgressBar) view.findViewById(R.id.progressBarInclude);

        if (w){
            view.setVisibility(View.INVISIBLE);
        }else{
            Toast.makeText(getContext(), "Os valores não foram passados de uma frag para outra.", Toast.LENGTH_SHORT).show();
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fav_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        rvAdapter = new PromoRecyclerAdapter(getActivity(), onClickPromo(), fotoCliente, fotoProduto, preco);
        mRecyclerView.setAdapter(rvAdapter);

        Log.d(TAG, "onCreateView: " + CachePot.getInstance().pop(String.class));
        return view;
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
                Intent intent = new Intent(getActivity(), ProdutoActivity.class);
                startActivity(intent.putExtras(bundle));
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mRecyclerView.removeAllViews();
        load_data();
        load_data_filial();
    }

    @Override
    public void onStop() {
        super.onStop();
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
                    String url = PreferencesManager.getString(getContext(), "URL") + "promo/";

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
                    String url = PreferencesManager.getString(getContext(), "URL" )+ "promo_fil/";

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
                    String url = PreferencesManager.getString(getContext(), "URL") + "promo/" + cat;

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
                    String url = PreferencesManager.getString(getContext(), "URL") + "promo_fil/" + cat;

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
