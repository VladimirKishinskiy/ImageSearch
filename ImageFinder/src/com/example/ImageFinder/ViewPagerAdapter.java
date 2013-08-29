package com.example.ImageFinder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    // Declare the number of ViewPager pages
    final int PAGE_COUNT = 2;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int arg0) {
        switch (arg0) {

            case 0:
                Fragment_Search fragmenttab1 = new Fragment_Search();
                return fragmenttab1;

            case 1:
                Fragment_Favorites fragmenttab2 = new Fragment_Favorites();

                return fragmenttab2;
        }
        return null;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return PAGE_COUNT;
    }

}
