package com.example.goku.using.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.example.goku.using.PermissionUtils;
import com.example.goku.using.R;
import com.example.goku.using.prefs.PreferencesManager;

import static java.security.AccessController.getContext;

/**
 * Created by Goku on 25/06/2017.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);

            // Lista de permissões necessárias.
            String permissions[] = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            };

            // Valida lista de permissões.
            boolean ok = PermissionUtils.validate(this, 0, permissions);

            if (ok) {
                // Tudo OK, pode entrar.
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (PreferencesManager.getInt(this, "Splash") == 0){
            PreferencesManager.setInteger(this, "Splash", 1);
        }else{
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                // Negou a permissão. Mostra alerta e fecha.
                alert(SplashActivity.this, R.string.app_name, R.string.mensagem_alerta_permission, R.string.button, new Runnable() {
                    @Override
                    public void run() {
                        // Negou permissão. Sai do app.
                        finish();
                    }
                });
                return;
            }
        }

        // Permissões concedidas, pode entrar.
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
