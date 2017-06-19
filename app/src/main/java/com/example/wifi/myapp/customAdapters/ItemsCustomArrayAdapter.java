package com.example.wifi.myapp.customAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.model.InitObjects;
import com.example.wifi.myapp.model.Item;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ItemsCustomArrayAdapter extends ArrayAdapter<Item> {

    private Context mContext;
    private String token;

    // View lookup cache
    private static class ViewHolder {
        ImageView imageViewThumbnail;
        TextView textViewItemName;
        TextView textViewIsSold;
        ImageView imageViewDeleteItem;
        ImageView imageViewSoldTag;
    }


    public ItemsCustomArrayAdapter(List<Item> data, Context context, String token){
        super(context, R.layout.custom_items_adapter_layout, data);
        this.mContext = context;
        this.token = token;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Item item = getItem(position);
        final long itemId = item.getId();
        final ViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.custom_items_adapter_layout, parent, false);
            viewHolder.imageViewThumbnail = (ImageView) convertView.findViewById(R.id.imageViewThumbnail);
            viewHolder.textViewItemName = (TextView) convertView.findViewById(R.id.textViewItemName);
            viewHolder.textViewIsSold = (TextView) convertView.findViewById(R.id.textViewIsSold);
            viewHolder.imageViewDeleteItem = (ImageView) convertView.findViewById(R.id.imageViewDeleteItem);
            viewHolder.imageViewSoldTag = (ImageView) convertView.findViewById(R.id.imageViewSoldTag);

            Picasso.with(getContext())
                    .load(InitObjects.ITEMS_URL + item.getPicture())
                    .resize(50, 50)
                    .centerCrop()
                    .into(viewHolder.imageViewThumbnail, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            viewHolder.imageViewThumbnail
                                    .setImageResource(R.drawable.img_not_found);
                        }
                    });

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


        String sold = "Not Sold!";
        if(item.isSold()){
            sold = "Sold!";
            RelativeLayout relativeLayout = (RelativeLayout) viewHolder.imageViewDeleteItem.getParent();
            if(relativeLayout != null)
                relativeLayout.removeView(viewHolder.imageViewDeleteItem);
        }

        viewHolder.textViewItemName.setText(item.getName());
        viewHolder.textViewIsSold.setText(sold);
        viewHolder.imageViewDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(mContext);
                builder.setTitle("Are you sure?");
                builder.setMessage("This item will be deleted");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem(itemId, token);
                    }
                });

                builder.setNegativeButton("NO", null);
                builder.create();
                builder.show();

            }
        });


        if(item.isSold()) {
            viewHolder.imageViewSoldTag.setImageResource(R.drawable.sold_tag);
        }

        return convertView;
    }


    private void deleteItem(long itemId, String token){

        Call<Void> call = InitObjects.itemApiService.deleteItem(itemId, token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Toast.makeText(getContext(), "Item deleted successfully!", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        String message = response.errorBody().string();
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

}
