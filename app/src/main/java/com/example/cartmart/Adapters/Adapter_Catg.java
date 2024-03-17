package com.example.cartmart.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartmart.Interfaces.Catg_RV_Interface;
import com.example.cartmart.R;

import java.util.ArrayList;

public class Adapter_Catg extends RecyclerView.Adapter<Adapter_Catg.MyViewHolder> {

    Context context;
    ArrayList<String> catgList;
    private final Catg_RV_Interface catg_rv_interface;



    public Adapter_Catg(Context context, ArrayList<String> catgList, Catg_RV_Interface catg_rv_interface) {
        this.context = context;
        this.catgList = catgList;
        this.catg_rv_interface = catg_rv_interface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rv_categ_h, parent, false);
        return new MyViewHolder(v, catg_rv_interface);
    }
    int index;
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String str = catgList.get(position);
        holder.textview.setText(str);
        holder.cv_catg_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = holder.getBindingAdapterPosition();
                notifyDataSetChanged();
                catg_rv_interface.onCatgClicked(position);
//                Toast.makeText(context, "Hello Guys", Toast.LENGTH_SHORT).show();
//                Log.d("ab", "clicked");
            }
        });
        if(index == position){
            holder.cv_catg_h.setBackgroundResource(R.drawable.brown_circle_bacground);
            holder.textview.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else {
            holder.textview.setTextColor(Color.parseColor("#000000"));
            holder.cv_catg_h.setBackgroundResource(R.drawable.white_layout_background);
        }

    }



    @Override
    public int getItemCount() {
        return catgList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textview;
//        CardView cv_catg_h;
        ConstraintLayout cv_catg_h;

        public MyViewHolder(@NonNull View itemView, Catg_RV_Interface catg_rv_interface) {
            super(itemView);
            textview = itemView.findViewById(R.id.textview);
            cv_catg_h = itemView.findViewById(R.id.cv_catg_h);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (catg_rv_interface != null){
//                        int pos = getAdapterPosition();
//
//                        if (pos != RecyclerView.NO_POSITION){
//                            catg_rv_interface.onCatgClicked(pos);
//                        }
//                    }
//                }
//            });
        }
    }
}
