package com.example.belipangan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.belipangan.R;
import com.example.belipangan.fragment.BuyerAccountFragment;
import com.example.belipangan.fragment.BuyerHomeFragment;
import com.example.belipangan.fragment.BuyerOrderFragment;
import com.example.belipangan.fragment.HomeFragment;
import com.example.belipangan.fragment.PenjualanFragment;
import com.example.belipangan.fragment.ProductFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivityBuyer extends AppCompatActivity {
    FirebaseAuth mAuth;
    private BottomNavigationView botNavigation;
    private Fragment selectedFragment;

    private SharedPreferences mPreferences;
    private String sharedPreFile = "com.example.android.hellosharedprefs";
    private String fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_buyer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        botNavigation = findViewById(R.id.botNavMenuBuyer);
        botNavigation.setOnNavigationItemSelectedListener(navListener);

        mAuth = FirebaseAuth.getInstance();

        fragment = "fragment";

        mPreferences = getSharedPreferences(sharedPreFile, MODE_PRIVATE);
        fragment = mPreferences.getString("fragment", fragment);

        Log.d("fragment", fragment);
        if(fragment.equalsIgnoreCase("home")){
            selectedFragment = new BuyerHomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerBuyer, selectedFragment).commit();

            BottomNavigationView item = findViewById(R.id.botNavMenuBuyer);
            Menu menu = item.getMenu();
            MenuItem item1 = menu.getItem(0);
            item1.setChecked(true);

        }else if(fragment.equalsIgnoreCase("akun") ){
            selectedFragment = new BuyerAccountFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerBuyer, selectedFragment).commit();

            BottomNavigationView item = findViewById(R.id.botNavMenuBuyer);
            Menu menu = item.getMenu();
            MenuItem item1 = menu.getItem(2);
            item1.setChecked(true);

        }else if(fragment.equalsIgnoreCase("order")){
            selectedFragment = new BuyerOrderFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerBuyer, selectedFragment).commit();

            BottomNavigationView item = findViewById(R.id.botNavMenuBuyer);
            Menu menu = item.getMenu();
            MenuItem item1 = menu.getItem(1);
            item1.setChecked(true);

        }else{
            selectedFragment = new BuyerHomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerBuyer, selectedFragment).commit();
        }

//        selectedFragment = new BuyerHomeFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerBuyer, selectedFragment).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.menuHomeBuyer:
                    selectedFragment = new BuyerHomeFragment();
                    fragment = "home";
                    break;
                case R.id.menuAkunBuyer:
                    selectedFragment = new BuyerAccountFragment();
                    fragment = "akun";
                    break;
                case R.id.menuOrderBuyer:
                    selectedFragment = new BuyerOrderFragment();
                    fragment = "order";
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerBuyer, selectedFragment).commit();
            return true;
        }
    };

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.setGroupVisible(R.id.product_menu_group, false);
        menu.setGroupVisible(R.id.edit_menu_product, false);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_utama, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout:
                mAuth.signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.menuAdd:
                Toast.makeText(this, "add ditekan", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("fragment", fragment);
        preferencesEditor.apply();
    }
}