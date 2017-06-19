package com.example.wifi.myapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.activity.AuctionActivity;
import com.example.wifi.myapp.activity.BaseActivity;
import com.example.wifi.myapp.model.Auction;
import com.example.wifi.myapp.model.InitObjects;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuctionDetailsFragment extends Fragment {

    private long auctionId;
    private String token;
    private View rootView;

    public AuctionDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.rootView = inflater.inflate(R.layout.fragment_auction_details, container, false);

//        this.baseActivity = new BaseActivity(getActivity());

        this.auctionId = getActivity().getIntent().getLongExtra("auctionId", 0);
        this.token = getActivity().getIntent().getStringExtra("token");
        loadAuction(auctionId, token);

//        Auction auction = (Auction) getArguments().getSerializable("auction");

        return this.rootView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser){
            if(this.auctionId != 0)
                loadAuction(this.auctionId, this.token);
        }
    }


    private void loadAuction(long auctionId, String token){
        InitObjects.showProgressDialog(getActivity());

        Call<Auction> call = InitObjects.auctionApiService.getAuction(auctionId, token);
        call.enqueue(new Callback<Auction>() {
            @Override
            public void onResponse(Call<Auction> call, Response<Auction> response) {
                Auction auction = response.body();
                updateUI(auction);
            }

            @Override
            public void onFailure(Call<Auction> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void updateUI(Auction auction){

        // dodaje se aukcija u 'AuctionActivity'
        // da bi bila vidljiva prilikom dodavanja novog bid-a
//        AuctionActivity.auction = auction;
        getActivity().getIntent().putExtra("auction", auction);

        final ImageView imageViewItemImage = (ImageView) rootView.findViewById(R.id.imageViewItemImage);

        TextView textViewItem = (TextView) rootView.findViewById(R.id.textViewAuctionItem);
        textViewItem.setText(auction.getItem().getName());

        TextView textViewItemDescription = (TextView) rootView.findViewById(R.id.auctionItemDescription);
        textViewItemDescription.setText(auction.getItem().getDescription());

        TextView textViewStartPrice = (TextView) rootView.findViewById(R.id.textViewAuctionStartPrice);
        textViewStartPrice.setText(String.valueOf(auction.getStartPrice()));

        TextView textViewStartDate = (TextView) rootView.findViewById(R.id.textViewAuctionStartDate);
        textViewStartDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(auction.getStartDate()));

        TextView textViewEndDate = (TextView) rootView.findViewById(R.id.textViewAuctionEndDate);
        textViewEndDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(auction.getEndDate()));

        TextView textViewEmail = (TextView) rootView.findViewById(R.id.textViewAuctionUserEmail);
        textViewEmail.setText(auction.getUser().getEmail());

        TextView textViewBids = (TextView) rootView.findViewById(R.id.textViewAuctionBids);
        textViewBids.setText(String.valueOf(auction.getBids().size()));

        Picasso.with(getActivity())
                .load(InitObjects.ITEMS_URL + auction.getItem().getPicture())
                .resize(150, 150)
                .centerCrop()
                .into(imageViewItemImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        imageViewItemImage.setImageResource(R.drawable.img_not_found);
                    }
                });

        InitObjects.hideProgressDialog();
    }

}
