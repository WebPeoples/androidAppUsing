package com.example.goku.using.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.goku.using.R;
import com.example.goku.using.prefs.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class FavFragment_esquerda extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fav_fragment_esquerda, container, false);

        Button btAlimentos = (Button) view.findViewById(R.id.fav_ac_btalimentos);
        Button btVestu = (Button) view.findViewById(R.id.fav_ac_btVestu);
        Button btBeb = (Button) view.findViewById(R.id.fav_ac_btBebida);
        Button btServicos = (Button) view.findViewById(R.id.fav_ac_btServico);
        Button btSaude = (Button) view.findViewById(R.id.fav_ac_btSaude);
        Button btVarejo = (Button) view.findViewById(R.id.fav_ac_btVarejo);
        Button btManu = (Button) view.findViewById(R.id.fav_ac_btManu);
        Button btEletro = (Button) view.findViewById(R.id.fav_ac_btEletro);

        btAlimentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> lista = new ArrayList<String>();
                lista.add(0, PreferencesManager.getString(getContext(), "cduser"));
                lista.add(1, "ALIME");
                ((FavFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fav_frame_direita)).load_data_filter(lista);

                List<String> listaFilial = new ArrayList<String>();
                listaFilial.add(0, PreferencesManager.getString(getContext(), "cduser"));
                listaFilial.add(1, "ALIME");
                ((FavFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fav_frame_direita)).load_data_filter_filial(listaFilial);
                ((FavFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fav_frame_direita)).limparRecycler();

            }
        });


        btVestu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> lista = new ArrayList<String>();
                lista.add(0, PreferencesManager.getString(getContext(), "cduser"));
                lista.add(1, "VESTU");
                ((FavFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fav_frame_direita)).load_data_filter(lista);

                List<String> listaFilial = new ArrayList<String>();
                listaFilial.add(0, PreferencesManager.getString(getContext(), "cduser"));
                listaFilial.add(1, "VESTU");
                ((FavFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fav_frame_direita)).load_data_filter_filial(listaFilial);
                ((FavFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fav_frame_direita)).limparRecycler();
            }
        });

        btBeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> lista = new ArrayList<String>();
                lista.add(0, PreferencesManager.getString(getContext(), "cduser"));
                lista.add(1, "BEBIDA");
                ((FavFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fav_frame_direita)).load_data_filter(lista);

                List<String> listaFilial = new ArrayList<String>();
                listaFilial.add(0, PreferencesManager.getString(getContext(), "cduser"));
                listaFilial.add(1, "BEBIDA");
                ((FavFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fav_frame_direita)).load_data_filter_filial(listaFilial);
                ((FavFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fav_frame_direita)).limparRecycler();
            }
        });
        btServicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> lista = new ArrayList<String>();
                lista.add(0, PreferencesManager.getString(getContext(), "cduser"));
                lista.add(1, "SERVIC");
                ((FavFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fav_frame_direita)).load_data_filter(lista);
                List<String> listaFilial = new ArrayList<String>();
                listaFilial.add(0, PreferencesManager.getString(getContext(), "cduser"));
                listaFilial.add(1, "SERVIC");
                ((FavFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fav_frame_direita)).load_data_filter_filial(listaFilial);

                ((FavFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fav_frame_direita)).limparRecycler();
            }
        });

        btSaude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> lista = new ArrayList<String>();
                lista.add(0, PreferencesManager.getString(getContext(), "cduser"));
                lista.add(1, "SAUDE");
                ((FavFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fav_frame_direita)).load_data_filter(lista);
                List<String> listaFilial = new ArrayList<String>();
                listaFilial.add(0, PreferencesManager.getString(getContext(), "cduser"));
                listaFilial.add(1, "SAUDE");
                ((FavFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fav_frame_direita)).load_data_filter_filial(listaFilial);

                ((FavFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fav_frame_direita)).limparRecycler();
            }
        });

        btVarejo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> lista = new ArrayList<String>();
                lista.add(0, PreferencesManager.getString(getContext(), "cduser"));
                lista.add(1, "VAREJO");
                ((FavFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fav_frame_direita)).load_data_filter(lista);
                List<String> listaFilial = new ArrayList<String>();
                listaFilial.add(0, PreferencesManager.getString(getContext(), "cduser"));
                listaFilial.add(1, "VAREJO");
                ((FavFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fav_frame_direita)).load_data_filter_filial(listaFilial);

                ((FavFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fav_frame_direita)).limparRecycler();
            }
        });
        btManu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btEletro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> lista = new ArrayList<String>();
                lista.add(0, PreferencesManager.getString(getContext(), "cduser"));
                lista.add(1, "ELETRO");
                ((FavFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fav_frame_direita)).load_data_filter(lista);
                List<String> listaFilial = new ArrayList<String>();
                listaFilial.add(0, PreferencesManager.getString(getContext(), "cduser"));
                listaFilial.add(1, "ELETRO");
                ((FavFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fav_frame_direita)).load_data_filter_filial(listaFilial);

                ((FavFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fav_frame_direita)).limparRecycler();
            }
        });

        return view;
    }


}
