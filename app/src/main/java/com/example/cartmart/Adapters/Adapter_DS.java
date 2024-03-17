package com.example.cartmart.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartmart.Models.Item_c;
import com.example.cartmart.Models.Item_ds;
import com.example.cartmart.Models.Item_h;
import com.example.cartmart.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter_DS extends RecyclerView.Adapter<Adapter_DS.MyViewHolder>{

    Context context;

    ArrayList<Item_h> itemList_h;
    ArrayList<Item_c> itemList_c;

    public Adapter_DS(Context context, ArrayList<Item_h> itemList_h, ArrayList<Item_c> itemList_c) {
        this.context = context;
        this.itemList_h = itemList_h;
        this.itemList_c = itemList_c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rv_item_ds, parent, false);

        return new Adapter_DS.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item_c item_c = itemList_c.get(position);

        Item_h item_h = itemList_h.get(position);
        holder.txt_price_rv_ds.setText(item_h.getStr_price_item_h() + "Rs");
        holder.txt_name_rv_ds.setText(item_c.getStr_count() + "." + item_h.getStr_name_item_h());
        int price1 = Integer.parseInt(item_h.getStr_price_item_h());
        int price2 = Integer.parseInt(item_c.getStr_count());
        int price = price1 * price2;
        holder.txt_price_rv_ds.setText(Integer.toString(price));
        Picasso.get().load(item_h.getImg_item_h()).into(holder.img_rv_ds);
//        holder.img_rv_ds.setImageResource(item_ds.getImg_item_ds());
//        holder.txt_price_rv_ds.setText(item_ds.getStr_price_ds() + " Rs");
//        String name = item_ds.getStr_noOfItems_ds() + "." + item_ds.getStr_name_ds();
//        holder.txt_name_rv_ds.setText(name);

//        int price1 = Integer.parseInt(item_ds.getStr_price_ds());
//        int price2 = Integer.parseInt(item_ds.getStr_noOfItems_ds());
//        int price = price1 * price2;
//        String prices = Integer.toString(price);
//        holder.txt_price_rv_ds.setText(prices);

    }

    @Override
    public int getItemCount() {
        return itemList_h.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView img_rv_ds;
        TextView txt_name_rv_ds, txt_price_rv_ds;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_rv_ds = itemView.findViewById(R.id.img_rv_ds);
            txt_name_rv_ds = itemView.findViewById(R.id.txt_name_rv_ds);
            txt_price_rv_ds = itemView.findViewById(R.id.txt_price_rv_ds);

        }
    }

}
