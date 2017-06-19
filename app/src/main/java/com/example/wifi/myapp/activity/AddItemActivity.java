package com.example.wifi.myapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.model.DTO.ItemDTO;
import com.example.wifi.myapp.model.InitObjects;
import com.example.wifi.myapp.model.Item;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItemActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;

    private String token;
    private long userId;
    private String imageName = "default.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        this.token = getIntent().getStringExtra("token");
        this.userId = getIntent().getLongExtra("userId", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("New Item");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        final EditText editTextItemName = (EditText) findViewById(R.id.editTextItemName);
        final EditText editTextItemDesc = (EditText) findViewById(R.id.editTextItemDescription);
        ImageView imageViewItemImage = (ImageView) findViewById(R.id.imageViewItemImage);

        imageViewItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        Button buttonAddItem = (Button) findViewById(R.id.buttonAddItem);
        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemName = editTextItemName.getText().toString();
                String itemDesc = editTextItemDesc.getText().toString();

                if(itemName.isEmpty() || itemDesc.isEmpty()){
                    Toast.makeText(AddItemActivity.this, "Fields can't be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ItemDTO itemDto = new ItemDTO(itemName, itemDesc, imageName, false, userId);
                addItem(itemDto, token);
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = this.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) this.findViewById(R.id.imageViewItemImage);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            uploadItemPicture(picturePath);

        }



    }


    private void selectImage(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    private void updateUI(){
        Toast.makeText(this, "Item added successfully!", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    private void addItem(ItemDTO itemDto, String token){
        Call<ItemDTO> call = InitObjects.itemApiService.addItem(itemDto, token);
        call.enqueue(new Callback<ItemDTO>() {
            @Override
            public void onResponse(Call<ItemDTO> call, Response<ItemDTO> response) {
                if(response.code() == 201){
                    updateUI();
                }
            }

            @Override
            public void onFailure(Call<ItemDTO> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void uploadItemPicture(String filePath){

        File file = new File(filePath);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Call<String> call = InitObjects.userApiService.uploadItemPicture(body, this.token);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code() == 200){
                    imageName = response.body().toString();
                    Toast
                        .makeText(AddItemActivity.this, "Image uploaded successfully!", Toast.LENGTH_SHORT)
                        .show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
