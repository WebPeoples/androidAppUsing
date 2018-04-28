package com.example.goku.using.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.example.goku.using.activity.MapsActivity;
import com.example.goku.using.activity.PesquisaSemMapActivity;
import com.example.goku.using.domain.CatSubFragDomain;

import com.example.goku.using.R;
import com.example.goku.using.domain.UsingDB;
import com.example.goku.using.domain.WebUsingDomain;
import com.example.goku.using.domain.WebUsingMapsDomain;
import com.example.goku.using.prefs.PreferencesManager;
import com.example.goku.using.service.WebUsing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SubCatFrag1 extends Fragment{

    private int fabTab;
    private static final String TAG = "LOG";
    private String URL = "http://192.168.1.106:8080/WebUsing/ws/generic/";
    private String URL_getValuesToMap = "http://192.168.1.106:8080/WebUsing/ws/usingws/";
    private String URL_getEspecialidades = "http://192.168.1.106:8080/WebUsing/ws/usingws/especialidades/";
    private UsingDB db;

    private List<String> cd_especialidade;
    private List<String> nm_esp;
    private List<String> logo;
    private double[] lat;
    private double[] longi;
    private List<String> nr_cpf_cnpj;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_cat_frag1, container, false);
        db = new UsingDB(getContext());

        cd_especialidade = new ArrayList<>();
        nm_esp = new ArrayList<>();
        logo = new ArrayList<>();
        nr_cpf_cnpj = new ArrayList<>();

        Button bt1 = (Button) view.findViewById(R.id.button1);
        Button bt2 = (Button) view.findViewById(R.id.button2);
        Button bt3 = (Button) view.findViewById(R.id.button3);
        Button bt4 = (Button) view.findViewById(R.id.button4);

        fabTab = CatSubFragDomain.getFabTab();

        if (fabTab == 1) {
            bt1.setBackgroundResource(R.drawable.subcategoria_alime_mercados);
            bt2.setBackgroundResource(R.drawable.subcategoria_alime_restau);
            bt3.setBackgroundResource(R.drawable.subcategoria_alime_pizza);
            bt4.setBackgroundResource(R.drawable.subcategoria_alime_fastfood);

            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Bundle bundle = new Bundle();
                    bundle.putString("subcat", "mercad");

                    if (PreferencesManager.getBoolean(getContext(), "isNoMap")){
                        Intent intent = new Intent(getContext(), PesquisaSemMapActivity.class);
                        startActivity(intent.putExtras(bundle));
                    }else {
                    Intent intent = new Intent(getContext(), MapsActivity.class);
                        startActivity(intent.putExtras(bundle));
                    }

                }
            });

            bt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putString("subcat", "restau");
                    if (PreferencesManager.getBoolean(getContext(), "isNoMap")){
                        Intent intent = new Intent(getContext(), PesquisaSemMapActivity.class);
                        startActivity(intent.putExtras(bundle));
                    }else {
                        Intent intent = new Intent(getContext(), MapsActivity.class);
                        startActivity(intent.putExtras(bundle));
                    }
                }
            });
        }else if(fabTab == 2) {

            bt1.setBackgroundResource(R.drawable.subcategoria_vestu_roupas);

            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("subcat", "roupa");
                    if (PreferencesManager.getBoolean(getContext(), "isNoMap")){
                        Intent intent = new Intent(getContext(), PesquisaSemMapActivity.class);
                        startActivity(intent.putExtras(bundle));
                    }else {
                        Intent intent = new Intent(getContext(), MapsActivity.class);
                        startActivity(intent.putExtras(bundle));
                    }
                }
            });

            bt2.setBackgroundResource(R.drawable.subcategoria_vestu_calc);
            bt3.setBackgroundResource(R.drawable.subcategoria_vestu_biju);
            bt4.setBackgroundResource(R.drawable.subcategoria_vestu_comestico);


        }else if (fabTab == 3){
            bt1.setBackgroundResource(R.drawable.subcategoria_bebidas_nacionais);
            bt2.setBackgroundResource(R.drawable.subcategoria_bebi_importadas);

            bt3.setVisibility(View.INVISIBLE);
            bt4.setVisibility(View.INVISIBLE);

			DisplayMetrics metrics = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

			int height = getDPI(250, metrics);

			TableRow.LayoutParams l = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);
			TableRow.LayoutParams l2 = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);
			l.weight = 1;
			l2.weight = 1;
			l2.leftMargin = 4;
			bt1.setLayoutParams(l);
			bt2.setLayoutParams(l2);


            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

            bt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("subcat", "IMPORT");
                    Intent intent = new Intent(getContext(), MapsActivity.class);
                    startActivity(intent.putExtras(bundle));
                }
            });

        } else if (fabTab == 4){

            bt1.setBackgroundResource(R.drawable.subcategorai_ser_cabele);
            bt2.setBackgroundResource(R.drawable.subcategoria_ser_academia);
            bt3.setBackgroundResource(R.drawable.subcategoria_ser_petshop);
            bt4.setBackgroundResource(R.drawable.subcategoria_ser_dentista);

        }else if(fabTab == 5) {

            bt1.setBackgroundResource(R.drawable.subcat_dentista);
            bt2.setBackgroundResource(R.drawable.subcat_farmacias);
            bt3.setBackgroundResource(R.drawable.subcat_hospital);
            bt4.setBackgroundResource(R.drawable.subcat_suplementos);

        }else if (fabTab == 6){

            bt1.setBackgroundResource(R.drawable.subcat_utilidades);
            bt2.setBackgroundResource(R.drawable.subcat_instrumentos);
            bt3.setBackgroundResource(R.drawable.subcat_contrucao);
            bt4.setBackgroundResource(R.drawable.subcat_autopeca);

        }else if (fabTab == 7){

            bt1.setBackgroundResource(R.drawable.subcat_construcao);
            bt2.setBackgroundResource(R.drawable.subcat_automotiva);
            bt3.setBackgroundResource(R.drawable.subcat_eletrodomestico);
            bt4.setBackgroundResource(R.drawable.subcat_informatica);

        }else if(fabTab == 8){

            bt1.setBackgroundResource(R.drawable.subcat_computadores);
            bt2.setBackgroundResource(R.drawable.subcat_games);
            bt3.setBackgroundResource(R.drawable.subcat_eletrodomesticos);
            bt4.setBackgroundResource(R.drawable.subcat_smartphones);
        }

        return view;
    }

	public static int getDPI(int size, DisplayMetrics metrics){
		return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
	}



}
