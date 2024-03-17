package com.example.cartmart.Activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cartmart.Adapters.Adapter_DS;
import com.example.cartmart.Models.Item_c;
import com.example.cartmart.Models.Item_ds;
import com.example.cartmart.Models.Item_h;
import com.example.cartmart.Models.User;
import com.example.cartmart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeliveryStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeliveryStatusFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Item_ds item_ds;
    ProgressBar progressBar;
    int sizeArray;
    ArrayList<Item_c> itemList_c;
    ArrayList<Item_h> itemList_h;


    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    View v;
    RecyclerView recyclerView;
    TextView txt_Price_ds, txt_DCharge_ds, txt_Total_ds, txt_ST_ds;
    String email, userId;

    public DeliveryStatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeliveryStatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeliveryStatusFragment newInstance(String param1, String param2) {
        DeliveryStatusFragment fragment = new DeliveryStatusFragment();
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
        return inflater.inflate(R.layout.fragment_delivery_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v = view;
        findViewsDS();

        progressBar.setVisibility(View.VISIBLE);
        try {
            getUserIdFromRFB();
        }catch (Exception e){
            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }

//        dataInitializeList();
//        setTextInTV();

    }

    private void getDetailsFromFB() {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference("Orders").child(userId).child("Details");
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    DataSnapshot dataSnapshot = task.getResult();
                    item_ds = dataSnapshot.getValue(Item_ds.class);
                    progressBar.setVisibility(View.GONE);
                    getItem_cFromFB();
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



    private void getItem_cFromFB() {
        progressBar.setVisibility(View.VISIBLE);
        itemList_c = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Orders").child(userId).child("Items");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList_c = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Item_c item_c = dataSnapshot.getValue(Item_c.class);
                    itemList_c.add(item_c);
                }
                getItem_hFromFB();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getItem_hFromFB() {
        progressBar.setVisibility(View.VISIBLE);
        itemList_h = new ArrayList<>();
        sizeArray = 0;
        for (Item_c item_c: itemList_c){
            databaseReference = FirebaseDatabase.getInstance().getReference("Category").child(item_c.getStr_category()).child(item_c.getStr_item_id());
            databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()){
                        DataSnapshot dataSnapshot = task.getResult();
                        Item_h item_h = dataSnapshot.getValue(Item_h.class);
                        itemList_h.add(item_h);
                        sizeArray++;
                    }
                    if (sizeArray == itemList_c.size()){
                        progressBar.setVisibility(View.GONE);
                        setRecyclerView(v);
                        setTextInTV();
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
                        getDetailsFromFB();
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

    private void findViewsDS() {
        txt_Price_ds = v.findViewById(R.id.txt_Price_ds);
        txt_DCharge_ds = v.findViewById(R.id.txt_DCharge_ds);
        txt_Total_ds = v.findViewById(R.id.txt_Total_ds);
        txt_ST_ds = v.findViewById(R.id.txt_ST_ds);
        progressBar = v.findViewById(R.id.progressBar);
    }

    private void setTextInTV() {

        txt_DCharge_ds.setText("20 Rs");
        int price = 0;
        int total;
        for (int i=0; i<itemList_h.size(); i++){
            price += Integer.parseInt(itemList_h.get(i).getStr_price_item_h()) * Integer.parseInt(itemList_c.get(i).getStr_count());
        }
        total = price + 20;
        txt_Price_ds.setText(Integer.toString(price));
        txt_Total_ds.setText(Integer.toString(total));
        if (item_ds != null){
            txt_ST_ds.setText(item_ds.getStr_status_ds());
        }
        else {
            txt_ST_ds.setText("Status");
        }


//        int price = 0;
//        int dcharge = 20;
//        int total = 0;
//
//        for (Item_h i: itemListDS){
//            price += Integer.parseInt(i.getStr_price_ds()) * Integer.parseInt(i.getStr_noOfItems_ds());
//        }
//        total = price + dcharge;
//
//        txt_Total_ds.setText(Integer.toString(total) + " Rs");
//        txt_Price_ds.setText(Integer.toString(price) + " Rs");
//        txt_DCharge_ds.setText(Integer.toString(dcharge) + " Rs");
//        txt_ST_ds.setText("Ready");
    }


    private void setRecyclerView(View view) {

        recyclerView = view.findViewById(R.id.rv_item_ds);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        Adapter_DS adapter_DS = new Adapter_DS(getContext(), itemList_h, itemList_c);
        recyclerView.setAdapter(adapter_DS);
        adapter_DS.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);

    }

//    private void dataInitializeList() {
//
//        itemListDS = new ArrayList<>();
//
//        Item_ds item_ds = new Item_ds(R.drawable.book3, "Book3", "150", "2");
//        itemListDS.add(item_ds);
//        item_ds = new Item_ds(R.drawable.watch2, "Watch2", "1500", "1");
//        itemListDS.add(item_ds);
//        item_ds = new Item_ds(R.drawable.food4, "Food4", "50", "4");
//        itemListDS.add(item_ds);
//        item_ds = new Item_ds(R.drawable.watch5, "Watch5", "1000", "1");
//        itemListDS.add(item_ds);
//        item_ds = new Item_ds(R.drawable.car3, "Car3", "150000", "1");
//        itemListDS.add(item_ds);
//        itemListDS.add(item_ds);
//
//    }
}