package com.example.wifi.myapp.fragments.My;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.activity.AddAuctionActivity;
import com.example.wifi.myapp.activity.EditAuctionActivity;
import com.example.wifi.myapp.customAdapters.MyAuctionsCustomArrayAdapter;
import com.example.wifi.myapp.model.Auction;
import com.example.wifi.myapp.model.InitObjects;
import com.example.wifi.myapp.model.Item;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyAuctionsFragment extends Fragment {

    private String token;
    private long userId;
    private List<Auction> auctions = new ArrayList<>();
    private ListView listViewMyAuctions;
    private Activity activity;

    public MyAuctionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_auctions, container, false);

        this.activity = getActivity();
        this.token = this.activity.getIntent().getStringExtra("token");
        this.userId = this.activity.getIntent().getLongExtra("userId", 0);
        this.listViewMyAuctions = (ListView) rootView.findViewById(R.id.listViewMyAuctions);

        BottomNavigationView bnv = (BottomNavigationView) rootView.findViewById(R.id.bottom_navigation);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent = new Intent(activity, AddAuctionActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("userId", userId);
                startActivity(intent);

                return true;
            }
        });

        this.listViewMyAuctions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Auction auction = auctions.get(position);

                System.out.println("Selektovana aukcija: " + auction.getId());

                if(!auction.isOver()) {
                    Intent intent = new Intent(getActivity(), EditAuctionActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("userId", userId);
                    intent.putExtra("auctionId", auction.getId());
                    startActivity(intent);
                }else{
                    Toast.makeText(activity, "You can't edit finished auctions!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadMyAuctions(token);

        return rootView;

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser && activity != null){
            loadMyAuctions(this.token);
        }
    }


    private void updateUI(List<Auction> auctions){
        this.auctions = auctions;

        MyAuctionsCustomArrayAdapter adapter = new
                MyAuctionsCustomArrayAdapter(auctions, this.activity, this.token);
        this.listViewMyAuctions.setAdapter(adapter);

        InitObjects.hideProgressDialog();

    }

    private void loadMyAuctions(String token){
        InitObjects.showProgressDialog(this.activity);

        Call<List<Auction>> call = InitObjects.meApiServiceInterface.getMyAuctions(token);
        call.enqueue(new Callback<List<Auction>>() {
            @Override
            public void onResponse(Call<List<Auction>> call, Response<List<Auction>> response) {

                if(response.code() == 200){

                    List<Auction> auctions = response.body();
                    updateUI(auctions);

                }else{
                    System.out.println("Something fucked up!");
                }

            }

            @Override
            public void onFailure(Call<List<Auction>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
