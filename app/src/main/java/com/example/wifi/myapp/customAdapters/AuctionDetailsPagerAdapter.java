package com.example.wifi.myapp.customAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.wifi.myapp.fragments.AuctionDetailsFragment;
import com.example.wifi.myapp.fragments.BidsFragment;


public class AuctionDetailsPagerAdapter extends FragmentPagerAdapter {

    public AuctionDetailsPagerAdapter(FragmentManager m){
        super(m);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AuctionDetailsFragment();
            case 1:
                return new BidsFragment();

            default:
                return null;
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "Details";
            case 1:
                return "Bids";
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
