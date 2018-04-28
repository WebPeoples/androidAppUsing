package com.example.goku.using.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.goku.using.R;
import com.example.goku.using.domain.WebUsingDomain;
import com.github.kimkevin.cachepot.CachePot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 */
public class PromoFragment_esquerda extends Fragment {

    public PromoFragment_esquerda() {
        // Required empty public constructor
    }
    private List<String> fotoProduto;
    private List<String> fotoCliente;
    private List<String> preco;
    private RecyclerView.Adapter rvAdapter;

    private Communicate cm;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_promo_fragment_esquerda, container, false);
        fotoCliente = new ArrayList<>();
        fotoProduto = new ArrayList<>();
        preco = new ArrayList<>();
            Button btAlimentos = (Button) view.findViewById(R.id.promo_activity_btAlimentos);
            Button btVestu = (Button) view.findViewById(R.id.promo_activity_btVestu);
            Button btBeb = (Button) view.findViewById(R.id.promo_activity_btBebida);
            Button btServicos = (Button) view.findViewById(R.id.promo_activity_btServicos);
            Button btSaude = (Button) view.findViewById(R.id.promo_activity_btSaude);
            Button btVarejo = (Button) view.findViewById(R.id.promo_activity_btVarejo);
            Button btManu = (Button) view.findViewById(R.id.promo_activity_btManu);
            Button btLazer = (Button) view.findViewById(R.id.promo_activity_btLazer);

            btAlimentos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((PromoFragment_direita) getActivity().getSupportFragmentManager()
                            .findFragmentById(R.id.promo_frame_direita)).load_data_filter("ALIMEN");

                    ((PromoFragment_direita) getActivity().getSupportFragmentManager()
                            .findFragmentById(R.id.promo_frame_direita)).load_data_filter_filial("ALIMEN");
                }
            });

            btVestu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((PromoFragment_direita) getActivity().getSupportFragmentManager()
                            .findFragmentById(R.id.promo_frame_direita)).load_data_filter("VESTU");
                    ((PromoFragment_direita) getActivity().getSupportFragmentManager()
                            .findFragmentById(R.id.promo_frame_direita)).load_data_filter_filial("VESTU");
                }
            });

            btBeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PromoFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.promo_frame_direita)).load_data_filter("BEBIDA");
                ((PromoFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.promo_frame_direita)).rvAdapter.notifyDataSetChanged();
                ((PromoFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.promo_frame_direita)).load_data_filter_filial("BEBIDA");
            }
        });
        btServicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PromoFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.promo_frame_direita)).load_data_filter("SERVIC");
                ((PromoFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.promo_frame_direita)).rvAdapter.notifyDataSetChanged();
                ((PromoFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.promo_frame_direita)).load_data_filter_filial("SERVIC");
            }
        });

        btSaude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PromoFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.promo_frame_direita)).load_data_filter("SAUDE");
                ((PromoFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.promo_frame_direita)).rvAdapter.notifyDataSetChanged();
                ((PromoFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.promo_frame_direita)).load_data_filter_filial("SAUDE");
            }
        });

        btVarejo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PromoFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.promo_frame_direita)).load_data_filter("VAREJO");
                ((PromoFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.promo_frame_direita)).rvAdapter.notifyDataSetChanged();
                ((PromoFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.promo_frame_direita)).load_data_filter_filial("VAREJO");
            }
        });
        btManu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btLazer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PromoFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.promo_frame_direita)).load_data_filter("ELETRO");
                ((PromoFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.promo_frame_direita)).rvAdapter.notifyDataSetChanged();
                ((PromoFragment_direita) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.promo_frame_direita)).load_data_filter_filial("ELETRO");
            }
        });
            return view;
    }

        @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
