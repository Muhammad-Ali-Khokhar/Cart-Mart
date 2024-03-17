package com.example.cartmart.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cartmart.Adapters.Adapter_Cart;
import com.example.cartmart.Interfaces.Cart_RV_Interface;
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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment implements Cart_RV_Interface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View v;
    ArrayList<Item_c> itemListC;
    ArrayList<Item_h> item_hArrayList;

    RecyclerView recyclerView;

    Button btn_COrder_c;
    TextView txt_Price_cart, txt_DCharge_cart, txt_Total_cart;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String userId, email;
    ProgressBar progressBar;
    int sizeArray;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v = view;
        findViewsCart(view);
        progressBar.setVisibility(View.VISIBLE);
        getUserIdFromRFB();
        progressBar.setVisibility(View.GONE);



//        setTextInTV();

        btn_COrder_c = view.findViewById(R.id.btn_COrder_c);
        btn_COrder_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity().getApplicationContext(), "Hello Cart", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity().getApplicationContext(), InfoForDelivery.class);
                intent.putExtra("UserID", userId);
                startActivity(intent);
            }
        });

    }
//    private void setTextInTV(){
//        int price = 0;
//        int dcharge = 20;
//        int total = 0;
//
//        for (Item_c i: itemListC){
//            price += Integer.parseInt(i.getStr_count_item_c()) * Integer.parseInt(i.getStr_price_item_c());
//        }
//        total = price + dcharge;
//
//        txt_Total_cart.setText(Integer.toString(total) + " Rs");
//        txt_Price_cart.setText(Integer.toString(price) + " Rs");
//        txt_DCharge_cart.setText(Integer.toString(dcharge) + " Rs");
//    }
    private void findViewsCart(View view){
        txt_Price_cart = view.findViewById(R.id.txt_Price_cart);
        txt_DCharge_cart = view.findViewById(R.id.txt_DCharge_cart);
        txt_Total_cart = view.findViewById(R.id.txt_Total_cart);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void getValueinItem_hList(){
        progressBar.setVisibility(View.VISIBLE);
        item_hArrayList = new ArrayList<>();
        sizeArray = 0;
        for (Item_c item_c: itemListC){
            databaseReference = FirebaseDatabase.getInstance().getReference("Category").child(item_c.getStr_category()).child(item_c.getStr_item_id());
            databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()){
                        DataSnapshot dataSnapshot = task.getResult();
                        Item_h item_h = dataSnapshot.getValue(Item_h.class);
                        item_hArrayList.add(item_h);
                        sizeArray++;
                    }
                    if (sizeArray == itemListC.size()){
                        progressBar.setVisibility(View.GONE);
                        setRecyclerView(v);
                        setTextTV();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
        progressBar.setVisibility(View.GONE);


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
//                        Toast.makeText(ItemDetail.this, userId, Toast.LENGTH_SHORT).show();

                        progressBar.setVisibility(View.GONE);
                        dataInitializeList(v);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    private  void setTextTV(){
        txt_DCharge_cart.setText("20 Rs");
        int price = 0;
        int total;
        for (int i=0; i<item_hArrayList.size(); i++){
            price += Integer.parseInt(item_hArrayList.get(i).getStr_price_item_h()) * Integer.parseInt(itemListC.get(i).getStr_count());
        }
        total = price + 20;
        txt_Price_cart.setText(Integer.toString(price));
        txt_Total_cart.setText(Integer.toString(total));

        progressBar.setVisibility(View.GONE);
    }

    private void setRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.rv_item_c);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        Adapter_Cart adapter_cart = new Adapter_Cart(getContext(), itemListC, item_hArrayList, this);
        recyclerView.setAdapter(adapter_cart);
        adapter_cart.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

    private void dataInitializeList(View view) {
        progressBar.setVisibility(View.VISIBLE);
        itemListC = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemListC = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Item_c item_c = dataSnapshot.getValue(Item_c.class);
                    itemListC.add(item_c);
                }

                progressBar.setVisibility(View.GONE);
                getValueinItem_hList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });


//        Item_c item_c = new Item_c("1122", "2");
//        itemListC.add(item_c);
//        item_c = new Item_c("1122", "2");
//        itemListC.add(item_c);
//        item_c = new Item_c("1122", "2");
//        itemListC.add(item_c);
//        item_c = new Item_c("1122", "2");
//        itemListC.add(item_c);
//        item_c = new Item_c("1122", "2");
//        itemListC.add(item_c);
    }

    @Override
    public void onAddClickedCart(int position, String count) {
        progressBar.setVisibility(View.VISIBLE);
        itemListC.get(position).setStr_count(count);
        databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child(userId).child(itemListC.get(position).getStr_item_id());
        databaseReference.setValue(itemListC.get(position)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity().getApplicationContext(), itemListC.get(position).getStr_count(), Toast.LENGTH_SHORT).show();
                setRecyclerView(v);
                setTextTV();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onSubClickedCart(int position, String count) {
        progressBar.setVisibility(View.VISIBLE);
        itemListC.get(position).setStr_count(count);
        databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child(userId).child(itemListC.get(position).getStr_item_id());
        databaseReference.setValue(itemListC.get(position)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity().getApplicationContext(), itemListC.get(position).getStr_count(), Toast.LENGTH_SHORT).show();
                setTextTV();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDeleteClickedCart(int position) {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child(userId).child(itemListC.get(position).getStr_item_id());
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
//                itemListC.remove(position);
//                setRecyclerView(v);
//                Toast.makeText(getActivity().getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);           }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

//                itemListC.remove(position);
                setRecyclerView(v);
                Toast.makeText(getActivity().getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                setTextTV();
            }
        });
//        setTextInTV();
    }
}