package com.example.wifi.myapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.fragments.AuctionsFragment;
import com.example.wifi.myapp.fragments.ItemsFragment;
import com.example.wifi.myapp.fragments.MeFragment;
import com.example.wifi.myapp.fragments.SettingsFragment;
import com.example.wifi.myapp.model.InitObjects;
import com.example.wifi.myapp.model.User;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private String token;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private View headerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        this.token = getIntent().getStringExtra("token");
        loadUser(token);

//        this.userId = prefs.getLong("userId", 0); // getting userId from prefs
//        System.out.println("AJDI: " + this.userId);
//        getIntent().putExtra("userId", this.userId); // and putting it as Extra, so i can get it from other fragments


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

//        loadFragment(-1);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);

        this.headerView = navigationView.getHeaderView(0);

        this.headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(3);
                drawerLayout.closeDrawers();

            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_item_auctions:
                        loadFragment(-1);
                        break;
                    case R.id.nav_item_items:
                        loadFragment(1);
                        break;
                    case R.id.nav_item_settings:
                        loadFragment(2);
                        break;
                    default:
                        logout();
                }


                drawerLayout.closeDrawers();
                return true;
            }
        });


        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();


    }


    private void loadFragment(int position){
        Fragment fragment;

        switch (position){
            case 1:
                getSupportActionBar().setTitle("Items");
                fragment = new ItemsFragment();
                break;
            case 2:
                fragment = new SettingsFragment();
                getSupportActionBar().setTitle("Settings");
                break;
            case 3:
                fragment = new MeFragment();
                getSupportActionBar().setTitle("Me");
                break;
            default:
                fragment = new AuctionsFragment();
                getSupportActionBar().setTitle("Auctions");
        }


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();



    }

    private void logout(){
        final Activity activity = this;
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setMessage("You're about to log out");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
                preferences.edit().putString("token", null).apply();
                preferences.edit().putLong("userId", 0).apply();

                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("NO", null);
        builder.create().show();

    }

    private void updateUI(User user){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        preferences.edit().putLong("userId", user.getId()).apply();
        getIntent().putExtra("userId", user.getId());

        System.out.println("AJDI: " + user.getId()); // obrisi

        ImageView imageView = (ImageView) this.headerView.findViewById(R.id.nav_drawer_image);

        Picasso.with(getApplicationContext())
                .load(InitObjects.AVATAR_URL + user.getPicture())
                .resize(50, 50)
                .centerCrop()
                .into(imageView);

        TextView textViewName = (TextView) this.headerView.findViewById(R.id.nav_drawer_user_name);
        TextView textViewEmail = (TextView) this.headerView.findViewById(R.id.nav_drawer_user_email);

        textViewName.setText(user.getName());
        textViewEmail.setText(user.getEmail());

        InitObjects.hideProgressDialog();

        loadFragment(-1);

    }

    private void loadUser(String token){
        InitObjects.showProgressDialog(this);

        Call<User> call = InitObjects.meApiServiceInterface.getMe(token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();

                updateUI(user);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
//        return false;

    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }


}
