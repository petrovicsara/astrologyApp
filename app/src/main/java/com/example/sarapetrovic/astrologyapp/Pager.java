package com.example.sarapetrovic.astrologyapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by sarapetrovic on 7/7/19.
 */

public class Pager extends FragmentStatePagerAdapter {

    int tabCount;

    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ProfileFragment pf = new ProfileFragment();
                return  pf;
            case 1:
                SwipeFragment sf = new SwipeFragment();
                return sf;
            case 2:
                ChatFragment cf = new ChatFragment();
                return cf;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
