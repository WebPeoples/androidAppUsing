package com.example.goku.using.service;

import android.os.AsyncTask;
import android.util.Log;

import com.example.goku.using.domain.WebUsingDomain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Goku on 30/06/2017.
 */

public class GetList extends AsyncTask<Void,Void,List<String>> {


    private static final String TAG = "USING";
    private static final String URL = "http://192.168.1.106:8080/WebUsing/ws/generic";



    //SubClasse que contém os métodos que fará a requisição para webservice retornar os dados do banco de dados


        //Método executado antes de carregar a lista, estou definindo a progressbar como visível.
        @Override
        protected void onPreExecute() {
            //updateDisplay("Iniciando Task...");
            //progressBar.setVisibility(View.VISIBLE);
        }

        //Método que busca a requisição da web service(em segundo plano)
        @Override
        protected List<String> doInBackground(Void... params) {
            //Instanciando a lista na qual será preenchida com os dados da webservice
            List<String> hover = new ArrayList<>();

            //Criando objetos que serão usados para a requisição
            HttpURLConnection conn = null;
            URL u = null;
            String s = null;

            try {
                //Instanciando objeto responsável por conter a URL do método que está na web service
                u = new URL(URL);

                //Instanciando e abrindo conexão com o método da webservice
                conn = (HttpURLConnection) u.openConnection();
                //Definindo o tipo de requisição que será feito (requisição via GET)
                conn.setRequestMethod("GET");
                //Definindo o tempo máximo em que a requisição será feita(após 15 milisegundos a requisição será cancelada)
                conn.setConnectTimeout(15000);
                //Definindo o tempo máximo em que a resposta terá que chegar antes da requisição ser cancelada
                conn.setReadTimeout(15000);
                //Conectando com o método que está na webservice
                conn.connect();

                //Criando objeto que receberá a resposta(conteúdo do banco de dados)
                InputStream in = null;

                //conn.getInputStream é o método responsável por receber os dados da requisição, estou colocando estes valores no Objeto "in"
                in = conn.getInputStream();
                //Chamando o método que está na classe webservice "toString", este método transforma os dados recebidos do banco de dados em uma string
                s = Converter.toString(in, "UTF-8");
                //Fechando conexão com a webservice para evitar baixo desempenho já que o papel da web service já foi feito neste neste método
                in.close();

                //Criando objeto responsável por ler os dados recebidos(Os dados recebidos vieram no tipo JSON)
                JSONArray jsonArray;

                //Instanciando o objeto com a variável 's' que contém os dados recebidos da webservice(os dados estão no formato JSON)
                jsonArray = new JSONArray(s);

                //Criando um for baseando-se na quantidade de lista(jsonArray.length) que existe no objeto jsonArray
                for (int i = 0; i < jsonArray.length(); i++) {
                    //Criando e instanciando um objeto responsável por receber os objetos contidos dentro das listas que foram definidas no objeto jsonArray
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    hover.add(jsonObject.getString("foto"));
                    Log.d(TAG, "doInBackground: " + jsonObject.getString("foto"));

                }
                // retornando a lista 'hover' já preenchida com os dados do banco de dados
                return hover;
            } catch(IOException | JSONException e){
                e.printStackTrace();
                return null;
            } finally{
                if (conn != null) {
                    conn.disconnect();
                }
            }

        }

        //Método que será executado após o término da execução do método acima(doInBackground)
        @Override
        protected void onPostExecute(List<String> list) {
        }

    }

