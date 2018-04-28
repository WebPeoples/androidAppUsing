package com.example.goku.using.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.goku.using.fragment.SubCatFrag1;
import com.example.goku.using.fragment.SubCatFrag2;
import com.example.goku.using.domain.CatSubFragDomain;

/**
 * Created by Goku on 20/06/2017.
 */

public class SubCatTabAdapter extends SmartFragmentStatePagerAdapter {
    private Context context;
    private int fabTab;
    public SubCatTabAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
       // this.fabTab = fabTab;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;

        if (position == 0){
            f = new SubCatFrag1();
        }else if(position == 1){
            f = new SubCatFrag2();
        }

        return f;
    }

    @Override
    public int getCount() {
        return CatSubFragDomain.getNumFrag();
    }
}
