package com.example.belipangan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.belipangan.R;
import com.example.belipangan.adapter.ProgressFinishAdapter;
import com.example.belipangan.adapter.ProgressOrderAdapter;
import com.example.belipangan.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class SellerFinishOrderActivity extends AppCompatActivity {
    RecyclerView rv;
    Order order;
    DatabaseReference db;
    LinkedList<Order> list;
    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_finish_order);

        list = new LinkedList<>();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        rv = findViewById(R.id.rvPendingOrder);

        db = FirebaseDatabase.getInstance().getReference("FinishOrders").child(fUser.getUid());

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        order = snapshot.getValue(Order.class);
                        order.setIdOrder(snapshot.getKey());
                        Order ordr = order;
                        list.add(ordr);
                    }

                    ProgressFinishAdapter adapter = new ProgressFinishAdapter(SellerFinishOrderActivity.this, list);
                    rv.setHasFixedSize(true);
                    rv.setLayoutManager(new LinearLayoutManager(SellerFinishOrderActivity.this));
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}