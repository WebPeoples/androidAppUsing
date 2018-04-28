package com.example.goku.using.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goku.using.R;
import com.example.goku.using.prefs.PreferencesManager;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Goku on 24/06/2017.
 */

public class PromoRecyclerAdapter extends RecyclerView.Adapter<PromoRecyclerAdapter.ViewHolder> {

    private PromoOnClickListener promoOnClickListener;
    private Context context;
    private List<String> fotoProduto;
    private List<String> fotoCliente;
    private List<String> preco;

    public PromoRecyclerAdapter(Context context, PromoOnClickListener promoOnClickListener, List<String> fotoCliente,
                                List<String> fotoProduto, List<String> preco) {
        this.context = context;
        this.promoOnClickListener = promoOnClickListener;
        this.fotoCliente = fotoCliente;
        this.fotoProduto = fotoProduto;
        this.preco = preco;
    }

    class ViewHolder extends RecyclerView.ViewHolder{


        public ImageView produto;
        public ImageView loja;
        public TextView preco;

        public ViewHolder(final View itemView) {
            super(itemView);
            produto = (ImageView) itemView.findViewById(R.id.promo_card_produto_img);
            loja = (ImageView) itemView.findViewById(R.id.promo_card_loja_img);
            preco = (TextView) itemView.findViewById(R.id.promo_card_preco);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                    Snackbar.make(v, "Click detected on item " + position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
    }

    public interface PromoOnClickListener{
        public void onClickPromo(View view, final int idx);
    }

    @Override
    public PromoRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.promo_card_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        String URL = PreferencesManager.getString(context, "URL_IMG") +"produtos/{foto}";
        Picasso.with(context).load(URL.replace("{foto}", fotoProduto.get(i))).into(viewHolder.produto);

        String URL_CLIENTE = PreferencesManager.getString(context, "URL_IMG") + "loja_logo/{foto}";
        Picasso.with(context).load(URL_CLIENTE.replace("{foto}", fotoCliente.get(i))).into(viewHolder.loja);

        if (preco != null && preco.size() != 0)
        viewHolder.preco.setText("R$" + preco.get(i));

        if (promoOnClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                int position = i;
                @Override
                public void onClick(View v) {
                    promoOnClickListener.onClickPromo(viewHolder.itemView, position);
                    Log.e("PromoRecyclerAdapter", String.valueOf(position));

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return fotoProduto.size();
    }
}