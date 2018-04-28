package com.example.goku.using.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.goku.using.R;
import com.example.goku.using.activity.SubCategoriaActivity;
import com.example.goku.using.domain.CatSubFragDomain;

/**
 * Created by Goku on 29/04/2017.
 */
public class CategoriaFragment2 extends android.support.v4.app.Fragment {
    private Button btSaude;
    private Button btVarejo;
    private Button btManutencao;
    private Button btLazer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState){
        View view = inflater.inflate(R.layout.categoria_fragment_2, container, false);

        btSaude = (Button) view.findViewById(R.id.categoriaSaude);
        btVarejo = (Button) view.findViewById(R.id.categoriaVarejo);
        btManutencao = (Button) view.findViewById(R.id.categoriaManutencao);
        btLazer = (Button) view.findViewById(R.id.categoriaLazer);

        btSaude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CatSubFragDomain.setNumFrag(1);
                Bundle bundle = new Bundle();
                bundle.putInt("replaceFragment", 5);
                Intent intent = new Intent(getContext(), SubCategoriaActivity.class);
                startActivity(intent.putExtras(bundle));
            }
        });

        btVarejo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CatSubFragDomain.setNumFrag(2);
                Bundle bundle = new Bundle();
                bundle.putInt("replaceFragment", 6);
                Intent intent = new Intent(getContext(), SubCategoriaActivity.class);
                startActivity(intent.putExtras(bundle));
            }
        });

        btManutencao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CatSubFragDomain.setNumFrag(1);
                Bundle bundle = new Bundle();
                bundle.putInt("replaceFragment", 7);
                Intent intent = new Intent(getContext(), SubCategoriaActivity.class);
                startActivity(intent.putExtras(bundle));
            }
        });

        btLazer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CatSubFragDomain.setNumFrag(1);
                Bundle bundle = new Bundle();
                bundle.putInt("replaceFragment", 8);
                Intent intent = new Intent(getContext(), SubCategoriaActivity.class);
                startActivity(intent.putExtras(bundle));
            }
        });

        return view;
    }
}
