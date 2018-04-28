package com.example.goku.using.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.example.goku.using.domain.CatSubFragDomain;

import com.example.goku.using.R;
import com.example.goku.using.activity.SubCategoriaActivity;
import com.example.goku.using.domain.WebUsingDomain;
import com.example.goku.using.service.WebUsing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

public class CategoriaFragment1 extends android.support.v4.app.Fragment {

    private CheckBox checkBox;
    private Button btAlimentos;
    private Button btVestuario;
    private Button btBebidas;
    private Button btServicos;



    private static String TAG = "USING";

    private CategoriaInterface categoriaListener;

    private int tipo;

    public static CategoriaFragment1 newInstance(int tipo){
        Bundle args = new Bundle();
        args.putInt("tipo", tipo);
        CategoriaFragment1 f1 = new CategoriaFragment1();
        f1.setArguments(args);
        return f1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            //Lê o tipo de parâmetro
            this.tipo = getArguments().getInt("tipo");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstaceState) {


        View view = inflater.inflate(R.layout.categoria_fragment_1, container, false);



        btAlimentos = (Button) view.findViewById(R.id.categoriaAlimentos);
        btVestuario = (Button) view.findViewById(R.id.categoriaVestuario);
        btBebidas = (Button) view.findViewById(R.id.categoriaBebidas);
        btServicos = (Button) view.findViewById(R.id.categoriaServicos);

        btAlimentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CatSubFragDomain.setNumFrag(2);
                Bundle bundle = new Bundle();
                bundle.putInt("replaceFragment", 1);
                Intent intent = new Intent(getContext(), SubCategoriaActivity.class);
                startActivity(intent.putExtras(bundle));
            }
        });

        btVestuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CatSubFragDomain.setNumFrag(1);
                Bundle bundle = new Bundle();
                bundle.putInt("replaceFragment", 2);
                Intent intent = new Intent(getContext(), SubCategoriaActivity.class);
                startActivity(intent.putExtras(bundle));
            }
        });

        btBebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CatSubFragDomain.setNumFrag(1);
                Bundle bundle = new Bundle();
                bundle.putInt("replaceFragment", 3);
                Intent intent = new Intent(getContext(), SubCategoriaActivity.class);
                startActivity(intent.putExtras(bundle));
            }
        });

        btServicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CatSubFragDomain.setNumFrag(1);
                Bundle bundle = new Bundle();
                bundle.putInt("replaceFragment", 4);
                Intent intent = new Intent(getContext(), SubCategoriaActivity.class);
                startActivity(intent.putExtras(bundle));
            }
        });



       /* checkBox = (CheckBox) view.findViewById(R.id.categoriaAlimentos);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()){
                    Log.i("USING","Checked True");

                    //Aqui eu fiz a checagem me baseando na checkBox de alimentos
                    //Se a checkBox estiver acionada, eu seto o parâmetro visible como true
                    //para em seguida passar o valor para a nossa activity escondendo ou não
                    //o nosso botão
                   categoriaListener.setVisible(true);

                }else{
                    Log.i("USING","Checked False");
                    categoriaListener.setVisible(false);
                }
            }
        });*/

        return view;
    }



    //Criei uma interface com um método no qual é responsável por definir um parâmetro
    //do tipo boolean, com o intuito de checar se a visibilidade do botão é
    //verdadeira ou falsa
    public interface CategoriaInterface{
        public void setVisible(boolean visible);
    }


    //Este método é responsável por inserir os dados em minha variável que é
    //do mesmo tipo que minha interface criada anteriormente
    //isso é necessário para que eu possa guardar os dados na mesma
    //e resgatá-los em minha activity
    public void onAttach(Activity a) {
        super.onAttach(a);
        try {
            categoriaListener = (CategoriaInterface) a;
        }catch (Exception e){

        }
    }
}
