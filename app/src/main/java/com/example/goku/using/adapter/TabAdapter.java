package com.example.goku.using.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.goku.using.R;
import com.example.goku.using.fragment.CategoriaFragment1;
import com.example.goku.using.fragment.CategoriaFragment2;
import com.example.goku.using.activity.SubCategoriaActivity;

/**
 * Created by Goku on 30/04/2017.
 */
public class TabAdapter extends SmartFragmentStatePagerAdapter {
    private Context context;
    private int fabTab;

    public TabAdapter(Context context, FragmentManager supportFragmentManager, int fabTab) {
        super(supportFragmentManager);
        this.context = context;
        this.fabTab = fabTab;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        if (position == 0){
            f = new CategoriaFragment1();

        }else  if (position == 1){
            f = new CategoriaFragment2();

        }
        return f;
    }


    @Override
    public CharSequence getPageTitle(int position){

        switch (position){
            case 0:
                return "";
            case 1:
                return "";
            default:  return "";
        }
    }

    @Override
    public int getCount() {
        //ViewPager vai ter 2 páginas
        return 2;
    }
}
