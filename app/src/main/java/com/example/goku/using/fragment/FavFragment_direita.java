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
import com.example.goku.using.activity.LojaActivity;
import com.example.goku.using.adapter.FavRecyclerAdapter;
import com.example.goku.using.domain.WebUsingDomain;
import com.example.goku.using.prefs.PreferencesManager;
import com.example.goku.using.service.Converter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavFragment_direita extends Fragment {

    private RecyclerView mRecyclerView;
    RecyclerView.Adapter rvAdapter;
    ProgressBar bar;

    List<WebUsingDomain> list;
    private List<String> fotoCliente;
    private List<String> nr_cpf_cnpj;
    private static final String TAG = "FavFragment";


    public FavFragment_direita() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav_fragment_direita, container, false);
        fotoCliente = new ArrayList<>();
        nr_cpf_cnpj = new ArrayList<>();

        bar = (ProgressBar) view.findViewById(R.id.progressBarInclude);
        bar.setVisibility(View.VISIBLE);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.fav_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        rvAdapter = new FavRecyclerAdapter(onClickLoja(), fotoCliente , getActivity(), nr_cpf_cnpj);
        mRecyclerView.setAdapter(rvAdapter);

        return view;

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
                Toast.makeText(getActivity(), nr_cpf_cnpj.get(idx), Toast.LENGTH_SHORT).show();
                 Bundle bundle = new Bundle();
                bundle.putString("cpf_cnpj", nr_cpf_cnpj.get(idx));
                startActivity(new Intent(getContext(), LojaActivity.class).putExtras(bundle));
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
                    String url = PreferencesManager.getString(getContext(), "URL") + "selectClientesFavoritos/"
                            + PreferencesManager.getString(getContext(), "cduser");

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
                    String url = PreferencesManager.getString(getContext(), "URL") + "selectFiliaisFavoritos/"
                            + PreferencesManager.getString(getContext(), "cduser");

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
                    String url = PreferencesManager.getString(getContext(), "URL") + "favFilter";
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
                    String url = PreferencesManager.getString(getContext(), "URL") + "favFilialFilter";
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
