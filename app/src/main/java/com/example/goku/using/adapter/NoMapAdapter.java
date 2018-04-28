package com.example.goku.using.adapter;

import android.content.Context;
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

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Goku on 16/08/2017.
 */

public class NoMapAdapter extends  RecyclerView.Adapter<NoMapAdapter.ViewHolder>{


	private static final String TAG = "adapter";
	private List<String> nomeLoja;
	private List<String> endereco;
	private List<String> Logo;
	private List<Float> distancia;
	private Context context;

	private onClickLoja onClickLoja;

	public NoMapAdapter(List<String> nomeLoja, List<String> endereco, List<String> logo, List<Float> distancia,
						Context context, onClickLoja onClickLoja) {
		this.nomeLoja = nomeLoja;
		this.endereco = endereco;
		Logo = logo;
		this.distancia = distancia;
		this.context = context;
		this.onClickLoja = onClickLoja;
	}

	class ViewHolder extends RecyclerView.ViewHolder{

		public ImageView lojas;
		private TextView nomeLoja;
		private TextView endereco;
		private TextView distancia;


		public ViewHolder(final View itemView) {
			super(itemView);
			lojas = (ImageView) itemView.findViewById(R.id.pesquisa_sem_mapa_logo);
			nomeLoja = (TextView) itemView.findViewById(R.id.pesquisa_sem_mapa_nome);
			endereco = (TextView) itemView.findViewById(R.id.pesquisa_sem_mapa_endereco);
			distancia = (TextView) itemView.findViewById(R.id.pesquisa_sem_maps_distancia);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.no_map_card_list, parent, false);

		return new ViewHolder(v);
	}

	public interface onClickLoja{
		public void onClickLoja(View view, int position);
	}


	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {

		String urlFoto = PreferencesManager.getString(context, "URL_IMG")+ "loja_logo/{img}";
		DecimalFormat distance = new DecimalFormat("#.#");
		String distanceFloat = distance.format(distancia.get(position));

		holder.nomeLoja.setText(nomeLoja.get(position));
		holder.endereco.setText(endereco.get(position));
		holder.distancia.setText("KM: " + distanceFloat + ".");
		if (Logo != null) {
			Picasso.with(context).load(urlFoto.replace("{img}", Logo.get(position))).into(holder.lojas);
		}

		if (onClickLoja != null){
			holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					onClickLoja.onClickLoja(holder.itemView,position);
				}
			});
		}

	}

	@Override
	public int getItemCount() {
		Log.d(TAG, "getItemCount: resposta" + distancia.size());
		return distancia.size();
	}
}
