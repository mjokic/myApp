package com.example.wifi.myapp.customAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.model.Auction;
import com.example.wifi.myapp.model.Bid;
import com.example.wifi.myapp.model.InitObjects;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class AuctionsCustomArrayAdapter extends ArrayAdapter<Auction> {

    public AuctionsCustomArrayAdapter(List<Auction> data, Context context){
        super(context, R.layout.custom_auctions_adapter_layout, data);

    }


    // View lookup cache
    private static class ViewHolder {
        ImageView imageViewThumbnail;
        TextView textViewStartPrice;
        TextView textViewItemName;
        TextView textViewNumberOfBids;
        TextView textViewEndDate;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Auction auction = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_auctions_adapter_layout, parent, false);
            viewHolder.imageViewThumbnail = (ImageView) convertView.findViewById(R.id.imageViewThumbnail);
            viewHolder.textViewStartPrice = (TextView) convertView.findViewById(R.id.textViewStartPrice);
            viewHolder.textViewItemName = (TextView) convertView.findViewById(R.id.textViewItemName);
            viewHolder.textViewNumberOfBids = (TextView) convertView.findViewById(R.id.textViewNumberOfBids);
            viewHolder.textViewEndDate = (TextView) convertView.findViewById(R.id.textViewEndDate);

            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        int size = 0;
        double price = auction.getStartPrice();

        if(auction.getBids().size() != 0) {
            size = auction.getBids().size();
            price = auction.getBids().get(size - 1).getPrice();
        }


        Date today = new Date();
        long tmp = auction.getEndDate().getTime() - today.getTime();

        long daysLeft = TimeUnit.DAYS.convert(tmp, TimeUnit.MILLISECONDS);

//        viewHolder.imageViewThumbnail.setImageResource(); // <-- set up image later
//        viewHolder.textViewStartPrice.setText(String.valueOf(auction.getStartPrice()));
        viewHolder.textViewStartPrice.setText(String.valueOf(price));
        viewHolder.textViewItemName.setText(auction.getItem().getName());
        viewHolder.textViewNumberOfBids.setText(String.valueOf(size));
        viewHolder.textViewEndDate.setText(String.valueOf(daysLeft));
        final ImageView imageViewThumbnail = (ImageView) viewHolder.imageViewThumbnail;

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

        return convertView;

    }
}
