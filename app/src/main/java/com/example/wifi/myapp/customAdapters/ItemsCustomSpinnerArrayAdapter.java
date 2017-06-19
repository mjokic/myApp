package com.example.wifi.myapp.customAdapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wifi.myapp.R;
import com.example.wifi.myapp.model.InitObjects;
import com.example.wifi.myapp.model.Item;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ItemsCustomSpinnerArrayAdapter extends ArrayAdapter<Item> {

    private Item item;
    private LayoutInflater layoutInflater;

    // View lookup cache
    private static class ViewHolder {
        TextView textViewItemName;
    }


    public ItemsCustomSpinnerArrayAdapter(List<Item> data, Context context, int textviewId){
        super(context, R.layout.custom_items_spinner_adapter_layout, textviewId, data);

        for(int i = 0; i < data.size(); i++){
            if(data.get(i).isSold()) data.remove(i);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        this.item = getItem(position);

        ItemsCustomSpinnerArrayAdapter.ViewHolder viewHolder;


        if (convertView == null) {

            viewHolder = new ItemsCustomSpinnerArrayAdapter.ViewHolder();
            layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.custom_items_spinner_adapter_layout, parent, false);
            viewHolder.textViewItemName = (TextView) convertView.findViewById(R.id.textViewItemName);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ItemsCustomSpinnerArrayAdapter.ViewHolder) convertView.getTag();
        }


        viewHolder.textViewItemName.setText(item.getName());


        return convertView;
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.custom_items_spinner_adapter_layout, parent, false);
        }

        Item item = getItem(position);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.textViewItemName);
        txtTitle.setText(item.getName());
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewItemThumbnail);
//        imageView.setImageResource(item.getPicture());
//        imageView.setImageResource(R.drawable.img_not_found);

        Picasso.with(getContext())
                .load(InitObjects.ITEMS_URL + item.getPicture())
                .resize(30, 30)
                .centerCrop()
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        imageView.setImageResource(R.drawable.img_not_found);
                    }
                });

        return convertView;
    }
}
