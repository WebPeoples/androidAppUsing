package com.example.goku.using.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.goku.using.R;
import com.example.goku.using.domain.UsingDB;
import com.example.goku.using.prefs.PreferencesManager;
import com.example.goku.using.service.NotificationIdService;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class LoginActivity extends BaseActivity {

    //Views que serão utilizadas na Activity
    private EditText editEmail;
    private EditText editSenha;
    private Button btEntrar;
    private Button cadastrar;
    private ProgressDialog dialog;

    //TAG para os logs
    private static final String TAG = "Using";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


         dialog = new ProgressDialog(this);


        //Verificamos se o GPS está ativo
       /* LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

            //CASO NÃO ESTEJA, CHAMAMOS UM DIALOG QUE LEVARÁ O USUÁRIO ATÉ A TELA DE CONFIGURAÇÃO
            //RESPONSÁVEL POR LIGAR O GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.alert_dialog_gps, null);
            Button ok = (Button) view.findViewById(R.id.alert_dialog_btOk);
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                    toast(LoginActivity.this, "Ative o GPS.");
                }
            });*/
      //  }else {

            editEmail = (EditText) findViewById(R.id.edit_email_Login);
            editSenha = (EditText) findViewById(R.id.edit_senha_Login);

            //Navega até a tela de cadastro
            cadastrar = (Button) findViewById(R.id.cadastrar);
            cadastrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                    startActivity(intent);
                }
            });
            //Botão de entrar
            btEntrar = (Button) findViewById(R.id.btEntrar_login);
            btEntrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                String email = editEmail.getText().toString();
                String senha = editSenha.getText().toString();

					if(!email.equals("") && !senha.equals("") ) {
						List<String> lista = new ArrayList<>();
						lista.add(0, email);
						lista.add(1, senha);
						login_load_data(lista);
					}else {
						WarningDialog("Preencha os campos para efetuar o login.");
					}
                }
            });
        //}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!PreferencesManager.getString(this, "username").equals("")){
            startActivity(new Intent(this, MenuActivity.class));
        }
    }

    private void login_load_data(final List<String> lista){
        final AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                btEntrar.setEnabled(false);
                editSenha.setEnabled(false);
                editEmail.setEnabled(false);
                dialog.setMessage("Efetuando login...");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();


            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    Gson g = new Gson();

                    String url = PreferencesManager.getString(LoginActivity.this, "URL") + "login";

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
                    JSONArray js = new JSONArray(s);
                    if (js.length() == 3){
                        PreferencesManager.setString(LoginActivity.this, "email", js.getString(0));
                        PreferencesManager.setString(LoginActivity.this, "username", js.getString(1));
                        PreferencesManager.setString(LoginActivity.this, "cduser", js.getString(2));
                        dialog.hide();
                        sendRegistrationToServer(PreferencesManager.getString(LoginActivity.this, "token"));
                        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                    }else{
                        WarningDialog("Email e ou senha incorretos.");
                        btEntrar.setEnabled(true);
                        editSenha.setEnabled(true);
                        editEmail.setEnabled(true);
                        dialog.hide();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        };
        task.execute();
    }

    private void sendRegistrationToServer(final String token){
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    String url = PreferencesManager.getString(LoginActivity.this, "URL") + "registerToken/";
                    Gson g = new Gson();
                    OkHttpClient client = new OkHttpClient();
                    Request.Builder builder = new Request.Builder();
                    builder.url(url);

                    List<String> list = new ArrayList<>();

                    list.add(token);
                    list.add(PreferencesManager.getString(LoginActivity.this, "cduser"));

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
                //Toast.makeText(NotificationIdService.this, s, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onPostExecute: " + s);
                super.onPostExecute(s);
            }
        };
        task.execute();

    }
}
