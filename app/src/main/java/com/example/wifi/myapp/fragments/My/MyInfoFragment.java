package com.example.wifi.myapp.fragments.My;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.model.InitObjects;
import com.example.wifi.myapp.model.User;
import com.squareup.picasso.Picasso;


import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyInfoFragment extends Fragment implements View.OnClickListener {

    private static int RESULT_LOAD_IMAGE = 1;

    private String token;
    private Activity activity;

    private ImageView imageViewAvatar;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private EditText editTextAddress;
    private EditText editTextPassword;
    private EditText editTextPasswordAgain;

    public MyInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_info, container, false);

        this.activity = getActivity();
        this.token = this.activity.getIntent().getStringExtra("token");
        loadMyInfo(token);

        this.imageViewAvatar = (ImageView) rootView.findViewById(R.id.imageViewUserAvatar);
        this.editTextName = (EditText) rootView.findViewById(R.id.editTextName);
        this.editTextEmail = (EditText) rootView.findViewById(R.id.editTextEmail);
        this.editTextAddress = (EditText) rootView.findViewById(R.id.editTextAddress);
        this.editTextPhone = (EditText) rootView.findViewById(R.id.editTextPhone);
        this.editTextPassword = (EditText) rootView.findViewById(R.id.editTextPassword);
        this.editTextPasswordAgain = (EditText) rootView.findViewById(R.id.editTextPasswordAgain);

        Button saveButton = (Button) rootView.findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(this);

        this.imageViewAvatar.setOnClickListener(this);


        return rootView;
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.imageViewUserAvatar){
            selectImage();
        }else {
            // save info
            User user = new User();
            user.setName(this.editTextName.getText().toString());
            user.setEmail(this.editTextEmail.getText().toString());
            user.setPhone(this.editTextPhone.getText().toString());
            user.setAddress(this.editTextAddress.getText().toString());

            String pass1 = this.editTextPassword.getText().toString();
            String pass2 = this.editTextPasswordAgain.getText().toString();

            if (!pass1.equals("") && !pass2.equals("")) {
                if (!pass1.equals(pass2)) {
                    Toast.makeText(getContext(), "Passwords don't match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                user.setPassword(pass1);
            }
            
            if(user.getName().isEmpty() || user.getEmail().isEmpty() ||
                    user.getPhone().isEmpty() || user.getAddress().isEmpty()) {
                Toast.makeText(activity, "Info fields can't be empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            updateMyInfo(user, this.token);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser && this.activity != null){
            loadMyInfo(this.token);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == this.activity.RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = this.activity.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) this.activity.findViewById(R.id.imageViewUserAvatar);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            uploadUserAvatar(picturePath);

        }



    }



    private void selectImage(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    private void updateUI(User user){
        Picasso.with(getContext())
                .load(InitObjects.AVATAR_URL + user.getPicture())
                .resize(100, 100)
                .centerCrop()
                .into(this.imageViewAvatar);

        this.editTextName.setText(user.getName());
        this.editTextEmail.setText(user.getEmail());
        this.editTextPhone.setText(user.getPhone());
        this.editTextAddress.setText(user.getAddress());

        InitObjects.hideProgressDialog();
    }

    private void loadMyInfo(String token){
        InitObjects.showProgressDialog(this.activity);

        Call<User> call = InitObjects.meApiServiceInterface.getMe(token);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(response.code() == 200){
                    User user = response.body();
                    updateUI(user);
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updateMyInfo(User user, String token){

        Call<User> call = InitObjects.meApiServiceInterface.updateMe(user, token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(response.code() == 200) {
                    User u = response.body();
                    updateUI(u);
                    Toast.makeText(activity, "Info changed successfully", Toast.LENGTH_SHORT).show();
                }else{
                    System.out.println("Something fucked up!");
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void uploadUserAvatar(String filePath){
        InitObjects.showProgressDialog(this.activity);

        File file = new File(filePath);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Call<String> call = InitObjects.userApiService.uploadUserAvatar(body, this.token);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code() == 200){
                    Toast.makeText(activity, "Avatar changed successfully!", Toast.LENGTH_SHORT).show();
                    InitObjects.hideProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                InitObjects.hideProgressDialog();
                Toast.makeText(activity, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
