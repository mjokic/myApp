package com.example.wifi.myapp.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.activity.AddItemActivity;
import com.example.wifi.myapp.activity.EditItemActivity;
import com.example.wifi.myapp.customAdapters.ItemsCustomArrayAdapter;
import com.example.wifi.myapp.model.InitObjects;
import com.example.wifi.myapp.model.Item;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemsFragment extends Fragment {

    private Activity activity;
    private List<Item> items = new ArrayList<>();
    private ListView listView;
    private String token;
    private long userId;

    public ItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_items, container, false);

        this.activity = getActivity();
        this.token = this.activity.getIntent().getStringExtra("token");
        this.userId = this.activity.getIntent().getLongExtra("userId", 0);
        System.out.println("USER ID: " + this.userId + " ItemsFragment!");
        loadItems(token);


        BottomNavigationView bnv = (BottomNavigationView) rootView.findViewById(R.id.bottom_navigation);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = new Intent(activity, AddItemActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("userId", userId);
                startActivity(intent);
                return true;
            }
        });

        listView = (ListView) rootView.findViewById(R.id.itemList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = items.get(position);
                if(item.isSold()){
                    Toast.makeText(activity, "Can't edit sold item!", Toast.LENGTH_SHORT).show();
                }else {
                    openItemActivity(item);
                }
            }
        });

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
//        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.search_menu_item, menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        System.out.println(item.getItemId() + "<-- itemId");
        System.out.println(R.menu.search_menu_item + "<-- MOJ ID");

        final EditText textInput = new EditText(getActivity());
        textInput.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Search")
                .setMessage("Search items by\n name/description")
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String searchTerm = textInput.getText().toString();
                        searchItems(searchTerm);
                    }
                })
                .setNeutralButton("Cancel", null)
                .setView(textInput)
                .create();

        builder.show();
        return true;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser && this.activity != null){
            loadItems(this.token);
        }
    }

    private void openItemActivity(Item selectedItem){
        Intent intent = new Intent(this.activity, EditItemActivity.class);
        intent.putExtra("itemId", selectedItem.getId());
        intent.putExtra("userId", this.userId);
        intent.putExtra("token", this.token);
        startActivity(intent);
    }


    private void updateUI(List<Item> items){
        this.items = items;

        ItemsCustomArrayAdapter adapter =
                new ItemsCustomArrayAdapter(this.items, getActivity(), this.token);
        this.listView.setAdapter(adapter);

        InitObjects.hideProgressDialog();
    }


    private void loadItems(String token){
        InitObjects.showProgressDialog(this.activity);

        Call<List<Item>> call = InitObjects.itemApiService.getAllItems(token);

        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                // !!! maybe use ItemsDTO under !!!
                List<Item> items = response.body();


//                for(Item item : items){
//                    System.out.println(item.getAuctions());
//
//                }

                updateUI(items);

            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void searchItems(String searchTerm){

        ArrayList<Item> foundItems = new ArrayList<>();

        for(Item item : this.items){
            if(item.getName().contains(searchTerm)
                    || item.getDescription().contains(searchTerm)){
                foundItems.add(item);
            }
        }
        ItemsCustomArrayAdapter adapter =
                new ItemsCustomArrayAdapter(foundItems, getActivity(), this.token);
        this.listView.setAdapter(adapter);

    }

}
