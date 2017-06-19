package com.example.wifi.myapp.customAdapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.model.Bid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BidsCustomArrayAdapter extends ArrayAdapter<Bid> {

    private List<Bid> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView textViewPrice;
        TextView textViewDate;
        TextView textViewUserEmail;
    }


    public BidsCustomArrayAdapter(List<Bid> data, Context context){
        super(context, R.layout.custom_bids_adapter_layout, data);
        this.dataSet = data;
        this.mContext = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);

        Bid bid = getItem(position);
        ViewHolder viewHolder;

//        View result;

        if(convertView == null){

            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.custom_bids_adapter_layout, parent, false);
            viewHolder.textViewPrice = (TextView) convertView.findViewById(R.id.textViewBidPrice);
            viewHolder.textViewDate = (TextView) convertView.findViewById(R.id.textViewBidDate);
            viewHolder.textViewUserEmail = (TextView) convertView.findViewById(R.id.textViewBidUserEmail);

//            result = convertView;
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
//            result=convertView;
        }

        viewHolder.textViewPrice.setText(String.valueOf(bid.getPrice()));
        viewHolder.textViewDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bid.getDateTime()));
        viewHolder.textViewUserEmail.setText(bid.getUser().getEmail());

        return convertView;
    }
}
