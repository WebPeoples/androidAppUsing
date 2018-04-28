package com.example.goku.using.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import android.os.Handler;

import com.example.goku.using.R;
import com.example.goku.using.domain.UsingDB;
import com.example.goku.using.prefs.PreferencesManager;

public class MainActivity extends BaseActivity {
    //Views que serão utilizadas na Activity
    private ImageView u_cortado;
    private ImageView sing;
    private ImageView frase;
    private ImageView fundo2;
    private Handler handler;
    private final String TAG = "MainA";

    //Timer para o fim da tela inicial
    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

		PreferencesManager.setString(this, "URL", "http://192.168.15.14:8080/WebUsing/ws/usingws/");
        PreferencesManager.setString(this, "URL_IMG", "http://192.168.15.14:8080/WebUsing/img/");

            //Verificamos se o GPS está ativo
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

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
                        toast(MainActivity.this, "Ative o GPS.");
                    }
                });
            } else {
                Log.d(TAG, "onCreate: PRIMEIRA VISITA");
                //TORNA A ACTIVITY FULL SCREEN
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                //Instanciando views que serão utilizadas
                u_cortado = (ImageView) findViewById(R.id.u_cortado);
                sing = (ImageView) findViewById(R.id.sing);
                frase = (ImageView) findViewById(R.id.frase);
                fundo2 = (ImageView) findViewById(R.id.fundo2);

                //AnimationSet U-Cortado
                AnimationSet animacoesU = new AnimationSet(true);
                //AnimationSet Todos as imagens
                AnimationSet animacoesGeral = new AnimationSet(true);
                // DEFININDO O FADE IN
                Animation fadein = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
                fadein.setDuration(1500);
                //DEFININDO O AUMENTO DO U-CORTADO
                ScaleAnimation aumentarU = new ScaleAnimation(
                        1.0f, 2.0f,
                        1.0f, 2.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                );
                aumentarU.setDuration(2000);
                //DEFININDO A MOVIMENTAÇÃO PARA O LADO
                Animation AnimationMoverParaOLado = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, -0.5f, Animation.RELATIVE_TO_SELF, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
                );
                AnimationMoverParaOLado.setDuration(2000);

                //Adicionando Fade in ao AnimationSet 'animacoesGeral'
                animacoesGeral.addAnimation(fadein);
                //Adicionando o aumento do U ao AnimationSet 'animacoesU'
                animacoesU.addAnimation(aumentarU);
                //Adicionando Fade in ao AnimationSet 'animacoesU'
                animacoesU.addAnimation(fadein);
                //Adicionando a movimentação para o lado no AnimationSet 'animacoesU'
                animacoesU.addAnimation(AnimationMoverParaOLado);

                //Define se a animação permanece após ser executada
                animacoesU.setFillAfter(true);

                //Anima as views instaciandas
                u_cortado.startAnimation(animacoesU);
                sing.startAnimation(animacoesGeral);
                frase.startAnimation(animacoesGeral);
                //Configura o Fade Out das views
                ObjectAnimator fadeout = ObjectAnimator.ofFloat(fundo2, "alpha", 1f, 0f);
                ObjectAnimator fadeout1 = ObjectAnimator.ofFloat(frase, "alpha", 1f, 0f);
                ObjectAnimator fadeout3 = ObjectAnimator.ofFloat(sing, "alpha", 1f, 0f);
                fadeout.setDuration(4000);
                fadeout1.setDuration(1500);
                fadeout3.setDuration(1500);
                fadeout.start();
                fadeout1.start();
                fadeout3.start();

                //Esta condição é responsável por perguntar se o app está sendo executado pela primeira vez
                //Se sim, ele navega para a tela de tutorial. Se não, ele pula para a tela de login.

                if (PreferencesManager.getInt(this, "Main") == 0) {
                    handler = new Handler();


                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(MainActivity.this, TutorialActivity.class);
                            startActivity(i);
                            MainActivity.this.finish();

                            //Guarda um valor no banco para iniciá-lo com objetivo de dar Updates na tabela
                            //posteriormente
                            new UsingDB(MainActivity.this).save();
                            new UsingDB(MainActivity.this).select();
                        }
                    };
                    handler.postDelayed(runnable, SPLASH_TIME_OUT);
                }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (PreferencesManager.getInt(this, "Main") == 0){
            PreferencesManager.setInteger(this, "Main", 1);
        }else{
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    /* @Override
    protected void onResume() {
        super.onResume();
        //Verificamos se o GPS está ativo
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

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
                    toast(MainActivity.this, "Ative o GPS.");
                }
            });
        } else {

            //TORNA A ACTIVITY FULL SCREEN
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //Instanciando views que serão utilizadas
            u_cortado = (ImageView) findViewById(R.id.u_cortado);
            sing = (ImageView) findViewById(R.id.sing);
            frase = (ImageView) findViewById(R.id.frase);
            fundo2 = (ImageView) findViewById(R.id.fundo2);

            //AnimationSet U-Cortado
            AnimationSet animacoesU = new AnimationSet(true);
            //AnimationSet Todos as imagens
            AnimationSet animacoesGeral = new AnimationSet(true);
            // DEFININDO O FADE IN
            Animation fadein = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
            fadein.setDuration(1500);
            //DEFININDO O AUMENTO DO U-CORTADO
            ScaleAnimation aumentarU = new ScaleAnimation(
                    1.0f, 2.0f,
                    1.0f, 2.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );
            aumentarU.setDuration(2000);
            //DEFININDO A MOVIMENTAÇÃO PARA O LADO
            Animation AnimationMoverParaOLado = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, -0.5f, Animation.RELATIVE_TO_SELF, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
            );
            AnimationMoverParaOLado.setDuration(2000);

            //Adicionando Fade in ao AnimationSet 'animacoesGeral'
            animacoesGeral.addAnimation(fadein);
            //Adicionando o aumento do U ao AnimationSet 'animacoesU'
            animacoesU.addAnimation(aumentarU);
            //Adicionando Fade in ao AnimationSet 'animacoesU'
            animacoesU.addAnimation(fadein);
            //Adicionando a movimentação para o lado no AnimationSet 'animacoesU'
            animacoesU.addAnimation(AnimationMoverParaOLado);

            //Define se a animação permanece após ser executada
            animacoesU.setFillAfter(true);

            //Anima as views instaciandas
            u_cortado.startAnimation(animacoesU);
            sing.startAnimation(animacoesGeral);
            frase.startAnimation(animacoesGeral);
            //Configura o Fade Out das views
            ObjectAnimator fadeout = ObjectAnimator.ofFloat(fundo2, "alpha", 1f, 0f);
            ObjectAnimator fadeout1 = ObjectAnimator.ofFloat(frase, "alpha", 1f, 0f);
            ObjectAnimator fadeout3 = ObjectAnimator.ofFloat(sing, "alpha", 1f, 0f);
            fadeout.setDuration(4000);
            fadeout1.setDuration(1500);
            fadeout3.setDuration(1500);
            fadeout.start();
            fadeout1.start();
            fadeout3.start();

            //Esta condição é responsável por perguntar se o app está sendo executado pela primeira vez
            //Se sim, ele navega para a tela de tutorial. Se não, ele pula para a tela de login.
            handler = new Handler();
            if (PreferencesManager.getInt(this, "FirstVisit") == 0) {
                PreferencesManager.setInteger(this, "FirstVisit", 1);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(MainActivity.this, TutorialActivity.class);
                        startActivity(i);
                        MainActivity.this.finish();

                        //Guarda um valor no banco para iniciá-lo com objetivo de dar Updates na tabela
                        //posteriormente
                        new UsingDB(MainActivity.this).save();
                        new UsingDB(MainActivity.this).select();
                    }
                };
                handler.postDelayed(runnable, SPLASH_TIME_OUT);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        }
    }*/
}
