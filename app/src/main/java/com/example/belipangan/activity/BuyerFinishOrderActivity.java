package com.example.belipangan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.belipangan.R;
import com.example.belipangan.adapter.BuyerFinishOrderAdapter;
import com.example.belipangan.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class BuyerFinishOrderActivity extends AppCompatActivity {
    RecyclerView rv;
    Order order;
    DatabaseReference db;
    LinkedList<Order> list, filtered;
    LinkedList<String> listKey;
    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_finish_order);

        list = new LinkedList<>();
        filtered = new LinkedList<>();
        listKey = new LinkedList<>();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        rv = findViewById(R.id.rvPendingOrder);

        db = FirebaseDatabase.getInstance().getReference("FinishOrders");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listKey.clear();
                if(dataSnapshot != null){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        listKey.add(snapshot.getKey());
                    }

                    if(listKey.size() > 0){
                        getOrder();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getOrder() {
        for(int i=0; i<listKey.size(); i++){
            DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("FinishOrders")
                    .child(listKey.get(i));

            db2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot != null){
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            order = snapshot.getValue(Order.class);
                            list.add(order);
                        }

                        filtered.clear();
                        if(list.size() > 0){
                            for (int i=0; i<list.size(); i++){
                                if(list.get(i).getUidBuyer().equals(fUser.getUid())){
                                    filtered.add(list.get(i));
                                }
                            }

                            BuyerFinishOrderAdapter adapter = new BuyerFinishOrderAdapter(BuyerFinishOrderActivity.this, filtered);
                            rv.setHasFixedSize(true);
                            rv.setLayoutManager(new LinearLayoutManager(BuyerFinishOrderActivity.this));
                            rv.setAdapter(adapter);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
}