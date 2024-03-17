package com.example.cartmart.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartmart.Interfaces.Cart_RV_Interface;
import com.example.cartmart.Models.Item_c;
import com.example.cartmart.Models.Item_h;
import com.example.cartmart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter_Cart extends RecyclerView.Adapter<Adapter_Cart.MyViewHolder> {

    Context context;
    ArrayList<Item_c> itemListC;
    ArrayList<Item_h> item_hArrayList;
    private final Cart_RV_Interface cart_rv_interface;
    DatabaseReference databaseReference;



    public Adapter_Cart(Context context, ArrayList<Item_c> itemListC, ArrayList<Item_h> item_hArrayList, Cart_RV_Interface cart_rv_interface) {
        this.context = context;
        this.itemListC = itemListC;
        this.cart_rv_interface = cart_rv_interface;
        this.item_hArrayList = item_hArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rv_item_c, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item_c item_c = itemListC.get(position);
        Item_h item_h = item_hArrayList.get(position);
        holder.txt_name_rv_c.setText(item_h.getStr_name_item_h());
        holder.txt_price_rv_c.setText(item_h.getStr_price_item_h() + " Rs");
        Picasso.get().load(item_h.getImg_item_h()).into(holder.img_rv_c);
        holder.txt_count_rv_c.setText(item_c.getStr_count());


//        databaseReference = FirebaseDatabase.getInstance().getReference("Category").child(item_c.getStr_category()).child(item_c.getStr_item_id());
//        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (task.isSuccessful()){
//                    DataSnapshot dataSnapshot = task.getResult();
//                    Item_h item_h = dataSnapshot.getValue(Item_h.class);
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//        holder.txt_name_rv_c.setText(item_c.getStr_name_item_c());
//        holder.txt_price_rv_c.setText(item_c.getStr_price_item_c() + " Rs");
//        holder.img_rv_c.setImageResource(item_c.getImg_item_c());
//        holder.txt_count_rv_c.setText(item_c.getStr_count());


        holder.img_bin_rv_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart_rv_interface.onDeleteClickedCart(position);
            }
        });

        holder.txt_sub_rv_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item_c item_c1 = itemListC.get(position);
                String countR = "1";
                int count = Integer.parseInt(item_c1.getStr_count());
                if(count > 1){
                    count--;
                    countR = Integer.toString(count);
                    holder.txt_count_rv_c.setText(countR);
                    item_c1.setStr_count(countR);
                }
                cart_rv_interface.onSubClickedCart(position, countR);
            }
        });

        holder.txt_add_rv_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item_c item_c1 = itemListC.get(position);
                String countR = "1";
                int count = Integer.parseInt(item_c1.getStr_count());
                if (count < 99){

                    count++;
                    countR = Integer.toString(count);
                    holder.txt_count_rv_c.setText(countR);
                    item_c1.setStr_count(countR);
                }
                cart_rv_interface.onAddClickedCart(position, countR);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemListC.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView img_bin_rv_c, img_rv_c;
        TextView txt_name_rv_c, txt_price_rv_c, txt_sub_rv_c, txt_add_rv_c, txt_count_rv_c;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            img_bin_rv_c = itemView.findViewById(R.id.img_bin_rv_c);
            img_rv_c = itemView.findViewById(R.id.img_rv_c);
            txt_name_rv_c = itemView.findViewById(R.id.txt_name_rv_c);
            txt_price_rv_c = itemView.findViewById(R.id.txt_price_rv_c);
            txt_sub_rv_c = itemView.findViewById(R.id.txt_sub_rv_c);
            txt_add_rv_c = itemView.findViewById(R.id.txt_add_rv_c);
            txt_count_rv_c = itemView.findViewById(R.id.txt_count_rv_c);
        }
    }
}

