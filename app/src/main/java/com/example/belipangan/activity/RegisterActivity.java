package com.example.belipangan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.belipangan.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    }

    public void registerSeller(View view) {
        Intent intent = new Intent(this, RegisterSellerActivity.class);
        startActivity(intent);
    }

    public void registerBuyer(View view) {
        Intent intent = new Intent(this, RegisterBuyerActivity.class);
        startActivity(intent);
    }
}