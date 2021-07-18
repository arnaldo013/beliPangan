package com.example.belipangan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.belipangan.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class ChangePasswordActivity extends AppCompatActivity {
    TextInputEditText etEmail;
    Button btn;

    String email;
    boolean valid = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        btn = findViewById(R.id.btnGantiPass);
        etEmail = findViewById(R.id.etEmail);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = etEmail.getText().toString().trim();

                Pattern pattern = Patterns.EMAIL_ADDRESS;

                if(email.length() == 0){
                    etEmail.setError("Email tidak boleh kosong");
                }else if(!pattern.matcher(email).matches()){
                    etEmail.setError("Email tidak valid");
                }else {
                    valid = true;
                }

                if (valid){
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.sendPasswordResetEmail(email);
                    Toast.makeText(
                            ChangePasswordActivity.this,
                            "Link reset password sudah dikirim, cek email anda!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}