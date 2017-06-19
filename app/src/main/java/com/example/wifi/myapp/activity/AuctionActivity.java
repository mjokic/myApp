package com.example.wifi.myapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.customAdapters.AuctionDetailsPagerAdapter;
import com.example.wifi.myapp.model.Auction;
import com.example.wifi.myapp.model.Bid;
import com.example.wifi.myapp.model.DTO.BidDTO;
import com.example.wifi.myapp.model.InitObjects;
import com.example.wifi.myapp.model.User;
import com.example.wifi.myapp.service.NotificationService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuctionActivity extends AppCompatActivity {

    private Auction auction;
    private String token;
    private long userId;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Auction");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        auction = (Auction) getIntent().getSerializableExtra("selectedAuction");
        long auctionId = getIntent().getLongExtra("auctionId", 0);
        this.token = getIntent().getStringExtra("token");
        this.userId = getIntent().getLongExtra("userId", 0);
//        loadAuction(auctionId, token);

        System.out.println("token: " + this.token + " AuctionActivity"); // obrisi
        System.out.println("user id: " + this.userId + " AuctionActivity"); // obrisi
        System.out.println("auction id: " + auctionId + " AuctionActivity"); // obrisi

        this.viewPager = (ViewPager) findViewById(R.id.viewPagerThing);
        PagerAdapter pagerAdapter = new AuctionDetailsPagerAdapter(getSupportFragmentManager());
        this.viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(this.viewPager);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_item, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_add){
            this.auction = (Auction) getIntent().getSerializableExtra("auction");
            onActionAddClick();
        }

        return super.onOptionsItemSelected(item);
    }

    private void onActionAddClick(){

        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enter your bid bellow")
                .setTitle("New Bid")
                .setView(editText)
                .setPositiveButton(R.string.bid, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = editText.getText().toString();
                        if(value.isEmpty()) return;
                        double price = Double.parseDouble(value);

                        List<Bid> bids = auction.getBids();
                        double minPrice = 0;

                        if(bids.size() == 0) {
                            minPrice = auction.getStartPrice();
                        }else{
                            minPrice = bids.get(bids.size() - 1).getPrice();
                        }

                        if(price <= minPrice){
                            Toast.makeText(AuctionActivity.this, "Invalid Price!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        createBid(price);

                    }

                })
                .setNeutralButton(R.string.cancel, null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    private void createBid(Double price){
//        long userId = PreferenceManager.getDefaultSharedPreferences(this).getLong("userId", 0);

        BidDTO bidDTO = new BidDTO(price, this.auction.getId(), this.userId);

        Call<Bid> call = InitObjects.bidApiService.createBid(bidDTO, this.token);
        call.enqueue(new Callback<Bid>() {
            @Override
            public void onResponse(Call<Bid> call, Response<Bid> response) {

                if(response.code() == 201) {
                    Bid bid = response.body();
                    auction.getBids().add(bid);

                    reload();

                    Toast.makeText(AuctionActivity.this, "You've made a bid!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(AuctionActivity.this, NotificationService.class);
                    AuctionActivity.this.startService(i);

                }else{
                    String message = "Something unexpected happened!";
                    try {
                        message = response.errorBody().string();
                    }catch (IOException ex){
                        ex.printStackTrace();
                    }
                    Toast.makeText(AuctionActivity.this, message, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Bid> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    // aukcija se ucitava u 'AuctionDetailsFragment' i popuni se
    // static atribut Auction i ovoj klasi
    private void loadAuction(long auctionId, String token){
        InitObjects.showProgressDialog(this);

        Call<Auction> call = InitObjects.auctionApiService.getAuction(auctionId, token);
        call.enqueue(new Callback<Auction>() {
            @Override
            public void onResponse(Call<Auction> call, Response<Auction> response) {
                if(response.code() == 200) {
                    auction = response.body();
                    InitObjects.hideProgressDialog();
                }else{
                    String message = "Something unexpected happened!";
                    try {
                        message = response.errorBody().string();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    Toast.makeText(AuctionActivity.this, message, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Auction> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void reload(){
        this.viewPager.getAdapter().notifyDataSetChanged();
    }

}
