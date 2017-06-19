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
import android.widget.TextView;
import android.widget.Toast;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.model.Auction;
import com.example.wifi.myapp.model.InitObjects;
import com.example.wifi.myapp.service.apiCalls.AuctionApiServiceInterface;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyAuctionsCustomArrayAdapter extends ArrayAdapter<Auction> {

    private Context mContext;
    private String token;

    public MyAuctionsCustomArrayAdapter(List<Auction> data, Context context, String token){
        super(context, R.layout.custom_my_auctions_adapter_layout, data);
        this.mContext = context;
        this.token = token;
    }


    // View lookup cache
    private static class ViewHolder {
        ImageView imageViewThumbnail;
        TextView textViewStartPrice;
        TextView textViewItemName;
        TextView textViewNumberOfBids;
        TextView textViewEndDate;
        ImageView imageViewSoldTag;
        ImageView imageViewDeleteAuction;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Auction auction = getItem(position);
        MyAuctionsCustomArrayAdapter.ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new MyAuctionsCustomArrayAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_my_auctions_adapter_layout, parent, false);
            viewHolder.imageViewThumbnail = (ImageView) convertView.findViewById(R.id.imageViewThumbnail);
            viewHolder.textViewStartPrice = (TextView) convertView.findViewById(R.id.textViewStartPrice);
            viewHolder.textViewItemName = (TextView) convertView.findViewById(R.id.textViewItemName);
            viewHolder.textViewNumberOfBids = (TextView) convertView.findViewById(R.id.textViewNumberOfBids);
            viewHolder.textViewEndDate = (TextView) convertView.findViewById(R.id.textViewEndDate);
            viewHolder.imageViewSoldTag = (ImageView) convertView.findViewById(R.id.imageViewSoldTag);
            viewHolder.imageViewDeleteAuction = (ImageView) convertView.findViewById(R.id.imageViewDeleteAuction);

            convertView.setTag(viewHolder);

        }else {
            viewHolder = (MyAuctionsCustomArrayAdapter.ViewHolder) convertView.getTag();
        }


        System.out.println("JEL NULL? ");
        System.out.println(viewHolder.imageViewDeleteAuction == null);

        int size = 0;
        double price = auction.getStartPrice();

        if(auction.getBids().size() != 0) {
            size = auction.getBids().size();
            price = auction.getBids().get(size - 1).getPrice();
        }


        Date today = new Date();
        long tmp = auction.getEndDate().getTime() - today.getTime();

        long daysLeft = TimeUnit.DAYS.convert(tmp, TimeUnit.MILLISECONDS);
        String daysLeftStr = String.valueOf(daysLeft);
        if(daysLeft < 0){
            daysLeftStr = "over";
        }

//        viewHolder.textViewStartPrice.setText(String.valueOf(auction.getStartPrice()));
        viewHolder.textViewStartPrice.setText(String.valueOf(price));
        viewHolder.textViewItemName.setText(auction.getItem().getName());
        viewHolder.textViewNumberOfBids.setText(String.valueOf(size));
        viewHolder.textViewEndDate.setText(daysLeftStr);

        final ImageView imageViewThumbnail = viewHolder.imageViewThumbnail;

        Picasso.with(getContext())
                .load(InitObjects.ITEMS_URL + auction.getItem().getPicture())
                .resize(50, 50)
                .centerCrop()
                .into(imageViewThumbnail, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        imageViewThumbnail.setImageResource(R.drawable.img_not_found);
                    }
                });

//        if(auction.getItem().isSold()) {
//            viewHolder.imageViewSoldTag.setImageResource(R.drawable.sold_tag);
//        }else if(auction.isOver()){
//            viewHolder.imageViewSoldTag.setImageResource(R.drawable.finish_tag);
//        }
        if(auction.isOver()){
            viewHolder.imageViewSoldTag.setImageResource(R.drawable.finish_tag);
        }else{
            viewHolder.imageViewDeleteAuction.setImageResource(R.drawable.ic_delete_black);
            viewHolder.imageViewDeleteAuction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(mContext);
                    builder.setTitle("Are you sure?");
                    builder.setMessage("This auction will be deleted");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteAuction(auction.getId(), token);
                        }
                    });

                    builder.setNegativeButton("NO", null);
                    builder.create();
                    builder.show();
                }
            });
        }

        return convertView;

    }

    private void deleteAuction(long auctionId, String token){
        Call<Void> call = InitObjects.auctionApiService.deleteAuction(auctionId, token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Toast.makeText(getContext(), "Auction deleted successfully!", Toast.LENGTH_SHORT).show();
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
