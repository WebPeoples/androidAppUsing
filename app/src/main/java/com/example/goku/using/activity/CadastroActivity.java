package com.example.goku.using.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goku.using.R;
import com.example.goku.using.prefs.PreferencesManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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

public class CadastroActivity extends BaseActivity{


    private EditText nome;
    private EditText nascimento;
    private EditText cpf;
    private EditText telefone1;
    private EditText telefone2;
    private EditText cep;
    private EditText pais;
    private EditText email;
    private EditText emailConfirm;
    private EditText senha;
    private EditText senhaConfirm;
    private ImageView btConfirm;
    private List<String> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        nome = (EditText) findViewById(R.id.cadastro_nome);
        telefone2 = (EditText) findViewById(R.id.cadstro_celular);
        email = (EditText) findViewById(R.id.cadastro_email);
        emailConfirm = (EditText) findViewById(R.id.cadastro_email_confirm);
        senha = (EditText) findViewById(R.id.cadastro_senha);
        senhaConfirm = (EditText) findViewById(R.id.cadastro_senha_confirm);
        btConfirm = (ImageView) findViewById(R.id.btConfirm);

    }

    public void btCadastrar(View view) {
        String nome = this.nome.getText().toString();
//        String cpf = this.cpf.getText().toString();
        String telefone2 = this.telefone2.getText().toString();
        String email = this.email.getText().toString();
        String emailConfirm = this.emailConfirm.getText().toString();
        String senha = this.senha.getText().toString();
        String senhaConfirm = this.senhaConfirm.getText().toString();

            if (!email.equals("") && !emailConfirm.equals("")) {
                if (!senha.equals("") && !senhaConfirm.equals("")) {
                    if (email.equals(emailConfirm)) {
                        if (senha.equals(senhaConfirm)) {
                            lista = new ArrayList<>();
                            lista.add(0, nome);
                            lista.add(1, telefone2);
                            lista.add(2, email);
                            lista.add(3, senha);
                            WarningDialog("Nome: " + nome + '\n'+"Celular: " + telefone2 + '\n'
                                    + "Email: " + email + '\n' + "Confirmação email: " + emailConfirm + '\n' +
                                    "Senha: " + senha + '\n' + "Confirmação senha: " + senhaConfirm, lista);
                        } else {
                        toast(this, "As senhas não coincidem.");
                        }
                    } else {
                            toast(this, "Os emails não coincidem.");
                    }
                } else {
                    toast(this, "Digitie uma senha.");
                }
            }else{
                toast(this, "Digite um email.");
            }

    }

    protected void WarningDialog(String text, final List<String> lista){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.alert_dialog_cadastro, null);

        Button ok = (Button) view.findViewById(R.id.alert_dialog_btOk);
        Button cancel = (Button) view.findViewById(R.id.alert_dialog_btCancel);
        TextView textView = (TextView) view.findViewById(R.id.text_dialog);

        cancel.setText("Cancelar");

        textView.setText(text);

        builder.setView(view);
        final AlertDialog dialog = builder.create();

        dialog.show();
        dialog.setCanceledOnTouchOutside(false);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_user_app(lista);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });
    }

    private void upload_user_app(final List<String> lista){
        final AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    Gson g = new Gson();

                    String url = PreferencesManager.getString(CadastroActivity.this, "URL") + "cadastro";

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
                if (s != null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CadastroActivity.this);
                    builder.setMessage(s)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(CadastroActivity.this, LoginActivity.class));
                                }
                            });
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);
                }else {
                    WarningDialog("Houve um problema em cadastrá-lo.");
                }
            }
        };
        task.execute();
    }

}
