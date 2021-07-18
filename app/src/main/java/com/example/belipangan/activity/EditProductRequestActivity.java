package com.example.belipangan.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.belipangan.R;
import com.example.belipangan.adapter.AddRequestAdapter;
import com.example.belipangan.adapter.EditRequestAdapter;
import com.example.belipangan.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class EditProductRequestActivity extends AppCompatActivity {
    RecyclerView rv;
    Product product;
    DatabaseReference db;
    LinkedList<Product> list;
    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product_request);

        list = new LinkedList<>();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        rv = findViewById(R.id.rvAddRequest);

        db = FirebaseDatabase.getInstance().getReference("UnverifiedEditProducts");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        product = snapshot.getValue(Product.class);
                        product.setKey(snapshot.getKey());
                        Product prdct = product;
                        list.add(prdct);
                    }

                    EditRequestAdapter adapter = new EditRequestAdapter(EditProductRequestActivity.this, list);
                    rv.setHasFixedSize(true);
                    rv.setLayoutManager(new LinearLayoutManager(EditProductRequestActivity.this));
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}