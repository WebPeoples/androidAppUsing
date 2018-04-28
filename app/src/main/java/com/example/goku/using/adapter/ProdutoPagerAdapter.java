package com.example.goku.using.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.goku.using.R;
import com.example.goku.using.prefs.PreferencesManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Goku on 11/07/2017.
 */

public class ProdutoPagerAdapter extends PagerAdapter {

    private Context context;
    private final String TAG = "ProdutoPagerAdapter";
    private ArrayList<String> produtoFotos;
    private String cnpj;
    int count;
   // private String[] produtoFotos;

    public ProdutoPagerAdapter(Context context, ArrayList<String> produtoFotos, String cnpj) {
        this.context = context;
        this.produtoFotos = produtoFotos;
        count = this.produtoFotos.size();
        this.cnpj = cnpj;
    }

    @Override
    public int getCount() {

        return produtoFotos.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_imagem, container, false);
        ImageView img = (ImageView) view.findViewById(R.id.imgProdutoAdapter);
        Log.d(TAG, "instantiateItem: " + produtoFotos.get(position));
        String URL = PreferencesManager.getString(context, "URL_IMG") + "produtos/"+cnpj.replace("/","")+"/"+"{foto}";
        Picasso.with(context).load(URL.replace("{foto}", produtoFotos.get(position))).into(img);
        ((ViewGroup) container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
