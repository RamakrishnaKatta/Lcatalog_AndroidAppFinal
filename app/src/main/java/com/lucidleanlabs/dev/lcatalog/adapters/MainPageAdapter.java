package com.lucidleanlabs.dev.lcatalog.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lucidleanlabs.dev.lcatalog.IntroductionActivity;
import com.lucidleanlabs.dev.lcatalog.OverviewActivity;

public class MainPageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public MainPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                IntroductionActivity tab1 = new IntroductionActivity();
                return tab1;
            case 1:
                OverviewActivity tab2 = new OverviewActivity();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}