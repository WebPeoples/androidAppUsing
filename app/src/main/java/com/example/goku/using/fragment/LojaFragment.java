package com.example.goku.using.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.goku.using.R;
import com.example.goku.using.activity.ProdutoActivity;
import com.example.goku.using.adapter.AdapterLoja;
import com.example.goku.using.domain.UsingDB;
import com.example.goku.using.domain.WebUsingDomain;
import com.example.goku.using.prefs.PreferencesManager;
import com.example.goku.using.service.Converter;
import com.example.goku.using.service.WebUsing;
import com.github.kimkevin.cachepot.CachePot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

public class LojaFragment  extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter rvAdapter;
    private List<String> listFotoProduto;
    private List<String> listTitle;
    private List<String> listPreco;
    private List<String> listDesc;
    private List<String> oldPriceList;
    private ProgressBar bar;
    private String URL_LOJA_FRAG = "http://192.168.42.52:8080/WebUsing/ws/lista_produto/";
    private String logoProduto;
    private Bundle bundle;
    private static TextView textView;


    private String TAG = "LojaFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.lista_produtos, container, false);

         bundle = getActivity().getIntent().getExtras();

        EventBus.getDefault().register(this);

        listFotoProduto = new ArrayList<>();
        listPreco = new ArrayList<>();
        listTitle = new ArrayList<>();
        listDesc = new ArrayList<>();
        List<String>listPromo = new ArrayList<>();
        oldPriceList = new ArrayList<>();
        List<String> a = new ArrayList<>();


        bar = (ProgressBar) view.findViewById(R.id.progressBarInclude);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        rvAdapter = new AdapterLoja( onClickProduto(), listFotoProduto, listTitle, listPreco,listPromo,oldPriceList,a, getContext(), bundle.getString("cpf_cnpj"));
        mRecyclerView.setAdapter(rvAdapter);

        if (bundle.getString("subcat") != null) {
            load_data(bundle.getString("subcat"), bundle.getString("cpf_cnpj"));
            load_data_filial(bundle.getString("subcat"), bundle.getString("cpf_cnpj"));
        }else{
            local_load_data(bundle.getString("cpf_cnpj"));
            local_load_data_filial(bundle.getString("cpf_cnpj"));
        }
        return view;
    }

    public void onEvent(WebUsingDomain w){
        logoProduto = w.getLogoProduto();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.getString("cpf_cnpj");
        super.onSaveInstanceState(outState);
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

            if (logoProduto != null) {
                bundle.putString("produtoLogo", logoProduto);
            }

            Intent intent = new Intent(getActivity(), ProdutoActivity.class);
            startActivity(intent.putExtras(bundle));
        }
    };
   }


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    //
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

                String url = PreferencesManager.getString(getContext(), "URL") + "lista_produto";
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

                    String url = PreferencesManager.getString(getContext(), "URL") + "getlista_local/";
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

                    String url = PreferencesManager.getString(getContext(), "URL") + "getlista_local_filial/";
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

                    String url = PreferencesManager.getString(getContext(), "URL") + "filial_lista_produto";
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
