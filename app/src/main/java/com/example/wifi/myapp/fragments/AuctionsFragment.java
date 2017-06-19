package com.example.wifi.myapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.activity.AuctionActivity;
import com.example.wifi.myapp.customAdapters.AuctionsCustomArrayAdapter;
import com.example.wifi.myapp.model.Auction;
import com.example.wifi.myapp.model.Bid;
import com.example.wifi.myapp.model.InitObjects;
import com.example.wifi.myapp.model.User;
import com.example.wifi.myapp.service.apiCalls.AuctionApiServiceInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AuctionsFragment extends Fragment {

    private List<Auction> auctions = new ArrayList<>();
    private ListView listView;
    private long userId;
    private String token;

    public AuctionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_auctions, container, false);

        this.token = getActivity().getIntent().getStringExtra("token");
        this.userId = getActivity().getIntent().getLongExtra("userId", 0);

        System.out.println("token: " + this.token + " AuctionsFragment"); // obrisi
        System.out.println("user id: " + this.userId + " AuctionsFragment"); // obrisi

        loadAuctions(token);

        listView = (ListView) rootView.findViewById(R.id.auctionsList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Auction selectedAuction = auctions.get(position);
                openAuctionActivity(selectedAuction, token, userId);
            }
        });


        return rootView;
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if(isVisibleToUser){
//            loadAuctions(this.token);
//        }
//    }

    private void openAuctionActivity(Auction selectedAuction, String token, Long userId){
        Intent intent = new Intent(getActivity(), AuctionActivity.class);
        intent.putExtra("auctionId", selectedAuction.getId());
        intent.putExtra("token", token);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void updateUI(List<Auction> auctions){
        this.auctions = auctions;

        if(auctions != null) {
            AuctionsCustomArrayAdapter adapter =
                    new AuctionsCustomArrayAdapter(auctions, getActivity().getApplicationContext());
            listView.setAdapter(adapter);
        }

    }

    private void loadAuctions(String token){
        Call<List<Auction>> call = InitObjects.auctionApiService.getAllAuctions(token);

        call.enqueue(new Callback<List<Auction>>() {
            @Override
            public void onResponse(Call<List<Auction>> call, Response<List<Auction>> response) {
                List<Auction> auctions = response.body();

                updateUI(auctions);
            }

            @Override
            public void onFailure(Call<List<Auction>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

}
