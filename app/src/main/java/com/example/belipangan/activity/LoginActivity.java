package com.example.belipangan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.belipangan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText etEmail, etPassword;
    TextView tvRegister, tvForgot;
    FirebaseDatabase database;
    FirebaseUser fUser, mUser;
    DatabaseReference dbReference;
    FirebaseAuth.AuthStateListener authStateListener;

    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();

        autentikasiListener();

        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgot = findViewById(R.id.tvForget);



    }

    private void autentikasiListener(){
        authStateListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if(mUser != null){
                    database = FirebaseDatabase.getInstance();
                    dbReference = database.getReference("Buyers").child(mUser.getUid());

                    dbReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try {
                                String role = dataSnapshot.child("role").getValue().toString();

                                if(!fUser.isEmailVerified()){
                                    Intent intent = new Intent(LoginActivity.this, VerifyActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }else{
                                    if(role.equals("buyer")){
                                        Intent intent = new Intent(LoginActivity.this, MainActivityBuyer.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }
                            }catch (Exception e){
                                sellerAuth();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        };
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }

    private void sellerAuth(){
        dbReference = database.getReference("Sellers").child(mUser.getUid());

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String role = dataSnapshot.child("role").getValue().toString();

                    if(!fUser.isEmailVerified()){
                        Intent intent = new Intent(LoginActivity.this, VerifyActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                        if(role.equals("seller")){
                            Intent intent = new Intent(LoginActivity.this, MainActivitySeller.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                }catch (Exception e){
                    adminAuth();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void adminAuth() {
        dbReference = database.getReference("Admins").child(mUser.getUid());

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String role = dataSnapshot.child("role").getValue().toString();

                    if(role.equals("admin")){
                        Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void registerUser(View view) {
        Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intentRegister);
    }

    public void login(View view) {
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if(email.length() == 0){
            etEmail.setError("Email harus di isi!");
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Email tidak valid");
        }
        else if(password.length() == 0){
            etPassword.setError("Password harus di isi!");
        }
        else{
            loginUser(email, password);
        }
    }

    private void loginUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    fUser = mAuth.getCurrentUser();
                    if(!fUser.isEmailVerified()){
                        if(fUser.getEmail().equals("admin@gmail.com")){
                            adminAuth();
                        }else{
                            Intent intent = new Intent(LoginActivity.this, VerifyActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }else{
                        autentikasiListener();

                    }
//                    checkRole(fUser);

                }else{
                    Toast.makeText(LoginActivity.this, "Email atau Password salah", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkRole(FirebaseUser user){
        dbReference = database.getReference("buyer").child(user.getUid());

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String role = dataSnapshot.child("role").getValue().toString();

                    if(role.equals("buyer")){
                        Log.d("checkRole", "role buyer");

                        Intent intent = new Intent(LoginActivity.this, MainActivityBuyer.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    if(role.equals("seller")){
                        Log.d("checkRole", "role seller");

                        Intent intent = new Intent(LoginActivity.this, MainActivitySeller.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                }catch (Exception e){
                    sellerAuth();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void forgotPassword(View view) {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }
}