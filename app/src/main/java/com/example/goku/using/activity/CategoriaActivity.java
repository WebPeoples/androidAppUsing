package com.example.goku.using.activity;

import android.os.Bundle;
import com.example.goku.using.R;
import com.example.goku.using.fragment.CategoriaTabFragment;

public class CategoriaActivity extends BaseActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        //Método que instancia a toolbar como act bar (BASE ACTIVITY)
            setUpToolBar();
            setUpNavDrawer();

            replaceFragment(new CategoriaTabFragment());
    }
}
