package com.example.wifi.myapp.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.activity.AuctionActivity;
import com.example.wifi.myapp.activity.BaseActivity;
import com.example.wifi.myapp.customAdapters.BidsCustomArrayAdapter;
import com.example.wifi.myapp.model.Auction;
import com.example.wifi.myapp.model.Bid;
import com.example.wifi.myapp.model.DTO.BidDTO;
import com.example.wifi.myapp.model.InitObjects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BidsFragment extends android.support.v4.app.Fragment {

    private Auction auction;
    private String token;
    private ListView listView;

    public BidsFragment(){}


    // make it load bids from ID
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bids, container, false);

//        this.baseActivity = new BaseActivity(getActivity());

//        Auction auction = (Auction) getActivity().getIntent().getSerializableExtra("selectedAuction");

        token = getActivity().getIntent().getStringExtra("token");

//        auction = (Auction) getActivity().getIntent().getSerializableExtra("auction");
//        auctionArrayAdapter = new ArrayAdapter<>(getActivity(),
//                android.R.layout.simple_list_item_1, auction.bids);

        if(auction != null) {

            loadBids(auction.getId(), token);
        }

        this.listView = (ListView) rootView.findViewById(R.id.bids_list);
        //            listView.setAdapter(auctionArrayAdapter);




        return rootView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser){
            auction = (Auction) getActivity().getIntent().getSerializableExtra("auction");

            if(auction != null) {

                loadBids(auction.getId(), token);
            }
        }
    }


    private void updateUI(List<Bid> bids){
        this.auction.setBids(bids);

        // reversing only for showing
        List<Bid> reversed = new ArrayList<>();
        reversed.addAll(this.auction.getBids());
        Collections.reverse(reversed);

//        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getActivity(),
//                android.R.layout.simple_list_item_1, reversed);

        BidsCustomArrayAdapter adapter = new BidsCustomArrayAdapter(reversed, getActivity().getApplicationContext());
        this.listView.setAdapter(adapter);
//        this.listView.setAdapter(arrayAdapter);


        InitObjects.hideProgressDialog();
    }

    private void loadBids(long auctionId, String token){
        InitObjects.showProgressDialog(getActivity());

        Call<List<Bid>> bids = InitObjects.auctionApiService.getBids(auctionId, token);
        bids.enqueue(new Callback<List<Bid>>() {
            @Override
            public void onResponse(Call<List<Bid>> call, Response<List<Bid>> response) {
                if(response.code() == 200){
                    List<Bid> bids = response.body();
                    updateUI(bids);

                }else{
                    String message = "Something unexpected happened!";
                    try {
                        message = response.errorBody().string();
                    }catch (IOException ex){
                        ex.printStackTrace();
                    }
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<Bid>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

}
