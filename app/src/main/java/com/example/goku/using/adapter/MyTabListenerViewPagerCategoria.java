package com.example.goku.using.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.widget.Button;

/**
 * Created by Goku on 29/04/2017.
 */
public class MyTabListenerViewPagerCategoria implements ActionBar.TabListener {
    private ViewPager viewPager;
    private int idx;

    public MyTabListenerViewPagerCategoria(ViewPager viewPager, int idx) {
        this.viewPager = viewPager;
        this.idx = idx;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        //Navega para a página desejada do viewPager
        viewPager.setCurrentItem(idx);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {}
}

