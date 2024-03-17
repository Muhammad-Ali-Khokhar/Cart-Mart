package com.example.cartmart.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import android.Manifest;

import com.example.cartmart.Models.Item_c;
import com.example.cartmart.Models.Item_ds;
import com.example.cartmart.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InfoForDelivery extends AppCompatActivity {
    ImageView img_back, img_location;
    Button btn_COrder_ifd;
    boolean sss ;
    EditText et_name_ifd, et_phone_ifd, et_location_ifd;
    String st_name_ifd, st_phone_ifd, st_location_ifd, st_adress, userID, status;
    FusedLocationProviderClient fusedLocationProviderClient;
    ArrayList<Item_c> itemList;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    boolean check;
    int sizeArray;
    private final static int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_for_delivery);
        sss = true;
        Intent intent = getIntent();
        check = false;
        status = "In Process";
        userID = intent.getStringExtra("UserID");
        findViewsIFD();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_COrder_ifd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getTextET()){
                    progressBar.setVisibility(View.VISIBLE);
                    checkIfOrderExistsOnFB();

                }
            }
        });
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        img_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                getCurrentLocation();
                if (et_location_ifd.getText().toString().equals("")){
                    Toast.makeText(InfoForDelivery.this, "Please check your internet connection and turn on your location.", Toast.LENGTH_SHORT).show();

                    progressBar.setVisibility(View.GONE);
                }
//                Toast.makeText(InfoForDelivery.this, "Location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getItemsFromFB() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Item_c item_c = dataSnapshot.getValue(Item_c.class);
                    itemList.add(item_c);
                }
                if (check){
                    addOrderOnFB();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(InfoForDelivery.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void addOrderOnFB() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Orders").child(userID).child("Details");
        Item_ds item_ds = new Item_ds(st_name_ifd, st_phone_ifd, st_location_ifd, status);
        databaseReference.setValue(item_ds).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (check){

                }
                addOrderOnFB2();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(InfoForDelivery.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void addOrderOnFB2() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Orders").child(userID).child("Items");
        sizeArray = 0;
        for (Item_c item_c : itemList){
            databaseReference.child(item_c.getStr_item_id()).setValue(item_c).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(InfoForDelivery.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    sizeArray++;
                    if (sizeArray == itemList.size()){
                        if (check){

                        }
                        deleteItemsFromCart();

                    }
                }
            });
        }
    }
    private void checkIfOrderExistsOnFB(){
        check = true;
        databaseReference = FirebaseDatabase.getInstance().getReference("Orders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String s = dataSnapshot.getKey();
                    if (userID.equals(s)){
                        check = false;
                        progressBar.setVisibility(View.GONE);
                        break;
                    }
                    else if (!userID.equals(s)){
                        check = true;
                        getItemsFromFB();
                        sss = false;
                    }
                }
                if (check == false && sss == true){
                    check = true;
                    Toast.makeText(InfoForDelivery.this, "Order already placed. Wait until its finished.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(InfoForDelivery.this, Nevigation_Bar.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    getItemsFromFB();
                    sss = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(InfoForDelivery.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void deleteItemsFromCart() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child(userID);
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(InfoForDelivery.this, Nevigation_Bar.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(InfoForDelivery.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getCurrentLocation() {
        progressBar.setVisibility(View.VISIBLE);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null){
                                Geocoder geocoder = new Geocoder(InfoForDelivery.this, Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    st_adress = addresses.get(0).getAddressLine(0);
                                    et_location_ifd.setText(st_adress);
                                    progressBar.setVisibility(View.GONE);
//                                    Toast.makeText(InfoForDelivery.this, st_adress, Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    progressBar.setVisibility(View.GONE);
                                    throw new RuntimeException(e);
                                }

                            }
                        }
                    });

        }else {
            askPermission();
            progressBar.setVisibility(View.GONE);
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(InfoForDelivery.this, new String[]
                {android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
                progressBar.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "Required Permission.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    private boolean getTextET() {
        st_name_ifd = et_name_ifd.getText().toString().trim();
        st_phone_ifd = et_phone_ifd.getText().toString().trim();
        st_location_ifd = et_location_ifd.getText().toString().trim();

        if (st_name_ifd.isEmpty() || st_phone_ifd.isEmpty() || st_location_ifd.isEmpty()){
            Toast.makeText(this, "Please enter complete info.", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }

        return false;
    }

    private void findViewsIFD() {
        img_back = findViewById(R.id.img_back);
        btn_COrder_ifd = findViewById(R.id.btn_COrder_ifd);
        et_name_ifd = findViewById(R.id.et_name_ifd);
        et_phone_ifd = findViewById(R.id.et_phone_ifd);
        et_location_ifd = findViewById(R.id.et_location_ifd);
        img_location = findViewById(R.id.img_location);
        progressBar = findViewById(R.id.progressBar);
    }
}