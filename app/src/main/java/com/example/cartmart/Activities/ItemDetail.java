package com.example.cartmart.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cartmart.Models.Item_c;
import com.example.cartmart.Models.Item_h;
import com.example.cartmart.Models.User;
import com.example.cartmart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ItemDetail extends AppCompatActivity {
    ImageView img_item_id, img_back;
    TextView txt_name_item_id, txt_price_item_id, txt_sub_id, txt_count_id, txt_add_id, txt_Description_item_id;
    Button btn_addToCart_id;

    Item_h item_h;

    int count;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    ProgressBar progressBar;
    String userId, email, uID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        findViewsItemDetail();
        getUserIdFromRFB();
        count = 1;

        progressBar.setVisibility(View.VISIBLE);
        Intent intent = getIntent();

        item_h = new Item_h();
        item_h.setImg_item_h(intent.getStringExtra("itemImage"));
        item_h.setStr_id_item_h(intent.getStringExtra("ItemID"));
        item_h.setStr_category_item_h(intent.getStringExtra("ItemCategory"));
        item_h.setStr_description_item_h(intent.getStringExtra("ItemDescription"));
        item_h.setStr_name_item_h(intent.getStringExtra("ItemName"));
        item_h.setStr_price_item_h(intent.getStringExtra("ItemPrice"));


        Picasso.get().load(item_h.getImg_item_h()).into(img_item_id);
        txt_name_item_id.setText(item_h.getStr_name_item_h());
        txt_price_item_id.setText(item_h.getStr_price_item_h() + " RS");
        txt_Description_item_id.setText(item_h.getStr_description_item_h());

        progressBar.setVisibility(View.GONE);




        txt_sub_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Sub", Toast.LENGTH_SHORT).show();
                if (count > 1){
                    count--;
                    txt_count_id.setText(Integer.toString(count));
                }
            }
        });
        txt_add_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Add", Toast.LENGTH_SHORT).show();
                if (count < 99){
                    count++;
                    txt_count_id.setText(Integer.toString(count));
                }
            }
        });
        btn_addToCart_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCartOnRFB();
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    private void getUserIdFromRFB() {
        progressBar.setVisibility(View.VISIBLE);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        email = firebaseUser.getEmail();
        databaseReference = FirebaseDatabase.getInstance().getReference("UserName");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getEmail().equals(email)){
                        userId = dataSnapshot.getKey();
                        progressBar.setVisibility(View.GONE);
//                        Toast.makeText(ItemDetail.this, userId, Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ItemDetail.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void addToCartOnRFB() {
        progressBar.setVisibility(View.VISIBLE);
        Item_c item_c = new Item_c(item_h.getStr_id_item_h(), Integer.toString(count), item_h.getStr_category_item_h());
        databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child(userId);
        databaseReference.child(item_h.getStr_id_item_h()).setValue(item_c).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressBar.setVisibility(View.GONE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ItemDetail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Added To Cart", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                finish();
            }
        });
    }


    private void findViewsItemDetail() {
        img_item_id = findViewById(R.id.img_item_id);
        txt_name_item_id = findViewById(R.id.txt_name_item_id);
        txt_price_item_id = findViewById(R.id.txt_price_item_id);
        txt_count_id = findViewById(R.id.txt_count_id);
        txt_sub_id = findViewById(R.id.txt_sub_id);
        txt_add_id = findViewById(R.id.txt_add_id);
        txt_Description_item_id = findViewById(R.id.txt_Description_item_id);
        btn_addToCart_id = findViewById(R.id.btn_addToCart_id);
        img_back = findViewById(R.id.img_back);
        progressBar = findViewById(R.id.progressBar);
    }
}