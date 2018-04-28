package com.example.goku.using.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.goku.using.R;
import com.example.goku.using.adapter.SubCatTabAdapter;
import com.example.goku.using.domain.CatSubFragDomain;

/**
 * Created by Goku on 20/06/2017.
 */

public class SubCategoriaTabFrag extends Fragment {

    private int fabTab;
    private ViewPager viewPager;

    public SubCategoriaTabFrag() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_subcategoria_tab, container, false);

        //ViewPager
         viewPager = (ViewPager) view.findViewById(R.id.viewPager_subcategoria);
        final ImageView seta = (ImageView) view.findViewById(R.id.imgProxSubCat);

        //Views que terão de ser mudadas conformes os botões selecionados
        LinearLayout frameLayout = (LinearLayout) view.findViewById(R.id.framelayout_subcategoria);
        TextView titleSubCat = (TextView) view.findViewById(R.id.title_subcategoria);
        ImageView imgTitle = (ImageView) view.findViewById(R.id.imgTitle_subcategoria);
        ImageView imgBottom = (ImageView) view.findViewById(R.id.subcat_bottom_icon);

        //Estilizando o conteúdo da tela que comportará as fragments baseando-se no que foi clicado em CATEGORIA
        fabTab = CatSubFragDomain.getFabTab();
        if (fabTab == 1){
            titleSubCat.setText(R.string.alimentos);
            imgTitle.setImageResource(R.drawable.subcat_alime_icon);
            int corBackTitle = ContextCompat.getColor(getContext(), R.color.alimentos_verde);
            frameLayout.setBackgroundColor(corBackTitle);
            imgBottom.setImageResource(R.drawable.subcat_alime_bicon);


        }else if(fabTab == 3){

            titleSubCat.setText(R.string.bebidas);
            imgTitle.setImageResource(R.drawable.subcat_bebida_icon);
            int corBackTitle = ContextCompat.getColor(getContext(), R.color.bebidas_vinho);
            frameLayout.setBackgroundColor(corBackTitle);

            imgBottom.setImageResource(R.drawable.subcat_bebida_bicon);

            seta.setVisibility(View.INVISIBLE);
        }else if(fabTab == 2){

            titleSubCat.setText(R.string.vestuario);
            imgTitle.setImageResource(R.drawable.subcat_roupa_icon);
            int corBackTitle = ContextCompat.getColor(getContext(), R.color.vestuario_azul);
            frameLayout.setBackgroundColor(corBackTitle);
            imgBottom.setImageResource(R.drawable.subcat_roupa_bicon);

            seta.setVisibility(View.INVISIBLE);

        }else if(fabTab == 4){
            titleSubCat.setText(R.string.servicos);
            imgTitle.setImageResource(R.drawable.subcategoria_serv_icontitle);
            int corBackTitle = ContextCompat.getColor(getContext(), R.color.servicos_azul);
            frameLayout.setBackgroundColor(corBackTitle);
            imgBottom.setImageResource(R.drawable.subcat_servico_bicon);

            seta.setVisibility(View.INVISIBLE);

        }else if(fabTab == 5){
            titleSubCat.setText(R.string.saude);
            imgTitle.setImageResource(R.drawable.subcat_saude_icon);
            int corBackTitle = ContextCompat.getColor(getContext(), R.color.saude_vermelho);
            frameLayout.setBackgroundColor(corBackTitle);
            imgBottom.setImageResource(R.drawable.subcat_saude_bicon);
            seta.setVisibility(View.INVISIBLE);


        }else if(fabTab == 6){

            titleSubCat.setText(R.string.varejo);
            imgTitle.setImageResource(R.drawable.subcategoria_var_icontitle);
            int corBackTitle = ContextCompat.getColor(getContext(), R.color.varejo_azul);
            frameLayout.setBackgroundColor(corBackTitle);
            imgBottom.setImageResource(R.drawable.subcat_varejo_bicon);
            seta.setVisibility(View.INVISIBLE);


        }else if(fabTab == 7){
            titleSubCat.setText(R.string.manutencao);
            imgTitle.setImageResource(R.drawable.subcat_manutencao_icon);
            int corBackTitle = ContextCompat.getColor(getContext(), R.color.manutencao_verde);
            frameLayout.setBackgroundColor(corBackTitle);
            imgBottom.setImageResource(R.drawable.subcat_manutencao_bicon);
            seta.setVisibility(View.INVISIBLE);


        }else if(fabTab == 8){
            titleSubCat.setText(R.string.eletronicos);
            imgTitle.setImageResource(R.drawable.subcategoria_laz_icontitle);
            int corBackTitle = ContextCompat.getColor(getContext(), R.color.eletronicos_verde);
            frameLayout.setBackgroundColor(corBackTitle);
            imgBottom.setImageResource(R.drawable.subcat_eletronicos_bicon);
            seta.setVisibility(View.INVISIBLE);


        }

        //Configurando a view pager com as fragments
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(new SubCatTabAdapter(getChildFragmentManager(), getContext()));



        seta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == 0){
                    viewPager.setCurrentItem(1);
                }else {
                    viewPager.setCurrentItem(0);
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (viewPager.getCurrentItem() == 1) {
                    DisplayMetrics metrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(getDPI(50, metrics), getDPI(50, metrics));
                    layoutParams.gravity = Gravity.START;
                    seta.setLayoutParams(layoutParams);
                    seta.setImageResource(R.drawable.categoria_botao_prox_inver);
                }else if (viewPager.getCurrentItem() == 0){

                    DisplayMetrics metrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(getDPI(50, metrics), getDPI(50, metrics));
                    layoutParams.gravity = Gravity.END;
                    seta.setLayoutParams(layoutParams);
                    seta.setImageResource(R.drawable.categoria_botao_proximodois);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Instanciando e configurando as tablayout
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tablayout_subcategoria);
        //TabLayout tabLayoutBottom = (TabLayout) view.findViewById(R.id.tablayoutBottom_subcategoria);


        tabLayout.setupWithViewPager(viewPager);
        //tabLayoutBottom.setupWithViewPager(viewPager);

        int cor = ContextCompat.getColor(getContext(),R.color.saude_vermelho);
       // tabLayoutBottom.setSelectedTabIndicatorColor(cor);

        tabLayout.setVisibility(View.INVISIBLE);
        return view;
    }

    public static int getDPI(int size, DisplayMetrics metrics){
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }

}
