package com.example.wifi.myapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.model.InitObjects;
import com.example.wifi.myapp.model.LoginCredentials;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }



    public void onClickButtonLogin(View view){

        EditText emailEditText = (EditText) findViewById(R.id.editTextEmail);
        EditText passEditText = (EditText) findViewById(R.id.editTextPassword);

        String email = emailEditText.getText().toString();
        String password = passEditText.getText().toString();

        if(email.equals("") || password.equals("")){
            Toast.makeText(this, "Email/Password cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // initializing shared preferences and editor
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.editor = this.prefs.edit();

        String device_token = FirebaseInstanceId.getInstance().getToken();

        LoginCredentials creds = new LoginCredentials(email, password, device_token);
        login(creds);

    }

    public void onClickButtonRegister(View view){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private void login(LoginCredentials creds){
        InitObjects.showProgressDialog(this);

        Call<Void> call = InitObjects.userApiService.login(creds);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int status = response.code();
                if(status == 200){
                    String token = response.headers().get("Authorization");

                    // saving key to shared preferences
                    editor.putString("token", token);
                    editor.commit();

                    InitObjects.hideProgressDialog();

                    startMainActivity(token);


                }else{
                    InitObjects.hideProgressDialog();
                    Toast.makeText(LoginActivity.this, "Invalid email/password", Toast.LENGTH_LONG).show();
                    return;
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }

    private void startMainActivity(String token){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("token", token);

        startActivity(intent);
    }

}
