package com.example.goku.using.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.goku.using.R;
import com.example.goku.using.domain.CatSubFragDomain;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubCatFrag2 extends Fragment {


    public SubCatFrag2() {
        // Required empty public constructor
    }

    private int fabTab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sub_cat_frag2, container, false);

        fabTab = CatSubFragDomain.getFabTab();
        Button bt1 = (Button) view.findViewById(R.id.button5);
        Button bt2 = (Button) view.findViewById(R.id.button6);
        Button bt3= (Button) view.findViewById(R.id.button7);
        Button bt4 = (Button) view.findViewById(R.id.button8);

        if(fabTab == 1){


            bt1.setBackgroundResource(R.drawable.subcategoria_alime_delivery);
            bt2.setBackgroundResource(R.drawable.subcategoria_alime_self);
            bt3.setBackgroundResource(R.drawable.subcategoria_alime_panifica);
            bt4.setBackgroundResource(R.drawable.subcategoria_alime_hortfrut);
        }

        return view;
    }

}
