package com.example.wifi.myapp.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.activity.BaseActivity;
import com.example.wifi.myapp.customAdapters.AuctionDetailsPagerAdapter;
import com.example.wifi.myapp.customAdapters.MePagerAdapter;

/**
 * In this fragment show:
 *      - My Auctions
 *      - My Items
 *      - My Info
 */
public class MeFragment extends Fragment {

    private static Activity activity;

    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_me, container, false);

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new MePagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        activity = getActivity();

        return rootView;
    }

}
