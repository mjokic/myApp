package com.example.wifi.myapp.customAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.wifi.myapp.fragments.ItemsFragment;
import com.example.wifi.myapp.fragments.MeFragment;
import com.example.wifi.myapp.fragments.My.MyAuctionsFragment;
import com.example.wifi.myapp.fragments.My.MyInfoFragment;


public class MePagerAdapter extends FragmentStatePagerAdapter {

    public MePagerAdapter(FragmentManager m){
        super(m);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MyAuctionsFragment();
            case 1:
                return new ItemsFragment();
            case 2:
                return new MyInfoFragment();
            default:
                return null;
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "My Auctions";
            case 1:
                return "My Items";
            case 2:
                return "My Info";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
