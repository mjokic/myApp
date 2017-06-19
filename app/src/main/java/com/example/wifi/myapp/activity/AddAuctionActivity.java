package com.example.wifi.myapp.activity;

import android.support.constraint.solver.SolverVariable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.customAdapters.ItemsCustomArrayAdapter;
import com.example.wifi.myapp.customAdapters.ItemsCustomSpinnerArrayAdapter;
import com.example.wifi.myapp.fragments.customDialogs.DatePickerFragment;
import com.example.wifi.myapp.fragments.customDialogs.TimePickerFragment;
import com.example.wifi.myapp.model.Auction;
import com.example.wifi.myapp.model.DTO.AuctionDTO;
import com.example.wifi.myapp.model.InitObjects;
import com.example.wifi.myapp.model.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAuctionActivity extends AppCompatActivity {

    private String token;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_auction);


        this.token = getIntent().getStringExtra("token");
        this.userId = getIntent().getLongExtra("userId", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("New Auction");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        final EditText editTextStartDate = (EditText) findViewById(R.id.editTextStartDate);
        final EditText editTextEndDate = (EditText) findViewById(R.id.editTextEndDate);

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


        loadMyItems(this.token);

        Button buttonAddAuction = (Button) findViewById(R.id.buttonAddAuction);
        buttonAddAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editTextStartPrice = (EditText) findViewById(R.id.editTextStartPrice);
                EditText editTextStartDate = (EditText) findViewById(R.id.editTextStartDate);
                EditText editTextEndDate = (EditText) findViewById(R.id.editTextEndDate);

                String startPriceStr = editTextStartPrice.getText().toString();
                String startDateStr = editTextStartDate.getText().toString();
                String endDateStr = editTextEndDate.getText().toString();

                if(startPriceStr.isEmpty() || startDateStr.isEmpty() || endDateStr.isEmpty()) {
                    Toast.makeText(AddAuctionActivity.this, "Fileds can't be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {


                    double startPrice = Double.parseDouble(startPriceStr);
                    Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDateStr);
                    Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDateStr);

                    Spinner spinner = (Spinner) findViewById(R.id.spinnerMyItems);
                    Item item = (Item) spinner.getSelectedItem();
                    if(item == null){
                        Toast.makeText(AddAuctionActivity.this, "You didn't chose item!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    AuctionDTO auctionDTO = new AuctionDTO(startPrice, startDate, endDate, userId, item.getId(), false);

                    createAuction(auctionDTO, token);

                }catch (ParseException ex){
                    Toast.makeText(AddAuctionActivity.this, "Wrong date/time format in start/end date", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

    private void updateUI(List<Item> items){
        List<Item> itemsToShow = new ArrayList<>();
        for(Item i : items){
            if(!i.isOnAuction()){
                itemsToShow.add(i);
            }
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinnerMyItems);
        ItemsCustomSpinnerArrayAdapter adapter =
                new ItemsCustomSpinnerArrayAdapter(itemsToShow, getApplicationContext(), R.id.textViewItemName);
        spinner.setAdapter(adapter);

        InitObjects.hideProgressDialog();
    }

    private void loadMyItems(String token){
        InitObjects.showProgressDialog(this);

        Call<List<Item>> call = InitObjects.itemApiService.getAllItems(token);

        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                // !!! maybe use ItemsDTO under !!!
                List<Item> items = response.body();

                updateUI(items);

            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void createAuction(AuctionDTO auctionDTO, String token){

        Call<Auction> call = InitObjects.auctionApiService.createAuction(auctionDTO, token);
        call.enqueue(new Callback<Auction>() {
            @Override
            public void onResponse(Call<Auction> call, Response<Auction> response) {
                if(response.code() == 201){
                    Toast.makeText(AddAuctionActivity.this, "Auction successfully created!", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }else{
                    try {
                        String message = response.errorBody().string();
                        Toast.makeText(AddAuctionActivity.this, message, Toast.LENGTH_SHORT).show();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Auction> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

}
