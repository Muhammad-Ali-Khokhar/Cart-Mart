package com.example.cartmart.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.cartmart.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Nevigation_Bar extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment homeFrag, profileFrag, cartFrag, deliveryFrag;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nevigation_bar);
        homeFrag = new HomeFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, homeFrag);
        fragmentTransaction.commit();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){

                switch (item.getItemId()){
                    case R.id.home:
                        homeFrag = new HomeFragment();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout, homeFrag);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.profile:
                        profileFrag = new ProfileFragment();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout, profileFrag);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.cart:
                        cartFrag = new CartFragment();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout, cartFrag);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.delivery:
                        deliveryFrag = new DeliveryStatusFragment();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frameLayout, deliveryFrag);
                        fragmentTransaction.commit();
                        return true;
                }

                return false;
            }

        });

    }
}