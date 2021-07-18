package com.example.belipangan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.belipangan.R;
import com.example.belipangan.fragment.HomeFragment;
import com.example.belipangan.fragment.PenjualanFragment;
import com.example.belipangan.fragment.ProductFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.LinkedList;
import java.util.List;

public class MainActivitySeller extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private BottomNavigationView botNavigation;
    private Fragment selectedFragment;

    private SharedPreferences mPreferences;
    private String sharedPreFile = "com.example.android.hellosharedprefs";

    private String fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_seller);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        botNavigation = findViewById(R.id.botNavMenu);
        botNavigation.setOnNavigationItemSelectedListener(navListener);

        fragment = "fragment";

        mPreferences = getSharedPreferences(sharedPreFile, MODE_PRIVATE);
        fragment = mPreferences.getString("fragment", fragment);

        if(fragment.equalsIgnoreCase("product")){
            selectedFragment = new ProductFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();

            BottomNavigationView item = findViewById(R.id.botNavMenu);
            Menu menu = item.getMenu();
            MenuItem item1 = menu.getItem(0);
            item1.setChecked(true);

        }else if(fragment.equalsIgnoreCase("akun") ){
            selectedFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();

            BottomNavigationView item = findViewById(R.id.botNavMenu);
            Menu menu = item.getMenu();
            MenuItem item1 = menu.getItem(2);
            item1.setChecked(true);

        }else if(fragment.equalsIgnoreCase("penjualan")){
            selectedFragment = new PenjualanFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();

            BottomNavigationView item = findViewById(R.id.botNavMenu);
            Menu menu = item.getMenu();
            MenuItem item1 = menu.getItem(1);
            item1.setChecked(true);

        }else{
            selectedFragment = new ProductFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.menuProduct:
                    selectedFragment = new ProductFragment();
                    fragment = "product";
                    break;
                case R.id.menuAkun:
                    selectedFragment = new HomeFragment();
                    fragment = "akun";
                    break;
                case R.id.menuSelling:
                    selectedFragment = new PenjualanFragment();
                    fragment = "penjualan";
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();

            return true;
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("fragment", fragment);
        preferencesEditor.apply();
    }
}