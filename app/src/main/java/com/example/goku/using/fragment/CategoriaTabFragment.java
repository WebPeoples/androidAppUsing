package com.example.goku.using.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.goku.using.R;
import com.example.goku.using.activity.BaseActivity;
import com.example.goku.using.activity.SubCategoriaActivity;
import com.example.goku.using.adapter.TabAdapter;
import com.example.goku.using.domain.CatSubFragDomain;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriaTabFragment extends Fragment {


    public CategoriaTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_categoria_tab, container, false);

        //View Pager
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        //Método para que o viewpager mantenha viva sempre as tabs passada por parâmetro
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(new TabAdapter(getContext(), getChildFragmentManager(),new CatSubFragDomain().getFabTab()));


        //Tabs
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        //TabLayout tabLayoutBottom = (TabLayout) view.findViewById(R.id.tablayoutBottom);
        int cor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        tabLayout.setTabTextColors(cor,cor);
       // tabLayoutBottom.setTabTextColors(cor, cor);
       // tabLayoutBottom.setClickable(true);
        final ImageView seta = (ImageView) view.findViewById(R.id.seta_cat_frag1);
        seta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == 0) {
                    viewPager.setCurrentItem(1);
                }else if (viewPager.getCurrentItem() == 1){
                    viewPager.setCurrentItem(0);
                }

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (viewPager.getCurrentItem() == 1) {
                    seta.setImageResource(R.drawable.frag_seta_prox_invert);
                }else if (viewPager.getCurrentItem() == 0){
                    seta.setImageResource(R.drawable.frag_seta_prox);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Cria as tabs com o mesmo adapter utilizado pelo ViewPager
        tabLayout.setupWithViewPager(viewPager);
       // tabLayoutBottom.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.INVISIBLE);

        return  view;
    }


}
