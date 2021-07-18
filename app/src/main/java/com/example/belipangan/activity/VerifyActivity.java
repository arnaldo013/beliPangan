package com.example.belipangan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.belipangan.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyActivity extends AppCompatActivity {
    FirebaseUser fUser;
    TextView resend;

    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        resend = findViewById(R.id.tvResend);
        login = findViewById(R.id.login);

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(VerifyActivity.this, "link verifikasi telah dikirim ulang ke email anda", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

//        if(fUser.isEmailVerified()){
//            Intent intent = new Intent(VerifyActivity.this, MainActivityBuyer.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void login(View view) {
        Intent intent = new Intent(VerifyActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK);
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        fAuth.signOut();
        startActivity(intent);

    }
}