package com.example.cartmart.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartmart.Interfaces.Item_RV_Interface;
import com.example.cartmart.Models.Item_h;
import com.example.cartmart.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter_Item extends RecyclerView.Adapter<Adapter_Item.MyViewHolder> {

    Context context;

    ArrayList<Item_h> item_hArrayList;

    Item_RV_Interface item_rv_interface;

    public Adapter_Item(Context context, ArrayList<Item_h> item_hArrayList, Item_RV_Interface item_rv_interface) {
        this.context = context;
        this.item_hArrayList = item_hArrayList;
        this.item_rv_interface = item_rv_interface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.rv_item_h, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Item_h item_h = item_hArrayList.get(position);
        holder.txt_name_item_h.setText(item_h.getStr_name_item_h());
        holder.txt_price_item_h.setText(item_h.getStr_price_item_h());
        Picasso.get().load(item_h.getImg_item_h()).into(holder.img_item_h);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item_rv_interface.onItemClicked(item_hArrayList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return item_hArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView img_item_h;
        TextView txt_name_item_h, txt_price_item_h;

        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_item_h = itemView.findViewById(R.id.img_item_h);
            txt_name_item_h = itemView.findViewById(R.id.txt_name_item_h);
            txt_price_item_h = itemView.findViewById(R.id.txt_price_item_h);
            cardView = itemView.findViewById(R.id.cv_item_h);
        }
    }

}

