package com.example.goku.using.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goku.using.R;
import com.example.goku.using.domain.WebUsingDomain;
import com.example.goku.using.prefs.PreferencesManager;
import com.example.goku.using.service.Converter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by Goku on 24/06/2017.
 */

public class FavRecyclerAdapter extends RecyclerView.Adapter<FavRecyclerAdapter.ViewHolder> {

    private LojaOnClickListener lojaOnClickListener;
    private static String TAG = "ProjectUSING";
    private List<String> fotoCliente;
    private Context context;
    private List<String> nr_cpf_cnpj;

    public FavRecyclerAdapter(LojaOnClickListener lojaOnClickListener, List<String> fotoCliente,
                              Context context,  List<String> nr_cpf_cnpj) {
        this.lojaOnClickListener = lojaOnClickListener;
        this.fotoCliente = fotoCliente;
        this.context = context;
        this.nr_cpf_cnpj = nr_cpf_cnpj;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView lojas;


        public ViewHolder(final View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolder: exec...");
            lojas = (ImageView) itemView.findViewById(R.id.fav_card_loja_img);
        }
    }

    public interface LojaOnClickListener{
        public void onClickLoja(View view, final int idx);
    }

    @Override
    public FavRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fav_card_list, viewGroup, false);

       ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder1, final int i) {
        String URL = PreferencesManager.getString(context, "URL_IMG") + "loja_logo/{foto}";

        //Log.d("USING", "onBindViewHolder: " + pathLojas[i]);
        Picasso.with(context).load(URL.replace("{foto}", nr_cpf_cnpj.get(i).replace("/", "") +"/"+ fotoCliente.get(i))).into(viewHolder1.lojas);

        if (lojaOnClickListener != null) {
            viewHolder1.itemView.setOnClickListener(new View.OnClickListener() {
                int position = i;
                @Override
                public void onClick(View v) {
                    lojaOnClickListener.onClickLoja(viewHolder1.itemView, position);
                    Log.e("FavRecyclerAdapter", String.valueOf(position));
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return fotoCliente.size();
    }
}