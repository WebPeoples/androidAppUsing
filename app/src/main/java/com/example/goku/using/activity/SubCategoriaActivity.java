package com.example.goku.using.activity;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.goku.using.R;
import com.example.goku.using.domain.CatSubFragDomain;
import com.example.goku.using.fragment.CategoriaTabFragment;
import com.example.goku.using.fragment.SubCategoriaTabFrag;

public class SubCategoriaActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categoria);



            setUpToolBar();
            setUpNavDrawer();

            int frag = 0;

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                frag = bundle.getInt("replaceFragment");
                new CatSubFragDomain().setFabTab(bundle.getInt("replaceFragment"));
            }

            Log.e("SubCategoriaActivity", String.valueOf(frag));

            replaceFragmentSub(new SubCategoriaTabFrag());


    }
}
