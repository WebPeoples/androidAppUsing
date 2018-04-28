package com.example.goku.using.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.example.goku.using.R;
import com.example.goku.using.domain.UsingDB;
import com.example.goku.using.prefs.PreferencesManager;

public class TutorialActivity extends AppCompatActivity {
    //Fotos do tutorial
    private int[] imgTutorial = {R.drawable.tela_um, R.drawable.tela_dois, R.drawable.tela_tres};
    //Instancia do objeto ImageSwitcher
    private ImageSwitcher imageSwitcher;
    //id da imagem que está sendo exibida
    private int idx = 0;
    private final String TAG = "Tuto";
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tutorial);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            // Configurando o ImageSwitcher
            imageSwitcher = (ImageSwitcher) findViewById(R.id.imgTutorial);
            imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    ImageView img = new ImageView(getBaseContext());
                    img.setImageResource(imgTutorial[0]);

                    return img;
                }
            });
            imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
            View btProximo = findViewById(R.id.buttonTutorial);
            idx = 1;
            btProximo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (idx == imgTutorial.length) {
                        idx = 0;
                        Intent intent = new Intent(TutorialActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        imageSwitcher.setImageResource(imgTutorial[idx++]);
                    }
                }
            });
        }

    @Override
    protected void onStart() {
        super.onStart();
        if (PreferencesManager.getInt(this, "Tuto") == 0){
            PreferencesManager.setInteger(this, "Tuto", 1);
        }else{
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
