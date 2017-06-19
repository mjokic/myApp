package com.example.wifi.myapp.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.model.InitObjects;
import com.example.wifi.myapp.model.RegisterCredentials;
import com.example.wifi.myapp.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }




    public void onClickButtonRegister(View view){

        EditText editTextName = (EditText) findViewById(R.id.editTextName);
        EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        EditText editTextPasswordAgain = (EditText) findViewById(R.id.editTextPasswordAgain);
        EditText editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        EditText editTextPhone = (EditText) findViewById(R.id.editTextPhone);


        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String passwordAgain = editTextPasswordAgain.getText().toString();
        String address = editTextAddress.getText().toString();
        String phone = editTextPhone.getText().toString();


        if(name.equals("") || email.equals("") || password.equals("")
                || address.equals("") || phone.equals("") || passwordAgain.equals("")){
            Toast.makeText(this, "Fields can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password.equals(passwordAgain)){
            Toast.makeText(this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterCredentials creds = new RegisterCredentials(
                name,email,password,address,phone
        );


        Call<User> call = InitObjects.userApiService.register(creds);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200) {
                    Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }else{
                    Toast.makeText(RegisterActivity.this, "Email or phone number already in use!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

}
