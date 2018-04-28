package com.example.goku.using.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goku.using.R;

import com.example.goku.using.activity.LojaActivity;
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

public class AdapterLoja extends RecyclerView.Adapter<AdapterLoja.ViewHolder> {

    private static final String TAG = "AdapterLoja";
    private produtoOnClickListener produtoOnClickListener;
    private List<String> list;
    private List<String> listTitle;
    private List<String> listPreco;
    private List<String> ifPromo;
    private List<String> oldPriceList;
    private List<String> porcentagemPreco;
    private LojaActivity.clickToggle clickToggle;
    private String cnpj;

    public AdapterLoja(LojaActivity.clickToggle produtoOnClickListener) {
        this.clickToggle = produtoOnClickListener;
    }

    public AdapterLoja(AdapterLoja.produtoOnClickListener produtoOnClickListener,
                       List<String> list,
                       List<String> listTitle,
                       List<String> listPreco,
                       List<String> ifPromo,
                       List<String> oldPriceList,
                       List<String> porcentagemPreco,
                       Context context,
                       String cnpj) {
        this.produtoOnClickListener = produtoOnClickListener;
        this.list = list;
        this.context = context;
        this.listTitle = listTitle;
        this.listPreco = listPreco;
        this.ifPromo = ifPromo;
        this.oldPriceList = oldPriceList;
        this.porcentagemPreco = porcentagemPreco;
        this.cnpj = cnpj;
    }

    private Context context;


    static class ViewHolder extends RecyclerView.ViewHolder {

        private int currentItem;
        private ImageView itemImage;
        private TextView itemTitle;
        private TextView itemDetail;
        private TextView placaDetalhe;
        private TextView oldPrice;
        private TextView ifPromo;
        private TextView porcentagemPreco;

        private ViewHolder(final View itemView) {
            super(itemView);

            itemTitle = (TextView) itemView.findViewById(R.id.cardText);
            itemImage = (ImageView) itemView.findViewById(R.id.imageView2);
            itemDetail = (TextView) itemView.findViewById(R.id.textView);
            placaDetalhe = (TextView) itemView.findViewById(R.id.placaDetalhe);
            oldPrice = (TextView) itemView.findViewById(R.id.oldPrice);
            ifPromo = (TextView) itemView.findViewById(R.id.ifPromo);
            porcentagemPreco = (TextView) itemView.findViewById(R.id.porcentagemPreco);
        }
    }

    public interface produtoOnClickListener {
        public void onClickProduto(View view, final int idx);
    }

    public interface clickToggleLoja {
        public void onClickBtn(View view);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.loja_card_list, viewGroup, false);


        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        cnpj = cnpj.replace("/", "");
        String URL = PreferencesManager.getString(context, "URL_IMG") + "produtos/"+cnpj+"/"+"{foto}";
        Picasso.with(context).load(URL.replace("{foto}", list.get(i))).into(viewHolder.itemImage);
        viewHolder.itemTitle.setText(listTitle.get(i));

        if (listPreco.size() != 0)
            viewHolder.itemDetail.setText("R$ " + listPreco.get(i));

        if (ifPromo.get(i).equalsIgnoreCase("s")) {
            viewHolder.oldPrice.setText(Html.fromHtml("<strike>R$" + oldPriceList.get(i) + "</strike>"));
            viewHolder.porcentagemPreco.setText(porcentagemPreco.get(i));
        } else {
            viewHolder.oldPrice.setVisibility(View.INVISIBLE);
            viewHolder.ifPromo.setVisibility(View.INVISIBLE);
            viewHolder.porcentagemPreco.setVisibility(View.INVISIBLE);
        }

        if (produtoOnClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                int position = i;

                @Override
                public void onClick(View v) {
                    produtoOnClickListener.onClickProduto(viewHolder.itemView, position);
                    Log.e("AdapterLoja", String.valueOf(position));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}