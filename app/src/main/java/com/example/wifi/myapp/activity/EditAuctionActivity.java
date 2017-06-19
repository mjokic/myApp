package com.example.wifi.myapp.activity;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.fragments.customDialogs.DatePickerFragment;
import com.example.wifi.myapp.fragments.customDialogs.TimePickerFragment;
import com.example.wifi.myapp.model.Auction;
import com.example.wifi.myapp.model.DTO.AuctionDTO;
import com.example.wifi.myapp.model.DTO.ItemDTO;
import com.example.wifi.myapp.model.InitObjects;
import com.example.wifi.myapp.model.Item;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAuctionActivity extends AppCompatActivity {

    private String token;
    private Long userId;
    private Long auctionId;


    private ImageView imageViewItemImage;
    private TextView textViewItemName;
    private TextView textViewItemDesc;

    private EditText editTextStartPrice;
    private EditText editTextStartDate;
    private EditText editTextEndDate;

    private Auction auction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_auction);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Auction");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        this.token = getIntent().getStringExtra("token");
        this.userId = getIntent().getLongExtra("userId", 0);
        this.auctionId = getIntent().getLongExtra("auctionId", 0);
        System.out.println("Akcual auctionId " + this.auctionId);

        this.imageViewItemImage = (ImageView) findViewById(R.id.imageViewItemImage);
        this.textViewItemName = (TextView) findViewById(R.id.textViewItemName);
        this.textViewItemDesc = (TextView) findViewById(R.id.textViewItemDesc);

        this.editTextStartPrice = (EditText) findViewById(R.id.editTextStartPrice);
        this.editTextStartDate = (EditText) findViewById(R.id.editTextStartDate);
        this.editTextEndDate = (EditText) findViewById(R.id.editTextEndDate);
        Button button = (Button) findViewById(R.id.buttonEditAuction);

        editTextStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    editTextStartDate.setRawInputType(InputType.TYPE_NULL);
                    editTextStartDate.setTextIsSelectable(true);
                    showTimePickerDialog(1);
                    showDatePickerDialog(1);
                }
            }
        });

        editTextEndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    editTextEndDate.setRawInputType(InputType.TYPE_NULL);
                    editTextEndDate.setTextIsSelectable(true);
                    showTimePickerDialog(0);
                    showDatePickerDialog(0);
                }
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextStartPrice.getText().toString().isEmpty() ||
                        editTextStartDate.getText().toString().isEmpty() ||
                        editTextEndDate.getText().toString().isEmpty()){
                    Toast.makeText(EditAuctionActivity.this, "Fields can't be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                double price = Double.parseDouble(editTextStartPrice.getText().toString());
                String startDateStr = editTextStartDate.getText().toString();
                String endDateStr = editTextEndDate.getText().toString();

                try {
                    auction.setStartPrice(price);
                    auction.setStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDateStr));
                    auction.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDateStr));

                    editAuction(auction, userId, token);

                }catch (ParseException ex){
                    ex.printStackTrace();
                    Toast.makeText(EditAuctionActivity.this, "Invalid date/time format!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadAuction(auctionId, token);

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }




    private void updateUI(Auction auction){
        this.auction = auction;

        Picasso.with(getApplicationContext())
                .load(InitObjects.ITEMS_URL + auction.getItem().getPicture())
                .resize(100, 100)
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

        this.textViewItemName.setText(auction.getItem().getName());
        this.textViewItemDesc.setText(auction.getItem().getDescription());

        this.editTextStartPrice.setText(String.valueOf(auction.getStartPrice()));
        this.editTextStartDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(auction.getStartDate()));
        this.editTextEndDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(auction.getEndDate()));

        InitObjects.hideProgressDialog();
    }

    private void loadAuction(long auctionId, String token){
        InitObjects.showProgressDialog(this);

        Call<Auction> call = InitObjects.auctionApiService.getAuction(auctionId, token);
        call.enqueue(new Callback<Auction>() {
            @Override
            public void onResponse(Call<Auction> call, Response<Auction> response) {
                if(response.code() == 200){
                    Auction auction = response.body();
                    updateUI(auction);
                }
            }

            @Override
            public void onFailure(Call<Auction> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void editAuction(final Auction auction, long userId, String token){
        InitObjects.showProgressDialog(this);

        AuctionDTO auctionDTO = new AuctionDTO(
                auction.getStartPrice(),
                auction.getStartDate(),
                auction.getEndDate(),
                auction.getUser().getId(),
                auction.getItem().getId(),
                auction.isOver()
        );

        Call<Auction> call = InitObjects.auctionApiService.editAuction(auctionDTO, auction.getId(), token);
        call.enqueue(new Callback<Auction>() {
            @Override
            public void onResponse(Call<Auction> call, Response<Auction> response) {

                if(response.code() == 200){
                    Auction auction1 = response.body();
                    System.out.println("Line 210: " + auction1.getId());
                    updateUI(auction1);
                }else{
                    try {
                        String message = response.errorBody().string();
                        Toast.makeText(EditAuctionActivity.this, message, Toast.LENGTH_SHORT).show();
                    }catch (Exception ex){
                        ex.printStackTrace();
                        Toast.makeText(EditAuctionActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                    InitObjects.hideProgressDialog();
                }
                onBackPressed();

            }

            @Override
            public void onFailure(Call<Auction> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }



    public void showTimePickerDialog(int option) {
        DialogFragment newFragment = new TimePickerFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("option", option);
        newFragment.setArguments(bundle);

        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(int option) {
        DialogFragment newFragment = new DatePickerFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("option", option);
        newFragment.setArguments(bundle);

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

}
