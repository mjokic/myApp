package com.example.wifi.myapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.model.DTO.ItemDTO;
import com.example.wifi.myapp.model.InitObjects;
import com.example.wifi.myapp.model.Item;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditItemActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;

    private String token;
    private long userId;
    private Item item;
    private String imageName = "default.png";


    private EditText editTextItemName;
    private EditText editTextItemDesc;
    private ImageView imageViewItemImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Item");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        final long itemId = getIntent().getLongExtra("itemId", 0);
        this.token = getIntent().getStringExtra("token");
        this.userId = getIntent().getLongExtra("userId", 0);

        loadItem(itemId, this.token);

        editTextItemName = (EditText) findViewById(R.id.editTextItemName);
        editTextItemDesc = (EditText) findViewById(R.id.editTextItemDescription);
        imageViewItemImage = (ImageView) findViewById(R.id.imageViewItemImage);

        imageViewItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        Button button = (Button) findViewById(R.id.buttonEditItem);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDTO itemDTO =
                        new ItemDTO(editTextItemName.getText().toString(),
                                editTextItemDesc.getText().toString(),
                                imageName,
                                item.isSold(),
                                userId);
                if(itemDTO.getName().isEmpty() || itemDTO.getDescription().isEmpty()){
                    Toast.makeText(EditItemActivity.this, "Fields can't be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateItem(itemDTO, itemId, token);
            }
        });


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

    private void updateUI(Item item){
        this.item = item;
        this.imageName = item.getPicture();

        InitObjects.hideProgressDialog();

//        final ImageView imageViewItemImage = (ImageView) findViewById(R.id.imageViewItemImage);
//        EditText editTextItemName = (EditText) findViewById(R.id.editTextItemName);
//        EditText editTextItemDesc = (EditText) findViewById(R.id.editTextItemDescription);

        editTextItemName.setText(item.getName());
        editTextItemDesc.setText(item.getDescription());

        Picasso.with(getApplicationContext())
                .load(InitObjects.ITEMS_URL + item.getPicture())
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
    }

    private void loadItem(long itemId, String token){
        InitObjects.showProgressDialog(this);

        Call<Item> call = InitObjects.itemApiService.getItemById(itemId, token);
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if(response.code() == 200){
                    Item item = response.body();
                    updateUI(item);
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void updateItem(ItemDTO itemDTO, long itemId, String token){

//        ItemDTO itemDTO =
//                new ItemDTO(item.getName(), item.getDescription(), item.getPicture(), item.isSold(), this.userId);

        Call<ItemDTO> call = InitObjects.itemApiService.editItem(itemDTO, itemId, token);
        call.enqueue(new Callback<ItemDTO>() {
            @Override
            public void onResponse(Call<ItemDTO> call, Response<ItemDTO> response) {

                if(response.code() == 200){
                    Toast.makeText(EditItemActivity.this, "Edit successful!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else{
                    Toast.makeText(EditItemActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
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
                            .makeText(EditItemActivity.this, "Image uploaded successfully!", Toast.LENGTH_SHORT)
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
